package com.petsay.utile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import com.nineoldandroids.view.ViewHelper;
import com.petsay.R;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author wangw
 *
 *	公用方法类库
 *
 */
public class PublicMethod {

	private static int CMNET = 3;
	private static int CMWAP = 2;
	private static int WIFI = 1;

	/**
	 * 弹出Toast(此方法已过期，请使用ToastUtiles中的方法)
	 * @param context
	 * @param msg
	 */
    @Deprecated
	public static void showToast(Context context,String msg){
		if (null!=msg&&!msg.trim().equals("")) {
//			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            ToastUtiles.showDefault(context,msg);
		}

	}
	/**
	 * 弹出Toast(此方法已过期，请使用ToastUtiles中的方法)
	 * @param context
	 * @param resId
	 */
    @Deprecated
	public static void showToast(Context context,int resId){
//		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        ToastUtiles.showDefault(context,resId);
	}

	/**
	 * 验证手机号
	 * @param num
	 * @return
	 */
	public static boolean verifyMobileNumber(String num){
		if(!TextUtils.isEmpty(num)){
			String regExp = "^13[0-9]{1}[0-9]{8}$|15[0125689]{1}[0-9]{8}$|18[0-3,5-9]{1}[0-9]{8}$|17[0-9]{1}[0-9]{8}";
			return Pattern.compile(regExp).matcher(num).matches();
		}
		return false;
	}

	/**
	 * 创建标题栏右侧文字
	 * （推荐调用统一方法，便于以后换主题时统一更换）
	 * @param titleBar
     * @param title
	 * @return
	 */
	public static TextView addTitleRightText(TitleBar titleBar, String title){
        if(titleBar == null || (titleBar != null && titleBar.getContext() == null))
            return null;
		TextView textView =new TextView(titleBar.getContext());
		textView.setTextSize(18f);
        textView.setText(title);
		textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        titleBar.addRightView(textView,params);
		return textView;
	}
	
	/**
	 * 创建标题栏右侧图标
	 * （推荐调用统一方法，便于以后换主题时统一更换）
	 * @param titleBar
	 * @param drawableId
	 * @return
	 */
	public static ImageView addTitleRightIcon(TitleBar titleBar, int drawableId){
        if(titleBar == null || (titleBar != null && titleBar.getContext() == null))
            return null;
		ImageView imageView =new ImageView(titleBar.getContext());
//		textView.setTextSize(18f);
//        textView.setText(folderPath);
//		textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageResource(drawableId);
        titleBar.addRightView(imageView,params);
		return imageView;
	}

	/**
	 * 将dip转换成px
	 * 
	 * @param dip
	 * @return
	 */
	public static int getPxInt(float dip, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context
				.getResources().getDisplayMetrics());
	}

	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int getDiptopx(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}  


	/**
	 * 获取屏幕密度 默认为1
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context) {
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			DisplayMetrics metric = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			return metric.density;
		}
		return 1;
	}

	public static void outMM(String msg){
		if(Constants.isDebug){
			float total = (float)(Math.round(Runtime.getRuntime().totalMemory()/1024/1024*100)/100.0);
			float free = (float)(Math.round(Runtime.getRuntime().freeMemory()/1024/1024*100)/100.0);
			Log.d("mm",msg + "---------总共内存："+total+" | 使用内存："+(total-free)+"  |  剩余内存："+free);
		}
		//		PetsayLog.d("mm", "===========已使用的内存大小："+Debug.getNativeHeapAllocatedSize()+
		//				"  |  剩余的内存大小: "+ Debug.getNativeHeapFreeSize()+"  |  总的内存大小: "+Debug.getNativeHeapSize());
	}

	/**
         * 输出调试信息（已废弃）
	 * @param msg
	 */
    @Deprecated
	public static void log_d(String msg){
		PetsayLog.d(msg);
	}

	/**
	 * 输出冗余切不重要的信息
	 * @param msg
	 */
    @Deprecated
	public static void log_v(String msg) {
		PetsayLog.i(msg);
	}

    /**
     * 输出错误信息
     * @param methodName 发送错误的方法
     * @param msg
     */
    @Deprecated
    public static void log_e(String methodName,String msg){
        PetsayLog.e("错误方法=%s | 错误内容=%s",methodName,msg);
    }

	public static void logOutput(String tag, String msg){
		if (Constants.isDebug)
			Log.i(tag,  msg);
	}




	/**
	 * 修改tab类型button组的背景
	 * @param views 控件数组 
	 * @param index 被选中的索引
	 * @param selectedBg 被选中背景
	 * @param defaultBg 默认背景
	 */
	public static void changeBtnBgImg(View[] views,int index,int selectedBg,int defaultBg){
		for (int i = 0; i < views.length; i++) {
			if (i==index) {
				views[i].setBackgroundResource(selectedBg);
			}else {
				views[i].setBackgroundResource(defaultBg);
			}
		}
	}
	public static void changeTextViewColor(TextView[] views,int index,int selectedColor,int defaultColor){
		for (int i = 0; i < views.length; i++) {
			if (i==index) {
				views[i].setTextColor(selectedColor);
			}else {
				views[i].setTextColor(defaultColor);
			}
		}
	}

	public static void changeBtnBgImg(View[] views,int index,Drawable selectedBg,Drawable defaultBg){
		for (int i = 0; i < views.length; i++) {
			if (i==index) {
				views[i].setBackgroundDrawable(selectedBg);
			}else {
				views[i].setBackgroundDrawable(defaultBg);
			}
		}
	}

	/**
	 * 修改tab类型button组的背景
	 * @param views 控件数组 
	 * @param index 被选中的索引
	 * @param selectedColor 被选中背景
	 * @param defaultColor 默认背景
	 */
	public static void changeBtnBgColor(View[] views,int index,int selectedColor,int defaultColor){
		for (int i = 0; i < views.length; i++) {
			if (i==index) {
				views[i].setBackgroundColor(selectedColor);
			}else {
				views[i].setBackgroundColor(defaultColor);
			}
		}
	}

	/**
	 * 获取手机屏幕的高度
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context){
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();//new DisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 获取手机屏幕的宽度
	 * @param context
	 * @return
	 */
	public static int getDisplayWidth(Context context){
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();//new DisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取当前网络状态 -1：没有网络 1：wifi网络 2：wap网络 3：net网络
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context){ 
		int netType = -1; 
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo==null){ 
			return netType; 
		} 
		int nType = networkInfo.getType(); 
		if(nType==ConnectivityManager.TYPE_MOBILE){ 
			if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
				netType = CMNET; 
			}else{ 
				netType = CMWAP; 
			} 
		} 
		else if(nType==ConnectivityManager.TYPE_WIFI){ 
			netType = WIFI; 
		} 
		return netType; 
	}


	/**
	 * byte[] 转为file
	 * @param b
	 * @param outputFile 输出文件路径
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile){
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null){
				try {
					stream.close();
				} catch (IOException e1){
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 检查SD卡状态是否可用
	 * @return
	 */
	public static boolean checkSDCard(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	/**
	 * 创建转圈进度条
	 * @param context
	 * @return
	 */
	public static ProgressDialog creageProgressDialog(Context context) {
		try {
			ProgressDialog mProgressdialog = new ProgressDialog(context);
			mProgressdialog.show();
			// mProgressdialog.setCancelable(false);
			View dialogView = getView(context);
			mProgressdialog.getWindow().setContentView(dialogView);
			return mProgressdialog;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * gzip
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decompress2(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 创建转圈的进度条
	 * @param context
	 * @param canCancle
	 * @return
	 */
	public static ProgressDialog creageProgressDialog(Context context, boolean canCancle) {
		ProgressDialog mProgressdialog = new ProgressDialog(context);
		mProgressdialog.show();
		mProgressdialog.setCancelable(canCancle);
		View dialogView = getView(context);
		mProgressdialog.getWindow().setContentView(dialogView);
		return mProgressdialog;
	}

	public static void closeProgressDialog(ProgressDialog progressdialog,Context context) {
		try{
			if (progressdialog != null && progressdialog.isShowing()){// && isAppRunning(context)) {
				progressdialog.dismiss();
				progressdialog = null;
			}
		}catch(Exception e){
			MobclickAgent.onEvent(context, "closeProgressDialog");
		}
	}

	/**
	 * 判断后退栈的顶端是否为指定的Context
	 * @param context
	 * @return
	 */
	public static boolean isAppRunning(Context context) {
		String packageName = getPackageName(context);
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName)
					&& info.baseActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}

    /**
     * 检查App是否正在前台运行
     * @param context
     * @return
     */
    public static boolean isAppTopRuning(Context context){
        String packageName = getPackageName(context);
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

	/**
	 * 获取包名
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		PackageManager manager = context.getPackageManager();
		String version="";
		try {
			PackageInfo info  = manager.getPackageInfo(context.getPackageName(), 0);
			version= info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}


	public static View getView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.progress_dialog_window_anim);
		LinearInterpolator lin = new LinearInterpolator();
		anim.setInterpolator(lin);
		imageView.startAnimation(anim);

		return view;
	}

	/**
	 * short类型数组转换byte数组
	 * @param buf
	 * @return
	 */
	public static byte[] shortToByteSmall(short[] buf){

		byte[] bytes = new byte[buf.length * 2];
		for(int i = 0, j = 0; i < buf.length; i++, j+=2){
			short s = buf[i];

			byte b1 = (byte) (s & 0xff);
			byte b0 = (byte) ((s >> 8) & 0xff);

			bytes[j] = b1;
			bytes[j+1] = b0;
		}
		return bytes;

	}

	/**
	 * 根据毫秒, 返回 分钟:秒 的格式  
	 * @param ms
	 * @return
	 */
	public static String changeTimeFormat(int ms) {  
		// 转换成秒数  
		int s = ms / 1000;  
		// 分钟  
		int min = s / 60;  
		// 取得剩余的秒数  
		s = s % 60;  

		StringBuilder builder = new StringBuilder();  
		if (min < 10) {  
			builder.append("0");  
		}  
		builder.append(min);  
		builder.append(":");  
		if (s < 10) {  
			builder.append("0");  
		}  
		builder.append(s);  

		return builder.toString();  
	} 

	public static long getTodayBaseTime()
	{
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		long a = time - date.getHours() * 60 * 1000 * 60 - date.getMinutes()* 60 * 1000 - date.getSeconds() * 1000;
		return a;
	}
	private final static int minute=60*1000;
	private final static int hour=60*minute;
	private final static long day=24*hour;
	public static String calculateTime(long time)
	{
		long TodayBaseTime=getTodayBaseTime();
		String str="";
		//		strs[0]=DateFormat.format("kk:mm", createTime).toString();
		long during=TodayBaseTime-time;

		if (System.currentTimeMillis()-time<=2*60*1000) {
			//两分钟
			str="刚刚";
		}else if (System.currentTimeMillis()-time<=hour) {
			//一小时
			str=(System.currentTimeMillis()-time)/minute+"分钟前";
		}else if (time-TodayBaseTime>=0) {
			//今天
			str="今天"+DateFormat.format("kk:mm", time).toString();
		}else if (during>0&&during<=7*day) {
			//七天内
			str=(during/day+1)+"天前";
		}else if (during>0&&during<=365*day) {
			//今年内
			str=DateFormat.format("MM-dd", time).toString();
		}else {
			str=DateFormat.format("yyyy-MM-dd", time).toString();
		}
		return str;
	}
	
	public static String calculateTopicTime(long time,boolean iscomment){
		long TodayBaseTime=getTodayBaseTime();
		String str="";
		//		strs[0]=DateFormat.format("kk:mm", createTime).toString();
		long during=TodayBaseTime-time;

		if (System.currentTimeMillis()-time<=60*1000) {
			//两分钟
			str="刚刚";
		}else if (System.currentTimeMillis()-time<=hour) {
			//一小时
			str=(System.currentTimeMillis()-time)/minute+"分钟前";
		}else if (during>0&&during<=365*day) {
			//今年内
			if (iscomment) {
				str=DateFormat.format("MM-dd", time).toString();
			}else {
				str=DateFormat.format("MM-dd kk:mm", time).toString();
			}
			
		}else {
			str=DateFormat.format("yyyy-MM-dd", time).toString();
		}
		return str;
	}

	public static String formatTimeToString(long time, String type)
	{
		return DateFormat.format(type, time).toString();
	}

	public static long formatTimeToLong(String time, String type)
	{
		long day = 0;
		SimpleDateFormat myFormatter = new SimpleDateFormat(type);
		try
		{
			Date startDate = myFormatter.parse(time);
			day = startDate.getTime();
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return day;
	}

	public static String getAge(long bitrhday,boolean isYearAndMonth){
		String ageStr="";
		long nowtime=System.currentTimeMillis();
		long offset=nowtime-bitrhday;
		long yearValue=31536000000L;
		long monthValue=2592000000L;
		int year=(int) (offset/yearValue);
		if (offset<0) {
			return "0岁";
		}else if (year==0) {
			int month=(int) (offset/monthValue)+1;
			if (month>=12) {
				return "1岁";
			}
			return month+"个月";
		}else {
			int month=(int) ((offset%yearValue)/monthValue);
			if (month>=12) {
				month=0;
				year+=1;
			}
			if (isYearAndMonth) {
				if (month>0) {
					return year+"岁"+month+"个月";
				}else {
					return year+"岁";
				}

			}else {
				return year+"岁";
			}
		}


	}
	
	/**
	 * 格式化倒计时
	 * @param countDownTime
	 * @return
	 */
	public static String formatCountDownTime(long countDownTime){
		String result="";
		long minute=60*1000;
		long hour=60*minute;
		long day=24*hour;
		int dayCount=(int) (countDownTime/day);
		int hourCount=(int) ((countDownTime%day)/hour);
		int minuteCount=(int) ((countDownTime%hour)/minute);
		int secondCount=(int) ((countDownTime%minute)/1000);
		if (countDownTime>day) {
			result=dayCount+"天"+hourCount+"小时"+minuteCount+"分";
		}else if(hourCount>0){
			result=hourCount+"小时"+minuteCount+"分"+secondCount+"秒";
		}else if (minuteCount>0) {
			result=minuteCount+"分"+secondCount+"秒";
		}else {
			result=secondCount+"秒";
		}
		return result;
	}

	public static String calcDaysFromToday(long timeValue) {
		long nowtime = System.currentTimeMillis();
		long offset = timeValue - nowtime;
		long dayValue = 1000 * 60 * 60 * 24;
		long hourValue = 1000 * 60 * 60;
		long minuteValue = 1000 * 60;
		int days = (int) (offset / dayValue);
		int hours = (int) ((offset % dayValue) / hourValue);
		int minutes = (int) (((offset % dayValue) % hourValue) / minuteValue);
		StringBuilder str = new StringBuilder();
		if (days > 0 || hours > 0 || minutes > 0) {
			if (days > 0) {
				str.append(days + "天");
			}

			if (hours > 0) {
				str.append(hours + "小时");
			}
			if (minutes > 0) {
				str.append(minutes + "分");
			}
		} else {
			str.append("0天0小时0分");
		}

		return str.toString();
	}


	//	
	//	/**
	//	 * 根据URL获取文件名不带扩展名
	//	 * @param url
	//	 * @return
	//	 */
	//	public static String getFileName(String url){
	//		if(TextUtils.isEmpty(url))
	//			return "";
	//		return url.substring(url.lastIndexOf("/"),url.lastIndexOf("."));
	//	}


	/**
	 * 从assets读取图片
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context context,String fileName) {  
		Bitmap image = null;  
		AssetManager am = context.getResources().getAssets();  
		try  
		{  
			InputStream is = am.open(fileName);  
			image = BitmapFactory.decodeStream(is);  
			is.close();  
		}  
		catch (IOException e)  
		{  
			e.printStackTrace(); 
			PublicMethod.log_d("从Assets读取图片失败");
		}  
		return image;  

	} 


	/**
	 * 获取assets文件夹下文件的文本内容
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getStringFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 显示软键盘
	 * @param editText
	 * @param index
	 */
	public static void openSoftKeyBoard(EditText editText,int index) {
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager =(InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, index);
	}

	/**
	 * 关闭软键盘
	 * @param context
	 */
	public static void closeSoftKeyBoard(Context context,EditText editText){
//		editText.setFocusableInTouchMode(false);
//		editText.setFocusable(false);
		InputMethodManager imm = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		//		     InputMethodManager imm = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
		//		     imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
		//		     imm.h
	}


	/**
	 * 获取软键盘显示状态
	 * @param editText
	 * @return true 显示  false 隐藏状态
	 */
	public static boolean isSoftKeyBoardShow(Context context ,EditText editText){
		InputMethodManager imm = (InputMethodManager)context. getSystemService(Activity.INPUT_METHOD_SERVICE);
		if(imm.hideSoftInputFromWindow(editText.getWindowToken(), 0))
		{  
			//软键盘已弹出  
			//imm.showSoftInput(editText,0);  
			return true;
		}  
		return false;
	}

	/**
	 * 获取一定范围随机数
	 * @param maxRange
	 * @return
	 */
	public static int getRangeRandomNum(int maxRange){
		return (int)(Math.random()*(maxRange+1));
	}

	/**
	 * 设置Matrix随机平移量
	 * @param maxWidth
	 * @param maxHeight
	 * @param bmpWidthd
	 * @param mx
	 */
	public static Matrix setRandomPosition(int maxWidth,int maxHeight,int bmpWidthd,int bmpHeight,Matrix mx){
		int mw = maxWidth - bmpWidthd;
		int mh = maxHeight - bmpHeight;
		mx.postTranslate(getRangeRandomNum(mw), getRangeRandomNum(mh));
		return mx;
	}

	/**
	 * 更新ListVIewItem的布局
	 * @param width
	 * @param height
	 * @param data
	 * @param view
	 * @param params
	 */
	@SuppressLint("NewApi")
	public static void updateGifItemLayout(int width,int height,PetalkDecorationVo data,View view,android.widget.FrameLayout.LayoutParams params) {
		float pw = width * data.getWidth();
		float ph = height * data.getHeight();
		float left = width * data.getCenterX() - width/2;
		float top = height * data.getCenterY() - width/2;
		float rotate = (float)(data.getRotationZ()*180/Math.PI);
		//采用第三方jar包避免Android2.3版本的异常
		ViewHelper.setScaleX(view, pw / width);
		ViewHelper.setScaleY(view, ph/height);
		ViewHelper.setRotation(view, rotate);
		params.width = width;
		params.height = height;
		params.leftMargin = (int)left;
		params.topMargin = (int)top;
		view.setLayoutParams(params);
	}

	/**
	 * 初始化列表宠物背景图的布局
	 * @param view
	 * @param width
	 */
	public static void initPetalkViewLayout(View view, int width){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = width;
		layoutParams.width = width;
		view.setLayoutParams(layoutParams);
	}

    /**
     * 初始化故事封面Layout布局
     * @param storyTitle
     * @param storyTime
     * @param imgCover
     * @param size
     */
    public static void initStoryCoverViewLayout(View storyTitle,View storyTime,View imgCover,int size){
        if(storyTitle != null){
            ViewGroup.MarginLayoutParams titleParam = (ViewGroup.MarginLayoutParams) storyTitle.getLayoutParams();
            titleParam.leftMargin = (int) (40.0f/640.0f*size);
            titleParam.topMargin =  (int) (116.0f/640.0f*size);
            titleParam.width = size - titleParam.leftMargin;
            storyTitle.setLayoutParams(titleParam);
        }
        if(storyTime != null){
            ViewGroup.MarginLayoutParams timeParam = (ViewGroup.MarginLayoutParams) storyTime.getLayoutParams();
            timeParam.leftMargin = (int) (40.0f/640.0f*size);
            timeParam.topMargin = (int) (164.0f/640.0f*size);
            storyTime.setLayoutParams(timeParam);
        }

        if(imgCover != null){
            ViewGroup.MarginLayoutParams imgParam = (ViewGroup.MarginLayoutParams) imgCover.getLayoutParams();
            imgParam.leftMargin = (int) (40.0f/640.0f*size);
            imgParam.topMargin = (int) (224.0f/640.0f*size);
            imgParam.width = (int) (560.0f/640.0f*size);
            imgParam.height = (int) (320.0f/640.0f*size);
            imgCover.setLayoutParams(imgParam);
        }
    }



	/**
	 * 动态设置ListView的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
		if(listView == null) return;

		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) { 
			// pre-condition 
			return; 
		} 

		int totalHeight = 0; 
		for (int i = 0; i < listAdapter.getCount(); i++) { 
			View listItem = listAdapter.getView(i, null, listView); 
			listItem.measure(0, 0); 
			totalHeight += listItem.getMeasuredHeight(); 
		} 

		ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
		listView.setLayoutParams(params); 
	}

    /**
     * 获取七牛服务器上传路径
     * @param path
     * @return
     */
	public static String getServerUploadPath(String path){
		long time = System.currentTimeMillis();
		return path+new SimpleDateFormat("yyyyMMdd").format(time)+"/";
	}

	public static void showAnnouncementDialog(Context context,String content){
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(content);
		builder.setTitle("公告");
		builder.setPositiveButton("我知道了", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public static void showSetPlayModeDialog(final Context context){
		Builder builder;
		if(Build.VERSION.SDK_INT >10)
			builder =new AlertDialog.Builder(context,R.style.ver_update_dialog);
		else
			builder =new AlertDialog.Builder(context);
		final Dialog dialog = builder.create();

		dialog.show();
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = getDisplayWidth(context);
		Window window = dialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.setplaymode_dialog, null);
		view.findViewById(R.id.tv_wifi).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Constants.PLAY_GIF_MODE = 1;
				SharePreferenceCache.getSingleton(context).setPlaymode(1);
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.tv_auto).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Constants.PLAY_GIF_MODE = 2;
				SharePreferenceCache.getSingleton(context).setPlaymode(2);
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Constants.PLAY_GIF_MODE = 0;
				SharePreferenceCache.getSingleton(context).setPlaymode(0);
				dialog.dismiss();
			}
		});
		window.setContentView(view);
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 为View设置晃动动画效果
	 * @param context
	 * @param view
	 */
	public static void startShakeAnimation(Context context,View view){
		try {
			Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
			view.startAnimation(shake);
			view.requestFocus();
		} catch (Exception e) {
		}

	}

	private static long lastClickTime;  
	/**
	 * 禁止快速连续点击
	 * @return
	 */
    public static boolean isFastDoubleClick() {  
        long time = System.currentTimeMillis();     
        if ( time - lastClickTime < 500) {     
            return true;     
        }     
        lastClickTime = time;     
        return false;     
    }
    
    /**
     * 通过时间戳获取年月日
     * @param time
     * @return
     */
    public static int[] getYMD(long time){
    	int[] dateArr=new int[3];
    	Calendar calendar=Calendar.getInstance();
    	Date date=new Date(time);
    	calendar.setTime(date);
    	dateArr[0]=calendar.get(Calendar.YEAR);
    	dateArr[1]=calendar.get(Calendar.MONTH)+1;
    	dateArr[2]=calendar.get(Calendar.DATE);
    	return dateArr;
    }

    /**
     * 复制文字
     * @param context
     * @param str
     */
    public static void copy(Context context,String str){
        ((ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(str);
    }
    
    /**
     * 计算浏览次数显示规则
     * 十万内播放：
   22231浏览  6554浏览
         十万以上，亿以下：
   12万浏览   12.3万浏览  9999.9万浏览  
         一亿以上
   1.1亿浏览
     * @param play
     */
    public static String  calPlayTimes(long play){
    	String result="";
    	  DecimalFormat df = new DecimalFormat("#.#");
    	float value=(float)play;
    	if (play<100000) {
			result=String.valueOf(play);
		}else if (play<100000000) {
			
			result=String.valueOf(df.format(value/10000))+"万";
		}else {
			result=String.valueOf(df.format(value/100000000))+"亿";
		}
    	
    	return result;
    }

    /**
     * view转成bitmap
     * @param view
     * @return
     */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
	
	public static float AddFloat(float float1, float float2) {
		BigDecimal b1 = new BigDecimal(float1);
		BigDecimal b2 = new BigDecimal(float2);
		return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

	}
	public static float MinusFloat(float float1,float float2){
		BigDecimal b1 = new BigDecimal(float1);
		BigDecimal b2 = new BigDecimal(float2);
		return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static float formatFloat(int decimalLenth,float value){
		BigDecimal b = new BigDecimal(value);
		return b.setScale(decimalLenth, BigDecimal.ROUND_HALF_UP).floatValue();
	}

}
