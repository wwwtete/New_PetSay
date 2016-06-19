package com.petsay.activity.user.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.network.net.GiftBagNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.vo.member.GiftBagVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangw on 2014/12/16.
 * 礼包列表Adapter
 */
public class GiftBagAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<GiftBagVo> mData;
    private GiftBagNet mNet;

    public GiftBagAdapter(Context context,GiftBagNet net){
        super();
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<GiftBagVo>();
        mNet = net;
    }

    public void refreshData(List<GiftBagVo> data){
        mData.clear();
        if(data != null && data.size() > 0){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addMoreData(List<GiftBagVo> data){
        if(data != null && data.size() > 0){
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.giftbag_item,null);
            holder = ViewHolder.create(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        GiftBagVo vo = (GiftBagVo) getItem(position);
        ImageLoaderHelp.displayImage(vo.getIcon(),holder.ivIcon, ImageLoaderOptionsManager.getGiftBagOptions());
        holder.tvDescription.setText(vo.getDescription());
        holder.tvLevel.setText(vo.getName());
        holder.btnDraw.setTag(vo);
        if(TextUtils.isEmpty(vo.getGiftBagState())){
            holder.tvState.setVisibility(View.GONE);
        }else{
            holder.tvState.setVisibility(View.VISIBLE);
            holder.tvState.setText(vo.getGiftBagState());
        }

        if(vo.getState() == 3){
            holder.btnDraw.setVisibility(View.VISIBLE);
            holder.btnDraw.setTag(vo);
            holder.btnDraw.setClickable(true);
            holder.btnDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag();
                    if(obj != null && obj instanceof GiftBagVo && mNet != null){
                        v.setClickable(false);
                        mNet.drawGifBag(UserManager.getSingleton().getActivePetId(),((GiftBagVo)obj).getCode());
                    }
                }
            });
        }else{
            holder.btnDraw.setOnClickListener(null);
            holder.btnDraw.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder {
        public final View rootView;
        public final TextView tvLevel;
        public final TextView tvState;
        public final ImageView ivIcon;
        public final TextView tvDescription;
        public final Button btnDraw;

        private ViewHolder(View rootView, TextView tvLevel, TextView tvState, ImageView ivIcon, TextView tvDescription,Button btnDraw) {
            this.rootView = rootView;
            this.tvLevel = tvLevel;
            this.tvState = tvState;
            this.ivIcon = ivIcon;
            this.tvDescription = tvDescription;
            this.btnDraw = btnDraw;
        }

        public static ViewHolder create(View rootView) {
            TextView tvLevel = (TextView)rootView.findViewById( R.id.tv_level );
            TextView tvState = (TextView)rootView.findViewById( R.id.tv_state );
            ImageView ivIcon = (ImageView)rootView.findViewById( R.id.iv_icon );
            TextView tvDescription = (TextView)rootView.findViewById( R.id.tv_description );
            Button btnDraw = (Button) rootView.findViewById(R.id.btn_draw);
            return new ViewHolder( rootView, tvLevel, tvState, ivIcon, tvDescription,btnDraw );
        }
    }
}
