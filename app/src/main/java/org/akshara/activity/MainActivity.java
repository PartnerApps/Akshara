package org.akshara.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.callback.DelegateAction;
import org.akshara.callback.ILanguage;
import org.akshara.callback.ITelemetryData;
import org.akshara.callback.LanguageResponseHandler;
import org.akshara.callback.TelemetryResponseHandler;
import org.akshara.db.DatabaseHandler;
import org.akshara.db.DatabaseHelper;
import org.akshara.fragment.SearchChildFragment;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieresolvers.language.LanguageService;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DelegateAction, NavigationDrawer.FragmentDrawerListener,
        ILanguage, ITelemetryData {
    private boolean D = Util.DEBUG;
    private String TAG = MainActivity.class.getSimpleName();
    private DatabaseHelper mDatabaseHandler = null;
    private DatabaseHandler databaseHandler = null;
    private Fragment fragment;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView mToolbar_text;
    private NavigationDrawer mDrawerFragment;
    private ImageView mToolbar_menu;
    private ImageView mToolbar_back, mToolbar_cancel;
    private Fragment mFragment;
    private FragmentManager mFragment_mgr;
    private DrawerLayout mDrawerLayout;
    private MaterialDialog materialDialog;
    private int writeDB = 0;
//    private Partner partner;
//    private UserProfile userProfile;
//    private LanguageList languageList;
//    private Telemetry telemetry;
    private TelemetryResponseHandler telemetryResponseHandler;
    private LanguageResponseHandler languageResponseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D)
            Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_home);


//        /*COPY DB*/
//        if (!doesDatabaseExist(MainActivity.this, DatabaseHandler.DATABASE_NAME)) {
//            copyDB();
//        } else {
//            mDatabaseHandler = new DatabaseHelper(MainActivity.this);
//        }
        mFragment_mgr = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mFragment = getSupportFragmentManager().findFragmentByTag("mContent");
        }

        initWidget();


    }


    @Override
    protected void onRestart() {
        if (D)
            Log.d(TAG, "onRestart");
        super.onRestart();

    }

    private void initWidget() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        mToolbar_text = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolbar_menu = (ImageView) mToolbar.findViewById(R.id.imageView_menu);
        mToolbar_back = (ImageView) mToolbar.findViewById(R.id.imageView_back);
        mToolbar_cancel = (ImageView) mToolbar.findViewById(R.id.imageView_cancel);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContext = MainActivity.this;
        mDrawerFragment = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        mDrawerFragment.setDrawerListener(this);

        //get language list
        languageResponseHandler = new LanguageResponseHandler(this);
        LanguageService languageService = GenieSDK.getGenieSDK().getLanguageService();

        languageService.getAllLanguages(languageResponseHandler);

        displayView(2);
        mToolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        //initialitize the partner
//        partner = new Partner(MainActivity.this);
//        userProfile = new UserProfile(MainActivity.this);
//        telemetry = new Telemetry(MainActivity.this);
        //Store object
//        Util util = (Util) mContext.getApplicationContext();
//        util.setPartner(partner);
//        util.setUserProfile(userProfile);
//        util.setTelemetry(telemetry);
//        util.setStartTime(System.currentTimeMillis());
//        telemetryResponseHandler = new TelemetryResponseHandler(MainActivity.this);
//        telemetry.send(TelemetryEventGenertor.generateOEStartEvent(mContext).toString(), telemetryResponseHandler);


    }


    @Override
    public void onActionStart() {

    }

    @Override
    public void onActionCompleted(Boolean result) {

        if (D)
            Log.d(TAG, " -->writeDB onActionCompleted  :" + result);

        DatabaseHelper.exportDatabse(DatabaseHandler.DATABASE_NAME);

    }

    @Override
    public void onActionFailed(String failedResult) {
        if (D)
            Log.d(TAG, "onActionFailed :" + failedResult);

    }

    /*
    * akshara.com.akshara.db will create from excel file and
    * same akshara.com.akshara.db will copy in asset folder
    *
    * */

    /*
    * copy akshara.com.akshara.db from assets folder*/
    private void copyDB() {
        if (D)
            Log.d(TAG, "copyDB");
        databaseHandler = new DatabaseHandler(MainActivity.this);
        try {
            databaseHandler.openDataBase();
            mDatabaseHandler = new DatabaseHelper(MainActivity.this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        boolean exist = dbFile.exists();
        if (D)
            Log.d(TAG, "exist :" + exist);
        return exist;
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
       /* if(position==0){
            displayView(position);
        }else{*/
        showMaterialDialog("Are you sure to exit?", "", "YES", "CANCEL");
        materialDialog.show();
        // }


    }

    private void displayView(int position) {
        // Fragment fragment;

        switch (position) {
           /* case 0:

                fragment = new HomeFragment();
                switchContent(fragment, 1, true);
                break;
           */
            case 2:

                fragment = new SearchChildFragment();
                switchContent(fragment, 1, true);
                break;


            default:
                break;
        }

    }

    public void switchContent(Fragment fragment, int id, boolean addToBackStack) {
        this.mFragment = fragment;
        if (addToBackStack) {
            mFragment_mgr.beginTransaction().replace(R.id.container_body, fragment, "mContent").addToBackStack(null).commit();

        } else {
            mFragment_mgr.beginTransaction().replace(R.id.container_body, fragment).commit();
        }

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            mToolbar_menu.setVisibility(View.VISIBLE);
            mToolbar_back.setVisibility(View.GONE);
            mToolbar_cancel.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
        }
    }

    private void showMaterialDialog(String title, String content, final String positiveText, String negativeText) {
        materialDialog = new MaterialDialog
                .Builder(MainActivity.this)
                .title(title)
                .content(content)
                .negativeText(negativeText)
                .positiveText(positiveText)
                .buttonsGravity(GravityEnum.CENTER)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        //exit appln
                        exitApp();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                }).build();
        materialDialog.setCancelable(false);
    }

    public void showBackIcon(String title, Fragment fragment) {
        mToolbar_menu.setVisibility(View.GONE);
        mToolbar_back.setVisibility(View.VISIBLE);
        mToolbar_cancel.setVisibility(View.GONE);
        mToolbar_text.setText(title);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.mFragment = fragment;
    }

    public void showBackIconWithConnect(String title, Fragment fragment) {
        mToolbar_menu.setVisibility(View.GONE);
        mToolbar_back.setVisibility(View.VISIBLE);
        mToolbar_cancel.setVisibility(View.GONE);
        mToolbar_text.setText(title);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.mFragment = fragment;
    }


    public boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            //Toast.makeText(MainActivity.this, packageName, Toast.LENGTH_LONG).show();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            if (intent == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            intent.putExtra("audience","learner");
//            intent.putExtra("channel","AP_1");
            HashMap<String,String> programTag=new HashMap<>();
            programTag.put("name", Util.partnerId);
            programTag.put("desc","Default Partner tag for Akshara ESL");

            // TODO: 23/5/17 Later add optimization for Partner Dates
            programTag.put("startDate","2017-05-23");
            programTag.put("endDate","2117-05-26");

            intent.putExtra("program_tag", programTag);
//            intent.putExtra("language","hi");
            intent.putExtra("mode","soft");


            context.startActivity(intent);
            finish();
            return true;
        } catch (Exception e) {

            return false;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (D)
            Log.d(TAG, "onStop");
    }


    public void exitApp() {
        if (D)
            Log.d(TAG, "exitApp");
        MainActivity.this.finish();//org.ekstep.genieservices
        openApp(MainActivity.this, "org.ekstep.genieservices");

    }


    @Override
    public void onSuccessLanguage(GenieResponse genieListResponse) {
        String result = new Gson().toJson(genieListResponse.getResult());
        if (D)
            Log.d(TAG, " onSuccessLanguage :" + result);
        Util util = (Util) mContext.getApplicationContext();
        util.setLanguage(result);


    }

    @Override
    public void onFailureLanguage(GenieResponse genieListResponse) {
        String result = new Gson().toJson(genieListResponse.getResult());
        if (D)
            Log.d(TAG, " onFailureLanguage :" + result);
    }


    @Override
    public void onSuccessTelemetry(GenieResponse genieResponse) {
        if (D)
            Log.d(TAG, " onSuccessTelemetry :" + genieResponse);

    }

    @Override
    public void onFailureTelemetry(GenieResponse genieResponse) {
        if (D)
            Log.d(TAG, " onFailureTelemetry :" + genieResponse);

    }


}
