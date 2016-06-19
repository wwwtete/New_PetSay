package com.petsay.component.view.slidingmenu;

import android.graphics.Color;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/13
 * @Description 侧滑菜单Item的数据模型
 */
public class SlidingMenuItemModel {

    public int iconResId = 0;
    public int contentResId;
    public int iconPosition = SlidingMenuItem.POSITION_LEFT;
    public int unReadMsgCount;
    public boolean isShowUnReadMsgCount = false;
    public ClickMenuItemCallback clickItemCallback;

    public int contentColor = Color.WHITE;

    public SlidingMenuItemModel(int contentResId) {
        this.contentResId = contentResId;
    }

    public SlidingMenuItemModel(int iconResId, int contentResId) {
        this.iconResId = iconResId;
        this.contentResId = contentResId;
    }

    public SlidingMenuItemModel(int iconResId, int contentResId, ClickMenuItemCallback clickItemCallback) {
        this.iconResId = iconResId;
        this.contentResId = contentResId;
        this.clickItemCallback = clickItemCallback;
    }

    public SlidingMenuItemModel(int iconResId, int contentResId, int iconPosition, ClickMenuItemCallback clickItemCallback) {
        this.iconResId = iconResId;
        this.contentResId = contentResId;
        this.iconPosition = iconPosition;
        this.clickItemCallback = clickItemCallback;
    }

    public interface ClickMenuItemCallback{
        public void onClickItem(SlidingMenuItemModel model);
    }

}
