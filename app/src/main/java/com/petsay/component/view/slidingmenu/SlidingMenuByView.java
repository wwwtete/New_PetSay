package com.petsay.component.view.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/9
 * @Description 由自定义的View组成的侧滑菜单
 */
public class SlidingMenuByView extends SlidingMenu {

    private MenuByViewInterface mLeftMenu;
    private MenuByViewInterface mRightMenu;

    public SlidingMenuByView(Context context,MenuByViewInterface leftMenu,MenuByViewInterface rightMenu) {
        super(context);
        this.mLeftMenu = leftMenu;
        this.mRightMenu = rightMenu;
        this.mViewLeftMenu = leftMenu.getView();
        this.mViewRightMenu = rightMenu.getView();
    }

    @Override
    protected void initValue(Activity activity) {
        super.initValue(activity);
        mLeftMenu.onAttachMenu(this);
        mRightMenu.onAttachMenu(this);
    }

    /**
     * 由子类实现并初始化左侧侧滑菜单的View
     *
     * @return
     */
    @Override
    protected View getLeftMenuView() {
        return this.mViewLeftMenu;
    }

    /**
     * 由子类实现并初始化右侧侧滑菜单View
     *
     * @return
     */
    @Override
    protected View getRightMenuView() {
        return this.mViewRightMenu;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLeftMenu.onResume();
        mRightMenu.onResume();
    }

    @Override
    protected void onOpenMenu() {
        if(mSlidingDirection == DIRECTION_RIGHT)
            mRightMenu.onOpenMenu();
        else
            mLeftMenu.onOpenMenu();
        super.onOpenMenu();
    }

    @Override
    protected void onCloseMenu() {
        if(mSlidingDirection == DIRECTION_RIGHT)
            mRightMenu.onCloseMenu();
        else
            mLeftMenu.onCloseMenu();
        super.onCloseMenu();
    }

    public interface MenuByViewInterface {
        public View getView();
        public void onAttachMenu(SlidingMenu menu);
        public void onOpenMenu();
        public void onCloseMenu();
        public void onResume();
    }

}
