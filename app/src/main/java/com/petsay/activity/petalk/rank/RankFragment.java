package com.petsay.activity.petalk.rank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseFragment;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.petalk.adapter.PetScoreRankAdapter;
import com.petsay.activity.petalk.adapter.PetalkRankAdapter;
import com.petsay.activity.user.OtherUserActivity;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.rank.PetScoreTotalRankDayDTO;
import com.petsay.vo.rank.PetalkPopRankWeekDTO;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/17
 * @Description
 */
public class RankFragment extends BaseFragment implements NetCallbackInterface, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView pulltorefreshview;
    @InjectView(R.id.lv_rank)
    private ListView lvRank;

    private ExBaseAdapter mAdapter;
    private SayDataNet mNet;

    private int mPageSize = 10;
    private int mPageIndex = 0;

    /**1：周排行，0：总排行榜*/
    private int mRankType;
    /**1:说说排行   0：爱宠排行榜*/
    private int mType;

    /**
     * @param type 1:说说排行   0：爱宠排行榜
     * @param rankType 1：周排行，0：总排行榜
     * @return
     */
    public static RankFragment getInstance(int type,int rankType){
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("rank_type",rankType);
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank,null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRankType = getArguments().getInt("rank_type",0);
        mType = getArguments().getInt("type",0);
        mNet = new SayDataNet();
        mNet.setTag(this);
        mNet.setCallback(this);

        if(mType == 0) {
            mAdapter = new PetScoreRankAdapter(getActivity(),mRankType);
            lvRank.setOnItemClickListener(this);
        }else {
            mAdapter = new PetalkRankAdapter(getActivity());
        }
        lvRank.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {
        pulltorefreshview.setOnFooterRefreshListener(this);
        pulltorefreshview.setOnHeaderRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter.getCount() == 0)
            getRankList(false);
    }

    private void getRankList(boolean isMore){
        showLoading();
        if(mType == 0) {
            mNet.petScoreTotalRankDayList(mRankType,mPageIndex,mPageSize,isMore);
        }else {
            mNet.petalkPopRankWeekList(mRankType,mPageIndex, mPageSize, isMore);
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_PETALKPOPRANKWEEKLIST:
                List<PetalkPopRankWeekDTO> list = JsonUtils.parseList(bean.getValue(),PetalkPopRankWeekDTO.class);
                if(bean.isIsMore()){
                        if(list == null || list.isEmpty()) {
                                showToast(R.string.no_more);
                        }
                    else {
                                mAdapter.addMoreData(list);
                        }
                }else {
                        mAdapter.refreshData(list);
                }
                break;
            case RequestCode.REQUEST_PETSCORETOTALRANKDAYLIST:
                List<PetScoreTotalRankDayDTO> datas = JsonUtils.parseList(bean.getValue(),PetScoreTotalRankDayDTO.class);
                if(bean.isIsMore()){
                    if(datas == null || datas.isEmpty()) {
                        showToast(R.string.no_more);
                    }
                    else {
                        mAdapter.addMoreData(datas);
                    }
                }else {
                    mAdapter.refreshData(datas);
                }
                break;
        }
        pulltorefreshview.onComplete(bean.isIsMore());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
        pulltorefreshview.onComplete(error.isIsMore());
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 0;
        getRankList(false);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPageIndex++;
        getRankList(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mType == 0){
//            Intent intent = new Intent(getActivity(),GrowupUndergoActivity.class);
            String petId = ((PetScoreTotalRankDayDTO)mAdapter.getItem(position)).getPetId();
//            intent.putExtra("petid",petId);
//            intent.putExtra("ranktype",mRankType);
//            startActivity(intent);

            PetVo petInfo = new PetVo();
            petInfo.setId(petId);
            Intent intent = new Intent(getActivity(), OtherUserActivity.class);
            intent.putExtra("petInfo", petInfo);
            startActivity(intent);
        }
    }
}
