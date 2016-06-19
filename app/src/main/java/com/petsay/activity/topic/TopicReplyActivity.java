package com.petsay.activity.topic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.application.PublishTopicReplyManager;
import com.petsay.application.UploadTokenManager;
import com.petsay.component.view.ImageSuperscriptView;
import com.petsay.component.view.UploadTopicReplyView;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.CreateTopicParams;
import com.petsay.vo.forum.PicDTO;

import java.util.Map;
import java.util.Set;

import roboguice.inject.InjectView;

/**
 * 回复主题评论
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/31
 * @Description
 */
public class TopicReplyActivity extends BaseActivity implements NetCallbackInterface {

    @InjectView(R.id.ev_content)
    private TextView mEvContent;
    @InjectView(R.id.ll_imgs)
    private LinearLayout llImgs;
    private TextView mTvPublish;
//    @InjectView(R.id.iv_pic01)
//    private ImageView ivPic01;
//    @InjectView(R.id.iv_pic02)
//    private ImageView ivPic02;
//    @InjectView(R.id.iv_pic03)
//    private ImageView ivPic03;

    private TopicNet mNet;
    private CreateTopicParams mParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_reply);
        mParam = getIntent().getParcelableExtra("param");
        if(mParam == null){
            mParam = new CreateTopicParams();
            mParam.topicId = getIntent().getStringExtra("topicid");
        }
        mNet = new TopicNet();
        mNet.setCallback(this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("编辑互动",true);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        mTvPublish = PublicMethod.addTitleRightText(mTitleBar,"发布");
        mTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPublish();
            }
        });
    }

    private void onPublish() {
        mParam.content = mEvContent.getText().toString().trim();
        if(TextUtils.isEmpty(mParam.content)) {
            showToast("请输入评论内容");
            return;
        }
        mTvPublish.setEnabled(false);
        showLoading();
        mParam.petId = getActivePetId();
        if(mParam.selectPicMap.isEmpty()){
//            mNet.topicCreateTalk(mParam.topicId, mParam.petId, mParam.content, "[]", null);
            mNet.topicCreateTalk(mParam,null);
        }else {
            UploadTokenManager manager = UploadTokenManager.getInstance();
            if(!manager.checkvalid()){
                manager.registerObserver(new UploadTokenManager.UploadTokenManagerCallback() {
                    @Override
                    public void onGetTokenSuccess(String token) {
                        onUploadPics();
                    }

                    @Override
                    public void onGetTokenError(PetSayError error) {
                        mTvPublish.setEnabled(true);
                        closeLoading();
                        showToast("获取Token失败！请重试");
                    }
                });
                manager.loadToken();
            }else {
                onUploadPics();
            }
        }
    }

    /**
     * 上传图片
     */
    private void onUploadPics() {
//        Set<Map.Entry<String,Bitmap>> sets = mParam.selectPicMap.entrySet();
        UploadTopicReplyView view = new UploadTopicReplyView(this);
//        int mw = PublicMethod.getDisplayHeight(this);
//        for(Map.Entry<String,Bitmap> entry:sets){
//            if(entry.getValue() == null){
//               Bitmap bmp =  FileUtile.loadImageBySdCard(10,mw,mw,entry.getKey());
////                Bitmap bmp = BitmapFactory.decodeFile(entry.getKey());
//                mParam.selectPicMap.put(entry.getKey(),bmp);
//            }
//        }
        view.startUpload(mParam);
        PublishTopicReplyManager.getInstance().addUploadView(view,mParam.topicId);
        closeLoading();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 500 && resultCode == RESULT_OK){
            try {
                mParam = data.getParcelableExtra("param");
            }catch (Exception e){
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePicList();
    }

    private void updatePicList() {
        if(mParam == null)
            mParam = new CreateTopicParams();
        llImgs.removeAllViews();
        Map<String,PicDTO> picMap = mParam.selectPicMap;

        int space = PublicMethod.getDiptopx(this,10);
        int size = (PublicMethod.getDisplayWidth(this) - space * 4)/3;
        if(!picMap.isEmpty()){
            Set<Map.Entry<String,PicDTO>> sets=picMap.entrySet();
            for (Map.Entry<String,PicDTO> entry : sets){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
                params.setMargins(space,0,0,0);
                llImgs.addView(getView(entry),params);
            }
        }
        if(picMap.size() < 3){
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
            params.setMargins(space,0,0,0);
            img.setImageResource(R.drawable.topic_add_pic);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpPhotoWallActivity();
                }
            });
            llImgs.addView(img,params);
        }
    }

    private View getView(final Map.Entry<String,PicDTO> entry){
        ImageSuperscriptView view = new ImageSuperscriptView(this);
        view.setImageContentURL("file://"+entry.getKey());
        view.setTopRightMarkResId(R.drawable.loop_img_close);
        view.setTag(entry);
        view.setCallback(new ImageSuperscriptView.ImageSuperscriptCallback() {
            @Override
            public void onClickSuperscriptView(ImageSuperscriptView view, ImageView markView) {
                Map.Entry<String,Bitmap> entry1 = (Map.Entry<String, Bitmap>) view.getTag();
                mParam.selectPicMap.remove(entry1.getKey());
                updatePicList();
            }
        });
        return view;
    }

    private void jumpPhotoWallActivity() {
        Intent intent = new Intent();
        intent.setClass(this, TopicPhotoWallActivity.class);
        intent.putExtra("param", mParam);
        startActivityForResult(intent, 500);
    }

    /**
     * 获取数据成功回调接口
     *
     * @param bean        服务器返回数据
     * @param requestCode 区分请求码
     */
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 获取数据失败回调接口(也包括服务器返回500的错误)
     *
     * @param error       错误信息类
     * @param requestCode 请求码
     */
    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        mTvPublish.setEnabled(true);
        onErrorShowToast(error);
    }
}
