package com.akhza.anitahrdassistant.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akhza.anitahrdassistant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText useremail, userpassword;
    String getemail, getpass, getId;
    SharedPreferences loginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        loginfo = this.getSharedPreferences("Login Info", Context.MODE_PRIVATE);

        useremail = findViewById(R.id.user_email);
        userpassword = findViewById(R.id.user_pass);
        TextView havent = findViewById(R.id.haventtxt);

        getemail = useremail.getText().toString();
        getpass = userpassword.getText().toString();

        Button loginbtn = findViewById(R.id.login_btn);

        havent.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        });

        loginbtn.setOnClickListener(view -> {
            if (userpassword.getText().toString().isEmpty()
                    && useremail.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                db.collection("accounts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {

                                        String a = doc.getString("email");
                                        String b = doc.getString("password");
                                        String c = doc.getString("name");
                                        getId = doc.getId();
                                        String a1 = useremail.getText().toString().trim();
                                        String b1 = userpassword.getText().toString().trim();
                                        if (a.equalsIgnoreCase(a1) & b.equalsIgnoreCase(b1)) {

                                            SharedPreferences.Editor logedit = loginfo.edit();
                                            logedit.putBoolean("log", true);
                                            logedit.putString("email", a); // save data email,pass, dan nama ke sharepref
                                            logedit.putString("pass", b);
                                            logedit.putString("name", c);
                                            logedit.apply();

                                            Map<String, Object> upinfo = new HashMap<>();
                                            upinfo.put("online", "0");

                                            // update db status akun menjadi online
                                            db.collection("accounts").document(getId).update(upinfo)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(LoginActivity.this, "Hi, Again!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                            Intent home = new Intent(LoginActivity.this, HRDDashboardActivity.class);
                                            startActivity(home);
                                            Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                            break;
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Akun tidak ditemukan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }
        });


    }
}