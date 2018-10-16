package com.san.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class NotifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("NotifyReceiver onReceive");
		// TODO Auto-generated method stub
		int iID = intent.getIntExtra("ID", -1);
		String szTitle = intent.getStringExtra("Title");
		String szContent = intent.getStringExtra("Content");
		String szTickerText = intent.getStringExtra("TickerText");
    	NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	nm.cancel(iID);
    	
    	String szActivityName = "";
    	int iSelfIcon = -1;
    	try {
    		ApplicationInfo appInfo;
		
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			iSelfIcon = appInfo.icon;
			Bundle bundle = appInfo.metaData;
	        szActivityName = bundle.getString("san_activity");
	        
    	} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	try {
			Intent newIntent = new Intent(context, Class.forName(szActivityName));
			
			PendingIntent pi= PendingIntent.getActivity(context, 0, newIntent, 0);
			
	    	Notification n = new Notification.Builder(context)
	    					.setSmallIcon(iSelfIcon)
	    					.setTicker(szTickerText)
	    					.setWhen(System.currentTimeMillis())
	    					.setContentTitle(szTitle)
	    					.setContentText(szContent)
	    					.setContentIntent(pi).setNumber(1).build();
	    	n.defaults = Notification.DEFAULT_ALL;
	    	n.flags |= Notification.FLAG_AUTO_CANCEL;
	    	
	    	nm.notify(iID, n);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

}
