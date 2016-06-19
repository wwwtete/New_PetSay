package com.petsay.activity.petalk.publishtalk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.vo.petalk.PetTagGroupVo;
import com.petsay.vo.petalk.PetTagVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/9
 * @Description
 */
public class TagGridAdapter<T> extends ExBaseAdapter {


    private boolean mIsGroup = false;

    public TagGridAdapter(Context context,boolean isGroup) {
        super(context);
        mIsGroup = isGroup;
    }

    public TagGridAdapter(Context context) {
        super(context);
    }

    public void setIsGropu(boolean isGroup){
        this.mIsGroup = isGroup;
    }

    public boolean isGroup(){
        return mIsGroup;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Object obj = getItem(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.gridview_tag_item,null);
            if(!isGroup()) {
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else {
                convertView.setBackgroundColor(Color.parseColor("#80FFFFFF"));
            }
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!isGroup()) {
            holder.tvName.setText(((PetTagVo)obj).getName());
        }else{
            holder.tvName.setText(((PetTagGroupVo)obj).getName());
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvName;

    }
}
