package com.san.util;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.concurrent.Callable;

public class AdsUtil {
    public static boolean IS_DEBUG = false;

    private static Activity mActivity;
    private static CallbackToJS mScriptCb;

    private static int mLogoResID = -1;

    public static void init(boolean isDebug, Activity activity, CallbackToJS scriptCb) {
        IS_DEBUG = isDebug;
        mScriptCb = scriptCb;
        setActivity(activity);
    }

    public static void setSplashLogoID(int iResID) {
        mLogoResID = iResID;
    }

    public static int getSplashLogoID() {
        return mLogoResID;
    }

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static Activity getActivity() {
        return mActivity;
    }

    public static void export_AlertText(final String szText) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast t = Toast.makeText(getActivity(),szText,Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }
        });
    }

    public static void callbackToJS(Object obj,String cb,Object arg) {
        //ExportJavaFunction.CallBackToJS(obj,cb,arg);
        if (mScriptCb != null) {
            mScriptCb.call(obj,cb,arg);
        }
    }
}
