package com.petsay.vo.petalk;

import com.petsay.application.PetSayApplication;
import com.petsay.utile.GPUImageFilterTools;
import com.petsay.utile.GPUImageFilterTools.FilterType;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * @author wangw
 * 图片滤镜数据模型
 */
public class ImageFilter {

	public GPUImageFilter filter;
	public int resid;
	
	public static final List<ImageFilter> IMAGEFILTERS = new ArrayList<ImageFilter>();
	public static List<ImageFilter> getImageFilters(){
		if(IMAGEFILTERS.size() > 0)
			return IMAGEFILTERS;
		IMAGEFILTERS.add(initFilter(new GPUImageFilter()));
		IMAGEFILTERS.add(initFilter(FilterType.HUE));
		IMAGEFILTERS.add(initFilter(FilterType.GAMMA));
		IMAGEFILTERS.add(initFilter(FilterType.SEPIA));
		IMAGEFILTERS.add(initFilter(FilterType.GRAYSCALE));
		IMAGEFILTERS.add(initFilter(FilterType.SATURATION));
		IMAGEFILTERS.add(initFilter(FilterType.HIGHLIGHT_SHADOW));
		IMAGEFILTERS.add(initFilter(FilterType.RGB));
		IMAGEFILTERS.add(initFilter(FilterType.WHITE_BALANCE));
		IMAGEFILTERS.add(initFilter(FilterType.VIGNETTE));
		IMAGEFILTERS.add(initFilter(FilterType.BULGE_DISTORTION));
		
//		IMAGEFILTERS.add(initFilter(FilterType.HAZE));
//		IMAGEFILTERS.add(initFilter(FilterType.COLOR_BALANCE));
//		IMAGEFILTERS.add(initFilter(FilterType.TONE_CURVE));
//		IMAGEFILTERS.add(initFilter(FilterType.MONOCHROME));
//		IMAGEFILTERS.add(initFilter(FilterType.SWIRL));
//		IMAGEFILTERS.add(initFilter(FilterType.FALSE_COLOR));
//		IMAGEFILTERS.add(initFilter(FilterType.WEAK_PIXEL_INCLUSION));
//		IMAGEFILTERS.add(initFilter(FilterType.SPHERE_REFRACTION));
		return IMAGEFILTERS;
	}
	
	public static ImageFilter initFilter(FilterType type){
		ImageFilter ft = new ImageFilter();
		ft.filter = GPUImageFilterTools.createFilterForType(PetSayApplication.getInstance(), type);
		return ft;
	}
	
	public static ImageFilter initFilter(GPUImageFilter filter){
		ImageFilter ft = new ImageFilter();
		ft.filter = filter;
		return ft;
	}
	
	
	
	
}

