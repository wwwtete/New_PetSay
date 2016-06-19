package com.petsay.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.AttentionActivity;
import com.petsay.activity.chat.adapter.ChatListAdapter;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.chat.ChatMsgCallback;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.swipelistview.BaseSwipeListViewListener;
import com.petsay.component.view.swipelistview.SwipeListView;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.chat.NewestMsg;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2014/12/31
 * @Description 聊天联系人列表Activity
 */
public class ChatMsgListActivity extends BaseActivity implements ChatMsgCallback {

    @InjectView(R.id.swipelistviwe)
    private SwipeListView mSwipelistviwe;
    @InjectView(R.id.tv_nomsg)
    private TextView mTvNomsg;

    private ChatListAdapter mAdapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmsglist);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("私信");
        mTitleBar.setFinishEnable(true);
        mAdapter = new ChatListAdapter(this);
        mSwipelistviwe.setAdapter(mAdapter);
        mSwipelistviwe.setOffsetLeft(PublicMethod.getDisplayWidth(this)-PublicMethod.getDiptopx(this,80));
        setListener();
    }

    @Override
    protected void initTitleBar(String title) {
        super.initTitleBar(title);
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.addchatbtn);
        view.setAdjustViewBounds(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatMsgListActivity.this,AttentionActivity.class);
                intent.putExtra("petId", UserManager.getSingleton().getActivePetId());
                intent.putExtra("type",1);
                startActivity(intent);

            }
        });
        mTitleBar.addRightView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.refreshData(getNewestMsgList());
    }

    private void setListener() {
        final String TAG = "listview";
        ChatMsgManager.getInstance().registerCallback(this);
        mSwipelistviwe.setSwipeListViewListener(mSwipeListener);
//                new BaseSwipeListViewListener()
//        {
//            @Override
//            public void onChoiceChanged(int position, boolean selected)
//            {
//                PetsayLog.d(TAG, "onChoiceChanged:" + position + ", " + selected);
//            }
//
//            @Override
//            public void onChoiceEnded()
//            {
//                PetsayLog.d(TAG, "onChoiceEnded");
//            }
//
//            @Override
//            public void onChoiceStarted()
//            {
//                PetsayLog.d(TAG, "onChoiceStarted");
//            }
//
//            @Override
//            public void onClickBackView(int position)
//            {
//                PetsayLog.d(TAG, "onClickBackView:" + position);
//            }
//
//            @Override
//            public void onClickFrontView(int position)
//            {
//                PetsayLog.d(TAG, "onClickFrontView:" + position);
//            }
//
//            @Override
//            public void onClosed(int position, boolean fromRight)
//            {
//                PetsayLog.d(TAG, "onClosed:" + position + "," + fromRight);
//            }
//
//            @Override
//            public void onDismiss(int[] reverseSortedPositions)
//            {
//                PetsayLog.d(TAG, "onDismiss");
//
//            }
//
//            @Override
//            public void onFirstListItem()
//            {
//                PetsayLog.d(TAG, "onFirstListItem");
//            }
//
//            @Override
//            public void onLastListItem()
//            {
//                PetsayLog.d(TAG, "onLastListItem");
//            }
//
//            @Override
//            public void onListChanged()
//            {
//                PetsayLog.d(TAG, "onListChanged");
//
//                mSwipelistviwe.closeOpenedItems();
//
//            }
//
//            @Override
//            public void onMove(int position, float x)
//            {
//                PetsayLog.d(TAG, "onMove:" + position + "," + x);
//            }
//
//            @Override
//            public void onOpened(int position, boolean toRight)
//            {
//                PetsayLog.d(TAG, "onOpened:" + position + "," + toRight);
//            }
//
//            @Override
//            public void onStartClose(int position, boolean right)
//            {
//                PetsayLog.d(TAG, "onStartClose:" + position + "," + right);
//            }
//
//            @Override
//            public void onStartOpen(int position, int action, boolean right)
//            {
//                PetsayLog.d(TAG, "onStartOpen:" + position + "," + action + ","
//                        + right);
//            }
//        });
    }

    private List<NewestMsg> getNewestMsgList(){
        List<NewestMsg> contactsLIst = ChatDataBaseManager.getInstance().getNewestMsgList(10);
        return contactsLIst;
    }

    private BaseSwipeListViewListener mSwipeListener = new BaseSwipeListViewListener(){

        @Override
        public void onListChanged()
        {
            mSwipelistviwe.closeOpenedItems();
        }

        @Override
        public void onClickFrontView(int position)
        {
           PublicMethod.log_d("onClickFrontView"+position);
            Intent intent = new Intent();
            intent.setClass(ChatMsgListActivity.this, ChatActivity.class);
            intent.putExtra("petid",((NewestMsg)mAdapter.getItem(position)).getPetId());
            startActivity(intent);
            overridePendingTransition(R.anim.fade, R.anim.hold);
        }

        @Override
        public void onClickBackView(int position)
        {

           NewestMsg msg =  mAdapter.remove(position);
            mSwipelistviwe.closeOpenedItems();
            if(msg != null) {
                ChatDataBaseManager.getInstance().deleteChatContacts(msg);
            }
        }

    };


    @Override
    public void onReceiveChatMsg(ChatMsgEntity entity) {
        mAdapter.refreshData(getNewestMsgList());
    }
}
