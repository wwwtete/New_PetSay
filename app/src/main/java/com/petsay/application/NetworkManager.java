package com.petsay.application;

import android.content.Context;

import com.petsay.R;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.utile.PublicMethod;


public class NetworkManager {
	private static NetworkManager _instance;
	public static NetworkManager getSingleton() {
		if (null == _instance) {
			_instance = new NetworkManager();
		}
		return _instance;
	}
	
	public void canclePullRefresh(Context context,PullToRefreshView view){
		if (null!=view) {
			view.onHeaderRefreshComplete();
			view.onFooterRefreshComplete();
		}
		if (null!=context) {
			PublicMethod.showToast(context, R.string.network_disabled);
		}
		
	}
	
	
	
	
	

}
