package com.petsay.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 *         Date 2014/12/18
 * BaseAdapter的扩展类，增加了刷新列表和增加数据列表的方法
 */
public class ExBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public ExBaseAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<T>();
    }

    /**
     * 刷新列表
     * @param data
     */
    public void refreshData(List<T> data){
        mDatas.clear();
        if(data != null && data.size() > 0 ){
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加更多数据
     * @param data
     */
    public void addMoreData(List<T> data){
        if(data != null && data.size() > 0){
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void deleteItem(int position){
        deleteItem(position,true);
    }

    public void deleteItem(int position,boolean isNotify){
        if(!mDatas.isEmpty() && position < getCount()){
            mDatas.remove(position);
            if(isNotify)
                notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
