package com.lakue.feelingdiary;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.lakue.feelingdiary.Base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ActivityEditImage extends BaseActivity {

    Bitmap bitmap;
    Bitmap rotatebitmap;
    SomeView someView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        someView = findViewById(R.id.someView);

        if(getIntent() != null){

            String image = getIntent().getStringExtra("image");
//            bitmap = (Bitmap)getIntent().getParcelableExtra("image");
            Uri dataUri = Uri.parse(image);

            bitmap = null;
            try {
                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(getRealpath(dataUri));
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


                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);

                rotatebitmap = rotate(bitmap, exifDegree);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                someView.addBitmap(rotatebitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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


    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(new SomeView(this, rotatebitmap));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == Define.REQUEST_CODE_GET_CROP_IMAGE){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", data.getSerializableExtra("result"));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
}
