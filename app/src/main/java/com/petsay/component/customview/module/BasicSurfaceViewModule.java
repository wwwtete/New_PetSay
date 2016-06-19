package com.petsay.component.customview.module;

import com.petsay.component.customview.BasicSurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author wangw
 * 组件的基类，默认不可编辑变形
 */
public class BasicSurfaceViewModule {

	protected Bitmap mainBmp;
	protected Matrix matrix ;
	private Paint paint;

	protected float [] srcPs;
	protected float[] dstPs ;
	protected Object mTag;

	/**上一次的矩形位置 */
	protected RectF srcRect;
	/**目标矩形位置*/
	protected RectF dstRect;
	protected Rect mRect;
	/**Bitmap原始宽度*/
	protected int mainBmpWidth;
	/**Bitmap原始共度*/
	protected int mainBmpHeight;
	/**Z轴层下标*/
	protected int mIndex = 0;

	protected boolean mAllowEdit = false;
	protected BasicSurfaceView mView;
	
	protected int mMaxWidth  = - 1;
	protected int mMaxHeight = -1;
	
	public BasicSurfaceViewModule(Bitmap bmp){
		changeView(bmp);
		paint = new Paint(); 
		matrix = new Matrix(); 
	}
	
	/**
	 * 添加到SurfaceView后回调函数
	 * @param mFunctionBarView
	 */
	public void onAddCallback(BasicSurfaceView view){
		mView = view;
	}
	
	public void changeView(Bitmap bmp){
		if(mainBmp != null){
			mainBmp.recycle();
			mainBmp = null;
		}
		this.mainBmp = bmp;
		initModule();
	}
	
	public void setBitmap(Bitmap bmp){
		this.mainBmp = bmp;
	}

	protected void initModule(){
		mainBmpWidth = mainBmp.getWidth();  
		mainBmpHeight = mainBmp.getHeight();  
		/* 3.初始化图片控制点 
		 * 0---1---2 
		 * |       | 
		 * 7   8   3 
		 * |       | 
		 * 6---5---4  
		 */ 
		srcPs = new float[]{  
				0,0,   
				mainBmpWidth/2,0,   
				mainBmpWidth,0,   
				mainBmpWidth,mainBmpHeight/2,  
				mainBmpWidth,mainBmpHeight,   
				mainBmpWidth/2,mainBmpHeight,   
				0,mainBmpHeight,   
				0,mainBmpHeight/2,   
				mainBmpWidth/2,mainBmpHeight/2  
		}; 
		//初始化目标控制点
		dstPs = srcPs.clone();  
		//初始化控制矩形的位置数据
		srcRect = new RectF(0, 0, mainBmpWidth, mainBmpHeight);  
		dstRect = new RectF(); 
		mRect = new Rect(0,0,mainBmpWidth,mainBmpHeight);
	}

	/**
	 * 获取要绘制时的Bitmap
	 * 注：区分获取Bitmap的用途，如果用于获取原图片，就调用这个方法
	 * 	  如果用于截图应该调用getCompositeBitmap方法
	 * @return 获取原图片
	 */
	public Bitmap getBitmap(){
		return mainBmp;
	}
	
	/**
	 * 获取要截图所使用的Bitmap，
	 * 一般使用原图片，如在文字框之类的需要合成的Module需要用到这个方法
	 * @return 获取合成后的图片，
	 */
	public Bitmap getCompositeBitmap(){
		return getBitmap();
	}

	/**
	 * 获取变形矩阵
	 * @return
	 */
	public Matrix getMatrix(){
		updateRect();
		return matrix;
	}
	
	/** 
	 * 矩阵变换,更新Rect范围
	 * @author wangw
	 */  
	public void updateRect(){
		matrix.mapPoints(dstPs, srcPs);  
		matrix.mapRect(dstRect, srcRect); 
		mRect.set((int)dstRect.left, (int)dstRect.top, (int)dstRect.right, (int)dstRect.bottom);
	}

	/**
	 * 获取绘制主要图片的画笔
	 * @return
	 */
	public Paint getMainPaint(){
		return paint;
	}

	/**
	 * 判断当前触摸点是否在矩形上
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOnPic(int x , int y){  
		updateRect();
		if(dstRect.contains(x, y)){  
			return true;  
		}else   
			return false;  
	}
	
	/**
	 * 释放资源
	 */
	public void release(){
		if(mainBmp != null){
			mainBmp.recycle();
			mainBmp = null;
		}
	}

	/**
	 * 是否允许变形
	 * @return
	 */
	public boolean allowEdit(){
		return mAllowEdit;
	}

	public void setAllowEdit(boolean flag){
		this.mAllowEdit = flag;
	}
	
	public void setIndex(int index){
		this.mIndex = index;
	}
	
	public int getIndex(){
		return mIndex;
	}
	
	public Rect getRect(){
		return mRect;
	}
	
	public float getWidthScale(){
		return dstRect.width() / mView.getViewWidth();
	}
	
	public float getHeightScale(){
		return dstRect.height() /mView.getViewHeight();
	}
	
	public float getCenterX(){
		return dstPs[16] / mView.getViewWidth();
	}
	
	public float getCenterY(){
		return (dstPs[17]) / mView.getViewHeight();
	}
	
	public float[] getDstPs(){
		return dstPs;
	}
	
	public double getRotationZ(){
		float[] vs = new float[9];
		matrix.getValues(vs);
		return Math.atan2((double)vs[3], (double)vs[4]);
	}
	
	public float getRotationX(){
		float[] vs = new float[9];
		matrix.getValues(vs);
		return vs[1];
	}

	public float getRotationY(){
		float[] vs = new float[9];
		matrix.getValues(vs);
		return vs[3];
	}
	
	public void setMaxWidth(int width){
		this.mMaxWidth = width;
	}
	
	public void setMaxHeight(int height){
		this.mMaxHeight = height;
	}
	
	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		this.mTag = tag;
	}
	
	public void onDrawCallback(Canvas canvas){
	}
}
