package com.lakue.imageEditor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ActivityTest extends AppCompatActivity {

    ImageView imageView;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        imageView = findViewById(R.id.image);

        if(getIntent() != null){
            image = getIntent().getByteArrayExtra("image");
        }

        Glide.with(this).load(image).into(imageView);
    }
}
