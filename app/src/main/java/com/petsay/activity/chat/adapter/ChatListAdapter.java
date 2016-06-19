package com.petsay.activity.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emsg.sdk.EmsgConstants;
import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.DateUtils;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.chat.ChatContacts;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.chat.NewestMsg;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/4
 * @Description
 */
public class ChatListAdapter extends ExBaseAdapter<NewestMsg> {


    public ChatListAdapter(Context context) {
        super(context);
    }

    public NewestMsg remove(int position){
        if(position < mDatas.size()){
            NewestMsg msg =  mDatas.remove(position);
            notifyDataSetChanged();
            return msg;
        }
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.chatlist_item,null);
            holder = new ViewHolder();
            holder.findViews(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        NewestMsg msg = (NewestMsg) getItem(position);

        if(msg.getMsgCount() > 0){
            holder.tvMsgCount.setVisibility(View.VISIBLE);
            holder.tvMsgCount.setText(msg.getMsgCount()+"");
        }else {
            holder.tvMsgCount.setVisibility(View.GONE);
        }

        ChatContacts model = ChatDataBaseManager.getInstance().getChatContacts(msg.getPetId());
        ImageLoaderHelp.displayHeaderImage(model.getHeadPortrait(),holder.imgHeader);
        holder.tvName.setText(model.getNickName());
        ChatMsgEntity entity = ChatDataBaseManager.getInstance().getChatMsgEntitiy(msg.getChatMsgEntityId());
        if(entity != null){
            holder.tvTime.setText(DateUtils.calculateTime(entity.getDate().getTime()));
            if(EmsgConstants.MSG_TYPE_FILEAUDIO.equals(entity.getType())){
                holder.tvContent.setText("");
                holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.playaudio_anim_1, 0);
            }else {
                holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                holder.tvContent.setText(entity.getText());
            }
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView ivDelete;
        private RelativeLayout swipeBackview;
        private CircleImageView imgHeader;
        private TextView tvTime;
        private TextView tvName;
        private TextView tvContent;
        private TextView tvMsgCount;

        private void findViews(View view) {
            ivDelete = (ImageView)view.findViewById(R.id.swipe_backview);
            swipeBackview = (RelativeLayout)view.findViewById(R.id.swipe_frontview);
            imgHeader = (CircleImageView)view.findViewById(R.id.img_header);
            tvTime = (TextView)view.findViewById(R.id.tv_time);
            tvName = (TextView)view.findViewById(R.id.tv_name);
            tvContent = (TextView)view.findViewById(R.id.tv_content);
            tvMsgCount = (TextView) view.findViewById(R.id.tv_msg_count);
        }
    }

}
