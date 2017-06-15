package org.akshara.Util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Common utils methods will available here
 *
 * @author vinayagasundar
 */

public final class Utils {


    /**
     * Check given package is Installed or Not
     *
     * @param context     Context to initialize the package managaer
     * @param packageName Package Name which need to be check
     * @return {@code true} if the packageName is installed on the device
     * otherwise {@code false}
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
