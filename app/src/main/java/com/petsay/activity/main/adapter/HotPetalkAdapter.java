package com.petsay.activity.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.MarkImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkVo;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/12
 * @Description
 */
@Deprecated
public class HotPetalkAdapter extends ExBaseAdapter<PetalkVo> {

    private int mItemSize;

    public HotPetalkAdapter(Context context) {
        super(context);
        int w = PublicMethod.getDisplayWidth(context);
        int sp = context.getResources().getDimensionPixelOffset(R.dimen.hot_petalk_spacing);
        mItemSize = (w-sp*3)/2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.hot_petalk_item,null);
            holder = new ViewHolder();
            holder.bindViews(convertView);
            FrameLayout.LayoutParams params = holder.mIv_content.getContentLayoutParams();
            params.width = mItemSize;
            params.height = mItemSize;
            holder.mIv_content.setContentLayoutParams(params);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PetalkVo vo = getItem(position);
        ImageLoaderHelp.displayHeaderImage(vo.getPetHeadPortrait(),holder.mImg_header);
        holder.mIv_content.setContentImage(vo.getPhotoUrl() + "?imageView2/2/w/" + mItemSize);
        if(vo.isAudioModel())
            holder.mIv_content.setMarkImage(R.drawable.audio_flag_icon);
        else
            holder.mIv_content.setMarkImage(R.drawable.image_flag_icon);
        holder.mTv_name.setText(vo.getPetNickName());
//        holder.mTv_type.setText(vo.getType());
        holder.mTv_count.setText(vo.getCounter().getPlay()+"人浏览");

        return convertView;
    }

    class ViewHolder{
        private MarkImageView mIv_content;
        private CircleImageView mImg_header;
        private TextView mTv_name;
//        private TextView mTv_type;
        private TextView mTv_count;

        private void bindViews(View view) {

            mIv_content = (com.petsay.component.view.MarkImageView) view.findViewById(R.id.iv_content);
            mImg_header = (com.petsay.component.view.CircleImageView) view.findViewById(R.id.img_header);
            mTv_name = (TextView) view.findViewById(R.id.tv_name);
//            mTv_type = (TextView) view.findViewById(R.id.tv_type);
            mTv_count = (TextView) view.findViewById(R.id.tv_count);
        }

    }

}
