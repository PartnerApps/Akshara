package org.akshara.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.akshara.BuildConfig;

import java.util.Set;


/**
 * Preference Util for the App
 * @author vinayagasundar
 */

public final class PrefUtil {

    public static final int INVALID_DATA = -1;

    private static final String APP_PREFERENCES = BuildConfig.APPLICATION_ID + ".APP_PREFS";


    private static SharedPreferences mAppPrefs;

    private PrefUtil() {

    }


    /**
     * Initialize the Preference from the {@link Application#onCreate()} method
     * @param context context to initialize the Preference
     */
    public static void init(Context context) {
        if (mAppPrefs == null) {
            mAppPrefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
    }


    /**
     * Store the string value in the preferences
     * @param key Key for the preferences
     * @param value value to be stored in the preferences
     */
    public static void storeString(String key, String value) {
        mAppPrefs.edit()
                .putString(key, value)
                .apply();
    }

    /**
     * Get the value from the preference for given key
     * @param key Key for which we need to get value from the preferences
     * @return if the value exist it'll return the value otherwise null
     */
    public static String getString(String key) {
        return mAppPrefs.getString(key, null);
    }


    /**
     * Store the long value into the SharedPreferences
     * @param key Key used for storing the value
     * @param value value which need to be stored
     */
    public static void storeLongValue(@NonNull String key, long value) {
        mAppPrefs.edit()
                .putLong(key, value)
                .apply();
    }


    /**
     * Get the long value from the SharedPreferences
     * @param key Key for which value need to be fetched
     * @return if the value exits it'll be return otherwise {@link #INVALID_DATA}
     */
    public static long getLongValue(@NonNull String key) {
        return mAppPrefs.getLong(key, INVALID_DATA);
    }


    /**
     * Store the boolean value into the SharedPreferences
     * @param key Key used for storing the value
     * @param value Value which need to be stored
     */
    public static void storeBooleanValue(@NonNull String key, boolean value) {
        mAppPrefs.edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * Get the boolean value from the SharedPreferences
     * @param key Key for which value need to be fetched
     * @return if the value exits it'll be return otherwise false
     */
    public static boolean getBooleanValue(@NonNull String key) {
        return mAppPrefs.getBoolean(key, false);
    }

    /**
     * Store the int value into the SharedPreferences
     * @param key Key used for storing the value
     * @param value value which need to be stored
     */
    public static void storeIntValue(@NonNull String key, int value) {
        mAppPrefs.edit()
                .putInt(key, value)
                .apply();
    }


    /**
     * Get the int value from the SharedPreferences
     * @param key Key for which value need to be fetched
     * @return if the value exits it'll be return otherwise {@link #INVALID_DATA}
     */
    public static int getIntValue(@NonNull String key) {
        return mAppPrefs.getInt(key, INVALID_DATA);
    }


    /**
     * Store the String set value into the SharedPreferences
     * @param key  key Key used for storing the value
     * @param values value which need to be stored
     */
    public static void storeStringSet(@NonNull String key, @NonNull Set<String> values) {
        mAppPrefs.edit()
                .putStringSet(key, values)
                .apply();
    }

    /**
     * Get the String set value from the SharedPreference
     * @param key Key for which value need to be fetched
     * @return set of string it's exists otherwise null
     */
    @Nullable
    public static Set<String> getStringSet(@NonNull String key) {
        return mAppPrefs.getStringSet(key, null);
    }

}
