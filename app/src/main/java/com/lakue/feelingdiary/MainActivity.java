package com.lakue.feelingdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lakue.feelingdiary.Base.BaseActivity;

public class MainActivity extends BaseActivity {

    TextView tv_move_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_move_edit = findViewById(R.id.tv_move_edit);

        tv_move_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityFeelingUpload.class);
                startActivity(intent);
            }
        });
    }
}
