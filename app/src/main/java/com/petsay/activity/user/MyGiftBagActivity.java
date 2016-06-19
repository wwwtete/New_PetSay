package com.petsay.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.adapter.GiftBagAdapter;
import com.petsay.component.view.GiftBagPreViewDialog;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.GiftBagNet;
import com.petsay.application.UserManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.member.GiftBagVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangw on 2014/12/17.
 * 我的礼包
 */
public class MyGiftBagActivity extends BaseActivity implements NetCallbackInterface, AdapterView.OnItemClickListener {

    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPulltorefreshview;
    @InjectView(R.id.lv_giftbag)
    private ListView mLvGiftbag;
    @InjectView(R.id.tv_tip)
    private TextView mTvTip;
    @Inject
    private GiftBagNet mNet;
    private int mCurrIndex = 0;
    private GiftBagAdapter mAdapter;
    private GiftBagPreViewDialog mPreViewDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_giftbag);
        initView();
        onRefresh();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("我的礼包");
        mTitleBar.setFinishEnable(true);
        setListener();
    }

    private void setListener() {
        mNet.setTag(this);
        mNet.setCallback(this);
        mAdapter = new GiftBagAdapter(this,mNet);
        mLvGiftbag.setAdapter(mAdapter);
        mLvGiftbag.setOnItemClickListener(this);
        mPulltorefreshview.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                    onRefresh();
            }
        });

        mPulltorefreshview.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                onAddMore();
            }
        });
    }


    private void onRefresh(){
        mPulltorefreshview.showHeaderAnimation();
        mCurrIndex = 0;
        mNet.getMyGifBag(UserManager.getSingleton().getActivePetId(),mCurrIndex,10,false);
    }

    private void onAddMore(){
        showLoading();
        mCurrIndex++;
        mNet.getMyGifBag(UserManager.getSingleton().getActivePetId(),mCurrIndex,10,true);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch(requestCode){
            case RequestCode.REQUEST_GETMYGIFBAG:
                onGetMyGifBag(bean);
                break;
        }
    }

    private void onGetMyGifBag(ResponseBean bean) {
        try {
            List<GiftBagVo> list = JsonUtils.getList(bean.getValue(),GiftBagVo.class);
            if(bean.isIsMore()){
                if(list != null && list.size() > 0){
                    mAdapter.addMoreData(list);
                }else{
                    showToast(R.string.no_more);
                }
            }else{
                if(list != null && list.size() > 0){
                    mTvTip.setVisibility(View.GONE);
                    mAdapter.refreshData(list);
                }else{
                    mTvTip.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mPulltorefreshview.onComplete(bean.isIsMore());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mNet != null) {
            mNet.cancelAll(this);
            mNet = null;
        }

        if(mPreViewDialog != null)
            mPreViewDialog.release();
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        onErrorShowToast(error);
        mPulltorefreshview.onComplete(error.isIsMore());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object obj = mAdapter.getItem(position);
        if(obj != null && obj instanceof  GiftBagVo){
            GiftBagVo vo = (GiftBagVo) obj;
            if(vo.getPreview()){
                showPreViewDialog(vo);
            }else{
                showToast("您还没有权限预览这个礼包");
            }
        }

    }

    private void showPreViewDialog(GiftBagVo vo){
        closeLoading();


        if(mPreViewDialog == null){
            mPreViewDialog = new GiftBagPreViewDialog(this,mNet);
        }
        mPreViewDialog.showDialog(vo);
    }

    private void closePreViewDialog(){
        if(mPreViewDialog != null && mPreViewDialog.isShowing()){
            mPreViewDialog.closeDialog();
        }
    }
}
