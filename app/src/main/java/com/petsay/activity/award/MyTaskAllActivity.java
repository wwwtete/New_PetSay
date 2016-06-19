package com.petsay.activity.award;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.award.adapter.MyTaskListAdapter;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.AwardNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.award.PetActivityDTO;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * 我的任务(所有)
 * 
 * @author G
 */
public class MyTaskAllActivity extends BaseActivity implements
		NetCallbackInterface {
	private ListView lv;

	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private RelativeLayout layoutHistory;// 与我的任务列表共用一套xml布局，这里不需要显示此layout
	private MyTaskListAdapter myTaskListAdapter;
	private List<PetActivityDTO> petActivityDTOs = new ArrayList<PetActivityDTO>();
	private int pageSize = 10;

	private AwardNet mAwardNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.award_task);
		initView();
		mAwardNet = new AwardNet();
		mAwardNet.setCallback(this);
		mAwardNet.setTag(MyTaskAllActivity.this);
		showLoading();
		onRefresh();
	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		layoutHistory = (RelativeLayout) findViewById(R.id.layout_history);
		layoutHistory.setVisibility(View.GONE);
		lv = (ListView) findViewById(R.id.lv_shop);
		
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.FILL_PARENT,
				android.widget.AbsListView.LayoutParams.WRAP_CONTENT);

		initTitleBar();
		initPullToRefreshView();
		myTaskListAdapter = new MyTaskListAdapter(MyTaskAllActivity.this,true);
		lv.setAdapter(myTaskListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				PetActivityDTO dto = (PetActivityDTO) myTaskListAdapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(MyTaskAllActivity.this,AwardDetailActivity.class);
				intent.putExtra("activityDTO", dto.getActivityDTO());
				startActivity(intent);
			}
		});

	}

	private void initTitleBar() {
		mTitleBar.setTitleText("历史任务");
		mTitleBar.setFinishEnable(true);
	}

	private void initPullToRefreshView() {
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
		if (myTaskListAdapter.getCount() > 0) {
			PetActivityDTO vo = (PetActivityDTO) myTaskListAdapter.getItem(myTaskListAdapter.getCount() - 1);
			mAwardNet.awardActivityMyListAll(UserManager.getSingleton().getActivePetId(), vo.getId(), pageSize, true);
		} else {
			showToast("没有数据");
			mPullView.onComplete(true);
		}
	}

	private void onRefresh() {
		mAwardNet.awardActivityMyListAll(UserManager.getSingleton().getActivePetId(), "", pageSize, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != myTaskListAdapter && myTaskListAdapter.getCount() > 0) {
			myTaskListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {

		switch (requestCode) {
		case RequestCode.REQUEST_AWARDACTIVITYMYLISTALL:
			closeLoading();
			try {
				petActivityDTOs = JsonUtils.getList(bean.getValue(),PetActivityDTO.class);
				for (int i = 0; i < petActivityDTOs.size(); i++) {
					petActivityDTOs.get(i).setStateDrawableRes();
					petActivityDTOs.get(i).setListItemResByCatagory();
					petActivityDTOs.get(i).setTaskProgres();
				}
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "解析试用商品列表出错");
				e.printStackTrace();
			}
			if (bean.isIsMore()) {
				mPullView.onFooterRefreshComplete();
				myTaskListAdapter.addMore(petActivityDTOs);
			}else {
				mPullView.onHeaderRefreshComplete();
				myTaskListAdapter.refreshData(petActivityDTOs);
			}
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_AWARDACTIVITYMYLISTALL:
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
}