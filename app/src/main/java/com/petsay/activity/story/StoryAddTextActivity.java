package com.petsay.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;
import com.petsay.vo.story.StoryTextItem;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryAddTextActivity extends BaseActivity {

    @InjectView(R.id.ev_content)
    private EditText mEvContent;

    private StoryParams mParam;
    private int mPosition;
    private int mType;
    private StoryItemVo mItemVo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_add_text);
        mParam = (StoryParams) getIntent().getSerializableExtra("params");
        mPosition = getIntent().getIntExtra("position",-1);
        mType = getIntent().getIntExtra("type",StoryItemVo.TYPE_TEXT);
        if(mPosition != -1 && mPosition < mParam.items.size()){
            mItemVo = mParam.items.get(mPosition);
        }
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("输入文字", true);
        if(mType == StoryItemVo.TYPE_TEXT){
            mEvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        }else {
            mEvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        }
        if(mItemVo != null){
            if(mItemVo instanceof StoryImageItem)
                mEvContent.setText((((StoryImageItem)mItemVo).getDescribe()));
            else
                mEvContent.setText((((StoryTextItem)mItemVo).getContent()));
        }
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        TextView txt = PublicMethod.addTitleRightText(mTitleBar,"确定");
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValue()) {
                    v.setOnClickListener(null);
                    Intent intent = new Intent(StoryAddTextActivity.this, EditStoryActivity.class);
                    String txt = mEvContent.getText().toString().trim();
                    if(mType == StoryItemVo.TYPE_TEXT){
                        if(mItemVo != null)
                            ((StoryTextItem)mItemVo).setContent(txt);
                        else {
                            mItemVo = new StoryTextItem(txt);
                            mParam.items.add(mItemVo);
                        }
                    }else {
                        if(mItemVo != null) {
                            ((StoryImageItem)mItemVo).setDescribe(txt);
                        }
                    }
                    intent.putExtra("params", mParam);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean checkValue() {
        String str = mEvContent.getText().toString().trim();
        if(TextUtils.isEmpty(str)){
            showToast("故事内容不能为空");
            PublicMethod.startShakeAnimation(this,mEvContent);
            return false;
        }else {
            return true;
        }

    }
}
