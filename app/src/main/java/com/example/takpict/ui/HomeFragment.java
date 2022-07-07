package com.example.takpict.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takpict.Adapter.Homeadapter;
import com.example.takpict.Database.Mydatabase;

import com.example.takpict.R;
import com.example.takpict.modle.Homemodle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    ArrayList<Homemodle> homeitems = new ArrayList<>();
    RecyclerView rvHome;
    Homeadapter homeadapter;
    Mydatabase mydatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context;
    Activity activity;

    FirebaseAuth mAuth;
    FirebaseFirestore store;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rvHome = root.findViewById(R.id.rview_home);
        mydatabase = new Mydatabase(getContext());
        context = root.getContext();
        activity = getActivity();

        mAuth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();



        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), MODE_PRIVATE);
        editor = preferences.edit();

        boolean isFirstTemOpen = preferences.getBoolean("isFirstTemOpen", true);
        if (isFirstTemOpen) {

            //add 2img   2text to app always
            mydatabase.insertitem("اللهم وإن تُهنا في ممرّات الطريق حدّد لنا الوجهة المُناسبة، وتولّنا بحُسنِ تدبيرك وسعة فضلك، وسخّر لنا ياربّ الخير، كُلّ خير", "https://www.muhtwa.com/wp-content/uploads/%D8%B5%D9%88%D8%B1-%D9%85%D8%A8%D9%87%D8%AC%D8%A939.jpg ", 0);
            mydatabase.insertitem("اللهم شُعورًا يُغشي الرُّوح بالطمأنينة", "https://morningg.cc/wp-content/uploads/2018/08/1923-3.jpg ", 0);

            editor.putBoolean("isFirstTemOpen", false);
            editor.apply();
        }

        homeitems.clear();
        homeitems = mydatabase.getItems();
        homeadapter = new Homeadapter(homeitems);

        //reverse the element order of mylist
        Collections.reverse(homeitems);
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHome.setAdapter(homeadapter);




        return root;
    }


}