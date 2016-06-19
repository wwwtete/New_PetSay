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
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.adapter.MyPetalkListViewAdapter;
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
 * 我的说说
 * @author G
 *
 */
public class MyPetalkListActivity extends BaseActivity implements IAddShowLocationViewService,NetCallbackInterface{
	private ListView lv;
	private TitleBar mTitleBar;
	//	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	@InjectView(R.id.nulltip)
	private NullTipView mNullTip;

	private SayDataNet mSayDataNet;
	private MyPetalkListViewAdapter mAdapter;
	private HotListViewAdapter mHotListViewAdapter;
	private GifListScrollListener mScrollListener;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_saylist_layout);
		type=getIntent().getStringExtra("type");
		mSayDataNet=new SayDataNet();
		mSayDataNet.setTag(MyPetalkListActivity.this);
		mSayDataNet.setCallback(this);

		initView();
		showLoading();
		onRefresh(true);
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_taglist);


		mTitleBar=(TitleBar) findViewById(R.id.titlebar);

		initTitleBar();
		initPullToRefreshView();
		View headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview, null);
		lv.addHeaderView(headerView);
		if (type.equals(Constants.ORIGINAL)) {
			mAdapter = new MyPetalkListViewAdapter(MyPetalkListActivity.this,this);
			lv.setAdapter(mAdapter);
		}else {
			mHotListViewAdapter = new HotListViewAdapter(MyPetalkListActivity.this, this, true);
			lv.setAdapter(mHotListViewAdapter);
		}

		initNullTipView();


		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position>=1) {

					PetalkVo sayVo;
					if (type.equals(Constants.ORIGINAL)) {
						sayVo=(PetalkVo) mAdapter.getItem(position-1);
					}else {
						sayVo=(PetalkVo) mHotListViewAdapter.getItem(position-1);
					}
					if (null!=sayVo) {
						Intent intent = new Intent();
						intent.setClass(MyPetalkListActivity.this, DetailActivity.class);
						Constants.Detail_Sayvo=sayVo;
						startActivityForResult(intent, Constants.GotoDetailAcRequestCode);
					}
				}
			}
		});


		//图片距离顶部的距离
		int titleHeight = getResources().getDimensionPixelOffset(R.dimen.title_height);//PublicMethod.getDiptopx(MyPetalkListActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv,titleHeight,true) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		lv.setOnScrollListener(mScrollListener);

	}

	private void initNullTipView() {

		if(Constants.ORIGINAL.equals(type))
			mNullTip.initViewResid(R.drawable.petalk_null_bg,R.drawable.petalk_null_btn);
		else if(Constants.RELAY.equals(type))
			mNullTip.initViewResid(R.drawable.forward_null_bg,R.drawable.forward_null_btn);
		else if(Constants.FANS.equals(type))
			mNullTip.initViewResid(R.drawable.step_null_bg,R.drawable.step_null_btn);
		mNullTip.setClickButtonListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyPetalkListActivity.this,MainActivity.class);
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
				onRefresh(true);
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
				PublicMethod.showToast(MyPetalkListActivity.this, R.string.no_more);
			if (type.equals(Constants.ORIGINAL)) {
				mAdapter.addMore(data);
			}else {
				mHotListViewAdapter.addMore(data);
			}
			mPullView.onFooterRefreshComplete();
		}else {
			if (type.equals(Constants.ORIGINAL)) {
				mAdapter.refreshData(data);
			}else {
				mHotListViewAdapter.refreshData(data);
			}
//			mAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
			if(!data.isEmpty()){
				mNullTip.setVisibility(View.GONE);
			}else {
				mNullTip.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (type.equals(Constants.ORIGINAL)) {
			if (null!=mAdapter&&mAdapter.getCount()>0) {
				mAdapter.notifyDataSetChanged();
			}
		}else if (null!=mHotListViewAdapter&&mHotListViewAdapter.getCount()>0) {
			mHotListViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==Constants.GotoDetailAcRequestCode) {
			onRefresh(false);
		}
	}

	/**
	 * 刷新操作
	 */
	public void onRefresh(boolean isNet) {
		if (isNet) {
			mPullView.showHeaderAnimation();
			netwrok(false);
		}else {
			if (type.equals(Constants.ORIGINAL)) {
				mAdapter.checkIsDeleted();
				mAdapter.notifyDataSetChanged();
			}else if (null!=mHotListViewAdapter&&mHotListViewAdapter.getCount()>0) {
				mHotListViewAdapter.checkIsDeleted();
				mHotListViewAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		netwrok(true);
	}

	/**
	 * 联网获取数据
	 * @param what
	 * @param what
	 */
	private void netwrok(boolean isMore){
		String id = "";
		if (type.equals(Constants.ORIGINAL)) {
			if (isMore) {
				if (mAdapter.getCount() > 0) {
					id = ((PetalkVo)mAdapter.getItem(mAdapter.getCount() - 1)).getId();
					mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, id, 10,isMore);
				}
			}else {
				mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, id, 10,isMore);
			}

		}else {
			if (isMore) {
				if (mHotListViewAdapter.getCount() > 0) {
					id = ((PetalkVo)mHotListViewAdapter.getItem(mHotListViewAdapter.getCount() - 1)).getId();
					mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, id, 10,isMore);
				}
			}else {
				mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),UserManager.getSingleton().getActivePetId(),type, id, 10,isMore);
			}
		}
	}

	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
//		if (null!=mScrollListener) {
//			mScrollListener.release();
//		}
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
}
