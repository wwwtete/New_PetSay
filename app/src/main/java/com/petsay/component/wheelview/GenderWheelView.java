package com.petsay.component.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.petsay.R;
import com.petsay.component.wheel.ArrayWheelAdapter;
import com.petsay.component.wheel.OnWheelChangedListener;
import com.petsay.component.wheel.WheelView;
import com.petsay.constants.Constants;

public class GenderWheelView extends LinearLayout {
	private Context mContext;

	private int index = 0;
    private boolean mIsShopping=false;
	public GenderWheelView(Context context,boolean isShopping) {
		super(context);
		mContext = context;
		mIsShopping=isShopping;
		inflate(context, R.layout.gender_wheel, this);
		initView();
	}

	public GenderWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(context, R.layout.gender_wheel, this);
		initView();
	}

	private void initView() {
		WheelView genders = (WheelView) findViewById(R.id.gender);

		genders.setVisibleItems(7);
		if (mIsShopping) {
			genders.setAdapter(new ArrayWheelAdapter<String>(Constants.genderShoppingArray));
		}else {
			genders.setAdapter(new ArrayWheelAdapter<String>(Constants.genderArray));
		}
		

		genders.setCurrentItem(0);
		genders.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				index = newValue;
			}
		});

	}

	public int getGenderId() {
		return index;
	}
	
	/**
	 * 返回性别文本
	 * @return
	 */
	public String getSelectedGender(){
		return Constants.genderShoppingArray[index];
	}
}