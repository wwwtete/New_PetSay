package com.petsay.activity.user.shippingaddress;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.vo.user.UserShippingAddressDTO;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/11
 * @Description 购物地址列表
 */
public class ShippingAddressAdapter extends ExBaseAdapter<UserShippingAddressDTO> {

    public ShippingAddressAdapter(Context context) {
        super(context);
    }

    public UserShippingAddressDTO remove(int position){
        if(position >= 0 && position < getCount()){
            UserShippingAddressDTO dto = mDatas.remove(position);
            notifyDataSetChanged();
            return dto;
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.shoppingaddress_item,null);
            holder = new ViewHolder();
            holder.findViews(convertView);
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,EditShippingAddressActivity.class);
                    intent.putExtra("usershippingaddress",(UserShippingAddressDTO)v.getTag());
                    mContext.startActivity(intent);
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserShippingAddressDTO dto = getItem(position);
        if(dto.getIsDefault()){
            holder.ivIcon.setImageResource(R.drawable.order_address_icon);
        }else {
            holder.ivIcon.setImageResource(R.drawable.order_noselected_address_icon);
        }
        holder.ivEdit.setTag(dto);
        holder.tvAddress.setText(dto.getAddress());
        holder.tvTelephone.setText(dto.getMobile());
        holder.tvReceivename.setText(dto.getName());
        return convertView;
//        return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        private ImageView ivIcon;
        private ImageView ivEdit;
        private TextView tvLabel;
        private TextView tvReceivename;
        private TextView tvTelephone;
        private TextView tvAddress;
        private ImageView swipeBackview;

        private void findViews(View view) {
            ivIcon = (ImageView)view.findViewById(R.id.iv_icon);
            ivEdit = (ImageView)view.findViewById( R.id.iv_edit );
            tvLabel = (TextView)view.findViewById(R.id.tv_label);
            tvReceivename = (TextView)view.findViewById(R.id.tv_receivename);
            tvTelephone = (TextView)view.findViewById(R.id.tv_telephone);
            tvAddress = (TextView)view.findViewById(R.id.tv_address);
            swipeBackview = (ImageView) view.findViewById(R.id.swipe_backview);
        }

    }

}
