package com.petsay.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.WrapContentHeightViewPager;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShopNet;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.shop.GoodsVo;

import java.util.ArrayList;
import java.util.List;
/**
 *商品详情
 * @author G
 *
 */
public class GoodsDetailActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface, OnClickListener{
	private TitleBar mTitleBar;
	private RelativeLayout layoutRoot;
	private WebView webview;
	private LinearLayout mPointLayout;
	private WrapContentHeightViewPager mVPager;
	private List<ImageView> mImageViews;
	private View[] views;
	private TextView mTvDescription,mTvCoin,mTvPostType,mTvBuy;
	
	private ShopNet mShopNet;
	private UserNet mUserNet;
	private GoodsVo mGoodsVo;
	private PetVo petInfo;
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private int PageIndex=0;
	private MyPagerAdapter myPagerAdapter;
	private DialogPopupWindow exchangePopupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_detail);
		mGoodsVo=(GoodsVo) getIntent().getSerializableExtra("goods");
		initView();
		initPager();
		mShopNet=new ShopNet();
		mShopNet.setCallback(this);
		mShopNet.setTag(GoodsDetailActivity.this);
		
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		exchangePopupWindow=new DialogPopupWindow(getActivity(), this);
		exchangePopupWindow.setOnClickListener(this);
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
		 if ( mGoodsVo.getPhotos().length<2) {
         	mPointLayout.setVisibility(View.GONE);
			}
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
//		webview.loadData(mGoodsVo.getDetail(), mimeType, encoding);
		 
		 webview.setWebChromeClient(new WebChromeClient());
		 webview.setWebViewClient(new WebViewClient() {
	 
	            /* (non-Javadoc)
	             * @see   android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	             */
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                Intent intent = new Intent(GoodsDetailActivity.this, WebViewActivity.class);
	                
	                
	                
	        		intent.putExtra("folderPath", "");
	        		intent.putExtra("url", url);
	        		startActivity(intent);
	                return true;
	            }
	             
	        });
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
		mTvBuy=(TextView) findViewById(R.id.tv_applyBuy);
		initTitleBar();		
		
		mTvBuy.setOnClickListener(this);
		mTvDescription.setText(mGoodsVo.getDescription());
		mTvCoin.setText(mGoodsVo.getPrice()+"");
		if (mGoodsVo.getPostageType()==0) {
			mTvPostType.setVisibility(View.VISIBLE);
			mTvPostType.setText("免运费");
		}else {
			mTvPostType.setVisibility(View.GONE);
		}
		
//		switch (mGoodsVo.getState()) {
//		case 0:
//			mTvBuy.setText("即将开售");
//			break;
//		case 1:
//			mTvBuy.setText("我要兑换");
//			break;
//		case 2:
//			mTvBuy.setText("已售完");
//			break;
//		case 3:
//			mTvBuy.setText("兑换结束");
//			break;
//		default:
//			break;
//		}
		switch (mGoodsVo.getState()) {
		case 10:
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setClickable(false);
			mTvBuy.setText(getString(R.string.shop_start_trial));
			break;
		case 11:
			mTvBuy.setBackgroundResource(R.drawable.shop_buy);
			mTvBuy.setClickable(true);
			mTvBuy.setText(getString(R.string.shop_exchange));
			break;
		case 12:
			mTvBuy.setClickable(false);
			mTvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			mTvBuy.setText(getString(R.string.shop_no_goods));
			break;
		case 13:
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
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
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
		mShopNet.cancelAll(GoodsDetailActivity.this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_applyBuy:
			mTvBuy.setClickable(false);
//			applyFreeBuy();
//			CustomDialogFactory customDialogFactory=CustomDialogFactory.getSingleton();
//			customDialogFactory.initActivity(getActivity());
//			customDialogFactory.MyDialog("商品兑换成功", "快去兑换记录中领取该商品吧", "继续兑换", "领取商品");
			exchange();
			break;
		case R.id.tv_dialog_ok:
			mTvBuy.setClickable(true);
			exchangePopupWindow.dismiss();
			if (exchangePopupWindow.mTag==1) {
			}else if (exchangePopupWindow.mTag==2) {
				mUserNet.petOne(UserManager.getSingleton().getActivePetId(),null);
				//TODO 现获取服务器宠豆
				
			}else {
			}
			break;
		case R.id.tv_dialog_cancle:
			mTvBuy.setClickable(true);
			exchangePopupWindow.dismiss();
            if (exchangePopupWindow.mTag==1) {
			}else if (exchangePopupWindow.mTag==2) {
				
			}else {
				Intent intent=new Intent(GoodsDetailActivity.this,ExchangeHistoryActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		
	}
	private void exchange(){
		
		if (UserManager.getSingleton().getActivePetInfo().getCoin()<mGoodsVo.getPrice()) {
			
			exchangePopupWindow.setPopupText(1,null, "哎呀，你的宠豆太少了，攒攒再来兑换吧", "确定", null);
			exchangePopupWindow.show();
		}else {
			exchangePopupWindow.setPopupText(2,null, "确定花费"+mGoodsVo.getPrice()+"宠豆兑换该商品吗", "确定", "取消");
			exchangePopupWindow.show();
		}
		
	}
	
	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_GOODSORDERCREATE:
			if (petInfo!=null) {
				petInfo.setCoin(petInfo.getCoin()-mGoodsVo.getPrice());
				UserManager.getSingleton().setActivePetInfo(petInfo);
			}else {
				int coin=UserManager.getSingleton().getActivePetInfo().getCoin()-mGoodsVo.getPrice();
				UserManager.getSingleton().getActivePetInfo().setCoin(coin);
			}
			DataFileCache.getSingleton().asyncSaveData(Constants.UserFile,UserManager.getSingleton().getUserInfo());
			mTvBuy.setClickable(true);
			exchangePopupWindow.setPopupText(3,"商品兑换成功！", "快去兑换记录中领取该商品吧", "继续兑换", "跳转到兑换记录");
			exchangePopupWindow.show();
			
			break;
		case RequestCode.REQUEST_PETONE:
			PetVo petInfo = JsonUtils.resultData(bean.getValue(),PetVo.class);
			petInfo.setActive(true);
			UserManager.getSingleton().setActivePetInfo(petInfo);
			DataFileCache.getSingleton().asyncSaveData(Constants.UserFile,UserManager.getSingleton().getUserInfo());
			mShopNet.goodsOrderCreate(UserManager.getSingleton().getActivePetId(), mGoodsVo.getCode());
			break;
		default:
			break;
		}
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
		mTvBuy.setClickable(true);
//		switch (requestCode) {
//		case RequestCode.REQUEST_GOODSORDERCREATE:
//			
//			break;
//
//		default:
//			
//			break;
//		}
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

	

}
