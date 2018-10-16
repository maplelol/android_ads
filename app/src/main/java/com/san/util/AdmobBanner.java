package com.san.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

public class AdmobBanner{
    private static final String GOOGLE_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

    private RelativeLayout mBanner = null;
    private AdView mAdView = null;
    private boolean mIsLoaded = false;

    private String mStrUnitID;

    public AdmobBanner(final String strUnitID) {
        this.mStrUnitID = strUnitID;
    }

    public boolean isLoaded() {
        return this.mAdView != null && this.mIsLoaded;
    }

    public void load() {
        if (this.mIsLoaded) {
            return;
        }

        if (mBanner == null) {
            mBanner = new RelativeLayout(AdsUtil.getActivity());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            AdsUtil.getActivity().addContentView(mBanner,lp);
            mBanner.setVisibility(View.INVISIBLE);
        }

        if (this.mAdView == null) {
            final AdmobBanner mThis = this;
            this.mAdView = new AdView(AdsUtil.getActivity());
            this.mAdView.setAdSize(AdSize.BANNER);
            this.mAdView.setAdUnitId(AdsUtil.IS_DEBUG ? GOOGLE_UNIT_ID : this.mStrUnitID);
            this.mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    System.out.println("AdmobBanner onAdClosed");

                    //mThis.close();
                }

                @Override
                public void onAdFailedToLoad(int var1) {
                    System.out.println("AdmobBanner onAdFailedToLoad");

                    mThis.mIsLoaded = false;
                    mThis.onStatusUpdate("error");
                }

                @Override
                public void onAdLeftApplication() {
                    System.out.println("AdmobBanner onAdLeftApplication");
                }

                @Override
                public void onAdOpened() {
                    System.out.println("AdmobBanner onAdOpened");
                }

                @Override
                public void onAdLoaded() {
                    System.out.println("AdmobBanner onAdLoaded");

                    mThis.mIsLoaded = true;
                    mThis.onStatusUpdate("loaded");
                }
            });

            RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            adParams.setMargins(5, 5, 5, 5);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBanner.addView(mAdView,adParams);
        }

        AdRequest adRequest = null;
        if (AdsUtil.IS_DEBUG) {
            adRequest = new AdRequest.Builder().addTestDevice(Admob.TEST_DEVICE).build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        this.mAdView.loadAd(adRequest);
    }

    public void show() {
        if (this.isLoaded()) {
            this.mBanner.setVisibility(View.VISIBLE);
            //this.mBanner.bringToFront();
            this.onStatusUpdate("show");
        }
    }

    public void close() {
        this.mBanner.setVisibility(View.INVISIBLE);

        this.mIsLoaded = false;
        this.onStatusUpdate("closed");
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
        System.out.println("AdmobBanner export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("AdmobBanner export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("AdmobBanner export_load");
        final AdmobBanner mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("AdmobBanner export_show");
        final AdmobBanner mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void export_close() {
        System.out.println("AdmobBanner export_close");
        final AdmobBanner mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.close();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("AdmobBanner import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
}
