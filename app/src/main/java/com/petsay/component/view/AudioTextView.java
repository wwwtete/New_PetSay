package com.petsay.component.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.constants.Constants;
import com.petsay.application.UserManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;

import java.io.File;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/21
 * @Description
 */
public class AudioTextView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener{

    private RelativeLayout mRlInput;
    private ImageButton mBtnFace;
    private Button mBtnSend;
    private EditText mEvInput;
    private LinearLayout mRlRecord;
    private ImageButton mBtnKeyboard;
    private Button mBtnRecord;
    private RelativeLayout mLlFacechoose;
    private ViewPager mVpContains;
    private LinearLayout mIvImage;
    private ImageView mIvRecord;
    private VolumeView mVolumeView;
    private AudioTextViewCallback mCallback;
    private MediaRecorder mRecorder;
    private boolean mRecordRunning;
    private long mRecordStartTime;
    private int mRecordSeconds = 0;
    private int mMaxRecorderTime = 60;
    private boolean mEnable = true;
    private File mRecordFile;
    private Handler mHandler = new Handler(){};

    public AudioTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.audio_text_view, this);
        findViews();
    }

    private void findViews() {
        mRlInput = (RelativeLayout)findViewById( R.id.rl_input );
        mBtnFace = (ImageButton)findViewById( R.id.btn_face );
        mBtnSend = (Button)findViewById( R.id.btn_send );
        mEvInput = (EditText)findViewById( R.id.et_sendmessage );
        mRlRecord = (LinearLayout)findViewById( R.id.layout_record );
        mBtnKeyboard = (ImageButton)findViewById( R.id.btn_keyboard );
        mBtnRecord = (Button)findViewById( R.id.btn_record );
        mLlFacechoose = (RelativeLayout)findViewById( R.id.ll_facechoose );
        mVpContains = (ViewPager)findViewById( R.id.vp_contains );
        mIvImage = (LinearLayout)findViewById( R.id.iv_image );
        mIvRecord = (ImageView) findViewById(R.id.iv_record);
        mVolumeView = (VolumeView) findViewById(R.id.volume);

        mBtnFace.setOnClickListener(this);
        mBtnRecord.setOnTouchListener(this);
        mBtnKeyboard.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mIvRecord.setOnClickListener(this);
        mBtnKeyboard.setOnClickListener(this);
    }

    /**
     * 最长录音时间(单位秒)
     * @param seconds
     */
    public void setMaxRecorderTime(int seconds){
        this.mMaxRecorderTime = seconds;
    }

    public void setCallback(AudioTextViewCallback callback){
        this.mCallback = callback;
    }

    public void closeKeyBoard(){
        PublicMethod.closeSoftKeyBoard(getContext(),mEvInput);
    }

    public void clearText(){
        mEvInput.setText("");
    }

    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        this.mEnable = enable;
        mEvInput.setEnabled(enable);
    }

    public void setEditTextFocusable(boolean focusable) {
        if(focusable) {
            mEvInput.requestFocus();
        }else {
            mEvInput.clearFocus();
        }
        mEvInput.setFocusable(focusable);
        mEvInput.setFocusableInTouchMode(focusable);
    }

    @Override
    public void onClick(View v) {
        if(!mEnable){
            PublicMethod.showToast(getContext(),"您不能给对方发送消息");
            return;
        }
        switch (v.getId()){
            case R.id.btn_send:
                onSendText();
                break;
            case R.id.btn_record:
                break;
            case R.id.iv_record:
                onShowRecordView();
                break;
            case R.id.btn_keyboard:
                onShowTextView();
                break;
        }
    }

    private void onShowTextView() {
        mRlRecord.setVisibility(GONE);
        mRlInput.setVisibility(VISIBLE);
        mEvInput.requestFocus();
        PublicMethod.openSoftKeyBoard(mEvInput,0);
        mRecordFile = null;
    }

    private void onShowRecordView() {
        closeKeyBoard();
        mRlInput.setVisibility(GONE);
        mRlRecord.setVisibility(View.VISIBLE);
    }

    private void onSendText() {
        if(mCallback != null){
            mCallback.onSendTextCallback(mEvInput.getText().toString());
        }
        clearText();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startRecord();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                stopRecord();
                break;
        }
        return true;
    }

    private void stopRecord() {
        mBtnRecord.setText("按住说话");
        if(mCallback != null)
            mCallback.onStopRecorder();
        mVolumeView.setVisibility(GONE);
        mRecordRunning = false;
        release();
        mRecordSeconds = (int)((System.currentTimeMillis() - mRecordStartTime)/1000);
        mRecordSeconds = mRecordSeconds > mMaxRecorderTime ? mMaxRecorderTime : mRecordSeconds;
        if(mRecordSeconds > 1){
            if(mCallback != null){
                mCallback.onSendAudioCallback(mRecordFile,mRecordSeconds);
            }
        }else {
            if(mRecordFile != null){
                if(mRecordFile.exists()){
                    mRecordFile.delete();
                }
            }
        }
        mRecordFile = null;
    }

    private void release(){
        if(mRecorder != null){
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void startRecord() {
        mBtnRecord.setText("松开结束");
        if(mCallback != null)
            mCallback.onStartRecorder();
        release();
        String path = FileUtile.getPath(getContext(), Constants.CHAT_SOUND_SEND)+getPetId()+"_"+System.currentTimeMillis()+".amr";
        initMediaRecorder(path);
        mRecordRunning = true;
        mVolumeView.setVisibility(VISIBLE);
        updateVoluem();
    }

    private String getPetId(){
        return UserManager.getSingleton().getActivePetId();
    }

    private void updateVoluem() {
        if(mRecorder != null && mRecordRunning){
            int ratio = mRecorder.getMaxAmplitude() / 600;
            int db = 10;// 分贝
            if(ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            mVolumeView.volumeChange(db);
            mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(((int) (System.currentTimeMillis() - mRecordStartTime)))+"“");
            //检查是否达到最长录音时间
            mRecordSeconds = (int)((System.currentTimeMillis() - mRecordStartTime)/1000);
            PublicMethod.log_d("录音时长：" + mRecordSeconds+"秒");
            if(mRecordSeconds >= mMaxRecorderTime){
                mRecordRunning  = false;
                stopRecord();
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, 200);
        }
    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateVoluem();
        }
    };

    private void initMediaRecorder(String path){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecordFile = new File(path);
        mRecorder.setOutputFile(path);
        try {
            mRecorder.prepare();
            mRecorder.start();
            mRecordStartTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AudioTextViewCallback {
        public void onSendTextCallback(String text);
        public void onStartRecorder();
        public void onStopRecorder();
        public void onSendAudioCallback(File voicefile,int voiceSeconds);
    }

}
