package com.petsay.component.customview.module;

import com.petsay.utile.PublicMethod;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;

/**
 * @author wangw
 * 对话框饰品
 */
public class DialogSurfaceViewModule extends EditSurfaceViewModule {


	private DialogSurfaceViewModuleCallback mCallback;
	private String mInputString;
	private TextPaint mTxtPaint;
	private int mTxtRangeW;
	private int mTxtRangeH;
	private int mTxtX;
	private int mTxtY;
	//文字大小最大值，单位：px
	private int mCurrTxtSize = 160;
	private StaticLayout mStaticLayout;
	private String mPreInputString;
//	private int mPreSizeAvg;
	private String mDefaultTip;
	private Typeface mTypeface;
	private long mDownTime;
	private Bitmap mOkBmp;
	private boolean mUpdate;
	private int mPreTxtW;
	private int mPreTxtH;
	
	public DialogSurfaceViewModule(Bitmap bmp,Bitmap controlBmp,Bitmap deleteBmp,String defaultTip,Bitmap okBmp) {
		super(bmp, controlBmp,deleteBmp);
		this.mOkBmp = okBmp;
		mDefaultTip = defaultTip;
		mInputString = mDefaultTip;
		mTxtPaint = new TextPaint();
		mTxtPaint.setColor(Color.BLACK);
		if(mTypeface != null)
			mTxtPaint.setTypeface(mTypeface);
	}
	
	@Override
	public void changeView(Bitmap bmp) {
		super.changeView(bmp);
		mUpdate = true;
	}
	
	/**
	 * 获取确认按钮图片
	 * @return
	 */
	public Bitmap getOkBitmap(){
		return mOkBmp;
	}
	
	public void setTextFont(Typeface typeface){
		this.mTypeface = typeface;
		if(mTxtPaint != null)
			mTxtPaint.setTypeface(mTypeface);
		mUpdate = true;
	}
	
	public void setTextColor(int color){
		if(mTxtPaint != null)
			mTxtPaint.setColor(color);
	}
	
	public void setTextBolod(boolean fakeBoldText){
		if(mTxtPaint != null)
			mTxtPaint.setFakeBoldText(fakeBoldText);
		mUpdate = true;
	}
	
	public void setTextShadow(boolean flag){
		if(mTxtPaint != null){
			if(flag)
				mTxtPaint.setShadowLayer(5f, 3, 3, Color.BLACK);
			else {
				mTxtPaint.setShadowLayer(0, 0, 0, Color.WHITE);
			}
		}
	}
	
	@Override
	protected void initModule() {
		super.initModule();
		//左右间距按整个宽的1/6
		//上下间距按整个高的30%
		int tw =mainBmpWidth/6;
		int th = (int)(mainBmpHeight*0.3);
		mTxtRangeW =mainBmpWidth - tw * 2;
		mTxtRangeH = mainBmpHeight - th * 2;
		mTxtX = tw;
		mTxtY = th;
		mPreInputString = "";
	}
	
	public void setCallback(DialogSurfaceViewModuleCallback callback){
		this.mCallback = callback;
	}
	
	public void setInputString(String str){
		mInputString = str.trim();
	}
	
	public String getInputString(){
		return mInputString;
	}
	
	@Override
	public int onDownHandler(MotionEvent event) {
		mDownTime = System.nanoTime();
		if(isOnInputRange(event.getX(),event.getY())){
//			onShowSoftInput();
		}else {
			onHidenSofInput();
		}
		return super.onDownHandler(event);
	}
	
	@Override
	public int onUpHandler(MotionEvent event) {
		//1.算出从按下到抬起的时间差
		mDownTime = System.nanoTime() - mDownTime;
		//2.将纳秒转换成毫秒
		mDownTime = mDownTime/(10000*100);
		//3.判断从按下到抬起的时间是否小于100毫秒，并且在输入范围内抬起，就弹出输入框
		if(mDownTime < 100 && isOnInputRange(event.getX(),event.getY())){
			onShowSoftInput();
		}
		return super.onUpHandler(event);
	}
	
	private boolean isOnInputRange(float evx,float evy){
		float sx =dstRect.width()/6;
		float sy =(float) (dstRect.height()*0.3);
		float tw =dstRect.width() - sx * 2;
		float th = dstRect.height() - sy * 2;
		float left = dstRect.left+sx;
		float top = dstRect.top+sy;
		float right = left+tw;
		float bottom = top+th;
		RectF rectF = new RectF(left, top, right, bottom);
		return rectF.contains(evx, evy);
	}
	
	
	@Override
	public void onDrawCallback(Canvas canvas) {
		if(TextUtils.isEmpty(mInputString) || canvas == null)
			return;
		int total = mInputString.length();
		if(!mInputString.equals(mPreInputString) || mUpdate){
			mPreInputString = mInputString;
			mCurrTxtSize = 160;
			Rect bounds = new Rect();
			//循环判断文字大小合适可以放下整个文字，不能小于30px
			while(mCurrTxtSize > 30){
				mTxtPaint.setTextSize(mCurrTxtSize);
				//计算出单个文字所占的平均大小
//				mPreSizeAvg = (int)mTxtPaint.measureText(mInputString)/mInputString.length();
				mTxtPaint.getTextBounds(mInputString, 0, mInputString.length(), bounds);
				PublicMethod.log_d("w=" + bounds.width() + " | h = " + bounds.height());
				mPreTxtW = bounds.width()/mInputString.length();
				mPreTxtH = bounds.height();
				//计算出一行可以放多少个字
				int row = mTxtRangeW/mPreTxtW;
				//计算出一列可以放多少个字
				int line = mTxtRangeH/mPreTxtH - 1;
				//计算总共可以放下多少字
				 total = line*row;
				 //如果当前字体大小正合身，就使用当前的字体大小
				if(total >= mInputString.length()){
					total = mInputString.length();
					break;
				}
				mCurrTxtSize -= 5;
				mUpdate = false;
			}
			mStaticLayout = new StaticLayout(mInputString, 0, total, mTxtPaint, mTxtRangeW, Alignment.ALIGN_CENTER, (float) 1.0,(float) 0.0, true, TruncateAt.MARQUEE, 0);
		}
		canvas.save();
		canvas.setMatrix(matrix);
		onDrawCore(canvas);
		super.onDrawCallback(canvas);
	}
	
	@Override
	public Bitmap getCompositeBitmap() {
		Bitmap bmp = Bitmap.createBitmap(mMaxWidth+mTxtRangeW, mMaxHeight+mTxtRangeH, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(mainBmp, 0, 0, paintFrame);
		if(TextUtils.isEmpty(mInputString) || mStaticLayout == null)// || mInputString.equals(mDefaultTip))
			return bmp;
		canvas.save();
//		//文字大小最大值，但px
//		int txtSize =80;
//		int total = mInputString.length();
//		//循环判断文字大小合适可以放下整个文字，不能小于30px
//		while(txtSize > 30){
//			mTxtPaint.setTextSize(txtSize);
//			//计算出单个文字所占的大小
//			int one = (int)mTxtPaint.measureText(mInputString, 0, 1);
//			//计算出一行可以放多少个字
//			int row = mTxtRangeW/one;
//			//计算出一列可以放多少个字
//			int line = mTxtRangeH/one;
//			//计算总共可以放下多少字
//			 total = line*row;
//			 //如果当前字体大小正合身，就使用当前的字体大小
//			if(total >= mInputString.length()){
//				total = mInputString.length();
//				break;
//			}
//			txtSize -= 5;
//		}
//		StaticLayout layout = new StaticLayout(mInputString, 0, total, mTxtPaint, mTxtRangeW, Alignment.ALIGN_NORMAL, (float) 1.0,(float) 0.0, true, TruncateAt.MARQUEE, 0);
//		canvas.translate(mTxtX,mTxtY);
//		mStaticLayout.draw(canvas);  
//		canvas.restore();
		onDrawCore(canvas);
		return bmp;
	}
	
	private void onDrawCore(Canvas canvas){
		int total = mInputString.length();
		int tempY = mTxtY;
		int tempX = mTxtX;
		if(mTxtRangeH - mStaticLayout.getHeight() > 0){
			tempY += (mTxtRangeH - mStaticLayout.getHeight())/2;
		}
		canvas.translate(tempX,tempY);
		
//		Rect rect = new Rect();
//		rect.set(0, 0, mTxtRangeW, mTxtRangeH);
//		Paint paint = getMainPaint();
//		paint.setColor(Color.RED);
//		canvas.drawRect(rect, paint);
		
		mStaticLayout.draw(canvas);
		canvas.restore();
	}
	
	
	
	protected void onShowSoftInput(){
		if(mCallback != null)
			mCallback.showSoftInput(this,mInputString);
	}
	
	protected void onHidenSofInput(){
		if(mCallback != null){
			mCallback.hidenSofInput(this);
		}
	}
	
	
	public interface DialogSurfaceViewModuleCallback{
		void showSoftInput(DialogSurfaceViewModule module,String preInputString);
		void hidenSofInput(DialogSurfaceViewModule module);
	}
	
	
	
	

}
