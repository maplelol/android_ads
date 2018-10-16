package com.san.util;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.san.util.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdmobNativeSplash implements View.OnClickListener{
    private static final String GOOGLE_UNIT_ID = "ca-app-pub-3940256099942544/1044960115";

    private String mStrUnitID;
    private UnifiedNativeAd mAd;

    public AdmobNativeSplash(final String strUnitID) {
        this.mStrUnitID = strUnitID;
    }

    public boolean isLoaded() {
        return this.mAd != null;
    }

    public void load() {
        if (this.isLoaded()) {
            return;
        }

        final AdmobNativeSplash mThis = this;

        AdLoader.Builder builder = new AdLoader.Builder(AdsUtil.getActivity(), AdsUtil.IS_DEBUG ? GOOGLE_UNIT_ID : this.mStrUnitID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                mThis.mAd = unifiedNativeAd;
                mThis.onStatusUpdate("loaded");
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
//                new  AlertDialog.Builder(FbAds.getActivity())
//                        .setTitle("测试" )
//                        .setMessage("AdmobNativeSplash onError errorid="+errorCode)
//                        .setPositiveButton("确定" ,  null )
//                        .show();
                mThis.onStatusUpdate("error");
            }
        }).build();

        if (AdsUtil.IS_DEBUG) {
            adLoader.loadAd(new AdRequest.Builder().addTestDevice(Admob.TEST_DEVICE).build());
        } else {
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void show() {
        if (this.isLoaded()) {
            VideoController vc = this.mAd.getVideoController();

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before refreshing
                    // or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });

            LayoutInflater inflater = LayoutInflater.from(AdsUtil.getActivity());
            RelativeLayout nativeAdContainer = (RelativeLayout)inflater.inflate(R.layout.admob_native_splash_ad, null, false);
            //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //AdsUtil.getActivity().addContentView(nativeAdContainer,lp);
            nativeAdContainer.findViewById(R.id.btn_close_native_ad).setOnClickListener(this);

            UnifiedNativeAdView adView = (UnifiedNativeAdView)nativeAdContainer.findViewById(R.id.ad_view);
            MediaView mediaView = adView.findViewById(R.id.xad_media);
            ImageView mainImageView = adView.findViewById(R.id.xad_image);

            // Apps can check the VideoController's hasVideoContent property to determine if the
            // NativeAppInstallAd has a video asset.
            if (vc.hasVideoContent()) {
                adView.setMediaView(mediaView);
                mainImageView.setVisibility(View.GONE);
            } else {
                adView.setImageView(mainImageView);
                mediaView.setVisibility(View.GONE);

                // At least one image is guaranteed.
                List<NativeAd.Image> images = this.mAd.getImages();
                mainImageView.setImageDrawable(images.get(0).getDrawable());
            }

            adView.setHeadlineView(adView.findViewById(R.id.xad_title));
            adView.setBodyView(adView.findViewById(R.id.xad_body));
            adView.setCallToActionView(adView.findViewById(R.id.xad_cta));
            adView.setIconView(adView.findViewById(R.id.xad_icon));
            adView.setPriceView(adView.findViewById(R.id.xad_text_price));
            adView.setStarRatingView(adView.findViewById(R.id.xad_stars));
            adView.setStoreView(adView.findViewById(R.id.xad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.xad_social_context));

            // Some assets are guaranteed to be in every UnifiedNativeAd.
            ((TextView) adView.getHeadlineView()).setText(this.mAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(this.mAd.getBody());
            ((Button) adView.getCallToActionView()).setText(this.mAd.getCallToAction());

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (this.mAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        this.mAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }

            if (this.mAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(this.mAd.getPrice());
            }

            if (this.mAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(this.mAd.getStore());
            }

            if (this.mAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(this.mAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (this.mAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(this.mAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
            adView.setNativeAd(this.mAd);

            SplashAds.addAdView(nativeAdContainer);

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
        System.out.println("AdmobNativeSplash export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("AdmobNativeSplash export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("AdmobNativeSplash export_load");
        final AdmobNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("AdmobNativeSplash export_show");
        final AdmobNativeSplash mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("AdmobNativeSplash import_onStatusUpdate");
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
}
