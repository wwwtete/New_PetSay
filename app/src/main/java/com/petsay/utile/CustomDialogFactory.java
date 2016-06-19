package com.petsay.utile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.petsay.R;
@Deprecated
public class CustomDialogFactory {
	private Activity mActivity;
	private static CustomDialogFactory _instance;
	public static CustomDialogFactory getSingleton() {
		if (null == _instance) {
			_instance = new CustomDialogFactory();
		}
		return _instance;
	}
	
	public void initActivity(Activity activity){
		mActivity=activity;
	}
	
	
	public void MyDialog(String title,String content,String leftBtnText,String rightBtnText){
		Builder builder;
		if (Build.VERSION.SDK_INT > 10)
			builder = new AlertDialog.Builder(mActivity,R.style.ver_update_dialog);
		else
			builder = new AlertDialog.Builder(mActivity);
		final Dialog dialog = builder.create();
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()-100); // 设置宽度
		Window window = dialog.getWindow();
		window.setContentView(R.layout.custom_dialog);
		// dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.getWindow().setAttributes(lp);
		// dialog.
		TextView tvTitle=(TextView) window.findViewById(R.id.tv_dialog_title);
		TextView tvContent = (TextView) window.findViewById(R.id.tv_dialog_content);
		TextView tvOk = (TextView) window.findViewById(R.id.tv_dialog_ok);
		TextView tvCancle = (TextView) window.findViewById(R.id.tv_dialog_cancle);
		View viewSpace=window.findViewById(R.id.view_space);
		tvContent.setText(content);
		if (null==title) {
			tvTitle.setVisibility(View.GONE);
		}else {
			tvTitle.setText(title);
		}
		tvOk.setText(leftBtnText);
		if (null==rightBtnText) {
			tvCancle.setVisibility(View.GONE);
			viewSpace.setVisibility(View.GONE);
		}else {
			tvCancle.setText(rightBtnText);
		}
		tvOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				

			}
		});

		tvCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		
//		return dialog;
	}

}
