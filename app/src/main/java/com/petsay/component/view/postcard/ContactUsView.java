package com.petsay.component.view.postcard;

import com.petsay.R;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.activity.shop.ExchangeHistoryActivity;
import com.petsay.activity.shop.GoodsDetailActivity;
import com.petsay.application.UserManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.constants.Constants;
import com.petsay.utile.ToastUtiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ContactUsView extends LinearLayout implements android.view.View.OnClickListener{
	private ImageView mIvTalkAssistant,mIvTalkTel,mIvTalkQQ;
	private Context mContext;
	private DialogPopupWindow mDialogPopupWindow;
	public ContactUsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		inflate(context, R.layout.view_contact_us, this);
		mIvTalkAssistant=(ImageView) findViewById(R.id.img_talkAssistant);
		mIvTalkTel=(ImageView) findViewById(R.id.img_talkTel);
		mIvTalkQQ=(ImageView) findViewById(R.id.img_talkQQ);
		
		setListener();
	}
	
	public void initPopup(IAddShowLocationViewService locationViewService){
		mDialogPopupWindow=new DialogPopupWindow(mContext, locationViewService);
		mDialogPopupWindow.setOnClickListener(this);
	}
	
	private void setListener(){
		mIvTalkAssistant.setOnClickListener(this);
		mIvTalkTel.setOnClickListener(this);
		mIvTalkQQ.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_talkAssistant:
			Intent intent = new Intent(mContext,ChatActivity.class);
	        intent.putExtra("petid", Constants.OFFICIAL_ID);
	        mContext.startActivity(intent);
			break;
		case R.id.img_talkQQ:
			try {
				String url="mqqwpa://im/chat?chat_type=wpa&uin=3028317583";  
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));  
			} catch (Exception e) {
				ToastUtiles.showDefault(mContext, "跳转QQ出错");
			}
			
			break;
		case R.id.img_talkTel:
			mDialogPopupWindow.setPopupText(1,"提示", "确定要拨打电话010-67528566吗？", "拨打", "不了");
			mDialogPopupWindow.show();
			break;
			
		case R.id.tv_dialog_ok:
			 //用intent启动拨打电话  
			mDialogPopupWindow.dismiss();
            intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:010-67528566"));  
            mContext.startActivity(intent); 
			break;
		case R.id.tv_dialog_cancle:
			mDialogPopupWindow.dismiss();
			break;
		default:
			break;
		}
		
	}

}
