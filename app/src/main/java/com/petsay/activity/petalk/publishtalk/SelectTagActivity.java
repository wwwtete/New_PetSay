package com.petsay.activity.petalk.publishtalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.petalk.publishtalk.adapter.TagGridAdapter;
import com.petsay.component.view.NoScrollGridView;
import com.petsay.component.view.publishtalk.MoreTagView;
import com.petsay.database.DBManager;
import com.petsay.database.greendao.petsay.PetTagVoDao;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.PublishPetSayNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetTagGroupVo;
import com.petsay.vo.petalk.PetTagVo;
import com.petsay.vo.petalk.PublishPublickParams;
import com.petsay.vo.story.StoryParams;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/9
 * @Description 选择标签
 */
public class SelectTagActivity extends BaseActivity implements NetCallbackInterface, View.OnClickListener, AdapterView.OnItemClickListener, MoreTagView.MoreTagCallback {



    @InjectView(R.id.ll_lables)
    private LinearLayout llLables;
    @InjectView(R.id.ll_hot)
    private LinearLayout llHot;
    @InjectView(R.id.tv_more_hot)
    private TextView tvMoreHot;
    @InjectView(R.id.gv_hot)
    private NoScrollGridView gvHot;
    @InjectView(R.id.ll_self)
    private LinearLayout llSelf;
    @InjectView(R.id.tv_more_self)
    private TextView tvMoreSelf;
    @InjectView(R.id.gv_self)
    private NoScrollGridView gvSelf;
    @InjectView(R.id.ll_title)
    private LinearLayout llTitle;
    @InjectView(R.id.tv_more_title)
    private TextView tvMoreTitle;
    @InjectView(R.id.gv_title)
    private NoScrollGridView gvTitle;
    @InjectView(R.id.moretagview)
    private MoreTagView mMoreTagView;

    private PetTagGroupVo mHotTagGroup;
    private List<PetTagGroupVo> mTagGroups;
//    private List<PetTagVo> mHotTags;

    private PublishPetSayNet mNet;
    private TagGridAdapter<PetTagVo> mSelfAdapter;
    private TagGridAdapter<PetTagVo> mHotAdapter;
    private TagGridAdapter<PetTagGroupVo> mGroupAdapter;

    private PetTagVoDao mTagDao;
//    private MoreTagView mMoreTagView;
//    private MoreTagView mGroupTagView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_label);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        showLoading(false);
        mNet = new PublishPetSayNet();
        mNet.setCallback(this);
        mNet.setTag(this);
        mNet.getSayTagAll();
        initTitleBar("选择标签", true);
        initSelfTagView();
        setListener();
    }

    private void initSelfTagView() {
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.isOpen()){
            mTagDao = dbManager.getDaoSession().getPetTagVoDao();
            long count = mTagDao.queryBuilder().buildCount().count();
            if(count > 0) {
                List<PetTagVo> tagVos = mTagDao.queryBuilder().limit(4).list();
                if (tagVos != null && !tagVos.isEmpty()){
                    mSelfAdapter = new TagGridAdapter<PetTagVo>(this,false);
                    gvSelf.setAdapter(mSelfAdapter);
                    mSelfAdapter.refreshData(tagVos);
                }
                if(count > 4){
                    tvMoreSelf.setVisibility(View.VISIBLE);
                }else {
                    tvMoreSelf.setVisibility(View.GONE);
                }
            }else {
                llSelf.setVisibility(View.GONE);
            }
        }
    }

    private void setListener() {
        tvMoreHot.setOnClickListener(this);
        tvMoreSelf.setOnClickListener(this);
        gvTitle.setOnItemClickListener(this);
        gvHot.setOnItemClickListener(this);
        gvSelf.setOnItemClickListener(this);
        mMoreTagView.setCallback(this);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        TextView txt = PublicMethod.addTitleRightText(mTitleBar, "自定义");
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTagActivity.this,AddCustomActivity.class);
                startActivityForResult(intent,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1000){
            if(resultCode == RESULT_OK && data != null){
                setTag((PetTagVo) data.getSerializableExtra("tag"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mNet != null){
            mNet.cancelAll(this);
            mNet = null;
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        try {
            List<PetTagGroupVo> groups = JsonUtils.getList(bean.getValue(),PetTagGroupVo.class);
            if(groups != null && !groups.isEmpty()) {
                initData(groups);
                initHotView();
                initTagGroupView();
            }else {
                llHot.setVisibility(View.GONE);
                llTitle.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(List<PetTagGroupVo> groups){
        mTagGroups = new ArrayList<PetTagGroupVo>();
        for (int i = 0;i< groups.size();i++) {
            PetTagGroupVo groupVo = groups.get(i);
            if ("1".equals(groupVo.getId())) {
                mHotTagGroup = groupVo;
            }else {
                mTagGroups.add(groupVo);
            }
        }
    }

    private void initTagGroupView() {
        mGroupAdapter = new TagGridAdapter<PetTagGroupVo>(this,true);
        gvTitle.setAdapter(mGroupAdapter);
        mGroupAdapter.refreshData(mTagGroups);
    }

    private void initHotView() {
        List<PetTagVo> hotTags= null;
        if(mHotTagGroup.getTags().size() > 6){
            hotTags = mHotTagGroup.getTags().subList(0,6);
        }else {
            hotTags = mHotTagGroup.getTags();
            tvMoreHot.setVisibility(View.GONE);
        }
        if(!hotTags.isEmpty()){
            tvMoreHot.setVisibility(View.VISIBLE);
            mHotAdapter = new TagGridAdapter<PetTagVo>(this,false);
            gvHot.setAdapter(mHotAdapter);
            mHotAdapter.refreshData(hotTags);
        }else {
            llHot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_more_hot:
                showMoreTagView(mHotTagGroup.getTags(),"热门标签");
                break;
            case R.id.tv_more_self:
                if(DBManager.getInstance().isOpen()) {
                    showMoreTagView(mTagDao.loadAll(),"个人标签");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mMoreTagView.isShow()) {
                mMoreTagView.onHiden();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TagGridAdapter adapter = (TagGridAdapter) parent.getAdapter();
        if(adapter.isGroup()){
            PetTagGroupVo groupVo = (PetTagGroupVo) adapter.getItem(position);
            showMoreTagView(groupVo.getTags(),groupVo.getName());
        }else {
            setTag((PetTagVo) adapter.getItem(position));
        }
    }

    private void showMoreTagView(List<PetTagVo> datas,String title){
        mMoreTagView.setFocusable(true);
        mMoreTagView.showView(datas, title);
    }

    @Override
    public void onClickTagCallback(MoreTagView view, PetTagVo tagVo) {
        setTag(tagVo);
    }

    public void setTag(PetTagVo tagVo){
//        BaseEditActivity.PublishParam.setTag(tagVo);
        Intent intent = new Intent();
        intent.putExtra("tag",tagVo);
        setResult(RESULT_OK, intent);
        this.finish();
    }



}
