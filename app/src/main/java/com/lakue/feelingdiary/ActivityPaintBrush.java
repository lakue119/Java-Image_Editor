package com.lakue.feelingdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xw.repo.BubbleSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ActivityPaintBrush extends AppCompatActivity {

    MyPaintView view;
    int tColor,n=0;

    ImageView iv_color_picker;
    ImageView iv_border;
    ImageView iv_brush;
    ImageView iv_save;
    ImageView iv_eraser;
    ImageView iv_content;
    RadioButton rBtn1;
    RadioButton rBtn2;
    RadioButton rBtn3;
    LinearLayout ll_bold_slider;
    LinearLayout ll_brush;
    BubbleSeekBar seekbar;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_brush);
        view = new MyPaintView(this);

        container = findViewById(R.id.container);
        Resources res = getResources();


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, params);

        rBtn1 = findViewById(R.id.radioButton);
        rBtn2 = findViewById(R.id.radioButton2);
        rBtn3 = findViewById(R.id.radioButton3);
        ll_bold_slider = findViewById(R.id.ll_bold_slider);
        seekbar = findViewById(R.id.seekbar);
        ll_brush = findViewById(R.id.ll_brush);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        view.setCap(0);
                        break;
                    case R.id.radioButton2:
                        view.setCap(1);
                        break;
                    case R.id.radioButton3:
                        view.setCap(2);
                        break;
                }
            }
        });

        iv_color_picker=findViewById(R.id.iv_color_picker);
        iv_border=findViewById(R.id.iv_border);
        iv_brush = findViewById(R.id.iv_brush);
        iv_save = findViewById(R.id.iv_save);
        iv_eraser = findViewById(R.id.iv_eraser);
        iv_content = findViewById(R.id.iv_content);

        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivitySelectContent.class);
                startActivityForResult(intent, Define.REQUEST_CODE_CONTENT);
            }
        });

        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
//                Bitmap sendBitmap = view.getmBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                resultIntent.putExtra("image",byteArray);

                resultIntent.putExtra("image", saveBitmapToJpeg(view.getmBitmap()).getPath());
                setResult(RESULT_OK, resultIntent);
                finish();

//                Intent intent = new Intent(getApplicationContext(), ActivityTest.class);
//
//                Bitmap sendBitmap = view.getmBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                intent.putExtra("image",byteArray);
//                startActivity(intent);

//                ImageView imageView = findViewById(R.id.imageView);
//                Glide.with(getApplicationContext()).load(view.getmBitmap()).into(imageView);
//                intent.putExtra("iamge", view.getmBitmap());
//                startActivity(intent);
            }
        });

        iv_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setEraser(true);
            }
        });

        iv_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setEraser(false);
                openColorPicker();
            }
        });

        iv_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setEraser(false);
                if(ll_bold_slider.isShown()){
                    ll_bold_slider.setVisibility(View.GONE);
                }
                ll_brush.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ll_brush.setVisibility(View.GONE);
                    }
                }, 3000);// 0.5초 정도 딜레이를 준 후 시작
            }
        });

        iv_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_brush.isShown()){
                    ll_brush.setVisibility(View.GONE);
                }
                ll_bold_slider.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ll_bold_slider.setVisibility(View.GONE);
                    }
                }, 3000);// 0.5초 정도 딜레이를 준 후 시작

//                show();
            }
        });

        seekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                view.setStrokeWidth(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
    }

    private File saveBitmapToJpeg(Bitmap bitmap) {

        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getCacheDir();
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        //저장할 파일 이름
        String fileName = date + ".png";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
        return tempFile;
    }

    private void show() {
        final EditText editText=new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("굵기 입력");
        builder.setView(editText);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        view.setStrokeWidth(Integer.parseInt(editText.getText().toString()));

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();

    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(),""+tColor,Toast.LENGTH_LONG).show();
                view.setColor(color);
                iv_color_picker.setColorFilter(color);
            }
        });
        colorPicker.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == Define.REQUEST_CODE_CONTENT){
                int content = data.getIntExtra("EXTRA_CONTENT",0);

                Bitmap bigPictureBitmap  = BitmapFactory.decodeResource(getApplicationContext().getResources(), content);

                view.setmBitmap(bigPictureBitmap);
//                ImageView imageView = new ImageView(this);
//                Glide.with(this).load(content).into(imageView);
//                container.addView(imageView);



            }
        }
    }
}
