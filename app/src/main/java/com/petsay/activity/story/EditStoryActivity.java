package com.petsay.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.draggridview.DragGridView;
import com.petsay.component.floatingmenu.FloatingActionMenu;
import com.petsay.component.floatingmenu.SubActionButton;
import com.petsay.component.floatingmenu.animation.DefaultAnimationHandler;
import com.petsay.component.view.HintPopupWindow;
import com.petsay.component.view.popupmenu.ExPopupMenuView;
import com.petsay.component.view.popupmenu.PopupMenuItem;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;

import java.io.File;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/13
 * @Description
 */
public class EditStoryActivity extends BaseActivity implements View.OnClickListener, FloatingActionMenu.MenuStateChangeListener, AdapterView.OnItemClickListener, StoryRecorderView.StoryRecorderCallback {

    public static EditStoryActivity instance;

    @InjectView(R.id.ll_title)
    private LinearLayout mLlTitle;
    @InjectView(R.id.ev_title)
    private EditText mEvTitle;
    @InjectView(R.id.dragGridView)
    private DragGridView mDragGridView;
    @InjectView(R.id.iv_edit)
    private ImageView mIvEdit;
    @InjectView(R.id.iv_preview)
    private ImageView mIvPreview;
    @InjectView(R.id.shadeview)
    private View mShadeView;
    @InjectView(R.id.recorderview)
    private StoryRecorderView mRecorderView;
    private FloatingActionMenu mActionMenu;
    private ExPopupMenuView mCancelMenu;

    private StoryAdapter mAdapter;

    private StoryParams mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_story_edit);
        mParams = (StoryParams) getIntent().getSerializableExtra("params");
        if(mParams == null){
            showToast("参数错误请重试！");
            return;
        }
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initEditFloatMenu();
        mEvTitle.setText(mParams.description);
        initTitleBar("添加故事", true);
        setListener();
        mAdapter = new StoryAdapter(this,mParams);
        mDragGridView.setAdapter(mAdapter);
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
        TextView txt = PublicMethod.addTitleRightText(mTitleBar,"下一步");
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckValue()) {
                    Intent intent = new Intent(EditStoryActivity.this, StorySetCoverActivity.class);
                    intent.putExtra("params", mParams);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean onCheckValue() {
        int imgCount = 0;
        int addCount =0;
        int txtCount =0;
        for (int i=0;i<mParams.items.size();i++){
            StoryItemVo itemVo = mParams.items.get(i);
            if(itemVo.getType() == StoryItemVo.TYPE_ADDRESS_TIME){
                addCount ++;
            }else if(itemVo.getType() == StoryItemVo.TYPE_IMAGE){
                imgCount ++;
            }else {
                txtCount ++;
            }
        }
        if(imgCount <4)
            showToast("故事至少包含5到20张图片");
        else if(addCount == 0)
            showToast("故事至少包含一个时间和地点");
        else if(txtCount == 0)
            showToast("故事至少包含一个文字描述");
        return imgCount >= 5 && addCount >= 1 && txtCount >= 1;
    }

    private void setListener() {
        mIvEdit.setOnClickListener(this);
        mIvPreview.setOnClickListener(this);
        mShadeView.setOnClickListener(this);
        mEvTitle.setOnClickListener(this);
        mActionMenu.setStateChangeListener(this);
        mDragGridView.setOnItemClickListener(this);
        mRecorderView.setCallback(this);
    }

    private void initEditFloatMenu() {

        SubActionButton.Builder subBuilder = new SubActionButton.Builder(this);

        SubActionButton addr = getSubbutton(subBuilder, R.drawable.story_add_addr_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFlotingMenu();
                Intent intent = new Intent(EditStoryActivity.this,StoryAddAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
        SubActionButton img = getSubbutton(subBuilder, R.drawable.story_add_img_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFlotingMenu();
                startActivity(new Intent(EditStoryActivity.this, StoryPhotoWallActivity.class));
                finish();
            }
        });
        SubActionButton text = getSubbutton(subBuilder, R.drawable.story_add_text_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFlotingMenu();
                Intent intent = new Intent(EditStoryActivity.this,StoryAddTextActivity.class);
                startActivity(intent);
                finish();
            }
        });


        int radius = PublicMethod.getDiptopx(this,140);
        mActionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(addr)
                .addSubActionView(img)
                .addSubActionView(text)
                .setStartAngle(-90)
                .setEndAngle(0)
                .setAnimationHandler(new DefaultAnimationHandler(false))
                .setRadius(radius)
                .attachTo(mIvEdit)
                .build();

    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra("params",mParams);
        super.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        mEvTitle.setCursorVisible(false);
        switch (v.getId()){
            case R.id.iv_edit:
                switchFlotingMenu();
                checkShowHint();
                break;
            case R.id.shadeview:
                closeFlotingMenu();
                break;
            case R.id.iv_preview:
                Intent intent = new Intent(this,StoryPreviewActivity.class);
                intent.putExtra("params",mParams);
                startActivity(intent);
                break;
            case R.id.ev_title:
                mEvTitle.setCursorVisible(true);
                break;
        }
    }

    private void switchFlotingMenu(){
        if(mActionMenu.isOpen()){
           closeFlotingMenu();
        }else {
            mActionMenu.open(true);
        }
    }

    private void closeFlotingMenu() {
        if(mActionMenu.isOpen()) {
            mActionMenu.close(true);
        }
    }

    private SubActionButton getSubbutton(SubActionButton.Builder builder,int resId,View.OnClickListener clickListener){
        ImageView img = new ImageView(this);
        img.setImageResource(resId);
        SubActionButton btn = builder.setContentView(img).build();
        btn.setOnClickListener(clickListener);
        return btn;
    }


    @Override
    public void onMenuOpened(FloatingActionMenu menu) {
        mShadeView.setFocusable(true);
        mShadeView.setVisibility(View.VISIBLE);
        mIvEdit.setImageResource(R.drawable.story_cancel_icon);
    }

    @Override
    public void onMenuClosed(FloatingActionMenu menu) {
        mIvEdit.setImageResource(R.drawable.story_add_icon);
        mShadeView.setVisibility(View.GONE);
    }

    private void showCancelMenu(PopupMenuItem[] menus){
        if(mCancelMenu == null){
            mCancelMenu = new ExPopupMenuView(this);
        }
        mCancelMenu.show(mLayoutRoot, menus);
    }

    private void hidenCancelMenu(){
        if(mCancelMenu != null)
            mCancelMenu.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoryItemVo itemVo = (StoryItemVo) mAdapter.getItem(position);
        PopupMenuItem[] items;
        if(itemVo.getType() == StoryItemVo.TYPE_IMAGE){
            items =getImageMenus((StoryImageItem) itemVo,position);
        }else {
            items = getGeneralMenus(itemVo,position);
        }
        showCancelMenu(items);
    }

    private PopupMenuItem[] getGeneralMenus(final StoryItemVo itemVo, final int position) {
        PopupMenuItem[] items =  new PopupMenuItem[2];
        PopupMenuItem updateMenu =new PopupMenuItem("修改");
        items[0]  = updateMenu;
        updateMenu.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenCancelMenu();
                if(itemVo.getType() == StoryItemVo.TYPE_ADDRESS_TIME){
                    Intent intent = new Intent(EditStoryActivity.this,StoryAddAddressActivity.class);
                    intent.putExtra("params",mParams);
                    intent.putExtra("position",position);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(EditStoryActivity.this,StoryAddTextActivity.class);
                    intent.putExtra("params",mParams);
                    intent.putExtra("position",position);
                    startActivity(intent);
                    finish();
                }
            }
        };
        PopupMenuItem delteMenu =new PopupMenuItem("删除");
        delteMenu.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenCancelMenu();
                mParams.items.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        };
        items[1]  = delteMenu;
        return items;
    }

    private PopupMenuItem[] getImageMenus(final StoryImageItem vo, final int position){
        PopupMenuItem[] items = new PopupMenuItem[3];
        PopupMenuItem txtMenu =new PopupMenuItem();
        txtMenu.title = TextUtils.isEmpty(vo.getDescribe()) ? "添加文字描述" : "删除文字描述";
        txtMenu.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenCancelMenu();
                if(TextUtils.isEmpty(vo.getDescribe())){
                    Intent intent = new Intent(EditStoryActivity.this,StoryAddTextActivity.class);
                    intent.putExtra("params",mParams);
                    intent.putExtra("position",position);
                    intent.putExtra("type",vo.getType());
                    startActivity(intent);
                    finish();
                }else {
                    vo.setDescribe("");
//                    mParams.items.get(position)
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        items[0] =txtMenu;
        PopupMenuItem audioMenu = new PopupMenuItem();
        items[1] = audioMenu;
        audioMenu.title = TextUtils.isEmpty(vo.getAudioUrl()) ? "添加语音" : "删除语音";
        audioMenu.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenCancelMenu();
                if(TextUtils.isEmpty(vo.getAudioUrl())){
                    mRecorderView.show(vo);
                }else {
                    File file = new File(vo.getAudioUrl());
                    if(file.exists()){
                        file.exists();
                    }
                    vo.setAudioSeconds(0);
                    vo.setAudioUrl("");
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        PopupMenuItem delteMenu = new PopupMenuItem();
        delteMenu.title = "删除图片";
        delteMenu.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenCancelMenu();
                mParams.items.remove(vo);
                mAdapter.notifyDataSetChanged();
            }
        };
        items[2] = delteMenu;
        return items;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(instance != null)
            instance = null;
    }

    @Override
    public void onClose() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            checkShowHint();
        }
    }



    private void checkShowHint(){
        if(mDragGridView.getChildCount() > 0) {
            final SharePreferenceCache cache = SharePreferenceCache.getSingleton(this);
            boolean isShow = cache.getBooleanValue("editstoryactivity_hint", true);
            if (isShow) {
                final HintPopupWindow hint = new HintPopupWindow(this);
                hint.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        hint.setOnDismissListener(null);
                        new HintPopupWindow(EditStoryActivity.this).show(mIvEdit,R.drawable.hint_story_add_item);
                        cache.setBooleanValue("editstoryactivity_hint",false);
                    }
                });
                hint.show(mDragGridView.getChildAt(0), Gravity.BOTTOM,R.drawable.hint_story_item);
            }
        }
    }

}
