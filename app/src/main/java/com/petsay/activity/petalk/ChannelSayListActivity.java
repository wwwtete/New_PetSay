package com.petsay.activity.petalk;

import java.util.List;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
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
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonParse;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.petalk.ChannelDTO;
import com.petsay.vo.petalk.PetalkVo;
/**
 * 频道，品种，全部
 * @author G
 *
 */
public class ChannelSayListActivity extends BaseActivity implements IAddShowLocationViewService, NetCallbackInterface{
	private ListView lv;
	private ImageView img;
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	
	private SayDataNet mSayDataNet;
	
	private HotListViewAdapter mAdapter;
	private GifListScrollListener mScrollListener;
	private int mPageIndex = 0;
	public static int FROM_OTHER=0;
	public static int FROM_PUSH=1;
	private SquareVo mSquareVo;
	
//	private int from;
	/**
	 * CHANNEL("3", "频道"),PETBREED("4", "宠物品种"),ALL("5", "全部原创");
	 */
	private int type;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_saylist_layout);
		mSquareVo=(SquareVo) getIntent().getSerializableExtra("squareVo");
		type=mSquareVo.getHandleType();
//		mSayData=new SayData(handler);
//		mSayModule=SayModule.getSingleton();
		
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(ChannelSayListActivity.this);
		
		
//		if (from==FROM_PUSH) {
//			String tagId=getIntent().getStringExtra("id");
//			showLoading();
//			mSayModule.addListener(mSayData);
//			mSayModule.tagOne(mSayData,tagId);
			
//		}else {
//			tagInfo=(TagInfo) getIntent().getSerializableExtra("tag");
			initView();
			showLoading();
			onRefresh(true);
//		}
		
		
		
	}
	protected void initView(){
		super.initView();
		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_taglist);
		mAdapter = new HotListViewAdapter(ChannelSayListActivity.this,this,true);
		
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
						intent.setClass(ChannelSayListActivity.this, DetailActivity.class);
						Constants.Detail_Sayvo=sayVo;
						startActivityForResult(intent, Constants.GotoDetailAcRequestCode);
//						startActivity(intent);
					}
				}
			
			}
		});
	
		
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(ChannelSayListActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv,titleHeight) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		lv.setOnScrollListener(mScrollListener);
		if (type==3) {
			mSayDataNet.channelOne(mSquareVo.getKey());
		}
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(mSquareVo.getTitle());
		mTitleBar.setFinishEnable(true);
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
	
	private void onGetDataCallback(List<PetalkVo> data,boolean isMore ){
		if(isMore){
			if(data == null || data.size() == 0)
				PublicMethod.showToast(ChannelSayListActivity.this, R.string.no_more);
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
			mPageIndex = 0;
			mPullView.showHeaderAnimation();
			netwrok(0,false);
		}else {
			mAdapter.checkIsDeleted();
			mAdapter.notifyDataSetChanged();
		}
		
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		mPageIndex ++;
		netwrok(mPageIndex,true);
	}

	/**
	 * 联网获取数据
	 * @param pageIndex
	 * @param what
	 */
	private void netwrok(int pageIndex,boolean isMore ){
//		mSayModule.addListener(mSayData);
		switch (type) {
		case 3:
			if (UserManager.getSingleton().isLoginStatus()) {
				mSayDataNet.petalkChannel( mSquareVo.getKey(), UserManager.getSingleton().getActivePetId(), pageIndex, 10,isMore);
			}else
				mSayDataNet.petalkChannel( mSquareVo.getKey(), "",pageIndex, 10,isMore);
			break;
		case 4:
			if (UserManager.getSingleton().isLoginStatus()) {
				mSayDataNet.petalkPetBreed( mSquareVo.getKey(), UserManager.getSingleton().getActivePetId(), pageIndex, 10,isMore);
			}else
				mSayDataNet.petalkPetBreed( mSquareVo.getKey(), "",pageIndex, 10,isMore);
			break;
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
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_CHANNELONE:
			final ChannelDTO channel=JsonUtils.resultData(bean.getValue(), ChannelDTO.class);
			
			if (null==channel.getBgUrl()||channel.getBgUrl().trim().equals("")) {
				img.setVisibility(View.GONE);
			}else {
				img.setVisibility(View.VISIBLE);
				ImageLoaderHelp.displayContentImage(channel.getBgUrl(), img);
			}
			String detailUrl=channel.getDetailUrl();
			if (null!=detailUrl&&!detailUrl.trim().equals("")) {
				img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent();
						intent.putExtra("folderPath", channel.getName());
						intent.putExtra("url", channel.getDetailUrl());
						intent.setClass(getApplicationContext(), WebViewActivity.class);
						startActivity(intent);
						
					}
				});
			}
//			initView();
//			showLoading();
//			onRefresh();
			break;
		case RequestCode.REQUEST_PETALKTAGLIST:
			List<PetalkVo> sayVos = null;
			try {
				sayVos = JsonUtils.parseList(bean.getValue(), PetalkVo.class);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			onGetDataCallback(sayVos,bean.isIsMore());
			break;
		case RequestCode.REQUEST_PETALKTAGLISTTIMELINE:
			sayVos=null;
			try {
				sayVos=JsonUtils.getList(bean.getValue(), PetalkVo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			onGetDataCallback(sayVos,bean.isIsMore());
			break;
		case RequestCode.REQUEST_PETALKCHANNEL:
		case RequestCode.REQUEST_PETALKPETBREED:
			String jsonStr=JsonParse.getSingleton().parseListString(bean.getValue());
			List<PetalkVo> tempSayVos = null;
			try {
				tempSayVos = JsonUtils.getList(jsonStr, PetalkVo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			lv.setAdapter(new TagSayListAdapter(getApplicationContext(), mSayVos));
			onGetDataCallback(tempSayVos,bean.isIsMore());
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKCHANNEL:
		case RequestCode.REQUEST_PETALKPETBREED:
			if(error.isIsMore()){
				mPullView.onFooterRefreshComplete();
			}else {
				mPullView.onHeaderRefreshComplete();
			}
			break;
		}
		
	}
}
