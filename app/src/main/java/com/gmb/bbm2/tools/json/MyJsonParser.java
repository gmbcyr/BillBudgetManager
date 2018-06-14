package com.gmb.bbm2.tools.json;

import android.content.Context;
import android.util.Log;

import com.gmb.bbm2.data.model.BbmStatementFB;
import com.gmb.bbm2.data.model.OnTimeNotificationFB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by GMB on 2/3/2016.
 */
public class MyJsonParser {

    

    public static OnTimeNotificationFB stringToOnTimeNotificationFB(String json){

        OnTimeNotificationFB val=new OnTimeNotificationFB();



        try{

            JSONObject jsOb=new JSONObject(json);

            try{
                val.setId(jsOb.getString("id"));
            }
            catch (Exception ex){
                System.out.println("MyJsonParser stringToOperations parseID error->"+ex.getMessage());
            }

            val.setDateop(jsOb.getLong("dateop"));
            val.setDateprojecte(jsOb.getLong("dateprojecte"));
            val.setDatereel(jsOb.getLong("datereel"));
            val.setMontantop(jsOb.getDouble("montant"));
            val.setMontantprojecte(jsOb.getDouble("montantprojecte"));
            val.setPuop(jsOb.getDouble("pu"));
            val.setQnteop(jsOb.getInt("qnte"));
            val.setPaidop(jsOb.getString("paid").equalsIgnoreCase("1"));
            val.setPenalite(jsOb.getDouble("penalite"));
            val.setTotalop(jsOb.getDouble("total"));

            val.setIdstate(jsOb.getString("idstate"));



        }
        catch (Exception e){

            System.out.println("MyJsonParser stringToOperations error->"+e.getMessage());
        }




        return val;
    }





    


   
    public static OnTimeNotificationFB getReminderPrepaForAlarmFB(OnTimeNotificationFB rem, int dayInYear,Context context){

        OnTimeNotificationFB val=rem;



        try{


            //MyDBinteraction bdin=new MyDBinteraction(context);

            //val.setIdalarm((rem.getAlarmset())?rem.getIdalarm():bdin.getIdAlarmForADay(rem.getIdstate()+""));

        }
        catch (Exception e){

            System.out.println("MyJsonParser stringToOperations error->"+e.getMessage());
        }




        return val;
    }


    public static String BbmStatementToOperations(BbmStatementFB statement,double montant,double penality,
                                                  double pu,int qnte,double total,long timeStart,int paid){

        JSONObject jsonObject = new JSONObject();





        //JSONParser jsonParser = new JSONParser();

        try{


            /*jsonObject.put(AllStaticKt.getCOL_NAME_BBMSTATEMENT_ID(), statement.getId());
            jsonObject.put(AllStaticKt.getCOL_NAME_MONTANT(), montant);
            jsonObject.put(AllStaticKt.getCOL_NAME_MONTANT_PROJECT(), statement.getMontant());
            jsonObject.put(AllStaticKt.getCOL_NAME_DATE_REEL(), timeStart);
            jsonObject.put(AllStaticKt.getCOL_NAME_DATE_PROJECT(), statement.getTimestart());
            jsonObject.put(AllStaticKt.getCOL_NAME_PU(), pu);
            jsonObject.put(AllStaticKt.getCOL_NAME_PU_PROJECT(), statement.getPu());
            jsonObject.put(AllStaticKt.getCOL_NAME_QNTE(), qnte);
            jsonObject.put(AllStaticKt.getCOL_NAME_QNTE_PROJECT(), statement.getQnte());
            jsonObject.put(AllStaticKt.getCOL_NAME_SENS(), statement.getSens());
            jsonObject.put(AllStaticKt.getCOL_NAME_PENALITY(), penality);
            jsonObject.put(AllStaticKt.getCOL_NAME_PAID(), paid);
            jsonObject.put(AllStaticKt.getCOL_NAME_TOTAL(), total);*/


            // jsonObject.accumulate("statement", values);
        }
        catch (Exception e){

        }

        return jsonObject.toString();
    }

    public static ArrayList<Long> jsonArrayToArrayList(JSONArray json){

        ArrayList<Long> values=null;



        try{

            if(json!=null){
                values=new ArrayList<>();

                int tail=json.length();

                for(int i=0;i<tail;i++){

                    values.add(json.getLong(i));
                }
            }


        }
        catch (Exception e){

            System.out.println("MyJsonParser jsonArrayToArrayList error->"+e.getMessage());
        }




        return values;
    }


    public static ArrayList<String> jsonArrayToArrayReminder(JSONArray json){

        ArrayList<String> values=null;



        try{

            if(json!=null){
                values=new ArrayList<>();

                int tail=json.length();

                for(int i=0;i<tail;i++){

                    //Log.e("MyJsonParser"," MyJsonParser jsonArrayToArrayReminder une ligne->"+json.getString(i));

                    values.add(json.getString(i));
                }
            }


        }
        catch (Exception e){

            System.out.println("MyJsonParser jsonArrayToArrayReminder error->"+e.getMessage());
        }




        return values;
    }




    public static JSONObject BbmStatementFBToJson(BbmStatementFB bbm){



        if(bbm==null) return null;

        JSONObject jsOb=new JSONObject();



        try{

            jsOb.put("nom",bbm.getNom());
            jsOb.put("id", bbm.getId());
            jsOb.put("descrip", bbm.getDescrip());
            jsOb.put("type", bbm.getType());
            jsOb.put("sens", bbm.getSens());
            jsOb.put("recurring", bbm.getRecurring() ? "1" : "0");
            jsOb.put("isautomaticpay", bbm.getIsautomaticpay() ? "1" : "0");

            jsOb.put("daysofweek",bbm.getDaysofweek());
            jsOb.put("dayofweekormonth",bbm.getDayofweekormonth());
            jsOb.put("dayweekinmonth",bbm.getDayweekinmonth());

            jsOb.put("qnte", bbm.getQnte());
            jsOb.put("pu", bbm.getPu());
            jsOb.put("montant", bbm.getMontant());
            jsOb.put("url", bbm.getUrl());
            jsOb.put("dateend", bbm.getDateend());
            jsOb.put("timeend", bbm.getTimeend());
            jsOb.put("datestart", bbm.getDatestart());
            jsOb.put("timestart", bbm.getTimestart());
            jsOb.put("unlimited", bbm.getUnlimited() ? "1" : "0");
            jsOb.put("typeend", bbm.getTypeend());
            jsOb.put("repeatunit", bbm.getRepeatunit());
            jsOb.put("repeatfreq", bbm.getRepeatfreq());
            jsOb.put("nbrerepeat", bbm.getNbrerepeat());
            jsOb.put("currentstate", bbm.getCurrentstate());

            jsOb.put("reminderconf",bbm.getReminderconf());

            jsOb.put("idcat",bbm.getIdcat());

            //Log.e("MyJsonParser", "stringToBbmStatement avec idCat->"+bbm.getIdcat());

            Log.e("AddStatement","BBMtoJson From RepeatDialog dayOfWeekOrMonth->"+bbm.getDayofweekormonth());

        }
        catch (Exception e){

            jsOb=null;
            //Log.e("MyJsonParser", "MyJsonParser stringToBbmStatement error->"+e.getMessage());
        }




        return jsOb;
    }
}
