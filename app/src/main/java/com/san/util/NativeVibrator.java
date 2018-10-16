package com.san.util;

import android.content.Context;
import android.os.Vibrator;

public class NativeVibrator {

    private Vibrator mVibrator;

    public void export_vibrate(final String szPattern,final int iRepeat) {
        final NativeVibrator nv = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (nv.mVibrator == null) {
                    nv.mVibrator = (Vibrator)AdsUtil.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                } else {
                    nv.mVibrator.cancel();
                }
                String[] pa = szPattern.split(",");
                long [] pattern = new long[pa.length];
                for(int i=0; i<pa.length; ++i) {
                    pattern[i] = Integer.valueOf(pa[i]);
                }
                nv.mVibrator.vibrate(pattern,iRepeat);
            }
        });
    }

    public void export_cancel() {
        final NativeVibrator nv = this;
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (nv.mVibrator != null) {
                    nv.mVibrator.cancel();
                }
            }
        });
    }
}
