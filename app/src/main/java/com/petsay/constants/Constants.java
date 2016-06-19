package com.petsay.constants;
import android.content.Context;

import com.petsay.R;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.xml.SaxPetTypeParse;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.user.PetType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangw
 *	常量类
 */
public class Constants {

	public static boolean isDebug = true;
	public static final String TAG = "petsay";
	public static  String PackageName="com.petsay";
//	public static String VersionName="1.0";
//	public static int VersionCode=1;
	/**文件路径*/
//	public static final String FilePath = Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+"petsay"+File.separator;
	/**文件根路径*/
	public static final String FilePath = "petsay"+ File.separator;
	/**图片sd卡缓存地址*/
	public static final String IMAGE_CACHE_PATH = FilePath + "imagescache"+File.separator;
//	/**饰品路径*/
	public static final String DECORATE = "decorate"+File.separator;
	/**饰品缩略图路径*/
	public static final String THUMBNAIL = DECORATE + "thumbnail"+File.separator;
	/**饰品zip包存放路径*/
	public static final String ZIP = DECORATE + "zip"+File.separator;
	/**饰品解压包路径*/
	public static final String SDCARD_DECORATE_UNZIP = FilePath +DECORATE +"unzip"+File.separator;
	/**SD卡饰品zip路径*/
	public static final String SDCARD_DECORATE_ZIP = FilePath + ZIP;
	/**SD卡饰品路径根路径*/
	public static final String SDCARD_DECORATE = FilePath + DECORATE;
	/**sd卡饰品缩略图路径*/
	public static final String SDCARD_DECORATE_THUMBNAIL = FilePath + THUMBNAIL;
	/**录音文件保存路径*/
	public static final String SOUND_FILEPATH=FilePath + "sound";
	/**下载的音频文件*/
	public static final String AUDIO_DOWNLOAD_PATHE = FilePath +"download"+File.separator+"audio"+File.separator;
	/**下载的字体文件*/
	public static final String SDCARD_TEXTFOUNT = FilePath + "fount";
	/**数据库路径*/
//	public static final String DATABASE_PATH = FilePath +"data"+File.separator;
//	/**数据库文件名称*/
//	public static final String DATABASE_NAME = "data.db";
    /**草稿箱*/
    public static final String SDCARD_DRAFTBOX = FilePath + "drfatbox";

    public static final String CHAT_PATH = FilePath + "chat/";
    public static final String CHAT_SOUND_RECEIVE = CHAT_PATH + "sound/receive";
    public static final String CHAT_SOUND_SEND =FilePath + "sound/send";
    /**七牛聊天语音文件服务器路径*/
    public static final String CHAT_SERVER_AUDIO = "audio/chat/";

	public static boolean isProxyConnect = false; // 是否代理服务器联网
	public static String mProxyHost; // 代理服务器地址
	public static int mProxyPort = 0; // 代理服务器端口

	/**
	 * 官方账号id
	 */
   
	
    /**网络连接超时*/
    public static int NET_CONNECTTIMEOUT_MS = 15000;
    
//    public static final String OFFICIAL_ID="189";//官方账号id
//	private static final String DOMAIN="http://182.92.163.115";//测试线
//	public static final String UPLOADDOMAIN = "testpetalk";//测试线
//    public static final String DOWNLOADDOMAIN = "qiniufile";//测试线下载地址
//	public static final String SHARESHOPURL="http://testwww.chongwushuo.com/h5/goods/";
//	public static final String SHARETOPICTALKURL="http://testwww.chongwushuo.com/h5/topic/index.html?id=";
//	public static final String SHARETOPCOMMENTURL="http://testwww.chongwushuo.com/h5/oneTopic/index.html?id=";
//	public static String EMSG_SUFFIX = "@chongwushuotest";
	
	public static final String OFFICIAL_ID="44239";//官方账号id
	private static final String DOMAIN = "http://123.57.12.107";//正式线
	public static final String UPLOADDOMAIN = "petalk";//正式线
    public static final String DOWNLOADDOMAIN = "qnfile";//下载地址
    public static final String SHARESHOPURL="http://www.chongwushuo.com/h5/goods/";
	public static final String SHARETOPICTALKURL="http://www.chongwushuo.com/h5/topic/index.html?id=";
	public static final String SHARETOPCOMMENTURL="http://www.chongwushuo.com/h5/oneTopic/index.html?id=";
    public static String EMSG_SUFFIX = "@chongwushuo";

	public static final String HTML5_URL=DOMAIN+"/h5/index.html?petakeID=";
	public static String LOT_SERVER_V1 =DOMAIN+ "/cws-api/servlet?isCompress=0&isEncrypt=";
	public static String LOT_SERVER_V2 =DOMAIN+ "/cws-api/servlet?isCompress=0&v=2";
        public static final String DOWNLOAD_SERVER = "http://"+DOWNLOADDOMAIN + ".chongwushuo.com/";//".qiniudn.com/";
	/**字体下载地址*/
	public static final String DOWNLOD_TEXTFONT = DOWNLOAD_SERVER + "b/pt/font/";
	/**评论语音上传路径*/
	public static final String COMMENT_AUDIO = "audio/comment/";
	/**编辑语音上传路径*/
	public static final String CONTENT_AUDIO = "audio/content/";
	/**编辑图片上传路径*/
	public static final String CONTENT_IMG = "img/content/";
	/**头像上传路径*/
	public static final String AVATAR_IMG = "img/avatar/";
	
	/* **********************缓存文件******************************* */
	public static final String HotListFile="hotlist.data";
	public static final String FocusListFile="focuslist.data";
	public static final String SquareFile="square.data";
	public static final String UserFile="user.data";
	public static final String MessageList="messagelist.data";
	public static final String DECORATION = "decoration.data";
    public static final String USULLYDECORATION = "_USULLYDECORATION.data";
	/* ****************************************************/
	
	public static int SquareViewLayoutFlag=0;
	
	/**是否自动播放GIf动画: -1:未设置，0：禁止自动播放，1：wifi自动播放，2：自动播放 */
	public static int PLAY_GIF_MODE = -1;
	
	/**上传token*/
	public static String UPLOAD_TOKEN = "";
	public static final String KEY = "<>hj12@#$$%^~~ff";

	/**说说互动类型  评论*/
	public static final String COMMENT="C";//评论
	/**说说互动类型   赞*/
	public static final String FAVOUR="F";//赞
	/**说说互动类型   转发*/
	public static final String RELAY="R";//转发
	/**说说互动类型   分享*/
	public static final String SHARE="S";//分享
	/**说说互动类型   浏览*/
	public static final String PLAY="P";//浏览
	/**说说互动类型   原创*/
	public static final String ORIGINAL="O";//原创
	
	public static final String COMMENT_RELAY="C_R";
	/**说说互动类型   精品*/
	public static final String DELICACY="delicacy";
	
	/**消息中的类型   被关注*/
	public static final String FANS="fans";
	
	public static final int MSG_ANNOUNCEMENT=1;
	public static final int MSG_USER=2;
	
	/**
	 * 说说列表每次访问服务器获取的个数
	 */
	public static final int SayListPageSize=10;
	/**
	 * 评论列表每次访问服务器获取的个数
	 */
	public static final int CommentListPageSize=20;
	
	//默认请求参数
	/** 移动设备国际识别码（设备）*/
	public static String IMEI = "";
	/** 移动用户国际识别码（卡）*/
	public static String IMSI="";
	/**网卡地址*/
	public static String MAC="";
	/**手机卡(自动读取当前设备，区别于用户绑定的手机号phoneNum)*/
	public static String PHONE_SIM = "";
	/**渠道号*///TODO 打包前一定要记得写上渠道号
	public static String CHANNEL = "2014";
	/**软件版本号*/
	public static String VERSION = "3.0.1";
	/**版本号用于升级判断*/
	public static int VERSION_CODE=15;
	
	/**请求饰品信息所用的版本号*/
	public static String DECORATION_VERSION = "3.0.0";
	
	/**广场布局版本,打包之前也要检查一下*/
	public static int SQUARE_LAYOUT_VERSION=1;
	
	/**平台*/
	public static String PLATFORM = "android";
	/**ip地址*/
	public static String IPADDR = "";
	/**手机机型*/
	public static String MODEL = "";
	
	public static String[] Provinces;
	public static String[][] cities;
	public static List<PetType> petTypes;
	public static Map<Integer, String> petMap=new HashMap<Integer, String>();
	public static final String[] genderArray={"女孩","男孩","保密"};
	public static final String[] genderShoppingArray={"女孩","男孩"};
	public static PetalkVo Detail_Sayvo;
	public static Map<String, PetalkVo> delPetalk=new HashMap<String, PetalkVo>();
	public static final int GotoDetailAcRequestCode=100;

    /**发布说说上传视图*/
    public static final LinkedList<UploadPetalkView> UPLOAD_VIEWS = new LinkedList<UploadPetalkView>();

    /**等级图标列表*/
    public static final int[] LEVEL_ICONS = {R.drawable.level_0,R.drawable.level_1,R.drawable.level_2,R.drawable.level_3,
            R.drawable.level_4,R.drawable.level_5,R.drawable.level_6,R.drawable.level_7,
            R.drawable.level_8,R.drawable.level_9,R.drawable.level_10,R.drawable.level_11,};
	
//	public static boolean isRunningApp=false;
	
	public static void parseCityJson(Context context){
		String jsonstr=PublicMethod.getStringFromAssets(context, "city.txt");
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(jsonstr);
			int len=jsonArray.length();
			Provinces=new String[len];
			cities=new String[len][];
			for (int i = 0; i < len; i++) {
				JSONObject provinceJson=jsonArray.optJSONObject(i);
				Provinces[i]=provinceJson.optString("Province");
				JSONArray array= provinceJson.optJSONArray("city");
				cities[i]=new String[array.length()];
				for (int j = 0; j < array.length(); j++) {
					String string=array.optString(j);
						cities[i][j]=string;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void  InitPettype(Context context){
		try {
			Constants.petTypes= SaxPetTypeParse.parse(context);
			for (int i = 0; i < Constants.petTypes.size(); i++) {
				for (int j = 0; j < Constants.petTypes.get(i).getId().length; j++) {
					Constants.petMap.put(Constants.petTypes.get(i).getId()[j],Constants.petTypes.get(i).getName()[j]);
				}
			}
		} catch (Exception e) {
			PublicMethod.log_e("Constants.class InitPettype()", "解析宠物类型出错");
			e.printStackTrace();
		}
	}
	
}
