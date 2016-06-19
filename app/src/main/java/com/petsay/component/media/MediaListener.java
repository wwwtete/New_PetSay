package com.petsay.component.media;

import java.io.File;

/**
 * @author wangw
 *	回调事件
 */
public interface MediaListener {

	/**
	 * 转码完成事件
	 * @param file
	 */
	public void onTranscodingFinishCallback(File file);
	
}
