package com.san.util;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class NativeShare {
    private static Activity mActivity;

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static void share(String strJson) {
        String strTitle = "Title";
        String strText = "Text";
        String strDialog = "DialogTitle";
        try {
            JSONObject pJson = new JSONObject(strJson);
            strTitle = pJson.getString("title");
            strText = pJson.getString("text");
            strDialog = pJson.getString("dialog_title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, strTitle);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, strText);//添加分享内容

        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, strDialog);
        mActivity.startActivity(share_intent);
    }


    public void export_share(final String strJson) {
        System.out.println("NativeShare export_share");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NativeShare.share(strJson);
            }
        });
    }
}
