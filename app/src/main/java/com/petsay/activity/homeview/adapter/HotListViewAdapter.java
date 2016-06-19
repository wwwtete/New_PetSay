package com.petsay.activity.homeview.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.petalk.adapter.BasePettalkAdapter;
import com.petsay.component.animationview.AttentionButtonView;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.TagView;
import com.petsay.component.view.functionbar.FunctionBarView;
import com.petsay.component.view.functionbar.ListItemFunctionBarEventHolder;
import com.petsay.component.view.functionbar.StepAnimView;
import com.petsay.component.view.petalklistitem.ListItemCommentLayout;
import com.petsay.component.view.petalklistitem.ListItemStepLayout;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

public class HotListViewAdapter extends BasePettalkAdapter implements NetCallbackInterface {
	private Context mContext;
	private List<PetalkVo> _sayVos;
//	private UserModule mUserModule;
//	private UserData mUserData;
//	private SayModule mSayModule;
//	private SayData mSayData;
	private UserNet mUserNet;
	private UserManager mUserManager;
	private IAddShowLocationViewService mViewService;
	private boolean mIsShowFocus;
	private int mClickPosition=0;
	public HotListViewAdapter(Context context,IAddShowLocationViewService viewService,boolean isShowFocus) {
        super(context,PublicMethod.getDisplayWidth(context));
        mContext = context;
        mIsShowFocus=isShowFocus;
		_sayVos = new ArrayList<PetalkVo>();
		mUserManager=UserManager.getSingleton();
		mViewService=viewService;
		
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(context);
	}

    @Override
    public void refreshData(List data) {
        if(data != null ){
            UserManager.getSingleton().addFocusAndStepByList(data);
            _sayVos.clear();
            _sayVos.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addMore(List<PetalkVo> data){
		if(data != null && data.size() > 0){
			UserManager.getSingleton().addFocusAndStepByList(data);
			_sayVos.addAll(data);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void checkIsDeleted(){
		if(null!=Constants.delPetalk){
			for (int i = 0; i < _sayVos.size(); i++) {
				if (Constants.delPetalk.containsKey(_sayVos.get(i).getPetalkId())) {
					_sayVos.remove(i);
				}
			}
		}
		
	}

	public void clear(){
		_sayVos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==_sayVos||_sayVos.isEmpty()) {
			return 0;
		}
		return _sayVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==_sayVos||_sayVos.isEmpty()) {
			return null;
		}
		return _sayVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_list_item, null);
			holder = new Holder();
			findPublicView(holder, convertView);
			holder.img_header = (CircleImageView) convertView.findViewById(R.id.img_header);
			holder.imgSex = (ImageView) convertView.findViewById(R.id.img_sex);
			holder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
			holder.tvGrade = (TextView) convertView.findViewById(R.id.tv_grade);
			holder.imgGrade = (ImageView) convertView.findViewById(R.id.img_grade);
			holder.imgStep = (ImageView) convertView.findViewById(R.id.img_step);
			holder.tagView = (TagView) convertView.findViewById(R.id.tagview);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.imgStar = (ImageView) convertView.findViewById(R.id.img_star);
			holder.functionBarView = (FunctionBarView) convertView.findViewById(R.id.functionbar);
			holder.stepAnimView = (StepAnimView) convertView.findViewById(R.id.stepanim);
			holder.functionBarView.setStepAnimView(holder.stepAnimView);
			holder.functionBarEventHolder = new ListItemFunctionBarEventHolder(mContext, holder.functionBarView, mViewService);
			holder.functionBarView.initListener(holder.functionBarEventHolder);
			holder.itemCommentLayout = (ListItemCommentLayout) convertView.findViewById(R.id.commentlayout);
			holder.itemStepLayout = (ListItemStepLayout) convertView.findViewById(R.id.steplayout);
			holder.tvAttention = (AttentionButtonView) convertView.findViewById(R.id.btn_attention);
			holder.tvAddress=(TextView) convertView.findViewById(R.id.tv_address);
			holder.imgPet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickGifView(v,holder);
				}
			});
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		final PetalkVo sayVo = _sayVos.get(position);
		holder.itemCommentLayout.setPetList(sayVo.getC(), sayVo.getCounter().getComment());
		holder.itemStepLayout.setPetList(sayVo.getF(),sayVo.getCounter().getFavour());
        holder.functionBarEventHolder.setSayVo(sayVo);
        holder.functionBarEventHolder.setItemStepLayout(holder.itemStepLayout);
        holder.tvAddress.setText(sayVo.getPositionName());
		if (sayVo.getDescription().trim().equals("")) {
			holder.tvContent.setVisibility(View.GONE);
		} else {
			holder.tvContent.setVisibility(View.VISIBLE);
			holder.tvContent.setText(sayVo.getDescription());
		}
		// holder.tvContent.setText(spannableString);
		// holder.tvContent.setText(sayVo.getDescription());
		holder.tvDate.setText(PublicMethod.calculateTime(sayVo.getCreateTime()));

		// if(mIsShowTime)
		// holder.tvDate.setText(PublicMethod.calculateTime(sayVo.getCreateTime()));
		// else {
		// TODO 浏览次数未改
		holder.tvDate.setText(PublicMethod.calPlayTimes(sayVo.getCounter().getPlay()) + "浏览");
		// }
		final PetVo petVo = sayVo.getPet();
		holder.tvName.setText(petVo.getNickName());
		if (petVo.getGender() == 0) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.female);

		} else if (petVo.getGender() == 1) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.male);
		} else {
			holder.imgSex.setVisibility(View.GONE);
		}
		if (petVo.getIntGrade() <= 0) {
			holder.tvAge.setText(Constants.petMap.get(petVo.getType()));
			holder.imgGrade.setVisibility(View.GONE);
			holder.tvGrade.setVisibility(View.GONE);
		} else {
			holder.tvAge.setText(Constants.petMap.get(petVo.getType()) + "｜");
			holder.tvGrade.setVisibility(View.VISIBLE);
			if (-1 == petVo.getLevenIconResId()) {
				holder.imgGrade.setVisibility(View.GONE);
			} else {
				holder.imgGrade.setVisibility(View.VISIBLE);
				holder.imgGrade.setImageResource(petVo.getLevenIconResId());
			}
			holder.tvGrade.setText("Lv" + petVo.getIntGrade());
		}

		holder.functionBarView.setValue(sayVo.getCounter().getRelay(), sayVo.getCounter().getComment(), sayVo.getCounter().getFavour(),
				sayVo.getCounter().getShare());
		holder.functionBarView.setStepStatus(sayVo.getPetalkId());

		if (mIsShowFocus) {
			if (UserManager.getSingleton().isAlreadyFocus(petVo.getId())) {
				holder.tvAttention.setVisibility(View.GONE);
			} else {
				holder.tvAttention.setVisibility(View.VISIBLE);
			}
		} else {
			holder.tvAttention.setVisibility(View.GONE);
		}
		holder.tvAttention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUserManager.isLoginStatus()) {
					holder.tvAttention.setClickable(false);
					mClickPosition=position;
					mUserNet.focus(petVo.getId(), mUserManager.getActivePetInfo().getId(),holder);
					
					// }
					// else if (sayVo.getRs()==1||sayVo.getRs()==2) {
					// //TODO 取消关注
					// mUserModule.addListener(mUserData);
					// mUserModule.cancleFocus(mUserData,petInfo.getId(),mUserManager.getActivePetInfo().getId());
					// }
				} else {
					Intent intent = new Intent(mContext,UserLogin_Activity.class);
					mContext.startActivity(intent);
				}
			}
		});

		holder.img_header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,petVo);
			}
		});

		holder.tagView.removeAllViews();
		holder.tagView.setTags(sayVo.getTags(),holder.tagView.getTagBgResId(mContext),holder.tagView.Use_SayList, mLayoutWidth);
		ImageLoaderHelp.displayHeaderImage(sayVo.getPet().getHeadPortrait(),holder.img_header);
		if (!TextUtils.isEmpty(sayVo.getPet().getStar())&&sayVo.getPet().getStar().equals("1")) {
			holder.imgStar.setVisibility(View.VISIBLE);
		} else {
			holder.imgStar.setVisibility(View.GONE);
		}
        refreshItemTypeView(sayVo.getModel(), holder.ivFlag);
		refreshPetalk(sayVo, holder, position);

		return convertView;
	}

	/**
	 * 设置布局及变形
	 * @param width
	 * @param height
	 * @param data
	 * @param view
	 * @param params
	 */
	@SuppressLint("NewApi")
	protected void updateLayout(int width,int height,PetalkDecorationVo data,View view,android.widget.FrameLayout.LayoutParams params) {
		float pw = width * data.getWidth();
		float ph = height * data.getHeight();
		float left = width * data.getCenterX() - width/2;
		float top = height * data.getCenterY() - width/2;
		float rotate = (float)(data.getRotationZ()*180/Math.PI);
		view.setScaleX(pw/width);
		view.setScaleY(ph/height);
		view.setRotation(rotate);
		params.width = width;
		params.height = height;
		params.leftMargin = (int)left;
		params.topMargin = (int)top;
		view.setLayoutParams(params);
	}

	private class Holder extends BasePetalkViewHolder {
		private ImageView imgSex;
		private CircleImageView img_header;
		private TextView tvContent;
		private TagView tagView;
		private TextView tvName,tvAge,tvGrade,tvAddress;
		private ImageView imgGrade;
		private TextView tvDate;
		private FunctionBarView functionBarView;
		private StepAnimView stepAnimView;
		private ListItemFunctionBarEventHolder functionBarEventHolder;
		private AttentionButtonView tvAttention;
		private ImageView imgStep,imgStar;
		private ListItemCommentLayout itemCommentLayout;
		private ListItemStepLayout itemStepLayout;

	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			ToastUtiles.showCenter(mContext,bean.getMessage());
			Holder holder = (Holder) bean.getTag();
			holder.tvAttention.startAnim();
			UserManager.getSingleton().addFocusBySayVo(_sayVos.get(mClickPosition));
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_FOCUS:
			Holder holder=(Holder) error.getTag();
			holder.tvAttention.setClickable(true);
			ToastUtiles.showDefault(mContext, R.string.add_attention_failed);
			break;
		default:
			break;
		}
	
	}
}
