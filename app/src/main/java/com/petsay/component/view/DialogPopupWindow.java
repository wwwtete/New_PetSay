package com.petsay.component.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.petsay.R;

public class DialogPopupWindow extends BasePopupWindow{
	private View popupView;
//	private TextView tvDel;
	private View parentView,viewSpace;
	private TextView tvCancle,tvOk;
	private TextView tvTitle,tvContent;
	private Activity mActivity;
	//用来判断btn执行事件
	public int mTag=1;
	public DialogPopupWindow(Context context,IAddShowLocationViewService viewService){
	    parentView=viewService.getParentView();
	    mActivity=viewService.getActivity();
	    init();
	    
	}
	private void init(){
		popupView=LayoutInflater.from(mActivity).inflate(R.layout.custom_dialog, null);
		tvOk=(TextView) popupView.findViewById(R.id.tv_dialog_ok);
		
		tvContent=(TextView) popupView.findViewById(R.id.tv_dialog_content);
		viewSpace= popupView.findViewById(R.id.view_space);
		tvTitle=(TextView) popupView.findViewById(R.id.tv_dialog_title);
		tvCancle=(TextView) popupView.findViewById(R.id.tv_dialog_cancle);
//		tvDel=(TextView) popupView.findViewById(R.id.tv_del);
		this.setContentView(popupView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
//		popupView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int heightTop = popupView.findViewById(R.id.popup_parent).getTop();
//				int heightBottom = popupView.findViewById(R.id.popup_parent).getBottom();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < heightTop || y > heightBottom) {
//						dismiss();
//					}
//				}
//				return true;
//			}
//		});
//		tvDel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				 dismiss();
////                 _diagnosis.remove(_position);
//                 _adapter.notifyDataSetChanged();
//			}
//		});
	}
	
	public void setPopupText(int tag,String title,String content,String leftBtnText,String rightBtnText){
		mTag=tag;
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
	}
	public void setOnClickListener(OnClickListener clickListener){
		tvCancle.setOnClickListener(clickListener);
		tvOk.setOnClickListener(clickListener);
	}
	
	public void show(){
		showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}
	
	
	
	
	
}
