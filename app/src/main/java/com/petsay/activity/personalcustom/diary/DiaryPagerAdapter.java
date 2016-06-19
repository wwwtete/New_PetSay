package com.petsay.activity.personalcustom.diary;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.petsay.activity.personalcustom.diary.diaryview.BasicDiaryView;
import com.petsay.component.face.ViewPagerAdapter;
import com.petsay.utile.PetsayLog;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/3
 * @Description
 */
public class DiaryPagerAdapter extends PagerAdapter {


    private Context mContext;
    private BasicDiaryView[] mTitlePages;
    private int mCount =0;
    private boolean isUpdate = true;

    public DiaryPagerAdapter(Context context,BasicDiaryView[] pages) {
        mContext = context;
        this.mTitlePages = pages;
    }

    public void setCount(int count){
        this.mCount = count;
        isUpdate = false;
        notifyDataSetChanged();
        isUpdate = true;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position);
        if(view.getParent() == null) {
            container.addView(view);
        }
        return view;
    }

    private View getView(int position){
        PetsayLog.d("getView=%s",""+position);
        int index = position % mTitlePages.length;
        BasicDiaryView view = mTitlePages[index];
        view.setPageIndex(position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PetsayLog.d("destroyItem=%s",""+position);
        container.removeView((View) object);
//        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return isUpdate ? ViewPagerAdapter.POSITION_NONE : super.getItemPosition(object);//
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
