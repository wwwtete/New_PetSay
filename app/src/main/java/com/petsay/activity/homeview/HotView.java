package com.petsay.activity.homeview;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.activity.main.MainActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetalkVo;

public class HotView extends RelativeLayout implements OnItemClickListener, NetCallbackInterface{

	private MainActivity mContext;
	private ArrayList<PetalkVo> sayVos;
	private ListView lvHot;

//	private SayModule mSayModule;
//	private SayData mSayData;
	private SayDataNet mSayDataNet;
	private HotListViewAdapter mHotListViewAdapter;
	private PullToRefreshView pullToRefreshView;
	private GifListScrollListener mOnScrollListener;
	private int mPageIndex = 0;
	private ProgressDialog mDialog;
    private List<PetalkVo> allSayVos;

	public HotView(MainActivity context) {
		super(context);
		mContext=context;
		inflate(context, R.layout.hot_view, this);
		initView();
		mHotListViewAdapter=new HotListViewAdapter(mContext,context,true);
		lvHot.setAdapter(mHotListViewAdapter);
		initData();
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(getContext(), 60);
		mOnScrollListener = new GifListScrollListener(lvHot,titleHeight);
		lvHot.setOnScrollListener(mOnScrollListener);
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(context);
//		mSayModule=SayModule.getSingleton();
//		mSayData=new SayData(handler);
		onRefresh(true);
	}

	/**
	 * 刷新操作
	 * @param isNet true联网获取数据  false 刷新本地数据(用于关注等操作)
	 */
	public void onRefresh(boolean isNet) {
		if (isNet) {
			pullToRefreshView.showHeaderAnimation();
			mPageIndex = 0;
			netwrok(0,false);
		}else {
			mHotListViewAdapter.checkIsDeleted();
			mHotListViewAdapter.notifyDataSetChanged();
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
	private void netwrok(int pageIndex,boolean  isMore){
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkList(UserManager.getSingleton()
					.getActivePetInfo().getId(), pageIndex,
					Constants.SayListPageSize, isMore);
		} else
			mSayDataNet.petalkList("", pageIndex, Constants.SayListPageSize,isMore);
	}

	private void showLoading(){
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(getContext());
	}

	private void closeLoading(){
		PublicMethod.closeProgressDialog(mDialog, getContext());
	}

	private void initView() {
		pullToRefreshView=(PullToRefreshView) findViewById(R.id.pulltorefreshview);
		int bottom = PublicMethod.getDiptopx(getContext(), 50);
		pullToRefreshView.setBottomMargin(0-bottom);
		lvHot = (ListView) findViewById(R.id.lv_hot);
		lvHot.setOnItemClickListener(this);
		pullToRefreshView.setPullDownRefreshEnable(true);
		pullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				//				mSayModule.addListener(mSayData);
				//				if (UserManager.getSingleton().isLoginStatus()) {
				//					mSayModule.petalkList(mSayData, UserManager.getSingleton().getActivePetInfo().getId(), 0,  Constants.SayListPageSize);
				//				} else
				//					mSayModule.petalkList(mSayData, "", 0, Constants.SayListPageSize);
				onRefresh(true);
			}
		});

		pullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onLoadMore();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		PetalkVo sayVo=(PetalkVo) mHotListViewAdapter.getItem(position);
		if (null!=sayVo) {
			Intent intent = new Intent();
			//		intent.setClass(mContext, HotDetails_Activity.class);
			intent.setClass(mContext, DetailActivity.class);
			Constants.Detail_Sayvo=sayVo;
			mContext.startActivity(intent);
		}
		
	}
	
	/**
	 * 停止播放GIf
	 */
	public void stopGif(){
		GifViewManager.getInstance().stopGif();
	}

	public void release(){
		GifViewManager.getInstance().release();
	}

	private void initData(){
		try {
			sayVos=(ArrayList<PetalkVo>) DataFileCache.getSingleton().loadObject(Constants.HotListFile);
			allSayVos=sayVos;
			if (null!=sayVos&&!sayVos.isEmpty()) {
				mHotListViewAdapter.refreshData(sayVos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("获取本地热门缓存数据失败");
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKLIST:
			sayVos = (ArrayList<PetalkVo>) JsonUtils.parseList(bean.getValue(), PetalkVo.class);
			UserManager.getSingleton().addFocusAndStepByList(sayVos);
			
			if (bean.isIsMore()) {
				if (sayVos == null || sayVos.size() == 0)
					ToastUtiles.showDefault(getContext(), R.string.no_more);
				mHotListViewAdapter.addMore(sayVos);
				allSayVos.addAll(sayVos);
				pullToRefreshView.onFooterRefreshComplete();
			} else {
				
				
				allSayVos = sayVos;
				UserManager.getSingleton().focusMap.clear();
				mHotListViewAdapter.refreshData(sayVos);
				pullToRefreshView.onHeaderRefreshComplete();
				DataFileCache.getSingleton().asyncSaveData(Constants.HotListFile, sayVos);
			}
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKLIST:
			PublicMethod.showToast(mContext, "网络错误");
			if (error.isIsMore()) {

				pullToRefreshView.onFooterRefreshComplete();
			} else {
				pullToRefreshView.onHeaderRefreshComplete();
			}

			break;

		default:
			break;
		}
	}

}
