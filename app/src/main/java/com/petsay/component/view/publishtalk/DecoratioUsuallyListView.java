package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.petsay.R;
import com.petsay.activity.petalk.publishtalk.BaseEditActivity;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.decoration.DecorationBean;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/30
 * @Description
 */
public class DecoratioUsuallyListView extends LinearLayout implements View.OnClickListener {

    private List<DecorationBean> mBeanList;
    private ImageView mIvMore;
    private View mFlNothing;
    private LinearLayout mLlContailner;
    private DecorationUsuallyItemView mCurrView;
    private int mSpace;
    private String mType;
    private ClickUsuallyDecorationCallback mCallback;

    public DecoratioUsuallyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.decoration_usually_list_view,this);
        this.setOrientation(HORIZONTAL);
        mSpace = PublicMethod.getDiptopx(getContext(),5);
        mIvMore = (ImageView) findViewById(R.id.iv_more);
        mIvMore.setOnClickListener(this);
        mFlNothing = findViewById(R.id.fl_nothing);
        mFlNothing.setOnClickListener(this);
        mLlContailner = (LinearLayout) findViewById(R.id.ll_container);
    }

    public void setCallback(ClickUsuallyDecorationCallback callback){
        this.mCallback = callback;
    }

    public void setSelectView(DecorationBean bean){
        if(bean == null)
            return;;
        for (int i= 0;i<mLlContailner.getChildCount();i++){
            DecorationItemView view1 = (DecorationItemView) mLlContailner.getChildAt(i);
            if(view1.getData().getId().equals(bean.getId())){
                changeSelectView((View) view1);
                break;
            }
        }
    }

    public void setUsuallyDecorationData(List<DecorationBean> beans,String type) {
        setUsuallyDecorationData(beans,"",type);
    }

    public void setUsuallyDecorationData(List<DecorationBean> beans,String selectId,String type) {
        mBeanList = beans;
        mLlContailner.removeAllViews();
        if(BaseEditActivity.TYPE_RAHMEN.equals(type))
            mFlNothing.setVisibility(VISIBLE);
        else
            mFlNothing.setVisibility(GONE);
        mType = type;
        setMoreIcon(type);
        if(beans != null && !beans.isEmpty()){
            for (int i = 0;i<beans.size();i++){
                View view = getView(beans.get(i));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickUsuallyDecoration(v);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.rightMargin = mSpace;
                mLlContailner.addView(view,params);
                if(!TextUtils.isEmpty(selectId) && beans.get(i).getId().equals(selectId)){
                    onClickUsuallyDecoration(view);
                }
            }
        }
    }

    private void setMoreIcon(String type) {
        int resId = - 1;
        if(BaseEditActivity.TYPE_MOUTH.equals(type)){
            resId = R.drawable.mouth_more;
        }else if(BaseEditActivity.TYPE_RAHMEN.equals(type)){
            resId = R.drawable.rahmen_more;
        }else if(BaseEditActivity.TYPE_COSTUME.equals(type)){
            resId = R.drawable.costume_more;
        }else if(BaseEditActivity.TYPE_DIALOG.equals(type)){
            resId = R.drawable.dialog_more;
        }
        if(resId > -1){
            mIvMore.setImageResource(resId);
        }else {
            mIvMore.setImageBitmap(null);
        }
    }

    private void onClickUsuallyDecoration(View view) {
        changeSelectView(view);
        if(mCallback != null){
            mCallback.onClickUsuallyDecoration((DecorationItemView) view);
        }
    }

    private void changeSelectView(View v){
        DecorationUsuallyItemView view = (DecorationUsuallyItemView) v;
        if(v != mCurrView){
            if(mCurrView != null){
                mCurrView.setBackgroundDrawable(null);
            }
            v.setBackgroundResource(R.drawable.decoration_select_shape);
        }
        mFlNothing.setBackgroundDrawable(null);
        this.mCurrView = view;
    }


    private View getView(DecorationBean bean){
//        View view = inflate(getContext(),R.layout.decoration_usually_list_item,null);
//        ImageView sv = (ImageView) view.findViewById(R.id.iv_icon);
//        ImageLoaderHelp.displayImage(bean.getThumbnail(),sv);
//        view.setTag(bean);
        DecorationUsuallyItemView view = new DecorationUsuallyItemView(getContext());
        view.updateView(bean);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_nothing:
                if(mCurrView != null){
                    mCurrView.setBackgroundDrawable(null);
                    mCurrView = null;
                }
                mFlNothing.setBackgroundResource(R.drawable.decoration_select_shape);
                if(mCallback != null)
                    mCallback.onClicClearView(mType);
                break;
            case R.id.iv_more:
                if(mCallback != null) {
                    mCallback.onClickDecorationMore(mType);
                }

                break;
        }

    }

    public interface ClickUsuallyDecorationCallback{
        public void onClickUsuallyDecoration(DecorationItemView view);
        public void onClicClearView(String type);
        public void onClickDecorationMore(String type);
    }

}
