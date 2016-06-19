package com.petsay.activity.homeview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseFragment;
import com.petsay.activity.global.SearchActivity;
import com.petsay.activity.main.SquareView;
import com.petsay.activity.user.signin.SigninActivity;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.MyViewPager;
import com.petsay.network.net.PublishPetSayNet;
import com.petsay.application.UserManager;
import com.petsay.vo.sign.ActivityPartakeVo;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class HomeFragment extends BaseFragment {

	public static final String PAGEINDEX = "PageIndex";

	private MyViewPager mPager;// 页卡内容
	private ImageView mImgSign;
	private ActivityPartakeVo mActivityPartakeVo;
	private List<View> listViews; // Tab页面列表
	private RelativeLayout t1, t2, t3;// 页卡头标
	private RelativeLayout layoutSearch;
	private ImageView view1,view2,view3;
	private ImageView imgTitle1,imgTitle2,imgTitle3;
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private MyPagerAdapter myPagerAdapter;
	private int PageIndex=0;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3;
	private ImageView[] views=new ImageView[3];
	private ImageView[] imgTitles=new ImageView[3];
	private int[] defaultResId ={R.drawable.main_hot_default,R.drawable.main_square_default,R.drawable.main_focus_default};
	private int[] pressResId ={R.drawable.main_hot_press,R.drawable.main_square_press,R.drawable.main_focus_press};
	private HomeFocusView focusView;
	private SquareView squareView;
	public  HotView hotView;
	private boolean mInited;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mInited = false;
		mView = inflater.inflate(R.layout.home, null);
		mView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		return mView;
	}
	
	
	protected void initView(View view) {
		super.initView(view);
		layoutSearch=(RelativeLayout) view.findViewById(R.id.layout_search);
		mPager = (MyViewPager)view. findViewById(R.id.vPager);
		mImgSign=(ImageView) view.findViewById(R.id.img_sign);
		t1 = (RelativeLayout)view. findViewById(R.id.text1);
		t2 = (RelativeLayout)view. findViewById(R.id.text2);
		t3 = (RelativeLayout)view. findViewById(R.id.text3);
		view1=(ImageView) view.findViewById(R.id.view1);
		view2=(ImageView) view.findViewById(R.id.view2);
		view3=(ImageView) view.findViewById(R.id.view3);
		views[0]=view1;
		views[1]=view2;
		views[2]=view3;
		
		imgTitle1 = (ImageView) view.findViewById(R.id.img_title1);
		imgTitle2 = (ImageView) view.findViewById(R.id.img_title2);
		imgTitle3 = (ImageView) view.findViewById(R.id.img_title3);
		imgTitles[0]=imgTitle1;
		imgTitles[1]=imgTitle2;
		imgTitles[2]=imgTitle3;
		mImgSign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SigninActivity.class);
				intent.putExtra("activitypartakevo", mActivityPartakeVo);
				startActivity(intent);				
			}
		});
		layoutSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchActivity.class);
				startActivity(intent);			
				
			}
		});
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
	}
	
	public void isShowSign(int result,ActivityPartakeVo activityPartakeVo){
		if (null!=mImgSign&&null!=activityPartakeVo) {
			mImgSign.setVisibility(result);
			mActivityPartakeVo=activityPartakeVo;
		}else {
			mImgSign.setVisibility(View.GONE);
		}
			
		
		
	}

	public void setPageIndex(int index){
		if(PageIndex >= 0)
			this.PageIndex = index;
	}

	public int getPageIndex(){
		return PageIndex;
	}

	@Override
	public void onResume() {
		if(!mInited)
			initFragment();
		onChangePager();
		super.onResume();
	}


	private void initFragment(){
		initView(mView);
		InitViewPager();
		mPager.setCurrentItem(PageIndex);
		changeSelectStatus(views, PageIndex);
//		PublicMethod.changeBtnBgColor(views, PageIndex,getActivity().getResources().getColor(R.color.home_0_tab_selected) , getActivity().getResources().getColor(R.color.transparent));
		mInited = true;
	}


	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
        MainActivity_Deprecated activity=(MainActivity_Deprecated) getActivity();
		listViews = new ArrayList<View>();
		linearLayout1 = new LinearLayout(activity);
		linearLayout2 = new LinearLayout(activity);
		linearLayout3 = new LinearLayout(activity);

		LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
		linearLayout1.setLayoutParams(layoutParams);

		listViews.add(linearLayout1);
//		hotView=new HotView(activity);
		linearLayout1.addView(hotView);

		listViews.add(linearLayout2);
		squareView=new SquareView(getActivity());
		squareView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		linearLayout2.addView(squareView);

		listViews.add(linearLayout3);
		focusView=new HomeFocusView(activity,new PublishPetSayNet());
		linearLayout3.addView(focusView);

		myPagerAdapter = new MyPagerAdapter(listViews);
		mPager.setAdapter(myPagerAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onPause() {
		pauseGif();
		super.onPause();
	}

	private void pauseGif(){
		GifViewManager.getInstance().stopGif();
		if(squareView!=null){
			squareView.clearView();		
		}
	}

	private void onChangePager(){
		//		PageIndex = getActivity().getIntent().getIntExtra(PAGEINDEX,PageIndex);
		mPager.setCurrentItem(PageIndex);
		if (PageIndex == 0) {
			hotView.onRefresh(false);
		}else if (PageIndex == 1) {
			if (null != squareView) {
				squareView.refreshView();
			} else {
				squareView = new SquareView(getActivity());
			}
		} else if (PageIndex == 2) {
			focusView.refreshUploadList();
			focusView.onRefresh();
		}
		
	}
	
	public void refresh(){
		if (PageIndex == 0) {
			hotView.onRefresh(true);
		}else if (PageIndex == 1) {
			if (null != squareView) {
				squareView.refreshView();
			} else {
				squareView = new SquareView(getActivity());
			}
		} else if (PageIndex == 2) {
			focusView.refreshUploadList();
			focusView.onRefresh();
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden){
			pauseGif();
		}else{ 
			if (UserManager.isUserChanged) {
				UserManager.isUserChanged=false;
				hotView.onRefresh(true);
				//		           attentionView.onRefresh();
			}
			onChangePager();
		}
		if(focusView != null)
			focusView.onHiddenChanged(hidden);

		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		hotView.release();
		focusView.release();
		squareView.clearView();
		super.onDestroy();
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
	 * 头标点击监听
	 */
	public class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			PageIndex  = arg0;
			changeSelectStatus(views, PageIndex);
//			PublicMethod.changeBtnBgColor(views, PageIndex,getActivity().getResources().getColor(R.color.home_0_tab_selected) , getActivity().getResources().getColor(R.color.transparent));
			switch (arg0) {
			case 0:		//热门
				focusView.stopGif();
				squareView.clearView();
				break;
			case 1:	//广场
				hotView.stopGif();
				focusView.stopGif();
				squareView.refreshView();
				break;
			case 2:	//关注
				hotView.stopGif();
				squareView.clearView();
				focusView.refreshUploadList();
				focusView.getFocusList();
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	
	private void changeSelectStatus(ImageView[] imgs,int index){
		for (int i = 0; i < imgs.length; i++) {
			if (i==index) {
				views[i].setVisibility(View.VISIBLE);
				imgTitles[i].setImageResource(pressResId[i]);
			}else {
				views[i].setVisibility(View.INVISIBLE);
				imgTitles[i].setImageResource(defaultResId[i]);
			}
		}
	}
	

}