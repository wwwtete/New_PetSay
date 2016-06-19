package com.petsay.activity.message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.user.OtherUserActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.message.adapter.PrivateMsgListAdapter;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.application.NetworkManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonParse;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.PMessage;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

public class MessageActivity extends BaseActivity implements NetCallbackInterface {
	private String type = "";
	private int msgFrom;
	private ListView lvPrivateMsg;
	private TitleBar mTitleBar;
	private PrivateMsgListAdapter mAdapter;
	private PullToRefreshView mPullToRefreshView;
	private UserNet mUserNet;
	private String title;
    private int pageIndex=0;
	
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getIntent().getStringExtra("type");
		msgFrom = getIntent().getIntExtra("from", Constants.MSG_USER);
		setContentView(R.layout.private_message);
		mUserNet = new UserNet();
		mUserNet.setCallback(this);
        mUserNet.setTag(this);
		initView();
		refreshMessage();

	}

	@Override
	protected void initView() {
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mTitleBar.setTitleText(getIntent().getStringExtra("folderPath"));
		mTitleBar.setFinishEnable(true);
		lvPrivateMsg = (ListView) findViewById(R.id.lv_private_msg);
		mAdapter = new PrivateMsgListAdapter(MessageActivity.this,msgFrom);
		lvPrivateMsg.setAdapter(mAdapter);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefreshview);
		mPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						refreshMessage();
					}
				});

		mPullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {
					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						onAddMore();
					}
				});

		lvPrivateMsg.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PMessage pMessage = (PMessage) mAdapter.getItem(position);
				if (msgFrom==Constants.MSG_USER) {
					if (null != pMessage) {
						if (pMessage.getType().equals(Constants.FANS)) {
							Intent intent = new Intent(MessageActivity.this,OtherUserActivity.class);
							PetVo info = new PetVo();
							info.setId(pMessage.getPetId());
							info.setNickName(pMessage.getPetNickName());
							info.setHeadPortrait(pMessage.getPetHeadPortrait());
							intent.putExtra("petInfo", info);
							startActivity(intent);
							// }else
							// if(pMessage.getType().equals(Constants.FAVOUR)){
							//
							// }else if(pMessage.getType().equals(Constants.RELAY)){
							//
							// }else
							// if(pMessage.getType().equals(Constants.COMMENT)){

						} else {
							Intent intent = new Intent(MessageActivity.this,DetailActivity.class);
							intent.putExtra("from", DetailActivity.FROM_MESSAGE);
							PetalkVo sayVo = new PetalkVo();
							sayVo.setPetalkId(pMessage.getPetalkId());
							Constants.Detail_Sayvo = sayVo;
							startActivity(intent);
						}
					}
				}else if (pMessage.getType().equals("1")) {
					PublicMethod.showAnnouncementDialog(MessageActivity.this, pMessage.getContent());
				}else if (pMessage.getType().equals("2")) {
					Intent intent=new Intent();
					intent.setClass(MessageActivity.this, WebViewActivity.class);
					intent.putExtra("content", pMessage.getContent());
					startActivity(intent);
				}
				
				
			}
		});
	}

	private void onAddMore() {
		if (mAdapter.getCount() > 0) {
			if (msgFrom == Constants.MSG_USER) {
				PMessage msg = (PMessage) mAdapter.getItem(mAdapter.getCount() - 1);
				if (msg != null) {
					getMessage(msg.getId(), true);
				}
			}else {
				pageIndex++;
				getMessage(null, true);
			}
			
		}
	}

	private void onPulltoRefreshCallback(boolean isMore) {
		if (isMore) {
			mPullToRefreshView.onFooterRefreshComplete();
		} else {
			mPullToRefreshView.onHeaderRefreshComplete();
		}
	}

	public void getMessage(String id, boolean isMore) {

		if (msgFrom == Constants.MSG_USER) {
			mUserNet.messageUML(UserManager.getSingleton().getActivePetId(),type, id, 20,isMore);
		} else {
			mUserNet.announcementList( "", pageIndex, 10,isMore);
		}
	}

	public void refreshMessage() {
		mPullToRefreshView.showHeaderAnimation();
		
		if (msgFrom != Constants.MSG_USER) {
			pageIndex=0;
		}
		getMessage("", false);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_MESSAGEUML:
			onPulltoRefreshCallback(bean.isIsMore());
			try {
				List<PMessage> pMessages = JsonUtils.getList(
						bean.getValue(), PMessage.class);
				// lvPrivateMsg.setAdapter(new
				// PrivateMsgListAdapter(mContext,pMessages));
				if (bean.isIsMore()) {
					mAdapter.addMore(pMessages);
				} else {
					mAdapter.refreshData(pMessages);
				}
			} catch (Exception e) {
				System.err.println("获取消息列表数据失败");
				e.printStackTrace();
			}
			break;
		case RequestCode.REQUEST_ANNOUNCEMENTLIST:
			onPulltoRefreshCallback(bean.isIsMore());
			try {
				String jsonStr=JsonParse.getSingleton().parseListString(bean.getValue());
				List<PMessage> pMessages = JsonUtils.getList(jsonStr, PMessage.class);
				if (bean.isIsMore()) {
					mAdapter.addMore(pMessages);
				} else {
					mAdapter.refreshData(pMessages);
				}
			} catch (Exception e) {
				System.err.println("获取系统消息列表数据失败");
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
		closeLoading();
		NetworkManager.getSingleton().canclePullRefresh(MessageActivity.this, mPullToRefreshView);
	}
}
