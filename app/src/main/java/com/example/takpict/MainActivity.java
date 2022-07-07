package com.example.takpict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.takpict.Fragment.Signin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser CurrentUser = mAuth.getCurrentUser();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    if (CurrentUser != null) {
                        Intent intent = new Intent(MainActivity.this, HomeApp.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), getstarted.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}