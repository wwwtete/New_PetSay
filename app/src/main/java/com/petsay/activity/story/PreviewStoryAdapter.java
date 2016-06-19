package com.petsay.activity.story;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryAddressItem;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryTextItem;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/3
 * @Description
 */
public class PreviewStoryAdapter extends RecyclerView.Adapter<PreviewStoryAdapter.ViewHolder> {

    private List<StoryItemVo> mDatas;
    private Context mContext;
    private int mHeight;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;
    private DisplayImageOptions mOptions;
    private String mCreateTime;
    private String mTitle;
    private Object mCover;

    public PreviewStoryAdapter(List<StoryItemVo> mDatas,String createTime,String title,Object cover,int height, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.mHeight = height;
        this.mCreateTime = createTime;
        this.mTitle = title;
        this.mCover = cover;
        mInflater = LayoutInflater.from(mContext);
        initImageLoaderOption();
    }

    private void initImageLoaderOption() {
        mOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoadingBackground(R.drawable.pet1)
                .showImageForEmptyUri(R.drawable.pet1)
                .showImageOnFail(R.drawable.pet1)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .resetViewBeforeLoading(true) // default
                .displayer(new FadeInBitmapDisplayer(500,true,true,false))
//				.displayer(new RoundedBitmapDisplayer(1000))
//				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
                .delayBeforeLoading(300)
                .build();
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.item_story_preview,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
    	
        if(i == 0){
            onInitImageItem(null, viewHolder, i);
        }else if(i == getItemCount()-1){
            onInitImageItem(null, viewHolder, i);
        }else {
            StoryItemVo itemVo = mDatas.get(i - 1);
            switch (itemVo.getType()){
                case StoryItemVo.TYPE_IMAGE:
                    onInitImageItem((StoryImageItem) itemVo,viewHolder, i);
                    break;
                case StoryItemVo.TYPE_ADDRESS_TIME:
                    onInitAddressItem((StoryAddressItem) itemVo,viewHolder,i);
                    break;
                case StoryItemVo.TYPE_TEXT:
                    onInitTextItem((StoryTextItem) itemVo,viewHolder, i);
                    break;
            }
        }
    }

    private void onInitImageItem(StoryImageItem item,ViewHolder viewHolder, final int i) {
        viewHolder.imgView.setVisibility(View.VISIBLE);
        viewHolder.txtView.setVisibility(View.GONE);
        viewHolder.addressView.setVisibility(View.GONE);
        viewHolder.ivMark.setVisibility(View.GONE);
        viewHolder.tvDescribe.setVisibility(View.GONE);
        viewHolder.tvCreateTime.setVisibility(View.GONE);
        viewHolder.tvTitle.setVisibility(View.GONE);
        viewHolder.ivCover.setVisibility(View.GONE);
        viewHolder.imgView.setOnClickListener(null);
        viewHolder.imgView.setLayoutParams(viewHolder.imgParams);
        viewHolder.ivContent.setImageResource(R.drawable.pet1);
        if(i == 0){
                initCover(viewHolder);
        }else if(i == getItemCount()-1){
            viewHolder.ivContent.setImageResource(R.drawable.story_backcover);
        }else {
            if(!TextUtils.isEmpty(item.getAudioUrl())) {
                viewHolder.ivMark.setVisibility(View.VISIBLE);
//                if(viewHolder.mDrawable != null){
              	  // 动画是否正在运行  
                	viewHolder.mDrawable.stop();  
//      		}
                viewHolder.imgView.setTag(item);
                viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null){
                            mListener.onItemClick(v,i);
                        }
                    }
                });
            }
            if(!TextUtils.isEmpty(item.getDescribe())){
                viewHolder.tvDescribe.setText(item.getDescribe());
                viewHolder.tvDescribe.setVisibility(View.VISIBLE);
            }
            String url = item.getImageUrl();
            if(!TextUtils.isEmpty(url) && !url.substring(0,10).contains("http://")){
                url = "file://" + url;
            }
            ImageLoaderHelp.displayImage(url, viewHolder.ivContent, mOptions);
        }
    }

    private void initCover(ViewHolder viewHolder) {
        if(mCover != null) {
            viewHolder.ivContent.setImageResource(R.drawable.story_cover_blank);
            if(mCover instanceof Bitmap)
                viewHolder.ivCover.setImageBitmap((Bitmap) mCover);
            else
                ImageLoaderHelp.displayImage((String) mCover,viewHolder.ivCover, mOptions);
            viewHolder.ivCover.setVisibility(View.VISIBLE);
            viewHolder.tvCreateTime.setText(mCreateTime);
            viewHolder.tvTitle.setText(mTitle);
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvCreateTime.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ivContent.setImageResource(R.drawable.story_cover);
        }
    }

    private void onInitAddressItem(StoryAddressItem item,ViewHolder viewHolder, int i) {
        viewHolder.addressView.setVisibility(View.VISIBLE);
        viewHolder.imgView.setVisibility(View.GONE);
        viewHolder.txtView.setVisibility(View.GONE);
        viewHolder.tvAddress.setText(item.getAddress());
        viewHolder.tvTime.setText(item.getTime());

    }

    private void onInitTextItem(StoryTextItem item,ViewHolder viewHolder, int i) {
        viewHolder.txtView.setVisibility(View.VISIBLE);
        viewHolder.imgView.setVisibility(View.GONE);
        viewHolder.addressView.setVisibility(View.GONE);
        viewHolder.tvContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View addressView;
        public TextView tvTime;
        public TextView tvAddress;

        public View txtView;
        public TextView tvContent;

        public View imgView;
        public ImageView ivContent;
        public ImageView ivMark;
        public AnimationDrawable mDrawable;
        public TextView tvDescribe;
        public RelativeLayout.LayoutParams imgParams;
        public ImageView ivCover;
        public TextView tvCreateTime;
        public TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            bindViews(itemView);
            

        }

        private void bindViews(View itemView) {
            addressView = itemView.findViewById(R.id.layout_address);
            RelativeLayout.LayoutParams ap = (RelativeLayout.LayoutParams) addressView.getLayoutParams();
            ap.height = mHeight;
            ap.width = mHeight;
//                addressView.setLayoutParams(ap);
            tvTime = (TextView)itemView.findViewById( R.id.tv_time );
            tvAddress = (TextView)itemView.findViewById( R.id.tv_address );

            txtView = itemView.findViewById(R.id.layout_text);
            RelativeLayout.LayoutParams tp = (RelativeLayout.LayoutParams) txtView.getLayoutParams();
            tp.height = mHeight;
            tp.width = mHeight;
            txtView.setLayoutParams(tp);
            tvContent = (TextView)itemView.findViewById( R.id.tv_content );
            int padding = PublicMethod.getDiptopx(mContext, 30);
            tvContent.setPadding(padding,padding,padding,padding);

            imgView = itemView.findViewById( R.id.rl_img );
            imgParams = new RelativeLayout.LayoutParams( imgView.getLayoutParams());

            initImageView(itemView);
        }

        private void initImageView(View itemView) {
            ivContent = (ImageView)itemView.findViewById(R.id.iv_content);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivContent.getLayoutParams();
//                params.height = mHeight;
//                ivContent.setLayoutParams(params);
            ivMark = (ImageView)itemView.findViewById( R.id.iv_mark );
            ivMark.setBackgroundResource(R.anim.playaudio_animation_story);
            mDrawable=(AnimationDrawable) ivMark.getBackground();
            mDrawable.stop();
          
            tvDescribe = (TextView)itemView.findViewById( R.id.tv_describe );
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            PublicMethod.initStoryCoverViewLayout(tvTitle,tvCreateTime,ivCover,mHeight);
//            RelativeLayout.LayoutParams icP = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
//            icP.leftMargin = (int) (40.0f/640.0f*mHeight);
//            icP.topMargin = (int) (224.0f/640.0f*mHeight);
//            icP.width = (int) (560.0f/640.0f*mHeight);
//            icP.height = (int) (320.0f/640.0f*mHeight);
//            ivCover.setLayoutParams(icP);
//            RelativeLayout.LayoutParams ttP = (RelativeLayout.LayoutParams) tvCreateTime.getLayoutParams();
//            ttP.leftMargin = icP.leftMargin;
//            ttP.topMargin = (int) (164.0f/640.0f*mHeight);
//            tvCreateTime.setLayoutParams(ttP);
//            RelativeLayout.LayoutParams tvtP = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
//            tvtP.leftMargin = ttP.leftMargin;
//            tvtP.topMargin = (int) (116.0f/640.0f*mHeight);
//            tvtP.width = mHeight;
//            tvTitle.setLayoutParams(tvtP);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

}
