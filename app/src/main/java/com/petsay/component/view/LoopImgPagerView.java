package com.petsay.component.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;

/**
 * 轮播图组件
 * 
 * @author G
 *
 */
public class LoopImgPagerView extends RelativeLayout {

	private Context mContext;

	private LinearLayout mPointLayout;
	private WrapContentHeightViewPager mVPager;
	private List<ImageView> mImageViews;
	private View[] views;
	private ImageView imgClose;

	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private int PageIndex = 0;
	private LoopImgPagerAdapter myPagerAdapter;
	private OnLoopImgItemClickListener onLoopImgItemClickListener;
	private String[] picUrls = {
			"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=1e5f0aabe4fe9925cb593a1052956aed/1b4c510fd9f9d72a442f0273d02a2834349bbb10.jpg",
			"https://ss3.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=af5b9445b451f819f170500abc897edf/c83d70cf3bc79f3d6e95f65ebea1cd11728b2963.jpg",
			"https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=bb332e7aaf18972ba36f538a80f04fbb/8d5494eef01f3a2946d0c7a09d25bc315c607c47.jpg",
			"https://ss3.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=f3abc04f94ef76c6d087a86bfb2bc9c8/9922720e0cf3d7ca6dbd2d18f61fbe096b63a910.jpg" };
	public LoopImgPagerView(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	
	public LoopImgPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(mContext, R.layout.component_loop_img, this);
		mVPager=(WrapContentHeightViewPager) findViewById(R.id.vPager);
		mPointLayout=(LinearLayout) findViewById(R.id.llayout);
		imgClose=(ImageView) findViewById(R.id.img_close);
//		initPager();
	}
	public void setImgClose(OnClickListener listener){
		imgClose.setVisibility(View.VISIBLE);
		imgClose.setOnClickListener(listener);
	}
	public void setOnLoopImgItemClickListener(OnLoopImgItemClickListener onLoopImgItemClickListener){
		this.onLoopImgItemClickListener=onLoopImgItemClickListener;
	}

	
	
//	private void initPager() {
//		mImageViews = new ArrayList<ImageView>();
//
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(PublicMethod.getPxInt(10,mContext), PublicMethod.getPxInt(10, mContext));
//		pointParams.setMargins(PublicMethod.getPxInt(5, mContext), 0,PublicMethod.getPxInt(5, mContext), 0);
//		Drawable focuse =getResources().getDrawable(R.drawable.page_indicator_focused);
//		Drawable unFocuse =getResources().getDrawable(R.drawable.page_indicator_unfocused);
//		views = new View[picUrls.length];
//		for (int i = 0; i < picUrls.length; i++) {
//			ImageView imageView = new ImageView(mContext);
//			imageView.setAdjustViewBounds(true);
//			imageView.setLayoutParams(layoutParams);
//			ImageLoaderHelp.displayContentImage(picUrls[i],imageView);
//			mImageViews.add(imageView);
//			View view = new View(mContext);
//			view.setLayoutParams(pointParams);
//			if (i == 0) {
//				view.setBackgroundDrawable(focuse);
//			} else {
//				view.setBackgroundDrawable(unFocuse);
//			}
//			mPointLayout.addView(view);
//			views[i] = view;
//		}
//		myPagerAdapter = new LoopImgPagerAdapter(mImageViews);
//		mVPager.setAdapter(myPagerAdapter);
//		mVPager.setCurrentItem(0);
//		mVPager.setOnPageChangeListener(new MyOnPageChangeListener(unFocuse,focuse));
//	}
	
	public void setImgUrls(String[] picUrls) {
//		picUrls=this.picUrls;
		mImageViews = new ArrayList<ImageView>();
		mPointLayout.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(PublicMethod.getPxInt(5,mContext), PublicMethod.getPxInt(5, mContext));
		pointParams.setMargins(PublicMethod.getPxInt(5, mContext), 0,PublicMethod.getPxInt(5, mContext), 0);
		Drawable focuse =getResources().getDrawable(R.drawable.page_indicator_focused);
		Drawable unFocuse =getResources().getDrawable(R.drawable.page_indicator_unfocused);
		int len=picUrls.length;
		 if (len<2) {
         	mPointLayout.setVisibility(View.GONE);
			}
		views = new View[len];
		for (int i = 0; i < len; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setAdjustViewBounds(true);
			imageView.setLayoutParams(layoutParams);
			ImageLoaderHelp.displayContentImage(picUrls[i],imageView);
			mImageViews.add(imageView);
			imageView.setOnClickListener(new LoopImgClickListener(i));
			View view = new View(mContext);
			view.setLayoutParams(pointParams);
			if (i == 0) {
				view.setBackgroundDrawable(focuse);
			} else {
				view.setBackgroundDrawable(unFocuse);
			}
			mPointLayout.addView(view);
			views[i] = view;
		}
		myPagerAdapter = new LoopImgPagerAdapter(mImageViews);
		mVPager.setAdapter(myPagerAdapter);
		mVPager.setCurrentItem(0);
		mVPager.setOnPageChangeListener(new MyOnPageChangeListener(unFocuse,focuse));
	}

	/**
	 * ViewPager适配器
	 */
	public class LoopImgPagerAdapter extends PagerAdapter {
		public List<ImageView> mListViews;

		public LoopImgPagerAdapter(List<ImageView> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class TabOnClickListener implements OnClickListener {
		private int index = 0;

		public TabOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mVPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		private Drawable mUnfocused;
		private Drawable mfocuse;

		private MyOnPageChangeListener(Drawable unfocused, Drawable foucese) {
			if (unfocused == null)
				mUnfocused = getResources().getDrawable(R.drawable.page_indicator_unfocused);
			else
				this.mUnfocused = unfocused;
			
			if (foucese == null)
				mfocuse = getResources().getDrawable(R.drawable.page_indicator_focused);
			else
				this.mfocuse = foucese;
		}

		@Override
		public void onPageSelected(int arg0) {
			PageIndex = arg0;
			PublicMethod.changeBtnBgImg(views, PageIndex, mfocuse, mUnfocused);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	private class LoopImgClickListener implements OnClickListener{

		private  int _position;
		public  LoopImgClickListener(int position) {
			_position=position;
		}
		@Override
		public void onClick(View v) {
			if (null!=onLoopImgItemClickListener) {
				onLoopImgItemClickListener.OnLoopImgItemClick(v, _position);
			}
			
		}
		
	}
	
	public interface OnLoopImgItemClickListener{
		void OnLoopImgItemClick(View v,int position);
	}
	
//	public interface OnLoopImgItemClickListener extends OnClickListener{
//		@Override
//		public void onClick(View v);
//		
//	}

}
