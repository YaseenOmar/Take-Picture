package com.example.takpict.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takpict.CheckInternetStatus;
import com.example.takpict.HomeApp;
import com.example.takpict.R;
import com.example.takpict.modle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;


public class Signup extends Fragment {

    private static final String TAG = "SignUpActivity";
    EditText nameUser, emailUser, passwordUser;
    Button signup;
    CheckBox show;

    FirebaseFirestore store;
    ProgressDialog progressDialog;
    CircleImageView profile_image;

    private Uri imgeUri = null;
    private StorageReference referenceUserImage;

    private FirebaseAuth mAuth;

    //internet
    CheckInternetStatus mCheckInternetStatus;
    boolean is_internet_connected = false;

    public Signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailUser = view.findViewById(R.id.Etemail);
        nameUser = view.findViewById(R.id.Et_name);
        passwordUser = view.findViewById(R.id.Etpass);
        profile_image = view.findViewById(R.id.profile_image);
        show = view.findViewById(R.id.Cb_showpass);

        store = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        referenceUserImage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://takpict.appspot.com/User photo");

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                profile_image.setImageBitmap(r.getBitmap());
                                imgeUri = r.getUri();

                            }

                        }).show(getActivity());


            }
        });


        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    passwordUser.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    // hide password
                    passwordUser.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });


        signup = view.findViewById(R.id.btn_singup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailUser.getText().toString().trim();
                String password = passwordUser.getText().toString().trim();
                String name = nameUser.getText().toString().trim();

                mCheckInternetStatus = new CheckInternetStatus(getActivity());
                is_internet_connected = mCheckInternetStatus.isInternetConnected();

                if (is_internet_connected) {
                    if (isValidEmail(email) && password.length() >= 6) {
                        regesteruserinfo(email, password);

                    } else {

                        if (!isValidEmail(email))
                            emailUser.setError("Invalid email format");
                       else
                            passwordUser.setError("Password must at least 6 Characters long");

                    }

                    if (TextUtils.isEmpty(password)) {
                        passwordUser.setError("Please enter password!");
                    }
                    if (name.isEmpty())
                        nameUser.setError("Please enter name!");

                    if (TextUtils.isEmpty(email))
                        emailUser.setError("Please enter email!");

                    if (imgeUri == null) {
                        Toast.makeText(getActivity(), "Please choose a picture!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please connect to internet!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }


    //شغال
    private void regesteruserinfo(String emailUser, String passUser) {
        if (imgeUri != null) {
            progressDialog.show();
            final StorageReference childRef = referenceUserImage
                    .child(imgeUri.getLastPathSegment() + ".UserPhoto");
            childRef.putFile(imgeUri).addOnSuccessListener(taskSnapshot ->
                    referenceUserImage.getDownloadUrl())
                    .addOnSuccessListener(imageUri -> {


                        String name = nameUser.getText().toString().trim();

                        mAuth.createUserWithEmailAndPassword(emailUser, passUser)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            String imageUser = imgeUri + "";

                                            String idUser = mAuth.getCurrentUser().getUid();
                                            User user = new User(idUser, emailUser, passUser, name, imageUser);
                                            store.collection("Users").document(mAuth.getCurrentUser().getUid())
                                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        getActivity().finishAffinity();
                                                        Intent intent = new Intent(getActivity(), HomeApp.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();
                                                }
                                            });


                                        }

                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();
                            }
                        });


                    });
        }
        }

        public static boolean isValidEmail (String emailUser){
            return (!TextUtils.isEmpty(emailUser) && Patterns.EMAIL_ADDRESS.matcher(emailUser).matches());
        }

        @Override
        public void onPause () {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPause();
        }

}



