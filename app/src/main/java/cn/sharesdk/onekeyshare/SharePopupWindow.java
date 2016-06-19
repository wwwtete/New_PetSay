package cn.sharesdk.onekeyshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.Share.ShareCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.component.view.BasePopupWindow;
import com.petsay.constants.Constants;
import com.petsay.vo.forum.TalkDTO;
import com.petsay.vo.forum.TopicDTO;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.shop.GoodsVo;

public class SharePopupWindow extends BasePopupWindow implements OnClickListener{
	private View popupView;
//	private TextView tvDel;
	private View parentView;
	private TextView tvCancle,tvTitle;
	private LinearLayout layoutWechat,layoutWechatM,layoutSina,layoutQQ,layoutShare;
	private PetalkVo mSayVo;
	private Activity mActivity;
	private Share share;
	private boolean isDetailAc=false;
	private boolean isShowTitle=false;
	private int shareType=0;//0说说分享，1试用分享,2话题分享,3讨论列表
	private GoodsVo mGoodsVo;
	private TopicDTO mTopicDTO;
	private TalkDTO mTalkDTO;
	public SharePopupWindow(Context context,PetalkVo sayVo,IAddShowLocationViewService viewService,ShareCallback shareCallback,boolean isDetailAc){
		mSayVo=sayVo;
	    parentView=viewService.getParentView();
	    mActivity=viewService.getActivity();
	    share=new Share();
	    share.setShareCallback(shareCallback);
	    this.isDetailAc=isDetailAc;
	    shareType=0;
	    init();
	    
	}
	public SharePopupWindow(Context context,IAddShowLocationViewService viewService,ShareCallback shareCallback,GoodsVo goodsVo,boolean isShowTitle){
	    parentView=viewService.getParentView();
	    mActivity=viewService.getActivity();
	    share=new Share();
	    mGoodsVo=goodsVo;
	    share.setShareCallback(shareCallback);
	    this.isShowTitle=isShowTitle;
	    shareType=1;
	    init();
	    
	}
	
	public SharePopupWindow(Context context,IAddShowLocationViewService viewService,ShareCallback shareCallback, TopicDTO topicDTO,boolean isShowTitle){
	    parentView=viewService.getParentView();
	    mActivity=viewService.getActivity();
	    share=new Share();
	    mTopicDTO=topicDTO;
	    share.setShareCallback(shareCallback);
	    this.isShowTitle=isShowTitle;
	    shareType=2;
	    init();
	    
	}
	public SharePopupWindow(Context context,IAddShowLocationViewService viewService,ShareCallback shareCallback, TopicDTO topicDTO,TalkDTO talkDTO,boolean isShowTitle){
	    parentView=viewService.getParentView();
	    mActivity=viewService.getActivity();
	    share=new Share();
	    mTopicDTO=topicDTO;
	    mTalkDTO=talkDTO;
	    share.setShareCallback(shareCallback);
	    this.isShowTitle=isShowTitle;
	    shareType=3;
	    init();
	    
	}
	private void init(){
		popupView=LayoutInflater.from(mActivity).inflate(R.layout.share_popup, null);
		layoutShare=(LinearLayout) popupView.findViewById(R.id.layout_forward);
		layoutQQ=(LinearLayout) popupView.findViewById(R.id.layout_qq);
		layoutWechat=(LinearLayout) popupView.findViewById(R.id.layout_wechat);
		layoutWechatM=(LinearLayout) popupView.findViewById(R.id.layout_wechatmoments);
		layoutSina=(LinearLayout) popupView.findViewById(R.id.layout_sina);
		tvCancle=(TextView) popupView.findViewById(R.id.tv_cancle);
		tvTitle=(TextView) popupView.findViewById(R.id.tv_popup_title);
		layoutShare.setOnClickListener(this);
		layoutQQ.setOnClickListener(this);
		layoutSina.setOnClickListener(this);
		layoutWechat.setOnClickListener(this);
		layoutWechatM.setOnClickListener(this);
		tvCancle.setOnClickListener(this);
//		tvDel=(TextView) popupView.findViewById(R.id.tv_del);
		this.setContentView(popupView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		popupView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int heightTop = popupView.findViewById(R.id.share_parent).getTop();
				int heightBottom = popupView.findViewById(R.id.share_parent).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < heightTop || y > heightBottom) {
						dismiss();
					}
				}
				return true;
			}
		});
//		tvDel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				 dismiss();
////                 _diagnosis.remove(_position);
//                 _adapter.notifyDataSetChanged();
//			}
//		});
	}
	
	public void isShowForward(boolean isShow){
		if (isShow) {
			layoutShare.setVisibility(View.VISIBLE);
		}else {
			layoutShare.setVisibility(View.GONE);
		}
		
		if (isShowTitle) {
			tvTitle.setVisibility(View.VISIBLE);
		}else {
			tvTitle.setVisibility(View.GONE);
		}
	}
	
	public void show(){
		showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_wechat:
			if (shareType==1) {
				share.showShareFreeBuy(mActivity, Wechat.NAME,mGoodsVo);
			}else if (shareType==2) {
				share.showShareTopic(mActivity, Wechat.NAME,mTopicDTO);
			}else if (shareType==3) {
				share.showShareTopicOne(mActivity, Wechat.NAME,mTopicDTO,mTalkDTO);
			} else {
				share.showShare(mActivity, mSayVo, Wechat.NAME);
			}
			
			break;
		case R.id.layout_wechatmoments:
			if (shareType==1) {
				share.showShareFreeBuy(mActivity, WechatMoments.NAME,mGoodsVo);
			}else if (shareType==2) {
				share.showShareTopic(mActivity, WechatMoments.NAME,mTopicDTO);
			}else if (shareType==3) {
				share.showShareTopicOne(mActivity, WechatMoments.NAME,mTopicDTO,mTalkDTO);
			}else {
				share.showShare(mActivity, mSayVo, WechatMoments.NAME);
			}
			break;
		case R.id.layout_sina:
			if (shareType==1) {
				share.showShareFreeBuy(mActivity, SinaWeibo.NAME,mGoodsVo);
			}else if (shareType==2) {
				share.showShareTopic(mActivity, SinaWeibo.NAME,mTopicDTO);
			}else if (shareType==3) {
				share.showShareTopicOne(mActivity, SinaWeibo.NAME,mTopicDTO,mTalkDTO);
			}else {
				share.showShare(mActivity, mSayVo, SinaWeibo.NAME);
			}
			break;
		case R.id.layout_qq:
			if (shareType==1) {
				share.showShareFreeBuy(mActivity, QQ.NAME,mGoodsVo);
			}else if (shareType==2) {
				share.showShareTopic(mActivity, QQ.NAME,mTopicDTO);
			}else if (shareType==3) {
				share.showShareTopicOne(mActivity, QQ.NAME,mTopicDTO,mTalkDTO);
			}else {
				share.showShare(mActivity, mSayVo, QQ.NAME);
			}
			break;
		case R.id.layout_forward:
//			ClipboardManager clipboardManager = (ClipboardManager)mActivity.getSystemService(Context.CLIPBOARD_SERVICE);  
//			clipboardManager.setText(Constants.HTML5_URL+mSayVo.getPetalkId()); 
//			PublicMethod.showToast(mActivity, "链接已复制到剪贴板");
			onForward(isDetailAc);
			break;
		default:
			break;
		}
		dismiss();
		
	}
	private void onForward(boolean isDetailAc) {
		if (isDetailAc) {
			DetailActivity activity=(DetailActivity) mActivity;
			activity.forward();
		}else {
			UserManager mUserManager = UserManager.getSingleton();
			if (mUserManager.isLoginStatus()) {
				Intent intent = new Intent();
				intent.setClass(mActivity, DetailActivity.class);
				Constants.Detail_Sayvo=mSayVo;
				intent.putExtra("operationType", 1);
				mActivity.startActivity(intent);
			} else {
				Intent intent = new Intent(mActivity,UserLogin_Activity.class);
				mActivity.startActivity(intent);
			}
		}
		
	}
}
