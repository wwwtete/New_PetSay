package com.petsay.component.gifview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;

import com.petsay.vo.petalk.PetalkDecorationVo;

/**
 * @author wangw
 * 根据位置播放Gif的View
 */
public class PositionGifViewCore extends MatrixGifViewCore {

	protected PetalkDecorationVo mData;
	protected int mWidth = -1;
	protected int mHeight = -1;
	
	
	public PositionGifViewCore(SurfaceHolder holder) {
		super(holder);
	}
	
	public void setViewWidth(int width){
		this.mWidth = width;
	}
	
	public void setViewHeight(int height){
		this.mHeight = height;
	}

	public void setPositionData(PetalkDecorationVo data){
		this.mData = data;
	}
	
	/**
	 * 初始化数据
	 * @param data
	 * @param width
	 * @param height
	 */
	public void initData(PetalkDecorationVo data,int width,int height){
		this.mData = data;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	@Override
	protected void onDrawView(Canvas canvas, Bitmap bmp) {
		if(bmp == null || canvas == null || mData == null ||  mWidth <= 0 || mHeight <= 0)
			return;
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		float pw = mWidth * mData.getWidth();
		float ph = mHeight * mData.getHeight();
		Matrix mx = new Matrix();
		float left = mWidth * mData.getCenterX() - pw/2;
		float top = mHeight * mData.getCenterY() - ph/2;
		mx.setScale(pw/w, ph/h);
		Bitmap temp = Bitmap.createBitmap(bmp, 0, 0, w, h, mx, true);
		canvas.drawBitmap(temp, left, top, mPaint);
		
//		canvas.drawBitmap(bmp, mx, mPaint);
		
		canvas.rotate((float) mData.getRotationZ());
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	
}
