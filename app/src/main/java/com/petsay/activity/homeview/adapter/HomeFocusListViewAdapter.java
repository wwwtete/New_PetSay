package com.petsay.activity.homeview.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.adapter.BasePettalkAdapter;
import com.petsay.component.gifview.ImageLoaderListener;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.TagView;
import com.petsay.component.view.functionbar.FunctionBarView;
import com.petsay.component.view.functionbar.ListItemFunctionBarEventHolder;
import com.petsay.component.view.functionbar.StepAnimView;
import com.petsay.constants.Constants;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

public class HomeFocusListViewAdapter extends BasePettalkAdapter {
	private Context mContext;
	private ArrayList<PetalkVo> _sayVos;

	public static final int HIDE_ATTENTION = 1;
	public static final int SHOW_ATTENTION = 2;
	private IAddShowLocationViewService addShowLocationViewService;

	public HomeFocusListViewAdapter(Context context,IAddShowLocationViewService addShowLocationViewService) {
        super(context,PublicMethod.getDisplayWidth(context)-PublicMethod.getDiptopx(context,80));
		mContext = context;
		this.addShowLocationViewService = addShowLocationViewService;
		_sayVos = new ArrayList<PetalkVo>();
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

	public void clear() {
		_sayVos.clear();
		notifyDataSetChanged();
	}

	private class Holder extends BasePetalkViewHolder {
		private ImageView  img_header,imgSex,imgStar;
		private TextView tvContent;
//		private TextView tvForwardName, tvForwardTime, tvForwardContent;
		private TagView tagView;
		private TextView tvName,tvPublishTime,tvType,tvRelayComment;//说说类型
//		private ImageView imgGrade;
//		private AttentionButtonView tvAttention;
//		private RelativeLayout rLayoutForward;
		private FunctionBarView functionBarView;
		private StepAnimView stepAnimView;
		private ListItemFunctionBarEventHolder functionBarEventHolder;
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_focus_list_item, null);
			holder = new Holder();
            findPublicView(holder,convertView);
			holder.img_header = (ImageView) convertView.findViewById(R.id.img_header);
			holder.imgSex=(ImageView) convertView.findViewById(R.id.img_sex);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvPublishTime=(TextView) convertView.findViewById(R.id.tv_publish_time);
			holder.tvType=(TextView) convertView.findViewById(R.id.tv_type);
			holder.tvRelayComment=(TextView) convertView.findViewById(R.id.tv_relaycomment);
			holder.imgStar=(ImageView) convertView.findViewById(R.id.img_star);
			holder.tagView = (TagView) convertView.findViewById(R.id.tagview);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.functionBarView=(FunctionBarView) convertView.findViewById(R.id.functionbar);
			holder.stepAnimView = (StepAnimView) convertView.findViewById(R.id.stepanim);
			holder.functionBarView.setStepAnimView(holder.stepAnimView);
			holder.functionBarEventHolder = new ListItemFunctionBarEventHolder(mContext, holder.functionBarView, addShowLocationViewService);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		 PetalkVo sayVo = _sayVos.get(position);
		 final PetVo petVo = sayVo.getPet();


		if (sayVo.getType().equals(Constants.RELAY)&&!TextUtils.isEmpty(sayVo.getComment())) {
			 holder.tvPublishTime.setText(PublicMethod.formatTimeToString(sayVo.getRelayTime(),"yyyy-MM-dd kk:mm"));
			 holder.tvType.setVisibility(View.VISIBLE);
			 holder.tvRelayComment.setVisibility(View.VISIBLE);
			 holder.tvType.setText(Html.fromHtml("<font color=\"#cfcdfd\">转发自：</font><font color=\"#787878\">"+sayVo.getPetNickName()+"</font>"));
             holder.tvRelayComment.setText(sayVo.getComment());
			 holder.tvName.setText(sayVo.getAimPetNickName());
			 ImageLoaderHelp.displayHeaderImage(sayVo.getAimPetHeadPortrait(), holder.img_header);
			 
			 
		} else {
			 holder.tvPublishTime.setText(PublicMethod.formatTimeToString(sayVo.getCreateTime(),"yyyy-MM-dd kk:mm"));
			 holder.tvName.setText(petVo.getNickName());
			 ImageLoaderHelp.displayHeaderImage(sayVo.getPetHeadPortrait(), holder.img_header);
			 holder.tvType.setVisibility(View.GONE);
			 holder.tvRelayComment.setVisibility(View.GONE);
			 holder.tvType.setText("");
		}
	   
		if (petVo.getGender()==0) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.female);
			
		}else if (petVo.getGender()==1) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.male);
		}else {
			holder.imgSex.setVisibility(View.GONE);
		}

		holder.img_header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,petVo);
			}
		});
		
		if (sayVo.getDescription().trim().equals("")) {
			holder.tvContent.setVisibility(View.GONE);
		}else {
			holder.tvContent.setVisibility(View.VISIBLE);
			holder.tvContent.setText(sayVo.getDescription());
		}
		holder.functionBarEventHolder.setSayVo(sayVo);
		holder.functionBarView.setValue(sayVo.getCounter().getRelay(), sayVo.getCounter().getComment(),sayVo.getCounter().getFavour(), sayVo.getCounter().getShare());
		holder.functionBarView.setStepStatus(sayVo.getPetalkId());
        ImageLoaderListener listener = holder.amGif.getImageLoaderListener();
        listener.reset();
        ImageLoaderHelp.displayContentImage(sayVo.getPhotoUrl(), holder.imgPet,listener, listener);


        refreshItemTypeView(sayVo.getModel(), holder.ivFlag);
        refreshPetalk(sayVo, holder, position);

		holder.tagView.removeAllViews();
		if (null != sayVo.getTags() && sayVo.getTags().length > 0) {
			holder.tagView.setTags(sayVo.getTags(), holder.tagView.getTagBgResId(mContext),holder.tagView.Use_SayList, mLayoutWidth);
		}
		
		return convertView;
	}

}
