package com.petsay.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.WrapContentHeightViewPager;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShopNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.shop.GoodsVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.Share.ShareCallback;
import cn.sharesdk.onekeyshare.SharePopupWindow;
/**
 *试用商品详情
 * @author G
 *
 */
public class GoodsFreeDetailActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface, OnClickListener, ShareCallback{
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	private WebView webview;
	private LinearLayout mPointLayout;
	private WrapContentHeightViewPager mVPager;
	private List<ImageView> mImageViews;
	private View[] views;
	private TextView mTvDescription,mTvCoin,mTvPostType,mTvAllCount,mTvJoinCount,mTvRemaindays;
	private Button mTvBuy;
	private ShopNet mShopNet;
	private GoodsVo mGoodsVo;
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private int PageIndex=0;
	private MyPagerAdapter myPagerAdapter;
	private boolean isShared=false;
	private final int SHARE_COMPLETE=111;
	private final int SHARE_ERROR=112;
	private final int SHARE_CANCLE=113;
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHARE_COMPLETE:
			    PublicMethod.showToast(getApplicationContext(), "分享成功");
				break;
			case SHARE_ERROR:
				 PublicMethod.showToast(getApplicationContext(), "分享失败");
				break;
			case SHARE_CANCLE:
				 PublicMethod.showToast(getApplicationContext(), "分享已取消");
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodsfree_detail);
		mGoodsVo=(GoodsVo) getIntent().getSerializableExtra("goods");
		initView();
		initPager();
		mShopNet=new ShopNet();
		mShopNet.setCallback(this);
		mShopNet.setTag(GoodsFreeDetailActivity.this);
//		mShopNet.goodsOrderList(UserManager.getSingleton().getActivePetId(), 0, 10);
//		showLoading();
	}
	private void initPager() {
		mImageViews = new ArrayList<ImageView>();

		LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		LayoutParams pointParams=new LayoutParams(PublicMethod.getPxInt(10,  getApplicationContext()), PublicMethod.getPxInt(10,  getApplicationContext()));
		pointParams.setMargins(PublicMethod.getPxInt(5, getApplicationContext()), 0, PublicMethod.getPxInt(5,  getApplicationContext()), 0);
//		Drawable focuse = SkinManager.getInstance(this).getDrawable(getString(R.string.page_indicator_focused));
//		Drawable unFocuse = SkinManager.getInstance(this).getDrawable(getString(R.string.page_indicator_unfocused));
		views=new View[ mGoodsVo.getPhotos().length];
		for (int i = 0; i < mGoodsVo.getPhotos().length; i++) {
			ImageView imageView=new ImageView(getApplicationContext());
			imageView.setAdjustViewBounds(true);
			imageView.setLayoutParams(layoutParams);
			ImageLoaderHelp.displayContentImage(mGoodsVo.getPhotos()[i], imageView);
			mImageViews.add(imageView);
			View view=new View(getApplicationContext());
			view.setLayoutParams(pointParams);
//			if (i==0) {
//				view.setBackgroundDrawable(focuse);
//			}else{
//				view.setBackgroundDrawable(unFocuse);
//			}
			mPointLayout.addView(view);
			views[i]=view;
		}
		myPagerAdapter = new MyPagerAdapter(mImageViews);
		mVPager.setAdapter(myPagerAdapter);
		mVPager.setCurrentItem(0);
		mVPager.setOnPageChangeListener(new MyOnPageChangeListener(null,null));
		webview.getSettings().setDefaultTextEncodingName("utf-8") ;
		webview.loadDataWithBaseURL(null, mGoodsVo.getDetail(), "text/html", "utf-8", null);
		 webview.setWebChromeClient(new WebChromeClient());
		 webview.setWebViewClient(new WebViewClient() {
	 
	            /* (non-Javadoc)
	             * @see   android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	             */
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                Intent intent = new Intent(GoodsFreeDetailActivity.this, WebViewActivity.class);
	                
	        		intent.putExtra("folderPath", "");
	        		intent.putExtra("url", url);
	        		startActivity(intent);
	                return true;
	            }
	             
	        });
//		webview.loadData(mGoodsVo.getDetail(), mimeType, encoding);
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mVPager=(WrapContentHeightViewPager) findViewById(R.id.vPager);
		mPointLayout=(LinearLayout) findViewById(R.id.llayout);
		webview=(WebView) findViewById(R.id.webview);
		mTvDescription=(TextView) findViewById(R.id.tv_description);
		mTvCoin=(TextView) findViewById(R.id.tv_coin);
		mTvPostType=(TextView) findViewById(R.id.tv_posttype);
		mTvAllCount=(TextView) findViewById(R.id.tv_allcount);
		mTvJoinCount=(TextView) findViewById(R.id.tv_joincount);
		mTvRemaindays=(TextView) findViewById(R.id.tv_remaindays);
		mTvBuy=(Button) findViewById(R.id.tv_applyBuy);
		initTitleBar();		
		
		
		
		mTvDescription.setText(mGoodsVo.getDescription());
		mTvCoin.setText(mGoodsVo.getPrice()+"");
		if (mGoodsVo.getPostageType()==0) {
			mTvPostType.setVisibility(View.VISIBLE);
			mTvPostType.setText("免运费");
		}else {
			mTvPostType.setVisibility(View.GONE);
		}
		mTvAllCount.setText("试用数："+mGoodsVo.getInventory());
		mTvJoinCount.setText("已参与："+mGoodsVo.getApply());
		
		mTvRemaindays.setText("距离结束："+PublicMethod.calcDaysFromToday(mGoodsVo.getEndTime()));
		
		mTvBuy.setOnClickListener(this);
		switch (mGoodsVo.getState()) {
		case 20:
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setClickable(false);
			mTvBuy.setText(getString(R.string.shop_start_trial));
			mTvRemaindays.setText(getString(R.string.shop_start_trial));
			break;
		case 21:
			mTvRemaindays.setText("距离结束："+PublicMethod.calcDaysFromToday(mGoodsVo.getEndTime()));
			if (mGoodsVo.getOrderState().equals("41")) {
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setClickable(false);
				mTvBuy.setText(getString(R.string.shop_applying));
			}else if (mGoodsVo.getOrderState().equals("42")) {
//				holder.tvBuy.setText("申请成功");
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setClickable(false);
				mTvBuy.setText(getString(R.string.shop_apply_success));
			}else if (mGoodsVo.getOrderState().equals("43")) {
//				holder.tvBuy.setText("申请失败");
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setClickable(false);
				mTvBuy.setText(getString(R.string.shop_apply_failed));
			}else if (mGoodsVo.getOrderState().equals("44")) {
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setClickable(false);
				mTvBuy.setText(getString(R.string.shop_already_receive));
			}else{
//				holder.tvBuy.setText("申请试用");
				mTvBuy.setBackgroundResource(R.drawable.shop_buy);
				mTvBuy.setClickable(true);
				mTvBuy.setText(getString(R.string.shop_applytrial));
			}
			break;
		case 22:
			mTvRemaindays.setText(getString(R.string.shop_end_trial));
			if (mGoodsVo.getOrderState().equals("41")) {
				mTvBuy.setClickable(false);
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setText(getString(R.string.shop_applying));
			}else if (mGoodsVo.getOrderState().equals("42")) {
				mTvBuy.setClickable(false);
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setText(getString(R.string.shop_apply_success));
			}else if (mGoodsVo.getOrderState().equals("43")) {
				mTvBuy.setClickable(false);
				mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				mTvBuy.setText(getString(R.string.shop_apply_failed));
			}else {
				mTvBuy.setBackgroundResource(R.drawable.shop_buy);
				mTvBuy.setText(getString(R.string.shop_applytrial));
			}
			break;
		case 23:
			mTvBuy.setClickable(false);
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setText(getString(R.string.shop_goods_already_over));
		default:
			mTvBuy.setClickable(false);
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setText(getString(R.string.shop_no_goods));
			break;
		}
		
		
		
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText("商品详情");
		mTitleBar.setFinishEnable(true);
		tvTitleRight = new TextView(this);
        tvTitleRight.setTextColor(Color.WHITE);
		tvTitleRight.setText("分享");
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(tvTitleRight,txt_Params);
		layout.setTag(0);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharePopupWindow popupWindow;
				popupWindow=new SharePopupWindow(getActivity(), GoodsFreeDetailActivity.this,GoodsFreeDetailActivity.this,mGoodsVo,!isShared);
				popupWindow.isShowForward(false);
				popupWindow.show();
			}
		});
		
		mTitleBar.addRightView(layout);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public View getParentView() {
		return layoutRoot;
	}
	@Override
	public Activity getActivity() {
		return this;
	}
	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_GOODSORDERCREATE:
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setText("申请中");
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		
	}
	
	
	
	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<ImageView> mListViews;

		public MyPagerAdapter(List<ImageView> mListViews) {
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
		private MyOnPageChangeListener(Drawable unfocused,Drawable foucese){
			if(unfocused == null)
				mUnfocused = getResources().getDrawable(R.drawable.page_indicator_focused);
			else
				this.mUnfocused = unfocused;
			if(foucese == null)
				mfocuse = getResources().getDrawable(R.drawable.page_indicator_unfocused);
			else
				this.mfocuse = foucese;
		}

		@Override
		public void onPageSelected(int arg0) {
			PageIndex  = arg0;
		    PublicMethod.changeBtnBgImg(views, PageIndex, mfocuse, mUnfocused);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_applyBuy:
			if (isShared) {
				applyFreeBuy();
			}else {
				SharePopupWindow popupWindow=new SharePopupWindow(GoodsFreeDetailActivity.this, this,this,mGoodsVo,!isShared);
				popupWindow.isShowForward(false);
				popupWindow.show();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void shareError(Platform platform, int arg1, Throwable arg2) {
		mHandler.sendEmptyMessage(SHARE_ERROR);
		
	}
	@Override
	public void shareComplete(Platform platform, int arg1,HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(SHARE_COMPLETE);
		isShared=true;
		applyFreeBuy();
	}
	@Override
	public void shareCancel(Platform platform, int arg1) {
		mHandler.sendEmptyMessage(SHARE_CANCLE);
	}

	private void applyFreeBuy(){
		mShopNet.goodsOrderCreate(UserManager.getSingleton().getActivePetId(), mGoodsVo.getCode());
	}
}

