package com.petsay.activity.topic.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.text.Clickable;
import com.petsay.component.view.ExCircleView;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.CommentDTO;
import com.petsay.vo.petalk.PetVo;

public class TopicCommentListAdapter extends BaseAdapter implements NetCallbackInterface {

	private Context mContext;
    private List<CommentDTO> mDtos;
    public int mCurrentSelectIndex=0;
    public int mWonderfulPosition=0;
    private TopicNet mTopicNet;
    
    
	public TopicCommentListAdapter(Context context) {
		mContext = context;
		this.mDtos=new ArrayList<CommentDTO>();
		mTopicNet=new TopicNet();
		mTopicNet.setTag(context);
		mTopicNet.setCallback(this);
	}
	
	public void refreshData(List<CommentDTO> dtos){
		if (dtos != null && dtos.size() > 0) {
			this.mDtos.clear();
			this.mDtos.addAll(dtos);
			notifyDataSetChanged();
		}
	}
	
	public void addMore(List<CommentDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			this.mDtos.addAll(dtos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	
	
	public List<CommentDTO> getmDtos() {
		return mDtos;
	}

	public void clear(){
		mDtos.clear();
		notifyDataSetChanged();
	}
	
	 

	@Override
	public int getCount() {
		if (null==mDtos) {
			return 0;
		}
		return  mDtos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==mDtos||mDtos.isEmpty()) {
			return null;
		}
		return  mDtos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.forum_comment_list_item, null);
			holder = new Holder();
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tvContent=(TextView) convertView.findViewById(R.id.tv_content);
			holder.exCircleView=(ExCircleView) convertView.findViewById(R.id.headview);
			holder.tvDate=(TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
	
		final CommentDTO commentDTO=mDtos.get(position); 
		holder.exCircleView.setBackgroudImage(commentDTO.getPetAvatar());
		holder.tvName.setText(commentDTO.getPetNickName());
		holder.tvContent.setText(commentDTO.getComment());
		holder.tvDate.setText(PublicMethod.calculateTopicTime(commentDTO.getCreateTime(),true));
		
		if (TextUtils.isEmpty(commentDTO.getAtPetNickName())) {
			holder.tvContent.setText(commentDTO.getComment());
		}else {
			String content="回复@"+commentDTO.getAtPetNickName()+":"+commentDTO.getComment();
			SpannableString spannableString=new SpannableString(content);
			spannableString.setSpan(new Clickable(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PetVo info=new PetVo();
					info.setId(commentDTO.getAtPetId());
					info.setNickName(commentDTO.getAtPetNickName());
//					info.setHeadPortrait(commentVo.getAimPetHeadPortrait());
					ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,info );
				}
			},false),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.list_name)),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 

			holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
//			holder.tvContent.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					mContext.setSelectCommentVo(commentDTO, position);
//				}
//			});
			holder.tvContent.setText(spannableString);
		
		
		}
		holder.tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,commentDTO.getPetId());

			}
		});
		holder.exCircleView.setClickHeaderToTurn(commentDTO.getPetId());
		return convertView;
	}

	private class Holder {
		private TextView tvContent,tvName,tvDate;
		private ExCircleView exCircleView;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		
		
	}
	
	
	
	
}