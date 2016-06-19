package com.petsay.component.view.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.petsay.R;
import com.petsay.utile.PublicMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/7
 * @Description 滑动菜单抽象类不能直接实例化，需由子类来实现
 */
public abstract class SlidingMenu extends FrameLayout {

    /**左侧方向*/
    public static final int DIRECTION_LEFT = 0;
    /**右侧方向*/
    public static final int DIRECTION_RIGHT = 1;
    private static final int MOTION_MOVE_HORIZONTAL = 2;
    private static final int MOTION_DOWN = 3;
    private static final int MOTION_UP = 4;
    private static final int MOTION_MOVE_VERTICAL = 5;
    //    private ImageView mIvBg;
    private ImageView mIvShadow;

    protected View mViewLeftMenu;
    protected View mViewRightMenu;

    private ActivityRootView mViewActivity;
    private ViewGroup mDecorView;
    private Activity mActivity;
    private View mCurrentMenu;
    private SlidingMenuCallback mMenuCallback;


    private List<View> mIgnoreViewList;
    private List<Integer> mDisabledSliding = new ArrayList<Integer>(2);

    //缩放比例
    private float mScale = 0.6f;
    //移动距离
    private float mTranslationX;
    private float mLeftTranslationX;
    private float mRightTranslationX;
    //阴影的缩放比例
    private float mShadowScaleX;
    private float mShadowScaleY;
    private int mAnimDuration = 250;
    private boolean mIsOpened;
    protected int mSlidingDirection;
    private float mLastRawX;
    private float mLastDownX;
    private float mLastDownY;
    private boolean mIsInIgnoreView;
    private int mMotionState;

    protected SlidingMenu(Context context) {
        super(context);
        initView();
    }

    protected SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 由子类实现并初始化左侧侧滑菜单的View
     * @return
     */
    protected abstract View getLeftMenuView();

    /**
     * 由子类实现并初始化右侧侧滑菜单View
     * @return
     */
    protected abstract View getRightMenuView();

    protected void initView() {
        inflate(getContext(), R.layout.slidingmenu, this);
        bindViews();
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        // Applies the content insets to the view's padding, consuming that content (modifying the insets to be 0),
        // and returning true. This behavior is off by default and can be enabled through setFitsSystemWindows(boolean)
        // in api14+ devices.
        this.setPadding(mViewActivity.getPaddingLeft() + insets.left, mViewActivity.getPaddingTop() + insets.top,
                mViewActivity.getPaddingRight() + insets.right, mViewActivity.getPaddingBottom() + insets.bottom);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }

    private void bindViews() {
//        mIvBg = (ImageView) findViewById(R.id.iv_bg);
        mIvShadow = (ImageView) findViewById(R.id.iv_shadow);
    }
    public void attachToActivity(Activity activity){
        initValue(activity);
        setShadowScaleValueByOrientation();
        mDecorView.addView(this, 0);
    }

    public void onResume(){
    }

    protected void initValue(Activity activity) {

        mViewLeftMenu = getLeftMenuView();
        mViewRightMenu = getRightMenuView();

        if(mViewLeftMenu == null || mViewRightMenu == null){
            throw new NullPointerException("侧滑菜单View不能为空");
        }
        mActivity = activity;

//        mTranslationX = PublicMethod.getDisplayWidth(getContext())/2; //PublicMethod.getDiptopx(getContext(),150);
        hidenAllMenuView();
        addView(mViewLeftMenu,getMenuLayoutParam(DIRECTION_LEFT));
        addView(mViewRightMenu,getMenuLayoutParam(DIRECTION_RIGHT));

        mIgnoreViewList = new ArrayList<View>();


        mViewActivity = new ActivityRootView(getContext());
        mDecorView = (ViewGroup) activity.getWindow().getDecorView();

        View content = mDecorView.getChildAt(0);
        mDecorView.removeViewAt(0);
        mViewActivity.setContent(content);
        addView(mViewActivity);
    }

    private LayoutParams getMenuLayoutParam(int direction){
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(direction == DIRECTION_RIGHT){
            params.gravity = Gravity.RIGHT;// | Gravity.CENTER_VERTICAL;
        }else {
            params.gravity = Gravity.LEFT;// | Gravity.CENTER_VERTICAL;
        }
        return params;
    }

    /**
     * 设置菜单Gravity
     * @param direction 那一边的侧滑菜单
     * @param gravity Gravity.TOP或Gravity.CENTER_VERTICAL
     */
    public void setMenuGravity(int direction,int gravity){
        if(direction == DIRECTION_LEFT){
            if(mViewLeftMenu != null){
                LayoutParams params = (LayoutParams) mViewLeftMenu.getLayoutParams();
                params.gravity = Gravity.LEFT | direction;
            }
        }else {
            if(mViewRightMenu != null){
                LayoutParams params = (LayoutParams) mViewRightMenu.getLayoutParams();
                params.gravity = Gravity.RIGHT | gravity;
            }
        }
    }

    /**
     * 根据屏幕的方向设置阴影缩放比例值
     */
    private void setShadowScaleValueByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            mShadowScaleX = 0.034f;
            mShadowScaleY = 0.12f;
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            mShadowScaleX = 0.06f;
            mShadowScaleY = 0.07f;
        }
    }

    /**
     * 显示Menu
     * @param direction 0：左侧 1：右侧
     */
    public void openMenu(int direction){
        setSlidingDirection(direction);
        mIsOpened = true;
        AnimatorSet set_activity = buildOpenAnimation(mViewActivity, mScale, mTranslationX);
        AnimatorSet set_shadow = buildOpenAnimation(mIvShadow, mScale + mShadowScaleX, mTranslationX);
        AnimatorSet set_menu = buildAlphaAnimation(mCurrentMenu,1.0f);
        set_shadow.addListener(mAnimListtener);
        set_activity.playTogether(set_shadow);
        set_activity.playTogether(set_menu);
        set_activity.start();
    }

    /**
     * 关闭Menu
     */
    public void closeMenu(){
        mIsOpened = false;
        AnimatorSet set_activity = buildCloseAnimation(mViewActivity, 1.0f, 0.0f);
        AnimatorSet set_shadow = buildCloseAnimation(mIvShadow, 1.0f, 0.0f);
        AnimatorSet set_menu = buildAlphaAnimation(mCurrentMenu,0.0f);
        set_shadow.addListener(mAnimListtener);
        set_activity.playTogether(set_shadow,set_menu);
        set_activity.start();
    }

    /**
     * Menu是否打开
     * @return
     */
    public boolean isOpened(){
        return mIsOpened;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivitScaleX = ViewHelper.getScaleX(mViewActivity);
        if(currentActivitScaleX == 1.0f && (mMotionState == MOTION_DOWN || mMotionState == MOTION_UP))
            setSlidingDirectionByRawX(ev.getRawX());

        switch (ev.getAction()){
            case  MotionEvent.ACTION_DOWN:
                mLastDownX = ev.getX();
                mLastDownY = ev.getY();
                mIsInIgnoreView = isInIgnoredView(ev) && !isOpened();
                mMotionState = MOTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                if((mIsInIgnoreView || isDisabledSlidingDirection(mSlidingDirection)) && !isOpened())
                    break;
                if(mMotionState != MOTION_DOWN && mMotionState != MOTION_MOVE_HORIZONTAL)
                    break;
                if(mMotionState == MOTION_DOWN){
                    int offsetY = (int) (ev.getY() - mLastDownY);
                    int offsetX = (int) (ev.getX() - mLastDownX);
                    if(offsetY > 25 || offsetY < -25){
                        mMotionState = MOTION_MOVE_VERTICAL;
                        break;
                    }

                    if(offsetX < -50 || offsetX > 50){
                        mMotionState = MOTION_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }else if(mMotionState == MOTION_MOVE_HORIZONTAL){
                    if(currentActivitScaleX < 0.95 && mCurrentMenu != null){
//                        showMenuView(mCurrentMenu);
                        mCurrentMenu.setVisibility(VISIBLE);
                    }
                    float targetScale = getTargetScale(ev.getRawX());
                    float translationX = getTranslationX(ev.getRawX(),targetScale);
                    ViewHelper.setScaleX(mViewActivity, targetScale);
                    ViewHelper.setScaleY(mViewActivity, targetScale);
                    ViewHelper.setScaleX(mIvShadow, targetScale + mShadowScaleX);
                    ViewHelper.setScaleY(mIvShadow, targetScale + mShadowScaleY);
                    ViewHelper.setTranslationX(mViewActivity, translationX);
                    ViewHelper.setTranslationX(mIvShadow, translationX);
                    ViewHelper.setAlpha(mCurrentMenu, (1 - targetScale) * 2.0f);
                    mLastRawX = ev.getRawX();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mIsInIgnoreView)
                    break;
                if(mMotionState != MOTION_MOVE_HORIZONTAL)
                    break;
                mMotionState = MOTION_UP;
//                if(isOpened()){
//                    if(currentActivitScaleX > 0.56f)
//                        closeMenu();
//                    else
//                        openMenu(mSlidingDirection);
//                }else {
//                    if(currentActivitScaleX < 0.94f){
//                        openMenu(mSlidingDirection);
//                    }else {
//                        closeMenu();
//                    }
//                }
                if(Math.abs(mViewActivity.getX()) > Math.abs(mTranslationX)/2){
                    openMenu(mSlidingDirection);
                }else {
                    closeMenu();
                }
                break;
        }
        mLastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    private float getTargetScale(float rawX) {
        float scaleX = (rawX - mLastRawX)/PublicMethod.getDisplayWidth(getContext()) * 0.75f;
        scaleX = mSlidingDirection == DIRECTION_RIGHT ? - scaleX : scaleX;
        float targetScale = ViewHelper.getScaleX(mViewActivity) - scaleX;
        targetScale = targetScale > 1.0f ? 1.0f:targetScale;
        targetScale = targetScale < mScale ? mScale:targetScale;
        return targetScale;
    }

    private float getTranslationX(float rawX,float currentActivitScaleX) {
        float translationX = (rawX - mLastRawX)/mTranslationX*1.5f;
        float tx = mViewActivity.getX() + mTranslationX* translationX;
        if(Math.abs(tx) > Math.abs(mTranslationX) || currentActivitScaleX == 1.0f) {
            tx = currentActivitScaleX == 1.0f ? 0 : mTranslationX;
        }
        return tx;
    }

    private void setSlidingDirectionByRawX(float rawX) {
        if(rawX < mLastRawX){
            setSlidingDirection(DIRECTION_RIGHT);
        }else {
            setSlidingDirection(DIRECTION_LEFT);
        }
    }

    /**
     * 构建显示Menu动画
     * @param target
     * @param targetScale
     * @param targetTranslationX
     * @return
     */
    private AnimatorSet buildOpenAnimation(View target,float targetScale,float targetTranslationX){
        AnimatorSet anim = new AnimatorSet();
        anim.playTogether(
                ObjectAnimator.ofFloat(target,"scaleX",targetScale),
                ObjectAnimator.ofFloat(target,"scaleY",targetScale),
                ObjectAnimator.ofFloat(target,"translationX",targetTranslationX)
        );
        anim.setInterpolator(AnimationUtils.loadInterpolator(getContext(),android.R.anim.decelerate_interpolator));
        anim.setDuration(mAnimDuration);
        return anim;
    }

    /**
     * 构建关闭Menu动画
     * @param target
     * @param targetScale
     * @param targetTranslationX
     * @return
     */
    private AnimatorSet buildCloseAnimation(View target,float targetScale,float targetTranslationX){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(target,"scaleX",targetScale),
                ObjectAnimator.ofFloat(target,"scaleY",targetScale),
                ObjectAnimator.ofFloat(target,"translationX",targetTranslationX)
        );
        set.setDuration(mAnimDuration);
        return set;
    }

    private AnimatorSet buildAlphaAnimation(View target,float alpha){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(target,"alpha",alpha));
        set.setDuration(mAnimDuration);
        return set;
    }


    private OnClickListener mClickActivityListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isOpened()){
                closeMenu();
            }
        }
    };

    private Animator.AnimatorListener mAnimListtener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if(isOpened()){
                showMenuView(mCurrentMenu);
                onOpenMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(isOpened()){
                mViewActivity.setInterceptTouch(true);
                mViewActivity.setOnClickListener(mClickActivityListener);
            }else {
                mViewActivity.setInterceptTouch(false);
                mViewActivity.setOnClickListener(null);
                hidenAllMenuView();
                onCloseMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    protected void onOpenMenu(){
        if(mMenuCallback != null){
            mMenuCallback.onOpenMenu(mSlidingDirection);
        }
    }

    protected void onCloseMenu(){
        if(mMenuCallback != null){
            mMenuCallback.onCloseMenu(mSlidingDirection);
        }
    }

    /**
     * 是否为禁用Touch滑动显示的方向
     * @param direction 0:左侧 1：右侧
     * @return
     */
    public boolean isDisabledSlidingDirection(int direction){
        return mDisabledSliding.contains(direction);
    }

    /**
     * 当前的Touc位置是否为忽略的View范围
     * @param event
     * @return
     */
    private boolean isInIgnoredView(MotionEvent event){
        if(mIgnoreViewList == null || mIgnoreViewList.isEmpty()){
            return false;
        }
        Rect rect = new Rect();
        for (View view:mIgnoreViewList){
            view.getGlobalVisibleRect(rect);
            if(rect.contains((int)event.getX(),(int)event.getY()));
            return true;
        }
        return false;

    }

    private void showMenuView(View currentMenu) {
        if(currentMenu != null)
            currentMenu.setVisibility(VISIBLE);
    }

    private void hidenAllMenuView(){
        if(mViewLeftMenu != null)
            mViewLeftMenu.setVisibility(GONE);
        if(mViewRightMenu != null)
            mViewRightMenu.setVisibility(GONE);
    }

    public void setMenuCallback(SlidingMenuCallback callback){
        this.mMenuCallback = callback;
    }

    public SlidingMenuCallback getMenuCallback(){
        return mMenuCallback;
    }

    /**
     * 设置背景
     * @param imageResource
     */
    public void setBackground(int imageResource){
//        mIvBg.setImageResource(imageResource);
        this.setBackgroundResource(imageResource);
    }

    /**
     * 设置是否显示阴影
     *
     * @param isVisible
     */
    public void setShadowVisible(boolean isVisible){
        if (isVisible) {
            mIvShadow.setVisibility(VISIBLE);
            mIvShadow.setBackgroundResource(R.drawable.shadow);
        }else {
            mIvShadow.setBackgroundResource(0);
            mIvShadow.setVisibility(GONE);
        }
    }

    /**
     * 添加忽略Touch事件的View
     * @param v
     */
    public void addIgnoredView(View v){
        mIgnoreViewList.add(v);
    }

    public void removeIgnoredView(View v){
        mIgnoreViewList.remove(v);
    }

    public void clearIgnoredViewList(){
        mIgnoreViewList.clear();
    }

    /**
     * 设置禁用Touch滑动的方向
     * @param direction 0：左侧 1：右侧
     */
    public void setDisabledSlidingDirection(int direction){
        mDisabledSliding.add(direction);
    }

    public void setAnimDuration(int duration){
        this.mAnimDuration = duration;
    }

    /**
     * 设置缩放比例
     * @param scale
     */
    public void setScaleValue(float scale){
        this.mScale = scale;
    }

    public float getmLeftTranslationX() {
        return mLeftTranslationX;
    }

    /**
     * 设置左侧的移动距离
     * @param mLeftTranslationX
     */
    public void setLeftTranslationX(float mLeftTranslationX) {
        this.mLeftTranslationX = mLeftTranslationX;
    }

    public float getmRightTranslationX() {
        return mRightTranslationX;
    }

    /**
     * 设置右侧的移动距离
     * @param mRightTranslationX
     */
    public void setRightTranslationX(float mRightTranslationX) {
        this.mRightTranslationX = mRightTranslationX;
    }

    private void setSlidingDirection(int slidingDirection) {
        int screenWidth = PublicMethod.getDisplayWidth(getContext());
        float pivotX = screenWidth * 0.5f;
        float pivotY = PublicMethod.getDisplayHeight(getContext()) * 0.5f;

        if(slidingDirection == DIRECTION_LEFT){
            mCurrentMenu = mViewLeftMenu;
            mTranslationX = Math.abs(mLeftTranslationX);
        }else {
            mCurrentMenu = mViewRightMenu;
            mTranslationX = -Math.abs(mRightTranslationX);
        }

        ViewHelper.setPivotX(mViewActivity,pivotX);
        ViewHelper.setPivotY(mViewActivity,pivotY);
        ViewHelper.setPivotX(mIvShadow,pivotX);
        ViewHelper.setPivotY(mIvShadow,pivotY);
        this.mSlidingDirection = slidingDirection;
    }


    public interface SlidingMenuCallback{
        public void onOpenMenu(int direction);
        public void onCloseMenu(int direction);
    }

}
