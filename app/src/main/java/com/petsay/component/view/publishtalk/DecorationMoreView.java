package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.petalk.publishtalk.adapter.DecorationMoreAdapter;
import com.petsay.component.view.TitleBar;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/5
 * @Description
 */
public class DecorationMoreView extends RelativeLayout implements TitleBar.OnClickBackListener, ExpandableListView.OnGroupClickListener {

    private TitleBar mTitleBar;
    private ExpandableListView mListView;
    private DecorationMoreAdapter mAdapter;
    private DecorationMoreViewCallback mCallback;

    public DecorationMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.decoration_more_view,this);
        initTitleBar();
        mListView = (ExpandableListView) findViewById(R.id.exlistview);
        mListView.setOnGroupClickListener(this);
        mAdapter = new DecorationMoreAdapter(getContext(),mListView);
        mListView.setAdapter(mAdapter);
    }

    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        mTitleBar.setOnClickBackListener(this);
        mTitleBar.setBackgroundColor((Color.parseColor("#5a5a5a")));
        mTitleBar.setTitleText("更多");
    }

    public void updateView(List<DecorationTitleBean> secondGroup){
        mAdapter.updateDate(secondGroup);
        for (int i=0;i<mAdapter.getGroupCount();i++) {
            mListView.collapseGroup(i);
        }
    }

    public void setCallback(DecorationMoreViewCallback callback){
        this.mCallback = callback;
        this.mAdapter.setCallback(callback);
    }

    @Override
    public void OnClickBackListener() {
        onHiden();
    }

    public void onHiden(){
        this.setVisibility(GONE);
    }

    public void onShow(){
        this.setVisibility(VISIBLE);
    }

    public boolean isShow(){
        return getVisibility() == VISIBLE;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    public interface DecorationMoreViewCallback{
        public void onClickDecorationItemView(DecorationItemView view);
    }
}
