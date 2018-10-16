package com.san.util;

import android.app.AlertDialog;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import org.json.JSONException;
import org.json.JSONObject;

public class FbAdsInterstitial implements InterstitialAdListener {
    private InterstitialAd mAd;

    private String mStrPlacementID;

    public FbAdsInterstitial(final String strPlacementID) {
        this.mStrPlacementID = strPlacementID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isAdLoaded();
    }

    public void load() {
        if (this.mAd == null) {
            this.mAd = new InterstitialAd(AdsUtil.getActivity(),this.mStrPlacementID);
            this.mAd.setAdListener(this);
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
    public void onInterstitialDisplayed(Ad ad) {
        System.out.println("FbAdsInterstitial onInterstitialDisplayed");

        this.onStatusUpdate("show");
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        System.out.println("FbAdsInterstitial onInterstitialDismissed");

        this.mAd = null;
        this.onStatusUpdate("closed");
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        System.out.println("FbAdsInterstitial onError:"+adError.getErrorMessage());

//        new  AlertDialog.Builder(FbAds.getActivity())
//                .setTitle("测试" )
//                .setMessage("FbAdsInterstitial onError errorid="+adError.getErrorCode() + " errormsg=" + adError.getErrorMessage())
//                .setPositiveButton("确定" ,  null )
//                .show();

        this.mAd = null;
        this.onStatusUpdate("error");
    }

    @Override
    public void onAdLoaded(Ad ad) {
        System.out.println("FbAdsInterstitial onAdLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onAdClicked(Ad ad) {
        System.out.println("FbAdsInterstitial onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        System.out.println("FbAdsInterstitial onLoggingImpression");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("FbAdsInterstitial export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("FbAdsInterstitial export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("FbAdsInterstitial export_load");
        final FbAdsInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("FbAdsInterstitial export_show");
        final FbAdsInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("FbAdsInterstitial import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
}
