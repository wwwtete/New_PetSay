package com.petsay.component.customview.module;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * 可编辑的组件
 */
public class EditSurfaceViewModule extends BasicSurfaceViewModule {
	public static int MAX_BMP = 600;
	public static int MIN_BMP = 200;
	/* 图片控制点 
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */  
	/**不在控制点*/
	public static final int CTR_NONE = -1;
	/**左上角*/
	public static final int CTR_LEFT_TOP = 0;
	/**顶部中间*/
	public static final int CTR_MID_TOP = 1;
	/**右上角*/
	public static final int CTR_RIGHT_TOP = 2;
	/**右侧中间*/
	public static final int CTR_RIGHT_MID = 3;
	/**右下角*/
	public static final int CTR_RIGHT_BOTTOM = 4;
	/**底部中间*/
	public static final int CTR_MID_BOTTOM = 5;
	/**左下角*/
	public static final int CTR_LEFT_BOTTOM = 6;
	/**左侧中间*/
	public static final int CTR_LEFT_MID = 7;
	/**中心点*/
	public static final int CTR_MID_MID = 8;
	/**顶部边框*/
	public static final int CTR_BORDER_TOP = 10;
	/**右侧边框*/
	public static final int CTR_BORDER_RIGHT = 11;
	/**底部边框*/
	public static final int CTR_BORDER_BOTTOM = 12;
	/**左侧边框*/
	public static final int CTR_BORDER_LEFT = 13;
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
	/**删除操作*/
	public static final int OPER_DELETE = 7;
	/**操作类型：缩放*/
	public static final int OPER_SCALE = 2;
	/**操作类型：旋转  */
	public static final int OPER_ROTATE = 3;        //旋转
	/**操作类型：选择 */
	public static final int OPER_SELECTED = 4;      //选择
	/**最后一次操作类型*/
	public int lastOper = OPER_SELECTED;  

	protected Paint paintFrame; 

	/**X位移值 */
	protected float deltaX = 0;
	/**Y位移值*/
	protected float deltaY = 0; //位移值  
	/**缩放值  */
	protected float scaleValue = 1; //缩放值  
	/**最后一次的操作位置*/
	protected Point lastPoint ;
	/**上次中心轴的位置*/
	protected Point prePivot;
	/**最后一次的中心轴位置*/
	protected Point lastPivot;  
	protected int controlBmpWidth , controlBmpHeight ; 
	protected int mDeleteBmpWidth = 0,mDeleteBmpHeight = 0;
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
	/**控制点*/
	protected Bitmap controlBmp ; 
	protected int current_ctr;
	/**当前操作点对称点  */
	protected Point symmetricPoint  = new Point();    //当前操作点对称点  
	private Bitmap mDeleteBmp;
	/**是否参与合成图片*/
	protected boolean mAllowCompound = true;
	/**最初初始化时的Matrix*/
	protected Matrix mInitMatrix;

	public EditSurfaceViewModule(Bitmap bmp,Bitmap controlBmp) {
		super(bmp);
		setControlBitmap(controlBmp);
		setAllowEdit(true);
	}

	public EditSurfaceViewModule(Bitmap bmp,Bitmap controlBmp,Bitmap deleteBmp) {
		super(bmp);
		setControlBitmap(controlBmp);
		setDeleteBmp(deleteBmp);
		setAllowEdit(true);
	}

	@Override
	protected void initModule() {
		super.initModule();
		//		controlBmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.yuan);

		//6.初始化中心轴
		prePivot = new Point(mainBmpWidth/2, mainBmpHeight/2);  
		lastPivot = new Point(mainBmpWidth/2, mainBmpHeight/2);
		lastPoint = new Point(0,0);

		paintFrame = new Paint();  
		paintFrame.setStrokeWidth(5);
		paintFrame.setColor(Color.WHITE);  
		paintFrame.setAntiAlias(true);
	}

	/**
	 * 设置控制点的样式
	 * @param bmp
	 */
	public void setControlBitmap(Bitmap bmp){
		this.controlBmp = bmp;
		controlBmpWidth = controlBmp.getWidth();  
		controlBmpHeight = controlBmp.getHeight();  
	}
	
	/***
	 * 设置初始化时的Matrix
	 * @param matrix
	 */
	public void setInitMatrix(Matrix matrix){
		mInitMatrix = new Matrix(matrix);
	}

	@Override
	public boolean isOnPic(int x, int y) {
		return super.isOnPic(x, y) || isOnCP(x, y) != CTR_NONE || isOnBorder(x,y) != CTR_NONE;
	}

	/** 
	 * 判断点所在的控制点 
	 * @param evx
	 * @param evy
	 * @return 
	 */   
	public int isOnCP(int evx, int evy) {  
		updateRect();
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

	/**
	 * 判断触摸在那个边框上
	 * @param evx
	 * @param evy
	 * @return
	 */
	public int isOnBorder(int evx,int evy){
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;
		int space = 20;
		int res = 10 ;

		for (int i = 0; i <= 12; i+=4) {
			if(i == 0){	//TOP
				left = (int)dstPs[i]+mDeleteBmpWidth/2;
				top = (int)dstPs[i+1] - space;	
				right = (int)dstPs[i+4];
				bottom = (int)dstPs[i+5] +space;
			}else if(i == 8){ //Bottom
				left = (int)dstPs[i+4];
				right = (int)dstPs[i] - controlBmpWidth/2;
				top = (int)dstPs[i+1] - space;
				bottom = (int)dstPs[i+5] + space;
			}else if(i == 4){	//Right
				left = (int)dstPs[i]-space;
				top = (int)dstPs[i+1];
				bottom = (int)dstPs[i+5] - controlBmpHeight/2;
				right = (int)dstPs[i+4] + space;
			}else{	//left
				left = (int)dstPs[0] - space;
				top = (int)dstPs[1] - mDeleteBmpHeight/2;
				right = (int)dstPs[i]+space;
				bottom = (int)dstPs[i+1];
			}
			Rect rect = new Rect(left, top, right, bottom);
			if(rect.contains(evx,evy))
				return res;
			res++;
		}

		return CTR_NONE;
	}

	/**
	 * 按下时处理函数
	 */
	public int onDownHandler(MotionEvent event){
		curTouchX = event.getX();  
		curTouchY = event.getY();
		saveTouchPoint();
		current_ctr = isOnCP((int)event.getX(), (int)event.getY());
		//TODO 由于未处理好，不等比例拉伸，暂时放弃这个功能
		//		if(current_ctr != CTR_LEFT_TOP && current_ctr != CTR_RIGHT_BOTTOM){
		//			current_ctr = isOnBorder((int)event.getX(), (int)event.getY());
		//			PetsayLog.d("view","border == "+current_ctr);
		//		}
		return current_ctr;
	}

	/**
	 * 移动时处理函数
	 * @param event
	 */
	public int onMoveHandler(MotionEvent event){
		int curOper = lastOper;
		curTouchX = event.getX();  
		curTouchY = event.getY();
		int evX = (int) event.getX();
		int evY = (int) event.getY();
		PublicMethod.log_d("当前所按下的点===" + current_ctr + "  |  是否包含==" + dstRect.contains(evX, evY));
		if(current_ctr == CTR_RIGHT_BOTTOM  ){
			//右下角
			curOper = OPER_SCALE_ROTATE;  
		}else if(current_ctr == CTR_BORDER_TOP || current_ctr == CTR_BORDER_BOTTOM){
			//纵向缩放
			curOper = OPER_VERTICAL_SCALE;
		}else if(current_ctr == CTR_BORDER_LEFT || current_ctr == CTR_BORDER_RIGHT){
			//横向缩放
			curOper = OPER_Horizontal_SCALE;
		}else if(current_ctr == CTR_LEFT_TOP){
			//左上角
			//TODO 删除操作,暂时无操作
			curOper = OPER_DELETE;
		}else if(lastOper == OPER_SELECTED && super.isOnPic(evX, evY)){  
			//5.如果都不在这4个点上但在图片上，就是移动操作
			curOper = OPER_TRANSLATE;  
		}
		lastOper = curOper;
		operateBitmap(event,curOper);
		return curOper;
	}

	/**
	 * 根据操作类型来操作图像
	 * @param operType
	 */
	protected void operateBitmap(MotionEvent event,int operType){
		int evX = (int) event.getX();
		int evY = (int) event.getY();
		switch (operType) {  
		case EditSurfaceViewModule.OPER_TRANSLATE:  //移动
			translate(evX, evY);  
			break;  
		case EditSurfaceViewModule.OPER_SCALE_ROTATE:  //缩放
			onScale(event,OPER_SCALE);  
			rotate();  
			break;  
		case EditSurfaceViewModule.OPER_Horizontal_SCALE:
			onScale(event,operType);
			break;
		case OPER_VERTICAL_SCALE:
			onScale(event,operType);
			break;
		case OPER_DELETE:
			break;
		} 
	}

	public int onUpHandler(MotionEvent event){
		lastOper = OPER_SELECTED;
		return current_ctr;
	}

	/** 
	 * 移动 
	 * @param evx 
	 * @param evy 
	 * @author wangw 
	 */  
	protected void translate(int evx , int evy){  
		int tempX = prePivot.x;
		int tempY = prePivot.y;
		tempX += evx - lastPoint.x;
		tempY += evy - lastPoint.y;
		int vectorX = tempX - lastPivot.x;  
		int vectorY = tempY - lastPivot.y;
		float centerX = dstPs[16];
		float centerY = dstPs[17];
		
		//判断中心点是否小于0，如果小于0就判断手指当前移动方向，如果继续向小于中心点方向移动则return
		if((centerX < 0 || centerY < 0) && (centerX + vectorX < centerX || centerY+vectorY < centerY))
			return;
		//判断中心点大于最大值，如果大于最大值就判断手指当前移动的方向，如果继续向最大值移动则return
		if((centerX > mMaxWidth || centerY > mMaxHeight) && (centerX + vectorX > centerX || centerY+vectorY > centerY))
			return;

		prePivot.x += evx - lastPoint.x;  
		prePivot.y += evy -lastPoint.y;  
		deltaX = prePivot.x - lastPivot.x;  
		deltaY = prePivot.y - lastPivot.y;
		lastPivot.x = prePivot.x;  
		lastPivot.y = prePivot.y;  
		setMatrix(OPER_TRANSLATE); //设置矩阵  
	}

	/** 
	 * 缩放 
	 * 0---1---2 
	 * |       | 
	 * 1   8   3
	 * |       | 
	 * 6---5---4
	 */
	protected void onScale(MotionEvent event,int operationType) {

		int ctr = current_ctr;
		switch (current_ctr) {
		case CTR_BORDER_LEFT:
			ctr = CTR_LEFT_MID;
			break;
		case CTR_BORDER_BOTTOM:
			ctr = CTR_MID_BOTTOM;
			break;
		case CTR_BORDER_TOP:
			ctr = CTR_MID_TOP;
			break;
		case CTR_BORDER_RIGHT:
			ctr = CTR_RIGHT_MID;
			break;
		}

		//1.乘2是为了得到当前x坐标点
		int pointIndex = ctr%10*2 ;  
		//获取x坐标点
		float px = dstPs[pointIndex];  
		//再+1就得到y的坐标点
		float py = dstPs[pointIndex+1];  

		float evx = event.getX();  
		float evy = event.getY();  

		float oppositeX = 0 ;  
		float oppositeY = 0 ;  
		//
		if(ctr<4 && ctr >= 0){  
			oppositeX = dstPs[pointIndex+8];  
			oppositeY = dstPs[pointIndex+9];  
		}else if(ctr >= 4){  
			oppositeX = dstPs[pointIndex-8];  
			oppositeY = dstPs[pointIndex-7];  
		}  
		float temp1 = getDistanceOfTwoPoints(px,py,oppositeX,oppositeY);  
		float temp2 = getDistanceOfTwoPoints(evx,evy,oppositeX,oppositeY);  
		float bmpW = dstRect.width();
		float bmpH = dstRect.height();
		this.scaleValue = temp2 / temp1 ; 
		bmpW = bmpW * scaleValue;
		bmpH = bmpH * scaleValue;
//		PetsayLog.d("test","scale = "+scaleValue + "   | bmw="+bmpW + "  |  bmh="+bmpH+" | maxW="+MAX_BMP+" | minW="+MIN_BMP);
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
			matrix.postScale(scaleValue, scaleValue, dstPs[CTR_MID_MID * 2], dstPs[CTR_MID_MID * 2 + 1]);//symmetricPoint.x, symmetricPoint.y);
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
		//		matrix.mapPoints(dstPs, srcPs);  
		//		matrix.mapRect(dstRect, srcRect);
		updateRect();
	} 
	
	/**
	 * 放大操作
	 * @param precision 放大的倍数
	 */
	public void enlarged(float precision) {
		if(scaleValue < 1)
			scaleValue = 1;
		scaleValue = scaleValue+precision;
		float tw = dstRect.width()*scaleValue;
		if(tw < MAX_BMP){
		matrix.postScale(scaleValue, scaleValue,dstPs[CTR_MID_MID*2],dstPs[CTR_MID_MID*2+1]);
		updateRect();
		}
	}

	/**
	 * 缩小操作
	 * @param precision 缩小倍数
	 */
	public void reduced(float precision) {
		if(scaleValue > 1)
			scaleValue = 1;
		scaleValue = scaleValue - precision;
		float tw = dstRect.width()*scaleValue;
		if(tw > MIN_BMP){
		matrix.postScale(scaleValue, scaleValue,dstPs[CTR_MID_MID*2],dstPs[CTR_MID_MID*2+1]);
		updateRect();
		}
	}
	
	/**
	 * 向左旋转
	 * @param angle 旋转角度
	 */
	public void rotateleft(float angle) {
		if(changeDegree > -1)
			changeDegree = -1;
		changeDegree = changeDegree + angle;
		setMatrix(OPER_ROTATE);
	}

	/**
	 * 向右旋转
	 * @param angle 旋转倍数
	 */
	public void rotateright(float angle) {
		if(changeDegree < 1)
			changeDegree = 1;
		changeDegree = changeDegree - angle;
		setMatrix(OPER_ROTATE);
	}

	/**
	 * 重置操作
	 */
	public void reset() {
		if(mInitMatrix != null){
			matrix.set(mInitMatrix);
			updateRect();
		}
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

	@Override
	public void release() {
		super.release();
	}

	public void setDeleteBmp(Bitmap bmp){
		this.mDeleteBmp = bmp;
		if(bmp != null){
			mDeleteBmpHeight = bmp.getHeight();
			mDeleteBmpWidth = bmp.getWidth();
		}
	}

	public Bitmap getDeleteBmp(){
		return this.mDeleteBmp;
	}

	protected void saveTouchPoint() {  
		mDownx = curTouchX;  
		mDowny = curTouchY;  
	}

	public Paint getPaintFrame() {
		return paintFrame;
	}

	public Point getLoastPoint(){
		return lastPoint;
	}

	public int getControlBmpWidth(){
		return controlBmpWidth;
	}

	public Bitmap getControlBitmap(){
		return controlBmp;
	}

	public int getControlBmpHeight(){
		return controlBmpHeight;
	}

	public int getDeleteBmpHeight(){
		return mDeleteBmpHeight;
	}

	public int getDeleteBmpWidth(){
		return mDeleteBmpWidth;
	}

	public void setPaintFrame(Paint paintFrame) {
		this.paintFrame = paintFrame;
	}

	public boolean getAllowCompound() {
		return mAllowCompound;
	}

	public void setAllowCompound(boolean allowCompound) {
		this.mAllowCompound = allowCompound;
	}



}
