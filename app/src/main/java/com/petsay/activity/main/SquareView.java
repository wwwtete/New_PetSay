package com.petsay.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.petsay.R;
import com.petsay.activity.homeview.SquareActivity;
import com.petsay.activity.petalk.ALLSayListActivity;
import com.petsay.activity.petalk.ChannelSayListActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.award.AwardListActivity;
import com.petsay.activity.topic.TopicDetailActivity;
import com.petsay.activity.petalk.rank.RankActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.ActiveView;
import com.petsay.component.view.ScrollGridView;
import com.petsay.component.view.WrapContentHeightViewPager;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.network.net.TopicNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.forum.TopicDTO;
import com.petsay.vo.petalk.PetalkVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页广场界面
 *
 * @author G
 *
 */
public class SquareView extends LinearLayout implements  NetCallbackInterface {

    private int mLinkColor;
    private ScrollGridView mGrid;
    private ActiveView activeView;
    
    private LinearLayout mPointLayout;
    private WrapContentHeightViewPager mVPager;
    private List<ImageView> mImageViews;
    private View[] views;
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int PageIndex=0;
    private SquareVPagerAdapter myPagerAdapter;
    private Context mContext;

    private SayDataNet mSayDataNet;

    //广场所有
    private List<SquareVo> mSquareVos;
    //链接
    private List<SquareVo> mLinkSquareVos=new ArrayList<SquareVo>();
    //轮播图片
    private List<SquareVo> mPagerSquareVos=new ArrayList<SquareVo>();

    public SquareView(Context context) {
        super(context);
        initView();
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        mContext = getContext();
        inflate(mContext, R.layout.square, this);

        mSayDataNet=new SayDataNet();
        mSayDataNet.setCallback(this);
        mSayDataNet.setTag(mContext);
        mSayDataNet.layoutIntro(Constants.SQUARE_LAYOUT_VERSION);

        mGrid=(ScrollGridView) findViewById(R.id.grid_link);
        activeView=(ActiveView) findViewById(R.id.activeView);
        mVPager=(WrapContentHeightViewPager) findViewById(R.id.vPager);
        mPointLayout=(LinearLayout) findViewById(R.id.llayout);
        setClickListener();
    }
    
    private void setClickListener(){
    	mGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                SquareVo squareVo=mLinkSquareVos.get(position);
                SquareItemClickManager.squareVoClick(mContext,squareVo);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void refreshView() {
        try {
            activeView.removeAllViews();
            mSquareVos=(List<SquareVo>) DataFileCache.getSingleton().loadObject(Constants.SquareFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListData();
        mSayDataNet.layoutIntro(Constants.SQUARE_LAYOUT_VERSION);
    }

    private void setListData() {
        if (null != mSquareVos && !mSquareVos.isEmpty()) {
            ArrayList<SquareVo> temp = new ArrayList<SquareVo>();
            ArrayList<SquareVo> list = new ArrayList<SquareVo>();
            mLinkSquareVos.clear();
            mPagerSquareVos.clear();
            for (int i = 0; i < mSquareVos.size(); i++) {
                String url = mSquareVos.get(i).getIconUrl();
                temp.add(mSquareVos.get(i));
                if (mSquareVos.get(i).getDisplayType() == 2 && null != url&& !url.trim().equals("")) {
                    list.add(mSquareVos.get(i));
                } else if (mSquareVos.get(i).getDisplayType() == 3) {
                    mLinkSquareVos.add(mSquareVos.get(i));
                }else if (mSquareVos.get(i).getDisplayType() == 4) {
                    mPagerSquareVos.add(mSquareVos.get(i));
                }
            }
            DataFileCache.getSingleton().asyncSaveData(Constants.SquareFile,temp);
            activeView.initDataAndViews(list, Constants.SquareViewLayoutFlag);
            mGrid.setAdapter(new GridAdapter());

            mImageViews = new ArrayList<ImageView>();
            mPointLayout.removeAllViews();
            int height = PublicMethod.getDiptopx(getContext(),60);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutParams pointParams=new LayoutParams(PublicMethod.getPxInt(5,  mContext),PublicMethod.getPxInt(5,  mContext));
            pointParams.setMargins(PublicMethod.getPxInt(5, mContext), 0, PublicMethod.getPxInt(5,  mContext), 0);
            views=new View[ mPagerSquareVos.size()];
            DisplayImageOptions options = ImageLoaderOptionsManager.getGeneralOptions(R.drawable.rect_occupy_img);
            if (mPagerSquareVos.size()<2) {
            	mPointLayout.setVisibility(View.GONE);
			}
            for (int j = 0; j < mPagerSquareVos.size(); j++) {
                ImageView imageView=new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(layoutParams);
                ImageLoaderHelp.displayImage(mPagerSquareVos.get(j).getIconUrl(),imageView, options);
                mImageViews.add(imageView);
                imageView.setOnClickListener(new VPagerOnItemClickListener(1,j));
                View view=new View(mContext);
                view.setLayoutParams(pointParams);
                view.setOnClickListener(new VPagerOnItemClickListener(2,j));
                mPointLayout.addView(view);
                views[j]=view;
            }
            
            myPagerAdapter = new SquareVPagerAdapter(mImageViews);
            MyOnPageChangeListener pageListener = new MyOnPageChangeListener(null,null);
            mVPager.setOnPageChangeListener(pageListener);//new MyOnPageChangeListener(unFocuse,focuse));
            mVPager.setAdapter(myPagerAdapter);
            mVPager.setCurrentItem(0);
            pageListener.onPageSelected(0);
        }

    }

    public void clearView(){
        if (null!=activeView) {
//			activeView.removeAllViews();
        }
    }

    public class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (null!=mLinkSquareVos) {
                return mLinkSquareVos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (null != mLinkSquareVos && !mLinkSquareVos.isEmpty()) {
                return mLinkSquareVos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (null != mLinkSquareVos && !mLinkSquareVos.isEmpty()) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (null==convertView) {
                holder = new Holder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.square_grid_item, null);
                holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder=(Holder) convertView.getTag();
            }
            SquareVo squareVo=mLinkSquareVos.get(position);

            holder.tvTitle.setText(squareVo.getTitle());
            return convertView;
        }

    }
    private class Holder{
        private TextView tvTitle;
    }
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode) {
            case RequestCode.REQUEST_LAYOUTINTRO:
                long time;
                try {
                    time=Long.parseLong(JsonUtils.readjsonString("updateTime", bean.getValue()));
                } catch (Exception e) {
                    time=SharePreferenceCache.getSingleton(mContext).getSquareLayoutUpdateTime();
                }
                if (time!=SharePreferenceCache.getSingleton(mContext).getSquareLayoutUpdateTime()) {
                    SharePreferenceCache.getSingleton(mContext).setSquareLayoutUpdateTime(time);
                    mSayDataNet.layoutDatum(Constants.SQUARE_LAYOUT_VERSION);
                }else {
                    try {
                        activeView.removeAllViews();
                        mSquareVos=(List<SquareVo>) DataFileCache.getSingleton().loadObject(Constants.SquareFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setListData();
                }
                break;
            case RequestCode.REQUEST_LAYOUTDATUM:
                String jsonStr=bean.getValue();
                try {
                    activeView.removeAllViews();
                    mSquareVos=JsonUtils.getList(jsonStr, SquareVo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("广场标签json解析出错");
                }
                setListData();
                break;
            default:
                break;
        }

    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
    }

    /**
     * ViewPager适配器
     */
    private class SquareVPagerAdapter extends PagerAdapter {
        public List<ImageView> mListViews;

        public SquareVPagerAdapter(List<ImageView> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 头标点击监听
     */
    private  class VPagerOnItemClickListener implements OnClickListener {
        private int index = 0;
        //点击类型：pege 1，point 2
        private int _clickType;
        public VPagerOnItemClickListener(int clickType,int i) {
            index = i;
            _clickType=clickType;
        }

        @Override
        public void onClick(View v) {
            if (_clickType==1) {
                //pageitem点击事件
                SquareVo squareVo=mPagerSquareVos.get(index);
                SquareItemClickManager.squareVoClick(mContext,squareVo);
            }else {
                mVPager.setCurrentItem(index);
            }

        }
    };

    /**
     * 页卡切换监听
     */
    private  class MyOnPageChangeListener implements OnPageChangeListener {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        private Drawable mUnfocused;
        private Drawable mfocuse;
        private MyOnPageChangeListener(Drawable unfocused,Drawable foucese){
            if(unfocused == null)
                mUnfocused = getResources().getDrawable(R.drawable.page_indicator_focused);
            else
                this.mUnfocused = unfocused;
            if(foucese == null)
                mfocuse = getResources().getDrawable(R.drawable.page_indicator_unfocused);
            else
                this.mfocuse = foucese;
        }

        @Override
        public void onPageSelected(int arg0) {
            PageIndex  = arg0;
            PublicMethod.changeBtnBgImg(views, PageIndex, mfocuse, mUnfocused);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    }
}
