package com.lakue.feelingdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_content_decorate.*

class ActivityContentDecorate : AppCompatActivity() {

    var content = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_decorate)

        if(intent != null){
            content = intent.getIntExtra("EXTRA_CONTENT",0)
        }

        Glide.with(this).load(content).into(iv_content)
    }
}
