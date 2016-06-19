package com.petsay.activity.personalcustom.postcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.personalcustom.PostCardVo;
import com.petsay.vo.personalcustom.SpriteVo;
import com.petsay.vo.petalk.PetalkVo;

public class PostCardView extends View {

	private Canvas mCanvas;
	private Bitmap bitmap;
	private Bitmap mPhotoBitmap;
	private Bitmap mHeaderBitmap;
	//明信片模版里实际宽高
	private int width , height;
	private int viewWidth, viewHeight;
	private PostCardVo mPostCardVo;
    private PetalkVo mPetalkVo;
	private boolean isDrawPhoto=false;
	private boolean isDrawHeader=false;
	private Typeface mTypefaceContent; 

	private Rect mBackgroundDst;
	private Paint mPaint;
	
	private String[] mDateArr;
	//方便輸出日志查看，无其他作用
	private String mLogTag;
	public PostCardView(Context context,PostCardVo postCardVo,String logTag) {
		super(context);
		mPostCardVo=postCardVo;
		width=postCardVo.getWidth();
		height=postCardVo.getHeight();
		mLogTag=logTag;
		mPaint=new Paint();
		mTypefaceContent=Typeface.createFromAsset(getContext().getAssets(),"fonts/huakangwawati.ttc");
		bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.custom_posdcard_demo);
		mBackgroudSrc=new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = getWidth();
		viewHeight = getHeight();
        //setBackgroundResource(R.drawable.custom_posdcard_demo);
		//drawHeader(spriteVo);
	}

	private void setPhotoBitmap(Bitmap bitmap) {
		mPhotoBitmap = bitmap;
		if (null==mPhotoSrc) {
			mPhotoSrc=new Rect(0, 0, mPhotoBitmap.getWidth(), mPhotoBitmap.getHeight());
		}
		isDrawPhoto=true;
		invalidate();
	}
	private void setHeaderBitamp(Bitmap bitmap) {
		mHeaderBitmap = bitmap;
		if (null==mHeaderBitmap) {
			mHeaderBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_header);
		}
		if (null==mHeaderSrc) {
			mHeaderSrc=new Rect(0, 0, mHeaderBitmap.getWidth(), mHeaderBitmap.getHeight());
		}
		isDrawHeader=true;
		invalidate();
	}
	
	
	
	public void setPetalkVo(PetalkVo petalkVo){
		mPetalkVo=petalkVo;
		String str = PublicMethod.formatTimeToString(mPetalkVo.getCreateTime(),"yyyy MM/dd kk:mm");
		mDateArr=str.split(" ");
		ImageLoaderHelp.getImageLoader().loadImage(petalkVo.getPhotoUrl(),ImageLoaderHelp.contentPetImgOptions,  new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
				
				setPhotoBitmap(bitmap); 
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {}
		});
		ImageLoaderHelp.getImageLoader().loadImage(petalkVo.getPetHeadPortrait(),new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {}

			@Override
			public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {
			}

			@Override
			public void onLoadingComplete(String arg0,View arg1, Bitmap bitmap) {
				setHeaderBitamp(bitmap);
			}

			@Override
			public void onLoadingCancelled(String arg0,View arg1) {}
		});

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
			mCanvas = canvas;
			viewWidth=getWidth();
			viewHeight=getHeight();
			if (isDrawHeader) {
				drawHeader(mPostCardVo.getSprites()[0]);
			}
			if (isDrawPhoto) {
				drawPhoto(mPostCardVo.getSprites()[1]);
			}
			drawBackGround();
			
			if (null!=mPetalkVo) {
				drawContent(mPostCardVo.getSprites()[2]);
				drawDate(mPostCardVo.getSprites()[3]);
				drawNickName(mPostCardVo.getSprites()[4]);
				drawTime(mPostCardVo.getSprites()[5]);
			}
		
	}
	
	private void drawBackGround(){
		if (null!=bitmap) {
			if (null==mBackgroundDst) {
				mBackgroundDst=new Rect(0, 0, viewWidth, viewHeight);
			}else {
				mBackgroundDst.set(0, 0, viewWidth, viewHeight);
			}
			mCanvas.drawBitmap(bitmap,mBackgroudSrc,mBackgroundDst, null);
		}
		
	}

	private void drawPhoto(SpriteVo spriteVo) {
		mCanvas.drawBitmap(mPhotoBitmap,mPhotoSrc,
				new Rect((spriteVo.getStartX() * viewWidth) / width, (spriteVo
						.getStartY() * viewHeight) / height, ((spriteVo.getStartX()+spriteVo.getSpriteWidth()) * viewWidth) / width,
						((spriteVo
								.getStartY() +spriteVo.getSpriteHeight())* viewHeight) / height), null);
	}

	private void drawHeader(SpriteVo spriteVo) {
		mCanvas.drawBitmap(
				mHeaderBitmap,
				mHeaderSrc,
				new Rect((spriteVo.getStartX() * viewWidth) / width, (spriteVo
						.getStartY() * viewHeight) / height, ((spriteVo.getStartX()+spriteVo.getSpriteWidth()) * viewWidth) / width,
						((spriteVo
								.getStartY() +spriteVo.getSpriteHeight())* viewHeight) / height), null);
	}

	private void drawDate(SpriteVo spriteVo) {
		String year = mDateArr[0];
		String month =mDateArr[1];
		int startX = (spriteVo.getStartX() * viewWidth) / width;
		int startY = ((spriteVo.getStartY() -10)* viewHeight) / height;
		int endX = ((spriteVo.getStartX() + spriteVo.getSpriteWidth()) * viewWidth)/ width;
		int endY = ((spriteVo.getStartY() + spriteVo.getSpriteHeight()-10) * viewHeight)/ height;
		int areaWidth=endX-startX;
		int areaHeight=endY-startY;
		mPaint.reset();
		mPaint.setColor(Color.rgb(0xF5, 0xB5, 0xD0));
		mPaint.setAntiAlias(true);
		mPaint.setFakeBoldText(true);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTypeface(null);	
		int textSize1=areaHeight;
		mPaint.setTextSize(textSize1);
		mCanvas.drawText(month, startX, startY+textSize1, mPaint);
		int offsetSize=(15*viewHeight)/height;
		int textSize2=areaHeight+offsetSize;
		mPaint.setTextSize(textSize2);
		mCanvas.drawText(year, startX, startY+textSize1+textSize2, mPaint);
	}
	
   private void drawTime(SpriteVo spriteVo) {
		String time=mDateArr[2];
		int startX = (spriteVo.getStartX() * viewWidth) / width;
		int startY = ((spriteVo.getStartY() -10)* viewHeight) / height;
		int endX = ((spriteVo.getStartX() + spriteVo.getSpriteWidth()) * viewWidth)/ width;
		int endY = ((spriteVo.getStartY() + spriteVo.getSpriteHeight()) * viewHeight)/ height;
		int areaWidth=endX-startX;
		int areaHeight=endY-startY;
		mPaint.reset();
		mPaint.setColor(Color.rgb(0xF5, 0xB5, 0xD0));
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTypeface(null);	
		mPaint.setFakeBoldText(false);
//		int textSize1=areaHeight;
		
		mPaint.setTextSize(areaHeight);
		
		mCanvas.drawText(time, startX+areaWidth/2, endY, mPaint);
	}

	private void drawContent(SpriteVo spriteVo) {
		String str;
		if (mPetalkVo.getDescription().length()>6) {
			str="　　"+mPetalkVo.getDescription();
		}else {
			str=mPetalkVo.getDescription();
		}
		
		int startX=(spriteVo.getStartX() * viewWidth) / width;
		int startY=(spriteVo.getStartY() * viewHeight) / height;
		int endX=((spriteVo.getStartX()+spriteVo.getSpriteWidth()) * viewWidth) / width;
		int endY=((spriteVo.getStartY() +spriteVo.getSpriteHeight())* viewHeight) / height;
		int length=str.length();
		int textSize=0;
		mPaint.reset();
		mPaint.setColor(Color.rgb(0x8c, 0x8c, 0x8c));
		mPaint.setAntiAlias(true);
		mPaint.setFakeBoldText(false);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTypeface(mTypefaceContent);					
		if (length>0) {
			textSize=(int) (Math.sqrt(((endY-startY)*(endX-startX)))/Math.sqrt(length));
			int rowCount=(endX-startX)/textSize+1;
			textSize=(endX-startX)/rowCount;
			mPaint.setTextSize(textSize);
			int column=1;
			for (int i = 0; i <length; i+=rowCount) {
				if (i>=length-rowCount) {
					mCanvas.drawText(str.substring(i, str.length()), startX, startY+column*textSize, mPaint);
				    break;
				}else {
					mCanvas.drawText(str.substring(i, i+rowCount), startX, startY+column*textSize, mPaint);
				}
				column++;
			}
		}
	}
	
	private void drawNickName(SpriteVo spriteVo){
		String str=mPetalkVo.getPetNickName();
		int startX=(spriteVo.getStartX() * viewWidth) / width;
		int startY=(spriteVo.getStartY() * viewHeight) / height;
		int endX=((spriteVo.getStartX()+spriteVo.getSpriteWidth()) * viewWidth) / width;
		int endY=((spriteVo.getStartY() +spriteVo.getSpriteHeight())* viewHeight) / height;
		int textSize=endY-startY;
		mPaint.reset();
		mPaint.setColor(Color.rgb(0x8c, 0x8c, 0x8c));
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTextSize(textSize);
		mPaint.setTypeface(null);
		mPaint.setFakeBoldText(false);
		mCanvas.drawText(str, startX, startY+textSize, mPaint);
		
	}
	
	private Rect mHeaderSrc,mHeaderDst,mPhotoSrc,mPhotoDst,mBackgroudSrc;
	
	public void recycle(){
		mPhotoBitmap.recycle();
		mHeaderBitmap.recycle();
		bitmap.recycle();
	}
}
