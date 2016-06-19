package com.petsay.activity.personalcustom.diary;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.personalcustom.diary.diaryview.DiaryViewItem;
import com.petsay.activity.personalcustom.diary.diaryview.DiaryViewModule;
import com.petsay.vo.petalk.PetalkVo;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/25
 * @Description
 */
public class FlipBookAdapter extends ExBaseAdapter<PetalkVo> {

    private int mCount = 7;
    private int mBookPageSize;
    private int mTotalElements;

    public FlipBookAdapter(Context context) {
        super(context);
    }

    public void setCount(int count){
        this.mCount = count;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    /**
     * 设置笔记的单页总页数
     * @param size
     */
    public void setBookPageSize(int size){
        this.mBookPageSize = size;
    }

    public void setTotalElements(int totalElements){
        this.mTotalElements = totalElements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.diaryviewitem, null);
            convertView.setWillNotDraw(false);
            ViewHolder holder = ViewHolder.create(convertView);
            convertView.setTag(holder);
        }
        setContent(convertView, position);
//        ((DiaryViewItem)convertView).updateView(position, mDatas);
        return convertView;
//        return super.getView(position, convertView, parent);
    }

    private void setContent(View convertView, int position) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.leftPage.setTotalPage(mBookPageSize);
        holder.rightPage.setTotalPage(mBookPageSize);
        holder.leftPage.setTotalElements(mTotalElements);
        holder.rightPage.setTotalElements(mTotalElements);
        if(position == 0){
            holder.leftPage.updateView(-1,mDatas);
            holder.leftPage.setBackgroundColor(Color.TRANSPARENT);
            holder.leftShadown.setVisibility(View.GONE);
            holder.rightPage.updateView(position,mDatas);
        }else if(position >= mCount){
            holder.flRight.setVisibility(View.INVISIBLE);
            holder.leftPage.updateView(position*2-1,mDatas);
        }else {
            holder.flRight.setVisibility(View.VISIBLE);
            holder.leftPage.setBackgroundColor(Color.WHITE);
            holder.leftShadown.setVisibility(View.VISIBLE);
            holder.leftPage.updateView(position * 2 - 1,mDatas);
            holder.rightPage.updateView(position*2,mDatas);
        }
    }

    private static class ViewHolder {
        public final FrameLayout flLeft;
        public final DiaryViewModule leftPage;
        public final View leftShadown;
        public final FrameLayout flRight;
        public final DiaryViewModule rightPage;
        public final View rightShadown;

        private ViewHolder(FrameLayout flLeft, DiaryViewModule leftPage, View leftShadown, FrameLayout flRight, DiaryViewModule rightPage, View rightShadown) {
            this.flLeft = flLeft;
            this.leftPage = leftPage;
            this.leftShadown = leftShadown;
            this.flRight = flRight;
            this.rightPage = rightPage;
            this.rightShadown = rightShadown;
        }

        public static ViewHolder create(View rootView) {
            FrameLayout flLeft = (FrameLayout)rootView.findViewById( R.id.fl_left );
            DiaryViewModule leftPage = (DiaryViewModule)rootView.findViewById( R.id.left_page );
            View leftShadown = (View)rootView.findViewById( R.id.left_shadown );
            FrameLayout flRight = (FrameLayout)rootView.findViewById( R.id.fl_right );
            DiaryViewModule rightPage = (DiaryViewModule)rootView.findViewById( R.id.right_page );
            View rightShadown = (View)rootView.findViewById( R.id.right_shadown );
            return new ViewHolder( flLeft, leftPage, leftShadown, flRight, rightPage, rightShadown );
        }
    }


}
