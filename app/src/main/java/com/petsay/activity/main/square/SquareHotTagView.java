package com.petsay.activity.main.square;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetTagVo;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/7
 * @Description
 */
public class SquareHotTagView extends LinearLayout implements View.OnClickListener {
    public SquareHotTagView(Context context) {
        super(context);
        onInitView();
    }

    public SquareHotTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitView();
    }

    private void onInitView() {
        setOrientation(VERTICAL);
//        setBackgroundColor(Color.WHITE);
    }

    public void initView(List<PetTagVo> tags){
        if(tags == null || tags.isEmpty())
            return;
        removeAllViews();
        LinearLayout row = null;
        int size = tags.size();
        int space = PublicMethod.getDiptopx(getContext(), 5);
        int wh = PublicMethod.getDiptopx(getContext(),1);
        int rowNum = size/3;
        if(size%3 != 0)
            rowNum +=1;
        for (int i =0;i<rowNum;i++){
            row = new LinearLayout(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);
            row.setOrientation(HORIZONTAL);
            addView(row);
            for(int j=0;j<3;j++){
                int index = i*3+j;
                PetTagVo vo = null;
                if(index<size)
                    vo = tags.get(index);
                row.addView(getView(vo,space*3));
                if(j<2 && index < size-1) {
                    View vl = new View(getContext());
                    LayoutParams lp = new LayoutParams(wh, LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, space, 0, space);
                    vl.setBackgroundResource(R.color.activity_bg);
                    vl.setLayoutParams(lp);
                    row.addView(vl);
                }
            }
            if(i<rowNum-1) {
                View hl = new View(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, wh);
                lp.setMargins(space * 2, 0, space * 2, 0);
                hl.setBackgroundResource(R.color.activity_bg);
                hl.setLayoutParams(lp);
                addView(hl);
            }
        }
    }

    private View getView(PetTagVo vo,int space){
        TextView txt = new TextView(getContext());
        LayoutParams params = new LayoutParams(0,LayoutParams.WRAP_CONTENT,1);
        txt.setPadding(space,space,space,space);
        txt.setLayoutParams(params);
        if(vo != null) {
            txt.setGravity(Gravity.CENTER);
            txt.setTag(vo);
            txt.setTextColor(Color.parseColor("#72CCDF"));
            txt.setOnClickListener(this);
            txt.setText(vo.getName());
        }else {
            txt.setVisibility(INVISIBLE);
        }
        return txt;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null && v.getTag() instanceof PetTagVo){
            Intent intent = new Intent(getContext(), TagSayListActivity.class);
            intent.putExtra("id",((PetTagVo)v.getTag()).getId());
            getContext().startActivity(intent);
        }
    }
}
