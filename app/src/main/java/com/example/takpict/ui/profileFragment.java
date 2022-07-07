package com.example.takpict.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takpict.AdminPage;
import com.example.takpict.BottomSheet.BottomSheet;

import com.example.takpict.R;
import com.example.takpict.getstarted;
import com.example.takpict.modle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.jetbrains.annotations.NotNull;


public class profileFragment extends Fragment {

    Button logout, adminPage;
    TextView tv_email, tv_name, tv_pass;

    FirebaseAuth mAuth;
    FirebaseFirestore store;
    FirebaseUser user;
    String currentUserID;

    ProgressDialog progressDialog;
    CircleImageView profile_image;
    BottomSheet bottomSheet;

    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Sign out your account...");
        progressDialog.setCanceledOnTouchOutside(false);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);

        store = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        tv_name = view.findViewById(R.id.tv_nameUser);
        tv_email = view.findViewById(R.id.tv_emailUser);
        tv_pass = view.findViewById(R.id.tv_passUser);
        profile_image = view.findViewById(R.id.profile_image);
        adminPage = view.findViewById(R.id.adminbut);

        //User info
        store.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            User user = documentSnapshot.toObject(User.class);
                            tv_email.setText(user.getEmail());
                            tv_name.setText(user.getName());
                            tv_pass.setText(user.getPassword());
                            profile_image.setImageURI(Uri.parse(user.getPhoto()));


                        }
                    }
                });


        //check Admin
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserID = user.getEmail();
            if (currentUserID.equals("fuadmuharam20@gmail.com")) {
                adminPage.setVisibility(View.VISIBLE);
            } else
                adminPage.setVisibility(View.GONE);
        }


        adminPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminPage.class);
                startActivity(intent);
            }
        });


         bottomSheet=new BottomSheet();
        //sign out
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                store.collection("Rating").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            progressDialog.show();

                            mAuth.signOut();
                            Intent intent = new Intent(getActivity(), getstarted.class);
                            startActivity(intent);
                            getActivity().finishAffinity();


                        } else {
                            bottomSheet.show(getParentFragmentManager(),"TAG");
                        }

                    }
                });


            }
        });


        return view;

    }




}

