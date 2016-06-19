package com.petsay.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.EmsgClient;
import com.emsg.sdk.client.android.asynctask.TaskCallBack;
import com.emsg.sdk.util.NetStateUtil;
import com.google.inject.Singleton;
import com.petsay.chat.upload.ChatUploadManager;
import com.petsay.chat.upload.ChatUploadServer;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.chat.ChatContacts;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.petalk.PetVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/4
 * @Description
 */
@Singleton
public class ChatMsgManager extends EmsgClient implements EmsgClient.EmsStateCallBack, NetCallbackInterface {


    private static ChatMsgManager instance;
    public static ChatMsgManager getInstance(){
        if(instance == null)
            instance = new ChatMsgManager();
        return instance;
    }

    private List<ChatMsgCallback> mMsgCallback;
    private Context mContext;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private ChatDataBaseManager mChatDB;
    private UserNet mUserNet;
    private ChatUploadManager mUploadManager;
    private MediaPlayer mMediaPlayer;

    //正在聊天的Petid
    private String mChating_PetId;

    public ChatMsgManager(){
        super();
    }

    @Override
    public void init(Context mAppContext) {
        super.init(mAppContext);
        this.mContext = mAppContext;
        mWorkThread = new HandlerThread("chart_thread");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper());
        mMsgCallback = new ArrayList<ChatMsgCallback>();
        setEmsStCallBack(this);
        mChatDB = ChatDataBaseManager.getInstance();
        mUploadManager = ChatUploadManager.getInstance();
        mUserNet = new UserNet();
        mUserNet.setCallback(this);

        mFileServerTarget = new ChatUploadServer(mAppContext);
    }

    @Override
    public synchronized void auth(String jid, String pwd, EmsgCallBack mEmsgCallBack) {
        super.auth(jid, pwd, mEmsgCallBack);
        mChatDB.init(mContext,UserManager.getSingleton().getActivePetId());
    }

    public synchronized void auth(final EmsgCallBack callBack) {
        runWorkThread(new Runnable() {
            @Override
            public void run() {
                auth(UserManager.getSingleton().getActivePetId()+ Constants.EMSG_SUFFIX, UserManager.getSingleton().getActivePetId(),callBack);
            }
        });
    }

    @Override
    public void closeClient() {
        super.closeClient();
    }

    /**
     * 设置正在聊天的PetId
     * @param petId
     */
    public void setChating_PetId(String petId){
        this.mChating_PetId = petId;
    }

    public synchronized void auth() {
        auth(new EmsgCallBack() {
            @Override
            public void onSuccess() {
//                PublicMethod.showToast(mContext,"登陆聊天服务器成功");
            }

            @Override
            public void onError(TypeError mErrorType) {
//                        PublicMethod.log_e("onError","登陆错误"+mErrorType);
//                PublicMethod.showToast(mContext, "聊天服务器异常:" + mErrorType);
            }
        });
    }

    public void runWorkThread(Runnable runnable){
        mWorkHandler.post(runnable);
    }


    /**
     * 接受到聊天消息
     * @param entity
     */
    public void receiveChartMsg(ChatMsgEntity entity){
        synchronized (mMsgCallback){
            saveMsg(entity);
//            if(PublicMethod.isAppTopRuning(mContext)) {
            if (TextUtils.isEmpty(mChating_PetId) || !entity.getAccFromId().equals(mChating_PetId))
                playRing();
//            }else {
//                showNotification(entity);
//            }
        }
    }

    private void saveMsg(ChatMsgEntity entity){
        if(mChatDB.hasContacts(entity.getPetId())){
            onSaveMsg(entity);
        }else{
            mUserNet.petOne(entity.getPetId(),UserManager.getSingleton().getActivePetId(),entity);
        }
    }

    private void onSaveMsg(ChatMsgEntity entity){
        int oper = 0;
        if(entity.getAccFromId().equals(mChating_PetId)){
            oper = -1;
        }
        mChatDB.saveMsgEntity(entity,oper);
        notifyCallback(entity);
    }

    private void notifyCallback(ChatMsgEntity entity){
        if(! mMsgCallback.isEmpty()){
            for(int i= 0;i < mMsgCallback.size();i++){
                mMsgCallback.get(i).onReceiveChatMsg(entity);
            }
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_PETONE:
                onRequestPetOne(bean);
                break;
        }
    }

    private void onRequestPetOne(ResponseBean bean) {
        ChatMsgEntity entity = (ChatMsgEntity) bean.getTag();
        PetVo vo = JsonUtils.resultData(bean.getValue(), PetVo.class);
        ChatContacts contacts = new ChatContacts();
        contacts.setHeadPortrait(vo.getHeadPortrait());
        contacts.setNickName(vo.getNickName());
        contacts.setPetId(vo.getId());
        entity.setChatContacts(contacts);
        mChatDB.saveChatContacts(contacts);
//        mChatDB.saveMsgEntity(entity,0);
//        notifyCallback(entity);
        onSaveMsg(entity);
    }

    /**
     * 发送语音
     * @param file 语音文件
     * @param voiceDuring 音频文件时长
     * @param msgTo 发送给对方的账号
     * @param mDataMap 用于消息扩展 (无则传null)
     * @param mTargetType 消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
     * @param mCallBack 用于消息发送成功与否的回调
     */
    public void sendAudioMessage(File file, final int voiceDuring, final String msgTo,
                                 final Map<String, String> mDataMap, final MsgTargetType mTargetType,
                                 final EmsgCallBack mCallBack) {
        if (mCallBack == null) {
            return;
        }
        if (!NetStateUtil.isNetWorkAlive(mContext)) {
            runCallBackError(mCallBack, EmsgCallBack.TypeError.NETERROR);
            return;
        }

        mUploadManager.upload(file, new TaskCallBack() {

            @Override
            public void onSuccess(String message) {
                sendAudioTextMessage(mDataMap, voiceDuring, msgTo, mTargetType, mCallBack,
                        message);
            }

            @Override
            public void onFailure() {
                runCallBackError(mCallBack, EmsgCallBack.TypeError.FILEUPLOADERROR);
            }
        });

    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        PublicMethod.showToast(mContext, "获取用户信息失败！");
    }

    /**
     * 注册回调
     * @param callback
     */
    public void registerCallback(ChatMsgCallback callback){
        synchronized (mMsgCallback) {
            this.mMsgCallback.add(callback);
        }
    }

    /**
     * 移出回调
     * @param callback
     */
    public void unregisterCallback(ChatMsgCallback callback){
        if(callback == null)
            return;;
        synchronized (mMsgCallback){
            mMsgCallback.remove(callback);
        }
    }

    /**
     * 播放铃声
     */
    private void playRing(){
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(mContext,
                        RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_NOTIFICATION));
                if(mMediaPlayer == null)
                    return;
                mMediaPlayer.setLooping(false);
            }
            mMediaPlayer.start();
        }catch (Exception e){

        }
    }

//    /**
//     * 显示通知栏通知
//     * @param entity
//     */
//    private void showNotification(ChatMsgEntity entity) {
//        String tickerText = mContext.getString(R.string.app_name);
//        Notification notification = new Notification(R.drawable.icon,tickerText,System.currentTimeMillis());
//        Intent intent = new Intent(mContext, MainActivity.class);
//        intent.putExtra("fragment", 1);
//        //定义下拉通知栏时要展现的内容信息
//        CharSequence contentTitle =mContext.getResources().getString(R.string.app_name);
//        CharSequence contentText = "";
//
//        if(EmsgConstants.MSG_TYPE_FILEAUDIO.equals(entity.getType())){
//            contentText = entity.getAccFromId()+"发来一段语音";
//        }else {
//            contentText = entity.getAccFromId()+"："+entity.getText();
//        }
//        PendingIntent contentIntent = PendingIntent.getActivity(mContext,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setLatestEventInfo(mContext, contentTitle, contentText,contentIntent);
//        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.defaults |=Notification.DEFAULT_LIGHTS;
//        notification.defaults |=Notification.DEFAULT_SOUND;
//        notification.defaults |=Notification.DEFAULT_VIBRATE;
////		        notification.ledARGB = Color.BLUE;
////		        notification.ledOnMS =5000; //闪光时间，毫秒
////		        long[] vibrate = {0,300,300,300};
////		        notification.vibrate=vibrate;
//        ((NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,notification);
//    }

    @Override
    public void onAnotherClientLogin() {
//        PublicMethod.showToast(mContext, "账号在别的客户端登陆");
        PublicMethod.log_e("onAnotherClientLogin", "账号在别的客户端登陆");
    }
}
