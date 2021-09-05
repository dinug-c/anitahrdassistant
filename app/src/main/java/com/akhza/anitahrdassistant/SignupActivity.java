package com.akhza.anitahrdassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText username, useremail, userpassword;
    String getuser, getemail, getpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        username = findViewById(R.id.user_name);
        useremail = findViewById(R.id.user_email);
        userpassword = findViewById(R.id.user_pass);

        Button daftarbtn = findViewById(R.id.daftar_btn);

        getuser = username.getText().toString();
        getemail = useremail.getText().toString();
        getpass = userpassword.getText().toString();

        daftarbtn.setOnClickListener(view -> {
            if (username.getText().toString().isEmpty()
                    && userpassword.getText().toString().isEmpty()
                    && useremail.getText().toString().isEmpty()) {
                Toast.makeText(SignupActivity.this, "Mohon untuk mengisi form", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> user = new HashMap<>();
                user.put("nama", username);
                user.put("email", useremail);
                user.put("password", userpassword);
                user.put("position", "0");

                db.collection("accounts")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(SignupActivity.this, "Pendaftaran Berhasil! Silakan untuk masuk kembali", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, "Mohon Coba Lagi...", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
}