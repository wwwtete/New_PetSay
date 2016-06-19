package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.vo.petalk.VoiceEffect;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/4
 * @Description
 */
public class AudioEffectsView extends FrameLayout {

    private ViewGroup mContainer;
    private ClickAudioEffectsCallback mCallback;
    private View mCurrView;

    public AudioEffectsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setCallback(ClickAudioEffectsCallback callback){
        this.mCallback = callback;
    }

    private void initView() {
        inflate(getContext(), R.layout.audio_effects_view,this);
        mContainer = (ViewGroup) findViewById(R.id.ll_container);
        addChildren();
    }

    private void addChildren() {
        List<VoiceEffect> list = VoiceEffect.getAudioDatas();
        for(int i=0;i<list.size();i++){
            VoiceEffect data = list.get(i);
            View view = inflate(getContext(), R.layout.edit_list_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.iv_img);
            TextView txt = (TextView) view.findViewById(R.id.tv_title);
            txt.setVisibility(View.VISIBLE);
            img.setImageResource(data.imgSource);
            txt.setText(data.title);
            view.setTag(data);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    VoiceEffect effect = (VoiceEffect) v.getTag();
                    if(mCallback != null){
                        mCallback.onClickAudioEffects(effect,v);
                    }
                    if(v != mCurrView){
                        setSelectView(v);
                    }
                }
            });
            mContainer.addView(view);
            if(i == 0 )
                setSelectView(view);
        }
    }

    private void setSelectView(View v){
        if(mCurrView != null) {
            View bg = mCurrView.findViewById(R.id.fl_container);
            if(bg != null)
                bg.setBackgroundDrawable(null);
        }
        View temp = v.findViewById(R.id.fl_container);
        if(temp != null){
            temp.setBackgroundResource(R.drawable.decoration_select_shape);
        }
        mCurrView = v;
    }


    public interface ClickAudioEffectsCallback{
        public void onClickAudioEffects(VoiceEffect effect,View view);
    }

}
