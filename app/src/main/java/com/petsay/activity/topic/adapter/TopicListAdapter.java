package com.petsay.activity.topic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.TopicDTO;

public class TopicListAdapter extends ExBaseAdapter<TopicDTO> {

    public TopicListAdapter(Context context) {
        super(context);
    }

    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 Holder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.forum_topic_list_item, null);
			holder = new Holder();
			holder.tvBrowse=(TextView) convertView.findViewById(R.id.tv_browse);
			holder.tvContent=(TextView) convertView.findViewById(R.id.tv_content);
			holder.img=(ImageView) convertView.findViewById(R.id.img);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
	
		TopicDTO topicDTO=mDatas.get(position);
		ImageLoaderHelp.displayHeaderImage(topicDTO.getPic()+"?imageView2/2/w/120", holder.img);
		holder.tvContent.setText(topicDTO.getContent());
		holder.tvBrowse.setText(PublicMethod.calPlayTimes(topicDTO.getViewCount())+"浏览");
		return convertView;
	}

	private class Holder {
		private ImageView img;
		private TextView tvContent,tvBrowse;
	}
}