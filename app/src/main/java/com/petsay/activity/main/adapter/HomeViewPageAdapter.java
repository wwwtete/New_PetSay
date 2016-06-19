package com.petsay.activity.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/11
 * @Description 主页ViewPage适配器
 */
public class HomeViewPageAdapter extends FragmentPagerAdapter {//FragmentStatePagerAdapter{//

    private List<Fragment> mFragments;

    public HomeViewPageAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        if(mFragments == null || mFragments.isEmpty())
            return 0;
        else
            return mFragments.size();
    }

}
