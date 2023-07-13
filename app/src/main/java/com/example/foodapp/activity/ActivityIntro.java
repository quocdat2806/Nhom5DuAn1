package com.example.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.foodapp.MainActivity;
import com.example.foodapp.R;

public class ActivityIntro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView tv_Bat_Dau =findViewById(R.id.tv_bat_dau);
        tv_Bat_Dau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityIntro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}