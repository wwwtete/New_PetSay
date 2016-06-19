package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.petsay.R;
import com.petsay.activity.petalk.publishtalk.adapter.TagGridAdapter;
import com.petsay.component.view.TitleBar;
import com.petsay.vo.petalk.PetTagVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/10
 * @Description
 */
public class MoreTagView extends LinearLayout implements TitleBar.OnClickBackListener, AdapterView.OnItemClickListener {

    private TitleBar mTitleBar;
    private GridView mGridView;
    private TagGridAdapter mTagAdapter;
    private MoreTagCallback mCallback;

    public MoreTagView(Context context){
        super(context);
        initView();
    }

    public MoreTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(),R.layout.more_tag_view,this);
        setBackgroundResource(R.drawable.label_list_bg);
        setOrientation(VERTICAL);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        mGridView = (GridView) findViewById(R.id.gv_tags);
        mTagAdapter = new TagGridAdapter(getContext());
        mGridView.setAdapter(mTagAdapter);
        setListener();
    }

    private void setListener() {
        mTitleBar.setOnClickBackListener(this);
        mGridView.setOnItemClickListener(this);
    }

    public void setCallback(MoreTagCallback callback){
        this.mCallback = callback;
    }

    public boolean isShow(){
        return  getVisibility() == VISIBLE;
    }

    public void showView(List<? extends Serializable> tags,String title){
        mGridView.setSelection(0);
        mTagAdapter.refreshData(tags);
        mTitleBar.setTitleText(title);
        this.setVisibility(VISIBLE);
    }

    public void onHiden(){
        this.setVisibility(GONE);
    }

    @Override
    public void OnClickBackListener() {
        onHiden();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mCallback != null){
//            if(mTagAdapter.isGroup()){
//                mCallback.onCLickTagGroupCallback(this, (PetTagGroupVo) mTagAdapter.getItem(position));
//            }else {
                mCallback.onClickTagCallback(this, (PetTagVo) mTagAdapter.getItem(position));
//            }
        }
//        onHiden();
    }

    public interface MoreTagCallback{
        public void onClickTagCallback(MoreTagView view,PetTagVo tagVo);
//        public void onCLickTagGroupCallback(MoreTagView view,PetTagGroupVo groupVo);
    }
}
