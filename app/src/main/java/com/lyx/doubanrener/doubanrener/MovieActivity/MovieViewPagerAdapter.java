package com.lyx.doubanrener.doubanrener.MovieActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by root on 15-6-21.
 */
public class MovieViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =10;
    private String titles[] ;

    public MovieViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        if (position <= 9 && position >= 0) {
            return MovieAreaFragment.newInstance(titles[position]);
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
