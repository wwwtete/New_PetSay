package com.petsay.component.view.functionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.application.UserManager;

/**
 * 功能条：转发 | 评论 | 踩 | 分享
 * @author wangw
 */
public class FunctionBarView extends RelativeLayout{

	private LinearLayout mView;
	private LinearLayout layout_comment,layout_step,layout_share;
	private TextView tvComment,tvStep,tvShare;
	private ImageView imgStep;
	private StepAnimView mStepAnimView;
	private boolean isFromList;
	private int relayCount=0;
	private int shareCount=0;
//	public FunctionBarView(Context context){
//		super(context);
//		initViews();
//		
//	}

	public FunctionBarView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
//		initViews();
	}

	public FunctionBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FunctionBarView, defStyle, 0);

		isFromList = a.getBoolean(R.styleable.FunctionBarView_isfromlist, true);
//        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
		initViews();
		
	}

	public void setIsDetail(boolean isDetail){
		
	}
	
	public void setColorBg(int color){
//		setBackgroundResource(colorResId);
		setBackgroundColor(color);
	}
	
	public void initListener(OnClickListener listener){
//		layout_forward.setOnClickListener(listener);
		layout_comment.setOnClickListener(listener);
		layout_step.setOnClickListener(listener);
		layout_share.setOnClickListener(listener);
	}
	
	public void setStepAnimView(StepAnimView stepAnimView){
		mStepAnimView = stepAnimView;
	}
	
	public void startStepAnimation(){
		if(mStepAnimView != null)
			mStepAnimView.startAnimation();
	}

	private void initViews() {
		if (isFromList) {
			mView = (LinearLayout) inflate(getContext(), R.layout.functionbar_view, null);
		}else {
			mView = (LinearLayout) inflate(getContext(), R.layout.functionbar_view_detail, null);
		}
		
		
		layout_comment=(LinearLayout) mView.findViewById(R.id.layout_comment);
		layout_step=(LinearLayout) mView.findViewById(R.id.layout_step);
		layout_share=(LinearLayout) mView.findViewById(R.id.layout_share);
		tvComment=(TextView) mView.findViewById(R.id.tv_comment);
		tvShare=(TextView) mView.findViewById(R.id.tv_share);
		tvStep=(TextView) mView.findViewById(R.id.tv_step);
		imgStep=(ImageView) mView.findViewById(R.id.img_step);
		addView(mView,new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * 计数
	 * @param forward
	 * @param comment
	 * @param step
	 * @param share
	 */
	public void setValue(int forward,int comment,int step,int share){
		tvComment.setText(comment+"");
		tvStep.setText(step+"");
		tvShare.setText((forward+share)+"");
		relayCount=forward;
		shareCount=share;
	}
	
	
	/**
	 * 转发数+1
	 */
	public void  addRelayCount() {
//		int count=textValeToInt(tvShare);
		relayCount++;
		tvShare.setText((relayCount+shareCount)+"");
	}
	
	/**
	 * 转发数-1
	 */
	public void  minusRelayCount() {
//		int count=textValeToInt(tvShare);
		relayCount--;
		
		tvShare.setText((relayCount+shareCount)+"");
	}
	
	public int getRelayCount(){
		return relayCount;
	}
	
	/**
	 * 评论数+1
	 */
	public void  addCommentCount() {
		int count=textValeToInt(tvComment);
		tvComment.setText(count+1+"");
	}
	
	/**
	 * 评论数-1
	 */
	public void  minusCommentCount() {
		int count=textValeToInt(tvComment);
		tvComment.setText(count-1+"");
	}
	
	public int getCommentCount(){
		return textValeToInt(tvComment);
	}
	
	/**
	 * 分享数+1
	 */
	public void addShareCount(){
//		int count=textValeToInt(tvShare);
		shareCount++;
		tvShare.setText((relayCount+shareCount)+"");
	}
	
//	/**
//	 * 分享数-1
//	 */
//	public void  minusShareCount() {
//		int count=textValeToInt(tvShare);
//		tvShare.setText(count-1+"");
//	}
	
	public int getShareCount(){
		return shareCount;
		
	}
	
	
	/**
	 * 赞数+1
	 */
	public void  addStepCount(String petalkId) {
		int count=textValeToInt(tvStep);
		tvStep.setText(count+1+"");
		UserManager.getSingleton().stepMap.put(petalkId, petalkId);
		setStepStatus(petalkId);
	}
	
	public int getStepCount(){
		return textValeToInt(tvStep);
	}
	
	public void setStepStatus(String petalkId){
		if (!UserManager.getSingleton().stepMap.containsKey(petalkId)) {
			imgStep.setImageResource(R.drawable.step_ico);
		}else{
//			Drawable drawable = SkinManager.getInstance(getContext()).getDrawable(getContext().getString(R.string.step_icon));
//			if(drawable == null)
				imgStep.setImageResource(R.drawable.step_already);
//			else {
//				imgStep.setImageDrawable(drawable);
//			}
		}
	}
	
	
	
//	/**
//	 * 转发数+1
//	 */
//	public void addForwardCount(){
//		addShareCount();
//	}
	
//	/**
//	 * 由于转发数和分享数合并，所以获取到的数据是转发和分享加起来的数量
//	 * @return
//	 */
//	public int getForwardCount(){
//		return getShareCount();
//	}
	
	/**
	 * TextView值转换成Int
	 * @param txt
	 * @return
	 */
	protected int textValeToInt(TextView txt){
		String v = txt.getText().toString();
		if(TextUtils.isEmpty(v))
			return 0 ;
		else {
			return Integer.parseInt(v);
		}
	}

    public void hidenShareView(boolean flag){
        if(flag){
            layout_share.setVisibility(GONE);
        }else {
            layout_share.setVisibility(VISIBLE);
        }
    }
	
}
