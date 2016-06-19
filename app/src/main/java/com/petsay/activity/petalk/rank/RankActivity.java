package com.petsay.activity.petalk.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.SlidingView;
import com.petsay.utile.PublicMethod;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/17
 * @Description
 */
public class RankActivity extends BaseActivity implements SlidingView.SlidingViewCallback {


//    @InjectView(R.id.ranktitle)
//    private HorizontalTitle ranktitle;


    @InjectView(R.id.slidingview)
    private SlidingView mSlidingView;

    /**1:说说排行   0：爱宠排行榜*/
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mType = getIntent().getIntExtra("type",0);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        String title = mType == 1 ? "说说排行" : "萌宠排行";
        initTitleBar(title, true);
        initSlidingView();
        setListener();
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
//        TextView txt = PublicMethod.addTitleRightText(mTitleBar, "排行规则");
        if(mType ==1 ) {
            ImageView imageView = PublicMethod.addTitleRightIcon(mTitleBar, R.drawable.rank_button_rule);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(RankActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=205284014&idx=1&sn=7fa978b2abe59d83ccb25425a0709371#rd");
                    startActivity(intent);
                }
            });
        }
    }

    private void initSlidingView() {
        String[] titles = {"周榜","总榜"};
        RankFragment totalRank = RankFragment.getInstance(mType, 1);
        RankFragment weekRank = RankFragment.getInstance(mType,0);
        List<Fragment> list = new ArrayList<Fragment>(2);
        list.add(totalRank);
        list.add(weekRank);
        mSlidingView.initView(getSupportFragmentManager(),titles,list);
        mSlidingView.hideCursorView(true);
    }

    private void setListener() {
        mSlidingView.setCallback(this);
    }

    @Override
    public void setCurrentItem(int item) {
        GifViewManager.getInstance().stopGif();
    }

    @Override
    public void slidingViewPageChange(int item) {
        GifViewManager.getInstance().stopGif();
    }
}
