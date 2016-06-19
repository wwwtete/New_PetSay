package com.petsay.activity.petalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.story.StoryPreviewActivity;
import com.petsay.component.gifview.AudioGifView;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.gifview.ImageLoaderListener;
import com.petsay.component.view.ExProgressBar;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/10
 * @Description
 */
public abstract class BasePettalkAdapter<T> extends ExBaseAdapter<T> {

    protected int mLayoutWidth;
    protected boolean mFirstInit = true;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BasePettalkAdapter(Context context,int layoutWidth){
        super(context);
        this.mLayoutWidth = layoutWidth;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    protected void onClickGifView(View view,BasePetalkViewHolder holder) {
        if(view.getTag() != null && view.getTag() instanceof PetalkVo) {
            PetalkVo say = (PetalkVo) view.getTag();
            if(say.isAudioModel())
                GifViewManager.getInstance().playGif(holder.amGif);
            else if(say.isStoryModel()){
                GifViewManager.getInstance().stopGif();
                Intent intent = new Intent(mContext, StoryPreviewActivity.class);
                intent.putExtra("storypieces",say.getStoryPieces());
                mContext.startActivity(intent);
            }
        }
    }

    /**
     * 获取公用的View
     * @param holder
     * @param convertView
     */
    public void findPublicView(BasePetalkViewHolder holder,View convertView){
        if(convertView != null){
            holder.amGif = (AudioGifView) convertView.findViewById(R.id.am_gif);
            initGifView(holder.amGif, convertView);
            holder.ivFlag = (ImageView) convertView.findViewById(R.id.iv_flag);
            holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
            initStoryView(convertView,holder);
            View contentView = convertView.findViewById(R.id.layout_content);
            PublicMethod.initPetalkViewLayout(contentView, mLayoutWidth);

        }
    }

    private void initStoryView(View convertView, BasePetalkViewHolder holder) {
        holder.tvStoryTime = (TextView) convertView.findViewById(R.id.tv_storytime);
        holder.tvStoryTitle = (TextView) convertView.findViewById(R.id.tv_storytitle);
        PublicMethod.initStoryCoverViewLayout(holder.tvStoryTitle, holder.tvStoryTime, null, mLayoutWidth);
    }

    protected void initGifView(AudioGifView amGif, View convertView){
        ImageView playView = (ImageView) convertView.findViewById(R.id.img_play);
        ExProgressBar loadingBar = (ExProgressBar)convertView.findViewById(R.id.pro_loaderpro);
        ImageLoaderListener listener = new ImageLoaderListener(loadingBar);
        ProgressBar playBar = (ProgressBar) convertView.findViewById(R.id.playprogressbar);
        amGif.setPlayBtnView(playView);
        amGif.setImageLoaderListener(listener);
        amGif.setPlayProgressBar(playBar);
    }

    protected void refrshView(BasePetalkViewHolder holder, int position){

    }

    protected void refreshPetalk(PetalkVo sayVo, BasePetalkViewHolder holder, int position){
        holder.imgPet.setTag(sayVo);

        AudioGifView amGif = holder.amGif;
        ImageLoaderListener listener = amGif.getImageLoaderListener();
        listener.reset();
        ImageLoaderHelp.displayContentImage(sayVo.getPhotoUrl(), holder.imgPet, listener, listener);
        amGif.reset();
        amGif.setPlayBtnVisibility(false);
        amGif.setVisibility(View.INVISIBLE);
        holder.tvStoryTime.setVisibility(View.GONE);
        holder.tvStoryTitle.setVisibility(View.GONE);

        if(sayVo.isStoryModel()){
            PublicMethod.initStoryCoverViewLayout(null,null,holder.imgPet,mLayoutWidth);
        }else {
            RelativeLayout.LayoutParams imgP = (RelativeLayout.LayoutParams) holder.imgPet.getLayoutParams();
            imgP.leftMargin = imgP.topMargin = 0;
            imgP.width = imgP.height = mLayoutWidth;
            holder.imgPet.setLayoutParams(imgP);
        }
        if(sayVo.isAudioModel()) {
            amGif.setVisibility(View.VISIBLE);
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) amGif.getLayoutParams();
            amGif.initData(sayVo, mLayoutWidth, mLayoutWidth);
            PetalkDecorationVo ad = sayVo.getDecorations()[0];
            PublicMethod.updateGifItemLayout(mLayoutWidth, mLayoutWidth, ad, amGif, params);
            boolean autoPlay = GifViewManager.getInstance().getAllowAutoPlay();
            amGif.setPlayBtnVisibility(!autoPlay);
            if (position == 0 && mFirstInit && autoPlay) {
                GifViewManager.getInstance().playGif(amGif);
            }
        }else if(sayVo.isStoryModel()){
            holder.tvStoryTime.setText(sayVo.getStoryTime());
            holder.tvStoryTitle.setText(sayVo.getStoryTitle());
            holder.tvStoryTitle.setVisibility(View.VISIBLE);
            holder.tvStoryTime.setVisibility(View.VISIBLE);
        }
        mFirstInit  = false;
    }

    /**
     * 刷新左上角说说状态
     * @param model
     * @param ivFlag
     */
    protected void refreshItemTypeView(int model, ImageView ivFlag){
        if(model == 0){
            ivFlag.setImageResource(R.drawable.audio_flag_icon);
        }else if(model == 1) {
            ivFlag.setImageResource(R.drawable.image_flag_icon);
        }else {
            ivFlag.setImageResource(R.drawable.story_flag_icon);
        }
    }

    protected class BasePetalkViewHolder {
        public AudioGifView amGif;
        public ImageView ivFlag;
        public ImageView imgPet;
        public TextView tvStoryTitle;
        public TextView tvStoryTime;
    }

}
