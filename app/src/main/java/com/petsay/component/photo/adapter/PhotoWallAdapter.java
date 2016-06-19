package com.petsay.component.photo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.component.photo.PhotoWallView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.PicDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/2
 * @Description
 */
public class PhotoWallAdapter extends ExBaseAdapter<String> {

    private DisplayImageOptions options;
    private List<String> mSelectImgs;
    private int mSize;

    public PhotoWallAdapter(Context context,Map<String,PicDTO> maps) {
        super(context);
        initValue();
        if(maps != null && !maps.isEmpty()){
            for(String key:maps.keySet()) mSelectImgs.add(key);
        }
    }

    public PhotoWallAdapter(Context context) {
        super(context);
        initValue();
    }

    public void refreshSelectImgs(List<String> list){
        this.mSelectImgs.clear();
        if(list != null)
            this.mSelectImgs.addAll(list);
        notifyDataSetChanged();
    }

    private void initValue() {
        mSelectImgs = new ArrayList<String>();
        BitmapFactory.Options options1 = new BitmapFactory.Options();
//        options1.inJustDecodeBounds = true;
        options1.inSampleSize = 10;
        options1.inPreferredConfig = Bitmap.Config.RGB_565;
        options1.inPurgeable = true;
        options1.inInputShareable = true;
        mSize = (PublicMethod.getDisplayWidth(mContext) - PublicMethod.getDiptopx(mContext,4))/3;
        options  = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoadingBackground(R.color.img_content_bg_color)
                .showImageForEmptyUri(R.drawable.pet1)
                .showImageOnFail(R.drawable.pet1)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .resetViewBeforeLoading(true) // default
                .decodingOptions(options1)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .displayer(new FadeInBitmapDisplayer(500, true, true, false))
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
//                .delayBeforeLoading(500)
                .build();
    }

    @Override
    public void refreshData(List<String> data) {
        mDatas.clear();
        mDatas.add("拍照");
        if(data != null && data.size() > 0 ){
            mDatas.addAll(data);
        }
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.photowall_item, null);
            holder = ViewHolder.create(convertView);
            ViewGroup.LayoutParams params = holder.ivThumbnail.getLayoutParams();
            params.width = mSize;
            params.height =mSize;
            holder.ivThumbnail.setLayoutParams(params);
//            holder.photoWallItemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    String item = (String) buttonView.getTag(R.id.photo_wall_item_cb);
//                    ViewHolder holder1 = (ViewHolder) buttonView.getTag();
//                    if(mContext instanceof TopicPhotoWallActivity)
//                        ((TopicPhotoWallActivity)mContext).changeSelected(holder1, item);
//                }
//            });
            holder.photoWallItemCb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            holder.photoWallItemCb.setFocusable(false);
            holder.photoWallItemCb.setClickable(false);
            holder.photoWallItemCb.setEnabled(false);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setFocusable(false);
        if(position == 0){
            holder.ivThumbnail.setColorFilter(null);
            holder.ivThumbnail.setImageBitmap(null);
            holder.ivThumbnail.setImageResource(R.drawable.photograph_bg);
            holder.photoWallItemCb.setVisibility(View.GONE);
        }else {
            String item = getItem(position);
            if(mSelectImgs.contains(item)){
                holder.ivThumbnail.setColorFilter(mContext.getResources().getColor(R.color.transparent2));
                holder.photoWallItemCb.setChecked(true);
            }else {
                holder.ivThumbnail.setColorFilter(null);
                holder.photoWallItemCb.setChecked(false);
            }
            holder.photoWallItemCb.setTag(holder);
            holder.photoWallItemCb.setTag(R.id.photo_wall_item_cb,item);
            holder.photoWallItemCb.setVisibility(View.VISIBLE);
            ImageLoaderHelp.displayImage("file://" + item, holder.ivThumbnail, options);
//            int maxw = PublicMethod.getDisplayWidth(mContext);
//            int maxh = PublicMethod.getDisplayHeight(mContext);
//            LoadSdCardImageTask task = new LoadSdCardImageTask(holder.ivThumbnail,maxw,maxh);
//            task.execute(item);
        }

        return convertView;
    }

    public static class ViewHolder {
        public final ImageView ivThumbnail;
        public final CheckBox photoWallItemCb;

        private ViewHolder(ImageView ivThumbnail, CheckBox photoWallItemCb) {
            this.ivThumbnail = ivThumbnail;
            this.photoWallItemCb = photoWallItemCb;
        }

        public static ViewHolder create(View rootView) {
            ImageView ivThumbnail = (ImageView)rootView.findViewById( R.id.iv_thumbnail );
            CheckBox photoWallItemCb = (CheckBox)rootView.findViewById( R.id.photo_wall_item_cb );
            return new ViewHolder( ivThumbnail, photoWallItemCb );
        }
    }


}
