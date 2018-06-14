package com.gmb.bbm2

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import com.firebase.jobdispatcher.*
import com.gmb.bbm2.data.model.BbmStatementFB
import com.gmb.bbm2.data.model.CategoryFB
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.tools.alarm.MyJobService
import com.gmb.bbm2.tools.allstatic.getFireStoreEditor
import com.gmb.bbm2.tools.customviews.MySpinnerAsTextInputLayout
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.firestore.onActionDone
import com.gmb.bbm2.tools.json.MyJsonParserKotlin
import com.gmb.bbm2.tools.utils.UIeventManager
import com.gmb.gmbdatepickerpro.DatePickerPro
import com.gmb.mycalcolibrary.SimpleCalco2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.content_add_bbm2.*
import kotlinx.android.synthetic.main.content_add_bbm2.view.*
import kotlinx.android.synthetic.main.lyt_title_button_gen.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A fragment representing a single Notif detail screen.
 * This fragment is either contained in a [NotifListActivity]
 * in two-pane mode (on tablets) or a [NotifDetailActivity]
 * on handsets.
 */
class NotifDetailFragment : Fragment(),View.OnClickListener,View.OnTouchListener, AdapterView.OnItemSelectedListener, MySpinnerAsTextInputLayout.OnValueSelectedListner {
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

        val x = p1!!.getX().toInt()
        val y = p1!!.getY().toInt()

        /*if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            mWindow.dismiss();

            return true;
        }*/

        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.e("AddStatemet","onTouch Event this is view->"+v+" touched down");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("AddStatemet","onTouch Event this is view->"+v+" moving: (" + x + ", " + y + ")");
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("AddStatemet","onTouch Event this is view->"+v+" touched up");
                break;
        }*/


        if (p1!!.getAction() == MotionEvent.ACTION_UP) {

            ////Log.e("AddStatemet","onTouch Event this is view->"+v);


            when (p0!!.getId()) {


                R.id.txtMontantEvent -> {
                    showDialogCalco()
                }

                R.id.txtDateStart -> {
                    showDialogDate()
                }


                R.id.img_repeatOption -> {
                    showDialogRepeat()
                }
            }

            return true
        }
        return false
    }

    var onCompleteListener = OnCompleteListener<Void> { task ->
        if (progressDialog != null && progressDialog!!.isShowing()) {

            progressDialog!!.dismiss()
        }
        if (task.isSuccessful) {

            Snackbar.make(rootView, getString(R.string.op_completed), Snackbar.LENGTH_SHORT).show()
            //Toast.makeText(context, getString(R.string.op_completed), Toast.LENGTH_SHORT).show()
            //Log.d(TAG, "Write batch succeeded.");
            closefragment()
        } else {
            Snackbar.make(rootView, getString(R.string.op_failed), Snackbar.LENGTH_SHORT).show()
            //Toast.makeText(context, getString(R.string.op_failed) + "->" + task.exception, Toast.LENGTH_SHORT).show()
            Log.w("AddStatement", "write batch failed.", task.exception)
        }
    }


    var cal:GregorianCalendar= GregorianCalendar();

    lateinit var dayOfWeek:ArrayList<Int>


    private var progressDialog: ProgressDialog? = null


    private var hour: Int = 0
    private var min: Int = 0

    private var valExclude: java.util.ArrayList<String>? = null
    private var catVal: java.util.ArrayList<Any>? = null

    private var catList: List<CategoryFB>? = null
    private var catAffiche: java.util.ArrayList<Any>? = null
    private var catcour: CategoryFB? = null

    private var bufCour = OnTimeNotificationFB()

    internal var repeatingTime: java.util.ArrayList<Long>? = null

    lateinit var datFor: SimpleDateFormat
    lateinit var hourFor: SimpleDateFormat

    internal var isVertical = false

    lateinit internal var repeatUnitVal: List<String>

    internal var montMonth = 0.0
    internal var montYear = 0.0
    internal var datCour: Long = 0
    lateinit internal var lytTitle: RelativeLayout

    private val datePickedListener = object : DatePickerPro.OnDatePickedListener {
        override fun datePicked(datePicked: Long) {


            cal.setTimeInMillis(bufCour.datestart)
            val h = cal.get(Calendar.HOUR_OF_DAY)
            val m = cal.get(Calendar.MINUTE)

            cal.setTimeInMillis(datePicked)
            cal.set(Calendar.MINUTE, m)
            cal.set(Calendar.HOUR_OF_DAY, h)
            bufCour!!.datestart = cal.getTimeInMillis()
            bufCour!!.timestart = bufCour!!.datestart

            txtDateStartVal.setText("" + cal.getTimeInMillis())
            txtDateStart.setText(datFor.format(cal.getTime()))

           // recalculEnding()
        }
    }

    private val calcoListener = SimpleCalco2.OnCalcListener { v -> txtMontantEvent.setText(numFor!!.format(v)) }


    private val repeatListner = object : MyRepeatDialog.MyRepeatDialogListener {
        override fun onRepeatEdited(repeatUnit: String, typeend: String, dayOfWeek: ArrayList<Int>,
                           dayOfMonthOrWeek: Boolean, dayOfWeekOrMonth: Int, dayWeekInMonth: Int,
                           freqRepeat: Int, nbreRepeat: Int, unlimited: Int, timeStart: Long, dateEnd: Long, reminderList: String) {


            updateValFromRepeatDiaog(repeatUnit, typeend, dayOfWeek, dayOfMonthOrWeek, dayOfWeekOrMonth, dayWeekInMonth, freqRepeat, nbreRepeat, unlimited, timeStart, dateEnd, reminderList)

        }

    }

    private fun getNameRepeatUnit(repeatUnitCode: String): String {

        var res = ""

        val nom = resources.getStringArray(R.array.repeat_units)
        val `val` = resources.getStringArray(R.array.repeat_units_val)
        val tail = `val`.size
        for (i in 0 until tail) {

            if (`val`[i].equals(repeatUnitCode, ignoreCase = true)) {

                res = nom[i]
                break
            }
        }


        return res
    }


    fun updateValFromRepeatDiaog(repeatUnit: String?,
                                 typeend: String,
                                 dayOfWeek: ArrayList<Int>,
                                 dayOfMonthOrWeek: Boolean,
                                 dayOfWeekOrMonth: Int,
                                 dayWeekInMonth: Int,
                                 freqRepeat: Int,
                                 nbreRepeat: Int,
                                 unlimited: Int,
                                 timeStart: Long,
                                 dateEnd: Long, reminderList: String) {


        if (repeatUnit != null && !repeatUnit.equals(UIeventManager.VAL_NONE, ignoreCase = true)) {


            var textToEditRepeatOption = ""
            var timeEndRepeat = ""

            if (typeend.equals(getString(R.string.type_end_occu), ignoreCase = true))

                timeEndRepeat = ". " + nbreRepeat + " " + getString(R.string.repeat_how_long)
            else
                timeEndRepeat = ". " + getString(R.string.repeat_until) + " " + datFor.format(Date(dateEnd))

            if (unlimited == 1) timeEndRepeat = getString(R.string.repeat_unlimited)



            textToEditRepeatOption = getString(R.string.repeat_every) + " " + freqRepeat + " " + getNameRepeatUnit(repeatUnit) + " " + timeEndRepeat + getString(R.string.click_to_edit)



            txtRepeatInfo.setVisibility(View.VISIBLE)
            //cmdMore.setVisibility(View.GONE);
            txtRepeatInfo.setText(textToEditRepeatOption)
        } else {

           // cmbFreq.setSelection(repeatUnitVal.indexOf(repeatUnit))
            //cmdMore.setVisibility(View.VISIBLE)
            spinRepeatUnit.setSelectedIndex(repeatUnitVal.indexOf(repeatUnit))
            txtRepeatInfo.setVisibility(View.GONE)
            txtRepeatInfo.setText("")
            return
        }

        val dayOfWeekStr=StringBuilder()

        for (str in dayOfWeek){

            dayOfWeekStr.append(str.toString()+"-")
        }

        spinRepeatUnit.setSelectedIndex(repeatUnitVal.indexOf(repeatUnit))

        bufCour!!.reminderconf = reminderList


        bufCour!!.daysofweek = dayOfWeekStr.toString()
        //bufCour!!.dayOfMonthOrWeek = dayOfMonthOrWeek
        bufCour!!.dayofweekormonth = dayOfWeekOrMonth
        bufCour!!.dayweekinmonth = dayWeekInMonth

        bufCour!!.typeend = typeend
        bufCour!!.repeatfreq = freqRepeat
        bufCour!!.unlimited = unlimited == 1
        bufCour!!.nbrerepeat = nbreRepeat
        bufCour!!.repeatunit = repeatUnit

        if (UIeventManager.VAL_REPEAT_OCCURENCE.equals(typeend,true)) {

            bufCour!!.unlimited = false
        } else if (UIeventManager.VAL_REPEAT_UNTIL.equals(typeend,true)) {


            bufCour!!.unlimited = false
        } else if (UIeventManager.VAL_REPEAT_UNLIMITED.equals(typeend,true)) {

            bufCour!!.unlimited = true
        }

        bufCour!!.timestart = timeStart
        bufCour!!.datestart = timeStart
        bufCour!!.timeend = dateEnd


    }

    private fun recalculEnding() {

        if (!bufCour!!.repeatunit.equals(UIeventManager.VAL_NONE,true)) {


            val repeat = MyRepeatDialog(context, repeatListner, bufCour!!.repeatunit,
                    UIeventManager.VAL_REPEAT_UNLIMITED,
                    bufCour!!.daysofweek,
                    1,
                    bufCour!!.dayofweekormonth,
                    bufCour!!.dayweekinmonth,
                    1,
                    bufCour!!.nbrerepeat,
                    if (bufCour!!.unlimited) 1 else 0,
                    bufCour!!.timestart,
                    bufCour!!.timeend,
                    bufCour!!.reminderconf)

            repeat.saveState()

            //if (isVertical) txtRepeatInfo.setVisibility(View.VISIBLE)
            //cmdMore.setVisibility(View.GONE);
        } else {

            //rootView.img_repeatOption.setVisibility(View.VISIBLE)
            rootView.txtRepeatInfo.setVisibility(View.GONE)
            rootView.txtRepeatInfo.setText("")

        }
    }


    override fun getSelectedValue(selected: Any?,customSpin:MySpinnerAsTextInputLayout?) {

        val selectedValue:String =selected.toString()

        Log.e("NotifDetail", "onItemSelected et val->"+selectedValue)

        if(customSpin===rootView.spinCategory){

            if (selectedValue.equals(getString(R.string.add_new_val), ignoreCase = true) ||selectedValue.equals(getString(R.string.add_new), ignoreCase = true)) {

                var catListner= object :CategoryFragment.OnCategoryFragmentListener{
                    override fun newCatCreated(id: String, name: String) {

                        var values= customSpin?.getSpinListVal()

                        var valuesShow= customSpin?.getSpinListShown()

                        if(values!=null){
                            values.add(id)
                        }

                        if(valuesShow!=null){
                            valuesShow.add(name)
                        }

                        customSpin?.setSpinListVal(values,valuesShow,id)


                    }
                }



                val ft = activity.supportFragmentManager.beginTransaction()
                val newFragment = CategoryFragment.newInstance("", catListner,false)
                newFragment.setListner(catListner)
                newFragment.show(ft, "dialog")
            }
        }
        else if(customSpin===rootView.spinRepeatUnit){

            bufCour!!.repeatunit= selectedValue!!
            recalculEnding()
        }


        //Log.i("addStatementFragment", "onItemSelected pos 3");
    }





    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.cmdCancel -> {
                closefragment()
            }
            R.id.cmdSave -> {
                saveData()
            }

            R.id.txtMontantEvent -> {
                showDialogCalco()
            }

            R.id.txtDateStart -> {
                showDialogDate()
            }


            R.id.img_repeatOption -> {
                showDialogRepeat()
            }
        }
    }



    fun showDialogCalco(){

        val dialog = SimpleCalco2(context,calcoListener)
        dialog.show()
    }

    fun showDialogDate(){

        val dialog = DatePickerPro(context,datePickedListener,txtDateStartVal.text.toString().toLong(),false)
        dialog.show()
    }

    private fun showDialogRepeat() {

        /*repeatOn = ""
        if (dayOfWeek != null && dayOfWeek.size > 0) {

            for (`in` in dayOfWeek) {
                repeatOn = repeatOn + `in` + "-"
            }
        }*/


        val dialog = MyRepeatDialog(context, repeatListner, bufCour!!.repeatunit,
                bufCour!!.typeend,
                bufCour!!.daysofweek,
                bufCour!!.dayofweekormonth,
                bufCour!!.dayofweekormonth,
                bufCour!!.dayweekinmonth,
                bufCour!!.repeatfreq,
                bufCour!!.nbrerepeat,
                if (bufCour!!.unlimited) 1 else 0,
                bufCour!!.timestart,
                bufCour!!.timeend,
                bufCour!!.reminderconf)


        dialog.show()

    }

    private var objAsJson: String? = null
    //private var mPlusOneButton: PlusOneButton? = null

    private  var idCour:String?=null
    private lateinit var rootView:View

    private var numFor: NumberFormat?= NumberFormat.getCurrencyInstance()



    private var idCat: String?=null

    /**
     * The dummy content this fragment is presenting.
     */
    private lateinit var mItem: OnTimeNotificationFB

    internal lateinit var fsEdit: MyFirestoreEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fsEdit = getFireStoreEditor(this.context)

        bufCour= MyJsonParserKotlin.initBrandNewOnTimeNotif(context,getString(R.string.sens_debit),System.currentTimeMillis()+5*60*1000,"123")

        dayOfWeek=ArrayList()

        arguments?.let {
            if (it.containsKey(NotifDetailFragment.ARG_OBJ_AS_JSON)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                objAsJson = it.getString(CategoryFragment.ARG_OBJ_AS_JSON)
                //activity?.toolbar_layout?.title = mItem?.content
                //Log.e("CategoryF","this is arg->"+objAsJson)
            }


            if (it.containsKey(NotifDetailFragment.ARG_DATE_COUR)) {

                datCour = it.getLong(ARG_DATE_COUR)
            }


            if (it.containsKey(NotifDetailFragment.ARG_DATE_START_PARAM)) {


                var timeStart=System.currentTimeMillis()+(5*60*1000)

                val datStart=it.getString(ARG_DATE_START_PARAM).toLong()

                if(datStart>timeStart) timeStart=datStart

                bufCour!!.datestart = timeStart
            }


            if (it.containsKey(NotifDetailFragment.ARG_SENS)) {

                bufCour!!.sens = it.getString(ARG_SENS)
            }


            if (it.containsKey(NotifDetailFragment.ARG_ID_CAT)) {

                bufCour!!.idcat = it.getString(ARG_ID_CAT)
            }

            if (it.containsKey(NotifDetailFragment.ARG_ID_STAT)) {

                bufCour!!.idstate = it.getString(ARG_ID_STAT)
            }

        }



        if (savedInstanceState != null) {

            restaureMySavedInstance(savedInstanceState)
        }
    }











    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.notif_detail, container, false)


        datFor= SimpleDateFormat(getString(R.string.date_pattern))




        rootView.cmdCancel!!.setOnClickListener(this)
        rootView.cmdSave!!.setOnClickListener(this)

        cal.timeInMillis=System.currentTimeMillis()

        rootView.txtDateStartVal.text=cal.timeInMillis.toString()
        rootView.txtDateStart.setText(datFor.format(cal.time))

        rootView.txtDateStart!!.setOnClickListener(this)
        rootView.txtMontantEvent!!.setOnClickListener(this)
        rootView.img_repeatOption!!.setOnClickListener(this)

        class onAction(simple:String): onActionDone {

            override fun onListDone(list: QuerySnapshot?) {

                catList = ArrayList<CategoryFB>()
                if (list != null) {

                    catList = list.toObjects<CategoryFB>(CategoryFB::class.java) as ArrayList<CategoryFB>


                    //Log.e("CategoryListAct", "this is category list from firestore->" + list!!.size())


                    setCatList()

                }
            }


        }

        fsEdit.getListItems(MyFirestoreEditor.COLLECTION_CATEGORY,  onAction("val"))







        if(objAsJson!=null && !objAsJson.equals("")){


            bufCour=MyJsonParserKotlin.stringToOnTimeNotificationFB(objAsJson!!)

            if(bufCour!=null) populateData(bufCour!!)

        }
        /*else{

            bufCour=createBrandNewBuf(sens!!,dateStart!!,idCat!!)
        }*/





        return rootView
    }


    fun populateData(buf:OnTimeNotificationFB){


        //Log.e("addStatement", "populateData this is buf->" + buf.nomsystem)
        idCour=buf.id
        rootView.txtNomEvent.setText(buf.nom)
        rootView.txtNomSystem.setText(buf.nomsystem)


        rootView.spinCategory.setSelectedID(buf.idcat)

        rootView.txtMontantEvent.setText(numFor!!.format(buf.montantprojecte))

        rootView.txtDateStart.setText(datFor.format(buf.datestart))
        rootView.txtDateStartVal.setText(buf.datestart.toString())


        rootView.spinRepeatUnit.setSelectedID(buf.repeatfreq)

        rootView.swtAutoPay.isChecked=buf.isautomaticpay

        rootView.txtNote.setText(buf.descrip)
        //rootView.txtMontantEvent.setText(numFor!!.format(buf.montantop))
        //rootView!!.txtMontantEventEvent!!.setText(buf!!.montantop!!.toString())
        /*rootView!!.swtAutoPay!!.isSelected=buf.isautomaticpay
        rootView!!.txtNote!!.setText(buf.descrip)
        idCat=buf.idcat*/


        Log.e("addStatement", "populateData this is buf->" + buf.nomsystem+"_the ID->"+idCour)
    }





    fun setCatList(){

        if (catList != null) {


            catAffiche= ArrayList()
            catVal= ArrayList()
            //mViewModel.setCatcour(catList.get(0))
            //Log.e("addStatement", "this is cat list from firestore->" + catList)

            var inCat = 0
            if (catList != null && catList!!.size > 0) {
                catAffiche = ArrayList<Any>()
                catVal = ArrayList<Any>()
                var i = 0
                for (cat in catList!!) {

                    if(cat.nomaffiche!=null){

                        catAffiche!!.add(cat.getNomaffiche())
                        catVal!!.add(cat.id)
                    }
                    if (cat.getId().equals(idCat)) inCat = i

                    i++
                }
                //cmbCategory.setSelection(inCat);
                //mViewModel.setCatcour(catList.get(inCat))

                catAffiche!!.add(getString(R.string.add_new))
                catVal!!.add(getString(R.string.add_new_val))




                spinCategory.setSpinListVal(catVal,catAffiche);
                spinCategory.setValListner(this)
                spinCategory.setSelectedIndex(0)

                val myResArrayVal = resources.getStringArray(R.array.repeat_units_val2)
                repeatUnitVal = ArrayList<String>()
                for(str in myResArrayVal){

                    (repeatUnitVal as ArrayList<String>).add(str)
                }

                val myResArray = resources.getStringArray(R.array.repeat_units2)
                var repeatAff = ArrayList<Any>()
                for(str in myResArray){

                    repeatAff.add(str)
                }


                spinRepeatUnit.setSpinListVal(repeatUnitVal,repeatAff);
                spinRepeatUnit.setSelectedIndex(0)

            }

        }
    }


    private fun restaureMySavedInstance(savedInstance: Bundle?) {

        if (savedInstance == null) return


        //Log.e("addStatement","restaure pos 0");
        var bufSave = savedInstance.getString(ARG_OBJ_AS_JSON)

        if (bufSave == null) return

        bufCour=MyJsonParserKotlin.stringToOnTimeNotificationFB(bufSave)

        cal = GregorianCalendar()
        cal.timeInMillis = bufCour!!.datestart





        var tok: StringTokenizer


        if (savedInstance.getString("dayOfWeek") != null) {

            dayOfWeek = java.util.ArrayList()
            tok = StringTokenizer(savedInstance.getString("dayOfWeek"), "#")
            while (tok.hasMoreTokens()) {

                dayOfWeek.add(Integer.parseInt(tok.nextToken()))
            }
        }




        if (savedInstance.getString("repeatingTime") != null) {

            repeatingTime = java.util.ArrayList()
            tok = StringTokenizer(savedInstance.getString("repeatingTime"), "#")
            while (tok.hasMoreTokens()) {

                repeatingTime!!.add(java.lang.Long.parseLong(tok.nextToken()))
            }
        }




    }


    fun showProgresBarAndInsertInDB(buf: BbmStatementFB, jsonObject: JSONObject,updateOne:Boolean,notifUpdate:OnTimeNotificationFB,isAllNew:Boolean=true) {

        try {


            progressDialog = ProgressDialog(activity)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.alert_dialog_cancel_button), DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                //if(reloadData) {

                // onNotifLoaded(currentNotif);
                // }
                //else{

                closefragment()
                //}
            })
            progressDialog!!.setMessage(getString(R.string.work_in_process))
            progressDialog!!.show()

            fsEdit.addBBMandNotif(buf, jsonObject, onCompleteListener,updateOne,notifUpdate,isAllNew)

        } catch (ex: Exception) {

            Log.e("AddStatement", "ShowProgressBar error************************>" + ex.message)
        }

    }


    fun CreateAndDispatchJob(jobTag:String){


        // Create a new dispatcher using the Google Play driver.

        var dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context));

        /*


        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
                ))
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.schedule(constraintReminderJob);







        Job myJob = dispatcher.newJobBuilder()
    .setService(MyJobService.class) // the JobService that will be called
    .setTag("my-unique-tag")        // uniquely identifies the job
    .build();

dispatcher.mustSchedule(myJob);*/


        var myExtrasBundle = Bundle();
        myExtrasBundle.putString("some_key", "some_value");

    var myJob = dispatcher.newJobBuilder()
    // the JobService that will be called
    .setService(MyJobService::class.java)
    // uniquely identifies the job
    .setTag(jobTag)
    // one-off job
    .setRecurring(false)
    // don't persist past a device reboot
    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
    // start between 0 and 60 seconds from now
    .setTrigger(Trigger.executionWindow(0, 60))
    // don't overwrite an existing job with the same tag
    .setReplaceCurrent(false)
    // retry with exponential backoff
    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
    // constraints that need to be satisfied for the job to run
    .setConstraints(
        // only run on an unmetered network
        Constraint.ON_UNMETERED_NETWORK,
        // only run when the device is charging
        Constraint.DEVICE_CHARGING
    )
    .setExtras(myExtrasBundle)
    .build();

    dispatcher.mustSchedule(myJob);



    }

    fun cancelJob(jobTag:String){

        var dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context));

        dispatcher.cancel(jobTag);

    }


    fun saveData(){



        if("".equals(rootView.txtNomEvent.text.toString(),true) || "".equals(rootView.txtMontantEvent.text.toString(),true)

                || "".equals(rootView.txtDateStart.text.toString(),true)){

            Snackbar.make(rootView, getString(R.string.must_fill_out_field), Snackbar.LENGTH_SHORT).show()
            return
        }





        bufCour!!.nom=rootView.txtNomEvent.text.toString();
        //cat!!.minamount=numFor!!.parse(rootView.txtMontantEvent.text.toString()).toDouble()
        bufCour!!.montantprojecte= numFor!!.parse(rootView.txtMontantEvent.text.toString()).toDouble()
        bufCour!!.isautomaticpay=rootView.swtAutoPay.isChecked

        if(bufCour!!.nomsystem==null) bufCour!!.nomsystem=bufCour!!.nom

        bufCour!!.descrip=rootView.txtNote.text.toString()


        var timeStart=System.currentTimeMillis()+(2*60*1000)
        var datStart=rootView.txtDateStartVal.text.toString().toLong()

        if(datStart>timeStart) timeStart=datStart

        bufCour!!.datestart=timeStart

        bufCour!!.idcat=rootView.spinCategory.idAsString

        bufCour!!.repeatunit=rootView.spinRepeatUnit.idAsString

        //bufCour!!.id=idCour;

        val bufStat=BbmStatementFB()

        bufStat.currentstate=bufCour!!.currentstate
        bufStat.datestart=bufCour!!.datestart
        bufStat.dateend=bufCour!!.timeend
        bufStat.dayofweekormonth=bufCour!!.dayofweekormonth
        bufStat.daysofweek=bufCour!!.daysofweek
        bufStat.dayweekinmonth=bufCour!!.dayweekinmonth
        bufStat.descrip=bufCour!!.descrip
        bufStat.id=bufCour!!.idstate
        bufStat.idcat=bufCour!!.idcat
        bufStat.isautomaticpay=bufCour!!.isautomaticpay
        bufStat.montant=bufCour!!.montantprojecte
        bufStat.nbrerepeat=bufCour!!.nbrerepeat
        bufStat.nom=bufCour!!.nom
        bufStat.nomsystem=bufCour!!.nomsystem
        bufStat.pu=0.0
        bufStat.qnte=1
        bufStat.recurring=bufCour!!.recurring
        bufStat.reminderconf=bufCour!!.reminderconf
        bufStat.repeatfreq=bufCour!!.repeatfreq
        bufStat.repeatunit=bufCour!!.repeatunit
        bufStat.sens=bufCour!!.sens
        bufStat.timeend=bufCour!!.timeend
        bufStat.timestart=bufCour!!.timestart
        bufStat.type=bufCour!!.type
        bufStat.typeend=bufCour!!.typeend
        bufStat.unlimited=bufCour!!.unlimited
        bufStat.url=bufCour!!.url



        val typeEndOccu= if (UIeventManager.VAL_REPEAT_OCCURENCE.equals(bufCour!!.typeend,true)) true else false

        val typeEndDate= if (UIeventManager.VAL_REPEAT_UNTIL.equals(bufCour!!.typeend,true)) true else false



        repeatingTime = UIeventManager.generateRepeatOccurence(bufCour!!.repeatunit, bufCour!!.repeatfreq, bufCour!!.datestart, bufCour!!.unlimited,
                typeEndOccu, typeEndDate, bufCour!!.nbrerepeat, bufCour!!.timeend,
                bufCour!!.daysofweek, bufCour!!.dayofweekormonth, bufCour!!.dayweekinmonth)















       // Log.e("addStatement", "save this is buf->" + bufCour.nomsystem+"_the ID->"+idCour)



        var docRef: DocumentReference?=null
        var updateOne=false
        var isAllNew=true

        if(bufCour!!.id!=null){

            docRef=fsEdit.getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF).document(bufCour!!.id)
            updateOne=true
            isAllNew=false
        }
        else{
            docRef=fsEdit.getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF).document()
        }

        //update ID in case it wasn't already one
        bufCour!!.id=docRef!!.id

        var listner = OnCompleteListener<Void> { task ->
            /*if (progressDialog != null && progressDialog.isShowing()) {

                progressDialog.dismiss()
            }*/

            if (task.isSuccessful) {

                //Toast.makeText(context, getString(R.string.op_completed), Toast.LENGTH_SHORT).show()
                Snackbar.make(rootView, getString(R.string.op_completed), Snackbar.LENGTH_SHORT).show()
                //Log.d(TAG, "Write batch succeeded.");
                //closefragment()
            } else {
                //Toast.makeText(context, getString(R.string.op_failed) + "->" + task.exception, Toast.LENGTH_SHORT).show()
                Log.w("AddStatement", "write batch failed.", task.exception)

                //hideKeyboard()
                Snackbar.make(rootView, getString(R.string.op_failed), Snackbar.LENGTH_SHORT).show()
            }
        };

        var jsnObj=JSONObject(MyJsonParserKotlin.onTimeNotifToJsonString(bufCour))

        val tok = StringTokenizer(bufCour.reminderconf, "#")
        val reminderList = java.util.ArrayList<String>()
        if (tok != null && tok.hasMoreElements()) {

            while (tok.hasMoreTokens()) {

                reminderList.add(tok.nextToken())
            }
        }
        // jsonObject.accumulate("statement", values);
        val jsArrayReminder = JSONArray(reminderList)
        var jsAray = JSONArray(repeatingTime)
        jsnObj.putOpt("listNotif", jsAray)
        jsnObj.putOpt("listReminder", jsArrayReminder)


        if(updateOne){


            val builder = AlertDialog.Builder(this.activity)

            // Set the alert dialog title
            builder.setTitle(getString(R.string.mode_update))

            // Display a message on alert dialog
            builder.setMessage(getString(R.string.how_update_notif))

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(getString(R.string.onback_yes)){dialog, which ->
                // Do something when user press the positive button
                //Toast.makeText(this.context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()

                showProgresBarAndInsertInDB(bufStat, jsnObj,false,bufCour,false)
                // Change the app background color
                //root_layout.setBackgroundColor(Color.RED)
            }


            // Display a negative button on alert dialog
            builder.setNegativeButton(getString(R.string.onback_no)){dialog,which ->
                //Toast.makeText(this.context,"You are not agree.",Toast.LENGTH_SHORT).show()
                showProgresBarAndInsertInDB(bufStat, jsnObj,true,bufCour,false)
            }


            // Display a neutral button on alert dialog
            /* builder.setNeutralButton("Cancel"){_,_ ->
                 Toast.makeText(this.context,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
             }*/

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()



        }
        else{

            showProgresBarAndInsertInDB(bufStat, jsnObj,false,bufCour,isAllNew)
        }



        //fsEdit.addRecord(docRef,bufCour,listner)


        //closefragment()
    }



    private fun checkIfUpdateOne():Boolean{

        var res=false

        val builder = AlertDialog.Builder(this.activity)

        // Set the alert dialog title
        builder.setTitle("App background color")

        // Display a message on alert dialog
        builder.setMessage("Are you want to set the app background color to RED?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(this.context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
            res=true
            // Change the app background color
            //root_layout.setBackgroundColor(Color.RED)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No"){dialog,which ->
            Toast.makeText(this.context,"You are not agree.",Toast.LENGTH_SHORT).show()
            res=false
        }


        // Display a neutral button on alert dialog
       /* builder.setNeutralButton("Cancel"){_,_ ->
            Toast.makeText(this.context,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
        }*/

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

        return res
    }


    private fun closefragment() {

        val inten= Intent(this.context,NotifListActivity::class.java);
        startActivity(inten);

    }


    override fun onSaveInstanceState(outState: Bundle?) {

        val saveBuf=MyJsonParserKotlin.onTimeNotifToJsonString(bufCour)

        outState!!.putString(ARG_OBJ_AS_JSON,saveBuf)

        super.onSaveInstanceState(outState)
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
         val ARG_OBJ_AS_JSON = "objAsJson"
         val ARG_SENS = "sens"
         val ARG_IS_VERTICAL = "isVertical"
         val ARG_DATE_START_PARAM = "dateStartParam"
         val ARG_ID_CAT = "idCat"
         val ARG_LINE_BACK = "lineBack"
         val ARG_ID_STAT = "idStat"
         val ARG_DATE_COUR = "datecour"


        fun newInstance(objAsJson: String, sens:String, dateStartParam:String,idcat:String, idStat:String,datCour:Long): NotifDetailFragment {
            val fragment = NotifDetailFragment()
            val args = Bundle()
            args.putString(NotifDetailFragment.ARG_OBJ_AS_JSON, objAsJson)
            args.putString(ARG_SENS,sens)
            args.putString(ARG_ID_CAT,idcat)
            args.putString(ARG_DATE_START_PARAM,dateStartParam)
            args.putString(ARG_ID_STAT,idStat)
            args.putLong(ARG_DATE_COUR,datCour)
            fragment.arguments = args

            Log.e("NotifDetailFragment", "Companigon ->"+dateStartParam)

            return fragment
        }
    }



}
