package com.akhza.anitahrdassistant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.akhza.anitahrdassistant.GetStartedActivity;
import com.akhza.anitahrdassistant.R;
import com.akhza.anitahrdassistant.SignupActivity;

public class EntranceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        Button mulai_btn = findViewById(R.id.mulai_btn);

        mulai_btn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), GetStartedActivity.class)));
    }
}