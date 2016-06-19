package com.petsay.activity.petalk.rank;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.rank.PetRankEveryMomentDTO;

import java.util.Calendar;

import roboguice.inject.InjectView;

/**
 * 成长经历
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/18
 * @Description
 */
public class GrowupUndergoActivity extends BaseActivity implements NetCallbackInterface {

    @InjectView(R.id.tv_join)
    private TextView tvJoin;
    @InjectView(R.id.tv_friend)
    private TextView tvFriend;
    @InjectView(R.id.tv_petalk)
    private TextView tvPetalk;
    @InjectView(R.id.tv_rank)
    private TextView tvRank;


    private PetRankEveryMomentDTO mPetDto;
    private UserNet mNet;
    /**1：周排行，0：总排行榜*/
    private int mRankType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growup);
        initView();
        mRankType = getIntent().getIntExtra("ranktype",0);
        getData(getIntent().getStringExtra("petid"));
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("成长历程",true);
        mNet = new UserNet();
        mNet.setCallback(this);
        mNet.setTag(this);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        ImageView imageView=PublicMethod.addTitleRightIcon(mTitleBar, R.drawable.rank_button_rule);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GrowupUndergoActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=205284014&idx=1&sn=7fa978b2abe59d83ccb25425a0709371#rd");
                startActivity(intent);
            }
        });
    }

    private void getData(String petId){
        showLoading();
        mNet.petGrowup(petId,mRankType);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        if(RequestCode.REQUEST_PETGROWUP == requestCode){
            mPetDto = JsonUtils.resultData(bean.getValue(),PetRankEveryMomentDTO.class);
            if(mPetDto != null)
                initDataView();
        }
    }

    private void initDataView() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mPetDto.getJoinTime());
        String joinStr = calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DATE)+"日";
        joinStr += "\n加入宠物说\n发了第一条说说";
        SpannableString joinSp = new SpannableString(joinStr);
        joinSp.setSpan(getSize(),0,joinStr.lastIndexOf("年"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinSp.setSpan(getSize(),joinStr.indexOf("年")+1,joinStr.lastIndexOf("月"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        joinSp.setSpan(getSize(),joinStr.indexOf("月")+1,joinStr.lastIndexOf("日"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvJoin.setText(joinSp);

        String friendStr = "结识了"+mPetDto.getFriendCount()+"个朋友，被"+mPetDto.getFollowerCount()+"个人关注";
        SpannableString friendSp = new SpannableString(friendStr);
        friendSp.setSpan(getSize(), friendStr.indexOf("了")+1, friendStr.indexOf("个"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        friendSp.setSpan(getSize(),friendStr.indexOf("被")+1,friendStr.lastIndexOf("个"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvFriend.setText(friendSp);

        String talkStr = mPetDto.getPetalkCount()+"个精彩瞬间。收到了"+mPetDto.getLikedCount()+"个赞";
        SpannableString talkSp = new SpannableString(talkStr);
        talkSp.setSpan(getSize(),0,talkStr.indexOf("个"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        talkSp.setSpan(getSize(),talkStr.indexOf("了")+1,talkStr.lastIndexOf("个"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPetalk.setText(talkSp);

        String temp = mRankType == 1 ? "周" : "总";
        String rankStr = "在热门排行"+temp+"榜中排名第"+mPetDto.getRkNum()+"位";
        SpannableString rankSp = new SpannableString(rankStr);
        rankSp.setSpan(getSize(),rankStr.indexOf("第")+1,rankStr.lastIndexOf("位"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRank.setText(rankSp);


    }

    private AbsoluteSizeSpan getSize(){
        return new AbsoluteSizeSpan(35,true);
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }


}
