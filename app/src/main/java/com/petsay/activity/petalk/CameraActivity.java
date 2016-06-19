package com.petsay.activity.petalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.petalk.capture.DemoActivity;
import com.petsay.activity.petalk.publishtalk.BaseEditActivity;
import com.petsay.activity.petalk.publishtalk.MultiFunEditActivity;
import com.petsay.activity.petalk.publishtalk.SimpleEditActivity;
import com.petsay.activity.user.PetInfo_Acitivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.media.ExAudioMediaPlayer;
import com.petsay.component.view.CameraView;
import com.petsay.component.view.CameraView.OpenCameraCallBack;
import com.petsay.component.view.ExHintView;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetTagVo;

import java.lang.reflect.Field;

import roboguice.inject.InjectView;

public class CameraActivity extends BaseActivity implements OnClickListener ,OpenCameraCallBack{


	private int[] SOUND_ID = {-1,R.raw.sound1,R.raw.sound2,R.raw.sound3,R.raw.sound4,
			R.raw.sound5,R.raw.sound6,R.raw.sound7,R.raw.sound8};
	private int[] SOUND_ICON = {R.drawable.camera_sound_0,R.drawable.camera_sound_1,
			R.drawable.camera_sound_2,R.drawable.camera_sound_3,
			R.drawable.camera_sound_4,R.drawable.camera_sound_5,
			R.drawable.camera_sound_6,R.drawable.camera_sound_7,
			R.drawable.camera_sound_8};

	public static CameraActivity instance;

	/** Called when the activity is first created. */
	private Button btnCamera, btnChoosePic,
	btnCancle;
	private ImageView mIvChangeCamera;
	private ImageView mIvFlashLight;
	private RelativeLayout rlayoutCameraBottom;

	private RelativeLayout mRlayoutTitle;
	//	private int cameraPosition = 1;// 0代表前置摄像头，1代表后置摄像头
	// camera 类
	//	private Camera camera = null;
	private Camera.Size cameraSize;
	// 继承surfaceView的自定义view 用于存放照相的图片
	private CameraView mCameraView = null;
	Bundle bundle = null; // 声明一个Bundle对象，用来存储数据
	private SurfaceHolder surfaceHolder;
	private FocusView focusView;
	private int touchX, touchY;

	@InjectView(R.id.iv_sound)
	private ImageView mIvSound;
	@InjectView(R.id.layout_sound)
	private LinearLayout mLayoutSound;
	@InjectView(R.id.tv_tip)
	private TextView mTvTip;
//	private ExSoundPool mSoundPool;
	private ExAudioMediaPlayer mPlayer;
	private RelativeLayout mCameraLayout = null;
	int mDisplayHeight, mDisplayWidth;
	private int mBottomHeight=0;


	private int fromType=0;
    private int mModel;
    private PetTagVo mTagVo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		fromType=getIntent().getIntExtra("fromType", 0);
        mModel = getIntent().getIntExtra("model",0);
        mTagVo = (PetTagVo) getIntent().getSerializableExtra("tag");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		mDisplayWidth = PublicMethod.getDisplayWidth(this);
		mDisplayHeight = PublicMethod.getDisplayHeight(this);
		setContentView(R.layout.camera_main);
		initView();
		initSoundLayout();
		mCameraLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mCameraView.autoFocus();
				touchX = (int) arg1.getX() - 70;
				touchY = (int) arg1.getY() - 70;
				focusView.invalidate();
				return true;
			}
		});
	}

	protected void initView() {
		mCameraLayout = (RelativeLayout) findViewById(R.id.cameraView);
		LayoutParams Cameraparams = (LayoutParams) mCameraLayout.getLayoutParams();
		Cameraparams.height = mDisplayHeight;
		mCameraLayout.setLayoutParams(Cameraparams);
		btnCamera = (Button) findViewById(R.id.btn_camera);
		btnCancle=(Button) findViewById(R.id.btn_cancle);
		mRlayoutTitle=(RelativeLayout) findViewById(R.id.rlayout_title);
		mIvChangeCamera = (ImageView) findViewById(R.id.iv_change_camera);
		mIvFlashLight = (ImageView) findViewById(R.id.iv_flashlight);
		btnChoosePic = (Button) findViewById(R.id.btn_choose_pic);
		btnCamera.setOnClickListener(this);
		mRlayoutTitle.setOnClickListener(this);
		mIvChangeCamera.setOnClickListener(this);
		mIvFlashLight.setOnClickListener(this);
		btnChoosePic.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		mIvFlashLight.setVisibility(View.GONE);
		rlayoutCameraBottom=(RelativeLayout) findViewById(R.id.rlayout_camera_bottom);
		int titleH=PublicMethod.getDiptopx(getApplicationContext(), 60);
		mBottomHeight=mDisplayHeight-(mDisplayWidth+titleH);
		RelativeLayout.LayoutParams rlayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, mBottomHeight);
		rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlayoutCameraBottom.setLayoutParams(rlayoutParams);
		mCameraLayout.removeAllViews();
		mCameraView = new CameraView(CameraActivity.this,mDisplayWidth,mDisplayHeight,this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mDisplayWidth,mDisplayHeight);
		mCameraLayout.addView(mCameraView, params);
		focusView = new FocusView(getApplicationContext());
		mCameraLayout.addView(focusView, params);
	}

	private ImageView mCurrSound;
	private void initSoundLayout(){
//		mSoundPool = new ExSoundPool(this);
		mPlayer = new ExAudioMediaPlayer(this);
		mIvSound.setOnClickListener(this);
		int space = PublicMethod.getDiptopx(this, 5);
		int inSpace = PublicMethod.getDiptopx(this, 2);
		for (int i = 0; i < SOUND_ICON.length; i++) {
			ImageView img = new ImageView(this);
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(space, 0, space, 0);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayoutSound.setVisibility(View.GONE);
					mTvTip.setVisibility(View.VISIBLE);
					int resId = (Integer) v.getTag();
					if(resId == -1){
//						mSoundPool.stop();
						mPlayer.stopPlay();
					}else {
//						mSoundPool.play(resId);
						mPlayer.playResId(resId);
						mPlayer.setLooping(true);
					}
					if(mCurrSound != v){
						mCurrSound.setBackgroundDrawable(null);
					}
					mIvSound.setImageDrawable(((ImageView)v).getDrawable());
					mCurrSound = (ImageView) v;
				}
			});
			
			img.setTag(SOUND_ID[i]);
			img.setScaleType(ScaleType.CENTER);
			img.setPadding(inSpace,inSpace,inSpace,inSpace);
			img.setImageResource(SOUND_ICON[i]);
			img.setBackgroundResource(SOUND_ICON[i]);
			if(i == 0){
				mCurrSound = img;
//				SkinHelp.setBackground(mCurrSound, CameraActivity.this, getString(R.string.circle_selected_bg));
			}
			mLayoutSound.addView(img,params);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_camera:
			mCameraView.takePicture(picture);
			break;
		case R.id.iv_change_camera:
			mCameraView.changeCamera();
			break;
		case R.id.iv_flashlight:
			onChangeFlashMode();
			break;
		case R.id.btn_choose_pic:
			//			btnChoosePic.startAnimation(initAnimation());
			jumpMediaStore();
			break;
		case R.id.btn_cancle:
			finish();
			break;
		case R.id.iv_sound:
			if(mLayoutSound.getVisibility() == View.VISIBLE){
				mTvTip.setVisibility(View.VISIBLE);
				mLayoutSound.setVisibility(View.GONE);
			}else {
				mLayoutSound.setVisibility(View.VISIBLE);
				mTvTip.setVisibility(View.GONE);
			}
			break;
		}
	}
	

	private void jumpMediaStore(){
		try{
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			startActivityForResult(intent, 1);
		}catch(Exception e){
			showToast("启动相册失败！");
		}
	}

	/**
	 * 闪光灯
	 */
	private void onChangeFlashMode() {
		Camera.Parameters parameters = null;
		try {
			parameters = mCameraView.getCamera().getParameters();
		} catch (Exception e) {
		}
		if(parameters == null){
			showToast("相机启动异常，请重新启动一次");
			return;
		}
		
		String mode = parameters.getFlashMode();
		if(TextUtils.isEmpty(mode)){
			return;
		}
		if (Parameters.FLASH_MODE_OFF.equals(mode)) {
			parameters.setFlashMode(Parameters.FLASH_MODE_ON);
			mIvFlashLight.setImageResource(R.drawable.open_flashhlight);
		} else if(Parameters.FLASH_MODE_ON.equals(mode)){
			parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
			mIvFlashLight.setImageResource(R.drawable.auto_flashhlight);
		}else{
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			mIvFlashLight.setImageResource(R.drawable.close_flashlight);
		}
		mCameraView.setParameters(parameters);		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//从相册返回
		case 1:
			if (null!=data) {
				Uri originalUri = data.getData();        //获得图片的uri 
				Intent intent=new Intent(CameraActivity.this, DemoActivity.class);
				intent.putExtra("fromType", fromType);
				intent.putExtra("uri", originalUri);
                intent.putExtra("model",mModel);
                intent.putExtra("tag",mTagVo);
				startActivityForResult(intent,200);
			}
			break;
		case 200:
			if (resultCode==200) {
				setResult(PetInfo_Acitivity.GO_CAMERA_CODE);
				finish();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class FocusView extends View {

		private Bitmap mBmp;

		public FocusView(Context context) {
			super(context);
			mBmp = BitmapFactory.decodeResource(getResources(),R.drawable.focus);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (touchX <= 0 && touchY <= 0) {
			} else {
				canvas.drawBitmap(mBmp, touchX, touchY, null);
			}
		}

	}

	public  int getStatusBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}


	// 回调用的picture，实现里边的onPictureTaken方法，其中byte[]数组即为照相后获取到的图片信息
	private Camera.PictureCallback picture = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			showLoading(false);
			System.gc();
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			//			Options opts = new Options();
			//			opts.inSampleSize = 2;
			//			Bitmap bitmap =BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			rotateBitmap(bitmap,"");
		}
	};

	private void rotateBitmap(Bitmap bitmap, String imgPath) {
		Matrix matrix = new Matrix();
		int bx = 0;
		int bw = 0;
		if (mCameraView.getCameraPosition()==1) {
			matrix.postRotate(90);
			bw= bitmap.getHeight();
			int titleH=PublicMethod.getDiptopx(getApplicationContext(), 60);
			bx = (bw*titleH)/mDisplayWidth;
//			Bitmap temp = bitmap.copy(Config.ARGB_4444, true);
//			bitmap.recycle();
//			bitmap = null;
//			Bitmap dstbmp = null;
//			if((x+bH) <= temp.getWidth())
//				dstbmp = Bitmap.createBitmap(temp, x,0, bH,bH, matrix, true);
//			else {
//				dstbmp = temp;//Bitmap.createBitmap(temp, 0,0, bw,bw, matrix, true);
//			}
//			jumpActivity(dstbmp);
		}else if(mCameraView.getCameraPosition()==0){
			matrix.postRotate(270);//顺时针旋转270度
			matrix.postScale(-1, 1);   //镜像水平翻转
			bw=bitmap.getHeight();
			bx = (bw*mBottomHeight)/mDisplayWidth;
//			int titleH=PublicMethod.getDiptopx(getApplicationContext(), 60);
//			Bitmap temp = bitmap.copy(Config.ARGB_4444, true);
			// imgPath=Environment.getExternalStorageDirectory()+"/";
//			 String img1=FileUtile.getPath(getApplicationContext(), Constants.FilePath)+"123.jpg";
//			 FileUtile.saveImage(img1, bitmap,100);
//			bitmap.recycle();
//			bitmap = null;
			// System.out.println("CameraActivity.rotateBitmap() x:"+(bH*titleH)/mDisplayWidth+"   y:0     width:"+bH+"     height:"+bH);
//			Bitmap dstbmp = null;
//			if((x+bH) <= temp.getWidth())
//				dstbmp = Bitmap.createBitmap(temp,x,0, bH,bH, matrix, true);
//			else
//				dstbmp = temp;
//			jumpActivity(dstbmp);
		}
		Bitmap dstbmp = null;
		if((bx+bw) > bitmap.getWidth())
			bx = bx - ((bx+bw)-bitmap.getWidth()) - 10; 
			dstbmp = Bitmap.createBitmap(bitmap,bx,0, bw,bw, matrix, true);
//		else
//			dstbmp = bitmap;
		jumpActivity(dstbmp);
	}

	private void jumpActivity(Bitmap bmp){
		if(bmp == null){
			showToast("图片为null，无法编辑");
			return;
		}

		//		intent.putExtra("imgPath", impPath);

		if (fromType == 1) {
			PetInfo_Acitivity.headBitmap=bmp;
			setResult(PetInfo_Acitivity.GO_CAMERA_CODE);
		} else {
			PublicMethod.outMM("拍照：");
//            Intent intent = new Intent(CameraActivity.this, EditImgActivity.class);
//            EditImgActivity.petBg = bmp;
            Intent intent = null;
            if(mModel == 0) {
                intent = new Intent(CameraActivity.this, MultiFunEditActivity.class);
            }else {
                intent = new Intent(CameraActivity.this,SimpleEditActivity.class);
            }
            if(mTagVo != null)
                intent.putExtra("tag",mTagVo);
            BaseEditActivity.CameraBmp = bmp;

            startActivity(intent);
			closeLoading();
		}
		finish();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
//		mSoundPool.stop();
		mPlayer.pausePlay();
		super.onPause();
	}

    @Override
    protected void onResume() {
        super.onResume();
        checkShowHint();
    }

    private void checkShowHint(){
        final SharePreferenceCache cache = SharePreferenceCache.getSingleton(this);
        boolean isShow = cache.getSharedPreferences().getBoolean("cmeara_sound_showhint", true);
        if(isShow) {
            ExHintView ch =  (ExHintView) findViewById(R.id.hint_camera);
            ch.show(true);
            cache.getSharedPreferences().edit()
                    .putBoolean("cmeara_sound_showhint",false)
                    .commit();
        }
    }

    @Override
	protected void onDestroy() {
		instance = null;
//		mSoundPool.rerelease();
		mPlayer.stopPlay();
		super.onDestroy();
	}

	@Override
	public void isOpenCamera(boolean result) {
		if (result && mCameraView.getCamera() != null) {
			//闪光灯是否可用
			Camera.Parameters parameters = mCameraView.getCamera().getParameters();
			String mode = parameters.getFlashMode();
			if(TextUtils.isEmpty(mode)){
				mIvFlashLight.setVisibility(View.GONE);
			}else {
				mIvFlashLight.setVisibility(View.VISIBLE);
			}
			//是否有多个摄像头
			int cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
			if (cameraCount<=1) {
				mIvChangeCamera.setVisibility(View.GONE);
			}else {
				mIvChangeCamera.setVisibility(View.VISIBLE);
			}

		}
		//		else {
		//			mIvFlashLight.setVisibility(View.GONE);
		//		}
	}

	/**
	public void saveBitmap(Bitmap bitmap, String imgPath) {
		//		System.out.println("bitmap   width:"+bitmap.getWidth()+"  height:"+bitmap.getHeight());
		//		int bW=bitmap.getWidth();
		//		int titleH=PublicMethod.getDiptopx(getApplicationContext(), 60);
		//		System.gc();
		//		ImageLoader.getInstance().clearMemoryCache();
		//		Bitmap tempBitmap = bitmap;//Bitmap.createBitmap(bitmap, 0, (bW*titleH)/mDisplayWidth, bW, bW);
		//		File f = new File(imgPath);
		//		if (f.exists()) {
		//			f.delete();
		//		}
//		EditImgActivity.petBg = bitmap;
//		FileUtile.saveImage(imgPath, bitmap,100);
//		bitmap.recycle();
//		bitmap = null;
	}
	class SaveImageTask extends AsyncTask<Bitmap, Void, String>{
		@Override
		protected String doInBackground(Bitmap... params) {
			Bitmap bmp = params[0];
			if(bmp == null)
				return "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
			String filename = format.format(date) + ".jpg";
			date = null;
			format = null;
			String fileDirs = FileUtile.getPath(CameraActivity.this, Constants.FilePath+"temp");
			File fileFolder = new File(fileDirs);
			if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
				fileFolder.mkdir();
			}
			String filePath = fileFolder + "/" + filename;
			rotateBitmap(bmp, filePath);
			return filePath;
		}

		@Override
		protected void onPostExecute(String result) {
			if(TextUtils.isEmpty(result)){
				PublicMethod.showToast(CameraActivity.this, "保存图片失败！");
				CameraActivity.this.finish();
			}else{
				jumpEditImgActivity(result);
			}
		}
	}

	private void jumpEditImgActivity(String impPath){
		Intent intent = new Intent(CameraActivity.this,EditImgActivity.class);
		intent.putExtra("imgPath", impPath);
		startActivity(intent);
		PublicMethod.closeProgressDialog(mDialog, CameraActivity.this);
		finish();
	}
	 */
}