package com.petsay.activity.petalk;

import java.util.List;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.petalk.PetalkVo;
/**
 * 全部原创
 * @author G
 *
 */
public class ALLSayListActivity extends BaseActivity implements IAddShowLocationViewService, NetCallbackInterface{
	private ListView lv;
	private ImageView img;
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	
	private SayDataNet mSayDataNet;
	
//	private List<SayVo> mSayVos;
	private HotListViewAdapter mAdapter;
	private GifListScrollListener mScrollListener;
	private String mPageIndex ="";
	public static int FROM_OTHER=0;
	public static int FROM_PUSH=1;
	private SquareVo mSquareVo;
//	private int from;
	/**
	 * CHANNEL("3", "频道"),PETBREED("4", "宠物品种"),ALL("5", "全部原创");
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_saylist_layout);
		mSquareVo=(SquareVo) getIntent().getSerializableExtra("squareVo");
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		initView();
		showLoading();
		onRefresh(true);
		
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_taglist);
		mAdapter = new HotListViewAdapter(ALLSayListActivity.this,this,true);
		
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		
		initTitleBar();		
		initPullToRefreshView();
		View headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview, null);
		img=(ImageView) headerView.findViewById(R.id.img_tag_intro);
		lv.addHeaderView(headerView);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position>=1) {
					PetalkVo sayVo=(PetalkVo) mAdapter.getItem(position-1);
					if (null!=sayVo) {
						Intent intent = new Intent();
						intent.setClass(ALLSayListActivity.this, DetailActivity.class);
						Constants.Detail_Sayvo=sayVo;
						startActivityForResult(intent, Constants.GotoDetailAcRequestCode);
					}
				}
			
			}
		});
	
		
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(ALLSayListActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv,titleHeight) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		lv.setOnScrollListener(mScrollListener);
		
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(mSquareVo.getTitle());
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
				PublicMethod.showToast(ALLSayListActivity.this, R.string.no_more);
			mAdapter.addMore(data);
			mPullView.onFooterRefreshComplete();
		}else {
			mAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null!=mAdapter&&mAdapter.getCount()>0) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			mPageIndex = "";
			mPullView.showHeaderAnimation();
			netwrok(mPageIndex, false);
		}else {
			mAdapter.checkIsDeleted();
			mAdapter.notifyDataSetChanged();
		}
		
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		PetalkVo sayVo=(PetalkVo) mAdapter.getItem(mAdapter.getCount()-1);
//		mPageIndex =mAdapter.get;
		netwrok(sayVo.getId(),true);
	}

	/**
	 * 联网获取数据
	 * @param what
	 */
	private void netwrok(String  id,boolean isMore){
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkAll( UserManager.getSingleton().getActivePetId(), id, 10, isMore);
		} else
			mSayDataNet.petalkAll("",id, 10, isMore);
		
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
		
//		if (from==FROM_PUSH) {
//			ActivityTurnToManager.getSingleton().returnMainActivity(ChannelSayListActivity.this);
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
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKALL:
			String jsonStr=bean.getValue();
			List<PetalkVo> sayVos = null;
			try {
				sayVos = JsonUtils.getList(jsonStr, PetalkVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			lv.setAdapter(new TagSayListAdapter(getApplicationContext(), mSayVos));
			onGetDataCallback(sayVos,bean.isIsMore());
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKALL:
			if(error.isIsMore()){
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
