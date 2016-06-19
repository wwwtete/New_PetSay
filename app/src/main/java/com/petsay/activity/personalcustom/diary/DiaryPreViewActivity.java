package com.petsay.activity.personalcustom.diary;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.diary.diaryview.BasicDiaryView;
import com.petsay.component.view.FlipBookView;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetalkVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/5/25
 * @Description
 */
public class DiaryPreViewActivity extends BaseActivity implements NetCallbackInterface, View.OnClickListener, FlipBookView.OnPageTurnListener, SeekBar.OnSeekBarChangeListener {

//    @InjectView(R.id.viewpager)
//    private ViewPager mViewPage;

    @InjectView(R.id.flipbook)
    private FlipBookView mFlipBook;
    @InjectView(R.id.seekbar)
    private SeekBar mSeekBar;
    @InjectView(R.id.iv_leftborder)
    private ImageView mIvLeftBorder;
    @InjectView(R.id.iv_rightborder)
    private ImageView mIvRightBorder;

    @InjectView(R.id.tv_page)
    private TextView mTvPage;
//    @InjectView(R.id.iv_full)
//    private ImageView mIvFull;
    @InjectView(R.id.img_back)
    private ImageView mIvBack;
    @InjectView(R.id.iv_finish)
    private ImageView mIvFinish;

    @InjectView(R.id.rl_title)
    private RelativeLayout mLlTitle;
    @InjectView(R.id.rl_bottom)
    private RelativeLayout mRlBottom;

    private SayDataNet mNet;
    private ArrayList<PetalkVo> mSayVos;
//    private BasicDiaryView[] mPages;
//    private DiaryPagerAdapter mAdapter;
    private FlipBookAdapter mAdapter;
    private int mPageIndex = -1;
    private int mTotalPaging = -1;
    private int mTotalElements = 0;
    private int mTotalPageCount = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarypereview);
        initData();
        initView();
    }

    private void initData() {
        mSayVos = new ArrayList<PetalkVo>();
        mNet = new SayDataNet();
        mNet.setTag(this);
        mNet.setCallback(this);
        onMore();
//        initPageView();
//        mAdapter = new DiaryPagerAdapter(this,mPages);
        mAdapter = new FlipBookAdapter(this);
        updatePageCount();
    }

    private void onMore(){
        mPageIndex++;
        if(mTotalPaging == -1 || mPageIndex < mTotalPaging) {
            showLoading(false);
            mNet.petalkListBook(mPageIndex, 30);
        }
    }


//    private void initPageView() {
//        mPages = new BasicDiaryView[4];
//        for (int i=0;i<mPages.length;i++){
//            mPages[i] = new BasicDiaryView(this);
//        }
//    }

//    private void updateLayout(){
//        RelativeLayout.LayoutParams pl = (RelativeLayout.LayoutParams) mViewPage.getLayoutParams();
////        RelativeLayout.LayoutParams bl = (RelativeLayout.LayoutParams) mRlBottom.getLayoutParams();
//        RelativeLayout.LayoutParams tl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tl.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        int width = PublicMethod.getDisplayWidth(this);
//        int height = PublicMethod.getDisplayHeight(this);
//        if(isLandscape()){
//            int space = PublicMethod.getDiptopx(this, 60);
//            int top = PublicMethod.getDiptopx(this, 10);
//            pl.setMargins(space,top,space,top);
//            pl.width = width;
//            pl.height = height - top*2;
////            bl.width = space;
//            tl.leftMargin = 10;
//            tl.width = space -10;
//            tl.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        }else {
//            int top = getResources().getDimensionPixelOffset(R.dimen.diary_title_height);
//            int bottom = getResources().getDimensionPixelOffset(R.dimen.diary_bottom_height);
//            pl.setMargins(0,top,0,bottom);
//            pl.width = width;
//            pl.height = height - top - bottom;
////            bl.width = width;
//            tl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
//        }
//        mViewPage.setLayoutParams(pl);
////        mRlBottom.setLayoutParams(bl);
//        mTvPage.setLayoutParams(tl);
//    }

    @Override
    protected void initView() {
        super.initView();
//        updateLayout();
//        mIvFull.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mFlipBook.setAdapter(mAdapter);
        mFlipBook.setOnPageTurnListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mFlipBook.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFlipBook.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                float mTemplatewidth = 1819.0f;
                float mTemplateHeight = 2551.0f;
                float scale = mFlipBook.getHeight()/mTemplateHeight;
                ViewGroup.LayoutParams layoutParams = mFlipBook.getLayoutParams();
                layoutParams.width = (int) (mTemplatewidth*scale)*2;
                layoutParams.height = (int) (mTemplateHeight*scale);
                mFlipBook.setLayoutParams(layoutParams);
                mFlipBook.invalidate();
            }
        });

//        mViewPage.setAdapter(mAdapter);
//        mViewPage.setOnPageChangeListener(this);
        updatePageText(0);
//        mViewPage.setOffscreenPageLimit(2);

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        PetsayLog.d("屏幕发送变化：%s", newConfig.orientation);
//        int position = mViewPage.getCurrentItem();
//        if(isLandscape()){
//            if( position == 0) {
//                position = 0;
//            }else {
//                position = position % 2 == 0 ? position/2 : position/2+1;
//            }
//        }else {
//            position = position* 2;
//        }
//        updatePageCount();
//        mViewPage.setCurrentItem(position);
//        updateLayout();
//        mAdapter.notifyDataSetChanged();
//        updatePageText(mViewPage.getCurrentItem());
//        super.onConfigurationChanged(newConfig);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.iv_full:
//                onSetOrientation();
//                break;
            case R.id.img_back:
                this.finish();
                break;
            case R.id.iv_finish:
                Intent intent = new Intent(this,DiaryDetailsActivity.class);
                intent.putExtra("totalelements",mTotalElements);
                startActivity(intent);
                break;
        }
    }

    private void onSetOrientation() {
//        if(isLandscape()){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//        }else if(mViewPage.getCurrentItem() != 0 && mViewPage.getCurrentItem() != mAdapter.getCount()-1) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        try {
            if(mTotalPaging == -1) {
                JSONObject object = new JSONObject(bean.getValue());
                mTotalElements = object.getInt("totalElements");
                mTotalPaging = object.getInt("totalPages");
                mTotalPageCount = getTotalPage(true);
//                updatePageText(mViewPage.getCurrentItem());
                updatePageCount();
            }
            List<PetalkVo> datas = (ArrayList<PetalkVo>) JsonUtils.parseList(bean.getValue(), PetalkVo.class);
            setData(datas);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("解析Json异常，请联系客服");
        }
    }

    public void setData(List<PetalkVo> datas){
        mSayVos.addAll(datas);
        mAdapter.addMoreData(mSayVos);

//        for (int i=0;i<mPages.length;i++){
//            mPages[i].setData(mSayVos);
//        }
    }

    private boolean isLandscape(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void updatePageCount(){
        int totalPage = getTotalPage(false);
        if(mAdapter != null) {
            mAdapter.setCount(totalPage);
            mAdapter.setBookPageSize(getTotalPage(true));
            mAdapter.setTotalElements(mTotalElements);
        }
        mSeekBar.setMax(totalPage);
        mFlipBook.setAdapter(mAdapter);
    }

    /**
     * 计算总页数
     * @param isReal    是否计算真实的总页数不受横屏影响
     * @return
     */
    private int  getTotalPage(boolean isReal){
        int totalPage = 0;
        totalPage += mTotalElements/5*3;
        switch (mTotalElements%5) {
            case 1:
            case 2:
                totalPage+=1;
            break;
            case 3:
                totalPage+=2;
            break;
            case 4:
                totalPage+=3;
            break;
            default:
                break;
        }
        totalPage += Math.ceil(totalPage/16.0)*2;
        if (totalPage%2 != 0) {
            totalPage +=1;
        }
        if(isLandscape() && !isReal){
            totalPage = totalPage /2;
            totalPage += 4;
        }else {
            totalPage += 7;
        }

        return totalPage;
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }


//    @Override
//    public void onPageScrolled(int i, float v, int i1) {
//        if(isLandscape()) {
//            if(i == 0 ) {
////                mViewPage.setCurrentItem(0);
////                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//            }
////            else if(i == mAdapter.getCount()-1) {
////                mViewPage.setCurrentItem(mAdapter.getCount()-1);
////                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
////            }
//        }
//    }
//
//    @Override
//    public void onPageSelected(int i) {
//        updatePageText(i);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int i) {
//    }

    private void updatePageText(int currPage){
//        int curr = currPage;
//            if (isLandscape()) {
//            }else {
//                curr +=1;
//            }
        currPage = currPage*2 > mTotalPageCount ? mTotalPageCount : currPage*2;
        mTvPage.setText(""+currPage+"/"+ mTotalPageCount);
        if(currPage > mSayVos.size()/2){
            onMore();
        }
    }

    @Override
    public void onTurnStart(int currentPosition, int nextPosition) {
        if(nextPosition <= 0)
            mIvLeftBorder.setVisibility(View.INVISIBLE);

        if(nextPosition >= mFlipBook.getItemCount())
            mIvRightBorder.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTurnFinish(int currentPosition) {
//        PetsayLog.d("[onTurnFinish] count=%s | currentPosition=%s", count, currentPosition);
        if(currentPosition > 0)
            mIvLeftBorder.setVisibility(View.VISIBLE);
        else
            mIvLeftBorder.setVisibility(View.INVISIBLE);

        if(currentPosition >= mFlipBook.getItemCount())
            mIvRightBorder.setVisibility(View.INVISIBLE);
        else
            mIvRightBorder.setVisibility(View.VISIBLE);
        mSeekBar.setProgress(currentPosition);
        updatePageText(currentPosition);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        PetsayLog.d("[onProgressChanged]->progress=%s",progress);
        if(fromUser)
            mFlipBook.setCurrentPosition(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
