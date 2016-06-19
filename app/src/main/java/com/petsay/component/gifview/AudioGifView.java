package com.petsay.component.gifview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.petsay.component.media.ExAudioMediaPlayer;
import com.petsay.constants.Constants;
import com.petsay.network.download.DownloadTask;
import com.petsay.network.download.DownloadTask.DownloadTaskCallback;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;

import java.io.File;

/**
 * @author wangw
 *
 */
public class AudioGifView extends AnimationGifView implements OnCompletionListener, ExAudioMediaPlayer.OnPlayerProgressListener {

	public static final int ONDOWLOADAUDIOFINISH=350;
	private String mAudioDirs = FileUtile.getPath(getContext(),Constants.AUDIO_DOWNLOAD_PATHE);

	private boolean mAudioReadyed = false;
	private File mAudioFile;
	private ExAudioMediaPlayer mPlayer;
	private ImageLoaderListener mListener;
	private GifViewCallback mCallback;
	private View mPlayBtnView;
    private ProgressBar mPlayBar;
	/**是否已停止*/
	private boolean mPaused = false;

	public AudioGifView(Context context){
		super(context);
	}

	public AudioGifView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void initViews() {
		super.initViews();
		mPlayer = new ExAudioMediaPlayer(getContext());
		mPlayer.setOnCompletionListener(this);
        mPlayer.setOnProgressListener(this);
	}

//	@Override
//	@Deprecated
//	public void initData(SayVo say, int width, int height) {
//		super.initData(say, width, height);
//
//		mAudioFile = null;
//		mAudioReadyed = false;
//	}

    public void reset(){
        super.reset();
        mAudioFile = null;
        mAudioReadyed = false;
        setPlayBarVisibility(false);
        setPlayProgress(0);
    }
	
	/**
	 * 清空ImageView背景图片
	 */
	public void clearView(){
		stopGif();
		this.setBackgroundDrawable(null);
	}

    public void setPlayProgressBar(ProgressBar bar){
        this.mPlayBar = bar;
    }

	public void setImageLoaderListener(ImageLoaderListener listener){
		this.mListener = listener;
	}

	public ImageLoaderListener getImageLoaderListener(){
		return mListener;
	}

	public void setGifViewCallback(GifViewCallback callback){
		this.mCallback = callback;
	}

	public GifViewCallback getGifViewCallback(){
		return mCallback;
	}

	public void removeGifViewCallback(){
		this.mCallback = null;
	}

	public void showLoading(boolean flag){
		if(mListener != null){
			if(flag){
				mListener.showProgressBar();
			}else {
				mListener.hidenProgressBar();
			}
		}

	}

	public void setPlayBtnView(View view){
		this.mPlayBtnView = view;
	}

	public void setPlayBtnVisibility(boolean flag){
		if(mPlayBtnView != null){
			mPlayBtnView.setVisibility(flag ? View.VISIBLE : View.GONE);
		}
	}

    public void setPlayBarVisibility(boolean flag){
        if(mPlayBar != null){
            mPlayBar.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
    }

	@Override
	public void playGif() {
		mPaused = false;
		boolean temp = verifyAudioFile();
        if(temp) {
            super.playGif();
            setPlayBtnVisibility(false);
        }
	}

	/**
	 * 暂停播放（不会是否资源）
	 */
	public void pause(boolean pause){
		if(mPlayer != null){
			if(mPlayer.isPlaying() || pause){
				mPaused = true;
				mPlayer.pausePlay();
				stopAnimation();
//				setPlayBtnVisibility(false);
//                setPlayBarVisibility(true);
			}else if(!pause) {
				resumePlay();
			}
		}
	}

	public void pause(){
		if(mPlayer != null){
			if(mPlayer.isPlaying()){
				mPaused = true;
				mPlayer.pausePlay();
				stopAnimation();
				setPlayBtnVisibility(true);
			}else {
				resumePlay();
			}
		}
	}

	public void resumePlay(){
		mPaused = false;
		if(mPlayer != null){
			if(mPlayer.isStoping()){
				playGif();
			}else {
				mPlayer.resumePlay();
				startAnimation();
			}
			setPlayBtnVisibility(false);
		}
	}

	public boolean isStoping(){
		if(mPlayer != null)
			return mPlayer.isStoping();
		return true;
	}

	public boolean isPlaying(){
		if(mPlayer != null)
			return mPlayer.isPlaying();
		return false;
	}

	public boolean isPaused(){
		return mPaused;
	}


	/**
	 * 停止播放，并是否资源
	 */
	@Override
	public void stopGif() {
		mPaused = true;
		super.stopGif();
		if(mPetalk != null)
			mPetalk.setPrePlayTime(mPlayer.getCurrentPosition());
//		if(mSayVo != null)
//			mSayVo.setPrePlayTime(mPlayer.getCurrentPosition());
		mPlayer.stopPlay();
		setPlayBtnVisibility(true);
        setPlayBarVisibility(false);
        setPlayProgress(0);
	}

	@Override
	protected void onReadyedFinish() {
        if(mStep == 3){
            super.onReadyedFinish();
        }else if(mBmpReadyed && mAudioReadyed && mAudioFile != null && !mPaused){
			super.onReadyedFinish();
			mPlayer.playFile(Uri.fromFile(mAudioFile));
            setPlayBarVisibility(true);
			//			mPlayer.playFile(mAudioFile);
			//			if(mSayVo.getPrePlayTime() > 0){
			//				mPlayer.seekTo(mSayVo.getPrePlayTime());
			//			}
			if(mPetalk.getPrePlayTime() > 0)
				mPlayer.seekTo(mPetalk.getPrePlayTime());
			if(mCallback != null){
				mCallback.onReadyedFinish(mAudioReadyed, mBmpReadyed);
			}
		}
	}

	public boolean checkReadyedSuccess(){
		return mBmpReadyed && mAudioReadyed;
	}


	/**
	 * 验证音频文件是否存在
	 */
	private boolean verifyAudioFile(){
//		String audioURL = mPetalk != null ? mPetalk.getAudioUrl() : mSayVo.getAudioUrl();
        if(mPetalk == null)
            return false;
		String audioURL =  mPetalk.getAudioUrl();
		String audioName =  FileUtile.getFileNameByUrl(audioURL);
		File file = new File(mAudioDirs,audioName);
		if(file.exists()){
			mAudioFile = file;
			mAudioReadyed = true;
			onReadyedFinish();
		}else {
			downloadAudio(audioURL);
		}
        return true;
	}

	/**
	 * 下载音频文件
	 * @param url
	 */
	protected void downloadAudio(String url){
		if(PublicMethod.getAPNType(getContext()) > 0){
			showLoading(true);
			//		mService.download(url, mAudioDirs, this);
			DownloadTask task = new DownloadTask(this, mAudioDirs);
			task.setCallback(new DownloadTaskCallback() {
				@Override
				public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess, String url,
						File file, Object what) {
					if(!isSuccess){
						//TODO 待优化，如果下载音频文件失败，应有个重试机制
						//					PublicMethod.showToast(getContext(), "声音文件下载失败,重试中！");
						//					downloadAudio(mSayVo.getAudioUrl());
					}else {
						mAudioFile = file;
						mAudioReadyed = true;
						onReadyedFinish();
					}
					showLoading(false);
				}

				@Override
				public void onCancelCallback(DownloadTask task,String url, Object what) {
				}
			});
			task.execute(url);
		}
	}

//	private Handler mAudioHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case ONDOWLOADAUDIOFINISH:
//				if(msg.arg1 < 0){
//					//TODO 待优化，如果下载音频文件失败，应有个重试机制
//					//					PublicMethod.showToast(getContext(), "声音文件下载失败,重试中！");
//					//					downloadAudio(mSayVo.getAudioUrl());
//				}else {
//					onReadyedFinish();
//				}
//				showLoading(false);
//				break;
//			}
//
//		};
//	};


//	@Override
//	public void onDownloadFinishCallback(boolean isSuccess, String url,
//			File file, View view) {
//		if(mDecorationBean.getImageUrl().equals(url)){
//			super.onDownloadFinishCallback(isSuccess, url, file, view);
//		}else if(mPetalk != null && mPetalk.getAudioUrl().equals(url)){
//			onDownloadAudioFinish(isSuccess,file);
//		}
//		else if(mSayVo != null && mSayVo.getAudioUrl().equals(url)) {
//			onDownloadAudioFinish(isSuccess,file);
//		}
//	}

//	protected void onDownloadAudioFinish(boolean isSuccess,File file){
//		Message msg = mAudioHandler.obtainMessage();
//		msg.arg1 = -1;
//		msg.what = ONDOWLOADAUDIOFINISH;
//		if(isSuccess){
//			mAudioFile = file;
//			mAudioReadyed = true;
//			msg.arg1 = 1;
//		}
//		msg.sendToTarget();
//	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if(!mPlayer.isLooping())
			stopAnimation();
		if(mPetalk != null){
			mPetalk.setPrePlayTime(0);
		}
//		else
//			mSayVo.setPrePlayTime(0);
		setPlayBtnVisibility(true);
        setPlayBarVisibility(false);
        setPlayProgress(100);
	}

    @Override
    public void onProgressListener(int currentPosition) {
        int pro = (int) (currentPosition*100.0f/mPlayer.getDuration());
        setPlayProgress(pro);
    }

    public void setPlayProgress(int currentPosition){
        if(mPlayBar != null){
//            if(mPlayBar.getVisibility() == GONE)
//                setPlayBarVisibility(true);
            mPlayBar.setProgress(currentPosition);
        }
    }


    public interface GifViewCallback{
		void onReadyedFinish(boolean audioFlag,boolean decorationFlag);
	}



}
