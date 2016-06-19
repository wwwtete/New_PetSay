package com.petsay.component.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetTagVo;

/**
 * 自定义标签布局
 * 
 * @author G
 * 
 */
public class TagView extends LinearLayout {

	private Context mContext;
	private PetTagVo[] mTags;
	private List<PetTagVo> mTagList;
	private int tagTextColor=Color.WHITE;
	private float tagTextSize=15;
	private int mTagBgResID=0;
	private int mDrawableLeftResId=-1;
	//使用类型 0说说列表    1发布
	private int useType=0;
	public  final int Use_SayList=0,Use_Release_UnSelected=1,Use_Release_Selected=2;
	public static final int Selected_Tag=3;
	public static final int Remove_Tag=4;
	private Handler mHandler;
	private int arrayIndex=0;
	private LayoutParams layoutParams;
	private int mSelectedTagBgResID;
	private int mSelectedTagTextColor;
	/*
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Use_SayList:
				int layoutWidth=msg.arg1;
				//按钮的最大宽度
				int maxWidth=layoutWidth-50;
				int index=0;
			    Drawable drawable = mContext.getResources().getDrawable(R.drawable.label);
				for (int i = 0; i <mTags.length; i++) {
					int tempWidth=layoutWidth;
					LinearLayout tagLayout=new LinearLayout(mContext);
					tagLayout.setOrientation(LinearLayout.HORIZONTAL);
					for ( int j=index; j < mTags.length; j++) {
						TextView button=new TextView(mContext);
						button.setText(mTags[j].getName());
						button.setTextColor(tagTextColor);
						button.setTextSize(tagTextSize);
//						button.setBackgroundColor(tagBackColor);
						button.setBackgroundResource(mTagBgResID);
						
						button.setMaxWidth(maxWidth);
						button.setSingleLine(true);
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
//						button.setCompoundDrawables(drawable, null, null, null);
						layoutParams.setMargins(5, 5, 0, 5);
						
						button.setLayoutParams(layoutParams);
						int width=getViewWidth(button);
						if (width<tempWidth) {
							index++;
							tagLayout.addView(button);
							tempWidth=tempWidth-width;
						}else {
							break;
						}
					}
					addView(tagLayout);
				}
				
				break;
			case Use_Release_UnSelected:
				 layoutWidth=msg.arg1;
				//按钮的最大宽度
				 maxWidth=layoutWidth-50;
				 int layoutCount=0;
				for (int i = 0; i <mTags.length; i++) {
				
					int tempWidth=layoutWidth;
					LinearLayout tagLayout=new LinearLayout(mContext);
					tagLayout.setOrientation(LinearLayout.HORIZONTAL);
					
					for ( int j=arrayIndex; j < mTags.length; j++) {
						final TextView button=new TextView(mContext);
						button.setText(mTags[j].getName());
						button.setTextColor(tagTextColor);
						button.setTextSize(tagTextSize);
						button.setBackgroundResource(mTagBgResID);
						button.setMaxWidth(maxWidth);
						button.setSingleLine(true);
						final int cur=j;
						layoutParams.setMargins(10, 5, 0, 5);
						button.setLayoutParams(layoutParams);
						button.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if (button.isSelected()) {
									button.setSelected(false);
									ReleaseTagPagerView.SelectedCount--;
									button.setBackgroundResource(mTagBgResID);
									Message message=new Message();
									message.arg1=arrayIndex;
									message.what=Remove_Tag;
									message.obj=mTags[cur];
									mHandler.sendMessage(message);
								}else {
									if (ReleaseTagPagerView.SelectedCount==4) {
										PublicMethod.showToast(mContext, "最多添加"+ReleaseTagPagerView.SelectedCount+"个标签");
									}else{
										button.setBackgroundResource(R.drawable.tag_cilck);
										button.setSelected(true);
										ReleaseTagPagerView.SelectedCount++;
//										System.out.println("选中");
										Message message=new Message();
										message.arg1=cur;
										message.what=Selected_Tag;
										message.obj=mTags[cur];
										mHandler.sendMessage(message);
									}
									
								}
							}
						});
						int width=getViewWidth(button);
						tempWidth-=10;
						if (width<tempWidth) {
							arrayIndex++;
							tagLayout.addView(button);
							tempWidth=tempWidth-width;
						}else {
							break;
						}
						
					}
					addView(tagLayout);
					layoutCount++;
//					System.out.println("mTags:=========="+mTags.length);
					if (layoutCount>0&&layoutCount%3==0&&arrayIndex<mTags.length) {
//						System.out.println("TagView.enclosing_method()==========");
						Message message=new Message();
						message.arg1=arrayIndex;
						message.what=ReleaseTagPagerView.Add_Tag;
						mHandler.sendMessage(message);
						break;
					}else if(arrayIndex==mTags.length){
						Message message=new Message();
						arrayIndex++;
						message.arg1=arrayIndex;
						message.what=ReleaseTagPagerView.Add_Tag;
						mHandler.sendMessage(message);
						break;
					}
				}
				break;
			case Use_Release_Selected:
				layoutWidth=msg.arg1;
				//按钮的最大宽度
				 maxWidth=layoutWidth-50;
				 index=0;
				for (int i = 0; i <mTagList.size(); i++) {
					int tempWidth=layoutWidth;
					LinearLayout tagLayout=new LinearLayout(mContext);
					tagLayout.setOrientation(LinearLayout.HORIZONTAL);
					for ( int j=index; j < mTagList.size(); j++) {
						TextView button=new TextView(mContext);
						button.setText(mTagList.get(j).getName());
						button.setTextColor(tagTextColor);
						button.setTextSize(tagTextSize);
						button.setBackgroundResource(mTagBgResID);
						button.setMaxWidth(maxWidth);
						button.setSingleLine(true);
						layoutParams.setMargins(5, 5, 0, 5);
						button.setLayoutParams(layoutParams);
						int width=getViewWidth(button);
						if (width<tempWidth) {
							index++;
							tagLayout.addView(button);
							tempWidth=tempWidth-width;
						}else {
							break;
						}
					}
					addView(tagLayout);
				}
				break;
			}
			
		};
	};
	
	*/

	public TagView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mContext = context;
	}

	public TagView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, PublicMethod.getPxInt(25, context));
	}

	
	private void initView(int layoutWidth) {
//		getLayoutWidth(this);
		if(useType  == Use_SayList)
			initSayTagList(layoutWidth);
//		else {
//			initPublishTagView(useType,layoutWidth);
//		}
	}
	
	@SuppressLint("NewApi")
	private void initSayTagList(int layoutWidth){
		int space = PublicMethod.getDiptopx(getContext(), 3);
		//按钮的最大宽度
		int maxWidth=layoutWidth-50;
		int index=0;
		Drawable drawable=null;  
		if (mDrawableLeftResId>0) {
			drawable= getResources().getDrawable(mDrawableLeftResId);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
		}
		
//	    Drawable drawable = mContext.getResources().getDrawable(R.drawable.label);
		for (int i = 0; i <mTags.length; i++) {
			int tempWidth=maxWidth;
			LinearLayout tagLayout=new LinearLayout(mContext);
			tagLayout.setOrientation(LinearLayout.HORIZONTAL);
			for ( int j=index; j < mTags.length; j++) {
				final TextView textView=new TextView(mContext);
				textView.setText(mTags[j].getName());
				textView.setTextColor(tagTextColor);
				textView.setTextSize(tagTextSize);
//				button.setBackgroundColor(tagBackColor);
				textView.setBackgroundResource(mTagBgResID);
				if (null!=drawable) {
					textView.setCompoundDrawables(drawable,null,null,null);  
				}
				textView.setCompoundDrawablePadding(5);
				textView.setMaxWidth(maxWidth);
				textView.setSingleLine(true);
                setListener(textView,mTags[j]);
                textView.setGravity(Gravity.CENTER);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
//				button.setCompoundDrawables(drawable, null, null, null);
//				layoutParams.setMargins(5, 5, 5, 5);
				layoutParams.setMargins(space,space, space, space);
				textView.setLayoutParams(layoutParams);
				int width=getViewWidth(textView);
				if (width<tempWidth) {
					index++;
					tagLayout.addView(textView);
					tempWidth=tempWidth-width -space;
				}else {
					break;
				}
			}
			addView(tagLayout);
		}
	}

    private void setListener(View view,PetTagVo tagVo){
        view.setTag(tagVo);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PetTagVo tagVo = (PetTagVo) v.getTag();
                if(tagVo != null) {
                    Intent intent = new Intent(mContext, TagSayListActivity.class);
                    intent.putExtra("id", tagVo.getId());
                    intent.putExtra("folderPath",tagVo.getName());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /*
	private void initPublishTagView(int key,int layoutWidth){
		int maxWidth = 0;
		switch (key) {
		case Use_Release_UnSelected:
			//按钮的最大宽度
			maxWidth=layoutWidth-50;
			 int layoutCount=0;
			for (int i = 0; i <mTags.length; i++) {
			
				int tempWidth=maxWidth;
				LinearLayout tagLayout=new LinearLayout(mContext);
				tagLayout.setOrientation(LinearLayout.HORIZONTAL);
				
				for ( int j=arrayIndex; j < mTags.length; j++) {
					final TextView button=new TextView(mContext);
					button.setText(mTags[j].getName());
					button.setTextColor(tagTextColor);
					button.setTextSize(tagTextSize);
					button.setBackgroundResource(mTagBgResID);
					button.setMaxWidth(maxWidth);
					button.setSingleLine(true);
					final int cur=j;
					layoutParams.setMargins(10, 5, 0, 5);
					button.setLayoutParams(layoutParams);
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (button.isSelected()) {
								button.setSelected(false);
//								System.out.println("删除");
//								ReleaseTagPagerView.SelectedCount--;
								button.setTextColor(tagTextColor);
								button.setBackgroundResource(mTagBgResID);
								Message message=new Message();
								message.arg1=arrayIndex;
								message.what=Remove_Tag;
								message.obj=mTags[cur];
								mHandler.sendMessage(message);
							}else {
								if (ReleaseTagPagerView.SelectedCount==4) {
									PublicMethod.showToast(mContext, "最多添加"+ReleaseTagPagerView.SelectedCount+"个标签");
								}else{
									button.setTextColor(mSelectedTagTextColor);
									button.setBackgroundResource(mSelectedTagBgResID);
									button.setSelected(true);
									ReleaseTagPagerView.SelectedCount++;
//									System.out.println("选中");
									Message message=new Message();
									message.arg1=cur;
									message.what=Selected_Tag;
									message.obj=mTags[cur];
									mHandler.sendMessage(message);
								}
								
							}
						}
					});
					int width=getViewWidth(button);
					tempWidth-=10;
					if (width<tempWidth) {
						arrayIndex++;
						tagLayout.addView(button);
						tempWidth=tempWidth-width - 10;
					}else {
						break;
					}
					
				}
				addView(tagLayout);
				layoutCount++;
				if (layoutCount>0&&layoutCount%3==0&&arrayIndex<mTags.length) {
					Message message=new Message();
					message.arg1=arrayIndex;
					message.what=ReleaseTagPagerView.Add_Tag;
					mHandler.sendMessage(message);
					break;
				}else if(arrayIndex==mTags.length){
					Message message=new Message();
					arrayIndex++;
					message.arg1=arrayIndex;
					message.what=ReleaseTagPagerView.Add_Tag;
					mHandler.sendMessage(message);
					break;
				}
			}
			break;
		case Use_Release_Selected:
			//按钮的最大宽度
			 maxWidth=layoutWidth-50;
			 int index=0;
			for (int i = 0; i <mTagList.size(); i++) {
				int tempWidth=layoutWidth;
				LinearLayout tagLayout=new LinearLayout(mContext);
				tagLayout.setOrientation(LinearLayout.HORIZONTAL);
				for ( int j=index; j < mTagList.size(); j++) {
					TextView button=new TextView(mContext);
					button.setText(mTagList.get(j).getName());
					button.setTextColor(tagTextColor);
					button.setTextSize(tagTextSize);
					button.setBackgroundResource(mTagBgResID);
					button.setMaxWidth(maxWidth);
					button.setSingleLine(true);
					layoutParams.setMargins(5, 5, 0, 5);
					button.setLayoutParams(layoutParams);
					int width=getViewWidth(button);
					if (width<tempWidth) {
						index++;
						tagLayout.addView(button);
						tempWidth=tempWidth-width -5;
					}else {
						break;
					}
				}
				addView(tagLayout);
			}
			break;
		}
	}
	*/

	/**
	 * 设置标签数据、 背景
	 * @param tags
	 * @param tagImgBgRes
	 * @param useType 使用类型 0说说列表    1发布
	 */
	public void setTags(PetTagVo[] tags,int tagBgRes,int useType,int layoutWidth) {
		removeAllViews();
		if (null==tags||tags.length==0) {
			
		}else {
			mTags = tags;
			mTagBgResID=tagBgRes;
			mDrawableLeftResId=R.drawable.tag_drawableleft;
			this.useType=useType;
			initView(layoutWidth);
		}
	}
	
	public void setIndex(int index){
		arrayIndex=index;
	}
	
	
	/**
	 * 设置标签数据、 背景
	 * @param tags
	 * @param tagImgBgRes
	 * @param useType 使用类型 0说说列表    1发布
	 * @param handler 发布时通知
	 */
	public void setTags(List<PetTagVo> tags,int tagBgRes,int useType,Handler handler,int layoutWidth) {
		mTagList = tags;
		mTagBgResID=tagBgRes;
		mHandler=handler;
		this.useType=useType;
		initView(layoutWidth);
	}
	
	public void setSelectedTagTextColor(int color){
		mSelectedTagTextColor = color;
	}
	
	public void setPublishTags(PetTagVo[] tags,int tagBgRes,int selectedBgRes,int useType,Handler handler,int layoutWidth) {
		mTags = tags;
		mTagBgResID=tagBgRes;
		mHandler=handler;
		mSelectedTagBgResID = selectedBgRes;
		this.useType=useType;
		initView(layoutWidth);
	}
	
	

	/**
	 * 设置标签字体颜色
	 */
	public void setTagTextColor(int textColor){
		tagTextColor=textColor;
	}
	
	/**
	 * 设置字体大小
	 * @param textSize
	 */
	public void setTagTextSize(float textSize){
		tagTextSize=textSize;
	}
	

	public int getViewWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
//		System.out.println("wh1:" + height + "," + width);
		return width;
	}

//	public void getLayoutWidth(final View view) {
//		ViewTreeObserver vto2 = view.getViewTreeObserver();
//		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				getViewTreeObserver().removeGlobalOnLayoutListener(this);
////				System.out.println("wh3:" + view.getHeight() + ","+view.getWidth());
//				Message message=new Message();
//				message.what=useType;
//				message.arg1=view.getWidth();
//				handler.sendMessage(message);
//			}
//		});
//	}
	
	public static int getTagBgResId(Context context){
//		String skinName = SkinManager.getInstance(context).getSkinName();
		int resID = R.drawable.petalk_tag_shape;
//		if(getString(context,R.string.skin_violet).equals(skinName)){
//			resID = R.drawable.display_tag_bg_pink;
//		}else if(getString(context,R.string.skin_blue).equals(skinName)){
//			resID = R.drawable.display_tag_bg_blue;
//		}else if(getString(context,R.string.skin_pink).equals(skinName)) {
//			resID = R.drawable.display_tag_bg_pink;
//		}
		return resID;
	}
	
	public static String getString(Context context,int resId){
		return context.getString(resId);
	}

}
