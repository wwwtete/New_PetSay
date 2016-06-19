package com.petsay.component.text;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

public class Clickable extends ClickableSpan implements OnClickListener{
	private final View.OnClickListener mListener;
    private boolean mIsShowUnderline;
	public Clickable(View.OnClickListener l,boolean isShowUnderline){
		mListener = l;
		mIsShowUnderline=isShowUnderline;
	}
	
	@Override
	public void updateDrawState(TextPaint ds) {
		// TODO Auto-generated method stub
		super.updateDrawState(ds);
		if (!mIsShowUnderline) {
			 ds.setColor(ds.linkColor);
		     ds.setUnderlineText(false);
		}
	}
	

	@Override
	public void onClick(View v) {
		mListener.onClick(v);
	}
}
