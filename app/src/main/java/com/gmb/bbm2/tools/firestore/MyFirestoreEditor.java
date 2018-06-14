package com.gmb.bbm2.tools.firestore;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.gmb.bbm2.R;
import com.gmb.bbm2.data.model.BbmStatementFB;
import com.gmb.bbm2.data.model.OnTimeNotificationFB;
import com.gmb.bbm2.tools.alarm.MyAlarmReceiverOnTime;
import com.gmb.bbm2.tools.allstatic.AllStaticKt;
import com.gmb.bbm2.tools.app.MyBbmApplication;
import com.gmb.bbm2.tools.json.MyJsonParser;
import com.gmb.bbm2.tools.json.MyJsonParserKotlin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by GMB on 11/15/2017.
 */

public class MyFirestoreEditor {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    public static final int CALLER_EVENT_REBOOT=0;
    public static final int CALLER_EVENT_NEW=1;
    public static final int CALLER_EVENT_UPDATE=2;
    public static final int CALLER_EVENT_DONE=3;
    public static final int CALLER_EVENT_CANCELED=4;
    public static final int CALLER_EVENT_MISSED=5;
    public static final int CALLER_EVENT_DELETE=6;
    public static final int CALLER_EVENT_DELETE_ALL=7;
    public static final int CALLER_EVENT_REFILL=8;
    public static final int CALLER_EVENT_CANCEL_ALL=9;
    public static final int CALLER_EVENT_NOTIF_ON_TIME=11;
    public static final int CALLER_EVENT_LIST=12;
    public static final int CALLER_EVENT_EDIT_ONE=13;
    public static final int CALLER_EVENT_EXPORT_DB=14;
    public static final int CALLER_EVENT_IMPORT_DB=15;

    public static final int LIMIT_NOTIFICATONS_TO_HANDLE=10;
    public static final long AN_HOUR_TIME=60*60*1000;

    private static final String TAG = "MyFirestoreEditor";

    public static String COLLECTION_CATEGORY = "Categories";
    public static String COLLECTION_STATEMENT = "BBMstatements";
    public static String COLLECTION_ONTIMENOTIF = "OntimeNotifs";
    public static String COLLECTION_REMINDER = "Reminders";
    public static String COLLECTION_MY_DEVICES="MyDevices";
    public static String COLLECTION_MY_USERS="my-users";
    public static String COLLECTION_MY_USERS_DEVICES="my-users-devices";
    public static String COLLECTION_BUDGET = "Budgets";
    public static String COLLECTION_MY_SETTINGS = "MySettings";

    Context context;
    String dbUserId = "";
    String deviceID="";
    FirebaseFirestore db;
    GregorianCalendar cal;
    ArrayList<DocumentSnapshot> mArrayList;
    MyBbmApplication bbmApp2;


    public MyFirestoreEditor(Context context) {
        this.context = context;

        bbmApp2=  new MyBbmApplication().getInstance();

        dbUserId = getUserID() /*+ new BBMinstallation().id(context)*/;

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        mArrayList = new ArrayList<>();

    }


    protected String getID(String collectionName) {

        String basicRef = getBasicRef();
        CollectionReference collectionRef = db.collection(basicRef + collectionName);

        String id = collectionRef.getId();

        return id;
    }


    public CollectionReference getCollectionRef(String collectionName) {

        String basicRef = getBasicRef();
        CollectionReference collectionRef = db.collection(basicRef + collectionName);

        return collectionRef;
    }


    public Query getQueryFromCollection(String collectionName, int LIMIT) {

        String basicRef = getBasicRef();
        Log.e("MyFirEditor","this is query text before->"+basicRef+collectionName);
        Query q = db.collection(basicRef + collectionName)
                //.orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT);

        Log.e("MyFirEditor","this is query text->"+q.toString());
        return q;


    }


    public Query getQueryForNotifParm(String collectionName,String idCat,String idBbm,Long dat1,
                                      long dat2,String status,String sens,boolean paidMatters,
                                      boolean paid,String sortBy,String sortType,int LIMIT) {

        String basicRef = getBasicRef();
        Log.e("MyFirEditor","this is query text before->"+basicRef+collectionName);
        Query q = db.collection(basicRef + collectionName)
                //.orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT);


        if(dat1>0){

            q=q.whereGreaterThanOrEqualTo(AllStaticKt.getCOL_NAME_TIMESTART(),dat1);

        }

        if(dat2>0){

            q=q.whereLessThanOrEqualTo(AllStaticKt.getCOL_NAME_TIMESTART(),dat2);
        }

        if(AllStaticKt.isNotEmpty(idCat)){

            q=q.whereEqualTo(AllStaticKt.getCOL_NAME_ID_CAT(),idCat);
        }

        if(AllStaticKt.isNotEmpty(idBbm)){

            q=q.whereEqualTo(AllStaticKt.getCOL_NAME_ID_STATE(),idBbm);
        }

        if(AllStaticKt.isNotEmpty(status)){

            q=q.whereEqualTo(AllStaticKt.getCOL_NAME_CURRENT_STATE(),status);
        }

        if(AllStaticKt.isNotEmpty(sens)){

            q=q.whereEqualTo(AllStaticKt.getCOL_NAME_SENS(),sens);
        }

        if(paidMatters){

            q=q.whereEqualTo(AllStaticKt.getCOL_NAME_PAID_OP(),paid);
        }

        Log.e("MyFirEditor","this is query text->"+q.toString());
        return q;


    }





    public Task<QuerySnapshot> getListNotifWithParam(String collectionName,String idCat,String idBbm,Long dat1,
                                                     long dat2,String status,String sens,boolean paidMatters,
                                                     boolean paid,String sortBy,String sortType,int LIMIT, final onActionDone listner) {
        //Log.e("getListItems","getListItems this is cat list from firestore->"+colRef);
        mArrayList=new ArrayList<>();
        Query q =getQueryForNotifParm(collectionName,idCat,idBbm,dat1,dat2,status,sens,paidMatters,paid,sortBy,sortType,LIMIT);

        return q.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "getListItems onSuccess: LIST EMPTY");
                            return;
                        } else {

                            Log.d(TAG, "getListItems onSuccess: " + documentSnapshots.size());
                            if(listner!=null) listner.onListDone(documentSnapshots);
                            return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "getListItems Error getting data!!!", Toast.LENGTH_LONG).show();
                        if(listner!=null) listner.onListDone(null);
                    }
                });

    }

    public String getBasicRef() {

        return AllStaticKt.getDB_NAME() + "/" + dbUserId + "/";
    }

    public CollectionReference getUserCollectionRef() {


        CollectionReference collectionRef = db.collection(COLLECTION_MY_USERS);

        return collectionRef;
    }


    public CollectionReference getUserDevicesCollectionRef() {


        CollectionReference collectionRef = db.collection(COLLECTION_MY_USERS_DEVICES);

        return collectionRef;
    }

    protected String addData(String collectionName, Object data, String id) {

        String basicRef = getBasicRef();
        CollectionReference collectionRef = db.collection(basicRef + collectionName);

        if (id == null || id.isEmpty()) id = getID(collectionName);

        collectionRef.document(id).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


        return id;
    }

    protected String addDataInBatch(WriteBatch batch, String collectionName, Object data, String id) {

        try {


            DocumentReference ref = getCollectionRef(collectionName).document();

            if (id == null || id.isEmpty()) id = ref.getId();


            // Add data
            batch.set(ref, data);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return id;
    }

    @SuppressLint("MissingPermission")
    private String getDeviceID() {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }

    private String getUserID() {

        //Save credentials to pref
        SharedPreferences pref=context.getSharedPreferences(AllStaticKt.getPREF_FOR_DATA_PATH(), Context.MODE_PRIVATE);

        deviceID=pref.getString(AllStaticKt.getDEVICE_ID(),"anonymous");

        return pref.getString(AllStaticKt.getROOT_USER_PATH(),"anonymous");
    }


    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    private void addReminderMatching(WriteBatch batch,OnTimeNotificationFB onTno,ArrayList<String> reminderConfList){

        GregorianCalendar cal2 = new GregorianCalendar();


        for (String buf : reminderConfList) {

            String[] tab = buf.split("-");
            OnTimeNotificationFB bufRem = MyJsonParserKotlin.Companion.ReminderFromOntimeNotif(onTno, null);


            if (tab.length < 2) continue;

            String val = "-" + Integer.valueOf(tab[1]);

                        /*/Log.e("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  " +
                                "saveState pos 2 avec reminder->"+buf);*/


            cal2.setTimeInMillis(onTno.getTimestart());
            switch (Integer.valueOf(tab[0])) {

                case Calendar.MINUTE:
                    cal2.add(Calendar.MINUTE, Integer.valueOf(val));
                    break;

                case Calendar.HOUR:
                    cal2.add(Calendar.HOUR, Integer.valueOf(val));
                    break;

                case Calendar.DAY_OF_YEAR:
                    cal2.add(Calendar.DAY_OF_YEAR, Integer.valueOf(val));
                    break;

                case Calendar.WEEK_OF_YEAR:
                    cal2.add(Calendar.WEEK_OF_YEAR, Integer.valueOf(val));
                    break;

                case Calendar.MONTH:
                    cal2.add(Calendar.MONTH, Integer.valueOf(val));
                    break;

                case Calendar.YEAR:
                    cal2.add(Calendar.YEAR, Integer.valueOf(val));
                    break;
            }

            if (cal2.getTimeInMillis() <= System.currentTimeMillis()) continue;

            bufRem.setTimestart(cal2.getTimeInMillis());

            ////Log.e("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  Insert OnTimeNotificationFB avec date->"+cal2.getTime()+"< et la dateNotif->"+new Date(onTno.getTimestart()));

            DocumentReference refRem = getCollectionRef(MyFirestoreEditor.COLLECTION_REMINDER).document();

            if (bufRem.getId() == null || bufRem.getId().isEmpty())
                bufRem.setId(refRem.getId());


            // Add BBMstatement
            batch.set(refRem, bufRem);

        }
    }

    private BbmStatementFB insertNotifAndReminderFB(WriteBatch batch, BbmStatementFB bbmState, ArrayList<Long> timesNotif,
                                                    ArrayList<String> reminderConfList,boolean updateOne, OnTimeNotificationFB bufToUpdate,boolean allNew) {

        try {


            long newDateEnd = 0;
            int tail = timesNotif.size();

            if(updateOne){

                DocumentReference ref = getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF).document(bufToUpdate.getId());

                // Add BBMstatement
                batch.set(ref, bufToUpdate);

                if (reminderConfList != null && reminderConfList.size() > 0) {

                    addReminderMatching(batch,bufToUpdate,reminderConfList);
                }

            }
            else{

                if(!allNew){

                    deleteOldNotif(COLLECTION_ONTIMENOTIF,"",bbmState.getId(),0L,0,"","",true,false);
                }

                for (int i = 0; i < tail; i++) {
                    cal.setTimeInMillis(timesNotif.get(i));
                    OnTimeNotificationFB onTno = MyJsonParserKotlin.Companion.onTimeNofificationFromBbmState(bbmState, null);


                    onTno.setDatestart(cal.getTimeInMillis());
                    onTno.setTimestart(cal.getTimeInMillis());
                    onTno.setTimeend(cal.getTimeInMillis());
                    onTno.setDateprojecte(cal.getTimeInMillis());

                    //.withValue(OnTimeNotificationOld.COL_CURRENT_STATE, values.getAsInteger(BbmStatementOld.COL_CURRENT_STATE))


                    DocumentReference ref = getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF).document();

                    if (onTno.getId() == null || onTno.getId().isEmpty()) onTno.setId(ref.getId());


                    // Add BBMstatement
                    batch.set(ref, onTno);

                    //Pour une notif correspont tjrs un remider
                    //Insert les reminders if present
                    if (reminderConfList != null && reminderConfList.size() > 0) {

                        addReminderMatching(batch,onTno,reminderConfList);
                    }

                    OnTimeNotificationFB remCorrespond = MyJsonParserKotlin.Companion.ReminderFromOntimeNotif(onTno, null);
                    remCorrespond.setId(onTno.getId());
                    remCorrespond.setCurrentstate(onTno.getCurrentstate());
                    remCorrespond.setIsactualnotif(true);
                    remCorrespond.setTimestart(onTno.getTimestart());


                    DocumentReference refRem = getCollectionRef(MyFirestoreEditor.COLLECTION_REMINDER).document();

                    if (remCorrespond.getId() == null || remCorrespond.getId().isEmpty())
                        remCorrespond.setId(refRem.getId());


                    // Add BBMstatement
                    batch.set(refRem, remCorrespond);


                }
            }

            bbmState.setTimeend(newDateEnd);
            bbmState.setDateend(newDateEnd);


            DocumentReference refUpdate = getCollectionRef(MyFirestoreEditor.COLLECTION_REMINDER).document(bbmState.getId());


            // Add BBMstatement
            batch.set(refUpdate, bbmState);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return bbmState;
    }


    private void updateNotifInBatch(WriteBatch batch, ArrayList<OnTimeNotificationFB> list) {

        try {


            long newDateEnd = 0;
            int tail = list.size();

            for (int i = 0; i < tail; i++) {

                OnTimeNotificationFB onTno = list.get(i);

                if(onTno==null || onTno.getId()==null || onTno.getId().isEmpty()) continue;

                DocumentReference ref = getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF).document(onTno.getId());


                // Add BBMstatement
                batch.set(ref, onTno);


            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    public Task<Void> updateListNotif(ArrayList<OnTimeNotificationFB> list, OnCompleteListener listener) {

        BbmStatementFB result = null;

        try {


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 1 and currentstate->"+bbmState.getCurrentstate());

            // Add a bunch of random restaurants
            WriteBatch batch = getDb().batch();

            updateNotifInBatch(batch,list);



            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }

    public Task<Void> addBBMandNotif(final BbmStatementFB bbmState, final JSONObject jsObj, OnCompleteListener listener,
                                     boolean updateOne,OnTimeNotificationFB notifUpdate,boolean isAllNew) {

        BbmStatementFB result = null;

        try {

            ArrayList<Long> timesNotif = MyJsonParser.jsonArrayToArrayList(jsObj.getJSONArray("listNotif"));


            ArrayList<String> reminderConfList = MyJsonParser.jsonArrayToArrayReminder(jsObj.getJSONArray("listReminder"));


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 1 and currentstate->"+bbmState.getCurrentstate());

            // Add a bunch of random restaurants
            WriteBatch batch = getDb().batch();

            DocumentReference statRef;

            if (bbmState.getId() == null || bbmState.getId().isEmpty()){

                statRef = getCollectionRef(MyFirestoreEditor.COLLECTION_STATEMENT).document();
                bbmState.setId(statRef.getId());
            }
            else{

                statRef = getCollectionRef(MyFirestoreEditor.COLLECTION_STATEMENT).document(bbmState.getId());

            }




            // Add BBMstatement
            batch.set(statRef, bbmState);

            result = insertNotifAndReminderFB(batch, bbmState, timesNotif, reminderConfList,updateOne,notifUpdate,isAllNew);



            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }


    public Task<Void> addRecord(final DocumentReference docRef, final Object object, OnCompleteListener listener) {


        try {

            WriteBatch batch = getDb().batch();


            // Add Record
            batch.set(docRef, object);


            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            Log.e("MyFirestoreEditor", "addRecord Error on add record");
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }



    public Task<Void> saveInitParam(String id,Object data, OnCompleteListener listener,boolean newInit,Map<String ,Object> dataToUpdate) {


        try {


                DocumentReference ref=getUserDevicesCollectionRef().document(id);



            WriteBatch batch = getDb().batch();



            // Add Record
            if(newInit){

                batch.set(ref, data);
            }
            else{

                batch.update(ref, dataToUpdate);
            }


            /*DocumentReference refDD=ref.collection(COLLECTION_MY_DEVICES).document(idD);

            batch.set(refDD,dataD);*/

            Log.e("MyAlarmService", "MyAlarmService saveInitParam  saveState pos 4_nbre Notif simpe->_Notif avecJoin->");

            if(listener!=null){
                return batch.commit().addOnCompleteListener(listener);
            }
            else{
                return batch.commit();
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }

    public Task<Void> updateData(final DocumentReference docRef, Map<String,Object> updates, OnCompleteListener listener) {


        try {

            WriteBatch batch = getDb().batch();

            // Add Record
            batch.update(docRef, updates);


            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }



    public void payNotif(OnTimeNotificationFB notif){

        if(notif==null) return;

        Map<String,Object> values=new HashMap<>();
        values.put(AllStaticKt.getCOL_NAME_PAID_OP(),true);
        values.put(AllStaticKt.getCOL_NAME_MONTTANT_OP(),notif.getMontantprojecte());
        values.put(AllStaticKt.getCOL_NAME_CURRENT_STATE(),context.getString(R.string.current_state_done));
        values.put(AllStaticKt.getCOL_NAME_DATE_REEL(),System.currentTimeMillis());
        values.put(AllStaticKt.getCOL_NAME_DATEOP(),System.currentTimeMillis());

        DocumentReference docRef=getCollectionRef(COLLECTION_ONTIMENOTIF).document(notif.getId());

        OnCompleteListener listener=new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isComplete()){

                    if(task.isSuccessful()){

                        Toast.makeText(context, context.getString(R.string.op_completed), Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(context, context.getString(R.string.op_failed), Toast.LENGTH_LONG).show();

                    }
                }
            }
        };


        updateData(docRef,values,listener);
    }



    public void cancelNotif(OnTimeNotificationFB notif){

        if(notif==null) return;

        Map<String,Object> values=new HashMap<>();

        values.put(AllStaticKt.getCOL_NAME_CURRENT_STATE(),context.getString(R.string.current_state_canceled));
        values.put(AllStaticKt.getCOL_NAME_DATE_REEL(),System.currentTimeMillis());
        values.put(AllStaticKt.getCOL_NAME_DATEOP(),System.currentTimeMillis());

        DocumentReference docRef=getCollectionRef(COLLECTION_ONTIMENOTIF).document(notif.getId());

        OnCompleteListener listener=new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isComplete()){

                    if(task.isSuccessful()){

                        Toast.makeText(context, context.getString(R.string.op_completed), Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(context, context.getString(R.string.op_failed), Toast.LENGTH_LONG).show();

                    }
                }
            }
        };


        updateData(docRef,values,listener);
    }


    public Task<Void> updateObject(final DocumentReference docRef, Object object, OnCompleteListener listener) {


        try {

            WriteBatch batch = getDb().batch();

            // Add Record
            batch.set(docRef, object);


            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }

    public Task<Void> deleteRecord(final DocumentReference docRef, OnCompleteListener listener) {


        try {

            WriteBatch batch = getDb().batch();


            // delete Record
            batch.delete(docRef);


            return batch.commit().addOnCompleteListener(listener);


            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }

    public Task<Void> deleteAllRecord(String collectionRef,ArrayList<DocumentSnapshot> list, OnCompleteListener listener) {


        try {

            //Log.e("MyFirestoreEditor", "deleteAllRecord this is pos 1->");


            if(list!=null){

                //Log.e("MyFirestoreEditor", "deleteAllRecord this is pos 2 and list->"+list);

                WriteBatch batch = getDb().batch();

                for(DocumentSnapshot snap:list){

                    Log.e("MyFirestoreEditor", "deleteAllRecord this is pos 2 and eltID->"+snap.getId());
                    //DocumentReference ref=snap.getReference();
                    DocumentReference ref=getCollectionRef(collectionRef).document(snap.getId());

                    // delete Record
                    batch.delete(ref);
                }

                if(listener!=null){

                    return batch.commit().addOnCompleteListener(listener);
                }
                else{
                    return batch.commit();
                }
            }




            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }






    public void deleteNotif(OnTimeNotificationFB notif){

        if(notif==null) return;



        DocumentReference docRef=getCollectionRef(COLLECTION_ONTIMENOTIF).document(notif.getId());

        OnCompleteListener listener=new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isComplete()){

                    if(task.isSuccessful()){

                        Toast.makeText(context, context.getString(R.string.op_completed), Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(context, context.getString(R.string.op_failed), Toast.LENGTH_LONG).show();

                    }
                }
            }
        };



        deleteRecord(docRef,listener);
    }

    public Task<QuerySnapshot> getListItems(String colRef, final onActionDone listner) {
        //Log.e("getListItems","getListItems this is cat list from firestore->"+colRef);
        mArrayList=new ArrayList<>();
        return db.collection(getBasicRef()+colRef).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "getListItems onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            // List<Type> types = documentSnapshots.toObjects(Type.class);

                            // Add all to your list
                           // mArrayList.addAll(mArrayList);
                            Log.d(TAG, "getListItems onSuccess: " + documentSnapshots.size());
                            if(listner!=null) listner.onListDone(documentSnapshots);
                            return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "getListItems Error getting data!!!", Toast.LENGTH_LONG).show();
                        if(listner!=null) listner.onListDone(null);
                    }
                });

    }




    public Task<QuerySnapshot> getListItems(Query q, final onActionDone listner) {
        //Log.e("getListItems","getListItems this is cat list from firestore->"+colRef);
        mArrayList=new ArrayList<>();
        return q.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "getListItems onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            // List<Type> types = documentSnapshots.toObjects(Type.class);

                            // Add all to your list
                            // mArrayList.addAll(mArrayList);
                            Log.d(TAG, "getListItems onSuccess: " + documentSnapshots.size());
                            if(listner!=null) listner.onListDone(documentSnapshots);
                            return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "getListItems Error getting data!!!", Toast.LENGTH_LONG).show();
                        if(listner!=null) listner.onListDone(null);
                    }
                });

    }



    public void deleteOldNotif(String collectionName,String idCat,String idBbm,Long dat1,
                               long dat2,String status,String sens,boolean paidMatters,
                               boolean paid){

        onActionDone listner=new onActionDone() {
            @Override
            public void onListDone(@Nullable QuerySnapshot list) {

                if(list!=null){

                    deleteAllRecord(COLLECTION_ONTIMENOTIF,(ArrayList<DocumentSnapshot>) list.getDocuments(),null);
                }
            }
        };

        getListItems(getQueryForNotifParm(collectionName,idCat,idBbm,dat1,dat2,status,sens,paidMatters,paid,"","",500),listner);
    }






    public void cancelNotification(){

        try{

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // Will display the notification in the notification bar
            notificationManager.cancelAll();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public  int getIdAlarmForADay(String idStat){


        int res=9999999;

        /*MySettings myS=null;//=getAlarmForADay(idStat);

        if(myS!=null){

            res= Integer.parseInt(myS.getVal());
        }*/

        //Log.e("MyBDinteraction","getAlarmForAday voici nbre retour->"+res);


        return res;
    }


    public PendingIntent getPendingFromOnTimeNofif(OnTimeNotificationFB reminder){


        Calendar calDayInYear=Calendar.getInstance();
        OnTimeNotificationFB rem=MyJsonParser.getReminderPrepaForAlarmFB(reminder,calDayInYear.get(Calendar.DAY_OF_YEAR),context);



        StringBuffer allVal = new StringBuffer();

        allVal.append("_ID Statement->");
        String idStat=rem.getIdstate();
        String nom=rem.getNom();
        String descrip= rem.getDescrip();
        double montant=rem.getMontantprojecte();
        long timeStart=rem.getTimestart();
        double pu=rem.getPuop();
        int qnte=rem.getQnteop();
        String sens=rem.getSens();
        int idAlarm =rem.getIdalarm();




        allVal.append(idStat);

        allVal.append("_IS REPEATING EVENT->");
        allVal.append(rem.getRecurring());

        allVal.append("_nom Statement->");
        allVal.append(nom);

        allVal.append("_Descrip Statement->");
        allVal.append(descrip);

        allVal.append("_iD Notif->");
        allVal.append(idStat);



        allVal.append("_Match Notif->");
        allVal.append(rem.getIsactualnotif());

        allVal.append("_Time notif->");
        //allVal.append(c.getString(c.getColumnIndexOrThrow("timestartNotif")));
        cal.setTimeInMillis(timeStart);
        allVal.append(cal.getTime().toString());



        Intent i = new Intent(context, MyAlarmReceiverOnTime.class);
        String idRem=rem.getId();
        i.putExtra("idStat", idStat);
        i.putExtra("idAlarm",idAlarm);
       // i.setAction(MyAlarmService.CREATE);
        i.putExtra("idReminder", idRem);
        i.putExtra("theContent",allVal.toString());
        i.putExtra("name", nom);
        i.putExtra("msg",descrip);
        i.putExtra("montant", montant);
        i.putExtra("timeNotif",timeStart);
        i.putExtra("typeStat",rem.getType());
        i.putExtra("renew", "false");
        i.putExtra("isReminder",rem.getIsactualnotif());
        i.putExtra("isRepeating",rem.getRecurring());
        //i.putExtra("idAlarmSet",idAlarm);





        allVal.append("_New IDalarm->");
        allVal.append(idAlarm);

        allVal.append("_Time Alarm->");
        allVal.append(timeStart);

        allVal.append("_DateAlarm->");
        allVal.append(new Date(timeStart));

        //Log.e("MyAlarmService", "MyAlarmService updateAlarms After info->" + allVal.toString());


        PendingIntent pi = PendingIntent.getBroadcast(context, idAlarm, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pi;
    }


    /**
     * updateAlarms the purpose of this is to take off any alarm set from the system
     * @param listAlarm
     * @param action
     * @return
     */
    public void updateAlarmsNew(List<OnTimeNotificationFB> listAlarm, String action, boolean cancelAll){


        ArrayList<String> resultIdSet=null;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //BbmStatementContentProvider bbmContProv=new BbmStatementContentProvider();




        if(listAlarm!=null){

            long timeStart=System.currentTimeMillis();
            int nbreNotif=listAlarm.size();

            //Log.e("MyAlarmService", "MyAlarmService updateAlarms pos 1 avec action->" + action + " AND cursor nbre->" + listAlarm.size());

            Iterator<OnTimeNotificationFB> itera=listAlarm.iterator();



            if(itera.hasNext()) {

                resultIdSet=new ArrayList<>();
                Calendar cal=new GregorianCalendar();

                while (itera.hasNext() ) {


                    OnTimeNotificationFB rem=itera.next();

                    timeStart=rem.getTimestart();
                    PendingIntent pi = getPendingFromOnTimeNofif(rem);


                    if (CREATE.equals(action)) {

                        try {


                            am.set(AlarmManager.RTC_WAKEUP, timeStart, pi);
                            /*/Log.e("MyAlarmService", "updateAlarms Alarm Set pour->" +rem.getOnTimeNotification().getBbmStatement().getNom()
                                    +" IDalarm->"+rem.getIdalarm()+"_At Time->"+new Date(timeStart));*/


                            /*if(onTn.getBbmStatement().getRecurring()){

                                am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeStart,
                                        UIeventManager.getRepeatInterval(onTn.getBbmStatement().getRepeatunit(),onTn.getBbmStatement().getRepeatfreq()),
                                        pi);

                                //Log.e("MyAlarmService", "updateAlarms Alarm REPETITIVE Set pour->" +nom+" IDalarm->"+idAlarm+"_At Time->"+new Date(timeStart));

                            }
                            else{
                                am.set(AlarmManager.RTC_WAKEUP, timeStart, pi);
                                //Log.e("MyAlarmService", "updateAlarms Alarm Set pour->" +nom+" IDalarm->"+idAlarm+"_At Time->"+new Date(timeStart));

                            }*/

                            rem.setAlarmset(true);
                            //remDao.update(rem);

                            resultIdSet.add(rem.getId());

                        }
                        catch (Exception e){

                        }



                    } else if (CANCEL.equals(action)) { /*On cancel une alarm uniquement si elle n'est pas repetitive ou si
                    c'est le dernier de la liste de repetition.*/

                        try{

                            am.cancel(pi);

                            /*Log.e("MyAlarmService","updateAlarm avec canceALl->"+cancelAll+"_isNotifMatch->"+rem.getIsnotifmatch());
                            resultIdSet.add(rem.getId());
                            if(!cancelAll && !rem.getIsnotifmatch()){//recurrent, on check si c'est le dernier

                                long res=checkSiDernierReminder(rem);
                                Log.e("MyAlarmService","updateAlarm avec res->"+res);
                                if(res==0){

                                    am.cancel(pi);

                                    updateAlarmsNew(getListNotifications3(null, null, true, false, rem.getTimestart(),
                                            0, getString(R.string.current_state_running), true, MyAlarmService.LIMIT_NOTIFICATONS_TO_HANDLE),
                                            CREATE,false);
                                }
                                else if(res>0){//si pas le dernier, on set l'etat du next sur la liste

                                    //on doit changer le alarmSet du next dans la liste
                                    //Si et seulement si il n'existe pas deja un next avec deja le alarmSet=true;

                                    rem=remDao.loadDeep(res);

                                    Log.e("MyAlarmService","updateAlarm avec rem from Load->"+rem);
                                    if(rem!=null){

                                        pi = getPendingFromReminder(rem);
                                        am.set(AlarmManager.RTC_WAKEUP, rem.getTimestart(), pi);
                                        Log.e("MyAlarmService", "updateAlarms next Alarm set pour->" +rem.getOnTimeNotification().getBbmStatement().getNom()
                                                +" IDalarm->"+rem.getIdalarm()+"_At Time->"+new Date(rem.getTimestart()));

                                        rem.setAlarmset(true);
                                        remDao.update(rem);

                                    }

                                    //updateNotificationAlarmSet(list,true);
                                }
                                else{//rem <0

                                    Log.e("MyAlarmService","updateAlarm avec rem <0******************************");
                                }

                            }
                            else{//non recurrent, on cancel tout de suite
                                am.cancel(pi);
                                Log.e("MyAlarmService", "updateAlarms Alarm cancel pour->" +rem.getOnTimeNotification().getBbmStatement().getNom()
                                        +" IDalarm->"+rem.getIdalarm()+"_At Time->"+new Date(timeStart));
                                updateAlarmsNew(getListNotifications3(null, null, true, false, rem.getTimestart(),
                                        0, getString(R.string.current_state_running), true, MyAlarmService.LIMIT_NOTIFICATONS_TO_HANDLE),
                                        CREATE,false);

                            }

                            //bbmApp.updateNbreNotifsHandle(-1);
                            ////Log.i("MyAlarmService", "updateAlarms Alarm Cancel->" + nom+" IDalarm->"+idAlarm);

                            */

                        }
                        catch (Exception e){

                        }
                    }
                }

                updateListNotif((ArrayList<OnTimeNotificationFB>) listAlarm, new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {

                    }
                });

            }

        }

        //return resultIdSet;

    }


    public Task<Void> excuteOnDB(excuteOnDBParam param) {


        try {

            if(param==null) return null;

            if(param.isFromNotif()){

                cancelNotification();
                //new MyDBinteraction(getApplicationContext()).setNotifSend(0,0);
                //todo uncomment line below
               // bbmApp2.saveNotifSendAndIdInPref("0","0");
                //notifOrg.doAction(""+MyNotifOrganizer.ACTION_SET_NOTIF_SEND,"0","0");

                ////Log.e("Accueil2Activity","AcceuilActivity onCreate activity call from a notification");
                //bbmApp.setInstanceNotifsSend(0,0);

            }


            //todo uncommment line below
            //long timeStart=bbmApp2.getPrefDateLastNotif();
            long timeStart=System.currentTimeMillis();

            timeStart=(param.timeStart>0)?param.timeStart:timeStart;
            long timeEnd=(param.timeEnd>0)?param.timeEnd: timeStart + 86400000;


            WriteBatch batch = getDb().batch();

            boolean updateALarmAtEnd=false;

            switch (param.callerEvent) {

                case CALLER_EVENT_REBOOT:

                    //bufUri = Uri.parse(BbmStatementContentProvider.CONTENT_URI_NOTIFICATIONS+"/notif");
                    //select=" "+ OnTimeNotificationFB.COL_TIMESTART+">= ?"+" AND "+OnTimeNotificationFB.COL_RUNNING+"= ?";


                    updateALarmAtEnd = true;

                    break;


                case CALLER_EVENT_REFILL:

                    //bufUri = Uri.parse(BbmStatementContentProvider.CONTENT_URI_NOTIFICATIONS+"/notif");
                    //select=" "+ OnTimeNotificationFB.COL_TIMESTART+">= ?"+" AND "+OnTimeNotificationFB.COL_RUNNING+"= ?";


                    updateALarmAtEnd = true;

                    break;


                case CALLER_EVENT_NEW:

                    ////Log.i("MyAlarmService", "MyAlarmService execute CALLER_EVENT_NEW->" + action );


                    try {

                        return addBBMandNotif(param.bbmState,param.jsObj,param.completeListener,false,null,true);


                    } catch (Exception e) {

                        ////Log.i("MyAlarmService", "MyAlarmService execute Exception sur JSON->" +e.getMessage());
                    }


                    updateALarmAtEnd = true;
                    //resetNbreRefil=true;

                    break;



                case CALLER_EVENT_UPDATE:

                    ////Log.i("MyAlarmService", "MyAlarmService execute CALLER_EVENT_UPDATE->" + action );


                    /*curTime=Long.toString(System.currentTimeMillis());

                    BbmStatement bbmStat=MyJsonParser.stringToBbmStatement(theContent);
                    /******Avant de les delete, se rassurer d'avoir annuler leur effet dans AlarmManager.******/

                    /*updateAlarmsNew(getListNotifications3(null,bbmStat.getId(),true,true,timeStart,timeEnd,getString(R.string.current_state_running),false,1),CANCEL,true);


                    ////Log.i("MyAlarmService", "MyAlarmService execute CALLER_EVENT_UPDATE apres cancel alarm nbre->" + notifIdList );

                    /******Mainteneant on peut delete les anciens.******/

                    /*transactionDeleteStatementNotif(null,null,bbmStat.getId(),false);

                    ////Log.i("MyAlarmService", "MyAlarmService execute CALLER_EVENT_UPDATE apres delete ->" );

                    try{

                        JSONObject jsObj=new JSONObject(theContent);

                        System.out.print("MyAlarmService execeute voici json->" + jsObj.toString());
                        ////Log.i("MyAlarmService", "MyAlarmService execute pos 0 avec action->" + action + " AND voici json->" + jsObj.toString());

                        ArrayList<String> reminderConfList=MyJsonParser.jsonArrayToArrayReminder(jsObj.getJSONArray("listReminder"));

                        transactionUpdateStatementNotif(bbmStat,
                                MyJsonParser.jsonArrayToArrayList(jsObj.getJSONArray("listNotif")),reminderConfList,true);




                    }
                    catch (Exception e){

                        ////Log.i("MyAlarmService", "MyAlarmService execute Exception sur JSON->" +e.getMessage());
                    }*/

                    updateALarmAtEnd=true;
                    break;


            }





            ////Log.i("MyAlarmService", "MyAlarmService transactionInsertStatementNotif  saveState pos 4_nbre Notif simpe->"+nbreSansJoin+"_Notif avecJoin->"+listBuf.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //daoSession.getDatabase().endTransaction();
        }

        return null;
    }

    public void manageAutoPay() {

        try{

            onActionDone listner=new onActionDone() {
                @Override
                public void onListDone(@Nullable QuerySnapshot list) {

                    if(list!=null){

                        for(DocumentSnapshot doc:  list.getDocuments()){

                            payNotif(doc.toObject(OnTimeNotificationFB.class));
                        }

                    }
                }
            };


            Query q=getCollectionRef(COLLECTION_ONTIMENOTIF).whereLessThanOrEqualTo("datestart",System.currentTimeMillis())
                    .whereEqualTo(AllStaticKt.getCOL_NAME_IS_AUTO_PAY(),true)
                    .whereEqualTo(AllStaticKt.getCOL_NAME_PAID_OP(),false)
                    .whereEqualTo(AllStaticKt.getCOL_NAME_IS_ACTUAL_NOTIF(),true);


            getListItems(q,listner);




        }
        catch (Exception ex){

            ex.printStackTrace();
        }
    }


    public class excuteOnDBParam{

        int callerEvent=0;
        String idRem="",idAlarm="";
        boolean isFromNotif=false;
        String collectionRef;
        long timeStart=0,timeEnd=0;
        DocumentReference docRef;
        String docID;
        OnCompleteListener completeListener;
        onActionDone actionDoneListner;
        Map<String,Object> updates;
        JSONObject jsObj;
        BbmStatementFB bbmState;
        ArrayList<Long> timesNotif;
        ArrayList<String> reminderConfList;

        public excuteOnDBParam(int callerEvent, String collectionRef, DocumentReference docRef) {
            this.callerEvent = callerEvent;
            this.collectionRef = collectionRef;
            this.docRef = docRef;
        }

        public int getCallerEvent() {
            return callerEvent;
        }

        public void setCallerEvent(int callerEvent) {
            this.callerEvent = callerEvent;
        }


        public String getIdRem() {
            return idRem;
        }

        public void setIdRem(String idRem) {
            this.idRem = idRem;
        }

        public long getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(long timeStart) {
            this.timeStart = timeStart;
        }

        public long getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(long timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getIdAlarm() {
            return idAlarm;
        }

        public void setIdAlarm(String idAlarm) {
            this.idAlarm = idAlarm;
        }

        public boolean isFromNotif() {
            return isFromNotif;
        }

        public void setFromNotif(boolean fromNotif) {
            isFromNotif = fromNotif;
        }

        public String getCollectionRef() {
            return collectionRef;
        }

        public void setCollectionRef(String collectionRef) {
            this.collectionRef = collectionRef;
        }

        public DocumentReference getDocRef() {
            return docRef;
        }

        public void setDocRef(DocumentReference docRef) {
            this.docRef = docRef;
        }

        public String getDocID() {
            return docID;
        }

        public void setDocID(String docID) {
            this.docID = docID;
        }

        public OnCompleteListener getCompleteListener() {
            return completeListener;
        }

        public void setCompleteListener(OnCompleteListener completeListener) {
            this.completeListener = completeListener;
        }

        public onActionDone getActionDoneListner() {
            return actionDoneListner;
        }

        public void setActionDoneListner(onActionDone actionDoneListner) {
            this.actionDoneListner = actionDoneListner;
        }

        public Map<String, Object> getUpdates() {
            return updates;
        }

        public void setUpdates(Map<String, Object> updates) {
            this.updates = updates;
        }

        public JSONObject getJsObj() {
            return jsObj;
        }

        public void setJsObj(JSONObject jsObj) {
            this.jsObj = jsObj;
        }

        public BbmStatementFB getBbmState() {
            return bbmState;
        }

        public void setBbmState(BbmStatementFB bbmState) {
            this.bbmState = bbmState;
        }

        public ArrayList<Long> getTimesNotif() {
            return timesNotif;
        }

        public void setTimesNotif(ArrayList<Long> timesNotif) {
            this.timesNotif = timesNotif;
        }

        public ArrayList<String> getReminderConfList() {
            return reminderConfList;
        }

        public void setReminderConfList(ArrayList<String> reminderConfList) {
            this.reminderConfList = reminderConfList;
        }
    }
}
