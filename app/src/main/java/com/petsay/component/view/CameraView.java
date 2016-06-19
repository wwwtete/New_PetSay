package com.petsay.component.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.petsay.R;
import com.petsay.utile.PublicMethod;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author wangw
 *
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder = null;
	private Context context;
	private Camera camera = null;
	private Camera.Size cameraSize;
	private int mDisplayHeight, mDisplayWidth,touchX, touchY;
	private Bitmap mBmpFocu;
	private int cameraPosition = 1;// 0代表前置摄像头，1代表后置摄像头
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();
	private OpenCameraCallBack mCallBack;

	public CameraView(Context context,int displayWidth,int displayHeight,OpenCameraCallBack openCameraCallBack){
		super(context);
		this.context = context;
		this.mDisplayHeight = displayHeight;
		this.mDisplayWidth = displayWidth;
		mCallBack=openCameraCallBack;
		initView();
	}

	public void initView() {
		holder = this.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
		mBmpFocu = BitmapFactory.decodeResource(getResources(),R.drawable.focus);
	}

	public Camera getCamera(){
		return camera;
	}

	public int getCameraPosition(){
		return cameraPosition;
	}

	public void setFocusView(Bitmap bmp){
		this.mBmpFocu = bmp;
	}

	public void takePicture(PictureCallback picture){
		try{
			if(this.camera != null)
				camera.takePicture(null, null, picture);
		}catch(Exception e){
			//发送友盟拍照失败
			MobclickAgent.onEvent(getContext(), "takePicture");
		}
	}

	public void setParameters(Parameters params){
		try{
			if(camera != null)
				camera.setParameters(params);
		}catch(Exception e){}
	}

	public void autoFocus(){
		try {
			if(camera != null && mIsCreated)
				camera.autoFocus(mAutoFocusCallBack);
		} catch (Exception e) {
		}
	}

	/**
	 * 切换前置后置摄像头
	 */
	public void changeCamera(){
		if(camera == null){
			PublicMethod.showToast(getContext(), "相机异常，请联系客服");
			return;
		}

		// 切换前后摄像头
		int cameraCount = 0;
		CameraInfo cameraInfo = new CameraInfo();
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

		for (int j = 0; j < cameraCount; j++) {
			Camera.getCameraInfo(j, cameraInfo);// 得到每一个摄像头的信息
			if (cameraPosition  == 1) {
				// 现在是后置，变更为前置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
					onOpenCamera(j);
					onChangeCamera();
					//					closeCamera();
					//					try {
					//						camera = Camera.open(j);// 打开当前选中的摄像头
					//						camera.setPreviewDisplay(getHolder());// 通过surfaceview显示取景画面
					//					} catch (IOException e) {
					//						e.printStackTrace();
					//					}
					//					if(checkCameraisNull())
					//						continue;
					//					camera.setDisplayOrientation(90);
					//					Camera.Parameters parameters = camera.getParameters();
					//					List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
					//					int curW=0,curH=0;
					//					int previewH=0,previewW=0;
					//					Collections.sort(sizeList, sizeComparator);
					//					for (int i = 0; i < sizeList.size(); i++) {
					////						System.out.println("camera width:"+sizeList.get(i).width+"      height:"+sizeList.get(i).height);
					//						if ((sizeList.get(i).height / mDisplayWidth)==(sizeList.get(i).width / mDisplayHeight)) {
					//							curH = (sizeList.get(i).width*mDisplayWidth)/sizeList.get(i).height;
					//							curW=mDisplayWidth;
					//							previewH=sizeList.get(i).width;
					//							previewW=sizeList.get(i).height;
					//							cameraSize=sizeList.get(i);
					//							break;
					//						}
					//					}
					//					onUpdateLayout(curW, curH);
					//					parameters.setPreviewSize(previewH, previewW);
					//					parameters.setPictureFormat(PixelFormat.JPEG);
					//					parameters.setPictureSize(previewH, previewW);
					//					parameters.setJpegQuality(100);
					//					try {
					//						camera.setParameters(parameters);
					//						camera.startPreview();
					//					} catch (Exception e) {
					//						e.printStackTrace();
					//						PublicMethod.showToast(getContext(), "请开启相机权限");
					//					}
					cameraPosition = 0;
					break;
				}
			} else {
				// 现在是前置， 变更为后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
					onOpenCamera(j);
					onChangeCamera();
					//					closeCamera();
					//					try {
					//						camera = Camera.open(j);// 打开当前选中的摄像头
					//						camera.setPreviewDisplay(getHolder());// 通过surfaceview显示取景画面
					//					} catch (IOException e) {
					//						e.printStackTrace();
					//					}
					//					if(checkCameraisNull())
					//						continue;
					//					camera.setDisplayOrientation(90);
					//					Camera.Parameters parameters = camera.getParameters();
					//					List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
					//					int curW=0,curH=0;
					//					int previewH=0,previewW=0;
					//					Collections.sort(sizeList, sizeComparator);
					//					for (int i = 0; i < sizeList.size(); i++) {
					////						System.out.println("camera width:"+sizeList.get(i).width+"      height:"+sizeList.get(i).height);
					//						if ((sizeList.get(i).height / mDisplayWidth)==(sizeList.get(i).width / mDisplayHeight)) {
					//							curH = (sizeList.get(i).width*mDisplayWidth)/sizeList.get(i).height;
					//							curW=mDisplayWidth;
					//							previewH=sizeList.get(i).width;
					//							previewW=sizeList.get(i).height;
					//							cameraSize=sizeList.get(i);
					//							break;
					//						}
					//					}
					//					onUpdateLayout(curW, curH);
					//					parameters.setPreviewSize(previewH, previewW);
					//					parameters.setPictureFormat(PixelFormat.JPEG);
					//					parameters.setPictureSize(previewH, previewW);
					//					parameters.setJpegQuality(100);
					//					try {
					//						camera.setParameters(parameters);
					//						camera.startPreview();
					//					} catch (Exception e) {
					//						e.printStackTrace();
					//						PublicMethod.showToast(getContext(), "请开启相机权限");
					//					}
					cameraPosition = 1;
					break;
				}
			}
		}
	}

	protected boolean checkCameraisNull(){
		boolean flag = camera == null;
		if(flag)
			PublicMethod.showToast(getContext(), "相机启动异常，请联系客服");
		return flag;
	}

	/**
	 * 打开相机并设置预览
	 * @param cameraId 相机ID -1为打开默认的相机
	 */
	protected void onOpenCamera(int cameraId){
		try {
			closeCamera();
			if(cameraId == -1){
				camera = Camera.open();
			}else {
				camera = Camera.open(cameraId);
			}
			// 设置camera预览的角度，因为默认图片是倾斜90度的
			camera.setDisplayOrientation(90);
			// 设置holder主要是用于surfaceView的图片的实时预览，以及获取图片等功能，可以理解为控制camera的操作..
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			if(camera != null){
				camera.release();
				camera = null;
			}
			e.printStackTrace();
			PublicMethod.showToast(getContext(), "请开启相机权限");
		}
	}

	protected void onChangeCamera(){
		if(camera == null)
			return;
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
		List<Camera.Size> picSizeList = parameters.getSupportedPictureSizes();
		int curW=0,curH=0;
		int previewH=0,previewW=0;
		int picH=0,picW=0;
		if(sizeList != null ){
			Collections.sort(sizeList, sizeComparator);
			for (int i = 0; i < sizeList.size(); i++) {
				Size s = sizeList.get(i);
				if ((s.height / mDisplayWidth)==(s.width / mDisplayHeight)) {
					curH = (s.width*mDisplayWidth)/s.height;
					curW=mDisplayWidth;
					previewH=s.width;
					previewW=s.height;
					cameraSize=s;
					break;
				}
			}
		}
		if(picSizeList != null){
			Collections.sort(picSizeList,sizeComparator);
			int minDisparity = -1;
			for (int i = 0; i < picSizeList.size(); i++) {
				Size s = picSizeList.get(i);
				int disparity =Math.abs(s.width * s.height - previewH * previewW);
				if(disparity <= minDisparity || minDisparity == -1){
					picW = s.height;
					picH = s.width;
					minDisparity = disparity;
				}else {
					break;
				}
			}
		}
		picH = picH == 0 ? picH = previewH : picH;
		picW = picW == 0 ? picW = previewW : picW;
		onUpdateLayout(curW, curH);
		//		PublicMethod.log_d("最终确认值："+previewW+"  |  H :"+previewH);
		parameters.setPreviewSize(previewH, previewW);
		parameters.setPictureFormat(PixelFormat.JPEG);
		parameters.setPictureSize(picH, picW);
		parameters.setJpegQuality(100);
		try {
			camera.setParameters(parameters);
			camera.startPreview();
			camera.autoFocus(mAutoFocusCallBack);	
		} catch (Exception e) {
			e.printStackTrace();
			PublicMethod.showToast(getContext(), "请开启相机权限");
		}

	}

	protected void onUpdateLayout(int w,int h){
		LayoutParams params = (LayoutParams) getLayoutParams();
		if(params != null){
			params.width = w;
			params.height = h;
			this.setLayoutParams(params);
			invalidate();
		}
	}

	protected void closeCamera(){
		if(camera != null){
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	protected void release(){
		closeCamera();
	}


	private AutoFocusCallback mAutoFocusCallBack = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
			}
		}
	};

	private boolean mIsCreated = false;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		onOpenCamera(-1);
		mIsCreated = true;
		mCallBack.isOpenCamera(mIsCreated);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		onChangeCamera();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		release();
	}


	class CameraSizeComparator implements Comparator<Size>{

		@Override
		public int compare(Size lhs, Size rhs) {
			if(lhs.width == rhs.width)
				return 0;
			else if(lhs.width < rhs.width)
				return 1;
			else
				return -1;
		}

	}

	public interface OpenCameraCallBack{
		/**
		 * 是否已打开相机
		 * @param result
		 */
		void isOpenCamera(boolean result);
	}

}
