package com.petsay.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petsay.activity.BaseFragment;
import com.petsay.activity.homeview.HomeFocusView;
import com.petsay.network.net.PublishPetSayNet;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/11
 * @Description 主页：关注
 */
public class HomeFocusFragment extends BaseFragment {

    private HomeFocusView mFocusView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mFocusView =new HomeFocusView(getActivity(),new PublishPetSayNet());
        mFocusView.onRefresh();
        return mFocusView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onRefresh();
    }

    public void onRefresh(){
        if(mFocusView != null) {
            mFocusView.refreshUploadList();
            mFocusView.onRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
