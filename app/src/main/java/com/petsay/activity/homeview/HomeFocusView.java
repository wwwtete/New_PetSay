package com.petsay.activity.homeview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.story.UploadStoryView;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.homeview.adapter.HomeFocusListViewAdapter;
import com.petsay.application.PublishStoryManager;
import com.petsay.application.PublishTalkManager;
import com.petsay.cache.DataFileCache;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.UploadView;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.PublishPetSayNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetalkVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.Share;

public class HomeFocusView extends RelativeLayout implements NetCallbackInterface ,PlatformActionListener,OnClickListener{

	private ListView lvFocus;
	
//	private ImageView imgHeaderTop,imgSexTop;
//	private TextView tvNameTop,tvPublishTimeTop;
//	private RelativeLayout rlayoutInfoTop;

	private Context mContext;
	private LinearLayout mViewLayout;
//	private PublishPetService mService;
	private PublishPetSayNet mPetSayNet;
	private HomeFocusListViewAdapter mAdapter;
	private GifListScrollListener mScrollListener;
	private PullToRefreshView mPullView;
	private ImageView mIvLogin;
	private LinearLayout mLayoutLogin;
	private PetalkVo sharePetalkVo;
	private SayDataNet mSayDataNet;
	
	
	
	private ProgressDialog mDialog;
	public HomeFocusView(Context context,PublishPetSayNet petSayNet) {
		super(context);
		mContext=context;
		this.mPetSayNet = petSayNet;
		mPetSayNet.setCallback(this);
		inflate(context, R.layout.home_focus_view, this);
		initView();
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(context);
	}

	public void getFocusList(){
		initData();
		onRefresh();
	}
	
	private void initData(){
		try {
			List<PetalkVo> sayVos=(ArrayList<PetalkVo>) DataFileCache.getSingleton().loadObject(Constants.FocusListFile);
			if (null!=sayVos&&!sayVos.isEmpty()) {
				mAdapter.refreshData(sayVos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		lvFocus = (ListView) findViewById(R.id.lv_focus);
//		imgHeaderTop=(ImageView) findViewById(R.id.img_header_top);
//		imgSexTop=(ImageView) findViewById(R.id.img_sex_top);
//		tvNameTop=(TextView) findViewById(R.id.tv_name_top);
//		tvPublishTimeTop=(TextView) findViewById(R.id.tv_publish_time_top);
//		rlayoutInfoTop=(RelativeLayout) findViewById(R.id.rlayout_info_top);
		mAdapter = new HomeFocusListViewAdapter(mContext, (com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService) mContext);
		lvFocus.setAdapter(mAdapter);
		mIvLogin = (ImageView) findViewById(R.id.iv_login);
		mIvLogin.setOnClickListener(this);
		mLayoutLogin = (LinearLayout) findViewById(R.id.layout_login);
		//图片距离顶部的距离
		int titleHeight = getResources().getDimensionPixelOffset(R.dimen.title_height);//PublicMethod.getDiptopx(getContext(), 50);
		mScrollListener = new GifListScrollListener(lvFocus,titleHeight,true){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//                onListViewScroll(firstVisibleItem);
            }
        };
		lvFocus.setOnScrollListener(mScrollListener);
		mViewLayout = (LinearLayout) findViewById(R.id.layout_uploadlist);
		initPullToRefreshView();
		lvFocus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				PetalkVo sayVo=(PetalkVo) mAdapter.getItem(position);
		if (null!=sayVo) {
			Intent intent = new Intent();
			intent.setClass(mContext, DetailActivity.class);
			Constants.Detail_Sayvo=sayVo;
			mContext.startActivity(intent);
		}
			}
		});

//		lvFocus.setOnGroupClickListener(new OnGroupClickListener() {
//			
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v,int groupPosition, long id) {
//				
//				return true;
//			}
//		});
//		lvFocus.setOnChildClickListener(new OnChildClickListener() {
//			
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
//				PetalkVo sayVo=(PetalkVo) mAdapter.getGroup(groupPosition);
//				if (null!=sayVo) {
//					Intent intent = new Intent();
//					intent.setClass(mContext, DetailActivity.class);
//					Constants.Detail_Sayvo=sayVo;
//					mContext.startActivity(intent);
//				}
//				return true;
//			}
//		});
	}

    /*private void onListViewScroll(int firstVisibleItem){
        PetalkVo petalkVo=(PetalkVo) mAdapter.getItem(firstVisibleItem);
        if (null==petalkVo) {
            rlayoutInfoTop.setVisibility(View.GONE);
        }else {
            rlayoutInfoTop.setVisibility(View.VISIBLE);
            final PetVo petVo=petalkVo.getPet();
            ImageLoaderHelp.displayHeaderImage(petalkVo.getPetHeadPortrait(), imgHeaderTop);
            if (petalkVo.getType().equals(Constants.RELAY)&& null != petalkVo.getComment()) {
                tvPublishTimeTop.setText(PublicMethod.formatTimeToString(petalkVo.getRelayTime(), "yyyy-MM-dd kk:mm"));
            } else {
                tvPublishTimeTop.setText(PublicMethod.formatTimeToString(petalkVo.getCreateTime(), "yyyy-MM-dd kk:mm"));
//						holder.rLayoutForward.setVisibility(View.GONE);
            }

            tvNameTop.setText(petVo.getNickName());

            if (petVo.getGender()==0) {
                imgSexTop.setVisibility(View.VISIBLE);
                imgSexTop.setImageResource(R.drawable.female);

            }else if (petVo.getGender()==1) {
                imgSexTop.setVisibility(View.VISIBLE);
                imgSexTop.setImageResource(R.drawable.male);
            }else {
                imgSexTop.setVisibility(View.GONE);
            }

            imgHeaderTop.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,petVo);
                }
            });
        }

    }*/

	private void onGetDataCallback(List<PetalkVo> data,boolean isMore){
		if(isMore){
			if(data == null || data.size() == 0)
				ToastUtiles.showDefault(getContext(), R.string.no_more);
			mAdapter.addMore(data);
			mPullView.onFooterRefreshComplete();
		}else {
			mAdapter.refreshData(data);
			mPullView.onHeaderRefreshComplete();
			DataFileCache.getSingleton().asyncSaveData(Constants.FocusListFile, (Serializable) data);
		}
		expandGroup();
	}
	
	
	/**
	 * expandlistview默认展开
	 */
	private void expandGroup(){
//		for (int i = 0; i < mAdapter.getCount(); i++) {
//			lvFocus.expandGroup(i);
//		}
	}

	/**
	 * 停止播放Gif
	 */
	public void stopGif(){
		GifViewManager.getInstance().stopGif();
	}

	public void release(){
		GifViewManager.getInstance().release();
	}
	
	public void onHiddenChanged(boolean hidden) {
		if(!hidden)
			onRefresh();
	}

	public void refreshUploadList(){
        checkPublishStory();
        checkPublishPetalk();
//		if(PublicMethod.getAPNType(getContext()) == -1){
//			PublicMethod.showToast(getContext(), "请检查网络是否可用");
//		}
//		view.startUpload();

//		UploadView view = Constants.UPLOAD_VIEWS.pollFirst();
//		if(view == null)
//			return;
//		mViewLayout.addView(view);
//		view.setOnUploadListener(new UploadViewListener() {
//
//			@Override
//			public void onUploadFinish(UploadView view, boolean isSuccess) {
//				if(isSuccess){
//					mViewLayout.removeView(view);
//					publishPet(view);
//				}
//			}
//
//			@Override
//			public void onCancelUpload(UploadView view) {
//				mViewLayout.removeView(view);
//                Constants.UPLOAD_VIEWS.remove(view);
//			}
//		});
//		if(PublicMethod.getAPNType(getContext()) == -1){
//			PublicMethod.showToast(getContext(), "请检查网络是否可用");
//		}
//		view.startUpload();
	}

    private void checkPublishPetalk() {
        UploadPetalkView view = (UploadPetalkView) PublishTalkManager.getInstance().popupUploadView();//Constants.UPLOAD_VIEWS.pollFirst();
        if(view == null)
            return;
        mViewLayout.addView(view);
        view.setOnUploadListener(new UploadView.UploadViewListener(){

            @Override
            public void onUploadFinish(UploadView view, boolean isSuccess) {
                if(isSuccess){
                    mViewLayout.removeView(view);
                    PublishTalkManager.getInstance().removeUploadView((UploadPetalkView) view);
                    publishPet((UploadPetalkView) view);
                }
            }

            @Override
            public void onCancelUpload(UploadView view) {
                mViewLayout.removeView(view);
                PublishTalkManager.getInstance().removeUploadView((UploadPetalkView) view);
//                Constants.UPLOAD_VIEWS.remove(view);
            }
        });
    }

    protected void publishPet(UploadPetalkView view){
        if(view == null || view.getPublishParam() == null){
            PublicMethod.showToast(getContext(),"发布失败！请重试");
            return;
        }
        PublishTalkParam dto = view.getPublishParam();
        dto.photoUrl = Constants.DOWNLOAD_SERVER +view.getBitmapPath();
        dto.thumbUrl = Constants.DOWNLOAD_SERVER +view.getThumPath();
        if(dto.model == 0)
            dto.audioUrl = Constants.DOWNLOAD_SERVER +view.getVoicePath();
        mPetSayNet.publishPetTalk(dto);
        view.release();
        view = null;
    }


    /**
     * 检查是否有故事要发布
     */
    private void checkPublishStory() {
        UploadView view = PublishStoryManager.getInstance().popupUploadView();
        if(view == null){
            return;
        }
        mViewLayout.addView(view);
        view.setOnUploadListener(new UploadView.UploadViewListener(){

            @Override
            public void onUploadFinish(UploadView view, boolean isSuccess) {
                PetsayLog.e("[onUploadFinish]");
                if(isSuccess)
                    mPetSayNet.publishStory(((UploadStoryView) view).getPublishParams(), view);
            }

            @Override
            public void onCancelUpload(UploadView view) {
                mViewLayout.removeView(view);
            }
        });
    }

    private void initPullToRefreshView(){
		mPullView = (PullToRefreshView) findViewById(R.id.pulltorefreshview);
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

	/**
	 * 刷新操作
	 */
	public void onRefresh() {
		mPullView.showHeaderAnimation();
		netwrok("", false);
	}

	/**
	 * 上拉加载更多操作
	 */
	public void onLoadMore() {
		Object obj = mAdapter.getItem(mAdapter.getCount()-1);
		if(obj != null){
			netwrok(((PetalkVo)obj).getId(),true);
		}
	}

	/**
	 * 联网获取数据
	 * @param petId
	 * @param isMore
	 */
	private void netwrok(String petId,boolean isMore){
		if (UserManager.getSingleton().isLoginStatus()) {
			mLayoutLogin.setVisibility(View.GONE);
		    mSayDataNet.petalkFocusList(UserManager.getSingleton().getActivePetInfo().getId(), petId, 10,isMore);
		}else {
			mPullView.onHeaderRefreshComplete();
			mPullView.onFooterRefreshComplete();
			mAdapter.clear();
			mLayoutLogin.setVisibility(View.VISIBLE);
//			PublicMethod.showToast(mContext, "还未登陆");
		}
	}

	private void showLoading(){
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(getContext());
	}

	private void closeLoading(){
		PublicMethod.closeProgressDialog(mDialog, getContext());
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		PublicMethod.showToast(mContext, "分享成功");
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		PublicMethod.showToast(mContext, "分享失败");
//		if (arg0==) {
//			
//		}
	}

	@Override
	public void onClick(View v) {
		jumpLoginActivity();
	}

	private void jumpLoginActivity(){
		Intent intent = new Intent();
		intent.setClass(mContext, UserLogin_Activity.class);
		mContext.startActivity(intent);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PUBLISHPETTALK:
			onPublishPetTalk(bean);
			break;
            case RequestCode.REQUEST_PUBLISHSTORY:
                onPublishStory(bean);
                break;
		case RequestCode.REQUEST_PETALKFOCUSLIST:
//			List<SayVo> sayVos=JsonParse.getSingleton().parseAttentionPetalkList(jsonStr);
			List<PetalkVo> sayVos = null;
			try {
				sayVos = JsonUtils.getList(bean.getValue(), PetalkVo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onGetDataCallback(sayVos, bean.isIsMore());
			break;
		default:
			break;
		}
			
	}

    private void onPublishStory(ResponseBean bean) {
        onRefresh();
        UploadStoryView view = (UploadStoryView) bean.getTag();
        mViewLayout.removeView(view);
        view.release();
        if(!TextUtils.isEmpty(bean.getMessage())){
            ToastUtiles.showCenter(getContext(),bean.getMessage());
        }
        PetsayLog.e("[onPublishStory] bean => "+bean.getValue());
    }

    private void onPublishPetTalk(ResponseBean bean) {
		onRefresh();
        if(bean.getTag() != null){
            PublishTalkParam param = (PublishTalkParam) bean.getTag();
            PublishTalkManager.getInstance().deleteLocalData(param);
        }
    if(!TextUtils.isEmpty(bean.getMessage())){
        ToastUtiles.showCenter(getContext(),bean.getMessage());
    }
		String jsonStr="["+bean.getValue()+"]";
		ArrayList<PetalkVo> vos = null;
		try {
			vos = (ArrayList<PetalkVo>) JsonUtils.getList(jsonStr, PetalkVo.class);
			sharePetalkVo=vos.get(0);
			Share share=new Share();
			share.shareMore((android.app.Activity) mContext, sharePetalkVo);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKFOCUSLIST:
			if(error.isIsMore())
				mPullView.onFooterRefreshComplete();
			else {
				mPullView.onHeaderRefreshComplete();
			}
			break;
		case RequestCode.REQUEST_PUBLISHPETTALK:
			if(error.getCode() == PetSayError.CODE_NETWORK_DISABLED){
				PublicMethod.showToast(getContext(), R.string.network_disabled);
			}else {
				PublicMethod.showToast(getContext(), "发布说说失败！已保存到草稿箱");
			}
		default:
			break;
		}
		
	}
	
}
