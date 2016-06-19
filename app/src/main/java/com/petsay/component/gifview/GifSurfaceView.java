package com.petsay.component.gifview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author wangw
 * 播放Gif的SurfaceView
 */
public class GifSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	/**每80帧刷新一次屏幕**/  
	public static final int TIME_IN_FRAME = 80;

	protected boolean mIsRuning;
	private Thread mPlayGifThread;
	private Bitmap mMainBmp;
	private Matrix mMatrix;
	private Paint mPaint;
	private List<Bitmap> mBmpList;
	private Bitmap mBg;

	public GifSurfaceView(Context context){
		super(context);
		initView();
	}

	private void initView() {
		mBmpList = Collections.synchronizedList(new ArrayList<Bitmap>());
		getHolder().addCallback(this);
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
	}

	public GifSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * 设置未开始播放动画时显示的图片
	 * @param bmp
	 */
	public void setView(Bitmap bmp){
		this.mMainBmp = bmp;
	}

	/**
	 * 初始化数据
	 * @param matrix
	 * @param bmp
	 */
	public void initDatd(Matrix matrix,Bitmap bmp){
		setView(bmp);
		this.mMatrix = matrix;
		mPaint = new Paint(); 
	}

	/**
	 * 设置Matrix并立即更新视图
	 * @param matrix
	 */
	public void setMatrix(Matrix matrix){
		this.mMatrix = matrix;
		onDrawView(getHolder().lockCanvas(), mMainBmp);
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
	

	/**
	 * 绘画方法
	 * @param canvas
	 * @param bmp
	 */
	private void onDrawView(Canvas canvas,Bitmap bmp){
		if(bmp == null || mMatrix == null || mPaint == null || canvas == null)
			return;
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		//		canvas.drawBitmap(mBg, 0, 0, mPaint);
		canvas.drawBitmap(bmp, mMatrix, mPaint);
		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		onDrawView(getHolder().lockCanvas(), mMainBmp);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stop();
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
		if(!mIsRuning){
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
						synchronized (getHolder()) {
							onDrawView(getHolder().lockCanvas(), bmp);
						}
						long endTime = System.currentTimeMillis();
						int diffTime = (int)(startTime - endTime);
						while (diffTime <= TIME_IN_FRAME) {
							diffTime =(int)(System.currentTimeMillis() - startTime);
							Thread.yield();
						}
					}
				}
			}catch(Exception e){}
		}
	};

}
