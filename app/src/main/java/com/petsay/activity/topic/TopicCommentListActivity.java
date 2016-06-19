package com.petsay.activity.topic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.SharePopupWindow;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.topic.adapter.TopicCommentListAdapter;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.TitleBar.OnClickBackListener;
import com.petsay.component.view.forum.ImageTextView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.CommentDTO;
import com.petsay.vo.forum.TalkDTO;
import com.petsay.vo.forum.TopicDTO;
import com.petsay.vo.petalk.PetVo;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * 讨论详情，评论列表
 * 
 * @author G
 */
public class TopicCommentListActivity extends BaseActivity implements IAddShowLocationViewService,
		NetCallbackInterface,OnClickListener {
	private ListView lv;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	private Button mBtnSend;
	private EditText mEdComment;
	private ImageTextView mImageTextView;

	private TopicCommentListAdapter mTopicCommentListAdapter;
	// private ShopNet mShopNet;
	private List<CommentDTO> mCommentDTOs = new ArrayList<CommentDTO>();
	
	private TopicDTO mTopicDTO;
	private TalkDTO mTalkDTO;
	
	private int pageIndex = 0;
	private int pageSize = 10;
	
	private TopicNet mTopicNet;
	
//	private int firstAllListPosition=-1;
//	private String mAtPetId;
	private PetVo mAtPet;
	
	private int commentType=0;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_commentlist);
		mTopicDTO=(TopicDTO) getIntent().getSerializableExtra("topicDTO");
		mTalkDTO=(TalkDTO) getIntent().getSerializableExtra("talkDto");
		commentType=mTalkDTO.getTalkType();
		initView();
		showLoading();
		mTopicNet = new TopicNet();
		mTopicNet.setCallback(this);
		mTopicNet.setTag(this);
		
		onRefresh();

	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		lv = (ListView) findViewById(R.id.lv);
		// mTvExchangeHistory=(TextView) findViewById(R.id.tv_exchangeHistory);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		mBtnSend=(Button) findViewById(R.id.btn_send);
		mEdComment=(EditText) findViewById(R.id.ed_content);
		 mImageTextView=new ImageTextView(TopicCommentListActivity.this);
		setListener();
		initTitleBar();
		initPullToRefreshView();
		mTopicCommentListAdapter = new TopicCommentListAdapter(TopicCommentListActivity.this);
	}
	
	private void setListener(){
		mBtnSend.setOnClickListener(this);
		setBtnSendEnable(false);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				int count=lv.getHeaderViewsCount();
				int clickItemPosition=position-count;
				if (clickItemPosition>=0) {
				   CommentDTO commentDTO=(CommentDTO) mTopicCommentListAdapter.getItem(clickItemPosition);
//				   mAtPetId=commentDTO.getPetId();
				   mAtPet=new PetVo();
				   mAtPet.setId(commentDTO.getPetId());
				   mAtPet.setNickName(commentDTO.getPetNickName());
				   mEdComment.setHint("@"+mAtPet.getNickName());
				}
			}
		});
		mEdComment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String str=s.toString();
				if (str.trim().equals("")) {
					setBtnSendEnable(false);
				}else {
					setBtnSendEnable(true);
				}
				
			}
		});

        mLayoutRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PublicMethod.closeSoftKeyBoard(TopicCommentListActivity.this,mEdComment);
                return false;
            }
        });

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PublicMethod.closeSoftKeyBoard(TopicCommentListActivity.this,mEdComment);
                return false;
            }
        });
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==event.KEYCODE_BACK) {
			setResultData();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setResultData(){
		mTalkDTO.setTalkType(commentType);
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), TopicDetailActivity.class);
		List<CommentDTO> list=mTopicCommentListAdapter.getmDtos();
		List<CommentDTO> temp=new ArrayList<CommentDTO>();
//		list.subList(start, end)
		for (int i = 0; i < list.size(); i++) {
			if (i<2) {
				temp.add(list.get(i));
			}else 
				break;
			
		}
		mTalkDTO.setComments(temp);
		intent.putExtra("talkDTO", mTalkDTO);
		setResult(100, intent);
		finish();
	}

	private void initTitleBar() {
		mTitleBar.setTitleText(mTopicDTO.getContent());
//		mTitleBar.setFinishEnable(true);
		mTitleBar.setOnClickBackListener(new OnClickBackListener() {
			
			@Override
			public void OnClickBackListener() {
				setResultData();
				
			}
		});
		ImageView imgView = new ImageView(TopicCommentListActivity.this);
		imgView.setImageResource(R.drawable.more);
		LinearLayout.LayoutParams txt_Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		txt_Params.gravity = Gravity.CENTER;
		txt_Params.leftMargin = 10;
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(imgView, txt_Params);
		layout.setTag(0);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharePopupWindow popupWindow;
				popupWindow=new SharePopupWindow(getActivity(),TopicCommentListActivity.this,null,mTopicDTO,mTalkDTO,false);
				
				popupWindow.isShowForward(false);
				popupWindow.show();
			}
		});

		mTitleBar.addRightView(layout);
	}

	private void initPullToRefreshView() {
		// mPullView.setPullDownRefreshEnable(false);
		// mPullView.setPullUpRefreshEnable(false);
		mPullView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				onRefresh();
			}
		});

		mPullView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onAddMore();
			}
		});
	}

	private void onAddMore() {
		pageIndex++;
		if (mTopicCommentListAdapter.getCount() > 0) {
			CommentDTO vo = (CommentDTO) mTopicCommentListAdapter.getItem(mTopicCommentListAdapter.getCount() - 1);
			if (UserManager.getSingleton().isLoginStatus()) {
				mTopicNet.topicCommentList(mTalkDTO.getId(),vo.getId(),pageSize,true);
			}else {
				mTopicNet.topicCommentList(mTalkDTO.getId(),vo.getId(),pageSize,true);
			}
		} else {
//			 mPullView.setPullDownRefreshEnable(false);
			mPullView.onFooterRefreshComplete();
			showToast("没有数据");
		}
	}

	private void onRefresh() {
		
//		mTopicNet.topicTalkOne(mTalkDTO.getId(), UserManager.getSingleton().getActivePetId());
		if (UserManager.getSingleton().isLoginStatus()) {
			mTopicNet.topicTalkOne(mTalkDTO.getId(), UserManager.getSingleton().getActivePetId());
		}else {
			mTopicNet.topicTalkOne(mTalkDTO.getId(), "");
			
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mTopicCommentListAdapter && mTopicCommentListAdapter.getCount() > 0) {
			mTopicCommentListAdapter.notifyDataSetChanged();
		}
		// mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
	}

	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {

		switch (requestCode) {
		
		case RequestCode.REQUEST_TOPICTALKONE:
			mTalkDTO=JsonUtils.resultData(bean.getValue(), TalkDTO.class);
			mImageTextView.setContent(mTalkDTO.getContent(), mTalkDTO);
			lv.removeHeaderView(mImageTextView);
			lv.addHeaderView(mImageTextView);
			lv.setAdapter(mTopicCommentListAdapter);
			
			if (UserManager.getSingleton().isLoginStatus()) {
				mTopicNet.topicCommentList(mTalkDTO.getId(),"",pageSize,false);
			}else {
				mTopicNet.topicCommentList(mTalkDTO.getId(),"",pageSize,false);
				
			}
			break;
		case RequestCode.REQUEST_TOPICCOMMENTLIST:
			
			closeLoading();
			try {
				mCommentDTOs = JsonUtils.getList(bean.getValue(),CommentDTO.class);
			} catch (Exception e) {
				PublicMethod.showToast(getApplicationContext(), "解析话题评论列表出错");
				e.printStackTrace();
			}
			if (bean.isIsMore()) {
				 mPullView.onFooterRefreshComplete();
				mTopicCommentListAdapter.addMore(mCommentDTOs);
			} else {
				mPullView.onHeaderRefreshComplete();
				mTopicCommentListAdapter.refreshData(mCommentDTOs);
			}
			break;
		case RequestCode.REQUEST_TOPICCREATECOMMENT:
			mEdComment.setHint("");
			mEdComment.setText("");
			mAtPet=null;
			mBtnSend.setClickable(true);
			PublicMethod.closeSoftKeyBoard(getApplicationContext(), mEdComment);
			onRefresh();
			
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_TOPICTALKONE:
			
			break;
		case RequestCode.REQUEST_TOPICCOMMENTLIST:
			
			
			break;
		case RequestCode.REQUEST_TOPICCREATECOMMENT:
			setBtnSendEnable(true);			
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			setBtnSendEnable(false);
			String commnet=mEdComment.getText().toString();
			send(commnet);
			break;
		default:
			break;
		}
	}
	
	private void send(String comment){
		if (UserManager.getSingleton().isLoginStatus()) {
			String atPetId="";
			if (null!=mAtPet) {
				atPetId=mAtPet.getId();
			}
			mTopicNet.topicCreateComment(mTalkDTO.getId(), UserManager.getSingleton().getActivePetId(), comment, atPetId);
		}else {
			setBtnSendEnable(true);
			Intent intent=new Intent(TopicCommentListActivity.this,UserLogin_Activity.class);
			startActivity(intent);
		}
	}
	
	

	@Override
	public View getParentView() {
		return layoutRoot;
	}

	@Override
	public Activity getActivity() {
		return this;
	}
	
	private void setBtnSendEnable(boolean enable){
		mBtnSend.setClickable(enable);
		if (enable) {
			mBtnSend.setBackgroundColor(Color.TRANSPARENT);
			
		}else {
			mBtnSend.setBackgroundColor(Color.GRAY);
		}
	}
}