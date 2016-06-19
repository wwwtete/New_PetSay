package com.petsay.component.gifview;

import android.content.Context;
import android.util.AttributeSet;

import com.petsay.vo.decoration.DecorationBean;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

/**
 * @author wangw
 *
 */
public class AnimationGifView extends BaseGifView{ //extends ImageView implements DownloadListener{

	protected PetalkVo mPetalk;
	protected DecorationBean mDecorationBean;
	protected PetalkDecorationVo mData;
	protected int mWidth = -1;
	protected int mHeight = -1;
//
	public AnimationGifView(Context context){
		super(context);
	}

	public AnimationGifView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    public void initData(PetalkVo talk,int width,int height){
        if(talk != null && talk.isAudioModel()) {
            this.mData = talk.getDecorations()[0];
            this.mDecorationBean = mData.getOrigin();
            this.mWidth = width;
            this.mHeight = height;
            super.initData(mDecorationBean);
        }else {
            reset();
        }
        this.mPetalk = talk;
    }

    @Override
    public void reset() {
        super.reset();
        mPetalk = null;
        mData = null;
        mDecorationBean = null;
        mWidth = 0;
        mHeight = 0;
    }

    public PetalkVo getData(){
        return mPetalk;
    }

}
