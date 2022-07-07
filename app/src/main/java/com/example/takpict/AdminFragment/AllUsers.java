package com.example.takpict.AdminFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.takpict.Adapter.UsersAdapter;
import com.example.takpict.R;
import com.example.takpict.modle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllUsers extends Fragment {

    RecyclerView rec;
    ArrayList<User> users;

    UsersAdapter adapter;
    FirebaseFirestore store;


    public AllUsers() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_all_users, container, false);


        store=FirebaseFirestore.getInstance();
        rec=view.findViewById(R.id.recusers);
        users=new ArrayList<User>();
        adapter=new UsersAdapter(users,getContext());
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(adapter);

        EventChangeLister();



        return view;
    }

    //users info
    private void EventChangeLister() {
        store.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        users.add(doc.getDocument().toObject(User.class));
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });


    }
}