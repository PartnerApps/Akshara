package org.akshara.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.model.StudentInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper {
    public static final String TABLE_STUDENT_INFO = "student_info";
    public static final String TABLE_SCHOOL_INFO = "school_info";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private final ArrayList<StudentInfo> StudentInfo_list = new ArrayList<StudentInfo>();
    private boolean D = Util.DEBUG;
    private String TAG = DatabaseHelper.class.getSimpleName();
    private SQLiteDatabase mWritableDb;
    private SQLiteDatabase mReadableDb;

    public DatabaseHelper(Context context) {
        DatabaseSingleton ds = DatabaseSingleton.getInstance(context);
        mWritableDb = ds.getWriteableDB();
        mReadableDb = ds.getWriteableDB();
    }

    // Creating Tables

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    /**
     *Adds UserInfos to database
     *@param
     */
    // Adding new UserInfoDb
    /*public boolean addUserInfo(ArrayList rowList) {
		boolean result=false;
		String insertQuery = "INSERT INTO "+ TABLE_UserInfo +" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
		SQLiteStatement statement = mWritableDb.compileStatement(insertQuery);
		mWritableDb.beginTransaction();
		if(D)
			Log.d(TAG, "  rowList size==>" + rowList.size());
		//iterating over each row
		for (int row = 0; row < rowList.size(); row++) {
			ArrayList cellList=(ArrayList)rowList.get(row); //1st row of 1st sheet

			statement.clearBindings();
			for (int i = 0; i<cellList.size(); i++) {
				try {
					statement.bindString(i + 1, "" + cellList.get(i));

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			statement.execute();

		}
*/
	/*	mWritableDb.setTransactionSuccessful();
		mWritableDb.endTransaction();
		result=true;
		return result;

	}*/

    public static boolean exportDatabse(String databaseName) {
        boolean result = false;
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "org.akshara" + "//databases//" + databaseName + "";
                Log.i("exportDatabse", "currentDBPath" + currentDBPath);

                String backupDBPath = databaseName;
                File currentDB = new File(data, currentDBPath);
                Log.i("exportDatabse", "currentDB" + currentDB);

                File backupDB = new File(sd, backupDBPath);
                Log.i("exportDatabse", "backupDB" + backupDB);


                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    Log.i("exportDatabse", "src" + src);
                    Log.i("exportDatabse", "src size" + src.size());
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    Log.i("exportDatabse", "dst" + dst);
                    src.close();
                    dst.close();
                    result = true;
                }
            }
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * Adds StudentInfo to database
     *
     * @param
     */
    // Adding new UserInfoDb
    public boolean addStudentInfo(ArrayList rowList) {
        boolean result = false;

        String insertQuery = "INSERT INTO " + TABLE_STUDENT_INFO + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mWritableDb.compileStatement(insertQuery);
        mWritableDb.beginTransaction();
        if (D)
            Log.d(TAG, " addStudentInfo  rowList size==>" + rowList.size());

        //iterating over each row
        for (int row = 0; row < rowList.size(); row++) {
            ArrayList cellList = (ArrayList) rowList.get(row); //1st row of 1st sheet
            if (row == 0) {
                if (D)
                    Log.d(TAG, " addStudentInfo  cellList size==>" + cellList.size());

            }
            ///Log.d(TAG,"rowList.get(row)=>"+rowList.get(row));
            statement.clearBindings();
            for (int i = 0; i < cellList.size(); i++) {
                if (D)
                    Log.d(TAG, i + " is i value, addStudentInfo  cellList size==>" + cellList.get(i));

                try {
                    statement.bindString((i + 1), "" + cellList.get(i));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            statement.execute();
        }

        mWritableDb.setTransactionSuccessful();
        mWritableDb.endTransaction();
        result = true;
        return result;

    }

    public void deleteRecord() {
        String _class = "5";
        mReadableDb.delete(TABLE_STUDENT_INFO, StudentInfoDb._CLASS + "='" + _class + "'", null);
    }

    /**
     * Adds SchoolInfo to database
     *
     * @param
     */
    // Adding new SchoolInfo
    public boolean addSchoolInfo(ArrayList rowList) {
        boolean result = false;
        String insertQuery = "INSERT INTO " + TABLE_SCHOOL_INFO + " VALUES (?,?,?,?,?);";
        SQLiteStatement statement = mWritableDb.compileStatement(insertQuery);
        mWritableDb.beginTransaction();
        if (D)
            Log.d(TAG, " addSchoolInfo  rowList size==>" + rowList.size());
        //iterating over each row
        for (int row = 0; row < rowList.size(); row++) {
            ArrayList cellList = (ArrayList) rowList.get(row); //1st row of 1st sheet
            if (row == 0)
                Log.d(TAG, "addSchoolInfo  cellList size==>" + cellList.size());

            statement.clearBindings();
            for (int i = 0; i < cellList.size(); i++) {
                if (D)
                    Log.d(TAG, i + " is i value, addSchoolInfo  cellList size==>" + cellList.get(i));

                try {
                    statement.bindString((i + 1), "" + cellList.get(i));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            statement.execute();
        }

        mWritableDb.setTransactionSuccessful();
        mWritableDb.endTransaction();
        result = true;
        return result;

    }


    /**
     *Getting single  UserInfoDb from database
     *@param id
     *@param
     *
     *@return String
     */

    /**
     * Getting all UserInfos  from database
     *
     * @return ArrayList<UserInfoDb>
     */
    public ArrayList<String> getAllDistrict_Block_Cluster(Context context, String type, String district, String block, String cluster) {
        String selectQuery = "";
        if (D)
            Log.d(TAG, " getAllDistrict :");
        ArrayList<String> districList = new ArrayList<>();
        try {

            if (type.equals("D")) {
                selectQuery = "select distinct " + SchoolInfoDb.DISTRICT + " from " + TABLE_SCHOOL_INFO;
                districList.add(context.getString(R.string.district_default));
            } else if (type.equals("B")) { //select distinct BlockName from UserInfo where DistrictName='BELLARY';
                selectQuery = "select distinct " + SchoolInfoDb.BLOCK + " from " + TABLE_SCHOOL_INFO + " WHERE " + SchoolInfoDb.DISTRICT + "='" + district + "'";
                districList.add(context.getString(R.string.block_default));
            } else if (type.equals("C")) {
                selectQuery = "select distinct " + SchoolInfoDb.CLUST + "   from " + TABLE_SCHOOL_INFO + " WHERE " + SchoolInfoDb.DISTRICT + "='" + district + "' AND " + SchoolInfoDb.BLOCK + "='" + block + "'";
                districList.add(context.getString(R.string.cluster_default));
            }

            Cursor cursor = mWritableDb.rawQuery(selectQuery, null);
            if (D)
                Log.d(TAG, "" + cursor.getCount());
            if (cursor.moveToFirst()) {

                do {
                    districList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (D)
                Log.d(TAG, "getAllDistrict districList size:" + districList.size());

            return districList;
        } catch (Exception e) {
            if (D)
                Log.e(TAG, " getAllDistrict Exception :" + e);
        }

        return districList;
    }

    public Map<String, String> getAllSchool(String district, String block, String cluster) {
        String selectQuery = "";
        if (D)
            Log.d(TAG, " getAllSchool :");
        Map<String, String> hashMap = new HashMap<>();
        try {

            selectQuery = "select distinct " + SchoolInfoDb.KLP_ID + "," + SchoolInfoDb.SCHOOL_NAME + " from " + TABLE_SCHOOL_INFO + " WHERE " + SchoolInfoDb.DISTRICT + "='" + district + "' AND " + SchoolInfoDb.BLOCK + "='" + block + "" + "' AND " + SchoolInfoDb.CLUST + "='" + cluster + "'";
            //hashMap.put("key", "PLEASE SELECT SCHOOL");
            Cursor cursor = mWritableDb.rawQuery(selectQuery, null);
            if (D)
                Log.d(TAG, "" + cursor.getCount());
            if (cursor.moveToFirst()) {

                do {
                    hashMap.put(cursor.getString(0), cursor.getString(1));

                } while (cursor.moveToNext());
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (D)
                Log.d(TAG, "getAllSchool hashMap :" + hashMap);

            return hashMap;
        } catch (Exception e) {
            if (D)
                Log.e(TAG, " getAllSchool Exception :" + e);
        }

        return hashMap;
    }

    /*
    * get motherToungue*/
    public String getmotherToungue(String district, String block, String cluster, String school, String schoolCode) {
        String selectQuery = "", motherToungue = "";
        if (D)
            Log.d(TAG, " getmotherToungue DISTRICT:" + district + ":" + block + ":" + cluster + ":" + school + ":" + schoolCode);

        try {

			/*selectQuery = "select "+SchoolInfoDb.moi+"  from " + TABLE_SCHOOL_INFO+" WHERE "+SchoolInfoDb.DISTRICT+ "='"+DISTRICT+
					"' AND "+SchoolInfoDb.BLOCK+ "='"+BLOCK+""+"' AND "
					+SchoolInfoDb.CLUST+ "='"+cluster+"' AND "+SchoolInfoDb.SCHOOL_NAME+ "='"+school
					+"' AND "+SchoolInfoDb.KLP_ID+ "='"+schoolCode+"'";
			*/
            Cursor cursor = mWritableDb.rawQuery(selectQuery, null);
            if (D)
                Log.d(TAG, "" + cursor.getCount());
            cursor.moveToNext();
            motherToungue = cursor.getString(0);
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return motherToungue;
        } catch (Exception e) {
            if (D)
                Log.e(TAG, " getmotherToungue Exception :" + e);
        }

        return motherToungue;
    }


    /**
     * Getting all StudentInfo  from database
     *
     * @return ArrayList<StudentInfo>
     */

    public StudentInfo[] getAllStudentInfoObject(String district, String block, String culster, String schoolCode, String searchBy) {
        StudentInfo[] studentInfo = null;

        if (D)
            Log.d(TAG, " getAllStudentInfo searchBy:" + searchBy + " DISTRICT:" + district + " BLOCK" + block + " culster" + culster);
        try {
            StudentInfo_list.clear();
            String selectQuery = "SELECT  * FROM " + TABLE_STUDENT_INFO + " WHERE " + StudentInfoDb.DISTRICT + "='" + district
                    + "' AND " + StudentInfoDb.BLOCK + "='" + block + "' AND " + StudentInfoDb.CLUST + "='" + culster + "' AND "
                    + StudentInfoDb.SCHOOL_CODE + "='" + schoolCode + "' AND " + StudentInfoDb.CHILD_NAME + " LIKE " + "'%" + searchBy + "%'";

            //LTRIM(RTRIM(DISTRICT)) = LTRIM(RTRIM("bellary")) replaceAll("[-+.^:,]","")
			/*String selectQuery = "SELECT  * FROM " + TABLE_STUDENT_INFO+" WHERE "+SchoolInfoDb.DISTRICT+"'"+DISTRICT.toLowerCase()+"')) AND LTRIM(RTRIM("
					+SchoolInfoDb.BLOCK+ ")) = LTRIM(RTRIM('"+BLOCK.toLowerCase()+"')) AND LTRIM(RTRIM("
					+SchoolInfoDb.CLUST+ ")) = LTRIM(RTRIM('"+culster.toLowerCase().replaceAll("[,]","")+"')) AND "+StudentInfoDb.child_name+" LIKE " + "'%"+searchBy+"%'";
*/
            if (D)
                Log.d(TAG, selectQuery);

            Cursor cursor = mWritableDb.rawQuery(selectQuery, null);
            int count = cursor.getCount();
            studentInfo = new StudentInfo[count];
            int x = 0;

            if (D)
                Log.d(TAG, "getAllStudentInfo" + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    studentInfo[x] = new StudentInfo();
                    studentInfo[x].setDistrict(cursor.getString(cursor.getColumnIndex(StudentInfoDb.DISTRICT)));
                    studentInfo[x].setBlock(cursor.getString(cursor.getColumnIndex(StudentInfoDb.BLOCK)));
                    studentInfo[x].setClust(cursor.getString(cursor.getColumnIndex(StudentInfoDb.CLUST)));
                    studentInfo[x].setSchool_code(cursor.getString(cursor.getColumnIndex(StudentInfoDb.SCHOOL_CODE)));
                    studentInfo[x].setSchool_name(cursor.getString(cursor.getColumnIndex(StudentInfoDb.SCHOOL_NAME)));
                    studentInfo[x].set_class(cursor.getString(cursor.getColumnIndex(StudentInfoDb._CLASS)));
                    studentInfo[x].setStudent_id(cursor.getString(cursor.getColumnIndex(StudentInfoDb.STUDENT_ID)));
                    studentInfo[x].setChild_name(cursor.getString(cursor.getColumnIndex(StudentInfoDb.CHILD_NAME)));
                    studentInfo[x].setSex(cursor.getString(cursor.getColumnIndex(StudentInfoDb.SEX)));
//                    studentInfo[x].setDob(cursor.getString(9));
                    studentInfo[x].setFather_name(cursor.getString(cursor.getColumnIndex(StudentInfoDb.FATHER_NAME)));
//                    studentInfo[x].setMother_name(cursor.getString(11));
                    studentInfo[x].setUid(cursor.getString(cursor.getColumnIndex(StudentInfoDb.UID)));
                    x++;

                } while (cursor.moveToNext());
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return studentInfo;
        } catch (Exception e) {
            if (D)
                Log.e(TAG, " StudentInfo_list Exception :" + e);
        }

        return studentInfo;
    }



    public void updateStudentUID(String studentId, String uid) {
        if (TextUtils.isEmpty(studentId)) {
            return;
        }

        final String where = StudentInfoDb.STUDENT_ID + " = ?";
        final String [] whereArgs = {
                studentId
        };

        ContentValues values = new ContentValues();
        values.put(StudentInfoDb.UID, uid);

        mWritableDb.update(TABLE_STUDENT_INFO, values, where, whereArgs);
    }


}
