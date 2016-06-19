
package com.petsay.activity.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.emsg.sdk.EmsgConstants;
import com.petsay.chat.media.ChatMediaPlayer;
import com.petsay.component.view.chat.ChatMsgItemView;
import com.petsay.application.UserManager;
import com.petsay.utile.DateUtils;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.chat.ChatMsgEntity;

import java.util.LinkedList;
import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();


    private Context mContext;
    private LayoutInflater mInflater;
    private LinkedList<ChatMsgEntity> mDatas;
    private ChatMediaPlayer mPlayer;
    private int mMaxWidth;
    private int mMinWidth;

    public ChatMsgViewAdapter(Context context,ChatMediaPlayer player) {
        mContext = context;
        this.mPlayer = player;
        mMaxWidth = PublicMethod.getDisplayWidth(mContext)/2;
        mMinWidth = PublicMethod.getDiptopx(mContext,50);
        mInflater = LayoutInflater.from(context);
        mDatas = new LinkedList<ChatMsgEntity>();
    }

    public LinkedList<ChatMsgEntity> getDatas(){
        return mDatas;
    }

    public void addMoreData(List<ChatMsgEntity> list){
        if(list != null && !list.isEmpty()) {
            for (int i =0;i<list.size();i++){
                mDatas.addFirst(list.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void addMoreData(ChatMsgEntity entity){
        if(entity != null)
            mDatas.add(entity);
        notifyDataSetChanged();
    }

    public void clearData(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void deleteData(ChatMsgEntity entity){
        if(entity != null){
            mDatas.remove(entity);
        }
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        ChatMsgEntity entity = mDatas.get(position);

        if (entity.getIsComMeg()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    public ChatMediaPlayer getPlayer(){
        return mPlayer;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsgEntity entity = mDatas.get(position);
        ChatMsgItemView itemView = null;
        if(convertView == null){
            convertView = new ChatMsgItemView(mContext,this);
            itemView = (ChatMsgItemView)convertView;
            itemView.initView(entity.getIsComMeg());
            if(entity.getIsComMeg()){
                ImageLoaderHelp.displayHeaderImage(entity.getChatContacts().getHeadPortrait(),itemView.ivHeader);
            }else{
                ImageLoaderHelp.displayHeaderImage(UserManager.getSingleton().getActivePetInfo().getHeadPortrait(),itemView.ivHeader);
            }
        }else {
            itemView = (ChatMsgItemView) convertView;
        }
        itemView.setChatMsgEntity(entity);
        itemView.resetView();

        boolean showTime = position == 0;
        if(!showTime){
            showTime = entity.getDate().getTime() - mDatas.get(position-1).getDate().getTime() > 1000*60;
        }
        if(showTime){
            itemView.tvSendTime.setText(DateUtils.getFormatTime(entity.getDate()));
            itemView.tvSendTime.setVisibility(View.VISIBLE);
        }



        if(EmsgConstants.MSG_TYPE_FILEAUDIO.equals(entity.getType())){
            itemView.rlTextContent.setVisibility(View.GONE);
            itemView.audioView.setVisibility(View.VISIBLE);
            if(entity.getMediaTime() != null) {
                if(entity.getStates() != -1) {
                    itemView.audioView.getTimeText().setVisibility(View.VISIBLE);
                    itemView.audioView.setAudioSecond(entity.getMediaTime() + "\"");
                }
                float temp = entity.getMediaTime()*100.0f/60/100.0f;
                int width = (int) (mMaxWidth*temp);
                width = width > mMinWidth ? width : mMinWidth;
                itemView.audioView.setVoluemWidth(width);
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) itemView.audioView.getLayoutParams();
//                params.width = width > mMinWidth ? width : mMinWidth;
//                itemView.audioView.setLayoutParams(params);
            }

        }else if(EmsgConstants.MSG_TYPE_FILEIMG.equals(entity.getType())){
        }else {
            itemView.audioView.setVisibility(View.GONE);
            itemView.rlTextContent.setVisibility(View.VISIBLE);
            itemView.tvText.setText(entity.getText());
            itemView.tvText.setVisibility(View.VISIBLE);
        }
//        itemView.tvUserName.setText(entity.getChatContacts().getNickName());

        if(entity.getStates() == -1){
            itemView.ivStatas.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /*
    public View getView_old(int position, View convertView, ViewGroup parent) {

        final ChatMsgEntity entity = mDatas.get(position);
        final boolean isComMsg = entity.getIsComMeg();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_right, null);
            }


            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView
                    .findViewById(R.id.tv_username);
            viewHolder.tvText = (TextView) convertView
                    .findViewById(R.id.tv_chat_text);
            viewHolder.tvImage = (ImageView) convertView
                    .findViewById(R.id.tv_chat_image);
            viewHolder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.ivHeader = (CircleImageView) convertView.findViewById(R.id.img_header);
            viewHolder.ivStatas = (ImageView) convertView.findViewById(R.id.iv_status);

            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSendTime.setText(entity.getDate().toLocaleString());

        if (entity.getType() != null && entity.getType().equals("audio")) {
//            new File(android.os.Environment.getExternalStorageDirectory() + "/emsg/receive/audio/")
//                    .mkdirs();
//            new File(android.os.Environment.getExternalStorageDirectory() + "/emsg/send/audio/")
//                    .mkdirs();
//            viewHolder.tvText.setText("");
//            viewHolder.tvText.setVisibility(View.VISIBLE);
//            viewHolder.tvImage.setVisibility(View.GONE);
//            viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                    R.drawable.chatto_voice_playing, 0);
//            viewHolder.tvTime.setText(entity.getTime());
//
//            if (isComMsg) {
//                String key = entity.getText();
//                String filename = android.os.Environment.getExternalStorageDirectory()
//                        + "/emsg/receive/audio/" + key;
//                if (!new File(filename).exists()) {
//                    new AudioTask(filename).execute(key);
//                }
//            }
//            viewHolder.tvText.setOnClickListener(new OnClickListener() {
//
//                public void onClick(View v) {
//                    if (isComMsg) {
//                        playMusic(android.os.Environment.getExternalStorageDirectory()
//                                + "/emsg/receive/audio/" + entity.getText());
//                    } else {
//                        playMusic(android.os.Environment.getExternalStorageDirectory() + "/"
//                                + entity.getText());
//                    }
//                }
//            });
        } else if (entity.getType() != null && entity.getType().equals("image")) {
//            viewHolder.tvText.setVisibility(View.GONE);
//            viewHolder.tvImage.setVisibility(View.VISIBLE);
//            viewHolder.tvTime.setText("");
//
//            new File(android.os.Environment.getExternalStorageDirectory()
//                    + "/emsg/receive/image/thumb/").mkdirs();
//            new File(android.os.Environment.getExternalStorageDirectory()
//                    + "/emsg/receive/image/original/").mkdirs();
//            new File(android.os.Environment.getExternalStorageDirectory()
//                    + "/emsg/send/image/thumb/").mkdirs();
//            new File(android.os.Environment.getExternalStorageDirectory()
//                    + "/emsg/send/image/original/").mkdirs();
//
//            if (isComMsg) {
//                String key = entity.getText();
//                String filename = android.os.Environment.getExternalStorageDirectory()
//                        + "/emsg/receive/image/thumb/" + key;
//                if (new File(filename).exists()) {
//                    Bitmap bMap = BitmapFactory.decodeFile(filename);
//                    viewHolder.tvImage.setImageBitmap(bMap);
//                } else {
//                    viewHolder.tvImage.setImageResource(R.drawable.loading);
//                    String options = "imageView2/2/w/200/h/200";
//                    new ImageTask(viewHolder, filename).execute(filename, key, options);
//                }
//            } else {
//                String filename = entity.getText();
//                Bitmap bMap = BitmapFactory.decodeFile(filename);
//                viewHolder.tvImage.setImageBitmap(ThumbExtractor.extractMiniThumb(bMap, 200, 200));
//            }
//
//            viewHolder.tvImage.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    if (isComMsg) {
//
//                    }
//                }
//            });
        } else {
            viewHolder.tvText.setText(entity.getText());
            viewHolder.tvText.setVisibility(View.VISIBLE);
            viewHolder.tvImage.setVisibility(View.GONE);
            viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            viewHolder.tvTime.setText("");
        }
//        if(entity.getChatContacts() != null)
//        viewHolder.tvUserName.setText(entity.getChatContacts().getNickName());
        ImageLoaderHelp.displayHeaderImage(entity.getChatContacts().getHeadPortrait(),viewHolder.ivHeader);
            if(entity.getStates() == -1){
                viewHolder.ivStatas.setVisibility(View.VISIBLE);
            }else {
                viewHolder.ivStatas.setVisibility(View.GONE);
            }

        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvText;
        public ImageView tvImage;
        public TextView tvTime;
        public CircleImageView ivHeader;
        public ImageView ivStatas;
        public boolean isComMsg = true;
    }
*/
}
