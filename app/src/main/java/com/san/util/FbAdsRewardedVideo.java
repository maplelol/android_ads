package com.san.util;

import android.app.AlertDialog;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

import org.json.JSONException;
import org.json.JSONObject;

public class FbAdsRewardedVideo implements RewardedVideoAdListener {
    private RewardedVideoAd mAd;
    private String mStrPlacementID;

    public FbAdsRewardedVideo(final String strPlacementID) {
        this.mStrPlacementID = strPlacementID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isAdLoaded();
    }

    public void load() {
        if (this.mAd == null) {
            this.mAd = new RewardedVideoAd(AdsUtil.getActivity(),this.mStrPlacementID);
            this.mAd.setAdListener(this);
            this.mAd.setRewardData(new RewardData("", ""));
            this.mAd.loadAd();
        }
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

    @Override
    public void onError(Ad ad, AdError adError) {
        System.out.println("FbAdsRewardedVideo onError:"+adError.getErrorMessage());

//        new  AlertDialog.Builder(FbAds.getActivity())
//                .setTitle("测试" )
//                .setMessage("FbAdsRewardedVideo onError errorid="+adError.getErrorCode() + " errormsg=" + adError.getErrorMessage())
//                .setPositiveButton("确定" ,  null )
//                .show();

        this.mAd = null;
        this.onStatusUpdate("error");
    }

    @Override
    public void onAdLoaded(Ad ad) {
        System.out.println("FbAdsRewardedVideo onAdLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onAdClicked(Ad ad) {
    }

    @Override
    public void onRewardedVideoCompleted() {
        System.out.println("FbAdsRewardedVideo onRewardedVideoCompleted");

        this.onStatusUpdate("reward");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        System.out.println("FbAdsRewardedVideo onLoggingImpression");

        this.onStatusUpdate("show");
    }

    @Override
    public void onRewardedVideoClosed() {
        System.out.println("FbAdsRewardedVideo onRewardedVideoClosed");

        this.mAd = null;
        this.onStatusUpdate("closed");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("FbAdsRewardedVideo export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("FbAdsRewardedVideo export_load");
        final FbAdsRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("FbAdsRewardedVideo export_show");
        final FbAdsRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
}
