package com.petsay.component.view;

import com.petsay.cache.SharePreferenceCache;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author wangw
 * 新功能角标View
 */
public class NewFunctionStateView extends ImageView {

	private String mKey;
	
	public NewFunctionStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 检查是否显示
	 * @param key
	 */
	public void checkShow(String key){
		mKey = key;
		boolean flag = SharePreferenceCache.getSingleton(getContext()).checkNewFunction(key);
		if(flag){
			this.setVisibility(View.VISIBLE);
		}else {
			this.setVisibility(View.GONE);
		}
	}
	
	public void setUsedSate(){
		if(!TextUtils.isEmpty(mKey))
			SharePreferenceCache.getSingleton(getContext()).setFunctionUsedState(mKey);
	}
	
	/**
	 * 设置功能已被使用
	 * @param view
	 */
	public static void setUsedSate(View view){
		if(view == null)
			return;
		if(view instanceof NewFunctionStateView){
			((NewFunctionStateView)view).setUsedSate();
		}
	}
	
	
}
