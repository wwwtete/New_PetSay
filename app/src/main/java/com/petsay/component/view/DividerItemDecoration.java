package com.petsay.component.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView间隔
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/31
 * @Description
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int mOrientation;
    private Drawable mDivider;
    private int mSpace;

    public DividerItemDecoration(int mOrientation,int mSpace) {
        this.mSpace = mSpace;
        this.mOrientation = mOrientation;
    }

    public DividerItemDecoration(int mOrientation, int mSpace, Drawable mDivider) {
        this.mOrientation = mOrientation;
        this.mSpace = mSpace;
        this.mDivider = mDivider;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public int getmOrientation() {
        return mOrientation;
    }

    public Drawable getmDivider() {
        return mDivider;
    }

    public void setmDivider(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    public int getmSpace() {
        return mSpace;
    }

    public void setmSpace(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(mDivider == null)
            return;
        if(mOrientation == VERTICAL_LIST)
            onDrawVertical(c,parent);
        else
            ondrawHorizontal(c,parent);

    }

    private void onDrawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        int top =0;
        int bottom = 0;
        for (int i =0;i<count;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin;
            bottom = top+mSpace;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    private void ondrawHorizontal(Canvas c, RecyclerView parent) {
        int left = 0;
        int right = 0;
        int top =parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        for (int i =0;i<count;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            left = child.getRight()+ params.rightMargin;
            right = left + mSpace;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        if(mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,mSpace);
        }else {
            outRect.set(0,0,mSpace,0);
        }
    }
}
