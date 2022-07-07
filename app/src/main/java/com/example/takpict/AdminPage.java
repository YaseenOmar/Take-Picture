package com.example.takpict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.takpict.Adapter.HomePagerAdapter;
import com.example.takpict.AdminFragment.AllUsers;
import com.example.takpict.AdminFragment.UserRates;
import com.example.takpict.Fragment.Signin;
import com.example.takpict.Fragment.Signup;
import com.google.android.material.tabs.TabLayout;

public class AdminPage extends AppCompatActivity {


    TabLayout tapLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);



        viewPager = findViewById(R.id.viewPager);
        tapLayout = findViewById(R.id.tapLayout);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new AllUsers(), "Users");
        pagerAdapter.addFragment(new UserRates(), "Rates");


        viewPager.setAdapter(pagerAdapter);
        tapLayout.setupWithViewPager(viewPager);
        tapLayout.getTabAt(0).setText(pagerAdapter.getFragmentTitle(0));
        tapLayout.getTabAt(1).setText(pagerAdapter.getFragmentTitle(1));
    }
}