package com.gmb.bbm2.tools.utils;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by GMB on 1/15/2016.
 */
public class UIeventManager {

    public static final String VAL_YEAR="yr";
    public static final String VAL_MONTH="mt";
    public static final String VAL_WEEK="wk";
    public static final String VAL_DAY="dl";
    public static final String VAL_HOUR="hr";
    public static final String VAL_NONE="no";

    public static final String VAL_REPEAT_UNLIMITED="unli";
    public static final String VAL_REPEAT_OCCURENCE="occu";
    public static final String VAL_REPEAT_UNTIL="date";


    public static void showHideView(boolean show,View v){

        if(show){

            v.setVisibility(View.VISIBLE);
        }
        else {

            v.setVisibility(View.GONE);
        }
    }


    public static long getRepeatInterval(String repeatUnit,int repeatFreq){


        long hour=60*60*1000;
        long res=24*hour;

        if(repeatUnit.equalsIgnoreCase(VAL_YEAR)){

            res=hour*24*365;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_MONTH)){

            res=hour*24*30;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_WEEK)){

            res=hour*24*7;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_DAY)){

            res=hour*24;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_HOUR)){

            //TODO54 : remettre la valeur de heur a sa reel valeur apres tous les tests faits avec sa valeur a minutes

            res=hour;
            //res=60*1000;
        }

        else if(repeatUnit.equalsIgnoreCase(VAL_NONE)){

            //TODO54 : remettre la valeur de heur a sa reel valeur apres tous les tests faits avec sa valeur a minutes

            res=1;
            repeatFreq=1;
            //res=60*1000;
        }





        //interval=unit*freq
        res=res*repeatFreq;

        //Log.e("UIeventMan"," getRepeatInterval le repeat unit etait->"+repeatUnit+"_et la freq->"+repeatFreq+"_donc res->"+res);

        return res;
    }

    /**
     * Genere toutes les futures date de next occurences
     * @param repeatUnit :type de repeat, week, month...
     * @param repeatFreq : interval entre 2 occurence
     * @param timeStart : date du first occurence
     * @param unlimited : type d evenement infini
     * @param occurence : fin definie par le nbre occurence
     * @param upTo : fin definie par une date
     * @param nbrOccu : nbre d'occurence avant la fin
     * @param endDate : date de fin de l'evt
     * @param dayOfWeekStr : les jours de la semaine choisies pour l'evt
     * @param dayInWeekOrMonth : TYpe de repeat dans le mois, jour du mois ou semaine*/
    public static ArrayList<Long> generateRepeatOccurence(String repeatUnit,int repeatFreq,long timeStart,
                                               boolean unlimited,boolean occurence,boolean upTo,
                                               int nbrOccu,long endDate,String dayOfWeekStr,
                                                          int dayInWeekOrMonth,int dayInWeekForMonthOnly){


        Log.e("UIeventManager","UIeventManager generateRepeatOccurence" +
                "repeatUnit->"+repeatUnit+
        "\n<repeatFreq->"+repeatFreq+
        "\n<timeStart->"+timeStart+
        "\n<unlimited->"+unlimited+
        "\n<occurence->"+occurence+
        "\n<upTo->"+upTo+
        "\n<nbrOccu->"+nbrOccu+
        "\n<endDate->"+endDate+
                "\n<dayOfWeekStr->"+dayOfWeekStr+
        "\n<dayInWeekOrMonth->"+dayInWeekOrMonth);

        long timeRepeatInHour=1;
        int facto=1;

        String[] tabStr=dayOfWeekStr.split("-");
        ArrayList<Integer> dayOfWeek=new ArrayList<>();
        for(String st:tabStr){
            try {
                dayOfWeek.add(Integer.parseInt(st));
            }
            catch (Exception ex){

            }
        }


        ArrayList<Long> result=new ArrayList<>();
        Calendar cal=new GregorianCalendar();
        Calendar cal2=new GregorianCalendar();

        cal.setTimeInMillis(timeStart);
        cal2.setTimeInMillis(timeStart);

        int chamAtraiter=Calendar.YEAR;

        if(repeatUnit.equalsIgnoreCase(VAL_YEAR)){

            chamAtraiter=Calendar.YEAR;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_MONTH)){

            chamAtraiter=Calendar.MONTH;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_WEEK)){

            chamAtraiter=Calendar.WEEK_OF_YEAR;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_DAY)){

            chamAtraiter=Calendar.DAY_OF_YEAR;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_HOUR)){

            chamAtraiter=Calendar.HOUR_OF_DAY;
            //chamAtraiter=Calendar.MINUTE;
        }
        else if(repeatUnit.equalsIgnoreCase(VAL_NONE)){

            result.add(timeStart);
            //chamAtraiter=Calendar.MINUTE;
            return result;
        }




        if(occurence){

            cal2.add(chamAtraiter,nbrOccu*repeatFreq);

        }
        else if(upTo){


            cal2.setTimeInMillis(endDate);

        }
        else if(unlimited){ //Unlimited occurence,soit jusqu'a la fin de l'annee, soit on commence avec 10, then chaque fois qu'on prend un, on ajoute un

            if(repeatUnit.equalsIgnoreCase(VAL_YEAR)){

                cal2.add(Calendar.YEAR,10);
            }

            else if(repeatUnit.equalsIgnoreCase(VAL_HOUR)){

                cal2.add(Calendar.MONTH,1);
            }

            else{

                /*int y=cal.get(Calendar.YEAR);
                cal2.set(Calendar.MONTH,Calendar.DECEMBER);
                cal2.set(Calendar.DAY_OF_MONTH,31);*/
                //cal2.setTimeInMillis(cal.getTimeInMillis());
                cal2.add(Calendar.MONTH,24);
            }


        }


        if(dayOfWeek!=null && dayOfWeek.size()>0){

            int tailDayOfWeek=dayOfWeek.size();

            while (cal.before(cal2)){

                int pos=0;

                do {


                    if(dayInWeekForMonthOnly>0){
                        cal.set(Calendar.DAY_OF_WEEK,dayInWeekForMonthOnly);
                    }

                    cal.set(dayInWeekOrMonth,dayOfWeek.get(pos));

                    result.add(cal.getTimeInMillis());

                    pos++;

                }while (cal.before(cal2) && pos<tailDayOfWeek);



                cal.add(chamAtraiter,repeatFreq);
            }

        }
        else{



            while (cal.before(cal2)){

                result.add(cal.getTimeInMillis());

                cal.add(chamAtraiter,repeatFreq);
            }

        }




        //Log.e("UIeventManager", "get repeting time size->"+result.size());

        return result;

    }



}
