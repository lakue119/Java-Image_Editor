package com.lakue.imageEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyPaintView extends View {

    public boolean changed = false;

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    public static List<Point> points;

    float lastX;
    float lastY;

    Path mPath = new Path();

    float mCurveEndX;
    float mCurveEndY;

    int mInvalidateExtraBorder = 10;

    static final float TOUCH_TOLERANCE = 8;

    public MyPaintView(Context context) {
        super(context);
        init(context);
    }

    public MyPaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        points = new ArrayList<Point>();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3.0F);

        this.lastX = -1;
        this.lastY = -1;
    }

    public void setEraser(boolean isEraser) {
//        this.isEraser = isEraser;
        if (isEraser) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            mPaint.setXfermode(null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        canvas.drawColor(Color.TRANSPARENT);

        mBitmap = img;
        mCanvas = canvas;
    }

    public void setmBitmap(Bitmap bitmap){
        mBitmap = null;
        mCanvas = null;

        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas();
        canvas.setBitmap(mBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        mCanvas = canvas;

        Point point = new Point();
        point.x = 0;
        point.y = 0;
        points.add(point);

        Point point1 = new Point();
        point1.x = mBitmap.getWidth();
        point1.y = mBitmap.getHeight();
        points.add(point1);
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                changed = true;
                Rect rect = touchUp(event, false);
                if (rect != null) {
                    invalidate(rect);
                }
                mPath.rewind();

                return true;
            case MotionEvent.ACTION_DOWN:
                rect = touchDown(event);
                if (rect != null) {
                    invalidate(rect);
                }

                return true;
            case MotionEvent.ACTION_MOVE:
                Point point = new Point();
                point.x = (int) event.getX();
                point.y = (int) event.getY();

                rect = touchMove(event);

                points.add(point);
//                Log.e("KALWJKL", "X : " + event.getX() + "    /      Y : " + event.getY());
                if (rect != null) {
                    invalidate(rect);
                }
                return true;

        }
        return false;
    }

    private Rect touchMove(MotionEvent event) {
        Rect rect=processMove(event);
        return rect;
    }

    private Rect processMove(MotionEvent event) {
        final float x=event.getX();
        final float y=event.getY();

        final float dx=Math.abs(x-lastX);
        final float dy=Math.abs(y-lastY);

        Rect mInvalidateRect=new Rect();

        if(dx>=TOUCH_TOLERANCE || dy>=TOUCH_TOLERANCE){
            final int border=mInvalidateExtraBorder;

            mInvalidateRect.set((int)mCurveEndX-border,(int)mCurveEndY-border,(int)mCurveEndX+border,(int)mCurveEndY+border);

            float cx=mCurveEndX=(x+lastX)/2;
            float cy=mCurveEndY=(y+lastY)/2;

            mPath.quadTo(lastX,lastY,cx,cy);

            mInvalidateRect.union((int)lastX-border,(int)lastY-border,(int)lastX+border,(int)lastY+border);
            mInvalidateRect.union((int)cx-border,(int)cy-border,(int)cx,(int)cy+border);

            lastX=x;
            lastY=y;

            mCanvas.drawPath(mPath,mPaint);

        }

        return mInvalidateRect;
    }

    private Rect touchDown(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();

        lastX=x;
        lastY=y;

        Rect mInvalidateRect=new Rect();
        mPath.moveTo(x,y);

        final int border=mInvalidateExtraBorder;
        mInvalidateRect.set((int)x-border,(int)y-border,(int)x+border,(int)y+border);
        mCurveEndX=x;
        mCurveEndY=y;

        mCanvas.drawPath(mPath,mPaint);
        return mInvalidateRect;
    }
    public void setStrokeWidth(int width){
        mPaint.setStrokeWidth(width);
    }

    private Rect touchUp(MotionEvent event, boolean b) {
        Rect rect=processMove(event);
        return rect;
    }

    public void setColor(int color){
        mPaint.setColor(color);

    }
    public void setCap(int cap){
        switch(cap){
            case 0:
                mPaint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case 1:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case 2:
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
        }
    }


}


