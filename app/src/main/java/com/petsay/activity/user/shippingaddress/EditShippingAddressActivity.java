package com.petsay.activity.user.shippingaddress;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.component.wheelview.CityWheelView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShippingAddressNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.VerifyUserUtile;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.user.UserShippingAddressDTO;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/13
 * @Description 编辑收货地址
 */
public class EditShippingAddressActivity extends BaseActivity implements NetCallbackInterface, View.OnClickListener {


//    @InjectView(R.id.ev_name)
//    private EditText evName;
//    @InjectView(R.id.ev_phonenum)
//    private EditText evPhonenum;
//    @InjectView(R.id.tv_lblcity)
//    private TextView tvLblcity;
//    @InjectView(R.id.ev_address)
//    private EditText evAddress;
//    @InjectView(R.id.ev_postalcode)
//    private EditText evPostalcode;
//    @InjectView(R.id.cb_default_toggle)
//    private CheckBox cbDefaultToggle;
//    @InjectView(R.id.fl_save)
//    private FrameLayout flSave;

    @InjectView(R.id.tv_name)
    private TextView mTvName;
    @InjectView(R.id.ev_name)
    private EditText mEvName;
    @InjectView(R.id.tv_phonenum)
    private TextView mTvPhonenum;
    @InjectView(R.id.ev_phonenum)
    private EditText mEvPhonenum;
    @InjectView(R.id.tv_lblcity)
    private TextView mTvLblcity;
    @InjectView(R.id.iv_city)
    private ImageView mIvCity;
    @InjectView(R.id.tv_address)
    private TextView mTvAddress;
    @InjectView(R.id.ev_address)
    private EditText mEvAddress;
    @InjectView(R.id.tv_postalcode)
    private TextView mTvPostalcode;
    @InjectView(R.id.ev_postalcode)
    private EditText mEvPostalcode;
    @InjectView(R.id.cb_default_toggle)
    private CheckBox mCbDefaultToggle;
    @InjectView(R.id.fl_save)
    private FrameLayout mFlSave;
    @InjectView(R.id.rlayout_wheel)
    private RelativeLayout mRlayoutWheel;
    @InjectView(R.id.wv_city)
    private CityWheelView mWvCity;
    @InjectView(R.id.btn_ok)
    private Button mBtnOk;



    private ShippingAddressNet mNet;
    /**0:添加地址  |  1：更新*/
    private int mType;
    private UserShippingAddressDTO mDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shippingaddress);
        initValue();
        initView();
    }

    private void initValue() {
        mNet = new ShippingAddressNet();
        mNet.setCallback(this);
        mNet.setTag(this);

        mDto = (UserShippingAddressDTO) getIntent().getSerializableExtra("usershippingaddress");
        mType = mDto == null ? 0 : 1;
    }

    @Override
    protected void initView() {
        super.initView();
        if(mType == 0){
            initTitleBar("新建收货地址", true);
        }else {
            initTitleBar("编辑收货地址", true);
            initValueView(mDto);
        }
        setListener();
    }

    private void initValueView(UserShippingAddressDTO dto) {
        if(dto == null)
            return;
        mEvName.setText(dto.getName());
        mEvPhonenum.setText(dto.getMobile());
        mTvLblcity.setText(dto.getCity());
        mEvAddress.setText(dto.getAddress());
        mEvPostalcode.setText(dto.getZipcode());
//        if(dto.getIsDefault())
//            mCbDefaultToggle.setSelected(true);
//        else
//            mCbDefaultToggle.setSelected(false);

        mCbDefaultToggle.setChecked(dto.getIsDefault());
    }

    private void setListener() {
        mIvCity.setOnClickListener(this);
        mTvLblcity.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mFlSave.setOnClickListener(this);

    }

//    @Override
//    protected void initTitleBar(String title, boolean finsihEnable) {
//        super.initTitleBar(title, finsihEnable);
//        TextView txt = PublicMethod.addTitleRightText(mTitleBar,"保存");
//        txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLoading();
//                onSaveShippingAdress();
//            }
//        });
//    }

    private boolean onCheckValue(){
        return VerifyUserUtile.verifyNotNull(mEvName) && VerifyUserUtile.verifyMobileNumber(this,mEvPhonenum) &&
                !TextUtils.isEmpty(mTvLblcity.getText()) && VerifyUserUtile.verifyNotNull(mEvAddress) && VerifyUserUtile.verifyNotNull(mEvPostalcode);
    }

    private void onGetValue() {
        if(mDto == null)
            mDto = new UserShippingAddressDTO();
        mDto.setName(getText(mEvName));
        mDto.setMobile(getText(mEvPhonenum));
        mDto.setCity(mWvCity.getSelectCity2());
        mDto.setAddress(getText(mEvAddress));
        mDto.setZipcode(getText(mEvPostalcode));
        mDto.setIsDefault(mCbDefaultToggle.isChecked());
        mDto.setProvince(mWvCity.getSelectProvince());
    }


    private String getText(EditText ev){
        if(ev != null)
            return ev.getText().toString();
        return "";
    }

    /**
     * 获取数据成功回调接口
     *
     * @param bean        服务器返回数据
     * @param requestCode 区分请求码
     */
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_CREATESHIPPINGADDRESS:
            	Intent intent=new Intent();
            	intent.putExtra("address", mDto);
            	setResult(OrderConfirmActivity.REQUEST_CODE_ADDRESS, intent);
            	finish();
                break;
            case RequestCode.REQUEST_UPDATESHIPPINGADDRESS:
                break;
        }
        this.finish();
    }

    /**
     * 获取数据失败回调接口(也包括服务器返回500的错误)
     *
     * @param error       错误信息类
     * @param requestCode 请求码
     */
    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }

    private void showCityView(boolean isShow){
        if(isShow){
            mRlayoutWheel.requestFocus();
            PublicMethod.closeSoftKeyBoard(this, mEvName);
        }
        mRlayoutWheel.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_ok:
                showCityView(false);
                mTvLblcity.setText(mWvCity.getSelectCity());
                break;
            case  R.id.iv_city:
            case  R.id.tv_lblcity:
                showCityView(true);
                break;
            case  R.id.fl_save:
                if(onCheckValue()){
                    onGetValue();
                    showLoading();
                    if(mType == 0){
                        mNet.createShippingAddress(mDto);
                    }else {
                        mNet.updateShippingAddress(mDto);
                    }
                }


                break;

        }
    }
}
