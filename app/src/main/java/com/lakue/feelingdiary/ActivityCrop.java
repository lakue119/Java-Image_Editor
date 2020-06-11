//package com.lakue.feelingdiary;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//public class ActivityCrop extends BaseActivity {
//
//    ImageView compositeImageView;
//    Button btn_success;
//
//    boolean crop;
//    Bitmap resultingImage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crop);
//
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            crop = extras.getBoolean("crop");
//        }
//
//        byte[] byteArray = getIntent().getByteArrayExtra("image");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//
//        int widthOfscreen = 0;
//        int heightOfScreen = 0;
//
//        DisplayMetrics dm = new DisplayMetrics();
//        try {
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//        } catch (Exception ex) {
//            printLog(ex.toString());
//        }
//        widthOfscreen = dm.widthPixels;
//        heightOfScreen = dm.heightPixels;
//
//        compositeImageView = (ImageView) findViewById(R.id.imageview);
//        btn_success = findViewById(R.id.btn_success);
//
//        resultingImage = Bitmap.createBitmap(widthOfscreen, heightOfScreen, bitmap.getConfig());
//
//        Canvas canvas = new Canvas(resultingImage);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//
//        Path path = new Path();
//        for (int i = 0; i < SomeView.points.size(); i++) {
//            path.lineTo(SomeView.points.get(i).x, SomeView.points.get(i).y);
//        }
//        canvas.drawPath(path, paint);
//        if (crop) {
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        } else {
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//        }
//        canvas.drawBitmap(bitmap, 0, 0, paint);
////        compositeImageView.setImageBitmap(resultingImage);
//
//        getMaxMinData();
////
////        Bitmap ori_img = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
//        Log.i("QLWJRJKJ", "resultingImage.getHeight() : " + resultingImage.getHeight());
//        Log.i("QLWJRJKJ", "resultingImage.getWidth() : " + resultingImage.getWidth());
//
//        Bitmap bm1 = Bitmap.createBitmap(resultingImage, (int)minX, (int)minY, (int)maxX - (int)minX, (int)maxY - (int)minY);
//        compositeImageView.setLayoutParams(new LinearLayout.LayoutParams((int)maxX - (int)minX, (int)maxY - (int)minY));
//        compositeImageView.setImageBitmap(bm1);
//
//
//
//        btn_success.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bitmap aaa = BitmapUtil.cropCenterBitmap(resultingImage,3000,3000);
//                compositeImageView.setImageBitmap(aaa);
////                performCrop(getImageUri(getApplicationContext(), resultingImage));
////                Intent resultIntent = new Intent();
////                resultIntent.putExtra("result", saveBitmapToJpeg(resultingImage));
////                setResult(RESULT_OK, resultIntent);
////                finish();
//            }
//        });
//
//    }
//
//    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    float maxX = Integer.MIN_VALUE;
//    float minX = Integer.MAX_VALUE;
//    float maxY = Integer.MIN_VALUE;
//    float minY = Integer.MAX_VALUE;
//
//    private void getMaxMinData(){
//        for(int i=0;i<SomeView.points.size();i++){
//           if(SomeView.points.get(i).x > maxX){
//               maxX = SomeView.points.get(i).x;
//           }
//
//            if(SomeView.points.get(i).y > maxY){
//                maxY = SomeView.points.get(i).y;
//            }
//
//            if(SomeView.points.get(i).x < minX){
//                minX = SomeView.points.get(i).x;
//            }
//
//            if(SomeView.points.get(i).y < minY){
//                minY = SomeView.points.get(i).y;
//            }
//        }
//
//        printLog("Hi maxX : " + maxX);
//        printLog("Hi maxY : " + maxY);
//        printLog("Hi minX : " + minX);
//        printLog("Hi minY : " + minY);
//    }
//
//    private final int PIC_CROP = 1001;
////    private void performCrop(Uri picUri) {
////        try {
////            Intent cropIntent = new Intent("com.android.camera.action.CROP");
////            // indicate image type and Uri
////            cropIntent.setDataAndType(picUri, "image/*");
////            // set crop properties here
////            cropIntent.putExtra("crop", true);
////            // indicate aspect of desired crop
////            cropIntent.putExtra("aspectX", 1);
////            cropIntent.putExtra("aspectY", 1);
////            // indicate output X and Y
////            cropIntent.putExtra("outputX", 128);
////            cropIntent.putExtra("outputY", 128);
////            // retrieve data on return
////            cropIntent.putExtra("return-data", true);
////            // start the activity - we handle returning in onActivityResult
////            startActivityForResult(cropIntent, PIC_CROP);
////        }
////        // respond to users whose devices do not support the crop action
////        catch (ActivityNotFoundException anfe) {
////            // display an error message
////            String errorMessage = "Whoops - your device doesn't support the crop action!";
////            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
////            toast.show();
////        }
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == PIC_CROP) {
////            if (data != null) {
////                // get the returned data
////                Bundle extras = data.getExtras();
////                // get the cropped bitmap
////                Bitmap selectedBitmap = extras.getParcelable("data");
////
////                compositeImageView.setImageBitmap(selectedBitmap);
////            }
////        }
////    }
////
////    private File saveBitmapToJpeg(Bitmap bitmap) {
////
////        //내부저장소 캐시 경로를 받아옵니다.
////        File storage = getCacheDir();
////        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd_HHmmss");
////        Date date = new Date();
////        //저장할 파일 이름
////        String fileName = date + ".png";
////
////        //storage 에 파일 인스턴스를 생성합니다.
////        File tempFile = new File(storage, fileName);
////
////        try {
////
////            // 자동으로 빈 파일을 생성합니다.
////            tempFile.createNewFile();
////
////            // 파일을 쓸 수 있는 스트림을 준비합니다.
////            FileOutputStream out = new FileOutputStream(tempFile);
////
////            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
////
////            // 스트림 사용후 닫아줍니다.
////            out.close();
////
////        } catch (FileNotFoundException e) {
////            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
////        } catch (IOException e) {
////            Log.e("MyTag","IOException : " + e.getMessage());
////        }
////        return tempFile;
////    }
//}
//
