package com.petsay.activity.petalk.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.petalk.publishtalk.DraftboxActivity;
import com.petsay.application.PublishTalkManager;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.DraftboxVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/3
 * @Description
 */
public class DraftBoxAdapter extends ExBaseAdapter<DraftboxVo> {

    private DraftboxActivity mActivity;
    private DisplayImageOptions options;

    public DraftBoxAdapter(DraftboxActivity activity) {
        super(activity);
        this.mActivity = activity;
        initImageOptions();
    }

    public DraftboxVo remove(int position){
        if(position < mDatas.size()){
            DraftboxVo vo =  mDatas.remove(position);
            notifyDataSetChanged();
            return vo;
        }
        return null;
    }

    private void initImageOptions() {
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoadingBackground(R.color.img_content_bg_color)
                .showImageForEmptyUri(R.drawable.downloading)
                .showImageOnFail(R.drawable.downloading)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .resetViewBeforeLoading(true) // default
                .displayer(new FadeInBitmapDisplayer(500,true,true,false))
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
//                .delayBeforeLoading(500)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.draftbox_item,null);
              holder = new ViewHolder();
            holder.findViews(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DraftboxVo vo = getItem(position);
        ImageLoaderHelp.displayImage("file://"+vo.getThumbPath(),holder.ivThumbnail,options);
        if(TextUtils.isEmpty(vo.getDescription())){
            holder.tvDescription.setTextColor(Color.parseColor("#B8B8B8"));
            holder.tvDescription.setText("未添加描述");
        }else {
            holder.tvDescription.setTextColor(Color.parseColor("#747474"));
            holder.tvDescription.setText(vo.getDescription());
        }

        UploadPetalkView uploadView = PublishTalkManager.getInstance().checkUploading(vo);
        if(uploadView != null){
            if(uploadView.getUploadStatus() == 0) {
                holder.tvPublish.setBackgroundResource(R.drawable.gray_cir_bg);
                holder.tvPublish.setText("发布中");
            }else if(uploadView.getUploadStatus() == 2){
                holder.tvPublish.setBackgroundResource(R.drawable.publish_btn_bg);
                holder.tvPublish.setText("重新发布");
            }
        }else {
            holder.tvPublish.setBackgroundResource(R.drawable.publish_btn_bg);
            holder.tvPublish.setText("发布");
        }

        holder.tvTime.setText(vo.getCreateTime().toLocaleString());

        return convertView;
    }

    public class ViewHolder{
        private ImageView swipeBackview;
        private RelativeLayout swipeFrontview;
        private ImageView ivThumbnail;
        private TextView tvPublish;
        private TextView tvDescription;
        private TextView tvTime;

        public void findViews(View rootView) {
            swipeBackview = (ImageView)rootView.findViewById( R.id.swipe_backview );
            swipeFrontview = (RelativeLayout)rootView.findViewById( R.id.swipe_frontview );
            ivThumbnail = (ImageView)rootView.findViewById( R.id.iv_thumbnail );
            tvPublish = (TextView)rootView.findViewById( R.id.tv_publish );
            tvDescription = (TextView)rootView.findViewById( R.id.tv_description );
            tvTime = (TextView)rootView.findViewById( R.id.tv_time );
        }

    }

}
