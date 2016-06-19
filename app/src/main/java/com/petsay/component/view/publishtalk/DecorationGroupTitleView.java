package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.publishtalk.BaseEditActivity;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/30
 * @Description
 */
public class DecorationGroupTitleView extends FrameLayout {


    private int[] mNomalIcons = {R.drawable.dt_moth_nomarl,R.drawable.dt_costume_nomal,
            R.drawable.dt_dialog_nomal,R.drawable.dt_rahmen_nomal};
    private int[] mPullIcons = {R.drawable.dt_moth_pull,R.drawable.dt_costume_pull,
            R.drawable.dt_dialog_pull,R.drawable.dt_rahmen_pull};
    private String[] mKeys = {BaseEditActivity.TYPE_MOUTH,BaseEditActivity.TYPE_COSTUME,
            BaseEditActivity.TYPE_DIALOG,BaseEditActivity.TYPE_RAHMEN};
    private LinearLayout mContainerView;
    private List<DecorationTitleBean> mTitleBeans;
    private ClickDecorationGroupCallback mCallback;
    private int mSpace;
    private View mCurrView;

    public DecorationGroupTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mSpace = PublicMethod.getDiptopx(getContext(),5);
        HorizontalScrollView scrollView = new HorizontalScrollView(getContext());
        this.setPadding(0,mSpace,0,mSpace);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        params.setMargins(mSpace,space,space,space);
        scrollView.setLayoutParams(params);
        mContainerView = new LinearLayout(getContext());
        mContainerView.setOrientation(LinearLayout.HORIZONTAL);
        scrollView.addView(mContainerView);
        addView(scrollView);
    }

    public void setCallback(ClickDecorationGroupCallback callback){
        this.mCallback = callback;
    }

    /**
     * 设置数据
     * @param titleBeans
     */
    public void setGroupData(List<DecorationTitleBean> titleBeans) {
        mTitleBeans = titleBeans;
       mContainerView.removeAllViews();
        if(titleBeans != null){
            int avg = PublicMethod.getDisplayWidth(getContext()) - mSpace*2;
            avg = avg/titleBeans.size();
            int size = titleBeans.size();
            for (int i = 0 ;i < size ; i++){
                View view= getView(titleBeans.get(i));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickGroupTitle(v);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.width = avg;
                if(i<size-1)
                    params.rightMargin = mSpace;
                mContainerView.addView(view,params);
                if(i == 0) {
                    onClickGroupTitle(view);
                }
            }
        }

    }

    private void onClickGroupTitle(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if(view != mCurrView){
            if(mCurrView != null){
                ViewHolder currHoldre = (ViewHolder)mCurrView.getTag();
                currHoldre.ivIcon.setImageResource(currHoldre.nomalSourceId);
            }
            holder.ivIcon.setImageResource(holder.pullSourceId);
        }
        mCurrView = view;

        if(mCallback != null){
            mCallback.onClickDecoratoiinGroupTitle(holder.titleBean);
        }
    }

    private View getView(DecorationTitleBean titleBean){
        View view = inflate(getContext(), R.layout.decoration_group_item_view,null);
        ViewHolder holder = new ViewHolder();
        holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        for(int i= 0;i<mKeys.length;i++){
            if(mKeys[i].equals(titleBean.getType())){
                holder.nomalSourceId = mNomalIcons[i];
                holder.pullSourceId = mPullIcons[i];
                break;
            }
        }
        holder.ivIcon.setImageResource(holder.nomalSourceId);
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvTitle.setText(titleBean.getName());
        holder.titleBean = titleBean;
        view.setTag(holder);
        return view;
    }

    public interface ClickDecorationGroupCallback{
        public void onClickDecoratoiinGroupTitle(DecorationTitleBean bean);
    }

    class ViewHolder{
        public TextView tvTitle;
        public ImageView ivIcon;
        public DecorationTitleBean titleBean;
        public int nomalSourceId = -1;
        public int pullSourceId = -1;
    }

}
