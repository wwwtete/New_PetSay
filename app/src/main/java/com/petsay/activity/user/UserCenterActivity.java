package com.petsay.activity.user;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.coupon.MyCouponsActivity;
import com.petsay.activity.personalcustom.pay.OrderManagerActivity;
import com.petsay.activity.shop.ShopActivity;
import com.petsay.activity.user.signin.SigninActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.MemberNet;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.sign.ActivityPartakeVo;

public class UserCenterActivity extends BaseActivity implements OnClickListener ,NetCallbackInterface{

	

	private ListView lvUsercenter;

	// 修改当前激活宠物layout
//	private LinearLayout layoutSelectPet;//, layout_unActivePets;
	private TextView tvAttention, tvFans;
	private TextView tvPetName, tvAge;
	private ImageView imgSex;
	private PullToRefreshView mPullToRefreshView;
	private ImageView mImgStar;
	// 当前激活的宠物
	private PetVo activePet;
	// 刚被删除的宠物（全局记录）
	private PetVo delPet;
	// 除已激活的宠物外宠物列表
	private List<PetVo> otherPets;
	private CircleImageView imgHeader;
	private RelativeLayout mLayoutRoot;
	private ImageView mImgHeaderViewBg;
	private LayoutParams mLayoutParams;

//	private UserData mUserData;
//	private UserModule mUserModule;
	private UserNet mUserNet;
//	private CircleImageView mAddPet;
	private View mHeadView;
	private ImageView mImgMore;
	private TextView mTvShop,mTvSignin;
	private TextView mTvPetalk,mTvRelay,mTvComment,mTvFavour;
	/**
	 * mImgMore点击是否是添加宠物功能,true添加宠物功能，false显示其他未激活宠物
	 */
	private boolean isAddStatus=false;
	private MemberNet mMemberNet;
	private List<ActivityPartakeVo> mActivityPartakeVos;
	private ActivityPartakeVo mActivityPartakeVo;

	private AddPetPopupWindow addPetPopupWindow;
	private String[] itemTitles = {"我的等级", "我的宠豆", "我的积分","我的订单","我的优惠券" };
    private UserAdapter mAdapter;
	private int[] itemIcons = {R.drawable.usercenter_item_01, R.drawable.usercenter_item_02,
			R.drawable.usercenter_item_03,R.drawable.usercenter_item_04,R.drawable.usercenter_item_05};
	
	private ProgressDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercenter);
		addPetPopupWindow=new AddPetPopupWindow(this);
//		mView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (layoutSelectPet.getVisibility()==View.VISIBLE) {
//					layoutSelectPet.setVisibility(View.GONE);
//				}
//				
//			}
//		});
		
//		mUserData = new UserData(handler);
//		mUserModule = UserModule.getSingleton();
		
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		mMemberNet=new MemberNet();
		mMemberNet.setTag(this);
		mMemberNet.setCallback(this);
		initView();
		initTitleBar("个人中心");
	}
	  @Override
	    protected void initTitleBar(String title) {
	        super.initTitleBar(title);
	        mTitleBar.setFinishEnable(false);
	        mTitleBar.setBgColor(Color.TRANSPARENT);;
	        mTitleBar.setOnClickBackListener(new TitleBar.OnClickBackListener() {
                @Override
                public void OnClickBackListener() {
                    finish();
                }
            });
	      
	    }


	protected void initView() {
		super.initView();
		lvUsercenter = (ListView) findViewById(R.id.lv_usercenter);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefreshview);
		mPullToRefreshView.setPullUpRefreshEnable(false);
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						onRefreshUser();
					}
				});
        mImgHeaderViewBg=(ImageView) findViewById(R.id.img_headerbg);
        mLayoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
        mTvSignin=(TextView) findViewById(R.id.tv_signin);
		mHeadView = LayoutInflater.from(this).inflate(R.layout.usercenter_header_new, null);
		lvUsercenter.addHeaderView(mHeadView);
		mAdapter=new UserAdapter();
		lvUsercenter.setAdapter(mAdapter);
		mImgMore=(ImageView) mHeadView.findViewById(R.id.img_more);
		mTvShop=(TextView) mHeadView.findViewById(R.id.tv_shop);
		
		
		mTvPetalk=(TextView) mHeadView.findViewById(R.id.tv_petalk);
		mTvRelay=(TextView) mHeadView.findViewById(R.id.tv_relay);
		mTvComment=(TextView) mHeadView.findViewById(R.id.tv_comment);
		mTvFavour=(TextView) mHeadView.findViewById(R.id.tv_favour);
		mImgStar=(ImageView) mHeadView.findViewById(R.id.img_star);
//		layoutSelectPet = (LinearLayout) mHeadView.findViewById(R.id.layout_select_pet);
//		layout_unActivePets = (LinearLayout) mHeadView.findViewById(R.id.layout_other);
		
//		mAddPet = (CircleImageView) mHeadView.findViewById(R.id.add_pet);
		tvPetName = (TextView) mHeadView.findViewById(R.id.tv_name);
		tvAge = (TextView) mHeadView.findViewById(R.id.tv_age);
		imgSex = (ImageView) mHeadView.findViewById(R.id.img_sex);
		tvAttention = (TextView) mHeadView.findViewById(R.id.tv_attention);
		tvFans = (TextView) mHeadView.findViewById(R.id.tv_fans);
		imgHeader = (CircleImageView) mHeadView.findViewById(R.id.img_header_usercenter);
		
//		mAddPet.setOnClickListener(this);
		mImgMore.setOnClickListener(this);
		imgHeader.setOnClickListener(this);
		tvAttention.setOnClickListener(this);
        tvFans.setOnClickListener(this);
        mTvShop.setOnClickListener(this);
        mTvSignin.setOnClickListener(this);
        
        mTvPetalk.setOnClickListener(this);
        mTvRelay.setOnClickListener(this);
        mTvComment.setOnClickListener(this);
        mTvFavour.setOnClickListener(this);
		
		lvUsercenter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent=null;
				switch (position) {
				case 1:
					intent=new Intent(UserCenterActivity.this,PetScoreDetailActivity.class);
					break;
				case 2:
					intent=new Intent(UserCenterActivity.this,PetCoinDetailActivity.class);
					break;
				case 3:
					intent=new Intent(UserCenterActivity.this, PetScoreDetailActivity.class);
					break;
                case 4:
                    intent=new Intent(UserCenterActivity.this, OrderManagerActivity.class);
                    break;
                case 5:
                	intent=new Intent(UserCenterActivity.this,MyCouponsActivity.class);
                	break;
				}
				if (null!=intent) {
					 startActivity(intent);
				}
               
			}
		});
		lvUsercenter.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int[] location = new int[2];   
				mHeadView.getLocationOnScreen(location);
				
				mLayoutParams=new LayoutParams(LayoutParams.FILL_PARENT,location[1]+ mHeadView.getMeasuredHeight());
				mImgHeaderViewBg.setLayoutParams(mLayoutParams);
				mImgHeaderViewBg.setScaleType(ScaleType.CENTER_CROP);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent= new Intent();
		switch (v.getId()) {
		case R.id.add_pet:
			intent.setClass(UserCenterActivity.this, PetInfo_Acitivity.class);
			intent.putExtra(PetInfo_Acitivity.TURN_TYPE,PetInfo_Acitivity.TYPE_ADDPET);
			startActivity(intent);
			break;
		case R.id.img_more:
			if (PublicMethod.isFastDoubleClick()) {
				return;
			}
			if (isAddStatus) {
				intent.setClass(UserCenterActivity.this, PetInfo_Acitivity.class);
				intent.putExtra(PetInfo_Acitivity.TURN_TYPE,PetInfo_Acitivity.TYPE_ADDPET);
				startActivity(intent);
			} else if (addPetPopupWindow.isShowing()) {
				addPetPopupWindow.dismiss();
			} else {
				addPetPopupWindow.showAsDropDown(imgHeader, 0, PublicMethod.getDiptopx(UserCenterActivity.this, 10));
			}
			break;
		case R.id.img_header_usercenter:
			intent.setClass(UserCenterActivity.this, PetInfo_Acitivity.class);
			startActivity(intent);
			break;
		case R.id.tv_attention:
			intent .setClass(UserCenterActivity.this,AttentionActivity.class);
			intent.putExtra("petId", UserManager.getSingleton().getActivePetId());
			startActivity(intent);
			break;
		case R.id.tv_fans:
			intent .setClass(UserCenterActivity.this, FansActivity.class);
			intent.putExtra("petId", UserManager.getSingleton().getActivePetId());
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.tv_signin:
			//TODO 签到
			if (null==mActivityPartakeVo) {
				PublicMethod.showToast(UserCenterActivity.this, R.string.sign_unable);
			}else {
				intent.setClass(UserCenterActivity.this, SigninActivity.class);
				intent.putExtra("activitypartakevo", mActivityPartakeVo);
				startActivity(intent);
			}
			break;
		case R.id.tv_shop:
			//TODO 商城
			intent.setClass(UserCenterActivity.this, ShopActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_petalk:
			intent.setClass(UserCenterActivity.this, MyPetalkListActivity.class);
			intent.putExtra("folderPath", "我的说说");
			intent.putExtra("type", Constants.ORIGINAL);
			startActivity(intent);
			break;
		case R.id.tv_comment:
			intent.setClass(UserCenterActivity.this, MyCommentListActivity.class);
			intent.putExtra("folderPath", "我评论的");
			intent.putExtra("type", Constants.COMMENT);
			startActivity(intent);
			break;
		case R.id.tv_relay:
			intent.setClass(UserCenterActivity.this, MyPetalkListActivity.class);
			intent.putExtra("folderPath", "我转发的");
			intent.putExtra("type", Constants.RELAY);
			startActivity(intent);
			break;
		case R.id.tv_favour:
			intent.setClass(UserCenterActivity.this, MyPetalkListActivity.class);
			intent.putExtra("folderPath", "我踩过的");
			intent.putExtra("type", Constants.FAVOUR);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initPets();
	}


	@Override
	public void onPause() {
		super.onPause();
	}

//	@Override
//	public void onHiddenChanged(boolean hidden) {
//		if (hidden) {
//		} else {
//			onRefreshUser();
//		}
//		super.onHiddenChanged(hidden);
//	}

	/**
	 * 初始化宠物，进入页面或者激活宠物时触发
	 */
	private void initPets() {
		activePet = UserManager.getSingleton().getActivePetInfo();
		otherPets = UserManager.getSingleton().getOtherPets();

		addPetPopupWindow.layout_unActivePets.removeAllViews();
		if (otherPets.size()>0) {
			isAddStatus=false;
			mImgMore.setImageResource(R.drawable.usercenter_more);
//			if (otherPets.size()>=2) {
//				mAddPet.setVisibility(View.GONE);
//			}
		}else {
			isAddStatus=true;
			mImgMore.setImageResource(R.drawable.usercenter_add);
		}
		
		for (int i = 0; i < otherPets.size(); i++) {
			final PetVo petInfo = otherPets.get(i);
			View view = (LinearLayout) LayoutInflater.from(UserCenterActivity.this).inflate(R.layout.unactive_pet_item, null);
			CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_unactive);
			ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(),circleImageView);
			addPetPopupWindow.layout_unActivePets.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showLoading();
					mUserNet.changePet(petInfo.getId());
					petInfo.setActive(true);
					UserManager.getSingleton().setActivePetInfo(petInfo);
					DataFileCache.getSingleton().asyncSaveData(Constants.UserFile,UserManager.getSingleton().getUserInfo());
					initPets();
					UserManager.isUserChanged = true;
					addPetPopupWindow.dismiss();
                    DecorationDataManager.getInstance(UserCenterActivity.this).getServerDecorationData();
                    ChatMsgManager.getInstance().closeClient();
                    ChatMsgManager.getInstance().auth();
				}
			});
//			 view.setOnLongClickListener(new OnLongClickListener() {
//			
//			 @Override
//			 public boolean onLongClick(View v) {
//			 mUserModule.addListener(mUserData);
//			 mUserModule.delPet(mUserData, petInfo.getId());
//			 delPet = petInfo;
//			 return true;
//			 }
//			 });
		}
		
		tvPetName.setText(activePet.getNickName());
		tvAttention.setText("关注：" + activePet.getCounter().getFocus());
		tvFans.setText("|粉丝：" + activePet.getCounter().getFans());
		// ImageLoader.getInstance().displayImage(activePet.getHeadPortrait(),imgHeader,
		// Constants.headerImgOptions);
		// PicassoUtile.loadHeadImg(getActivity(), activePet.getHeadPortrait(),
		// imgHeader);
		ImageLoaderHelp.displayHeaderImage(activePet.getHeadPortrait(),imgHeader);
		if (activePet.getGender() == 0) {
			imgSex.setVisibility(View.VISIBLE);
			imgSex.setImageResource(R.drawable.female);
		} else if (activePet.getGender() == 1) {
			imgSex.setVisibility(View.VISIBLE);
			imgSex.setImageResource(R.drawable.male);
		} else {
			imgSex.setVisibility(View.GONE);
		}
		tvAge.setText(Constants.petMap.get(activePet.getType()) + " " + activePet.getAge());
		mUserNet.petOne( UserManager.getSingleton().getActivePetId(), UserManager.getSingleton().getActivePetId());
	}

	/**
	 * 刷新用户信息
	 */
	private void onRefreshUser() {
		if (UserManager.getSingleton().isLoginStatus()) {
			mPullToRefreshView.showHeaderAnimation();
			showLoading();
			mUserNet.petOne(UserManager.getSingleton()
					.getActivePetId(), UserManager.getSingleton()
					.getActivePetId());
		}
	}

	private void onPulltoRefreshCallback() {
//		if (msg.arg1 == 0) {
			mPullToRefreshView.onHeaderRefreshComplete();
//		} else {
//			mPullToRefreshView.onFooterRefreshComplete();
//		}
	}

	protected void showLoading() {
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(UserCenterActivity.this);
	}

	protected void closeLoading() {
		PublicMethod.closeProgressDialog(mDialog, UserCenterActivity.this);
	}

	private class UserAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemTitles.length;
		}

		@Override
		public Object getItem(int position) {
			return itemTitles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null == convertView) {
				holder = new Holder();
				convertView = LayoutInflater.from(UserCenterActivity.this).inflate(R.layout.usercenter_list_item_new, null);
				holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
				holder.tvItemTitle = (TextView) convertView.findViewById(R.id.tv_itemtitle);
				holder.rLayout=(RelativeLayout) convertView.findViewById(R.id.layout_item);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.rLayout.setBackgroundColor(Color.WHITE);
			switch (position) {
			case 0:
				holder.tvItemTitle.setText(itemTitles[position]+"  (Lv"+activePet.getIntGrade()+")");
				break;
			case 1:
				holder.tvItemTitle.setText(itemTitles[position]+"  ("+activePet.getCoin()+")");
				break;
			case 2:
				holder.tvItemTitle.setText(itemTitles[position]+"  ("+activePet.getScore()+")");
				break;
             default:
                holder.tvItemTitle.setText(itemTitles[position]);
                break;
			}
			
//			holder.tvItemTitle.setText(itemTitles[position]);
			holder.imgIcon.setImageResource(itemIcons[position]);
			return convertView;
		}

	}

	private class Holder {
		private TextView tvItemTitle;
		private ImageView imgIcon;
		private RelativeLayout rLayout;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		onPulltoRefreshCallback();
		switch (requestCode) {
		case RequestCode.REQUEST_ACTIVITYPARTAKE:
			try {
				mActivityPartakeVos=JsonUtils.getList(bean.getValue(), ActivityPartakeVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < mActivityPartakeVos.size(); i++) {
				if (mActivityPartakeVos.get(i).getCode().equals("signIn")) {
					mActivityPartakeVo=mActivityPartakeVos.get(i);
					if (mActivityPartakeVo.getState()==1) {
						mTvSignin.setBackgroundResource(R.drawable.usercenter_sign);
					}else {
						mTvSignin.setBackgroundResource(R.drawable.usercenter_sign_alredy);
					}
					break;
				}
			}
			break;
		case RequestCode.REQUEST_CHANGEPET:
			
			break;
		case RequestCode.REQUEST_DELPET:
			if (null != delPet) {
				UserManager.getSingleton().removePet(delPet);
			}
			initPets();
			break;
		case RequestCode.REQUEST_PETONE:
			PetVo mPetInfo = JsonUtils.resultData(bean.getValue(),PetVo.class);
			ImageLoaderHelp.displayHeaderImage(mPetInfo.getHeadPortrait(),imgHeader);
			tvAttention.setText("关注：" + mPetInfo.getCounter().getFocus());
			tvFans.setText("　|　粉丝：" + mPetInfo.getCounter().getFans());
			mPetInfo.setActive(true);
			activePet=mPetInfo;
			if (mPetInfo.getGender() == 0) {
				imgSex.setVisibility(View.VISIBLE);
				imgSex.setImageResource(R.drawable.female);
			} else if (mPetInfo.getGender() == 1) {
				imgSex.setVisibility(View.VISIBLE);
				imgSex.setImageResource(R.drawable.male);
			} else {
				imgSex.setVisibility(View.GONE);
			}
			if (null!=mAdapter) {
				mAdapter.notifyDataSetChanged();
//				lvUsercenter.setAdapter(mAdapter);
			}
			mTvPetalk.setText(mPetInfo.getCounter().getIssue()+"\n说说");
			mTvRelay.setText(mPetInfo.getCounter().getRelay()+"\n转发");
			mTvComment.setText(mPetInfo.getCounter().getComment()+"\n评论");
			mTvFavour.setText(mPetInfo.getCounter().getFavour()+"\n踩");
			
			tvAge.setText(Constants.petMap.get(mPetInfo.getType()) + " "+ mPetInfo.getAge());
			UserManager.getSingleton().getActivePetInfo().setCounter(mPetInfo.getCounter());
			
			if (!TextUtils.isEmpty(mPetInfo.getStar())&&mPetInfo.getStar().equals("1")) {
				mImgStar.setVisibility(View.VISIBLE);
			} else {
				mImgStar.setVisibility(View.GONE);
			}	
			UserManager.getSingleton().setActivePetInfo(mPetInfo);
//			
			DataFileCache.getSingleton().asyncSaveData(Constants.UserFile,UserManager.getSingleton().getUserInfo());
			
			mMemberNet.activityPartake(mPetInfo.getId());
			break;
		default:
			break;
		}
		closeLoading();
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onPulltoRefreshCallback();
		closeLoading();
		onErrorShowToast(error);
	}
}
