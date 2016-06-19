package com.petsay.component.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.wheel.ArrayWheelAdapter;
import com.petsay.component.wheel.OnWheelChangedListener;
import com.petsay.component.wheel.WheelView;
import com.petsay.constants.Constants;

public class CityWheelView extends LinearLayout{
	private Context mContext;
	
	private int proIndex=0;
	private int cityIndex=0;
	
	public CityWheelView(Context context) {
		super(context);
		mContext=context;
		inflate(context, R.layout.city_wheel, this);
		initView();
	}
	
	public CityWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		inflate(context, R.layout.city_wheel, this);
		initView();
	}
	
	private void initView(){
//		tvDate=(TextView) findViewById(R.id.tv_date123);
		WheelView province = (WheelView) findViewById(R.id.province);
		 
		province.setVisibleItems(7);
		province.setAdapter(new ArrayWheelAdapter<String>(Constants.Provinces));
		
		final WheelView city = (WheelView) findViewById(R.id.city);
		city.setVisibleItems(7);
		province.setCurrentItem(0);
		city.setAdapter(new ArrayWheelAdapter<String>(Constants.cities[0]));
		city.setCurrentItem(0);
		province.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                city.setAdapter(new ArrayWheelAdapter<String>(Constants.cities[newValue]));
                city.setCurrentItem(0);
                proIndex = newValue;
//				tvDate.setText(Constants.Provinces[proIndex]+" "+Constants.cities[proIndex][0]);
            }
        });
		
		city.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityIndex = newValue;
//				tvDate.setText(Constants.Provinces[proIndex]+" "+Constants.cities[proIndex][cityIndex]);
            }
        });
	}
	
	public String getSelectCity(){
		return Constants.Provinces[proIndex]+" "+Constants.cities[proIndex][cityIndex];
	}

    public String getSelectCity2(){
        return Constants.cities[proIndex][cityIndex];
    }

    public String getSelectProvince(){
        return Constants.Provinces[proIndex];
    }

}