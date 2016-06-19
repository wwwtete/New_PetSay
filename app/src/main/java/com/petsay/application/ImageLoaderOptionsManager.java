package com.petsay.application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.petsay.R;

/**
 * Created by wangw on 2014/12/16.
 * ImageLoader组件Option管理器
 */
public class ImageLoaderOptionsManager {

    private static DisplayImageOptions mGiftBagOptions;
    private static DisplayImageOptions mDecorationOptions;
    private static DisplayImageOptions mRectOptions;
    private static DisplayImageOptions mDiaryOptions;


    /**
     * 获取礼包的Options
     * @return
     */
    public static DisplayImageOptions getGiftBagOptions(){
        if(mGiftBagOptions == null)
            mGiftBagOptions = getGeneralOptions(R.drawable.griftbag_loading);
        return mGiftBagOptions;
    }

    /**
     * 获取饰品下载中的占位图
     * @return
     */
    public static DisplayImageOptions getmDecorationOptions(){
        if(mDecorationOptions == null)
            mGiftBagOptions = getGeneralOptions(R.drawable.downloading);
        return mDecorationOptions;
    }

    public static DisplayImageOptions getRectOptions(){
        if(mRectOptions == null){
            mRectOptions = getGeneralOptions(R.drawable.rect_occupy);
        }
        return mRectOptions;
    }

    public static DisplayImageOptions getDiaryOptions(){
        if(mRectOptions == null){
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(false)
                    .resetViewBeforeLoading(true) // default
//                    .displayer(new FadeInBitmapDisplayer(500, true, true, false))
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片缩放方式
                    .decodingOptions(opt)  //图像解码
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
//                .delayBeforeLoading(500)
                    .build();
        }
        return mRectOptions;
    }


    /**
     * 根据Resid获取一个DisplayImageOpations对象
     * @param resID
     * @return
     */
    public static DisplayImageOptions getGeneralOptions(int resID){
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .showImageOnLoading(resID)
//                .showImageForEmptyUri(resID)
//                .showImageOnFail(resID)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(false)
//                .resetViewBeforeLoading(true) // default true
////                .displayer(new FadeInBitmapDisplayer(500,true,true,false))
////				.displayer(new RoundedBitmapDisplayer(1000))
////				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
////                .delayBeforeLoading(500)
//                .build();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoadingBackground(R.color.img_content_bg_color)
                .showImageForEmptyUri(resID)
                .showImageOnFail(resID)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true) // default
                .displayer(new FadeInBitmapDisplayer(500, true, true, false))
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片缩放方式
                .decodingOptions(opt)  //图像解码
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
//                .delayBeforeLoading(500)
                .build();

        return options;
    }


}
