package com.petsay.activity.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.chat.adapter.ChatBlackListAdapter;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ChatSettingNet;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/26
 * @Description
 */
public class ChatBlackListActivity extends BaseActivity implements NetCallbackInterface, PullToRefreshView.OnFooterRefreshListener, PullToRefreshView.OnHeaderRefreshListener, AdapterView.OnItemClickListener {


    @InjectView(R.id.refreshview)
    private PullToRefreshView mRefreshView;
    @InjectView(R.id.lv_blacklist)
    private ListView mListView;
    private ChatBlackListAdapter mAdapter;
    private ChatSettingNet mNet;
    private int mPageIndex =1;
    private int mPageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_blacklist);
        mNet = new ChatSettingNet();
        mNet.setCallback(this);
        mNet.setTag(this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("黑名单");
        mTitleBar.setFinishEnable(true);
        mAdapter = new ChatBlackListAdapter(this);
        mListView.setAdapter(mAdapter);
        mRefreshView.setOnHeaderRefreshListener(this);
        mRefreshView.setOnFooterRefreshListener(this);
        mListView.setOnItemClickListener(this);
        onRefresh();
    }

    private void onRefresh(){
        showLoading();
        mPageIndex = 0;
        mNet.getBlackList(getActivePetId(),mPageIndex,mPageSize,false);
    }

    private void onAddMore(){
        showLoading();
        mPageIndex ++;
        mNet.getBlackList(getActivePetId(),mPageIndex,mPageSize,true);
    }

    public void deleteBlack(int position){
        showLoading();
        PetVo vo = mAdapter.getItem(position);
        mNet.deleteBlack(vo.getId(), getActivePetId());
        mAdapter.deleteItem(position);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_GETBLACKLIST:
                closeLoading();
                try {
                    List<PetVo> datas = JsonUtils.getList(bean.getValue(),PetVo.class);
                    if(bean.isIsMore()){
                        if(datas != null && datas.isEmpty()){
                            showToast(R.string.no_more);
                        }else {
                            mAdapter.addMoreData(datas);
                        }
                    }else{
                        mAdapter.refreshData(datas);
                    }
                    mRefreshView.onComplete(bean.isIsMore());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RequestCode.REQUEST_DELETEBLACK:
                onRefresh();
                break;
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
        mRefreshView.onComplete(error.isIsMore());
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        onAddMore();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        onRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PetVo petInfo = (PetVo) mAdapter.getItem(position);
        ActivityTurnToManager.getSingleton().userHeaderGoto(ChatBlackListActivity.this, petInfo);
    }
}
