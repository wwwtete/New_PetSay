package com.petsay.activity.petalk.publishtalk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.petalk.adapter.CustomTagAdapter;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.PublishPetSayNet;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetTagVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/10
 * @Description
 */
public class AddCustomActivity extends BaseActivity implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener, NetCallbackInterface {

    @InjectView(R.id.iv_use)
    private ImageView ivUse;
    @InjectView(R.id.ev_input)
    private EditText evInput;
    @InjectView(R.id.lv_tip)
    private ListView lvTip;

    private PublishPetSayNet mNet;
    private CustomTagAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("自定义标签", true);
        mNet = new PublishPetSayNet();
        mNet.setCallback(this);
        mNet.setTag(this);
        mAdapter = new CustomTagAdapter(this);
        lvTip.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {
        evInput.addTextChangedListener(this);
        ivUse.setOnClickListener(this);
        lvTip.setOnItemClickListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String key = evInput.getText().toString().trim();
        if(!TextUtils.isEmpty(key)){
            mNet.searchTag(getActivePetId(),key,0,100);
        }else {
            mAdapter.refreshData(null);
        }
    }

    @Override
    public void onClick(View v) {
        String key = evInput.getText().toString().trim();
        if(!TextUtils.isEmpty(key)) {
            showLoading();
            mNet.createTag(getActivePetId(),key);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setTag(mAdapter.getItem(position));
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_SEARCHTAG:
                try {
                    List<PetTagVo> list = JsonUtils.getList(bean.getValue(), PetTagVo.class);
                    mAdapter.refreshData(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RequestCode.REQUEST_CREATETAG:
                    PetTagVo tagVo = JsonUtils.resultData(bean.getValue(),PetTagVo.class);
                    setTag(tagVo);
                break;
        }
    }

    private void setTag(PetTagVo tagVo){
        if(tagVo == null)
            return;;
        if(tagVo.getDeleted()){
            showToast("标签已被禁用");
        }else {
            Intent intent = new Intent();
            intent.putExtra("tag",tagVo);
            setResult(RESULT_OK, intent);
//            BaseEditActivity.PublishParam.setTag(tagVo);
            this.finish();
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        if(requestCode != RequestCode.REQUEST_SEARCHTAG){
            onErrorShowToast(error);
        }
    }
}
