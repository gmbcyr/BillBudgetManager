package com.gmb.bbm2;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyReminderDialog.MyReminderListener} interface
 * to handle interaction events.
 * Use the {@link MyReminderDialog} factory method to
 * create an instance of this fragment.
 */
public class MyReminderDialog extends AlertDialog implements View.OnClickListener,
        Spinner.OnItemSelectedListener {
    // TODOdone: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "timeStart";
    private static final String ARG_PARAM2 = "param2";

    // TODOdone: Rename and change types of parameters
    private long timeStart;
    private String mParam2;
    private String valToSend="1-2";
    private ArrayList<String> reminderTypeAff,reminderTypeVal;
    private ArrayList<String> reminderVal;
    private Spinner cmbType,cmbVal;
    private ImageView cmdvalid,cmdCancel;

    private String typeStr="",valStr="";

    private MyReminderListener mListener;

    private ArrayAdapter<String> dataValAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param timeStart Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyReminderFragment.
     */
    // TODOdone: Rename and change types and number of parameters
    public MyReminderDialog(Context context, MyReminderListener listener, long timeStart, String param2) {

        super(context);

        this.timeStart = timeStart;
        mParam2 = param2;
        mListener=listener;


        this.setView(this.initView());
    }




    public View initView() {
        // Inflate the layout for this fragment

        Context themeContext = this.getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        View rootView = inflater.inflate(R.layout.fragment_my_reminder, null);


        cmbType=(Spinner) rootView.findViewById(R.id.rmder_type);
        cmbVal=(Spinner) rootView.findViewById(R.id.rmder_val);
        //reminderTypeVal =new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.reminder_units_val)));

        reminderTypeAff =new ArrayList<String>(Arrays.asList(this.getContext().getResources().getStringArray(R.array.reminder_units)));

        reminderTypeVal=new ArrayList<String>();
        reminderTypeVal.add(""+Calendar.MINUTE);
        reminderTypeVal.add(""+Calendar.HOUR);
        reminderTypeVal.add(""+Calendar.DAY_OF_YEAR);
        reminderTypeVal.add(""+Calendar.WEEK_OF_YEAR);
        reminderTypeVal.add(""+Calendar.MONTH);
        reminderTypeVal.add(""+Calendar.YEAR);

        reminderVal =new ArrayList<String>(Arrays.asList(this.getContext().getResources().getStringArray(R.array.reminder_val)));


        reminderVal=new ArrayList<>();
        for(int i=1;i<60;i++){


            reminderVal.add(""+i);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, reminderTypeAff);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        cmbType.setAdapter(dataAdapter);
        cmbType.setOnItemSelectedListener(this);


        // Creating adapter for spinner
        dataValAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, reminderVal);
        // Drop down layout style - list view with radio button
        dataValAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        cmbVal.setAdapter(dataValAdapter);
        cmbVal.setOnItemSelectedListener(this);


        cmdvalid=(ImageView) rootView.findViewById(R.id.cmd_valid);
        cmdCancel=(ImageView) rootView.findViewById((R.id.cmd_cancel));

        cmdCancel.setOnClickListener(this);
        cmdvalid.setOnClickListener(this);




        return rootView;
    }

    // TODOdone: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onMyReminderFragment(valToSend);
        }
    }





    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cmd_valid:

                String choix=typeStr+"-"+valStr;

                mListener.onMyReminderFragment(timeStart,choix);
                //Toast.makeText(getContext(),"voici typeStr et valStr->"+typeStr+"-"+valStr,Toast.LENGTH_LONG).show();
                this.dismiss();

                break;

            case R.id.cmd_cancel:

                this.dismiss();

                break;


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(parent.getContext(), (String) parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

        if(parent.getId()==R.id.rmder_type){


            if(reminderTypeVal.size()>0 ){

                typeStr=  reminderTypeVal.get(position);

                if(typeStr.equalsIgnoreCase(""+Calendar.MINUTE)){

                    reminderVal=new ArrayList<>();
                    for(int i=1;i<60;i++){


                        reminderVal.add(""+i);
                    }
                }
                else if(typeStr.equalsIgnoreCase(""+Calendar.HOUR)){

                    reminderVal=new ArrayList<>();
                    for(int i=1;i<24;i++){


                        reminderVal.add(""+i);
                    }
                }

                else if(typeStr.equalsIgnoreCase(""+Calendar.DAY_OF_YEAR)){

                    reminderVal=new ArrayList<>();
                    for(int i=1;i<20;i++){


                        reminderVal.add(""+i);
                    }
                }
                else{

                    reminderVal=new ArrayList<>();
                    reminderVal =new ArrayList<String>(Arrays.asList(this.getContext().getResources().getStringArray(R.array.reminder_val)));
                }


                dataValAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, reminderVal);
                // Drop down layout style - list view with radio button
                dataValAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cmbVal.setAdapter(dataValAdapter);

            }
        }
        else if(parent.getId()==R.id.rmder_val){

            valStr=  (String) parent.getItemAtPosition(position);

            //Toast.makeText(getContext(),"voici  valStr->"+valStr,Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(getContext(),"voici typeStr et valStr->"+typeStr+"-"+valStr,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MyReminderListener {
        // TODOdone: Update argument type and name
        void onMyReminderFragment(long timeStart, String content);
    }
}
