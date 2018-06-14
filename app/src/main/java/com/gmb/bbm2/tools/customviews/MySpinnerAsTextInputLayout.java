package com.gmb.bbm2.tools.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmb.bbm2.R;
import com.gmb.bbm2.tools.utils.CustomDialogListSelector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by GMB on 4/23/2018.
 */

public class MySpinnerAsTextInputLayout extends RelativeLayout implements View.OnClickListener {

    View rootView;
    TextInputLayout txtLayout;
    TextInputEditText txtVal;
    TextView txtId;

    private String spinHint="Select a value";
    private String spinPrompt="Select one in the list below";
    private int spinHeight=55;
    private int spinWidth=245;
    private int spinTextSize=14;
    private int spinTextColor;
    private int spinfillColor;
    private int selectedIndex=0;
    private ArrayList<Object> spinListVal;
    private ArrayList<Object> spinListShown;

    private OnValueSelectedListner valListner;


    public OnValueSelectedListner getValListner() {
        return valListner;
    }

    public void setValListner(OnValueSelectedListner valListner) {
        this.valListner = valListner;
    }

    public interface OnValueSelectedListner {

        public void getSelectedValue(Object selectedValue, MySpinnerAsTextInputLayout customSpinner);
    }



    private void callTheListner(Object input){

        Log.e("customSpin", "callTheListner et val->"+input+"_listnr->"+valListner);
        if(valListner!=null) valListner.getSelectedValue(input,this);
    }

    /*public MySpinnerAsTextInputLayout(Context context) {
        super(context);
    }*/


    public MySpinnerAsTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context,@Nullable AttributeSet attrs) {

        spinListVal=new ArrayList<>();
        spinListShown=new ArrayList<>();
        TypedArray ta=null;

        try{

            rootView = inflate(context, R.layout.my_spinner_as_textinputlayout, this);


            txtLayout = (TextInputLayout) rootView.findViewById(R.id.txtlayout);

            txtVal = (TextInputEditText) rootView.findViewById(R.id.txtVal);

            txtId = (TextView) rootView.findViewById(R.id.txtId);

            txtVal.setOnClickListener(this);




            if(attrs==null) return;

            ta=context.obtainStyledAttributes(attrs,R.styleable.customSpinnerAsInput);

            spinHint=ta.getString(R.styleable.customSpinnerAsInput_spinHint);

            String title=ta.getString(R.styleable.customSpinnerAsInput_spinPrompt);

            spinPrompt=(title!=null && !title.isEmpty())?title:spinPrompt;

            txtLayout.setHint(spinHint);

            //String[] lstVal=ta.getResources().getStringArray(R.styleable.customSpinnerAsInput_spinListVal);

            CharSequence[] vals=ta.getTextArray(R.styleable.customSpinnerAsInput_android_entries);

            boolean isIdDispo=false;
            ArrayList<Object> lstId=null,lstVal=null;
            if(vals!=null){

                isIdDispo=true;
                lstId=new ArrayList<>();
                lstVal=new ArrayList<>();
                for(int i=0;i<vals.length;i++){

                    lstId.add(vals[i].toString());
                    lstVal.add(vals[i].toString());
                }
                //spinListVal.addAll(Arrays.asList(vals));
            }


            /*vals=ta.getTextArray(R.styleable.customSpinnerAsInput_android_entriesShown);

            boolean isValDispo=false;
            if(vals!=null){

                isValDispo=true;
                lstVal=new ArrayList<>();
                for(int i=0;i<vals.length;i++){

                    lstVal.add(vals[i].toString());
                }
                //spinListVal.addAll(Arrays.asList(vals));
            }*/

            //txtVal.setHint(spinHint);

            if(isIdDispo){

                setListToSpinner(lstId,lstVal);
            }


            rootView.invalidate();

           // ta.recycle();
        }
        catch (Exception ex){

            Log.e("CustomSpinner","init: error while initializing the component->");
            ex.printStackTrace();
        }
        finally {

            rootView.invalidate();

            if(ta!=null) ta.recycle();
        }

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


        private void setListToSpinner(ArrayList<Object> listVal, ArrayList<Object> listShown) throws Exception{


            if(listVal !=null){

                this.spinListVal=listVal;
                this.spinListShown=listVal;
                List<Object> spinnerArray = (listVal);

                if(listShown!=null && listShown.size()==listVal.size()){

                    spinnerArray = (listShown);
                    spinListShown=listShown;
                }

// (3) create an adapter from the list
                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        super.getContext(),
                        android.R.layout.simple_spinner_item,
                        spinnerArray
                );*/



//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// (4) set the adapter on the spinner
               // mspinner.setAdapter(adapter);
            }
            else{

                throw new Exception("The list of value can't be null: ");
            }



        }


    public String getIdAsString(){

        return txtId.getText().toString();
    }

    public String getValueAsString(){

       return txtVal.getText().toString();
    }


    public Object getIdAsAt(int index){

        if(spinListVal!=null && index>=0 && index<spinListVal.size()){

            return spinListVal.get(index);
        }
        else return null;
    }

    public Object getValueAt(int index){

        if(spinListShown!=null && index>=0 && index<spinListShown.size()){

            return spinListShown.get(index);
        }
        else return null;
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
                selectedIndex=getIndexOf(idVal);
                txtId.setText(idVal.toString());
                txtVal.setText(valSelected.toString());

                callTheListner(idVal);
            }
        };


        Dialog dialog=new CustomDialogListSelector(getContext(),spinPrompt,spinListVal,spinListShown, listener);
        dialog.show();

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

    public void setSpinListVal(Collection<Object> spinIDs, Collection<Object> spinListVal) {

        this.spinListShown=new ArrayList<>();
        this.spinListShown.addAll(spinListVal);


        this.spinListVal=new ArrayList<>();
        this.spinListVal.addAll(spinIDs);


        try {
            setListToSpinner(this.spinListVal,this.spinListShown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setSpinListVal(Collection<Object> spinIDs, Collection<Object> spinListVal,Object idSelect) {

        setSpinListVal(spinIDs,spinListVal);

        setSelectedID(idSelect);
    }

    public void setSelectedVal(Object val){


        if(spinListShown.contains(val)){

            selectedIndex=spinListShown.indexOf(val);
            txtVal.setText(val.toString());
            txtId.setText(spinListVal.get(selectedIndex).toString());
        }
    }


    public void setSelectedID(Object id){

        if(spinListVal.contains(id)){

            selectedIndex=spinListVal.indexOf(id);
            txtId.setText(id.toString());
            txtVal.setText(spinListShown.get(selectedIndex).toString());
        }
    }


    public int getIndexOf(Object id){

        if(spinListVal.contains(id)){

            return spinListVal.indexOf(id);
        }
        else return -1;
    }

    public void setSelectedIndex(int index){


        selectedIndex=index;

        if(index>=0 && index<spinListVal.size()){

            txtId.setText(spinListVal.get(index).toString());
            txtVal.setText(spinListShown.get(index).toString());
        }
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
