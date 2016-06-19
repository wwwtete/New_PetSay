package com.petsay.activity.personalcustom.pay;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.coupon.MyCouponsActivity;
import com.petsay.activity.user.shippingaddress.EditShippingAddressActivity;
import com.petsay.activity.user.shippingaddress.ShippingAddressManagActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.network.net.ShippingAddressNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PayTools;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.coupon.CouponDTO;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderProductDTO;
import com.petsay.vo.user.UserShippingAddressDTO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author GJ
 *定制明信片首页
 */
public class OrderConfirmActivity extends BaseActivity implements OnClickListener, NetCallbackInterface {
	private TitleBar mTitleBar;
	private AddOrderView mAddOrderView;
	private TextView mtvProductPrice,mTvPostage;
	private TextView mTvOrderOnePrice,mTvOrderCount,mTvOrderInfo;
	private ImageView mImgThumb;
	private ImageView mImgAlipay,mImgWx,mImgUpacp;
	private EditText mEdNote;
	private ImageView mImgAddaddress;
	private RelativeLayout mLayoutAddress;
	private TextView mTvReceiver,mTvReceiverPhone,mTvAddress;
	private RelativeLayout mLayoutCoupon;
	private TextView mTvCouponCount;
	
//	private ArrayList<OrderVo> mOrderVos=new ArrayList<OrderVo>();
	private OrderDTO mOrderDTO;
	private int mTotalCount=0;
	private float mTotalPrice=0;
	
	 private OrderNet mOrderNet;
	 private ShippingAddressNet mAddressNet;
	 private UserShippingAddressDTO mAddressDTO;
	 private String[] payTypes={"wx","alipay","upacp"};
	 private int mSelectPayTypeIndex=0;
     private CouponDTO mCoupon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderconfirm);
		mOrderDTO=(OrderDTO) getIntent().getSerializableExtra("OrderDTO");
		initView();
		setListener();
		
		mAddressNet=new ShippingAddressNet();
		mAddressNet.setCallback(this);
		mAddressNet.setTag(this);
		mAddressNet.getDefaultAddress();
	}
	
	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar("订单确认",true);
		mAddOrderView=(AddOrderView) findViewById(R.id.view_addorder);
		
		mTvPostage=(TextView) findViewById(R.id.tv_postage);
		mtvProductPrice=(TextView) findViewById(R.id.tv_product_price);
		mImgAlipay=(ImageView) findViewById(R.id.img_alipay);
		mImgWx=(ImageView) findViewById(R.id.img_wx);
		mImgUpacp=(ImageView) findViewById(R.id.img_upacp);
		mEdNote=(EditText) findViewById(R.id.ed_note);
		mTvOrderOnePrice=(TextView) findViewById(R.id.tv_order_oneprice);
		mTvOrderCount=(TextView) findViewById(R.id.tv_order_count);
		mTvOrderInfo=(TextView) findViewById(R.id.tv_orderinfo);
		mImgThumb=(ImageView) findViewById(R.id.img_thumb);
		
		mImgAddaddress=(ImageView) findViewById(R.id.img_add_address);
		mLayoutAddress=(RelativeLayout) findViewById(R.id.layout_address);
		mTvReceiver=(TextView) findViewById(R.id.tv_receiver);
		mTvReceiverPhone=(TextView) findViewById(R.id.tv_receiver_phone);
		mTvAddress=(TextView) findViewById(R.id.tv_address);
		
		mLayoutCoupon=(RelativeLayout) findViewById(R.id.layout_coupon);
		mTvCouponCount=(TextView) findViewById(R.id.tv_coupon_count);
//		mTvCouponTag=(TextView) findViewById(R.id.tv_coupon_tag);
		
		mSelectPayTypeIndex=0;
		mImgWx.setImageResource(R.drawable.xuanzhongweixin);
		mImgAlipay.setImageResource(R.drawable.weixuanzhongzhifubao);
		mImgUpacp.setImageResource(R.drawable.weixuanzhongyinlian);
		
		mtvProductPrice.setText("商品："+PublicMethod.formatFloat(2, mOrderDTO.getProductAmount()));
		mTvPostage.setText("运费："+mOrderDTO.getShippingFee());
		mAddOrderView.setTotalPrice(mOrderDTO.getAmount());
		
		OrderProductDTO orderProductDTO=mOrderDTO.getOrderProducts().get(0);
		mTvOrderInfo.setText(orderProductDTO.getName());
		mTvOrderCount.setText("x "+mOrderDTO.getProductCount());
		mTvOrderOnePrice.setText("￥ "+orderProductDTO.getPrice());
		ImageLoaderHelp.displayContentImage(orderProductDTO.getCover(), mImgThumb);
		
		
	}
	private void setAddressText(UserShippingAddressDTO addressDTO){
		mLayoutAddress.setVisibility(View.VISIBLE);
		mImgAddaddress.setVisibility(View.GONE);
		mTvReceiver.setText("收件人："+addressDTO.getName());
		mTvReceiverPhone.setText(" "+addressDTO.getMobile());
		mTvAddress.setText(addressDTO.getProvince()+addressDTO.getCity()+addressDTO.getAddress());
	}
	
	private void setListener(){
		mAddOrderView.setClickListener(this);
		mImgAlipay.setOnClickListener(this);
		mImgWx.setOnClickListener(this);
		mImgUpacp.setOnClickListener(this);
		mLayoutAddress.setOnClickListener(this);
		mLayoutCoupon.setOnClickListener(this);
	}
	
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit_order:
			showLoading(false);
			if (null == mAddressDTO) {
				closeLoading();
				ToastUtiles.showDefault(getApplicationContext(), "请添加收货地址");
			} else {
				if (null == mOrderNet) {
					mOrderNet = new OrderNet();
					mOrderNet.setCallback(this);
					mOrderNet.setTag(this);
				}
				if (null==mCoupon) {
					mOrderNet.orderConfirm(mOrderDTO.getId(),"",payTypes[mSelectPayTypeIndex], mEdNote.getText().toString(), mAddressDTO);
				}else {
					mOrderNet.orderConfirm(mOrderDTO.getId(),mCoupon.getId(),payTypes[mSelectPayTypeIndex], mEdNote.getText().toString(), mAddressDTO);
				}
			}
			break;
		case R.id.img_wx:
			if (mSelectPayTypeIndex!=0) {
				mSelectPayTypeIndex=0;
				mImgWx.setImageResource(R.drawable.xuanzhongweixin);
				mImgAlipay.setImageResource(R.drawable.weixuanzhongzhifubao);
				mImgUpacp.setImageResource(R.drawable.weixuanzhongyinlian);
			}
			break;
		case R.id.img_alipay:
			if (mSelectPayTypeIndex!=1) {
				mSelectPayTypeIndex=1;
				mImgWx.setImageResource(R.drawable.weixuanzhongweixin);
				mImgAlipay.setImageResource(R.drawable.xuanzhongzhifubao);
				mImgUpacp.setImageResource(R.drawable.weixuanzhongyinlian);
			}
			break;
		case R.id.img_upacp:
			if (mSelectPayTypeIndex!=2) {
				mSelectPayTypeIndex=2;
				mImgWx.setImageResource(R.drawable.weixuanzhongweixin);
				mImgAlipay.setImageResource(R.drawable.weixuanzhongzhifubao);
				mImgUpacp.setImageResource(R.drawable.xuanzhongyinlian);
			}
			break;
		case R.id.img_add_address:
			Intent intent=new Intent();
			intent.setClass(this, EditShippingAddressActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADDRESS);
			break;
		case R.id.layout_address:
			 intent=new Intent();
			intent.setClass(this, ShippingAddressManagActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADDRESS);
			break;
		case R.id.layout_coupon:
			if (null!=mOrderDTO) {
				intent=new Intent(this, MyCouponsActivity.class);
				intent.putExtra("IsSelectedEnable", true);
				intent.putExtra("orderId", mOrderDTO.getId());
				startActivityForResult(intent, REQUEST_CODE_COUPON);
			}
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_ORDERCONFIRM:
		    PayTools.turnToPay(this, bean.getValue());
			break;
		case RequestCode.REQUEST_DEFAULTSHIPPINGADDRESS:
			mAddressDTO=JsonUtils.resultData(bean.getValue(), UserShippingAddressDTO.class);
			setAddressText(mAddressDTO);
			break;
		default:
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		
		case RequestCode.REQUEST_DEFAULTSHIPPINGADDRESS:
			if (error.getResponseBean().getError()==6080) {
				mImgAddaddress.setVisibility(View.VISIBLE);
				mLayoutAddress.setVisibility(View.GONE);
				mImgAddaddress.setOnClickListener(this);
			}else {
				mImgAddaddress.setVisibility(View.GONE);
				mLayoutAddress.setVisibility(View.VISIBLE);
				onErrorShowToast(error);
			}
			break;
		}
		
	}
	
//	public static int REQUEST_CODE_PAYMENT=10;
	public static int REQUEST_CODE_ADDRESS=101;
	public static int REQUEST_CODE_COUPON=102;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		    //支付页面返回处理
//		    if (requestCode == REQUEST_CODE_PAYMENT) {
//		        if (resultCode == Activity.RESULT_OK) {
//		            String result = data.getExtras().getString("pay_result");
//		            /* 处理返回值
//		             * "success" - payment succeed
//		             * "fail"    - payment failed
//		             * "cancel"  - user canceld
//		             * "invalid" - payment plugin not installed
//		             *
//		             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
//		             */
//		            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//		            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
////		            if (mSelectPayTypeIndex==2&&result.equals("invalid")) {
////		            	UPPayAssistEx.installUPPayPlugin(this);
////					}
//		        } else if (resultCode == Activity.RESULT_CANCELED) {
//		            Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
////		        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
////		            Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
//		        }else {
//		        	Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
////					PaymentActivity.
//				}
//		    }
		    if (resultCode==REQUEST_CODE_ADDRESS) {
		    	
				mAddressDTO=(UserShippingAddressDTO) data.getSerializableExtra("address");
				setAddressText(mAddressDTO);
			}else if (resultCode==REQUEST_CODE_COUPON) {
				 mCoupon=(CouponDTO) data.getSerializableExtra("CouponDto");
				mTvCouponCount.setText(mCoupon.getFaceValue()+"元");
				float couponValue=Float.parseFloat(mCoupon.getFaceValue());
				float offsetValue=mOrderDTO.getAmount()-couponValue;
				if (offsetValue>0) {
					mAddOrderView.setTotalPrice(offsetValue+mOrderDTO.getShippingFee());
				}else {
					mAddOrderView.setTotalPrice(mOrderDTO.getShippingFee());
				}
				
			}else {
				closeLoading();
				PayTools.managePayResult(this, requestCode, resultCode, data,mOrderDTO);
				finish();
			}
		}
	
}
