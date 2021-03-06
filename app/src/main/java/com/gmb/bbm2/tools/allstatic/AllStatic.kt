package com.gmb.bbm2.tools.allstatic

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.Nullable
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by GMB on 2/15/2018.
 */
public val DEFAULT_ID_ALARM = 20558
public val DB_NAME = "bbmdb"

public val PREF_FOR_DATA_PATH = "DB_PATH_PREF"
public val ROOT_USER_PATH = "rootUserDoc"
public val USER_EMAIL = "userEmail"
public val DEVICE_ID = "deviceID"
public val DEVICE_ID_CREATED = "dateDIcreated"
public val DEVICE_ID_SHARE = "deviceIDShare"
public val DEVICE_ID_SHARE_CREATED = "dateDIshareCreated"

public val DB_NAME_EXTENSION = ".db"
public val DB_NAME_EXPORTED = "bbmDBexported"

public val MY_CONFIG_FILE = "bbmconffile.txt"


 public val _MODE = 0
 public val PREF_NAME = "ReminderPro"
 public val IS_FIRSTTIME = "IsFirstTime"
 public val PREF_SHOW_PUB = "showPub"
 public val PREF_PUB_PERCENT = "pubPercent"
 public val PREF_PUB_PERCENT_CORRECTION = "pubPercentCorrection"
 public val PREF_DB_VERSION = "dbVersion"
 public val PREF_DB_NEEDS_UPDATE = "dbNeedsUpdate"
public val userName = "user_name"
public val NBRE_NOTIF_HANDLE = "NBRE_NOTIF_HANDLE"
public val DATE_LAST_NOTIF_HANDLE = "DATE_LAST_NOTIF_HANDLE"
public val NBRE_NOTIF_SEND = "NBRE_NOTIF_SEND"
public val ID_NOTIF_SEND = "ID_NOTIF_SEND"
public val MIN_NOTIF_HANDLE = 4
public var nbreCurrentNotifHeld = 0
public var dateLastNotif = System.currentTimeMillis()

public var nbreCurrentNotifSend = 0
public var idNotifSend: Long = 0

 public val notifSendLock = Any()


/**********************************************************************Field Names for Notif***************************/
public val COL_NAME_ID: String = "id"
public val COL_NAME_DATESTART: String = "datestart"
public val COL_NAME_TIMESTART: String = "timestart"
public val COL_NAME_CURRENT_STATE: String = "currentstate"
public val COL_NAME_DATEOP: String = "dateop"
public val COL_NAME_PUOP: String = "puop"
public val COL_NAME_QNTEOP: String = "qnteop"
public val COL_NAME_MONTTANT_OP: String = "montantop"
public val COL_NAME_TOTAL_OP: String = "totalop"
public val COL_NAME_PAID_OP: String = "paidop"
public val COL_NAME_MONTANT_PROJECTE: String = "montantprojecte"
public val COL_NAME_DATE_PROJECTE: String = "dateprojecte"
public val COL_NAME_DATE_REEL: String = "datereel"
public val COL_NAME_PENALITE: String = "penalite"
public val COL_NAME_CH1: String = "ch1"
public val COL_NAME_CH2: String = "ch2"
public val COL_NAME_CH3: String = "ch3"
public val COL_NAME_ID_STATE: String = "idstate"
public val COL_NAME_ID_CAT: String = "idcat"


public val COL_NAME_IS_ACTUAL_NOTIF: String = "isactualnotif"
public val COL_NAME_ALARM_SET: String = "alarmset"
public val COL_NAME_ID_ALARM: String = "idalarm"


public val COL_NAME_NOM: String = "nom"
public val COL_NAME_NOM_SYSTEM: String = "nomsystem"
public val COL_NAME_DESCRIP: String = "descrip"
public val COL_NAME_TYPE: String = "type"
public val COL_NAME_SENS: String = "sens"
public val COL_NAME_RECURRING: String = "recurring"
public val COL_NAME_IS_AUTO_PAY: String = "isautomaticpay"
public val COL_NAME_TIME_END: String = "timeend"
public val COL_NAME_URL: String = "url"
public val COL_NAME_UNLIMITED: String = "unlimited"
public val COL_NAME_TYPE_END: String = "typeend"
public val COL_NAME_REPEAT_UNIT: String = "repeatunit"
public val COL_NAME_REPEAT_FREQ: String = "repeatfreq"
public val COL_NAME_NBRE_REPEAT: String = "nbrerepeat"
public val COL_NAME_REMINDER_CONF: String = "reminderconf"
public val COL_NAME_DAYS_OF_WEEK: String = "daysofweek"
public val COL_NAME_DAYS_OF_WEEK_OR_MONTH: String = "dayofweekormonth"
public val COL_NAME_DAYS_WEEK_IN_MONTH: String = "dayweekinmonth"


/********************************************************************** END Field Names for Notif***************************/






/*******************************************************************Static field for FCM exchance********************************************************/
val BBM_TYPE_MSG="bbmTypeMessage"
val BBM_MSG_UPDATE_ALARM="bbmUpdateUalarm"
val BBM_MSG_CONTENT="bbm-msg-content"
val BBM_MSG_CONTENT_LIST="bbm-msg-content-list"
val BBM_MSG_PREF_NAME="bbm-msg-pref-name"
val BBM_UPDATE_ALARM_LIST_NEW="bbm-update-alarm-list-new"
val BBM_UPDATE_ALARM_LIST_OLD="bbm-update-alarm-list-old"


private var myFbEditor:MyFirestoreEditor?=null

/********************************************IMPORTANT METHODS***********/

public   fun getDateAtCurrentTime(date: Long): Long {

    val cal = GregorianCalendar()
    cal.timeInMillis = System.currentTimeMillis()
    val h = cal.get(Calendar.HOUR_OF_DAY)
    val m = cal.get(Calendar.MINUTE)
    cal.timeInMillis = date
    cal.set(Calendar.HOUR_OF_DAY, h)
    cal.set(Calendar.MINUTE, m)

    return cal.timeInMillis
   }



public fun getFireStoreEditor(context:Context): MyFirestoreEditor{

    if(myFbEditor==null){

        myFbEditor= MyFirestoreEditor(context)
    }

    return myFbEditor as MyFirestoreEditor
}



public fun getEmailAsKey(email:String):String{

    var emailKey = email!!.replace("@", "#$#")
    emailKey = emailKey.replace(".", "$#$")

    return emailKey
}


public fun getEmailFromKey(emailAsKey:String):String{

    var email = emailAsKey!!.replace( "#$#","@")
    email = email.replace( "$#$",".")

    return email
}








@TargetApi(Build.VERSION_CODES.KITKAT)
fun getLocalCurrency(context: Context): Currency {

 val pref = PreferenceManager.getDefaultSharedPreferences(context)
 val numFor = NumberFormat.getCurrencyInstance(Locale.US)
 var cur = numFor.currency

 try {

  val defCurCode = pref.getString("lst_default_currency", numFor.currency.currencyCode)

  //Log.e("MyBBmApp","getLocalCurrency this is defautCode->"+defCurCode);

  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
   val curs = Currency.getAvailableCurrencies()

   val it = curs.iterator()

   while (it.hasNext()) {
    val c = it.next()
    if (c.currencyCode.equals(defCurCode!!, ignoreCase = true)) {
     cur = c
     break
    }
   }
  } else {

   cur = Currency.getInstance(defCurCode)
  }


 } catch (ex: Exception) {

  ex.printStackTrace()
 }


 return cur

}






fun isClickMenuContextuel(context: Context): Boolean {

 val pref = PreferenceManager.getDefaultSharedPreferences(context)
 return pref.getBoolean("swt_mouse_bouton", true)
}


fun compareDate(date1: Long, date2: Long, withoutTime: Boolean): Int {


 var res: Int = 0
 if (withoutTime) {

  val cal = GregorianCalendar()
  cal.timeInMillis = date1
  cal.set(Calendar.HOUR_OF_DAY, 0)
  cal.set(Calendar.MINUTE, 0)
  cal.set(Calendar.SECOND, 0)
  cal.set(Calendar.MILLISECOND, 0)
  val d1 = cal.time

  cal.timeInMillis = date2
  cal.set(Calendar.HOUR_OF_DAY, 0)
  cal.set(Calendar.MINUTE, 0)
  cal.set(Calendar.SECOND, 0)
  cal.set(Calendar.MILLISECOND, 0)
  val d2 = cal.time

  res = d1.compareTo(d2)

 } else {

  val d1 = Date(date1)
  val d2 = Date(date2)
  res = d1.compareTo(d2)
 }



 return res
}


fun isOverdueAllowed(context: Context): Boolean {

 val pref = PreferenceManager.getDefaultSharedPreferences(context)

 return pref.getBoolean("swt_overdue_allowed", false)

}


fun isBlockOverBudgetActivated(context: Context): Boolean {

 val pref = PreferenceManager.getDefaultSharedPreferences(context)

 return pref.getBoolean("swt_over_budget_blocked", true)

}


/***************************************************************UI Usual functions**************************************************************/

fun hideSoftInput(context: Context, view: View?, showKeyboard: Boolean) {

 if (showKeyboard) {

  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
 } else {

  if (view != null) {
   val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
   imm.hideSoftInputFromWindow(view.windowToken, 0)
  }
 }
}


/***************************************************************All Date Functions********************************************************/
fun getLastDayMonth(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getLastDayMonth voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curMont = cal.get(Calendar.MONTH)
 var lastDay = cal.get(Calendar.DAY_OF_MONTH)

 while (cal.get(Calendar.MONTH) == curMont) {

  lastDay = cal.get(Calendar.DAY_OF_MONTH)
  cal.add(Calendar.DAY_OF_MONTH, 1)
 }

 //Remise au mois dont on faisait la boucle car a la fin, on a change de mois et pour certains cas l annee
 cal.add(Calendar.DAY_OF_MONTH, -1)

 //cal.set(Calendar.MONTH,curMont);
 cal.set(Calendar.DAY_OF_MONTH, lastDay)
 cal.set(Calendar.HOUR_OF_DAY, 23)
 cal.set(Calendar.MINUTE, 59)
 cal.set(Calendar.SECOND, 59)

 //Log.e("MyBbmAppli", "getLastDayMonth voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis

}


fun getLastHourDay(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getLastDayMonth voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour

 cal.set(Calendar.HOUR_OF_DAY, 23)
 cal.set(Calendar.MINUTE, 59)
 cal.set(Calendar.SECOND, 59)


 return cal.timeInMillis

}

fun getFirstHourDay(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getLastDayMonth voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour

 cal.set(Calendar.HOUR_OF_DAY, 0)
 cal.set(Calendar.MINUTE, 0)
 cal.set(Calendar.SECOND, 0)

 return cal.timeInMillis

}


fun getNumberOfDaysInMonth(dateJour: Long): Int {

 getFirstDayMonth(dateJour)

 val cal = GregorianCalendar()

 cal.timeInMillis = dateJour

 cal.set(Calendar.DAY_OF_MONTH, 1)
 ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth first day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
 cal.add(Calendar.MONTH, 1)
 cal.add(Calendar.DAY_OF_MONTH, -1)

 ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth last day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
 return cal.get(Calendar.DAY_OF_MONTH)
}


fun getFirstDayMonth(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getFirstDayMonth voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curMont = cal.get(Calendar.MONTH)
 var firstDay = cal.get(Calendar.DAY_OF_MONTH)

 while (cal.get(Calendar.MONTH) == curMont) {

  firstDay = cal.get(Calendar.DAY_OF_MONTH)
  cal.add(Calendar.DAY_OF_MONTH, -1)
 }

 //Remise au mois dont on faisait la boucle car a la fin, on a change de mois et pour certains cas l annee
 cal.add(Calendar.DAY_OF_MONTH, 1)

 //cal.set(Calendar.MONTH,curMont);
 cal.set(Calendar.DAY_OF_MONTH, firstDay)
 cal.set(Calendar.HOUR_OF_DAY, 0)
 cal.set(Calendar.MINUTE, 0)
 cal.set(Calendar.SECOND, 0)

 //Log.e("MyBbmAppli", "getFirstDayMonth voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis
}


fun getFirstDayWeek(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getFirstDayWeek voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curWeek = cal.get(Calendar.WEEK_OF_YEAR)

 while (cal.get(Calendar.WEEK_OF_YEAR) == curWeek) {

  cal.add(Calendar.DAY_OF_MONTH, -1)
 }

 //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
 cal.add(Calendar.DAY_OF_MONTH, 1)


 cal.set(Calendar.HOUR_OF_DAY, 0)
 cal.set(Calendar.MINUTE, 0)
 cal.set(Calendar.SECOND, 0)

 //Log.e("MyBbmAppli", "getFirstDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis
}

fun getLastDayWeek(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getLastDayWeek voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curWeek = cal.get(Calendar.WEEK_OF_YEAR)

 while (cal.get(Calendar.WEEK_OF_YEAR) == curWeek) {

  cal.add(Calendar.DAY_OF_MONTH, 1)
 }

 //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
 cal.add(Calendar.DAY_OF_MONTH, -1)


 cal.set(Calendar.HOUR_OF_DAY, 23)
 cal.set(Calendar.MINUTE, 59)
 cal.set(Calendar.SECOND, 59)

 //Log.e("MyBbmAppli", "getLastDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis
}

fun getFirstDayWeekOfMonth(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getFirstDayWeek voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curWeek = cal.get(Calendar.WEEK_OF_MONTH)

 while (cal.get(Calendar.WEEK_OF_MONTH) == curWeek) {

  cal.add(Calendar.DAY_OF_MONTH, -1)
 }

 //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
 cal.add(Calendar.DAY_OF_MONTH, 1)


 cal.set(Calendar.HOUR_OF_DAY, 0)
 cal.set(Calendar.MINUTE, 0)
 cal.set(Calendar.SECOND, 0)

 //Log.e("MyBbmAppli", "getFirstDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis
}

fun getLastDayWeekOfMonth(datCour: Long): Long {

 //Log.e("MyBbmAppli", "getLastDayWeek voici ce que je recois->" + new Date(datCour));

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 val curWeek = cal.get(Calendar.WEEK_OF_MONTH)

 while (cal.get(Calendar.WEEK_OF_MONTH) == curWeek) {

  cal.add(Calendar.DAY_OF_MONTH, 1)
 }

 //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
 cal.add(Calendar.DAY_OF_MONTH, -1)


 cal.set(Calendar.HOUR_OF_DAY, 23)
 cal.set(Calendar.MINUTE, 59)
 cal.set(Calendar.SECOND, 59)

 //Log.e("MyBbmAppli", "getLastDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

 return cal.timeInMillis
}

fun getFirstDayPreviousMonth(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.MONTH, -1)

 // Log.e("MyBbmAppli","getFirstDayLastMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));

 return getFirstDayMonth(cal.timeInMillis)

}

fun getLastDayPreviousMonth(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.MONTH, -1)

 //Log.e("MyBbmAppli","getLastDayLastMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getLastDayMonth(cal.timeInMillis)

}


fun getFirstDayNextMonth(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.MONTH, 1)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getFirstDayMonth(cal.timeInMillis)

}

fun getFirstDayNextYear(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.YEAR, 1)
 cal.set(Calendar.MONTH, Calendar.JANUARY)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getFirstDayMonth(cal.timeInMillis)

}

fun getLastDayNextMonth(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.MONTH, 1)

 //Log.e("MyBbmAppli","getLastDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getLastDayMonth(cal.timeInMillis)

}


fun getFirstDayPreviousYear(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.YEAR, -1)
 cal.set(Calendar.MONTH, Calendar.JANUARY)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getFirstDayMonth(cal.timeInMillis)

}


fun getLastDayPreviousYear(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.add(Calendar.YEAR, -1)
 cal.set(Calendar.MONTH, Calendar.DECEMBER)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getLastDayMonth(cal.timeInMillis)

}


fun getFirstDayYear(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.set(Calendar.MONTH, Calendar.JANUARY)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getFirstDayMonth(cal.timeInMillis)

}


fun getLastDayYear(datCour: Long): Long {

 val cal = GregorianCalendar()
 cal.timeInMillis = datCour
 cal.set(Calendar.MONTH, Calendar.DECEMBER)

 //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
 return getLastDayMonth(cal.timeInMillis)

}


fun getMonthShortAsString(dateJour: Long): String {

 val daFor = SimpleDateFormat("MMM.")

 return daFor.format(Date(dateJour))
}


fun getMonthAsString(dateJour: Long): String {

 val daFor = SimpleDateFormat("MMMM")

 return daFor.format(Date(dateJour))
}

fun getMonthAndYearAsString(dateJour: Long): String {

 val daFor = SimpleDateFormat("MMMM yyyy")

 return daFor.format(Date(dateJour))
}

fun getWeekDayAsString(dateJour: Long): String {
 val daFor = SimpleDateFormat("EEEEE")

 return daFor.format(Date(dateJour))
}

fun getWeekDayShortAsString(dateJour: Long): String {
 val daFor = SimpleDateFormat("EEE")

 return daFor.format(Date(dateJour))
}

fun getWeekDayOneLetterAsString(dateJour: Long): String {
 val daFor = SimpleDateFormat("E")

 return daFor.format(Date(dateJour))
}





fun isSundayFirstDayWeek(context: Context): Boolean {

 val pref = PreferenceManager.getDefaultSharedPreferences(context)
 var res = true

 try {

  res = pref.getBoolean("swt_sunday_first_day", true)
 } catch (ex: Exception) {

  ex.printStackTrace()
 }


 return res

}




fun getAmountMinCat(_context: Context): Double {

 val pref = PreferenceManager.getDefaultSharedPreferences(_context)

 var min = 10.00

 try {
  min = java.lang.Double.parseDouble(pref.getString("txt_default_cat_min_amount", "10.00"))
 } catch (ex: Exception) {

 }

 return min
}

fun isNotEmpty(test:String):Boolean{

 var res:Boolean=true

 if(test==null) return false

 if("".equals(test,true)) return false

 return  res

}


fun setPreferenceVal(context: Context,@Nullable prefName:String,prefKey:String,prefVal:String):Boolean {

 var pref:SharedPreferences?=null

 if(prefName==null){

  pref = PreferenceManager.getDefaultSharedPreferences(context)
 }
 else{


  pref = context.getSharedPreferences(prefName, PRIVATE_MODE)
 }

 val editor = pref.edit()



 try {

  editor.putString(prefKey,prefVal)
  editor.commit()

  return true


 } catch (ex: Exception) {

  ex.printStackTrace()
 }


 return false

}


fun getPreferenceVal(context: Context,@Nullable prefName:String,prefKey:String):String {

 var pref:SharedPreferences?=null

 if(prefName==null){

  pref = PreferenceManager.getDefaultSharedPreferences(context)
 }
 else{


  pref = context.getSharedPreferences(prefName, PRIVATE_MODE)
 }



 try {



  return pref!!.getString(prefKey,"")


 } catch (ex: Exception) {

  ex.printStackTrace()
 }


 return ""

}

val PRIVATE_MODE: Int=0

