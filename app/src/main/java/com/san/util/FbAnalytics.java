package com.san.util;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class FbAnalytics {
    private static Activity mActivity = null;

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static void logEvent(String strEvent,String strJson) {
        Bundle params = new Bundle();
        try {
            JSONObject resultJson = new JSONObject(strJson);
            Iterator it = resultJson.keys();
            while (it.hasNext()) {
                String key = (String)it.next();
                String value = resultJson.getString(key);
                params.putString(key,value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
        logger.logEvent(strEvent, params);
    }

    public static void export_logEvent(final String strEvent,final String strJson) {
        System.out.println("export_logEvent export_show event_name="+strEvent+" event_data="+strJson);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FbAnalytics.logEvent(strEvent,strJson);
            }
        });
    }
}
