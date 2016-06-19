package com.petsay.activity.award;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.award.adapter.AwardListAdapter;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.LoopImgPagerView.OnLoopImgItemClickListener;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.AwardNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.award.ActivityDTO;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * 奖品
 * 
 * @author G
 */
public class AwardListActivity extends BaseActivity implements IAddShowLocationViewService,
		NetCallbackInterface, OnLoopImgItemClickListener,OnClickListener {
	private ListView lv;

	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private LoopImgPagerView mLoopImgPagerView;
	private DialogPopupWindow popupWindow;

	private AwardListAdapter mAwardListAdapter;
	// private ShopNet mShopNet;
	private List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
	private List<SquareVo> squareVos;
	// private List<GoodsVo> freeGoodVos;
	private int pageIndex = 0;
	private int pageSize = 10;

	private AwardNet mAwardNet;
	private SayDataNet mSayDataNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.award);
		initView();
		showLoading();
		mSayDataNet = new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		mSayDataNet.layoutDatum(4);

		mAwardNet = new AwardNet();
		mAwardNet.setCallback(this);
		mAwardNet.setTag(AwardListActivity.this);
		popupWindow=new DialogPopupWindow(AwardListActivity.this, this);
		popupWindow.setOnClickListener(this);
	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		lv = (ListView) findViewById(R.id.lv_shop);
		mLoopImgPagerView = new LoopImgPagerView(getApplicationContext());
		// mTvExchangeHistory=(TextView) findViewById(R.id.tv_exchangeHistory);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.FILL_PARENT,
				android.widget.AbsListView.LayoutParams.WRAP_CONTENT);
		// mTvExchangeHistory.setOnClickListener(this);

		// mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());

		initTitleBar();
		initPullToRefreshView();
		// View
		// headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview,
		// null);
		// lv.addHeaderView(headerView);
		mAwardListAdapter = new AwardListAdapter(AwardListActivity.this);
		mLoopImgPagerView.setLayoutParams(layoutParams);
		mLoopImgPagerView.setOnLoopImgItemClickListener(this);
		mLoopImgPagerView.setImgClose(this);
		mLoopImgPagerView.setVisibility(View.GONE);
		lv.addHeaderView(mLoopImgPagerView);
		lv.setAdapter(mAwardListAdapter);

		// initList();
		// mAwardListAdapter.refreshData(activityDTOs);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ActivityDTO activityDTO;
				if (lv.getHeaderViewsCount()==0) {
					 activityDTO = (ActivityDTO) mAwardListAdapter.getItem(position);
				}else {
					 activityDTO = (ActivityDTO) mAwardListAdapter.getItem(position - 1);
				}
				Intent intent = new Intent();
				intent.setClass(AwardListActivity.this,
						AwardDetailActivity.class);
				intent.putExtra("activityDTO", activityDTO);
				startActivity(intent);
			}
		});

	}

	private void initTitleBar() {
		mTitleBar.setTitleText("奖品列表");
		mTitleBar.setFinishEnable(true);
		// tvTitleRight = PublicMethod.getTitleRightText(this);
		// tvTitleRight.setText("兑换记录");
		// LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// txt_Params.gravity = Gravity.CENTER;
		// txt_Params.leftMargin = 10;
		// LinearLayout layout = new LinearLayout(this);
		// layout.setOrientation(LinearLayout.HORIZONTAL);
		// layout.setGravity(Gravity.CENTER_VERTICAL);
		// layout.addView(tvTitleRight,txt_Params);
		// layout.setTag(0);
		// layout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent=new
		// Intent(PrizeListActivity.this,ExchangeHistoryActivity.class);
		// startActivity(intent);
		// }
		// });
		//
		// mTitleBar.addRightView(layout);
	}

	private void initPullToRefreshView() {
		// mPullView.setPullDownRefreshEnable(false);
		// mPullView.setPullUpRefreshEnable(false);
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

	private void onAddMore() {
		pageIndex++;
		if (mAwardListAdapter.getCount() > 0) {
			ActivityDTO vo = (ActivityDTO) mAwardListAdapter
					.getItem(mAwardListAdapter.getCount() - 1);
			mAwardNet.awardActivityList(vo.getId(), UserManager.getSingleton()
					.getActivePetId(), 10, true);
		} else {
			showToast("没有数据");
			mPullView.onComplete(true);
		}
	}

	private void onRefresh() {
		mSayDataNet.layoutDatum(4);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mAwardListAdapter && mAwardListAdapter.getCount() > 0) {
			mAwardListAdapter.notifyDataSetChanged();
		}
		// mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
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
		case RequestCode.REQUEST_AWARDACTIVITYLIST:
			
			closeLoading();
			try {
				activityDTOs = JsonUtils.getList(bean.getValue(),
						ActivityDTO.class);
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "解析试用商品列表出错");
				e.printStackTrace();
			}
			if (bean.isIsMore()) {
				mPullView.onFooterRefreshComplete();
				mAwardListAdapter.addMore(activityDTOs);
			} else {
				mPullView.onHeaderRefreshComplete();
				mAwardListAdapter.refreshData(activityDTOs);
			}
			if (SharePreferenceCache.getSingleton(getActivity()).getFirstVisitAward()) {
				SharePreferenceCache.getSingleton(getActivity()).setFirstVisitAward();
				popupWindow.setPopupText(1,null, "宠物说这次有大活动，一堆大奖等你拿。只要完成简单的任务就能获得，更有神秘大奖等你来抢。", "确定", null);
				popupWindow.show();
			}
			
			break;
		case RequestCode.REQUEST_LAYOUTDATUM:
			try {
				squareVos = JsonUtils.getList(bean.getValue(), SquareVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("奖品列表轮播图json解析出错");
			}
			int size=squareVos.size();
			if (size>0) {
				mLoopImgPagerView.setVisibility(View.VISIBLE);
				String[] imgUrls = new String[size];
				for (int i = 0; i < squareVos.size(); i++) {
					imgUrls[i] = squareVos.get(i).getIconUrl();
				}
				mLoopImgPagerView.setImgUrls(imgUrls);
			}else {
				lv.removeHeaderView(mLoopImgPagerView);
//				mLoopImgPagerView.setVisibility(View.GONE);
			}
			
			
			mAwardNet.awardActivityList("", UserManager.getSingleton().getActivePetId(), 10, false);
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_AWARDACTIVITYLIST:
			if (error.isIsMore()) {
				mPullView.onFooterRefreshComplete();
			}else {
				mPullView.onHeaderRefreshComplete();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void OnLoopImgItemClick(View v, int position) {
		SquareVo squareVo = squareVos.get(position);
		if (!TextUtils.isEmpty(squareVo.getKey())) {
			Intent intent = new Intent();
			intent.setClass(AwardListActivity.this, WebViewActivity.class);
			intent.putExtra("url", squareVo.getKey());
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_close:
			lv.removeHeaderView(mLoopImgPagerView);
			break;
		case R.id.tv_dialog_ok:
		    popupWindow.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return layoutRoot;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

}