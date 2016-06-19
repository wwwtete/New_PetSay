package com.petsay.activity.petalk.publishtalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.petsay.component.view.publishtalk.DecorationMoreItemView;
import com.petsay.vo.decoration.DecorationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/6
 * @Description
 */
public class DecorationMoreGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<DecorationBean> mDatas;
    private LayoutInflater mInflater;

    public DecorationMoreGridAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<DecorationBean>();
    }

    public void updateDecorationData(List<DecorationBean> list){
        mDatas.clear();
        if(list != null)
            mDatas.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public DecorationBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new DecorationMoreItemView(mContext);
        }
        DecorationMoreItemView view = (DecorationMoreItemView) convertView;
        view.updateView(getItem(position));
        return convertView;
    }
}
