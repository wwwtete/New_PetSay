package com.petsay.activity.topic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.topic.adapter.TopicListAdapter;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TopicListHeadView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.TopicDTO;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * 话题列表
 * 
 * @author G
 */
public class TopicListActivity extends BaseActivity implements IAddShowLocationViewService,
		NetCallbackInterface,OnClickListener {
	private ListView lv;

	private TextView tvTitleRight;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;

    private TopicNet mTopicNet;
	private TopicListAdapter mTopicListAdapter;
	// private ShopNet mShopNet;
	private int pageSize = 10;
    private TopicListHeadView mHeadView;
//	private AwardNet mAwardNet;
//	private SayDataNet mSayDataNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_topic_list);
		initView();
        mTopicNet = new TopicNet();
        mTopicNet.setCallback(this);
        onRefresh();
	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		lv = (ListView) findViewById(R.id.lv_shop);
		initTitleBar("互动吧",true);
		initPullToRefreshView();
		mTopicListAdapter = new TopicListAdapter(TopicListActivity.this);
        mHeadView = new TopicListHeadView(this);
        lv.addHeaderView(mHeadView);
		lv.setAdapter(mTopicListAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicDTO topicDTO = mTopicListAdapter.getItem(position-lv.getHeaderViewsCount());
                jumpDetailActivity(topicDTO);
            }
        });
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
        showLoading(false);
        pageSize++;
        mTopicNet.topicList(mTopicListAdapter.getItem(mTopicListAdapter.getCount()-1).getId(),pageSize,true);

	}

	private void onRefresh() {
        showLoading(false);
        mTopicNet.topicList("",pageSize,false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_TOPICLIST:
                PublicMethod.log_d(""+bean.getValue());
                List<TopicDTO> datas = null;
                try {
                    datas = JsonUtils.getList(bean.getValue(), TopicDTO.class);
                    if(datas.isEmpty()){
                        if(bean.isIsMore())
                            showToast(R.string.no_more);
                        else
                            showToast("暂时没有话题");
                    }else {
                        if(bean.isIsMore())
                            mTopicListAdapter.addMoreData(datas);
                        else {
                            TopicDTO topicDTO = datas.remove(0);
                            if(mHeadView.getTag() == null) {
                                initListHeadView(topicDTO);
                            }
                            mTopicListAdapter.refreshData(datas);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        mPullView.onComplete(bean.isIsMore());
	}

    private void initListHeadView(TopicDTO dto){
        if(dto == null)
            return;

        mHeadView.setImageUrl(dto.getPic());
        mHeadView.setContent(dto.getContent());
//        int bottom = PublicMethod.getDiptopx(this,20);
//        mHeadView.setPadding(0, 0, 0, bottom);
        mHeadView.setBackgroundColor(Color.parseColor("#f0f0f0"));
        mHeadView.setTag(dto);
        mHeadView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicDTO topicDTO = (TopicDTO) mHeadView.getTag();
                jumpDetailActivity(topicDTO);
            }
        });
        mHeadView.invalidate();
    }


    private void jumpDetailActivity(TopicDTO dto){
        Intent intent = new Intent();
        intent.setClass(this,TopicDetailActivity.class);
        intent.putExtra("topicDTO", dto);
        startActivity(intent);
    }

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_TOPICLIST:
			mPullView.onComplete(error.isIsMore());
			break;
		default:
			break;
		}
	}

	

	@Override
	public void onClick(View v) {
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return layoutRoot;
	}

	@Override
	public Activity getActivity() {
		return this;
	}

}