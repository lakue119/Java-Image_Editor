package com.lakue.feelingdiary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DragImageView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener {
    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float oldXvalue;
    float oldYvalue;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
        int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height
        Log.d("viewTest", "oldXvalue : "+ oldXvalue + " oldYvalue : " + oldYvalue);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // 뷰 누름
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            Log.d("viewTest", "oldXvalue : "+ oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
            Log.d("viewTest", "v.getX() : "+v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
            Log.d("viewTest", "RawX : " + event.getRawX() +" RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
            Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            // 뷰 이동 중
            v.setX(v.getX() + (event.getX()) - (v.getWidth()/2));
            v.setY(v.getY() + (event.getY()) - (v.getHeight()/2));

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            // 뷰에서 손을 뗌

            if(v.getX() < 0){
                v.setX(0);
            }else if((v.getX() + v.getWidth()) > parentWidth){
                v.setX(parentWidth - v.getWidth());
            }

            if(v.getY() < 0){
                v.setY(0);
            }else if((v.getY() + v.getHeight()) > parentHeight){
                v.setY(parentHeight - v.getHeight());
            }

        }
        return true;
    }
}
