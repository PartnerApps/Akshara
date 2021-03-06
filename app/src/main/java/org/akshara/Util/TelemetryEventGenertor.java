package org.akshara.Util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;


public class TelemetryEventGenertor {
    public static final String OE_START = "OE_START";
    public static final String OE_END = "OE_END";
    public static final String EVENT_DATA = "edata";
    public static final String GAME_DATA = "gdata";
    public static final String EVENT_ID = "eid";
    public static final String TIME_STAMP = "ts";
    public static final String VERSION = "ver";
    public static final String SESSION_UUID = "sid";
    public static final String ID = "id";
    public static final String EKS = "eks";
    public static final String EXT = "ext";
    public static final String TELEMETRY_VERSION = "2.0";
    public static final String USER_UUID = "uid";
    public static final String DEVICE_UUID = "did";
    public static final String LENGTH = "length";
    private static String mUid = "0";

    public static JSONObject generateOEStartEvent(Context context) {
        JSONObject mainJSONObject = new JSONObject();
        try {

            JSONObject gameDataJsonObject = null;
            mainJSONObject.put(EVENT_ID, OE_START);
            mainJSONObject.put(TIME_STAMP, "");

            mainJSONObject.put(VERSION, TELEMETRY_VERSION);

            gameDataJsonObject = new JSONObject();
            gameDataJsonObject.put(ID, Util.getPackageName(context));
            gameDataJsonObject.put(VERSION, Util.getVersion(context));

            mainJSONObject.put(GAME_DATA, gameDataJsonObject);
            mainJSONObject.put(SESSION_UUID, mUid);
            mainJSONObject.put(USER_UUID, mUid);
            mainJSONObject.put(DEVICE_UUID, "");

            JSONObject dspecJsonObject = new JSONObject();
            JSONObject eDataJsonObject = new JSONObject();
            eDataJsonObject.put(EKS, dspecJsonObject);

           /* JSONObject extObject=new JSONObject();
            eDataJsonObject.put(EXT, extObject);
           */
            mainJSONObject.put(EVENT_DATA, eDataJsonObject);

            Log.e("OE_START_EVENT", mainJSONObject.toString());


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mainJSONObject;

    }

    public static JSONObject generateOEEndEvent(Context context, long startInterActionTime, long endInterActionTime, String UID) {
        JSONObject mainJSONObject = new JSONObject();
        try {


            JSONObject gameDataJsonObject = null;
            mainJSONObject.put(EVENT_ID, OE_END);
            mainJSONObject.put(TIME_STAMP, "");
            mainJSONObject.put(VERSION, TELEMETRY_VERSION);

            gameDataJsonObject = new JSONObject();
            gameDataJsonObject.put(ID, Util.getPackageName(context));
            gameDataJsonObject.put(VERSION, Util.getVersion(context));

            mainJSONObject.put(GAME_DATA, gameDataJsonObject);
            mainJSONObject.put(SESSION_UUID, UID);
            mainJSONObject.put(USER_UUID, UID);
            mainJSONObject.put(DEVICE_UUID, "");

            JSONObject eksChildJsonObject = new JSONObject();
            long timedifferenceSession = Long.valueOf(endInterActionTime) - Long.valueOf(startInterActionTime);
            long timeinSecondsSession = timedifferenceSession / 1000;
            eksChildJsonObject.put(LENGTH, timeinSecondsSession);


            JSONObject eksJsonObject = new JSONObject();
            eksJsonObject.put(EKS, eksChildJsonObject);
            /*JSONObject extObject=new JSONObject();
            eksJsonObject.put(EXT, extObject);
            */
            mainJSONObject.put(EVENT_DATA, eksJsonObject);

            Log.e("OE_END_EVENT", mainJSONObject.toString());


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mainJSONObject;

    }
}
