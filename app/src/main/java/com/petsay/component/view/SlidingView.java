package com.petsay.component.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动切换界面
 *
 */
public class SlidingView extends LinearLayout {

    private ViewPager viewPager;
    private FragmentManager mFragmentManager;
    private List<Fragment> mFragments; // Tab页面列表
    private ImageView imageView;// 动画图片view
    private TextView mTopView;// 页卡头标
    private List<TextView> listTopViews; // Tab页面列表
    private int initialOffset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int mCursorWidth;// 动画图片宽度
    //	private LinearLayout layout;
    private LinearLayout tabTitleLayout;// 存放tab表头的linearlayout
    private String[] topViewName;// tab表头集合
    //	private Context context;
    private SlidingViewCallback mListener;//
    private int textSize;// tab表头字体大小
    private int textSelectColor;// tab表头选中字体颜色
    private View mainView;
    private ImageView mNoReadMsgIcon = null;
    private int textNormalColor;
    
    
    private int mTitleLayoutBgColor=-100;
    private int mViewpagerBgColor=-100;
    private boolean mIsShowTitleSpaceLine=false;

    public SlidingView(Context context){
        super(context);
        setOrientation(VERTICAL);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public interface SlidingViewCallback {
        /**单击tab表头自定义监听*/
        public void setCurrentItem(int item);
        /**viewpage改变自定义监听*/
        public void slidingViewPageChange(int item);
    }

    public void setCallback(SlidingViewCallback listener) {
        mListener = listener;
    }

    public void initView(FragmentManager fragmentManager,String[] topViewName, List<Fragment> listViews){
        initView(fragmentManager,topViewName,listViews,20,Color.parseColor("#4EBAF8"),Color.parseColor("#FFFFFF"));

    }
    
    public void initView(FragmentManager fragmentManager,String[] topViewName, List<Fragment> listViews,int textSelectColor, int textNormalColor,boolean isShowTitleSpaceLine,int titleLayoutBgColor,int viewpagerBgColor){
    	mIsShowTitleSpaceLine=isShowTitleSpaceLine;
    	mTitleLayoutBgColor=titleLayoutBgColor;
    	mViewpagerBgColor=viewpagerBgColor;
    	initView(fragmentManager,topViewName,listViews,18,textSelectColor,textNormalColor);

    }

    /**
     * @param textSize
     *            tab表头字体大小
     * @param textSelectColor
     *            tab表头选中字体颜色
     * @param textNormalColor
     * 			   tab表头正常字体颜色
     */
    public void initView(FragmentManager fragmentManager,String[] topViewName, List<Fragment> listViews, int textSize, int textSelectColor, int textNormalColor){
        this.mFragmentManager = fragmentManager;
        this.topViewName = topViewName;
        this.mFragments = listViews;
        this.textSize = textSize;
        this.textSelectColor = textSelectColor;
        this.textNormalColor   = textNormalColor;
//        mCursorWidth = BitmapFactory.decodeResource(getContext().getResources(),
//                R.drawable.sanjiao).getWidth();// 获取图片宽度
        mCursorWidth = PublicMethod.getDisplayWidth(getContext())/listViews.size();

        onInitView();
        initAnimation();
        initTitleView();
        initViewPage();
        setCursorViewWidth(mCursorWidth);
    }

    private void onInitView() {
        setOrientation(VERTICAL);
        mainView = inflate(getContext(),R.layout.common_sliding_component_layout, this);
        tabTitleLayout = (LinearLayout) mainView.findViewById(R.id.viewPagerTabLayout);
        imageView = (ImageView) mainView.findViewById(R.id.cursor);
        viewPager = (ViewPager) mainView.findViewById(R.id.vPager);
    }

    public void setMainViewBg(int color) {
        if (mainView != null) {
            mainView.setBackgroundColor(getContext().getResources().getColor(color));
        }
    }

    /**
     *
     * @param resId
     */
    public void setCursorViewImgResource(int resId) {
        // mCursorWidth = width;
        // initialOffset = initValue;// 计算偏移量
        // LinearLayout.LayoutParams params =
        // (LinearLayout.LayoutParams)imageView.getLayoutParams();
        // params.width = width;
        // imageView.setLayoutParams(params);
        // Matrix matrix = new Matrix();
        // matrix.postTranslate(initialOffset, 0);
        // imageView.setImageMatrix(matrix);// 设置动画初始位置
        imageView.setImageResource(resId);
        imageView.setBackgroundResource(getContext().getResources().getColor(R.color.transparent));
    }

    /**
     * 设置指示器的背景颜色
     *
     * @param color
     */
    public void setCursorViewBgColor(int color) {
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setImageDrawable(null);
        imageView.setBackgroundColor(color);
    }

    /**
     * 设置指示器的宽度
     * @param width 指示器的宽
     */
    public void setCursorViewWidth(int width) {
        mCursorWidth = width;
//		initialOffset = initValue;// 计算偏移量
        initialOffset = (PublicMethod.getDisplayWidth(getContext()) / topViewName.length - mCursorWidth) / 2;// 计算偏移量
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.width = width;
        params.leftMargin = initialOffset;
        imageView.setLayoutParams(params);
        Matrix matrix = new Matrix();
        matrix.postTranslate(initialOffset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }


    public void setCursorViewHeight(int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.height = height;
        imageView.setLayoutParams(params);
    }

    /**
     * 是否因此游标
     * @param flag
     */
    public void hideCursorView(boolean flag){
        if(flag){
            imageView.setVisibility(GONE);
        }else {
            imageView.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置tab的背景图片
     *
     * @param resId
     */
    public void setTabBackgroundResource(int resId) {
        tabTitleLayout.setBackgroundResource(resId);
    }

    /**
     * 设置tab的背景色
     *
     * @param color
     */
    public void setTabBackgroundColor(int color) {
        tabTitleLayout.setBackgroundColor(getContext().getResources().getColor(color));
    }

    /**
     * 设置tab的高度 单位dip
     *
     * @param dip
     */
    public void setTabHeight(float dip) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabTitleLayout
                .getLayoutParams();
        params.height = PublicMethod.getPxInt(dip, getContext());
        tabTitleLayout.setLayoutParams(params);
    }

    public LinearLayout getTitleLayout() {
        return tabTitleLayout;
    }

    public int getViewPagerCurrentItem() {
        return currIndex;
    }

    /**
     * 初始化头标
     */
    private void initTitleView() {
        listTopViews = new ArrayList<TextView>();
        if (mTitleLayoutBgColor!=-100) {
			tabTitleLayout.setBackgroundColor(mTitleLayoutBgColor);
		}
        LayoutParams layoutParams=new LinearLayout.LayoutParams(2,  LayoutParams.FILL_PARENT);
        layoutParams.setMargins(0, 10, 0, 10);
        for (int i = 0; i < topViewName.length; i++) {
            mTopView = new TextView(getContext());
            mTopView.setGravity(Gravity.CENTER);
            mTopView.setText(topViewName[i]);
            mTopView.setTextColor(textNormalColor);//context.getResources().getColor(R.color.black));
            mTopView.setTextSize(textSize);
            mTopView.setId(i);
            mTopView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT, 1.0f));
            tabTitleLayout.addView(mTopView);
            if (mIsShowTitleSpaceLine&&i<topViewName.length-1) {
				View view=new View(getContext());
				view.setLayoutParams(layoutParams);
				view.setBackgroundColor(Color.parseColor("#f0f0f0"));
				view.setPadding(0, 10, 0, 10);
				tabTitleLayout.addView(view);
			}
            listTopViews.add(mTopView);
            mTopView.setOnClickListener(new MyOnClickListener(i));
        }
        listTopViews.get(0).setTextColor(textSelectColor);
    }

    /**
     * 设置标题
     * @param index
     * @param text
     */
    public void setTitleText(int index,String text){
        if(listTopViews != null){
            listTopViews.get(index).setText(text);
        }
    }

    /**
     * 初始化头标
     */
    public void initTextView(int index) {
        tabTitleLayout.removeAllViews();
        listTopViews = new ArrayList<TextView>();
        for (int i = 0; i < topViewName.length; i++) {
            TextView textView = null;
            View view = null;
            if (index == i) {
                view = inflate(getContext(),R.layout.buy_guess_no_read_msg,null);
                mNoReadMsgIcon = (ImageView) view.findViewById(R.id.ruyi_guess_no_read_msg_icon);
                textView = (TextView) view.findViewById(R.id.ruyi_guess_no_read_msg_title);
                view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT, 1.0f));
            } else {
                textView = new TextView(getContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT, 1.0f));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setText(topViewName[i]);
            textView.setTextColor(textNormalColor);//context.getResources().getColor(R.color.black));
            textView.setTextSize(textSize);
            textView.setId(i);

            if (index == i) {
                tabTitleLayout.addView(view);
            } else {
                tabTitleLayout.addView(textView);
            }
            listTopViews.add(textView);
            textView.setOnClickListener(new MyOnClickListener(i));
        }
        listTopViews.get(0).setTextColor(textSelectColor);
    }

    /**
     * 设置未读消息图标的显示状态
     *
     * @param visibility
     *            View.VISIBLE; View.GONE
     */
    public void setNoReadIconShowState(int visibility) {
        if (mNoReadMsgIcon != null) {
            mNoReadMsgIcon.setVisibility(visibility);
        }
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPage() {
    	if (mViewpagerBgColor==-100) {
    		viewPager.setBackgroundColor(Color.WHITE);
		}else {
			viewPager.setBackgroundColor(mViewpagerBgColor);
		}
        viewPager.setAdapter(new CustomFragmentAdapter(mFragmentManager,mFragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void setViewPageIndex(int id){
        viewPager.setCurrentItem(id);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        initialOffset = (screenW / topViewName.length - mCursorWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(initialOffset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    public ImageView getImageView() {
        return imageView;
    }

    /**
     * ViewPage适配器
     */
    public class CustomFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;

        public CustomFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            if(fragmentList == null)
                return  0;
            else
                return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            if (mListener != null) {
                mListener.setCurrentItem(index);
            }
            // 字体换色
            for (int i = 0; i < listTopViews.size(); i++) {
                if (i == index) {
                    listTopViews.get(i).setTextColor(textSelectColor);
                } else {
                    listTopViews.get(i)
                            .setTextColor(textNormalColor);//context.getResources().getColor(R.color.black));
                }
            }
        }
    };

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int offset = initialOffset * 2 + mCursorWidth;// 页卡1 -> 页卡2 偏移量

        @Override
        public void onPageSelected(int arg0) {
            // 字体换色
            for (int i = 0; i < listTopViews.size(); i++) {
                if (i == arg0) {
                    listTopViews.get(i).setTextColor(textSelectColor);
                } else {
                    listTopViews.get(i)
                            .setTextColor(textNormalColor);//context.getResources().getColor(R.color.black));
                }
            }
            if(imageView.getVisibility() == VISIBLE) {
                Animation animation = null;
                if (currIndex == 0) {
                    animation = new TranslateAnimation(initialOffset, offset * arg0, 0, 0);
                } else {
                    animation = new TranslateAnimation(offset * currIndex, offset * arg0, 0, 0);
                }

                currIndex = arg0;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                imageView.startAnimation(animation);
            }
            if (mListener != null) {
                mListener.slidingViewPageChange(arg0);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}