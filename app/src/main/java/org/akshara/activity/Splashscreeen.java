package org.akshara.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import org.akshara.BuildConfig;
import org.akshara.R;
import org.akshara.Util.PrefUtil;
import org.akshara.Util.TelemetryEventGenertor;
import org.akshara.Util.Util;
import org.akshara.Util.Utils;
import org.akshara.callback.IRegister;
import org.akshara.callback.IStartSession;
import org.akshara.callback.RegisterResponseHandler;
import org.akshara.callback.StartSessionResponseHandler;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieresolvers.partner.PartnerService;
import org.ekstep.genieresolvers.telemetry.TelemetryService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Splash screen will display Logo and Register the Partner Information
 * @author vinayagasundar
 */
public class Splashscreeen extends Activity implements IRegister, IStartSession {
    private static final String TAG = "SplashActivity";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final int REQUEST_APP_PERMISSION_CODE = 665;


    public static final String LAST_SYNC_TIME = "last_sync_time";


    private static final long ONE_DAY_IN_MILLISECONDS = 86400000;


    /**
     * Partner used to register the Partner Info to Genie Service
     */
    private PartnerService mPartnerService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


//        if (Utils.isAppInstalled(this, Util.PACKAGENAME)) {
//            registerPartnerApp();
//        } else {
//            Toast.makeText(this, "Genie must be installed for App work", Toast.LENGTH_SHORT)
//                    .show();
//            Intent openPlayStore = new Intent(Intent.ACTION_VIEW);
//            openPlayStore.setData(Uri.parse(
//                    "https://play.google.com/store/apps/details?id=org.ekstep.genieservices"));
//            startActivity(openPlayStore);
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.isAppInstalled(this, Util.PACKAGENAME)) {
            registerPartnerApp();
        } else {
            Toast.makeText(this, "Genie must be installed for App work", Toast.LENGTH_SHORT)
                    .show();
            Intent openPlayStore = new Intent(Intent.ACTION_VIEW);
            openPlayStore.setData(Uri.parse(
                    "https://play.google.com/store/apps/details?id=org.ekstep.genieservices"));
            startActivity(openPlayStore);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_APP_PERMISSION_CODE) {
            boolean hasAllPermission = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;
                    break;
                }
            }

            if (hasAllPermission) {
                startTelemetryEvent();
            }
        }


    }


    ///////////////////////////////////////////////////////////////////////////
    // Callback from RegisterResponseHandler
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse);

        if (DEBUG) {
            Log.i(TAG, "onSuccess: Registration Successful : " + result);
        }

        startPartnerSession();

    }


    @Override
    public void onFailure(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse);

        if (DEBUG) {
            Log.e(TAG, "onFailure: Partner App Registration Failed " + result);
        }

        Util.processSendFailure(this, genieResponse);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }


    @Override
    public void onSuccessSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessSession: " + new Gson().toJson(genieResponse));
        }

        checkForAppPermission();
    }

    @Override
    public void onFailureSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureSession: " + new Gson().toJson(genieResponse));
        }
    }

    /**
     * Register the Partner with Genie Service
     */
    private void registerPartnerApp() {
        if (DEBUG) {
            Log.i(TAG, "registerPartnerApp: ");
        }

        PartnerData partnerData = new PartnerData(null, null, Util.partnerId, null, Util.publicKey);

        mPartnerService = GenieSDK.getGenieSDK().getPartnerService();
        RegisterResponseHandler registerResponseHandler = new RegisterResponseHandler(this);

        mPartnerService.registerPartner(partnerData, registerResponseHandler);

    }

    private void startPartnerSession() {
        if (DEBUG) {
            Log.i(TAG, "startPartnerSession: ");
        }

        PartnerData partnerData = new PartnerData(null, null, Util.partnerId, null, Util.publicKey);
        StartSessionResponseHandler startSessionResponseHandler
                = new StartSessionResponseHandler(this);
        mPartnerService.startPartnerSession(partnerData, startSessionResponseHandler);

    }



    /**
     * Check app has all the permissions
     */
    private void checkForAppPermission() {
        boolean hasStoragePermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean hasGetAccountPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionList = new ArrayList<>();

        if (!hasStoragePermission) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!hasGetAccountPermission) {
            permissionList.add(Manifest.permission.GET_ACCOUNTS);
        }

        if (permissionList.size() == 0) {
            startTelemetryEvent();
            return;
        }

        ActivityCompat.requestPermissions(this, permissionList.toArray(new String[]{}),
                REQUEST_APP_PERMISSION_CODE);
    }


    /**
     * it'll log the OE_START event for the App
     */
    private void startTelemetryEvent() {
        if (DEBUG) {
            Log.d(TAG, "startTelemetryEvent: ");
        }


        String telemetryJSONData = TelemetryEventGenertor.generateOEStartEvent(this).toString();

        TelemetryService telemetryService = GenieSDK.getGenieSDK().getTelemetryService();
        telemetryService.saveTelemetryEvent(telemetryJSONData, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                moveToMainScreen();
            }

            @Override
            public void onError(GenieResponse genieResponse) {

            }
        });
    }

    /**
     * Move to LoginActivity and kill this Activity after 2 seconds
     */
    private void moveToMainScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long lastSyncTime = PrefUtil.getLongValue(LAST_SYNC_TIME);
                long syncDiff = System.currentTimeMillis() - lastSyncTime;

                if (DEBUG) {
                    Log.i(TAG, "run: Sync Difference : " + syncDiff);
                }

                Intent intent;

                if (lastSyncTime < 0 || syncDiff > ONE_DAY_IN_MILLISECONDS) {
                    if (DEBUG) {
                        Log.i(TAG, "run: Sync Activity");
                    }
                    intent = new Intent(Splashscreeen.this, DriveSyncActivity.class);
                } else {
                    if (DEBUG) {
                        Log.i(TAG, "run: Login Activity");
                    }
                    intent = new Intent(Splashscreeen.this, MainActivity.class);
                }


                startActivity(intent);

                finish();
            }
        }, 2000);
    }

}
