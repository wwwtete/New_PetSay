package com.petsay.component.view.chat;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.petsay.R;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.chat.ChatMsgEntity;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/19
 * @Description
 */
public class ChatPopupWindow implements View.OnClickListener {

    private PopupWindow mWindow;
    private ChatMsgEntity mEntity;
    private Context mContext;
    private View mCopyView;
    private View mDeleteView;
    private ClickChatPopuWindow mListener;
    private View mView;
    private int mViewH;
    public ChatPopupWindow(Context context){
        mContext = context;
        initView();
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.chat_popupwindow_view,null);
        mCopyView = mView.findViewById(R.id.ll_copy);
        mDeleteView = mView.findViewById(R.id.tv_delete);
        mCopyView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
        mViewH = PublicMethod.getDiptopx(mContext,50);
    }

    public void setListener(ClickChatPopuWindow clickChatPopuWindow){
        this.mListener = clickChatPopuWindow;
    }

    public void showWindow(ChatMsgItemView view,ChatMsgEntity entity,boolean showCopy){
        dismiss();
        this.mEntity = entity;
        mWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(showCopy){
            mCopyView.setVisibility(View.VISIBLE);
        }else {
            mCopyView.setVisibility(View.GONE);
        }
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        view.tvText.getLocationOnScreen(location);

        mWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0],location[1]-mViewH);
    }

    public void dismiss(){
        if(mWindow != null){
            mWindow.dismiss();
            mWindow = null;
        }
    }

    public void release(){
        dismiss();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_copy:
                if(mListener != null)
                    mListener.onClickCopyListener(this,mEntity);
                break;
            case R.id.tv_delete:
                if(mListener != null){
                    mListener.onClickDeleteListener(this,mEntity);
                }
                break;
        }
    }


    public interface ClickChatPopuWindow{
        public void onClickCopyListener(ChatPopupWindow window,ChatMsgEntity entity);
        public void onClickDeleteListener(ChatPopupWindow window,ChatMsgEntity entity);
    }

}
