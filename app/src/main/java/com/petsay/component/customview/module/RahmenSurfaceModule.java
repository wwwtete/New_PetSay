package com.petsay.component.customview.module;

import android.graphics.Bitmap;

/**
 * @author wangw
 *
 */
public class RahmenSurfaceModule extends BasicSurfaceViewModule {

	public RahmenSurfaceModule(Bitmap bmp) {
		super(bmp);
		setAllowEdit(false);
	}
	
	@Override
	public boolean isOnPic(int x, int y) {
		return false;
	}

}
