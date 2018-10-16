package com.san.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotifySender {
    public static void export_sendNotify(final int ID,final String szTitle,final String szContent,final String szTickerText,final int iUnixTimeSecond) {
        System.out.println("NotifySender export_sendNotify");
        AdsUtil.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //创建Intent对象，action为ELITOR_CLOCK，附加信息为字符串“你该打酱油了”
                Intent intent = new Intent(AdsUtil.getActivity(),NotifyReceiver.class);
                intent.putExtra("ID",ID);
                intent.putExtra("Title",szTitle);
                intent.putExtra("Content",szContent);
                intent.putExtra("TickerText",szTickerText);

                //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
                //也就是发送了action 为"ELITOR_CLOCK"的intent
                PendingIntent pi = PendingIntent.getBroadcast(AdsUtil.getActivity(),ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                //AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
                AlarmManager am = (AlarmManager)AdsUtil.getActivity().getSystemService(ALARM_SERVICE);

                //设置闹钟从当前时间开始，每隔5s执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
                am.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis()+iUnixTimeSecond * 1000, pi);
            }
        });
    }
}
