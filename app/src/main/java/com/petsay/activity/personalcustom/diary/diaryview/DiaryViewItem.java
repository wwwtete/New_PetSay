package com.petsay.activity.personalcustom.diary.diaryview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.personalcustom.diary.diaryview.DiaryViewModule;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/25
 * @Description
 */
public class DiaryViewItem extends LinearLayout {

    private DiaryViewModule mLeftView;
    private DiaryViewModule mRightView;
    private View mFlLeft;
    private View mFlRight;
    private View mLShadown;
    private View mRShadown;

    private ImageView mImg1;
    private ImageView mImg2;

    public DiaryViewItem(Context context) {
        super(context);
        initView();
    }

    public DiaryViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.WHITE);

        inflate(getContext(), R.layout.diaryviewitem, this);
        mLeftView = (DiaryViewModule) findViewById(R.id.left_page);
        mRightView = (DiaryViewModule) findViewById(R.id.right_page);
        mFlLeft = findViewById(R.id.fl_left);
        mFlRight = findViewById(R.id.fl_right);
        mLShadown = findViewById(R.id.left_shadown);
        mRShadown = findViewById(R.id.right_shadown);

//        mLeftView = new DiaryViewModule(getContext());
//        mRightView = new DiaryViewModule(getContext());

//        mSpace = PublicMethod.getDiptopx(getContext(), 2);

//        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
//        lp.rightMargin = mSpace;
//        addView(mLeftView, lp);
//        LayoutParams rp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
//        rp.leftMargin = mSpace;
//        addView(mRightView, rp);

//        mImg1 = new ImageView(getContext());
//        mImg2 = new ImageView(getContext());
//        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
////        lp.rightMargin = mSpace;
//        addView(mImg1, lp);
//        LayoutParams rp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
////        rp.leftMargin = mSpace;
//        mRightView.setVisibility(INVISIBLE);
//        addView(mImg2, rp);
    }

    public void updateView(int pageIndex,List<PetalkVo> vos){
        if(pageIndex <= 0){
            mRightView.updateView(0,vos);
            mLeftView.setVisibility(GONE);

        }
        mLeftView.updateView(pageIndex * 2, vos);
        mRightView.updateView(pageIndex*2+1,vos);
//        mImg1.setImageResource(R.drawable.guide1_pic);
//        mImg2.setImageResource(R.drawable.guide2_pic);
    }

}
