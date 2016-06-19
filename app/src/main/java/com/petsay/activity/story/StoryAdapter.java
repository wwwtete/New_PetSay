package com.petsay.activity.story;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.draggridview.DragGridBaseAdapter;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.story.StoryAddressItem;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;
import com.petsay.vo.story.StoryTextItem;

import java.util.Collections;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryAdapter extends BaseAdapter implements DragGridBaseAdapter{

    private int mHidePosition = -1;
    private StoryParams mParam;
    private LayoutInflater mInflater;

    public StoryAdapter(Context context,StoryParams params) {
        mParam = params;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mParam.items.size();
    }

    @Override
    public Object getItem(int position) {
        return mParam.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = null;
        StoryItemVo itemVo = (StoryItemVo) getItem(position);
        switch (itemVo.getType()){
            case 0:
                view = getItemView(position, (StoryAddressItem) itemVo);
                break;
            case 1:
                view = getItemView(position, (StoryImageItem) itemVo);
                break;
            case 2:
                view = getItemView(position, (StoryTextItem) itemVo);
                break;
        }
        if(mHidePosition == position)
            view.setVisibility(View.INVISIBLE);
        return view;
    }

    private View getItemView(int posititon,StoryImageItem item){
        View view = mInflater.inflate(R.layout.story_image_item,null);
        ImageView iv_Mark = (ImageView)view.findViewById( R.id.iv_mark );
        ImageView ivContent = (ImageView)view.findViewById( R.id.iv_content );
        TextView tv_describe = (TextView)view.findViewById( R.id.tv_describe );
        ImageLoaderHelp.displayContentImage("file://"+item.getImageUrl(),ivContent);
        if(!TextUtils.isEmpty(item.getAudioUrl()))
            iv_Mark.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(item.getDescribe())) {
            tv_describe.setText(item.getDescribe());
            tv_describe.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(item.getAudioUrl())){
            iv_Mark.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private View getItemView(int posititon,StoryAddressItem item){
        View view = mInflater.inflate(R.layout.story_address_item,null);
        TextView tvTime = (TextView)view.findViewById( R.id.tv_time );
        TextView tvAddress = (TextView)view.findViewById( R.id.tv_address );
        tvTime.setText(item.getTime());
        tvAddress.setText(item.getAddress());
        return view;
    }

    private View getItemView(int posititon,StoryTextItem item){
        View view = mInflater.inflate(R.layout.story_text_item,null);
        TextView tvContent = (TextView)view.findViewById( R.id.tv_content );
        tvContent.setText(item.getContent());
        return view;
    }


    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        StoryItemVo temp = mParam.items.get(oldPosition);
        if(oldPosition < newPosition){
            for(int i=oldPosition; i<newPosition; i++){
                Collections.swap(mParam.items, i, i + 1);
            }
        }else if(oldPosition > newPosition){
            for(int i=oldPosition; i>newPosition; i--){
                Collections.swap(mParam.items, i, i-1);
            }
        }
        mParam.items.set(newPosition,temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(int removePosition) {
        notifyDataSetChanged();
    }
}
