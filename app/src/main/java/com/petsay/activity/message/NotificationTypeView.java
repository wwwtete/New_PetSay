package com.petsay.activity.message;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.chat.ChatMsgListActivity;
import com.petsay.activity.homeview.MainActivity_Deprecated;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.component.view.BottomCameraView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.StringUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.petalk.MessageVo;
import com.petsay.vo.ResponseBean;

/**
 * 通知消息类型界面
 * @author G
 *
 */
@Deprecated
public class NotificationTypeView extends LinearLayout implements OnClickListener, NetCallbackInterface {

	
	private MainActivity_Deprecated mContext;
	private ImageView mImgMsgComment, mImgMsgStep, mImgMsgFocus,mImgMsgAssistant,mImgMsgTalk;
	//未读消息count
	private RelativeLayout mLayoutComment, mLayoutStep, mLayoutFocus,mLayoutAssistant,mLayoutTalk;
	private TextView mTvCommentCount, mTvStepCount, mTvFocusCount,mTvAssistantCount,mTvTalkCount;
	private int commentCount=0,stepCount=0,focusCount=0,assistantCount=0;
//	private UserData mUserData;
//	private UserModule mUserModule;
	private UserNet mUserNet;
    private MessageVo messageVo;
    private SharePreferenceCache mCache;
    private BottomCameraView mBottomView;
    
    private int announcementCount=0;
	public NotificationTypeView(MainActivity_Deprecated context) {
		super(context);
		mContext = context;
		 mCache=SharePreferenceCache.getSingleton(mContext);
		inflate(context, R.layout.notification_type, this);
		initView();
//		changeSkin();
		initData();
//		getAnnouncementCount();
	}

    public void setBottomView(BottomCameraView bottomView){
        this.mBottomView = bottomView;
    }

	private void initView() {
		mLayoutComment = (RelativeLayout) findViewById(R.id.layout_msg_comment);
		mLayoutStep = (RelativeLayout) findViewById(R.id.layout_msg_step);
		mLayoutFocus = (RelativeLayout) findViewById(R.id.layout_msg_focus);
		mLayoutAssistant = (RelativeLayout) findViewById(R.id.layout_msg_assistant);
        mLayoutTalk = (RelativeLayout) findViewById(R.id.layout_msg_talk);


		mImgMsgComment = (ImageView) findViewById(R.id.img_msg_comment);
		mImgMsgStep = (ImageView) findViewById(R.id.img_msg_step);
		mImgMsgFocus = (ImageView) findViewById(R.id.img_msg_focus);
		mImgMsgAssistant = (ImageView) findViewById(R.id.img_msg_assistant);
        mImgMsgTalk = (ImageView) findViewById(R.id.img_msg_talk);

        mTvAssistantCount = (TextView) findViewById(R.id.tv_msg_assistant_count);
		mTvCommentCount = (TextView) findViewById(R.id.tv_msg_comment_count);
		mTvFocusCount = (TextView) findViewById(R.id.tv_msg_focus_count);
		mTvStepCount = (TextView) findViewById(R.id.tv_msg_step_count);
        mTvTalkCount = (TextView) findViewById(R.id.tv_msg_talk_count);

        mLayoutComment.setBackgroundColor(Color.WHITE);
		mLayoutStep.setBackgroundColor(Color.WHITE);
		mLayoutFocus.setBackgroundColor(Color.WHITE);
		mLayoutAssistant.setBackgroundColor(Color.WHITE);
        mLayoutTalk.setBackgroundColor(Color.WHITE);

		setListener();
	}

	private void setListener() {
		mLayoutComment.setOnClickListener(this);
		mLayoutStep.setOnClickListener(this);
		mLayoutFocus.setOnClickListener(this);
		mLayoutAssistant.setOnClickListener(this);
        mLayoutTalk.setOnClickListener(this);
	}

//	/**
//	 * 更换皮肤
//	 */
//	public void changeSkin() {
//		mImgMsgComment.setImageDrawable(SkinHelp.getDrawable(mContext,R.string.msg_comment));
//		mImgMsgStep.setImageDrawable(SkinHelp.getDrawable(mContext,R.string.msg_step));
//		mImgMsgFocus.setImageDrawable(SkinHelp.getDrawable(mContext,R.string.msg_focus));
//		mImgMsgAssistant.setImageDrawable(SkinHelp.getDrawable(mContext,R.string.msg_assistant));
//        mImgMsgTalk.setImageDrawable(SkinHelp.getDrawable(mContext,R.string.msg_talk));
//	}

	private void initData() {
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(mContext);
		
	}
	
	
	public void refreshCount(){
        getAnnouncementCount();
		refreshChatMsgCount();
	}
	
	public void getAnnouncementCount(){
//		mUserModule.addListener(mUserData);
//		mUserModule.announcementCount(mUserData, "");
		mUserNet.announcementCount("");
		
	}
	public void getUserMsgCount(){
//		mUserModule.addListener(mUserData);
//		mUserModule.messageUMCG(mUserData, UserManager.getSingleton().getActivePetId());
	    mUserNet.messageUMCG(UserManager.getSingleton().getActivePetId());
	}

    public void refreshChatMsgCount(){
        int count = ChatDataBaseManager.getInstance().getNewestMsgTotalCount();
        if(count > 0){
            mTvTalkCount.setVisibility(View.VISIBLE);
            mTvTalkCount.setText(StringUtiles.numberFormat(count,99));
        }else {
            mTvTalkCount.setVisibility(GONE);
        }
        updateBottomViewState();
    }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_msg_comment:
			if (mTvCommentCount.getVisibility()==View.VISIBLE) {
				mCache.setLocal_C_RMsgCount(messageVo.getC_R());
				mTvCommentCount.setVisibility(View.GONE);
			}
			Intent intent=new Intent();
			intent.setClass(mContext, MessageActivity.class);
			intent.putExtra("type", Constants.COMMENT_RELAY);
			intent.putExtra("from", Constants.MSG_USER);
			intent.putExtra("folderPath", "评论和转发");
			mContext.startActivity(intent);
			
			break;
		case R.id.layout_msg_step:
			if (mTvStepCount.getVisibility()==View.VISIBLE) {
				mCache.setLocal_FMsgCount(messageVo.getF());
				mTvStepCount.setVisibility(View.GONE);
			}
			intent=new Intent();
			intent.setClass(mContext, MessageActivity.class);
			intent.putExtra("type", Constants.FAVOUR);
			intent.putExtra("from", Constants.MSG_USER);
			intent.putExtra("folderPath", "踩了我");
			mContext.startActivity(intent);
			
			break;
		case R.id.layout_msg_focus:
			if (mTvFocusCount.getVisibility()==View.VISIBLE) {
				mCache.setLocal_FansMsgCount(messageVo.getFans());
				mTvFocusCount.setVisibility(View.GONE);
			}
			intent=new Intent();
			intent.setClass(mContext, MessageActivity.class);
			intent.putExtra("type", Constants.FANS);
			intent.putExtra("from", Constants.MSG_USER);
			intent.putExtra("folderPath", "关注我的");
			mContext.startActivity(intent);
			
			break;
		case R.id.layout_msg_assistant:
			if (mTvAssistantCount.getVisibility()==View.VISIBLE) {
				mCache.setLocal_announcement_MsgCount(announcementCount);
				mTvAssistantCount.setVisibility(View.GONE);
			}
			intent=new Intent();
			intent.setClass(mContext, MessageActivity.class);
            // intent.putExtra("type", Constants.FANS);
			intent.putExtra("folderPath", "宠物说助手");
			intent.putExtra("from", Constants.MSG_ANNOUNCEMENT);
			mContext.startActivity(intent);
			
			break;
            case R.id.layout_msg_talk:
            intent = new Intent();
                intent.setClass(mContext, ChatMsgListActivity.class);
                mContext.startActivity(intent);
                break;
		}

		int totalReadCount = mCache.getLocal_C_RMsgCount()
				+ mCache.getLocal_FansMsgCount() + mCache.getLocal_FMsgCount()
				+ mCache.getLocal_announcementMsgCount();
		if (mCache.getReadMsgCount()<=totalReadCount) {
			mCache.setReadMsgCount(totalReadCount);
		}
        updateBottomViewState();
	}

    private void updateBottomViewState(){
        mBottomView.hasUnreadMsg(isVisibility(mTvAssistantCount) || isVisibility(mTvCommentCount)
                || isVisibility(mTvFocusCount) || isVisibility(mTvStepCount) || isVisibility(mTvTalkCount));
    }

    public boolean isVisibility(View view){
        return view.getVisibility() == VISIBLE;
    }

	private void setMessageCount(){
		
		int local_C_R_MsgCount=mCache.getLocal_C_RMsgCount();
		if (messageVo.getC_R()>local_C_R_MsgCount) {
			commentCount=messageVo.getC_R()-local_C_R_MsgCount;
			mTvCommentCount.setVisibility(View.VISIBLE);
			mTvCommentCount.setText(commentCount+"");
			
		}else {
			mTvCommentCount.setVisibility(View.GONE);
//			mTvCommentCount.setText(local_C_R_MsgCount);
		}
		
		int local_F_MsgCount=mCache.getLocal_FMsgCount();
		if (messageVo.getF()>local_F_MsgCount) {
			stepCount=messageVo.getF()-local_F_MsgCount;
			mTvStepCount.setVisibility(View.VISIBLE);
			mTvStepCount.setText(stepCount+"");
//			mCache.setLocal_FMsgCount(messageVo.getF());
		}else {
			mTvStepCount.setVisibility(View.GONE);
//			mTvStepCount.setText(local_F_MsgCount);
		}
		
		
		int local_fans_MsgCount=mCache.getLocal_FansMsgCount();
		if (messageVo.getFans()>local_fans_MsgCount) {
			focusCount=messageVo.getFans()-local_fans_MsgCount;
			mTvFocusCount.setVisibility(View.VISIBLE);
			mTvFocusCount.setText(focusCount+"");
//			mCache.setLocal_FansMsgCount(messageVo.getFans());
		}else {
			mTvFocusCount.setVisibility(View.GONE);
//			mTvFocusCount.setText(local_fans_MsgCount);
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {

		switch (requestCode) {
		case RequestCode.REQUEST_ANNOUNCEMENTCOUNT:
			SharePreferenceCache mCache=SharePreferenceCache.getSingleton(mContext);
			int localCount=mCache.getLocal_announcementMsgCount();
			try {
				announcementCount=Integer.parseInt(bean.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (announcementCount>localCount) {
				assistantCount=announcementCount-localCount;
				mTvAssistantCount.setText(assistantCount+"");
				mTvAssistantCount.setVisibility(View.VISIBLE);
			}else {
				mTvAssistantCount.setVisibility(View.GONE);
			}
			getUserMsgCount();
			break;
		case RequestCode.REQUEST_MESSAGEUMCG:
			messageVo=JsonUtils.resultData(bean.getValue(), MessageVo.class);
			setMessageCount();
			break;
		default:
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_ANNOUNCEMENTCOUNT:
			getUserMsgCount();
			break;
		case RequestCode.REQUEST_MESSAGEUMCG:
			
			break;
		default:
			break;
		}
	}
}
