package com.petsay.activity.story;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.media.MediaPlayManager;
import com.petsay.component.media.MediaPlayManager.OnPlayerStateChangedListener;
import com.petsay.component.view.DividerItemDecoration;
import com.petsay.constants.Constants;
import com.petsay.network.download.DownloadTask;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryAddressItem;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;
import com.petsay.vo.story.StoryPieceDTO;
import com.petsay.vo.story.StoryTextItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/30
 * @Description
 */
public class StoryPreviewActivity extends BaseActivity implements OnPlayerStateChangedListener {

    @InjectView(R.id.recyclerview)
    private RecyclerView mReView;
    private StoryParams mParams;

    private Parcelable[] mStoryPics;
    private String mAudioDirs = FileUtile.getPath(this,Constants.AUDIO_DOWNLOAD_PATHE);
    private LinearLayoutManager mLayoutManager;
    private PreviewStoryAdapter mAdapter;
    private MediaPlayManager mPlayer;
    private String mCurrAudioPath;
    private int mHeight;
    private boolean mAllowPlay;
    private ImageView mImgAudio;
    private AnimationDrawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_preview);
        if(getIntent().getSerializableExtra("params") != null)
            mParams = (StoryParams) getIntent().getSerializableExtra("params");
        if(getIntent().getParcelableArrayExtra("storypieces") != null)
            mStoryPics = (Parcelable[]) getIntent().getParcelableArrayExtra("storypieces");
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("预览", true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReView.setLayoutManager(mLayoutManager);
        mReView.setHasFixedSize(true);
        if(mParams == null && mStoryPics == null) {
            showToast("数据错误，请返回重试");
        }
        else {
            initRecyclerView();
            mPlayer = MediaPlayManager.getSingleton();
            mPlayer.setOnPlayerStateChangedListener(this);
        }
    }

    private void initRecyclerView() {
        mHeight = (PublicMethod.getDisplayHeight(this) - getResources().getDimensionPixelOffset(R.dimen.title_height))/2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mReView.getLayoutParams();
        params.height = mHeight;
        mReView.setLayoutParams(params);
        int space = PublicMethod.getDiptopx(this, 4);
        DividerItemDecoration divider = new DividerItemDecoration(DividerItemDecoration.HORIZONTAL_LIST,space);
        mReView.addItemDecoration(divider);
        initAdapter();

    }

    private void initAdapter() {
        if(mParams != null) {
            mAdapter = new PreviewStoryAdapter(mParams.items, mParams.createTime, mParams.description, StorySetCoverActivity.storyCover, mHeight, this);
            initTitleBar(mParams.description);
        }else if(mStoryPics != null) {
           toStoryItemVo();
        }
        mAdapter.setListener(new PreviewStoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getTag() != null) {
                    StoryImageItem item = (StoryImageItem) view.getTag();
                    onClickStoryImageItem(item,view);
                }
            }
        });
        mReView.setAdapter(mAdapter);
    }

    private void toStoryItemVo() {
        List<StoryItemVo> datas = new ArrayList<StoryItemVo>(mStoryPics.length - 2);
        String createTime = "";
        String title = "";
        String cover = "";
        int length = mStoryPics.length - 1;
        for (int i=0;i< length;i++){
            StoryPieceDTO dto = (StoryPieceDTO) mStoryPics[i];
            List<String> vals= dto.getTypeVals();
            if(dto.getType() == 1){
                    cover = vals.get(0);
                    title = vals.get(1);
                    createTime = vals.get(2);
            }else {
                StoryItemVo itemVo = null;
                switch (dto.getType()) {
                    case 2:
                        itemVo = new StoryImageItem("");
                        break;
                    case 3:
                        itemVo = new StoryTextItem();
                        break;
                    case 4:
                        itemVo = new StoryAddressItem();
                        break;
                }
                if(itemVo != null) {
                    itemVo.setStoryPieceDto(dto);
                    datas.add(itemVo);
                }
            }
        }
        mAdapter = new PreviewStoryAdapter(datas,createTime,title,cover,mHeight,this);
        initTitleBar(title);
    }

    private void onClickStoryImageItem(StoryImageItem item,View view) {
        if(item != null && !TextUtils.isEmpty(item.getAudioUrl())) {
        	if (mDrawable!=null) {
            	mDrawable.stop();
			}
        	mImgAudio=(ImageView) view.findViewById(R.id.iv_mark);
        	mDrawable=(AnimationDrawable) mImgAudio.getBackground();
            if(item.getAudioUrl().equals(mCurrAudioPath)){
                if(mPlayer != null)
                    mPlayer.pause();
            }else {
                if(item.getAudioUrl().contains("http://")){
                    onPrepareServerFile(item.getAudioUrl());
                }else {
                    onPlay(item.getAudioUrl());
                }
            }
            mCurrAudioPath = item.getAudioUrl();
        }
    }

    private void onPrepareServerFile(String audioUrl) {
        String audioName =  FileUtile.getFileNameByUrl(audioUrl);
        File audioFile = new File(mAudioDirs,audioName);
        if(audioFile.exists()){
            onPlay(audioFile.getAbsolutePath());
        }else {
            downloadFile(audioUrl);
        }
    }

    private void downloadFile(String audioUrl) {
        if(PublicMethod.getAPNType(this) < 0){
            showToast("请检查网络后下载");
            return;
        }

        DownloadTask task = new DownloadTask(this,mAudioDirs);
        task.setCallback(new DownloadTask.DownloadTaskCallback() {
            @Override
            public void onDownloadFinishCallback(DownloadTask task, boolean isSuccess, String url, File file, Object what) {
                if(isSuccess && url.equals(mCurrAudioPath)){
                    onPlay(file.getAbsolutePath());
                }
            }

            @Override
            public void onCancelCallback(DownloadTask task, String url, Object what) {
            }
        });
        task.execute(audioUrl);
    }

    private void onPlay(String filePath){
        if(mAllowPlay)
            mPlayer.play(StoryPreviewActivity.this, filePath);
    }


    @Override
    protected void onResume() {
        mAllowPlay = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAllowPlay = false;
        mPlayer.stopAudio();
        super.onPause();
    }

	@Override
	public void onPlayStop() {
		if (null!=mImgAudio&&null!=mDrawable) {
			mDrawable.stop();
		}
	}

	@Override
	public void onPlayPause() {
		if (null!=mImgAudio&&null!=mDrawable) {
			mDrawable.stop();
		}
		
	}

	@Override
	public void onPlayStart() {
		if (null!=mImgAudio&&null!=mDrawable) {
			mDrawable.start();
		}
		
	}

//    class PreviewStoryAdapter extends RecyclerView.Adapter<PreviewStoryAdapter.ViewHolder>{
//
//        private List<StoryItemVo> mDatas;
//        private Context mContext;
//        //        private int mHeight;
//        private LayoutInflater mInflater;
//        private OnItemClickListener mListener;
//        private DisplayImageOptions contentPetImgOptions;
//        private String mCreateTime;
//        private Object mCore;
//
//        public PreviewStoryAdapter(List<StoryItemVo> mDatas, Context mContext) {
//            this.mDatas = mDatas;
//            this.mContext = mContext;
//            mInflater = LayoutInflater.from(mContext);
//            initImageLoaderOption();
//        }
//
//        private void initImageLoaderOption() {
//            contentPetImgOptions = new DisplayImageOptions.Builder()
//                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                    .showImageOnLoadingBackground(R.drawable.pet1)
//                    .showImageForEmptyUri(R.drawable.pet1)
//                    .showImageOnFail(R.drawable.pet1)
//                    .cacheInMemory(false)
//                    .cacheOnDisk(false)
//                    .considerExifParams(false)
//                    .resetViewBeforeLoading(true) // default
//                    .displayer(new FadeInBitmapDisplayer(500,true,true,false))
////				.displayer(new RoundedBitmapDisplayer(1000))
////				.displayer(new RoundedVignetteBitmapDisplayer(1000, 100))
//                    .delayBeforeLoading(300)
//                    .build();
//        }
//
//        public OnItemClickListener getListener() {
//            return mListener;
//        }
//
//        public void setListener(OnItemClickListener listener) {
//            this.mListener = listener;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            PetsayLog.e("[onCreateViewHolder]i->"+i);
//            return new ViewHolder(mInflater.inflate(R.layout.item_story_preview,null));
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, int i) {
//            PetsayLog.e("[onBindViewHolder]i->"+i);
//            if(i == 0){
//                onInitImageItem(null, viewHolder, i);
//            }else if(i == getItemCount()-1){
//                onInitImageItem(null, viewHolder, i);
//            }else {
//                StoryItemVo itemVo = mDatas.get(i - 1);
//                switch (itemVo.getType()){
//                    case StoryItemVo.TYPE_IMAGE:
//                        onInitImageItem((StoryImageItem) itemVo,viewHolder, i);
//                        break;
//                    case StoryItemVo.TYPE_ADDRESS_TIME:
//                        onInitAddressItem((StoryAddressItem) itemVo,viewHolder,i);
//                        break;
//                    case StoryItemVo.TYPE_TEXT:
//                        onInitTextItem((StoryTextItem) itemVo,viewHolder, i);
//                        break;
//                }
//            }
//        }
//
//        private void onInitImageItem(StoryImageItem item,ViewHolder viewHolder, final int i) {
//            viewHolder.imgView.setVisibility(View.VISIBLE);
//            viewHolder.txtView.setVisibility(View.GONE);
//            viewHolder.addressView.setVisibility(View.GONE);
//            viewHolder.ivMark.setVisibility(View.GONE);
//            viewHolder.tvDescribe.setVisibility(View.GONE);
//            viewHolder.tvCreateTime.setVisibility(View.GONE);
//            viewHolder.tvTitle.setVisibility(View.GONE);
//            viewHolder.ivCover.setVisibility(View.GONE);
//            viewHolder.imgView.setOnClickListener(null);
//            viewHolder.imgView.setLayoutParams(viewHolder.imgParams);
//            viewHolder.ivContent.setImageResource(R.drawable.pet1);
//            if(i == 0){
//                if(StorySetCoverActivity.storyCover != null && !StorySetCoverActivity.storyCover.isRecycled())
//                    initCover(viewHolder);
//                else
//                    viewHolder.ivContent.setImageResource(R.drawable.story_cover);
//            }else if(i == getItemCount()-1){
//                viewHolder.ivContent.setImageResource(R.drawable.story_backcover);
//            }else {
//                if(!TextUtils.isEmpty(item.getAudioUrl())) {
//                    viewHolder.ivMark.setVisibility(View.VISIBLE);
//                    viewHolder.imgView.setTag(item);
//                    viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(mListener != null){
//                                mListener.onItemClick(v,i);
//                            }
//                        }
//                    });
//                }
//                if(!TextUtils.isEmpty(item.getDescribe())){
//                    viewHolder.tvDescribe.setText(item.getDescribe());
//                    viewHolder.tvDescribe.setVisibility(View.VISIBLE);
//                }
//                ImageLoaderHelp.displayImage("file://"+item.getImageUrl(),viewHolder.ivContent,contentPetImgOptions);
//            }
//        }
//
//        private void initCover(ViewHolder viewHolder) {
//            viewHolder.ivContent.setImageResource(R.drawable.story_cover_blank);
//            viewHolder.ivCover.setImageBitmap(StorySetCoverActivity.storyCover);
//            viewHolder.tvCreateTime.setText("2015.10.8");
//            viewHolder.tvTitle.setText("是放大法师法师法来来来暗示法发生的发来了来了");
//            viewHolder.ivCover.setVisibility(View.VISIBLE);
//            viewHolder.tvTitle.setVisibility(View.VISIBLE);
//            viewHolder.tvCreateTime.setVisibility(View.VISIBLE);
//        }
//
//        private void onInitAddressItem(StoryAddressItem item,ViewHolder viewHolder, int i) {
//            viewHolder.addressView.setVisibility(View.VISIBLE);
//            viewHolder.imgView.setVisibility(View.GONE);
//            viewHolder.txtView.setVisibility(View.GONE);
//            viewHolder.tvAddress.setText(item.getAddress());
//            viewHolder.tvTime.setText(item.getTime());
//
//        }
//
//        private void onInitTextItem(StoryTextItem item,ViewHolder viewHolder, int i) {
//            viewHolder.txtView.setVisibility(View.VISIBLE);
//            viewHolder.imgView.setVisibility(View.GONE);
//            viewHolder.addressView.setVisibility(View.GONE);
//            viewHolder.tvContent.setText(item.getContent());
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDatas.size()+2;
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            public View addressView;
//            public TextView tvTime;
//            public TextView tvAddress;
//
//            public View txtView;
//            public TextView tvContent;
//
//            public View imgView;
//            public ImageView ivContent;
//            public ImageView ivMark;
//            public TextView tvDescribe;
//            public RelativeLayout.LayoutParams imgParams;
//            public ImageView ivCover;
//            public TextView tvCreateTime;
//            public TextView tvTitle;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                bindViews(itemView);
//            }
//
//            private void bindViews(View itemView) {
//                addressView = itemView.findViewById(R.id.layout_address);
//                RelativeLayout.LayoutParams ap = (RelativeLayout.LayoutParams) addressView.getLayoutParams();
//                ap.height = mHeight;
//                ap.width = mHeight;
////                addressView.setLayoutParams(ap);
//                tvTime = (TextView)itemView.findViewById( R.id.tv_time );
//                tvAddress = (TextView)itemView.findViewById( R.id.tv_address );
//
//                txtView = itemView.findViewById(R.id.layout_text);
//                RelativeLayout.LayoutParams tp = (RelativeLayout.LayoutParams) txtView.getLayoutParams();
//                tp.height = mHeight;
//                tp.width = mHeight;
//                txtView.setLayoutParams(tp);
//                tvContent = (TextView)itemView.findViewById( R.id.tv_content );
//                int padding = PublicMethod.getDiptopx(mContext, 30);
//                tvContent.setPadding(padding,padding,padding,padding);
//
//                imgView = itemView.findViewById( R.id.rl_img );
//                imgParams = new RelativeLayout.LayoutParams( imgView.getLayoutParams());
//
//                initImageView(itemView);
//            }
//
//            private void initImageView(View itemView) {
//                ivContent = (ImageView)itemView.findViewById(R.id.iv_content);
////                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivContent.getLayoutParams();
////                params.height = mHeight;
////                ivContent.setLayoutParams(params);
//                ivMark = (ImageView)itemView.findViewById( R.id.iv_mark );
//                tvDescribe = (TextView)itemView.findViewById( R.id.tv_describe );
//
//                ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
//                RelativeLayout.LayoutParams icP = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
//                icP.leftMargin = (int) (40.0f/640.0f*mHeight);
//                icP.topMargin = (int) (224.0f/640.0f*mHeight);
//                icP.width = (int) (560.0f/640.0f*mHeight);
//                icP.height = (int) (320.0f/640.0f*mHeight);
//                ivCover.setLayoutParams(icP);
//                tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
//                RelativeLayout.LayoutParams ttP = (RelativeLayout.LayoutParams) tvCreateTime.getLayoutParams();
//                ttP.leftMargin = icP.leftMargin;
//                ttP.topMargin = (int) (164.0f/640.0f*mHeight);
//                tvCreateTime.setLayoutParams(ttP);
//                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
//                RelativeLayout.LayoutParams tvtP = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
//                tvtP.leftMargin = ttP.leftMargin;
//                tvtP.topMargin = (int) (116.0f/640.0f*mHeight);
//                tvtP.width = mHeight;
//                tvTitle.setLayoutParams(tvtP);
//            }
//        }
//
//    }
//
//    public interface OnItemClickListener{
//        void onItemClick(View view, int position);
//    }

}
