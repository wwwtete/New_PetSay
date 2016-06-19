package com.petsay.activity.petalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.OtherUserActivity;
import com.petsay.component.view.ExCircleView;
import com.petsay.component.view.petalklistitem.ListItemCommentLayout;
import com.petsay.component.view.petalklistitem.ListItemStepLayout;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.rank.PetalkPopRankWeekDTO;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/17
 * @Description
 */
public class PetalkRankAdapter extends BasePettalkAdapter<PetalkPopRankWeekDTO> implements View.OnClickListener {

    public PetalkRankAdapter(Context context) {
        super(context,PublicMethod.getDisplayWidth(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.rank_petalk_item,null);
            holder = new ViewHolder();
            findPublicView(holder,convertView);
            holder.findViews(convertView);
//            holder.rlTopview.setOnClickListener(this);
            holder.llBottom.setOnClickListener(this);
            holder.headview.setOnClickListener(this);
            holder.tvName.setOnClickListener(this);
            holder.imgPet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGifView(v,holder);
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PetalkPopRankWeekDTO  rank = getItem(position);
        PetalkVo sayVo = rank.getPetalkDTO();
        holder.llBottom.setTag(sayVo);
//        holder.rlTopview.setTag(sayVo);
//        holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
//        holder.tvRanknum.setText("");
//        switch (rank.getRankNum()){
//            case 1:
//                holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rank_talk_top1,0,0,0);
//                break;
//            case 2:
//                holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rank_talk_top2,0,0,0);
//                break;
//            case 3:
//                holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rank_talk_top3,0,0,0);
//                break;
//            default:
//                holder.tvRanknum.setBackgroundDrawable(null);
//                break;
//        }

        if(rank.getRankNum() <= 3){
            holder.tvRanknum.setText("");
            int resId = mContext.getResources().getIdentifier("rank_talk_top"+rank.getRankNum(),"drawable",mContext.getPackageName());
            holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
        }else {
            holder.tvRanknum.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            holder.tvRanknum.setText("TOP" + rank.getRankNum());
        }

        holder.tvHotnum.setText(rank.getPopNum()+"热度");
        holder.tvName.setText(sayVo.getPetNickName());
        PetVo petVo = sayVo.getPet();

        holder.tvName.setTag(petVo);
        holder.headview.setTag(petVo);
        if (petVo.getGender() == 0) {
            holder.ivSex.setVisibility(View.VISIBLE);
            holder.ivSex.setImageResource(R.drawable.female);

        } else if (petVo.getGender() == 1) {
            holder.ivSex.setVisibility(View.VISIBLE);
            holder.ivSex.setImageResource(R.drawable.male);
        } else {
            holder.ivSex.setVisibility(View.GONE);
        }

        holder.headview.setBackgroudImage(petVo.getHeadPortrait());
        if (!TextUtils.isEmpty(petVo.getStar())&&petVo.getStar().equals("1")) {
            holder.headview.getBottomRightImg().setVisibility(View.VISIBLE);
        } else {
            holder.headview.getBottomRightImg().setVisibility(View.GONE);
        }

//        holder.functionBarView.setValue(sayVo.getCounter().getRelay(), sayVo.getCounter().getComment(), sayVo.getCounter().getFavour(),
//                sayVo.getCounter().getShare());
//        holder.functionBarView.setStepStatus(sayVo.getPetalkId());
//        holder.functionBarEventHolder.setSayVo(sayVo);
        refreshPetalk(sayVo, holder, position);
        refreshItemTypeView(sayVo.getModel(), holder.ivFlag);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.tv_name:
            case R.id.headview:
                PetVo petInfo = (PetVo) v.getTag();
                intent = new Intent(mContext, OtherUserActivity.class);
                intent.putExtra("petInfo", petInfo);

                break;
            case R.id.ll_bottom:
                Constants.Detail_Sayvo = (PetalkVo) v.getTag();
                intent = new Intent(mContext, DetailActivity.class);
                break;
        }
        if(intent != null)
            mContext.startActivity(intent);
    }

    class ViewHolder extends BasePetalkViewHolder{
//        public View rlTopview;
//        public View llInfo;
//        public View llRank;
        public View llBottom;
        public TextView tvRanknum;
        public TextView tvHotnum;
        public ListItemStepLayout steplayout;
        public ListItemCommentLayout commentlayout;
        public LinearLayout layoutComment;
        private ExCircleView headview;
        private ImageView ivSex;
        private TextView tvName;
//        private FunctionBarView functionBarView;
//        private StepAnimView stepAnimView;
//        private ListItemFunctionBarEventHolder functionBarEventHolder;

        private void findViews(View rootView) {
//            llInfo = rootView.findViewById(R.id.ll_info);
//            llRank = rootView.findViewById(R.id.ll_rank);
            llBottom = rootView.findViewById(R.id.ll_bottom);
            tvRanknum = (TextView)rootView.findViewById( R.id.tv_ranknum );
            tvHotnum = (TextView) rootView.findViewById(R.id.tv_hotnum);
//            functionBarView = (FunctionBarView)rootView.findViewById( R.id.functionbar );
//            functionBarView.hidenShareView(true);
//            functionBarEventHolder = new ListItemFunctionBarEventHolder(mContext, functionBarView, null);
//            functionBarView.initListener(functionBarEventHolder);
            steplayout = (ListItemStepLayout)rootView.findViewById( R.id.steplayout );
            commentlayout = (ListItemCommentLayout)rootView.findViewById( R.id.commentlayout );
            layoutComment = (LinearLayout)rootView.findViewById( R.id.layout_comment );
//            stepAnimView = (StepAnimView)rootView.findViewById( R.id.stepanim );
//            functionBarView.setStepAnimView(stepAnimView);
            headview = (ExCircleView) rootView.findViewById(R.id.headview);
            headview.setBottomRightImage(R.drawable.star);
            ivSex = (ImageView) rootView.findViewById(R.id.iv_sex);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
//            rlTopview = rootView.findViewById(R.id.rl_topview);

        }

    }
}
