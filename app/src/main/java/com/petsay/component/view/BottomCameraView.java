package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;

/**
 * 底部相机 与侧滑按钮
 * @author G
 *
 */
public class BottomCameraView extends RelativeLayout implements OnClickListener {

	private Context mContext;
	private ImageView imgCamera, imgRight;
	private ImageView imgLeft;
//	private View mBg;
//	private SkinManager mSkinManager;
//	private boolean isShowMenu=false;
	private int pageIndex=0;
	private OnBottomTabChangeListener mTabChangeListener;

	public BottomCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(context, R.layout.bottom_layout, this);
		initView();
		setListener();
	}

	public BottomCameraView(Context context) {
		super(context);
		mContext = context;
		inflate(context, R.layout.bottom_layout, this);
		initView();
		setListener();
		
	}
	
	public void setClickListener(OnClickListener listener){
		imgCamera.setOnClickListener(listener);
	}
	public void setOnBottomTabChangeListener(OnBottomTabChangeListener bottomTabChangeListener){
		mTabChangeListener=bottomTabChangeListener;
	}

	private void initView() {
		imgRight = (ImageView) findViewById(R.id.img_right_icon);
		imgLeft=(ImageView) findViewById(R.id.img_left_icon);
		imgCamera = (ImageView) findViewById(R.id.img_camera);
		imgLeft.setImageResource(R.drawable.main_bottom_petalk_slected);
		imgRight.setImageResource(R.drawable.main_bottom_custom_defult);
//		imgMsgIcon=(ImageView) findViewById(R.id.img_msg_icon);
//		imgMsgIcon.setVisibility(View.INVISIBLE);
//		imgSliding.setOnTouchListener(this);
//		imgMsgSliding.setOnTouchListener(this);
//		mBg = findViewById(R.id.layout_bg);
//		mSkinManager = SkinManager.getInstance(mContext);
	}
	
//	public void initActivity(MainActivity activity){
//		mActivity=activity;
//	}
	
	private void setListener() {
		imgRight.setOnClickListener(this);
//		imgCamera.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
	}
	
	public void refreshSkin(){
//		String normalSatae = mContext.getString(R.string.camera_bottom_btn_normal);
//		String pullSatae = mContext.getString(R.string.camera_bottom_btn_pull);
////		SkinHelp.setImageDrawable(imgMsgSliding, mContext, mContext.getString(R.string.bottom_msg));
////		imgMsgSliding.setBackgroundResource(R.drawable.bottom_msg);
//		SkinHelp.setBackgroundColor(mBg,mSkinManager.getColor(mContext.getString(R.string.camera_bottom_bg_color)));
//		SkinHelp.setImageDrawable(imgCamera,mContext, normalSatae);
//		SkinHelp.setImageViewSelector(imgCamera,mContext, normalSatae, pullSatae);
	}

	
   public void returnMainPage(){
		pageIndex=0;
	   imgLeft.setImageResource(R.drawable.main_bottom_petalk_slected);
		imgRight.setImageResource(R.drawable.main_bottom_custom_defult);
   }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.img_camera:
//			
//			//拍照时进行登录判断
//			if (UserManager.getSingleton().isLoginStatus()) {
//				PublicMethod.outMM("热门：");
//				Intent intent=new Intent(mContext, CameraActivity.class);
//				mContext.startActivity(intent);
////				Intent intent=new Intent(mContext, RecommendPetsActivity.class);
////				mContext.startActivity(intent);
//			}else {
//				Intent intent=new Intent(mContext, UserLogin_Activity.class);
//				mContext.startActivity(intent);
//			}
//			break;
		case R.id.img_right_icon:
//				//mActivity.changeSlidingState(Gravity.RIGHT);
			
//			if (null==mTabChangeListener) {
//				return;
//			}
//			if (pageIndex!=1) {
//				imgRight.setImageResource(R.drawable.main_bottom_custom_slected);
//				imgLeft.setImageResource(R.drawable.main_bottom_petalk_default);
//				pageIndex=1;
//				mTabChangeListener.onBottomTabChange(pageIndex);
//			}

			setTabChnageIndex(1);

//			if (UserManager.getSingleton().isLoginStatus()) {
//				Intent intent=new Intent(mContext,CustomPostCardActivity.class) ;
//				mContext.startActivity(intent);
//			}
//			else {
//				Intent intent=new Intent(mContext,UserLogin_Activity.class) ;
//				mContext.startActivity(intent);
//			}
			
			
			break;
		case R.id.img_left_icon:
//				if (UserManager.getSingleton().isLoginStatus()) {
//					mActivity.changeSlidingState(Gravity.LEFT);
//				}else {
//					Intent intent=new Intent(mContext, UserLogin_Activity.class);
//					mContext.startActivity(intent);
//				}
//			if (null==mTabChangeListener) {
//				return;
//			}
//			if (pageIndex!=0) {
//				pageIndex=0;
//				imgLeft.setImageResource(R.drawable.main_bottom_petalk_slected);
//				imgRight.setImageResource(R.drawable.main_bottom_custom_defult);
//				mTabChangeListener.onBottomTabChange(pageIndex);
//			}
			setTabChnageIndex(0);
			break;
		}
	}

	public void setTabChnageIndex(int index){
		if (null==mTabChangeListener) {
			return;
		}
		if (pageIndex!=index) {
			pageIndex=index;
			if (pageIndex ==0) {
				imgLeft.setImageResource(R.drawable.main_bottom_petalk_slected);
				imgRight.setImageResource(R.drawable.main_bottom_custom_defult);
			}else if (pageIndex ==1) {
				imgRight.setImageResource(R.drawable.main_bottom_custom_slected);
				imgLeft.setImageResource(R.drawable.main_bottom_petalk_default);
			}
			mTabChangeListener.onBottomTabChange(pageIndex);
		}
	}

    public void hasUnreadMsg(boolean hasUnreadMsg){
		if (hasUnreadMsg) {
//			imgMsgIcon.setVisibility(View.VISIBLE);
		}else {
//			imgMsgIcon.setVisibility(View.INVISIBLE);
		}
	}

	public interface OnBottomTabChangeListener{
		/**
		 * 0说说， 1定制
		 * @param tabIndex
		 */
		void onBottomTabChange(int tabIndex);
	}

}
