package com.petsay.activity.award;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.award.adapter.MyTaskListAdapter;
import com.petsay.activity.main.MainActivity;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.NullTipView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar.OnClickBackListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.AwardNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.award.PetActivityDTO;
import com.petsay.vo.award.PetActivityInfoDTO;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
/**
 *我的任务
 * @author G
 */
public class MyTaskActivity extends BaseActivity implements NetCallbackInterface{
	private ListView lv;
	
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private CircleImageView imgHeader;
	private TextView tvCompleted,tvUndone;
	private Button btnHistory;
	private NullTipView mNullTip;
	
	private MyTaskListAdapter myTaskListAdapter;
	private List<PetActivityDTO> petActivityDTOs=new ArrayList<PetActivityDTO>();
	private int pageSize=10;
	
	private AwardNet mAwardNet;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.award_task);
		initView();
		mAwardNet=new AwardNet();
		mAwardNet.setCallback(this);
		mAwardNet.setTag(MyTaskActivity.this);
		showLoading();
		onRefresh();
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		imgHeader=(CircleImageView) findViewById(R.id.img_header);
		tvCompleted=(TextView) findViewById(R.id.tv_completed);
		tvUndone=(TextView) findViewById(R.id.tv_undone);
		btnHistory=(Button) findViewById(R.id.btn_award_history);
		lv=(ListView) findViewById(R.id.lv_shop);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mNullTip = (NullTipView) findViewById(R.id.nulltip);
		android.widget.AbsListView.LayoutParams layoutParams=new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.FILL_PARENT, android.widget.AbsListView.LayoutParams.WRAP_CONTENT);
		
		initTitleBar();		
		initPullToRefreshView();
		myTaskListAdapter=new MyTaskListAdapter(MyTaskActivity.this,false);
		lv.setAdapter(myTaskListAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PetActivityDTO dto = (PetActivityDTO) myTaskListAdapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(MyTaskActivity.this, AwardDetailActivity.class);
				intent.putExtra("activityDTO", dto.getActivityDTO());
				startActivity(intent);


			}
		});
		
		btnHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyTaskActivity.this, MyTaskAllActivity.class);
				startActivity(intent);
			}
		});
		ImageLoaderHelp.displayHeaderImage(UserManager.getSingleton().getActivePetInfo().getHeadPortrait(), imgHeader);

		mNullTip.setClickButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyTaskActivity.this,AwardListActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText("我的任务");
//		mTitleBar.setFinishEnable(true);
		mTitleBar.setOnClickBackListener(new OnClickBackListener() {

			@Override
			public void OnClickBackListener() {
				returnMainActivity();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			returnMainActivity();
			 return true;
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
		
	}
	
	private void returnMainActivity(){
		 Intent intent=new Intent(MyTaskActivity.this,MainActivity.class);
		 intent.putExtra(HomeFragment.PAGEINDEX, 0);
		 startActivity(intent);
		 finish();
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
	
	 private void onAddMore(){
	        if(myTaskListAdapter.getCount() > 0){
	            PetActivityDTO vo = (PetActivityDTO) myTaskListAdapter.getItem(myTaskListAdapter.getCount()- 1);
	        	mAwardNet.awardActivityMyList(UserManager.getSingleton().getActivePetId(), vo.getId(), pageSize,true);
	        }else{
	            showToast("没有数据");
	            mPullView.onComplete(true);
	        }
	    }
	
	private void onRefresh(){
		mAwardNet.awardActivityMyList(UserManager.getSingleton().getActivePetId(), "", pageSize,false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=myTaskListAdapter&&myTaskListAdapter.getCount()>0) {
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
		case RequestCode.REQUEST_AWARDACTIVITYMYLIST:
			closeLoading();
			try {
					petActivityDTOs=JsonUtils.getList(bean.getValue(), PetActivityDTO.class);
					for (int i = 0; i < petActivityDTOs.size(); i++) {
						petActivityDTOs.get(i).setStateDrawableRes();
						petActivityDTOs.get(i).setListItemResByCatagory();
						petActivityDTOs.get(i).setTaskProgres();;
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
				if(petActivityDTOs.isEmpty()){
					mNullTip.setVisibility(View.VISIBLE);
				}else {
					mNullTip.setVisibility(View.GONE);
				}
			}
			
			mAwardNet.awardActivityMyListInfo(UserManager.getSingleton().getActivePetId());
			break;
		case RequestCode.REQUEST_AWARDACTIVITYMYLISTINFO:
			closeLoading();
			PetActivityInfoDTO activityInfoDTO;
			activityInfoDTO=JsonUtils.resultData(bean.getValue(), PetActivityInfoDTO.class);
			tvCompleted.setText("已完成："+activityInfoDTO.getFinishedActCount()+"个");
			tvUndone.setText("未完成："+activityInfoDTO.getUnfinishedActCount()+"个");
			break;
		}
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_AWARDACTIVITYMYLIST:
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