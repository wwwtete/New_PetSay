package cn.sharesdk.onekeyshare;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.petsay.R;
import com.petsay.application.PetSayApplication;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.TalkDTO;
import com.petsay.vo.forum.TopicDTO;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.shop.GoodsVo;

public class Share {
	private Context mContext;
	private ShareCallback mShareCallback;
	private final int SHARE_COMPLETE=111;
	private final int SHARE_ERROR=112;
	private final int SHARE_CANCLE=113;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHARE_COMPLETE:
				PublicMethod.showToast(mContext, "分享成功");
				break;
			case SHARE_ERROR:
				PublicMethod.showToast(mContext, "分享失败");
				break;
			case SHARE_CANCLE:
				PublicMethod.showToast(mContext, "已取消分享");
				break;
			}
		};
	};
	public  void showShare(final Activity activity,PetalkVo sayVo,String platformName) {
		mContext=activity;
//		final ProgressDialog dialog=PublicMethod.creageProgressDialog(context);
		Platform platform = ShareSDK.getPlatform (platformName);
		String nick=sayVo.getPetNickName();
		String title="听"+nick+"的宠物说";
		String content="";
		HashMap<String, Object> shareContent=new HashMap<String, Object>();
		if (platformName==SinaWeibo.NAME) {
			content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”"+ Constants.HTML5_URL+sayVo.getPetalkId();
		}else if(platformName==WechatMoments.NAME) {
			content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
		}else if(platformName==Wechat.NAME) {
			content="听，爱宠有话说。快来围观>>分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
		}else{
			content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
		}
		shareContent.put("title", title);
		shareContent.put("folderPath", title);
		shareContent.put("titleUrl", Constants.HTML5_URL+sayVo.getPetalkId());
		shareContent.put("text", content);
		shareContent.put("url", Constants.HTML5_URL+sayVo.getPetalkId());
		shareContent.put("comment", "");
		shareContent.put("site", activity.getString(R.string.app_name));
		shareContent.put("siteUrl", Constants.HTML5_URL+sayVo.getPetalkId());
		shareContent.put("imageUrl", sayVo.getThumbUrl());
		HashMap<Platform, HashMap<String, Object>> map=new HashMap<Platform, HashMap<String,Object>>();
		map.put(platform, shareContent);
		
		OnekeyShare oks=new OnekeyShare();
		oks.setActivity(activity);
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
//				arg1:9   {"error":"{\"error\":\"invalid_access_token\",\"error_code\":21332,\"request\":\"\/2\/statuses\/upload_url_text.json\"}","status":403}
				handler.sendEmptyMessage(SHARE_ERROR);
				mShareCallback.shareError(arg0, arg1, arg2);
//				PublicMethod.closeProgressDialog(dialog, context);
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				handler.sendEmptyMessage(SHARE_COMPLETE);
				mShareCallback.shareComplete(arg0, arg1, arg2);
//				PublicMethod.closeProgressDialog(dialog, context);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				handler.sendEmptyMessage(SHARE_CANCLE);
				mShareCallback.shareCancel(arg0, arg1);
//				PublicMethod.closeProgressDialog(dialog, context);
			}
		});
		oks.share(map);
   }
	
	public  void shareMore(final Activity activity,PetalkVo sayVo){
		mContext=activity;
		HashMap<Platform, HashMap<String, Object>> map=new HashMap<Platform, HashMap<String,Object>>();
		for (int i = 0; i < PetSayApplication.getInstance().platformNames.size(); i++) {
			String platformName = PetSayApplication.getInstance().platformNames.get(i);
			String nick=sayVo.getPetNickName();
			String title="听"+nick+"的宠物说";
			String content="";
			HashMap<String, Object> shareContent=new HashMap<String, Object>();
			if (platformName==SinaWeibo.NAME) {
				content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”"+ Constants.HTML5_URL+sayVo.getPetalkId();
			}else if(platformName==WechatMoments.NAME) {
				content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
			}else if(platformName==Wechat.NAME) {
				content="听，爱宠有话说。快来围观>>分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
			}else{
				content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
			}
			shareContent.put("title", title);
			shareContent.put("folderPath", title);
			shareContent.put("titleUrl", Constants.HTML5_URL+sayVo.getPetalkId());
			shareContent.put("text", content);
			shareContent.put("url", Constants.HTML5_URL+sayVo.getPetalkId());
			shareContent.put("comment", "");
			shareContent.put("site", activity.getString(R.string.app_name));
			shareContent.put("siteUrl", Constants.HTML5_URL+sayVo.getPetalkId());
			shareContent.put("imageUrl", sayVo.getThumbUrl());
			
			map.put(ShareSDK.getPlatform (platformName), shareContent);
		}
		
		
		
//		Share.showShareAll(mContext, sayVo.getTheID(),  sayVo.getThumbUrl());
		
		OnekeyShare oks=new OnekeyShare();
		oks.setActivity(activity);
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
//				arg2.getMessage();
				handler.sendEmptyMessage(SHARE_ERROR);
//				PublicMethod.closeProgressDialog(dialog, context);
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				handler.sendEmptyMessage(SHARE_COMPLETE);
				
//				PublicMethod.closeProgressDialog(dialog, context);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				handler.sendEmptyMessage(SHARE_CANCLE);
//				PublicMethod.closeProgressDialog(dialog, context);
			}
		});
		oks.share(map);
	}
	
	
	public  void showShareFreeBuy(final Activity activity,String platformName,GoodsVo goodsVo) {
		mContext=activity;
//		final ProgressDialog dialog=PublicMethod.creageProgressDialog(context);
		Platform platform = ShareSDK.getPlatform (platformName);
//		String nick=sayVo.getPetNickName();
		String title="【免费试用】“商品名称商品名称”可以免费领取啦，有钱就任性，有领就敢送。";
		String content="【免费试用】“商品名称商品名称”可以免费领取啦，有钱就任性，有领就敢送。";
		HashMap<String, Object> shareContent=new HashMap<String, Object>();
		if (platformName==SinaWeibo.NAME) {
			content="【免费试用】“商品名称商品名称”可以免费领取啦，有钱就任性，有领就敢送。"+Constants.SHARESHOPURL+goodsVo.getCode()+".html";//+ Constants.HTML5_URL+sayVo.getPetalkId();
		}//else if(platformName==WechatMoments.NAME) {
//			content="【免费试用】“商品名称商品名称”可以免费领取啦，有钱就任性，有领就敢送。";
//		}else if(platformName==Wechat.NAME) {
//			content="听，爱宠有话说。快来围观>>分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
//		}else{
//			content="听，爱宠有话说。分享自"+nick+"的宠物说：“"+sayVo.getDescription()+"”";
//		}
		shareContent.put("title", title);
		shareContent.put("folderPath", title);
		shareContent.put("titleUrl", Constants.SHARESHOPURL+goodsVo.getCode()+".html");
		shareContent.put("text", content);
		shareContent.put("url", Constants.SHARESHOPURL+goodsVo.getCode()+".html");
		shareContent.put("comment", "");
		shareContent.put("site", activity.getString(R.string.app_name));
		shareContent.put("siteUrl", Constants.SHARESHOPURL+goodsVo.getCode()+".html");
		String imgUrl=goodsVo.getCoverUrl();
		if (null==imgUrl) {
			imgUrl="http://petalk.qiniudn.com/icon-256.png";
		}
		shareContent.put("imageUrl",imgUrl);
		HashMap<Platform, HashMap<String, Object>> map=new HashMap<Platform, HashMap<String,Object>>();
		map.put(platform, shareContent);
		
		OnekeyShare oks=new OnekeyShare();
		oks.setActivity(activity);
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform platform, int arg1, Throwable arg2) {
//				arg1:9   {"error":"{\"error\":\"invalid_access_token\",\"error_code\":21332,\"request\":\"\/2\/statuses\/upload_url_text.json\"}","status":403}

//				handler.sendEmptyMessage(SHARE_ERROR);
//				PublicMethod.closeProgressDialog(dialog, context);
				mShareCallback.shareError(platform, arg1, arg2);
			}
			
			@Override
			public void onComplete(Platform platform, int arg1, HashMap<String, Object> arg2) {
//				handler.sendEmptyMessage(SHARE_COMPLETE);
//				PublicMethod.closeProgressDialog(dialog, context);
				mShareCallback.shareComplete(platform, arg1, arg2);
			}
			
			@Override
			public void onCancel(Platform platform, int arg1) {
//				handler.sendEmptyMessage(SHARE_CANCLE);
//				PublicMethod.closeProgressDialog(dialog, context);
				mShareCallback.shareCancel(platform, arg1);
			}
		});
		oks.share(map);
   }
	
	
	public  void showShareTopic(final Activity activity,String platformName,TopicDTO topicDTO) {
		mContext=activity;
//		final ProgressDialog dialog=PublicMethod.creageProgressDialog(context);
		Platform platform = ShareSDK.getPlatform (platformName);
//		String nick=sayVo.getPetNickName();
		String title=topicDTO.getContent();
		String content="互动吧—发现更多养宠秘籍 ";
		HashMap<String, Object> shareContent=new HashMap<String, Object>();
//		if(platformName==WechatMoments.NAME||platformName==Wechat.NAME) {
//				content="互动吧—发现更多养宠秘籍 ";
//		}
		shareContent.put("title", title);
		shareContent.put("folderPath", title);
		shareContent.put("titleUrl",Constants.SHARETOPICTALKURL+topicDTO.getId());
		shareContent.put("text", content);
		shareContent.put("url", Constants.SHARETOPICTALKURL+topicDTO.getId());
		shareContent.put("comment", "");
		shareContent.put("site", activity.getString(R.string.app_name));
		shareContent.put("siteUrl", Constants.SHARETOPICTALKURL+topicDTO.getId());
		String imgUrl=topicDTO.getPic();
		if (null==imgUrl) {
			imgUrl="http://petalk.qiniudn.com/icon-256.png";
		}
		shareContent.put("imageUrl",imgUrl);
		HashMap<Platform, HashMap<String, Object>> map=new HashMap<Platform, HashMap<String,Object>>();
		map.put(platform, shareContent);
		
		OnekeyShare oks=new OnekeyShare();
		oks.setActivity(activity);
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform platform, int arg1, Throwable arg2) {
//				arg1:9   {"error":"{\"error\":\"invalid_access_token\",\"error_code\":21332,\"request\":\"\/2\/statuses\/upload_url_text.json\"}","status":403}

//				handler.sendEmptyMessage(SHARE_ERROR);
//				PublicMethod.closeProgressDialog(dialog, context);
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_ERROR);
				}else
				    mShareCallback.shareError(platform, arg1, arg2);
			}
			
			@Override
			public void onComplete(Platform platform, int arg1, HashMap<String, Object> arg2) {
//				mShareCallback.shareComplete(platform, arg1, arg2);
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_COMPLETE);
				}else {
					mShareCallback.shareComplete(platform, arg1, arg2);
				}
			}
			
			@Override
			public void onCancel(Platform platform, int arg1) {
//				mShareCallback.shareCancel(platform, arg1);
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_CANCLE);
				}else {
					mShareCallback.shareCancel(platform, arg1);
				}
			}
		});
		oks.share(map);
   }
	
	public  void showShareTopicOne(final Activity activity,String platformName,TopicDTO topicDTO , TalkDTO talkDTO) {
		mContext=activity;
//		final ProgressDialog dialog=PublicMethod.creageProgressDialog(context);
		Platform platform = ShareSDK.getPlatform (platformName);
//		String nick=sayVo.getPetNickName();
		String title=topicDTO.getContent();
		String content="互动吧—发现更多养宠秘籍 ";
		HashMap<String, Object> shareContent=new HashMap<String, Object>();
//		if(platformName==WechatMoments.NAME||platformName==Wechat.NAME) {
//				content="互动吧—发现更多养宠秘籍 ";
//		}
		shareContent.put("title", title);
		shareContent.put("folderPath", title);
		shareContent.put("titleUrl",Constants.SHARETOPCOMMENTURL+talkDTO.getId());
		shareContent.put("text", content);
		shareContent.put("url", Constants.SHARETOPCOMMENTURL+talkDTO.getId());
		shareContent.put("comment", "");
		shareContent.put("site", activity.getString(R.string.app_name));
		shareContent.put("siteUrl", Constants.SHARETOPCOMMENTURL+talkDTO.getId());
		String imgUrl=topicDTO.getPic();
		if (null==imgUrl) {
			imgUrl="http://petalk.qiniudn.com/icon-256.png";
		}
		shareContent.put("imageUrl",imgUrl);
		HashMap<Platform, HashMap<String, Object>> map=new HashMap<Platform, HashMap<String,Object>>();
		map.put(platform, shareContent);
		
		OnekeyShare oks=new OnekeyShare();
		oks.setActivity(activity);
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform platform, int arg1, Throwable arg2) {
//				arg1:9   {"error":"{\"error\":\"invalid_access_token\",\"error_code\":21332,\"request\":\"\/2\/statuses\/upload_url_text.json\"}","status":403}

//				
//				PublicMethod.closeProgressDialog(dialog, context);
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_ERROR);
				}else
				    mShareCallback.shareError(platform, arg1, arg2);
			}
			
			@Override
			public void onComplete(Platform platform, int arg1, HashMap<String, Object> arg2) {
//				
//				PublicMethod.closeProgressDialog(dialog, context);
				
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_COMPLETE);
				}else {
					mShareCallback.shareComplete(platform, arg1, arg2);
				}
			}
			
			@Override
			public void onCancel(Platform platform, int arg1) {
//				
//				PublicMethod.closeProgressDialog(dialog, context);
				
				if (null==mShareCallback) {
					handler.sendEmptyMessage(SHARE_CANCLE);
				}else {
					mShareCallback.shareCancel(platform, arg1);
				}
			}
		});
		oks.share(map);
   }
	
	
	public void setShareCallback(ShareCallback shareCallback){
		mShareCallback=shareCallback;
	}
	
	public interface ShareCallback{
		void shareError(Platform platform, int arg1, Throwable arg2);
		void shareComplete(Platform platform, int arg1, HashMap<String, Object> arg2);
		void shareCancel(Platform platform, int arg1);
	}
}
