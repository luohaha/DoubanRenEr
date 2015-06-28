package com.lyx.doubanrener.doubanrener.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =2;
    private String titles[] ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return new MovieFragment();
            case 1:
                return new PlanFragment();
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        int a = position;
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}