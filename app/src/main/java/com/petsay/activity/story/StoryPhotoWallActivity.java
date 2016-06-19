package com.petsay.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.petsay.activity.BaseActivity;
import com.petsay.component.photo.PhotoWallView;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/13
 * @Description
 */
public class StoryPhotoWallActivity extends BaseActivity implements PhotoWallView.PhotoWallCallback {

    private PhotoWallView mWallView;
    private StoryParams mParams;
    private File mPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallView = new PhotoWallView(this);
        mParams = (StoryParams) getIntent().getSerializableExtra("params");
        if(mParams == null){
            mParams = new StoryParams();
        }
        setContentView(mWallView);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mWallView.setMaxLength(20);
//        List<String> pics =null;
//        if(!mParams.items.isEmpty()) {
//            pics = new ArrayList<>();
//            for (StoryItemVo vo:mParams.items){
//                if(vo.getType() == StoryItemVo.TYPE_IMAGE)
//                    pics.add(((StoryImageItem)vo).getImageUrl());
//            }
//        }
//        if(pics != null)
//            mWallView.refreshSelectImgs(pics);
        mWallView.setCallback(this);
    }

    @Override
    public void onNext() {
        jumpEditStoryActivity();
    }

    private void jumpEditStoryActivity(){
        List<String> pics = mWallView.getSelectImgs();
//        List<StoryItemVo> items = new ArrayList<>(mParams.items.size());
//        items.addAll(mParams.items);
        //判断用户是否未选择照片
//        if((pics == null || pics.isEmpty()) && !items.isEmpty()){
//            for (StoryItemVo item : items){
//                if(item.getType() == StoryItemVo.TYPE_IMAGE)
//                    mParams.items.remove(item);
//            }
//        }else if(pics != null && !pics.isEmpty()) {
//            boolean flag = true;
//            for (String pic:pics){
//               if(items.isEmpty()){
//                   mParams.items.add(new StoryImageItem(StoryItemVo.TYPE_IMAGE,pic));
//               }else {
//                   flag = true;
//                   for (StoryItemVo vo : items){
//                       if(vo.getType() == StoryItemVo.TYPE_IMAGE && pic.equals(((StoryImageItem)vo).getImageUrl())){
//                           flag = false;
//                           break;
//                       }
//                   }
//                   if(flag){
//                       mParams.items.add(new StoryImageItem(StoryItemVo.TYPE_IMAGE,pic));
//                   }
//               }
//            }
//        }

        for (String pic:pics){
            mParams.items.add(new StoryImageItem(pic));
        }


        Intent intent = new Intent(this,EditStoryActivity.class);
        intent.putExtra("params",mParams);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickTakePictureItem() {
        mPictureFile = mWallView.jumpTakePictureActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PhotoWallView.ACTIVITYRESULT_CODE && resultCode == RESULT_OK) {
            if(mPictureFile != null && mPictureFile.exists()){
                mParams.items.add(new StoryImageItem(mPictureFile.getAbsolutePath()));
                insertSysAlbum(mPictureFile);
                jumpEditStoryActivity();
            }else {
                showToast("拍照失败，请返回后重试");
            }
        }
    }

    private void insertSysAlbum(File file){
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),"petsay");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
