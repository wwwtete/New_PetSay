package com.petsay.activity.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.member.PetCoinDetailVo;

/**
 * @author wangw (404441027@qq.com)
 *         Date 2014/12/19
 */
public class PetCoinAdapter extends ExBaseAdapter<PetCoinDetailVo> {

    public PetCoinAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.petscore_item,null);
            holder = ViewHolder.create(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        PetCoinDetailVo vo = (PetCoinDetailVo) getItem(position);

        holder.tvDate.setText(PublicMethod.formatTimeToString(vo.getCreateTime(), "yyyy-MM-dd"));
        holder.tvOperate.setText(vo.getMemo());
        String sign = vo.getBlsign() > 0 ? "" : "-";
        holder.tvScore.setText(sign+vo.getAmount());


        return convertView;
    }

    private static class ViewHolder {
        public final TextView tvDate;
        public final TextView tvOperate;
        public final TextView tvScore;

        private ViewHolder(TextView tvDate, TextView tvOperate, TextView tvScore) {
            this.tvDate = tvDate;
            this.tvOperate = tvOperate;
            this.tvScore = tvScore;
        }

        public static ViewHolder create(View rootView) {
            TextView tvDate = (TextView)rootView.findViewById( R.id.tv_date );
            TextView tvOperate = (TextView)rootView.findViewById( R.id.tv_operate );
            TextView tvScore = (TextView)rootView.findViewById( R.id.tv_score );
            return new ViewHolder( tvDate, tvOperate, tvScore );
        }
    }
}
