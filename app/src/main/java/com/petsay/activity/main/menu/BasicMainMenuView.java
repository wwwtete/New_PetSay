package com.petsay.activity.main.menu;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.petsay.activity.main.adapter.MainSlidingMenuAdapter;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.component.view.slidingmenu.SlidingMenu;
import com.petsay.component.view.slidingmenu.SlidingMenuByView;
import com.petsay.component.view.slidingmenu.SlidingMenuItemModel;
import com.petsay.vo.petalk.PetVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/14
 * @Description
 */
public abstract class BasicMainMenuView extends RelativeLayout implements SlidingMenuItemModel.ClickMenuItemCallback,SlidingMenuByView.MenuByViewInterface {

    protected MainSlidingMenuAdapter mAdapter;

    public BasicMainMenuView(Context context) {
        super(context);
        initView();
    }

    public BasicMainMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    protected void initView() {
        mAdapter = new MainSlidingMenuAdapter(getContext());
    }

    protected boolean isLogin(){
        return UserManager.getSingleton().isLoginStatus();
    }

    protected PetVo getActivePetInfo(){
        return UserManager.getSingleton().getActivePetInfo();
    }

    /**
     * 检查是否未登录需要跳转到登陆页面
     * @return true：未登录需要跳转登陆页面 | false：已登陆，不需要跳转
     */
    protected boolean checkJumpLogin(){
        boolean status = isLogin();
        if(!status){
            jumpActivity(UserLogin_Activity.class);
        }
        return !status;
    }

    protected void jumpActivity(Intent intent){
        getContext().startActivity(intent);
    }

    protected void jumpActivity(Class clasz){
        jumpActivity(new Intent(getContext(),clasz));
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onAttachMenu(SlidingMenu menu) {

    }

    @Override
    public void onOpenMenu() {

    }

    @Override
    public void onCloseMenu() {

    }

    @Override
    public void onResume() {

    }

}
