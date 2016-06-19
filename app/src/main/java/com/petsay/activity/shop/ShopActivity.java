package com.petsay.activity.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.shop.adapter.ShopAdapter;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShopNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.shop.GoodsVo;

import java.util.List;

import roboguice.inject.InjectView;
/**
 *商城
 * @author G
 */
public class ShopActivity extends BaseActivity implements NetCallbackInterface{
	private ListView lv;
	
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	
	private ShopAdapter mShopAdapter;
	private TextView mTvPetName,mTvCoin,mTvSuspendType;
	private ShopNet mShopNet;
	private List<GoodsVo> exchangeGoodVos;
	private List<GoodsVo> freeGoodVos;
	private int pageIndex=0;
	private int pageSize=10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
        
		initView();
		mShopNet=new ShopNet();
		mShopNet.setCallback(this);
		mShopNet.setTag(ShopActivity.this);
		mShopNet.goodsTrialTop2(UserManager.getSingleton().getActivePetId());
		showLoading();
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_shop);
//		mTvExchangeHistory=(TextView) findViewById(R.id.tv_exchangeHistory);
		mTvCoin=(TextView) findViewById(R.id.tv_coin);
		mTvPetName=(TextView) findViewById(R.id.tv_petname);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mTvSuspendType=(TextView) findViewById(R.id.tv_suspend_type);
//		mTvExchangeHistory.setOnClickListener(this);
		
//		mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
		
		mTvPetName.setText(UserManager.getSingleton().getActivePetInfo().getNickName());
		initTitleBar();		
		initPullToRefreshView();
//		View headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview, null);
//		lv.addHeaderView(headerView);
		mShopAdapter=new ShopAdapter(ShopActivity.this);
		lv.setAdapter(mShopAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				GoodsVo goodsVo=(GoodsVo) mShopAdapter.getItem(position);
				Intent intent=new Intent();
				if (goodsVo.getSalesMode()==0) {
					//试用
					intent.setClass(ShopActivity.this,GoodsFreeDetailActivity.class);
				}else {
					//兑换
					intent.setClass(ShopActivity.this,GoodsDetailActivity.class);
				}
				
				intent.putExtra("goods", goodsVo);
				startActivity(intent);
				
			}
		});
		
		lv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				GoodsVo goodsVo=(GoodsVo) mShopAdapter.getItem(firstVisibleItem);
				if (null!=goodsVo) {
					if (goodsVo.getSalesMode()==GoodsVo.SalesMode_Free) {
						mTvSuspendType.setText("免费试用");
					}else {
						mTvSuspendType.setText("宠豆商城");
					}
				}
				
			}
		});
		
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText("商城");
		mTitleBar.setFinishEnable(true);
		tvTitleRight = new TextView(this);
        tvTitleRight.setTextColor(Color.WHITE);
		tvTitleRight.setText("兑换记录");
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
				Intent intent=new Intent(ShopActivity.this,ExchangeHistoryActivity.class);
				startActivity(intent);
			}
		});
		
		mTitleBar.addRightView(layout);
	}
	
	private void initPullToRefreshView(){
//		mPullView.setPullDownRefreshEnable(false);
		mPullView.setPullUpRefreshEnable(false);
//		mPullView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
//			@Override
//			public void onHeaderRefresh(PullToRefreshView view) {
////				onRefresh();
//			}
//		});
		
		  

		mPullView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onAddMore();
			}
		});
	}
	
	 private void onAddMore(){
	    	pageIndex++;
	        if(mShopAdapter.getCount() > 0){
//	            PetScoreDetailVo vo = (PetScoreDetailVo) mBuyHistoryAdapter.getItem(mBuyHistoryAdapter.getCount()- 1);
	        	mShopNet.goodsExchangeList(UserManager.getSingleton().getActivePetId(), pageIndex, pageSize,true);
	        }else{
	            showToast("没有数据");
	            mPullView.onComplete(true);
	        }
	    }
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=mShopAdapter&&mShopAdapter.getCount()>0) {
			mShopAdapter.notifyDataSetChanged();
		}
		mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
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
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_GOODSTRIALTOP2:
			try {
					freeGoodVos=JsonUtils.getList(bean.getValue(), GoodsVo.class);
				} catch (Exception e) {
					PublicMethod.showToast(getApplicationContext(), "解析试用商品列表出错");
					e.printStackTrace();
				}
			pageIndex=0;
			mShopNet.goodsExchangeList(UserManager.getSingleton().getActivePetId(), pageIndex, pageSize,false);
			break;
		case RequestCode.REQUEST_GOODSEXCHANGELIST:
			closeLoading();
			try {
				exchangeGoodVos=JsonUtils.getList(bean.getValue(), GoodsVo.class);
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "解析兑换商品列表出错");
				e.printStackTrace();
			}
			if (bean.isIsMore()) {
				mShopAdapter.addMore(exchangeGoodVos);
				mPullView.onFooterRefreshComplete();
			}else {
				mShopAdapter.refreshData(freeGoodVos,exchangeGoodVos);
			}
			
			
			break;
		}
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
	}
	
}