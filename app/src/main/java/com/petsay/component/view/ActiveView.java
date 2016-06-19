package com.petsay.component.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.petsay.activity.petalk.ALLSayListActivity;
import com.petsay.activity.petalk.ChannelSayListActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.homeview.SquareActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.award.AwardListActivity;
import com.petsay.activity.topic.TopicDetailActivity;
import com.petsay.activity.petalk.rank.RankActivity;
import com.petsay.constants.Constants;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.SquareVo;
import com.petsay.vo.petalk.PetalkVo;

public class ActiveView extends LinearLayout{
	

	private int _1_3;
//	private int _2_3;
	private int screenWidth;
	private int viewWidth;
	private Context mContext;
	private List<SquareVo> mSquareVos;
	public ActiveView(Context context) {
		super(context);
		mContext=context;
		init();
		
	}
	
	public ActiveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
		
	}

	public void initDataAndViews(List<SquareVo> tags,int flag){
		mSquareVos=tags;
		
		if (flag==0) {
			getView1();
		}else {
			getView2();
		}
	}
	
    public void getView1(){
//    	List<TagInfo> infos=mTags.subList(start, end)
    	int len =mSquareVos.size();
    	switch (len) {
		case 0:
			break;
		case 1:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			break;
		case 2:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 2)));
			break;
		case 3:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 3)));
			break;
		case 4:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			break;
		case 5:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 5)));
			break;
		case 6:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 6)));
			break;
		case 7:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
			break;
		case 8:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			break;
		case 9:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 9)));
			break;
		case 10:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 10)));
			break;
		case 11:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 10)));
			addView(get11_11_22_3View(5, mSquareVos.subList(10, 11)));
			break;
		case 12:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 10)));
			addView(get11_11_22_3View(5, mSquareVos.subList(10, 12)));
			break;
		case 13:
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 10)));
			addView(get11_11_22_3View(5, mSquareVos.subList(10, 13)));
			break;
		default :
			addView(get31_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_11_3View(5,mSquareVos.subList(1, 4)));
			addView(get22_11_11_3View(5,mSquareVos.subList(4, 7)));
	    	addView(get31_1View(5,mSquareVos.subList(7, 8)));
			addView(get11_21_2View(5, mSquareVos.subList(8, 10)));
			addView(get11_11_22_3View(5, mSquareVos.subList(10, 13)));
			for (int i = 13; i < len; i++) {
				addView(get31_1View(5,mSquareVos.subList(i, i+1)));
			}
			break;
		}
    }
    
    public void getView2(){		
		int len =mSquareVos.size();
    	switch (len) {
		case 0:
			break;
		case 1:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			break;
		case 2:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 2)));
			break;
		case 3:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 3)));
			break;
		case 4:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			break;
		case 5:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 5)));
			break;
		case 6:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			break;
		case 7:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 7)));
			break;
		case 8:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 8)));
			break;
		case 9:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 9)));
			break;
		case 10:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 9)));
			addView(get11_21_2View(5, mSquareVos.subList(9, 10)));
			break;
		case 11:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 9)));
			addView(get11_21_2View(5, mSquareVos.subList(9, 11)));
			break;
		default:
			addView(get32_1View(0,mSquareVos.subList(0, 1)));
			addView(get11_11_22_3View(5,mSquareVos.subList(1, 4)));
			addView(get11_21_2View(5,mSquareVos.subList(4, 6)));
			addView(get22_11_11_3View(5,mSquareVos.subList(6, 9)));
			addView(get11_21_2View(5, mSquareVos.subList(9, 11)));
			for (int i = 11; i < len; i++) {
				addView(get31_1View(5,mSquareVos.subList(i, i+1)));
			}
			break;
    	}
    }
	
	
	
	private void init(){
		setOrientation(LinearLayout.VERTICAL);
		screenWidth=PublicMethod.getDisplayWidth(mContext);
		viewWidth=screenWidth-PublicMethod.getPxInt(10, mContext);
		_1_3=viewWidth/3;
//		_2_3=_1_3*2;
	}
	
	/**
	 * 3*1
	 * @return
	 */
	private View get31_1View(int top,List<SquareVo> tags){
		LinearLayout layout=new LinearLayout(mContext);
		for (int i = 0; i <tags.size();i++) {
			LinearLayout.LayoutParams layoutParams=new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,_1_3);
			TagImageView tagImageView=new TagImageView(mContext);
			tagImageView.setLayoutParams(layoutParams);
			tagImageView.setTagText(tags.get(i).getTitle());
			tagImageView.setTagImgUrl(tags.get(i).getIconUrl());
			layout.addView(tagImageView);
			layout.setPadding(0, top, 0, 0);
			tagImageView.setOnClickListener(new MyTagClickListener(tags.get(i)));
		}
		return layout;
	}
	
	private View get32_1View(int top,List<SquareVo> tags){
		LinearLayout layout=new LinearLayout(mContext);
		for (int i = 0; i <tags.size();i++) {
			LinearLayout.LayoutParams layoutParams=new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,_1_3*2);
			TagImageView tagImageView=new TagImageView(mContext);
			tagImageView.setLayoutParams(layoutParams);
			tagImageView.setTagText(tags.get(i).getTitle());
//			tagImageView.setTagImgRes(R.drawable.pet);
			tagImageView.setTagImgUrl(tags.get(i).getIconUrl());
			tagImageView.setOnClickListener(new MyTagClickListener(tags.get(i)));
			layout.addView(tagImageView);
			layout.setPadding(0, top, 0, 0);
		}
		return layout;
	}
	
	private View get11_11_11_3View(int top,List<SquareVo> tags){
		int itemWidth=(viewWidth-10)/3;
		LinearLayout layout=new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 0; i < tags.size(); i++) {
			LinearLayout.LayoutParams layoutParams=new LayoutParams(itemWidth,itemWidth);
			TagImageView tagImageView=new TagImageView(mContext);
			if (i < 2) {
				layoutParams.setMargins(0, 0, 5, 0);
			}
			tagImageView.setLayoutParams(layoutParams);
			tagImageView.setTagText(tags.get(i).getTitle());
//			tagImageView.setTagImgRes(R.drawable.pet);
			tagImageView.setTagImgUrl(tags.get(i).getIconUrl());
			tagImageView.setOnClickListener(new MyTagClickListener(tags.get(i)));
			layout.addView(tagImageView);
		}
		layout.setPadding(0, top, 0, 0);
		return layout;
	}
	
	private View get22_11_11_3View(int top,List<SquareVo> tags){
		int itemWidth=(viewWidth-10)/3; 
		int _2_2width=itemWidth*2+5;
		int _1_1width=itemWidth;
		
		LinearLayout layout=new LinearLayout(mContext);
		int len=tags.size();
		if (len>=1) {
			layout.setPadding(0, top, 0, 0);
			LinearLayout.LayoutParams params_2_2=new LayoutParams(_2_2width,_2_2width);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			TagImageView tagImageView=new TagImageView(mContext);
			tagImageView.setLayoutParams(params_2_2);
			tagImageView.setTagText(tags.get(0).getTitle());
//			tagImageView.setTagImgRes(R.drawable.pet);
			tagImageView.setTagImgUrl(tags.get(0).getIconUrl());
			tagImageView.setOnClickListener(new MyTagClickListener(tags.get(0)));
			layout.addView(tagImageView);
			
			LinearLayout layout_1_1=new LinearLayout(mContext);
			layout_1_1.setOrientation(VERTICAL);
			for (int i = 1; i < len; i++) {
				LinearLayout.LayoutParams params_1_1=new LayoutParams(_1_1width,_1_1width);
				TagImageView tagImageView1_1=new TagImageView(mContext);
				if (i<2) {
					params_1_1.setMargins(5, 0, 0, 5);
				}else {
					params_1_1.setMargins(5, 0, 0, 0);
				}
				tagImageView1_1.setLayoutParams(params_1_1);
				tagImageView1_1.setTagText(tags.get(i).getTitle());
//				tagImageView.setTagImgRes(R.drawable.pet);
				tagImageView1_1.setTagImgUrl(tags.get(i).getIconUrl());
				tagImageView1_1.setOnClickListener(new MyTagClickListener(tags.get(i)));
				layout_1_1.addView(tagImageView1_1);
			}
			layout.addView(layout_1_1);
		}
		return layout;
	}
	
	private View get11_21_2View(int top,List<SquareVo> tags) {
		int itemWidth = (viewWidth - 10) / 3;
		int _2_2width=itemWidth*2+5;
		int _1_1width=viewWidth-5-_2_2width;
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		if (tags.size()>=1) {
			layout.setPadding(0, top, 0, 0);
			LinearLayout.LayoutParams params_1_1 = new LayoutParams(_1_1width,_1_1width);
			TagImageView tagImageView_1_1 = new TagImageView(mContext);
			params_1_1.setMargins(0, 0, 5, 0);
			tagImageView_1_1.setLayoutParams(params_1_1);
			tagImageView_1_1.setTagText(tags.get(0).getTitle());
			tagImageView_1_1.setTagImgUrl(tags.get(0).getIconUrl());
			tagImageView_1_1.setOnClickListener(new MyTagClickListener(tags.get(0)));
			layout.addView(tagImageView_1_1);
			if (tags.size()==2) {
				LinearLayout.LayoutParams params_2_1 = new LayoutParams(_2_2width,_1_1width);
				TagImageView tagImageView_2_1 = new TagImageView(mContext);
				tagImageView_2_1.setLayoutParams(params_2_1);
				tagImageView_2_1.setTagText(tags.get(1).getTitle());
				tagImageView_2_1.setTagImgUrl(tags.get(1).getIconUrl());
				tagImageView_2_1.setOnClickListener(new MyTagClickListener(tags.get(1)));
				layout.addView(tagImageView_2_1);
				
			}
		}
		return layout;
	}
	
	private View get11_11_22_3View(int top,List<SquareVo> tags){
		int itemWidth=(viewWidth-10)/3;
		int _2_2width=itemWidth*2+5;
		int _1_1width=viewWidth-5-_2_2width;
		int len =tags.size();
		LinearLayout.LayoutParams params_2_2=new LayoutParams(_2_2width,_2_2width);
		LinearLayout layout=new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout layout_1_1=new LinearLayout(mContext);
		layout_1_1.setOrientation(VERTICAL);
		for (int i = 0; i < len; i++) {
			if (i<2) {
				layout.setPadding(0, top, 0, 0);
				LinearLayout.LayoutParams params_1_1=new LayoutParams(_1_1width,_1_1width);
				TagImageView tagImageView1_1=new TagImageView(mContext);
				if (i<1) {
					params_1_1.setMargins(0, 0, 5, 5);
				}else {
					params_1_1.setMargins(0, 0, 5, 0);
				}
				tagImageView1_1.setLayoutParams(params_1_1);
				tagImageView1_1.setTagText(tags.get(i).getTitle());
//				tagImageView.setTagImgRes(R.drawable.pet);
				tagImageView1_1.setTagImgUrl(tags.get(i).getIconUrl());
				tagImageView1_1.setOnClickListener(new MyTagClickListener(tags.get(i)));
				layout_1_1.addView(tagImageView1_1);
			}
			
		}
		layout.addView(layout_1_1);
		if (len==3) {
			TagImageView tagImageView=new TagImageView(mContext);
			tagImageView.setLayoutParams(params_2_2);
			tagImageView.setTagText(tags.get(2).getTitle());
//			tagImageView.setTagImgRes(R.drawable.pet);
			tagImageView.setTagImgUrl(tags.get(2).getIconUrl());
			tagImageView.setOnClickListener(new MyTagClickListener(tags.get(2)));
			layout.addView(tagImageView);
		}
		return layout;
	}

	class MyTagClickListener implements OnClickListener {
		private SquareVo mSquareVo;
		public MyTagClickListener(SquareVo squareVo) {
			mSquareVo=squareVo;
		}

		@Override
		public void onClick(View v) {
			switch (mSquareVo.getHandleType()) {
			case 1:
				break;
			case 2:
				Intent intent=new Intent(mContext, TagSayListActivity.class);
				intent.putExtra("id", mSquareVo.getKey());
				intent.putExtra("folderPath", mSquareVo.getTitle());
				mContext.startActivity(intent);
				break;
			case 3:
			case 4:
				 intent=new Intent(mContext, ChannelSayListActivity.class);
				 intent.putExtra("squareVo", mSquareVo);
				 mContext.startActivity(intent);
				break;
			case 5:
				intent=new Intent(mContext, ALLSayListActivity.class);
				intent.putExtra("squareVo", mSquareVo);
				mContext.startActivity(intent);
				break;
			 case 6:
				// PublicMethod.showToast(mContext, "广场跳转");
				intent = new Intent(mContext, SquareActivity.class);
				intent.putExtra("squareVo", mSquareVo);
				mContext.startActivity(intent);
				break;
			case 7:
				intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra("url", mSquareVo.getKey());
				mContext.startActivity(intent);
				break;
			case 8:
				intent = new Intent(mContext, DetailActivity.class);
				PetalkVo petalkVo = new PetalkVo();
				petalkVo.setPetalkId(mSquareVo.getKey());
				Constants.Detail_Sayvo = petalkVo;
				mContext.startActivity(intent);
				break;
			case 9:
				// TODO 排行榜
				mContext.startActivity(new Intent(getContext(),
						RankActivity.class));
				break;
			case 10:
				// TODO 奖品列表
			    if (UserManager.getSingleton().isLoginStatus()) {
			    	intent = new Intent(mContext, AwardListActivity.class);
			    	mContext.startActivity(intent);
				}else {
					intent = new Intent(mContext, UserLogin_Activity.class);
			    	mContext.startActivity(intent);
				}
				break;
			case 11:
  			  intent =new Intent(mContext, TopicDetailActivity.class);
                mContext.startActivity(intent);
  			break;
			default:
				break;
			}
			
		}

	}
}