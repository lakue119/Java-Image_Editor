package com.lakue.feelingdiary;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lakue.feelingdiary.Permission.ModulePermission;
import com.lakue.feelingdiary.Permission.PermissionListener;
import com.lakue.feelingdiary.StickerView.BitmapStickerIcon;
import com.lakue.feelingdiary.StickerView.DeleteIconEvent;
import com.lakue.feelingdiary.StickerView.DrawableSticker;
import com.lakue.feelingdiary.StickerView.FlipHorizontallyEvent;
import com.lakue.feelingdiary.StickerView.StickerView;
import com.lakue.feelingdiary.StickerView.ZoomIconEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ActivityFeelingUpload extends BaseActivity {

    Button btn_get_picture;
    ConstraintLayout constlayout;
    FrameLayout framelayout;
    StickerView stickerView;
//    ImageView iv_image;
    Boolean isCamera = false;

    private String imageFilePath;
    private Uri photoUri;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_upload);

        btn_get_picture = findViewById(R.id.btn_get_picture);
        stickerView = findViewById(R.id.sticker_view);
//        framelayout = findViewById(R.id.framelayout);
//        iv_image = findViewById(R.id.iv_image);

        btn_get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    private void getCropImage(byte[] byteArray, Boolean crop){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
            printLog(ex.toString());
        }
        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        Bitmap resultingImage = Bitmap.createBitmap(widthOfscreen, heightOfScreen, bitmap.getConfig());

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < SomeView.points.size(); i++) {
            path.lineTo(SomeView.points.get(i).x, SomeView.points.get(i).y);
        }
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);
//        compositeImageView.setImageBitmap(resultingImage);

        getMaxMinData();
//
//        Bitmap ori_img = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        Log.i("QLWJRJKJ", "resultingImage.getHeight() : " + resultingImage.getHeight());
        Log.i("QLWJRJKJ", "resultingImage.getWidth() : " + resultingImage.getWidth());

        Bitmap bm1 = Bitmap.createBitmap(resultingImage, (int)minX, (int)minY, (int)maxX - (int)minX, (int)maxY - (int)minY);

        //StickerView stickerView = new StickerView(getApplicationContext());
//        stickerView.setLayoutParams(new LinearLayout.LayoutParams((int)maxX - (int)minX, (int)maxY - (int)minY));
//        img.setImageBitmap(bm1);

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

//        BitmapStickerIcon heartIcon =
//                new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp),
//                        BitmapStickerIcon.LEFT_BOTTOM);
//        heartIcon.setIconEvent(new HelloIconEvent());
        Drawable drawable = new BitmapDrawable(bm1);

        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        stickerView.addSticker(new DrawableSticker(drawable));

//        img.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
//                int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height
//
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    // 뷰 누름
//                    oldXvalue = event.getX();
//                    oldYvalue = event.getY();
//                    Log.d("viewTest", "oldXvalue : "+ oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
//                    Log.d("viewTest", "v.getX() : "+v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
//                    Log.d("viewTest", "RawX : " + event.getRawX() +" RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
//                    Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height
//
//                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
//                    // 뷰 이동 중
//                    v.setX(v.getX() + (event.getX()) - (v.getWidth()/2));
//                    v.setY(v.getY() + (event.getY()) - (v.getHeight()/2));
//
//                }else if(event.getAction() == MotionEvent.ACTION_UP){
//                    // 뷰에서 손을 뗌
//
//                    if(v.getX() < 0){
//                        v.setX(0);
//                    }else if((v.getX() + v.getWidth()) > parentWidth){
//                        v.setX(parentWidth - v.getWidth());
//                    }
//
//                    if(v.getY() < 0){
//                        v.setY(0);
//                    }else if((v.getY() + v.getHeight()) > parentHeight){
//                        v.setY(parentHeight - v.getHeight());
//                    }
//
//                }
//                return true;
//            }
//        });

//        framelayout.addView(stickerView);
//        Glide.with(this).load(bm1).into(img);

//        compositeImageView.setImageBitmap(bm1);
    }

    float oldXvalue;
    float oldYvalue;

    float maxX = Integer.MIN_VALUE;
    float minX = Integer.MAX_VALUE;
    float maxY = Integer.MIN_VALUE;
    float minY = Integer.MAX_VALUE;

    private void getMaxMinData(){
        for(int i=0;i<SomeView.points.size();i++){
            if(SomeView.points.get(i).x > maxX){
                maxX = SomeView.points.get(i).x;
            }

            if(SomeView.points.get(i).y > maxY){
                maxY = SomeView.points.get(i).y;
            }

            if(SomeView.points.get(i).x < minX){
                minX = SomeView.points.get(i).x;
            }

            if(SomeView.points.get(i).y < minY){
                minY = SomeView.points.get(i).y;
            }
        }

        printLog("Hi maxX : " + maxX);
        printLog("Hi maxY : " + maxY);
        printLog("Hi minX : " + minX);
        printLog("Hi minY : " + minY);
    }
    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진/앨범");
        builder.setMessage("사진/앨범 중 선택해주세요.");
        builder.setPositiveButton("사진",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isCamera = true;
                        String[] permissions= {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

                        checkPermission(permissions);
                    }
                });
        builder.setNegativeButton("앨범",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        isCamera = false;
                        //권한부여
                        String[] permissions= {Manifest.permission.READ_EXTERNAL_STORAGE};
                        checkPermission(permissions);
                    }
                });
        builder.show();
    }

    private void checkPermission(String[] permission){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showToast("권한 허가");
                if(isCamera){
                    sendTakePhotoIntent();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, Define.REQUEST_CODE_ALBUM);
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                showToast("권한 거부\n" + deniedPermissions.toString());
            }
        };

        ModulePermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("사진 접근 권한을 허용하지 않으면 사진을 올릴 수 없습니다.\n[설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(permission)
                .check();
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, Define.REQUEST_CODE_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    Bitmap rotatebitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;


        if (requestCode == Define.REQUEST_CODE_ALBUM) {
            Uri dataUri = data.getData();

            //image = data.getStringExtra("editimage");
            if (dataUri != null) {
                image = getRealpath(dataUri);
//                Glide.with(this).load(image).into(iv_image);


//                Bitmap bitmap = imagePathToBitmap(image);

                Intent intent = new Intent(getApplicationContext(), ActivityEditImage.class);
                intent.putExtra("image", dataUri.toString());
                startActivityForResult(intent, Define.REQUEST_CODE_GET_CROP_IMAGE);
            }
        } else if (requestCode == Define.REQUEST_CODE_CAMERA) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            rotatebitmap = rotate(bitmap, exifDegree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            Log.i("AAAAAAAAAAAAAAAA", "imageFilePath" + imageFilePath);

            byte[] currentData = stream.toByteArray();

            new SaveImageTask().execute(currentData);

            image = "NOTEMPTY";

//            Glide.with(this).load(rotatebitmap).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(iv_image);
        } else if(requestCode == Define.REQUEST_CODE_GET_CROP_IMAGE){

            byte[] byteArray = data.getByteArrayExtra("image");
            Boolean crop = data.getBooleanExtra("crop",false);
            getCropImage(byteArray, crop);

//            ImageView imageView = new ImageView(this);
//            LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(params);
//            Glide.with(this).load(data.getSerializableExtra("result")).into(iv_image);
            //constlayout.addView(imageView);

        }
    }

    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }


    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;


            try {
                SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date date = new Date();

                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
                if (!path.exists()) {
                    path.mkdirs();
                }

                String fileName = day.format(date) + ".jpg";
                File outputFile = new File(path, fileName);

                image = outputFile.getPath();

                outStream = new FileOutputStream(outputFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d("PHOTO", "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outputFile.getAbsolutePath());


                // 갤러리에 반영
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                sendBroadcast(mediaScanIntent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private Bitmap imagePathToBitmap(String image) {

        Bitmap bitmap = null;
        try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
            Uri imageUri = Uri.parse(image);

            try {
                bitmap = Bitmap.createBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            File imgFile = new File(image);
            try {
                imgFile.createNewFile();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(imgFile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage().toString());
        }
        return bitmap;
    }

}
