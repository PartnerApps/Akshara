package org.akshara.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.akshara.BuildConfig;
import org.akshara.model.StudentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A DAO class for creating Student Information
 * @author vinayagasundar
 */

@SuppressWarnings("WeakerAccess")
public final class StudentDAO {


    private static final String TAG = "StudentDAO";
    private static final boolean DEBUG = BuildConfig.DEBUG;


    /**
     * Name of the Table
     */
    private static final String TABLE_NAME = "student_info";


    // Table Column Names

    public static final String COLUMN_DISTRICT = "district";
    public static final String COLUMN_BLOCK = "block";
    public static final String COLUMN_CLUSTER = "clust";
    public static final String COLUMN_SCHOOL_CODE = "school_code";
    public static final String COLUMN_SCHOOL_NAME = "school_name";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_CHILD_NAME = "child_name";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_FATHER_NAME = "father_name";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_SYNC = "sync";


    private static final String [] ALL_COLUMN_MAP = {
            COLUMN_DISTRICT,
            COLUMN_BLOCK,
            COLUMN_CLUSTER,
            COLUMN_SCHOOL_CODE,
            COLUMN_SCHOOL_NAME,
            COLUMN_STUDENT_ID,
            COLUMN_CHILD_NAME,
            COLUMN_SEX,
            COLUMN_CLASS,
            COLUMN_FATHER_NAME,
            COLUMN_UID
    };

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_STUDENT_ID + " TEXT PRIMARY KEY,"
            + COLUMN_DISTRICT + " TEXT,"
            + COLUMN_BLOCK + " TEXT,"
            + COLUMN_CLUSTER + " TEXT,"
            + COLUMN_SCHOOL_CODE + " TEXT,"
            + COLUMN_SCHOOL_NAME + " TEXT,"
            + COLUMN_CHILD_NAME + " TEXT,"
            + COLUMN_SEX + " TEXT,"
            + COLUMN_CLASS + " TEXT,"
            + COLUMN_FATHER_NAME + " TEXT,"
            + COLUMN_UID + " TEXT,"
            + COLUMN_SYNC + " INTEGER DEFAULT 0"
            + ");";



    public static final String [] DEFAULT_INSERT_COLUMN_MAP = {
            COLUMN_DISTRICT,
            COLUMN_BLOCK,
            COLUMN_CLUSTER,
            COLUMN_SCHOOL_CODE,
            COLUMN_SCHOOL_NAME,
            COLUMN_STUDENT_ID,
            COLUMN_CHILD_NAME,
            COLUMN_SEX,
            COLUMN_FATHER_NAME,
            COLUMN_CLASS,
    };



    private static StudentDAO mInstance;



    private StudentDAO() {
        // To avoid object creation
    }


    public static StudentDAO getInstance() {
        if (mInstance == null) {
            mInstance = new StudentDAO();
        }
        return mInstance;
    }


    static String getCreateTableQuery() {
        return CREATE_TABLE_QUERY;
    }


    /**
     * Insert a List of Student information into the Table
     * @param valuesList List of Content Value contain the student information
     */
    public void insertData(List<ContentValues> valuesList) {
        if (DEBUG) {
            Log.i(TAG, "insertData: Total -> " + valuesList.size());
        }

        SQLiteDatabase database = PartnerDB.getInstance().getDatabase();

        database.beginTransaction();

        for (ContentValues values : valuesList) {
            String email = values.getAsString(COLUMN_STUDENT_ID);

            if (isValueExists(database, email)) {
                final String where = COLUMN_STUDENT_ID + " = ?";
                final String[] whereArgs = {
                        email
                };
                database.update(TABLE_NAME, values, where, whereArgs);
            } else {
                database.insert(TABLE_NAME, null, values);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }




    /**
     * Get the all unique value for a field in a given Table
     * @param fieldName Field which need to be get from the Table
     * @param placeHolder A string which is used to display as Hint
     * @return {@code List<String>}
     */
    public ArrayList<String> getUniqueFieldData(String fieldName, String placeHolder) {
        ArrayList<String> fieldData = new ArrayList<>();

        if (TextUtils.isEmpty(fieldName)) {
            return fieldData;
        }


        if (!TextUtils.isEmpty(placeHolder)) {
            fieldData.add(placeHolder);
        }

        Cursor cursor = PartnerDB.getInstance().query(true, TABLE_NAME, new String[]{fieldName},
                null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                fieldData.add(cursor.getString(0));
            }

        }

        if (cursor != null) {
            cursor.close();
        }

        return fieldData;
    }


    /**
     * Get the all unique value for a field in a given Table
     * @param fieldName Field which need to be get from the Table
     * @param placeHolder A string which is used to display as Hint
     * @param optionalParams it'll contain both Condition Fields and Value so
     *                       it'll should have always Even count
     *                       first will be field names followed by values
     * @return {@code List<String>}
     */
    public ArrayList<String> getUniqueFieldData(String fieldName, String placeHolder,
                                                String... optionalParams) {

        ArrayList<String> fieldData = new ArrayList<>();

        if (TextUtils.isEmpty(fieldName)) {
            return fieldData;
        }


        if (!TextUtils.isEmpty(placeHolder)) {
            fieldData.add(placeHolder);
        }


        if (optionalParams != null && optionalParams.length % 2 == 0) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < optionalParams.length / 2; i++) {
                if (i != 0) {
                    builder.append(" AND ");
                }
                builder.append(" ");
                builder.append(optionalParams[i]);
                builder.append(" = ?");
            }

            String where = builder.toString();

            if (DEBUG) {
                Log.i(TAG, "getUniqueFieldData: Where " + where);
            }


            String [] whereArgs = new String[optionalParams.length / 2];

            int counter = 0;

            for (int i = optionalParams.length / 2; i < optionalParams.length; i++) {
                whereArgs[counter] = optionalParams[i];
                counter++;
            }

            if (DEBUG) {
                Log.i(TAG, "getUniqueFieldData: Where Args " + whereArgs);
            }

            Cursor cursor = PartnerDB.getInstance().query(true, TABLE_NAME, new String[]{fieldName},
                    where, whereArgs, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    fieldData.add(cursor.getString(0));
                }
            }

            if (cursor != null) {
                cursor.close();
            }

        } else {
            throw new IllegalArgumentException("optionalParams is null or not having even length" +
                    " args");
        }

        return fieldData;
    }


    /**
     * Return the School Names and codes in a Hash Map
     * @param district District value
     * @param block block Value
     * @param cluster cluster value
     * @return Map of schools & code match with given parameters
     */
    public HashMap<String, String> getAllSchool(String district, String block, String cluster) {
        HashMap<String, String> hashMap = new HashMap<>();

        final String selection = COLUMN_DISTRICT + " = ? AND " + COLUMN_BLOCK + " = ? AND "
                + COLUMN_CLUSTER + " = ? ";

        final String [] selectionArgs = {
                district,
                block,
                cluster
        };

        final String [] columns = {
                COLUMN_SCHOOL_CODE,
                COLUMN_SCHOOL_NAME
        };


        Cursor cursor = PartnerDB.getInstance().query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                hashMap.put(cursor.getString(0), cursor.getString(1));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return hashMap;
    }


    /**
     * Add student information into the local database
     * @param studentMap HashMap contain the student information
     */
    public void addNewStudent(HashMap<String, Object> studentMap) {
        ContentValues contentValues = new ContentValues();

        Set<String> keySet = studentMap.keySet();

        for (String key : keySet) {
            String value = studentMap.get(key).toString();
            contentValues.put(key, value);
        }

        PartnerDB.getInstance().insert(TABLE_NAME, null, contentValues);
    }


    /**
     * It'll return the List of Student Information which is not synced to Google Excel
     * @return List of Student Information in List of list object format
     */
    public List<List<Object>> getAllUnSyncedData() {
        List<List<Object>> allData = new ArrayList<>();

        final String selection = COLUMN_SYNC + " = ?";
        final String [] selectionArgs = {
                "1"
        };

        Cursor cursor = PartnerDB.getInstance().query(TABLE_NAME, DEFAULT_INSERT_COLUMN_MAP,
                selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                List<Object> studentInfo = new ArrayList<>();
                final int columnCount = cursor.getColumnCount();

                for (int index = 0; index < columnCount; index++) {
                    studentInfo.add(cursor.getString(index));
                }

                allData.add(studentInfo);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return allData;
    }


    public void updateSyncState(List<List<Object>> syncedData) {
        if (syncedData != null && syncedData.size() > 0) {
            for (List<Object> studentInfo : syncedData) {
                String studentId = studentInfo.get(6).toString();

                final String where = COLUMN_STUDENT_ID + " = ?";
                final String []whereArgs = {
                        studentId
                };

                ContentValues values = new ContentValues();
                values.put(COLUMN_SYNC, 0);

                PartnerDB.getInstance().update(TABLE_NAME, values, where, whereArgs);
            }
        }
    }



    public StudentInfo [] getAllStudentInfoObject(String schoolCode, String searchBy) {
        StudentInfo[] studentInfos = new StudentInfo[0];

        if (TextUtils.isEmpty(schoolCode) || TextUtils.isEmpty(searchBy)) {
            throw new IllegalArgumentException("Some parameters are null");
        }

        final String selection = COLUMN_SCHOOL_CODE + " = ? AND " + COLUMN_CHILD_NAME
                + " LIKE '%" + searchBy + "%'";

        final String []selectionArgs = {
                schoolCode
        };

        Cursor cursor = PartnerDB.getInstance().query(TABLE_NAME, null, selection, selectionArgs,
                null, null, null);


        if (cursor != null && cursor.getCount() > 0) {
            studentInfos = new StudentInfo[cursor.getCount()];
            int counter = 0;

            while (cursor.moveToNext()) {
                studentInfos[counter] = new StudentInfo();

                studentInfos[counter].setDistrict(
                        cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICT)));
                studentInfos[counter].setBlock(
                        cursor.getString(cursor.getColumnIndex(COLUMN_BLOCK)));
                studentInfos[counter].setClust(
                        cursor.getString(cursor.getColumnIndex(COLUMN_CLUSTER)));
                studentInfos[counter].setSchool_code(
                        cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL_CODE)));
                studentInfos[counter].setSchool_name(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL_NAME)));
                studentInfos[counter].set_class(
                        cursor.getString(cursor.getColumnIndex(COLUMN_CLASS)));
                studentInfos[counter].setStudent_id(
                        cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)));
                studentInfos[counter].setChild_name(
                        cursor.getString(cursor.getColumnIndex(COLUMN_CHILD_NAME)));
                studentInfos[counter].setSex(
                        cursor.getString(cursor.getColumnIndex(COLUMN_SEX)));
                studentInfos[counter].setFather_name(
                        cursor.getString(cursor.getColumnIndex(COLUMN_FATHER_NAME)));
                studentInfos[counter].setUid(
                        cursor.getString(cursor.getColumnIndex(COLUMN_UID)));

                counter++;
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return studentInfos;
    }


//
//
//    /**
//     * Validate email & password and return if they exits it'll return User Info Hash map
//     * @param email Email id used to be validate
//     * @param password password of given Email
//     * @return if email & password is exits it'll return HashMap of Student otherwise null
//     */
//    public HashMap<String, String> validateUserEmailAndPassword(String email, String password) {
//        final String where = COLUMN_STUDENT_ID + " = ? AND " + COLUMN_PASSWORD + " = ?";
//        final String[] whereArgs = {
//                email,
//                password
//        };
//
//        Cursor cursor = PartnerDB.getInstance().query(TABLE_NAME, null, where, whereArgs, null,
//                null, null);
//
//        HashMap<String, String> map = null;
//
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToNext();
//
//            int columnCount = cursor.getColumnCount();
//
//            if (columnCount == ALL_COLUMN_MAP.length) {
//                map = new HashMap<>();
//
//                for (int i = 0; i < columnCount; i++) {
//                    String key = ALL_COLUMN_MAP[i];
//                    String value = cursor.getString(cursor.getColumnIndex(key));
//
//                    map.put(key, value);
//                }
//            }
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        return map;
//    }
//
//
    /**
     * Update the UID in the Student Information
     * @param studentId Email id
     * @param uid UID map with Student Information
     */
    public void updateStudentUID(String studentId, String uid) {
        final String where = COLUMN_STUDENT_ID + " = ?";
        final String [] whereArgs = {
                studentId
        };

        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, uid);

        PartnerDB.getInstance().update(TABLE_NAME, values, where, whereArgs);
    }
//
//
    /**
     * Check given studentId id is exits in table or not
     * @param database Database used to retrieve the data
     * @param studentId studentId which checked in the table
     * @return true if the <code>studentId</code> exists otherwise false
     */
    private boolean isValueExists(SQLiteDatabase database, String studentId) {
        final String selection = COLUMN_STUDENT_ID + " = ?";
        final String [] selectionArgs = {
            studentId
        };

        boolean isExists = false;

        Cursor cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            isExists = true;
        }

        if (cursor != null) {
            cursor.close();
        }

        return isExists;
    }


}

