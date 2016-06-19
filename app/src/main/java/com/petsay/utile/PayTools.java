package com.petsay.utile;

import com.petsay.activity.personalcustom.pay.PaySuccessActivity;
import com.petsay.constants.Constants;
import com.petsay.vo.personalcustom.OrderDTO;
import com.pingplusplus.android.PaymentActivity;
import com.unionpay.UPPayAssistEx;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.math.MathContext;

public class PayTools {
	public static int REQUEST_CODE_PAYMENT=10;
	
	public static void turnToPay(Activity activity,String charge){
		Intent intent = new Intent();
	    ComponentName componentName = new ComponentName(Constants.PackageName, Constants.PackageName + ".wxapi.WXPayEntryActivity");
	    intent.setComponent(componentName);
	    intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
	    activity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}
	
	public static boolean managePayResult(Context context,int requestCode, int resultCode, Intent data,OrderDTO orderDTO){
        boolean state =false;
		 if (requestCode == REQUEST_CODE_PAYMENT) {
             if (resultCode == Activity.RESULT_OK) {
                 /* 处理返回值
		             * "success" - payment succeed
		             * "fail"    - payment failed
		             * "cancel"  - user canceld
		             * "invalid" - payment plugin not installed
		             *
		             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
		             */
                 String result = data.getExtras().getString("pay_result");
                 String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                 if("success".equals(result)){
                     state = true;
                     if(context instanceof Activity) {
                         Intent intent = new Intent(context, PaySuccessActivity.class);
                         intent.putExtra("orderdto",orderDTO);
                         context.startActivity(intent);
                         ((Activity)context).finish();
                     }
                 }else if("fail".equals(result)) {
                     errorMsg = "支付失败："+errorMsg;
                 }else if("cancel".equals(result)){
                    errorMsg = "支付取消";
                 }else if("invalid".equals(result)) {
                    errorMsg = "安装银联安全支付控件";
                     UPPayAssistEx.installUPPayPlugin(context);
                 }

                 //TODO 临时测试
//                 if(context instanceof Activity) {
//                     Intent intent = new Intent(context, PaySuccessActivity.class);
//                     intent.putExtra("orderdto",orderDTO);
//                     context.startActivity(intent);
//                     ((Activity)context).finish();
//                 }
                 if(!TextUtils.isEmpty(errorMsg))
                    ToastUtiles.showDefault(context,errorMsg);
//		            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//		            if (mSelectPayTypeIndex==2&&result.equals("invalid")) {
//		            	UPPayAssistEx.installUPPayPlugin(this);
//					}
		        } else if (resultCode == Activity.RESULT_CANCELED) {
		            Toast.makeText(context, "User canceled", Toast.LENGTH_SHORT).show();
//		        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//		            Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
		        }else {
//		        	Toast.makeText(context, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
                 ToastUtiles.showDefault(context, "支付超时请重试");
//					PaymentActivity.
				}
		    }
        return state;
	}

}
