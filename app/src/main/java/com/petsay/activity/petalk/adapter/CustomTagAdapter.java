package com.petsay.activity.petalk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.vo.petalk.PetTagVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/10
 * @Description
 */
public class CustomTagAdapter extends ExBaseAdapter<PetTagVo> {

    public CustomTagAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.custom_tag_tip_item,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(getItem(position).getName());

        return convertView;//super.getView(position, convertView, parent);
    }

    class ViewHolder{
        public TextView tvName;
    }
}
