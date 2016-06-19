package com.petsay.component.view.publishtalk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.petsay.R;
import com.petsay.activity.petalk.publishtalk.PublishTalkActivity;
import com.petsay.component.gifview.BaseGifView;
import com.petsay.component.media.ExAudioMediaPlayer;
import com.petsay.component.media.ExAudioRecorder;
import com.petsay.component.media.MediaListener;
import com.petsay.component.media.RecordingThread;
import com.petsay.component.media.Transcoding;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.VolumeView;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.petalk.VoiceEffect;
import com.petsay.vo.decoration.DecorationBean;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/4
 * @Description
 */
public class EditRecordView extends RelativeLayout implements TitleBar.OnClickBackListener, AudioEffectsView.ClickAudioEffectsCallback, MediaListener, View.OnClickListener {

    private static final int ONVOLUMECHANGE=40;
    public static final int TRANSCODINGFINISH = 800;
    public static final int COUNTTIME = 500;

    private static final int TOTALTIME = 60;
    private TitleBar mTitleBar;
    private FrameLayout flContent;
    private LinearLayout llControl;
    private ImageView mIvContent;
    private BaseGifView gifView;
    private View voiceLayout;
    private VolumeView mVolumeView;
//    private LinearLayout mPlayview;
    private ImageView mImgPlay;
//    private TextView mTvDuration;
    private AudioEffectsView audioeffects;
    private LinearLayout layoutRecord;
    private ImageView imgRecord;
    private TextView tvRecordToast;
    private ProgressDialog mDialog;

    private ExAudioMediaPlayer mPlayer;
    private ExAudioRecorder mRecorder;
    private Transcoding mTranscoding;
    public File mAudioFile;
    public int mTimeCount;
    private boolean mRecording;
    private VoiceEffect mEffect;
    private Timer mTimer;
    private PublishTalkParam mTalkParam;
    private PopupWindow mCancelMenu;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case ONVOLUMECHANGE:
                    mVolumeView.volumeChange((Integer)msg.obj);
                    break;
                case TRANSCODINGFINISH:	//转码完毕
                    closeLoading();
                    File file = (File) msg.obj;
                    if(file.exists()){
                        mAudioFile = file;
                        //当前是异步线程
                        playRecorder(Uri.fromFile(file));
                        mTimeCount = mPlayer.getDuration()/1000;
                        mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(mTimeCount*1000)+"”");
                    }else {
                        PublicMethod.showToast(getContext(), "录音失败！");
                    }
                    break;
                case COUNTTIME:	//计时线程
                    //				PublicMethod.log_d("recode", "createTime:"+mTimeCount);
                    mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(mTimeCount*1000)+"”");
                    if(mTimeCount >= TOTALTIME){
                        stopRecord();
                    }
                    break;
            }
        }
    };


    public EditRecordView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.edit_record_view, this);
        findViews();
        initTitleBar();
        initMedia();
        setListener();
        int height = PublicMethod.getDisplayHeight(getContext());
        int ch = (int) (height - PublicMethod.getDisplayWidth(getContext()) - (getContext().getResources().getDimension(R.dimen.title_height)));
        LayoutParams params = (LayoutParams) llControl.getLayoutParams();
        params.height = ch;
        llControl.setLayoutParams(params);
    }

    /**
     * 初始化音频相关操作
     */
    private void initMedia(){
        mPlayer = new ExAudioMediaPlayer(getContext());
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
//                mImgPlay.setVisibility(View.VISIBLE);
//                mTvDuration.setText(mVolumeView.getRecordTime());
//                mVolumeView.setVisibility(View.GONE);
//                if(mPlayer.getDuration() > 0)
//                    mBar.setProgress(mPlayer.getDuration());
                setPauseView();
                stopGif();
            }
        });
        mPlayer.setOnProgressListener(new ExAudioMediaPlayer.OnPlayerProgressListener() {

            @Override
            public void onProgressListener(int currentPosition) {
//                mBar.setProgress(currentPosition);
            }
        });
        mRecorder  = new ExAudioRecorder();
        mRecorder.setOnVolumeChangeListener(new RecordingThread.OnVolumeChangeListener() {

            @Override
            public void onVolumeChange(int volume) {
                Message msg = mHandler.obtainMessage();
                msg.obj = volume;
                msg.what = ONVOLUMECHANGE;
                msg.sendToTarget();
            }
        });
        mTranscoding = new Transcoding(UUID.randomUUID().toString(), Constants.SOUND_FILEPATH, getContext(), this);
    }

    private void stopGif() {
        if(gifView != null)
            gifView.stopGif();
    }

    private void playGif(){
        if(gifView != null){
            gifView.playGif();
        }
    }

    private void setListener() {
        audioeffects.setCallback(this);
        mImgPlay.setOnClickListener(this);
        mIvContent.setOnClickListener(this);
        imgRecord.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        imgRecord.setImageResource(R.drawable.record_click);
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        imgRecord.setImageResource(R.drawable.record_normal);
                        pauseRecord();
                        break;
                }
                return true;
            }
        });
    }

    private void initTitleBar() {
        mTitleBar.setTitleText("录音");
        mTitleBar.setOnClickBackListener(this);
        mTitleBar.setBackgroundColor((Color.parseColor("#5a5a5a")));
        TextView next= PublicMethod.addTitleRightText(mTitleBar, "下一步");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
    }

    private void onNext() {

        if(mTalkParam.editImg == null){
            PublicMethod.showToast(getContext(), "合成图片失败！");
            return;
        }

        if(mAudioFile == null || !mAudioFile.exists()){
            PublicMethod.showToast(getContext(), "请先录音再发布");
            return;
        }

        if(mTimeCount <= 3){
            PublicMethod.showToast(getContext(), "录音不少于3秒");
            return;
        }

        mTalkParam.audioSecond = mPlayer.getDuration();
        mTalkParam.audioFile = mAudioFile;
        Intent intent = new Intent(getContext(), PublishTalkActivity.class);
        getContext().startActivity(intent);
    }

    private void findViews() {
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        flContent = (FrameLayout)findViewById( R.id.fl_content );
        mIvContent = (ImageView) findViewById(R.id.iv_content);
        gifView = (BaseGifView) findViewById(R.id.gifview);
        voiceLayout = findViewById( R.id.voice_layout );
        mVolumeView = (VolumeView)findViewById( R.id.img_volume );
//        mPlayview = (LinearLayout)findViewById( R.id.layout_imglayout );
        mImgPlay = (ImageView)findViewById( R.id.img_play );
//        mTvDuration = (TextView)findViewById( R.id.tv_duration );
        audioeffects = (AudioEffectsView)findViewById( R.id.audioeffects );
        layoutRecord = (LinearLayout)findViewById( R.id.layout_record );
        imgRecord = (ImageView)findViewById( R.id.img_record );
        tvRecordToast = (TextView)findViewById( R.id.tv_record_toast );
        llControl = (LinearLayout) findViewById(R.id.ll_control);
    }


    public void updateView(PublishTalkParam param){
        mTalkParam = param;
        mIvContent.setImageBitmap(param.editImg);
        DecorationBean bean = param.mouth;
        gifView.initData(bean);
        int width = param.editImg.getWidth();
        updateGifItemLayout(width, width, param.decorations.get(0), gifView, (FrameLayout.LayoutParams) gifView.getLayoutParams());
        gifView.showFirstFrame();
    }

    private void updateGifItemLayout(int width,int height,PublishTalkParam.Position position,View view,android.widget.FrameLayout.LayoutParams params) {
        if(position == null)
            return;
        float pw = (float) (width * position.width);
        float ph = (float) (height * position.height);
        float left = (float) (width * position.centerX - width/2);
        float top = (float) (height * position.centerY - width/2);
        float rotate = (float)(position.rotationZ*180/Math.PI);
        //采用第三方jar包避免Android2.3版本的异常
        ViewHelper.setScaleX(view, pw / width);
        ViewHelper.setScaleY(view, ph/height);
        ViewHelper.setRotation(view, rotate);
        params.width = width;
        params.height = height;
        params.leftMargin = (int)left;
        params.topMargin = (int)top;
        view.setLayoutParams(params);
    }

    public void onDestroy() {
        if(mPlayer != null){
            mPlayer.stopPlay();
            mPlayer = null;
        }

        if(mTimer != null){
            mTimer.cancel();
        }
        if(mRecorder != null){
            mRecorder.release();
        }
        if(VoiceEffect.mDatas != null){
            VoiceEffect.mDatas.clear();
        }
    }

    public boolean isShow(){
        return getVisibility() == VISIBLE;
    }

    public void onHiden(){
        onPause();
        setVisibility(GONE);
    }

    public void onShow(){
        setVisibility(VISIBLE);
    }

    /**
     * 播放录音文件
     * @param uri
     */
    private void playRecorder(Uri uri){
        if(isShow()) {
            boolean flag = mPlayer.playFile(uri);
            if (flag) {
//            mTvDuration.setVisibility(GONE);
//            mImgPlay.setVisibility(View.GONE);
//            mVolumeView.setVisibility(View.GONE);
//            mBar.setVisibility(View.VISIBLE);
//            mBar.setMax(mPlayer.getDuration());
//            mBar.setProgress(0);
                setPlayView();
                mTimeCount = mPlayer.getDuration() / 1000;
                playGif();
            }
        }
    }

    /**
     * 暂停播放
     */
    private void pausePlay(){
//        mTvDuration.setVisibility(VISIBLE);
//        mVolumeView.setVisibility(View.GONE);
//        mImgPlay.setVisibility(View.VISIBLE);
        setPauseView();
//        mTvDuration.setText(PublicMethod.changeTimeFormat(mPlayer.getDuration())+"“");
//        mTvDuration.setVisibility(VISIBLE);
        mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(mPlayer.getDuration())+"“");
        mPlayer.pausePlay();
        stopGif();
    }

    /**
     * 恢复播放
     */
    private void resumePlay(){
        if(mPlayer.getDuration() > 3000 ){
//            mBar.setVisibility(View.VISIBLE);
//            mTvDuration.setVisibility(GONE);
//            mVolumeView.setVisibility(View.GONE);
//            mImgPlay.setVisibility(View.GONE);
            setPlayView();
            mPlayer.resumePlay();
            playGif();
        }
    }

    /**
     * 开始录音
     */
    private void startRecord(){
        mRecording = true;
//        mIsPause= false;
        startTimer();
//        mTvDuration.setVisibility(GONE);
//        mVolumeView.setVisibility(View.VISIBLE);
//        mImgPlay.setVisibility(View.GONE);
//        mBar.setVisibility(View.GONE);
        setRecordView();
        mTranscoding.clearFiles();
        mPlayer.stopPlay();
        mRecorder.startRecord();
        stopGif();
//        imgFinish.setEnabled(false);
//        imgCancle.setEnabled(false);

    }

    private void startTimer(){
        if(mTimer == null){
            mTimer = new Timer();
            mTimer.schedule(mTask, 0,1000);
        }
    }

    /**
     * 时间到了以后自动停止录音
     */
    private void stopRecord(){
        imgRecord.setImageResource(R.drawable.luyin_jinyong);
//        imgFinish.setEnabled(true);
//        imgCancle.setEnabled(true);
        mRecording = false;
//        mIsPause = true;
        imgRecord.setEnabled(false);
        onTanscoding(mEffect);
    }

    /**
     * 转换音效
     * @param effect
     */
    private void onTanscoding(VoiceEffect effect){
        if(mTimeCount > 0 && mRecorder.getSourceData().size() > 10){
            showLoading();
            mPlayer.stopPlay();
            if(effect == null){
                mEffect = VoiceEffect.getAudioDatas().get(0);
            }else {
                mEffect = effect;
            }
            mTranscoding.transcoding(mEffect.id, mRecorder.getSourceData(), mEffect.pitch, mEffect.rate, mEffect.tempo);
        }
    }

    private TimerTask mTask = new TimerTask() {

        @Override
        public void run() {
            if(mRecording){
                mTimeCount ++;
                mHandler.sendEmptyMessage(COUNTTIME);
            }
            if(mTimeCount == TOTALTIME ){
                mTimer.cancel();
                mTimer = null;
                return;
            }
        }
    };

    /**
     * 重新录音
     */
    private void resetRecord(){
        pausePlay();
        mTimeCount = 0;
        mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(0)+"”");
        if(mAudioFile != null && mAudioFile.exists())
            mAudioFile.delete();
        mRecorder.clear();
        mPlayer.stopPlay();
    }

    /**
     * 停止录音
     */
    private void pauseRecord(){
        mRecording = false;
//        mVolumeView.setVisibility(View.GONE);
//        mImgPlay.setVisibility(View.GONE);
//        imgFinish.setEnabled(true);
//        imgCancle.setEnabled(true);
//        mIsPause = true;
        setPlayView();
        mRecorder.stopRecord();
        onTanscoding(mEffect);
    }

    private void setRecordView(){
        mVolumeView.setVisibility(VISIBLE);
//        mPlayview.setVisibility(GONE);
        mImgPlay.setVisibility(GONE);
    }

    private void setPlayView(){
        mVolumeView.setVisibility(GONE);
//        mPlayview.setVisibility(GONE);
        mImgPlay.setVisibility(GONE);
//        mTvDuration.setVisibility(GONE);
    }

    private void setPauseView(){
        mVolumeView.setVisibility(GONE);
//        mPlayview.setVisibility(VISIBLE);
        mImgPlay.setVisibility(VISIBLE);
//        mTvDuration.setVisibility(VISIBLE);
    }

    @Override
    public void OnClickBackListener() {
        showCustomMenu();
    }

    @Override
    public void onClickAudioEffects(VoiceEffect effect, View view) {
        mEffect = effect;
        onTanscoding(effect);
    }

    @Override
    public void onTranscodingFinishCallback(File file) {
        Message msg = mHandler.obtainMessage();
        msg.obj = file;
        msg.what = TRANSCODINGFINISH;
        msg.sendToTarget();
    }

    protected void showLoading(){
        closeLoading();
        mDialog = PublicMethod.creageProgressDialog(getContext(),false);
    }

    protected void closeLoading(){
        PublicMethod.closeProgressDialog(mDialog, getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_play:
                resumePlay();
                break;
            case R.id.iv_content:
                if(mPlayer.isPlaying())
                    pausePlay();
                else
                    resumePlay();
                break;
            case R.id.tv_editmouth:
                hidenCustomMenu();
                onHiden();
                break;
            case R.id.tv_resetrecord:
                hidenCustomMenu();
                resetRecord();
                break;
            case R.id.tv_logout:
                hidenCustomMenu();
                if(getContext() instanceof Activity){
                    ((Activity)getContext()).finish();
                }
                break;
            case R.id.btn_cancel:
                hidenCustomMenu();
                break;
        }
    }

    public void onPause() {
        pausePlay();
        mRecorder.stopRecord();
    }

    public void onResume() {

    }

    public void hidenCustomMenu(){
        if(mCancelMenu != null)
            mCancelMenu.dismiss();
    }

    public void showCustomMenu(){
        hidenCustomMenu();
        View view = inflate(getContext(),R.layout.cancelmenu_view, null);
        view.findViewById(R.id.tv_editmouth).setOnClickListener(this);
        view.findViewById(R.id.tv_resetrecord).setOnClickListener(this);
        view.findViewById(R.id.tv_logout).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCancelMenu = new PopupWindow(view,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCancelMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        mCancelMenu.setFocusable(true);
        mCancelMenu.setBackgroundDrawable(new BitmapDrawable());
        mCancelMenu.setAnimationStyle(R.anim.bottom_in);
        mCancelMenu.showAtLocation(this, Gravity.BOTTOM, 0, 0);
    }
}
