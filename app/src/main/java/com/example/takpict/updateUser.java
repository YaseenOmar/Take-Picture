package com.example.takpict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.takpict.ui.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class updateUser extends AppCompatActivity implements IPickResult {
    Button update;
    EditText et_name, et_pass;
    DatabaseReference reference;
    String userID;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    CircleImageView updateImg;
    FirebaseFirestore store;
    StorageReference referenceStorage;
    FirebaseStorage storage;
    Uri imguser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        progressDialog = new ProgressDialog(updateUser.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Update..");
        progressDialog.setCancelable(false);

        update = findViewById(R.id.btn_update);
        et_name = findViewById(R.id.et_nameUser);
        et_pass = findViewById(R.id.et_passUser);
        updateImg = findViewById(R.id.updateImg);
        store = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        referenceStorage = storage.getReferenceFromUrl("gs://takpict.appspot.com/User photo");


        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        //update user info
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_name.getText().toString();
                String pass = et_pass.getText().toString();


                if (name.isEmpty() && pass.isEmpty() && imguser == null) {

                    et_name.setError("Please enter name!");
                    et_pass.setError("Please enter password!");
                    Toast.makeText(updateUser.this, "You didn't Update anything", Toast.LENGTH_SHORT).show();

                } else {

                    if (!name.isEmpty()) {
                        updateName(name);
                        Intent intent = new Intent(updateUser.this, HomeApp.class);
                        intent.putExtra("update", "update");
                        startActivity(intent);
                        finishAffinity();
                    }

                    if (!pass.isEmpty()) {
                        if (pass.length() >= 6) {

                            updatePass(pass);
                            Intent intent = new Intent(updateUser.this, HomeApp.class);
                            intent.putExtra("update", "update");
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            et_pass.setError("Password must at least 6 Characters long");
                        }
                    }

                    if (imguser != null) {
                        updateimgage();
                        Intent intent = new Intent(updateUser.this, HomeApp.class);
                        intent.putExtra("update", "update");
                        startActivity(intent);
                        finishAffinity();
                    }

                }


            }


        });

        updateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(updateUser.this);
            }
        });


    }

    private void updatePass(String pass) {
        progressDialog.show();
        HashMap User = new HashMap();
        User.put("password", pass);

        mAuth.getCurrentUser().updatePassword(pass);
        store.collection("Users").document(mAuth.getCurrentUser().getUid()).update("password", pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    et_pass.setText(pass);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(updateUser.this, "Failed to Update ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void updateName(String name) {


        progressDialog.show();
        HashMap User = new HashMap();
        User.put("name", name);

        store.collection("Users").document(mAuth.getCurrentUser().getUid()).update("name", name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    et_name.setText(name);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(updateUser.this, "Failed to Update ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            imguser = r.getUri();
            updateImg.setImageBitmap(r.getBitmap());

        }
    }

    private void updateimgage() {
        if (imguser != null) {

            store.collection("Users").document(mAuth.getCurrentUser().getUid()).update("photo", imguser + "")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            StorageReference child = referenceStorage.child(imguser.getLastPathSegment() + ".UserPhoto");
                            UploadTask uploadTask = child.putFile(imguser);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    referenceStorage = storage.getReference(child.getPath());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(updateUser.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(updateUser.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


}