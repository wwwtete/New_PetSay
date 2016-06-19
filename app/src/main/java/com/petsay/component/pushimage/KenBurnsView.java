package com.petsay.component.pushimage;

import java.util.Random;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsView extends RelativeLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private String mUrl;
//    private ImageView[] mImageViews;
    private ImageView mImageView;
    private int mActiveImageIndex = -1;
    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    public int imgWidth=0,imgHeight=0;
    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;
    public TextView mHeaderLogo;
    private ImageLoadCompleteCallback mCallback;
    public boolean downState=false;
    RelativeLayout.LayoutParams layoutParams ;
//    private Runnable mSwapImageRunnable = new Runnable() {
//        @Override
//        public void run() {
//            swapImage();
//            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs*2);
//        }
//    };

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }
    
    public void setText(String text){
    	mHeaderLogo.setText(text);
    }
    
    public void setTextPadding(int Leftdp,int Rightdp){
    	mHeaderLogo.setPadding(PublicMethod.getDiptopx(getContext(), Leftdp), PublicMethod.getDiptopx(getContext(), 10), PublicMethod.getDiptopx(getContext(), Rightdp), PublicMethod.getDiptopx(getContext(), 10));
    }
    
    
//    public void setProgressHeight(int height,int count){
//    	progressLayoutHeight=height*count;
//    	LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(PublicMethod.getDisplayWidth(getContext()), height);
////        Textvi
//    	TextView text=new TextView(getContext());
////    	La
//    }

    public void setImgUrls(String url) {
    	mUrl = url;
        fillImageViews();
    }
    public void setImageLoadCompleteCallback(ImageLoadCompleteCallback callback){
    	mCallback=callback;
    }

//    private void swapImage() {
//        PetsayLog.d(TAG, "swapImage active=" + mActiveImageIndex);
//        if(mActiveImageIndex == -1) {
//            mActiveImageIndex = 0;
//            animate(mImageViews[mActiveImageIndex]);
//            return;
//        }
//
//        int inactiveIndex = mActiveImageIndex;
//        mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;
//        PetsayLog.d(TAG, "new active=" + mActiveImageIndex);
//
//        final ImageView activeImageView = mImageViews[mActiveImageIndex];
//        activeImageView.setAlpha(0.0f);
//        ImageView inactiveImageView = mImageViews[inactiveIndex];
//
//        animate(activeImageView);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(mFadeInOutMs);
//        animatorSet.playTogether(
//                ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
//                ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)
//        );
//        animatorSet.start();
//    }

//    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
//        view.setScaleX(fromScale);
//        view.setScaleY(fromScale);
//        view.setTranslationX(fromTranslationX);
//        view.setTranslationY(fromTranslationY);
//        ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
//        propertyAnimator.start();
//        PetsayLog.d(TAG, "starting Ken Burns animation " + propertyAnimator);
//    }

//    private float pickScale() {
//        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
//    }

//    private float pickTranslation(int value, float ratio) {
//        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
//    }

//    public void animate(View view) {
//    	
//        float fromScale = pickScale();
//        float toScale = pickScale();
//        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
//        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
//        float toTranslationX = pickTranslation(view.getWidth(), toScale);
//        float toTranslationY = pickTranslation(view.getHeight(), toScale);
//        start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mHandler.removeCallbacks(mSwapImageRunnable);
    }

//    private void startKenBurnsAnimation() {
////        mHandler.post(mSwapImageRunnable);
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.view_kenburns, this);
    	mHeaderLogo = (TextView) findViewById(R.id.header_logo);
//        mImageView= new ImageView[1];
        mImageView= (ImageView) view.findViewById(R.id.image0);
//        mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
    }

    private void fillImageViews() {
//        for ( int i = 0; i < mImageViews.length; i++) {
//        	final int position=i;
        	ImageLoaderHelp.displayContentImage(mUrl,  mImageView,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1,  Bitmap arg2) {
					imgWidth=PublicMethod.getDisplayWidth(getContext());
					if (null==arg2) {
						arg2=Bitmap.createBitmap(imgWidth, imgWidth/2,Config.ALPHA_8);
					}
					
//					if (null!=arg2) {
						imgHeight=(arg2.getHeight()*imgWidth)/arg2.getWidth();
						layoutParams=new RelativeLayout.LayoutParams(imgWidth,imgHeight);
						mImageView.setLayoutParams(layoutParams);
						
						
						 mHeaderLogo.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			                 public void onGlobalLayout() {
			                	 int height = mHeaderLogo.getHeight();
			                	 if (null!=mCallback&&!downState) {
			 						mCallback.imageLoadComplete(imgWidth, imgHeight,height);
			 					}
			                 
			                 }
			             });
//					}else {
//						 mHeaderLogo.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			                 public void onGlobalLayout() {
//			                	 int height = mHeaderLogo.getHeight();
//			                	 if (null!=mCallback) {
//			 						mCallback.imageLoadComplete( imgWidth, height,height);
//			 					}
//			                 
//			                 }
//			             });
//					}
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					
				}
			},null);
        	
        	
//            mImageViews[i].setImageResource(mResourceIds[i]);
//        }
        
    }
    
    public interface ImageLoadCompleteCallback{
    	void imageLoadComplete(int imgWidth,int imgHeight,int textviewHeight);
    }
    
    public void setImageDisplay(int height){
    	if (null!=layoutParams) {
    		imgWidth=PublicMethod.getDisplayWidth(getContext());
        	
       	 layoutParams.height=height;
       	 mImageView.setLayoutParams(layoutParams);
       	 invalidate();
		}
    	
    	 
    }
}
