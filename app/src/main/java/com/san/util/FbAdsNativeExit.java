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
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FbAdsNativeExit implements NativeAdListener,View.OnClickListener,View.OnTouchListener {
    private NativeAd mAd;
    private RelativeLayout mNativeAdContainer;
    private LinearLayout mAdView;

    private String mStrPlacementID;

    public FbAdsNativeExit(final String strPlacementID) {
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
            // Add the Ad view into the ad container.
            //mNativeAdContainer = (LinearLayout) AdsUtil.getActivity().findViewById(R.id.native_ad_container);
            //mNativeAdContainer = new LinearLayout(AdsUtil.getActivity());
            LayoutInflater inflater = LayoutInflater.from(AdsUtil.getActivity());
            if (mNativeAdContainer == null) {
                mNativeAdContainer = (RelativeLayout)inflater.inflate(R.layout.fb_native_exit_ad_view, null, false);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                AdsUtil.getActivity().addContentView(mNativeAdContainer,lp);
                mNativeAdContainer.findViewById(R.id.btn_native_exit_confirm).setOnClickListener(this);
                mNativeAdContainer.findViewById(R.id.btn_native_exit_cancel).setOnClickListener(this);
                mNativeAdContainer.setOnTouchListener(this);
            } else {
                mNativeAdContainer.setVisibility(View.VISIBLE);
            }

            //mAdView = (LinearLayout) inflater.inflate(R.layout.fb_native_ad, mNativeAdContainer, false);
            //mNativeAdContainer.addView(mAdView);
            //mAdView.setVisibility(View.INVISIBLE);

            LinearLayout adView = (LinearLayout)inflater.inflate(R.layout.fb_native_exit_ad, null);
            // Create native UI using the ad metadata.
            com.facebook.ads.AdIconView nativeAdIcon = (com.facebook.ads.AdIconView) adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
            Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(mAd.getAdvertiserName());
            nativeAdSocialContext.setText(mAd.getAdSocialContext());
            nativeAdBody.setText(mAd.getAdBodyText());
            nativeAdCallToAction.setText(mAd.getAdCallToAction());
            nativeAdCallToAction.setVisibility(mAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

            // Add the AdChoices icon
            RelativeLayout adChoicesContainer = (RelativeLayout) adView.findViewById(R.id.ad_choices_container);
            AdChoicesView adChoicesView = new AdChoicesView(AdsUtil.getActivity(), mAd, true);
            adChoicesContainer.addView(adChoicesView);

            // Register the Title and CTA button to listen for clicks.
            List<View> clickableViews = new ArrayList<>();
            //clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            mAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon,clickableViews);

            FrameLayout frameLayout = this.mNativeAdContainer.findViewById(R.id.ad_placeholder);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
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
        System.out.println("FbAdsNativeExit onError:"+adError.getErrorMessage());

//        new  AlertDialog.Builder(FbAds.getActivity())
//                .setTitle("测试" )
//                .setMessage("FbAdsNativeExit onError errorid="+adError.getErrorCode() + " errormsg=" + adError.getErrorMessage())
//                .setPositiveButton("确定" ,  null )
//                .show();

        this.mAd = null;
        this.onStatusUpdate("error");
    }

    @Override
    public void onAdLoaded(Ad ad) {
        System.out.println("FbAdsNativeExit onAdLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onAdClicked(Ad ad) {
        System.out.println("FbAdsNativeExit onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        System.out.println("FbAdsNativeExit onLoggingImpression");

        this.onStatusUpdate("show");
    }

    @Override
    public void onMediaDownloaded(Ad var1) {
        System.out.println("FbAdsNativeExit onMediaDownloaded");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("FbAdsNativeExit export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("FbAdsNativeExit export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("FbAdsNativeExit export_load");
        final FbAdsNativeExit mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("FbAdsNativeExit export_show");
        final FbAdsNativeExit mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("FbAdsNativeExit import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_native_exit_confirm) {
            this.onStatusUpdate("confirm");
            this.onStatusUpdate("closed");
        } else if (v.getId() == R.id.btn_native_exit_cancel) {
            this.mAd = null;
            mNativeAdContainer.setVisibility(View.INVISIBLE);
            this.onStatusUpdate("closed");
        }
    }

    @Override
    public boolean onTouch(View var1, MotionEvent var2) {
        return true;
    }
}
