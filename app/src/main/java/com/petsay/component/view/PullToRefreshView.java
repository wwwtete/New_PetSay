package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.petsay.R;

/**
 * 下拉刷新/上拉加载更多组件
 * @author wangw
 *
 */
public class PullToRefreshView extends LinearLayout {
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	/**
	 * last y
	 */
	private int mLastMotionY;
	/**
	 * lock
	 */
	//	private boolean mLock;
	/**
	 * header view
	 */
	private RefreshHeadView mHeadView;
	/**
	 * footer view
	 */
	private View mFooterView;
	/**
	 * list or grid
	 */
	private AdapterView<?> mAdapterView;
	/**
	 * scrollview
	 */
	private ScrollView mScrollView;
	/**
	 * footer view height
	 */
	private int mFooterViewHeight;
	/**
	 * footer view image
	 */
	private ImageView mFooterImageView;
	/**
	 * footer tip text
	 */
	private TextView mFooterTextView;
	/**
	 * footer refresh createTime
	 */
	private TextView mFooterUpdateTextView;
	/**
	 * footer progress bar
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * layout inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * header view current state
	 */
	private int mHeaderState;
	/**
	 * footer view current state
	 */
	private int mFooterState;
	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */
	private int mPullState;
	/**
	 * 变为向下的箭头,改变箭头方向
	 */
	private RotateAnimation mFlipAnimation;
	/**
	 * footer refresh listener
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * footer refresh listener
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	private boolean mPullDownEnable; // 下拉刷新是否可以，默认不可用
	private boolean mPullUpEnable; // 上拉刷新是否可用，默认不可用
	private int mBottomMargin=0;
	private int mTopMargin = 0;
	private int mHeaderViewHeight;
	private int mPreTopMargin = 0;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	/**
	 * 设置下拉刷新是否可用
	 *
	 * @param flage
	 */
	public void setPullDownRefreshEnable(boolean flage) {
		this.mPullDownEnable = flage;
	}

	/**
	 * 设置上拉更新功能是否可用
	 *
	 * @param flage
	 */
	public void setPullUpRefreshEnable(boolean flage) {
//		mFooterView.setVisibility(View.VISIBLE);
		this.mPullUpEnable = flage;
	}

	/**
	 * init
	 *
	 */
	private void init() {
		//需要设置成vertical
		setOrientation(LinearLayout.VERTICAL);
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mInflater = LayoutInflater.from(getContext());
		// header view 在此添加,保证是第一个添加到linearlayout的最上端
		addHeaderView();
	}

	private void addHeaderView() {
		mHeadView = new RefreshHeadView(getContext());
		measureView(mHeadView);
		mHeaderViewHeight = mHeadView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		addView(mHeadView,params);

	}

	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
		mFooterView.setVisibility(View.GONE);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有值了,不再是0;
		// getHeight()什么时候会赋值,稍候再研究一下
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后,并隐藏
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的最后
		addFooterView();
		initContentAdapterView();
	}

	/**
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 *
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException(
					"This layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException(
					"此布局中必须包含一个AdapterView或ScrollView");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
	 * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//		if (mLock) {
		//			return true;
		//		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent已经记录
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE && mPullDownEnable) {//执行下拉
                if(mHeaderState == REFRESHING){
                    setHeaderViewTopMargin(deltaY);
                }else
				headerPrepareToRefresh(deltaY);
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (mPullState == PULL_UP_STATE && mPullUpEnable) {//执行上拉
				if(mFooterView.getVisibility() == View.GONE)
					mFooterView.setVisibility(View.VISIBLE);
				    footerPrepareToRefresh(deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
            if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
                break;
            }
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE && mPullDownEnable) {
				if (topMargin >= 0) {
					// 开始刷新
					headerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}else if (mPullState == PULL_UP_STATE && mPullUpEnable){
				if (Math.abs(topMargin) >= mHeaderViewHeight
						+ mFooterViewHeight) {
					// 开始执行footer 刷新
					footerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
                    mFooterView.setVisibility(GONE);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 *
	 * @param deltaY
	 *            , deltaY > 0 是向下运动,< 0是向上运动
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
        boolean flag = checkRefreshViewScroll(deltaY);
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
            if(mHeaderState == REFRESHING){
                if(Math.abs(getHeaderTopMargin()) >= mHeaderViewHeight && !flag)
                    return false;
                else
                    return true;
            }
			return false;
		}

        if(flag){
            if(deltaY > 0){
                mPullState = PULL_DOWN_STATE;
            }else {
                mPullState = PULL_UP_STATE;
            }
        }
        return flag;

		//对于ListView和GridView
//		if (mAdapterView != null) {
//			// 子view(ListView or GridView)滑动到最顶端
//			if (deltaY > 0) {
//
//				View child = mAdapterView.getChildAt(0);
//				if (child == null) {
//					// 如果mAdapterView中没有数据,则拦截进行刷新
//					return true;
//				}
//				if (mAdapterView.getFirstVisiblePosition() == 0
//						&& child.getTop() == 0) {
//					mPullState = PULL_DOWN_STATE;
//					return true;
//				}
//				int top = child.getTop();
//				int padding = mAdapterView.getPaddingTop();
//				if (mAdapterView.getFirstVisiblePosition() == 0
//						&& Math.abs(top - padding) <= 8) {//这里之前用3可以判断,但现在不行,还没找到原因
//					mPullState = PULL_DOWN_STATE;
//					return true;
//				}
//
//			} else if (deltaY < 0) {
//				View lastChild = mAdapterView.getChildAt(mAdapterView
//						.getChildCount() - 1);
//				if (lastChild == null) {
//					// 如果mAdapterView中没有数据,不拦截
//					return false;
//				}
//				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
//				// 等于父View的高度说明mAdapterView已经滑动到最后
//				if (lastChild.getBottom() <= getHeight()
//						&& mAdapterView.getLastVisiblePosition() == mAdapterView
//						.getCount() - 1) {
//					mPullState = PULL_UP_STATE;
//					return true;
//				}
//			}
//		}
//		// 对于ScrollView
//		if (mScrollView != null) {
//			// 子scroll view滑动到最顶端
//			View child = mScrollView.getChildAt(0);
//			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
//				mPullState = PULL_DOWN_STATE;
//				return true;
//			} else if (deltaY < 0
//					&& child.getMeasuredHeight() <= getHeight()
//					+ mScrollView.getScrollY()) {
//				mPullState = PULL_UP_STATE;
//				return true;
//			}
//		}
//		return false;
	}

    private boolean checkRefreshViewScroll(int deltaY){
        if (mAdapterView != null) {
            // 子view(ListView or GridView)滑动到最顶端
            if (deltaY > 0) {

                View child = mAdapterView.getChildAt(0);
                if (child == null) {
                    // 如果mAdapterView中没有数据,则拦截进行刷新
                    return true;
                }
                if (mAdapterView.getFirstVisiblePosition() == 0
                        && child.getTop() == 0) {
//                    mPullState = PULL_DOWN_STATE;
                    return true;
                }
                int top = child.getTop();
                int padding = mAdapterView.getPaddingTop();
                if (mAdapterView.getFirstVisiblePosition() == 0
                        && Math.abs(top - padding) <= 8) {//这里之前用3可以判断,但现在不行,还没找到原因
//                    mPullState = PULL_DOWN_STATE;
                    return true;
                }

            } else if (deltaY < 0) {
                View lastChild = mAdapterView.getChildAt(mAdapterView
                        .getChildCount() - 1);
                if (lastChild == null) {
                    // 如果mAdapterView中没有数据,不拦截
                    return false;
                }
                // 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
                // 等于父View的高度说明mAdapterView已经滑动到最后
                if (lastChild.getBottom() <= getHeight()
                        && mAdapterView.getLastVisiblePosition() == mAdapterView
                        .getCount() - 1) {
//                    mPullState = PULL_UP_STATE;
                    return true;
                }
            }
        }
        // 对于ScrollView
        if (mScrollView != null) {
            // 子scroll view滑动到最顶端
            View child = mScrollView.getChildAt(0);
            if (deltaY > 0 && mScrollView.getScrollY() == 0) {
//                mPullState = PULL_DOWN_STATE;
                return true;
            } else if (deltaY < 0
                    && child.getMeasuredHeight() <= getHeight()
                    + mScrollView.getScrollY()) {
//                mPullState = PULL_UP_STATE;
                return true;
            }
        }
        return false;
    }

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 *
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
			mHeaderState = PULL_TO_REFRESH;
		}
		if(newTopMargin >= 0){
			int temp = newTopMargin - mPreTopMargin;
			if(temp > 0){
				mHeadView.nextFrame();
			}else if(temp < 0){
				mHeadView.preFrame();
			}
		}
		mPreTopMargin = newTopMargin;
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 *
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterTextView.setVisibility(View.VISIBLE);
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView
			.setText(R.string.listview_footer_hint_ready);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText(R.string.listview_footer_up_pull);
			mFooterState = PULL_TO_REFRESH;
		}
	}

	/**
	 * 修改Header view top margin的值
	 *
	 * @param deltaY
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();//mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.4f;
		//这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了
		//表示如果是在上拉后一段距离,然后直接下拉
		if(deltaY>0&&mPullState == PULL_UP_STATE&&Math.abs(params.topMargin) <= mHeaderViewHeight){
			return params.topMargin;
		}
		//同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if(deltaY<0&&mPullState == PULL_DOWN_STATE&&Math.abs(params.topMargin)>=mHeaderViewHeight){
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeadView.setLayoutParams(params);
		invalidate();

		return params.topMargin;
	}

    /**
     * 手动设置HeaderView距离顶部的距离(无延迟)
     * @param deltaY
     * @return
     */
    private int setHeaderViewTopMargin(int deltaY){
        LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();//mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY;
        if(mPullState == PULL_DOWN_STATE) {
            if (newTopMargin > 0) {
                newTopMargin = 0;
            } else if (Math.abs(newTopMargin) > mHeaderViewHeight) {
                newTopMargin = -mHeaderViewHeight;
            }
        }else {
            if (newTopMargin > 0) {
                newTopMargin = 0;
            } else if (Math.abs(newTopMargin) > mFooterViewHeight) {
                newTopMargin = -mFooterViewHeight;
            }
        }
        params.topMargin = (int) newTopMargin;
        mHeadView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }

	/**
	 * 显示头部刷新动画
	 * 但不触发回调通知
	 */
	public void showHeaderAnimation(){
		if(mHeaderState != REFRESHING){
			mHeaderState = REFRESHING;
            mPullState = PULL_DOWN_STATE;
			setHeaderTopMargin(mTopMargin);
			mHeadView.playGif();
		}
	}

	/**
	 * header refreshing
	 *
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(mTopMargin);
		mHeadView.playGif();
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	/**
	 * footer refreshing
	 *
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top+mBottomMargin);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
		.setText(R.string.ruyi_guess_loading);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	public void setBottomMargin(int margin){
		mBottomMargin = margin;
	}

    /**
     * 设置header view 的topMargin的值
     *
     * @param margin
     */
	public void setTopMargin(int margin){
		mTopMargin = margin;
	}

	/**
	 * 设置header view 的topMargin的值
	 *
	 * @param topMargin
	 *            ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();//mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeadView.setLayoutParams(params);
		invalidate();
	}

    /***
     * 刷新或加载更多完毕
     * @param isMore  是否执行加载更多操作
     */
    public void onComplete(boolean isMore){
        if(isMore){
            onFooterRefreshComplete();
        }else{
            onHeaderRefreshComplete();
        }
    }

	/**
	 * header view 完成更新后恢复初始状态
	 *
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mPreTopMargin = 0;
		mHeadView.stopGif();
		mHeadView.reset();
		mHeaderState = PULL_TO_REFRESH;
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 *
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.arrow_up);
		mFooterTextView.setText(R.string.listview_footer_up_pull);
		mFooterProgressBar.setVisibility(View.GONE);
		mFooterView.setVisibility(View.GONE);
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * Set a text to represent when the list was last updated.
	 *
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			//			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			//			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			//			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取当前header view 的topMargin
	 *
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * set headerRefreshListener
	 *
	 * @param headerRefreshListener
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		setPullDownRefreshEnable(headerRefreshListener != null);
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		setPullUpRefreshEnable(footerRefreshListener != null);
		mOnFooterRefreshListener = footerRefreshListener;
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid footer
	 * view should be refreshed.
	 */
	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid header
	 * view should be refreshed.
	 */
	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}
}
