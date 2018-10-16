package com.san.util;

import android.app.Activity;

import com.facebook.ads.AdSettings;

public class FbAds {
    //public static final String TEST_DEVICE = "374636a0-5e68-4b48-a1c8-32c2573aff57"; //HUAWEI-MATE9
    //public static final String TEST_DEVICE = "2e10027e-ef82-4d1a-bd51-9ea6fdd3e281"; //IUNI
    public static final String TEST_DEVICE = "513f577c-b11e-4385-a151-a0c72305abee"; //NOKIA 7

    public static void init(String strJson) {
        if (AdsUtil.IS_DEBUG) {
            AdSettings.addTestDevice(TEST_DEVICE);
        }
    }
}
