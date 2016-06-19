package com.petsay.activity.global.guide;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.guide.view.GuideView;
import com.petsay.activity.main.MainActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.FlipBookView.OnPageTurnListener;

public class GuideActivity extends BaseActivity {
	private ViewPager mPager;// 页卡内容
	private MyPagerAdapter myPagerAdapter;
	private List<View> listViews; // Tab页面列表
	private LinearLayout linearLayout1, linearLayout2, linearLayout3,linearLayout4,linearLayout5;
	private GuideView mGuideView1,mGuideView2,mGuideView3,mGuideView4,mGuideView5;
	boolean isStart;
	private int curPosition=0;
	private float downX,upX;
//	private GuideView4 mGuideView5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.guide);
		mPager=(ViewPager) findViewById(R.id.vPager);
		InitViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		isStart=getIntent().getBooleanExtra("isStart", false);
		listViews = new ArrayList<View>();
		linearLayout1 = new LinearLayout(getApplicationContext());
		linearLayout2 = new LinearLayout(getApplicationContext());
		linearLayout3 = new LinearLayout(getApplicationContext());
		linearLayout4 = new LinearLayout(getApplicationContext());
		linearLayout5 = new LinearLayout(getApplicationContext());


		listViews.add(linearLayout1);
		 mGuideView1=new GuideView(GuideActivity.this,1,isStart);
		linearLayout1.addView(mGuideView1);

		listViews.add(linearLayout2);
		mGuideView2=new GuideView(GuideActivity.this,2,isStart);
		linearLayout2.addView(mGuideView2);

		listViews.add(linearLayout3);
		mGuideView3=new GuideView(GuideActivity.this,3,isStart);
		linearLayout3.addView(mGuideView3);
		
		listViews.add(linearLayout4);
		mGuideView4=new GuideView(GuideActivity.this,4,isStart);
		linearLayout4.addView(mGuideView4);
		
		listViews.add(linearLayout5);
		mGuideView5=new GuideView(GuideActivity.this,5,isStart);
		linearLayout5.addView(mGuideView5);
		
		myPagerAdapter = new MyPagerAdapter(listViews);
		mPager.setAdapter(myPagerAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
//		mGuideView1.stopAnim();
		
		mPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX=event.getX();
					break;
				case MotionEvent.ACTION_UP:
					upX=event.getX();
					if (downX-upX>=100&&curPosition==4) {
						SharePreferenceCache.getSingleton(GuideActivity.this).setShowMainpageGuide(false);
						Intent intent = new Intent(GuideActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
					break;
				}
				return false;
			}
		});
			
	}
	
	

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
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
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			curPosition=arg0;
//			stop();
//			if (arg0==4&&isStart) {
//				mGuideView5.playAnim();
//			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
		
	}
	
	@Override
	protected void onPause() {
//		stop();
		super.onPause();
	}
	
	
//	private void stop(){
//		if (null!=mGuideView1) {
//			mGuideView1.stopAnim();
//		}
//		if (null!=mGuideView2) {
//			mGuideView2.stopAnim();
//		}
//		if (null!=mGuideView3) {
//			mGuideView3.stopAnim();
//		}
//	}

}
