package com.petsay.component.wheelview;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.petsay.R;
import com.petsay.component.wheel.NumericWheelAdapter;
import com.petsay.component.wheel.OnWheelChangedListener;
import com.petsay.component.wheel.OnWheelScrollListener;
import com.petsay.component.wheel.WheelView;
import com.petsay.utile.PublicMethod;

public class DateWheelView extends LinearLayout{
	private boolean timeChanged = false;
	private boolean timeScrolled = false;
	private Context mContext;
	private int startYear=1990;
	private int yearOffset=0;
	
	
	private int selectedDay=1;
	private int selectedMonth=1;
	private int selectedYear=2014;
	private long mDefaultDate;
	public DateWheelView(Context context,long dateLong) {
		super(context);
		mContext=context;
		mDefaultDate=dateLong;
		inflate(context, R.layout.date_wheel, this);
		initView();
	}
	
	
	public DateWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		inflate(context, R.layout.date_wheel, this);
		
	}
	

    public void setDefaultDate(long defaultDate){
        mDefaultDate = defaultDate;
        initView();
    }
	
	private void initView(){
		
//		Date d = new Date(mDefaultDate);
//		  selectedYear=  d.getYear();
//		  selectedMonth=d.getMonth();
//		  selectedDay=d.getDay();
//		  d.
		Calendar c = Calendar.getInstance();
		int maxYear=c.get(Calendar.YEAR);
		c.setTimeInMillis(mDefaultDate);
		selectedYear=c.get(Calendar.YEAR);
		selectedMonth = c.get(Calendar.MONTH);
	    selectedDay= c.get(Calendar.DAY_OF_MONTH);
		final WheelView years = (WheelView) findViewById(R.id.year);
		years.setAdapter(new NumericWheelAdapter(startYear, maxYear,"年"));
		years.setVisibleItems(7);
		
		
		
		final WheelView months = (WheelView) findViewById(R.id.month);
		months.setAdapter(new NumericWheelAdapter(1, 12,"月"));
		months.setVisibleItems(7);
		months.setCyclic(true);
		
		int dayCount=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		final WheelView days = (WheelView) findViewById(R.id.day);
		days.setAdapter(new NumericWheelAdapter(1, dayCount,"日"));
		days.setVisibleItems(7);
		days.setCyclic(true);
		// set current createTime
		
		
//		tempC.set
//		System.out.println("fileCount:"+tempC.getActualMaximum(Calendar.DAY_OF_MONTH));
	
		years.setCurrentItem(selectedYear-1990);
		months.setCurrentItem(selectedMonth);
		days.setCurrentItem(selectedDay-1);
		selectedMonth=selectedMonth+1;
	
		// add listeners
		addChangingListener(years, "year");
		addChangingListener(months, "month");
		addChangingListener(days, "day");
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (wheel==years) {
					String yearItem=years.getTextItem(newValue);
					selectedYear=Integer.parseInt(yearItem.substring(0, yearItem.length()-1));
					Calendar tempC=Calendar.getInstance();
					tempC.set(Calendar.YEAR,selectedYear );
					tempC.set(Calendar.MONTH, selectedMonth-1);
					days.setAdapter(new NumericWheelAdapter(1, tempC.getActualMaximum(Calendar.DAY_OF_MONTH),"日"));
				}else if(wheel==months){
					String monthItem=months.getTextItem(newValue);
					selectedMonth=Integer.parseInt(monthItem.substring(0, monthItem.length()-1));
					Calendar tempC=Calendar.getInstance();
					tempC.set(Calendar.YEAR, selectedYear);
					tempC.set(Calendar.MONTH, selectedMonth-1);
					days.setAdapter(new NumericWheelAdapter(1, tempC.getActualMaximum(Calendar.DAY_OF_MONTH),"日"));
				}else {
					String dayItem=days.getTextItem(newValue);
					selectedDay= Integer.parseInt(dayItem.substring(0, dayItem.length()-1));
				}
//				System.out.println("日期："+selectedYear+"-"+selectedMonth+"-"+selectedDay);
			}
		};

		years.addChangingListener(wheelListener);
		months.addChangingListener(wheelListener);
		days.addChangingListener(wheelListener);
		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
//				System.out.println("text:");
//				

				//				picker.setCurrentHour(hours.getCurrentItem());
//				picker.setCurrentMinute(mins.getCurrentItem());
				timeChanged = false;
			}
		};
		years.addScrollingListener(scrollListener);
		months.addScrollingListener(scrollListener);
		days.addScrollingListener(scrollListener);
		
	}
	
	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
	
	
	public String getSelectDate(){
		String monthStr;
		int tempMonth=selectedMonth;
		
		if (tempMonth<10) {
			monthStr="0"+tempMonth;
		}else
			monthStr=tempMonth+"";
		String dayStr;
		if (selectedDay<10) {
			dayStr="0"+selectedDay;
		}else
			dayStr=""+selectedDay;
		String dateStr=selectedYear+"-"+monthStr+"-"+dayStr;
		long selDate=PublicMethod.formatTimeToLong(dateStr, "yyyy-MM-dd");
		if (selDate>System.currentTimeMillis()) {
			return PublicMethod.formatTimeToString(System.currentTimeMillis(), "yyyy-MM-dd");
		}else {
			return selectedYear+"-"+monthStr+"-"+dayStr;
		}
		
		
	}

}
