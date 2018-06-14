package com.gmb.bbm2.tools.customviews;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmb.bbm2.R;
import com.gmb.bbm2.tools.utils.CustomDialogListSelector;
import com.gmb.bbm2.tools.utils.EasyCustomInputText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMB on 4/23/2018.
 */

public class MySpinnerAsTextInputLayout2 extends RelativeLayout implements View.OnClickListener,Spinner.OnItemSelectedListener {

    View rootView;
    Spinner mspinner;
    TextInputLayout txtLayout;
    TextInputEditText txtVal;
    TextView txtId;

    private String spinHint="Select one";
    private String spinPrompt="Select one in the list below";
    private int spinHeight=55;
    private int spinWidth=245;
    private int spinTextSize=14;
    private int spinTextColor;
    private int spinfillColor;
    private ArrayList<Object> spinListVal;
    private ArrayList<Object> spinListShown;



    private EasyCustomInputText.OnInputCompleted inputListner=new EasyCustomInputText.OnInputCompleted() {
        @Override
        public void inputCompleted(String input) {
            txtVal.setText(input);
        }
    };


    public MySpinnerAsTextInputLayout2(Context context) {
        super(context);
    }


    public MySpinnerAsTextInputLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.my_spinner_as_textinputlayout, this);
        //mspinner = (Spinner) rootView.findViewById(R.id.spinner);

        txtLayout = (TextInputLayout) rootView.findViewById(R.id.txtlayout);

        txtVal = (TextInputEditText) rootView.findViewById(R.id.txtVal);

        txtId = (TextView) rootView.findViewById(R.id.txtId);

        txtVal.setOnClickListener(this);

        //mspinner.setOnItemSelectedListener(this);


        /*XmlPullParser parser = getResources().getXml(R.xml.test_xml_att);
        try {
            parser.next();
            parser.nextTag();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AttributeSet attr = Xml.asAttributeSet(parser);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attr, R.styleable.customSpinnerAsInput, 0, 0);
        //barHeight = ta.getDimensionPixelSize(R.styleable.ValueBar_barHeight, 0);

        spinHint=ta.getString(R.styleable.customSpinnerAsInput_spinHeight);

        spinPrompt=ta.getString(R.styleable.customSpinnerAsInput_spinPrompt);

        spinHeight=ta.getDimensionPixelOffset(R.styleable.customSpinnerAsInput_spinHeight,55);
        spinWidth=ta.getDimensionPixelOffset(R.styleable.customSpinnerAsInput_spinWidth,245);

        spinListShown=ta.getString(R.styleable.customSpinnerAsInput_spinListShown).split("|");
        spinListVal=ta.getString(R.styleable.customSpinnerAsInput_spinListVal).split("|");

        ta.recycle();*/


        /*labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            labelPaint.setTextSize(labelTextSize);
            labelPaint.setColor(labelTextColor);
            labelPaint.setTextAlign(Paint.Align.LEFT);
            labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            maxValuePaint.setTextSize(maxValueTextSize);
            maxValuePaint.setColor(currentValueTextColor);
            maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            maxValuePaint.setTextAlign(Paint.Align.RIGHT);

            barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            barBasePaint.setColor(baseColor);

            barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            barFillPaint.setColor(fillColor);

            circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaint.setColor(fillColor);

            currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            currentValuePaint.setTextSize(circleTextSize);
            currentValuePaint.setColor(circleTextColor);
            currentValuePaint.setTextAlign(Paint.Align.CENTER);*/

        }



    /*@Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }


    private int measureHeight(int measureSpec) {
        int size = getPaddingTop() + getPaddingBottom();
        size += labelPaint.getFontSpacing();
        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
        return resolveSizeAndState(size, measureSpec, 0);
    }


    private int measureWidth(int measureSpec) {
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
        size += bounds.width();

        return resolveSizeAndState(size, measureSpec, 0);
    }


    private void drawLabel(Canvas canvas) {
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();

        canvas.drawText(labelText, x, y, labelPaint);
    }



    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
        barCenter += getPaddingTop() + .1f * getHeight(); //move it down a bit
        return barCenter;
    }


    private void drawMaxValue(Canvas canvas) {
        String maxValue = String.valueOf(this.maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), maxValueRect);

        float xPos = getWidth() - getPaddingRight();
        float yPos = getBarCenter() + maxValueRect.height() / 2;
        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);
    }*/


        private void setListToSpinner(ArrayList<Object> listVal, ArrayList<Object> listShown){


            if(listVal !=null){

                this.spinListVal=listVal;
                this.spinListShown=listVal;
                List<Object> spinnerArray = (listVal);

                if(listShown!=null){

                    spinnerArray = (listShown);
                    spinListShown=listShown;
                }

// (3) create an adapter from the list
                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        super.getContext(),
                        android.R.layout.simple_spinner_item,
                        spinnerArray
                );*/

                ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                        super.getContext(),
                        R.layout.spinner_item,
                        R.id.txtSpinnerItem,
                        spinnerArray
                );

//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// (4) set the adapter on the spinner
                mspinner.setAdapter(adapter);
            }



        }


    public String getIdAsString(){

        return txtId.getText().toString();
    }

    public String getValueAsString(){

       return txtVal.getText().toString();
    }

    public Object getValueAsObject(){

       return mspinner.getSelectedItem();
    }

    private void setSpinnerVisible(boolean visible){

        Log.e("MySpinner","setSpinnerVisible -- with val->"+visible+"!!!!!!!!!");
        if(visible){

            txtVal.setVisibility(GONE);
            mspinner.setVisibility(View.VISIBLE);
        }
        else{

            txtVal.setVisibility(VISIBLE);
            mspinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        //Log.e("MySpinner","onCLick -- click on the txtView!!!!!!!!!!!");

        //setSpinnerVisible(true);
        //mspinner.performClick();


        CustomDialogListSelector.onItemSelectListener listener=new CustomDialogListSelector.onItemSelectListener() {
            @Override
            public void onItemSelected(@NotNull Object idVal,@NotNull Object valSelected) {

               // Log.e("MySpinner","onItemSelected -- the val selected was->"+valSelected+"!!!!!!!!!!!");
                txtId.setText(idVal.toString());
                txtVal.setText(valSelected.toString());
            }
        };



        Dialog dialog=new CustomDialogListSelector(getContext(),txtVal.getText().toString(),spinListVal,spinListShown, listener);
        dialog.show();

    }


    private void popUpCustomSaisie() {

        Dialog dialog=new EasyCustomInputText(getContext(),txtVal.getText().toString(), inputListner,EasyCustomInputText.INPUT_TYPE_DECIMAL);
        dialog.show();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
        //Log.e("MySpinner","onItemSelected -- an item selected!!!!!!!!!!!");
        txtVal.setText(mspinner.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
        //setSpinnerVisible(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        //Log.e("MySpinner","onItemSelected -- nothing selected!!!!!!!!!!!");
        txtVal.setText("");
        setSpinnerVisible(false);
    }


    public String getSpinHint() {
        return spinHint;
    }

    public void setSpinHint(String spinHint) {
        this.spinHint = spinHint;
        txtVal.setHint(spinHint);
    }

    public int getSpinHeight() {
        return spinHeight;
    }

    public void setSpinHeight(int spinHeight) {
        this.spinHeight = spinHeight;
    }

    public int getSpinWidth() {
        return spinWidth;
    }

    public void setSpinWidth(int spinWidth) {
        this.spinWidth = spinWidth;
    }

    public int getSpinTextSize() {
        return spinTextSize;
    }

    public void setSpinTextSize(int spinTextSize) {
        this.spinTextSize = spinTextSize;
    }

    public int getSpinTextColor() {
        return spinTextColor;
    }

    public void setSpinTextColor(int spinTextColor) {
        this.spinTextColor = spinTextColor;
    }

    public int getSpinfillColor() {
        return spinfillColor;
    }

    public void setSpinfillColor(int spinfillColor) {
        this.spinfillColor = spinfillColor;
    }

    public ArrayList<Object> getSpinListVal() {
        return spinListVal;
    }

    public void setSpinListVal(ArrayList<Object> spinIDs,ArrayList<Object> spinListVal) {
        this.spinListVal = spinListVal;

        setListToSpinner(spinIDs,spinListVal);
    }



    public ArrayList<Object> getSpinListShown() {
        return spinListShown;
    }

    public void setSpinListShown(ArrayList<Object> spinListShown) {
        this.spinListShown = spinListShown;
    }

    public String getSpinPrompt() {
        return spinPrompt;
    }

    public void setSpinPrompt(String spinPrompt) {
        this.spinPrompt = spinPrompt;
    }
}
