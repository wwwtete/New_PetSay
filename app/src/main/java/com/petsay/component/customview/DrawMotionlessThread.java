package com.petsay.component.customview;


/**
 * @author wangw
 * 绘制线程
 */
public class DrawMotionlessThread extends Thread {

	/**每80帧刷新一次屏幕**/  
	public static final int TIME_IN_FRAME = 80;
	
	protected boolean mRuning;
	protected BasicSurfaceView mView;
	
	public DrawMotionlessThread(BasicSurfaceView view){
		this.mView = view;
	}
	
	/**
	 * 启动线程
	 */
	@Override
	public synchronized void start() {
		mRuning = true;
		super.start();
	}
	
	/**
	 * 停止线程
	 */
	public void stopThread(){
		mRuning = false;
	}
	
	
	
	@Override
	public void run() {
		try {
			//判断是否允许继续运行
			while (mRuning) {
				//1.获取当前时间
				long startTime = System.currentTimeMillis();
				//2.增加线程锁线程安全
				synchronized (mView.getHolder()) {
					//3.进行绘画
					mView.onDrawView();
				}
				//3.获取绘画结束时间
				long endTime = System.currentTimeMillis();
				//4.计算更新一次画面所需要的时间
				int diffTime = (int)(endTime - startTime);
				//				PublicMethod.log_d("当前线程="+Thread.currentThread().getName()+"更新一次画面所需时间："+diffTime);
				//5.确保以80帧每秒的频率刷新
				while(diffTime <= TIME_IN_FRAME){
					if(!mRuning)
						return;
					diffTime = (int)(System.currentTimeMillis() - startTime);
					//线程等待
					Thread.yield();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}
