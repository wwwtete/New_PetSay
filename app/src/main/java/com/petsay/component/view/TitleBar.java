package com.petsay.component.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;

/**
 * @author wangw
 * 自定义TitleBar
 */
public class TitleBar extends RelativeLayout implements OnClickListener {

	
	private View mMainView;
	private LinearLayout mLayout_Back;
	private ImageView mImg_Back;
	private TextView mTxt_Back;
	private OnClickBackListener mBackListener;
    private OnClickTitleListener mTitleListener;
	private TextView mTitle;
	private LinearLayout mRight_layout;
//    private Spinner mSpView;
	private boolean mFinishEnable = false;



	

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}
	
	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	public TitleBar(Context context){
		super(context);
		initViews();
	}

	private void initViews() {
		mMainView = inflate(getContext(), R.layout.titlebar_layout, this);
		mTitle = (TextView) mMainView.findViewById(R.id.txt_title);
		mRight_layout = (LinearLayout) mMainView.findViewById(R.id.layout_title_right);
		mImg_Back=(ImageView) mMainView.findViewById(R.id.img_back);
		mLayout_Back = (LinearLayout) mMainView.findViewById(R.id.layout_title_back);
		mLayout_Back.setOnClickListener(this);
        mTitle.setOnClickListener(this);
//        mSpView = (Spinner) mMainView.findViewById(R.id.sp_title);
	}
	public void setBgColor(int color){
		this.setBackgroundColor(color);
	}
	public void setLeftBtnRes(int res){
		mImg_Back.setImageResource(res);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_title_back:
			if(this.mBackListener != null)
				mBackListener.OnClickBackListener();
			if(getContext() instanceof Activity && mFinishEnable){
				((Activity) getContext()).finish();
			}
			break;
            case R.id.txt_title:
                if(mTitleListener != null)
                    mTitleListener.onClickTitleListener((TextView) v);
                break;
		}
	}
	
//	public void setBackVisibility(boolean flag){
//		if(flag){
//			mLayout_Back.setVisibility(View.VISIBLE);
//		}else{
//			mLayout_Back.setVisibility(View.GONE);
//		}
//	}
	
	/**
	 * 向TitleBar右侧添加组件
	 * @param child
	 */
	public void addRightView(View child){
		mRight_layout.addView(child);
	}
	
	/**
	 * 向TitleBar右侧添加组件
	 * @param child
	 */
	public void addRightView(View child,android.widget.LinearLayout.LayoutParams params){
		mRight_layout.addView(child,params);
	}
		
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitleText(String title) {
		mTitle.setText(title);
	}

	/**
	 * 设置标题
	 * @param resId
	 */
	public void setTitleText(int resId) {
		mTitle.setText(resId);
	}
	
	/***
	 * 设置点击返回按钮回调事件
	 * @param mBackListener
	 */
	public void setOnClickBackListener(OnClickBackListener mBackListener) {
		this.mBackListener = mBackListener;
	}

    /**
     * 设置点击标题事件
     * @param listen
     */
    public void setOnClickTitleListener(OnClickTitleListener listen) {
        this.mTitleListener = listen;
    }
	
	public boolean isFinishEnable() {
		return mFinishEnable;
	}

	/**
	 * 使用后退finsh当前Activity
	 * @param mFinishEnable
	 */
	public void setFinishEnable(boolean mFinishEnable) {
		this.mFinishEnable = mFinishEnable;
	}
	
	public void setBackVisibility(int visibility){
		mLayout_Back.setVisibility(visibility);
	}
	
	public interface OnClickBackListener{
		public void OnClickBackListener();
	}

    public interface OnClickTitleListener{
        public void onClickTitleListener(TextView titleView);
    }


//    public Spinner getSpinner(){
//        return  mSpView;
//    }
//
//    public void showSpinner(BaseAdapter adapter){
//        mSpView.setAdapter(adapter);
//        mSpView.setVisibility(VISIBLE);
//        mTitle.setVisibility(GONE);
//
//    }

}
