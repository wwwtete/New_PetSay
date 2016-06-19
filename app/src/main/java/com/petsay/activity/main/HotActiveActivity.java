package com.petsay.activity.main;

import android.os.Bundle;

import com.petsay.R;
import com.petsay.activity.BaseActivity;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/7
 * @Description 热门活动
 */
public class HotActiveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_active);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("热门活动",true);
    }
}
