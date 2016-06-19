package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.user.adapter.PetCoinAdapter;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.MemberNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.member.PetCoinDetailVo;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * Date: 2014/12/19.
 * 宠豆详情
 */
public class PetCoinDetailActivity extends BaseActivity implements NetCallbackInterface {

    @InjectView(R.id.tv_rule)
    private TextView mTvRule;
    @InjectView(R.id.img_header_usercenter)
    private CircleImageView mImgHeaderUsercenter;
    @InjectView(R.id.tv_coin)
    private TextView mTvCoin;
    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPulltorefreshview;
    @InjectView(R.id.lv_coin)
    private ListView mLvCoin;
    @InjectView(R.id.iv_sex)
    private ImageView mIvSex;
    @InjectView(R.id.tv_name)
    private TextView mTvName;
    @Inject
    private MemberNet mMemberNet;
    private PetCoinAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_coin_detail);
        mMemberNet.setCallback(this);
        mMemberNet.setTag(this);
        initView();
        onRefresh();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("我的宠豆");
        mTitleBar.setFinishEnable(true);
        mAdapter = new PetCoinAdapter(this);
        mLvCoin.setAdapter(mAdapter);
        setListener();
        ImageLoaderHelp.displayHeaderImage(UserManager.getSingleton().getActivePetInfo().getHeadPortrait(),mImgHeaderUsercenter);
        PetVo petVo = UserManager.getSingleton().getActivePetInfo();
        mTvName.setText(petVo.getNickName());
        if (petVo.getGender()==0) {
            mIvSex.setImageResource(R.drawable.female);

        }else if (petVo.getGender()==1) {
            mIvSex.setImageResource(R.drawable.male);
        }else {
            mIvSex.setVisibility(View.GONE);
        }
        mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
    }

    private void setListener() {
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
        mTvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PetCoinDetailActivity.this,WebViewActivity.class);
                intent.putExtra("folderPath","宠豆规则");
                intent.putExtra("url","http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=202670540&idx=1&sn=35f8555c432bfb9df8217f8f34cff90c#rd");
                startActivity(intent);
            }
        });
    }

    private void onRefresh() {
        mPulltorefreshview.showHeaderAnimation();
        mMemberNet.getCoinDetail(UserManager.getSingleton().getActivePetId(),"",10,false);
    }

    private void onAddMore() {
        if(mAdapter.getCount() > 0){
            PetCoinDetailVo vo = (PetCoinDetailVo) mAdapter.getItem(mAdapter.getCount()- 1);
            mMemberNet.getCoinDetail(UserManager.getSingleton().getActivePetId(),vo.getId(),10,true);
        }else{
            showToast("没有数据");
            mPulltorefreshview.onComplete(true);
        }

    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_GETCOINDETAIL:
                onGetCoinDetail(bean);
                break;
        }

    }

    private void onGetCoinDetail(ResponseBean bean) {

        try {
            List<PetCoinDetailVo> datas = JsonUtils.getList(bean.getValue(),PetCoinDetailVo.class);
            if(bean.isIsMore()){
                if(datas != null && datas.size() > 0)
                    mAdapter.addMoreData(datas);
                else
                    showToast(R.string.no_more);
            }else{
                mAdapter.refreshData(datas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPulltorefreshview.onComplete(bean.isIsMore());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
        mPulltorefreshview.onComplete(error.isIsMore());
    }

    @Override
    protected void onDestroy() {
        if(mMemberNet != null){
            mMemberNet.cancelAll(this);
        }
        super.onDestroy();
    }
}
