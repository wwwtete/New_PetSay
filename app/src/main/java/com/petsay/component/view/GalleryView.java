package com.petsay.component.view;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.personalcustom.PostCardVo;
import com.petsay.vo.personalcustom.SpriteVo;
import com.petsay.vo.petalk.PetalkVo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GalleryView extends RelativeLayout{// implements android.view.View.OnClickListener{

	private RecyclerView mRecyclerView;
	private  LinearLayoutManager mLayoutManager ;
	private Context mContext;
    private List<PetalkVo> mPetalkVos=new ArrayList<PetalkVo>();
    private MyAdapter mAdapter;
    private OnItemClickListener mItemClickListener;
    private int mSelectionPosition=0;
	public GalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(mContext, R.layout.galleryview, this);
		
		mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(mContext);  
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);  
        mRecyclerView.setLayoutManager(mLayoutManager);  
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new MyAdapter(mPetalkVos);  
        mRecyclerView.setAdapter(mAdapter); 
	}
	
	public void setList(List<PetalkVo> petalkVos){
		int size=mPetalkVos.size();
		mPetalkVos.addAll(petalkVos);
		mAdapter.notifyItemRangeInserted(size, petalkVos.size());
	}

	
	public void setOnItemClickListener(OnItemClickListener itemClickListener){
		mItemClickListener=itemClickListener;
	}
	
	public void setOnRecylerViewScrollListener(RecyclerView.OnScrollListener listener){
		mRecyclerView.setOnScrollListener(listener);
	}
	
	public void setReclyerViewSelection(int position){
		mSelectionPosition=position;
		mLayoutManager.scrollToPosition(position);
		mAdapter.notifyDataSetChanged();
	}
	
	public int getReclyerViewSelection(){
		return mSelectionPosition;
	}
	
	public int getRecylerViewItemCount(){
		if (null==mAdapter) {
			return 0;
		}else {
			return mAdapter.getItemCount();
		}
	}
	
	public interface OnItemClickListener{
		void onItemClick(View itemView,int position);
	}

	
	public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
		private List<PetalkVo> mVos; // 外面传入的数据
		public class MyViewHolder extends RecyclerView.ViewHolder {
			ImageView mImageView;
            View mBorderView;
			public MyViewHolder(View v) {
				super(v);
				mImageView = (ImageView) v.findViewById(R.id.img);
				mBorderView=v.findViewById(R.id.border);
			}

		}

		public MyAdapter(List<PetalkVo> petalkVos) {
			this.mVos = petalkVos;
		}

		/**
		 * 获取总的条目数量
		 */
		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			if (null != mVos) {
				return mVos.size();
			}
				return 0;
			
		}

		/**
		 * 创建ViewHolder
		 */
		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			// TODO Auto-generated method stub
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyler_item, parent,false);
//			ImageView v=new ImageView(mContext);
			MyViewHolder holder = new MyViewHolder(v);
			
			return holder;
		}

		/**
		 * 将数据绑定到ViewHolder上
		 */
		@Override
		public void onBindViewHolder(final MyViewHolder holder, final int position) {
			ImageLoaderHelp.displayContentImage(mVos.get(position).getPhotoUrl()+"?imageView2/2/w/100", holder.mImageView);
			if (mSelectionPosition==position) {
				holder.mBorderView.setBackgroundResource(R.drawable.postcard_select_shape);
				
			}else {
				holder.mBorderView.setBackgroundColor(Color.TRANSPARENT);
			}
			
			holder.mImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (null!=mItemClickListener&&mSelectionPosition!=position) {
						mSelectionPosition=position;
						mItemClickListener.onItemClick(holder.itemView, position);
						notifyDataSetChanged();
					}
				}
			});
		}
	}
}
