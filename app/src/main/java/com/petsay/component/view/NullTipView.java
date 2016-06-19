package com.petsay.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.petsay.R;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/8/19
 * @Description
 */
public class NullTipView extends LinearLayout implements View.OnClickListener {

    private ImageView mIvBg;
    private ImageView mIvBtn;
    private OnClickListener mClickListener;

    public NullTipView(Context context) {
        super(context);
        initView();
    }

    public NullTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs,-1);
    }

    public NullTipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs, defStyle);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray arr;
        if(defStyle > -1)
            arr = getContext().obtainStyledAttributes(attrs,R.styleable.nulltipview,defStyle,0);
        else
            arr = getContext().obtainStyledAttributes(attrs,R.styleable.nulltipview);
        int btn = arr.getResourceId(R.styleable.nulltipview_btn_src,0);
        int bg = arr.getResourceId(R.styleable.nulltipview_bg_src,0);
        initViewResid(bg, btn);

    }

    private void initView() {
        inflate(getContext(),R.layout.view_null_tip,this);
        setOrientation(VERTICAL);
        mIvBg = (ImageView) findViewById(R.id.iv_bg);
        mIvBtn = (ImageView) findViewById(R.id.iv_btn);
        mIvBtn.setOnClickListener(this);
    }

    public void initViewResid(int bgResid,int btnResid){
        this.mIvBtn.setImageResource(btnResid);
        this.mIvBg.setImageResource(bgResid);
    }

    public void setClickButtonListener(OnClickListener listener){
        this.mClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mClickListener != null)
            mClickListener.onClick(v);
    }
}
