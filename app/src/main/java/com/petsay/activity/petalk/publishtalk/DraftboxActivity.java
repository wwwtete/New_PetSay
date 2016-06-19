package com.petsay.activity.petalk.publishtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.adapter.DraftBoxAdapter;
import com.petsay.application.PublishTalkManager;
import com.petsay.component.view.BasePopupWindow;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.component.view.swipelistview.BaseSwipeListViewListener;
import com.petsay.component.view.swipelistview.SwipeListView;
import com.petsay.database.DBManager;
import com.petsay.database.greendao.petsay.DraftboxVoDao;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.DecorationPosition;
import com.petsay.vo.petalk.DraftboxVo;
import com.petsay.vo.petalk.PublishTalkParam;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * 草稿箱
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/2
 * @Description
 */
public class DraftboxActivity extends BaseActivity implements PublishTalkManager.PublishTalkManagerCallback, BasePopupWindow.IAddShowLocationViewService, View.OnClickListener {

    @InjectView(R.id.lv_draft)
    private SwipeListView mLvDraft;
    @InjectView(R.id.iv_nulltip)
    private View mNullTip;

    private DialogPopupWindow mPopupWindow;
    private DraftBoxAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draftbox);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("草稿箱", true);
        PublishTalkManager.getInstance().registerObserver(this);
        mPopupWindow = new DialogPopupWindow(this,this);
        mPopupWindow.setOnClickListener(this);
        mAdapter = new DraftBoxAdapter(this);
        mLvDraft.setAdapter(mAdapter);
        mLvDraft.setSwipeListViewListener(mSwipeListener);
        mLvDraft.setOffsetLeft(PublicMethod.getDisplayWidth(this)-PublicMethod.getDiptopx(this,80));
        refreshData();
    }

    private void refreshData() {
        DBManager dbManager  = DBManager.getInstance();
        if(dbManager.isOpen()){
            List<DraftboxVo> datas = dbManager.getDaoSession().getDraftboxVoDao()
                    .queryBuilder()
                    .orderDesc(DraftboxVoDao.Properties.CreateTime)
                    .build()
                    .list();
            mAdapter.refreshData(datas);
            if(datas.isEmpty()){
                mLvDraft.setVisibility(View.GONE);
                mNullTip.setVisibility(View.VISIBLE);
            }else {
                mLvDraft.setVisibility(View.VISIBLE);
                mNullTip.setVisibility(View.GONE);
            }
        }else {
            showToast("获取数据异常，请重试");
            mLvDraft.setVisibility(View.GONE);
            mNullTip.setVisibility(View.VISIBLE);
        }
    }


    private BaseSwipeListViewListener mSwipeListener = new BaseSwipeListViewListener(){

        @Override
        public void onListChanged()
        {
            mLvDraft.closeOpenedItems();
        }

        @Override
        public void onClickFrontView(int position){
            DraftboxVo vo = mAdapter.getItem(position);
            PublishTalkManager manager = PublishTalkManager.getInstance();
            UploadPetalkView view = manager.checkUploading(vo);
            if(view == null){
                PublishTalkParam param = PublishTalkParam.parseDraftBoxVo(vo);
                if(param != null) {
                    UploadPetalkView uploadView = new UploadPetalkView(DraftboxActivity.this);
                    uploadView.setPublishParam(param);
                    uploadView.setPetThumb(param.thumbImg);
                    if(param.editImg != null){
                        uploadView.setPhoto(param.editImg);
                    }else if(param.editFile != null){
                        uploadView.setPhotoFile(param.editFile);
                    }
                    if(param.model == 0 && param.audioFile != null){
                        uploadView.setAudioFile(param.audioFile);
                    }
                    manager.startUpload(uploadView);
                    jumpMainActivity();
                }else {
                    showToast("发布失败，请删除重试");
                }
            }else {
                if(view.getUploadStatus() == 2){
                    view.onRetryUpload();
                    jumpMainActivity();
                }else {
                    showToast("正在发布中。。");
                }
            }

        }

        @Override
        public void onClickBackView(int position){
            DraftboxVo vo = mAdapter.getItem(position);
            UploadPetalkView view = PublishTalkManager.getInstance().checkUploading(vo);
            if(vo != null && view == null) {
                showAlert(position);
            }else {
                showToast("正在上传，不能删除");
            }
        }

    };

    private void jumpMainActivity(){
        if(BaseEditActivity.Instance != null){
            BaseEditActivity.Instance.finish();
        }
        Intent intent = new Intent();
        intent.putExtra(HomeFragment.PAGEINDEX, 2);
        intent.putExtra("flag", true);
        intent.setClass(this, MainActivity.class);
        closeLoading();
        startActivity(intent);

        finish();
    }


    public boolean deleteDraftBoxItem(DraftboxVo vo){
        if(vo == null)
            return false;
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.isOpen()){
            List<DecorationPosition> positions = vo.getDecorations();

            if(positions != null && !positions.isEmpty() && positions.get(0).getId() >0) {
                dbManager.getDaoSession().getDecorationPositionDao()
                        .deleteByKey(positions.get(0).getId());
            }
            if(vo.getModel() == 0 ) {
                FileUtile.deleteFile(vo.getAudioPath());
            }
            FileUtile.deleteFile(vo.getPhotoPath());
            FileUtile.deleteFile(vo.getThumbPath());
            if(vo.getId()>0) {
                dbManager.getDaoSession().getDraftboxVoDao()
                        .deleteByKey(vo.getId());
            }
            return true;
        }
        return false;
    }

    private void showAlert(final int position){
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("提示")
//                .setMessage("是否删除该草稿")
//                .setPositiveButton("好",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        showLoading();
//                        mLvDraft.closeOpenedItems();
//                        DraftboxVo vo = mAdapter.remove(position);
//                        if(vo != null)
//                            deleteDraftBoxItem(vo);
//                        closeLoading();
//                    }
//                })
//                .setNegativeButton("取消",null)
//                .showDefault();

        mPopupWindow.setPopupText(position,"", "是否删除该草稿", "好", "取消");
        mPopupWindow.show();
    }

    @Override
    public void onDeleteLocalData(PublishTalkParam param) {
        if(mAdapter != null){
            refreshData();
        }
    }

    @Override
    protected void onDestroy() {
        PublishTalkManager.getInstance().unregisterObserver(this);
        super.onDestroy();
    }

    @Override
    public View getParentView() {
        return mLayoutRoot;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_ok:
                mPopupWindow.dismiss();
                if(mAdapter != null && !mAdapter.isEmpty()) {
                    showLoading();
                        mLvDraft.closeOpenedItems();
                        DraftboxVo vo = mAdapter.remove(mPopupWindow.mTag);
                        if(vo != null)
                            deleteDraftBoxItem(vo);
                    showToast("删除成功");
                        closeLoading();
                    if(mAdapter.isEmpty()){
                        mNullTip.setVisibility(View.VISIBLE);
                        mLvDraft.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.tv_dialog_cancle:
                mPopupWindow.dismiss();

                break;
            default:
                break;
        }
    }
}
