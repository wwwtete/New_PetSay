package com.petsay.activity.main.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.chat.ChatMsgListActivity;
import com.petsay.activity.message.MessageActivity;
import com.petsay.application.MessageManager;
import com.petsay.application.UserManager;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgCallback;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.MarkImageView;
import com.petsay.component.view.slidingmenu.SlidingMenuItemModel;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.petalk.MessageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/13
 * @Description 主页左侧菜单
 */
public class MainLeftMenuView extends BasicMainMenuView implements AdapterView.OnItemClickListener, NetCallbackInterface, ChatMsgCallback {

    private ListView mLvMenu;
    private UserNet mUserNet;
    private SharePreferenceCache mCache;
    private MessageVo mMessageVo;
    private MarkImageView mSwitchView;
    private int mAnnouncementCount;
    private MsgReceiver msgReceiver;

    public MainLeftMenuView(Context context,MarkImageView switchView) {
        super(context);
        this.mSwitchView = switchView;
    }

    public MainLeftMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void initView() {
        super.initView();
        inflate(getContext(), R.layout.leftmenu_view, this);
        mLvMenu = (ListView) findViewById(R.id.lv_menu);
        mLvMenu.setAdapter(mAdapter);
        initMenuItem();
        mLvMenu.setOnItemClickListener(this);
        initData();
        regReceiver();
        refreshMsg();
        refreshChatMsgCount();
    }

    private void initData() {
        mUserNet=new UserNet();
        mUserNet.setCallback(this);
        mUserNet.setTag(getContext());
        mCache=SharePreferenceCache.getSingleton(getContext());

//        MessageManager.getSingleton().setCallBack(this);
        ChatMsgManager.getInstance().registerCallback(this);
    }

    /**注册广播*/
    private void regReceiver(){
		 /*动态方式注册广播接收者*/
        msgReceiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.petsay.msg");
        getContext().registerReceiver(msgReceiver, filter);
    }

    @Override
    public void onReceiveChatMsg(ChatMsgEntity entity) {
        refreshChatMsgCount();
        onRefreshSwitchViewSate();
    }

    public  class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            if (intent.getAction().equals("com.petsay.msg"))
                refreshMsg();
        }
    }

    @Override
    public void onResume() {
        refreshMsg();
        super.onResume();
    }

    public void refreshMsg(){

        if(isLogin()) {
//            refreshChatMsgCount();
            getUserMsgCount();
        }else {
            int count = mAdapter.getCount();
            for (int i=0;i<count;i++){
                mAdapter.getItem(i).isShowUnReadMsgCount = false;
            }
            mAdapter.notifyDataSetChanged();
            if(mSwitchView != null)
                mSwitchView.setMarkVisible(false);
        }
        getAnnouncementCount();
    }

    public void getAnnouncementCount(){
        mUserNet.announcementCount("");

    }
    public void getUserMsgCount(){
        mUserNet.messageUMCG(UserManager.getSingleton().getActivePetId());
    }

    public void refreshChatMsgCount(){
        int count = ChatDataBaseManager.getInstance().getNewestMsgTotalCount();
        setModelUnreadMsgCount(R.string.menu_msg_message,count);
        mAdapter.notifyDataSetChanged();
    }

    private SlidingMenuItemModel getModelByContentResId(int contentResId){
        int count = mAdapter.getCount();
        for (int i=0;i<count;i++){
            SlidingMenuItemModel model = mAdapter.getItem(i);
            if(model.contentResId == contentResId)
                return model;
        }
        return null;
    }

    private void setModelUnreadMsgCount(int contentResId,int count){
        SlidingMenuItemModel model = getModelByContentResId(contentResId);
        if(model != null){
            model.unReadMsgCount = count;
            model.isShowUnReadMsgCount = count > 0;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if(params != null){
            params.width = (int)getResources().getDimension(R.dimen.left_menu_width);
        }
        super.setLayoutParams(params);
    }

    private void initMenuItem() {
        List<SlidingMenuItemModel> models = new ArrayList<SlidingMenuItemModel>(5);
        models.add(new SlidingMenuItemModel(R.drawable.msg_comment,R.string.menu_msg_comment,this));
        models.add(new SlidingMenuItemModel(R.drawable.msg_favour,R.string.menu_msg_favour,this));
        models.add(new SlidingMenuItemModel(R.drawable.msg_focus,R.string.menu_msg_focus,this));
        models.add(new SlidingMenuItemModel(R.drawable.msg_message,R.string.menu_msg_message,this));
        models.add(new SlidingMenuItemModel(R.drawable.msg_assistant,R.string.menu_msg_assistant,this));
        mAdapter.refreshData(models);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SlidingMenuItemModel model = mAdapter.getItem(position);
        if(model != null && model.clickItemCallback != null)
            model.clickItemCallback.onClickItem(model);
    }

    @Override
    public void onClickItem(SlidingMenuItemModel model) {
        if(model == null)
            return;
        Intent intent = null;
        switch (model.contentResId){
            case R.string.menu_msg_comment:
                if(!checkJumpLogin()) {
                    if(model.isShowUnReadMsgCount && mMessageVo != null)
                        mCache.setLocal_C_RMsgCount(mMessageVo.getC_R());
                    intent = new Intent();
                    intent.setClass(getContext(), MessageActivity.class);
                    intent.putExtra("type", Constants.COMMENT_RELAY);
                    intent.putExtra("from", Constants.MSG_USER);
                    intent.putExtra("folderPath", "评论和转发");
                }
                break;
            case R.string.menu_msg_favour:
                if(!checkJumpLogin()) {
                    if(model.isShowUnReadMsgCount && mMessageVo != null)
                        mCache.setLocal_FMsgCount(mMessageVo.getF());
                    intent = new Intent();
                    intent.setClass(getContext(), MessageActivity.class);
                    intent.putExtra("type", Constants.FAVOUR);
                    intent.putExtra("from", Constants.MSG_USER);
                    intent.putExtra("folderPath", "踩了我");
                }
                break;
            case R.string.menu_msg_focus:
                if(!checkJumpLogin()) {
                    if(model.isShowUnReadMsgCount && mMessageVo != null)
                        mCache.setLocal_FansMsgCount(mMessageVo.getFans());
                    intent = new Intent();
                    intent.setClass(getContext(), MessageActivity.class);
                    intent.putExtra("type", Constants.FANS);
                    intent.putExtra("from", Constants.MSG_USER);
                    intent.putExtra("folderPath", "关注我的");
                }
                break;
            case R.string.menu_msg_message:
                if(!checkJumpLogin()) {
                    intent = new Intent();
                    intent.setClass(getContext(), ChatMsgListActivity.class);
                }
                break;
            case R.string.menu_msg_assistant:
                mCache.setLocal_announcement_MsgCount(mAnnouncementCount);
                intent = new Intent();
                intent.setClass(getContext(), MessageActivity.class);
                intent.putExtra("folderPath", "宠物说助手");
                intent.putExtra("from", Constants.MSG_ANNOUNCEMENT);
                break;
        }
        if(intent != null)
            jumpActivity(intent);
        if(model.isShowUnReadMsgCount) {
            model.isShowUnReadMsgCount = false;
            mAdapter.notifyDataSetChanged();
        }
        onRefreshSwitchViewSate();
    }

    private void onRefreshSwitchViewSate() {
        int count = mAdapter.getCount();
        boolean isShow = false;
        for(int i=0;i<count;i++){
            if(mAdapter.getItem(i).isShowUnReadMsgCount){
                isShow = true;
                break;
            }
        }
        if(mSwitchView != null){
            mSwitchView.setMarkVisible(isShow);
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {

        switch (requestCode) {
            case RequestCode.REQUEST_ANNOUNCEMENTCOUNT:
                int localCount=mCache.getLocal_announcementMsgCount();
                try {
                    mAnnouncementCount =Integer.parseInt(bean.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int assistantCount= mAnnouncementCount -localCount;
                setModelUnreadMsgCount(R.string.menu_msg_assistant,assistantCount);
                mAdapter.notifyDataSetChanged();
                break;
            case RequestCode.REQUEST_MESSAGEUMCG:
                mMessageVo = JsonUtils.resultData(bean.getValue(), MessageVo.class);
                setMessageCount();
                break;
            default:
                break;
        }
        onRefreshSwitchViewSate();
    }

    private void setMessageCount() {
        setModelUnreadMsgCount(R.string.menu_msg_comment,mMessageVo.getC_R()-mCache.getLocal_C_RMsgCount());
        setModelUnreadMsgCount(R.string.menu_msg_favour,mMessageVo.getF()-mCache.getLocal_FMsgCount());
        setModelUnreadMsgCount(R.string.menu_msg_focus,mMessageVo.getFans()-mCache.getLocal_FansMsgCount());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        PetsayLog.e("MainLeftViewError");
    }
}
