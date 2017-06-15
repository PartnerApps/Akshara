package org.akshara.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.akshara.Util.Util;
import org.akshara.model.UserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Name
    public static final String DATABASE_NAME = "Aksharadb";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static Context ctx;
    private final ArrayList<UserInfo> contact_list = new ArrayList<UserInfo>();
    private String TAG = DatabaseHandler.class.getSimpleName();
    private boolean D = Util.DEBUG;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        //12 fields
        String CREATE_STUDENTINFO_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.TABLE_STUDENT_INFO + "("
                + StudentInfoDb.DISTRICT + " TEXT,"
                + StudentInfoDb.BLOCK + " TEXT,"
                + StudentInfoDb.CLUST + " TEXT,"
                + StudentInfoDb.SCHOOL_CODE + " TEXT,"
                + StudentInfoDb.SCHOOL_NAME + " TEXT,"
                + StudentInfoDb._CLASS + " TEXT,"
                + StudentInfoDb.STUDENT_ID + " TEXT,"
                + StudentInfoDb.CHILD_NAME + " TEXT,"
                + StudentInfoDb.SEX + " TEXT,"
                + StudentInfoDb.FATHER_NAME + " TEXT"

				/*+ StudentInfoDb.mother_tongue + " TEXT,"
                + StudentInfoDb.dise_code + " TEXT,"*/

                + ")";
        db.execSQL(CREATE_STUDENTINFO_TABLE);

        //8 fields
        String CREATE_SCHOOLINFO_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.TABLE_SCHOOL_INFO + "("
                + SchoolInfoDb.DISTRICT + " TEXT,"
                + SchoolInfoDb.BLOCK + " TEXT,"
                + SchoolInfoDb.CLUST + " TEXT,"
                + SchoolInfoDb.KLP_ID + " TEXT,"
                //+ SchoolInfoDb.dise_code + " TEXT,"
                + SchoolInfoDb.SCHOOL_NAME + " TEXT"
                //+ SchoolInfoDb.moi + " TEXT,"
				/*+ SchoolInfoDb.management + " TEXT,"
				+ SchoolInfoDb.institution_gender + " TEXT,"
				*/
                //+ SchoolInfoDb.category + " TEXT"
				/*+ SchoolInfoDb.address + " TEXT,"
				+ SchoolInfoDb.area + " TEXT,"
				+ SchoolInfoDb.pincode + " TEXT,"
				+ SchoolInfoDb.identification + " TEXT,"
				+ SchoolInfoDb.route_information + " TEXT"*/
                + ")";
        db.execSQL(CREATE_SCHOOLINFO_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_SCHOOL_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_STUDENT_INFO);
        // Create tables again
        onCreate(db);
    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

        // Path to the just created empty org.akshara.db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty org.akshara.db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public SQLiteDatabase openDataBase() throws SQLException {

        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                if (D)
                    Log.d(TAG, "Copying sucess from Assets folder");
            } catch (IOException e) {
                if (D)
                    Log.e(TAG, "Error creating source database" + e);

                throw new RuntimeException("Error creating source database", e);

            }

        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

    }


}
