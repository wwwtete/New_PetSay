package com.petsay.activity.personalcustom.clothing;

import com.petsay.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 编辑宠物信息框
 * @author GJ
 *
 */
public class InputView extends LinearLayout{

	private Context mContext;
	private TextView mTvInfoType,mTvUnit;
	private EditText mEdValue;
	private AttributeSet mAttrs;
	private OnTouchListener mOnTouchListener;
	public InputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		mAttrs=attrs;
		initView();
		
	}
	
	private  void initView(){
		inflate(mContext, R.layout.view_input_petinfo, this);
		mTvInfoType=(TextView) findViewById(R.id.tv_item_name);
		mTvUnit=(TextView) findViewById(R.id.tv_unit);
		mEdValue=(EditText) findViewById(R.id.ed_value);
		
		TypedArray a = mContext.obtainStyledAttributes(mAttrs, R.styleable.inputview);//TypedArray是一个数组容器  
	    String infoType=a.getString(R.styleable.inputview_info_type);
	    mTvInfoType.setText(infoType);
	    mTvUnit.setText(a.getString(R.styleable.inputview_info_unit));
	}
	
	public void setOnEdClickListener(OnTouchListener onTouchListener){
		mOnTouchListener=onTouchListener;
		mEdValue.setInputType(InputType.TYPE_NULL);
		mEdValue.setOnTouchListener(mOnTouchListener);
	}
	
	public void setText(String text){
		mEdValue.setText(text);
	}
	
	public String getText(){
		return mEdValue.getText().toString();
	}

}
