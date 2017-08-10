package org.akshara.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.akshara.BuildConfig;
import org.akshara.Util.PrefUtil;
import org.akshara.activity.DriveSyncActivity;
import org.akshara.activity.Splashscreeen;
import org.akshara.db.StudentDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Service to download the Partner Data file from Google Drive and update the Local database
 * @author vinayagasundar
 */

public class FetchPartnerDataService extends IntentService {

    private static final String TAG = "FetchPartnerDataService";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static final String PARTNER_DATA_FILE_ID = "1F0bEFeuJCRGqs_jm1MiOGg6jkE8ofCNOsbM8HPf56ps";

    private static final String SHEETS_DATA_RANGE = "student_info!A2:L";

    private static final String SHEETS_ADD_DATA_RANGE = "student_info!A:J";

    private static final String ACTION_SYNC_STATE = "org.ekstep.partner.akshara.SYNC_STATE";

    public static final String EXTRA_IS_USER_RECOVERABLE_ERROR
            = "org.ekstep.partner.akshara.IS_RECOVERABLE_ERROR";

    public static final String EXTRA_RECOVERABLE_INTENT
            = "org.ekstep.partner.akshara.RECOVERABLE_INTENT";

    public static final String EXTRA_ERROR_MESSAGE
            = "org.ekstep.partner.akshara.ERROR_MESSAGE";

    public static final String EXTRA_DISTRICT = "extra_district";
    public static final String EXTRA_FILE_ID = "extra_file_id";


    public static final int ERROR_RECOVERABLE = 1;
    public static final int ERROR_UN_RECOVERABLE = 2;



    static final String DOWNLOAD_FILE_PATH = "/Akshara-ESL/tmp/file.csv";


    private String mDistrictName;
    private String mFileId;


    public FetchPartnerDataService() {
        super(TAG);
    }


    /**
     * Get the Intent for Broadcast receiver for Sync State
     * @return {@link Intent} for Broadcast
     */
    public static IntentFilter getActionIntent() {
        return new IntentFilter(ACTION_SYNC_STATE);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (DEBUG) {
            Log.i(TAG, "onHandleIntent: ");
        }

        if (intent != null) {
            mDistrictName = intent.getStringExtra(EXTRA_DISTRICT);
            mFileId = intent.getStringExtra(EXTRA_FILE_ID);
        }

        downloadDataAndSync();
    }

    private void downloadDataAndSync() {
        if (DEBUG) {
            Log.i(TAG, "downloadDataAndSync: ");
        }

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                getApplication(), Arrays.asList(DriveSyncActivity.SCOPES))
                .setBackOff(new ExponentialBackOff());

        if (credential.getSelectedAccountName() == null) {
            String accountName = PrefUtil.getString(DriveSyncActivity.PREF_ACCOUNT_NAME);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
            }
        }

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        Sheets mSheetsService = new Sheets.Builder(transport, jsonFactory, credential)
                .setApplicationName(BuildConfig.APPLICATION_ID)
                .build();


//        We're now disable Writing the data back to the Excel
//        List<List<Object>> dataListNotSync = StudentDAO.getInstance()
//                .getAllUnSyncedData();
//
//
//        if (dataListNotSync.size() > 0) {
//            if (DEBUG) {
//                Log.i(TAG, String.format("downloadDataAndSync: We've to add %d in Excel",
//                        dataListNotSync.size()));
//            }
//
//            ValueRange content = new ValueRange();
//            content.setValues(dataListNotSync);
//
//            try {
//                Sheets.Spreadsheets.Values.Append request = mSheetsService.spreadsheets().values()
//                        .append(PARTNER_DATA_FILE_ID, SHEETS_ADD_DATA_RANGE, content);
//
//                request.setValueInputOption("RAW");
//                request.setInsertDataOption("INSERT_ROWS");
//
//                AppendValuesResponse response = request.execute();
//
//                if (response.getUpdates().getUpdatedRows() == dataListNotSync.size()) {
//                    if (DEBUG) {
//                        Log.i(TAG, "downloadDataAndSync: Successfully Updated data in Excel");
//                    }
//
//                    StudentDAO.getInstance().updateSyncState(dataListNotSync);
//                }
//
//                if (DEBUG) {
//                    Log.i(TAG, "downloadDataAndSync: " + response);
//                }
//
//
//            } catch (IOException ioException) {
//                if (DEBUG) {
//                    Log.e(TAG, "onHandleIntent: ", ioException);
//                }
//            }
//        }


        try {

            if (DEBUG) {
                Log.i(TAG, "onHandleIntent: ");
            }

            String fileName = String.format(Locale.ENGLISH, "%s!%s", "data", "A2:L");

            if (TextUtils.isEmpty(mFileId)) {
                mFileId = PARTNER_DATA_FILE_ID;
            }

            ValueRange valueRange = mSheetsService.spreadsheets().values()
                    .get(mFileId, fileName)
                    .execute();

            List<List<Object>> values = valueRange.getValues();

            if (values != null) {
                if (DEBUG) {
                    Log.i(TAG, "downloadDataAndSync: Column Data Size : " + values.size());
                }

                List<ContentValues> contentValues = new ArrayList<>();

                int counter = 0;

                for (List row : values) {
                    if (DEBUG) {
                        Log.i(TAG, "downloadDataAndSync: Row Data Size : " + row.size());
                    }

                    if (row.size() == StudentDAO.DEFAULT_INSERT_COLUMN_MAP.length) {
                        if (DEBUG) {
                            Log.i(TAG, "downloadDataAndSync: Inserting Data...!");
                        }

                        ContentValues insertValues = new ContentValues();

                        for (int index = 0; index < row.size(); index++) {
                            insertValues.put(StudentDAO.DEFAULT_INSERT_COLUMN_MAP[index],
                                    row.get(index).toString().trim());
                        }

                        contentValues.add(insertValues);

                        counter++;

                        if (counter % 1000 == 0) {
                            StudentDAO.getInstance().insertData(contentValues);
                            contentValues.clear();
                            counter = 0;
                        }
                    }
                }

                StudentDAO.getInstance().insertData(contentValues);

                PrefUtil.storeLongValue(Splashscreeen.LAST_SYNC_TIME, System.currentTimeMillis());

                LocalBroadcastManager.getInstance(this).sendBroadcast(
                        new Intent(ACTION_SYNC_STATE));

            }

        } catch (IOException ioException) {
            if (DEBUG) {
                Log.e(TAG, "onHandleIntent: ", ioException);
            }

            if (ioException instanceof UserRecoverableAuthIOException) {
                UserRecoverableAuthIOException ex = (UserRecoverableAuthIOException) ioException;

                Intent intent = new Intent(ACTION_SYNC_STATE);

                intent.putExtra(EXTRA_IS_USER_RECOVERABLE_ERROR, ERROR_RECOVERABLE);
                intent.putExtra(EXTRA_RECOVERABLE_INTENT, ex.getIntent());

                LocalBroadcastManager.getInstance(this).sendBroadcast(
                        intent);
            } else if (ioException instanceof GoogleJsonResponseException) {
                GoogleJsonResponseException ex = (GoogleJsonResponseException) ioException;

                Intent intent = new Intent(ACTION_SYNC_STATE);

                intent.putExtra(EXTRA_IS_USER_RECOVERABLE_ERROR, ERROR_UN_RECOVERABLE);
                intent.putExtra(EXTRA_ERROR_MESSAGE, ex.getDetails().getMessage());


                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                Intent intent = new Intent(ACTION_SYNC_STATE);

                intent.putExtra(EXTRA_IS_USER_RECOVERABLE_ERROR, ERROR_UN_RECOVERABLE);
                intent.putExtra(EXTRA_ERROR_MESSAGE, ioException.getLocalizedMessage());

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }

        if (DEBUG) {
            Log.i(TAG, "onHandleIntent: Download And Sync Completed");
        }
    }
}
