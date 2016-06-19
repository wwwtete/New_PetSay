package com.petsay.activity.petalk.publishtalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.publishtalk.DecorationItemView;
import com.petsay.component.view.publishtalk.DecorationMoreItemView;
import com.petsay.component.view.publishtalk.DecorationMoreView;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.decoration.DecorationBean;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/5
 * @Description 更多饰品
 */
public class DecorationMoreAdapter extends BaseExpandableListAdapter {

    private int mColumnNums = 4;
    private int mSpace;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DecorationTitleBean> mDatas;
    private ExpandableListView mListView;
    private DecorationMoreView.DecorationMoreViewCallback mCallback;

    public DecorationMoreAdapter(Context context,ExpandableListView expandableListView){
        this.mContext = context;
        this.mListView = expandableListView;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = new ArrayList<DecorationTitleBean>();
        mSpace = PublicMethod.getDiptopx(mContext,4);
    }

    public void updateDate(List<DecorationTitleBean> group){
        mDatas.clear();
        mDatas.addAll(group);
        notifyDataSetChanged();
    }

    public void setCallback(DecorationMoreView.DecorationMoreViewCallback callback){
        this.mCallback = callback;
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition < mDatas.size() && mColumnNums < getGroup(groupPosition).getDecorations().size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public DecorationTitleBean getGroup(int groupPosition) {
        if(groupPosition < mDatas.size())
            return mDatas.get(groupPosition);
        return null;
    }

    @Override
    public DecorationBean getChild(int groupPosition, int childPosition) {
        if(groupPosition < mDatas.size()) {
            List<DecorationBean> beans =  mDatas.get(groupPosition).getDecorations();
            if(beans != null && childPosition < beans.size())
                return beans.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.decoration_more_group_item_view, null);
            holder = new GroupViewHolder();
            holder.findViews(convertView);
            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupViewHolder hd = (GroupViewHolder) v.getTag();
                    if (hd != null) {
                        if (hd.isExpanded)
                            mListView.collapseGroup(hd.groupPosition);
                        else
                            mListView.expandGroup(hd.groupPosition, false);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
//        if ( holder.groupPosition != groupPosition){
            holder.llContainer.removeAllViews();
            DecorationTitleBean group = getGroup(groupPosition);
            holder.tvTitle.setText(group.getName());
            holder.ivIcon.setTag(holder);
            List<DecorationBean> beans = group.getDecorations();
            int lenght = Math.min(beans.size(), mColumnNums);
            int mGroupItemWidth =(mListView.getWidth() - mSpace*(mColumnNums -1))/ mColumnNums;
            for (int i = 0; i < lenght; i++) {
                final DecorationMoreItemView view = new DecorationMoreItemView(mContext);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickDecorationItemView(view);
                    }
                });
                view.setWidht(mGroupItemWidth);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.width = mGroupItemWidth;
                if(i < lenght-1)
                    params.rightMargin = mSpace;
                view.updateView(beans.get(i));
                holder.llContainer.addView(view,params);
            }
            if(beans.size() > mColumnNums) {
                holder.ivIcon.setVisibility(View.VISIBLE);
            }else {
                holder.ivIcon.setVisibility(View.GONE);
            }
//        }
        holder.isExpanded = isExpanded;
        holder.groupPosition = groupPosition;

        if (isExpanded) {
            holder.ivIcon.setImageResource(R.drawable.expanded);
            holder.line.setVisibility(View.GONE);
        } else {
            holder.ivIcon.setImageResource(R.drawable.collapsed);
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.decoration_more_childer_item_view,null);
            holder = new ChildViewHolder();
            holder.mGridView = (GridView) convertView.findViewById(R.id.gv_decroation);
            holder.line = convertView.findViewById(R.id.line);
            holder.mAdapter = new DecorationMoreGridAdapter(mContext);
            holder.mGridView.setNumColumns(mColumnNums);
            holder.mGridView.setAdapter(holder.mAdapter);
            holder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onClickDecorationItemView((DecorationItemView) view);
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        List<DecorationBean> beans = getGroup(groupPosition).getDecorations();
        List<DecorationBean> datas = null;
        if(mColumnNums < beans.size()){
            datas = beans.subList(mColumnNums,beans.size());
        }
        holder.mAdapter.updateDecorationData(datas);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void onClickDecorationItemView(DecorationItemView view){
        if(mCallback != null)
            mCallback.onClickDecorationItemView(view);
    }

    class GroupViewHolder{
        public TextView tvTitle;
        public ImageView ivIcon;
        public LinearLayout llContainer;
        public View line;

        public int groupPosition;
        public boolean isExpanded;

        public void findViews(View view) {
            tvTitle = (TextView)view.findViewById(R.id.tv_title);
            ivIcon = (ImageView)view.findViewById(R.id.iv_icon);
            llContainer = (LinearLayout)view.findViewById(R.id.ll_container);
            line = view.findViewById(R.id.line);
        }

    }

    class ChildViewHolder{
        public GridView mGridView;
        public View line;
        public DecorationMoreGridAdapter mAdapter;
    }

}
