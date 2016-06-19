package com.petsay.utile;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.petsay.R;

/**
 * @author wangw
 * 用户信息验证工具类
 * 比如：验证用户名是否合法及密码长度等功能
 */ 
public class VerifyUserUtile {

    /**
     * 非空验证
     * @param ev
     * @return
     */
    public static boolean verifyNotNull(EditText ev){
        if(ev == null)
            return false;
        Context context = ev.getContext();
        String txt = ev.getText().toString().trim();
        if(TextUtils.isEmpty(txt)){
            showToast(context,"请填写正确信息");
            startShakeAnimation(context,ev);
            return false;
        }
        return true;
    }

    /**
     * 验证手机号
     * @param context
     * @param txt
     * @return
     */
	public static boolean verifyMobileNumber(Context context,EditText txt){
		if(txt == null)
			return false;
		String phoneNum = txt.getText().toString().trim();
		boolean flag = PublicMethod.verifyMobileNumber(phoneNum);
		if(!flag){
			showToast(context,R.string.reg_tip_name_error);
			startShakeAnimation(context,txt);
		}
		return flag;
	}
	
	private static void startShakeAnimation(Context context,EditText txt) {
		PublicMethod.startShakeAnimation(context, txt);
	}

    /**
     * 验证验证码
     * @param context
     * @param txt
     * @return
     */
	public static  boolean verifyCaptcha(Context context,EditText txt){
		if(txt == null)
			return false;
		String code = txt.getText().toString().trim();
		boolean flag = TextUtils.isEmpty(code);
		if(flag){
			showToast(context,R.string.reg_tip_code_null);
		}else {
			flag = code.length() != 6;
			if(flag)
				showToast(context,R.string.reg_tip_code_length);
		}
		if(flag)
			startShakeAnimation(context,txt);
		return !flag;
	}

    /**
     * 验证密码
     * @param context
     * @param txt
     * @return
     */
	public static  boolean verifyPwd(Context context,EditText txt){
		if(txt == null)
			return false;
		String pwd = txt.getText().toString().trim();
		boolean flag = TextUtils.isEmpty(pwd);
		if(flag){
			showToast(context,R.string.reg_tip_pwd_null);
		}else {
			flag = pwd.length() < 6;
			if(flag)
				showToast(context,R.string.reg_tip_pwd_length);
		}
		if(flag)
			startShakeAnimation(context,txt);
		return !flag;
	}
	
	private static void showToast(Context context,int resid){
		PublicMethod.showToast(context, resid);
	}

    private static void showToast(Context context,String str){
        PublicMethod.showToast(context, str);
    }
	
}
