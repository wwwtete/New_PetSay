package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.activity.user.adapter.AttentionAdapter;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * 我关注的
 * @author G
 *
 */
public class AttentionActivity extends BaseActivity implements NetCallbackInterface {
    private TitleBar mTitleBar;
    private ListView lvAttention;
    private AttentionAdapter attentionAdapter;
    //	private UserModule mUserModule;
//	private UserData mUserData;
    private String petId;
    private int mPageIndex = 0;
    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPullToRefreshView;
    /*0:个人中心跳转过来的  1：聊天界面跳转过来的*/
    private int mType = 0;

    private UserNet mUserNet;
//	private List<PetInfo> mPetInfos;
//	private Handler handler=new Handler(){
//		public void handleMessage(Message msg) {
//			onPulltoRefreshCallback(msg);
//			switch (msg.what) {
//			case UserData.GET_MYFOCUS_SUCCESS:
//				mUserModule.removeListener(mUserData);
//				
//				
//				break;
//			case UserData.GET_MYFOCUS_FAILED:
//				mUserModule.removeListener(mUserData);
//				if (msg.arg1==-1) {
//					NetworkManager.getSingleton().canclePullRefresh(AttentionActivity.this,mPullToRefreshView );
//				}
//				break;
//			}
//			
//		};
//	};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.attention_activity);
        mUserNet=new UserNet();
        mUserNet.setCallback(this);
        mUserNet.setTag(this);
        initView();
        initTitleBar();
        setListener();
        petId=getIntent().getStringExtra("petId");
        mType = getIntent().getIntExtra("type", 0);
//		mUserModule=UserModule.getSingleton();
//		mUserData=new UserData(handler);
        showLoading();
    }

    @Override
    protected void onResume() {
        onRefresh();
        super.onResume();
    }

    private void initTitleBar() {
        mTitleBar.setTitleText("关注");
        mTitleBar.setFinishEnable(true);
    }

    protected void initView(){
        super.initView();
        mTitleBar=(TitleBar) findViewById(R.id.titlebar);
        lvAttention=(ListView) findViewById(R.id.lv_attention);
        attentionAdapter = new AttentionAdapter(AttentionActivity.this);
        lvAttention.setAdapter(attentionAdapter);
    }

    private void setListener() {
        lvAttention.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                PetVo petInfo = (PetVo) attentionAdapter.getItem(position);
                if(mType == 1) {
                    Intent intent = new Intent(AttentionActivity.this, ChatActivity.class);
                    intent.putExtra("petinfo", petInfo);
                    startActivity(intent);
                }else {
                    ActivityTurnToManager.getSingleton().userHeaderGoto(AttentionActivity.this, petInfo);
                }
            }
        });
        mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                onRefresh();
            }
        });

        mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                onLoadMore();
            }
        });
    }

    private void onPulltoRefreshCallback(boolean isMore){
        if(isMore){
            mPullToRefreshView.onFooterRefreshComplete();
        }else {
            mPullToRefreshView.onHeaderRefreshComplete();
        }
    }

    /**
     * 刷新操作
     */
    public void onRefresh() {
        mPullToRefreshView.showHeaderAnimation();
        mPageIndex = 0;
        netwrok(0,false);
    }

    /**
     * 上拉加载更多操作
     */
    public void onLoadMore() {
        mPageIndex ++;
        netwrok(mPageIndex,true);
    }

    private void netwrok(int index,boolean isMore){
//		showLoading();
//		mUserModule.addListener(mUserData);
        mUserNet.MyFocus(petId, index, 30,isMore);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode) {
            case RequestCode.REQUEST_MYFOCUS:
                List<PetVo> mPetInfos = JsonUtils.parseList(bean.getValue(), PetVo.class);
                onPulltoRefreshCallback(bean.isIsMore());
                if(bean.isIsMore()){

                    attentionAdapter.addMore(mPetInfos);
                }else {
                    attentionAdapter.refreshData(mPetInfos);
                }
                break;
            case RequestCode.REQUEST_CANCLEFOCUS:
                break;
            default:
                break;
        }

    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        onErrorShowToast(error);
        switch (requestCode) {
            case RequestCode.REQUEST_MYFOCUS:
                onPulltoRefreshCallback(error.isIsMore());
                break;
            default:
                break;
        }

    }
}
