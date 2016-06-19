package com.petsay.component.gifview;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * 还有Gif动画的ListView滚动事件
 */
public class GifListScrollListener implements OnScrollListener {

	private int mMaxWidth;
	private ListView mlistView;
	private GifViewManager mGifManager;
    //仅仅用于展示，不播放
    private boolean mOnlyFirstFrame;

	private int mTitleHeight; //图片距离顶部的高度也就是标题的高度
	private AudioGifView mGifView;
	public GifListScrollListener(ListView listView,int titleHeight){
	    this(listView,titleHeight,false);
	}

    public GifListScrollListener(ListView listView,int titleHeight,boolean onlyFirstFrame){
        this.mlistView = listView;
        this.mTitleHeight = titleHeight;
        mGifManager = GifViewManager.getInstance();
        mOnlyFirstFrame = onlyFirstFrame;
    }
	
	/**
	 * 设置图片距离顶部的距离
	 */
	public void setTitleHeight(int titleHeight){
		this.mTitleHeight = titleHeight;
	}
	
	public void setListAdapter(BaseAdapter adapter){
		stop();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		super.onScrollStateChanged(view, scrollState);
		if(OnScrollListener.SCROLL_STATE_IDLE == scrollState && mlistView.getChildCount() > 0) {
			if(mGifManager.getAllowAutoPlay() || mOnlyFirstFrame){
				compareView( view.getChildAt(0), view.getChildAt(1));
			}
            if(!mGifManager.getAllowAutoPlay()){
				mGifManager.pauseGif(true);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	
	/**
	 * 根据位置播放Gif
	 * @param position
	 */
	public void playGifByPosition(int position){
		if(position >= 0 && position < mlistView.getChildCount() && mGifManager.getAllowAutoPlay()){
			View child = mlistView.getChildAt(position);
			if(child != null){
				if(mlistView.getHeaderViewsCount() > 0){
					int top = Math.abs(mlistView.getMeasuredHeight() - child.getTop());
					if(top < mlistView.getMeasuredWidth()/3*2)
						return;
				}
				playGif(getGifView(child));
			}
		}
	}

	/**
	 * 比较两个View，播放那个View
	 * @param v1
	 * @param v2
	 */
	private void compareView(View v1,View v2){

		int v1_top = -1;
		int v2_top = -1;
		boolean v1_flag = false;
		boolean v2_flag = false;
		//TODO 真实的值是从外部传入的，临时测试使用
//		mTitleHeight = PublicMethod.getDiptopx(mlistView.getContext(), 60);
		mMaxWidth = mlistView.getMeasuredWidth();
		int headerCount = mlistView.getHeaderViewsCount();
		int firstIndex = mlistView.getFirstVisiblePosition();
		if(headerCount > 0 && firstIndex < headerCount){
			v1 = null;
		}
		
		if(v1 != null){
			v1_top = Math.abs(v1.getTop()) - mTitleHeight;
			v1_flag =v1_top < mMaxWidth/3; 
		}

		if(v2 != null && !v1_flag){
			v2_top = Math.abs(mlistView.getMeasuredHeight() - v2.getTop()) - mTitleHeight;
			v2_flag = v2_top > (mMaxWidth/3*2);
		}
		int position  = -1;
		View view = null;
		if(v1_flag){
			view = v1;
			position = mlistView.getFirstVisiblePosition();
		}else if(v2_flag){
			view = v2;
			position = mlistView.getFirstVisiblePosition() + 1;
		}
		PublicMethod.log_d("v1_top === " + v1_top + "   |   v2_top === " + v2_top + "  |  position === " + position);

		if(position >= 0 && view != null) {
            if(mOnlyFirstFrame)
                showFirstFrame(getGifView(view));
            else
                playGif(getGifView(view));
        }
		
		if(!v1_flag && !v2_flag) {
			stop();
		}
	}

	/**
	 * 播放gif
	 */
	private void playGif(AudioGifView gifView){
        if(!verify(gifView)) {
            mGifManager.pauseGif(true);
            return;
        }
		if(gifView != mGifView || !mGifView.isPlaying())
			mGifManager.playGif(gifView);
		mGifView = gifView;
	}

    private void showFirstFrame(AudioGifView gifView){
        if(!verify(gifView))
            return;
        mGifManager.pauseGif(true);
        gifView.showFirstFrame();
        mGifView = gifView;
    }

    private boolean verify(AudioGifView gifView){
        return gifView != null && gifView.getData() != null && gifView.getData().isAudioModel();
    }

    public AudioGifView getGifView(View view){
        View child =  view.findViewById(R.id.am_gif);
        if(child instanceof AudioGifView)
            return (AudioGifView) child;
        return  null;
    }
	
	private void stop(){
		mGifManager.stopGif();
	}
	
	/**
	 * 释放内存
	 */
	public void release(){
		mGifManager.stopGif();
	}
	
	
}
