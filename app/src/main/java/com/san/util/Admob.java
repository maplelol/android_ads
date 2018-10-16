package com.san.util;


import android.app.Activity;

import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

public class Admob {
    //public static final String TEST_DEVICE = "CD7716FEBC774CC716E6BBE92D0D1AE9"; //IUNI
    //public static final String TEST_DEVICE = "1E1EBB8BB17872B03FA8CF8B30CCC7EA"; //华为mate-9
    public static final String TEST_DEVICE = "151171256F13A316E823FD98694CB0C1"; //NOKIA 7

    public static final String GOOGLE_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    public static void init(String strJson) {
        String strAppID = "";
        try {
            JSONObject pJson = new JSONObject(strJson);
            strAppID = pJson.getString("app_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MobileAds.initialize(AdsUtil.getActivity(), AdsUtil.IS_DEBUG ? GOOGLE_APP_ID : strAppID);
    }
}

