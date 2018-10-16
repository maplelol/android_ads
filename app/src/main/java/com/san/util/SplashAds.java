package com.san.util;

import android.animation.AnimatorSet;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.san.util.R;

public class SplashAds {
    private static RelativeLayout mView;

    public static void show() {
        if (mView == null) {
            LayoutInflater inflater = LayoutInflater.from(AdsUtil.getActivity());
            mView = (RelativeLayout)inflater.inflate(R.layout.splash_ad_view, null, false);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            AdsUtil.getActivity().addContentView(mView,lp);
        }
        mView.bringToFront();
    }

    public static void close() {
        if (mView != null) {
            ViewGroup vg = (ViewGroup) mView.getParent();
            vg.removeView(mView);
            mView = null;
        }
    }

    public static void addAdView(final View adView) {
        if (mView != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mView == null) {
                        return;
                    }
                    FrameLayout frameLayout = mView.findViewById(R.id.ad_placeholder);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);

                    ImageView bg = mView.findViewById(R.id.im_bg);
                    bg.setImageDrawable(AdsUtil.getActivity().getResources().getDrawable(R.drawable.xad_native_splash_bg));
                }
            },800);

            final ImageView logo_part = mView.findViewById(R.id.img_logo);
            logo_part.setImageResource(AdsUtil.getSplashLogoID());

            final FrameLayout logo = mView.findViewById(R.id.fl_logo);
            TranslateAnimation translateAnimation1 = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.45f);
            translateAnimation1.setDuration(800);
            translateAnimation1.setFillAfter(true);

            logo.startAnimation(translateAnimation1);

            //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) logo.getLayoutParams();
            //lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    public static void export_show() {
        System.out.println("SplashAds export_show");
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SplashAds.show();
            }
        });
    }

    public static void export_close() {
        System.out.println("SplashAds export_close");
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SplashAds.close();
            }
        });
    }
}
