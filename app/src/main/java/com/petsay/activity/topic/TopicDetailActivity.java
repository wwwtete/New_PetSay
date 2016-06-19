package com.petsay.activity.topic;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.SharePopupWindow;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.topic.adapter.TopicTalkListAdapter;
import com.petsay.application.PublishTopicReplyManager;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.pushimage.KenBurnsView;
import com.petsay.component.pushimage.KenBurnsView.ImageLoadCompleteCallback;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.UploadTopicReplyView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.TalkDTO;
import com.petsay.vo.forum.TopicDTO;

/**
 * 主题详情
 * 
 * @author G
 */
public class TopicDetailActivity extends BaseActivity implements
		IAddShowLocationViewService, NetCallbackInterface, OnClickListener,
		ImageLoadCompleteCallback,
		UploadTopicReplyView.PublishTopicReplyListener {
	private ListView lv;

	private TitleBar mTitleBar;
	private RelativeLayout layoutRoot;
	private FrameLayout layoutPullParent;
	private TextView tvAddTalk;
	private ImageView addImg;
	private LinearLayout layoutProgress;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private TopicTalkListAdapter mTalkListAdapter;
	// private ShopNet mShopNet;
	private TopicDTO mTopicDTO;
	private List<TalkDTO> talkDTOs = new ArrayList<TalkDTO>();
	private int pageIndex = 0;
	private int pageSize = 10;

	private TopicNet mTopicNet;

	private int firstAllListPosition = -1;

	private int mActionBarHeight;
	private int mMinHeaderTranslation = 0;
	private KenBurnsView mHeaderPicture;

	private View mHeader;
	private View mPlaceHolderView;

	private TypedValue mTypedValue = new TypedValue();

	private int h1, h2;
	private boolean isFirst = true;
	private int mClickPosition=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_detail);
		initView();
		mTopicNet = new TopicNet();
		mTopicNet.setCallback(this);
		mTopicNet.setTag(this);
		try {
			mTopicDTO = (TopicDTO) getIntent().getSerializableExtra("topicDTO");
			if (null == mTopicDTO) {
				mTopicNet.topicTopOne();
			} else {
				setContent();
			}
		} catch (Exception e) {
			mTopicNet.topicTopOne();
		}
		
		
		showLoading();
	}

	private void setContent() {
		mHeaderPicture.setImgUrls(mTopicDTO.getPic());
		mHeaderPicture.setText(mTopicDTO.getContent());
		onRefresh();
	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		layoutPullParent = (FrameLayout) findViewById(R.id.layout_pullparent);
		lv = (ListView) findViewById(R.id.lv);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		initTitleBar();
		initPullToRefreshView();
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
		mHeaderPicture.setImageLoadCompleteCallback(this);

		tvAddTalk = (TextView) findViewById(R.id.tv_addtalk);
		tvAddTalk.setOnClickListener(this);
		addImg = (ImageView) findViewById(R.id.img_add);
		addImg.setOnClickListener(this);

		mPlaceHolderView = getLayoutInflater().inflate(
				R.layout.view_header_placeholder, lv, false);
		// mPlaceHolderView=lv.getHeaderView();
		lv.addHeaderView(mPlaceHolderView);
		layoutProgress = new LinearLayout(getApplicationContext());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.FILL_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT);
		layoutProgress.setLayoutParams(params);
		layoutProgress.setOrientation(LinearLayout.VERTICAL);
		lv.addHeaderView(layoutProgress);
		mTalkListAdapter = new TopicTalkListAdapter(TopicDetailActivity.this);
		lv.setAdapter(mTalkListAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int childCount = lv.getHeaderViewsCount();
				int clickItemPosition = position - childCount;
				if (clickItemPosition >= 0) {
					TalkDTO dto = (TalkDTO) mTalkListAdapter
							.getItem(clickItemPosition);
					if (null != dto) {
						mClickPosition=clickItemPosition;
						Intent intent = new Intent(TopicDetailActivity.this,
								TopicCommentListActivity.class);
						intent.putExtra("talkDto", dto);
						intent.putExtra("topicDTO", mTopicDTO);
						startActivityForResult(intent, 100);
					}
				}

			}
		});

		mPullView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					mHeaderPicture.downState = false;
					mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
					break;
				}
				return false;
			}
		});

		lv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				//
				if (lv.getTop() > 0) {

				} else {
					if (isFirst && mMinHeaderTranslation < 0) {
						isFirst = false;
						h1 = mMinHeaderTranslation;
					}
					if (h1 != 0 && mMinHeaderTranslation < h2) {
						h2 = mMinHeaderTranslation;
					}
					if (-scrollY > h1) {
						mHeaderPicture.mHeaderLogo.setSingleLine(false);
						mHeaderPicture.mHeaderLogo.setMaxLines(2);
						mHeaderPicture.setTextPadding(10, 10);
					} else {
						mHeaderPicture.mHeaderLogo.setSingleLine(true);
						// mHeaderPicture.mHeaderLogo.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
						mHeaderPicture.setTextPadding(40, 40);
					}
					mHeader.setTranslationY(Math.max(-scrollY,
							mMinHeaderTranslation));
				}
				// mHeader.setTranslationY(Math.max(-scrollY,
				// mMinHeaderTranslation));
			}

		});
	}

	private void initTitleBar() {
		mTitleBar.setBackgroundResource(R.color.transparent);
		mTitleBar.setTitleText("");
		mTitleBar.setFinishEnable(true);

		ImageView imgView = new ImageView(TopicDetailActivity.this);
		imgView.setImageResource(R.drawable.more);
		LinearLayout.LayoutParams txt_Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(imgView, txt_Params);
		layout.setTag(0);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (null!=mTopicDTO) {
					SharePopupWindow popupWindow;
					popupWindow = new SharePopupWindow(getActivity(),
							TopicDetailActivity.this, null, mTopicDTO, false);
					popupWindow.isShowForward(false);
					popupWindow.show();
				}
			}
		});

		mTitleBar.addRightView(layout);
	}

	private void initPullToRefreshView() {
		// mPullView.setPullDownRefreshEnable(false);
		// mPullView.setPullUpRefreshEnable(false);
		mPullView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				mHeaderPicture.downState = false;
				mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
				mPullView.onComplete(false);
				onRefresh();
			}
		});

		mPullView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				// lv.seton
				onAddMore();
			}
		});

	}

	private void onAddMore() {
		// pageIndex++;
		if (mTalkListAdapter.getCount() > 0) {
			TalkDTO vo = (TalkDTO) mTalkListAdapter.getItem(mTalkListAdapter
					.getCount() - 1);
			if (UserManager.getSingleton().isLoginStatus()) {
				mTopicNet.topicTalkList(UserManager.getSingleton()
						.getActivePetId(), mTopicDTO.getId(), vo.getId(),
						pageSize, true);
			} else {
				mTopicNet.topicTalkList("", mTopicDTO.getId(), vo.getId(),
						pageSize, true);
			}
		} else {
			mPullView.onFooterRefreshComplete();
			mHeaderPicture.downState = false;
			mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
			showToast("没有数据");
		}
	}

	private void onRefresh() {
		if (UserManager.getSingleton().isLoginStatus()) {
			mTopicNet.topicTalkList(
					UserManager.getSingleton().getActivePetId(),
					mTopicDTO.getId(), "", pageSize, false);
		} else {
			mTopicNet.topicTalkList("", mTopicDTO.getId(), "", pageSize, false);

		}

	}

	private void updateUploadList() {
		layoutProgress.removeAllViews();
		if (mTopicDTO == null)
			return;
		List<UploadTopicReplyView> views = PublishTopicReplyManager
				.getInstance().getUploadViewList(mTopicDTO.getId());
		if (views != null) {
			for (UploadTopicReplyView view : views) {
				view.setPublishListener(this);
				layoutProgress.addView(view);
			}
		}
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		updateUploadList();
		if (null != mTalkListAdapter && mTalkListAdapter.getCount() > 0) {
			mTalkListAdapter.notifyDataSetChanged();
		}
		// mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
	}

	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		layoutProgress.removeAllViews();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {

		switch (requestCode) {
		case RequestCode.REQUEST_TOPICTALKLIST:
			closeLoading();
			try {
				talkDTOs = JsonUtils.getList(bean.getValue(), TalkDTO.class);
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "解析试用商品列表出错");
				e.printStackTrace();
			}
			if (bean.isIsMore()) {
				for (int i = 0; i < talkDTOs.size(); i++) {
					if (!talkDTOs.get(i).getTop() && firstAllListPosition == -1) {
						firstAllListPosition = i;
						talkDTOs.get(i).setTalkType(2);
						break;
					}
				}
				mTalkListAdapter.addMore(talkDTOs);
			} else {
				mHeaderPicture.downState = false;
				mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
				for (int i = 0; i < talkDTOs.size(); i++) {
					if (i == 0 && talkDTOs.get(i).getTop()) {
						talkDTOs.get(i).setTalkType(1);
					} else if (!talkDTOs.get(i).getTop()) {
						firstAllListPosition = i;
						talkDTOs.get(i).setTalkType(2);
						break;
					}
				}
				mTalkListAdapter.refreshData(talkDTOs);
			}
			mPullView.onComplete(bean.isIsMore());
			break;
		case RequestCode.REQUEST_TOPICTOPONE:
			mTopicDTO = JsonUtils.resultData(bean.getValue(), TopicDTO.class);
			setContent();
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_TOPICTALKLIST:
			mPullView.onComplete(error.isIsMore());
			// if (error.isIsMore()) {
			// mPullView.onFooterRefreshComplete();
			// } else {
			// mPullView.onHeaderRefreshComplete();
			// }
			mHeaderPicture.downState = false;
			mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_addtalk:
			if (checkLogin()) {
				intent = new Intent();
				intent.setClass(TopicDetailActivity.this,
						TopicReplyActivity.class);
				intent.putExtra("topicid", mTopicDTO.getId());
				startActivityForResult(intent, 1000);
			}
			break;
		case R.id.img_add:
			if (checkLogin()) {
				intent = new Intent();
				intent.setClass(TopicDetailActivity.this,
						TopicPhotoWallActivity.class);
				intent.putExtra("topicid", mTopicDTO.getId());
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			onRefresh();
		}else if(requestCode==100){
			TalkDTO talkDTO=(TalkDTO) data.getSerializableExtra("talkDTO");
			mTalkListAdapter.setDto(talkDTO, mClickPosition);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean checkLogin() {
		boolean state = UserManager.getSingleton().isLoginStatus();
		if (!state) {
			Intent intent = new Intent(this, UserLogin_Activity.class);
			startActivity(intent);
		}
		return state;
	}

	@Override
	public View getParentView() {
		return layoutRoot;
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	public int getScrollY() {

		View c = lv.getChildAt(0);
		if (c == null) {
			return 0;
		}
		if (lv.getTop() > 0) {
			mHeaderPicture.downState = true;
			mHeaderPicture.setImageDisplay(lv.getChildAt(0).getBottom()
					+ lv.getTop());
		} else {
			mHeaderPicture.downState = false;
			mHeaderPicture.setImageDisplay(mHeaderPicture.imgHeight);
		}
		int firstVisiblePosition = lv.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
				true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}

	android.widget.AbsListView.LayoutParams layoutParams;

	@Override
	public void imageLoadComplete(int imgWidth, final int imgHeight,
			int textViewHeight) {
		if (mPlaceHolderView.getHeight() != imgHeight) {
			if (null == layoutParams) {
				layoutParams = new android.widget.AbsListView.LayoutParams(
						android.widget.AbsListView.LayoutParams.FILL_PARENT,
						mHeaderPicture.imgHeight);
			}

			mPlaceHolderView.setLayoutParams(layoutParams);
		}
		mMinHeaderTranslation = -imgHeight + textViewHeight;
	}

	@Override
	public void onPublishTopicReplyFinish(boolean isSuccess,
			UploadTopicReplyView view) {
		if (isSuccess)
			onRefresh();
	}

}