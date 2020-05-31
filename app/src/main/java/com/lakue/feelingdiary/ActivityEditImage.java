package com.lakue.feelingdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ActivityEditImage extends AppCompatActivity {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);


        if(getIntent() != null){

            String image = getIntent().getStringExtra("image");
//            bitmap = (Bitmap)getIntent().getParcelableExtra("image");
            Uri dataUri = Uri.parse(image);

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(new SomeView(this, bitmap));
    }
}
