package com.javajiale.accounting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by javajiale on 2015/12/8.
 */
public class MainPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MainPageAdapter(FragmentManager fm){
        super(fm);
    }

    public MainPageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return fragments.size();
    }


}
