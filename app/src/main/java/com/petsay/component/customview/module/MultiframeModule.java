package com.petsay.component.customview.module;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw
 * 含有多帧的SurfaceViewModule
 */
public class MultiframeModule extends EditSurfaceViewModule {

	protected List<Bitmap> mFrames;

	public MultiframeModule(Bitmap bmp,Bitmap controBitmap){
		super(bmp, controBitmap);
		mAllowCompound = false;
	}
	
	public MultiframeModule(Bitmap bmp, Bitmap controlBmp, Bitmap deleteBmp) {
		super(bmp, controlBmp, deleteBmp);
		mAllowCompound = false;
	}

	@Override
	protected void initModule() {
		super.initModule();
		mAllowCompound = false;
		mFrames = new ArrayList<Bitmap>();
	}
	
	/**
	 * 添加帧
	 * @param bmp
	 */
	public void addFrame(Bitmap bmp){
		mFrames.add(bmp);
	}
	
	/**
	 * 获取帧数
	 * @return
	 */
	public int getFrameCount(){
		return mFrames.size();
	}
	
	/**
	 * 清空帧
	 * @return
	 */
	public void clearFrame(){
		for (int i = 0; i < mFrames.size(); i++) {
			Bitmap bmp = mFrames.get(i);
			if(bmp != null){
				bmp.recycle();
				bmp = null;
			}
		}
		mFrames.clear();
	}
	
	/**
	 * 获取单个帧
	 * @param position
	 * @return
	 */
	public Bitmap getFrame(int position){
		if(position < 0 || position >= mFrames.size())
			return mainBmp;
		return mFrames.get(position);
	}
	
	/**
	 * 获取帧列表
	 * @return
	 */
	public List<Bitmap> getFrameList(){
		return mFrames;
	}
	
	@Override
	public void release() {
		super.release();
		if(mFrames != null && mFrames.size() > 0){
			for (int i = 0; i < mFrames.size(); i++) {
				Bitmap bmp = mFrames.get(i);
				if(bmp != null && !bmp.isRecycled()){
					bmp.recycle();
					bmp = null;
				}
			}
			mFrames.clear();
			mFrames = null;
		}
			
	}

}
