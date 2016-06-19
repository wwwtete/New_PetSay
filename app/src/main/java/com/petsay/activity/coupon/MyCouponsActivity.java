package com.petsay.activity.coupon;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.component.view.NullTipView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.CouponNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.coupon.CouponDTO;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的优惠券
 * 
 * @author gaojian
 *
 */
public class MyCouponsActivity extends BaseActivity implements OnClickListener, NetCallbackInterface {
	private ListView mLv;
	private TextView mTvExchangeMsg;
	private EditText mEdCode;
	private Button mBtnExchange;
	private Button mBtnConfirm;
	private List<CouponDTO> mCouponDTOs;
	private View mViewExchange;
	private NullTipView mTipNull;
	private CouponNet mCouponNet;
	private boolean mIsSelectedEnable = true;
	private CouponListAdapter mCouponListAdapter; 
	private int mSelectedPosition=-1;
    private final int PageSize=100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycoupons);
		mIsSelectedEnable = getIntent().getBooleanExtra("IsSelectedEnable", false);
		initView();
		setListener();
		// initList();
		mCouponNet = new CouponNet();
		mCouponNet.setCallback(this);
		mCouponNet.setTag(this);
		mBtnConfirm.setEnabled(false);
		onRefresh();
		
	}

	@Override
	protected void initView() {
		super.initView();
		initTitleBar("我的优惠券", true);
		mLv = (ListView) findViewById(R.id.lv);
		mViewExchange = LayoutInflater.from(this).inflate(R.layout.view_exchange_coupon, null);
		mTvExchangeMsg = (TextView) mViewExchange.findViewById(R.id.tv_exchange_msg);
		mEdCode = (EditText) mViewExchange.findViewById(R.id.ed_code);
		mBtnExchange = (Button) mViewExchange.findViewById(R.id.btn_exchange);
		mTipNull = (NullTipView) findViewById(R.id.nulltip);
		mBtnConfirm=(Button) findViewById(R.id.btn_confirm);
		mLv.addHeaderView(mViewExchange);
		mCouponListAdapter=new CouponListAdapter();
		
	}

	private void setListener() {
		mBtnExchange.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position>0) {
					
					if (mIsSelectedEnable) {
						if (mSelectedPosition==position-1) {
							mSelectedPosition=-1;
						}else {
							mSelectedPosition=position-1;
						}
						mCouponListAdapter.notifyDataSetChanged();
					}
				}
				
			}
		});

	}
	
	private void onRefresh(){
		if (mIsSelectedEnable) {
			mBtnConfirm.setVisibility(View.VISIBLE);
			
			String orderId=getIntent().getStringExtra("orderId");
			mCouponNet.couponOrderAvailableList(orderId, "", PageSize);
		}else {
			mBtnConfirm.setVisibility(View.GONE);
			mCouponNet.couponAvailableList("", PageSize);
		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exchange:
			mCouponNet.couponGetByCode(mEdCode.getText().toString());
			break;
		case R.id.btn_confirm:
			if (mIsSelectedEnable&&mSelectedPosition>=0) {
				Intent intent=new Intent();
				intent.putExtra("CouponDto", mCouponDTOs.get(mSelectedPosition));
				setResult(OrderConfirmActivity.REQUEST_CODE_COUPON, intent);
			}
			finish();
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (mIsSelectedEnable&&mSelectedPosition>=0) {
//			Intent intent=new Intent();
//			intent.putExtra("CouponDto", mCouponDTOs.get(mSelectedPosition));
//			setResult(OrderConfirmActivity.REQUEST_CODE_COUPON, intent);
//		}
	}

	private class CouponListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (null == mCouponDTOs || mCouponDTOs.isEmpty()) {
				return 0;
			} else
				return mCouponDTOs.size();
		}

		@Override
		public Object getItem(int position) {
			if (null == mCouponDTOs || mCouponDTOs.isEmpty()) {
				return null;
			} else
				return mCouponDTOs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(MyCouponsActivity.this).inflate(R.layout.my_coupon_list_item, null);
				holder = new Holder();
				holder.tvFaceValue = (TextView) convertView.findViewById(R.id.tv_facevalue);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_couponname);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tvLimit = (TextView) convertView.findViewById(R.id.tv_limit);
				holder.imgCouponHeader = (ImageView) convertView.findViewById(R.id.img_coupon_header);
				holder.imgSelect=(ImageView) convertView.findViewById(R.id.img_select);
				holder.imgCouponState=(ImageView) convertView.findViewById(R.id.img_coupon_state);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			CouponDTO couponDTO = mCouponDTOs.get(position);
			holder.tvFaceValue.setText("￥" + couponDTO.getFaceValue());
			holder.tvName.setText(couponDTO.getName());
			holder.tvLimit.setText(couponDTO.getDesc());

			holder.tvDate.setText("有效期："+PublicMethod.formatTimeToString(couponDTO.getBeginTime(), "yyyy.MM.dd") + "-"
					+ PublicMethod.formatTimeToString(couponDTO.getEndTime(), "yyyy.MM.dd"));
			if (mIsSelectedEnable&&mSelectedPosition==position) {
				holder.imgSelect.setVisibility(View.VISIBLE);
			}else {
				holder.imgSelect.setVisibility(View.INVISIBLE);
			}
			if (couponDTO.getState().equals("1")) {
				holder.imgCouponHeader.setImageResource(R.drawable.coupon_header_valid);
				holder.imgCouponState.setVisibility(View.GONE);
				holder.tvFaceValue.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
				holder.tvName.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
				holder.tvDate.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
				holder.tvLimit.setTextColor(Color.rgb(0x64, 0x64, 0x64));
			} else {
				holder.imgCouponHeader.setImageResource(R.drawable.coupon_header_invalid);
				holder.imgCouponState.setVisibility(View.VISIBLE);
				holder.tvFaceValue.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvName.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvDate.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvLimit.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				if (couponDTO.getState().equals("2")) {
					holder.imgCouponState.setImageResource(R.drawable.coupon_already_use);
				} else {
					holder.imgCouponState.setImageResource(R.drawable.coupon_already_timeout);
				}
			}

			return convertView;
		}

	}

	private class Holder {
		private TextView tvFaceValue, tvName, tvLimit, tvDate;
		private ImageView imgCouponHeader,imgSelect,imgCouponState;
		// private
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_COUPONAVAILABLELIST:
		case RequestCode.REQUEST_COUPONORDERAVAILABLELIST:
			try {
				mCouponDTOs = JsonUtils.getList(bean.getValue(), CouponDTO.class);
				mLv.setAdapter(mCouponListAdapter);
				if(mCouponDTOs.isEmpty()){
					mTipNull.setVisibility(View.VISIBLE);
				}else {
					mTipNull.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mCouponDTOs.size()>0) {
				mBtnConfirm.setEnabled(true);
			}
			 
			break;
		case RequestCode.REQUEST_COUPONGETBYCODE:
			mEdCode.setText("");
			ToastUtiles.showDefault(this, "兑换成功");
			onRefresh();
			break;
		default:
			break;
		}

	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		if (TextUtils.isEmpty(error.getMessage())) {
			onErrorShowToast(error);
		}else {
			switch (requestCode) {
			case RequestCode.REQUEST_COUPONAVAILABLELIST:
			case RequestCode.REQUEST_COUPONORDERAVAILABLELIST:
				 
				break;
			case RequestCode.REQUEST_COUPONGETBYCODE:
				
				ToastUtiles.showDefault(this, error.getResponseBean().getMessage());
				onRefresh();
				break;
			default:
				break;
			}
		}
		
	}
}
