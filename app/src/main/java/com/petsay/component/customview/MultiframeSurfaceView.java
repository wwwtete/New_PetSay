package com.petsay.component.customview;

import java.util.ArrayList;
import java.util.List;

import com.petsay.component.customview.module.BasicSurfaceViewModule;
import com.petsay.component.customview.module.EditSurfaceViewModule;
import com.petsay.component.customview.module.MultiframeModule;
import com.petsay.component.customview.module.RahmenSurfaceModule;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author wangw
 * 可播放和编辑帧动画的SurfaceView
 */
public class MultiframeSurfaceView extends BasicSurfaceView {

	private List<MultiframeModule> mFramModules;
	private PlayMultiFrameThread mMFThread;

	public MultiframeSurfaceView(Context context){
		super(context);
	}

	public MultiframeSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void initViews() {
		super.initViews();
		mFramModules = new ArrayList<MultiframeModule>();
	}

	@Override
	public void addModule(BasicSurfaceViewModule module) {
		mAllowDraw = true;
		super.addModule(module);
		if(module instanceof MultiframeModule)
			mFramModules.add((MultiframeModule) module);
	}

	@Override
	public void addModule(int location, BasicSurfaceViewModule module) {
		mAllowDraw = true;
		super.addModule(location, module);
		if(module instanceof MultiframeModule)
			mFramModules.add(location,(MultiframeModule) module);
	}

	@Override
	public void removeModule(BasicSurfaceViewModule module) {
		super.removeModule(module);
		if(module instanceof MultiframeModule)
			mFramModules.remove(module);
	}

	/**
	 * 播放帧动画
	 */
	public void playGif(){
		if(mFramModules.size() > 0 ){
			if(mMFThread != null && mMFThread.mRuning){
				return;
			}
			hidenBorder(true);
			mMFThread = new PlayMultiFrameThread(this);
			mMFThread.setName("GIF-"+mMFThread.getName());
			mMFThread.start();
		}
	}

	/**
	 * 停止播放帧动画
	 */
	public void stopGif(){
//		stopThread();
		if(mMFThread != null){
			mMFThread.stopThread();
		}
		try{
			mMFThread = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 绘制帧动画
	 * @param mtModule
	 * @param frameIndex
	 */
	protected void onDrawFrame(MultiframeModule mtModule,int frameIndex){
//				Canvas canvas = getHolder().lockCanvas(module.getRect());
		Canvas canvas = getHolder().lockCanvas();
		if(canvas == null){
			return;
		}
		Bitmap bmp = mtModule.getFrame(frameIndex);
		if(bmp == null){
			getHolder().unlockCanvasAndPost(canvas);
			return;
		}
//		onDrawCore(canvas);
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		drawBackGround(canvas);
		for (int i = 0; i < mModules.size(); i++) {
			BasicSurfaceViewModule module = mModules.get(i);
			if(module.getBitmap() != null && module != mtModule ){
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
			}
			module.onDrawCallback(canvas);
		}
		
		canvas.drawBitmap(bmp, mtModule.getMatrix(), mtModule.getMainPaint());
		drawRahmenModule(canvas);
		getHolder().unlockCanvasAndPost(canvas);
	}
	
	class PlayMultiFrameThread extends DrawMotionlessThread{

		private MultiframeSurfaceView mMFView;

		public PlayMultiFrameThread(MultiframeSurfaceView view) {
			super(view);
			this.mMFView = view;
		}

		@Override
		public void run() {
			try {
				//判断是否允许继续运行
				while (mRuning) {
					//1.获取当前时间
					long endTime =0;
					int diffTime = 0;
					long startTime = 0;
					//2.增加线程锁线程安全
					synchronized (mMFView.getHolder()) {
						//3.进行绘画
						//TODO 暂定由一个线程串行绘制多个帧动画Module，以后可以为每个帧动画起一个单独线程来绘制
						for (int i = 0; i < mFramModules.size(); i++) {
							MultiframeModule fm = mFramModules.get(i);
							for (int j = 0; j < fm.getFrameCount(); j++) {
								startTime = System.currentTimeMillis();
								mMFView.onDrawFrame(fm, j);
								//3.获取绘画结束时间
								endTime = System.currentTimeMillis();
								//4.计算更新一次画面所需要的时间
								diffTime = (int)(endTime - startTime);
//								PetsayLog.d("view", "绘制所需时间==="+diffTime);
								//				PublicMethod.log_d("当前线程="+Thread.currentThread().getName()+"更新一次画面所需时间："+diffTime);
								//5.确保以80帧每秒的频率刷新
								while(diffTime <= 120){
									if(!mRuning)
										return;
									diffTime = (int)(System.currentTimeMillis() - startTime);
									//线程等待
									Thread.yield();
								}
							}
						}
					}
					//TODO 临时定为200毫秒，时间有待优化
					Thread.sleep(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

}
