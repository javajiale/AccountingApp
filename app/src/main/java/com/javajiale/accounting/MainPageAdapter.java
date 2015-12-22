package com.javajiale.accounting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;
import java.util.Objects;

/**
 * Created by javajiale on 2015/12/8.
 */
public class MainPageAdapter extends FragmentPagerAdapter {

//    public MainPageAdapter(FragmentManager fm){
//        super(fm);
//
//    }
//    @Override
//    public Fragment getItem(int position) {
//        return ArrayListFragment.newInstance(position);
//    }
//
//    @Override
//    public int getCount() {
//        return NUM_ITEMS;
//    }
//
//    @Override
//    public int getItemPosition(Object object){
//        return POSITION_NONE;
//    }
//
    private static final String TAG = "YiPageAdapter";
    private List<Fragment> fragments;
    private FragmentManager mFragmentManager;
    /**
     * 当数据发生改变时的回调接口
     */
    private OnReloadListener mListener;

    public MainPageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    /**
     * 重新设置页面内容
     * @param items
     */
    public void setPagerItems(List<Fragment> items)
    {
        if (items != null)
        {
            for (int i = 0; i < fragments.size(); i++)
            {
                mFragmentManager.beginTransaction().remove(fragments.get(i)).commit();
            }
            fragments = items;
        }
    }

    /**
     *当页面数据发生改变时你可以调用此方法
     *
     * 重新载入数据，具体载入信息由回调函数实现
     */
    public void reLoad()
    {
        if(mListener != null)
        {
            mListener.onReload();
        }
        this.notifyDataSetChanged();
    }
    public void setOnReloadListener(OnReloadListener listener)
    {
        this.mListener = listener;
    }
    /**
     * @author Rowand jj
     *回调接口
     */
    public interface OnReloadListener
    {
        public void onReload();
    }

}
