package com.petsay.activity.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.adapter.HomeViewPageAdapter;
import com.petsay.activity.main.menu.MainLeftMenuView;
import com.petsay.activity.main.menu.MainRightMenuView;
import com.petsay.activity.main.square.SquareFragment;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.user.signin.SigninActivity;
import com.petsay.application.CheckVersionManager;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow;
import com.petsay.component.view.BottomCameraView;
import com.petsay.component.view.BottomCameraView.OnBottomTabChangeListener;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.MarkImageView;
import com.petsay.component.view.ReleaseTypeSelectView;
import com.petsay.component.view.slidingmenu.SlidingMenu;
import com.petsay.component.view.slidingmenu.SlidingMenuByView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.MemberNet;
import com.petsay.network.net.UserNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.sign.ActivityPartakeVo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/7
 * @Description
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,BasePopupWindow.IAddShowLocationViewService, OnBottomTabChangeListener, NetCallbackInterface, SlidingMenu.SlidingMenuCallback {


    private SlidingMenu mSlidingMenu;
    @InjectView(R.id.miv_msg)
    private MarkImageView mIvMsg;
    @InjectView(R.id.iv_user)
    private CircleImageView mIvUser;
    @InjectView(R.id.vp_content)
    private ViewPager mViewPage;
    @InjectView(R.id.layout_petalk)
    private LinearLayout mLayoutPetalk;
    @InjectView(R.id.tv_hot)
    private TextView mTvHot;
    @InjectView(R.id.tv_square)
    private TextView mTvSquare;
    @InjectView(R.id.tv_focus)
    private TextView mTvFocus;
    @InjectView(R.id.personalcustomview)
    private PersonalCustomView mPersonalCustomView;
    @InjectView(R.id.main_bottom)
    private BottomCameraView mBottomView;
    //发布说说
    @InjectView(R.id.release_sel)
    private ReleaseTypeSelectView mReleaseTypeSelectView;
    @InjectView(R.id.img_sign)
    private ImageView mIvSing;

    private TextView[] mTabTitles;
    private HomeViewPageAdapter mAdapter;
    private MainLeftMenuView mLeftMenu;
    private MainRightMenuView mRightMenu;
    private MemberNet mMemberNet;
    private UserNet mUserNet;
    private int mClickBackCount;
    private int mUnReadSysMsgCount;
    private ActivityPartakeVo mActivityPartakeVo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        initData();
        initUserInfo();
        initView();
        initPettalkContent();
        checkAnnouncement();
    }
    //------------------------------初始化代码区------------------------------------------//
//    /** 初始化用户信息*/
    private void initUserInfo() {
//        UserInfo info;
//        try {
//            Object object= DataFileCache.getSingleton().loadObject(Constants.UserFile);
//            if (null!=object) {
//                info=(UserInfo) object;
                if (isLogin()) {
//                    getUserManager().setUserInfo(this, info);
                    ChatMsgManager.getInstance().auth();
                    mUserNet.petOne(getActivePetId(), getActivePetId());
                    DecorationDataManager.getInstance(this).getServerDecorationData();
                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**初始化数据*/
    private void initData(){
        mMemberNet=new MemberNet();
        mMemberNet.setTag(this);
        mMemberNet.setCallback(this);

        mUserNet=new UserNet();
        mUserNet.setTag(this);
        mUserNet.setCallback(this);
        CheckVersionManager.getSingleton().checkVersion(this);
    }



    /**初始View*/
    @Override
    protected void initView() {
        super.initView();
        mTabTitles = new TextView[]{mTvHot,mTvSquare,mTvFocus};
        initSlidingMenu();
        setListener();
        mLayoutPetalk.setVisibility(View.VISIBLE);
		mPersonalCustomView.setVisibility(View.GONE);
    }

    /**初始化侧滑菜单*/
    private void initSlidingMenu() {
        mLeftMenu = new MainLeftMenuView(this,mIvMsg);
        mRightMenu = new MainRightMenuView(this);
        mSlidingMenu = new SlidingMenuByView(this,mLeftMenu,mRightMenu);
        mSlidingMenu.setBackground(R.drawable.menu_bg);
        mSlidingMenu.attachToActivity(this);
        mSlidingMenu.setScaleValue(0.85f);
        mSlidingMenu.setLeftTranslationX(getResources().getDimension(R.dimen.left_menu_width));
        mSlidingMenu.setRightTranslationX(getResources().getDimension(R.dimen.right_menu_width));
        mSlidingMenu.setDisabledSlidingDirection(SlidingMenu.DIRECTION_LEFT);
        mSlidingMenu.setDisabledSlidingDirection(SlidingMenu.DIRECTION_RIGHT);
    }

    private void setListener() {
        mIvUser.setOnClickListener(this);
        mIvMsg.setOnClickListener(this);
        mTvFocus.setOnClickListener(this);
        mTvSquare.setOnClickListener(this);
        mTvHot.setOnClickListener(this);
        mViewPage.setOnPageChangeListener(this);
        mBottomView.setClickListener(this);
        mIvSing.setOnClickListener(this);
        mBottomView.setOnBottomTabChangeListener(this);
        mSlidingMenu.setMenuCallback(this);
    }

    /**初始化petalk内容*/
    private void initPettalkContent() {
        Fragment hot = new HotPetalkFragment();
        Fragment squ = new SquareFragment();
        Fragment focus = new HomeFocusFragment();
        List<Fragment> fragments = new ArrayList<Fragment>(3);
        fragments.add(hot);
        fragments.add(squ);
        fragments.add(focus);
        mAdapter = new HomeViewPageAdapter(getSupportFragmentManager(),fragments);
        mViewPage.setAdapter(mAdapter);
        mViewPage.setOffscreenPageLimit(fragments.size());
        onChangeTabTitle(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mClickBackCount = 0;
        mSlidingMenu.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    //-----------------------------开始显示区域----------------------------------------//
    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null) {
            setIntent(intent);
            checkAnnouncement();
                int index = getIntent().getIntExtra(HomeFragment.PAGEINDEX,mViewPage.getCurrentItem());
                boolean flag = getIntent().getBooleanExtra("flag", false);
            if(flag)
                mViewPage.setCurrentItem(index);

            int tabIndex = getIntent().getIntExtra("tagindex",-1);
            if(tabIndex > -1){
                mSlidingMenu.closeMenu();
                mBottomView.setTabChnageIndex(tabIndex);
            }

        }
        super.onNewIntent(intent);
    }

    /**检查是否有公告内容*/
    private void checkAnnouncement() {
        if(getIntent().getBooleanExtra("announcement", false)){
            PublicMethod.showAnnouncementDialog(this, getIntent().getStringExtra("content"));
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_msg:
                mSlidingMenu.openMenu(0);
                break;
            case R.id.iv_user:
                mSlidingMenu.openMenu(1);
                break;
            case R.id.tv_hot:
                mViewPage.setCurrentItem(0);
                break;
            case R.id.tv_square:
                mViewPage.setCurrentItem(1);
                break;
            case R.id.tv_focus:
                mViewPage.setCurrentItem(2);
                break;
            case R.id.img_camera:
    			// 拍照时进行登录判断
    			if (UserManager.getSingleton().isLoginStatus()) {
    				if (mReleaseTypeSelectView.getVisibility() == View.GONE) {
    					mReleaseTypeSelectView.show();
    				} else {
    					mReleaseTypeSelectView.hide();
    				}
    			} else {
    				Intent intent = new Intent(getApplicationContext(),UserLogin_Activity.class);
    				startActivity(intent);
    			}
    			break;
            case R.id.img_sign:
                Intent intent = new Intent();
                if(isLogin() && mActivityPartakeVo != null) {
                    intent.setClass(this, SigninActivity.class);
                    intent.putExtra("activitypartakevo", mActivityPartakeVo);
                }else {
                    intent.setClass(this,UserLogin_Activity.class);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        onChangeTabTitle(i);
        if(i != 0)
            GifViewManager.getInstance().stopGif();
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    protected void onResume() {
        mSlidingMenu.onResume();
        MobclickAgent.onResume(this);
        onRefreshUserState();
        onCheckShowHint();
        onCheckPlayMode();
        super.onResume();
        holderPowerManager();
    }

    private void onRefreshUserState(){
        PetVo activePetInfo=getActivePetInfo();
        if (isLogin() && null !=activePetInfo) {
            if(!(mActivityPartakeVo != null && getActivePetId().equals(mActivityPartakeVo.getmCurrentPetID()) && mActivityPartakeVo.ismSigned()))
                mMemberNet.activityPartake(activePetInfo.getId());
            ImageLoaderHelp.displayHeaderImage(activePetInfo.getHeadPortrait(),mIvUser);
        }else {
            mIvSing.setVisibility(View.GONE);
            mIvUser.setImageResource(R.drawable.placeholderhead);
        }
    }

    private void onCheckPlayMode(){
        int mode = SharePreferenceCache.getSingleton(this).getPlayMode();
        if(mode == -1)
            PublicMethod.showSetPlayModeDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mSlidingMenu.isOpened()){
                mSlidingMenu.closeMenu();
            }else if (mPersonalCustomView.getVisibility() == View.VISIBLE) {
            	mBottomView.returnMainPage();
                onBottomTabChange(0);
            }else if(mReleaseTypeSelectView.getVisibility()==View.VISIBLE){
                mReleaseTypeSelectView.hide();
            }else {
                onExit();
            }
            return true;
        }
        mClickBackCount = 0;
        return super.onKeyDown(keyCode, event);
    }
    public void onExit(){
        mClickBackCount ++;
        if(mClickBackCount == 1){
            ToastUtiles.showDefault(this, "再按一次退出");
        }else {
            this.finish();
        }
    }

    private void onChangeTabTitle(int position){
       if(position >=0 && position < mTabTitles.length) {
           for (int i=0;i<mTabTitles.length;i++){
               TextView tv = mTabTitles[i];
               if(i == position){
                   tv.setBackgroundResource(R.drawable.home_title_select);
                   tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                   tv.setTextColor(getResources().getColor(R.color.home_title_selected));
                   tv.getPaint().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//setFakeBoldText(true);
               }else {
                   tv.setBackgroundResource(0);
                   tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                   tv.setTextColor(getResources().getColor(R.color.home_title_text));
                   tv.getPaint().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//setFakeBoldText(false);
               }
           }
       }
    }

    @Override
    public View getParentView() {
        return mLayoutRoot;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

	@Override
	public void onBottomTabChange(int tabIndex) {
		if (tabIndex==0) {
			mLayoutPetalk.setVisibility(View.VISIBLE);
			mPersonalCustomView.setVisibility(View.GONE);
		}else {
			mPersonalCustomView.setVisibility(View.VISIBLE);
			mLayoutPetalk.setVisibility(View.GONE);
		}
		
	}

    /**
     * 获取数据成功回调接口
     *
     * @param bean        服务器返回数据
     * @param requestCode 区分请求码
     */
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode) {
            case RequestCode.REQUEST_ACTIVITYPARTAKE:
                List<ActivityPartakeVo> activityPartakeVos = null;
                try {
                    activityPartakeVos= JsonUtils.getList(bean.getValue(), ActivityPartakeVo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < activityPartakeVos.size(); i++) {
                    if (activityPartakeVos.get(i).getCode().equals("signIn")) {
                        mActivityPartakeVo =activityPartakeVos.get(i);
                        if (mActivityPartakeVo.getState()==1) {
                            mActivityPartakeVo.setmSigned(false);
                            mActivityPartakeVo.setmCurrentPetID(getActivePetId());
                            mIvSing.setVisibility(View.VISIBLE);
                        }else {
                            mActivityPartakeVo.setmCurrentPetID(getActivePetId());
                            mActivityPartakeVo.setmSigned(true);
                            mIvSing.setVisibility(View.GONE);
                        }
                        break;
                    }
                }

                break;
            case RequestCode.REQUEST_PETONE:
                PetVo petInfo = JsonUtils.resultData(bean.getValue(),PetVo.class);
                petInfo.setActive(true);
                UserManager.getSingleton().setActivePetInfo(petInfo);
                DataFileCache.getSingleton().asyncSaveData(Constants.UserFile,UserManager.getSingleton().getUserInfo());
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据失败回调接口(也包括服务器返回500的错误)
     *
     * @param error       错误信息类
     * @param requestCode 请求码
     */
    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
    }

    @Override
    public void onOpenMenu(int direction) {
        if(direction == SlidingMenu.DIRECTION_RIGHT) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvUser, "alpha", 0.0f);
            animator.setDuration(300);
            animator.start();
        }
    }

    @Override
    public void onCloseMenu(int direction) {
        if(direction == SlidingMenu.DIRECTION_RIGHT) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvUser, "alpha", 1.0f);
            animator.setDuration(300);
            animator.start();
        }
    }

    //----------------------------------Destroy代码区-------------------------------------------//
    @Override
    protected void onDestroy() {
        UserManager.isUserChanged=false;
        release();
        SharePreferenceCache.getSingleton(getApplicationContext()).setIsRunningApp(false);
        mMemberNet.cancelAll(this);
        releasePower();
        super.onDestroy();
    }

    /**
     * 释放资源
     */
    private void release(){
        String soudPath = FileUtile.getPath(getApplicationContext(), Constants.SOUND_FILEPATH);
        FileUtile.deleteDir(new File(soudPath));
        ChatDataBaseManager.getInstance().release();
        ChatMsgManager.getInstance().closeClient();
    }

    private PowerManager.WakeLock mWakeLock;
    public void holderPowerManager(){
        if(mWakeLock == null) {
            PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
            mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "emsg");
            if (!mWakeLock.isHeld()){
                try {
                    mWakeLock.acquire();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void releasePower(){
        if(mWakeLock != null){
            mWakeLock.release();
        }
        mWakeLock = null;
    }

    private void onCheckShowHint(){
        boolean isFirst = SharePreferenceCache.getSingleton(this).getBooleanValue("mainactivity_firstin",true);
        if(isFirst) {
            MainHintView view = new MainHintView(this);
            view.setmCallback(new MainHintView.MainHintViewCallback() {
                @Override
                public void onClose(MainHintView view) {
                    mLayoutRoot.removeView(view);
                    SharePreferenceCache.getSingleton(MainActivity.this).setBooleanValue("mainactivity_firstin", false);
                }
            });
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLayoutRoot.addView(view,params);
        }
    }

}
