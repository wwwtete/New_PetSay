package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/6
 * @Description 不滚动的GridView
 */
public class NoScrollGridView extends GridView {


    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mh = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, mh);
    }
}
