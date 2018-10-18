package com.san.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.GooglePlayServicesAdRenderer;
import com.mopub.nativeads.MediaViewBinder;
import com.san.util.R;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import org.json.JSONException;
import org.json.JSONObject;

public class MoPubAdsNativeSplash implements View.OnClickListener,View.OnTouchListener {
    private NativeAd mAdContent;
    private MoPubNative mAd;
    private MoPubNative.MoPubNativeNetworkListener mAdListener = null;

    private RelativeLayout mNativeAdContainer;
    private LinearLayout mAdView;

    private String mStrUnitID;

    public MoPubAdsNativeSplash(final String strUnitID) {
        this.mStrUnitID = AdsUtil.IS_DEBUG ? "11a17b188668469fb0412708c3d16813" : strUnitID;

        final MoPubAdsNativeSplash mThis = this;
        mAdListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {
                System.out.println("MoPubAdsNativeSplash onNativeLoad");
                mThis.mAdContent = nativeAd;
                mThis.onStatusUpdate("loaded");
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                System.out.println("MoPubAdsNativeSplash onNativeFail errorCode "+errorCode);
                mThis.onStatusUpdate("error");
            }
        };
    }

    public boolean isLoaded() {
        return this.mAdContent != null;
    }

    public void load() {
        if (this.isLoaded()) {
            return;
        }

        if (this.mAd == null) {
            this.mAd = new MoPubNative(AdsUtil.getActivity(),this.mStrUnitID,this.mAdListener);

            FacebookAdRenderer fbAdRender = new FacebookAdRenderer(new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.fb_native_splash_ad)
                    .mediaViewId(R.id.xad_media)
                    .adIconViewId(R.id.xad_icon)
                    .titleId(R.id.xad_title)
                    .textId(R.id.xad_body)
                    .adChoicesRelativeLayoutId(R.id.xad_choices_container)
                    .callToActionId(R.id.xad_cta)
                    .build());
            this.mAd.registerAdRenderer(fbAdRender);

            GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(new MediaViewBinder.Builder(R.layout.mopub_admob_native_splash_ad)
                    .mediaLayoutId(R.id.xad_media)
                    .iconImageId(R.id.xad_icon)
                    .titleId(R.id.xad_title)
                    .textId(R.id.xad_body)
                    .privacyInformationIconImageId(R.id.xad_privacy_ad_icon)
                    .callToActionId(R.id.xad_cta)
                    .build());
            this.mAd.registerAdRenderer(googlePlayServicesAdRenderer);

            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.mopub_native_splash_ad)
                    .mainImageId(R.id.xad_media)
                    .iconImageId(R.id.xad_icon)
                    .titleId(R.id.xad_title)
                    .textId(R.id.xad_body)
                    .privacyInformationIconImageId(R.id.xad_privacy_ad_icon)
                    .callToActionId(R.id.xad_cta)
                    .build());
            this.mAd.registerAdRenderer(moPubStaticNativeAdRenderer);
        }

        this.mAd.makeRequest();
    }

    public void show() {
        if (this.isLoaded()) {
            AdapterHelper adapterHelper = new AdapterHelper(AdsUtil.getActivity(), 0, 3);
            // Retrieve the pre-built ad view that AdapterHelper prepared for us.
            View v = adapterHelper.getAdView(null, null, this.mAdContent, new ViewBinder.Builder(0).build());
            // Set the native event listeners (onImpression, and onClick).
            v.findViewById(R.id.btn_close_native_ad).setOnClickListener(this);
            SplashAds.addAdView(v);

            this.onStatusUpdate("show");
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
        System.out.println("MoPubAdsNativeSplash export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("MoPubAdsNativeSplash export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("MoPubAdsNativeSplash export_load");
        final MoPubAdsNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("MoPubAdsNativeSplash export_show");
        final MoPubAdsNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("MoPubAdsNativeSplash import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close_native_ad) {
            this.mAdContent = null;
            SplashAds.close();
            this.onStatusUpdate("closed");
        }
    }

    @Override
    public boolean onTouch(View var1, MotionEvent var2) {
        return true;
    }
}
