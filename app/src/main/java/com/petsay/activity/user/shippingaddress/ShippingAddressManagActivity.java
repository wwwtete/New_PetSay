package com.petsay.activity.user.shippingaddress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.component.view.swipelistview.BaseSwipeListViewListener;
import com.petsay.component.view.swipelistview.SwipeListView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ShippingAddressNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.user.UserShippingAddressDTO;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/11
 * @Description 购物地址管理器
 */
public class ShippingAddressManagActivity extends BaseActivity implements View.OnClickListener, NetCallbackInterface {

    @InjectView(R.id.fl_add)
    private FrameLayout mFlAdd;
    @InjectView(R.id.lv_address)
    private SwipeListView mLvAddress;

    private ShippingAddressAdapter mAdapter;
    private ShippingAddressNet mNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shippingaddressmanage);
        initVale();
        initView();
    }

    private void initVale() {
        mNet = new ShippingAddressNet();
        mNet.setCallback(this);
        mNet.setTag(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("地址管理", true);
        mLvAddress.setOffsetLeft(PublicMethod.getDisplayWidth(this) - PublicMethod.getDiptopx(this, 80));
        mAdapter = new ShippingAddressAdapter(this);
        mLvAddress.setAdapter(mAdapter);
        mLvAddress.setSwipeListViewListener(mSwipeListener);

        mFlAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,EditShippingAddressActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        onRefresh();
        super.onResume();

    }

    private void onRefresh(){
        showLoading();
        mNet.getListAddress();
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_LISTSHIPPINGADDRESS:
                closeLoading();
                try {
                    List<UserShippingAddressDTO> list = JsonUtils.getList(bean.getValue(),UserShippingAddressDTO.class);
                    mAdapter.refreshData(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RequestCode.REQUEST_DELETESHIPPINGADDRESS:
                onRefresh();
                break;
        }

    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }


    private BaseSwipeListViewListener mSwipeListener = new BaseSwipeListViewListener(){

        @Override
        public void onListChanged()
        {
            mLvAddress.closeOpenedItems();
        }

        @Override
        public void onClickFrontView(int position){
        	UserShippingAddressDTO dto=mAdapter.getItem(position);
        	Intent intent=new Intent();
        	intent.putExtra("address", dto);
        	setResult(OrderConfirmActivity.REQUEST_CODE_ADDRESS,intent);
        	finish();
        }

        @Override
        public void onClickBackView(int position){
            UserShippingAddressDTO dto =  mAdapter.remove(position);
            mLvAddress.closeOpenedItems();
            if(dto != null) {
                mNet.deleteShippingAddress(dto.getId());
            }
        }

    };

}
