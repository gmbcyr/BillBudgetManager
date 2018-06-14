package com.gmb.bbm2.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by GMB on 12/23/2015.
 */
//public class MyAlarmSetterOnBoot extends WakefulBroadcastReceiver {

public class MyAlarmSetterOnBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent service = new Intent(context, MyAlarmService.class);
        service.setAction(MyAlarmService.CREATE);
        service.putExtra("notificationId", "reboot");
        service.putExtra("callerEvent", MyAlarmService.CALLER_EVENT_REBOOT);

        //Toast.makeText(context, "Hello! Got the boot message",Toast.LENGTH_LONG).show();

       // pushNotification(context,intent,"From onBoot","Id by onBoot",0,0);

        context.startService(service);*/
    }


    private void pushNotification(Context context,Intent intent,String msg, String msgId, int notificationId,
                                  int type) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = new Bundle();
        //bundle.putString(Utility.KEY_MESSAGE_DELIVERY_ID, msgId);
        //bundle.putBoolean(Utility.KEY_IS_NOTIFICATION, true);
        //Intent intent = new Intent(this, Activity_ReceivedGreeting.class);
        intent.putExtras(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                //.setSmallIcon(R.drawable.next)
                .setContentTitle("ReminderPro")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(
                        RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}
