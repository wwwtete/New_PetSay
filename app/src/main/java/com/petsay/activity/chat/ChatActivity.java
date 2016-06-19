package com.petsay.activity.chat;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.EmsgClient;
import com.emsg.sdk.EmsgConstants;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.chat.adapter.ChatMsgViewAdapter;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgCallback;
import com.petsay.chat.ChatMsgManager;
import com.petsay.chat.media.ChatMediaPlayer;
import com.petsay.component.view.AudioTextView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.chat.ChatMsgItemView;
import com.petsay.component.view.chat.ChatPopupWindow;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ChatSettingNet;
import com.petsay.application.UserManager;
import com.petsay.network.net.UserNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.chat.ChatContacts;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.petalk.PetVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2014/12/31
 * @Description 单个聊天窗口
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener, ChatMsgCallback, ChatPopupWindow.ClickChatPopuWindow, AdapterView.OnItemLongClickListener, AudioTextView.AudioTextViewCallback, PullToRefreshView.OnHeaderRefreshListener, NetCallbackInterface, AbsListView.OnScrollListener,AdapterView.OnItemClickListener {

    @InjectView(R.id.listview)
    private ListView mListView;
    @InjectView(R.id.refreshview)
    private PullToRefreshView mRefreshView;
    @InjectView(R.id.audiotext)
    private AudioTextView mAudioTextView;
    @InjectView(R.id.fl_authfail)
    private FrameLayout mFlAuthfail;
    @InjectView(R.id.tv_tip)
    private TextView mTvTip;

    private ChatPopupWindow mChatPopupWindow;
    //    private PetVo mPetVo;
    private ChatMsgManager mChatManager;
    private ChatMsgViewAdapter mAdapter;
    private ChatDataBaseManager mDB;

    private ChatContacts mContacts;
    private String mAccToId;
    private int mPageIndex = 1;
    private int mPageItem = 10;
    private int mPageCount = 0;
    private long mTotalItemCount =0;
    private ChatMediaPlayer mPlayer;
    private PopupWindow mCustomMenu;
    private ChatSettingNet mNet;
    /**对方是否在我的黑名单中*/
    private boolean mInMyBalck = false;
    /**我是否在对方黑名单中*/
    private boolean mInHerBalck = false;
//    private String mHeadPortrait;
//    private String mChatPopupWindowNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVale();
        checkAuth();
    }

    private void initVale() {
        mChatManager = ChatMsgManager.getInstance();
        mChatManager.registerCallback(this);
        mNet = new ChatSettingNet();
        mNet.setCallback(this);
        mNet.setTag(this);
    }

    protected void checkAuth() {
        super.initView();
        if(mChatManager.isAuth()){
            onInitView();
        }else if(UserManager.getSingleton().isLoginStatus()) {
            showLoading();
            mChatManager.auth(new EmsgCallBack() {
                @Override
                public void onSuccess() {
                    onInitView();
                }

                @Override
                public void onError(TypeError mErrorType) {
                    closeLoading();
                    showToast("登陆聊天服务器失败");
                }
            });
        }else{
            Intent intent =new Intent(this,UserLogin_Activity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter == null){
            onInitView();
        }
    }

    private void onInitView(){
        initDate();
        initChatView();
        mChatPopupWindow = new ChatPopupWindow(this);
        setListener();
        mPlayer = new ChatMediaPlayer(this);
        mAdapter = new ChatMsgViewAdapter(this,mPlayer);
        mTotalItemCount = mDB.getMsgEntityCountByid(mAccToId);
        mPageCount = (int) (mTotalItemCount /mPageItem);
        mPageCount += mTotalItemCount%mPageItem > 0 ? 1 : 0;
        mRefreshView.setPullDownRefreshEnable(mPageCount>1);
        loadMsgEntityList(-1);
        mListView.setAdapter(mAdapter);
        updateNewestMsgCount();
        closeLoading();
    }

    private void initChatView() {
        if(mContacts != null) {
            mAudioTextView.setEnabled(true);
            initTitleBar(mContacts.getNickName());
        }else {
            initTitleBar("",true);
            mAudioTextView.setEnabled(false);
        }
    }

    private void initDate() {
        mDB = ChatDataBaseManager.getInstance();
        PetVo petVo = (PetVo) getIntent().getSerializableExtra("petinfo");
        if(petVo == null){
            mAccToId = getIntent().getStringExtra("petid");
            mContacts = mDB.getChatContacts(mAccToId);
        }else{
            mAccToId  = petVo.getId();
            mContacts = new ChatContacts(mAccToId,petVo.getHeadPortrait(),petVo.getNickName());
            if(!mDB.hasContacts(mAccToId)){
                mDB.saveChatContacts(mContacts);
            }
        }
        mInMyBalck = mDB.hasBlackPetId(mAccToId);
        if(mContacts == null){
            getPetInfo();
        }
        mNet.canChat(mAccToId, getActivePetId());
    }

    private void getPetInfo(){
        showLoading(false);
        UserNet net =new UserNet();
        net.setCallback(this);
        net.petOne(mAccToId,getActivePetId());
    }

    @Override
    protected void initTitleBar(String title) {
        super.initTitleBar(title);
        mTitleBar.setFinishEnable(false);
        mTitleBar.setOnClickBackListener(new TitleBar.OnClickBackListener() {
            @Override
            public void OnClickBackListener() {
                if (checkSoftKeyBoardVisibility()) {
                    mAudioTextView.closeKeyBoard();
                } else {
                    ChatActivity.this.finish();
                }
            }
        });
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.more);
        imageView.setAdjustViewBounds(true);
        mTitleBar.addRightView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAudioTextView.closeKeyBoard();
                showCustomMenu();
            }
        });
    }

    private void loadMsgEntityList(long start){
        List<ChatMsgEntity> list = mDB.getMsgEntityById(mAccToId,start,mPageItem,true);
        if(list != null && !list.isEmpty()) {
            mAdapter.addMoreData(list);
            setListViewSelection(mPageItem);
        }
        mRefreshView.onComplete(false);
    }

    private void updateNewestMsgCount(){
        mChatManager.setChating_PetId(mAccToId);
        mDB.clearMsgCount(mAccToId);
    }

    private void setListener() {
//        mBtnSend.setOnClickListener(this);
        mAudioTextView.setCallback(this);
        mChatPopupWindow.setListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
//        mListView.setOnRefreshListener(this);
        mRefreshView.setOnHeaderRefreshListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_addblack:
                if(!mInMyBalck){
                    mNet.addBlack(mAccToId,getActivePetId());
                }else {
                    mNet.deleteBlack(mAccToId,getActivePetId());
                }
                hidenCustomMenu();
                break;
            case R.id.tv_clear:
                mDB.clearChatMsgByPetId(mAccToId);
                mAdapter.clearData();
                hidenCustomMenu();
                break;
            case R.id.btn_cancel:
                hidenCustomMenu();
                break;
        }
    }


    private void sendMsg(String content){
        if(TextUtils.isEmpty(content.trim())){
            return;
        }
        ChatMsgEntity entity = newMsgEntity();//new ChatMsgEntity();
        entity.setDate(new Date());
        entity.setText(content);
        entity.setType(EmsgConstants.MSG_TYPE_FILETEXT);
        if(mChatManager.isClose()){
            entity.setStates(-1);
        }
        mAdapter.addMoreData(entity);
//        mListView.setSelection(mAdapter.getCount() - 1);
        setListViewSelection(mAdapter.getCount() - 1);
        if(!mChatManager.isClose())
            mChatManager.sendMessage(getMsgTo(), content, EmsgClient.MsgTargetType.SINGLECHAT, getMsgCallabck(entity));//mEmsgCallBack);
    }

    private void sendAudio(File file,int voiceDuring){
        if(file == null || !file.exists()){
            return;
        }
        ChatMsgEntity entity = newMsgEntity();
        entity.setDate(new Date());
        entity.setType(EmsgConstants.MSG_TYPE_FILEAUDIO);
        entity.setMediaTime(voiceDuring);
        entity.setText(file.getName());
        if(mChatManager.isClose()){
            entity.setStates(-1);
        }
        mAdapter.addMoreData(entity);
//        mListView.setSelection(mAdapter.getCount() - 1);
        setListViewSelection(mAdapter.getCount() -1);
        if(!mChatManager.isClose())
            mChatManager.sendAudioMessage(file,voiceDuring,getMsgTo(),null, EmsgClient.MsgTargetType.SINGLECHAT,getMsgCallabck(entity));
    }

    private void setListViewSelection(final int position){
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(position);
            }
        });
    }

    private String getMsgTo(){
        return mAccToId + Constants.EMSG_SUFFIX;
    }

    private ChatMsgEntity newMsgEntity(){
        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setAccToId(mAccToId);
        entity.setAccFromId(UserManager.getSingleton().getActivePetId());
        entity.setPetId(mAccToId);
        entity.setIsComMeg(false);
        entity.setChatContacts(mContacts);
        //消息状态：0：发送中，1：发送成功，-1：发送失败
        entity.setStates(0);
        return entity;
    }

    private EmsgCallBack getMsgCallabck(final ChatMsgEntity entity){
        return new EmsgCallBack() {
            @Override
            public void onSuccess() {
                entity.setStates(1);
                mDB.saveMsgEntity(entity);
            }

            @Override
            public void onError(TypeError mErrorType) {
                entity.setStates(-1);
                mDB.saveMsgEntity(entity);
                mAdapter.notifyDataSetChanged();
//                showToast("发送消息失败！[" + mErrorType + "]");
            }
        };
    }

    @Override
    public void onReceiveChatMsg( ChatMsgEntity entity) {
        if(entity.getAccFromId().equals(mAccToId)) {
            mAdapter.addMoreData(entity);
            mListView.setSelection(mAdapter.getCount() - 1);
        }
    }

    @Override
    public void onClickCopyListener(ChatPopupWindow window, ChatMsgEntity entity) {
        if(entity != null){
            PublicMethod.copy(this, entity.getText());
            showToast("已复制");
        }
        window.dismiss();
    }

    @Override
    public void onClickDeleteListener(ChatPopupWindow window, ChatMsgEntity entity) {
        if(entity != null){
            mAdapter.deleteData(entity);
            mDB.deleteChatMsgEntity(entity.getId(), entity.getAccToId());
            showToast("删除成功");
        }
        window.dismiss();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(view instanceof ChatMsgItemView){
            ChatMsgEntity entity = ((ChatMsgItemView)view).getChatMsgEntity();
            boolean shwoCopy = TextUtils.isEmpty(entity.getType()) || EmsgConstants.MSG_TYPE_FILETEXT.equals(entity.getType());
            mChatPopupWindow.showWindow((ChatMsgItemView) view, entity, shwoCopy);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause(true);
    }

    @Override
    protected void onDestroy() {
        mChatManager.setChating_PetId("");
        mChatManager.unregisterCallback(this);
        mPlayer.release();
        if(mNet != null) {
            mNet.cancelAll(this);
            mNet = null;
        }
        super.onDestroy();
    }

    @Override
    public void onSendTextCallback(String text) {
        sendMsg(text);
    }

    @Override
    public void onStartRecorder() {
        mPlayer.pause(true);
    }

    @Override
    public void onStopRecorder() {
    }

    @Override
    public void onSendAudioCallback(File voicefile,int voiceSeconds) {
        sendAudio(voicefile, voiceSeconds);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex ++;
        if(mPageIndex <= mPageCount){
            loadMsgEntityList(((ChatMsgEntity) mAdapter.getItem(0)).getId());
        }
        if((mPageIndex+1) >= mPageCount){
            mRefreshView.setPullDownRefreshEnable(false);
            mRefreshView.onComplete(false);
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_CANCHAT:
                try {
                    JSONObject object = new JSONObject(bean.getValue());
                    //TODO 以后需要优化本地的黑名单是否与服务器的黑名单同步
                    mInMyBalck = object.getBoolean("black");
                    mInHerBalck = !object.getBoolean("chat");
                    setChatStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestCode.REQUEST_ADDBLACK:
                mInMyBalck = true;
                mDB.addChatBlack(mAccToId);
                setChatStatus();
                showToast("已添加到黑名单");
                break;
            case RequestCode.REQUEST_DELETEBLACK:
                mInMyBalck = false;
                mDB.deleteChatBlackByPetId(mAccToId);
                setChatStatus();
                showToast("已从黑名单移除");
                break;
            case RequestCode.REQUEST_PETONE:
                PetVo petVo = JsonUtils.resultData(bean.getValue(), PetVo.class);
                mContacts = new ChatContacts(mAccToId,petVo.getHeadPortrait(),petVo.getNickName());
                setChatStatus();
                break;
        }
    }

    private void setChatStatus() {
        if(mInHerBalck){
            mTvTip.setVisibility(View.VISIBLE);
            mTvTip.setText(R.string.chat_inher_black_tip);
            mAudioTextView.setEnabled(false);
        }else if(mInMyBalck){
            mTvTip.setVisibility(View.VISIBLE);
            mTvTip.setText(R.string.chat_inmy_black_tip);
            mAudioTextView.setEnabled(true && mContacts != null);
        }else {
            mTvTip.setVisibility(View.GONE);
            mAudioTextView.setEnabled(true && mContacts != null);
        }
    }

    private void hidenCustomMenu(){
        if(mCustomMenu != null) {
            mCustomMenu.setFocusable(false);
            mAudioTextView.setEditTextFocusable(true);
            mCustomMenu.dismiss();
        }
    }

    private void showCustomMenu(){
        hidenCustomMenu();
        View view = getLayoutInflater().inflate(R.layout.chat_menu_view, null);
        view.findViewById(R.id.tv_clear).setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(R.id.tv_addblack);
        textView.setOnClickListener(this);
        if(!mInMyBalck)
            textView.setText("加入黑名单");
        else
            textView.setText("移除黑名单");
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCustomMenu = new PopupWindow(view,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCustomMenu.setFocusable(true);
        mCustomMenu.setBackgroundDrawable(new BitmapDrawable());
        mCustomMenu.setAnimationStyle(R.anim.bottom_in);
        mCustomMenu.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        if(requestCode == RequestCode.REQUEST_PETONE){
            closeLoading();
            mAudioTextView.setEnabled(false);
            showToast("获取用户信息失败，请重试");
        }else {
            onErrorShowToast(error);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mAudioTextView.closeKeyBoard();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }


    /**
     * 判断键盘是否显示状态
     * @return
     */
    private boolean checkSoftKeyBoardVisibility(){
        int wh = PublicMethod.getDisplayHeight(this);
        int sh = mAudioTextView.getHeight();
        //判断当前的输入组件是否大于屏幕高度的三分之一，如果大于则证明键盘是显示状态
        if(sh < wh/2){
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAudioTextView.closeKeyBoard();
    }
}
