package com.gmb.bbm2.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by GMB on 8/3/2016.
 */
public class MyBufferedReceiver extends BroadcastReceiver {
    MediaPlayer mp;


    @Override
    public void onReceive(Context context, Intent intent) {
        /*mp=MediaPlayer.create(context, R.raw.alrm  );
        mp.start();*/
        //Toast.makeText(context, "MyBufferedReceiver button pressed just now....", Toast.LENGTH_LONG).show();

        //Log.e("MyBufferedReceiver","OnReceive this is extra->"+intent.getExtras());

        /*String action = intent.getAction();
        String idNotif =""+ intent.getLongExtra("idNotif",0);
        String idAlarm =""+ intent.getIntExtra("idAlarm",0);
        int callerEvent = intent.getIntExtra("callerEvent", 0);


        //MyBbmApplication bbmApp2= MyBbmApplication.getInstance();
        //notifOrg=new MyNotifOrganizer(getApplicationContext());

        /*boolean isFromNotif=intent.getBooleanExtra("isFromNotif",false);
        if(isFromNotif){

            cancelNotification(context);
            //new MyDBinteraction(getApplicationContext()).setNotifSend(0,0);
            bbmApp2.saveNotifSendAndIdInPref("0","0");
            //notifOrg.doAction(""+MyNotifOrganizer.ACTION_SET_NOTIF_SEND,"0","0");

            //Log.e("AccueilActivityOld","AcceuilActivity onCreate activity call from a notification");
            //bbmApp.setInstanceNotifsSend(0,0);

        }*/

        cancelNotification(context);

        /*Intent service = new Intent(context, MyAlarmService.class);

        service.setAction(MyAlarmService.CREATE);
        //service.putExtra("notificationId", bufUri.getLastPathSegment());



        service.putExtras(intent.getExtras());

        //Log.e("MyBufferedReceiver","MyBufferedReceiver APpel de MyAlarmService avec extra->"+intent.getExtras());
        context.startService(service);*/
    }



    public void cancelNotification(Context context){

        try{

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // Will display the notification in the notification bar
            //notificationManager.cancel(Integer.parseInt(idNotif));

            notificationManager.cancelAll();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }
}
