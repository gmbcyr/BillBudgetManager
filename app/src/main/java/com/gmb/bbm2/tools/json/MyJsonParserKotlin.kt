package com.gmb.bbm2.tools.json

import android.content.Context
import com.gmb.bbm2.R
import com.gmb.bbm2.data.model.BbmStatementFB
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.tools.utils.UIeventManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.util.*

/**
 * Created by GMB on 3/15/2018.
 */
class MyJsonParserKotlin {




    companion object {


        fun stringToOnTimeNotificationFB(json: String): OnTimeNotificationFB {

            var buf = OnTimeNotificationFB()



            try {

                /*val jsOb = JSONObject(json)

                try {
                    buf.id = jsOb.getString(COL_NAME_ID)
                } catch (ex: Exception) {
                    println("MyJsonParser stringToOperations parseID error->" + ex.message)
                }

                buf.dateop = jsOb.getLong(COL_NAME_DATEOP)
                buf.dateprojecte = jsOb.getLong(COL_NAME_DATE_PROJECTE)
                buf.datereel = jsOb.getLong(COL_NAME_DATE_REEL)
                buf.montantop = jsOb.getDouble(COL_NAME_MONTTANT_OP)
                buf.montantprojecte = jsOb.getDouble(COL_NAME_MONTANT_PROJECTE)
                buf.puop = jsOb.getDouble(COL_NAME_PUOP)
                buf.qnteop = jsOb.getInt(COL_NAME_QNTEOP)
                buf.paidop = jsOb.getString(COL_NAME_PAID_OP).equals("1", ignoreCase = true)
                buf.penalite = jsOb.getDouble(COL_NAME_PENALITE)
                buf.totalop = jsOb.getDouble(COL_NAME_TOTAL_OP)

                buf.idstate = jsOb.getString(COL_NAME_ID_STATE)*/

                val gson = Gson()

                buf  = gson.fromJson(json, OnTimeNotificationFB::class.java)

            } catch (e: Exception) {

                println("MyJsonParser stringToOperations error->" + e.message)
            }




            return buf
        }


        fun onTimeNotifToJsonString(buf: OnTimeNotificationFB): String {

            var jsonObject:String?=null


            //JSONParser jsonParser = new JSONParser();

            try {

                val gson = GsonBuilder().setPrettyPrinting().create()

                jsonObject = gson.toJson(buf)

                /*jsonObject.put(COL_NAME_ID, buf.id)
                jsonObject.put(COL_NAME_MONTTANT_OP, buf.montantop)
                jsonObject.put(COL_NAME_DATEOP, buf.dateop)
                jsonObject.put(COL_NAME_TIMESTART, buf.timestart)
                jsonObject.put(COL_NAME_TIME_END, buf.timeend)
                jsonObject.put(COL_NAME_NOM, buf.nom)
                jsonObject.put(COL_NAME_NOM_SYSTEM, buf.nomsystem)
                jsonObject.put(COL_NAME_IS_ACTUAL_NOTIF, buf.actualNotif)
                jsonObject.put(COL_NAME_IS_AUTO_PAY, buf.isautomaticpay)
                jsonObject.put(COL_NAME_SENS, buf.sens)
                jsonObject.put(COL_NAME_PENALITE, buf.penalite)
                jsonObject.put(COL_NAME_PAID_OP, buf.paidop)
                jsonObject.put(COL_NAME_TOTAL_OP, buf.totalop)*/


                // jsonObject.accumulate("statement", values);
            } catch (e: Exception) {

            }

            return jsonObject!!
        }








        fun jsonArrayToArrayList(json: JSONArray?): ArrayList<Long>? {

            var values: ArrayList<Long>? = null



            try {

                if (json != null) {
                    values = ArrayList()

                    val tail = json.length()

                    for (i in 0 until tail) {

                        values.add(json.getLong(i))
                    }
                }


            } catch (e: Exception) {

                println("MyJsonParser jsonArrayToArrayList error->" + e.message)
            }




            return values
        }




        

        fun jsonArrayToArrayReminder(json: JSONArray?): ArrayList<String>? {

            var values: ArrayList<String>? = null



            try {

                if (json != null) {
                    values = ArrayList()

                    val tail = json.length()

                    for (i in 0 until tail) {

                        //Log.e("MyJsonParser"," MyJsonParser jsonArrayToArrayReminder une ligne->"+json.getString(i));

                        values.add(json.getString(i))
                    }
                }


            } catch (e: Exception) {

                println("MyJsonParser jsonArrayToArrayReminder error->" + e.message)
            }




            return values
        }


        public fun onTimeNofificationFromBbmState(bbmState: BbmStatementFB, onTnoParam: OnTimeNotificationFB?): OnTimeNotificationFB {

            val onTno = onTnoParam ?: OnTimeNotificationFB()



            try {

                onTno.currentstate = bbmState.currentstate
                //onTno.setBbmStatement(bbmState);


                onTno.idstate = bbmState.id
                onTno.datestart = bbmState.timestart
                onTno.timestart = bbmState.timestart
                onTno.timeend = bbmState.timestart

                onTno.dateop = 0L
                onTno.dateprojecte = bbmState.timestart
                onTno.datereel = 0L
                onTno.montantop = 0.0
                onTno.montantprojecte = bbmState.montant
                onTno.paidop = false
                onTno.penalite = 0.0
                onTno.totalop = 0.0
                onTno.qnteop = 0
                onTno.puop = 0.0

                onTno.ch1 = ""
                onTno.ch2 = ""
                onTno.ch3 = ""



                onTno.nom = bbmState.nom
                onTno.descrip = bbmState.descrip
                onTno.type = bbmState.type
                onTno.sens = bbmState.sens
                onTno.recurring = bbmState.recurring
                onTno.isautomaticpay = bbmState.isautomaticpay


                onTno.daysofweek = bbmState.daysofweek
                onTno.dayofweekormonth = bbmState.dayofweekormonth
                onTno.dayweekinmonth = bbmState.dayweekinmonth


                onTno.url = bbmState.url
                onTno.unlimited = bbmState.unlimited
                onTno.typeend = bbmState.typeend
                onTno.repeatunit = bbmState.repeatunit
                onTno.repeatfreq = bbmState.repeatfreq
                onTno.nbrerepeat = bbmState.nbrerepeat
                onTno.nomsystem = bbmState.nomsystem
                //onTno.setRecurring((jsOb.getString("running").equalsIgnoreCase("1")?true:false));
                onTno.currentstate = bbmState.currentstate

                onTno.idcat = bbmState.idcat

                onTno.reminderconf = bbmState.reminderconf


            } catch (e: Exception) {

                println("MyJsonParser stringToOperations error->" + e.message)
            }




            return onTno
        }


        fun ReminderFromOntimeNotif(onTnoParam: OnTimeNotificationFB, remind: OnTimeNotificationFB?): OnTimeNotificationFB {

            var buf = remind ?: OnTimeNotificationFB()



            try {

                buf = onTnoParam



                buf.isactualnotif = false

                buf.alarmset = false
                buf.idalarm = 0


            } catch (e: Exception) {

                println("MyJsonParser stringToOperations error->" + e.message)
            }




            return buf
        }




        fun initBrandNewOnTimeNotif(context: Context, sens: String, datStart:Long=System.currentTimeMillis()+(5*60*1000), idCat:String):OnTimeNotificationFB{

            var buf=OnTimeNotificationFB()

            buf.sens=sens;
            buf.idcat=idCat

            var timeStart=System.currentTimeMillis()+(5*60*1000)

            if(datStart>timeStart) timeStart=datStart

            buf.datestart = com.gmb.bbm2.tools.allstatic.getDateAtCurrentTime(timeStart)
            buf.timestart = com.gmb.bbm2.tools.allstatic.getDateAtCurrentTime(timeStart)

            buf.timeend=buf.datestart


            buf.repeatunit = UIeventManager.VAL_NONE
            buf.repeatfreq=1
            buf.nbrerepeat=1
            buf!!.currentstate = context.getString(R.string.current_state_running)

            buf.unlimited=false
            buf.recurring=false

            buf.typeend=UIeventManager.VAL_REPEAT_OCCURENCE

            buf!!.daysofweek=""
            buf!!.dayofweekormonth=1
            buf!!.dayweekinmonth=1
            buf!!.reminderconf=""

            buf!!.isactualnotif=true
            buf!!.alarmset=false
            buf!!.ch1=""
            buf!!.ch2=""
            buf!!.ch3=""
            buf!!.descrip=""
            buf!!.idalarm=0
            buf!!.idstate=""
            buf!!.isautomaticpay=false
            buf!!.montantop=0.0
            buf!!.nom=""
            buf!!.nomsystem=""
            buf!!.type=""
            buf!!.url=""



            return buf
        }



    }
}