package com.gmb.bbm2.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by GMB on 12/23/2015.
 */
public class MyAlarmReceiverOnTime extends BroadcastReceiver {

    public static final String groupNotif="AppGroupNotif";
    //MyBbmApplication bbmApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        long idStat = intent.getLongExtra("idStat", 0);
        int idAlarm = intent.getIntExtra("idAlarm", 0);
        long idNotif = intent.getLongExtra("idNotif", 0);
        long idRem = intent.getLongExtra("idReminder", 0);

        //Log.e("MyAlarmOnTIme", "MyAlarmReceiverOnTime onReceive NotifID->" + idNotif+"< idAlarm->"+idAlarm+"< idStat->"+idStat);


        //delegue tout le travail a MyAlarmService



            /*Intent service = new Intent(context, MyAlarmService.class);

            service.setAction(MyAlarmService.CREATE);
            //service.putExtra("notificationId", bufUri.getLastPathSegment());
            service.putExtra("callerEvent", MyAlarmService.CALLER_EVENT_NOTIF_ON_TIME);
            service.putExtra("notificationId", "reboot");

            service.putExtra("runningNotif",1);
            service.putExtra("doneNotif", 0);
            service.putExtra("canceledNotif",0);
            service.putExtra("missedNotif", 0);
            service.putExtra("idAlarm",idAlarm);
            service.putExtra("idNotif",  idNotif);
            service.putExtra("idReminder",  idRem);
            service.putExtras(intent.getExtras());

            //Log.e("ReceiverOnTime","onReceive APpel de MyAlarmService");
            context.startService(service);*/









    }
}
