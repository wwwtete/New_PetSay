package com.petsay.application.location;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 定位
 * @author GJ
 *
 */
public class LocationManager {
	private LocationClient mLocationClient;
//	private TextView LocationResult,ModeInfor;
//	private Button startLocation;
//	private RadioGroup selectMode,selectCoordinates;
//	private EditText frequence;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
//	private CheckBox checkGeoLocation;
	
	private static LocationManager _instance;
	
	
	
//	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	public TextView mLocationResult,logMsg;
	public TextView trigger,exit;
	public Vibrator mVibrator;
	

	public static LocationManager getSingleton() {
		if (null == _instance) {
			_instance = new LocationManager();
		}
		return _instance;
	}
	
	public void setBdlistener(Context context,BDLocationListener bdLocationListener){
		if (null==mLocationClient) {
			/***定位***/
			mLocationClient = new LocationClient(context);
			mMyLocationListener = new MyLocationListener();
			mGeofenceClient = new GeofenceClient(context);
			mVibrator =(Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
			/**********/
		}
		mLocationClient.registerLocationListener(bdLocationListener);	
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		option.setScanSpan(100);//设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
	}
	

	/**
	 * 实现实位回调监听
	 * 市location.getCity()
	 * 区location.getDistrict()
	 * 详细location.getAddrStr()
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("createTime : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
//				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getProvince()+"    "+location.getCity());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
		}


	}
	
	
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startLocate(){
		if (null!=mLocationClient) {
			InitLocation();
			mLocationClient.start();
		}else {
			System.out.println("LocationManager.startLocate() mLocationClient为null");
		}
		
	}
	
	public void stopLocate(){
		if (null!=mLocationClient) {
			InitLocation();
			mLocationClient.stop();
		}else {
			System.out.println("LocationManager.stopLocate() mLocationClient为null");
		}
	}

}
