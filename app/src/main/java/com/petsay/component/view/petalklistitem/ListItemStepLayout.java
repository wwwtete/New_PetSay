package com.petsay.component.view.petalklistitem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.CommentVo;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

/**
 * 2.5.0版本添加
 * 说说列表中踩用户显示的layout
 * @author G
 *
 */
public class ListItemStepLayout extends LinearLayout {
	private Context mContext;
	private TextView tvCount;
	private LinearLayout petLayout;
	private LayoutParams layoutParams ;
    private int mMaxItem = 6;
	

	public ListItemStepLayout(Context context) {
		super(context);
		mContext = context;
		inflate(mContext, R.layout.listitem_step_layout, this);
		initView();

	}

	public ListItemStepLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(mContext, R.layout.listitem_step_layout, this);
		initView();
	}

	private void initView() {
		tvCount = (TextView) findViewById(R.id.tv_step1);
		petLayout = (LinearLayout) findViewById(R.id.layout_userheader);
		layoutParams = new LayoutParams(PublicMethod.getPxInt(30,mContext), PublicMethod.getPxInt(30, mContext));
		layoutParams.setMargins(0, PublicMethod.getPxInt(2,mContext), PublicMethod.getPxInt(5,mContext), PublicMethod.getPxInt(2,mContext));
	}

    public void addFirstView(CommentVo commentVo,int count){
        tvCount.setText(count+"");
        setVisibility(View.VISIBLE);
        if(petLayout.getChildCount() >= mMaxItem){
            View view = petLayout.getChildAt(0);
            CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_unactive);
            ImageLoaderHelp.displayHeaderImage(commentVo.getPetHeadPortrait(),circleImageView);
            circleImageView.setTag(commentVo);
        }else {
            View view = getView(commentVo);
            petLayout.addView(view,0);
        }

    }

	public void setPetList(List<CommentVo> commentVos,int count) {
		petLayout.removeAllViews();
		tvCount.setText(count+"");
		if (null!=commentVos&&!commentVos.isEmpty()) {
			setVisibility(View.VISIBLE);
			
			for (int i = 0; i < commentVos.size(); i++) {
				if (i < mMaxItem) {
					CommentVo commentVo=commentVos.get(i);
                    View view = getView(commentVo);
					petLayout.addView(view);
				}
			}
		}else {
			setVisibility(View.GONE);
		}
		
	}

    private View getView(CommentVo commentVo){
        View view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.unactive_pet_item, null);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_unactive);
        // ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(),circleImageView);
        // CircleImageView header=new CircleImageView(mContext);
        circleImageView.setBorderWidth(0);
        // header.setImageResource(R.drawable.placeholderhead);
        ImageLoaderHelp.displayHeaderImage(commentVo.getPetHeadPortrait(),circleImageView);
        circleImageView.setLayoutParams(layoutParams);
        circleImageView.setTag(commentVo);
        circleImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CommentVo vo = (CommentVo) v.getTag();
                PetVo pet=new PetVo();
                pet.setId(vo.getPetId());
                ActivityTurnToManager.getSingleton().userHeaderGoto(getContext(), pet);
            }
        });
        return view;
    }

	public void setPetList(int size) {
		
	}

}
