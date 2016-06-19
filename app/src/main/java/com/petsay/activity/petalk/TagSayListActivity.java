package com.petsay.activity.petalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TabTitleLayoutView;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetTagVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * 标签
 * @author G
 *
 */
public class TagSayListActivity extends BaseActivity implements OnClickListener, IAddShowLocationViewService, NetCallbackInterface{
	private ListView lv;
	private ImageView img;
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private LinearLayout layoutTab;
	//最热，最新
	private TabTitleLayoutView mViewHottest,mViewLatest;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	
	private SayDataNet mSayDataNet;
	
//	private SayData mSayData;
//	private SayModule mSayModule;
	
	private PetTagVo tagInfo;
//	private List<SayVo> mSayVos;
	private HotListViewAdapter mHottestAdapter;
	private HotListViewAdapter mLatestAdapter;
	private GifListScrollListener mScrollListener;
	private int mPageIndex = 0;
	public static int FROM_OTHER=0;
	public static int FROM_PUSH=1;
	private int from;
	private static final int PAGE_SIZE=10;
	/**
	 * 0最新，1最热
	 */
	private int mTabIndex=1;
    private PopupWindow mCancelMenu;
//	private int 
	
	
//	private Handler handler=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			closeLoading();
//			switch (msg.what) {
//			case SayData.GET_TAG_LIST_SUCCESS:
//				
//				break;
//			case SayData.GET_TAG_LIST_FAILED:
//				mSayModule.removeListener(mSayData);
//				if(msg.arg1 == 0){
//					mPullView.onHeaderRefreshComplete();
//				}else if (msg.arg1==-1) {
//					NetworkManager.getSingleton().canclePullRefresh(TagSayListActivity.this,mPullView );
//				}else {
//					mPullView.onFooterRefreshComplete();
//				}
//				break;
//			case SayData.GET_TAG_LATEST_LIST_SUCCESS:
//				
//				break;
//			case SayData.GET_TAG_LATEST_LIST_FAILED:
//				mSayModule.removeListener(mSayData);
//				if(msg.arg1 == 0){
//					mPullView.onHeaderRefreshComplete();
//				}else if (msg.arg1==-1) {
//					NetworkManager.getSingleton().canclePullRefresh(TagSayListActivity.this,mPullView );
//				}else {
//					mPullView.onFooterRefreshComplete();
//				}
//				break;
//			case SayData.GET_TAG_SUCCESS:
//				mSayModule.removeListener(mSayData);
//				
//				break;
//			case SayData.GET_TAG_FAILED:
//				break;
//			}
//		};
//	};



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_saylist_layout);
		from=getIntent().getIntExtra("from", FROM_OTHER);
//		mSayData=new SayData(handler);
//		mSayModule=SayModule.getSingleton();
//		if (from==FROM_PUSH) {
			String tagId=getIntent().getStringExtra("id");
			showLoading();
//			
			mSayDataNet=new SayDataNet();
			mSayDataNet.setCallback(this);
			mSayDataNet.setTag(this);
			super.initView();
        mPullView.showHeaderAnimation();
        mSayDataNet.tagOne(tagId);
//		}else {
//			tagInfo=(TagInfo) getIntent().getSerializableExtra("tag");
//			initView();
//			showLoading();
//			onRefresh();
//		}
		
		
		
	}
	protected void initView(){
		lv=(ListView) findViewById(R.id.lv_taglist);
		mHottestAdapter = new HotListViewAdapter(TagSayListActivity.this,this,true);
		mLatestAdapter =new HotListViewAdapter(TagSayListActivity.this,this,true);
	
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		
		initTitleBar();		
		initPullToRefreshView();
		View headerView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tag_list_headerview, null);
		layoutTab=(LinearLayout)headerView. findViewById(R.id.layout_tab);
		mViewHottest=(TabTitleLayoutView)headerView. findViewById(R.id.layout_hottest);
		mViewLatest =(TabTitleLayoutView)headerView. findViewById(R.id.layout_latest);
		mViewHottest.setTitle("精华");
		mViewLatest.setTitle("最新");
		mViewLatest.setCountVisibility(View.GONE);
		mViewHottest.setCountVisibility(View.GONE);
//		layoutTab.setBackgroundColor(Color.WHITE);
		layoutTab.setVisibility(View.VISIBLE);
		changeTabLayoutBg();
		
		img=(ImageView) headerView.findViewById(R.id.img_tag_intro);
		lv.addHeaderView(headerView);
		lv.setAdapter(mHottestAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position>=1) {
					PetalkVo sayVo;
					if (mTabIndex==0) {
						sayVo=(PetalkVo) mLatestAdapter.getItem(position-1);
					}else {
						sayVo=(PetalkVo) mHottestAdapter.getItem(position-1);
					}
					if (null!=sayVo) {
						Intent intent = new Intent();
						intent.setClass(TagSayListActivity.this, DetailActivity.class);
						Constants.Detail_Sayvo=sayVo;
						startActivityForResult(intent, Constants.GotoDetailAcRequestCode);
					}
				}
			
			}
		});
		if (null==tagInfo.getBgUrl()||tagInfo.getBgUrl().trim().equals("")) {
			img.setVisibility(View.GONE);
		}else {
			img.setVisibility(View.VISIBLE);
//			ImageLoader.getInstance().displayImage(tagInfo.getBgUrl(), img, Constants.contentPetImgOptions);
			
//			PicassoUtile.loadImgByWidthScale(TagSayListActivity.this, tagInfo.getBgUrl(), img,PublicMethod.getDisplayWidth(TagSayListActivity.this));
			ImageLoaderHelp.displayContentImage(tagInfo.getBgUrl(), img);
		}
		String detailUrl=tagInfo.getDetailUrl();
		if (null!=detailUrl&&!detailUrl.trim().equals("")) {
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.putExtra("folderPath", tagInfo.getName());
					intent.putExtra("url", tagInfo.getDetailUrl());
					intent.setClass(getApplicationContext(), WebViewActivity.class);
					startActivity(intent);
					
				}
			});
		}
		
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(TagSayListActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv,titleHeight) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		lv.setOnScrollListener(mScrollListener);
		mViewHottest.setOnClickListener(this);
		mViewLatest.setOnClickListener(this);
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(tagInfo.getName());
		mTitleBar.setFinishEnable(true);
		tvTitleRight = new TextView(this);
        tvTitleRight.setTextColor(Color.WHITE);
		tvTitleRight.setText(R.string.release_0_title_right);
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(tvTitleRight,txt_Params);
		layout.setTag(0);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getSingleton().isLoginStatus()) {
					showCustomMenu();
				}else {
					Intent  intent=new Intent(getApplicationContext(),UserLogin_Activity.class);
					startActivity(intent);
				}
			}
		});
		
		mTitleBar.addRightView(layout);
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
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_hottest:
			if (mTabIndex==0) {
				mTabIndex=1;
				changeTabLayoutBg();
				if (null==mHottestAdapter||mHottestAdapter.getCount()==0) {
					onRefresh(true);
				}else {
					lv.setAdapter(mHottestAdapter);
				}
			}
			break;
		case R.id.layout_latest:
			if (mTabIndex==1) {
				mTabIndex=0;
				changeTabLayoutBg();
				if (null==mLatestAdapter||mLatestAdapter.getCount()==0) {
					onRefresh(true);
				}else {
					lv.setAdapter(mLatestAdapter);
				}
			}
			break;
            case R.id.tv_image:
                jumpCamerActivity(1);
                break;
            case R.id.tv_audio:
                jumpCamerActivity(0);
                break;
            case R.id.btn_cancel:
                hidenCustomMenu();
                break;
		default:
			break;
		}
		
	}

    private void jumpCamerActivity(int model){
        hidenCustomMenu();
        Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
        intent.putExtra("model",model);
        intent.putExtra("tag",tagInfo);
        startActivity(intent);
    }
	
	private void onGetDataCallback(List<PetalkVo> data,boolean isMore){
		if (mTabIndex==0) {
			
			if(isMore){
				if(data == null || data.size() == 0)
					PublicMethod.showToast(TagSayListActivity.this, R.string.no_more);
				mLatestAdapter.addMore(data);
				mPullView.onFooterRefreshComplete();
			}else {
				lv.setAdapter(mLatestAdapter);
				mLatestAdapter.refreshData(data);
				mPullView.onHeaderRefreshComplete();
			}
		}else {
			
			if(isMore){
				if(data == null || data.size() == 0)
					PublicMethod.showToast(TagSayListActivity.this, R.string.no_more);
				mHottestAdapter.addMore(data);
				mPullView.onFooterRefreshComplete();
			}else {
				lv.setAdapter(mHottestAdapter);
				mHottestAdapter.refreshData(data);
				mPullView.onHeaderRefreshComplete();
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (null!=mHottestAdapter&&mHottestAdapter.getCount()>0) {
			mHottestAdapter.notifyDataSetChanged();
		}
		if (null!=mLatestAdapter&&mLatestAdapter.getCount()>0) {
			mLatestAdapter.notifyDataSetChanged();
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
			String[] a=tagInfo.getCtrl().split("");
			
			if (a[1].equals("1")&&a[2].equals("1")) {
				layoutTab.setVisibility(View.VISIBLE);
			}else if(a[1].equals("1")){
				layoutTab.setVisibility(View.GONE);
				mTabIndex=1;
			}else if (a[2].equals("1")) {
				layoutTab.setVisibility(View.GONE);
				mTabIndex=0;
			}
			if (mTabIndex==1) {
				mPageIndex = 0;
				netwrokHottest(0,false);
			}else {
				netwrokLatest("",false);
			}
		}else {
			if (mTabIndex==1) {
//				mHottestAdapter
				mHottestAdapter.checkIsDeleted();
				mHottestAdapter.notifyDataSetChanged();
			}else {
				mLatestAdapter.checkIsDeleted();
				mLatestAdapter.notifyDataSetChanged();
			}
		}
		
		
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		if (mTabIndex==1) {
			mPageIndex ++;
			netwrokHottest(mPageIndex,true);
		}else {
			mPageIndex =0;
            String id = "";
            if(mLatestAdapter.getCount() > 0){
                id = ((PetalkVo)mLatestAdapter.getItem(mLatestAdapter.getCount()-1)).getId();
            }
			netwrokLatest(id,true);
		}
		
	}

	/**
	 * 联网获取数据   最热
	 * @param pageIndex
	 * @param isMore
	 */
	private void netwrokHottest(int pageIndex,boolean isMore){
		//获取最新与最热说说列表传值接口不一样 最新要传id，最热要传页数索引
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkTagList( tagInfo.getId(), UserManager.getSingleton().getActivePetInfo().getId(), pageIndex, PAGE_SIZE,isMore);
		}else
			mSayDataNet.petalkTagList(tagInfo.getId(), "",pageIndex, PAGE_SIZE,isMore);
	}
	
	
	/**
	 * 联网获取数据     最新
	 * @param id
	 * @param isMore
	 */
	private void netwrokLatest(String id,boolean isMore){
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkTagListTimeline( tagInfo.getId(), UserManager.getSingleton().getActivePetInfo().getId(), id, PAGE_SIZE, isMore);
		}else
			mSayDataNet.petalkTagListTimeline( tagInfo.getId(), "", id, PAGE_SIZE, isMore);
		
	}
	
	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		closeLoading();
		if (null!=mScrollListener) {
			mScrollListener.release();
		}
		
		if (from==FROM_PUSH) {
			ActivityTurnToManager.getSingleton().returnMainActivity(TagSayListActivity.this);
		}
		super.onDestroy();
	}
	
	@Override
	public View getParentView() {
		return mLayoutRoot;
	}
	@Override
	public Activity getActivity() {
		return this;
	}
	
	/**
	 * 修改列表tab背景
	 */
	public void changeTabLayoutBg() {
		if (mTabIndex==0) {
			mViewLatest.setBackgroundResource(R.drawable.tag_select_tab_r);
			mViewLatest.setTextColor(getResources().getColor(R.color.select_color));
			mViewHottest.setBackgroundResource(R.drawable.tag_default_tab_l);
			mViewHottest.setTextColor(Color.GRAY);
		}else {
			mViewHottest.setBackgroundResource(R.drawable.tag_select_tab_l);
			mViewHottest.setTextColor(getResources().getColor(R.color.select_color));
//			mViewHottest.setTextColor(getSkinManager().getColor(getString(R.string.usercenter_selected_text_color)));
			mViewLatest.setBackgroundResource(R.drawable.tag_default_tab_r);
			mViewLatest.setTextColor(Color.GRAY);
		}
	}

    private void showCustomMenu(){
        hidenCustomMenu();
        View view = getLayoutInflater().inflate(R.layout.menu_select_publish_model, null);
        view.findViewById(R.id.tv_image).setOnClickListener(this);
        view.findViewById(R.id.tv_audio).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCancelMenu = new PopupWindow(view,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCancelMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        mCancelMenu.setFocusable(true);
        mCancelMenu.setBackgroundDrawable(new BitmapDrawable());
        mCancelMenu.setAnimationStyle(R.anim.bottom_in);
        mCancelMenu.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);
    }

    private void hidenCustomMenu(){
        if(mCancelMenu != null)
            mCancelMenu.dismiss();
    }
	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_TAGONE:
			tagInfo=JsonUtils.resultData(bean.getValue(), PetTagVo.class);
			initView();
			showLoading();
			onRefresh(true);
			break;
		case RequestCode.REQUEST_PETALKTAGLIST:
			List<PetalkVo> sayVos = null;
			try {
				sayVos = JsonUtils.parseList(bean.getValue(), PetalkVo.class);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			onGetDataCallback(sayVos,bean.isIsMore());
			break;
		case RequestCode.REQUEST_PETALKTAGLISTTIMELINE:
			sayVos=null;
			try {
				sayVos=JsonUtils.getList(bean.getValue(), PetalkVo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		mPullView.onComplete(error.isIsMore());
		switch (requestCode) {
		case RequestCode.REQUEST_TAGONE:
			break;
		case RequestCode.REQUEST_PETALKTAGLIST:
			
			break;
		case RequestCode.REQUEST_PETALKTAGLISTTIMELINE:
			
			break;
		default:
			break;
		}
	}
}
