package com.petsay.activity.story;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.constants.Constants;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.vo.story.StoryImageItem;

import java.io.File;
import java.io.IOException;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/29
 * @Description
 */
public class StoryRecorderView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {

    private RelativeLayout mRlRecorder;
    private TextView mTvRecordTime;
    private TextView mTvClear;
    private TextView mTvSave;
    private ImageButton mIvRecord;
    private MediaRecorder mRecorder;
    private StoryImageItem mItemVo;
    private String mAudioFilePath;
    private long mRecordStartTime;
    private long mRecordStopTime;
    private long mRecordSeconds = -1;
    //录音状态 -1：录音失败 0：停止录音 1：录音中
    private int mRecordStauts = 0;
    private CountDownTimer mTimer;

    private StoryRecorderCallback mCallback;

    public StoryRecorderView(Context context) {
        super(context);
        initView();
    }

    public StoryRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.view_story_recorder, this);
        setBackgroundResource(R.color.transparent2);
        this.setClickable(true);
        findViews(this);
    }

    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mAudioFilePath = FileUtile.getPath(getContext(), Constants.SOUND_FILEPATH) + System.currentTimeMillis() + ".amr";
        mRecorder.setOutputFile(mAudioFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    private void findViews(View rootView) {
        mRlRecorder = (RelativeLayout)rootView.findViewById( R.id.rl_recorder );
        mTvRecordTime = (TextView)rootView.findViewById( R.id.tv_record_time );
        mTvClear = (TextView)rootView.findViewById( R.id.tv_clear );
        mTvSave = (TextView)rootView.findViewById( R.id.tv_save );
        mIvRecord = (ImageButton)rootView.findViewById( R.id.iv_record );

        mIvRecord.setOnTouchListener(this);
        mTvClear.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }

    public void show(StoryImageItem item){
        this.mItemVo = item;
        this.setFocusable(true);
        this.setVisibility(VISIBLE);
        reset();
    }

    public void close(){
        this.setVisibility(GONE);
        if(mCallback != null)
            mCallback.onClose();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mRlRecorder.setVisibility(VISIBLE);
                startRecord();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopRecord();
                break;
        }
        return false;
    }

    private void reset(){
        LayoutParams params = (LayoutParams) mRlRecorder.getLayoutParams();
        params.width = PublicMethod.getDiptopx(getContext(),30);
        mRlRecorder.setLayoutParams(params);
        mRecordStopTime = 0;
        mRecordStartTime = 0;
        if(mRecorder != null)
            mRecorder = null;
        if(mTimer != null)
            mTimer = null;
        mTvRecordTime.setText("");
        mTvClear.setText("取消");
        mAudioFilePath = "";
        mIvRecord.setOnTouchListener(this);
        mIvRecord.setBackgroundResource(R.drawable.story_recoder_selector);
        mRecordSeconds = -1;
    }

    private void stopRecord(){
        mRecordStopTime = System.currentTimeMillis();
        if (null!=mRecorder) {
            try{
                mRecorder.stop();
            }catch(Exception e){
            }
            mRecorder.release();
            mRecorder = null;
        }
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mRecordStauts == 1) {
            mRecordSeconds = ((mRecordStopTime - mRecordStartTime) + 500) / 1000;
            mTvRecordTime.setText((int) mRecordSeconds + "”");

//        if (mRecordSeconds >=3f) {
//            if (isSendRecord) {
//                onUpload(audioFilePath);
//            } else {
//                Toast.makeText(getContext(), "取消录音", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(getContext(), "录音太短...", Toast.LENGTH_SHORT).show();
//        }

            mTvClear.setText("清除");
            mTvRecordTime.setVisibility(VISIBLE);
            mIvRecord.setOnTouchListener(null);
            mIvRecord.setBackgroundResource(R.drawable.story_recorder_disable);
        }else if(mRecordStauts == -1) {
            reset();
        }
        mRecordStauts = 0;
    }

    private void startRecord(){
        initRecorder();
        mRecordSeconds = 0;
        try {
            mRecorder.prepare();
            mRecorder.start();
            mRecordStartTime = System.currentTimeMillis();
            mRecordStauts = 1;
        } catch (Exception e) {
            mRecordStauts = -1;
            ToastUtiles.showDefault(getContext(), "请开启录音权限后录音");
            e.printStackTrace();
            return;
        }
        int sw = PublicMethod.getDisplayWidth(getContext())/3;
        final int sp = Math.max(sw/120,1);
        mTimer = new CountDownTimer(1000*60,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                LayoutParams params = (LayoutParams) mRlRecorder.getLayoutParams();
                params.width += sp;
                mRlRecorder.setLayoutParams(params);
            }

            @Override
            public void onFinish() {
                stopRecord();
            }
        };
        mTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clear:
                if(mRecordSeconds > -1){
                   onClearAudio();
                }else {
                    this.close();
                }
                break;
            case R.id.tv_save:
               onSave();
                break;
        }
    }

    private void onSave() {
        if(mRecordSeconds > -1) {
            mItemVo.setAudioUrl(mAudioFilePath);
            mItemVo.setAudioSeconds(mRecordSeconds);
            this.close();
        }else {
            ToastUtiles.showDefault(getContext(),"请先录音后再保存");
        }
    }

    private void onClearAudio() {
        if(!TextUtils.isEmpty(mAudioFilePath)){
            File file = new File(mAudioFilePath);
            if(file.exists())
                file.delete();
            mAudioFilePath = "";
        }
        reset();
    }

    public StoryRecorderCallback getCallback() {
        return mCallback;
    }

    public void setCallback(StoryRecorderCallback Callback) {
        this.mCallback = Callback;
    }

    public interface StoryRecorderCallback{
        void onClose();
    }
}

