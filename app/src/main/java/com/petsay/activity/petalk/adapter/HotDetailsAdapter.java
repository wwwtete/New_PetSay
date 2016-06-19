package com.petsay.activity.petalk.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.component.gifview.AudioGifView;
import com.petsay.component.media.CommentRecordPlayerView;
import com.petsay.component.media.MediaPlayManager;
import com.petsay.component.text.Clickable;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.component.view.HotDetailsListTtile;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.HotDetailsListTtile.TitleChangeListener;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.download.DownloadQueue;
import com.petsay.network.download.DownloadTask;
import com.petsay.network.download.DownloadTask.DownloadTaskCallback;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.CommentVo;
import com.petsay.vo.petalk.PetVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw 转发和评论 | 踩
 */
public class HotDetailsAdapter extends BaseAdapter implements NetCallbackInterface,OnClickListener{

	/** 转发和评论 */
	public static final int TYPE_REVIEW = 0;
	/** 踩 */
	public static final int TYPE_TREAD = 1;
	//	private static final Class<?> OtherUserListAdapter = null;

	private DetailActivity mContext;
	private LayoutInflater mInflater;
	private int mType;
	private TitleChangeListener mListener;
	private List<CommentVo> mCommentVos;
	// private List<PetInfo> mPetInfos;
	//	private DownloadService mDownloadService;
	private AudioGifView mAudiogifview;
	private UserManager mUserManager;
	/**
	 * 发布该条说说的petdi
	 */
	private String mPetTalkFromPetId;
	public SayDataNet mSayDataNet;
    private DialogPopupWindow mPopupWindow;
    private int selectPosition;

	public HotDetailsAdapter(DetailActivity context, int type,TitleChangeListener listener) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		//		mDownloadService = DownloadService.getSingleton();
		mCommentVos = new ArrayList<CommentVo>();
		mUserManager=UserManager.getSingleton();
		mType = type;
		mListener = listener;
		
	}

	public void refreshData(List<CommentVo> data){
		if(data != null ){
			mCommentVos.clear();
			mCommentVos.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addMore(List<CommentVo> data){
		if(data != null && data.size() > 0){
			mCommentVos.addAll(data);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}


	public HotDetailsAdapter(DetailActivity context, int type,
			TitleChangeListener listener,AudioGifView audiogifview) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		mUserManager=UserManager.getSingleton();
		//		mDownloadService = DownloadService.getSingleton();
		mCommentVos = new ArrayList<CommentVo>();
		mAudiogifview=audiogifview;
		mType = type;
		mListener = listener;

	}
	
    public void setData(String petTalkFromPetId,IAddShowLocationViewService viewService){
    	mPetTalkFromPetId=petTalkFromPetId;
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(mContext);
		mPopupWindow=new DialogPopupWindow(mContext, viewService);
		mPopupWindow.setOnClickListener(this);
    }

	// public HotDetailsAdapter(Context context,int type,List<PetInfo>
	// petInfos,TitleChangeListener listener){
	// this.mContext = context;
	// mPetInfos=mPetInfos;
	// this.mInflater = LayoutInflater.from(context);
	// mType = type;
	// mListener = listener;
	// }

	@Override
	public int getCount() {
		int count = 0;
		// switch(mType){
		// case TYPE_REVIEW:
		if (null == mCommentVos || mCommentVos.isEmpty()) {
			count = 0;
		} else
			count = mCommentVos.size();
		// break;
		// case TYPE_TREAD:
		// if (null == mPetInfos || mPetInfos.isEmpty()) {
		// fileCount = 0;
		// } else
		// fileCount = mPetInfos.size();
		// break;
		// }
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (null != mCommentVos && !mCommentVos.isEmpty()&&position<mCommentVos.size()) {
			return mCommentVos.get(position);
		} else {
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (mType) {
		case TYPE_REVIEW:
			convertView = getReView(position, convertView);
			break;
		case TYPE_TREAD:
			convertView = getTreadView(position, convertView);
			break;
		}
		convertView.setBackgroundColor(Color.WHITE);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		//		holder.listTtile.setReviewCount(mSayVo.getForwardNum(),
		//				mSayVo.getCommentNum());
		//		holder.listTtile.setUnderLinePosition(mType);
		//		if (position == 0) {
		//			holder.listTtile.setVisibility(View.VISIBLE);
		//			holder.listTtile.setOnTitleChangeListener(mListener);
		//		} else {
		//			holder.listTtile.setVisibility(View.GONE);
		//		}

		return convertView;
	}

	private View getTreadView(int position, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tread_item, null);
			holder = new ViewHolder();
			holder.imgHeader = (CircleImageView) convertView.findViewById(R.id.img_header);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.listTtile = (HotDetailsListTtile) convertView.findViewById(R.id.hotdetail_listtitle);
			holder.tvAttention=(TextView) convertView.findViewById(R.id.btn_attention);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentVo commentVo = mCommentVos.get(position);
		holder.tvName.setText(commentVo.getPetNickName());
		//		ImageLoader.getInstance().displayImage(commentVo.getPetHeadPortrait(), holder.imgHeader,Constants.headerImgOptions);
		//		PicassoUtile.loadHeadImg(mContext, commentVo.getPetHeadPortrait(), holder.imgHeader);
		ImageLoaderHelp.displayHeaderImage(commentVo.getPetHeadPortrait(), holder.imgHeader);
		return convertView;
	}

	private View getReView(final int position, View convertView) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.review_item, null);
			holder = new ViewHolder();
			holder.imgHeader = (CircleImageView) convertView.findViewById(R.id.img_header);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.listTtile = (HotDetailsListTtile) convertView.findViewById(R.id.hotdetail_listtitle);
			holder.commentRecordPlayerView = (CommentRecordPlayerView) convertView.findViewById(R.id.play);
			holder.tvCommentType=(TextView) convertView.findViewById(R.id.tv_type);
			holder.imgDel=(ImageView) convertView.findViewById(R.id.img_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CommentVo commentVo = mCommentVos.get(position);
		holder.tvName.setText(commentVo.getPetNickName());
		holder.tvTime.setText(PublicMethod.calculateTime(commentVo.getCreateTime()));
		if (commentVo.getType().equals(Constants.RELAY)) {
			holder.tvCommentType.setText("转发");
		}else {
			holder.tvCommentType.setText("评论");
		}

		if (commentVo.getAimPetNickName().equals("")) {
			holder.tvContent.setText(commentVo.getComment());
		}else {
			String content="回复@"+commentVo.getAimPetNickName()+":"+commentVo.getComment();
			SpannableString spannableString=new SpannableString(content);
			spannableString.setSpan(new Clickable(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PetVo info=new PetVo();
					info.setId(commentVo.getAimPetId());
					info.setNickName(commentVo.getAimPetNickName());
					info.setHeadPortrait(commentVo.getAimPetHeadPortrait());
					ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,info );
				}
			},false),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.list_name)),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 

			holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
			holder.tvContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mContext.setSelectCommentVo(commentVo, position);
				}
			});
			holder.tvContent.setText(spannableString);
		}
		String audioUrl = commentVo.getCommentAudioUrl().trim();

		if (audioUrl.equals("")) {
			holder.commentRecordPlayerView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(View.VISIBLE);
		} else {
			int index = audioUrl.lastIndexOf("/");
			if (index == -1) {
				holder.commentRecordPlayerView.setVisibility(View.GONE);
				holder.tvContent.setVisibility(View.VISIBLE);
			} else {
				holder.commentRecordPlayerView.setVisibility(View.VISIBLE);
				holder.tvContent.setVisibility(View.GONE);
				holder.commentRecordPlayerView.setAudioSecond(commentVo.getCommentAudioSecond());
				String audioPath =FileUtile.getPath(mContext, Constants.AUDIO_DOWNLOAD_PATHE)+ audioUrl.substring(index+1);
				holder.commentRecordPlayerView.setAudioPath(audioPath);
				holder.commentRecordPlayerView.setAudioUrl(audioUrl);
				holder.commentRecordPlayerView.reset();

				holder.commentRecordPlayerView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						File file = new File(holder.commentRecordPlayerView.getAudioPath());
						if(file.exists()){
							playAudio(holder.commentRecordPlayerView);
						} else {
							holder.commentRecordPlayerView.setDownloadIng(true);
							downloadAudio(holder.commentRecordPlayerView);
							holder.commentRecordPlayerView.setClickable(false);
						}
					}
				});
			}

		}
		holder.imgHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PetVo info=new PetVo();
				info.setId(commentVo.getPetId());
				info.setNickName(commentVo.getPetNickName());
				info.setHeadPortrait(commentVo.getPetHeadPortrait());
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,info );

			}
		});
		ImageLoaderHelp.displayHeaderImage(commentVo.getPetHeadPortrait(), holder.imgHeader);
		
		if (mUserManager.isLoginStatus()) {
			
			if (mPetTalkFromPetId.equals(mUserManager.getActivePetId())||commentVo.getPetId().equals(mUserManager.getActivePetId())) {
				holder.imgDel.setVisibility(View.VISIBLE);
			}else {
				holder.imgDel.setVisibility(View.GONE);
			}
		}else {
			holder.imgDel.setVisibility(View.GONE);
		}
		holder.imgDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			if (UserManager.getSingleton().isLoginStatus()) {
				// TODO 删除该条评论
				// del();
				int tag;
				if (commentVo.getType()==Constants.COMMENT) {
					tag=1;
				}else {
					tag=2;
				}
				selectPosition = position;
				mPopupWindow.setPopupText(tag, "提示", "确定要删除此条评论吗?", "确定", "取消");
				mPopupWindow.show();
			}else {
				Intent intent=new Intent(mContext,UserLogin_Activity.class);
				mContext.startActivity(intent);
			}
				
			}
		});
		
		return convertView;
	}

	private class ViewHolder {
		private CircleImageView imgHeader;
		private TextView tvName;
		private TextView tvTime;
		private TextView tvContent;
		private TextView tvAttention;
		private HotDetailsListTtile listTtile;
		private LinearLayout layoutReview;
		private CommentRecordPlayerView commentRecordPlayerView;
		private TextView tvCommentType;
		private ImageView imgDel;
	}

	private void playAudio(CommentRecordPlayerView view) {
		mAudiogifview.stopGif();
//		MediaPlayManager.getSingleton().stopAudio();
		MediaPlayManager.getSingleton().play(mContext, view.getAudioPath(),view);
	}

	private void downloadAudio(CommentRecordPlayerView commentRecordPlayerView){
		if(PublicMethod.getAPNType(mContext) == -1){
			PublicMethod.showToast(mContext, R.string.network_disabled);
			return;
		}
		final DownloadTask task = new DownloadTask(commentRecordPlayerView, FileUtile.getPath(mContext, Constants.AUDIO_DOWNLOAD_PATHE));
		task.setCallback(new DownloadTaskCallback() {
			@Override
			public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess, String url,
					File file, Object what) {
				if(isSuccess){
					CommentRecordPlayerView commentRecordPlayerView = (CommentRecordPlayerView) what;
					commentRecordPlayerView.setDownloadIng(false);
					commentRecordPlayerView.setClickable(true);
					DownloadQueue.getInstance().remove(task);
					playAudio(commentRecordPlayerView);
				}else {
					PublicMethod.showToast(mContext, R.string.network_download_audio_error);
				}
			}

			@Override
			public void onCancelCallback(DownloadTask task,String url, Object what) {
				CommentRecordPlayerView commentRecordPlayerView = (CommentRecordPlayerView) what;
				commentRecordPlayerView.setDownloadIng(false);
				commentRecordPlayerView.setClickable(true);
				DownloadQueue.getInstance().remove(task);
			}
		});
		task.setTag(mContext);
		task.execute(commentRecordPlayerView.getAudioUrl());
		DownloadQueue.getInstance().add(task);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_INTERACTIONDELETE:
			mCommentVos.remove(selectPosition);
			notifyDataSetChanged();
			mContext.refreshCommentCount(mPopupWindow.mTag);
			break;

		default:
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_dialog_ok:
			mSayDataNet.interactionDelete(mCommentVos.get(selectPosition).getId());
			mPopupWindow.dismiss();
			break;
		case R.id.tv_dialog_cancle:
			mPopupWindow.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	private class DownloadAudioListener implements DownloadListener {
		private CommentRecordPlayerView _commentRecordPlayerView;

		public DownloadAudioListener(
				CommentRecordPlayerView commentRecordPlayerView) {
			_commentRecordPlayerView = commentRecordPlayerView;
		}

		@Override
		public void onDownloadFinishCallback(boolean isSuccess, String url,File file, View view) {
			if (_commentRecordPlayerView == view) {
				Message message = new Message();
				message.what = 1;
				message.obj = _commentRecordPlayerView;
				handler.sendMessage(message);
			}
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				CommentRecordPlayerView _commentRecordPlayerView = (CommentRecordPlayerView) msg.obj;
				_commentRecordPlayerView.setIsDownload(true);
				break;
			}
		};
	};
	 */
}