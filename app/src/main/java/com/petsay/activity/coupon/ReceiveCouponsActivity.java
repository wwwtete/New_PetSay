package com.petsay.activity.coupon;

import java.util.List;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.CouponNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.coupon.CouponActivityDTO;
import com.petsay.vo.coupon.CouponActivitySettingsDTO;

import android.R.color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 领取优惠券
 * 
 * @author gaojian
 *
 */
public class ReceiveCouponsActivity extends BaseActivity implements OnClickListener, NetCallbackInterface {
	private ListView mLv;
	private View mFooterView;
	private TextView mTvRule;
	
	private int mSelectedPosition;
	private CouponActivityDTO  mCouponActivityDTO;
	private List<CouponActivitySettingsDTO> mCouponDTOs;
	private CouponNet mCouponNet;
	private CouponListAdapter mCouponListAdapter; 
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycoupons);
		key=getIntent().getStringExtra("key");
//		mIsSelectedEnable = getIntent().getBooleanExtra("IsSelectedEnable", false);
		initView();
		setListener();
		// initList();
		mCouponNet = new CouponNet();
		mCouponNet.setCallback(this);
		mCouponNet.setTag(this);
		mCouponNet.couponActivity(key);
	}

	@Override
	protected void initView() {
		super.initView();
		initTitleBar("领取优惠券", true);
		mLv = (ListView) findViewById(R.id.lv);
		ImageView imgHeader=new ImageView(this);
		imgHeader.setAdjustViewBounds(true);
		imgHeader.setImageResource(R.drawable.coupon_header_banner);
		
		mFooterView=LayoutInflater.from(this).inflate(R.layout.activity_receiver_coupons_lvfooter, null);
		mTvRule=(TextView) mFooterView.findViewById(R.id.tv_rule);
		mLv.addFooterView(mFooterView);
		mLv.addHeaderView(imgHeader);
		mCouponListAdapter=new CouponListAdapter();
	}

	private void setListener() {
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position>0) {
					mSelectedPosition=position-1;
					CouponActivitySettingsDTO dto=mCouponDTOs.get(mSelectedPosition);
					if (!dto.getIsTaken()) {
						mCouponNet.couponGetByActivity(dto.getId());
					}
//					if (mIsSelectedEnable) {
//						if () {
//							mSelectedPosition=-1;
//						}else {
//							mSelectedPosition=position-1;
//						}
//						mCouponListAdapter.notifyDataSetChanged();
//					}
				}
				
			}
		});

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exchange:
//			mCouponNet.couponGetByCode(mEdCode.getText().toString());
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
//			intent.putExtra("coupon", mCouponDTOs.get(mSelectedPosition));
//			setResult(100, intent);
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
				convertView = LayoutInflater.from(ReceiveCouponsActivity.this).inflate(R.layout.my_coupon_list_item, null);
				holder = new Holder();
				holder.tvFaceValue = (TextView) convertView.findViewById(R.id.tv_facevalue);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_couponname);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tvLimit = (TextView) convertView.findViewById(R.id.tv_limit);
				holder.imgCouponHeader = (ImageView) convertView.findViewById(R.id.img_coupon_header);
//				holder.imgSelect=(ImageView) convertView.findViewById(R.id.img_select);
				holder.imgCouponState=(ImageView) convertView.findViewById(R.id.img_coupon_state);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			CouponActivitySettingsDTO couponDTO = mCouponDTOs.get(position);
			holder.tvFaceValue.setText("￥" + couponDTO.getCouponFaceValue());
			holder.tvName.setText(couponDTO.getCouponName());
			holder.tvLimit.setText(couponDTO.getCouponDesc());

			
			if (couponDTO.getIsTaken()) {
				holder.tvDate.setText("有效期："+PublicMethod.formatTimeToString(couponDTO.getCouponBeginTime(), "yyyy.MM.dd") + "-"
						+ PublicMethod.formatTimeToString(couponDTO.getCouponEndTime(), "yyyy.MM.dd"));
				holder.imgCouponHeader.setImageResource(R.drawable.coupon_header_invalid);
				holder.imgCouponState.setImageResource(R.drawable.coupon_already_receive);
				holder.imgCouponState.setVisibility(View.VISIBLE);
				holder.tvFaceValue.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvName.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvDate.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
				holder.tvLimit.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
			}else {
				if (couponDTO.getCouponLimitCount()==-1||couponDTO.getCouponLimitCount()>couponDTO.getCouponSendCount()) {
					holder.tvDate.setText("点击领取");
					holder.imgCouponHeader.setImageResource(R.drawable.coupon_header_valid);
					holder.imgCouponState.setVisibility(View.GONE);
					holder.tvFaceValue.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
					holder.tvName.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
					holder.tvDate.setTextColor(Color.rgb(0x85, 0xCB, 0xFC));
					holder.tvLimit.setTextColor(Color.rgb(0x64, 0x64, 0x64));
				}else {
					holder.tvDate.setText(" ");
					holder.imgCouponHeader.setImageResource(R.drawable.coupon_header_invalid);
					holder.imgCouponState.setVisibility(View.VISIBLE);
					holder.imgCouponState.setImageResource(R.drawable.coupon_already_null);
					holder.tvFaceValue.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
					holder.tvName.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
					holder.tvDate.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));
					holder.tvLimit.setTextColor(Color.rgb(0xB3, 0xB3, 0xB3));

				}
				
			}
			return convertView;
		}

	}

	private class Holder {
		private TextView tvFaceValue, tvName, tvLimit, tvDate;
		private ImageView imgCouponHeader,imgCouponState;
		// private
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_COUPONACTIVITY:
			try {
				mCouponActivityDTO = JsonUtils.resultData(bean.getValue(), CouponActivityDTO.class);
				mCouponDTOs=mCouponActivityDTO.getSettings();
				mLv.setAdapter(mCouponListAdapter);
				mTvRule.setText(mCouponActivityDTO.getDesc());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case RequestCode.REQUEST_COUPONGETBYACTIVITY:
			mCouponDTOs.get(mSelectedPosition).setIsTaken(true);
			mCouponListAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		if(error.getCode() == PetSayError.CODE_PERMISSION_ERROR || error.getCode() == PetSayError.CODE_SESSIONTOKEN_DISABLE){
			onSessionTokenDisable(error);
		}else if (TextUtils.isEmpty(error.getMessage())) {
			onErrorShowToast(error);
		}else {
			ToastUtiles.showDefault(getApplicationContext(), error.getResponseBean().getMessage());
		}
		
	}
}
