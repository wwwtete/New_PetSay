package com.petsay.activity.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.gifview.AudioGifView;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.gifview.ImageLoaderListener;
import com.petsay.component.view.ExProgressBar;
import com.petsay.component.view.TagView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

public class OtherUserListAdapter extends BaseAdapter {

	private Context mContext;
	private List<PetalkVo> _sayVos;
	public int currentSelectIndex=0;
	private int mLayoutWidth;
	private boolean mFirstInit = true;
	public OtherUserListAdapter(Context context, List<PetalkVo> sayVos) {
		mContext = context;
		this._sayVos=sayVos;
		mLayoutWidth= PublicMethod.getDisplayWidth(mContext) - PublicMethod.getDiptopx(mContext, 10);
	}

	/**
	 * 刷新数据
	 * @param sayVos
	 */
	public void refreshData(List<PetalkVo> sayVos){
		if(sayVos != null && sayVos.size() > 0){
			_sayVos.clear();
			_sayVos.addAll(sayVos);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if (null==_sayVos) {
			return 0;
		}else {
			return _sayVos.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (null==_sayVos||_sayVos.isEmpty()) {
			return null;
		}else {
			return _sayVos.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder ;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_list_item, null);
			holder = new Holder();
			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
			PublicMethod.initPetalkViewLayout(holder.imgPet, mLayoutWidth);
			holder.imgHeader = (ImageView) convertView.findViewById(R.id.img_header);
//			holder.layoutTag = (LinearLayout) convertView.findViewById(R.id.layout_tag);
			holder.tagView=(TagView) convertView.findViewById(R.id.tagview);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.layoutPadding = (LinearLayout) convertView.findViewById(R.id.layout_padding);
			holder.tvAttention = (TextView) convertView.findViewById(R.id.btn_attention);
			holder.layoutComment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
			holder.layoutForward = (LinearLayout) convertView.findViewById(R.id.layout_forward);
			holder.layoutShare = (LinearLayout) convertView.findViewById(R.id.layout_share);
			holder.layoutStep = (LinearLayout) convertView.findViewById(R.id.layout_step);
			holder.layoutTab = (LinearLayout) convertView.findViewById(R.id.layout_tab);
			holder.layoutTabRelease=(LinearLayout) convertView.findViewById(R.id.llayout_release);
			holder.layoutTabForward=(LinearLayout) convertView.findViewById(R.id.llayout_forward);
			holder.layoutTabComment=(LinearLayout) convertView.findViewById(R.id.llayout_comment);
			holder.layoutTabStep=(LinearLayout) convertView.findViewById(R.id.llayout_step);
			holder.amGif = (AudioGifView) convertView.findViewById(R.id.am_gif);
			ExProgressBar progressBar = (ExProgressBar)convertView.findViewById(R.id.pro_loaderpro);
//			PicassoCallback callback = new PicassoCallback(progressBar);
//			holder.amGif.setPicassoCallback(callback);
			holder.amGif.setImageLoaderListener(new ImageLoaderListener(progressBar));
			holder.imgPet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickGifView(holder.amGif);
				}
			});
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		PetalkVo sayVo = _sayVos.get(position);
		holder.tvContent.setText(sayVo.getDescription());
		holder.tvDate.setText(PublicMethod.calculateTime(sayVo.getCreateTime()));
		holder.tvName.setText(sayVo.getPetNickName());
		holder.tvAttention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		
		if (position == _sayVos.size() - 1) {
			holder.layoutPadding.setVisibility(View.VISIBLE);
		} else {
			holder.layoutPadding.setVisibility(View.GONE);
		}

//		holder.layoutTabRelease.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mFragment.changeTabLayoutBg(holder.layoutTabRelease, holder.layoutTabComment, holder.layoutTabForward, holder.layoutTabStep);
//				mFragment.changeAdapter(R.id.layout_top_release);			
//			}
//		});
//		holder.layoutTabComment.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mFragment.changeTabLayoutBg(holder.layoutTabComment,holder.layoutTabRelease, holder.layoutTabForward,holder.layoutTabStep);
//				mFragment.changeAdapter(R.id.layout_top_comment);	
//			}
//		});
//		holder.layoutTabForward.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mFragment.changeTabLayoutBg(holder.layoutTabForward,holder.layoutTabRelease, holder.layoutTabComment,holder.layoutTabStep);
//				mFragment.changeAdapter(R.id.layout_top_forward);	
//			}
//		});
//		holder.layoutTabStep.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mFragment.changeTabLayoutBg(holder.layoutTabStep,holder.layoutTabRelease, holder.layoutTabForward,holder.layoutTabComment);
//				mFragment.changeAdapter(R.id.layout_top_step);	
//			}
//		});

		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		holder.tagView.removeAllViews();
		if (null != sayVo.getTags() && sayVo.getTags().length > 0) {
			holder.tagView.setTags(sayVo.getTags(),holder.tagView.getTagBgResId(mContext),holder.tagView.Use_SayList,mLayoutWidth);
		} else{}
        if(sayVo.isAudioModel()) {
            //初始化Gif的Item的布局
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) holder.amGif.getLayoutParams();
            holder.amGif.initData(sayVo, mLayoutWidth, mLayoutWidth);
            PetalkDecorationVo ad = sayVo.getDecorations()[0];
            PublicMethod.updateGifItemLayout(mLayoutWidth, mLayoutWidth, ad, holder.amGif, params);

            ImageLoaderListener listener = holder.amGif.getImageLoaderListener();
            listener.reset();
            ImageLoaderHelp.displayContentImage(sayVo.getPhotoUrl(), holder.imgPet, listener, listener);
            ImageLoaderHelp.displayHeaderImage(sayVo.getPet().getHeadPortrait(), holder.imgHeader);
            if (position == 0 && mFirstInit) {
                GifViewManager.getInstance().playGif(holder.amGif);
                mFirstInit = false;
            }
        }
		return convertView;
	}

	private void onClickGifView(AudioGifView view){
		GifViewManager.getInstance().pauseGif(view);
	}

	private class Holder {
		private LinearLayout layoutTab;
		private ImageView imgPet, imgHeader,imgTopHeader;
		private TextView tvContent;
//		private LinearLayout layoutTag;
		private TagView tagView;
		private TextView tvName;
		private TextView tvDate;
		private LinearLayout layoutPadding;
		private TextView tvAttention;
		private LinearLayout layoutForward, layoutStep, layoutShare,layoutComment;
		private LinearLayout layoutTabRelease,layoutTabForward,layoutTabComment,layoutTabStep;
		private AudioGifView amGif;
	}
}