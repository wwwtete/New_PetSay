package com.petsay.activity.story;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.publishtalk.SelectTagActivity;
import com.petsay.application.PetSayApplication;
import com.petsay.application.PublishStoryManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetTagVo;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * 设置故事封面
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/31
 * @Description
 */
public class StorySetCoverActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, PlatformActionListener {

    public static Bitmap storyCover;

    //    @InjectView(R.id.clip)
//    private CropImageLayout mClip;
    @InjectView(R.id.sv_cover)
    private ScrollView mSvCover;
    @InjectView(R.id.iv_cover)
    private ImageView mIvCover;
    @InjectView(R.id.ll_share)
    private LinearLayout mLlShare;
    @InjectView(R.id.layout_sina)
    private LinearLayout mLayoutSina;
    @InjectView(R.id.img_sina)
    private ImageView mImgSina;
    @InjectView(R.id.layout_pengyouquan)
    private LinearLayout mLayoutPengyouquan;
    @InjectView(R.id.img_pengyouquan)
    private ImageView mImgPengyouquan;
    @InjectView(R.id.gv_items)
    private GridView mGridView;
    @InjectView(R.id.tv_tag)
    private TextView mTvTag;
    @InjectView(R.id.iv_preview)
    private ImageView mIvPreview;

    private StoryParams mParams;
    private CoverAdapter mAdapter;
    private int mCoverWidth=0,mCoverHeight=0;
    private boolean isShareWX = true, isShareSina = false;
    private TextView mTvPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_setcover);
        mParams = (StoryParams) getIntent().getSerializableExtra("params");
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("设置故事封面", true);
        mAdapter = new CoverAdapter(this);
        List<StoryImageItem> items = new ArrayList<StoryImageItem>();
        String url = "";
        for(int i=0;i<mParams.items.size();i++){
            if(mParams.items.get(i) instanceof StoryImageItem) {
                if(TextUtils.isEmpty(url))
                    url = ((StoryImageItem) mParams.items.get(i)).getImageUrl();
                items.add((StoryImageItem) mParams.items.get(i));
            }
        }
        mAdapter.refreshData(items);
        mGridView.setAdapter(mAdapter);
        ImageLoaderHelp.displayContentImage("file://"+url,mIvCover);
        initClipView();
        initShare();
        setListener();
    }

    private void initClipView() {
        mCoverWidth = PublicMethod.getDisplayWidth(this) - PublicMethod.getDiptopx(this,10)*2;
        mCoverHeight = mCoverWidth/7*4;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSvCover.getLayoutParams();
        params.height = mCoverHeight;
        params.width = mCoverWidth;
        mSvCover.setLayoutParams(params);

    }

    private void initShare() {
        PetSayApplication.getInstance().platformNames.clear();
        PetSayApplication.getInstance().platformNames.add(WechatMoments.NAME);
        mImgPengyouquan.setImageResource(R.drawable.pengyouquan_cli);
    }

    private void setListener() {
        mGridView.setOnItemClickListener(this);
        mIvPreview.setOnClickListener(this);
        mTvTag.setOnClickListener(this);
        mLayoutPengyouquan.setOnClickListener(this);
        mLayoutSina.setOnClickListener(this);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        mTvPublish = PublicMethod.addTitleRightText(mTitleBar,"发布");
        mTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParams.tags.isEmpty()){
                    showToast(R.string.publish_no_tag);
                }else {
                    mTvPublish.setEnabled(false);
                    onPublishStory();
                }
            }
        });
    }

    private void onPublishStory() {
        onClipPic();
        UploadStoryView view =new UploadStoryView(this,mParams,StorySetCoverActivity.storyCover);
        PublishStoryManager.getInstance().startUpload(view);

        Intent intent = new Intent();
        intent.putExtra(HomeFragment.PAGEINDEX, 2);
        intent.putExtra("flag", true);
        intent.setClass(StorySetCoverActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        if(EditBeginStoryActivity.instance != null)
            EditBeginStoryActivity.instance.finish();
        if(EditStoryActivity.instance != null)
            EditStoryActivity.instance.finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoryItemVo itemVo = (StoryItemVo) mAdapter.getItem(position);
        if(itemVo instanceof StoryImageItem){
            ImageLoaderHelp.displayContentImage("file://" + ((StoryImageItem)itemVo).getImageUrl(), mIvCover);
//            mClip.setImagePath(mCurrItem.getImageUrl());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_preview:
                onClipPic();
                Intent intent = new Intent(this,StoryPreviewActivity.class);
                intent.putExtra("params",mParams);
                startActivity(intent);
                break;
            case R.id.tv_tag:
                Intent intent2 = new Intent(this,SelectTagActivity.class);
                startActivityForResult(intent2,8000);
                break;
            case R.id.layout_sina:
                if (ShareSDK.getPlatform(SinaWeibo.NAME).isValid()) {
                    if (isShareSina) {
                        isShareSina = false;
                        mImgSina.setImageResource(R.drawable.sinamicro_blog);
                        PetSayApplication.getInstance().platformNames.remove(SinaWeibo.NAME);
                    } else {
                        isShareSina = true;
                        mImgSina.setImageResource(R.drawable.sinamicro_blog_cli);
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
                    mImgPengyouquan.setImageResource(R.drawable.pengyouquan_uncli);
                    PetSayApplication.getInstance().platformNames.remove(WechatMoments.NAME);
                } else {
                    isShareWX = true;
                    mImgPengyouquan.setImageResource(R.drawable.pengyouquan_cli);
                    if (!PetSayApplication.getInstance().platformNames.contains(WechatMoments.NAME)) {
                        PetSayApplication.getInstance().platformNames.add(WechatMoments.NAME);
                    }
                    break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 8000){
            if(resultCode == RESULT_OK && data != null){
                PetTagVo tag = (PetTagVo) data.getSerializableExtra("tag");
                mParams.setTag(tag);
                mTvTag.setText(tag.getName());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(SinaWeibo.NAME)) {
            isShareSina = true;
            mImgSina.setImageResource(R.drawable.sinamicro_blog_cli);
            PetSayApplication.getInstance().platformNames.add(SinaWeibo.NAME);
        } else if (platform.getName().equals(QZone.NAME)) {

        } else if (platform.getName().equals(TencentWeibo.NAME)) {

        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (platform.getName().equals(SinaWeibo.NAME)) {

        } else if (platform.getName().equals(QZone.NAME)) {

        } else if (platform.getName().equals(TencentWeibo.NAME)) {

        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    private void onClipPic() {
        if(storyCover != null) {
            storyCover.recycle();
            storyCover = null;
        }
//        storyCover = Bitmap.createBitmap(mCoverWidth,mCoverHeight,Bitmap.Config.RGB_565);
//        mSvCover.draw(new Canvas(storyCover););
        mSvCover.setDrawingCacheEnabled(true);
        mSvCover.buildDrawingCache();
        storyCover = Bitmap.createBitmap(mSvCover.getDrawingCache());
        mSvCover.setDrawingCacheEnabled(false);
    }

    class CoverAdapter extends ExBaseAdapter<StoryImageItem>{

        public CoverAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.story_image_item, null);
            }
            StoryImageItem item = getItem(position);
            ImageView iv_Mark = (ImageView)convertView.findViewById( R.id.iv_mark );
            ImageView ivContent = (ImageView)convertView.findViewById( R.id.iv_content );
            TextView tv_describe = (TextView)convertView.findViewById( R.id.tv_describe );
            ImageLoaderHelp.displayContentImage("file://"+item.getImageUrl(),ivContent);
            if(!TextUtils.isEmpty(item.getAudioUrl()))
                iv_Mark.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(item.getDescribe())) {
                tv_describe.setText(item.getDescribe());
                tv_describe.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(item.getAudioUrl())){
                iv_Mark.setVisibility(View.VISIBLE);
            }
            return convertView;
//            return super.getView(position, convertView, parent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
