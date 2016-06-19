package com.petsay.component.view.petalklistitem;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.CommentDTO;
import com.petsay.vo.forum.TopicCommentDTO;
import com.petsay.vo.petalk.CommentVo;
import com.petsay.vo.petalk.PetVo;

/**
 * 2.5.0版本添加 说说列表中评论内容 的自定义layout
 * 
 * @author G
 *
 */
public class ListItemCommentLayout extends RelativeLayout {

	private Context mContext;
	private LinearLayout layoutComment;
	// private LayoutParams layoutParams ;
	private ImageView mImgCommentFlag1;
	private TextView mTvCommentCount1;
	private TextView mTvCommentCount2;
	private LinearLayout mLayoutCommentCount2;

	// private List<CommentVo> mCommentVos;

	public ListItemCommentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(mContext, R.layout.listitem_comment_layout, this);
		initView();
		// setPetList(7);
	}

	private void initView() {
		layoutComment = (LinearLayout) findViewById(R.id.layout_comment);
		mImgCommentFlag1=(ImageView) findViewById(R.id.img_comment_flag1);
		mTvCommentCount1 = (TextView) findViewById(R.id.tv_commentcount1);
		mTvCommentCount2=(TextView) findViewById(R.id.tv_commentcount2);
		mLayoutCommentCount2=(LinearLayout) findViewById(R.id.layout_commentcount2);
		// layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// layoutParams.setMargins(0, 50, 0, 0);
	}

	public void setPetList(List<CommentVo> commentVos, int count) {
		mLayoutCommentCount2.setVisibility(View.GONE);
		mTvCommentCount1.setText("查看所有" + count + "条评论");
		layoutComment.removeAllViews();
		if (null != commentVos && !commentVos.isEmpty()) {
			setVisibility(View.VISIBLE);
			for (int i = 0; i < commentVos.size(); i++) {
				if (i < 2) {
					final CommentVo commentVo = commentVos.get(i);
					View view = (LinearLayout) LayoutInflater.from(mContext)
							.inflate(R.layout.listitem_comment_layout_item,
									null);
					CircleImageView circleImageView = (CircleImageView) view
							.findViewById(R.id.img_unactive);
					TextView tvCommentContent = (TextView) view
							.findViewById(R.id.tv_commnet_content);
					TextView tvCommentNick = (TextView) view
							.findViewById(R.id.tv_comment_petnick);
					tvCommentNick.setVisibility(View.GONE);
					// ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(),circleImageView);
					// CircleImageView header=new CircleImageView(mContext);
					tvCommentContent.setTextColor(getResources().getColor(
							R.color.list_content));
					circleImageView.setBorderWidth(0);
					// header.setImageResource(R.drawable.placeholderhead);
					ImageLoaderHelp.displayHeaderImage(
							commentVo.getPetHeadPortrait(), circleImageView);
					if (TextUtils.isEmpty(commentVo.getCommentAudioUrl())) {
						tvCommentContent.setText(commentVo.getComment());
					} else {
						tvCommentContent.setText("语音评论");
					}
					circleImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							PetVo pet = new PetVo();
							pet.setId(commentVo.getPetId());
							ActivityTurnToManager.getSingleton()
									.userHeaderGoto(getContext(), pet);
						}
					});
					// view.setLayoutParams(layoutParams);
					layoutComment.addView(view);
				}
			}
		} else {
			setVisibility(View.GONE);
		}

	}

	public void setTopicCommentList(List<CommentDTO> topicCommentDTOs, int count) {
		mTvCommentCount1.setVisibility(View.GONE);
		mImgCommentFlag1.setVisibility(View.GONE);
		mTvCommentCount2.setText("更多" + count + "条评论");
		layoutComment.removeAllViews();
		setBackgroundResource(R.drawable.toppic_comment);
		if (null != topicCommentDTOs && !topicCommentDTOs.isEmpty()) {
			setVisibility(View.VISIBLE);
			for (int i = 0; i < topicCommentDTOs.size(); i++) {
				if (i < 2) {
					final CommentDTO commentVo = topicCommentDTOs.get(i);
					View view = (LinearLayout) LayoutInflater.from(mContext)
							.inflate(R.layout.listitem_comment_layout_item,
									null);
					CircleImageView circleImageView = (CircleImageView) view
							.findViewById(R.id.img_unactive);
					TextView tvCommentContent = (TextView) view
							.findViewById(R.id.tv_commnet_content);
					TextView tvCommentNick = (TextView) view
							.findViewById(R.id.tv_comment_petnick);
					tvCommentNick.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ActivityTurnToManager.getSingleton().userHeaderGoto(mContext, commentVo.getPetId());
							
						}
					});
					circleImageView.setVisibility(View.GONE);
					tvCommentContent.setTextColor(getResources().getColor(
							R.color.list_content));
					tvCommentNick.setText(commentVo.getPetNickName() + "：");
					tvCommentContent.setText(commentVo.getComment());
					// circleImageView.setBorderWidth(0);
					layoutComment.addView(view);
				}
			}
		} else {
			setVisibility(View.GONE);
		}

	}

	public void setPetList(int size) {

	}
}
