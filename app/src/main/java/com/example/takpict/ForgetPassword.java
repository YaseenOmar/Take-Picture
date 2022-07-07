package com.example.takpict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class ForgetPassword extends AppCompatActivity {
    EditText emailPass;
    Button restPass;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        progressDialog = new ProgressDialog(ForgetPassword.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Send email to reset password...");
        progressDialog.setCanceledOnTouchOutside(false);

        emailPass = findViewById(R.id.Et_email_pass);
        restPass = findViewById(R.id.btn_restPass);
        

        mAuth = FirebaseAuth.getInstance();

        restPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailPass.getText() + "";
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailPass.setError("Invalid email format");

                }
                if (email.isEmpty()) {
                    emailPass.setError("Please enter email!");

                }
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()) {

                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();


                            } else {
                                Toast.makeText(ForgetPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }




            }

        });



    }


}