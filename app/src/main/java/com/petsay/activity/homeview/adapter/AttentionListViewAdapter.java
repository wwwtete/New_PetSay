//package com.petsay.activity.homeview.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
//import android.widget.TextView;
//
//import com.petsay.R;
//import com.petsay.activity.user.UserLogin_Activity;
//import com.petsay.component.animationview.AttentionButtonView;
//import com.petsay.component.gifview.AudioGifView;
//import com.petsay.component.gifview.GifViewManager;
//import com.petsay.component.gifview.ImageLoaderListener;
//import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
//import com.petsay.component.view.ExProgressBar;
//import com.petsay.component.view.TagView;
//import com.petsay.component.view.functionbar.FunctionBarView;
//import com.petsay.component.view.functionbar.ListItemFunctionBarEventHolder;
//import com.petsay.component.view.functionbar.StepAnimView;
//import com.petsay.constants.Constants;
//import com.petsay.http.SayData;
//import com.petsay.http.UserData;
//import com.petsay.module.SayModule;
//import com.petsay.module.UserModule;
//import com.petsay.application.UserManager;
//import com.petsay.utile.ActivityTurnToManager;
//import com.petsay.utile.ImageLoaderHelp;
//import com.petsay.utile.PublicMethod;
//import com.petsay.vo.petalk.PetVo;
//import com.petsay.vo.petalk.PetalkDecorationVo;
//import com.petsay.vo.petalk.PetalkVo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Deprecated
//public class AttentionListViewAdapter extends BaseAdapter {
//	private Context mContext;
//	private ArrayList<PetalkVo> _sayVos;
//	private Drawable drawable;
//	// ImageLoader imageLoader = ImageLoader.getInstance();
//
//	private UserModule mUserModule;
//	private UserData mUserData;
//	private SayModule mSayModule;
//	private SayData mSayData;
//	private int mFrom;
//	public static final int HIDE_ATTENTION = 1;
//	public static final int SHOW_ATTENTION = 2;
//	private IAddShowLocationViewService addShowLocationViewService;
//	private int mLayoutWidth;
//	private boolean mFirstInit = true;
//	private boolean mIsShowTime;
//
//	public AttentionListViewAdapter(Context context, int from,boolean isShowTime,
//			IAddShowLocationViewService addShowLocationViewService) {
//		mContext = context;
//		mFrom = from;
//		mIsShowTime=isShowTime;
//		this.addShowLocationViewService = addShowLocationViewService;
//		_sayVos = new ArrayList<PetalkVo>();
//		mUserModule = UserModule.getSingleton();
//		mSayModule = SayModule.getSingleton();
//		drawable = mContext.getResources().getDrawable(R.drawable.label);
//		mLayoutWidth = PublicMethod.getDisplayWidth(mContext);//- PublicMethod.getDiptopx(mContext, 10);
//	}
//
//	public void refreshData(List<PetalkVo> sayVos) {
//		_sayVos.clear();
//		if (sayVos != null && sayVos.size() > 0) {
//			UserManager.getSingleton().addFocusAndStepByList(sayVos);
//			_sayVos.addAll(sayVos);
//			notifyDataSetChanged();
//		}
//	}
//
//	public void addMore(List<PetalkVo> sayVos) {
//		if (sayVos != null && sayVos.size() > 0) {
//			UserManager.getSingleton().addFocusAndStepByList(sayVos);
//			_sayVos.addAll(sayVos);
//			notifyDataSetChanged();
//		} else {
//			PublicMethod.showToast(mContext, R.string.no_more);
//		}
//	}
//
//	public void clear() {
//		_sayVos.clear();
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public int getCount() {
//		if (null == _sayVos || _sayVos.isEmpty()) {
//			return 0;
//		}
//		return _sayVos.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		if (null == _sayVos || _sayVos.isEmpty()) {
//			return null;
//		}
//		return _sayVos.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		final Holder holder;
//		if (null == convertView) {
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.attention_list_item, null);
//			holder = new Holder();
//			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
//			PublicMethod.initPetalkViewLayout(holder.imgPet, mLayoutWidth);
//			holder.img_header = (ImageView) convertView.findViewById(R.id.img_header);
//			holder.imgSex=(ImageView) convertView.findViewById(R.id.img_sex);
//			holder.tvAge=(TextView) convertView.findViewById(R.id.tv_age);
//			holder.tvGrade=(TextView) convertView.findViewById(R.id.tv_grade);
//			holder.imgGrade=(ImageView) convertView.findViewById(R.id.img_grade);
//			holder.tagView = (TagView) convertView.findViewById(R.id.tagview);
//			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
//			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
//			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//
//			holder.tvAttention = (AttentionButtonView) convertView.findViewById(R.id.btn_attention);
//			holder.functionBarView=(FunctionBarView) convertView.findViewById(R.id.functionbar);
//			holder.stepAnimView = (StepAnimView) convertView.findViewById(R.id.stepanim);
//			holder.functionBarView.setStepAnimView(holder.stepAnimView);
//			holder.functionBarEventHolder = new ListItemFunctionBarEventHolder(mContext, holder.functionBarView, addShowLocationViewService);
//			holder.rLayoutForward=(RelativeLayout) convertView.findViewById(R.id.rlayout_forward);
//			holder.amGif = (AudioGifView) convertView.findViewById(R.id.am_gif);
//			holder.tvForwardName = (TextView) convertView.findViewById(R.id.tv_forward_name);
//			holder.tvForwardTime = (TextView) convertView.findViewById(R.id.tv_forward_time);
//			holder.tvForwardContent = (TextView) convertView.findViewById(R.id.tv_forward_content);
//			ExProgressBar progressBar = (ExProgressBar) convertView.findViewById(R.id.pro_loaderpro);
//			ImageView playView = (ImageView) convertView.findViewById(R.id.img_play);
//			LayoutParams params = (LayoutParams) playView.getLayoutParams();
//			params.topMargin = (mLayoutWidth - PublicMethod.getDiptopx(mContext, 100))/2;//PublicMethod.getDiptopx(mContext, 60);
//			holder.amGif.setPlayBtnView(playView);
//			ImageLoaderListener listener = new ImageLoaderListener(progressBar);
//			holder.amGif.setImageLoaderListener(listener);
//			// PicassoCallback callback = new PicassoCallback(progressBar);
//			// holder.amGif.setPicassoCallback(callback);
//			holder.imgPet.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickGifView(holder.amGif);
//				}
//			});
//			convertView.setTag(holder);
//		} else
//			holder = (Holder) convertView.getTag();
//		final PetalkVo sayVo = _sayVos.get(position);
//		final PetVo petVo = sayVo.getPet();
//
//		if (mFrom == SHOW_ATTENTION) {
//			if (UserManager.getSingleton().isAlreadyFocus(petVo.getId())) {
//				holder.tvAttention.setVisibility(View.GONE);
//			} else {
//				holder.tvAttention.setVisibility(View.VISIBLE);
//			}
//		} else {
//			holder.tvAttention.setVisibility(View.GONE);
//		}
//
//		if (sayVo.getType().equals(Constants.RELAY)&& null != sayVo.getComment()) {
//			holder.rLayoutForward.setVisibility(View.VISIBLE);
//			holder.tvForwardName.setText(sayVo.getAimPetNickName());
//			holder.tvForwardTime.setText(PublicMethod.calculateTime(sayVo.getRelayTime()));
//			String comment = sayVo.getComment();
//			if (comment.trim().equals("")) {
//				holder.tvForwardContent.setVisibility(View.GONE);
//			} else {
//				holder.tvForwardContent.setVisibility(View.VISIBLE);
//				holder.tvForwardContent.setText(comment);
//			}
//		} else {
//			holder.rLayoutForward.setVisibility(View.GONE);
//		}
//		
//		if (sayVo.getDescription().trim().equals("")) {
//			holder.tvContent.setVisibility(View.GONE);
//		}else {
//			holder.tvContent.setVisibility(View.VISIBLE);
//			holder.tvContent.setText(sayVo.getDescription());
//		}
////		holder.tvContent.setText(sayVo.getDescription());
//		if (mIsShowTime) {
//			holder.tvDate.setVisibility(View.VISIBLE);
//		}else {
//			holder.tvDate.setVisibility(View.GONE);
//		}
//		holder.tvDate.setText( PublicMethod.calculateTime(sayVo.getCreateTime()));
//		holder.tvName.setText(petVo.getNickName());
//		if (petVo.getGender()==0) {
//			holder.imgSex.setVisibility(View.VISIBLE);
//			holder.imgSex.setImageResource(R.drawable.female);
//			
//		}else if (petVo.getGender()==1) {
//			holder.imgSex.setVisibility(View.VISIBLE);
//			holder.imgSex.setImageResource(R.drawable.male);
//		}else {
//			holder.imgSex.setVisibility(View.GONE);
//		}
//		if (petVo.getIntGrade()<=0) {
//			holder.tvAge.setText(Constants.petMap.get(petVo.getType()));
//			holder.imgGrade.setVisibility(View.GONE);
//			holder.tvGrade.setVisibility(View.GONE);
//		}else {
//			holder.tvAge.setText(Constants.petMap.get(petVo.getType())+"｜");
//			holder.tvGrade.setVisibility(View.VISIBLE);
//			if (-1==petVo.getLevenIconResId()) {
////				holder.imgGrade.setImageDrawable(null);
//				holder.imgGrade.setVisibility(View.GONE);
//			}else {
//				holder.imgGrade.setVisibility(View.VISIBLE);
//				holder.imgGrade.setImageResource(petVo.getLevenIconResId());
//			}
//			holder.tvGrade.setText("Lv"+petVo.getIntGrade());
//		}
//		holder.functionBarEventHolder.setSayVo(sayVo);
//		holder.functionBarView.setValue(sayVo.getCounter().getRelay(), sayVo.getCounter().getComment(),sayVo.getCounter().getFavour(), sayVo.getCounter().getShare());
//		holder.functionBarView.setStepStatus(sayVo.getPetalkId());
//
//		holder.tvAttention.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (UserManager.getSingleton().isLoginStatus()) {
//					holder.tvAttention.setClickable(false);
//					mUserData = new UserData(new MyHandler(holder, position));
//					// if (sayVo.getRs()==0) {
//					mUserModule.addListener(mUserData);
//					mUserModule.focus(mUserData, petVo.getId(), UserManager.getSingleton().getActivePetInfo().getId());
//					// }
//					// else if (sayVo.getRs()==1||sayVo.getRs()==2) {
//					// //TODO 取消关注
//					// mUserModule.addListener(mUserData);
//					// mUserModule.cancleFocus(mUserData,petInfo.getId(),UserManager.getSingleton().getActivePetInfo().getId());
//					// }
//
//				} else {
//					Intent intent = new Intent(mContext,UserLogin_Activity.class);
//					mContext.startActivity(intent);
//				}
//
//			}
//		});
//
//		holder.img_header.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,petVo);
//			}
//		});
//		
//		holder.tagView.removeAllViews();
//		if (null != sayVo.getTags() && sayVo.getTags().length > 0) {
//			holder.tagView.setTags(sayVo.getTags(), holder.tagView.getTagBgResId(mContext),holder.tagView.Use_SayList, mLayoutWidth);
//		}
//
//		ImageLoaderListener listener = holder.amGif.getImageLoaderListener();
//		listener.reset();
//		ImageLoaderHelp.displayContentImage(sayVo.getPhotoUrl(), holder.imgPet,listener, listener);
//		ImageLoaderHelp.displayHeaderImage(sayVo.getPetHeadPortrait(), holder.img_header);
//
//		// 初始化Gif的Item的布局
//
//		android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) holder.amGif.getLayoutParams();
//		holder.amGif.initData(sayVo, mLayoutWidth, mLayoutWidth);
//		PetalkDecorationVo ad = sayVo.getDecorations()[0];
//		PublicMethod.updateGifItemLayout(mLayoutWidth, mLayoutWidth, ad,holder.amGif, params);
//		boolean autoPlay = GifViewManager.getInstance().getAllowAutoPlay();
//		holder.amGif.setPlayBtnVisibility(!autoPlay);
//		if (position == 0 && mFirstInit && autoPlay) {
//			GifViewManager.getInstance().playGif(holder.amGif);
//			mFirstInit = false;
//		}
//		return convertView;
//	}
//
//	private void onClickGifView(AudioGifView view) {
//		GifViewManager.getInstance().playGif(view);
//	}
//
//	private class Holder {
//		private ImageView imgPet, img_header,imgSex;
//		private TextView tvContent;
//		private TextView tvForwardName, tvForwardTime, tvForwardContent;
//		private TagView tagView;
//		private TextView tvName,tvAge,tvGrade;
//		private ImageView imgGrade;
//		private TextView tvDate;
//		private AttentionButtonView tvAttention;
//		private RelativeLayout rLayoutForward;
//		private FunctionBarView functionBarView;
//		private StepAnimView stepAnimView;
//		private ListItemFunctionBarEventHolder functionBarEventHolder;
//		private AudioGifView amGif;
//	}
//
//	private class MyHandler extends Handler {
//		Holder _holder;
//		int _position;
//
//		public MyHandler(Holder holder, int position) {
//			_holder = holder;
//			_position = position;
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case SayData.STEP_SUCCESS:
//				mSayModule.removeListener(mSayData);
//				UserManager.getSingleton().addStepByPetalkVo(_sayVos.get(_position));
//				// int stepCount = _sayVos.get(_position).getFavorNum();
//				// _sayVos.get(_position).setFavorNum(stepCount + 1);
//				// _holder.functionBarView.addStepCount();
//				// _holder.imgStep.setImageResource(R.drawable.step_already);
//				_sayVos.get(_position).setZ(1);
//				break;
//			case SayData.STEP_FAILED:
//				mSayModule.removeListener(mSayData);
//				break;
//			case UserData.FOCUS_SUCCESS:
//				PublicMethod.showToast(mContext, R.string.add_attention_success);
//				mUserModule.removeListener(mUserData);
//				int focusFlag = _sayVos.get(_position).getRs();
//				_sayVos.get(_position).setRs(1);
//				_holder.tvAttention.startAnim();
//				UserManager.getSingleton().addFocusBySayVo(_sayVos.get(_position));
//				// _holder.tvAttention.setBackgroundResource(R.drawable.guanzhu_already);
//				// _holder.tvAttention.setText("已关注");
//				break;
//			case UserData.FOCUS_FAILED:
//				mUserModule.removeListener(mUserData);
//				_holder.tvAttention.setClickable(true);
//				PublicMethod.showToast(mContext, R.string.add_attention_failed);
//				break;
//			case UserData.CANCLE_FOCUS_SUCCESS:
//				mUserModule.removeListener(mUserData);
//				focusFlag = _sayVos.get(_position).getRs();
//				_sayVos.get(_position).setRs(0);
//				// _holder.tvAttention.setBackgroundResource(R.drawable.guanzhu_normal);
//				// _holder.tvAttention.setText("+关注");
//				break;
//			case UserData.CANCLE_FOCUS_FAILED:
//				mUserModule.removeListener(mUserData);
//
//				break;
//			case SayData.SHARE_SUCCESS:
//				mSayModule.removeListener(mSayData);
//				int shareCount = _sayVos.get(_position).getCounter().getShare();
//				_sayVos.get(_position).getCounter().setShare(shareCount + 1);
//				_holder.functionBarView.addShareCount();
//				break;
//			case SayData.SHARE_FAILED:
//				mSayModule.removeListener(mSayData);
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//}
