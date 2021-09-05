package com.akhza.anitahrdassistant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.akhza.anitahrdassistant.R;
import com.akhza.anitahrdassistant.activities.HRDDashboardActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        Button plmrbtn = findViewById(R.id.plmr_btn);

        //iki aku mau ngerubah tujuan activity ne, kanggo ngecek layoutku wkwks
        plmrbtn.setOnClickListener(view -> startActivity(new Intent(getApplication(), HRDDashboardActivity.class)));

    }
}