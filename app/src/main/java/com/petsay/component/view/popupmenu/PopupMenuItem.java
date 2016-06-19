package com.petsay.component.view.popupmenu;

import android.view.View;

public class PopupMenuItem{
    public PopupMenuItem() {
    }

    public PopupMenuItem(String title) {

        this.title = title;
    }

    public String title;
    public View.OnClickListener listener;
}
