package com.gmb.bbm2.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.gmb.bbm2.data.model.CategoryFB;
import com.gmb.bbm2.data.model.OnTimeNotificationFB;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by GMB on 12/4/2017.
 */

public class AddStatementViewModel extends ViewModel {
    String sens,dateStartParam,idStat,idCat,repeatOn,repeatUnit,categoryStr,typeend,currentState,reminderConf;
    int lineBack,qnte,dayOfWeekOrMonth,dayWeekInMonth,hour,min,freqRepeat,nbreRepeat;
    long datCour,dateStart,timeStart,timeEnd;
    boolean reminderSet,dayOfMonthOrWeek,unlimited;
    double pu,montant;
    GregorianCalendar cal;
    ArrayList<Integer> dayOfWeek;
    ArrayList<Long> repeatingTime;
    CategoryFB catcour;
    OnTimeNotificationFB notifCour;

    public AddStatementViewModel() {
        this.sens = "";

        this.dateStartParam = "";
        this.idStat = "";
        this.idCat = "";
        this.repeatOn = "";
        this.repeatUnit = "";
        this.categoryStr = "";
        this.typeend = "";
        this.currentState = "";
        reminderConf="";
        this.lineBack = 0;
        this.qnte = 0;
        this.dayOfWeekOrMonth = 0;
        this.dayWeekInMonth = 0;
        this.hour = 0;
        this.min = 0;
        this.freqRepeat = 1;
        this.nbreRepeat = 1;
        this.datCour = System.currentTimeMillis();
        this.dateStart = datCour;
        this.timeStart = datCour;
        this.timeEnd = datCour;
        this.reminderSet = false;
        this.dayOfMonthOrWeek = false;
        this.unlimited = false;
        this.pu = 0;
        this.montant = 0;
        this.cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        this.dayOfWeek = new ArrayList<>();
        this.repeatingTime = new ArrayList<>();
        this.catcour = null;

        notifCour=new OnTimeNotificationFB();
    }


    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getDateStartParam() {
        return dateStartParam;
    }

    public void setDateStartParam(String dateStartParam) {
        this.dateStartParam = dateStartParam;
    }

    public String getIdStat() {
        return idStat;
    }

    public void setIdStat(String idStat) {
        this.idStat = idStat;
    }

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public String getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(String repeatOn) {
        this.repeatOn = repeatOn;
    }

    public String getRepeatUnit() {
        return repeatUnit;
    }

    public void setRepeatUnit(String repeatUnit) {
        this.repeatUnit = repeatUnit;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public String getTypeend() {
        return typeend;
    }

    public void setTypeend(String typeend) {
        this.typeend = typeend;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public int getLineBack() {
        return lineBack;
    }

    public void setLineBack(int lineBack) {
        this.lineBack = lineBack;
    }

    public int getQnte() {
        return qnte;
    }

    public void setQnte(int qnte) {
        this.qnte = qnte;
    }

    public int getDayOfWeekOrMonth() {
        return dayOfWeekOrMonth;
    }

    public void setDayOfWeekOrMonth(int dayOfWeekOrMonth) {
        this.dayOfWeekOrMonth = dayOfWeekOrMonth;
    }

    public int getDayWeekInMonth() {
        return dayWeekInMonth;
    }

    public void setDayWeekInMonth(int dayWeekInMonth) {
        this.dayWeekInMonth = dayWeekInMonth;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getFreqRepeat() {
        return freqRepeat;
    }

    public void setFreqRepeat(int freqRepeat) {
        this.freqRepeat = freqRepeat;
    }

    public int getNbreRepeat() {
        return nbreRepeat;
    }

    public void setNbreRepeat(int nbreRepeat) {
        this.nbreRepeat = nbreRepeat;
    }

    public long getDatCour() {
        return datCour;
    }

    public void setDatCour(long datCour) {
        this.datCour = datCour;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
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

    public boolean isReminderSet() {
        return reminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        this.reminderSet = reminderSet;
    }

    public boolean isDayOfMonthOrWeek() {
        return dayOfMonthOrWeek;
    }

    public void setDayOfMonthOrWeek(boolean dayOfMonthOrWeek) {
        this.dayOfMonthOrWeek = dayOfMonthOrWeek;
    }

    public boolean isUnlimited() {
        return unlimited;
    }

    public void setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public GregorianCalendar getCal() {
        return cal;
    }

    public void setCal(GregorianCalendar cal) {
        this.cal = cal;
    }

    public ArrayList<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(ArrayList<Integer> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ArrayList<Long> getRepeatingTime() {
        return repeatingTime;
    }

    public void setRepeatingTime(ArrayList<Long> repeatingTime) {
        this.repeatingTime = repeatingTime;
    }

    public CategoryFB getCatcour() {
        return catcour;
    }

    public void setCatcour(CategoryFB catcour) {
        this.catcour = catcour;
    }

    public String getReminderConf() {
        return reminderConf;
    }

    public void setReminderConf(String reminderConf) {
        this.reminderConf = reminderConf;
    }

    public OnTimeNotificationFB getNotifCour() {
        return notifCour;
    }

    public void setNotifCour(OnTimeNotificationFB notifCour) {
        this.notifCour = notifCour;
    }
}




