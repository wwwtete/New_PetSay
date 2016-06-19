package com.petsay.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;


/**
 * 自定义广场中的图文标签
 * @author G
 *
 */
public class TagImageView extends RelativeLayout {

	private ImageView imgTag;
	private TextView tvTag;

	public TagImageView(Context context) {
		super(context);
		inflate(context, R.layout.tag_image_view, this);
		initView();
	}

	public TagImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.tag_image_view, this);
		initView();
	}

	private void initView() {
		imgTag=(ImageView) findViewById(R.id.img_tag);
		tvTag=(TextView) findViewById(R.id.tv_tag);
	}
	
	/**
	 * 获取标签文本
	 * @return
	 */
	public String getTagText(){
		return tvTag.getText().toString();
	}
	
	/**
	 * 设置标签文本
	 * @param tag
	 */
	public void setTagText(String tag){
		tvTag.setText(tag);
	}
	
	public void setTagImgRes(int resId){
		imgTag.setImageResource(resId);
	}
	
	public void setTagImgDrawable(Drawable drawable){
		imgTag.setImageDrawable(drawable);
	}
	
	public void setTagImgBitmap(Bitmap bitmap){
		imgTag.setImageBitmap(bitmap);
	}
	
	public void setTagImgBg(int color){
		imgTag.setBackgroundColor(color);
	}
	
	public void setTagImgUrl(String url){
//		PicassoUtile.loadImg(getContext(), url, imgTag);
		ImageLoaderHelp.displayContentImage(url, imgTag);
	}

}
