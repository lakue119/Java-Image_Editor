package com.lakue.feelingdiary;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lakue.feelingdiary.Permission.ModulePermission;
import com.lakue.feelingdiary.Permission.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityFeelingUpload extends BaseActivity {

    Button btn_get_picture;
    ImageView iv_image;
    Boolean isCamera = false;

    private String imageFilePath;
    private Uri photoUri;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_upload);

        btn_get_picture = findViewById(R.id.btn_get_picture);
        iv_image = findViewById(R.id.iv_image);

        btn_get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
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
                startActivity(intent);
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

            Glide.with(this).load(rotatebitmap).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(iv_image);
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
