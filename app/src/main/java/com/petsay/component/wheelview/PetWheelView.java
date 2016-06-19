package com.petsay.component.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.petsay.R;
import com.petsay.component.wheel.ArrayWheelAdapter;
import com.petsay.component.wheel.OnWheelChangedListener;
import com.petsay.component.wheel.WheelView;
import com.petsay.constants.Constants;
import com.petsay.utile.xml.SaxPetTypeParse;

public class PetWheelView extends LinearLayout{
	private Context mContext;
//	private TextView tvDate;
	private String[] kindNameArray;
	private int[] kindIdArray;
	private int kindIndex=0;
	private int nameIndex=0;
	
	public PetWheelView(Context context) {
		super(context);
		mContext=context;
		inflate(context, R.layout.pet_wheel, this);
		init();
		initView();
	}
	
	public PetWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		inflate(context, R.layout.pet_wheel, this);
		init();
		initView();
	}
	private void init(){
		if (null==Constants.petTypes) {
			Constants.InitPettype(mContext);
		}
		int len=Constants.petTypes.size();
		kindNameArray=new String[len];
		for (int i = 0; i < len; i++) {
			kindNameArray[i]=Constants.petTypes.get(i).getTypeName();
		}
		
	}
	
	private void initView(){
//		tvDate=(TextView) findViewById(R.id.tv_date123);
		WheelView kind = (WheelView) findViewById(R.id.kind);
		 
		kind.setVisibleItems(7);
		kind.setAdapter(new ArrayWheelAdapter<String>(kindNameArray));
		
		final WheelView name = (WheelView) findViewById(R.id.name);
		name.setVisibleItems(7);
		kind.setCurrentItem(0);
		name.setAdapter(new ArrayWheelAdapter<String>(Constants.petTypes.get(0).getName()));
		name.setCurrentItem(0);
		kind.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				name.setAdapter(new ArrayWheelAdapter<String>(Constants.petTypes.get(newValue).getName()));
				name.setCurrentItem(0);
				kindIndex=newValue;
//				tvDate.setText(kindNameArray[kindIndex]+" "+Constants.petTypes.get(kindIndex).getName()[0]);
			}
		});
		
		name.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				nameIndex=newValue;
//				tvDate.setText(kindNameArray[kindIndex]+" "+Constants.petTypes.get(kindIndex).getName()[nameIndex]);
			}
		});
	}
	
	public int[] getSelectIndex(){
		// kindNameArray[proIndex]+" "+Constants.petTypes.get(proIndex).getName()[cityIndex];
		int[] index = new int[2];
		index[0] = kindIndex;
		index[1] = nameIndex;
		return index;
	}
}