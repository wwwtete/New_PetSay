package com.petsay.component.view;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {
	public interface IAddShowLocationViewService{
		View getParentView();
		Activity getActivity();
	}

}
