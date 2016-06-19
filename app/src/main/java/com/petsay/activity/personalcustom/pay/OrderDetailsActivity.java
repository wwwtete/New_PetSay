package com.petsay.activity.personalcustom.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PayTools;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.coupon.CouponDTO;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderProductDTO;
import com.petsay.vo.user.UserShippingAddressDTO;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/11
 * @Description
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener, NetCallbackInterface {

    @InjectView(R.id.iv_service)
    private ImageView mIvService;
    @InjectView(R.id.iv_cancel)
    private ImageView mIvCancel;
    @InjectView(R.id.iv_pay)
    private ImageView mIvPay;
    @InjectView(R.id.tv_order_state)
    private TextView mTvOrderState;
    @InjectView(R.id.tv_number)
    private TextView mTvNumber;
    @InjectView(R.id.tv_time)
    private TextView mTvTime;
    @InjectView(R.id.tv_payinfo)
    private TextView mTvPayinfo;
    @InjectView(R.id.tv_paymode)
    private TextView mTvPaymode;
    @InjectView(R.id.tv_receivename)
    private TextView mTvReceivename;
    @InjectView(R.id.tv_telephone)
    private TextView mTvTelephone;
    @InjectView(R.id.tv_address)
    private TextView mTvAddress;
    @InjectView(R.id.iv_img)
    private ImageView mIvImg;
    @InjectView(R.id.tv_productinfo)
    private TextView mTvProductinfo;
    @InjectView(R.id.tv_price)
    private TextView mTvPrice;
    @InjectView(R.id.tv_count)
    private TextView mTvCount;
    @InjectView(R.id.tv_freight)
    private TextView mTvFreight;
    @InjectView(R.id.tv_money)
    private TextView mTvMoney;
    @InjectView(R.id.tv_coupon)
    private TextView mTvCoupon;

    private OrderDTO mDto;
    private String mOrderId;
    private OrderNet mNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        mDto = (OrderDTO) getIntent().getSerializableExtra("orderdto");
        mOrderId = getIntent().getStringExtra("orderid");
        initData();
        if(!TextUtils.isEmpty(mOrderId)) {
            showLoading(false);
            mNet.orderDetails(mOrderId);
        }
        initView();
    }

    private void initData() {
        mNet = new OrderNet();
        mNet.setCallback(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("订单详情", true);
        if(mDto != null)
         initValueView();
    }

    private void setListener() {
        mIvService.setOnClickListener(this);
    }

    private void initValueView() {
        if(mDto == null)
            return;
        setListener();
        mTvOrderState.setText(mDto.getStateDesc());
        mTvNumber.setText(mDto.getId());
        if(mDto.getConfirmTime() != 0)
            mTvTime.setText(DateFormat.format("yyyy/MM/dd", mDto.getConfirmTime()));
        mTvPayinfo.setText("￥" + mDto.getAmount());
        String channel = "银联";
        if("wx".equals(mDto.getPayChannel())){
            channel = "微信";
        }else if("alipay".equals(mDto.getPayChannel())){
            channel = "支付宝";
        }
        mTvPaymode.setText(channel);
        mTvReceivename.setText("收货人:"+mDto.getShippingName());
        mTvTelephone.setText(""+mDto.getShippingMobile());
        mTvAddress.setText(mDto.getShippingAddress());
        if(!mDto.getOrderProducts().isEmpty()) {
            OrderProductDTO productDTO = mDto.getOrderProducts().get(0);
            ImageLoaderHelp.displayContentImage(productDTO.getCover(),mIvImg);
            mTvProductinfo.setText(productDTO.getName());
            mTvPrice.setText("￥ "+productDTO.getPrice());
        }
        mTvCount.setText("x"+mDto.getProductCount());
        if(mDto.getShippingFee() > 0){
            mTvFreight.setVisibility(View.VISIBLE);
            mTvFreight.setText("运费: ￥"+mDto.getShippingFee());
        }else {
            mTvFreight.setVisibility(View.GONE);
        }
        mTvMoney.setText("￥"+mDto.getAmount());
        if (mDto.getUseCoupon()) {
        	mTvCoupon.setVisibility(View .VISIBLE);
        	mTvCoupon.setText("优惠：￥"+mDto.getDiscountAmount());
		}else {
			mTvCoupon.setVisibility(View .GONE);
		}

        if("2".equals(mDto.getState())){
            mIvPay.setVisibility(View.VISIBLE);
            mIvCancel.setVisibility(View.VISIBLE);
            mIvPay.setOnClickListener(this);
            mIvCancel.setOnClickListener(this);
        }else {
            mIvPay.setVisibility(View.GONE);
            mIvCancel.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_service:
                onContactHeler();
                break;
            case R.id.iv_cancel:
                onCancelOrder();
                break;
            case R.id.iv_pay:
                onPayOrder();
                break;
        }
    }

    private void onPayOrder() {
        if("2".equals(mDto.getState())) {
            showLoading();
            UserShippingAddressDTO addressDTO = new UserShippingAddressDTO(mDto.getShippingName(),mDto.getShippingProvince(),mDto.getShippingCity(),mDto.getShippingAddress(),mDto.getShippingMobile(),mDto.getShippingZipcode());
            CouponDTO couponDTO=mDto.getCoupon();
            if (null==couponDTO) {
            	 mNet.orderConfirm(mDto.getId(),"",mDto.getPayChannel(),mDto.getNote(),addressDTO);
			}else {
				 mNet.orderConfirm(mDto.getId(),couponDTO.getId(),mDto.getPayChannel(),mDto.getNote(),addressDTO);
			}
           
            mIvPay.setEnabled(false);
        }
    }

    private void onCancelOrder() {
        showLoading();
        mNet.cancelOrder(mDto.getId());
    }

    private void onContactHeler() {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("petid", Constants.OFFICIAL_ID);
        startActivity(intent);
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_CANCELORDER:
                showToast("取消成功");
                this.finish();
                break;
            case RequestCode.REQUEST_ORDERCONFIRM:
                PayTools.turnToPay(this, bean.getValue());
                break;
            case RequestCode.REQUEST_ORDERDETAILS:
                mDto = JsonUtils.resultData(bean.getValue(),OrderDTO.class);
                initValueView();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PayTools.REQUEST_CODE_PAYMENT){
            boolean state = PayTools.managePayResult(this, requestCode, resultCode, data,mDto);
            mIvPay.setEnabled(!state);
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
        switch (requestCode){
            case RequestCode.REQUEST_ORDERCONFIRM:
                mIvPay.setEnabled(true);
                break;
        }
    }
}
