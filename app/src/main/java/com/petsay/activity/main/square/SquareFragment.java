package com.petsay.activity.main.square;

import java.util.ArrayList;
import java.util.List;

import com.petsay.R;
import com.petsay.activity.BaseFragment;
import com.petsay.activity.award.AwardListActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.main.HotActiveActivity;
import com.petsay.activity.main.SquareItemClickManager;
import com.petsay.activity.petalk.rank.RankActivity;
import com.petsay.activity.topic.TopicDetailActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.LoopImgPagerView.OnLoopImgItemClickListener;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.PublishPetSayNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.network.net.TopicNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.forum.TopicDTO;
import com.petsay.vo.petalk.PetTagVo;
import com.petsay.vo.petalk.TagPetalkDTO;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/11
 * @Description 主页：广场
 */
public class SquareFragment extends BaseFragment implements NetCallbackInterface, View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener, OnLoopImgItemClickListener {

    private PullToRefreshView mPullRefresh;
    private LinearLayout mLlTopic;
    private ImageView mIvThumbnail;
    private TextView mTvContent;
    private TextView mTvCount;
    private LinearLayout mLlPetsayTop;
    private LinearLayout mLlPetTop;
//    private LinearLayout mLlPlaytourTop;
    private ImageView mImgPetalk;
    private LinearLayout mLlGift;
//    private FrameLayout mFlHot;
    private ImageView mIvTopHotMore,mIvTopHot1,mIvTopHot2,mIvTopHot3;
    private ImageView mIvAwardPic;
    private LinearLayout mLlHotPetalk;
    private LinearLayout mLlHotTag;
    private SquareHotTagView mTagView;
    private LoopImgPagerView mLoopImgPagerView;

    private TopicNet mTopicNet;
    private SayDataNet mSayNet;
    private PublishPetSayNet mTagNet;

    private TopicDTO mTopicDTO;

    private List<SquareVo> mTopHots = null;
    private List<SquareVo> mDatumSquareVos;
    private List<SquareVo> mPagerSquareVos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square, container,false);
        initView(view);
        initData();
        return view;
//        return new SquareView(getActivity());
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        findViews(view);
        setListener();
    }

    private void findViews(View view) {
        mPullRefresh = (PullToRefreshView) view.findViewById(R.id.pullrefresh);
        mLlTopic = (LinearLayout)view.findViewById(R.id.ll_topic);
        mIvThumbnail = (ImageView)view.findViewById(R.id.iv_thumbnail);
        mTvContent = (TextView)view.findViewById(R.id.tv_content);
        mTvCount = (TextView)view.findViewById(R.id.tv_count);
        mLlPetsayTop = (LinearLayout)view.findViewById(R.id.ll_petsay_top);
        mLlPetTop = (LinearLayout)view.findViewById(R.id.ll_pet_top);
//        mLlPlaytourTop = (LinearLayout)view.findViewById(R.id.ll_Playtour_top);
        mImgPetalk = (ImageView)view.findViewById(R.id.img_petalk);
        mLlGift = (LinearLayout)view.findViewById(R.id.ll_gift);
//        mFlHot = (FrameLayout)view.findViewById(R.id.fl_hot);
        mIvTopHotMore=(ImageView) view.findViewById(R.id.iv_tophot_more);
        mIvTopHot1=(ImageView) view.findViewById(R.id.iv_tophot1);
        mIvTopHot2=(ImageView) view.findViewById(R.id.iv_tophot2);
        mIvTopHot3=(ImageView) view.findViewById(R.id.iv_tophot3);
        mIvAwardPic=(ImageView) view.findViewById(R.id.iv_awardpic);
        mLlHotPetalk = (LinearLayout)view.findViewById(R.id.ll_hot_petalk);
        mLlHotTag = (LinearLayout)view.findViewById(R.id.ll_hot_tag);
        mTagView = (SquareHotTagView) view.findViewById(R.id.hottagview);
        mLoopImgPagerView=(LoopImgPagerView) view.findViewById(R.id.loopImgview);
		mLoopImgPagerView.setOnLoopImgItemClickListener(this);
		mLoopImgPagerView.setVisibility(View.GONE);
    }

    private void setListener() {
        mLlPetsayTop.setOnClickListener(this);
        mLlPetTop.setOnClickListener(this);
//        mLlPlaytourTop.setOnClickListener(this);
        mLlGift.setOnClickListener(this);
        mLlTopic.setOnClickListener(this);
        mPullRefresh.setOnHeaderRefreshListener(this);
        
      
        mIvTopHotMore.setOnClickListener(this);
    }

    private void initData() {
        mTopicNet = new TopicNet();
        mTopicNet.setCallback(this);
        mSayNet = new SayDataNet();
        mSayNet.setCallback(this);
        mTagNet = new PublishPetSayNet();
        mTagNet.setCallback(this);
        gethotTagSayList();
        getHotTag();
        getTopicTopOne();
        getTop3Hot();
        getAwardPic();
        getVPagerData();
    }

    private void getHotTag(){
        mTagNet.getHotTagList();
    }

    private void getTopicTopOne(){
        mTopicNet.topicTopOne();
    }

    private void gethotTagSayList(){
        mSayNet.getTopTagHotList();
    }
    
    private void getTop3Hot(){
    	mSayNet.layoutTop3Hot();
    }
    
    private void getAwardPic(){
    	mSayNet.layoutDatum(6);
    }
    
    private void getVPagerData(){
    	mSayNet.layoutDatum(Constants.SQUARE_LAYOUT_VERSION);
    }
    
    private void initVPagerData(){
		if (null != mDatumSquareVos && !mDatumSquareVos.isEmpty()) {
			 mPagerSquareVos = new ArrayList<SquareVo>();
			for (int i = 0; i < mDatumSquareVos.size(); i++) {
				if (mDatumSquareVos.get(i).getDisplayType() == 4) {
					mPagerSquareVos.add(mDatumSquareVos.get(i));
				}
			}
			int size = mPagerSquareVos.size();
			if (size > 0) {
				mLoopImgPagerView.setVisibility(View.VISIBLE);
				String[] imgUrls = new String[size];
				for (int i = 0; i < mPagerSquareVos.size(); i++) {
					imgUrls[i] = mPagerSquareVos.get(i).getIconUrl();
				}
				mLoopImgPagerView.setImgUrls(imgUrls);
			} else {
			}
		}
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_GETHOTTAGLIST:
                onGetHotTagList(bean);
                break;
            case RequestCode.REQUEST_GETTOPTAGHOTLIST:
                onGethotTagSayList(bean);
                break;
            case RequestCode.REQUEST_TOPICTOPONE:
                onTopicTopOne(bean);
                break;
            case RequestCode.REQUEST_LAYOUTTOP3HOT:
            	onLayoutTop3Hot(bean);
            	break;
            case RequestCode.REQUEST_LAYOUTDATUM:
            	
            	if (bean.getTag().equals(6)) {
            		onAwardPic(bean);
				}else if(bean.getTag().equals(Constants.SQUARE_LAYOUT_VERSION)){
					try {
	            		mDatumSquareVos = JsonUtils.getList(bean.getValue(), SquareVo.class);
	            		initVPagerData();
	            	} catch (Exception e) {
	    				e.printStackTrace();
	    				System.err.println("奖品列表轮播图json解析出错");
	    			}
				}
            	break;
        }
        mPullRefresh.onComplete(false);
    }
    private void onAwardPic(ResponseBean bean){
    	try {
			List<SquareVo> awardSquares=JsonUtils.getList(bean.getValue(), SquareVo.class);
		    ImageLoaderHelp.displayContentImage(awardSquares.get(0).getIconUrl(), mIvAwardPic);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    //热门活动
    private void onLayoutTop3Hot(ResponseBean bean){
    	
        try {
        	mTopHots=JsonUtils.getList(bean.getValue(), SquareVo.class);
			 if (null!=mTopHots&&!mTopHots.isEmpty()) {
					ImageLoaderHelp.displayContentImage(mTopHots.get(0).getIconUrl(), mIvTopHot1);
					mIvTopHot1.setOnClickListener(this);
					ImageLoaderHelp.displayContentImage(mTopHots.get(1).getIconUrl(), mIvTopHot2);
					mIvTopHot2.setOnClickListener(this);
					ImageLoaderHelp.displayContentImage(mTopHots.get(2).getIconUrl(), mIvTopHot3);
					mIvTopHot3.setOnClickListener(this);
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
        
    }

    private void onTopicTopOne(ResponseBean bean) {
        mLlTopic.setVisibility(View.VISIBLE);
        mTopicDTO = JsonUtils.resultData(bean.getValue(), TopicDTO.class);
        ImageLoaderHelp.displayContentImage(mTopicDTO.getPic(), mIvThumbnail);
        mTvContent.setText(mTopicDTO.getContent());
        mTvCount.setText(mTopicDTO.getViewCount() + "人参与讨论");
    }

    private void onGethotTagSayList(ResponseBean bean) {
        try {
            List<TagPetalkDTO> datas = JsonUtils.getList(bean.getValue(), TagPetalkDTO.class);
            if(datas != null && !datas.isEmpty()){
                mLlHotPetalk.setVisibility(View.VISIBLE);
                mLlHotPetalk.removeAllViews();
                for (int i=0;i<datas.size();i++){
                    mLlHotPetalk.addView(getPetalkItem(datas.get(i)));
                }
            }else {
                mLlHotPetalk.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLlTopic.setVisibility(View.GONE);
        }

    }

    private SquareHotTagPetalkItemView getPetalkItem(TagPetalkDTO dto){
        SquareHotTagPetalkItemView view = new SquareHotTagPetalkItemView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = PublicMethod.getDiptopx(getActivity(),10);
        view.setLayoutParams(params);
        view.initView(dto);
        return view;
    }

    private void onGetHotTagList(ResponseBean bean) {
        try {
            List<PetTagVo> tags = JsonUtils.getList(bean.getValue(), PetTagVo.class);
            if(tags == null || tags.isEmpty())
                mLlHotTag.setVisibility(View.GONE);
            else {
                mLlHotTag.setVisibility(View.VISIBLE);
                mTagView.initView(tags);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLlHotTag.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        mPullRefresh.onComplete(false);
        switch (requestCode){
            case RequestCode.REQUEST_GETHOTTAGLIST:
                mLlHotTag.setVisibility(View.GONE);
                break;
            case RequestCode.REQUEST_GETTOPTAGHOTLIST:
                mLlHotPetalk.setVisibility(View.GONE);
                break;
            case RequestCode.REQUEST_TOPICTOPONE:
                mLlTopic.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.ll_topic:
                if(mTopicDTO != null){
                    intent = new Intent();
                    intent.setClass(getActivity(), TopicDetailActivity.class);
                    intent.putExtra("topicDTO",mTopicDTO);
                    startActivity(intent);
                }
                break;
            case R.id.ll_petsay_top:
                intent = new Intent();
                intent=new Intent(getActivity(), RankActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.ll_pet_top:
                intent = new Intent();
                intent=new Intent(getActivity(), RankActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
//            case R.id.ll_Playtour_top:
//                break;
            case R.id.ll_gift:
                intent = new Intent();
                if (UserManager.getSingleton().isLoginStatus()) {
                    intent.setClass(getActivity(), AwardListActivity.class);
                }else {
                    intent=new Intent(getActivity(), UserLogin_Activity.class);
                }
                startActivity(intent);
                break;
            case R.id.iv_tophot_more:
                intent =new Intent(getActivity(),HotActiveActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_tophot1:
            	 SquareItemClickManager.squareVoClick(getActivity(),mTopHots.get(0));
            	break;
            case R.id.iv_tophot2:
            	SquareItemClickManager.squareVoClick(getActivity(),mTopHots.get(1));
            	break;
            case R.id.iv_tophot3:
            	SquareItemClickManager.squareVoClick(getActivity(),mTopHots.get(2));
            	break;
        }
            
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        onRefresh(true);
    }

    private void onRefresh(boolean force) {
        if(mLlHotPetalk.getVisibility() == View.GONE || force)
            gethotTagSayList();
        if(mTopicDTO == null || force)
            getTopicTopOne();
        if(mLlHotTag.getVisibility() == View.GONE || force)
            getHotTag();
    }

	@Override
	public void OnLoopImgItemClick(View v, int position) {
		 SquareItemClickManager.squareVoClick(getActivity(),mPagerSquareVos.get(position));
			
	}
}
