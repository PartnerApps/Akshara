package org.akshara.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.akshara.Util.ReadCsvFile;
import org.akshara.Util.Util;
import org.akshara.callback.DelegateAction;
import org.akshara.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Dhruv on 2/19/2016.
 */
public class CSVService extends IntentService {
    public static DelegateAction delegateAction;
    private static boolean D = Util.DEBUG;
    private static String TAG = CSVService.class.getSimpleName();
    private DatabaseHelper mDatabaseHandler = null;

    public CSVService() {
        super("CSVService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean result = readCsv();
        if (result) {
            delegateAction.onActionCompleted(result);
        } else {
            delegateAction.onActionFailed("Failed");
        }

    }

    private boolean readCsv() {
        mDatabaseHandler = new DatabaseHelper(CSVService.this);

        boolean result1 = false;
        boolean result2 = false;
        boolean result3 = false;
        try {
            ArrayList rowList1, rowList2, rowList3;

          /*  String fileName2 = "gka_schools_list.csv";
            rowList2=ReadCsvFile.readCSVSchool(fileName2);
            if(rowList2.size()!=0){
                if(mDatabaseHandler.addSchoolInfo(rowList2)){
                    result2=true;
                    if(D)
                        Log.d(TAG, "addSchoolInfo result2:" + result2);
                }
            }

            String fileName1 = "klp_gka_students_list.csv";
            rowList1= ReadCsvFile.readCSVStudent(fileName1);
            if(rowList1.size()!=0){
                if(mDatabaseHandler.addStudentInfo(rowList1)){
                    result1=true;
                    if(D)
                        Log.d(TAG, "addStudentInfo result1:" + result1);
                }
            }
*/


            String fileName3 = "klp_esl_student_list_class5_2016.csv";
            rowList3 = ReadCsvFile.readCSVStudentDifferentFormat(fileName3);
            if (rowList3.size() != 0) {
                if (mDatabaseHandler.addStudentInfo(rowList3)) {
                    result3 = true;
                    if (D)
                        Log.d(TAG, "addStudentInfo result3 :" + result3);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



      /*  if (result1 && result2 && result3){
            result1= true;
        }else
            result1= false;*/

        return result3;

    }

   /* private boolean readCsv(){
        mDatabaseHandler=new DatabaseHelper(CSVService.this);

        boolean result1=false;
        try {
            ArrayList rowList1,rowList2;
            String fileName1 = "klp_gka_students_list.csv";
            rowList1= ReadCsvFile.readCSVStudent(fileName1);
            if(rowList1.size()!=0){
                if(mDatabaseHandler.addStudentInfo(rowList1)){
                    result1=true;
                    if(D)
                        Log.d(TAG, "addStudentInfo :" + result1);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }





        return  result1;

    }
*/
}
