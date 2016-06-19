package com.petsay.utile;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/20
 * @Description
 */
public class StringUtiles {

    /**
     * 将数字格式成 “num+"格式
     * @param num
     * @param max 超过最大数后进行格式
     */
    public static String numberFormat(int num,int max){
        if(num > max)
            return max+"+";
        return num+"";
    }
    
    /**
     * 
     * @param content
     * @param start
     * @param end
     * @param color
     * @param textSize
     * @return
     */
    public static SpannableString formatSpannableString(String content,int start,int end,int color,int textSize) {
    	SpannableString	spanText=new SpannableString(content);
		spanText.setSpan(new ForegroundColorSpan(color), start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new AbsoluteSizeSpan(textSize, true), start, end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spanText;
    }

}
