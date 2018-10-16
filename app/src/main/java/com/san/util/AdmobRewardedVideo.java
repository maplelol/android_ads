package com.san.util;

import android.app.AlertDialog;

import com.facebook.ads.Ad;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AdmobRewardedVideo implements RewardedVideoAdListener {
    private static final String GOOGLE_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    private RewardedVideoAd mAd;

    private String mStrUnitID;

    public AdmobRewardedVideo(final String strUnitID) {
        this.mStrUnitID = strUnitID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isLoaded();
    }

    public void load() {
        if (this.mAd == null) {
            this.mAd = MobileAds.getRewardedVideoAdInstance(AdsUtil.getActivity());
            this.mAd.setRewardedVideoAdListener(this);
        }

        AdRequest adRequest = null;
        if (AdsUtil.IS_DEBUG) {
            adRequest = new AdRequest.Builder().addTestDevice(Admob.TEST_DEVICE).build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        this.mAd.loadAd(AdsUtil.IS_DEBUG ? GOOGLE_UNIT_ID : this.mStrUnitID,adRequest);
    }

    public void show() {
        if (this.isLoaded()) {
            this.mAd.show();
        }
    }

    private void onStatusUpdate(String status) {
        JSONObject resultJson = new JSONObject();
        try {
            resultJson.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.import_onStatusUpdate(resultJson.toString());
    }

    public void onRewardedVideoAdLoaded() {
        System.out.println("AdmobRewardedVideo onRewardedVideoAdLoaded");

        this.onStatusUpdate("loaded");
    }

    public void onRewardedVideoAdOpened() {
        System.out.println("AdmobRewardedVideo onRewardedVideoAdOpened");

        this.onStatusUpdate("show");
    }

    public void onRewardedVideoStarted() {
        System.out.println("AdmobRewardedVideo onRewardedVideoStarted");
    }

    public void onRewardedVideoAdClosed() {
        System.out.println("AdmobRewardedVideo onRewardedVideoAdClosed");

        this.onStatusUpdate("closed");
    }

    public void onRewarded(RewardItem var1) {
        System.out.println("AdmobRewardedVideo onRewarded");

        this.onStatusUpdate("reward");
    }

    public void onRewardedVideoCompleted() {
        System.out.println("AdmobRewardedVideo onRewardedVideoCompleted");
    }

    public void onRewardedVideoAdLeftApplication() {
        System.out.println("AdmobRewardedVideo onRewardedVideoAdLeftApplication");
    }

    public void onRewardedVideoAdFailedToLoad(int var1) {
        System.out.println("AdmobRewardedVideo onRewardedVideoAdFailedToLoad");

//        new  AlertDialog.Builder(FbAds.getActivity())
//                .setTitle("测试" )
//                .setMessage("AdmobRewardedVideo onError errorid="+var1)
//                .setPositiveButton("确定" ,  null )
//                .show();

        this.onStatusUpdate("error");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("AdmobRewardedVideo export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("AdmobRewardedVideo export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("AdmobRewardedVideo export_load");
        final AdmobRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("AdmobRewardedVideo export_show");
        final AdmobRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("AdmobRewardedVideo import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
}
