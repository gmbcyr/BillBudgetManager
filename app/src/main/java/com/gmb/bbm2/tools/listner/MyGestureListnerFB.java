package com.gmb.bbm2.tools.listner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.gmb.bbm2.tools.adapter.MyAdapterUpdate;
import com.gmb.bbm2.tools.allstatic.AllStaticKt;

/**
 * Created by GMB on 10/26/2016.
 */

public class MyGestureListnerFB implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private MyAdapterUpdate myAdapter;
    Context context;
    RecyclerView.ViewHolder viewHolder;


    public MyGestureListnerFB(Context ctx, MyAdapterUpdate adapter, RecyclerView.ViewHolder viewHol){
        myAdapter=adapter;
        context=ctx;
        viewHolder=viewHol;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Log.d("Gesture ", " onDown");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Log.d("Gesture ", " onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Log.d("Gesture ", " onSingleTapUp");

        if(AllStaticKt.isClickMenuContextuel(context)){


            myAdapter.afficheMenuContextuel(viewHolder.getAdapterPosition());

        }
        else {

            myAdapter.afficheEltCorrespondantAuChoix(viewHolder.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Log.d("Gesture ", " onShowPress");
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //Log.d("Gesture ", " onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //Log.d("Gesture ", " onDoubleTapEvent");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        if(AllStaticKt.isClickMenuContextuel(context)){


            myAdapter.afficheEltCorrespondantAuChoix(viewHolder.getAdapterPosition());

        }
        else {

            myAdapter.afficheMenuContextuel(viewHolder.getAdapterPosition());

        }
        //Log.d("Gesture ", " onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

       /* Log.d("Gesture ", " onScroll");
        if (e1.getY() < e2.getY()){
            Log.d("Gesture ", " Scroll Down");
        }
        if(e1.getY() > e2.getY()){
            Log.d("Gesture ", " Scroll Up");
        }*/
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            //Log.d("Gesture ", "Left to Right swipe: "+ e1.getX() + " - " + e2.getX());
            myAdapter.onSwipeRight(viewHolder.getAdapterPosition());
            //Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
        }
        if (e1.getX() > e2.getX()) {
            //Log.d("Gesture ", "Right to Left swipe: "+ e1.getX() + " - " + e2.getX());
            myAdapter.onSwipeLeft(viewHolder.getAdapterPosition());
            //Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
        }
        if (e1.getY() < e2.getY()) {
            //Log.d("Gesture ", "Up to Down swipe: " + e1.getX() + " - " + e2.getX());
            //Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
        }
        if (e1.getY() > e2.getY()) {
            //Log.d("Gesture ", "Down to Up swipe: " + e1.getX() + " - " + e2.getX());
            //Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
        }
        return true;

    }






}
