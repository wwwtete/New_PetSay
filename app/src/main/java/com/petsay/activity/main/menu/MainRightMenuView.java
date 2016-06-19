package com.petsay.activity.main.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.award.MyTaskActivity;
import com.petsay.activity.global.SearchActivity;
import com.petsay.activity.petalk.ReviewHotSayActivity;
import com.petsay.activity.settings.SettingActivity;
import com.petsay.activity.shop.ShopActivity;
import com.petsay.activity.topic.TopicListActivity;
import com.petsay.activity.user.GiftBagActivity;
import com.petsay.activity.user.UserCenterActivity;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.slidingmenu.SlidingMenuItemModel;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/13
 * @Description 主页右侧菜单
 */
public class MainRightMenuView extends BasicMainMenuView implements View.OnClickListener, AdapterView.OnItemClickListener {

    private CircleImageView mImg_header;
    private TextView mTv_name;
    private TextView mTv_settings;
    private TextView mTv_search;
    private ListView mLv_menu;
    private LinearLayout mLlHeader;


    public MainRightMenuView(Context context) {
        super(context);
    }

    public MainRightMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        inflate(getContext(), R.layout.rightmenu_view, this);
        bindViews();
        initMenuItem();
        mLv_menu.setAdapter(mAdapter);
        setListener();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if(params != null){
            params.width = (int)getResources().getDimension(R.dimen.right_menu_width);
        }
        super.setLayoutParams(params);
    }

    private void initMenuItem() {
        List<SlidingMenuItemModel> items = new ArrayList<SlidingMenuItemModel>(5);
        items.add(new SlidingMenuItemModel(R.drawable.right_menu_shop,R.string.menu_shop,this));
//        items.add(new SlidingMenuItemModel(R.drawable.right_review_newsay_icon,R.string.menu_reviewhot,this));
        items.add(new SlidingMenuItemModel(R.drawable.right_menu_giftbag_icon,R.string.menu_giftbag,this));
        items.add(new SlidingMenuItemModel(R.drawable.right_menu_forum,R.string.menu_forum,this));
        items.add(new SlidingMenuItemModel(R.drawable.right_menu_task,R.string.menu_task,this));
        mAdapter.refreshData(items);
    }

    private void bindViews() {
        mLlHeader = (LinearLayout) findViewById(R.id.ll_header);
        mImg_header = (com.petsay.component.view.CircleImageView) findViewById(R.id.img_header);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_settings = (TextView) findViewById(R.id.tv_settings);
        mTv_search = (TextView) findViewById(R.id.tv_search);
        mLv_menu = (ListView) findViewById(R.id.lv_menu);
    }


    private void setListener() {
        mLlHeader.setOnClickListener(this);
        mTv_search.setOnClickListener(this);
        mTv_settings.setOnClickListener(this);
        mLv_menu.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        if(isLogin() && getActivePetInfo() != null){
            mTv_name.setText(getActivePetInfo().getNickName());
            ImageLoaderHelp.displayHeaderImage(getActivePetInfo().getHeadPortrait(),mImg_header);
        }else {
            mTv_name.setText("未登录");
            mImg_header.setImageResource(R.drawable.placeholderhead);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_header:
                if(!checkJumpLogin()){
                    jumpActivity(UserCenterActivity.class);
                }
                break;
            case R.id.tv_search:
                jumpActivity(SearchActivity.class);
                break;
            case R.id.tv_settings:
                jumpActivity(SettingActivity.class);
                break;
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SlidingMenuItemModel model = mAdapter.getItem(position);
        if(model != null && model.clickItemCallback != null){
            model.clickItemCallback.onClickItem(model);
        }
    }

    @Override
    public void onClickItem(SlidingMenuItemModel model) {
        if(model == null)
            return;
        switch (model.contentResId){
            case R.string.menu_forum:
                jumpActivity(TopicListActivity.class);
                break;
            case R.string.menu_giftbag:
                if(!checkJumpLogin())
                    jumpActivity(GiftBagActivity.class);
                break;
            case R.string.menu_reviewhot:
                if(!checkJumpLogin())
                    jumpActivity(ReviewHotSayActivity.class);
                break;
            case R.string.menu_shop:
                if(!checkJumpLogin()) {
                    jumpActivity(ShopActivity.class);
                }
                break;
            case R.string.menu_task:
                if(!checkJumpLogin())
                    jumpActivity(MyTaskActivity.class);
                break;
        }
    }



}
