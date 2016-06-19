package com.petsay.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.petsay.R;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.PMessage;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.user.UserInfo;
import com.petsay.vo.petalk.PetalkVo;

public class PushDemoReceiver extends BroadcastReceiver implements NetCallbackInterface {
//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
//		PetsayLog.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
			
			if (payload != null) {
				String data = new String(payload);
//				PetsayLog.d("GetuiSdkDemo", "Got Payload:" + data);
				PMessage pMessage=JsonUtils.resultData(data, PMessage.class);
				showNotify(context,pMessage);
//				if (GetuiSdkDemoActivity.tLogView != null)
//					GetuiSdkDemoActivity.tLogView.append(data + "\n");
			}
			
			
			
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			if (null==mUserNet) {
				mUserNet=new UserNet();
				mUserNet.setCallback(this);
				mUserNet.setTag(this);
			}
			
			if (UserManager.getSingleton().isLoginStatus()) {
				mUserNet.messageMPTS(UserManager.getSingleton().getUserInfo().getId(), UserManager.getSingleton().getActivePetId(), cid);
			}else {
				mUserNet.messageMPTS("", "", cid);
			}
			
			
			
//			if (GetuiSdkDemoActivity.tView != null)
//				GetuiSdkDemoActivity.tView.setText(cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			PetsayLog.d("GetuiSdkDemo", "appid = " + appid);
			PetsayLog.d("GetuiSdkDemo", "taskid = " + taskid);
			PetsayLog.d("GetuiSdkDemo", "actionid = " + actionid);
			PetsayLog.d("GetuiSdkDemo", "result = " + result);
			PetsayLog.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
			break;
		default:
			break;
		}
	}
	
	
	private void showNotify(Context context,PMessage pMessage){
		UserInfo temp=null;
		DataFileCache.initContext(context);
		try {
			temp = (UserInfo) DataFileCache.getSingleton().loadObject(Constants.UserFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null!=temp&&temp.isLogin()) {
			if (SharePreferenceCache.getSingleton(context).isRunningApp()) {
				//当前应用正在运行,不需要通知栏通知
				
				Intent intent = new Intent("com.petsay.msg");
                context.sendBroadcast(intent);
				
			}else {
				//宠物说当前没有运行,需要通知栏通知
				//消息通知栏

		        //定义NotificationManager
		        String ns = Context.NOTIFICATION_SERVICE;
		        NotificationManager mNotificationManager = (NotificationManager)context. getSystemService(ns);
		        //定义通知栏展现的内容信息

		        int icon = R.drawable.icon;
		        CharSequence tickerText = context.getResources().getString(R.string.app_name);
		        long when = System.currentTimeMillis();
		        Notification notification = new Notification(icon, tickerText, when);

		        //定义下拉通知栏时要展现的内容信息
		        CharSequence contentTitle =context.getResources().getString(R.string.app_name);
		        CharSequence contentText = pMessage.getContent();
		        Intent notificationIntent ;//= new Intent(context, MainActivity.class);
		        if (pMessage.getType().equals("1")||pMessage.getType().equals("2")||pMessage.getType().equals("3")) {
		        	notificationIntent = new Intent();
		        	notificationIntent.putExtra("fragment", 1);
		        	notificationIntent.setClass(context, MainActivity.class);
				}else if (pMessage.getType().equals("4")) {
		        	notificationIntent = new Intent(context,DetailActivity.class);
		        	notificationIntent.putExtra("from", DetailActivity.FROM_PUSH);
		        	
		    		PetalkVo sayVo = new PetalkVo();
		    		sayVo.setPetalkId(pMessage.getId());
		    		Constants.Detail_Sayvo = sayVo;
				}else if(pMessage.getType().equals("5")) {
					notificationIntent = new Intent();
					notificationIntent.setClass(context, TagSayListActivity.class);
					notificationIntent.putExtra("folderPath", pMessage.getContent());
					notificationIntent.putExtra("from", TagSayListActivity.FROM_PUSH);
					notificationIntent.putExtra("id", pMessage.getId());
				}else if (pMessage.getType().equals("6")) {
					notificationIntent = new Intent(context, WebViewActivity.class);
					notificationIntent.putExtra("content", pMessage.getContent());
					notificationIntent.putExtra("from", WebViewActivity.FROM_PUSH);
				}else if (pMessage.getType().equals("7")) {
					notificationIntent = new Intent(context, MainActivity.class);
					notificationIntent.putExtra("announcement", true);
					notificationIntent.putExtra("content", pMessage.getContent());
				}else{
					//只跳转 不处理
					notificationIntent = new Intent(context, MainActivity.class);
//					notificationIntent.putExtra("announcement", true);
//					notificationIntent.putExtra("content", pMessage.getContent());
				}
		        
		      
//		        int notifyId=Integer.parseInt(pMessage.getId());
		        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		        notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent); 
		        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
		        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		        notification.flags |= Notification.FLAG_NO_CLEAR;
		        notification.defaults |=Notification.DEFAULT_LIGHTS;
		        notification.defaults |=Notification.DEFAULT_SOUND;
		        notification.defaults |=Notification.DEFAULT_VIBRATE;
//		        notification.ledARGB = Color.BLUE;   
//		        notification.ledOnMS =5000; //闪光时间，毫秒
//		        long[] vibrate = {0,300,300,300};    
//		        notification.vibrate=vibrate;
		        mNotificationManager.notify(0, notification);
			}
		}
		
		
       
	}


	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		// TODO Auto-generated method stub
		
	}
}
