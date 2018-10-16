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

import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.GooglePlayServicesAdRenderer;
import com.mopub.nativeads.MediaViewBinder;
import com.san.util.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import org.json.JSONException;
import org.json.JSONObject;

public class MoPubAdsNativeExit implements AdListener,View.OnClickListener,View.OnTouchListener {
    private com.mopub.nativeads.NativeAd mAdContent;
    private MoPubNative mAd;
    private MoPubNative.MoPubNativeNetworkListener mAdListener = null;

    private RelativeLayout mNativeAdContainer;
    private LinearLayout mAdView;

    private String mStrUnitID;

    public MoPubAdsNativeExit(final String strUnitID) {
        this.mStrUnitID = AdsUtil.IS_DEBUG ? "11a17b188668469fb0412708c3d16813" : strUnitID;

        final MoPubAdsNativeExit mThis = this;
        mAdListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(com.mopub.nativeads.NativeAd nativeAd) {
                System.out.println("MoPubAdsNativeExit onNativeLoad");
                mThis.mAdContent = nativeAd;
                mThis.onStatusUpdate("loaded");
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                System.out.println("MoPubAdsNativeExit onNativeFail errorCode "+errorCode);
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

            FacebookAdRenderer fbAdRender = new FacebookAdRenderer(new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.fb_native_exit_ad)
                    .mediaViewId(R.id.native_ad_media)
                    .adIconViewId(R.id.native_ad_icon)
                    .titleId(R.id.native_ad_title)
                    .textId(R.id.native_ad_body)
                    .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                    .callToActionId(R.id.native_ad_call_to_action)
                    .build());
            this.mAd.registerAdRenderer(fbAdRender);

            GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(new MediaViewBinder.Builder(R.layout.mopub_native_exit_ad)
                    .mediaLayoutId(R.id.xad_media)
                    .iconImageId(R.id.xad_icon)
                    .titleId(R.id.xad_title)
                    .textId(R.id.xad_body)
                    .privacyInformationIconImageId(R.id.xad_privacy_ad_icon)
                    .callToActionId(R.id.xad_cta)
                    .build());
            this.mAd.registerAdRenderer(googlePlayServicesAdRenderer);

            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.mopub_native_exit_ad)
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
            LayoutInflater inflater = LayoutInflater.from(AdsUtil.getActivity());
            if (mNativeAdContainer == null) {
                mNativeAdContainer = (RelativeLayout)inflater.inflate(R.layout.mopub_native_exit_ad_view, null, false);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                AdsUtil.getActivity().addContentView(mNativeAdContainer,lp);
                mNativeAdContainer.findViewById(R.id.btn_native_exit_confirm).setOnClickListener(this);
                mNativeAdContainer.findViewById(R.id.btn_native_exit_cancel).setOnClickListener(this);
                mNativeAdContainer.setOnTouchListener(this);
            } else {
                mNativeAdContainer.setVisibility(View.VISIBLE);
            }

            // Add the Ad view into the ad container.
            AdapterHelper adapterHelper = new AdapterHelper(AdsUtil.getActivity(), 0, 3);
            // Retrieve the pre-built ad view that AdapterHelper prepared for us.
            View v = adapterHelper.getAdView(null, null, this.mAdContent, new ViewBinder.Builder(0).build());
            FrameLayout frameLayout = this.mNativeAdContainer.findViewById(R.id.ad_placeholder);
            frameLayout.removeAllViews();
            frameLayout.addView(v);

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

    @Override
    public void onError(Ad ad, AdError adError) {
        System.out.println("MoPubAdsNativeExit onError:"+adError.getErrorMessage());

        this.mAd = null;
        this.onStatusUpdate("error");
    }

    @Override
    public void onAdLoaded(Ad ad) {
        System.out.println("MoPubAdsNativeExit onAdLoaded");

        this.onStatusUpdate("loaded");
    }

    @Override
    public void onAdClicked(Ad ad) {
        System.out.println("MoPubAdsNativeExit onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        System.out.println("MoPubAdsNativeExit onLoggingImpression");

        this.onStatusUpdate("show");
    }

    public void export_setOnStatusUpdate() {
        System.out.println("MoPubAdsNativeExit export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("MoPubAdsNativeExit export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("MoPubAdsNativeExit export_load");
        final MoPubAdsNativeExit mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("MoPubAdsNativeExit export_show");
        final MoPubAdsNativeExit mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("MoPubAdsNativeExit import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_native_exit_confirm) {
            this.onStatusUpdate("confirm");
            this.onStatusUpdate("closed");
        } else if (v.getId() ==  R.id.btn_native_exit_cancel) {
            this.mAdContent = null;
            mNativeAdContainer.setVisibility(View.INVISIBLE);
            this.onStatusUpdate("closed");
        }
    }

    @Override
    public boolean onTouch(View var1, MotionEvent var2) {
        return true;
    }
}
