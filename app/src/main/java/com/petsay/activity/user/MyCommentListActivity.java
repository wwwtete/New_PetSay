package com.petsay.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.adapter.UserCommentAdapter;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.NullTipView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

import roboguice.inject.InjectView;
/**
 * 频道，品种，全部
 * @author G
 *
 */
public class MyCommentListActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface{
	private ListView lv;
	private TitleBar mTitleBar;
//	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	@InjectView(R.id.nulltip)
	private NullTipView mNullTip;
	
	private SayDataNet mSayDataNet;
	private UserCommentAdapter mUserCommentAdapter;
	private GifListScrollListener mScrollListener;
	private String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_saylist_layout);
		type=getIntent().getStringExtra("type");
        mSayDataNet=new SayDataNet();
        mSayDataNet.setTag(MyCommentListActivity.this);
        mSayDataNet.setCallback(this);
        
		initView();
		showLoading();
		onRefresh();
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_taglist);
		lv.setDividerHeight(2);
		
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		
		initTitleBar();		
		initPullToRefreshView();
		View headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview, null);
		lv.addHeaderView(headerView);
			mUserCommentAdapter=new UserCommentAdapter(this,MyCommentListActivity.this);
			lv.setAdapter(mUserCommentAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position>=1) {
					PetalkVo sayVo=(PetalkVo) mUserCommentAdapter.getItem(position-1);
					if (null!=sayVo) {
						Intent intent = new Intent();
						intent.setClass(MyCommentListActivity.this, DetailActivity.class);
						Constants.Detail_Sayvo=sayVo;
						startActivityForResult(intent, 1);
					}
				}
			
			}
		});
	
		
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(MyCommentListActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv,titleHeight) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		lv.setOnScrollListener(mScrollListener);


		mNullTip.initViewResid(R.drawable.comment_null_bg, R.drawable.comment_null_btn);
		mNullTip.setClickButtonListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCommentListActivity.this,MainActivity.class);
				intent.putExtra("tagindex",0);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(getIntent().getStringExtra("folderPath"));
		mTitleBar.setFinishEnable(true);
//		tvTitleRight = PublicMethod.addTitleRightText(this);
//		tvTitleRight.setText(R.string.release_0_title_right);
//		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		txt_Params.gravity = Gravity.CENTER;
//		txt_Params.leftMargin = 10;
//		LinearLayout layout = new LinearLayout(this);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		layout.setGravity(Gravity.CENTER_VERTICAL);
//		layout.addView(tvTitleRight,txt_Params);
//		layout.setTag(0);
//		layout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (UserManager.getSingleton().isLoginStatus()) {
//					Intent  intent=new Intent(getApplicationContext(),CameraActivity.class);
//					startActivity(intent);
//				}else {
//					Intent  intent=new Intent(getApplicationContext(),UserLogin_Activity.class);
//					startActivity(intent);
//				}
//			}
//		});
//		
//		mTitleBar.addRightView(layout);
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
				onLoadMore();
			}
		});
	}
	
	private void onGetDataCallback(List<PetalkVo> data,boolean isMore){
		if(isMore){
			if(data == null || data.size() == 0)
				PublicMethod.showToast(MyCommentListActivity.this, R.string.no_more);
			mUserCommentAdapter.addMore(data);
			mPullView.onFooterRefreshComplete();
		}else {
			mUserCommentAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
			if(!data.isEmpty())
				mNullTip.setVisibility(View.GONE);
			else
				mNullTip.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=mUserCommentAdapter&&mUserCommentAdapter.getCount()>0) {
			mUserCommentAdapter.notifyDataSetChanged();
		}
//		onRefresh();
	}
	
	/**
	 * 刷新操作
	 */
	public void onRefresh() {
		mPullView.showHeaderAnimation();
		netwrok(false);
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		netwrok(true);
	}

	/**
	 * 联网获取数据
	 * @param pageIndex
	 * @param what
	 */
	private void netwrok(boolean isMore){
		String id = "";
		if (isMore) {
			if (mUserCommentAdapter.getCount() > 0) {
				id = ((PetalkVo)mUserCommentAdapter.getItem(mUserCommentAdapter.getCount() - 1)).getId();
				mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, id, 10,isMore);
			}
		}else {
			mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, "", 10,isMore);
		}
		
	}
	
	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if (null!=mScrollListener) {
			mScrollListener.release();
		}
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
		List<PetalkVo> original_Sayvos = null;
		try {
			original_Sayvos = JsonUtils.getList( bean.getValue(), PetalkVo.class);
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		onGetDataCallback(original_Sayvos,bean.isIsMore());
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==1) {
			onRefresh();
		}
	}
}
