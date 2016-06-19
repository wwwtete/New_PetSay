package com.petsay.activity.main.square;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.constants.Constants;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.petalk.TagPetalkDTO;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/7
 * @Description 广场热门标签说说Item
 */
public class SquareHotTagPetalkItemView extends LinearLayout implements View.OnClickListener {

    private TextView mTvName;
    private ImageView mIvMore;
    private LinearLayout mLlPetalks;
    private ImageView mIvIcon;

    private TagPetalkDTO mDto;

    public SquareHotTagPetalkItemView(Context context) {
        super(context);
        onInitView();
    }

    private void onInitView() {
        inflate(getContext(), R.layout.view_square_hottagpetalk_item, this);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
        int space = PublicMethod.getDiptopx(getContext(),10);
        setPadding(space,space,space,space);
        findViews();
        mIvMore.setOnClickListener(this);
    }

    private void findViews() {
        mTvName = (TextView)findViewById( R.id.tv_name );
        mIvMore = (ImageView)findViewById( R.id.iv_more );
        mLlPetalks = (LinearLayout)findViewById( R.id.ll_petalks );
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
    }

    public void initView(TagPetalkDTO dto){
        if(dto == null)
            return;
        this.mDto = dto;
        mTvName.setText(dto.getName());
        ImageLoaderHelp.displayContentImage(dto.getIconUrl(), mIvIcon);
        mLlPetalks.removeAllViews();
        if(dto.getPetalks() != null && !dto.getPetalks().isEmpty()){
            mLlPetalks.setVisibility(VISIBLE);
            for (int i=0;i<dto.getPetalks().size();i++){
                mLlPetalks.addView(getViewItem(dto.getPetalks().get(i)));
            }
        }else {
            mLlPetalks.setVisibility(GONE);
        }
    }

    private View getViewItem(PetalkVo vo) {
//        RelativeLayout view = new RelativeLayout(getContext());
//
//        LayoutParams vp =new RadioGroup.LayoutParams(0,LayoutParams.WRAP_CONTENT,1);
//        view.setLayoutParams(vp);
//        ImageView img = new ImageView(getContext());
//        view.addView(img);
//        TextView txt = new TextView(getContext());
//        txt.setSingleLine(true);
//        txt.setEllipsize(TextUtils.TruncateAt.END);
//        txt.setText(vo.getDescription());
//        txt.setBackgroundResource(R.color.transparent2);
//        RelativeLayout.LayoutParams tp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        tp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
//        txt.setLayoutParams(tp);
//        view.addView(txt);
//        ImageLoaderHelp.displayContentImage(vo.getThumbUrl(),img);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tagpetalk_item,null);
        LayoutParams vp =new RadioGroup.LayoutParams(0,LayoutParams.WRAP_CONTENT,1);
        int space = PublicMethod.getDiptopx(getContext(),5);
        vp.setMargins(space,0,space,0);
        view.setLayoutParams(vp);
        ImageView mIvThumb = (ImageView)view.findViewById( R.id.iv_thumb );
        TextView mTvDescription = (TextView)view.findViewById( R.id.tv_description );
        ImageLoaderHelp.displayContentImage(vo.getThumbUrl()+ "?imageView2/1/w/100/h/100", mIvThumb);
//        System.out.println("url:"+vo.getThumbUrl()+ "?imageView2/2/w/100");
        if(TextUtils.isEmpty(vo.getDescription())){
            mTvDescription.setVisibility(GONE);
        }else {
            mTvDescription.setText(vo.getDescription());
        }
        view.setTag(vo);
        view.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.iv_more:
                if(mDto != null){
                    intent = new Intent(getContext(), TagSayListActivity.class);
                    intent.putExtra("id",mDto.getId());
                }
                break;
            default:
                if(v.getTag() != null && v.getTag() instanceof PetalkVo){
                    intent = new Intent(getContext(), DetailActivity.class);
                    Constants.Detail_Sayvo= (PetalkVo) v.getTag();
                }
                break;
        }
        if(intent != null)
            getContext().startActivity(intent);
    }
}
