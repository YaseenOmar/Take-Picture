package com.example.takpict.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takpict.CheckInternetStatus;
import com.example.takpict.ForgetPassword;
import com.example.takpict.HomeApp;
import com.example.takpict.MainActivity;
import com.example.takpict.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Signin extends Fragment {
    EditText email, password;
    TextView forgetPass;
    Button singin;
    CheckBox show;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    //internet
    CheckInternetStatus mCheckInternetStatus;
    boolean is_internet_connected = false;

    public Signin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging In...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);


        email = view.findViewById(R.id.Etemail);
        password = view.findViewById(R.id.Etpass);
        show = view.findViewById(R.id.Cb_showpass);

        forgetPass = view.findViewById(R.id.tv_forgetPass);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgetPassword.class);
                startActivity(intent);

            }
        });
        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }

            }
        });
        singin = view.findViewById(R.id.btn_singin);
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText() + "";
                String passUser = password.getText() + "";

                mCheckInternetStatus = new CheckInternetStatus(getActivity());
                is_internet_connected = mCheckInternetStatus.isInternetConnected();

                if (is_internet_connected) {

                    if (isValidEmail(emailUser) && passUser.length() >= 6) {
                        startLogin(emailUser, passUser);
                    } else {
                        if (!isValidEmail(emailUser))
                            email.setError("Invalid email format");
                        else
                            password.setError("Password must at least 6 Characters long");
                    }

                    if (TextUtils.isEmpty(passUser)) {
                        password.setError("Please enter password!");
                    }

                    if (emailUser.isEmpty())
                        email.setError("Please enter email!");
                } else {
                    Toast.makeText(getActivity(), "Please connect to internet!", Toast.LENGTH_LONG).show();
                }

            }


        });
        return view;

    }

    private void startLogin(String emailUser, String passUser) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(emailUser, passUser)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), HomeApp.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();

            }
        });
    }

    public static boolean isValidEmail(String emailUser) {
        return (!TextUtils.isEmpty(emailUser) && Patterns.EMAIL_ADDRESS.matcher(emailUser).matches());
    }

    @Override
    public void onPause() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        super.onPause();
    }
}