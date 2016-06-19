package com.petsay.utile;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.petsay.R;

/**
 * 显示各种样式的Toast
 *
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/24
 * @Description
 */
public class ToastUtiles {

    public static void showDefault(Context context, String msg){
        if(TextUtils.isEmpty(msg))
            return;
        makeText(context,msg).show();
    }

    public static void showDefault(Context context,int resId){
        makeText(context,resId).show();
    }

    public static void showCenter(Context context,String msg){
        showByGravity(context, msg, Gravity.CENTER);
    }

    public static void showCenter(Context context,int resId){
        showByGravity(context,resId,Gravity.CENTER);
    }

    public static void showByGravity(Context context,String msg,int gravity){
        if(TextUtils.isEmpty(msg))
            return;
        Toast toast = makeText(context,msg);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

    public static void showByGravity(Context context,int resId,int gravity){
        if(resId <= 0)
            return;
        Toast toast = makeText(context,resId);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

    public static void showCustomView(Context context,View view){
      showCustomView(context,view,Gravity.CENTER);
    }

    public static void showCustomView(Context context,View view,int gravity){
        if(view == null)
            return;
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private static Toast makeText(Context context,String msg){
        Toast toast = new Toast(context);//Toast.makeText(context,msg,Toast.LENGTH_LONG);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.view_custom_toast,null);
        ((TextView)view.findViewById(R.id.message)).setText(msg);
        toast.setView(view);
        return toast;
    }

    private static Toast makeText(Context context,int resId){
        return makeText(context,context.getResources().getText(resId).toString());
    }

}
