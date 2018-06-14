package com.gmb.bbm2.tools.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmb.bbm2.R;
import com.gmb.bbm2.data.model.OnTimeNotificationFB;
import com.gmb.bbm2.tools.allstatic.AllStaticKt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by GMB on 3/21/2018.
 */

public abstract class GridCellAdapter  extends BaseAdapter implements View.OnClickListener {
    private static final String tag = "MyGridCellAdapter";
    private final Context _context;

    private List<HoldEventsForAday> list;
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private long currentDate=0;
    private boolean monthMode=false;
    private int month,year;
    private View currentDateView;

    private RelativeLayout lytGrillCell;
    private TextView txtCenterInfos,txtTopLeft,txtTopRight,txtBottomLeft,txtBottomRight;
    private HashMap<String, HoldEventsForAday> eventsPerMonthMap;

    // Days in Current Month
    public GridCellAdapter(Context context, long date,boolean monthMode) {
        super();
        this._context = context;
        //this.list = new ArrayList<String>();

        //Log.d(tag, "==> Passed in Date FOR Month: " + month + " "               + "Year: " + year);


        this.monthMode=monthMode;
        redefineGrill(date);
    }

    public GridCellAdapter(Context context, long date,boolean monthMode,boolean other) {
        super();
        this._context = context;
        //this.list = new ArrayList<String>();

        //Log.d(tag, "==> Passed in Date FOR Month: " + month + " "               + "Year: " + year);

        this.monthMode=other;
        this.monthMode=monthMode;
        redefineGrill(date);
    }


    public void redefineGrill(long date){

        list = new ArrayList<HoldEventsForAday>();
        GregorianCalendar cal=new GregorianCalendar();
        cal.setTimeInMillis(date);
        month=cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);
        currentDate=cal.getTimeInMillis();
        setCurrentDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(cal.get(Calendar.DAY_OF_WEEK));
            /*Log.d(tag, "New Calendar:= " + cal.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());*/

// Print Month


// Find Number of Events
        long d1= AllStaticKt.getFirstDayMonth(currentDate);
        long d2=AllStaticKt.getLastDayMonth(currentDate);

        if(!monthMode){

            d1=AllStaticKt.getFirstDayWeek(currentDate);
            d2=AllStaticKt.getLastDayWeek(currentDate);

        }

        printMonth21(d1, d2);
        eventsPerMonthMap = findNumberOfEventsPerPeriod(d1,d2);
    }


    private int getNumberOfDaysOfMonth(int month,int year) {

        GregorianCalendar cal=new GregorianCalendar();
        cal.setTime(new Date());

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,month);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth first day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth last day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public HoldEventsForAday getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }








    private void printMonth21(long d1,long d2) {

        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int yy=0;

        GregorianCalendar cal=new GregorianCalendar();
        cal.setTimeInMillis(d1);


        yy=cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);

        daysInMonth = getNumberOfDaysOfMonth(currentMonth,yy);

        if(!monthMode) daysInMonth=7;


        cal.setTimeInMillis(System.currentTimeMillis());
        int currentDay=cal.get(Calendar.DAY_OF_MONTH);
        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK);

        cal.setTimeInMillis(AllStaticKt.getLastDayPreviousMonth(d1));
        daysInPrevMonth=cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(d1);


            /*if(monthMode){

                cal.set(Calendar.DAY_OF_MONTH,currentDay);

            }
            else {

                cal.set(Calendar.DAY_OF_WEEK,currentWeekDay);
            }*/



            /*//Log.e(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay - 1));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);*/


        //insert days title;
        boolean isSundayFirst=AllStaticKt.isSundayFirstDayWeek(_context);

        String[] tabWeek=_context.getResources().getStringArray(R.array.week_label);

        if(isSundayFirst){

            cal.setFirstDayOfWeek(Calendar.SUNDAY);
            //cal.set(Calendar.DAY_OF_MONTH,1);





        }
        else{

            tabWeek=_context.getResources().getStringArray(R.array.week_label_Monday_first);
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            //cal.set(Calendar.DAY_OF_MONTH,1);

        }



        for (int i=0;i<7;i++){

            HoldEventsForAday buf=new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.week_title));
            buf.setDayWeekName(tabWeek[i]);
            list.add(buf);

        }


        if(monthMode){

            cal.setTimeInMillis(d1);
            cal.set(Calendar.DAY_OF_MONTH,1);
            currentWeekDay=cal.get(Calendar.DAY_OF_WEEK);
            trailingSpaces = currentWeekDay-1;
            // Trailing Month days
            cal.setTimeInMillis(d1);
            cal.add(Calendar.MONTH,-1);
            for (int i = 1; i <= trailingSpaces; i++) {

                int buf=daysInPrevMonth - trailingSpaces +i;

                /*Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));*/

                cal.set(Calendar.DAY_OF_MONTH,buf);

                list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.past_month_print_color)));
            }

// Current Month Days
            cal.setTimeInMillis(d1);
            for (int i = 1; i <= daysInMonth; i++) {
                //Log.d(currentMonthName, String.valueOf(i) + " "  + getMonthAsString(currentMonth) + " " + yy);


                cal.set(Calendar.DAY_OF_MONTH, i);

                if (i == currentDay) {

                    list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_day_month_print_color)));
                } else {

                    list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_month_print_color)));
                }
            }


// Leading Month days
            cal.setTimeInMillis(d1);
            cal.add(Calendar.MONTH,1);
            //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
            int tailMax=35-daysInMonth-trailingSpaces;
            for (int i = 0; i < tailMax; i++) {
                int buf=i+1;


                cal.set(Calendar.DAY_OF_MONTH, buf);

                list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.next_month_print_color)));
            }
        }
        else{//***************************************************************This is week mode


            cal.setTimeInMillis(System.currentTimeMillis());
            currentWeekDay=cal.get(Calendar.DAY_OF_WEEK);

            GregorianCalendar cal2=new GregorianCalendar();
            cal2.setTimeInMillis(System.currentTimeMillis());
            cal.setTimeInMillis(d2);
            int month2=cal.get(Calendar.MONTH);
            int day2=cal.get(Calendar.DAY_OF_MONTH);
            cal.setTimeInMillis(d1);
            int month1=cal.get(Calendar.MONTH);
            int day1=cal.get(Calendar.DAY_OF_MONTH);
            int grise=0;

            if(day1>=26 && month1<cal2.get(Calendar.MONTH)){

                grise=-1;
            }
            else if(day2<=6 && month2>cal2.get(Calendar.MONTH)){

                grise=1;
            }


            while (cal.getTimeInMillis()<=d2) {


                month1=cal.get(Calendar.MONTH);
                if (cal.get(Calendar.DAY_OF_WEEK)==currentWeekDay) {

                    list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_day_month_print_color)));
                } else {

                    switch (grise){

                        case -1:

                            if(month1<month2){
                                list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.past_month_print_color)));
                            }
                            else list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_month_print_color)));

                            break;

                        case 0:
                            list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_month_print_color)));

                            break;


                        case 1:
                            if(month1<month2){
                                list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.next_month_print_color)));
                            }
                            else list.add(new HoldEventsForAday(cal.getTimeInMillis(),_context.getString(R.string.current_month_print_color)));

                            break;
                    }
                }

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }


        }


    }
    /**
     * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
     * ALL entries from a SQLite database for that month. Iterate over the
     * List of All entries, and get the dateCreated, which is converted into
     * day.
     *
     * @param date1
     * @param date2
     * @return
     */
    private HashMap<String, HoldEventsForAday> findNumberOfEventsPerPeriod(long date1,
                                                                           long date2) {
        HashMap<String, HoldEventsForAday> map = new HashMap<String, HoldEventsForAday>();

        List<OnTimeNotificationFB> listBuf;

        GregorianCalendar cal=new GregorianCalendar();
        cal.setTimeInMillis(date1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        long d1=cal.getTimeInMillis();

        cal.setTimeInMillis(date2);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        long d2=cal.getTimeInMillis();





        //listBuf = dBrequest.getNotificationBetweenDatePlusState(d1,d2,_context.getString(R.string.current_state_running));

        listBuf =getNotificationParCatDateStateSens(null,null,d1,d2,"","","","",false,false);

        ////Log.e("CustomCalendar", "findNumberOfEventsPerMonsth pos 2 nbre pour le mois->" + listBuf.size());

        cal.set(year, month, 1);



        for (HoldEventsForAday hold:list){



            int nbreIn=0;
            int nbreOut=0;
            for(OnTimeNotificationFB buf:listBuf){

                if(buf.getTimestart()>=hold.getDate1() && buf.getTimestart()<=hold.getDate2()){

                    ////Log.e("CustomCalendar", "findNumberOfEventsPerMonsth pos 3 un match pour->"+hold.getDayMonth());

                    if(buf.getSens().equalsIgnoreCase(_context.getString(R.string.sens_credit)))
                        nbreIn++;
                    else
                        nbreOut++;
                }

            }

            if(nbreIn!=0 || nbreOut!=0){

                // //Log.e("CustomCalendar", "findNumberOfEventsPerMonsth pos 4 Map est MAJ");

                hold.setNbreEventIn(nbreIn);
                hold.setNbreEventOut(nbreOut);

                map.put(hold.getKeyEvent(),hold);
                // TODOdone: MAJ getView
                // TODOdone: MAJ getView
            }

            cal.add(Calendar.DAY_OF_MONTH,1);
        }




        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ////Log.e(tag,"MyCustomCalendarFragment getview pos 1");

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.fragment_custom_day_in_calendar, parent, false);
        }

// Get a reference to the Day gridcell
        lytGrillCell = (RelativeLayout) row.findViewById(R.id.lytCustomDay);
        lytGrillCell.setOnClickListener(this);

// ACCOUNT FOR SPACING

        //Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            /*String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];*/

        HoldEventsForAday holdEvent=list.get(position);

        if (holdEvent.getDayWeekName().equalsIgnoreCase("NA") && (eventsPerMonthMap != null) && (!eventsPerMonthMap.isEmpty())) {
            if (eventsPerMonthMap.containsKey(holdEvent.getKeyEvent())) {

                if(holdEvent.getNbreEventIn()>0){

                    txtBottomLeft = (TextView) row
                            .findViewById(R.id.bottomLeft);

                    txtBottomLeft.setText(""+holdEvent.getNbreEventIn());
                }

                if (holdEvent.getNbreEventOut()>0){

                    txtBottomRight = (TextView) row
                            .findViewById(R.id.bottomRight);

                    txtBottomRight.setText("" +holdEvent.getNbreEventOut());
                }


                if (holdEvent.getNbreEventRsce()>0){

                    txtTopLeft = (TextView) row
                            .findViewById(R.id.topLeft);

                    txtTopLeft.setText("" +holdEvent.getNbreEventRsce());
                }


                if (holdEvent.getNbreEventUnpaid()>0){

                    txtTopRight = (TextView) row
                            .findViewById(R.id.topRight);

                    txtTopRight.setText("" +holdEvent.getNbreEventUnpaid());
                }
            }
        }



// Set the Day GridCell
        txtCenterInfos = (TextView) row
                .findViewById(R.id.centerInfos);

        if(holdEvent.getDayWeekName().equalsIgnoreCase("NA")){

            txtCenterInfos.setText("" + holdEvent.getDayMonth());
        }
        else txtCenterInfos.setText(holdEvent.getDayWeekName());

        String theTag=""+position+"-"+holdEvent.getKeyEvent()+"-"+holdEvent.getDate1()+"-"+holdEvent.getDate2()+"-"+(new Date(holdEvent.getDate1()).toString());
        txtCenterInfos.setTag(theTag);
        lytGrillCell.setTag(theTag);

        //Log.d(tag, "Setting GridCell " + theTag);

        if (holdEvent.getDayPrintColor().equalsIgnoreCase(_context.getString(R.string.past_month_print_color))) {
            txtCenterInfos.setTextColor(_context.getResources()
                    .getColor(R.color.lightgray02));
        }
        if (holdEvent.getDayPrintColor().equalsIgnoreCase(_context.getString(R.string.current_month_print_color))) {
            txtCenterInfos.setTextColor(_context.getResources().getColor(
                    R.color.black));
        }
        if (holdEvent.getDayPrintColor().equalsIgnoreCase(_context.getString(R.string.next_month_print_color))) {
            txtCenterInfos.setTextColor(_context.getResources().getColor(R.color.lightgray02));
        }

        if (holdEvent.getDayPrintColor().equalsIgnoreCase(_context.getString(R.string.current_day_month_print_color))) {
               /* LayoutParams params = textView.getLayoutParams();
                params.height = 70;
                pf.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/


            //txtCenterInfos.setLayoutParams(new RelativeLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));;

            //txtCenterInfos.setBackground(Drawable.createFromXml((R.drawable.customshapecurrentday));

            //txtCenterInfos.setTextColor(_context.getResources().getColor(R.color.primary_dark_total));
            txtCenterInfos.setTextColor(_context.getResources().getColor(R.color.black));
            lytGrillCell.setBackgroundResource(R.drawable.back_sub_title_no_radius);


        }

        if (holdEvent.getDayPrintColor().equalsIgnoreCase(_context.getString(R.string.week_title))) {
               /* LayoutParams params = textView.getLayoutParams();
                params.height = 70;
                pf.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/


            txtCenterInfos.setLayoutParams(new RelativeLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));

            //txtCenterInfos.setBackground(Drawable.createFromXml((R.drawable.customshapecurrentday));

            txtCenterInfos.setBackgroundColor(_context.getResources().getColor(R.color.grey_600));
            txtCenterInfos.setGravity(Gravity.CENTER);

        }

        return row;
    }

    @Override
    public void onClick(View view) {
        String date_month_year = (String) view.getTag();
        //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
        ////Log.e("Selected date", date_month_year);
        try {
            //Date parsedDate = dateFormatter.parse(date_month_year);
            //Log.d(tag, "Parsed Date: " + date_month_year);

            String[] tabData=date_month_year.split("-");

            long d1=Long.parseLong(tabData[2]);
            long d2=Long.parseLong(tabData[3]);

            /*cal.setTimeInMillis(d1);
            dateCour=d1;*/

            Log.e("GridCellAdap", "onClick this is dat1->"+new Date(d1)+"_and dat2->"+new Date(d2));


            updateDayChooseEvents(d1, d2);


            if(currentDateView!=null){


                //currentDateView.setBackgroundColor(Color.TRANSPARENT);
                // currentDateView.setBackgroundColor(Color.argb(100, 200, 200, 200));
                txtCenterInfos.setTextColor(_context.getResources().getColor(R.color.black));
                currentDateView.setBackgroundResource(R.drawable.back_custom_day);

                   /* String tagIn = (String) currentDateView.getTag();
                    //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
                    ////Log.e("Selected date", date_month_year);
                    try {
                        //Date parsedDate = dateFormatter.parse(date_month_year);
                        //Log.d(tag, "Parsed Date: " + date_month_year);

                        String[] tabD = tagIn.split("-");

                        long d = Long.parseLong(tabData[2]);

                        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");

                        if(df.format(new Date(d)).equalsIgnoreCase(df.format(new Date(System.currentTimeMillis())))){

                            currentDateView.setBackgroundResource(R.drawable.back_sub_title_no_radius);
                        }
                        else{
                            currentDateView.setBackgroundResource(R.drawable.back_custom_day);
                        }
                    }
                    catch (Exception e){
                        currentDateView.setBackgroundResource(R.drawable.back_custom_day);
                    }*/


            }

            currentDateView=view;


            txtCenterInfos.setTextColor(_context.getResources().getColor(R.color.red_600));
            currentDateView.setBackgroundResource(R.drawable.back_day_cal_clicked);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void updateDayChooseEvents(long d1, long d2);


    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }




    public  List<OnTimeNotificationFB> getNotificationParCatDateStateSens(String idCat, String  idBbm, long date1, long d2, String status,
                                                                                  String sens, String sortBy, String sortType,
                                                                                  boolean paidMatters, boolean paid){

        /*updateViewFromQuery(custom.fsEdit!!.getQueryForNotifParm(MyFirestoreEditor.COLLECTION_ONTIMENOTIF,idCat,idBbm,date1,d2,status,
                sens,paidMatters,paid,sortBy,sortType, 100))*/

        return new ArrayList<OnTimeNotificationFB>();
    }








    private class HoldEventsForAday{


        String keyEvent;
        long date1,date2;
        int dayMonth,nbreEventIn,nbreEventOut,nbreEventRsce,nbreEventUnpaid;
        String dayPrintColor;
        String dayWeekName;

        public HoldEventsForAday(){

            keyEvent="";
            date1=0;
            date2=0;
            dayMonth=0;
            nbreEventIn=0;
            nbreEventOut=nbreEventRsce=nbreEventUnpaid=nbreEventIn;

            dayPrintColor="";
            dayWeekName="NA";
        }


        public HoldEventsForAday(long dateStart,String dayPrintingColor){

            GregorianCalendar cal=new GregorianCalendar();
            cal.setTimeInMillis(dateStart);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            date1=cal.getTimeInMillis();



            keyEvent=""+date1;

            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);

            date2=cal.getTimeInMillis();
            dayMonth=cal.get(Calendar.DAY_OF_MONTH);
            nbreEventIn=0;
            nbreEventOut=nbreEventRsce=nbreEventUnpaid=nbreEventIn;

            dayPrintColor=dayPrintingColor;
            dayWeekName="NA";
        }


        public String getKeyEvent() {
            return keyEvent;
        }

        public void setKeyEvent(String keyEvent) {
            this.keyEvent = keyEvent;
        }

        public long getDate1() {
            return date1;
        }

        public void setDate1(long date1) {
            this.date1 = date1;
        }

        public long getDate2() {
            return date2;
        }

        public void setDate2(long date2) {
            this.date2 = date2;
        }

        public int getDayMonth() {
            return dayMonth;
        }

        public void setDayMonth(int dayMonth) {
            this.dayMonth = dayMonth;
        }

        public int getNbreEventIn() {
            return nbreEventIn;
        }

        public void setNbreEventIn(int nbreEventIn) {
            this.nbreEventIn = nbreEventIn;
        }

        public int getNbreEventOut() {
            return nbreEventOut;
        }

        public void setNbreEventOut(int nbreEventOut) {
            this.nbreEventOut = nbreEventOut;
        }

        public int getNbreEventRsce() {
            return nbreEventRsce;
        }

        public void setNbreEventRsce(int nbreEventRsce) {
            this.nbreEventRsce = nbreEventRsce;
        }

        public int getNbreEventUnpaid() {
            return nbreEventUnpaid;
        }

        public void setNbreEventUnpaid(int nbreEventUnpaid) {
            this.nbreEventUnpaid = nbreEventUnpaid;
        }


        public String getDayPrintColor() {
            return dayPrintColor;
        }

        public void setDayPrintColor(String dayPrintColor) {
            this.dayPrintColor = dayPrintColor;
        }

        public String getDayWeekName() {
            return dayWeekName;
        }

        public void setDayWeekName(String dayWeekName) {
            this.dayWeekName = dayWeekName;
        }
    }
}
