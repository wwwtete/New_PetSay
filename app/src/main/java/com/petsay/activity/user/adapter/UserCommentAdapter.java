package com.petsay.activity.user.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.petsay.activity.user.MyCommentListActivity;
import com.petsay.component.media.CommentRecordPlayerView;
import com.petsay.component.media.MediaPlayManager;
import com.petsay.component.text.Clickable;
import com.petsay.component.view.DialogPopupWindow;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.download.DownloadQueue;
import com.petsay.network.download.DownloadTask;
import com.petsay.network.download.DownloadTask.DownloadTaskCallback;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

public class UserCommentAdapter extends BaseAdapter implements NetCallbackInterface,OnClickListener{

	private MyCommentListActivity mContext;
    private List<PetalkVo> _sayVos;
    public int currentSelectIndex=0;
    public SayDataNet mSayDataNet;
    private DialogPopupWindow mPopupWindow;
    private int selectPosition;
//    private UserCenterFragment mFragment;
	public UserCommentAdapter(MyCommentListActivity context,IAddShowLocationViewService viewService) {
		mContext = context;
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(context);
		mPopupWindow=new DialogPopupWindow(context, viewService);
		mPopupWindow.setOnClickListener(this);
//		mFragment=fragment;
		this._sayVos=new ArrayList<PetalkVo>();
	}
	
	public void refreshData(List<PetalkVo> sayVos){
		
		if(sayVos != null){
			_sayVos.clear();
			_sayVos.addAll(sayVos);
			notifyDataSetChanged();
			
		}
		
	}
	
	public void addMore(List<PetalkVo> sayVos){
		if(sayVos != null && sayVos.size() > 0){
			_sayVos.addAll(sayVos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		_sayVos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==_sayVos) {
			return 0;
		}
		return  _sayVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==_sayVos||_sayVos.isEmpty()) {
			return null;
		}
		return  _sayVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.usercentenr_comment_list_item, null);
			holder = new Holder();
			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.imgDel=(ImageView) convertView.findViewById(R.id.img_del);
			holder.layoutCommentRoot=(LinearLayout) convertView.findViewById(R.id.layout_comment_root);
			holder.playView=(CommentRecordPlayerView) convertView.findViewById(R.id.play);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final PetalkVo sayVo=_sayVos.get(position);
		holder.layoutCommentRoot.setBackgroundColor(Color.WHITE);
//		PicassoUtile.loadImg(mContext, sayVo.getThumbUrl(), holder.imgPet);
		ImageLoaderHelp.displayContentImage(sayVo.getThumbUrl(), holder.imgPet);
		
		
		String content="评论@"+sayVo.getAimPetNickName()+":"+sayVo.getComment();
		SpannableString spannableString=new SpannableString(content);
		spannableString.setSpan(new Clickable(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PetVo info=new PetVo();
				info.setId(sayVo.getAimPetId());
				info.setNickName(sayVo.getAimPetNickName());
				info.setHeadPortrait(sayVo.getAimPetHeadPortrait());
				ActivityTurnToManager.getSingleton().userHeaderGoto(mContext,info );
			}
		},false),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.list_name)),2,content.indexOf(":"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		
		holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
		holder.tvContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, DetailActivity.class);
				Constants.Detail_Sayvo=(PetalkVo) getItem(position);
				mContext.startActivityForResult(intent, 1);
			}
		});
		
		holder.imgDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO 删除该条评论
				//del();
				selectPosition=position;
				mPopupWindow.setPopupText(2,"提示","确定要删除此条评论吗?","确定","取消");
				mPopupWindow.show();
			}
		});
		
		if (sayVo.getCommentAudioUrl().equals("")) {
			holder.playView.setVisibility(View.GONE);
//			holder.tvContent.setVisibility(View.VISIBLE);
		} else {

			int index = sayVo.getCommentAudioUrl().lastIndexOf("/");
			if (index == -1) {
				holder.playView.setVisibility(View.GONE);
//				holder.tvContent.setVisibility(View.VISIBLE);
			} else {
				holder.playView.setVisibility(View.VISIBLE);
//				holder.tvContent.setVisibility(View.GONE);
				holder.playView.setAudioSecond(sayVo.getCommentAudioSecond());
				String audioPath =FileUtile.getPath(mContext, Constants.AUDIO_DOWNLOAD_PATHE)+  sayVo.getCommentAudioUrl().substring(index+1);
				holder.playView.setAudioPath(audioPath);
				holder.playView.setAudioUrl(sayVo.getCommentAudioUrl());
				holder.playView.reset();
				//				File file = new File(audioPath);
				//				if (file.exists()) {
				//					holder.commentRecordPlayerView.setIsDownload(true);
				//				} else {
				//					holder.commentRecordPlayerView.setIsDownload(false);
				//					mDownloadService.addListener(new DownloadAudioListener(holder.commentRecordPlayerView));
				//					mDownloadService.download(audioUrl, FileUtile.getPath(mContext, Constants.AUDIO_DOWNLOAD_PATHE),holder.commentRecordPlayerView);
				//				}

				holder.playView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						File file = new File(holder.playView.getAudioPath());
						if(file.exists()){
							playAudio(holder.playView);
						} else {
							holder.playView.setDownloadIng(true);
							downloadAudio(holder.playView);
							holder.playView.setClickable(false);
						}
					}
				});
			}

		}
		
		
		holder.tvContent.setText(spannableString);
		holder.tvDate.setText(PublicMethod.calculateTime(sayVo.getRelayTime()));
		return convertView;
	}

	private class Holder {
		private ImageView imgPet;
		private TextView tvContent;
		private TextView tvDate;
		private LinearLayout layoutCommentRoot;
		private ImageView imgDel;
		private CommentRecordPlayerView playView;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_INTERACTIONDELETE:
			_sayVos.remove(selectPosition);
			notifyDataSetChanged();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_dialog_ok:
			mSayDataNet.interactionDelete(_sayVos.get(selectPosition).getId());
			mPopupWindow.dismiss();
			break;
		case R.id.tv_dialog_cancle:
			mPopupWindow.dismiss();
			break;
		default:
			break;
		}
	}
	
	private void playAudio(CommentRecordPlayerView view) {
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
}