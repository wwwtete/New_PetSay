package com.petsay.activity.shop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.shop.adapter.ExchangeHistoryAdapter;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShopNet;
import com.petsay.application.UserManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.shop.GoodsOrderVo;

import java.util.List;

import roboguice.inject.InjectView;
/**
 *兑换记录
 * @author G
 *
 */
public class ExchangeHistoryActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface{
	private ListView lv;
	private TitleBar mTitleBar;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private ShopNet mShopNet;
	private ExchangeHistoryAdapter mBuyHistoryAdapter;
	private List<GoodsOrderVo> mGoodsOrderVos;
	private int pageIndex=0;
	private int pageSize=10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_allfree);
		initView();
		mShopNet=new ShopNet();
		mShopNet.setCallback(this);
		mShopNet.setTag(ExchangeHistoryActivity.this);
		onRefresh();
		showLoading();
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_shop);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar();		
		initPullToRefreshView();
		mBuyHistoryAdapter=new ExchangeHistoryAdapter(this);
		lv.setAdapter(mBuyHistoryAdapter);
		
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText("兑换记录");
		mTitleBar.setFinishEnable(true);
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
	}
	
	private void initPullToRefreshView(){
		mPullView.setPullDownRefreshEnable(false);
		mPullView.setPullUpRefreshEnable(false);
		mPullView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				onRefresh();
			}
		});

		mPullView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onAddMore();
			}
		});
	}
	
	private void onRefresh() {
		pageIndex = 0;
		mShopNet.goodsOrderList(UserManager.getSingleton().getActivePetId(),pageIndex, pageSize, false);
	}

	private void onAddMore() {
		pageIndex++;
		if (mBuyHistoryAdapter.getCount() > 0) {
			mShopNet.goodsOrderList(UserManager.getSingleton().getActivePetId(), pageIndex, pageSize,true);
		} else {
			showToast("没有数据");
			mPullView.onComplete(true);
		}
	}
	
	private void onGetDataCallback(List<GoodsOrderVo> data,boolean isMore){
		if(isMore){
			mBuyHistoryAdapter.addMore(data);
			mPullView.onFooterRefreshComplete();
		}else {
			mBuyHistoryAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=mBuyHistoryAdapter&&mBuyHistoryAdapter.getCount()>0) {
			mBuyHistoryAdapter.notifyDataSetChanged();
		}
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
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_GOODSORDERLIST:
			try {
				mGoodsOrderVos=JsonUtils.getList(bean.getValue(), GoodsOrderVo.class);
				onGetDataCallback(mGoodsOrderVos, bean.isIsMore());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
	}
}
