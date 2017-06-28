package org.akshara.Util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.akshara.R;
import org.akshara.db.PartnerDB;
import org.akshara.model.UserModel;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Jaya on 9/24/2015.
 */
public class Util extends Application {
    public static final boolean DEBUG = false;
    public static final String PACKAGENAME = "org.ekstep.genieservices";
    public static final String partnerId = "org.ekstep.partner.akshara";
    public static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvgDm/lRk4ZU4ZUAaLRqX\n" +
            "hzxGbRzSFOjOsIEgGAMYkh3+pULK/9evvOhI5X2afbnLLTo6h9MzjzWKio/G5jTH\n" +
            "8YRS61ohBnhL8TKkVwXlU9GYnvOZimIoizPXimhNrVcAYvo4GNwrB9sxGFyNPup0\n" +
            "CBCnyWifdhKOWGo5LGhNCP9ehmJJchPw23RN+VeF/fsW9WVJNTZFXy4WYbsM7YVG\n" +
            "cQWYgCZX4eNqBcckP3aXaFTej1pPHfti2n+BLmudGK60lnZ4ePBidEi6WoPzpMrd\n" +
            "MnwzkYOnQ8KBV0LKJr0vzqATzxGMC85fo1OUm+ZMobdl8SCLAzn5+2WFnNKyct1m\n" +
            "twIDAQAB";
    public static final int dropDownItemSize = 50;
    public static final String student_fileName = "klp_gka_student_list.csv";
    public static final String school_fileName = "klp_gka_school_list.csv";
    public static final String aksharacsv_fileName = "aksharacsv.ser";
    public static final String UID = "uid";
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static String USERMODEL_DATA = "userModel";
    public static String TEMPLATE_NAME = "Akshara";
    public static String avatar = "AKSHARA";
    static String FILE_PATH;
    private static String TAG = Util.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private HashMap<String, String> hashMap;
    private Context mContext;
    private String PARTNER_REG = "partnerreg";
    private String spinnerDistric_selected, spinnerBlock_selected, spinnerCluster_selected, spinnerSchool_selected, spinnerSchoolCode_selected;
    private String language;
//    private Partner partner;
//    private UserProfile userProfile;
//    private Telemetry telemetry;
    private long startTime;

    public Util() {
    }

    public Util(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences("metalman", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        PartnerDB.initDataBase(this);

        PrefUtil.init(this);

        GenieSDK.init(this, "org.ekstep.genie");
    }

    /*
       *@return String FILE_PATH
       */
    public static String getFilePath(String fileName) {

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            FILE_PATH = sdCard.getAbsolutePath() + "/MyPic/" + fileName;
            if (DEBUG)
                Log.d(TAG, "FILE_PATH :" + FILE_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return FILE_PATH;
    }

    public static void showToastmessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /* To hide keyboard */
    public static void hideKeyboard(Activity activity, Context context) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);


        }
    }

    public static String getPackageName(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = pInfo.packageName;
        return version;

    }

    public static String getVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = pInfo.versionName + "." + pInfo.versionCode;
        return version;

    }

    public static void processSuccess(Context context, Object object) {
        GenieResponse response = (GenieResponse) object;
        if (DEBUG)
            Log.i("Successfull", "Status " + response.getStatus());
        if (response.getResult() != null) {
            if (DEBUG)
                Log.i("Success Gson Response", response.getResult().toString());
        }

    }

    public static void processSendFailure(Context context, Object object) {
        GenieResponse response = (GenieResponse) object;
        String error = response.getError();
        if (DEBUG)
            Log.e("Genie Service Error Log", error);
        for (int i = 0; i < response.getErrorMessages().size(); i++) {
            String errorPos = response.getErrorMessages().get(i).toString();
            if (DEBUG)
                Log.e("Error info", errorPos);
        }
        if (response.getResult() != null) {
            if (DEBUG)
                Log.e("Failure Gson Response", response.getResult().toString());
        }

        if (error.equalsIgnoreCase(context.getResources().getString(R.string.invalid_event))) {
            Toast.makeText(context, context.getResources().getString(R.string.invalid_event_message), Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getResources().getString(R.string.db_error))) {
            Toast.makeText(context, context.getResources().getString(R.string.db_error_message), Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getResources().getString(R.string.validation_error))) {
            Toast.makeText(context, context.getResources().getString(R.string.validation_error_message), Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getResources().getString(R.string.db_error))) {
            // DialogUtils.showAppNotInstalledDialog(((Activity) context));
        }

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

//    public Telemetry getTelemetry() {
//        return telemetry;
//    }
//
//    public void setTelemetry(Telemetry telemetry) {
//        this.telemetry = telemetry;
//    }

    public String getSpinnerSchoolCode_selected() {
        return spinnerSchoolCode_selected;
    }

    public void setSpinnerSchoolCode_selected(String spinnerSchoolCode_selected) {
        this.spinnerSchoolCode_selected = spinnerSchoolCode_selected;
    }

    public String getSpinnerSchool_selected() {
        return spinnerSchool_selected;
    }

    public void setSpinnerSchool_selected(String spinnerSchool_selected) {
        this.spinnerSchool_selected = spinnerSchool_selected;
    }

//    public Partner getPartner() {
//        return partner;
//    }
//
//    public void setPartner(Partner partner) {
//        this.partner = partner;
//    }
//
//    public UserProfile getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(UserProfile userProfile) {
//        this.userProfile = userProfile;
//    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSpinnerDistric_selected() {
        return spinnerDistric_selected;
    }

    public void setSpinnerDistric_selected(String spinnerDistric_selected) {
        this.spinnerDistric_selected = spinnerDistric_selected;
    }

    public String getSpinnerBlock_selected() {
        return spinnerBlock_selected;
    }

    public void setSpinnerBlock_selected(String spinnerBlock_selected) {
        this.spinnerBlock_selected = spinnerBlock_selected;
    }

    public String getSpinnerCluster_selected() {
        return spinnerCluster_selected;
    }

    public void setSpinnerCluster_selected(String spinnerCluster_selected) {
        this.spinnerCluster_selected = spinnerCluster_selected;
    }

    public void storeUserModel(HashMap<String, UserModel> hashMapTemplate_data) {
        // Convert the object to a JSON string
        String json = new Gson().toJson(hashMapTemplate_data);
        if (DEBUG)
            Log.d(TAG, "storeUserModel json==>" + json);
        //Strore USERMODEL_DATA
        editor.putString(USERMODEL_DATA, json);
        editor.commit();
    }

    /*
    *
    * return object
    * as HashMap */
    public HashMap<String, UserModel> getUserModel() {
        HashMap<String, UserModel> hashMapTemplate_data = null;
        // Now convert the JSON string back to your java object
        Type type = new TypeToken<HashMap<String, UserModel>>() {
        }.getType();
        String json = sharedPreferences.getString(USERMODEL_DATA, "NOTHING");
        if (DEBUG)
            Log.d(TAG, "getUserModel json==>" + json);
        if (!json.equals("NOTHING"))
            hashMapTemplate_data = new Gson().fromJson(json, type);

        // mStudentInfoArrayList=gson.fromJson(studentInfoObject,new ArrayList<StudentInfo>(Arrays.asList(mStudentInfoArrayList)));
        if (DEBUG)
            Log.d(TAG, "hashMapTemplate_data:" + hashMapTemplate_data);

        return hashMapTemplate_data;
    }

    public void storePartnerRegistration(String partnerid) {
        editor.putString(PARTNER_REG, partnerid);
        editor.commit();
    }

    public void clearSharedPreferences() {
        //clear sharedPreferences
        editor.clear();
        editor.commit();

    }

    public boolean isRegisterPartner() {
        if (!(sharedPreferences.getString(PARTNER_REG, "NOTHING").equals("NOTHING")))
            return true;
        else
            return false;

    }

}
