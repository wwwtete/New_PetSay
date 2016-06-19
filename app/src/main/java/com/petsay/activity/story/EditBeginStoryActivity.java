package com.petsay.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.HintPopupWindow;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryParams;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/9
 * @Description
 */
public class EditBeginStoryActivity extends BaseActivity {

    public static EditBeginStoryActivity instance;
    @InjectView(R.id.ev_title)
    private EditText mEvTitle;
    private StoryParams mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_story_edit_begin);
        mParams =new StoryParams();
        mParams.model = 2;
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("故事标题", true);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        PublicMethod.addTitleRightText(mTitleBar,"下一步").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckValue()) {
                    mParams.description = mEvTitle.getText().toString();
                    mParams.createTime = DateFormat.format("yyyy.MM.dd", System.currentTimeMillis()).toString();
                    Intent intent = new Intent(EditBeginStoryActivity.this, StoryPhotoWallActivity.class);
                    intent.putExtra("params", mParams);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean onCheckValue() {
        String str = mEvTitle.getText().toString().trim();
        boolean flag = !TextUtils.isEmpty(str);
        if(!flag){
            PublicMethod.startShakeAnimation(this,mEvTitle);
        }
        return flag;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            checkShowHint();
        }
    }

    private void checkShowHint() {
        boolean isShow = SharePreferenceCache.getSingleton(this).getBooleanValue("editstorybeginactivity_hint", true);
        if(isShow){
            HintPopupWindow popu = new HintPopupWindow(this);
            popu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    SharePreferenceCache.getSingleton(EditBeginStoryActivity.this).setBooleanValue("editstorybeginactivity_hint", false);
                }
            });
            popu.show(mEvTitle, Gravity.BOTTOM,R.drawable.hint_story_title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(instance != null)
            instance = null;
    }
}
