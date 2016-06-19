package com.petsay.component.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.photo.adapter.PhotoAlbumAdapter;
import com.petsay.component.photo.adapter.PhotoWallAdapter;
import com.petsay.component.view.TitleBar;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.ToolsAesCrypt;
import com.petsay.vo.petalk.PhotoAlbumItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/13
 * @Description 照片库
 */
public class PhotoWallView extends RelativeLayout implements TitleBar.OnClickTitleListener, AdapterView.OnItemClickListener {

    public static final int ACTIVITYRESULT_CODE= 5000;

    private TitleBar mTitleBar;
    private GridView mGvPhotos;

    private PhotoAlbumAdapter mAlbumAdapter;
    private PhotoWallAdapter mWallAdapter;
    private TextView mTvNext;
    private PopupWindow mPopWindow;
    private List<String> mSelectImgs;
    private PhotoWallCallback mCallback;
    private File mPictureFile;
    private int mMaxLength;

    public PhotoWallView(Context context) {
        super(context);
        initView();
    }

    public PhotoWallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initPopupWindow() {
        List<PhotoAlbumItem> imgs = FileUtile.getImagePathsByContentProvider(getContext());
        mAlbumAdapter = new PhotoAlbumAdapter(getContext());
        mAlbumAdapter.refreshData(imgs);

        ListView listView = new ListView(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateView(mAlbumAdapter.getItem(position).folderPath);
                mPopWindow.dismiss();
            }
        });
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

    protected void initView() {
        inflate(getContext(), R.layout.view_photowall, this);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        mGvPhotos = (GridView) findViewById(R.id.gv_photos);

        mTitleBar.setTitleText("选择照片∧");
        mTitleBar.setFinishEnable(true);
        mGvPhotos.setCacheColorHint(0);
        mGvPhotos.setSelector(R.color.transparent);
        mTitleBar.setOnClickTitleListener(this);
        mGvPhotos.setOnItemClickListener(this);
        mWallAdapter = new PhotoWallAdapter(getContext());
        mGvPhotos.setAdapter(mWallAdapter);
        mWallAdapter.refreshData(FileUtile.getImagePathsByContentProvider(getContext(),-1));

        mSelectImgs = new ArrayList<String>();
        mTvNext = PublicMethod.addTitleRightText(mTitleBar, "下一步(" + mSelectImgs.size() + ")");
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
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

    private void onNext() {
        if(mCallback != null)
            mCallback.onNext();
    }

    public File jumpTakePictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
//        startActivityForResult(intent,500);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
        mPictureFile =FileUtile.createSysAlbumFile(UUID.randomUUID().toString()+".jpg");
        Uri fileUri = Uri.fromFile(mPictureFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        try {
            ((Activity)getContext()).startActivityForResult(intent, ACTIVITYRESULT_CODE);
        }catch (Exception e){
            ToastUtiles.showDefault(getContext(),"未找到系统相机！");
            PublicMethod.log_e("TopicPhotoWallActivity","启动系统相机失败！");
        }
        return mPictureFile;
    }

    public void changeSelected(PhotoWallAdapter.ViewHolder holder,String item){
        if (mSelectImgs.contains(item)) {
            holder.ivThumbnail.setColorFilter(null);
            holder.photoWallItemCb.setChecked(false);
            mSelectImgs.remove(item);
        } else {
            if(mSelectImgs.size() < mMaxLength) {
                holder.ivThumbnail.setColorFilter(getResources().getColor(R.color.transparent2));
                holder.photoWallItemCb.setChecked(true);
//                mSelectList.add(item);
                mSelectImgs.add(item);
            }else {
                ToastUtiles.showDefault(getContext(),"您最多只能选择"+mSelectImgs.size()+"张");
            }
        }
        mTvNext.setText("下一步(" + (mSelectImgs.size()) + ")");
    }

    public void refreshSelectImgs(List<String> imgs){
        mWallAdapter.refreshSelectImgs(imgs);
    }

    public void setMaxLength(int max){
        this.mMaxLength = max;
    }

    public void setCallback(PhotoWallCallback callback){
        this.mCallback = callback;
    }

    @Override
    public void onClickTitleListener(TextView titleView) {
        if(mPopWindow == null)
            initPopupWindow();
        showAlbumListView();
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

    public List<String> getSelectImgs(){
        return mSelectImgs;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            if(mSelectImgs.size()<mMaxLength) {
                if(mCallback != null)
                    mCallback.onClickTakePictureItem();
            }else
                ToastUtiles.showDefault(getContext(), "您最多只能选择" + mSelectImgs.size() + "张");
        }else {
            PhotoWallAdapter.ViewHolder holder = (PhotoWallAdapter.ViewHolder) view.getTag();
            String item = mWallAdapter.getItem(position);
            changeSelected(holder,item);
        }
    }

    public interface PhotoWallCallback{
        public void onNext();
        public void onClickTakePictureItem();
    }

}
