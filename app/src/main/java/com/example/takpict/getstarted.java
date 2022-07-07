package com.example.takpict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.takpict.Adapter.HomePagerAdapter;
import com.example.takpict.Fragment.Signin;
import com.example.takpict.Fragment.Signup;
import com.example.takpict.databinding.ActivityGetstartedBinding;
import com.google.android.material.tabs.TabLayout;

public class getstarted extends AppCompatActivity {


    TabLayout tapLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);


        viewPager = findViewById(R.id.viewPager);
        tapLayout = findViewById(R.id.tapLayout);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new Signin(), "Sign in");
        pagerAdapter.addFragment(new Signup(), "Sign up");


        viewPager.setAdapter(pagerAdapter);
        tapLayout.setupWithViewPager(viewPager);
        tapLayout.getTabAt(0).setText(pagerAdapter.getFragmentTitle(0));
        tapLayout.getTabAt(1).setText(pagerAdapter.getFragmentTitle(1));

    }
}