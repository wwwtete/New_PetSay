package com.petsay.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petsay.activity.BaseFragment;
import com.petsay.activity.homeview.HotView;
import com.petsay.component.gifview.GifViewManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/11
 * @Description 主页：热门说说
 */
public class HotPetalkFragment extends BaseFragment {

    /**已删除自己的Petalk ID列表*/
    public static final List<String> deletedSelfPetalkList = new ArrayList<String>(10);


//    private HotPetalkAdapter mAdapter;
//    private SayDataNet mSayDataNet;
//    private int mPageIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new HotView((MainActivity)getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mSayDataNet=new SayDataNet();
//        mSayDataNet.setCallback(this);
//        mSayDataNet.setTag(this);
//
//        mAdapter = new HotPetalkAdapter(getActivity());
//        mGvPetalk.setAdapter(mAdapter);
//        mRefreshView.setOnFooterRefreshListener(this);
//        mRefreshView.setOnHeaderRefreshListener(this);
//        mGvPetalk.setOnItemClickListener(this);
//        readLocalData();
//        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        GifViewManager.getInstance().stopGif();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden)
            GifViewManager.getInstance().stopGif();
    }

    //    private void readLocalData(){
//        try {
//            List<PetalkVo> sayVos= (List<PetalkVo>) DataFileCache.getSingleton().loadObject(Constants.HotListFile);
//            if (null!=sayVos&&!sayVos.isEmpty()) {
//                mAdapter.refreshData(sayVos);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("获取热门缓存数据失败");
//        }
//    }
//
//    @Override
//    public void onFooterRefresh(PullToRefreshView view) {
//        onLoadMore();
//    }
//
//    @Override
//    public void onHeaderRefresh(PullToRefreshView view) {
//        onRefresh();
//    }
//
//    /**
//     * 刷新操作
//     */
//    public void onRefresh() {
//            mRefreshView.showHeaderAnimation();
//            mPageIndex = 0;
//            netwrok(0,false);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        checkDelectedSefPetalk();
//    }
//
//    private void checkDelectedSefPetalk() {
//        int count = mAdapter.getCount();
//        if(count > 0 && !deletedSelfPetalkList.isEmpty()){
//            for (int i=0;i<count;i++){
//                if(deletedSelfPetalkList.contains(mAdapter.getItem(i).getId()))
//                    mAdapter.deleteItem(i,false);
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    /**
//     * 上拉加载更多操作
//     */
//    public void onLoadMore() {
//        mPageIndex ++;
//        netwrok(mPageIndex,true);
//    }
//
//    /**
//     * 联网获取数据
//     * @param pageIndex
//     * @param isMore
//     */
//    private void netwrok(int pageIndex,boolean  isMore){
//        if (UserManager.getSingleton().isLoginStatus()) {
//            mSayDataNet.petalkList(UserManager.getSingleton()
//                            .getActivePetInfo().getId(), pageIndex,
//                    Constants.SayListPageSize, isMore);
//        } else
//            mSayDataNet.petalkList("", pageIndex, Constants.SayListPageSize,isMore);
//    }
//
//
//    /**
//     * 获取数据成功回调接口
//     *
//     * @param bean        服务器返回数据
//     * @param requestCode 区分请求码
//     */
//    @Override
//    public void onSuccessCallback(ResponseBean bean, int requestCode) {
//        ArrayList<PetalkVo> sayVos = (ArrayList<PetalkVo>) JsonUtils.parseList(bean.getValue(), PetalkVo.class);
//        UserManager.getSingleton().addFocusAndStepByList(sayVos);
//        if (bean.isIsMore()) {
//            if (sayVos == null || sayVos.isEmpty())
//                showToast(R.string.no_more);
//            mAdapter.addMoreData(sayVos);
//        } else {
//            UserManager.getSingleton().focusMap.clear();
//            mAdapter.refreshData(sayVos);
//            DataFileCache.getSingleton().asyncSaveData(Constants.HotListFile, sayVos);
//        }
//        mRefreshView.onComplete(bean.isIsMore());
//    }
//
//    /**
//     * 获取数据失败回调接口(也包括服务器返回500的错误)
//     *
//     * @param error       错误信息类
//     * @param requestCode 请求码
//     */
//    @Override
//    public void onErrorCallback(PetSayError error, int requestCode) {
//        mRefreshView.onComplete(error.isIsMore());
//    }
//
//    /**
//     * Callback method to be invoked when an item in this AdapterView has
//     * been clicked.
//     * <p/>
//     * Implementers can call getItemAtPosition(position) if they need
//     * to access the data associated with the selected item.
//     *
//     * @param parent   The AdapterView where the click happened.
//     * @param view     The view within the AdapterView that was clicked (this
//     *                 will be a view provided by the adapter)
//     * @param position The position of the view in the adapter.
//     * @param id       The row id of the item that was clicked.
//     */
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        PetalkVo sayVo= mAdapter.getItem(position);
//        if (null!=sayVo) {
//            Intent intent = new Intent();
//            //		intent.setClass(mContext, HotDetails_Activity.class);
//            intent.setClass(getActivity(), DetailActivity.class);
//            Constants.Detail_Sayvo=sayVo;
//            getActivity().startActivity(intent);
//        }
//    }
}
