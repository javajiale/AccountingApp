package com.javajiale.accounting;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        OnPageChangeListener, OnCheckedChangeListener{

    private ViewPager pager;
    private MainPageAdapter adapter;
    private List<Fragment> fragments;
    private RadioGroup group;
    private RadioButton query_button,main_button,static_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DBHelper helper = new DBHelper(MainActivity.this,"acc.db",null,1);
        try{
            //helper.deleteDatabase(MainActivity.this);
            helper.createDatabase(MainActivity.this);
        }catch (IOException e ){
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new ArrayList<Fragment>();
        fragments.add(new QueryFragment());
        fragments.add(new MainFragment());
        fragments.add(new StaticFragment());

        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new MainPageAdapter(getSupportFragmentManager(),fragments);

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size() - 1);
        pager.setOnPageChangeListener(this);

        group = (RadioGroup) findViewById(R.id.titlegroup);
        query_button = (RadioButton)findViewById(R.id.query_button);
        main_button = (RadioButton)findViewById(R.id.main_button);
        static_button = (RadioButton)findViewById(R.id.static_button);
        group.setOnCheckedChangeListener(this);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.query_button:
                pager.setCurrentItem(0);
                break;

            case R.id.main_button:
                pager.setCurrentItem(1);
                break;

            case R.id.static_button:
                pager.setCurrentItem(2);
                break;

            default:
                break;
        }
    }

    private void getTabState(int index) {
        query_button.setChecked(false);
        main_button.setChecked(false);
        static_button.setChecked(false);


        switch (index){
            case 0:
                query_button.setChecked(true);
                break;
            case 1:
                main_button.setChecked(true);
                break;
            case 2:
                static_button.setChecked(true);
                break;
            default:
                break;
        }
    }

    public MainPageAdapter getAdapter(){
        return adapter;
    }
}
