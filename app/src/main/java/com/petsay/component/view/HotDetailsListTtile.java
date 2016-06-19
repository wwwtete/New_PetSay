package com.petsay.component.view;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author wangw
 *	热门详情切换表头 评论和恢复 | 踩
 */
public class HotDetailsListTtile extends LinearLayout implements OnClickListener {

	public View mView;
	private TextView mTv_Review;
	private TextView mTV_Tread;
	private ImageView mUnderline;
	private int bmpW;
	private int offset;
	private int oneViewMove;
	private TitleChangeListener mListener;
	private Context mContext;

	public HotDetailsListTtile(Context context){
		super(context);
		mContext=context;
		initViews();
	}

	public HotDetailsListTtile(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initViews();
	}

	public void setOnTitleChangeListener(TitleChangeListener listener){
		this.mListener = listener;
	}
	

	private void initViews() {
		mView = inflate(getContext(), R.layout.hotdetails_listtitle, null);
		addView(mView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		mTv_Review = (TextView) mView.findViewById(R.id.tv_review);
		mTv_Review.setOnClickListener(this);
		mTV_Tread = (TextView) mView.findViewById(R.id.tv_tread);
		mTV_Tread.setOnClickListener(this);
		mUnderline = (ImageView) mView.findViewById(R.id.underline);
		mUnderline.setTag(0);
		mTv_Review.setTextColor(getResources().getColor(R.color.black));
		initUnderLine();
		setBackgroundColor(mContext.getResources().getColor(R.color.hot_details_bg));
	}

	private void initUnderLine() {
		int screenW = PublicMethod.getDisplayWidth(getContext());
		int space = PublicMethod.getDiptopx(getContext(), 40);
		bmpW = (screenW/2)-space;// 获取图片宽度
		android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mUnderline.getLayoutParams();
		params.width = bmpW;
		params.leftMargin = space/2;
		params.rightMargin = space/2;
		mUnderline.setLayoutParams(params);
		offset = space/2;//(screenW / 2 - bmpW) / 2;// 计算偏移量
		oneViewMove = screenW / 2;// 页卡1 -> 页卡2 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		mUnderline.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	/**
	 * 设置转发和评论数
	 * @param relayCount
	 * @param commentCount
	 */
	public void setReviewCount(int relayCount,int commentCount){
		mTv_Review.setText(relayCount+"转发和"+commentCount+"评论");
	}
	
	/**
	 * 设置踩
	 * @param stepCount
	 */
	public void setTreadCount(int stepCount){
		mTV_Tread.setText(stepCount+"踩");
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) mUnderline.getTag();
		switch (v.getId()) {
		case R.id.tv_review:
			if(tag == 1){
				setUnderLineAnimation(false,500);
				if(mListener != null){
					mListener.onTitleChangeCallback(0);
				}
			}
			break;

		case R.id.tv_tread:
			if(tag == 0){
				setUnderLineAnimation(true,500);
				if(mListener != null){
					mListener.onTitleChangeCallback(1);
				}
			}
			break;
		}
	}
	
	public void setUnderLinePosition(int position){
		if(((Integer)mUnderline.getTag()) == position)
			return;
		if(position == 0){
			setUnderLineAnimation(false,0);
		}else {
			setUnderLineAnimation(true, 0);
		}
	}

	private void setUnderLineAnimation(boolean isRight,int time){
		Animation animation;
		if(isRight){
			animation = new TranslateAnimation(0, oneViewMove, 0,0);
			mUnderline.setTag(1);
			mTv_Review.setTextColor(getResources().getColor(R.color.hot_titlebar));
			mTV_Tread.setTextColor(getResources().getColor(R.color.black));
		}else {
			animation = new TranslateAnimation(oneViewMove, 0, 0, 0);
			mUnderline.setTag(0);
			mTv_Review.setTextColor(getResources().getColor(R.color.black));
			mTV_Tread.setTextColor(getResources().getColor(R.color.hot_titlebar));
		}
		animation.setFillAfter(true);
		animation.setDuration(time);
		mUnderline.startAnimation(animation);
	}



	/**
	 * Title点击回调事件
	 * @author wangw
	 *
	 */
	public interface TitleChangeListener{
		public void onTitleChangeCallback(int position);
	}





}
