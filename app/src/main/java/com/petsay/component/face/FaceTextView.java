package com.petsay.component.face;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 显示表情的textview
 * @author G
 *
 */
public class FaceTextView extends TextView{

	private Context mContext;
	public FaceTextView(Context context) {
		super(context);
		mContext=context;
	}
	
	public FaceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		super.setText(text, type);
		
	}

	public void setText(String string){
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(mContext,string);
		setText(spannableString);
	}
	
	

}
