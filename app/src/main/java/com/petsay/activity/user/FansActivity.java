package com.petsay.activity.user;

import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.adapter.FansAdapter;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.application.NetworkManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;

/**
 * 我关注的
 * @author G
 *
 */
public class FansActivity extends BaseActivity implements NetCallbackInterface {
	private TitleBar mTitleBar;
	private ListView lvAttention;
	private FansAdapter fansAdapter;
//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;
	private String petId;
	private int mPageIndex = 0;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullToRefreshView;
	/**跳转类型：0：代表别人的粉丝列表，1：代表自己的粉丝列表*/
	private int mJumpType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attention_activity);
		petId=getIntent().getStringExtra("petId");
		mJumpType = getIntent().getIntExtra("type", 0);
		initView();
		initTitleBar();
		setListener();
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
	}
	
	private void initTitleBar() {
		mTitleBar.setTitleText("粉丝");
		mTitleBar.setFinishEnable(true);
	}

	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		lvAttention=(ListView) findViewById(R.id.lv_attention);
		fansAdapter = new FansAdapter(FansActivity.this,mJumpType);
		lvAttention.setAdapter(fansAdapter);
	}
	
	@Override
	protected void onResume() {
		onRefresh();
		super.onResume();
	}
	
	private void setListener() {
		lvAttention.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PetVo petInfo = (PetVo) fansAdapter.getItem(position);
                ActivityTurnToManager.getSingleton().userHeaderGoto(FansActivity.this, petInfo);
            }
        });
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				onRefresh();
			}
		});
		
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                onLoadMore();
            }
        });
	}
	
	
	private void onPulltoRefreshCallback(Message msg){
		if(msg.arg1 == 0){
			mPullToRefreshView.onHeaderRefreshComplete();
		}else {
			mPullToRefreshView.onFooterRefreshComplete();
		}
	}
	
	/**
	 * 刷新操作
	 */
	public void onRefresh() {
		mPullToRefreshView.showHeaderAnimation();
		mPageIndex = 0;
		netwrok(0, 0);
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		mPageIndex ++;
		netwrok(mPageIndex,1);
	}
	
	private void netwrok(int index,int arg){
//		showLoading();
		mUserNet.MyFans(petId, index, 30, arg);
	}
	
	public void onAttention(PetVo petInfo){
		if (UserManager.getSingleton().isLoginStatus()) {
			showLoading();
			if (petInfo.getRs()==0 || petInfo.getRs()==1) {
				mUserNet.focus(petInfo.getId(),UserManager.getSingleton().getActivePetInfo().getId());
			}else if (petInfo.getRs()==2) {
				//TODO 取消关注
				mUserNet.cancleFocus(petInfo.getId(),UserManager.getSingleton().getActivePetInfo().getId());
			}

		}else {
			Intent intent=new Intent(this, UserLogin_Activity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			onRefresh();
			break;
		case RequestCode.REQUEST_CANCLEFOCUS:
			onRefresh();
			break;
		case RequestCode.REQUEST_MYFANS:
			List<PetVo> mPetInfos = null;
			try {
				mPetInfos = JsonUtils.parseList(bean.getValue(), PetVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(bean.isIsMore()){
				fansAdapter.addMore(mPetInfos);
			}else {
				fansAdapter.refreshData(mPetInfos);
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			
			break;
		case RequestCode.REQUEST_CANCLEFOCUS:
			break;
		case RequestCode.REQUEST_MYFANS:
			NetworkManager.getSingleton().canclePullRefresh(FansActivity.this,mPullToRefreshView );
			break;
		default:
			break;
		}
	}
	
}