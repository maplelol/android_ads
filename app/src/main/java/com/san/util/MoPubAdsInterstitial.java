package com.san.util;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import org.json.JSONException;
import org.json.JSONObject;

public class MoPubAdsInterstitial implements com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener {
    private MoPubInterstitial mAd;

    private String mStrUnitID;

    public MoPubAdsInterstitial(final String strUnitID) {
        this.mStrUnitID = AdsUtil.IS_DEBUG ? "24534e1901884e398f1253216226017e" : strUnitID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isReady();
    }

    public void load() {
        if (this.isLoaded()) {
            return;
        }

        if (this.mAd == null) {
            this.mAd = new MoPubInterstitial(AdsUtil.getActivity(), this.mStrUnitID);
            this.mAd.setInterstitialAdListener(this);
        }
        this.mAd.load();
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

    public void export_setOnStatusUpdate() {
        System.out.println("MoPubAdsInterstitial export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("MoPubAdsInterstitial export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("MoPubAdsInterstitial export_load");
        final MoPubAdsInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("MoPubAdsInterstitial export_show");
        final MoPubAdsInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("MoPubAdsInterstitial import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }

    @Override
    public void onInterstitialLoaded(com.mopub.mobileads.MoPubInterstitial interstitial) {
        System.out.println("MoPubAdsInterstitial onInterstitialLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onInterstitialFailed(com.mopub.mobileads.MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        System.out.println("MoPubAdsInterstitial onInterstitialFailed errorCode="+errorCode);

        this.onStatusUpdate("error");
    }

    @Override
    public void onInterstitialShown(com.mopub.mobileads.MoPubInterstitial interstitial) {
        System.out.println("MoPubAdsInterstitial onInterstitialShown");

        this.onStatusUpdate("show");
    }

    @Override
    public void onInterstitialClicked(com.mopub.mobileads.MoPubInterstitial interstitial) {
        System.out.println("MoPubAdsInterstitial onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(com.mopub.mobileads.MoPubInterstitial interstitial) {
        System.out.println("MoPubAdsInterstitial onInterstitialDismissed");

        this.onStatusUpdate("closed");
    }
}
