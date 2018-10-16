package com.san.util;

import com.ironsource.mediationsdk.IronSource;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
//import com.mopub.mobileads.ChartboostRewardedVideo;
import com.mopub.mobileads.MoPubRewardedVideos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoPubAds {
    public static void init(final String strJson) {
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String strRewardedVideoUnitID = "";
                List<String> networksToInit = new ArrayList<String>();
                try {
                    JSONObject pJson = new JSONObject(strJson);
                    JSONArray vRewardedVideoName = pJson.getJSONArray("networks");
                    if (vRewardedVideoName != null) {
                        for (int i=0; i<vRewardedVideoName.length(); ++i) {
                            networksToInit.add(vRewardedVideoName.get(i).toString());
                        }
                    }
                    strRewardedVideoUnitID = pJson.getString("reward_video_unit_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(AdsUtil.IS_DEBUG?"920b6145fb1546cf8b5cf2ac34638bb7":strRewardedVideoUnitID)
                        //.withMediationSettings("MEDIATION_SETTINGS")
                        .withNetworksToInit(networksToInit)
                        .build();

                MoPub.initializeSdk(AdsUtil.getActivity(),sdkConfiguration,initSdkListener());
            }
        });

    }

    private static SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                System.out.println("MoPubAds onInitializationFinished");
            }
        };
    }
}
