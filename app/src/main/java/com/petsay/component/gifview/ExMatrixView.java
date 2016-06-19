package com.petsay.component.gifview;

import com.petsay.utile.PublicMethod;
import com.petsay.vo.decoration.DecorationBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author wangw
 * 可以支持播放Gif动画的View
 */
public class ExMatrixView extends TouchMatrixImageView {

	protected DrawThread mThread;
	private boolean mShowBorder = true;
	private boolean mEnabled = true;
	protected DecorationBean mDecorationBean;
	protected MatrixViewListener mListener;
	protected MatrixGifViewCore mGifCore;

	public ExMatrixView(Context context){
		super(context);
	}

	public ExMatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void initView() {
		super.initView();
		mGifCore = new MatrixGifViewCore(getHolder());
	}

	public void setDecoration(DecorationBean bean){
		this.mDecorationBean = bean;
	}

	public DecorationBean getDecorationBean(){
		return this.mDecorationBean;
	}

	public void addFrame(Bitmap bmp){
		mGifCore.addFrame(bmp);
	}

	public void clearFrame(){
		mGifCore.clearFrame();
	}

	public int getFrameCount(){
		return mGifCore.getFrameCount();
	}
	
	public Bitmap getMainBitmap(){
		return mainBmp;
	}

	public void playGif(){
//		mGifCore.setMatrix(matrix);
		float[] vs = new float[9];
		matrix.getValues(vs);
//		float x = getLeftScale() * mainBmpWidth;
//		float y = getTopScale() * mainBmpHeight;
//		vs[2] = x;
//		vs[5] = y;
		mGifCore.setValues(vs);
		mGifCore.start();
	}

	public void stopGif(){
		mGifCore.stop();
		onDrawView();
//		changeView(mainBmp);
//		start();
	}

	public void setOnMatrixViewListener(MatrixViewListener listener){
		this.mListener = listener;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		PublicMethod.log_d("=============matrix===============");
//		if(!this.mEnabled){
//			return false;
//		}
		int evX = (int)event.getX();  
		int evY = (int)event.getY();  
		boolean flag  = isOnPic(evX, evY) || isOnCP(evX, evY) != -1;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(flag){
				stopGif();
				onFocus();
				start();
			}else {
				stop();
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			stop();
			break;
		}
		
		flag = super.dispatchTouchEvent(event) && flag;
		if(!flag)
			super.superDispatchTouchEvent(event);
		return flag;
	}

	protected void onFocus(){
		if(mListener != null){
			mListener.onFocusListener(this);
		}
	}

	@Override
	protected void onDrawView() {
		Canvas canvas = getHolder().lockCanvas();
		if(canvas == null)
			return;
		//		drawBackground(canvas);//绘制背景,以便测试矩形的映射
		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		canvas.drawBitmap(mainBmp, matrix, paint);//绘制主图片
		if(mShowBorder){
			drawFrame(canvas);//绘制边框,以便测试点的映射  
			drawControlPoints(canvas);//绘制控制点图片
		}
		getHolder().unlockCanvasAndPost(canvas);
	}

	public void showBorder(boolean flag){
		this.mShowBorder = flag;
		if(!flag)
			stop();
		onDrawView();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.mEnabled  = enabled;
		onDrawView();
		super.setEnabled(enabled);
	}


	protected void start(){
		stop();
		mThread = new DrawThread();
		mThread.start();

	}

	protected void stop(){
		try{
			if(mThread != null){
				mThread.isRuning = false;
				mThread = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	class DrawThread extends Thread{

		public boolean isRuning = true;

		@Override
		public void run() {
			try {
				//判断是否允许继续运行
				while (isRuning) {
					//1.获取当前时间
					long startTime = System.currentTimeMillis();
					//2.增加线程锁线程安全
					synchronized (getHolder()) {
						//3.进行绘画
						onDrawView();
					}
					//3.获取绘画结束时间
					long endTime = System.currentTimeMillis();
					//4.计算更新一次画面所需要的时间
					int diffTime = (int)(endTime - startTime);
					//				PublicMethod.log_d("当前线程="+Thread.currentThread().getName()+"更新一次画面所需时间："+diffTime);
					//5.确保以80帧每秒的频率刷新
					while(diffTime <= TIME_IN_FRAME){
						if(!isRuning)
							return;
						diffTime = (int)(System.currentTimeMillis() - startTime);
						//线程等待
						Thread.yield();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				PublicMethod.log_d("嘴型编辑View异常：" + e);
			}
		}
	}

	public interface MatrixViewListener{
		public void onFocusListener(ExMatrixView view);
	}

}
