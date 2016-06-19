package com.petsay.chat.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.petsay.constants.Constants;
import com.petsay.network.download.DownloadQueue;
import com.petsay.network.download.DownloadTask;
import com.petsay.utile.FileUtile;

import java.io.File;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/22
 * @Description 聊天播放语音
 */
public class ChatMediaPlayer implements MediaPlayer.OnCompletionListener, DownloadTask.DownloadTaskCallback {

    private MediaPlayer mPlayer;
    private Context mContext;
    private DownloadQueue mQueue;
    /**正在播放的文件*/
    private File mCurrPlayFile;
    /**想要播放的文件*/
    private File mExpectFile;
    private ChatAudioView mAudioView;

    public ChatMediaPlayer(Context context){
        this.mContext = context;
        mQueue = DownloadQueue.getInstance();
    }

    public void play(String audioUrl,boolean isCommsg,ChatAudioView chatAudioView){
        stopAnimation();
        mAudioView = chatAudioView;
        //1.验证音频文件是否存在
        String fileName = FileUtile.getFileNameByUrl(audioUrl);
        if(isCommsg){
            mExpectFile = new File(FileUtile.getPath(mContext,Constants.CHAT_SOUND_RECEIVE),fileName);
        }else {
            mExpectFile = new File(FileUtile.getPath(mContext,Constants.CHAT_SOUND_SEND),fileName);
        }
        //验证要播放的文件是否为正在播放或下载的文件
        if(mCurrPlayFile != null && mCurrPlayFile.getAbsolutePath().equals(mExpectFile.getAbsolutePath())){
            pause();
        }else if(mExpectFile.exists()){
            onPlayCore(mExpectFile);
        }else {
            onDownLoad(audioUrl,mExpectFile);
        }
    }

    public void play(String audioUrl,boolean isCommsg){
        play(audioUrl,isCommsg,null);
    }

    private void onDownLoad(String audioUrl,File file) {
        DownloadTask task = new DownloadTask(null,file.getParent());
        task.setCallback(this);
        task.setTag(this);
        task.execute(audioUrl);
        mQueue.add(task);
    }

    private void onPlayCore(File file) {
        mCurrPlayFile = file;
        try {
            release();
            mPlayer = MediaPlayer.create(mContext, Uri.fromFile(file));
            mPlayer.setOnCompletionListener(this);
            mPlayer.start();
            startAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        if(mPlayer != null){
            if(mPlayer.isPlaying()) {
                mPlayer.pause();
                stopAnimation();
            }else {
                mPlayer.start();
                startAnimation();
            }
        }
    }

    private void startAnimation(){
        if(mAudioView != null)
            mAudioView.playAnimation();
    }

    private void stopAnimation(){
        if(mAudioView != null)
            mAudioView.stopAnimation();
    }

    public void pause(boolean pause){
        if(mPlayer != null){
            if(pause){
                mPlayer.pause();
                stopAnimation();
            }else {
                mPlayer.start();
                startAnimation();
            }
        }
    }

    public void release(){
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        mQueue.cancelDownload(this);
        stopAnimation();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        stopAnimation();
    }

    @Override
    public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess, String url, File file, Object what) {
        mQueue.remove(task);
        if(isSuccess){
            if(mExpectFile != null && file.getAbsolutePath().equals(mExpectFile.getAbsolutePath())){
                onPlayCore(file);
            }
        }
    }

    @Override
    public void onCancelCallback(DownloadTask task,String url, Object what) {
        mQueue.remove(task);
    }
}
