package com.example.takpict.BottomSheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.takpict.R;
import com.example.takpict.getstarted;
import com.example.takpict.modle.Rater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class BottomSheet extends BottomSheetDialogFragment {

    Button ok, rateLater;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore store;
    Rater rater;
    RatingBar ratingBar;
    String TAG = "MAS";


    public BottomSheet() {
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet, container, false);


        ok = view.findViewById(R.id.btnOk);
        rateLater = view.findViewById(R.id.btn_rateLater);
        ratingBar = view.findViewById(R.id.rating);

        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();


        String userID = mAuth.getCurrentUser().getUid();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float rateNo = ratingBar.getRating();
                rater = new Rater(userID, rateNo + "");

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rater != null) {

                    store.collection("Rating").document(userID).set(rater).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e(TAG,  task.getResult()+"");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.e(TAG, e.getMessage() + "");
                        }
                    });

                }

                mAuth.signOut();
                Intent intent = new Intent(getContext(), getstarted.class);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Sign out your account...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                startActivity(intent);
                dismiss();

            }
        });


        rateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(getContext(), getstarted.class);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Sign out your account...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                startActivity(intent);
                dismiss();

            }
        });

        return view;
    }
}
