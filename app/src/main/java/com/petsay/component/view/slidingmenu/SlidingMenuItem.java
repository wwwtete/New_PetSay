package com.petsay.component.view.slidingmenu;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.StringUtiles;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/7
 * @Description 侧滑菜单的Item
 */
public class SlidingMenuItem extends RelativeLayout {

    public static final int POSITION_LEFT = 0;
    public static final int POSITION_TOP = 1;
    public static final int POSITION_RIGHT = 2;
    public static final int POSITION_BOTTOM = 3;

    private String mTitleStr;
    private ImageView mIvFlag;
    private TextView mTvUnreadmsgCount;
    private TextView mTvTitle;
    private SlidingMenuItemModel mModel;

    public SlidingMenuItem(Context context) {
        super(context);
        initView();
    }

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public SlidingMenuItem(Context context,SlidingMenuItemModel model) {
        super(context);
        this.mModel = model;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.slidingmenuitem,this);
//        int pading = PublicMethod.getDiptopx(getContext(),5);
//        setPadding(0,pading,0,pading);
        mIvFlag = (ImageView) findViewById(R.id.iv_flag);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvUnreadmsgCount = (TextView) findViewById(R.id.tv_unreadmsgcount);
    }

    public void setItemModel(SlidingMenuItemModel model){
        this.mModel = model;
        if(mModel != null) {
            mTvTitle.setText(model.contentResId);
            setIcon(mModel.iconResId, mModel.iconPosition);
            setTiteleColor(mModel.contentColor);
            setUnreadMsgCount(model.unReadMsgCount,model.isShowUnReadMsgCount);
        }else {
            setTitle("");
            setIcon(0,0);
        }
    }

    public void setUnreadMsgCount(int unReadMsgCount, boolean isShowUnReadMsgCount) {
        if(isShowUnReadMsgCount){
            mTvUnreadmsgCount.setText(StringUtiles.numberFormat(unReadMsgCount,99));
        }
        mTvUnreadmsgCount.setVisibility(isShowUnReadMsgCount ? VISIBLE : GONE);
    }

    public SlidingMenuItemModel getItemModel(){
        return  mModel;
    }


    /**
     * 设置Icon
     * @param resId 资源ID
     * @param postion  ICon位置
     */
    public void setIcon(int resId,int postion){

        switch (postion){
            case POSITION_LEFT:
                mTvTitle.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
                break;
            case POSITION_TOP:
                mTvTitle.setCompoundDrawablesWithIntrinsicBounds(0,resId,0,0);
                break;
            case POSITION_RIGHT:
                mTvTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,resId,0);
                break;
            case POSITION_BOTTOM:
                mTvTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,resId);
                break;
        }


//        mIvIcon.setImageResource(ResId);
//        mIvIcon.setVisibility(VISIBLE);
//        LayoutParams params;
//        if(postion == POSITION_LEFT || postion == POSITION_TOP){
//            params = (LayoutParams) mTvTitle.getLayoutParams();
//            if(postion == POSITION_LEFT)
//                params.addRule(RelativeLayout.RIGHT_OF,mIvIcon.getId());
//            else
//                params.addRule(RelativeLayout.BELOW,mIvIcon.getId());
//            mTvTitle.setLayoutParams(params);
//        }else {
//            params = (LayoutParams) mIvIcon.getLayoutParams();
//            int verb = -1;
//            switch (postion){
//                case LOCATION_BOTTOM:
//                    verb = RelativeLayout.BELOW;
//                    break;
//                case POSITION_RIGHT:
//                    verb = RelativeLayout.RIGHT_OF;
//                    break;
//            }
//            if(verb > -1) {
//                params.addRule(verb, mTvTitle.getId());
//                mIvIcon.setLayoutParams(params);
//            }
//        }
//        invalidate();
    }

    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    public void setTiteleColor(int color){
        mTvTitle.setTextColor(color);
    }



}
