package com.petsay.activity.chat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.chat.ChatBlackListActivity;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.PetVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/26
 * @Description
 */
public class ChatBlackListAdapter extends ExBaseAdapter<PetVo> {

    private ChatBlackListActivity mActivity;

    public ChatBlackListAdapter(ChatBlackListActivity  activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.chat_blacklist_item_view,null);
            holder = new ViewHolder();
            holder.findViews(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PetVo vo = getItem(position);
        holder.tvName.setText(vo.getNickName());
        holder.tvCount.setText("宠物说：" + vo.getCounter().getIssue());
        holder.tvFans.setText("粉丝："+vo.getCounter().getFans());
        holder.tvDelete.setTag(position);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.deleteBlack((Integer) v.getTag());

            }
        });

        ImageLoaderHelp.displayHeaderImage(vo.getHeadPortrait(), holder.imgHeader);

        return convertView;
    }

    class ViewHolder{
        public CircleImageView imgHeader;
        public TextView tvDelete;
        public TextView tvName;
        public TextView tvCount;
        public TextView tvFans;

        private void findViews(View view) {
            imgHeader = (CircleImageView)view.findViewById( R.id.img_header );
            tvDelete = (TextView)view.findViewById( R.id.tv_delete );
            tvName = (TextView)view.findViewById( R.id.tv_name );
            tvCount = (TextView)view.findViewById( R.id.tv_count );
            tvFans = (TextView)view.findViewById( R.id.tv_fans );
        }

    }
}
