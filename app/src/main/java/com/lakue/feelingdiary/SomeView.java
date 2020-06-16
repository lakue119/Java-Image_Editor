package com.lakue.feelingdiary;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SomeView extends View implements View.OnTouchListener {
    private Paint paint;
    public static List<Point> points;
    int DIST = 2;
    boolean flgPathDraw = true;

    Point mfirstpoint = null;
    boolean bfirstpoint = false;

    Point mlastpoint = null;

    Bitmap bitmap;
    byte[] byteArray;

    Context mContext;
    int height;
    int width;

    int max_height = 0;

    public SomeView(Context c) {
        super(c);

        mContext = c;


    }

    public SomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        bfirstpoint = false;
    }

    public void addBitmap(Bitmap map) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

//        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.spider_man);
        float scale = (float) ((width / (float) map.getWidth()));

        int image_w = (int) (map.getWidth() * scale);
        int image_h = (int) (map.getHeight() * scale);

        bitmap = Bitmap.createScaledBitmap(map, image_w, image_h, true);

        //이미지 height 사이즈
        max_height = bitmap.getHeight();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byteArray = stream.toByteArray();



        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();

        bfirstpoint = false;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);

        Path path = new Path();
        boolean first = true;

        for (int i = 0; i < points.size(); i += 2) {
            Point point = points.get(i);
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            } else {
                mlastpoint = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        //아미지 사이즈를 벗어나면, 이미지의 최대 사이즈만큼 point.y값을 가짐
        if(point.y > max_height){
            point.y = max_height;
        }

        Log.i("QLKWRJLQKQWR", "this.height : " + this.height);
        Log.i("QLKWRJLQKQWR", "this.width : " + this.width);

        if (flgPathDraw) {

            if (bfirstpoint) {

                if (comparepoint(mfirstpoint, point)) {
                    // points.add(point);
                    points.add(mfirstpoint);
                    flgPathDraw = false;
                    showcropdialog();
                } else {
                    points.add(point);
                }
            } else {
                points.add(point);
            }

            if (!(bfirstpoint)) {

                mfirstpoint = point;
                bfirstpoint = true;
            }
        }

        invalidate();
        Log.e("Hi  ==>", "Size: " + point.x + " " + point.y);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("Action up*******~~~>>>>", "called");
            mlastpoint = point;
            if (flgPathDraw) {
                if (points.size() > 12) {
                    if (!comparepoint(mfirstpoint, mlastpoint)) {
                        flgPathDraw = false;
                        points.add(mfirstpoint);
                        showcropdialog();
                    }
                }
            }
        }

        return true;
    }

    private boolean comparepoint(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public void fillinPartofPath() {
        Point point = new Point();
        point.x = points.get(0).x;
        point.y = points.get(0).y;

        points.add(point);
        invalidate();
    }

    public void resetView() {
        points.clear();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        flgPathDraw = true;
        invalidate();
    }

    private void showcropdialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent resultIntent;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        // bfirstpoint = false;


                        resultIntent = new Intent();
                        resultIntent.putExtra("crop", true);
                        resultIntent.putExtra("image", byteArray);
                        ((BaseActivity)mContext).setResult(RESULT_OK, resultIntent);
                        ((BaseActivity)mContext).finish();

//                        intent = new Intent(mContext, ActivityCrop.class);
//                        intent.putExtra("crop", true);
//                        intent.putExtra("image", byteArray);
////                        ((BaseActivity)mContext).startActivity(intent);
//                        ((BaseActivity) mContext).startActivityForResult(intent, Define.REQUEST_CODE_GET_CROP_IMAGE);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked

                        resultIntent = new Intent();
                        resultIntent.putExtra("crop", false);
                        resultIntent.putExtra("image", byteArray);
                        ((BaseActivity)mContext).setResult(RESULT_OK, resultIntent);
                        ((BaseActivity)mContext).finish();

//                        intent = new Intent(mContext, ActivityCrop.class);
//                        intent.putExtra("crop", false);
//                        intent.putExtra("image", byteArray);
////                        mContext.startActivity(intent);
//                        ((BaseActivity) mContext).startActivityForResult(intent, Define.REQUEST_CODE_GET_CROP_IMAGE);

                        bfirstpoint = false;
                        // resetView();

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Do you Want to save Crop or Non-crop image?")
                .setPositiveButton("Crop", dialogClickListener)
                .setNegativeButton("Non-crop", dialogClickListener).show()
                .setCancelable(false);
    }
}





