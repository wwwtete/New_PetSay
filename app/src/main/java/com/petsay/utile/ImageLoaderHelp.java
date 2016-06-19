package com.petsay.utile;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.petsay.R;
import com.petsay.constants.Constants;

import java.io.File;

import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * @author wangw
 * ImageLoader助手类
 */
public class ImageLoaderHelp {

	public static DisplayImageOptions contentPetImgOptions;
	public static DisplayImageOptions headerImgOptions;
	

	public static void displayContentImage(String uri,ImageView imgView){
		displayImage(uri, imgView,contentPetImgOptions);
	}
	
	public static void displayContentImage(String uri,ImageView imgView,ImageLoadingListener listener,ImageLoadingProgressListener progressListener){
		displayImage(uri, imgView,contentPetImgOptions,listener,progressListener);
	}
	
	public static void displayHeaderImage(String uri, ImageView imgView){
		displayImage(uri, imgView,headerImgOptions);
	}
	
	public static void displayImage(String uri,ImageView imgView){
		getImageLoader().displayImage(uri, imgView);
	}
	
	public static void displayImage(String uri,ImageView imgView,DisplayImageOptions options){
		getImageLoader().displayImage(uri, imgView, options);
	}
	
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener){
		getImageLoader().displayImage(uri, imageView, options, listener, progressListener);
	}
	
	
	public static ImageLoader getImageLoader(){
		return ImageLoader.getInstance();
	}

	/**
	 * 初始化ImageLoader
	 * @param context
	 */
	public static void initImageLoader(Context context){
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, Constants.IMAGE_CACHE_PATH); 
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
				.Builder(context)
		.threadPoolSize(5)
		.diskCacheSize(50*1024*1024)
		.memoryCacheSize(calculateMemoryCacheSize(context))//2*1024*1024)
		.diskCache(new UnlimitedDiscCache(cacheDir))
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.memoryCache(new UsingFreqLimitedMemoryCache(calculateMemoryCacheSize(context)))
		.build();
		ImageLoader.getInstance().init(configuration);
		initImgOptions();
	}
	
	/**
	 * 初始化imageloader option
	 */
	public static void initImgOptions(){
		contentPetImgOptions = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.showImageOnLoadingBackground(R.color.img_content_bg_color)
				.showImageForEmptyUri(R.drawable.pet1)
				.showImageOnFail(R.drawable.pet1)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(false)
				.resetViewBeforeLoading(true) // default
				.displayer(new FadeInBitmapDisplayer(500,true,true,false))
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
				.delayBeforeLoading(500)
				.build();
		
		headerImgOptions= new DisplayImageOptions.Builder()
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.showImageOnLoading(R.drawable.placeholderhead)
		.showImageForEmptyUri(R.drawable.placeholderhead)
		.showImageOnFail(R.drawable.placeholderhead)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(false)
		.resetViewBeforeLoading(true)
//		.displayer(new FadeInBitmapDisplayer(1000,true,true,false))
		.delayBeforeLoading(500)
		.build();
	}
	
	public static DisplayImageOptions GetOptions(int resImgId,ImageScaleType scaleType){
		DisplayImageOptions	options = new DisplayImageOptions.Builder()
				.imageScaleType(scaleType)
//				.showImageOnLoadingBackground(R.color.img_content_bg_color)
				.showImageOnLoading(resImgId)
				.showImageForEmptyUri(resImgId)
				.showImageOnFail(resImgId)
				.cacheInMemory(true)
				
				.cacheOnDisk(true)
				.considerExifParams(false)
				.resetViewBeforeLoading(true) // default
				.displayer(new FadeInBitmapDisplayer(500,true,true,false))
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
				.delayBeforeLoading(500)
				.build();
		return options;
	}
	
	/**
	 * 计算内存大小
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	static int calculateMemoryCacheSize(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Application.ACTIVITY_SERVICE);//getService(context, ACTIVITY_SERVICE);
		boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
		int memoryClass = am.getMemoryClass();
		if (largeHeap && SDK_INT >= HONEYCOMB) {
			memoryClass = am.getLargeMemoryClass();//getLargeMemoryClass.getLargeMemoryClass(am);
		}
		// Target ~15% of the available heap.
		return 1024 * 1024 * memoryClass / 7;
	}

}
