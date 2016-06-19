package com.petsay.component.gifview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.petsay.utile.PublicMethod;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;

/**
 * @author wangw
 * 播放Gif动画
 */
public class MatrixGifViewCore {
	/**每80帧刷新一次屏幕**/  
	public static final int TIME_IN_FRAME = 80;

	protected boolean mIsRuning;
	protected Thread mPlayGifThread;
	protected Matrix mMatrix;
	protected Paint mPaint;
	protected List<Bitmap> mBmpList;
	protected SurfaceHolder mHolder;
	
	public MatrixGifViewCore(SurfaceHolder holder){
		mHolder = holder;
		mPaint = new Paint();
		mBmpList = Collections.synchronizedList(new ArrayList<Bitmap>());
		mMatrix = new Matrix();
	}
	
	/**
	 * 设置Matrix并立即更新视图
	 * @param matrix
	 */
	public void setMatrix(Matrix matrix){
		this.mMatrix = matrix;
	}
	
	public void setValues(float[] vs){
		this.mMatrix.setValues(vs);
	}

	public void addFrame(Bitmap bmp){
		mBmpList.add(bmp);
	}

	public void clearFrame(){
		mBmpList.clear();
	}

	public int getFrameCount(){
		return mBmpList.size();
	}
	
	public boolean isRuning() {
		return mIsRuning;
	}

	/**
	 * 绘画方法
	 * @param canvas
	 * @param bmp
	 */
	protected void onDrawView(Canvas canvas,Bitmap bmp){
		if(bmp == null || mMatrix == null || mPaint == null || canvas == null)
			return;
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		canvas.drawBitmap(bmp, mMatrix, mPaint);
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	/**
	 * 停止播放
	 */
	public void stop(){
		mIsRuning = false;
		if(mPlayGifThread != null){
			try {
				mPlayGifThread = null;
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 开始播放
	 */
	public void start(){
		if(!mIsRuning && mHolder.getSurface().isValid()){
			mIsRuning = true;
			mPlayGifThread = new Thread(mPlayGifRunnable);
			mPlayGifThread.start();
		}
	}

	private Runnable mPlayGifRunnable = new Runnable() {

		@Override
		public void run() {
			try{
				while(mIsRuning){
					Iterator<Bitmap> terator = mBmpList.iterator();
					while(terator.hasNext()){
						Bitmap bmp = terator.next();
						long startTime = System.currentTimeMillis();
						synchronized (mHolder) {
							onDrawView(mHolder.lockCanvas(), bmp);
						}
						long endTime = System.currentTimeMillis();
						int diffTime = (int)(startTime - endTime);
						while (diffTime <= TIME_IN_FRAME) {
							diffTime =(int)(System.currentTimeMillis() - startTime);
							Thread.yield();
						}
					}
				}
			}catch(Exception e){
				PublicMethod.log_d("播放Gif线程异常：" + e);
			}
		}
	};
	
}
