package com.petsay.activity.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.petsay.activity.ExBaseAdapter;
import com.petsay.component.view.slidingmenu.SlidingMenuItem;
import com.petsay.component.view.slidingmenu.SlidingMenuItemModel;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/13
 * @Description
 */
public class MainSlidingMenuAdapter extends ExBaseAdapter<SlidingMenuItemModel> {

    private int mPadding;

    public MainSlidingMenuAdapter(Context context) {
        super(context);
        mPadding = PublicMethod.getDiptopx(context,10);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        if(convertView == null){
            convertView = new SlidingMenuItem(mContext);//new TextView(mContext);//
            convertView.setPadding(0, mPadding,0,mPadding/2);
        }
        ((SlidingMenuItem)convertView).setItemModel(getItem(position));
//        TextView view = (TextView) convertView;
//        SlidingMenuItemModel model = getItem(position);
//        view.setText(model.contentResId);
//        view.setCompoundDrawablesWithIntrinsicBounds(model.iconResId, 0, 0, 0);
//        view.setCompoundDrawablePadding(mPadding);
//        view.setTextColor(model.contentColor);
//        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        return  convertView;
    }
}
