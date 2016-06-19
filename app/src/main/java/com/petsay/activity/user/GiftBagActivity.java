package com.petsay.activity.user;

import android.content.Intent;
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
import com.petsay.component.view.HorizontalTitle;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.GiftBagNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.member.GiftBagVo;
import com.petsay.vo.member.GradeRuleVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;

/**
 * Created by wangw on 2014/12/15.
 * 礼包列表
 */
public class GiftBagActivity extends BaseActivity implements HorizontalTitle.ClickTitleCallback,NetCallbackInterface,AdapterView.OnItemClickListener {

    @InjectView(R.id.scrollview)
    private View mScrollView;
    @InjectView(R.id.titles)
    private HorizontalTitle mTitleView;
    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPullToRefreshView;
    @InjectView(R.id.lv_giftbag)
    private ListView mLvGiftBag;
    @Inject
    private GiftBagNet mGiftNet;
    /**tab标题列表*/
    private List<GradeRuleVo> mRuleVos;
    private GradeRuleVo mCurrRule;
    private GiftBagAdapter mAdapter;
    private Map<String,List<GiftBagVo>> mGiftListMap;
    private int mCurrIndex;
    private GiftBagPreViewDialog mPreViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftbag);
        mGiftNet.setTag(this);
        mGiftNet.setCallback(this);
        initView();
        getTabTitle();
    }

    @Override
    protected void initView() {
        super.initView();
       initTilte();
        mAdapter = new GiftBagAdapter(this,mGiftNet);
        mLvGiftBag.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {
        mPullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if(mCurrRule != null){
                    onAddMore(mCurrRule.getCode());
                }
            }
        });
        mPullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                if(mCurrRule != null){
                    onRefresh(mCurrRule.getCode());
                }
            }
        });
        mLvGiftBag.setOnItemClickListener(this);
    }

    private void initTilte() {
        initTitleBar("会员礼包");
        mTitleBar.setFinishEnable(true);
        TextView txt = PublicMethod.addTitleRightText(mTitleBar, "我的礼包");//new TextView(this);
//        txt.setText("我的礼包");
//        txt.setTextSize(18);
//        txt.setTextColor(Color.WHITE);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GiftBagActivity.this,MyGiftBagActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onClickTitleCallback(String title, int position) {
        if(mRuleVos != null && mRuleVos.size() > 0){
            mCurrRule = mRuleVos.get(position);
            if(title.equals(mCurrRule.getMemo())){
               List<GiftBagVo> list = mGiftListMap.get(mCurrRule.getCode());
                if(list.size() > 0){
                    mAdapter.refreshData(list);
                }else{
                    onRefresh(mCurrRule.getCode());
                }
            }else{
                showToast("数据异常，正在刷新");
                showLoading();
                getTabTitle();
            }
        }
    }

    private void getTabTitle(){
        mPullToRefreshView.showHeaderAnimation();
        mGiftNet.getGiftTabTitle(getPetId());
    }

    private void onRefresh(String code){
        mCurrIndex = 0;
        mGiftNet.getAllGiftBag(UserManager.getSingleton().getActivePetId(),code, mCurrIndex, 10,code,false);
    }

    private void onAddMore(String code){
        showLoading();
        mCurrIndex ++;
        mGiftNet.getAllGiftBag(UserManager.getSingleton().getActivePetId(),code, mCurrIndex, 10,code,true);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch(requestCode){
            case RequestCode.REQUEST_GETGIFTTABTITLE:
                onGetGiftBagTabTitle(bean);
                break;
            case RequestCode.REQUEST_GETALLGIFBAG:
                onGetAllGiftBag(bean);
                break;
            case RequestCode.REQUEST_DRAWGIFBAG:
                onDrawGifbag(bean);
                break;
        }
    }

    private void onDrawGifbag(ResponseBean bean) {
        if(bean.getError() == ProtocolManager.SUCCESS_CODE){
            showLoading();
            DecorationDataManager.getInstance(this).getServerDecorationData();
            onRefresh(mCurrRule.getCode());
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 礼包选项卡标题处理函数
     * @param bean
     */
    private void onGetGiftBagTabTitle(ResponseBean bean) {
        try {
            mRuleVos = JsonUtils.getList(bean.getValue(),GradeRuleVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mRuleVos != null && mRuleVos.size() > 0){
            mCurrRule = mRuleVos.get(0);
            mGiftListMap = new HashMap<String, List<GiftBagVo>>();
            if(mRuleVos.size() > 1) {
                mScrollView.setVisibility(View.VISIBLE);
                String[] titles = new String[mRuleVos.size()];
                for (int i = 0; i < mRuleVos.size(); i++) {
                    titles[i] = mRuleVos.get(i).getMemo();
                    mGiftListMap.put(mRuleVos.get(i).getCode(), new ArrayList<GiftBagVo>());
                }
                mTitleView.initView(titles, this);
            }else{
                mGiftListMap.put(mCurrRule.getCode(),new ArrayList<GiftBagVo>());
                mScrollView.setVisibility(View.GONE);
            }
            onRefresh(mCurrRule.getCode());
        }else{
            PublicMethod.log_e("onGetGiftBagTabTitle","礼包标题为null");
        }
    }

    private void onGetAllGiftBag(ResponseBean bean) {
        try {
            List<GiftBagVo> gifts = JsonUtils.getList(bean.getValue(),GiftBagVo.class);
            List<GiftBagVo> list = mGiftListMap.get(bean.getTag());
            if(bean.isIsMore()){
                if(gifts.size() > 0){
                    list.addAll(gifts);
                    mAdapter.addMoreData(gifts);
                }else{
                    showToast("没有更多了");
                }
            }else{
                list.clear();
                list.addAll(gifts);
                mAdapter.refreshData(gifts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PublicMethod.log_e("onGetAllGiftBag",e.getMessage());
        }
        mPullToRefreshView.onComplete(bean.isIsMore());

    }


    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        if(requestCode == RequestCode.REQUEST_DRAWGIFBAG){
            mAdapter.notifyDataSetChanged();
        }else {
            onErrorShowToast(error);
        }
        mPullToRefreshView.onComplete(error.isIsMore());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGiftNet != null) {
            mGiftNet.cancelAll(this);
            mGiftNet = null;
        }
        if(mPreViewDialog != null)
            mPreViewDialog.release();
        closeLoading();
    }

    private String getPetId(){
        return UserManager.getSingleton().getActivePetId();
    }


    private void showPreViewDialog(GiftBagVo vo){
        closeLoading();
        if(mPreViewDialog == null){
            mPreViewDialog = new GiftBagPreViewDialog(this,mGiftNet);
        }
        mPreViewDialog.showDialog(vo);
    }

    private void closePreViewDialog(){
        if(mPreViewDialog != null && mPreViewDialog.isShowing()){
            mPreViewDialog.closeDialog();
        }
    }


}
