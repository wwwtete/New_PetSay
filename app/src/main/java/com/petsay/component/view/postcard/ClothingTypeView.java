package com.petsay.component.view.postcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.personalcustom.SpecValueDTO;

/**
 * 自定义标签布局
 * 
 * @author G
 * 
 */
public class ClothingTypeView extends LinearLayout {

	private Context mContext;
	private SpecValueDTO[] mSpecDTOs;
	private int tagTextColor=Color.WHITE;
	private float tagTextSize=15;
	private int mTagBgResID=0;
	//使用类型 0说说列表    1发布
	private int useType=0;
	public  final int Use_SayList=0,Use_Release_UnSelected=1,Use_Release_Selected=2;
	public static final int Selected_Tag=3;
	public static final int Remove_Tag=4;
	private int arrayIndex=0;
	private LayoutParams layoutParams;
	private int mSelectedTagBgResID;
	private int mSelectedTagTextColor;
	
	private OnTagItemClickListener mOnTagItemClickListener;

	public ClothingTypeView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mContext = context;
	}

	public ClothingTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	
	private void initView(int layoutWidth) {
//		getLayoutWidth(this);
		if(useType  == Use_SayList)
			initSayTagList(layoutWidth);
//		else {
//			initPublishTagView(useType,layoutWidth);
//		}
	}
	
	public void setOnTagItemClickListener(OnTagItemClickListener onTagItemClickListener){
		mOnTagItemClickListener=onTagItemClickListener;
	}
	
	@SuppressLint("NewApi")
	private void initSayTagList(int layoutWidth){
		int space = PublicMethod.getDiptopx(getContext(), 3);
		//按钮的最大宽度
		int maxWidth=layoutWidth-50;
		int index=0;
//	    Drawable drawable = mContext.getResources().getDrawable(R.drawable.label);
		for (int i = 0; i <mSpecDTOs.length; i++) {
			int tempWidth=maxWidth;
			LinearLayout tagLayout=new LinearLayout(mContext);
			tagLayout.setOrientation(LinearLayout.HORIZONTAL);
			for ( int j=index; j < mSpecDTOs.length; j++) {
				final TextView button=new TextView(mContext);
				button.setText(mSpecDTOs[j].getValue());
				button.setTextColor(tagTextColor);
				button.setTextSize(tagTextSize);
//				button.setBackgroundColor(tagBackColor);
				if (i==0&&j==0) {
					button.setBackgroundResource(mSelectedTagBgResID);
				}else {
					button.setBackgroundResource(mTagBgResID);
				}
				
				
				button.setMaxWidth(maxWidth);
				button.setSingleLine(true);
				button.setPadding(20,10, 20, 10);
                setListener(button,mSpecDTOs[j]);
				layoutParams.setMargins(space,space, space, space);
				button.setLayoutParams(layoutParams);
				int width=getViewWidth(button);
				if (width<tempWidth) {
					index++;
					tagLayout.addView(button);
					tempWidth=tempWidth-width -space;
				}else {
					break;
				}
			}
			addView(tagLayout);
		}
	}

    private void setListener(View view,final SpecValueDTO specDTO){
        view.setTag(specDTO);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	setSelectedType(v);
            	if (null!=mOnTagItemClickListener) {
					mOnTagItemClickListener.onItemClick(specDTO);
				}
            }
        });
    }
    
    private void setSelectedType(View view){
    	for (int i = 0; i <getChildCount() ; i++) {
    		LinearLayout tagLayout=(LinearLayout) getChildAt(i);
			for (int j = 0; j < tagLayout.getChildCount(); j++) {
				if (view==tagLayout.getChildAt(j)) {
					tagLayout.getChildAt(j).setBackgroundResource(mSelectedTagBgResID);
				}else {
					tagLayout.getChildAt(j).setBackgroundResource(mTagBgResID);
				}
			}
		}
    }

	/**
	 * 设置标签数据、 背景
	 * @param tags
	 * @param tagImgBgRes
	 * @param useType 使用类型 0说说列表    1发布
	 */
	public void setTags(SpecValueDTO[] specDTOs,int tagBgRes,int selectedBgRes,int layoutWidth) {
		removeAllViews();
		if (null==specDTOs||specDTOs.length==0) {
			
		}else {
			mSpecDTOs = specDTOs;
			mSelectedTagBgResID=selectedBgRes;
			mTagBgResID=tagBgRes;
			initView(layoutWidth);
		}
	}
	
	public void setIndex(int index){
		arrayIndex=index;
	}
	
	public void setSelectedTagTextColor(int color){
		mSelectedTagTextColor = color;
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
		return width;
	}
	
	public static int getTagBgResId(Context context){
		int resID = R.drawable.display_tag_bg_pink;
		return resID;
	}
	
	public static String getString(Context context,int resId){
		return context.getString(resId);
	}
	
	public interface OnTagItemClickListener{
		void onItemClick(SpecValueDTO specDTO);
	}
}
