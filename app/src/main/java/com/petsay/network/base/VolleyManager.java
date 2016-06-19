package com.petsay.network.base;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.Volley;
import com.petsay.application.PetSayApplication;

/**
 * @author wangw
 * Volley管理器(单例)
 */
public class VolleyManager {
	
	private static VolleyManager mInstance;
	
	public static VolleyManager getInstance(){
		if(mInstance == null){
			mInstance = new VolleyManager();
		}
		return mInstance;
	}
	
	private RequestQueue mQueue;
	
	public VolleyManager(){
		initRequestQueue();
	}

	private void initRequestQueue() {
		mQueue = Volley.newRequestQueue(PetSayApplication.getInstance());
		mQueue.start();
	}
	
	public <T> Request<T> add(Request<T> request) {
		return mQueue.add(request);
	}
	
	public void stop() {
		mQueue.stop();
	}
	
	public void cancelAll(RequestFilter filter) {
		mQueue.cancelAll(filter);
	}
	
	 public void cancelAll(final Object tag) {
		 mQueue.cancelAll(tag);
	 }
	
	public RequestQueue getRequestQueue(){
		return mQueue;
	}
	
}
