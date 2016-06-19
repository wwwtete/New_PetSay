package com.petsay.component.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.petsay.component.customview.module.BasicSurfaceViewModule;
import com.petsay.component.customview.module.DialogSurfaceViewModule;
import com.petsay.component.customview.module.EditSurfaceViewModule;
import com.petsay.component.customview.module.RahmenSurfaceModule;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * 只绘制静止的变形图形
 */
public class BasicSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	protected List<BasicSurfaceViewModule> mModules;
	protected ExSurfaceViewListener mListener;
	protected BasicSurfaceViewModule mFocusModule;
	protected DrawMotionlessThread mThread;
	protected boolean mAllowShowBorder = true;
	/**背景Module*/
	protected BasicSurfaceViewModule mBgModule;
	/**相框Module*/
	private RahmenSurfaceModule mRahmenModule;
	protected int mViewWidth = -1;
	protected int mViewHeight = -1;
	protected boolean mAllowDraw = true;

	public BasicSurfaceView(Context context){
		super(context);
		initViews();
	}

	public BasicSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public void setOnListener(ExSurfaceViewListener listener){
		this.mListener = listener;
	}

	/**
	 * 初始化视图
	 */
	protected void initViews() {
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		getHolder().addCallback(this);
		mModules = new ArrayList<BasicSurfaceViewModule>();
	}

	/**
	 * 添加组件
	 */
	public void addModule(BasicSurfaceViewModule module){
		if(module.allowEdit()){
			mFocusModule = module;
			mAllowShowBorder = true;
		}
		mModules.add(module);
		module.onAddCallback(this);
		module.setIndex(mModules.size()-1);
		onDrawView();
	}

	/**
	 * 根据下标添加组件
	 * @param location
	 * @param module
	 */
	public void addModule(int location,BasicSurfaceViewModule module){
		if(location < mModules.size() || location == 0){
			if(module.allowEdit()){
				mFocusModule = module;
				mAllowShowBorder = true;
			}
			mModules.add(location, module);
			module.onAddCallback(this);
			onSortLayer(location);
			onDrawView();
		}
	}

	/**
	 * 设置指定的Module为聚焦点，并顶层显示
	 */
	public void setModuleFocus(BasicSurfaceViewModule module){
		mAllowShowBorder = true;
		mFocusModule = module;
		if(mModules.contains(module)){
			if(module.getIndex() < mModules.size()-1){
				changeLayerToTop(module);
			}else {
				onDrawView();
			}
		}
	}

	/**
	 * 获取当前正在编辑的Module
	 * @return
	 */
	public BasicSurfaceViewModule getFocusModule(){
		return mFocusModule;
	}

	/**
	 * 删除Module
	 * @param module
	 */
	public void removeModule(BasicSurfaceViewModule module){
		if(module == mFocusModule)
			mFocusModule = null;
		if(mModules.contains(module)){
			mModules.remove(module);
		}
	}

	/**
	 * 获取有多少module
	 * @return
	 */
	public int getModuleCount(){
		if(mModules != null)
			return mModules.size();
		return 0;
	}

	@Override
	public void setFocusable(boolean focusable) {
		mAllowShowBorder = focusable;
		if(!focusable){
			onDrawView();
		}
		mAllowDraw = focusable;
		super.setFocusable(focusable);
	}

	public boolean getFocusable(){
		return mAllowDraw;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean isDispatchEvent = true;
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			mFocusModule = onDownHandler(event);
			if(mFocusModule != null && mFocusModule.allowEdit())
				startThread();
			break;
		case MotionEvent.ACTION_MOVE:
			if(mFocusModule != null && mFocusModule.allowEdit() && mAllowDraw){
				EditSurfaceViewModule ed = (EditSurfaceViewModule)mFocusModule;
				ed.onMoveHandler(event);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(mFocusModule != null && mFocusModule.allowEdit() && mAllowDraw){
				EditSurfaceViewModule ed = (EditSurfaceViewModule)mFocusModule;
				int oper = ed.onUpHandler(event);
				if(oper == EditSurfaceViewModule.CTR_LEFT_TOP && ed.getDeleteBmp() != null){
					removeModule(ed);
					onDrawView();
					isDispatchEvent = false;
				}
				//文本框增加确认功能
				if(ed instanceof DialogSurfaceViewModule && oper == EditSurfaceViewModule.CTR_RIGHT_TOP){
					mFocusModule = null;
					onDrawView();
					isDispatchEvent = false;
				}
			}
			stopThread();
			break;
		}

		if(mFocusModule != null && mFocusModule instanceof EditSurfaceViewModule) {
			EditSurfaceViewModule ed = (EditSurfaceViewModule)mFocusModule;
			ed.getLoastPoint().x = (int) event.getX();
			ed.getLoastPoint().y = (int) event.getY();
		}
		if(MotionEvent.ACTION_CANCEL == event.getAction() || MotionEvent.ACTION_UP == event.getAction()){
			stopThread();
		}
		if(isDispatchEvent)
			onDispatchModuleTouch(mFocusModule, event);
		return true;
	}

	/**
	 * 启动绘制线程
	 */
	public void startThread(){
		stopThread();
		if(mAllowDraw){
			mThread = new DrawMotionlessThread(this);
			mThread.setName("DrawSurfaceView-"+mThread.getName());
			mThread.start();
			//			PetsayLog.d("View","启动绘制线程name=="+mThread.getName());
		}
	}

	/**
	 * 停止绘制线程
	 */
	public void stopThread(){
		if(mThread != null){
			mThread.stopThread();
			//			PetsayLog.d("view", "停止线程"+mThread.getName());
		}
		try {
			mThread = null;
		} catch (Exception e) {
		}
	}

	/**
	 * 刷新View
	 */
	public void refreshView(){
		stopThread();
		boolean temp = mAllowDraw;
		mAllowDraw = true;
		onDrawView();
		mAllowDraw = temp;
	}

	/**
	 * onDown处理函数
	 * @param event
	 * @return
	 */
	private BasicSurfaceViewModule onDownHandler(MotionEvent event){
		int x = (int) event.getX();
		int y = (int) event.getY();
		int startIndex = 0;
		BasicSurfaceViewModule module = null;
		mFocusModule = null;
		for (int i = mModules.size()-1; i >= 0; i--) {
			module = mModules.get(i);
			if(module.isOnPic(x, y)){
				startIndex = i;
				break;
			}
			module = null;
		}
		if(module != null && module.allowEdit()){
			mAllowShowBorder = true;
			((EditSurfaceViewModule)module).onDownHandler(event);
			//				if(startIndex != mModules.size()-1){
			changeLayerToTop(module);
			onSortLayer(startIndex);
			//				}
		}
		return module;
	}

	/**
	 * 将当前的Module切换到顶层
	 * @param module
	 */
	public void changeLayerToTop(BasicSurfaceViewModule module){
		if(mModules.contains(module)){
			//TODO 临时解决方案
			mModules.remove(module);
			mModules.add(module);
			mFocusModule = module;
			onDrawView();
		}
	}

	/**
	 * 重新排序层的下标
	 * @param startIndex
	 */
	protected void onSortLayer(int startIndex){
		if(startIndex < 0 || startIndex >= mModules.size()){
			return;
		}
		for (int i = startIndex; i < mModules.size(); i++) {
			mModules.get(i).setIndex(i);
		}
	}

	/**
	 * 隐藏边框
	 * @param flag
	 */
	public void hidenBorder(boolean flag){
		this.mAllowShowBorder = !flag;
		onDrawView();
	}


	public void onDrawView(){
		if(!mAllowDraw)
			return;
		synchronized (getHolder()) {
			Canvas canvas = getHolder().lockCanvas();
			if(canvas == null)
				return;
			onDrawCore(canvas);
			getHolder().unlockCanvasAndPost(canvas);
		}
	}

	protected void onDrawCore(Canvas canvas){
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		drawBackGround(canvas);
		for (int i = 0; i < mModules.size(); i++) {
			BasicSurfaceViewModule module = mModules.get(i);
			if(module.getBitmap() != null){
				//					PetsayLog.d("view", "当前的位置："+module.getMatrix());
				canvas.drawBitmap(module.getBitmap(), module.getMatrix(), module.getMainPaint());
				if(mFocusModule != null && module == mFocusModule
						&& mFocusModule instanceof EditSurfaceViewModule
						&& mAllowShowBorder && mAllowDraw){
					EditSurfaceViewModule md = (EditSurfaceViewModule)mFocusModule;
					drawFrame(module.getDstPs(),canvas,(md).getPaintFrame());
					int left = (int) (module.getDstPs()[8]-md.getControlBmpWidth()/2);
					int top = (int) (module.getDstPs()[9]-md.getControlBmpHeight()/2);
					if(md.getControlBitmap() != null){
						drawControlPoints(canvas, md.getControlBitmap(), left, top, md.getMainPaint());
					}
					if(md.getDeleteBmp() != null){
						left = (int) (module.getDstPs()[0]-md.getDeleteBmpWidth()/2);
						top = (int) (module.getDstPs()[1]-md.getDeleteBmpHeight()/2);
						drawControlPoints(canvas, md.getDeleteBmp(), left, top, md.getMainPaint());
					}
					drawDialogOkBitmap(canvas,md);
				}
				module.onDrawCallback(canvas);
			}
			drawRahmenModule(canvas);
		}
	}

	protected void drawFrame(float[] dstPs,Canvas canvas,Paint paintFrame){  
		canvas.drawLine(dstPs[0], dstPs[1], dstPs[4], dstPs[5], paintFrame);  
		canvas.drawLine(dstPs[4], dstPs[5], dstPs[8], dstPs[9], paintFrame);  
		canvas.drawLine(dstPs[8], dstPs[9], dstPs[12], dstPs[13], paintFrame);  
		canvas.drawLine(dstPs[0], dstPs[1], dstPs[12], dstPs[13], paintFrame);  
	}

	/**
	 * 绘制对话框的确认按钮
	 * @param canvas
	 * @param md
	 */
	protected void drawDialogOkBitmap(Canvas canvas,BasicSurfaceViewModule md){
		if(md instanceof DialogSurfaceViewModule && ((DialogSurfaceViewModule)md).getOkBitmap() != null){
			DialogSurfaceViewModule dialog = (DialogSurfaceViewModule) md;
			int left = (int) (md.getDstPs()[4]-dialog.getOkBitmap().getWidth()/2);
			int top = (int) (md.getDstPs()[5]-dialog.getOkBitmap().getHeight()/2);
			drawControlPoints(canvas, dialog.getOkBitmap(), left, top, md.getMainPaint());
		}
	}

	protected void drawBackGround(Canvas canvas){
		if(mBgModule != null)
			canvas.drawBitmap(mBgModule.getBitmap(), mBgModule.getMatrix(), mBgModule.getMainPaint());
	}

	protected void drawRahmenModule(Canvas canvas){
		if(mRahmenModule != null)
			canvas.drawBitmap(mRahmenModule.getBitmap(), mRahmenModule.getMatrix(), mRahmenModule.getMainPaint());
	}

	/**
	 * 设置背景Module
	 * @param module
	 */
	public void setBackGroundModule(BasicSurfaceViewModule module){
		this.mBgModule = module;
		onDrawView();
	}

	public BasicSurfaceViewModule getBackGroundModule(){
		return mBgModule;
	}

	/**
	 * 设置相框Module
	 * @param module
	 */
	public void setRahmenModule(RahmenSurfaceModule module){
		this.mRahmenModule = module;
		onDrawView();
	}

	public RahmenSurfaceModule getRahmenModule(){
		return mRahmenModule;
	}

	/**
	 * 合成图片
	 * @param flag 缩放合成嘴型
	 * @return
	 */
	public Bitmap compositeBitmap(boolean flag){
		return onCompositeCore(flag, mBgModule.getCompositeBitmap());
	}

	/**
	 * 合成图片
	 * @param flag 是否合成嘴型
	 * @param backgroud 背景图片
	 * @return
	 */
	public Bitmap compositeBitmap(boolean flag,Bitmap backgroud){
		return onCompositeCore(flag, backgroud);
	}


	/**
	 * 合成图片核心方法
	 * @param flag	是否合成嘴型
	 * @param backgroud	背景图片
	 * @return
	 */
	public Bitmap onCompositeCore(boolean flag,Bitmap backgroud){
		Canvas canvas = null;
		Bitmap bmp = null;
		Paint paint = new Paint();
		bmp = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
		canvas = new Canvas(bmp);
		if(backgroud != null)
			canvas.drawBitmap(backgroud, 0, 0, paint);
		try{
			for (int i = 0; i < mModules.size(); i++) {
				BasicSurfaceViewModule module = mModules.get(i);
				if(flag || (module instanceof EditSurfaceViewModule && ((EditSurfaceViewModule) module).getAllowCompound())) {
					Bitmap mp = module.getCompositeBitmap();
					paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
					canvas.drawBitmap(mp, module.getMatrix(), paint);
				}
			}
			if(mRahmenModule != null)
				canvas.drawBitmap(mRahmenModule.getCompositeBitmap(), mRahmenModule.getMatrix(), paint);
		}catch (Exception e){
			PublicMethod.log_d("合成图片失败：" + e);
			e.printStackTrace();
		}
		return bmp;
	}




	/**
	 * 绘制控制点
	 * @param canvas
	 * @param controlBmp
	 * @param left
	 * @param top
	 * @param paint
	 */
	protected void drawControlPoints(Canvas canvas,Bitmap controlBmp,int left,int top,Paint paint) {
		canvas.drawBitmap(controlBmp, left,top, paint);
	}

	/**
	 * 绘制删除点
	 * @param canvas
	 * @param controlBmp
	 * @param left
	 * @param top
	 * @param paint
	 */
	protected void drawDeletePoint(Canvas canvas,Bitmap controlBmp,int left,int top,Paint paint) {
		canvas.drawBitmap(controlBmp, left,top, paint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		boolean temp = mAllowDraw;
		mAllowDraw = true;
		onDrawView();
		mAllowDraw = temp;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	private void onDispatchModuleTouch(BasicSurfaceViewModule module,MotionEvent event){
		if(mListener != null)
			mListener.onTouchModule(module, event);
	}

	/**
	 * 获取当前View真实高度
	 */
	public int getViewHeight(){
		if(mViewHeight > 0)
			return mViewHeight;
		else
			return getHeight();
	}

	/**
	 * 获取当前View真是宽度
	 * @return
	 */
	public int getViewWidth(){
		if(mViewWidth > 0){
			return mViewWidth;
		}else {
			return mViewWidth;
		}
	}

	public void setViewHeight(int height){
		this.mViewHeight = height;
	}

	public void setViewWidth(int width){
		this.mViewWidth = width;
	}

	/**
	 * 释放资源
	 */
	public void release(){
		for (int i = 0; i < mModules.size(); i++) {
			BasicSurfaceViewModule module = mModules.get(i);
			module.release();
		}
	}


}
