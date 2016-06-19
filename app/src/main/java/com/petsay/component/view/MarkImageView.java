package com.petsay.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/11
 * @Description 带有角标的ImageView
 */
public class MarkImageView extends FrameLayout {

    public static final int POSITION_TOP_LEFT = 0;
    public static final int POSITION_TOP_RIGHT = 1;
    public static final int POSITION_BOTTOM_RIGHT = 2;
    public static final int POSITION_BOTTOM_LEFT = 3;

    private ImageView mIvcontent;
    private ImageView mIvMark;
    private int mMarkPosition;

    public MarkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs,-1);
    }

    public MarkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        initAttrs(attrs,defStyle);
    }

    private void initAttrs(AttributeSet attrs,int defStyle){
        TypedArray array;
        if(defStyle > -1){
            array = getContext().obtainStyledAttributes(attrs, R.styleable.markimageView,defStyle,0);
        }else {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.markimageView);
        }
        int cResId = array.getResourceId(R.styleable.markimageView_content_src,-1);
        if(cResId > 0){
            setContentImage(cResId);
        }

        int mResId = array.getResourceId(R.styleable.markimageView_mark_src,-1);
        if(mResId > 0 ){
            setMarkImage(mResId);
        }

        int position = array.getInt(R.styleable.markimageView_mark_position,0);
        setMarkPosition(position);

        int visible = array.getInt(R.styleable.markimageView_mark_visibility,1);
        setMarkVisible(visible == 1);
    }

    public MarkImageView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mIvcontent = new ImageView(getContext());
        mIvcontent.setAdjustViewBounds(true);
        mIvcontent.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        int padding = PublicMethod.getDiptopx(getContext(),5);
//        setContentPadding(padding);
        LayoutParams contentParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        addView(mIvcontent,contentParams);
        mIvMark = new ImageView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mIvMark.setAdjustViewBounds(true);
        mIvMark.setVisibility(GONE);
        mIvMark.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(mIvMark, params);
        setMarkPosition(mMarkPosition);
    }

    public void setMarkPosition(int markPosition) {
        LayoutParams params = (LayoutParams) mIvMark.getLayoutParams();
        if(params == null)
            params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        switch (markPosition){
            case POSITION_BOTTOM_RIGHT:
                params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                break;
            case POSITION_BOTTOM_LEFT:
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                break;
            case POSITION_TOP_RIGHT:
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                break;
            case POSITION_TOP_LEFT:
                default:
                    params.gravity = Gravity.TOP | Gravity.LEFT;
                    break;
        }
        mIvMark.setLayoutParams(params);
        mMarkPosition = markPosition;
        invalidate();
    }

    public void setContentImage(int resId){
        mIvcontent.setImageResource(resId);
    }

    public void setContentImage(String url){
        ImageLoaderHelp.displayContentImage(url, mIvcontent);
    }

    public void setMarkImage(int resId){
        mIvMark.setImageResource(resId);
    }

    public void setMarkVisible(boolean isVisible){
        if(isVisible){
            mIvMark.setVisibility(VISIBLE);
        }else {
            mIvMark.setVisibility(GONE);
        }
    }

    public void setContentLayoutParams(LayoutParams params){
        this.mIvcontent.setLayoutParams(params);
        invalidate();
    }

    public LayoutParams getContentLayoutParams(){
        return (LayoutParams) mIvcontent.getLayoutParams();
    }

    public void setContentPadding(int padding){
        mIvcontent.setPadding(padding, padding, padding, padding);
        invalidate();
    }

}
