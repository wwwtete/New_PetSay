package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.PetVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/16
 * @Description 带角标的圆形图片
 */
public class ExCircleView extends RelativeLayout {

    private CircleImageView mImg_bg;
    private ImageView mImg_BR;

    public ExCircleView(Context context) {
        super(context);
        initView();
    }

    public ExCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.excircle_view,this);
        mImg_bg = (CircleImageView) findViewById(R.id.img_bg);
        mImg_BR = (ImageView) findViewById(R.id.img_br);
    }


    public void setBackgroudImage(String url){
        ImageLoaderHelp.displayHeaderImage(url, mImg_bg);
    }

    public void setBackgroudImage(int resID){
        mImg_bg.setImageResource(resID);
    }

    public void setBottomRightImage(String url){
        mImg_BR.setVisibility(VISIBLE);
        ImageLoaderHelp.displayHeaderImage(url, mImg_BR);
    }

    public void setBottomRightImage(int resID){
        mImg_BR.setVisibility(VISIBLE);
        mImg_BR.setImageResource(resID);
    }

    public ImageView getBottomRightImg(){
        return mImg_BR;
    }

    public CircleImageView getBackgroudImg(){
        return mImg_bg;
    }

    public void setClickHeaderToTurn(final String petId){
    	setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PetVo info=new PetVo();
				info.setId(petId);
//				info.setNickName(talkDTO.getPetNickName());
//				info.setHeadPortrait(commentVo.getAimPetHeadPortrait());
				ActivityTurnToManager.getSingleton().userHeaderGoto(getContext(),info );
				
			}
		});
    	
    }


}
