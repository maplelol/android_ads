package com.san.util;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.NativeAdListener;
import com.san.util.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FbAdsNativeSplash implements NativeAdListener,View.OnClickListener,View.OnTouchListener {
    private NativeAd mAd;
    private RelativeLayout mNativeAdContainer;
    private LinearLayout mAdView;

    private String mStrPlacementID;

    public FbAdsNativeSplash(final String strPlacementID) {
        this.mStrPlacementID = strPlacementID;
    }

    public boolean isLoaded() {
        return this.mAd != null && this.mAd.isAdLoaded();
    }

    public void load() {
        if (this.mAd == null) {
            this.mAd = new NativeAd(AdsUtil.getActivity(), this.mStrPlacementID);
            this.mAd.setAdListener(this);
            this.mAd.loadAd();
        }
    }

    public void show() {
        if (this.isLoaded()) {
//            if (mAdView != null) {
//                mNativeAdContainer.removeView(mAdView);
//                mAdView = null;
//            }

            LayoutInflater inflater = LayoutInflater.from(AdsUtil.getActivity());
            // Add the Ad view into the ad container.
            RelativeLayout nativeAdContainer = (RelativeLayout)inflater.inflate(R.layout.fb_native_splash_ad, null, false);
            nativeAdContainer.findViewById(R.id.btn_close_native_ad).setOnClickListener(this);

            //mAdView = (LinearLayout) inflater.inflate(R.layout.fb_native_ad, mNativeAdContainer, false);
            //mNativeAdContainer.addView(mAdView);
            //mAdView.setVisibility(View.INVISIBLE);

            // Create native UI using the ad metadata.
            com.facebook.ads.AdIconView nativeAdIcon = (com.facebook.ads.AdIconView) nativeAdContainer.findViewById(R.id.xad_icon);
            TextView nativeAdTitle = (TextView) nativeAdContainer.findViewById(R.id.xad_title);
            MediaView nativeAdMedia = (MediaView) nativeAdContainer.findViewById(R.id.xad_media);
            TextView nativeAdSocialContext = (TextView) nativeAdContainer.findViewById(R.id.xad_social_context);
            TextView nativeAdBody = (TextView) nativeAdContainer.findViewById(R.id.xad_body);
            Button nativeAdCallToAction = (Button) nativeAdContainer.findViewById(R.id.xad_cta);

            // Set the Text.
            nativeAdTitle.setText(mAd.getAdvertiserName());
            nativeAdSocialContext.setText(mAd.getAdSocialContext());
            nativeAdBody.setText(mAd.getAdBodyText());
            nativeAdCallToAction.setText(mAd.getAdCallToAction());
            nativeAdCallToAction.setVisibility(mAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

            // Add the AdChoices icon
            RelativeLayout adChoicesContainer = (RelativeLayout) nativeAdContainer.findViewById(R.id.xad_choices_container);
            AdChoicesView adChoicesView = new AdChoicesView(AdsUtil.getActivity(), mAd, true);
            adChoicesContainer.addView(adChoicesView);

            // Register the Title and CTA button to listen for clicks.
            List<View> clickableViews = new ArrayList<>();
            //clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            mAd.registerViewForInteraction(nativeAdContainer, nativeAdMedia, nativeAdIcon,clickableViews);

            SplashAds.addAdView(nativeAdContainer);
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
        System.out.println("FbAdsNativeSplash onError:"+adError.getErrorMessage());

//        new  AlertDialog.Builder(FbAds.getActivity())
//                .setTitle("测试" )
//                .setMessage("FbAdsNativeSplash onError errorid="+adError.getErrorCode() + " errormsg=" + adError.getErrorMessage())
//                .setPositiveButton("确定" ,  null )
//                .show();

        this.mAd = null;
        this.onStatusUpdate("error");
    }

    @Override
    public void onAdLoaded(Ad ad) {
        System.out.println("FbAdsNativeSplash onAdLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onAdClicked(Ad ad) {
        System.out.println("FbAdsNativeSplash onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        System.out.println("FbAdsNativeSplash onLoggingImpression");

        this.onStatusUpdate("show");
    }

    @Override
    public void onMediaDownloaded(Ad var1) {
        System.out.println("FbAdsNativeSplash onMediaDownloaded");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("FbAdsNativeSplash export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("FbAdsNativeSplash export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("FbAdsNativeSplash export_load");
        final FbAdsNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("FbAdsNativeSplash export_show");
        final FbAdsNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("FbAdsNativeSplash import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close_native_ad) {
            this.mAd = null;
            SplashAds.close();
            this.onStatusUpdate("closed");
        }
    }

    @Override
    public boolean onTouch(View var1, MotionEvent var2) {
        return true;
    }
}
