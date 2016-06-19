package com.petsay.component.view.popupmenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/29
 * @Description
 */
public class ExPopupMenuView extends PopupWindow {

    private LinearLayout mLlMenus;
    private Button mBtnCancel;
    private View mBg;
    private Context mContext;
    private int mHeight;

    public ExPopupMenuView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ExPopupMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mHeight = PublicMethod.getDiptopx(mContext,40);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_expopupmenu,null);
        mLlMenus = (LinearLayout) view.findViewById(R.id.ll_menus);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBg = view.findViewById(R.id.bg);
        mBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.popupWindowAnim_bottom);
        setContentView(view);
    }

    public void show(View parentView,PopupMenuItem[] datas){
        mLlMenus.removeAllViews();
        if(datas == null || datas.length == 0)
            return;
        View divide = null;
        for (PopupMenuItem item : datas){
            View iv = getView(item);
            mLlMenus.addView(iv);
            divide = getDivide();
            mLlMenus.addView(divide);
        }
        mLlMenus.removeView(divide);
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    private View getView(PopupMenuItem item) {
        TextView view = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,mHeight);
        view.setLayoutParams(params);
        view.setTextColor(Color.parseColor("#407DA8"));
        view.setTextSize(14);
        view.setText(item.title);
        view.setOnClickListener(item.listener);
        view.setGravity(Gravity.CENTER);

        return view;
    }

    private View getDivide(){
        View view = new View(mContext);
        view.setBackgroundColor(Color.parseColor("#D0D0D0"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,4);
        view.setLayoutParams(params);
        return view;
    }


}

