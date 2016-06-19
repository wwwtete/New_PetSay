package com.petsay.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ChatSettingNet;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/26
 * @Description 私信设置
 */
public class ChatSettingActivity extends BaseActivity implements View.OnClickListener, NetCallbackInterface {

    public static final String key = "chatsetting_";

    @InjectView(R.id.rl_blacklist) private RelativeLayout rlBlacklist;
    @InjectView(R.id.rl_everyone)  private RelativeLayout rlEveryone;
    @InjectView(R.id.iv_everyone)  private ImageView ivEveryone;
    @InjectView(R.id.rl_attention) private RelativeLayout rlAttention;
    @InjectView(R.id.iv_attention) private ImageView ivAttention;

    private ChatSettingNet mNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);
        mNet = new ChatSettingNet();
        mNet.setCallback(this);
        mNet.getChatSetting(getActivePetId());
        initView();
        int mode = SharePreferenceCache.getSingleton(this).getValue(key + getActivePetId());
        setImageView(mode);
    }

    private void setImageView(int mode) {
        if(mode == 0){
            ivEveryone.setVisibility(View.VISIBLE);
            ivAttention.setVisibility(View.GONE);
        }else {
            ivAttention.setVisibility(View.VISIBLE);
            ivEveryone.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("私信设置");
        mTitleBar.setFinishEnable(true);
        setListener();
    }

    private void setListener() {
        rlBlacklist.setOnClickListener(this);
        rlEveryone.setOnClickListener(this);
        rlAttention.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_blacklist:
                Intent intent = new Intent();
                intent.setClass(this,ChatBlackListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_everyone:
                mNet.modifyChatSetting(getActivePetId(),0);
                setImageView(0);
                break;
            case R.id.rl_attention:
                mNet.modifyChatSetting(getActivePetId(),1);
                setImageView(1);
                break;
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
         switch (requestCode){
             case RequestCode.REQUEST_GETCHATSETTING:
                 PublicMethod.log_d(bean.getValue());
                 try {
                     JSONObject object = new JSONObject(bean.getValue());
                     int value = object.getInt("mesg");
                     setImageView(value);
                     SharePreferenceCache.getSingleton(this).saveValue(key+getActivePetId(),value);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 break;
         }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }
}
