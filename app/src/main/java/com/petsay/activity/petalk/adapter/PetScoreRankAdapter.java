package com.petsay.activity.petalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.petalk.rank.GrowupUndergoActivity;
import com.petsay.activity.user.OtherUserActivity;
import com.petsay.component.view.ExCircleView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.rank.PetScoreTotalRankDayDTO;
import com.petsay.vo.rank.SimplePetalkDTO;

/**
 * 宠物积分排行榜
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/18
 * @Description
 */
public class PetScoreRankAdapter extends ExBaseAdapter<PetScoreTotalRankDayDTO> implements View.OnClickListener {


    /**1：周排行，0：总排行榜*/
    private int mRankType;

    public PetScoreRankAdapter(Context context,int rankType) {
        super(context);
        this.mRankType = rankType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.rank_petscore_item,null);
            holder = new ViewHolder();
            holder.findViews(convertView);
            holder.headview.setOnClickListener(this);
            holder.headview2.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PetScoreTotalRankDayDTO dto = getItem(position);
        holder.headview.setTag(dto.getPetId());
        holder.headview2.setTag(dto.getPetId());
        if(dto.getRankNum() <= 3) {
            holder.rlFrame2.setVisibility(View.GONE);
            initTop3View(holder, dto);
        }else {
            holder.rlFrame1.setVisibility(View.GONE);
            initOtherView(holder,dto);
        }

        return convertView;
    }

    private void initOtherView(ViewHolder holder, PetScoreTotalRankDayDTO dto) {
        holder.rlFrame2.setVisibility(View.VISIBLE);

        holder.tvRank.setText(dto.getRankNum()+"");
        holder.tvName2.setText(dto.getPetNickName());
        if (dto.getPetGender() == 0) {
            holder.tvName2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female, 0, 0, 0);
        } else if (dto.getPetGender() == 1) {
            holder.tvName2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male, 0, 0, 0);
        } else {
            holder.tvName2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        holder.headview2.setBackgroudImage(dto.getPetHeadPortrait());
        if (!TextUtils.isEmpty(dto.getPetStar())&&dto.getPetStar().equals("1")) {
            holder.headview2.getBottomRightImg().setVisibility(View.VISIBLE);
        } else {
            holder.headview2.getBottomRightImg().setVisibility(View.GONE);
        }

        holder.tvScore2.setText("积分："+dto.getScore());

        if(dto.getSimplePetalkDTOs().isEmpty()){
            holder.ivTh04.setVisibility(View.GONE);
            holder.ivTh05.setVisibility(View.GONE);
        }else {
            int lenght = dto.getSimplePetalkDTOs().size();
            for (int i = 0; i < lenght; i++) {
                SimplePetalkDTO sp = dto.getSimplePetalkDTOs().get(i);
                ImageView img = null;
                if (i == 0) {
                    img = holder.ivTh04;
                } else if (i == 1) {
                    img = holder.ivTh05;
                }
                if (img != null) {
                    if (TextUtils.isEmpty(sp.getPhotoUrl())) {
                        img.setVisibility(View.GONE);
                    } else {
                        img.setVisibility(View.VISIBLE);
                        ImageLoaderHelp.displayImage(sp.getPhotoUrl() + "?imageView2/2/w/100", img);
                    }
                }else {
                    break;
                }
            }
        }
    }

    private void initTop3View(ViewHolder holder,PetScoreTotalRankDayDTO dto) {
        holder.rlFrame1.setVisibility(View.VISIBLE);
        int resId = mContext.getResources().getIdentifier("top"+dto.getRankNum(),"drawable",mContext.getPackageName());
        if(resId > 0){
            holder.ivRanknum.setImageResource(resId);
        }

        holder.tvName.setText(dto.getPetNickName()+"/");
        if (dto.getPetGender() == 0) {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female, 0, 0, 0);
        } else if (dto.getPetGender() == 1) {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male, 0, 0, 0);
        } else {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        holder.headview.setBackgroudImage(dto.getPetHeadPortrait());
        if (!TextUtils.isEmpty(dto.getPetStar())&&dto.getPetStar().equals("1")) {
            holder.headview.getBottomRightImg().setVisibility(View.VISIBLE);
        } else {
            holder.headview.getBottomRightImg().setVisibility(View.GONE);
        }

        holder.tvScore.setText("积分："+dto.getScore());

        if(dto.getSimplePetalkDTOs().isEmpty()){
            holder.ivTh01.setVisibility(View.GONE);
            holder.ivTh02.setVisibility(View.GONE);
            holder.ivTh03.setVisibility(View.GONE);
            holder.llTh.setVisibility(View.GONE);
        }else {
            holder.llTh.setVisibility(View.VISIBLE);
            int lenght = dto.getSimplePetalkDTOs().size();
            for (int i = 0; i < lenght; i++) {
                SimplePetalkDTO sp = dto.getSimplePetalkDTOs().get(i);
                ImageView img;
                if (i == 0) {
                    img = holder.ivTh01;
                } else if (i == 1) {
                    img = holder.ivTh02;
                } else {
                    img = holder.ivTh03;
                }
                if (TextUtils.isEmpty(sp.getPhotoUrl())) {
                    img.setVisibility(View.GONE);
                } else {
                    img.setVisibility(View.VISIBLE);
                    ImageLoaderHelp.displayImage(sp.getPhotoUrl() + "?imageView2/2/w/200", img);
                }
            }
        }


    }

    public int getLevelResId(int level){
        return mContext.getResources().getIdentifier("level_"+level,"drawable",mContext.getPackageName());
    }

    @Override
    public void onClick(View v) {
//        PetVo petInfo = new PetVo();
//        petInfo.setId((String) v.getTag());
//        Intent intent = new Intent(mContext, OtherUserActivity.class);
//        intent.putExtra("petInfo", petInfo);
//        mContext.startActivity(intent);

        Intent intent = new Intent(mContext,GrowupUndergoActivity.class);
        String petId = (String) v.getTag();
        intent.putExtra("petid",petId);
        intent.putExtra("ranktype",mRankType);
        mContext.startActivity(intent);
    }


    class ViewHolder{
        private RelativeLayout rlFrame1;
        private ImageView ivRanknum;
        private LinearLayout llTop;
        private LinearLayout llTh;
        private ExCircleView headview;
        private TextView tvName;
        private TextView tvScore;
        private ImageView ivTh01;
        private ImageView ivTh02;
        private ImageView ivTh03;
        private LinearLayout rlFrame2;
        private ExCircleView headview2;
        private TextView tvName2;
        private TextView tvScore2;
        private LinearLayout llThumbnail;
        public TextView tvRank;
        private ImageView ivTh04;
        private ImageView ivTh05;

        private void findViews(View view) {
            rlFrame1 = (RelativeLayout)view.findViewById( R.id.rl_frame1 );
            ivRanknum = (ImageView)view.findViewById( R.id.iv_ranknum );
            llTop = (LinearLayout)view.findViewById( R.id.ll_top );
            headview = (ExCircleView)view.findViewById( R.id.headview );
            headview.setBottomRightImage(R.drawable.star);
            tvName = (TextView)view.findViewById( R.id.tv_name );
            tvScore = (TextView)view.findViewById( R.id.tv_score );
            ivTh01 = (ImageView)view.findViewById( R.id.iv_th01 );
            ivTh02 = (ImageView)view.findViewById( R.id.iv_th02 );
            ivTh03 = (ImageView)view.findViewById( R.id.iv_th03 );
            rlFrame2 = (LinearLayout)view.findViewById( R.id.rl_frame2 );
            headview2 = (ExCircleView)view.findViewById( R.id.headview2 );
            headview2.setBottomRightImage(R.drawable.star);
            tvName2 = (TextView)view.findViewById( R.id.tv_name2 );
            tvScore2 = (TextView)view.findViewById( R.id.tv_score2 );
            llThumbnail = (LinearLayout)view.findViewById( R.id.ll_thumbnail );
            ivTh04 = (ImageView)view.findViewById( R.id.iv_th04 );
            ivTh05 = (ImageView)view.findViewById( R.id.iv_th05 );
            tvRank = (TextView) view.findViewById(R.id.tv_rank);
            llTh = (LinearLayout) view.findViewById(R.id.ll_th);
        }

    }

}
