package org.akshara.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.opencsv.CSVWriter;

import org.akshara.BuildConfig;
import org.akshara.Util.PrefUtil;
import org.akshara.activity.DriveSyncActivity;
import org.akshara.db.StudentDAO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A Service will write data to File using Google Drive API
 * @author vinayagasundar
 */

public class WritePartnerDataInFile extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "WritePartnerDataInFile";

    private static final boolean DEBUG = BuildConfig.DEBUG;


    private static final String FOLDER_NAME = "Akshara-ESL";

    private static final String PREF_FILE_ID = "file_id";
    private static final String PREF_FOLDER_ID = "folder_id";
    private static final String PREF_FILE_NAME = "fileName";

    private GoogleApiClient mGoogleApiClient;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    private String mFileId;

    private String mFolderId;


    private boolean mIsAddHeader = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .setAccountName(PrefUtil.getString(DriveSyncActivity.PREF_ACCOUNT_NAME))
                    .build();
        }

        mGoogleApiClient.connect();


        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (DEBUG) {
            Log.i(TAG, "onConnected: ");
        }


        String fileName = PrefUtil.getString(PREF_FILE_NAME);

        if (!TextUtils.isEmpty(fileName)) {
            String date = mDateFormat.format(new Date());

            if (fileName.equals(date)) {
                mFileId = PrefUtil.getString(PREF_FILE_ID);
                writeDataIntoFile();
            } else {
                mFolderId = PrefUtil.getString(PREF_FOLDER_ID);
                if (TextUtils.isEmpty(mFolderId)) {
                    createFolderAndFile();
                } else {
                    createFile();
                }
            }
        } else {
            mFolderId = PrefUtil.getString(PREF_FOLDER_ID);
            if (TextUtils.isEmpty(mFolderId)) {
                createFolderAndFile();
            } else {
                createFile();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int cause) {
        if (DEBUG) {
            Log.i(TAG, "onConnectionSuspended: ");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (DEBUG) {
            Log.i(TAG, "onConnectionFailed: " + result.toString());
        }


//        if (!result.hasResolution()) {
//            // show the localized error dialog.
//            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
//            return;
//        }
//        try {
//            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
//        } catch (IntentSender.SendIntentException e) {
//            Log.e(TAG, "Exception while starting resolution activity", e);
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void createFolderAndFile() {
        if (DEBUG) {
            Log.i(TAG, "createFolderAndFile: ");
        }

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(FOLDER_NAME).build();

        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .createFolder(mGoogleApiClient, changeSet)
                .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                    @Override
                    public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                        if (driveFolderResult.getStatus().isSuccess()) {
                            mFolderId = driveFolderResult.getDriveFolder().getDriveId()
                                    .encodeToString();
                            if (DEBUG) {
                                Log.i(TAG, "onResult: Create Folder : "
                                        + mFolderId);
                            }

                            PrefUtil.storeString(PREF_FOLDER_ID, mFolderId);

                            createFile();
                        }
                    }
                });
    }



    private void createFile() {
        mIsAddHeader = true;

        final DriveId folderDriveId = DriveId.decodeFromString(mFolderId);

        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (DEBUG) {
                            Log.i(TAG, "onResult: " + driveContentsResult);
                        }

                        if (driveContentsResult.getStatus().isSuccess()) {

                            if (DEBUG) {
                                Log.i(TAG, "onResult: Folder Created");
                            }

                            DriveFolder folder = folderDriveId.asDriveFolder();

                            final String fileName = mDateFormat.format(new Date());
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(fileName)
                                    .setMimeType("text/csv")
                                    .build();

                            folder.createFile(mGoogleApiClient, changeSet, null)
                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                        @Override
                                        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                            if (DEBUG) {
                                                Log.i(TAG, "onResult: " + driveFileResult);
                                            }

                                            if (driveFileResult.getStatus().isSuccess()) {
                                                if (DEBUG) {
                                                    Log.i(TAG, "onResult: File Created");
                                                }

                                                mFileId = driveFileResult.getDriveFile()
                                                        .getDriveId().encodeToString();

                                                PrefUtil.storeString(PREF_FILE_ID, mFileId);
                                                PrefUtil.storeString(PREF_FILE_NAME, fileName);

                                                writeDataIntoFile();
                                            }
                                        }
                                    });
                        }
                    }
                });



    }



    private void writeDataIntoFile() {
        if (DEBUG) {
            Log.i(TAG, "writeDataIntoFile: ");
        }
        
        
        final DriveId fileId = DriveId.decodeFromString(mFileId);

        
        Runnable writeRunnable = new Runnable() {
            @Override
            public void run() {
                DriveFile driveFile = fileId.asDriveFile();

                DriveApi.DriveContentsResult result = driveFile
                        .open(mGoogleApiClient, DriveFile.MODE_READ_WRITE, null).await();

                if (!result.getStatus().isSuccess()) {
                    stopSelf();
                    return;
                }

                DriveContents contents = result.getDriveContents();
                ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();

                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor
                        .getFileDescriptor());

                try {
                    //noinspection ResultOfMethodCallIgnored
                    inputStream.read(new byte[inputStream.available()]);

                    FileOutputStream outputStream = new FileOutputStream(
                            parcelFileDescriptor.getFileDescriptor());

                    Writer writer = new OutputStreamWriter(outputStream);

                    CSVWriter csvWriter = new CSVWriter(writer);
                    
                    if (mIsAddHeader) {
                        String [] header = StudentDAO.DEFAULT_INSERT_COLUMN_MAP;
                        csvWriter.writeNext(header);
                    }


                    List<List<Object>> data = StudentDAO.getInstance().getAllUnSyncedData();
                    
                    for (List<Object> insertData : data) {
                        String [] convertedData = new String[insertData.size()];
                        
                        for (int i = 0; i < insertData.size(); i++) {
                            convertedData[i] = String.valueOf(insertData.get(i));
                        }
                        
                        csvWriter.writeNext(convertedData);
                    }
                    
                    csvWriter.flush();
                    csvWriter.close();
                    
                    StudentDAO.getInstance().updateSyncState(data);


                    Status status = contents.commit(mGoogleApiClient, null).await();

                    if (status.isSuccess()) {
                        if (DEBUG) {
                            Log.i(TAG, "run: Data appended successfully");
                        }
                    }

                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        Thread writeThread = new Thread(writeRunnable);
        writeThread.start();
    }
}
