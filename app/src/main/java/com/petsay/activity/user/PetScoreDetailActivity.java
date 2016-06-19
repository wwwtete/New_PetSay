package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.user.adapter.PetScoreAdapter;
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
import com.petsay.vo.member.GradeRuleVo;
import com.petsay.vo.member.PetScoreDetailVo;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * Date: 2014/12/18.
 * 积分详情
 */
public class PetScoreDetailActivity extends BaseActivity implements NetCallbackInterface,View.OnClickListener {

    @InjectView(R.id.tv_rule)
    private TextView mTvRule;
    @InjectView(R.id.img_header_usercenter)
    private CircleImageView mImgHeaderUsercenter;
    @InjectView(R.id.iv_sex)
    private ImageView mIvSex;
    @InjectView(R.id.tv_name)
    private TextView mTvName;
    @InjectView(R.id.tv_score)
    private TextView mTvScore;
    @InjectView(R.id.tv_grade)
    private TextView mTvGrade;
    @InjectView(R.id.iv_level)
    private ImageView mIvLevel;
    @InjectView(R.id.tv_currgrade)
    private TextView mTvCurrGrade;
    @InjectView(R.id.tv_nextgrade)
    private TextView mTvNextGrade;
    @InjectView(R.id.tv_needscore)
    private TextView mTvNeddScore;
    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPullToRefreshView;
    @InjectView(R.id.lv_score)
    private ListView mLvScore;
    @InjectView(R.id.pro_grade)
    private ProgressBar mProGrade;
    @Inject
    private MemberNet mMemberNet;

    private PetScoreAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_score_detail);
        mMemberNet.setTag(this);
        mMemberNet.setCallback(this);
        initView();
        showLoading();
        getGradeRule();
        onRefresh();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("我的等级");
        mTitleBar.setFinishEnable(true);
        mAdapter = new PetScoreAdapter(this);
        mLvScore.setAdapter(mAdapter);

        PetVo petVo = UserManager.getSingleton().getActivePetInfo();
        mTvName.setText(petVo.getNickName());
        mTvScore.setText("积分："+petVo.getScore());
        mTvGrade.setText("等级："+petVo.getIntGrade());
        mTvCurrGrade.setText("LV"+petVo.getIntGrade());
        mTvNextGrade.setText("LV"+(petVo.getIntGrade()+1));
        ImageLoaderHelp.displayHeaderImage(petVo.getHeadPortrait(),mImgHeaderUsercenter);
        if (petVo.getGender()==0) {
            mIvSex.setImageResource(R.drawable.female);

        }else if (petVo.getGender()==1) {
            mIvSex.setImageResource(R.drawable.male);
        }else {
            mIvSex.setVisibility(View.GONE);
        }
        if(petVo.getIntGrade() > 0)
            mIvLevel.setImageResource(petVo.getLevenIconResId());
        else
            mIvLevel.setVisibility(View.INVISIBLE);

        setListener();
    }

    private void setListener() {
        mTvRule.setOnClickListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                onAddMore();
            }
        });

        mPullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                onRefresh();
            }
        });
    }

    private void getGradeRule(){
        mMemberNet.getGradeRule();
    }

    private void onRefresh(){
        mMemberNet.getScoreDetail(UserManager.getSingleton().getActivePetId(),"",10,false);

    }

    private void onAddMore(){
        if(mAdapter.getCount() > 0){
            PetScoreDetailVo vo = (PetScoreDetailVo) mAdapter.getItem(mAdapter.getCount()- 1);
            mMemberNet.getScoreDetail(UserManager.getSingleton().getActivePetId(),vo.getId(),10,true);
        }else{
            showToast("没有数据");
            mPullToRefreshView.onComplete(true);
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_GETGRADERULE:
                onGradeRule(bean);
                break;
            case RequestCode.REQUEST_GETSCOREDETAIL:
                onGetScoreDetail(bean);
                break;
        }
    }

    private void onGradeRule(ResponseBean bean) {
        try {
            List<GradeRuleVo> list = JsonUtils.getList(bean.getValue(),GradeRuleVo.class);
            GradeRuleVo vo = null;
            int currScore = UserManager.getSingleton().getActivePetInfo().getScore();
            for (int i = 0;i < list.size();i++){
                GradeRuleVo temp = list.get(i);
                if(currScore >= temp.getScoreMin() && currScore <= temp.getScoreMax()){
                    vo = temp;
                    break;
                }
            }
            if(vo != null){
                mTvNeddScore.setText("升级还需要"+(vo.getScoreMax() - currScore+1)+"分");
                float progress = (currScore - vo.getScoreMin())*100.0f/(vo.getScoreMax() - vo.getScoreMin());
                mProGrade.setProgress((int)progress);
            }else{
                showToast("数据异常！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGetScoreDetail(ResponseBean bean) {
        try {
            List<PetScoreDetailVo> datas = JsonUtils.getList(bean.getValue(),PetScoreDetailVo.class);
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
        closeLoading();
        mPullToRefreshView.onComplete(bean.isIsMore());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        onErrorShowToast(error);
        mPullToRefreshView.onComplete(error.isIsMore());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_rule:
                Intent intent = new Intent();
                intent.setClass(this,WebViewActivity.class);
                intent.putExtra("folderPath","积分规则");
                intent.putExtra("url","http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=202669793&idx=1&sn=c9f5d37b4dbbb73c1455e4f502324aa9#rd");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeLoading();
        if(mMemberNet != null){
            mMemberNet.cancelAll(this);
            mMemberNet = null;
        }
    }
}
