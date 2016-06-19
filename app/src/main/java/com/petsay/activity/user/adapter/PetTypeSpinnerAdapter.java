package com.petsay.activity.user.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.petsay.vo.user.PetType;

public class PetTypeSpinnerAdapter extends BaseAdapter{

	private Context mContext;
	private int mFlag;//0宠物种类，1宠物名称
	private static final int PetKindFlag=0;
	private static final int PetTypeFlag=1;
	private List<PetType> mPetTypes;
	private int mIndex;
	public PetTypeSpinnerAdapter(Context context,int flag,List<PetType> petTypes,int index){
		mContext=context;
		mFlag=flag;
		mPetTypes=petTypes;
		mIndex=index;
	}
	
	
	@Override
	public int getCount() {
		if (mFlag==PetTypeFlag) {
			return mPetTypes.get(mIndex).getId().length;
		}else {
			return mPetTypes.size();
		}
		
		
	}

	@Override
	public Object getItem(int position) {
		if (mFlag==PetTypeFlag) {
			return mPetTypes.get(mIndex).getId()[position];
		}else {
			return mPetTypes.get(mIndex);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mFlag==PetTypeFlag) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item, null);
			}
			TextView txt = (TextView) convertView.findViewById(android.R.id.text1);
			txt.setText(mPetTypes.get(mIndex).getName()[position]);
			txt.setTextColor(Color.BLACK);
		}else {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item, null);
			}
			TextView txt = (TextView) convertView.findViewById(android.R.id.text1);
			txt.setText(mPetTypes.get(position).getTypeName());
			txt.setTextColor(Color.BLACK);
		}
		
		return convertView;
	}

}
