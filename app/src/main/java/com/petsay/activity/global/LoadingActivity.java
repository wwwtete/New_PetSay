package com.petsay.activity.global;

import java.io.File;
import java.lang.reflect.Method;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.guide.GuideActivity;
import com.petsay.activity.main.MainActivity;
import com.petsay.application.MobileInfoManager;
import com.petsay.application.PetSayApplication;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SystemNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.xml.SaxPetTypeParse;
import com.petsay.vo.ResponseBean;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * 启动界面
 * 
 * @author gaojian
 * 
 */
public class LoadingActivity extends BaseActivity implements NetCallbackInterface {
	private static final int SPLASH_DISPLAY_LENGHT = 500; // 延迟三秒
//	private UserModule mUserModule;
//	private UserData mUserData;
    private SystemNet mSystemNet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mSystemNet=new SystemNet();
		mSystemNet.setCallback(this);
		mSystemNet.setTag(this);
		setContentView(R.layout.load);
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mSystemNet.systemStartUp();

				if(SharePreferenceCache.getSingleton(LoadingActivity.this).isShowMainpageGuide()){
					jumpWelcomActivity();
				}else {
					jumpMainActivity();
				}
			}
		}, SPLASH_DISPLAY_LENGHT);
		//		mUserModule=UserModule.getSingleton();
		//		mUserData=new UserData(handler);
		//		mUserModule.addListener(mUserData);
		//		mUserModule.systemStartUp(mUserData);
		//		initData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	private void jumpWelcomActivity(){
		Intent intent = new Intent();
		intent.setClass(LoadingActivity.this,GuideActivity.class);//
		intent.putExtra("isStart", true);
		startActivityForResult(intent, 100);
		finish();
	}

	private void jumpMainActivity(){
		Intent mainIntent = new Intent(LoadingActivity.this,MainActivity.class);//MainActivity.class);//
		LoadingActivity.this.startActivity(mainIntent);
		LoadingActivity.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			jumpMainActivity();
			break;

		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initData() {
		//TODO 检查配置文件是否存在是每次开机启动还是只是第一次装机检查有待商榷
		checkNomediaFile();
		getmaxMemory();
		MobileInfoManager.getSingleton().getMachineInfo(PetSayApplication.getInstance());
        //TODO 暂时ainActivity的onCreate方法中去更新饰品信息，以后待优化
//        PublicMethod.updateDecorationData(LoadingActivity.this);
		initCityAndPetType();
	}

	public void getmaxMemory(){
		setMinHeapSize(120*1024*1024);
		PublicMethod.log_d("程序内存大小 ===" + Runtime.getRuntime().maxMemory() + "  |   总内存大小 ====" + Runtime.getRuntime().totalMemory());
	}

	private void setMinHeapSize(long size) {
		try {
			Class<?> cls = Class.forName("dalvik.system.VMRuntime");
			Method getRuntime = cls.getMethod("getRuntime");
			Object obj = getRuntime.invoke(null);// obj就是Runtime
			if (obj == null) {
				System.err.println("obj is null");
			} else {
				Class<?> runtimeClass = obj.getClass();
				Method setMinimumHeapSize = runtimeClass.getMethod(
						"setMinimumHeapSize", long.class);

				setMinimumHeapSize.invoke(obj, size);
			}

		} catch (Exception e) {
			PublicMethod.log_d("--------------设置内存无效------------");
			e.printStackTrace();
		} 
	}

//	/**
//	 * 获取手机设备信息
//	 */
//	public void getMachineInfo() {
//		try {  
//			TelephonyManager telephonyManager = (TelephonyManager) this
//					.getSystemService(TELEPHONY_SERVICE);
//			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//			WifiInfo info = wifi.getConnectionInfo();
//			Constants.MAC = info.getMacAddress();
//			Constants.IMSI = telephonyManager.getSubscriberId();
//			Constants.MODEL = Build.MODEL;
//			String moblie = telephonyManager.getLine1Number();
//			if (moblie != null && moblie.equals("null")) {
//				Constants.PHONE_SIM = moblie;
//			}
//			//获取所有本地可用网络接口信息，然后返回枚举中的元素  
//			for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) {  
//				//返回枚举本地网络接口的下一个元素  
//				NetworkInterface networkInterface = enumeration.nextElement();  
//				//获取IP地址信息，然后返回枚举中的元素  
//				for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
//					//返回枚举集合中的下一个IP地址信息  
//					InetAddress inetAddress = enumIpAddr.nextElement();  
//					//如果该地址不为回环地址  
//					if (!inetAddress.isLoopbackAddress()) {  
//						//显示出主机IP地址  
//						Constants.IPADDR = inetAddress.getHostAddress().toString();
//					}  
//				}  
//			}  
//		} catch (SocketException ex) {  
//			MobclickAgent.onEvent(this, "getMachineInfo");
//			PublicMethod.log_d("[getMachineInfo]异常:"+ex);
//		}
//		Constants.IMEI = DeviceIdentityFactory.builder(this);
//
//		PackageInfo info;
//		try {
//			info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//			Constants.VERSION=info.versionName;  
//			Constants.VERSION_CODE=info.versionCode;
//			Constants.PackageName=info.packageName;
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//	}

	/**
	 * 获取饰品树
	 */
//	private void checkDecoration(){
//		AccessoriesService service = new AccessoriesService();
//		final DecorationDataManager task = DecorationDataManager.getInstance(getApplicationContext());
//		service.addListener(new AccessoriesListener() {
//
//			@Override
//			public void onGetDecorationCallback(DecorationRoot decoration) {
//				if(decoration != null)
//					task.setDecorationData(decoration);
//				else
//					task.setState(-1);
//			}
//		});
//		task.setState(-2);
//		service.getDecorationData();
//	}
	
	/**
	 * 检查noMedia文件是否存在
	 */
	public void checkNomediaFile(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String path = FileUtile.getPath(LoadingActivity.this, Constants.FilePath)+".nomedia";
				File file = new File(path);
				try {
					if(!file.exists()){
						file.createNewFile();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_SYSTEMSTARTUP:
			Constants.SquareViewLayoutFlag=bean.getLayout();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_SYSTEMSTARTUP:
			break;
		default:
			break;
		}
		
	}
	
	private void initCityAndPetType() {
		if (null== Constants.petTypes) {
			DataFileCache.initContext(getApplicationContext());
			Constants.parseCityJson(getApplicationContext());
			//		Constants.initImgOptions();
			//		PublicMethod.initAppInfo(getApplicationContext());
			Constants.InitPettype(this);
		}

	}

}