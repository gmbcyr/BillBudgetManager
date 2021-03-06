package com.gmb.bbm2.tools.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmb.bbm2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EasyCustomInputText} interface
 * to handle interaction events.
 * Use the {@link EasyCustomInputText} factory method to
 * create an instance of this fragment.
 */
public class EasyCustomInputText extends AlertDialog implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static int INPUT_TYPE_TEXT= InputType.TYPE_TEXT_VARIATION_NORMAL;
    public static int INPUT_TYPE_NUMBER= InputType.TYPE_NUMBER_VARIATION_NORMAL;
    public static int INPUT_TYPE_DECIMAL= InputType.TYPE_NUMBER_FLAG_DECIMAL;
    public static int INPUT_TYPE_PWD= InputType.TYPE_TEXT_VARIATION_PASSWORD;


    TextView txtSaisie;
    ImageView cmdOk;


    OnInputCompleted mListener;




    public EasyCustomInputText(Context context, String previousText, OnInputCompleted listener, int inputType) {
        super(context);


        if(listener != null) {
            mListener=listener;
            this.setView(this.initView(previousText,inputType));

        }
    }




    public View initView(String text,int inputType) {
        // Inflate the layout for this fragment
        Context themeContext = this.getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        View rootView = inflater.inflate(R.layout.dynamic_custom_input_text, null);


        try{



            txtSaisie=(TextView) rootView.findViewById(R.id.txt_saisie);

            cmdOk=(ImageView) rootView.findViewById(R.id.img_go);
            txtSaisie.setText(text);
            txtSaisie.setInputType(inputType);
            txtSaisie.setImeOptions(EditorInfo.IME_ACTION_DONE);
            txtSaisie.requestFocus();
            txtSaisie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mListener.inputCompleted(txtSaisie.getText().toString());// do function on pressing the word GO
                        handled = true;
                        closeDialog();
                    }
                    return handled;
                }
            });
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            cmdOk.setOnClickListener(this);



        }
        catch (Exception ex){


        }




        return rootView;
    }



    @Override
    public void onClick(View v) {



        //Toast.makeText(getContext(),"this is call from ChooseDatePeriod",Toast.LENGTH_SHORT).show();

        switch (v.getId()){



            /*case R.id.btn_cancel:

                this.closeDialog();
                break;*/

            case R.id.img_go:
                /*FragmentManager fm = getActivity().getSupportFragmentManager();
                MyRepeatDialog dialogFragment = new MyRepeatDialog ();
                dialogFragment.show(fm, "Repeat options");*/
                mListener.inputCompleted(txtSaisie.getText().toString());
                this.closeDialog();
                break;

        }
    }


    public void closeDialog(){

        this.dismiss();
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
    public interface OnInputCompleted {

        void inputCompleted(String input);
    }
}
