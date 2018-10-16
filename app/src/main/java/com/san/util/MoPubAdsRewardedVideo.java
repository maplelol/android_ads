package com.san.util;

import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoPubAdsRewardedVideo {
    static private boolean mIsInit = false;
    static private MoPubRewardedVideoListener mAdListener = null;
    static private Map<String,MoPubAdsRewardedVideo> mMapAds = null;

    private String mStrUnitID;
    private boolean mIsLoaded;

    public MoPubAdsRewardedVideo(final String strUnitID) {
        _init();

        this.mIsLoaded = false;
        this.mStrUnitID = AdsUtil.IS_DEBUG ? "920b6145fb1546cf8b5cf2ac34638bb7" : strUnitID;
        mMapAds.put(this.mStrUnitID,this);
    }

    public boolean isLoaded() {
        return this.mIsLoaded;
    }


    public void load() {
        if (!this.isLoaded()) {
            MoPubRewardedVideos.loadRewardedVideo(this.mStrUnitID);
        }
    }

    public void show() {
        if (this.isLoaded()) {
            MoPubRewardedVideos.showRewardedVideo(this.mStrUnitID);
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
        System.out.println("MoPubAdsRewardedVideo export_setOnStatusUpdate");
    }

    public void export_isLoaded() {
        System.out.println("MoPubAdsRewardedVideo export_isLoaded");
        AdsUtil.callbackToJS(this,"export_isLoaded", this.isLoaded());
    }

    public void export_load() {
        System.out.println("MoPubAdsRewardedVideo export_load");
        final MoPubAdsRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.load();
            }
        });
    }

    public void export_show() {
        System.out.println("MoPubAdsRewardedVideo export_show");
        final MoPubAdsRewardedVideo mThis = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThis.show();
            }
        });
    }

    public void import_onStatusUpdate(String szResult) {
        System.out.println("MoPubAdsRewardedVideo import_onStatusUpdate");
        AdsUtil.callbackToJS(this,"export_setOnStatusUpdate", szResult);
    }

    private static void _init() {
        if (!mIsInit) {
            mIsInit = true;
            mMapAds = new HashMap<String,MoPubAdsRewardedVideo>();
            mAdListener = new MoPubRewardedVideoListener() {
                @Override
                public void onRewardedVideoLoadSuccess(String adUnitId) {
                    // Called when the video for the given adUnitId has loaded. At this point you should be able to call MoPubRewardedVideos.showRewardedVideo(String) to show the video.
                    System.out.println("MoPubAdsRewardedVideo onRewardedVideoLoadSuccess "+adUnitId);

                    if (mMapAds.containsKey(adUnitId)) {
                        mMapAds.get(adUnitId).mIsLoaded = true;
                        mMapAds.get(adUnitId).onStatusUpdate("loaded");
                    }
                }
                @Override
                public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
                    // Called when a video fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
                    System.out.println("MoPubAdsRewardedVideo onRewardedVideoLoadFailure "+adUnitId+" errorCode "+errorCode);

                    if (mMapAds.containsKey(adUnitId)) {
                        mMapAds.get(adUnitId).mIsLoaded = false;
                        mMapAds.get(adUnitId).onStatusUpdate("error");
                    }
                }

                @Override
                public void onRewardedVideoStarted(String adUnitId) {
                    // Called when a rewarded video starts playing.
                    System.out.println("MoPubAdsRewardedVideo onRewardedVideoStarted "+adUnitId);

                    if (mMapAds.containsKey(adUnitId)) {
                        mMapAds.get(adUnitId).onStatusUpdate("show");
                    }
                }

                @Override
                public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
                    //  Called when there is an error during video playback.
                    System.out.println("MoPubAdsRewardedVideo onRewardedVideoPlaybackError "+adUnitId);

                    if (mMapAds.containsKey(adUnitId)) {
                        mMapAds.get(adUnitId).mIsLoaded = false;
                        mMapAds.get(adUnitId).onStatusUpdate("error");
                    }
                }

                @Override
                public void onRewardedVideoClicked(String adUnitId) {

                }

                @Override
                public void onRewardedVideoClosed(String adUnitId) {
                    // Called when a rewarded video is closed. At this point your application should resume.
                    System.out.println("MoPubAdsRewardedVideo onRewardedVideoClosed "+adUnitId);

                    if (mMapAds.containsKey(adUnitId)) {
                        mMapAds.get(adUnitId).mIsLoaded = false;
                        mMapAds.get(adUnitId).onStatusUpdate("closed");
                    }
                }

                @Override
                public void onRewardedVideoCompleted(Set<String> adUnitIds, MoPubReward reward) {
                    // Called when a rewarded video is completed and the user should be rewarded.
                    // You can query the reward object with boolean isSuccessful(), String getLabel(), and int getAmount().

                    for (String adUnitId : adUnitIds) {
                        System.out.println("MoPubAdsRewardedVideo onRewardedVideoCompleted "+adUnitId);

                        if (mMapAds.containsKey(adUnitId)) {
                            mMapAds.get(adUnitId).onStatusUpdate("reward");
                        }
                    }
                }
            };
            MoPubRewardedVideos.setRewardedVideoListener(mAdListener);
        }
    }
}
