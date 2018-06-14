package com.gmb.bbm2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmb.bbm2.tools.allstatic.AllStaticKt;
import com.gmb.bbm2.tools.customviews.MySpinnerAsTextInputLayout;
import com.gmb.bbm2.tools.utils.UIeventManager;
import com.gmb.gmbdatepickerpro.DatePickerPro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;

import static android.view.View.VISIBLE;

/**
 * Created by GMB on 4/3/2016.
 */
public class MyRepeatDialog extends AlertDialog implements View.OnClickListener,
        Spinner.OnItemSelectedListener,RadioGroup.OnCheckedChangeListener,View.OnTouchListener,
        MySpinnerAsTextInputLayout.OnValueSelectedListner{


    long timeStart=0;
    long timeEnd=0;
    int nbreRepeat;

    ArrayList<Integer> dayOfWeek;
    int dayOfWeekOrMonth,dayWeekInMonth;
    boolean dayOfMonthOrWeek=false;

    String repeatOn;
    String typeend="";
    ArrayList<Long> repeatingTime;
    int freqRepeat=0;
    int unlimited=0;

    private Button cmdCancel,cmdDone;
    private MySpinnerAsTextInputLayout cmbRepeatUnit,cmbRepeatFreq,cmbRepeatEnd;
    private CheckBox chkMon,chkTue,chkWed,chkThr,chkFri,chkSat,chkSun;
    private TextView txtFreqLabel,txtRepeatFreqUnit;
    private TextInputLayout lytNbreRepeat,lytDateEnd;

    SimpleDateFormat datFor;
    SimpleDateFormat hourFor;
    private EditText txtNbreOccu;
    private TextView txtDateEnd;
    private TextView txtDateEndVal;
    private RadioButton rdbByDayOfMonth,rdbByDayOfWeek;

    private String repeatUnit="";
    private String repeatFreq="";
    private Calendar cal;
    private String[] repeatUnitVal;
    private ArrayList<String> valExclude;
    private ArrayList<String> valRepeatEnd;
    private int day,month,year;
    private int hour;
    private int min;
    private ArrayList<String> reminderList;
    private boolean reminderSet=false;
    String reminderConf;

    TextView txtTitle;


    ConstraintLayout lytRmder1,lytRmder2,lytRmder3;
    TextView txtRmderType1,txtRmderType2,txtRmderType3,txtRmderVal1,txtRmderVal2,txtRmderVal3,txtAddNewReminder;
    TextView txtRmderAff1,txtRmderAff2,txtRmderAff3;
    ImageButton cmdRmder1,cmdRmder2,cmdRmder3;
    TextView txtTimeStart,txtTimeStartVal;


    private RelativeLayout lytRepeat;

    private ConstraintLayout lytRepeatEnd,lytRepeatBy,lytRepeatOnDay;
    private MyRepeatDialogListener mListener;


    public MyRepeatDialog(Context context, MyRepeatDialogListener listener, String repeatUnit,
                          String typeend,
                          String repeatOn,
                          int dayOfMonthOrWeek,
                          int dayOfWeekOrMonth,
                          int dayWeekInMonth,
                          int freqRepeat,
                          int nbreRepeat,
                          int unlimited,
                          long timeStart,
                          long timeEnd,
                          String reminderConf) {

        super(context);

        mListener=listener;
        this.reminderConf=reminderConf;

        this.timeStart = timeStart;
        this.repeatUnit=repeatUnit;
        this.repeatOn=repeatOn;

        //Toast.makeText(getContext(),"this is typeend->"+typeend,Toast.LENGTH_SHORT).show();

        this.typeend = typeend;
        if(this.typeend==null || this.typeend.equalsIgnoreCase("")) this.typeend= UIeventManager.VAL_REPEAT_UNLIMITED;
        this.dayOfMonthOrWeek= dayOfMonthOrWeek==1;

        this.dayOfWeekOrMonth=dayOfWeekOrMonth;

        this.dayWeekInMonth=dayWeekInMonth;
        this.repeatFreq=freqRepeat+"";
        this.nbreRepeat=nbreRepeat;
        this.unlimited=unlimited;


        this.timeEnd=timeEnd;


        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        if(this.timeEnd<cal.getTimeInMillis()){
            cal.setTimeInMillis(timeStart);
            cal.add(Calendar.DAY_OF_YEAR,1);
            this.timeEnd=cal.getTimeInMillis();

        }

        // Supply num input as an argument.

        this.setView(initView());


    }

    private MyReminderDialog.MyReminderListener reminderListner=new MyReminderDialog.MyReminderListener() {
        @Override
        public void onMyReminderFragment(long timeStart, String content) {

            updateValFromReminderDiaog(timeStart,content);
        }

    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            /*int hour;
            String am_pm;
            if (hourOfDay > 12) {
                hour = hourOfDay - 12;
                am_pm = "PM";
            } else {
                hour = hourOfDay;
                am_pm = "AM";
            }*/


            cal.setTimeInMillis(timeStart);
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
            /*cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);*/

            timeStart=cal.getTimeInMillis();
            txtTimeStart.setText(hourFor.format(cal.getTime()));
            //txtTimeStart.setText(hour + " : " + minute + " " + am_pm);
            txtTimeStartVal.setText(""+cal.getTimeInMillis());
            //System.out.println("BbmStatementDetailActivity voici la val de hour de la UI->"+cal.getTimeInMillis());
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP){

            ////Log.e("AddStatemet","onTouch Event this is view->"+v);


            switch (v.getId()){


                case R.id.bbm_stat_edit_txt_dateend:
                    popUpDateDialog();
                    break;

                case R.id.txt_time_start:
                    popUpTimer();
                    break;

            }

            return true;
        }
        return false;
    }




    public void setDateChoosen(long newDate){

        cal.setTimeInMillis(newDate);
        txtDateEndVal.setText("" + cal.getTimeInMillis());
        txtDateEnd.setText(datFor.format(cal.getTime()));
    }


    private View initView() {

        Context themeContext = this.getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        View rootView = inflater.inflate(R.layout.repeat_dialog, null);



        chkMon = (CheckBox) rootView.findViewById(R.id.chkMonday);
        chkTue = (CheckBox) rootView.findViewById(R.id.chkTuesday);
        chkWed = (CheckBox) rootView.findViewById(R.id.chkWedn);
        chkThr = (CheckBox) rootView.findViewById(R.id.chkThursday);
        chkFri = (CheckBox) rootView.findViewById(R.id.chkFriday);
        chkSat= (CheckBox) rootView.findViewById(R.id.chkSaturday);
        chkSun = (CheckBox) rootView.findViewById(R.id.chkSunday);
        //chkNoend = (CheckBox) rootView.findViewById(R.id.bbm_stat_noend);


        cmdCancel = (Button) rootView.findViewById(R.id.headCmdCancel);
        cmdDone = (Button) rootView.findViewById(R.id.headCmdOk);

        txtNbreOccu = (EditText) rootView.findViewById(R.id.txt_nbr_repeat);
        txtDateEnd = (TextView) rootView.findViewById(R.id.bbm_stat_edit_txt_dateend);
        txtDateEndVal = (TextView) rootView.findViewById(R.id.bbm_stat_edit_txt_dateend_val);

        txtTitle = (TextView) rootView.findViewById(R.id.headTxtTitle);

        txtTimeStart = (TextView) rootView.findViewById(R.id.txt_time_start);
        txtTimeStartVal = (TextView) rootView.findViewById(R.id.txt_time_start_val);

        txtRepeatFreqUnit = (TextView) rootView.findViewById(R.id.txt_repeat_freq_unit);

        lytRepeat=(RelativeLayout) rootView.findViewById(R.id.bbm_stat_lyt_repeat);
        lytRepeatOnDay=(ConstraintLayout) rootView.findViewById(R.id.repeat_stat_lyt_repeat_on);
        lytRepeatBy=(ConstraintLayout) rootView.findViewById(R.id.lyt_type_repeat_month);
        lytRepeatEnd=(ConstraintLayout) rootView.findViewById(R.id.lyt_row_2);
        //lytRepeatFreq=(ConstraintLayout) rootView.findViewById(R.id.lyt_repeat_freq);

        cmbRepeatUnit=(MySpinnerAsTextInputLayout) rootView.findViewById(R.id.cmb_repeat_unit);
        cmbRepeatEnd=(MySpinnerAsTextInputLayout) rootView.findViewById(R.id.cmb_repeat_end);
        cmbRepeatFreq=(MySpinnerAsTextInputLayout) rootView.findViewById(R.id.cmb_repeat_freq);
        txtFreqLabel = (TextView) rootView.findViewById(R.id.txt_repeat_freq_label);
        repeatUnitVal =this.getContext().getResources().getStringArray(R.array.repeat_units_val2);

        lytNbreRepeat=(TextInputLayout) rootView.findViewById(R.id.txtNbreToEnd);
        lytDateEnd=(TextInputLayout) rootView.findViewById(R.id.txtDateToEnd);



        rdbByDayOfMonth=(RadioButton) rootView.findViewById(R.id.rdbRepeatByDayOfMonth);
        rdbByDayOfWeek=(RadioButton) rootView.findViewById(R.id.rdbRepeatByDayOfWeekInMonth);

        lytNbreRepeat.setVisibility(View.GONE);
        lytDateEnd.setVisibility(View.GONE);

        rdbByDayOfMonth.setChecked(true);

        String[] exclude =this.getContext().getResources().getStringArray(R.array.repeat_units_val_exclude);
        valExclude=new ArrayList<>();
        //Collections.addAll(valExclude, exclude);

        exclude =this.getContext().getResources().getStringArray(R.array.repeat_end_val);
        valRepeatEnd=new ArrayList<>();
        Collections.addAll(valRepeatEnd, exclude);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);


        cmdCancel.setOnClickListener(this);
        cmdDone.setOnClickListener(this);



        txtDateEnd.setOnTouchListener(this);
        txtTimeStart.setOnClickListener(this);
        rdbByDayOfMonth.setOnClickListener(this);
        rdbByDayOfWeek.setOnClickListener(this);

         cmbRepeatFreq.setValListner(this);
        cmbRepeatUnit.setValListner(this);
        cmbRepeatEnd.setValListner(this);

        datFor=new SimpleDateFormat(this.getContext().getString(R.string.date_pattern));
        hourFor=new SimpleDateFormat(this.getContext().getString(R.string.hour_pattern));


        String[] freqCode =this.getContext().getResources().getStringArray(R.array.repeat_units_val2);
        ArrayList<Object> freqCodeArr=new ArrayList<>();
        Collections.addAll(freqCodeArr,freqCode);

        String[] freqVal =this.getContext().getResources().getStringArray(R.array.repeat_units2);
        ArrayList<Object> freqValArr=new ArrayList<>();
        Collections.addAll(freqValArr,freqVal);

        cmbRepeatUnit.setSpinListVal(freqCodeArr,freqValArr);
        //On set les val recus si deja call

        cal.setTimeInMillis(timeStart);
        txtTimeStart.setText(hourFor.format(cal.getTime()));
        //txtTimeStart.setText(hour + " : " + minute + " " + am_pm);
        txtTimeStartVal.setText(""+cal.getTimeInMillis());

        txtTitle.setText(R.string.repeat_dialog_title);

            cmbRepeatUnit.setSelectedIndex(1);

         int in=cmbRepeatUnit.getIndexOf(repeatFreq);
            if(in!=-1) cmbRepeatUnit.setSelectedIndex(in);

            cmbRepeatFreq.setSelectedIndex(0);

            in=cmbRepeatFreq.getIndexOf(repeatFreq);
            if(in!=-1) cmbRepeatFreq.setSelectedIndex(in);


        String[] endCode =this.getContext().getResources().getStringArray(R.array.repeat_end_val);
        ArrayList<Object> endCodeArr=new ArrayList<>();
        Collections.addAll(endCodeArr,endCode);

        String[] endVal =this.getContext().getResources().getStringArray(R.array.repeat_end);
        ArrayList<Object> endValArr=new ArrayList<>();
        Collections.addAll(endValArr,endVal);

        cmbRepeatEnd.setSpinListVal(endCodeArr,endValArr,"1");


        cmbRepeatEnd.setSelectedIndex(0);

        in=cmbRepeatEnd.getIndexOf(typeend);
        if(in!=-1) cmbRepeatEnd.setSelectedIndex(in);





if(repeatOn!=null && !repeatOn.equalsIgnoreCase("")){


    StringTokenizer tok=new StringTokenizer(repeatOn,"-");
    while (tok.hasMoreTokens()){

        int val=Integer.parseInt(tok.nextToken());

        switch (val){

            case 1:
                chkSun.setChecked(true);
                break;
            case 2:
                chkMon.setChecked(true);
                break;
            case 3:
                chkTue.setChecked(true);
                break;
            case 4:
                chkWed.setChecked(true);
                break;
            case 5:
                chkThr.setChecked(true);
                break;
            case 6:
                chkFri.setChecked(true);
                break;
            case 7:
                chkSat.setChecked(true);
                break;

        }
    }
}
else{//on set un par defaut

    GregorianCalendar ca=new GregorianCalendar();
    ca.setTimeInMillis(timeStart);
    int jour=ca.get(Calendar.DAY_OF_WEEK);

    switch (jour){

        case 1:
            chkSun.setChecked(true);
            break;
        case 2:
            chkMon.setChecked(true);
            break;
        case 3:
            chkTue.setChecked(true);
            break;
        case 4:
            chkWed.setChecked(true);
            break;
        case 5:
            chkThr.setChecked(true);
            break;
        case 6:
            chkFri.setChecked(true);
            break;
        case 7:
            chkSat.setChecked(true);
            break;

    }
}


            if(!dayOfMonthOrWeek){

                rdbByDayOfWeek.setChecked(true);
            }
            else{

                rdbByDayOfMonth.setChecked(true);
            }


            nbreRepeat= (nbreRepeat<1)? 1:nbreRepeat;


            txtNbreOccu.setText(nbreRepeat+"");

            cal.setTimeInMillis(timeEnd);
            txtDateEndVal.setText("" + cal.getTimeInMillis());
            txtDateEnd.setText(datFor.format(cal.getTime()));


        showHideCmbFreqOld();

        /**************************************Part ReminderFB*************************************************/
        reminderList=new ArrayList<>();

        lytRmder1=(ConstraintLayout) rootView.findViewById(R.id.bbm_stat_lyt_reminder_01);
        txtRmderType1=(TextView) rootView.findViewById(R.id.txt_reminder_011);
        txtRmderVal1=(TextView) rootView.findViewById(R.id.txt_reminder_012);
        txtRmderAff1=(TextView) rootView.findViewById(R.id.txt_reminder_013);
        cmdRmder1=(ImageButton) rootView.findViewById(R.id.cmd_reminder_011);
        cmdRmder1.setOnClickListener(this);

        lytRmder2=(ConstraintLayout) rootView.findViewById(R.id.bbm_stat_lyt_reminder_02);
        txtRmderType2=(TextView) rootView.findViewById(R.id.txt_reminder_021);
        txtRmderVal2=(TextView) rootView.findViewById(R.id.txt_reminder_022);
        txtRmderAff2=(TextView) rootView.findViewById(R.id.txt_reminder_023);
        cmdRmder2=(ImageButton) rootView.findViewById(R.id.cmd_reminder_021);
        cmdRmder2.setOnClickListener(this);

        lytRmder3=(ConstraintLayout) rootView.findViewById(R.id.bbm_stat_lyt_reminder_03);
        txtRmderType3=(TextView) rootView.findViewById(R.id.txt_reminder_031);
        txtRmderVal3=(TextView) rootView.findViewById(R.id.txt_reminder_032);
        txtRmderAff3=(TextView) rootView.findViewById(R.id.txt_reminder_033);
        cmdRmder3=(ImageButton) rootView.findViewById(R.id.cmd_reminder_031);
        cmdRmder3.setOnClickListener(this);

        txtAddNewReminder=(TextView) rootView.findViewById(R.id.txt_add_new_reminder);
        txtAddNewReminder.setOnClickListener(this);



        // Show soft keyboard automatically
        /*mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        */
        return rootView;
    }

    public void popUpTimer(){

        Log.e("MyRepeatDialog","popUpTimer must be shown now");
        cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStart);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        Dialog dialog=new TimePickerDialog(getContext(), timePickerListener, hour, min, false);
        dialog.show();
    }


    private void showHideCmbFreqOld(){

        if(UIeventManager.VAL_NONE.equalsIgnoreCase(repeatUnit)){

            cmbRepeatFreq.setVisibility(View.GONE);
            txtFreqLabel.setVisibility(View.GONE);
            txtRepeatFreqUnit.setVisibility(View.GONE);

            lytRepeatOnDay.setVisibility(View.GONE);
            lytRepeatBy.setVisibility(View.GONE);
            lytRepeatEnd.setVisibility(View.GONE);

        }
        else{

            cmbRepeatFreq.setVisibility(VISIBLE);
            txtFreqLabel.setVisibility(VISIBLE);
            txtRepeatFreqUnit.setVisibility(VISIBLE);


            //lytRepeatOnDay.setVisibility(View.VISIBLE);
            lytRepeatEnd.setVisibility(VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {

        //System.err.println("\n\n\n\nBbmStatementDetail2Activity onclick avec view->"+v.getId()+"Et ToString"+v.toString());
        int id=v.getId();
        switch(id){

            case R.id.cmd_reminder_011:

                deleteReminder(0);

                break;

            case R.id.cmd_reminder_021:

                deleteReminder(1);

                break;

            case R.id.cmd_reminder_031:

                deleteReminder(2);

                break;


            case R.id.txt_add_new_reminder:

                showDialogReminder();

                break;

            /*case R.id.bbm_stat_stat_chk_repeat:
                UIeventManager.showHideView(chkRepeat.isChecked(), lytRepeat);
                break;*/


            case R.id.bbm_stat_edit_txt_dateend:
                popUpDateDialog();
                break;

            case R.id.headCmdCancel:

                mListener.onRepeatEdited( null,
                        typeend,
                        dayOfWeek,
                        dayOfMonthOrWeek,
                        dayOfWeekOrMonth,
                        dayWeekInMonth,
                        freqRepeat,
                        nbreRepeat,
                        unlimited,
                        timeStart,
                        timeEnd,"" );
                this.dismiss();
                break;

            case R.id.headCmdOk:
                saveState();

                break;

            case R.id.txt_time_start:
                popUpTimer();
                break;


            default:

        }

    }

    public void updateValFromReminderDiaog(long firstEventTime,String val){

        //Log.e("addStatement","updateValFromReminderDialog avec val->"+val);

        if(val!=null){

            String[] tab=val.split("-");

            txtRmderType1.setText(tab[0]);
            txtRmderVal1.setText(tab[1]);

            if(getDateHourOfRepeat(Integer.parseInt(tab[0]),Integer.parseInt(tab[1]))>= System.currentTimeMillis()){

                reminderList.add(val);

                updateViewReminderPart();
                Toast.makeText(getContext(),this.getContext().getString(R.string.reminder_added),Toast.LENGTH_SHORT).show();
            }
            else {

                Toast.makeText(getContext(),this.getContext().getString(R.string.reminder_overdue),Toast.LENGTH_SHORT).show();
            }
        }

        reminderSet = reminderList.size() > 0;

    }

    public void deleteReminder(int pos){

        if(pos<reminderList.size()){

            reminderList.remove(pos);
            updateViewReminderPart();
        }
    }

    void showDialogReminder() {
        //int mStackLevel= new Random().nextInt(100);



        Dialog dialog=new MyReminderDialog(getContext(),reminderListner,timeStart,"");
        dialog.show();
    }

    public long getDateHourOfRepeat(int calendarPeriod,int nbreUniteBefore){

        GregorianCalendar cal2=new GregorianCalendar();
        cal2.setTimeInMillis(timeStart);
        switch (calendarPeriod){

            case Calendar.MINUTE:
                cal2.add(Calendar.MINUTE,-nbreUniteBefore);
                break;

            case Calendar.HOUR:
                cal2.add(Calendar.HOUR,-nbreUniteBefore);
                break;

            case Calendar.DAY_OF_YEAR:
                cal2.add(Calendar.DAY_OF_YEAR,-nbreUniteBefore);
                break;

            case Calendar.WEEK_OF_YEAR:
                cal2.add(Calendar.WEEK_OF_YEAR,-nbreUniteBefore);
                break;

            case Calendar.MONTH:
                cal2.add(Calendar.MONTH,-nbreUniteBefore);
                break;

            case Calendar.YEAR:
                cal2.add(Calendar.YEAR,-nbreUniteBefore);
                break;
        }

        return cal2.getTimeInMillis();
    }

    public String getNomPeriodeCorrespondant(int calendarPeriod){

        String val=this.getContext().getString(R.string.hour_str);

        switch (calendarPeriod){

            case Calendar.MINUTE:
                val=this.getContext().getString(R.string.minute_str);
                break;

            case Calendar.HOUR:
                val=this.getContext().getString(R.string.hour_str);
                break;

            case Calendar.DAY_OF_YEAR:
                val=this.getContext().getString(R.string.day_str);
                break;

            case Calendar.WEEK_OF_YEAR:
                val=this.getContext().getString(R.string.week_str);
                break;

            case Calendar.MONTH:
                val=this.getContext().getString(R.string.month_str);
                break;

            case Calendar.YEAR:
                val=this.getContext().getString(R.string.year_str);
                break;
        }

        return val;
    }

    public void updateViewReminderPart(){

        //hidding all reminders in the view
        lytRmder1.setVisibility(View.GONE);
        lytRmder2.setVisibility(View.GONE);
        lytRmder3.setVisibility(View.GONE);

        //Log.e("addStatement","updateViewReminderPart ");

        if(reminderList!=null){

            int tail=reminderList.size();

            if(tail<3) txtAddNewReminder.setVisibility(VISIBLE);
            else txtAddNewReminder.setVisibility(View.GONE);

            for(int i=0;i<tail;i++){

                String buf=reminderList.get(i);

                String[] tab=buf.split("-");



                SimpleDateFormat sim=new SimpleDateFormat(this.getContext().getString(R.string.date_pattern)+" "+this.getContext().getString(R.string.hour_pattern));
                String repeatOn=sim.format(new Date(getDateHourOfRepeat(Integer.parseInt(tab[0]),Integer.parseInt(tab[1]))));
                String nomPeriode=getNomPeriodeCorrespondant(Integer.parseInt(tab[0]))+"(s)";
                String myFormattedString = String.format(this.getContext().getString(R.string.show_reminder_infos), repeatOn, tab[1],nomPeriode);

                //Log.e("StatementFragment", "updateViewReminderPart voici une line->"+buf);

                switch (i){

                    case 0:

                        txtRmderType1.setText(tab[0]);
                        txtRmderVal1.setText(tab[1]);

                        txtRmderAff1.setText(myFormattedString);
                        lytRmder1.setVisibility(VISIBLE);

                        break;


                    case 1:
                        buf=reminderList.get(i);
                        tab=buf.split("-");

                        txtRmderType2.setText(tab[0]);
                        txtRmderVal2.setText(tab[1]);

                        txtRmderAff2.setText(myFormattedString);
                        lytRmder2.setVisibility(VISIBLE);

                        break;



                    case 2:
                        buf=reminderList.get(i);
                        tab=buf.split("-");

                        txtRmderType3.setText(tab[0]);
                        txtRmderVal3.setText(tab[1]);

                        txtRmderAff3.setText(myFormattedString);
                        lytRmder3.setVisibility(VISIBLE);

                        break;
                }
            }

        }
    }
    private void popUpDateDialog() {

        cal = Calendar.getInstance();

        Dialog dialog=new DatePickerPro(this.getContext(), datePickedListener, cal.getTimeInMillis(),false);
        dialog.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        /*Toast.makeText(parent.getContext(), (String) parent.getItemAtPosition(position) + " et la val reelle->" + repeatUnitVal[position],
                Toast.LENGTH_SHORT).show();*/


        ////Log.i("item", (String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Toast.makeText(parent.getContext(), "BbmStatementDetail2 no item selected",
                Toast.LENGTH_SHORT).show();
        ////Log.i("BbmStatementDetail2" , "Nothing selected");
    }







    private DatePickerDialog.OnDateSetListener dateEndPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            cal.set(selectedYear, selectedMonth, selectedDay);
            txtDateEndVal.setText("" + cal.getTimeInMillis());
            txtDateEnd.setText(datFor.format(cal.getTime()));

            //System.out.println("BbmStatementDetailActivity voici la val de lUI->" + cal.getTimeInMillis());
        }
    };


    private DatePickerPro.OnDatePickedListener datePickedListener =new DatePickerPro.OnDatePickedListener() {
        @Override
        public void datePicked( long datePicked) {

            cal.setTimeInMillis(datePicked);
            txtDateEndVal.setText("" + cal.getTimeInMillis());
            txtDateEnd.setText(datFor.format(cal.getTime()));
        }
    };




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    public void saveState() {

        Log.e("MyRepeaat","saveState pos->0");
        //String category = (String) mCategory.getSelectedItem();
        String nbrerepeatstr = txtNbreOccu.getText().toString();


         unlimited=UIeventManager.VAL_REPEAT_UNLIMITED.equalsIgnoreCase(typeend)?1:0;


         dayWeekInMonth=0;




        cal.setTimeInMillis(timeStart);
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int m=cal.get(Calendar.MINUTE);



         nbreRepeat=0;
         freqRepeat=0;

        try{
            timeEnd=Long.parseLong(txtDateEndVal.getText().toString());
        }
        catch (Exception e){

            timeEnd=timeStart;
        }

        if(UIeventManager.VAL_REPEAT_UNTIL.equalsIgnoreCase(typeend)){

            timeEnd=Long.valueOf(txtDateEndVal.getText().toString());
        }
        else if(UIeventManager.VAL_REPEAT_OCCURENCE.equalsIgnoreCase(typeend)){

            nbreRepeat=Integer.valueOf(txtNbreOccu.getText().toString());
        }



            try{
                freqRepeat=Integer.parseInt(repeatFreq);
            } catch (Exception e){

            }

             dayOfWeek=new ArrayList<>();
             dayOfWeekOrMonth=0;

            if(repeatUnit.equalsIgnoreCase(UIeventManager.VAL_WEEK)){

                dayOfWeekOrMonth=Calendar.DAY_OF_WEEK;

                if(chkSun.isChecked()){

                    dayOfWeek.add(1);
                }

                if(chkMon.isChecked()){

                    dayOfWeek.add(2);
                }

                if(chkTue.isChecked()){

                    dayOfWeek.add(3);
                }

                if(chkWed.isChecked()){

                    dayOfWeek.add(4);
                }

                if(chkThr.isChecked()){

                    dayOfWeek.add(5);
                }

                if(chkFri.isChecked()){

                    dayOfWeek.add(6);
                }

                if(chkSat.isChecked()){

                    dayOfWeek.add(7);
                }


            }
            else if(repeatUnit.equalsIgnoreCase(UIeventManager.VAL_MONTH)){

                dayOfMonthOrWeek=rdbByDayOfMonth.isChecked();

                if(dayOfMonthOrWeek){




                    dayOfWeekOrMonth=Calendar.DAY_OF_MONTH;

                }
                else{

                    dayOfWeekOrMonth=Calendar.DAY_OF_WEEK_IN_MONTH;
                    dayWeekInMonth=Calendar.DAY_OF_WEEK;
                    cal.setTimeInMillis(timeStart);
                    dayOfWeek.add(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));

                }



            }


            /*repeatingTime=UIeventManager.generateRepeatOccurence(repeatUnit,freqRepeat,timeStart,rdbRepeatUnlimited.isChecked(),
                    rdbRepeatOccu.isChecked(),rdbRepeatUpTo.isChecked(),nbreRepeat,timeEnd,

                    dayOfWeek,dayOfWeekOrMonth,dayWeekInMonth);*/




        String reminderStr="";
        if(reminderList!=null && reminderList.size()>0){
            StringBuilder buil=new StringBuilder();
            Iterator<String> it=reminderList.iterator();
            while (it.hasNext()){

                buil.append(it.next());
                buil.append("#");
            }
            reminderStr=buil.toString();
        }
        mListener.onRepeatEdited( repeatUnit,
                typeend,
                dayOfWeek,
                dayOfMonthOrWeek,
                dayOfWeekOrMonth,
                dayWeekInMonth,
                freqRepeat,
                nbreRepeat,
                unlimited,
                timeStart,
                timeEnd ,reminderStr);

        this.dismiss();

    }

    @Override
    public void getSelectedValue(Object selectedValue, MySpinnerAsTextInputLayout customSpinner) {

        Log.e("NotifDetail", "onItemSelected et val->"+selectedValue);



        if(customSpinner==cmbRepeatUnit){


            String valReel=selectedValue.toString();
            lytRepeatEnd.setVisibility(View.GONE);
            lytRepeatOnDay.setVisibility(View.GONE);
            lytRepeatBy.setVisibility(View.GONE);

            repeatUnit=valReel;
            String repeatFreqUnit=getContext().getString(R.string.days);
            ArrayList<Object> dataVal=new ArrayList<Object>(Arrays.asList(this.getContext().getResources().getStringArray(R.array.repeat_frequency)));
            if(UIeventManager.VAL_HOUR.equalsIgnoreCase(repeatUnit)){

                dataVal=new ArrayList<>();
                for(int i=1;i<47;i++){


                    dataVal.add(""+i);
                }
            }
            else if(UIeventManager.VAL_DAY.equalsIgnoreCase(repeatUnit)){

                dataVal=new ArrayList<>();
                for(int i=1;i<59;i++){


                    dataVal.add(""+i);
                }
            }
            else if(UIeventManager.VAL_WEEK.equalsIgnoreCase(repeatUnit)){

                repeatFreqUnit=getContext().getString(R.string.week_str);
            }
            else if(UIeventManager.VAL_MONTH.equalsIgnoreCase(repeatUnit)){


                repeatFreqUnit=getContext().getString(R.string.month_str);
            }
            else if(UIeventManager.VAL_YEAR.equalsIgnoreCase(repeatUnit)){


                repeatFreqUnit=getContext().getString(R.string.year_str);
            }
            else if(UIeventManager.VAL_NONE.equalsIgnoreCase(repeatUnit)){

                cmbRepeatFreq.setVisibility(View.GONE);
                txtFreqLabel.setVisibility(View.GONE);

                lytRepeatEnd.setVisibility(View.GONE);
            }

            txtRepeatFreqUnit.setText(repeatFreqUnit);

            /*ArrayAdapter<String> dataValAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, dataVal);
            // Drop down layout style - list view with radio button
            dataValAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cmbRepeatFreq.setAdapter(dataValAdapter);*/
            cmbRepeatFreq.setSpinListVal(dataVal,dataVal);
            cmbRepeatFreq.setSelectedIndex(0);

            UIeventManager.showHideView(false,lytRepeatBy);
            UIeventManager.showHideView(false,lytRepeatOnDay);


            if(UIeventManager.VAL_WEEK.equalsIgnoreCase(repeatUnit)){

                UIeventManager.showHideView(true,lytRepeatOnDay);
            }


            if(UIeventManager.VAL_MONTH.equalsIgnoreCase(repeatUnit)){
                UIeventManager.showHideView(true,lytRepeatBy);
            }

            showHideCmbFreqOld();
        }
        else if(customSpinner==cmbRepeatFreq){

            repeatFreq=   selectedValue.toString();
        }
        else if(customSpinner==cmbRepeatEnd) {

            String valReel = selectedValue.toString();
            lytNbreRepeat.setVisibility(View.GONE);
            lytDateEnd.setVisibility(View.GONE);

            typeend = valReel;

            if (UIeventManager.VAL_REPEAT_OCCURENCE.equalsIgnoreCase(typeend)) {


                lytNbreRepeat.setVisibility(VISIBLE);
                Log.e("MyRepeatDialog","getSelectedValue tis is typeend OCCU->"+typeend);

                txtNbreOccu.requestFocus();
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                AllStaticKt.hideSoftInput(getContext(),txtNbreOccu,true);

            }
            else if (UIeventManager.VAL_REPEAT_UNTIL.equalsIgnoreCase(typeend)) {

                lytDateEnd.setVisibility(VISIBLE);
                Log.e("MyRepeatDialog","getSelectedValue tis is typeend UNTIL->"+typeend);
                if(timeEnd>timeStart){

                    cal.setTimeInMillis(timeEnd);
                }
                else{
                    cal.add(Calendar.YEAR,1);
                }

                txtDateEnd.setText(datFor.format(cal.getTime()));
                txtDateEndVal.setText(""+cal.getTimeInMillis());

            }


        }


    }


    public interface MyRepeatDialogListener {
        void onRepeatEdited(String repeatUnit,
                            String typeend,
                            ArrayList<Integer> dayOfWeek,
                            boolean dayOfMonthOrWeek,
                            int dayOfWeekOrMonth,
                            int dayWeekInMonth,
                            int freqRepeat,
                            int nbreRepeat,
                            int unlimited,
                            long timeStart,
                            long dateEnd,
                            String reminderList);
    }

}
