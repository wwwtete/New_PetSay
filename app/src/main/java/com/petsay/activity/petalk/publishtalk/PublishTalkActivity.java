package com.petsay.activity.petalk.publishtalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.MainActivity;
import com.petsay.application.PetSayApplication;
import com.petsay.application.PublishTalkManager;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.ExHintView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.constants.Constants;
import com.petsay.database.DBManager;
import com.petsay.database.greendao.petsay.PetTagVoDao;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.DecorationPosition;
import com.petsay.vo.petalk.DraftboxVo;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetTagVo;

import java.util.HashMap;
import java.util.UUID;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.CustomAuth;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;
import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/7
 * @Description
 */
public class PublishTalkActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, NetCallbackInterface, TitleBar.OnClickBackListener {

    @InjectView(R.id.ll_content)
    private LinearLayout llContent;
    @InjectView(R.id.iv_thumbnail)
    private ImageView ivThumbnail;
    @InjectView(R.id.ev_describe)
    private EditText evDescribe;
    @InjectView(R.id.tv_label)
    private TextView tvLabel;
    @InjectView(R.id.tv_tip)
    private TextView tvTip;
    @InjectView(R.id.ll_share)
    private LinearLayout llShare;
    @InjectView(R.id.layout_sina)
    private LinearLayout layoutSina;
    @InjectView(R.id.img_sina)
    private ImageView imgSina;
    @InjectView(R.id.layout_pengyouquan)
    private LinearLayout layoutPengyouquan;
    @InjectView(R.id.img_pengyouquan)
    private ImageView imgPengyouquan;

    private boolean isShareQzone = false, isShareWX = true, isShareSina = false, isShareTencent = false;
    private TextView mTvPublish;
    private int mMaxDescribeLenth = 50;
    private PublishTalkParam mTalkParam;
    private UploadTokenNet mTokenNet;
    private PetTagVo mTagvo;
    private PopupWindow mCancelMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_talk);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("发布宠物说", false);
        mTalkParam = BaseEditActivity.PublishParam;
        if (mTalkParam != null) {
            PetSayApplication.getInstance().platformNames.clear();
            PetSayApplication.getInstance().platformNames.add(WechatMoments.NAME);
            imgPengyouquan.setImageResource(R.drawable.pengyouquan_cli);

            ivThumbnail.setImageBitmap(mTalkParam.thumbImg);
            setListener();
        }
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        mTvPublish = PublicMethod.addTitleRightText(mTitleBar, "发布");
        mTvPublish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mTalkParam.tags.isEmpty()){
                    showToast(R.string.publish_no_tag);
                }else {
                    showLoading(false);
                    mTvPublish.setEnabled(false);
//                if (TextUtils.isEmpty(Constants.UPLOAD_TOKEN)) {
//                    if (mTokenNet == null) {
//                        mTokenNet = new UploadTokenNet();
//                        mTokenNet.setCallback(PublishTalkActivity.this);
//                    }
//                    mTokenNet.getUploadToken();
//                } else {
                    onPublishTalk();
//                }
                }
            }

        });
        mTitleBar.setOnClickBackListener(this);
    }

    private void onPublishTalk(){
        if(mTalkParam == null) {
            closeLoading();
            showToast("发布失败，请返回重试！");
            return;
        }
//        saveTage();
        onSaveDraftbox();
//        mTalkParam.description = evDescribe.getText().toString().replaceAll(" ", "").replaceAll("　", "").replaceAll("\n", "");
        onSaveImage();
        doUpload();
        closeLoading();
        jumpMainActivity();
    }

    private void onSaveImage() {
        SharePreferenceCache cache = SharePreferenceCache.getSingleton(this);
        if(cache.getIsAutoSavePicMode()){
            FileUtile.saveImageToSysAlbum(this,mTalkParam.editImg, UUID.randomUUID().toString()+".png");
        }
        if(cache.getIsAutoSaveCameraMode()){
            FileUtile.saveImageToSysAlbum(this,mTalkParam.cameraImg, UUID.randomUUID().toString()+".png");
        }
        if(cache.getIsAutoSaveCameraMode() || cache.getIsAutoSaveCameraMode()){
//            showToast();
            Toast.makeText(this,"          图片已保存到相册\n图片保存可在设置中进行修改",Toast.LENGTH_LONG).show();
        }
    }

    private void saveTage() {
        if(mTalkParam.tags != null && !mTalkParam.tags.isEmpty()){
            DBManager dbManager = DBManager.getInstance();
            if(dbManager.isOpen()){
                PetTagVoDao dao = dbManager.getDaoSession().getPetTagVoDao();
                PetTagVo tagVo = mTalkParam.tags.get(0);
                if(dao.queryBuilder().where(PetTagVoDao.Properties.Id.eq(tagVo.getId())).buildCount().count() <= 0) {
                    dao.insert(tagVo);
                }
            }
        }
    }

    private void doUpload(){
        PublishTalkParam param = BaseEditActivity.PublishParam;
        UploadPetalkView up = new UploadPetalkView(this);
        up.setAudioFile(param.audioFile);
        up.setPhoto(param.editImg.copy(Bitmap.Config.RGB_565, true));
        up.setPetThumb(param.thumbImg.copy(Bitmap.Config.RGB_565, true));
        up.setPublishParam(param);
        param.recycle();
        PublishTalkManager.getInstance().startUpload(up);
//        Constants.UPLOAD_VIEWS.addFirst(up);
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
           showCustomMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {

        layoutSina.setOnClickListener(this);
        layoutPengyouquan.setOnClickListener(this);
        tvLabel.setOnClickListener(this);
        evDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = evDescribe.getText().toString();
                int temp = mMaxDescribeLenth;
                if (!TextUtils.isEmpty(str)) {
                    temp = mMaxDescribeLenth - str.length();
                }
                tvTip.setText("剩余" + temp);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvLabel.setEnabled(true);
        if(!mTalkParam.tags.isEmpty()){
            tvLabel.setText(mTalkParam.tags.get(0).getName());
        }
        checkShowHint();
    }

    private void checkShowHint(){
        final SharePreferenceCache cache = SharePreferenceCache.getSingleton(this);
        boolean isShow = cache.getSharedPreferences().getBoolean("addtag_hint", true);
        if(isShow) {
            ExHintView ch =  (ExHintView) findViewById(R.id.hint_addtag);
            ch.show(true);
            cache.getSharedPreferences().edit()
                    .putBoolean("addtag_hint",false)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_sina:
                if (ShareSDK.getPlatform(SinaWeibo.NAME).isValid()) {
                    if (isShareSina) {
                        isShareSina = false;
                        imgSina.setImageResource(R.drawable.sinamicro_blog);
                        PetSayApplication.getInstance().platformNames.remove(SinaWeibo.NAME);
                    } else {
                        isShareSina = true;
                        imgSina.setImageResource(R.drawable.sinamicro_blog_cli);
                        if (!PetSayApplication.getInstance().platformNames.contains(SinaWeibo.NAME)) {
                            PetSayApplication.getInstance().platformNames.add(SinaWeibo.NAME);
                        }
                    }
                } else {
                    CustomAuth.addAuth(this, SinaWeibo.NAME);
                }
                break;
            case R.id.layout_pengyouquan:
                if (isShareWX) {
                    isShareWX = false;
                    imgPengyouquan.setImageResource(R.drawable.pengyouquan_uncli);
                    PetSayApplication.getInstance().platformNames.remove(WechatMoments.NAME);
                } else {
                    isShareWX = true;
                    imgPengyouquan.setImageResource(R.drawable.pengyouquan_cli);
                    if (!PetSayApplication.getInstance().platformNames.contains(WechatMoments.NAME)) {
                        PetSayApplication.getInstance().platformNames.add(WechatMoments.NAME);
                    }
                    break;
                }
                break;

            case R.id.tv_label:
                Intent intent = new Intent(this,SelectTagActivity.class);
                startActivityForResult(intent,8000);
                break;
            case R.id.tv_logout:
                hidenCustomMenu();
                if(BaseEditActivity.Instance != null){
                    BaseEditActivity.Instance.finish();
                }
                this.finish();
                break;
            case R.id.tv_editmouth:
                hidenCustomMenu();
                this.finish();
                break;
            case R.id.tv_draftbox:
                hidenCustomMenu();
                showLoading(false);
                onSaveDraftbox();
                closeLoading();
                if (BaseEditActivity.Instance != null) {
                    BaseEditActivity.Instance.finish();
                }
                finish();
                break;
            case R.id.btn_cancel:
                hidenCustomMenu();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 8000){
            if(resultCode == RESULT_OK && data != null){
                PetTagVo tag = (PetTagVo) data.getSerializableExtra("tag");
                mTalkParam.setTag(tag);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSaveDraftbox() {

        saveTage();
        mTalkParam.description = evDescribe.getText().toString().replaceAll(" ", "").replaceAll("　", "").replaceAll("\n", "");
        DraftboxVo draftboxVo = DraftboxVo.parsePublishTalkParam(this,mTalkParam);
        if(draftboxVo == null){
            closeLoading();
            showToast("保存失败！请重试");
            return;
        }
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.isOpen()) {
            long rowID = dbManager.getDaoSession().getDraftboxVoDao().insert(draftboxVo);
            if(rowID >= 0){
//                DraftboxVo temp = dbManager.getDaoSession().getDraftboxVoDao().load(rowID);
                mTalkParam.id = draftboxVo.getId();
                if (draftboxVo.getModel() == 0 && !mTalkParam.decorations.isEmpty() && draftboxVo != null) {
                    PublishTalkParam.Position pp = mTalkParam.decorations.get(0);
                    DecorationPosition position = DecorationPosition.parsePublishTalkPosition(draftboxVo, pp);
                    long pr = dbManager.getDaoSession().getDecorationPositionDao().insert(position);
                    pp.id = pr;
                }
            }
        }else {
            closeLoading();
            showToast("保存失败！请重试");
            return;
        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        if (arg0.getName().equals(SinaWeibo.NAME)) {

        } else if (arg0.getName().equals(QZone.NAME)) {

        } else if (arg0.getName().equals(TencentWeibo.NAME)) {

        }

    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        if (arg0.getName().equals(SinaWeibo.NAME)) {
            isShareSina = true;
            imgSina.setImageResource(R.drawable.sinamicro_blog_cli);
            PetSayApplication.getInstance().platformNames.add(SinaWeibo.NAME);
        } else if (arg0.getName().equals(QZone.NAME)) {

        } else if (arg0.getName().equals(TencentWeibo.NAME)) {

        }

    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        if (arg0.getName().equals(SinaWeibo.NAME)) {

        } else if (arg0.getName().equals(QZone.NAME)) {

        } else if (arg0.getName().equals(TencentWeibo.NAME)) {

        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        Constants.UPLOAD_TOKEN = bean.getValue();
        onPublishTalk();
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        mTvPublish.setEnabled(true);
        showToast("发布失败，请检查网络是否可用！");
    }

    public void hidenCustomMenu(){
        if(mCancelMenu != null)
            mCancelMenu.dismiss();
    }

    public void showCustomMenu(){
        hidenCustomMenu();
        View view = LayoutInflater.from(this).inflate(R.layout.menu_publish_talk_cancel, null);
        view.findViewById(R.id.tv_editmouth).setOnClickListener(this);
        view.findViewById(R.id.tv_logout).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_draftbox).setOnClickListener(this);
        mCancelMenu = new PopupWindow(view,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCancelMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        mCancelMenu.setFocusable(true);
        mCancelMenu.setBackgroundDrawable(new BitmapDrawable());
        mCancelMenu.setAnimationStyle(R.anim.bottom_in);
        mCancelMenu.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void OnClickBackListener() {
        PublicMethod.closeSoftKeyBoard(this,evDescribe);
        showCustomMenu();
    }
}