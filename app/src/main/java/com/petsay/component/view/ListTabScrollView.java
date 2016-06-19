package com.petsay.component.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * ���͵�ַ:http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 *
 */
public class ListTabScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	private int lastScrollY;
	
	public ListTabScrollView(Context context) {
		this(context, null);
	}
	
	public ListTabScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListTabScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = ListTabScrollView.this.getScrollY();
			
			//��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
	         handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			break;
		}
		return super.onTouchEvent(ev);
	}


	
	public interface OnScrollListener{
	
		public void onScroll(int scrollY);
	}
	
	

}
