package com.petsay.component.customview;

import com.petsay.component.customview.module.BasicSurfaceViewModule;

import android.view.MotionEvent;

/**
 * @author wangw
 *
 */
public interface ExSurfaceViewListener {

	/**
	 * 触摸Module事件，
	 * @param module 当前触摸的Module，有可能为空
	 * @param event	当前的触摸事件
	 */
	public void onTouchModule(BasicSurfaceViewModule module,MotionEvent event);
	
}
