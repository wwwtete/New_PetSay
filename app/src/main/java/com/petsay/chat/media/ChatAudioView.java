package com.petsay.chat.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/23
 * @Description
 */
public class ChatAudioView extends RelativeLayout {

    private FrameLayout flVoluem;
    private ImageView ivVoluem;
    private TextView tvTime;
    private AnimationDrawable mDrawable;
    private boolean mIsCommsg;

    public ChatAudioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ChatAudioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.chataudioview,defStyle,0);
        mIsCommsg = array.getBoolean(R.styleable.chataudioview_iscommsg,false);
        initView();
    }


    private void initView() {
        if(mIsCommsg)
            inflate(getContext(),R.layout.chatfrom_audio_view,this);
        else
            inflate(getContext(),R.layout.chatto_audio_view,this);
        findViews();

    }

    private void findViews() {
        flVoluem = (FrameLayout)findViewById( R.id.fl_voluem );
        ivVoluem = (ImageView)findViewById( R.id.iv_voluem );
        tvTime = (TextView)findViewById( R.id.tv_time );
        mDrawable = (AnimationDrawable) ivVoluem.getDrawable();
        stopAnimation();
    }

    public void reset(){
    }

    public void playAnimation(){
        if(mDrawable != null && !mDrawable.isRunning())
            mDrawable.start();
    }

    public void stopAnimation(){
        if(mDrawable != null){
            mDrawable.selectDrawable(0);
            mDrawable.stop();
        }
    }

    public TextView getTimeText(){
        return tvTime;
    }

    public void setAudioSecond(String time){
//        if(TextUtils.isEmpty(createTime))
            this.tvTime.setText(time);
//        else
//            this.tvTime.setVisibility(GONE);
    }

    public void setVoluemWidth(int width){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flVoluem.getLayoutParams();
        params.width = width;
        flVoluem.setLayoutParams(params);
    }

}
