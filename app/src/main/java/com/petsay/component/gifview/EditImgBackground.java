package com.petsay.component.gifview;

import com.petsay.utile.PublicMethod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author wangw
 * EditImageActivity背景图片
 */
public class EditImgBackground extends SurfaceView implements SurfaceHolder.Callback {
	
	private Bitmap mBg;
	private Paint mPaint;
	
	public EditImgBackground(Context context){
		super(context);
		initView();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		PublicMethod.log_d("=============背景===============");
		return super.dispatchTouchEvent(event);
	}

	public EditImgBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}


	private void initView() {
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		getHolder().addCallback(this);
		mPaint = new Paint();
	}
	
	/**
	 * 初始化数据
	 * @param bmp
	 */
	public void initData(Bitmap bmp){
		this.mBg = bmp;
		onDrawView();
	}

	
	protected void onDrawView(){
		Canvas canvas = getHolder().lockCanvas();
		if(canvas == null || mBg == null)
			return;
		canvas.drawColor(Color.BLACK, Mode.CLEAR);
		canvas.drawBitmap(mBg, 0, 0, mPaint);
		getHolder().unlockCanvasAndPost(canvas);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		onDrawView();
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}
