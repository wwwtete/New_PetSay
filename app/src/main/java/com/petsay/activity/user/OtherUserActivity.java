package com.petsay.activity.user;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.CircleImageView;
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
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.NetworkManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetFansDTO;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

/**
 * 查看其它用户的说说与转发
 * 
 * @author G
 *
 */
public class OtherUserActivity extends BaseActivity implements OnClickListener,IAddShowLocationViewService, NetCallbackInterface {
	private ListView lv;
	private TextView tvPetName, tvAttention, tvFans, tvAge, tvGrade;
	private ImageView imgSex, imgGrade;
	private CircleImageView imgHeader;
	private TabTitleLayoutView layoutForward, layoutOriginal;
	private ImageView mImgStar;
	// private TextView tvCountForward,tvCountOriginal;
	private Button btnAttention;
	private TitleBar mTitleBar;
	private View mheaderView;
	private View mTabView;
	private RelativeLayout mLayoutRoot;
	private ImageView mImgHeaderViewBg;
	private RelativeLayout.LayoutParams mLayoutParams;

	private int fansCount = 0;
	private PetVo mPetInfo;
	private PullToRefreshView mPullToRefreshView;
	private SayDataNet mSayDataNet;
	private UserNet mUserNet;
	// private List<SayVo> mOriginal_Sayvos;
	// private List<SayVo> mForward_Sayvos;
	private HotListViewAdapter attentionListViewAdapter;
	private HotListViewAdapter mRelayAdapter;
	private GifListScrollListener mScrollListener;
	
	// private PetInfo petInfo;
	private int currentTabIndex = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_user);
		mPetInfo = (PetVo) getIntent().getSerializableExtra("petInfo");
		initView();
		initTitleBar();
		setListener();
		
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);

		onRefresh();

	}

	protected void initView() {
		super.initView();
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		lv = (ListView) findViewById(R.id.lv);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefreshview);
		mImgHeaderViewBg=(ImageView) findViewById(R.id.img_headerbg);
        mLayoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		mheaderView = LayoutInflater.from(this).inflate(R.layout.otheruser_header, null);
		lv.addHeaderView(mheaderView);
		tvPetName = (TextView) mheaderView.findViewById(R.id.tv_name);
		tvAge = (TextView) mheaderView.findViewById(R.id.tv_age);
		tvGrade=(TextView) mheaderView.findViewById(R.id.tv_grade);
		imgGrade=(ImageView) mheaderView.findViewById(R.id.img_grade);
		imgSex = (ImageView) mheaderView.findViewById(R.id.img_sex);
		mTabView = mheaderView.findViewById(R.id.layout_top_tab);
		tvAttention = (TextView) mheaderView.findViewById(R.id.tv_attention);
		tvFans = (TextView) mheaderView.findViewById(R.id.tv_fans);
		imgHeader = (CircleImageView) mheaderView.findViewById(R.id.img_header_usercenter);
		layoutForward = (TabTitleLayoutView) mheaderView.findViewById(R.id.layout_top_forward);
		layoutOriginal = (TabTitleLayoutView) mheaderView.findViewById(R.id.layout_top_release);
		btnAttention = (Button) mheaderView.findViewById(R.id.btn_attention);
		mImgStar=(ImageView) mheaderView.findViewById(R.id.img_star);
		//官方ID
		if (mPetInfo.getId().equals(Constants.OFFICIAL_ID)) {
			btnAttention.setClickable(false);
		}
		// 图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(OtherUserActivity.this, 50);
		mScrollListener = new GifListScrollListener(lv, titleHeight) {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				super.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
				int[] location = new int[2];  
				mheaderView.getLocationOnScreen(location);
				int endY=location[1]+mheaderView.getMeasuredHeight();
				mLayoutParams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,endY);
				mImgHeaderViewBg.setLayoutParams(mLayoutParams);
				mImgHeaderViewBg.setScaleType(ScaleType.CENTER_CROP);
			}
		};
		lv.setOnScrollListener(mScrollListener);
		attentionListViewAdapter=new HotListViewAdapter(OtherUserActivity.this, this,false);
//		attentionListViewAdapter = new AttentionListViewAdapter(
//				OtherUserActivity.this,
//				AttentionListViewAdapter.HIDE_ATTENTION, true, this);
		mRelayAdapter=new HotListViewAdapter(OtherUserActivity.this, this,false);
//		mRelayAdapter = new AttentionListViewAdapter(OtherUserActivity.this,
//				AttentionListViewAdapter.HIDE_ATTENTION, true, this);
		lv.setAdapter(attentionListViewAdapter);
		// ImageLoader.getInstance().displayImage(mPetInfo.getHeadPortrait(),
		// imgHeader, Constants.headerImgOptions);
		// PicassoUtile.loadHeadImg(OtherUserActivity.this,
		// mPetInfo.getHeadPortrait(), imgHeader);
		ImageLoaderHelp.displayHeaderImage(mPetInfo.getHeadPortrait(),
				imgHeader);
		tvPetName.setText(mPetInfo.getNickName());
		if (mPetInfo.getGender() == 0) {
			imgSex.setVisibility(View.VISIBLE);
			imgSex.setImageResource(R.drawable.female);

		} else if (mPetInfo.getGender() == 1) {
			imgSex.setVisibility(View.VISIBLE);
			imgSex.setImageResource(R.drawable.male);
		} else {
			imgSex.setVisibility(View.GONE);
		}
		if (mPetInfo.getIntGrade()<=0) {
			tvAge.setText(Constants.petMap.get(mPetInfo.getType())+"｜"+mPetInfo.getAge());
			imgGrade.setVisibility(View.GONE);
			tvGrade.setVisibility(View.GONE);
		}else {
			tvAge.setText(Constants.petMap.get(mPetInfo.getType())+"｜"+mPetInfo.getAge()+"｜");
			tvGrade.setVisibility(View.VISIBLE);
			if (-1==mPetInfo.getLevenIconResId()) {
//				holder.imgGrade.setImageDrawable(null);
				imgGrade.setVisibility(View.GONE);
			}else {
				imgGrade.setVisibility(View.VISIBLE);
				imgGrade.setImageResource(mPetInfo.getLevenIconResId());
			}
			tvGrade.setText("Lv"+mPetInfo.getIntGrade());
		}
		layoutForward.setTitle("转发");
		layoutOriginal.setTitle("发布");
		changeTabLayoutBg(layoutOriginal, layoutForward);
	}

	private void setListener() {
		tvAttention.setOnClickListener(this);
		tvFans.setOnClickListener(this);
		layoutForward.setOnClickListener(this);
		layoutOriginal.setOnClickListener(this);
		btnAttention.setOnClickListener(this);
		mPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						onRefresh();
					}
				});
		mPullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {
					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						onAddMore();
					}
				});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position >= 1) {
					switch (currentTabIndex) {
					case 0:
						PetalkVo sayVo = (PetalkVo) attentionListViewAdapter
								.getItem(position - 1);
						if (null != sayVo) {
							Intent intent = new Intent();
							intent.setClass(OtherUserActivity.this,
									DetailActivity.class);
							Constants.Detail_Sayvo = sayVo;
							startActivity(intent);
						}
						break;
					case 1:
						sayVo = (PetalkVo) mRelayAdapter.getItem(position - 1);
						if (null != sayVo) {
							Intent intent = new Intent();
							intent.setClass(OtherUserActivity.this,
									DetailActivity.class);
							Constants.Detail_Sayvo = sayVo;
							startActivity(intent);
						}
						break;
					}
				}
			}
		});

        mheaderView.findViewById(R.id.btn_talk).setOnClickListener(this);
	}

	private void initTitleBar() {
		mTitleBar.setTitleText(mPetInfo.getNickName());
		mTitleBar.setFinishEnable(true);
		LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_attention:
			Intent intent = new Intent(getApplicationContext(),
					AttentionActivity.class);
			intent.putExtra("petId", mPetInfo.getId());
			startActivity(intent);
			break;
		case R.id.tv_fans:
			intent = new Intent(getApplicationContext(), FansActivity.class);
			intent.putExtra("petId", mPetInfo.getId());
			startActivity(intent);
			break;
		case R.id.layout_top_forward:
			if (currentTabIndex == 0) {
				currentTabIndex = 1;
				changeTabLayoutBg(layoutForward, layoutOriginal);
				showForwardList(currentTabIndex);
			}

			break;
		case R.id.layout_top_release:
			if (currentTabIndex == 1) {
				currentTabIndex = 0;
				changeTabLayoutBg(layoutOriginal, layoutForward);
				showForwardList(currentTabIndex);
			}
			break;
		case R.id.img_header_usercenter:

			break;
		case R.id.btn_attention:
			if (UserManager.getSingleton().isLoginStatus()) {
				showLoading();
				if (mPetInfo.getRs() == 0) {
					mUserNet.focus( mPetInfo.getId(), UserManager.getSingleton().getActivePetInfo().getId());
				} else if (mPetInfo.getRs() == 1 || mPetInfo.getRs() == 2) {
					// TODO 取消关注
					mUserNet.cancleFocus( mPetInfo.getId(),UserManager.getSingleton().getActivePetInfo().getId());
				}

			} else {
				intent = new Intent(this, UserLogin_Activity.class);
				startActivity(intent);
			}
			break;
            case R.id.btn_talk:
                intent = new Intent(this,ChatActivity.class);
                intent.putExtra("petinfo",mPetInfo);
                startActivity(intent);
                break;
		}
	}

	private void onRefresh() {
		mPullToRefreshView.showHeaderAnimation();
		if (currentTabIndex == 0) {
			getPublishData("", false);
		} else {
			getRelayData("", false);
		}
	}

//	@Override
//	protected void applySkin() {
//		super.applySkin();
////		SkinHelp.setBackground(mheaderView, this,getString(R.string.other_usercenter_header_bg));
////		mTabView.setBackgroundDrawable(SkinManager.getInstance(this).getDrawable(getString(R.string.other_usercenter_tab_bg)));
//	}

	private void onAddMore() {
		if (currentTabIndex == 0) {
			if (attentionListViewAdapter.getCount() > 0)
				getPublishData(
						((PetalkVo) attentionListViewAdapter.getItem(attentionListViewAdapter
								.getCount() - 1)).getId(),true);
			else {
				onPulltoRefreshCallback(true);
			}
		} else {
			if (mRelayAdapter.getCount() > 0) {
				getRelayData(((PetalkVo) mRelayAdapter.getItem(mRelayAdapter
						.getCount() - 1)).getId(), true);
			} else {
				onPulltoRefreshCallback(true);
			}
		}
	}

	/**
	 * @param type
	 *            0：刷新 1：加载更多
	 */
	private void onPulltoRefreshCallback(boolean isMore) {
		if (isMore) {
			mPullToRefreshView.onFooterRefreshComplete();
		} else {
			mPullToRefreshView.onHeaderRefreshComplete();
		}
	}

	private void showForwardList(int index) {
		GifViewManager.getInstance().stopGif();
		if (index == 0) {
			lv.setAdapter(attentionListViewAdapter);
			if (attentionListViewAdapter.getCount() == 0) {
				getPublishData("", false);
			}
		} else {
			lv.setAdapter(mRelayAdapter);
			if (mRelayAdapter.getCount() == 0) {
				getRelayData("", false);
			}
		}
		// if (index==0) {
		// //原创
		// if (null==mOriginal_Sayvos||mOriginal_Sayvos.isEmpty()) {
		// mSayModule.addListener(mSayData);
		// mSayModule.petalkUserList(mSayData,
		// mPetInfo.getId(),Constants.ORIGINAL, "", 10);
		// }else {
		// attentionListViewAdapter=new
		// AttentionListViewAdapter(OtherUserActivity.this,AttentionListViewAdapter.HIDE_ATTENTION,this);
		// lv.setAdapter(attentionListViewAdapter);
		// attentionListViewAdapter.refreshData(mOriginal_Sayvos);
		// }
		// }else {
		// //转发
		// if (null==mForward_Sayvos||mForward_Sayvos.isEmpty()) {
		// mSayModule.addListener(mSayData);
		// mSayModule.petalkUserList(mSayData, mPetInfo.getId(),Constants.RELAY,
		// "", 10);
		// }else {
		// attentionListViewAdapter=new
		// AttentionListViewAdapter(OtherUserActivity.this,AttentionListViewAdapter.HIDE_ATTENTION,this);
		// lv.setAdapter(attentionListViewAdapter);
		// attentionListViewAdapter.refreshData(mForward_Sayvos);
		// }
		// }
	}

	/**
	 * 获取自己发布的列表
	 */
	private void getPublishData(String id, boolean isMore) {
		// showLoading();
		GifViewManager.getInstance().stopGif();
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkUserList(mPetInfo.getId(),UserManager.getSingleton().getActivePetId(),Constants.ORIGINAL, id, 10, isMore);
		}else {
			mSayDataNet.petalkUserList(mPetInfo.getId(),"",Constants.ORIGINAL, id, 10, isMore);
			
		}
		
	}

	/**
	 * 获取转发列表
	 */
	private void getRelayData(String id,  boolean isMore) {
		// showLoading();
		GifViewManager.getInstance().stopGif();
		if (UserManager.getSingleton().isLoginStatus()) {
			mSayDataNet.petalkUserList( mPetInfo.getId(),UserManager.getSingleton().getActivePetId(), Constants.RELAY,id, 10, isMore);
		}else {
			mSayDataNet.petalkUserList( mPetInfo.getId(),"", Constants.RELAY,id, 10, isMore);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mScrollListener.release();
		super.onDestroy();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public View getParentView() {
		return mLayoutRoot;
	}

	/**
	 * 修改列表tab背景
	 * 
	 * @param layout1
	 *            为当前选中layout
	 * @param layout2
	 */
	public void changeTabLayoutBg(TabTitleLayoutView layout1,
			TabTitleLayoutView layout2) {
		layout1.setBackgroundResource(R.drawable.zhishi1);
		layout1.setTextColor(getResources().getColor(R.color.select_color));
		layout2.setBackgroundColor(Color.TRANSPARENT);
		layout2.setTextColor(Color.WHITE);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		onPulltoRefreshCallback(bean.isIsMore());
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			PublicMethod.showToast(OtherUserActivity.this,R.string.add_attention_success);
			// int focusFlag=mPetInfo.getRs();
			PetFansDTO petFansDTO=JsonUtils.resultData(bean.getValue(), PetFansDTO.class);
			
			if (petFansDTO.getBothway()) {
				mPetInfo.setRs(2);
				btnAttention.setBackgroundResource(R.drawable.other_focus_each);
			}else {
				mPetInfo.setRs(1);
				btnAttention.setBackgroundResource(R.drawable.other_focus_finish);
			}
			
			fansCount += 1;
			tvFans.setText("｜粉丝：" + fansCount);
			UserManager.getSingleton().focusMap.put(mPetInfo.getId(),mPetInfo.getId());
			break;
		case RequestCode.REQUEST_CANCLEFOCUS:
			// focusFlag=mPetInfo.getRs();
			UserManager.getSingleton().focusMap.remove(mPetInfo.getId());
			PublicMethod.showToast(OtherUserActivity.this,R.string.cancle_attention_success);
			mPetInfo.setRs(0);

			int fansCount = mPetInfo.getCounter().getFans();
			if (fansCount > 0) {
				fansCount -= 1;
			} else {
				fansCount = 0;

			}
			tvFans.setText("｜粉丝：" + fansCount);
			btnAttention.setBackgroundResource(R.drawable.other_focus_add);
			break;
		case RequestCode.REQUEST_PETALKUSERLIST:
			String type=(String) bean.getTag();
			if (type.equals(Constants.ORIGINAL)) {
				String jsonStr = bean.getValue();
				List<PetalkVo> mOriginal_Sayvos = null;
				try {
					mOriginal_Sayvos = JsonUtils.getList(jsonStr,PetalkVo.class);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// attentionListViewAdapter=new
				// AttentionListViewAdapter(OtherUserActivity.this,AttentionListViewAdapter.HIDE_ATTENTION,OtherUserActivity.this);
				// lv.setAdapter(attentionListViewAdapter);
				if (bean.isIsMore()) {
					attentionListViewAdapter.addMore(mOriginal_Sayvos);
				} else {
					attentionListViewAdapter.refreshData(mOriginal_Sayvos);
				}
				if (UserManager.getSingleton().isLoginStatus()) {
					mUserNet.petOne(mPetInfo.getId(), UserManager.getSingleton().getActivePetId());
				} else
					mUserNet.petOne(mPetInfo.getId(), "");
			}else {
				List<PetalkVo> mForward_Sayvos = null;
				try {
					mForward_Sayvos = JsonUtils.getList(bean.getValue(), PetalkVo.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (bean.isIsMore()) {
					mRelayAdapter.addMore(mForward_Sayvos);
				} else {
					mRelayAdapter.refreshData(mForward_Sayvos);
				}
			}
			
			break;
		case RequestCode.REQUEST_PETONE:
			mPetInfo = JsonUtils.resultData(bean.getValue(), PetVo.class);
			ImageLoaderHelp.displayHeaderImage(mPetInfo.getHeadPortrait(), imgHeader);
			fansCount = mPetInfo.getCounter().getFans();
			tvPetName.setText(mPetInfo.getNickName());
			mTitleBar.setTitleText(mPetInfo.getNickName());
			tvAttention.setText("关注：" + mPetInfo.getCounter().getFocus());
			tvFans.setText("｜粉丝：" + fansCount);
			if (!TextUtils.isEmpty(mPetInfo.getStar())&&mPetInfo.getStar().equals("1")) {
				mImgStar.setVisibility(View.VISIBLE);
			} else {
				mImgStar.setVisibility(View.GONE);
			}		
			layoutOriginal.setValue(mPetInfo.getCounter().getIssue() + "");
			layoutForward.setValue(mPetInfo.getCounter().getRelay() + "");
			if (mPetInfo.getGender() == 0) {
				imgSex.setVisibility(View.VISIBLE);
				imgSex.setImageResource(R.drawable.female);

			} else if (mPetInfo.getGender() == 1) {
				imgSex.setVisibility(View.VISIBLE);
				imgSex.setImageResource(R.drawable.male);
			} else {
				imgSex.setVisibility(View.GONE);
			}
			
			if (mPetInfo.getIntGrade()<=0) {
				tvAge.setText(Constants.petMap.get(mPetInfo.getType())+"｜"+mPetInfo.getAge());
				imgGrade.setVisibility(View.GONE);
				tvGrade.setVisibility(View.GONE);
			}else {
				tvAge.setText(Constants.petMap.get(mPetInfo.getType())+"｜"+mPetInfo.getAge()+"｜");
				tvGrade.setVisibility(View.VISIBLE);
				if (-1==mPetInfo.getLevenIconResId()) {
//					holder.imgGrade.setImageDrawable(null);
					imgGrade.setVisibility(View.GONE);
				}else {
					imgGrade.setVisibility(View.VISIBLE);
					imgGrade.setImageResource(mPetInfo.getLevenIconResId());
				}
				tvGrade.setText("Lv"+mPetInfo.getIntGrade());
			}
//			tvAge.setText(Constants.petMap.get(mPetInfo.getType()) + "｜"+ mPetInfo.getAge()+"｜");
//			tvGrade.setText("Lv"+mPetInfo.getIntGrade());
//			if (-1==mPetInfo.getLevenIconResId()) {
//				imgGrade.setImageDrawable(null);
//			}else {
//				imgGrade.setImageResource(mPetInfo.getLevenIconResId());
//			}
			if (mPetInfo.getRs() == 0) {
				UserManager.getSingleton().removeFocusByPetId(mPetInfo.getId());
//				btnAttention.setText(R.string.add_attention);
				btnAttention.setBackgroundResource(R.drawable.other_focus_add);
			} else if (mPetInfo.getRs() == 1 ) {
				UserManager.getSingleton().addFocusByPetId(mPetInfo.getId());
//				btnAttention.setText(R.string.already_add_attention);
				btnAttention.setBackgroundResource(R.drawable.other_focus_finish);
			}else if ( mPetInfo.getRs() == 2) {
				UserManager.getSingleton().addFocusByPetId(mPetInfo.getId());
				btnAttention.setBackgroundResource(R.drawable.other_focus_each);
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onPulltoRefreshCallback(error.isIsMore());
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			PublicMethod.showToast(OtherUserActivity.this,R.string.add_attention_failed);
			break;
		case RequestCode.REQUEST_CANCLEFOCUS:
			PublicMethod.showToast(OtherUserActivity.this,R.string.cancle_attention_failed);
			break;
		case RequestCode.REQUEST_PETALKUSERLIST:
				String type = (String) error.getResponseBean().getTag();
				if (!TextUtils.isEmpty(type) && type.equals(Constants.ORIGINAL)) {
					mSayDataNet.counterPet(mPetInfo.getId());
				}
			NetworkManager.getSingleton().canclePullRefresh(OtherUserActivity.this, mPullToRefreshView);
			break;
        case RequestCode.REQUEST_PETONE:
			break;
		default:
			break;
		}
		
	}
}
