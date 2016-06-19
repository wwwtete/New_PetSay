package com.petsay.activity.user;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.petsay.R;

public class AddPetPopupWindow extends PopupWindow implements OnClickListener{
	private View popupView;
//	private TextView tvDel;
//	private View parentView;
	private Context context;
	public  LinearLayout layout_unActivePets;
//	private Activity mActivity;
//	private boolean isDetailAc=false;
	public AddPetPopupWindow(Context context){
		this.context=context;
//	    mActivity=viewService.getActivity();
	    init();
	}
	private void init(){
		popupView=LayoutInflater.from(context).inflate(R.layout.addpet_popup, null);
		layout_unActivePets=(LinearLayout) popupView.findViewById(R.id.layout_other);
		this.setContentView(popupView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		popupView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
                View view=popupView.findViewById(R.id.layout_select_pet);
				int heightTop = view.getTop();
				int heightBottom =view.getBottom();
				int widthLeft = view.getLeft();
				int widthRight = view.getRight();
				int y = (int) event.getY();
				int x=(int) event.getX();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < heightTop || y > heightBottom||x<widthLeft|| x > widthRight) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
	
//	public void onShow(){
//		showAtLocation(parentView, Gravity.CENTER, 0, 0);
//	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
