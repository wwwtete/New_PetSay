package com.petsay.activity.message.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.constants.Constants;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.PMessage;

public class PrivateMsgListAdapter extends BaseAdapter {

	private Context mContext;
	private List<PMessage> pMessages;
    private int msgFrom;
	public PrivateMsgListAdapter(Context context,int from) {
		mContext = context;
		this.pMessages = new ArrayList<PMessage>();
		msgFrom=from;
	}

	public void refreshData(List<PMessage> data) {
		if (data != null) {
			pMessages.clear();
			pMessages.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addMore(List<PMessage> data) {
		if (data != null && data.size() > 0) {
			pMessages.addAll(data);
			notifyDataSetChanged();
		} else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}

	@Override
	public int getCount() {
		if (null == pMessages) {
			return 0;
		}
		return pMessages.size();
	}

	@Override
	public Object getItem(int position) {
		if (null == pMessages || pMessages.isEmpty()) {
			return null;
		}
		return pMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (null == pMessages || pMessages.isEmpty()) {
			return 0;
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null == convertView) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.private_msg_list_item, null);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.imgHeader = (CircleImageView) convertView.findViewById(R.id.img_header);
			holder.imgthumb = (ImageView) convertView.findViewById(R.id.img_thumb);
			holder.tvType=(TextView) convertView.findViewById(R.id.tv_type);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		
		PMessage pMessage = pMessages.get(position);
		if (msgFrom==Constants.MSG_USER) {
			holder.tvContent.setText(pMessage.getContent());
			holder.tvName.setText(pMessage.getPetNickName());
			holder.tvTime.setText(PublicMethod.calculateTime(pMessage.getCreateTime()));
//			ImageLoader.getInstance().displayImage(pMessage.getPetHeadPortrait(), holder.imgHeader, Constants.headerImgOptions);
//			ImageLoader.getInstance().displayImage(pMessage.getPetHeadPortrait(),holder.imgHeader, Constants.headerImgOptions);
//			PicassoUtile.loadHeadImg(mContext, pMessage.getPetHeadPortrait(), holder.imgHeader);
			
			if (pMessage.getType().equals("1")||pMessage.getType().equals("2")) {
				holder.imgHeader.setImageResource(R.drawable.icon);
			}else {
				ImageLoaderHelp.displayHeaderImage(pMessage.getPetHeadPortrait(), holder.imgHeader);
			}
			
			if (pMessage.getThumbUrl().trim().equals("")) {
				holder.imgthumb.setVisibility(View.GONE);
			} else {
				holder.imgthumb.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(pMessage.getThumbUrl(),holder.imgthumb, Constants.contentPetImgOptions);
//				PicassoUtile.loadHeadImg(mContext, pMessage.getPetHeadPortrait(), holder.imgHeader);
				ImageLoaderHelp.displayContentImage(pMessage.getThumbUrl(), holder.imgthumb);
			}
			
			if (pMessage.getType().equals(Constants.FANS)) {
				holder.tvType.setText("");
				holder.tvContent.setText("关注了你");
			}else if(pMessage.getType().equals(Constants.FAVOUR)){
				holder.tvType.setText("");
				holder.tvContent.setText("踩了你的说说");
			}else if(pMessage.getType().equals(Constants.RELAY)){
				holder.tvType.setText("转发");
				if (pMessage.getContent().trim().equals("")) {
					holder.tvContent.setVisibility(View.GONE);
				}else {
					holder.tvContent.setVisibility(View.VISIBLE);
					holder.tvContent.setText(pMessage.getContent());
				}
			}else if(pMessage.getType().equals(Constants.COMMENT)){
				holder.tvType.setText("评论");
				if (pMessage.getContentAudioUrl().trim().equals("")) {
					holder.tvContent.setVisibility(View.VISIBLE);
					holder.tvContent.setText(pMessage.getContent());
				}else {
					holder.tvContent.setVisibility(View.GONE);
				}
			}
		}else {
			holder.tvContent.setText(pMessage.getContent());
			holder.tvName.setText("宠物说助手");
			holder.tvTime.setText(PublicMethod.calculateTime(pMessage.getCreateTime()));
			if (pMessage.getType().equals("1")||pMessage.getType().equals("2")) {
				holder.imgHeader.setImageResource(R.drawable.icon);
			}else {
				ImageLoaderHelp.displayHeaderImage(pMessage.getPetHeadPortrait(), holder.imgHeader);
			}
//			ImageLoaderHelp.displayHeaderImage(pMessage.getPetHeadPortrait(), holder.imgHeader);
//			if (pMessage.getThumbUrl().trim().equals("")) {
				holder.imgthumb.setVisibility(View.GONE);
//			} else {
//				holder.imgthumb.setVisibility(View.VISIBLE);
//				ImageLoaderHelp.displayContentImage(pMessage.getThumbUrl(), holder.imgthumb);
//			}
			
		}
		
		return convertView;
	}

	private class Holder {
		private CircleImageView imgHeader;
		private ImageView imgthumb;
		private TextView tvName;
		private TextView tvContent;
		private TextView tvTime;
		private TextView tvCount;
		private TextView tvType;
	}
}
