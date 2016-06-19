package com.petsay.activity.user.signin;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.petsay.R;

public class SigninPopupWindow extends PopupWindow implements OnClickListener{
	private View popupView;
//	private TextView tvDel;
	private View parentView;
	private TextView tvSign1,tvSign2;
	private String str1,str2;
	private Context context;
//	private Activity mActivity;
//	private boolean isDetailAc=false;
	public SigninPopupWindow(View view,Context context,String str1, String str2){
		this.context=context;
	    parentView=view;
	    this.str1=str1;
	    this.str2=str2;
//	    mActivity=viewService.getActivity();
	    init();
	}
	private void init(){
		popupView=LayoutInflater.from(context).inflate(R.layout.signin_popup, null);
		tvSign1=(TextView) popupView.findViewById(R.id.tv_sign1);
		tvSign2=(TextView) popupView.findViewById(R.id.tv_sign2);
		this.setContentView(popupView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	    tvSign1.setText(str1);
	    tvSign2.setText(str2);
		popupView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int heightTop = popupView.findViewById(R.id.share_parent).getTop();
				int heightBottom = popupView.findViewById(R.id.share_parent).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < heightTop || y > heightBottom) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
	
	public void show(){
		showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
