package com.akhza.anitahrdassistant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.akhza.anitahrdassistant.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText useremail, userpassword;
    String getemail, getpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        useremail = findViewById(R.id.user_email);
        userpassword = findViewById(R.id.user_pass);

        getemail = useremail.getText().toString();
        getpass = userpassword.getText().toString();


    }
}