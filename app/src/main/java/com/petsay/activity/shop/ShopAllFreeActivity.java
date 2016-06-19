package com.petsay.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.shop.adapter.ShopFreeAllAdapter;
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
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.shop.GoodsVo;

import java.util.List;

import roboguice.inject.InjectView;
/**
 *全部试用
 * @author G
 *
 */
public class ShopAllFreeActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface{
	private ListView lv;
	private TitleBar mTitleBar;
//	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	
	private ShopFreeAllAdapter mFreeAllAdapter;
	private ShopNet mShopNet;
//	private GifListScrollListener mScrollListener;
	private List<GoodsVo> goodsVos;
	private int pageIndex=0;
	private int pageSize=10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_allfree);
        
		initView();
		mShopNet=new ShopNet();
		mShopNet.setCallback(this);
		mShopNet.setTag(ShopAllFreeActivity.this);
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
		mFreeAllAdapter=new ShopFreeAllAdapter(this);
		lv.setAdapter(mFreeAllAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				GoodsVo goodsVo=(GoodsVo) mFreeAllAdapter.getItem(position);
				Intent intent=new  Intent(ShopAllFreeActivity.this,GoodsFreeDetailActivity.class);
				intent.putExtra("goods", goodsVo);
				startActivity(intent);
			}
		});
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText("全部试用");
		mTitleBar.setFinishEnable(true);
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
	}
	
	private void initPullToRefreshView(){
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
		mShopNet.goodsTrialList(UserManager.getSingleton().getActivePetId(), pageIndex, pageSize,false);
	}

	private void onAddMore() {
		pageIndex++;
		if (mFreeAllAdapter.getCount() > 0) {
			mShopNet.goodsTrialList(UserManager.getSingleton().getActivePetId(), pageIndex, pageSize,true);
		} else {
			showToast("没有数据");
			mPullView.onComplete(true);
		}
	}
	
	private void onGetDataCallback(List<GoodsVo> data,boolean isMore){
		if(isMore){
			mFreeAllAdapter.addMore(data);
			mPullView.onFooterRefreshComplete();
			
		}else {
			mFreeAllAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=mFreeAllAdapter&&mFreeAllAdapter.getCount()>0) {
			mFreeAllAdapter.notifyDataSetChanged();
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
		case RequestCode.REQUEST_GOODSTRIALLIST:
			try {
				goodsVos =JsonUtils.getList(bean.getValue(), GoodsVo.class);
				onGetDataCallback(goodsVos, bean.isIsMore());
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "获取所有试用商品列表出错");
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
