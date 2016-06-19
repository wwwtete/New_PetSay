package com.petsay.activity.personalcustom.diary.diaryview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.widget.LinearLayout;

import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/5/25
 * @Description 日记View
 */
@Deprecated
public class BasicDiaryView extends LinearLayout {


    private DiaryViewModule mLeftView;
    private DiaryViewModule mRightView;
    private int mSpace;
    private int mWidth;
    private int mHeight;

    public BasicDiaryView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mLeftView = new DiaryViewModule(getContext());
        mRightView = new DiaryViewModule(getContext());
        mSpace = PublicMethod.getDiptopx(getContext(),2);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
        lp.rightMargin = mSpace;
        addView(mLeftView, lp);
        LayoutParams rp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
        rp.leftMargin = mSpace;
        mRightView.setVisibility(GONE);
        addView(mRightView, rp);
    }

    public void setData(List<PetalkVo> vos){
        this.mLeftView.setData(vos);
        this.mRightView.setData(vos);
    }

    public void setTotalPage(int totalPage){
        this.mLeftView.setTotalPage(totalPage);
        this.mRightView.setTotalPage(totalPage);
    }

    public void setPageIndex(int pageIndex){
        if(isLandscape()) {
            mRightView.setVisibility(VISIBLE);
            mRightView.setPageIndex(pageIndex*2);
            mLeftView.setPageIndex(pageIndex*2-1);
        }else{
            mRightView.setVisibility(GONE);
            mLeftView.setPageIndex(pageIndex);
        }
    }

    private boolean isLandscape(){
        return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
