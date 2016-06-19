package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;

/**
 * @author wangw
 * 编辑界面-装饰标题
 */
public class DecorateTitleView extends LinearLayout implements OnClickListener {

	private LayoutInflater mInflater;
	private View mPreSelectView;
	private DecorateTitleListener mListener;

	public DecorateTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
	}
	
	public void setOnDecorateListener(DecorateTitleListener listener){
		this.mListener = listener;
	}
	
	/**
	 * 设置标题
	 * @param titles
	 */
	public void setTitle(String[] titles){
		this.removeAllViews();
		for (int i = 0; i < titles.length; i++) {
			View view = addChild(titles[i], i);
			if(i == 0)
				changeSelectState(view);
		}
	}

	/**
	 * 获取Child
	 * @param title
	 * @param index
	 * @return
	 */
	private View addChild(String title,int index){
		View child = mInflater.inflate(R.layout.horizontal_list_item, null);
		TextView mTitle=(TextView)child.findViewById(R.id.text_list_item);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//		params.rightMargin = PublicMethod.getDiptopx(getContext(), 20);
//		int space = PublicMethod.getDiptopx(getContext(), 10);
//		child.setPadding(space, 0, space, 0);
		child.setLayoutParams(params);
		mTitle.setText(title);
		child.setId(index);
		child.setOnClickListener(this);
		addView(child);
		return child;
	}

	@Override
	public void onClick(View v) {
		boolean flag = changeSelectState(v);
		if(flag && mListener != null){
			mListener.onClickTitleListener(v,v.getId());
		}
	}

	/**
	 * 切换选中状态
	 * @param view
	 * @param selected
	 * @return 是否需要切换
	 */
	public boolean changeSelectState(View view){
		if(mPreSelectView != null && view == mPreSelectView)
			return false;
		
		if(mPreSelectView != null){
			getTextView(mPreSelectView, R.id.text_list_item).setTextColor(Color.parseColor("#898989"));
		}
		if(view != null){
			getTextView(view, R.id.text_list_item).setTextColor(Color.WHITE);
		}
		mPreSelectView = view;
		return true;
	}
	
	private TextView getTextView(View parent,int id){
		return (TextView) parent.findViewById(id);
	}
	
	public interface DecorateTitleListener{
		public void onClickTitleListener(View view,int position);
	}

}
