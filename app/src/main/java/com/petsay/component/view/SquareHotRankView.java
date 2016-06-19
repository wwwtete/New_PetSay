package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.rank.PetScoreTotalRankDayDTO;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/16
 * @Description
 */
@Deprecated
public class SquareHotRankView extends RelativeLayout {

    private LinearLayout mLlHeads;

    public SquareHotRankView(Context context){
        super(context);
        initView();
    }

    public SquareHotRankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.squarehotrank_view, this);
        mLlHeads = (LinearLayout) findViewById(R.id.ll_heads);
    }

    public void updateView(List<PetScoreTotalRankDayDTO> pets){
        mLlHeads.removeAllViews();
        if(pets == null || pets.isEmpty()){
            return;
        }

        int dw = PublicMethod.getDisplayWidth(getContext());
        int size = PublicMethod.getDiptopx(getContext(),60);
        int space = PublicMethod.getDiptopx(getContext(),10);
        int length = dw/(size+space);
        length = length > pets.size() ? pets.size() : length;
        for (int i=0;i<length;i++){
            PetScoreTotalRankDayDTO vo = pets.get(i);
//            ImageView imageView = new ImageView(getContext());
            ExCircleView view = new ExCircleView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
            params.setMargins(space,0,0,0);
            params.width = size;
            params.height = size;
            view.setLayoutParams(params);
            mLlHeads.addView(view,params);
            view.setBackgroudImage(vo.getPetHeadPortrait());
            if (vo.getPetGender()==0) {
                view.setBottomRightImage(R.drawable.square_female);
            }else if (vo.getPetGender()==1) {
                view.setBottomRightImage(R.drawable.square_male);
            }else {
                view.getBottomRightImg().setVisibility(GONE);
            }
        }


    }

}
