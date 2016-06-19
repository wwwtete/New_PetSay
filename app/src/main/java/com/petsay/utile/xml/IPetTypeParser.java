package com.petsay.utile.xml;

import java.io.InputStream;
import java.util.List;

import android.content.Context;

import com.petsay.vo.user.PetType;

public interface IPetTypeParser {
	 /** 
     * 解析输入流 得到PetType对象集合 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    List<PetType> parse(InputStream is) throws Exception;

	List<PetType> parse(Context context) throws Exception;  
      
//    /** 
//     * 序列化PetType对象集合 得到XML形式的字符串 
//     * @param petTypes 
//     * @return 
//     * @throws Exception 
//     */  
//    public String serialize(List<PetType> petTypes) throws Exception;  
}
