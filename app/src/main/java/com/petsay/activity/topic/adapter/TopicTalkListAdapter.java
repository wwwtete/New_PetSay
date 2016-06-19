package com.petsay.activity.topic.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.component.view.ExCircleView;
import com.petsay.component.view.petalklistitem.ListItemCommentLayout;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.TalkDTO;

public class TopicTalkListAdapter extends BaseAdapter implements NetCallbackInterface {

	private Context mContext;
    private List<TalkDTO> mDtos;
    public int mCurrentSelectIndex=0;
    public int mWonderfulPosition=0;
    private TopicNet mTopicNet;
    
    
	public TopicTalkListAdapter(Context context) {
		mContext = context;
		this.mDtos=new ArrayList<TalkDTO>();
		mTopicNet=new TopicNet();
		mTopicNet.setTag(context);
		mTopicNet.setCallback(this);
	}
	
	public void refreshData(List<TalkDTO> dtos){
		if (dtos != null && dtos.size() > 0) {
			this.mDtos.clear();
			this.mDtos.addAll(dtos);
			notifyDataSetChanged();
		}
	}
	
	public void addMore(List<TalkDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			this.mDtos.addAll(dtos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		mDtos.clear();
		notifyDataSetChanged();
	}

	public void setDto(TalkDTO dto ,int position){
		if (position>=0&&position<mDtos.size()) {
			mDtos.set(position, dto);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		if (null==mDtos) {
			return 0;
		}
		return  mDtos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==mDtos||mDtos.isEmpty()) {
			return null;
		}
		return  mDtos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.forum_talk_list_item, null);
			holder = new Holder();
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tvContent=(TextView) convertView.findViewById(R.id.tv_content);
			holder.imgSex=(ImageView) convertView.findViewById(R.id.img_sex);
			holder.exCircleView=(ExCircleView) convertView.findViewById(R.id.headview);
			holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
			holder.tvTalkType=(TextView) convertView.findViewById(R.id.tv_talktype);
			holder.layoutFavour=(LinearLayout) convertView.findViewById(R.id.layout_favour);
			holder.imgFavour=(ImageView) convertView.findViewById(R.id.img_favour);
			holder.tvFavourCount=(TextView) convertView.findViewById(R.id.tv_favourcount);
			holder.layoutImg=(RelativeLayout) convertView.findViewById(R.id.layout_img);
			holder.img1=(ImageView) convertView.findViewById(R.id.img1);
			holder.tvImgCount=(TextView) convertView.findViewById(R.id.tv_imgcount);
			holder.layoutComment=(ListItemCommentLayout) convertView.findViewById(R.id.comment_layout);
//			holder.exCircleView=(ExCircleView) convertView.findViewById(R.id.headview);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
	
		final TalkDTO talkDTO=mDtos.get(position); 
		holder.exCircleView.setBackgroudImage(talkDTO.getPetAvatar());
		holder.tvName.setText(talkDTO.getPetNickName());
//		ImageLoaderHelp.displayHeaderImage(talkDTO.getPic(), holder.img);
		holder.tvContent.setText(talkDTO.getContent());
		if (talkDTO.getTalkType()==0) {
			holder.tvTalkType.setVisibility(View.GONE);
		}else if (talkDTO.getTalkType()==2) {
			holder.tvTalkType.setVisibility(View.VISIBLE);
			holder.tvTalkType.setText("全部互动");
		}else if (talkDTO.getTalkType()==1){
			holder.tvTalkType.setVisibility(View.VISIBLE);
			holder.tvTalkType.setText("出彩互动");
		}
		if(!TextUtils.isEmpty(talkDTO.getPetStar()) && "1".equals(talkDTO.getPetStar())){
			holder.exCircleView.setBottomRightImage(R.drawable.star);
        }
		
		if (talkDTO.getPetGender().equals("0")) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.female);

		}else if (talkDTO.getPetGender().equals("1")) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.male);
		}else {
			holder.imgSex.setVisibility(View.GONE);
		}
		holder.tvDate.setText(PublicMethod.calculateTopicTime(talkDTO.getCreateTime(),false));
		holder.tvFavourCount.setText(talkDTO.getLikeCount()+"");
		if (talkDTO.getIsLiked()) {
			holder.imgFavour.setImageResource(R.drawable.step_already);
		}else {
			holder.imgFavour.setImageResource(R.drawable.step_ico);
		}
		holder.position=position;
		int picsSize= talkDTO.getPictures().length;
		if (picsSize>0) {
			holder.layoutImg.setVisibility(View.VISIBLE);
			holder.tvImgCount.setText("1/"+talkDTO.getPictures().length);
			ImageLoaderHelp.displayContentImage(talkDTO.getPictures()[0].getPic()+"?imageView2/1/w/100", holder.img1);
		}else {
			holder.layoutImg.setVisibility(View.GONE);
		}
		holder.layoutFavour.setOnClickListener(new OnFavourClickListener(position,holder));
		if (talkDTO.getTop()) {
			holder.layoutComment.setVisibility(View.GONE);
		}else {
			holder.layoutComment.setVisibility(View.VISIBLE);
			holder.layoutComment.setTopicCommentList(talkDTO.getComments(), talkDTO.getCommentCount());
		}
		
		holder.tvName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext, talkDTO.getPetId());
				
			}
		});
		holder.exCircleView.setClickHeaderToTurn(talkDTO.getPetId());
		
//		holder.tvBrowse.setText(PublicMethod.calPlayTimes(talkDTO.getViewCount())+"浏览");
		return convertView;
	}

	private class Holder {
		private ImageView imgSex;
		private TextView tvContent,tvName,tvDate;
		private ExCircleView exCircleView;
		private TextView tvTalkType;
		private LinearLayout layoutFavour;
		private TextView tvFavourCount;
		private ImageView imgFavour;
		private RelativeLayout layoutImg;
		private ImageView img1;
		private TextView tvImgCount;
	    private ListItemCommentLayout layoutComment;
	    private int position;
	    
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_TOPICCREATELIKE:
			Holder holder=(Holder) bean.getTag();
			mDtos.get(holder.position).setIsLiked(true);
			int favourCount=Integer.parseInt(holder.tvFavourCount.getText().toString());
			 mDtos.get(holder.position).setLikeCount(favourCount+1);
			holder.tvFavourCount.setText((favourCount+1)+"");
			holder.imgFavour.setImageResource(R.drawable.step_already);
			break;
		case RequestCode.REQUEST_TOPICCANCELLIKE:
			 holder=(Holder) bean.getTag();
			 mDtos.get(holder.position).setIsLiked(false);
			 favourCount=Integer.parseInt(holder.tvFavourCount.getText().toString());
			 mDtos.get(holder.position).setLikeCount(favourCount-1);
			holder.tvFavourCount.setText((favourCount-1)+"");
			holder.imgFavour.setImageResource(R.drawable.step_ico);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		PublicMethod.showToast(mContext, "操作失败");
	}
	
	private class OnFavourClickListener implements OnClickListener{

		private int clickPosition;
		private Holder _Holder;
		public OnFavourClickListener(int position,Holder holder){
			clickPosition=position;
			_Holder=holder;
		}
		@Override
		public void onClick(View v) {
			if (UserManager.getSingleton().isLoginStatus()) {
				if (mDtos.get(clickPosition).getIsLiked()) {
					mTopicNet.topicCancelLike(mDtos.get(clickPosition).getId(), UserManager.getSingleton().getActivePetId(),_Holder);
				}else {
					mTopicNet.topicCreateLike(mDtos.get(clickPosition).getId(), UserManager.getSingleton().getActivePetId(),_Holder);
				}
			}else {
				Intent intent=new Intent(mContext,UserLogin_Activity.class);
				mContext.startActivity(intent);
				
			}
		}
		
	}
	
	
}