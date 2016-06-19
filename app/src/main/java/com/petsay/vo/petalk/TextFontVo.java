package com.petsay.vo.petalk;

import android.graphics.Typeface;

import com.petsay.R;
import com.petsay.application.PetSayApplication;
import com.petsay.constants.Constants;
import com.petsay.vo.decoration.DecorationDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw
 * 字体数据模型
 */
public class TextFontVo {

	public String id;
	public int thumbnailId;
	public String downloadUrl;
	public boolean dowloading = false;
	public boolean downloaded;
	public Typeface typeface;
	
	private static final List<TextFontVo> DATAS = new ArrayList<TextFontVo>();
	
	public static List<TextFontVo> getData(){
		if(DATAS.size() > 0){
			return DATAS;
		}
		initTextFontVo("nofont", R.drawable.no_textfont, "").downloaded = true;
		initTextFontVo("hkwwt001", R.drawable.textfont_01, Constants.DOWNLOD_TEXTFONT+"hkwwt.TTF");
		initTextFontVo("hywwz002", R.drawable.textfont_02, Constants.DOWNLOD_TEXTFONT+"hywwz.ttf");
		initTextFontVo("hymbjt003", R.drawable.textfont_03, Constants.DOWNLOD_TEXTFONT+"hymbjt.ttf");
//		initTextFontVo("yy004", R.drawable.textfont_04, Constants.DOWNLOD_TEXTFONT+"yy.ttf");
		initTextFontVo("sjt005", R.drawable.textfont_05, Constants.DOWNLOD_TEXTFONT+"sjt.ttf");
		initTextFontVo("hyqyjt006", R.drawable.textfont_06, Constants.DOWNLOD_TEXTFONT+"hyqyjt.ttf");
		initTextFontVo("hwxw007", R.drawable.textfont_07, Constants.DOWNLOD_TEXTFONT+"hwxw.TTF");
		initTextFontVo("hwcy008", R.drawable.textfont_08, Constants.DOWNLOD_TEXTFONT+"hwcy.TTF");
		return DATAS;
	}
	
	
	
	private static TextFontVo initTextFontVo(String id,int resId,String url){
		TextFontVo vo = new TextFontVo();
		vo.downloaded = DecorationDataManager.getInstance(PetSayApplication.getInstance()).checkFileDownload(id);
		vo.id = id;
		vo.thumbnailId = resId;
		vo.downloadUrl = url;
		DATAS.add(vo);
		return vo;
	}
	
	
}
