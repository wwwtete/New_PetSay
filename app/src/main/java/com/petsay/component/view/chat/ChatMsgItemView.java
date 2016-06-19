package com.petsay.component.view.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.EmsgClient;
import com.emsg.sdk.EmsgConstants;
import com.petsay.R;
import com.petsay.activity.chat.adapter.ChatMsgViewAdapter;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgManager;
import com.petsay.chat.media.ChatAudioView;
import com.petsay.chat.media.ChatMediaPlayer;
import com.petsay.component.view.CircleImageView;
import com.petsay.constants.Constants;
import com.petsay.vo.chat.ChatMsgEntity;

import java.io.File;
import java.util.Date;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/19
 * @Description 聊天消息
 */
public class ChatMsgItemView extends RelativeLayout implements View.OnClickListener {

    public TextView tvSendTime;
    public TextView tvUserName;
    public TextView tvText;
    public ImageView tvImage;
//    public TextView tvTime;
    public CircleImageView ivHeader;
    public ImageView ivStatas;
    public View rlTextContent;
    public ChatAudioView audioView;
//    public RelativeLayout rlSoundContent;
//    public FrameLayout flVoluem;
//    public ImageView ivVoluem;
    public boolean isComMsg = true;

    private ChatMsgEntity mChatMsgEntity;
    private ChatMsgViewAdapter mAdapter;
    private ChatMediaPlayer mPlayer;

    public ChatMsgItemView(Context context,ChatMsgViewAdapter adapter) {
        super(context);
        this.mAdapter = adapter;
        mPlayer = adapter.getPlayer();
    }

    public void initView(boolean isComMsg) {
        this.isComMsg = isComMsg;
        if (isComMsg) {
            inflate(getContext(),R.layout.chatting_item_msg_left, this);
        } else {
            inflate(getContext(),R.layout.chatting_item_msg_right, this);
        }

        findViews();
        ivStatas.setOnClickListener(this);
        audioView.setOnClickListener(this);
//        flVoluem.setOnClickListener(this);
    }

    private void findViews() {
        tvSendTime = (TextView) findViewById(R.id.tv_sendtime);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        tvText = (TextView) findViewById(R.id.tv_chat_text);
        tvImage = (ImageView) findViewById(R.id.tv_chat_image);
//        tvTime = (TextView) findViewById(R.id.tv_time);
        ivHeader = (CircleImageView) findViewById(R.id.img_header);
        ivStatas = (ImageView) findViewById(R.id.iv_status);
        rlTextContent = findViewById(R.id.tv_chatcontent);
        audioView = (ChatAudioView) findViewById(R.id.audioview);
//        rlSoundContent = (RelativeLayout) findViewById(R.id.rl_sound);
//        flVoluem = (FrameLayout) findViewById(R.id.fl_voluem);
//        ivVoluem = (ImageView) findViewById(R.id.iv_voluem);
    }

    public void setChatMsgEntity(ChatMsgEntity entity){
        this.mChatMsgEntity = entity;
    }

    public void resetView(){
        tvSendTime.setText("");
        tvText.setText("");
        tvText.setText("");
        tvUserName.setVisibility(View.GONE);
        ivStatas.setVisibility(View.GONE);
        tvSendTime.setVisibility(View.GONE);
        tvImage.setVisibility(View.GONE);
    }

    public ChatMsgEntity getChatMsgEntity() {
        return mChatMsgEntity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_status:
                onRetrySendMsg(v);
                break;
            case R.id.audioview:
                onPlayAudio();
                break;
        }
    }

    private void onPlayAudio() {
        String url = mChatMsgEntity.getText();
        if(!mChatMsgEntity.getIsComMeg()){
            url = Constants.DOWNLOAD_SERVER +Constants.CHAT_SERVER_AUDIO +mChatMsgEntity.getText();
        }
        mPlayer.play(url,mChatMsgEntity.getIsComMeg(),audioView);
    }

    private void onRetrySendMsg(View v) {
        if(mChatMsgEntity != null && mChatMsgEntity.getStates() == -1 && !TextUtils.isEmpty(mChatMsgEntity.getText())){
            v.setVisibility(View.GONE);
            mChatMsgEntity.setDate(new Date());
            mChatMsgEntity.setStates(0);
            mAdapter.deleteData(mChatMsgEntity);
            if(EmsgConstants.MSG_TYPE_FILEAUDIO.equals(mChatMsgEntity.getType())){
                ChatMsgManager.getInstance().sendAudioMessage(new File(mChatMsgEntity.getText()),
                        mChatMsgEntity.getMediaTime(),
                        mChatMsgEntity.getAccToId() + Constants.EMSG_SUFFIX,
                        null,
                        EmsgClient.MsgTargetType.SINGLECHAT,
                        getMsgCallabck(mChatMsgEntity));
            }else if(EmsgConstants.MSG_TYPE_FILETEXT.equals(mChatMsgEntity.getType())) {
                ChatMsgManager.getInstance().sendMessage(mChatMsgEntity.getAccToId() + Constants.EMSG_SUFFIX, mChatMsgEntity.getText(), EmsgClient.MsgTargetType.SINGLECHAT, getMsgCallabck(mChatMsgEntity));//mEmsgCallBack);
            }
            mAdapter.addMoreData(mChatMsgEntity);
        }
    }

    private EmsgCallBack getMsgCallabck(final ChatMsgEntity entity){
        return new EmsgCallBack() {
            @Override
            public void onSuccess() {
                entity.setStates(1);
                ChatDataBaseManager.getInstance().updateMsgEntity(entity);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(TypeError mErrorType) {
                entity.setStates(-1);
                ChatDataBaseManager.getInstance().updateMsgEntity(entity);
                mAdapter.notifyDataSetChanged();
//                PublicMethod.showToast(getContext(), "发送消息失败！[" + mErrorType + "]");
            }
        };
    }

}
