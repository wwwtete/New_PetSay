package com.petsay.activity.topic;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.photo.adapter.PhotoAlbumAdapter;
import com.petsay.component.photo.adapter.PhotoWallAdapter;
import com.petsay.component.view.TitleBar;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PhotoAlbumItem;
import com.petsay.vo.forum.CreateTopicParams;
import com.petsay.vo.forum.PicDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import roboguice.inject.InjectView;

/**
 * 相册列表
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/1
 * @Description
 */
public class TopicPhotoWallActivity extends BaseActivity implements TitleBar.OnClickTitleListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.gv_photos)
    private GridView mGvPhotos;

    private PhotoAlbumAdapter mAlbumAdapter;
    private PhotoWallAdapter mWallAdapter;
    private PopupWindow mPopWindow;
    private TextView mTvNext;

    private Map<String,PicDTO> mMaps;
    private CreateTopicParams mParam;
    /**从那个页面跳转进来的：0：直接跳转，1：编辑界面跳转*/
    private int mFromJumpType = 0;
    private File mPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photowall);
        mParam = getIntent().getParcelableExtra("param");
        if(mParam == null) {
            mFromJumpType = 1;
            mParam = new CreateTopicParams();
            mParam.topicId = getIntent().getStringExtra("topicid");
        }
        mMaps = mParam.selectPicMap;
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("选择照片∧", true);
        mGvPhotos.setCacheColorHint(0);
        mGvPhotos.setSelector(R.color.transparent);
        mTitleBar.setOnClickTitleListener(this);
        mGvPhotos.setOnItemClickListener(this);
        mWallAdapter = new PhotoWallAdapter(this,mMaps);
        mGvPhotos.setAdapter(mWallAdapter);
        mWallAdapter.refreshData(FileUtile.getImagePathsByContentProvider(this,-1));
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        mTvNext = PublicMethod.addTitleRightText(mTitleBar,"下一步("+mMaps.size()+")");
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTopicReplyActivity();
            }
        });
    }

    private void jumpTopicReplyActivity() {
        if(mFromJumpType == 1){
            showLoading();
            Intent intent = new Intent(TopicPhotoWallActivity.this, TopicReplyActivity.class);
            intent.putExtra("param",mParam);
            closeLoading();
            startActivity(intent);
            finish();
        }else {
            finish();
        }

    }

    @Override
    public void finish() {
        if(mFromJumpType != -1){
            Intent intent = new Intent();
            intent.putExtra("param",mParam);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    private void showAlbumListView() {
        if(!mPopWindow.isShowing()){
            mPopWindow.setFocusable(true);
            mPopWindow.showAsDropDown(mTitleBar);
            mTitleBar.setTitleText("选择照片∧");
        }else {
            mPopWindow.dismiss();
            mTitleBar.setTitleText("选择照片∨");
        }
    }

    public void initPopupWindow() {
        List<PhotoAlbumItem> imgs = FileUtile.getImagePathsByContentProvider(this);
        mAlbumAdapter = new PhotoAlbumAdapter(this);
        mAlbumAdapter.refreshData(imgs);

        ListView listView = new ListView(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(mAlbumAdapter);
        mPopWindow = new PopupWindow(listView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTitleBar.setTitleText("选择照片∨");
                mTitleBar.setFocusable(false);
            }
        });
        mPopWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        mPopWindow.setOutsideTouchable(true);
    }

    private void updateView(String folderPath) {
        mWallAdapter.refreshData(getAllImagePathsByFolder(folderPath));
    }


    /**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (FileUtile.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }

    @Override
    public void onClickTitleListener(TextView titleView) {
        if(mPopWindow == null)
            initPopupWindow();
        showAlbumListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent instanceof ListView) {
            updateView(mAlbumAdapter.getItem(position).folderPath);
            mPopWindow.dismiss();
        }else {
            if(position == 0){
                if(mMaps.size()<3)
                    jumpTakePictureActivity();
                else
                    showToast(R.string.select_pic_tip);
            }else {
                PhotoWallAdapter.ViewHolder holder = (PhotoWallAdapter.ViewHolder) view.getTag();
                String item = mWallAdapter.getItem(position);
                changeSelected(holder,item);
            }
        }
    }

    public void changeSelected(PhotoWallAdapter.ViewHolder holder,String item){
        if (mMaps.containsKey(item)) {
            holder.ivThumbnail.setColorFilter(null);
            holder.photoWallItemCb.setChecked(false);
            mMaps.remove(item);
        } else {
            if(mMaps.size() < 3) {
                holder.ivThumbnail.setColorFilter(getResources().getColor(R.color.transparent2));
                holder.photoWallItemCb.setChecked(true);
//                mSelectList.add(item);
                mMaps.put(item,null);
            }else {
                showToast(R.string.select_pic_tip);
            }
        }
        mTvNext.setText("下一步(" + (mMaps.size()) + ")");
    }

    private void jumpTakePictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
//        startActivityForResult(intent,500);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
        mPictureFile =FileUtile.createSysAlbumFile(UUID.randomUUID().toString()+".jpg");
        Uri fileUri = Uri.fromFile(mPictureFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        try {
            startActivityForResult(intent, 500);
        }catch (Exception e){
            showToast("未找到系统相机！");
            PublicMethod.log_e("TopicPhotoWallActivity","启动系统相机失败！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 500 && resultCode == RESULT_OK) {
            if(mPictureFile != null && mPictureFile.exists()){
                mParam.selectPicMap.put(mPictureFile.getAbsolutePath(),null);
                insertSysAlbum(mPictureFile);
                jumpTopicReplyActivity();
            }else {
                showToast("拍照失败，请返回后重试");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertSysAlbum(File file){
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),"petsay");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
