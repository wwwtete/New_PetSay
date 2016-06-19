package com.petsay.component.view.forum;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.component.view.ExCircleView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.PicDTO;
import com.petsay.vo.forum.TalkDTO;

/**
 * 图文混排
 * @author gaojian
 *
 */
public class ImageTextView extends LinearLayout implements NetCallbackInterface{

	private LinearLayout mLayout,mLayoutImg;
	private TextView mTvTalk;
	private TextView mTvFavourCount,mTvTalkPetName,mTvDate;
	private ExCircleView mHeadview;
	private ImageView mImgSex;
	private ImageView mImgFavour;
	private LinearLayout mLayoutFavour;
	private PicDTO[] mDtos;
	private String mContent;
	private TopicNet mTopicNet;
	private TalkDTO mTalkDTO;
	
	
	public ImageTextView(Context context) {
		super(context);
		inflate(context, R.layout.component_image_text_view, this);
		initData();
		initView();
	}
	
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.component_image_text_view, this);
		initData();
		initView();
	}
	private void initData(){
		mTopicNet=new TopicNet();
		mTopicNet.setCallback(this);
		mTopicNet.setTag(getContext());
	}

	private void initView() {
		mLayout=(LinearLayout) findViewById(R.id.layout_talkparent);
		mLayoutImg=(LinearLayout) findViewById(R.id.layout_img);
		mTvTalk=(TextView) findViewById(R.id.tv_talkcontent);
		mTvTalkPetName=(TextView) findViewById(R.id.tv_name_talk);
		mTvFavourCount=(TextView) findViewById(R.id.tv_favourcount_talk);
		mTvDate=(TextView) findViewById(R.id.tv_date_talk);
		mImgSex=(ImageView) findViewById(R.id.img_sex_talk);
		mHeadview=(ExCircleView) findViewById(R.id.headview_talk);
		mImgFavour=(ImageView) findViewById(R.id.img_favour_talk);
		mLayoutFavour=(LinearLayout) findViewById(R.id.layout_favour_talk);
	}
	
	/**
	 * 设置图文混排内容
	 * @param text
	 * @param urls
	 */
	public void setContent(String text,final TalkDTO talkDTO){
		mTalkDTO=talkDTO;
		mDtos=talkDTO.getPictures();
		mContent=text;
		if (talkDTO.getPetGender().equals("0")) {
			mImgSex.setVisibility(View.VISIBLE);
			mImgSex.setImageResource(R.drawable.female);

		}else if (talkDTO.getPetGender().equals("1")) {
			mImgSex.setVisibility(View.VISIBLE);
			mImgSex.setImageResource(R.drawable.male);
		}else {
			mImgSex.setVisibility(View.GONE);
		}
		mTvTalk.setText(mContent);
		mTvDate.setText(PublicMethod.calculateTopicTime(talkDTO.getCreateTime(),false));
		mTvTalkPetName.setText(talkDTO.getPetNickName());
		mHeadview.setBackgroudImage(talkDTO.getPetAvatar());
		mTvTalkPetName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityTurnToManager.getSingleton().userHeaderGoto(getContext(), talkDTO.getPetId());
			}
		});
		mHeadview.setClickHeaderToTurn(talkDTO.getPetId());
		if (talkDTO.getIsLiked()) {
			mImgFavour.setImageResource(R.drawable.step_already);
		} else {
			mImgFavour.setImageResource(R.drawable.step_ico);
		}
		mTvFavourCount.setText(talkDTO.getLikeCount()+"");
		mLayout.setPadding(10, 10, 10, 10);
		mLayoutImg.removeAllViews();
		int width=PublicMethod.getDisplayWidth(getContext())-20;
		for (int i = 0; i < mDtos.length; i++) {
			int height=(int) (width/mDtos[i].getScaleWH());
			LinearLayout.LayoutParams layoutParams=new LayoutParams(width, height);
			layoutParams.setMargins(0, 10, 0, 0);
			ImageView img=new ImageView(getContext());
			img.setLayoutParams(layoutParams);
			
			ImageLoaderHelp.displayContentImage(mDtos[i].getPic(), img);
			mLayoutImg.addView(img);
			
		}
		
		mLayoutFavour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getSingleton().isLoginStatus()) {
					if (mTalkDTO.getIsLiked()) {
						mTopicNet.topicCancelLike(mTalkDTO.getId(), UserManager.getSingleton().getActivePetId(),"");
					}else {
						mTopicNet.topicCreateLike(mTalkDTO.getId(), UserManager.getSingleton().getActivePetId(),"");
					}
				}else {
					Intent intent=new Intent(getContext(),UserLogin_Activity.class);
					getContext().startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_TOPICCREATELIKE:
			mTalkDTO.setIsLiked(true);
			mTalkDTO.setLikeCount(mTalkDTO.getLikeCount()+1);
//			int favourCount=Integer.parseInt(holder.tvFavourCount.getText().toString());
			mTvFavourCount.setText(mTalkDTO.getLikeCount()+"");
			mImgFavour.setImageResource(R.drawable.step_already);
			break;
		case RequestCode.REQUEST_TOPICCANCELLIKE:
			mTalkDTO.setIsLiked(false);
			mTalkDTO.setLikeCount(mTalkDTO.getLikeCount()-1);
			mTvFavourCount.setText(mTalkDTO.getLikeCount()+"");
			mImgFavour.setImageResource(R.drawable.step_ico);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
	    PublicMethod.showToast(getContext(), "操作失败");	
	}
	
	

}
