package com.petsay.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.wheelview.DateWheelView;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryAddressItem;
import com.petsay.vo.story.StoryParams;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryAddAddressActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.ev_address)
    private EditText mEvAddress;
    @InjectView(R.id.tv_time)
    private TextView mTvTime;
    @InjectView(R.id.rl_wheel)
    private RelativeLayout mRlWheel;
    @InjectView(R.id.dateview)
    private DateWheelView mDateview;
    @InjectView(R.id.btn_ok)
    private Button mBtnOk;
    private StoryParams mParam;

    private StoryAddressItem mItem;
    private boolean mIsUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_add_address);
        mParam = (StoryParams) getIntent().getSerializableExtra("params");
        int position = getIntent().getIntExtra("position",-1);
        if(position != -1 && position < mParam.items.size()){
           mItem = (StoryAddressItem) mParam.items.get(position);
            mIsUpdate = mItem != null;
        }
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("添加事件与地点", true);
        if(mIsUpdate){
            mEvAddress.setText(mItem.getAddress());
            mTvTime.setText(mItem.getTime());
        }else {
            mItem = new StoryAddressItem();
        }
        mDateview.setDefaultDate(System.currentTimeMillis());
        mBtnOk.setOnClickListener(this);
        mTvTime.setOnClickListener(this);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        TextView txt = PublicMethod.addTitleRightText(mTitleBar, "确定");
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValue()) {
                    v.setOnClickListener(null);
                    Intent intent = new Intent(StoryAddAddressActivity.this, EditStoryActivity.class);
                    if(!mIsUpdate)
                        mParam.items.add(mItem);
                    intent.putExtra("params", mParam);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean checkValue() {
        String str = mEvAddress.getText().toString().trim();
        mItem.setAddress(str);
        boolean flag = TextUtils.isEmpty(mItem.getAddress()) && TextUtils.isEmpty(mItem.getTime());
        if(flag)
            showToast("请输入故事地点或时间！");
        return !flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                mRlWheel.setVisibility(View.GONE);
                mTvTime.setText(mDateview.getSelectDate());
                mItem.setTime(mDateview.getSelectDate());
                break;
            case R.id.tv_time:
                PublicMethod.closeSoftKeyBoard(this,mEvAddress);
                mRlWheel.setVisibility(View.VISIBLE);
                mRlWheel.setFocusable(true);
                break;
        }
    }
}
