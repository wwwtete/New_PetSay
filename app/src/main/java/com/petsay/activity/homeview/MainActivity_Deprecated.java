package com.petsay.activity.homeview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.award.MyTaskActivity;
import com.petsay.activity.homeview.adapter.SlidingListAdapter;
import com.petsay.activity.message.NotificationTypeView;
import com.petsay.activity.petalk.ReviewHotSayActivity;
import com.petsay.activity.settings.SettingActivity;
import com.petsay.activity.shop.ShopActivity;
import com.petsay.activity.topic.TopicListActivity;
import com.petsay.activity.user.GiftBagActivity;
import com.petsay.activity.user.UserCenterActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.CheckVersionManager;
import com.petsay.application.CheckVersionManager.OnCheckVersionListtener;
import com.petsay.application.MessageManager;
import com.petsay.application.MessageManager.MessageCallBack;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgCallback;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.BottomCameraView;
import com.petsay.component.view.ExHintView;
import com.petsay.component.view.NewFunctionStateView;
import com.petsay.component.view.ReleaseTypeSelectView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.MemberNet;
import com.petsay.network.net.UserNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.sign.ActivityPartakeVo;
import com.petsay.vo.user.UserInfo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

@Deprecated
@SuppressLint("NewApi")
public class MainActivity_Deprecated extends BaseActivity implements IAddShowLocationViewService,OnCheckVersionListtener,MessageCallBack, NetCallbackInterface, ChatMsgCallback ,OnClickListener{


    public static final String[] TITLES = {"宠豆商城","一起选热门","会员礼包","互动吧","我的任务", "设置" };
//    public static final int[] SKINNAMEIDS={R.string.right_menu_shop,R.string.right_review_newsay_icon,R.string.right_menu_giftbag_icon,R.string.right_menu_setting};
    private   final int[] RightItemImgRes = { R.drawable.right_menu_shop,R.drawable.right_review_newsay_icon,R.drawable.right_menu_giftbag_icon ,R.drawable.right_menu_forum,R.drawable.right_menu_task, R.drawable.right_menu_setting };
    public DrawerLayout mDrawer_layout;// DrawerLayout容器
    private LinearLayout mMenu_layout_right;// 右边抽屉
    private LinearLayout mMenu_layout_left;//左抽屉
    //发布说说
    private ReleaseTypeSelectView mReleaseTypeSelectView;
    private NotificationTypeView mNotificationTypeView;
    private RelativeLayout rlayout_info;
    /**
     * 9主页，-1个人中心
     */
    public int curPosition = 9;
    private HomeFragment homeFragment;
//    private MessageFragment messageFragment;
//    private UserCenterNewFragment userCenterFragment;
    private ListView menu_listview_r;
    private ImageView imgHeader;
    private TextView tvPetName;
    private FragmentTransaction ft;
    private static MainActivity_Deprecated instance;
    private UserManager userManager;

    private PetVo activePetInfo;
    private Fragment mCurrFragment;
    private int mClickBackCount = 0;
    private SlidingListAdapter mSlidingListAdapter;
    private int unreadMsgCount=0;
    private int munReadSystemMsgCount = 0;
    private BottomCameraView mBottomView;
    private MemberNet mMemberNet;
    private UserNet mUserNet;
    private boolean menuState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mMemberNet=new MemberNet();
        mMemberNet.setTag(this);
        mMemberNet.setCallback(this);

        mUserNet=new UserNet();
        mUserNet.setTag(this);
        mUserNet.setCallback(this);

        regReceiver();
        initPush();
//		initData();

        userManager=UserManager.getSingleton();
        UserInfo info;
        try {
            Object object=DataFileCache.getSingleton().loadObject(Constants.UserFile);

            if (null!=object) {
                info=(UserInfo) object;
                if (info.isLogin()) {
                    userManager.setUserInfo(info);
                    ChatMsgManager.getInstance().auth();
                    mUserNet.petOne(userManager.getActivePetId(), userManager.getActivePetId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        PublicMethod.updateDecorationData(MainActivity.this);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FaceConversionUtil.getInstace().getFileText(getApplication());
//            }
//        }).start();


        initView();
        initSlidingView();
        CheckVersionManager.getSingleton().checkVersion(MainActivity_Deprecated.this,this);
        if(getIntent().getBooleanExtra("announcement", false)){
            PublicMethod.showAnnouncementDialog(MainActivity_Deprecated.this,getIntent().getStringExtra("content"));
        }
        ChatMsgManager.getInstance().registerCallback(this);
    }



    /**
     * 初始化侧边栏
     */
    private void initSlidingView() {
        menu_listview_r = (ListView) mMenu_layout_right.findViewById(R.id.menu_listView_r);
        
        mSlidingListAdapter=new SlidingListAdapter(this, TITLES, RightItemImgRes);
        menu_listview_r.setAdapter(mSlidingListAdapter);
        // 监听菜单
        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());
        imgHeader = (ImageView) findViewById(R.id.img_header);
        tvPetName=(TextView) findViewById(R.id.tv_name);
        rlayout_info=(RelativeLayout) findViewById(R.id.rlayout_info);
        rlayout_info.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (userManager.isLoginStatus()) {
                    curPosition=-1;
                    mSlidingListAdapter.notifyDataSetChanged();
//                    tvPetName.setTextColor(SkinManager.getInstance(MainActivity.this).getColor(getString(R.string.right_menu_text_color)));//getResources().getColor(R.color.list_name));
//                    userCenterFragment = new UserCenterNewFragment();
//                    switchFragment(userCenterFragment);
                   jumpActivity(UserCenterActivity.class);
                }else {
                    jumpActivity(UserLogin_Activity.class);
                }
            }
        });

    }

    @SuppressLint("NewApi")
    protected void initView() {
        mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenu_layout_right = (LinearLayout) findViewById(R.id.menu_layout_right);
        mMenu_layout_left=(LinearLayout) findViewById(R.id.menu_layout_left);
        mReleaseTypeSelectView=(ReleaseTypeSelectView) findViewById(R.id.release_sel);
        mNotificationTypeView=new NotificationTypeView(this);
        mMenu_layout_left.addView(mNotificationTypeView);
        mBottomView=(BottomCameraView) findViewById(R.id.main_bottom);
        mNotificationTypeView.setBottomView(mBottomView);
        mBottomView.setClickListener(this);
        mDrawer_layout.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerStateChanged(int arg0) {
                initLoginInfo();
            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {}

            @Override
            public void onDrawerOpened(View arg0) {}

            @Override
            public void onDrawerClosed(View arg0) {}
        });

        homeFragment = new HomeFragment();
        switchFragment(homeFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
       
      
        initLoginInfo();
        refreshMsgCount();
//        mBottomView.initActivity(MainActivity.this);
//        mBottomView.refreshSkin();
        onCheckPlayMode();
        checkShowHint();
        holderPowerManager();
    }
    
    private void checkShowHint(){
        final SharePreferenceCache cache = SharePreferenceCache.getSingleton(this);
        boolean isShow = cache.getSharedPreferences().getBoolean("mainactivity_showhint", true);
        if(isShow) {
           ExHintView ch =  (ExHintView) findViewById(R.id.hint_camera);
            ch.setCallback(new ExHintView.ExHintViewCallback() {
                @Override
                public void onHidenAnimationFinish(ExHintView view) {
                    ExHintView hmsg =  (ExHintView) findViewById(R.id.hint_msg);
                    hmsg.show(true);
                    cache.getSharedPreferences().edit()
                            .putBoolean("mainactivity_showhint",false)
                            .commit();
                }
            });
            ch.show(true);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (null!=homeFragment) {
            homeFragment.isShowSign(View.GONE, null);
  		}
//        ChatMsgManager.getInstance().unregisterCallback(this);
    }

    private void onCheckPlayMode(){
        int mode = SharePreferenceCache.getSingleton(this).getPlayMode();
        if(mode == -1)
            PublicMethod.showSetPlayModeDialog(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        if (null!=intent) {
            setIntent(intent);
            int value=intent.getIntExtra("fragment", 0);
            if (value==-1) {
//                if (null==userCenterFragment) {
//                    userCenterFragment = new UserCenterNewFragment();
//                }
                curPosition=-1;
                mSlidingListAdapter.notifyDataSetChanged();
//                tvPetName.setTextColor(SkinManager.getInstance(MainActivity.this).getColor(getString(R.string.right_menu_text_color)));//getResources().getColor(R.color.list_name));
//                switchFragment(userCenterFragment);
                jumpActivity(UserCenterActivity.class);
            }else if (value==1) {
//                if (null==messageFragment) {
//                    messageFragment = new MessageFragment();
//                }
                curPosition=1;
                mSlidingListAdapter.notifyDataSetChanged();
//                tvPetName.setTextColor(Color.WHITE);
//                switchFragment(messageFragment);
            }else if(homeFragment != null){
                int index = getIntent().getIntExtra(HomeFragment.PAGEINDEX,homeFragment.getPageIndex());
                boolean flag = getIntent().getBooleanExtra("flag", false);
                homeFragment.setPageIndex(index);
                if(flag)
                    switchFragment(homeFragment);

            }
        }

        super.onNewIntent(intent);
    }

    public  void changeSlidingState(int gravity) {
    	if (gravity==Gravity.RIGHT) {
    		mDrawer_layout.closeDrawer(Gravity.LEFT);
		}else {
			mDrawer_layout.closeDrawer(Gravity.RIGHT);
		}
    	
        if (mDrawer_layout.isDrawerOpen(gravity)) {
            mDrawer_layout.closeDrawer(gravity);
        } else {
            mDrawer_layout.openDrawer(gravity);
        }
    }
   

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if (curPosition!=9) {
                curPosition=9;
                mSlidingListAdapter.notifyDataSetChanged();
//                tvPetName.setTextColor(Color.WHITE);
                if(homeFragment == null)
                    homeFragment = new HomeFragment();
                switchFragment(homeFragment);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mClickBackCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    public void onExit(){
        mClickBackCount ++;
        if(mClickBackCount == 1){
            PublicMethod.showToast(MainActivity_Deprecated.this, "再按一次退出");
        }else {
            this.finish();
        }
    }

    /**
     * 切换Fragment
     * 采用hide和shou的方式来切换，这样能减少Framgent的实例化次数
     * @param fragment
     */
    private void switchFragment(Fragment fragment){
        if(fragment == null || fragment == mCurrFragment)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!fragment.isAdded()){
            ft.add(R.id.fragment_layout, fragment);
        }
        if(mCurrFragment != null){
            ft.hide(mCurrFragment);
            ft.show(fragment);
        }else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        mCurrFragment = fragment;
        mDrawer_layout.closeDrawer(mMenu_layout_right);
        mDrawer_layout.closeDrawer(mMenu_layout_left);
    }

    @Override
    public void onReceiveChatMsg(ChatMsgEntity entity) {
        setUnreadMsgCount(munReadSystemMsgCount);
    }

    /**
     * 右侧列表点击事件
     */
    private class DrawerItemClickListenerRight implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            if (curPosition == position) {
                mDrawer_layout.closeDrawer(mMenu_layout_right);// 关闭mMenu_layout
                mDrawer_layout.closeDrawer(mMenu_layout_left);
//                if(curPosition == 1)    //如果点击了消息Item，则更新未读消息状态
//                    updateUnReadMsgView();
            } else {
				switch (position) {
				case 0:
					if (checkLogin(view)) {
						jumpActivity(ShopActivity.class);
					}
					mDrawer_layout.closeDrawer(mMenu_layout_right);
					break;
				case 1: // 一起选热门
					if (UserManager.getSingleton().isLoginStatus()) {
						jumpActivity(ReviewHotSayActivity.class);
						overridePendingTransition(R.anim.fade, R.anim.hold);
					} else {
						jumpActivity(UserLogin_Activity.class);
					}
					mDrawer_layout.closeDrawer(mMenu_layout_right);
					break;
				case 2: // 礼包
					if (checkLogin(view)) {
						jumpActivity(GiftBagActivity.class);
					}
					mDrawer_layout.closeDrawer(mMenu_layout_right);
					break;
				case 3:// 话题
					setNewFunState(view);
					jumpActivity(TopicListActivity.class);
					mDrawer_layout.closeDrawer(mMenu_layout_right);
					break;
				case 4:
					if (checkLogin(view)) {
						jumpActivity(MyTaskActivity.class);
						mDrawer_layout.closeDrawer(mMenu_layout_right);
					}
					break;
				
				case 5:// 设置
					jumpActivity(SettingActivity.class);
					mDrawer_layout.closeDrawer(mMenu_layout_right);
					break;
				}
//                tvPetName.setTextColor(Color.WHITE);
            }
        }
    }

//    /**更新未读消息状态(次方法已废弃)*/
//    @Deprecated
//    public void updateUnReadMsgView(){
//        mSlidingListAdapter.notifyDataSetChanged();
//        if (mSlidingListAdapter.hasUnreadMsg) {
//            mSlidingListAdapter.hasUnreadMsg=false;
//            mBottomView.hasUnreadMsg(mSlidingListAdapter.hasUnreadMsg);
//            SharePreferenceCache.getSingleton(MainActivity.this).setReadMsgCount(unreadMsgCount);
//        }
//    }

    private boolean checkLogin(View view){
        if(!UserManager.getSingleton().isLoginStatus()){
            jumpActivity(UserLogin_Activity.class);
            overridePendingTransition(R.anim.fade, R.anim.hold);
            return false;
        }
        return true;
    }
    
    /**
     * 设置新功能已使用状态
     * @param view
     */
    private void setNewFunState(View view){
    	NewFunctionStateView.setUsedSate(view.findViewById(R.id.img_stateview));
    }

    private void jumpActivity(Class<?> clz){
        Intent intent=new Intent(getApplicationContext(), clz);
        startActivity(intent);
    }

    private void initLoginInfo(){
    	
        activePetInfo=userManager.getActivePetInfo();
        if (userManager.isLoginStatus()) {
            if (null==activePetInfo) {
                setUnreadMsgCount(0);
                tvPetName.setText("未登录");
                imgHeader.setImageResource(R.drawable.placeholderhead);
            }else {
//				setUnreadMsgCount(SharePreferenceCache.getSingleton(MainActivity.this).getReadMsgCount());
                refreshMsgCount();
                tvPetName.setText(activePetInfo.getNickName());
//				ImageLoader.getInstance().displayImage(activePetInfo.getHeadPortrait(), imgHeader, Constants.headerImgOptions);
//				PicassoUtile.loadHeadImg(MainActivity.this, activePetInfo.getHeadPortrait(), imgHeader);
                ImageLoaderHelp.displayHeaderImage(activePetInfo.getHeadPortrait(), imgHeader);
                mMemberNet.activityPartake(activePetInfo.getId());
                
            }
        }else {
            setUnreadMsgCount(0);
            tvPetName.setText("未登录");
            imgHeader.setImageResource(R.drawable.placeholderhead);
        }



    }

    @Override
    protected void onDestroy() {
        UserManager.isUserChanged=false;
        release();
        SharePreferenceCache.getSingleton(getApplicationContext()).setIsRunningApp(false);
        if(msgReceiver!=null)
            this.unregisterReceiver(msgReceiver);
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




    @Override
    public View getParentView() {
        // TODO Auto-generated method stub
        return mDrawer_layout;
    }
    @Override
    public Activity getActivity() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public void onFinish(boolean hasNew) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUnreadMsgCount(int count) {
        munReadSystemMsgCount = count;
        unreadMsgCount=munReadSystemMsgCount + ChatDataBaseManager.getInstance().getNewestMsgTotalCount();
        if (unreadMsgCount>SharePreferenceCache.getSingleton(this).getReadMsgCount()) {
            //TODO 有未读消息
            if (null!=mSlidingListAdapter) {
                mSlidingListAdapter.hasUnreadMsg=true;
                mSlidingListAdapter.notifyDataSetChanged();
                mBottomView.hasUnreadMsg(mSlidingListAdapter.hasUnreadMsg);
            }
        }else {
            if (null!=mSlidingListAdapter) {
                mSlidingListAdapter.hasUnreadMsg=false;
                mSlidingListAdapter.notifyDataSetChanged();
                mBottomView.hasUnreadMsg(mSlidingListAdapter.hasUnreadMsg);
            }
        }
    }

    public void refreshMsgCount(){
        MessageManager.getSingleton().refreshMsgCount(this);
        mNotificationTypeView.refreshCount();
    }




    /**
     * 第三方应用Master Secret，修改为正确的值
     */
    private static final String MASTERSECRET = "TccZ9ittLxA8Iu4150dOZ";
    /**
     * SDK服务是否启动
     */
//    private boolean tIsRunning = true;
//    private Context mContext = null;
//    private SimpleDateFormat formatter = null;
//    private Date curDate = null;

    // SDK参数，会自动从Manifest文件中读取，第三方无需修改下列变量，请修改AndroidManifest.xml文件中相应的meta-data信息。
    // 修改方式参见个推SDK文档
    private String appkey = "";
    private String appsecret = "";
    private String appid = "";
    private void initPush(){
        // 从AndroidManifest.xml的meta-data中读取SDK配置信息
        String packageName = getApplicationContext().getPackageName();
        ApplicationInfo appInfo;
        try {
            appInfo = getPackageManager().getApplicationInfo(packageName,PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID");
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
                appkey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData.get("PUSH_APPKEY").toString() : null;
            }

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        Log.d("GetuiSdkDemo", "initializing sdk...");
        PushManager.getInstance().initialize(this.getApplicationContext());
        PushManager.getInstance().turnOnPush(MainActivity_Deprecated.this);
    }

    private MsgReceiver msgReceiver;
    private void regReceiver(){
		 /*动态方式注册广播接收者*/
        msgReceiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.petsay.msg");
        this.registerReceiver(msgReceiver, filter);
    }

    public  class MsgReceiver extends BroadcastReceiver//作为内部类的广播接收者
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("com.petsay.msg"))
            {
                Log.i("MainActivity","成功收到广播");
                refreshMsgCount();
//                if (null!=messageFragment) {
//                    messageFragment.refreshNewMsg();
//                }
            }
        }
    }

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_ACTIVITYPARTAKE:
			List<ActivityPartakeVo> activityPartakeVos = null;
			ActivityPartakeVo activityPartakeVo;
			try {
				activityPartakeVos=JsonUtils.getList(bean.getValue(), ActivityPartakeVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < activityPartakeVos.size(); i++) {
				if (activityPartakeVos.get(i).getCode().equals("signIn")) {
					activityPartakeVo=activityPartakeVos.get(i);
					if (activityPartakeVo.getState()==1) {
						homeFragment.isShowSign(View.VISIBLE,activityPartakeVo);
					}else {
						homeFragment.isShowSign(View.GONE,activityPartakeVo);
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



	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
//			
//				PublicMethod.outMM("热门：");
//				Intent intent=new Intent(getApplicationContext(), CameraActivity.class);
//				startActivity(intent);
////				Intent intent=new Intent(mContext, RecommendPetsActivity.class);
////				mContext.startActivity(intent);
//			
			break;

		default:
			break;
		}
		
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
    
}