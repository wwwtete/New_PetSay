package com.petsay.component.view.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/9
 * @Description 由SlidingMenuItem来组成的侧滑菜单
 */
public class SlidingMenuByItem extends SlidingMenu {

        private ScrollView mSv_left;
    private LinearLayout mLlLeftMenu;
    private ScrollView mSv_right;
    private LinearLayout mLlRightMenu;
    private List<SlidingMenuItem> mLeftItems;
    private List<SlidingMenuItem> mRightItems;


    public SlidingMenuByItem(Context context) {
        super(context);
    }

    public SlidingMenuByItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 由子类实现并初始化左侧侧滑菜单的View
     *
     * @return
     */
    @Override
    protected View getLeftMenuView() {
        return mSv_left;
    }

    /**
     * 由子类实现并初始化右侧侧滑菜单View
     *
     * @return
     */
    @Override
    protected View getRightMenuView() {
        return mSv_right;
    }

    @Override
    protected void initView() {
        super.initView();
        mSv_left = new ScrollView(getContext());
        mLlLeftMenu = new LinearLayout(getContext());
        mLlLeftMenu.setOrientation(LinearLayout.VERTICAL);
        mSv_left.addView(mLlLeftMenu);

        mSv_right = new ScrollView(getContext());
        mSv_right.setBackgroundColor(Color.RED);
        mLlRightMenu = new LinearLayout(getContext());
        mLlRightMenu.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        mSv_right.addView(mLlRightMenu);
        mLlRightMenu.setGravity(Gravity.RIGHT);
    }

    @Override
    protected void initValue(Activity activity) {
        mLeftItems = new ArrayList<SlidingMenuItem>();
        mRightItems = new ArrayList<SlidingMenuItem>();
        super.initValue(activity);
    }

    /**
     * Add a single items;
     *
     * @param menuItem
     * @param direction 0:左侧 1：右侧
     */
    public void addMenuItem(SlidingMenuItem menuItem, int direction){
        if (direction == DIRECTION_LEFT){
            this.mLeftItems.add(menuItem);
            mLlLeftMenu.addView(menuItem);
        }else{
            this.mRightItems.add(menuItem);
            mLlRightMenu.addView(menuItem);
        }
    }

    /**
     * 设置MenuItem
     *
     * @param menuItems MenuItem列表
     * @param direction 0:左侧 1：右侧
     */
    public void setMenuItems(List<SlidingMenuItem> menuItems, int direction){
        if (direction == DIRECTION_LEFT)
            this.mLeftItems = menuItems;
        else
            this.mRightItems = menuItems;
        rebuildMenu();
    }

    private void rebuildMenu(){
        mLlLeftMenu.removeAllViews();
        mLlRightMenu.removeAllViews();
        for (SlidingMenuItem leftMenuItem : mLeftItems)
            mLlLeftMenu.addView(leftMenuItem);

        for (SlidingMenuItem rightMenuItem : mRightItems)
            mLlRightMenu.addView(rightMenuItem);
    }

    /**
     * 获取参数指定方向的MneuItem
     * @param direction 0:左侧 1：右侧
     * @return
     */
    public List<SlidingMenuItem> getMenuItems(int direction) {
        if ( direction == DIRECTION_LEFT)
            return mLeftItems;
        else
            return mRightItems;
    }
}
