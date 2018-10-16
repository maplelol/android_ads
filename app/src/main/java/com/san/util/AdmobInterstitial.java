package com.san.util;

import android.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

public class AdmobInterstitial{
    private static final String GOOGLE_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    private InterstitialAd mAd;

    private String mStrUnitID;

    public AdmobInterstitial(final String strUnitID) {
        this.mStrUnitID = strUnitID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isLoaded();
    }

    public void load() {
        if (this.mAd == null) {
            final AdmobInterstitial mThis = this;
            this.mAd = new InterstitialAd(AdsUtil.getActivity());
            this.mAd.setAdUnitId(AdsUtil.IS_DEBUG ? GOOGLE_UNIT_ID : this.mStrUnitID);
            this.mAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    System.out.println("AdmobInterstitial onAdClosed");

                    mThis.onStatusUpdate("closed");
                }

                @Override
                public void onAdFailedToLoad(int var1) {
                    System.out.println("AdmobInterstitial onAdFailedToLoad");

//                    new  AlertDialog.Builder(FbAds.getActivity())
//                            .setTitle("测试" )
//                            .setMessage("AdmobInterstitial onError errorid="+var1)
//                            .setPositiveButton("确定" ,  null )
//                            .show();

                    mThis.onStatusUpdate("error");
                }

                @Override
                public void onAdLeftApplication() {
                    System.out.println("AdmobInterstitial onAdLeftApplication");
                }

                @Override
                public void onAdOpened() {
                    System.out.println("AdmobInterstitial onAdOpened");

                    mThis.onStatusUpdate("show");
                }

                @Override
                public void onAdLoaded() {
                    System.out.println("AdmobInterstitial onAdLoaded");

                    mThis.onStatusUpdate("loaded");
                }
            });
        }

        AdRequest adRequest = null;
        if (AdsUtil.IS_DEBUG) {
            adRequest = new AdRequest.Builder().addTestDevice(Admob.TEST_DEVICE).build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        this.mAd.loadAd(adRequest);
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
        System.out.println("AdmobInterstitial export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("AdmobInterstitial export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("AdmobInterstitial export_load");
        final AdmobInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("AdmobInterstitial export_show");
        final AdmobInterstitial mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("AdmobInterstitial import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
}
