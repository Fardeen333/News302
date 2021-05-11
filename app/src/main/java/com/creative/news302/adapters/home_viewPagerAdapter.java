package com.creative.news302.adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class home_viewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> lstFragment =new ArrayList<>();
    private final List<String> lstTitle=new ArrayList<>();



    public home_viewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return lstFragment.get(i);
    }

    @Override
    public int getCount() {
        return lstTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);
    }

    public void AddFragment(Fragment fragment, String title){
        lstFragment.add(fragment);
        lstTitle.add(title);
    }
}
