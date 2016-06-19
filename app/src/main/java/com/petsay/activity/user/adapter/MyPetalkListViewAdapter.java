package com.petsay.activity.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.adapter.BasePettalkAdapter;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.TagView;
import com.petsay.component.view.functionbar.FunctionBarView;
import com.petsay.component.view.functionbar.ListItemFunctionBarEventHolder;
import com.petsay.component.view.functionbar.StepAnimView;
import com.petsay.constants.Constants;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.ArrayList;
import java.util.List;

public class MyPetalkListViewAdapter extends BasePettalkAdapter {
	private Context mContext;
	private ArrayList<PetalkVo> _sayVos;

	private IAddShowLocationViewService addShowLocationViewService;
//	private int mLayoutWidth;
//	private boolean mFirstInit = true;

	public MyPetalkListViewAdapter(Context context,IAddShowLocationViewService addShowLocationViewService) {
        super(context,PublicMethod.getDisplayWidth(context)-PublicMethod.getDiptopx(context,80));
		mContext = context;
		this.addShowLocationViewService = addShowLocationViewService;
		_sayVos = new ArrayList<PetalkVo>();
//		mLayoutWidth=PublicMethod.getDisplayWidth(mContext)-PublicMethod.getDiptopx(mContext,80);
	}

    @Override
    public void refreshData(List data) {
        _sayVos.clear();
        if (data != null && data.size() > 0) {
            UserManager.getSingleton().addFocusAndStepByList(data);
            _sayVos.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addMore(List<PetalkVo> sayVos) {
		if (sayVos != null && sayVos.size() > 0) {
			UserManager.getSingleton().addFocusAndStepByList(sayVos);
			_sayVos.addAll(sayVos);
			notifyDataSetChanged();
		} else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void checkIsDeleted(){
		for (int i = 0; i < _sayVos.size(); i++) {
			if (Constants.delPetalk.containsKey(_sayVos.get(i).getPetalkId())) {
				_sayVos.remove(i);
			}
		}
	}

	public void clear() {
		_sayVos.clear();
		notifyDataSetChanged();
	}

	private class Holder extends BasePetalkViewHolder{
//		private ImageView imgPet;
		private TextView tvContent;
		private TagView tagView;
		private TextView tvPublishTime;
		private FunctionBarView functionBarView;
		private StepAnimView stepAnimView;
		private ListItemFunctionBarEventHolder functionBarEventHolder;
//		private AudioGifView amGif;
//        private ImageView ivFlag;
	}

	@Override
	public int getCount() {
		if (null == _sayVos || _sayVos.isEmpty()) {
			return 0;
		}
		return _sayVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null == _sayVos || _sayVos.isEmpty()) {
			return null;
		}
		return _sayVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.my_petalk_list_item, null);
			holder = new Holder();
            findPublicView(holder,convertView);
			holder.tvPublishTime=(TextView) convertView.findViewById(R.id.tv_publish_time);
//			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
//			PublicMethod.initPetalkViewLayout(holder.imgPet, mLayoutWidth);
			holder.tagView = (TagView) convertView.findViewById(R.id.tagview);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.functionBarView=(FunctionBarView) convertView.findViewById(R.id.functionbar);
			holder.stepAnimView = (StepAnimView) convertView.findViewById(R.id.stepanim);
			holder.functionBarView.setStepAnimView(holder.stepAnimView);
			holder.functionBarEventHolder = new ListItemFunctionBarEventHolder(mContext, holder.functionBarView, addShowLocationViewService);
//			holder.amGif = (AudioGifView) convertView.findViewById(R.id.am_gif);
//			ExProgressBar progressBar = (ExProgressBar) convertView.findViewById(R.id.pro_loaderpro);
//			ImageView playView = (ImageView) convertView.findViewById(R.id.img_play);
//			LayoutParams params = (LayoutParams) playView.getLayoutParams();
//			params.topMargin = (mLayoutWidth - PublicMethod.getDiptopx(mContext, 100))/2;//PublicMethod.getDiptopx(mContext, 60);
//			holder.amGif.setPlayBtnView(playView);
//			ImageLoaderListener listener = new ImageLoaderListener(progressBar);
//			holder.amGif.setImageLoaderListener(listener);
//			holder.imgPet.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickGifView(holder.amGif);
//				}
//			});
//            holder.ivFlag = (ImageView) convertView.findViewById(R.id.iv_flag);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		 PetalkVo sayVo = _sayVos.get(position);
		 final PetVo petVo = sayVo.getPet();


		if (sayVo.getType().equals(Constants.RELAY)&& null != sayVo.getComment()) {
			 holder.tvPublishTime.setText(PublicMethod.formatTimeToString(sayVo.getRelayTime(),"yyyy-MM-dd kk:mm"));
		} else {
			 holder.tvPublishTime.setText(PublicMethod.formatTimeToString(sayVo.getCreateTime(),"yyyy-MM-dd kk:mm"));
		}
		
		if (sayVo.getDescription().trim().equals("")) {
			holder.tvContent.setVisibility(View.GONE);
		}else {
			holder.tvContent.setVisibility(View.VISIBLE);
			holder.tvContent.setText(sayVo.getDescription());
		}
		holder.functionBarEventHolder.setSayVo(sayVo);
		holder.functionBarView.setValue(sayVo.getCounter().getRelay(), sayVo.getCounter().getComment(),sayVo.getCounter().getFavour(), sayVo.getCounter().getShare());
		holder.functionBarView.setStepStatus(sayVo.getPetalkId());
		
		holder.tagView.removeAllViews();
		if (null != sayVo.getTags() && sayVo.getTags().length > 0) {
			holder.tagView.setTags(sayVo.getTags(), holder.tagView.getTagBgResId(mContext),holder.tagView.Use_SayList, mLayoutWidth);
		}

//        if(sayVo.isAudioModel()) {
//        ImageLoaderListener listener = holder.amGif.getImageLoaderListener();
//        listener.reset();
//        ImageLoaderHelp.displayContentImage(sayVo.getPhotoUrl(), holder.imgPet, listener, listener);
//            // 初始化Gif的Item的布局
//            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) holder.amGif.getLayoutParams();
//            holder.amGif.initData(sayVo, mLayoutWidth, mLayoutWidth);
//            PetalkDecorationVo ad = sayVo.getDecorations()[0];
//            PublicMethod.updateGifItemLayout(mLayoutWidth, mLayoutWidth, ad, holder.amGif, params);
//            boolean autoPlay = GifViewManager.getInstance().getAllowAutoPlay();
//            holder.amGif.setPlayBtnVisibility(!autoPlay);
//            if (position == 0 && mFirstInit && autoPlay) {
//                GifViewManager.getInstance().playGif(holder.amGif);
//                mFirstInit = false;
//            }
//        }
        refreshItemTypeView(sayVo.getModel(),holder.ivFlag);
        refreshPetalk(sayVo, holder, position);
		return convertView;
	}
}
