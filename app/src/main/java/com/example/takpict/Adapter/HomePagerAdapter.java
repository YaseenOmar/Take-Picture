package com.example.takpict.Adapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragment = new ArrayList<>();
    ArrayList<String> mFragmentTitle = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, HomePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    public void addFragment(Fragment fragment , String title){
        mFragment.add(fragment);
        mFragmentTitle.add(title);
    }

    public String getFragmentTitle(int position){
        return mFragmentTitle.get(position);
    }


}
