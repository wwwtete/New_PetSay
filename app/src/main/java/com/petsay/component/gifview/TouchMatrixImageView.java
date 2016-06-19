package com.petsay.component.gifview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.petsay.R;
/** 
 * 使用矩阵控制图片移动、缩放、旋转 
 * @author wangw
 */  
public class TouchMatrixImageView extends SurfaceView implements SurfaceHolder.Callback {  

	public static final int MAX_BMP = 600;
	public static final int MIN_BMP = 200;
	/**每80帧刷新一次屏幕**/  
	public static final int TIME_IN_FRAME = 80;

	protected Context context ;
	/**原图*/
	protected Bitmap mainBmp;
	/**控制点*/
	protected Bitmap controlBmp ;  
	protected int mainBmpWidth , mainBmpHeight , controlBmpWidth , controlBmpHeight ;  
	protected Matrix matrix ;   
	protected float [] srcPs , dstPs ;  
	/**上一次的矩形位置 | 目标矩形位置*/
	protected RectF srcRect , dstRect ;
	/**原始画笔*/
	protected Paint paint ;
	/**背景画笔*/
	protected Paint paintRect;
	/**控制线的画笔*/
	protected Paint paintFrame;  
	/**X位移值 | Y位移值*/
	protected float deltaX = 0, deltaY = 0; //位移值  
	/**缩放值  */
	protected float scaleValue = 1; //缩放值  
	/**最后一次的操作位置*/
	protected Point lastPoint ;
	/**上次中心轴的位置*/
	protected Point prePivot;
	/**最后一次的中心轴位置*/
	protected Point lastPivot;  
	/**当前操作点对称点  */
	protected Point symmetricPoint  = new Point();    //当前操作点对称点  

	/** 
	 * 图片操作类型 
	 */  
	/**操作类型：没有任何动作*/
	public static final int OPER_DEFAULT = -1;      //默认  
	/**操作类型：移动  */
	public static final int OPER_TRANSLATE = 0;     //移动  
	/**操作类型：缩放或旋转  */
	public static final int OPER_SCALE_ROTATE = 1;         //缩放|旋转
	/**操作类型：水平缩放*/
	public static final int OPER_Horizontal_SCALE = 5;
	/**操作类型:垂直缩放*/
	public static final int OPER_VERTICAL_SCALE = 6;
	/**操作类型：缩放*/
	public static final int OPER_SCALE = 2;
	//    /**操作类型：旋转  */
	public static final int OPER_ROTATE = 3;        //旋转
	/**操作类型：选择 */
	public static final int OPER_SELECTED = 4;      //选择
	/**最后一次操作类型*/
	public int lastOper = OPER_DEFAULT;  

	/* 图片控制点 
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */  
	/**-1*/
	public static final int CTR_NONE = -1;
	/**0*/
	public static final int CTR_LEFT_TOP = 0;
	/**1*/
	public static final int CTR_MID_TOP = 1;
	/**2*/
	public static final int CTR_RIGHT_TOP = 2;
	/**3*/
	public static final int CTR_RIGHT_MID = 3;
	/**4*/
	public static final int CTR_RIGHT_BOTTOM = 4;
	/**5*/
	public static final int CTR_MID_BOTTOM = 5;
	/**6*/
	public static final int CTR_LEFT_BOTTOM = 6;
	/**7*/
	public static final int CTR_LEFT_MID = 7;
	/**8*/
	public static final int CTR_MID_MID = 8; 
	/**当前控制点*/
	public int current_ctr = CTR_NONE;
	/**上次的缩放比例*/
	protected float mPreScaleValue;
	/**当前保存的x*/
	protected float mDownx; // 当前保存的x  
	/**当前保存的y*/
	protected float mDowny; // 当前保存的y  
	/**当前触屏的x*/
	protected float curTouchX; // 当前触屏的x  
	/**当前触摸的y*/
	protected float curTouchY; // 当前触摸的y  
	/**改变的角度*/
	protected float changeDegree;
	protected int mViewWidth;
	protected int mViewHeight;   

	public TouchMatrixImageView(Context context){  
		super(context);  
		this.context = context ; 
		getHolder().addCallback(this);
		initView();
	}  

	public TouchMatrixImageView(Context context, AttributeSet attrs) {  
		super(context, attrs);  
		this.context = context ;  
		initView();
	}  

	protected void initView(){
		getHolder().addCallback(this);
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
	}

	public void changeView(Bitmap bmp){
		this.mainBmp = bmp;
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
		//4.初始化目标控制点
		dstPs = srcPs.clone();  
		//5.初始化控制矩形的位置数据
		srcRect = new RectF(0, 0, mainBmpWidth, mainBmpHeight);  
		dstRect = new RectF();  
		//6.初始化中心轴
		prePivot = new Point(mainBmpWidth/2, mainBmpHeight/2);  
		lastPivot = new Point(mainBmpWidth/2, mainBmpHeight/2);
		setMatrix(OPER_TRANSLATE);
		onDrawView();
	}

	/** 
	 * 初始化数据 
	 */  
	public void initData(Bitmap bmp,int width,int height){  
		//1.初始化图片
		//		mainBmp = bmp;  
		controlBmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.yuan);
		//2.获取图片的宽高
		mainBmpWidth = bmp.getWidth();  
		mainBmpHeight = bmp.getHeight();  
		controlBmpWidth = controlBmp.getWidth();  
		controlBmpHeight = controlBmp.getHeight();  
		mViewWidth = width;
		mViewHeight = height;

		//		/* 3.初始化图片控制点 
		//		 * 0---1---2 
		//		 * |       | 
		//		 * 7   8   3 
		//		 * |       | 
		//		 * 6---5---4  
		//		 */ 
		//		srcPs = new float[]{  
		//				0,0,   
		//				mainBmpWidth/2,0,   
		//				mainBmpWidth,0,   
		//				mainBmpWidth,mainBmpHeight/2,  
		//				mainBmpWidth,mainBmpHeight,   
		//				mainBmpWidth/2,mainBmpHeight,   
		//				0,mainBmpHeight,   
		//				0,mainBmpHeight/2,   
		//				mainBmpWidth/2,mainBmpHeight/2  
		//		};  
		//		//4.初始化目标控制点
		//		dstPs = srcPs.clone();  
		//		//5.初始化控制矩形的位置数据
		//		srcRect = new RectF(0, 0, mainBmpWidth, mainBmpHeight);  
		//		dstRect = new RectF();  
		matrix = new Matrix();  
		//		//6.初始化中心轴
		//		prePivot = new Point(mainBmpWidth/2, mainBmpHeight/2);  
		//		lastPivot = new Point(mainBmpWidth/2, mainBmpHeight/2);  

		lastPoint = new Point(0,0);  

		paint = new Paint();  

		paintRect = new Paint();  
		paintRect.setColor(Color.RED);  
		paintRect.setAlpha(100);  
		paintRect.setAntiAlias(true);  

		paintFrame = new Paint();  
		paintFrame.setColor(Color.GREEN);  
		paintFrame.setAntiAlias(true);  
		deltaX = (mViewWidth-mainBmpWidth) /2; 
		deltaY = (mViewHeight-mainBmpHeight)/2;
		changeView(bmp);
		deltaX = deltaY = 0;
		//		setMatrix(OPER_TRANSLATE);  
	}  

	/** 
	 * 矩阵变换，达到图形平移的目的 
	 * @author wangw
	 */  
	protected void setMatrix(int operationType){
		if(Float.isNaN(deltaX) || Float.isNaN(deltaY) || Float.isNaN(changeDegree) || Float.isNaN(scaleValue))
			return;
//		PublicMethod.log_d("[setMatrix]"+operationType);
		switch (operationType) {  
		case OPER_TRANSLATE:  //移动
//			PublicMethod.log_d("deltaX="+deltaX+"  | deltaY="+deltaY);
			matrix.postTranslate(deltaX , deltaY);  
			break;  
		case OPER_SCALE:  //缩放
			matrix.postScale(scaleValue, scaleValue, symmetricPoint.x, symmetricPoint.y);
			break;
		case OPER_ROTATE:	//旋转
			matrix.postRotate(changeDegree, dstPs[CTR_MID_MID * 2], dstPs[CTR_MID_MID * 2 + 1]);
			break;  
		case OPER_Horizontal_SCALE://水平缩放
			matrix.postScale(scaleValue, 1, symmetricPoint.x, symmetricPoint.y);
			break;
		case OPER_VERTICAL_SCALE://垂直缩放
			matrix.postScale(1, scaleValue, symmetricPoint.x, symmetricPoint.y);
			break;
		}  
		matrix.mapPoints(dstPs, srcPs);  
		matrix.mapRect(dstRect, srcRect);  
	}  

	/**
	 * 判断当前触摸点是否在矩形上
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isOnPic(int x , int y){  
		if(dstRect.contains(x, y)){  
			return true;  
		}else   
			return false;  
	}  

	/***
	 * 获取当前触摸的操作类型
	 * @param event
	 * @return
	 */
	protected int getOperationType(MotionEvent event){  
		curTouchX = event.getX();  
		curTouchY = event.getY();
		int evX = (int)event.getX();  
		int evY = (int)event.getY();  
		int curOper = lastOper;  
		switch(event.getAction()) {  
		case MotionEvent.ACTION_DOWN:  
			//1.获取当前控制点
			current_ctr = isOnCP(evX, evY);  
			//			PublicMethod.log_d("img", "current_ctr is "+current_ctr);
			//2.判断当前的控制点是否在图片上，如果
			if(current_ctr != CTR_NONE || isOnPic(evX, evY)){  
				curOper = OPER_SELECTED;  
			}  
			saveTouchPoint(); 
			break;  
		case MotionEvent.ACTION_MOVE:  
			if(current_ctr == CTR_RIGHT_BOTTOM  ){
				//3.判断当前的控制点是否在0-3个控制点中的任意一个上，那就是缩放或旋转操作类型
				curOper = OPER_SCALE_ROTATE;  
			}else if(current_ctr == CTR_RIGHT_MID || current_ctr == CTR_LEFT_MID){
				//横向缩放
				curOper = OPER_Horizontal_SCALE;
			}else if(current_ctr == CTR_MID_TOP || current_ctr == CTR_MID_BOTTOM){
				//纵向缩放
				curOper = OPER_VERTICAL_SCALE;
			}else if(lastOper == OPER_SELECTED && isOnPic(evX, evY)){  
				//5.如果都不在这4个点上但在图片上，就是移动操作
				curOper = OPER_TRANSLATE;  
			}  
			break;  
		case MotionEvent.ACTION_UP:  
			curOper = OPER_SELECTED;  
			break;  
		default:  
			break;  
		}  
		//		PublicMethod.log_d("img", "curOper is "+curOper);
		return curOper;  
	}  

	/** 
	 * 判断点所在的控制点 
	 * @param evx
	 * @param evy
	 * @return 
	 */   
	protected int isOnCP(int evx, int evy) {  
		Rect rect = new Rect(evx-controlBmpWidth,evy-controlBmpHeight,evx+controlBmpWidth,evy+controlBmpHeight);  
		int res = 0 ;  
		for (int i = 0; i < dstPs.length; i+=2) {  
			if(rect.contains((int)dstPs[i], (int)dstPs[i+1])){  
				return res ;  
			}  
			++res ;   
		}  
		return CTR_NONE;  
	}  



	@Override  
	public boolean dispatchTouchEvent(MotionEvent event) {  
		return onTouchHandler(event);
	}
	
	/**
	 * 调用系统的派发事件
	 * @param event
	 */
	protected boolean superDispatchTouchEvent(MotionEvent event){
		return super.dispatchTouchEvent(event);
	}
	
	/**
	 * 处理Touch事件
	 * @param event
	 * @return
	 */
	protected boolean onTouchHandler(MotionEvent event){
		int evX = (int)event.getX();  
		int evY = (int)event.getY();  
		int operType = OPER_DEFAULT;  
		//1.获取操作类型
		operType = getOperationType(event);  

		switch (operType) {  
		case OPER_TRANSLATE:  //移动
			translate(evX, evY);  
			break;  
		case OPER_SCALE_ROTATE:  //缩放
			scale(event,OPER_SCALE);  
			rotate();  
			break;  
		case OPER_Horizontal_SCALE:
			scale(event,operType);
			break;
		case OPER_VERTICAL_SCALE:
			scale(event,operType);
			break;
		}  
		lastPoint.x = evX;  
		lastPoint.y = evY;    

		lastOper = operType;  
		//		invalidate();//重绘  
		return isOnPic(evX, evY) || operType != -1;  
	}
	

	/** 
	 * 移动 
	 * @param evx 
	 * @param evy 
	 * @author wangw 
	 */  
	protected void translate(int evx , int evy){  

		prePivot.x += evx - lastPoint.x;  
		prePivot.y += evy -lastPoint.y;  

		deltaX = prePivot.x - lastPivot.x;  
		deltaY = prePivot.y - lastPivot.y;
		//		PublicMethod.log_d("移动x="+deltaX+" | 移动y="+deltaY+" | 图像X="+dstRect.left+" | 图像Y="+dstRect.top);
		//		boolean numInvalid = false;
		//		if(dstRect.left + deltaX < 0 || 
		//				dstRect.right + deltaX > mViewWidth || 
		//				dstRect.top + deltaY < 0 ||
		//				dstRect.bottom + deltaY > mViewHeight){
		//			numInvalid = true;
		//		}
		lastPivot.x = prePivot.x;  
		lastPivot.y = prePivot.y;  
		//		if(!numInvalid){
		setMatrix(OPER_TRANSLATE); //设置矩阵  
		//		}
	}  

	/** 
	 * 缩放 
	 * 0---1---2 
	 * |       | 
	 * 1   8   3
	 * |       | 
	 * 6---5---4
	 * @param event
	 * @param operationType 操作类型
	 */  
	protected void scale(MotionEvent event,int operationType) {  
		//1.乘2是为了得到当前x坐标点
		int pointIndex = current_ctr*2 ;  
		//获取x坐标点
		float px = dstPs[pointIndex];  
		//再+1就得到y的坐标点
		float py = dstPs[pointIndex+1];  

		float evx = event.getX();  
		float evy = event.getY();  

		float oppositeX = 0 ;  
		float oppositeY = 0 ;  
		//
		if(current_ctr<4 && current_ctr >= 0){  
			oppositeX = dstPs[pointIndex+8];  
			oppositeY = dstPs[pointIndex+9];  
		}else if(current_ctr >= 4){  
			oppositeX = dstPs[pointIndex-8];  
			oppositeY = dstPs[pointIndex-7];  
		}  
		float temp1 = getDistanceOfTwoPoints(px,py,oppositeX,oppositeY);  
		float temp2 = getDistanceOfTwoPoints(evx,evy,oppositeX,oppositeY);  
		float bmpW = dstRect.width();
		float bmpH = dstRect.height();
		this.scaleValue = temp2 / temp1 ; 
		//		PublicMethod.log_d("scale = "+scaleValue + "   | bmw="+bmpW + "  |  bmh="+bmpH);
		//判断当前的值是否有效
		boolean scaleInvalid = false;
		if(scaleValue > 1){
			if(operationType == OPER_SCALE && (bmpH > MAX_BMP || bmpW > MAX_BMP)){
				scaleInvalid = true;
			}else if(operationType == OPER_Horizontal_SCALE && bmpW > MAX_BMP){
				scaleInvalid = true;
			}else if(operationType == OPER_VERTICAL_SCALE && bmpH > MAX_BMP){
				scaleInvalid = true;
			}
		}else {
			if(operationType == OPER_SCALE && (bmpH < MIN_BMP || bmpW < MIN_BMP)){
				scaleInvalid = true;
			}else if(operationType == OPER_Horizontal_SCALE && bmpW < MIN_BMP){
				scaleInvalid = true;
			}else if(operationType == OPER_VERTICAL_SCALE && bmpH < MIN_BMP){
				scaleInvalid = true;
			}
		}
		if(scaleInvalid)
			return;
		symmetricPoint.x = (int) oppositeX;  
		symmetricPoint.y = (int)oppositeY;  

		if(!Float.isNaN(scaleValue)){
			//			PublicMethod.log_d("scaleValue is "+scaleValue);
			setMatrix(operationType);  
		}
	}

	protected void rotate() {  
		changeDegree = (float) getActionDegrees(dstPs[CTR_MID_MID * 2],  dstPs[CTR_MID_MID * 2 + 1], mDownx, mDowny,  
				curTouchX, curTouchY);  
		setMatrix(OPER_ROTATE);
		saveTouchPoint();  
	}  

	protected void saveTouchPoint() {  
		mDownx = curTouchX;  
		mDowny = curTouchY;  
	}  

	/** 
	 * 获取两点到第三点的夹角。 
	 *  
	 * @param x 
	 * @param y 
	 * @param x1 
	 * @param y1 
	 * @param x2 
	 * @param y2 
	 * @return 
	 */  
	protected double getActionDegrees(float x, float y, float x1, float y1,  
			float x2, float y2) {  

		double a = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));  
		double b = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));  
		double c = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));  
		// 余弦定理  
		double cosA = (b * b + c * c - a * a) / (2 * b * c);  
		// 返回余弦值为指定数字的角度，Math函数为我们提供的方法  
		double arcA = Math.acos(cosA);  
		double degree = arcA * 180 / Math.PI;  

		// 接下来我们要讨论正负值的关系了，也就是求出是顺时针还是逆时针。  
		// 第1、2象限  
		if (y1 < y && y2 < y) {  
			if (x1 < x && x2 > x) {// 由2象限向1象限滑动  
				return degree;  
			}  
			// 由1象限向2象限滑动  
			else if (x1 >= x && x2 <= x) {  
				return -degree;  
			}  
		}  
		// 第3、4象限  
		if (y1 > y && y2 > y) {  
			// 由3象限向4象限滑动  
			if (x1 < x && x2 > x) {  
				return -degree;  
			}  
			// 由4象限向3象限滑动  
			else if (x1 > x && x2 < x) {  
				return degree;  
			}  

		}  
		// 第2、3象限  
		if (x1 < x && x2 < x) {  
			// 由2象限向3象限滑动  
			if (y1 < y && y2 > y) {  
				return -degree;  
			}  
			// 由3象限向2象限滑动  
			else if (y1 > y && y2 < y) {  
				return degree;  
			}  
		}  
		// 第1、4象限  
		if (x1 > x && x2 > x) {  
			// 由4向1滑动  
			if (y1 > y && y2 < y) {  
				return -degree;  
			}  
			// 由1向4滑动  
			else if (y1 < y && y2 > y) {  
				return degree;  
			}  
		}  

		// 在特定的象限内  
		float tanB = (y1 - y) / (x1 - x);  
		float tanC = (y2 - y) / (x2 - x);  
		if ((x1 > x && y1 > y && x2 > x && y2 > y && tanB > tanC)// 第一象限  
				|| (x1 > x && y1 < y && x2 > x && y2 < y && tanB > tanC)// 第四象限  
				|| (x1 < x && y1 < y && x2 < x && y2 < y && tanB > tanC)// 第三象限  
				|| (x1 < x && y1 > y && x2 < x && y2 > y && tanB > tanC))// 第二象限  
			return -degree;  
		return degree;  
	}  



	/** 
	 * 计算两个点之间的距离 
	 * @param p1 
	 * @param p2 
	 * @return 
	 */  
	protected float getDistanceOfTwoPoints(Point p1, Point p2){  
		return (float)(Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));  
	}  

	/**
	 * 计算两个点之间的距离 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	protected float getDistanceOfTwoPoints(float x1,float y1,float x2,float y2){  
		return (float)(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)))/2;
	}  



	public Matrix getMatrix(){
		return matrix;
	}

	/**
	 * 返回当前图片left所占背景图片宽的比例
	 * @return
	 */
	public float getLeftScale(){
		return dstRect.left / mViewWidth;
	}

	/**
	 * 返回当前图片的top比上背景图片高度的比例
	 * @return
	 */
	public float getTopScale(){
		return dstRect.top / mViewHeight;
	}
	
	public float getWidthScale(){
		return dstRect.width() / mViewWidth;
	}
	
	public float getHeightScale(){
		return dstRect.height() /mViewHeight;
	}
	
	public float getCenterX(){
		return dstPs[16] / mViewWidth;
	}
	
	public float getCenterY(){
		return (dstPs[17]) / mViewHeight;
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
	
	//TODO　暂时未实现
	public double getRotationZ(){
		float[] vs = new float[9];
		matrix.getValues(vs);
		return 0-Math.atan2((double)vs[3], (double)vs[4]);
	}

	/**
	 * 是否资源
	 */
	public void release(){
		mainBmp.recycle();
		controlBmp.recycle();
		matrix = null;
	}




	protected void onDrawView(){  
		Canvas canvas = getHolder().lockCanvas();
		if(canvas == null)
			return;
		//		drawBackground(canvas);//绘制背景,以便测试矩形的映射
		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		canvas.drawBitmap(mainBmp, matrix, paint);//绘制主图片
		drawFrame(canvas);//绘制边框,以便测试点的映射  
		drawControlPoints(canvas);//绘制控制点图片
		getHolder().unlockCanvasAndPost(canvas);
	}  

	protected void drawBackground(Canvas canvas){  
		canvas.drawRect(dstRect, paintRect);  
	}  

	protected void drawFrame(Canvas canvas){  
		canvas.drawLine(dstPs[0], dstPs[1], dstPs[4], dstPs[5], paintFrame);  
		canvas.drawLine(dstPs[4], dstPs[5], dstPs[8], dstPs[9], paintFrame);  
		canvas.drawLine(dstPs[8], dstPs[9], dstPs[12], dstPs[13], paintFrame);  
		canvas.drawLine(dstPs[0], dstPs[1], dstPs[12], dstPs[13], paintFrame);  
	}  

	protected void drawControlPoints(Canvas canvas){  
		//		for (int i = 0; i < dstPs.length-2; i+=2) {  
		//			canvas.drawBitmap(controlBmp, dstPs[i]-controlBmpWidth/2, dstPs[i+1]-controlBmpHeight/2, paint);  
		//		}

		canvas.drawBitmap(controlBmp, dstPs[8]-controlBmpWidth/2, dstPs[9]-controlBmpHeight/2, paint);
//		canvas.drawBitmap(controlBmp, dstPs[2]-controlBmpWidth/2, dstPs[3]-controlBmpHeight/2, paint);
//		canvas.drawBitmap(controlBmp, dstPs[6]-controlBmpWidth/2, dstPs[7]-controlBmpHeight/2, paint);
//		canvas.drawBitmap(controlBmp, dstPs[10]-controlBmpWidth/2, dstPs[11]-controlBmpHeight/2, paint);
//		canvas.drawBitmap(controlBmp, dstPs[14]-controlBmpWidth/2, dstPs[15]-controlBmpHeight/2, paint);
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		onDrawView();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		onDrawView();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		onDrawView();
	}






}
