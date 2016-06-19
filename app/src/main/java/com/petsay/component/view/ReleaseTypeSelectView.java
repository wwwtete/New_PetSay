package com.petsay.component.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.petalk.CameraActivity;
import com.petsay.activity.story.EditBeginStoryActivity;
import com.petsay.cache.SharePreferenceCache;

public class ReleaseTypeSelectView extends RelativeLayout implements
		OnClickListener {

	private Context mContext;
	private ImageView imgCancel, imgPetalk, imgPic,imgStory,ivHintStory;

	public ReleaseTypeSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(mContext, R.layout.release_type_sel, this);
        this.setBackgroundColor(Color.parseColor("#33000000"));
		imgCancel = (ImageView) findViewById(R.id.img_cancel);
		imgPetalk = (ImageView) findViewById(R.id.img_petalk);
		imgPic = (ImageView) findViewById(R.id.img_pic);
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		imgCancel.setOnClickListener(this);
		imgPetalk.setOnClickListener(this);
		imgPic.setOnClickListener(this);
        imgStory = (ImageView) findViewById(R.id.img_story);
        ivHintStory = (ImageView) findViewById(R.id.iv_hint_story);
        imgStory.setOnClickListener(this);
        this.setOnClickListener(this);
		

	}
	public void show(){
		setVisibility(VISIBLE);
        ivHintStory.setVisibility(GONE);
        Animation cancelAnim = AnimationUtils.loadAnimation(mContext,R.anim.publish_petalk_rotate);
        cancelAnim.setFillAfter(true);
        imgCancel.startAnimation(cancelAnim);
		Animation animation1 = AnimationUtils.loadAnimation(mContext,R.anim.release_petalk_bottom_in);
        animation1.setStartOffset(300);
		Animation animation2=AnimationUtils.loadAnimation(mContext,R.anim.release_petalk_bottom_in);
        animation2.setStartOffset(400);
		imgPetalk.startAnimation(animation1);
		imgPic.startAnimation(animation2);
        imgStory.startAnimation(animation2);
        if(onCheckShowHint()) {
            animation2.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ivHintStory.setVisibility(VISIBLE);
                    SharePreferenceCache.getSingleton(mContext).setBooleanValue("releasetypeselectview_first",false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
	}

    private boolean onCheckShowHint() {
        return SharePreferenceCache.getSingleton(mContext).getBooleanValue("releasetypeselectview_first",true);
    }

    public void hide(){
        ivHintStory.setVisibility(GONE);
		Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.bottom_out);
		imgPetalk.startAnimation(animation);
		imgPic.startAnimation(animation);
        imgStory.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {

			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(GONE);
				
			}
		});
		
		  Animation cancelAnim = AnimationUtils.loadAnimation(mContext,R.anim.publish_petalk_rotateback);
	        cancelAnim.setFillAfter(true);
	        imgCancel.startAnimation(cancelAnim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_cancel:
			hide();
			break;
		case R.id.img_petalk:
            hide();
			Intent intent = new Intent(mContext, CameraActivity.class);
			intent.putExtra("model", 0);
			mContext.startActivity(intent);
			break;
		case R.id.img_pic:
            hide();
			intent = new Intent(mContext, CameraActivity.class);
			intent.putExtra("model", 1);
			mContext.startActivity(intent);
			break;
            case R.id.img_story:
                hide();
                intent = new Intent(mContext, EditBeginStoryActivity.class);
                mContext.startActivity(intent);
                break;
		default:
            hide();
			break;
		}

	}

}
