package com.example.takpict.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.takpict.Database.Mydatabase;
import com.example.takpict.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;


public class StoryFragment extends Fragment {


    EditText et_text;
    Button btn_publish_post;
    Mydatabase mydatabase;
    ImageView image;
    StorageReference reference;
    FirebaseStorage storage;
    Uri uri=null;
    BottomNavigationView but;


    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        et_text = view.findViewById(R.id.et_text_post);
        btn_publish_post = view.findViewById(R.id.btn_publish_post_new);
        image = view.findViewById(R.id.iv_ImageAdded);

        but=getActivity().findViewById(R.id.nav_view);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReferenceFromUrl("gs://takpict.appspot.com/post");



        //add image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                image.setImageBitmap(r.getBitmap());
                                uri = r.getUri();


                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {

                             //  Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                            }
                        }).show(getActivity());


            }
        });


        //add post
        btn_publish_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabase = new Mydatabase(getActivity());
                String text = et_text.getText().toString();

                if (text.isEmpty() && uri==null) {
                    Toast.makeText(getContext(), "You have to choose image or write a text", Toast.LENGTH_LONG).show();
                } else {
                    mydatabase.insertitem(text, uri + "", 1);

                    getParentFragmentManager().beginTransaction().
                            replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                    but.setSelectedItemId(R.id.navigation_home);



                }

                if (uri != null) {
                    //send image to firebase storage
                    StorageReference child = reference.child(System.currentTimeMillis() + ".post");
                    UploadTask uploadTask = child.putFile(uri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference = storage.getReference(child.getPath());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getActivity(), "The image failed to load", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }



        });





        return view;
    }





}