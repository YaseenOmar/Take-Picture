package com.example.takpict;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;


import com.example.takpict.modle.Homemodle;
import com.example.takpict.ui.HomeFragment;
import com.example.takpict.ui.profileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.appcheck.interop.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

public class HomeApp extends AppCompatActivity {

    ActionBar bar;
    BottomNavigationView but;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_Story, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03A9F4")));


        but = findViewById(R.id.nav_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String back = bundle.getString("update");
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new profileFragment()).commit();
            but.setSelectedItemId(R.id.navigation_profile);

        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/Take pict");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\n Hello, Join in Takpict family  \n\n";
                shareMessage = shareMessage + "https://drive.google.com/file/d/1wasl1ShBIfFSMrMqpWzgH5RgXR38OyKk/view?usp=sharing" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));


                break;

            case R.id.update:
                Intent update = new Intent(getApplicationContext(), updateUser.class);
                startActivity(update);
                break;

            case R.id.deleteAccount:
                //Alert
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to delete your account?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentUser();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("delete","cancel delete ");

                    }
                });
                builder.create();
                builder.setTitle("Delete Account ");
                builder.show();
                break;


        }


        return super.onOptionsItemSelected(item);
    }


    private void deleteCurrentUser() {
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(), getstarted.class);
                                            Toast.makeText(HomeApp.this, "Sorry to see you leaving us!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            Toast.makeText(HomeApp.this, "Failed to delete your account!! ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(HomeApp.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(HomeApp.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
            }
        });
    }



}