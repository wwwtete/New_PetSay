package com.petsay.activity.personalcustom.pay.fragment;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.petsay.R;
import com.petsay.activity.BaseFragment;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.personalcustom.pay.OrderDetailsActivity;
import com.petsay.activity.personalcustom.pay.OrderManagerActivity;
import com.petsay.activity.personalcustom.pay.OrderManagerListAdapter;
import com.petsay.component.view.NullTipView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.utile.PayTools;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.coupon.CouponDTO;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderVo;
import com.petsay.vo.user.UserShippingAddressDTO;

/**
 * 订单列表（未完成，已完成，待发货，待收货）
 * @author GJ
 *
 */
public class OrderListFragment extends BaseFragment implements NetCallbackInterface, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView pulltorefreshview;
    @InjectView(R.id.lv_order)
    private ListView lvRank;
    @InjectView(R.id.nulltip)
    private NullTipView mNullTip;

    private OrderManagerListAdapter mAdapter;
//    private SayDataNet mNet;

    private int mPageSize = 10;
//    private int mPageIndex = 0;
    private List<OrderVo> mOrderVos=new ArrayList<OrderVo>();
    private OrderNet mNet;

    /**0，待支付；   1，待发货；  2，待收货；     3，已完成*/
    private int mType;
    private OrderDTO mDto;

    /**
     * @param type 0，待支付；   1，待发货；  2，待收货；     3，已完成
     * @return
     */
    public static OrderListFragment getInstance(int type){
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public OrderListFragment(){
        mNet = new OrderNet();
        mNet.setCallback(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist,null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mType = getArguments().getInt("type",0);
//        mNet = new SayDataNet();
//        mNet.setTag(this);
//        mNet.setCallback(this);
//mOrderVos=new ArrayList<OrderVo>();
//        for (int i = 0; i < 3; i++) {
//			OrderVo orderVo=new OrderVo();
//			orderVo.setId(i+"");
//			orderVo.setDescription("今天晚上吃什么，喝什么，玩什么，明天你去啥地方拉开来的快放假啊时的说法哈喽啥地方快乐的说法");
//			orderVo.setPrice(100);
//			orderVo.setThumbnail("http://h.hiphotos.baidu.com/baike/g%3D0%3Bw%3D268/sign=e1d31c38bb389b5028ffe559f208d7eb/b21bb051f8198618e178e9a248ed2e738bd4e6dd.jpg");
//		    mOrderVos.add(orderVo);
//        }
        mAdapter = new OrderManagerListAdapter(this,mType);
        lvRank.setAdapter(mAdapter);
        setListener();
    }

    /**
     * 确认订单
     * @param view
     * @param dto
     */
    public void orderConfirm(View view, OrderDTO dto) {
        if("2".equals(dto.getState())) {
            showLoading(false);
            mDto = dto;
            CouponDTO couponDTO = dto.getCoupon();
            UserShippingAddressDTO addressDTO = new UserShippingAddressDTO(dto.getShippingName(), dto.getShippingProvince(), dto.getShippingCity(), dto.getShippingAddress(), dto.getShippingMobile(), dto.getShippingZipcode());
            if (null == couponDTO) {
                mNet.orderConfirm(dto.getId(), "", dto.getPayChannel(), dto.getNote(), addressDTO);
            } else {
                mNet.orderConfirm(dto.getId(), couponDTO.getId(), dto.getPayChannel(), dto.getNote(), addressDTO);
            }
        }
    }

    /**
     * 确认收货
     * @param view
     * @param dto
     */
    public void receiveProduct(View view,OrderDTO dto){
        if("5".equals(dto.getState())){
            mNet.receiveProduct(dto.getId());
        }
    }

    private void setListener() {
        pulltorefreshview.setOnFooterRefreshListener(this);
        pulltorefreshview.setOnHeaderRefreshListener(this);
        lvRank.setOnItemClickListener(this);
        mNullTip.setClickButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("tagindex",1);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
            netWork(false);
    }

    private void netWork(boolean isMore){
        showLoading();
        String startid = "";
        if(isMore && mAdapter.getCount() > 0){
            startid = mAdapter.getItem(mAdapter.getCount()-1).getId();
        }
        switch (mType){
            case 0:
                mNet.toPayList(startid,mPageSize,isMore);
                break;
            case 1:
                mNet.toShipList(startid,mPageSize,isMore);
                break;
            case 2:
                mNet.toReceiveList(startid,mPageSize,isMore);
                break;
            case 3:
                mNet.finishedList(startid,mPageSize,isMore);
                break;
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_TOPAYLIST:
            case RequestCode.REQUEST_TOSHIPLIST:
            case RequestCode.REQUEST_TORECEIVELIST:
            case RequestCode.REQUEST_FINISHEDLIST:
                onGetOrderList(bean);
                break;
            case RequestCode.REQUEST_RECEIVEPRODUCT:
                onReceiveProduct();
                break;
            case RequestCode.REQUEST_ORDERCONFIRM:
                PayTools.turnToPay(getActivity(), bean.getValue());
                break;
        }
        closeLoading();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PayTools.REQUEST_CODE_PAYMENT){
            boolean state = PayTools.managePayResult(getActivity(), requestCode, resultCode, data,mDto);
        }
    }

    private void onReceiveProduct() {
        netWork(true);
    }

    private void onGetOrderList(ResponseBean bean) {
        try {
            List<OrderDTO> dtos = JsonUtils.getList(bean.getValue(),OrderDTO.class);
            if(bean.isIsMore() && (dtos == null || dtos.isEmpty())){
                showToast(R.string.no_more);
            }
            if(bean.isIsMore()){
                mAdapter.addMoreData(dtos);
            }else {
                if(dtos == null || dtos.isEmpty()){
                    mNullTip.setVisibility(View.VISIBLE);
                }else {
                    mNullTip.setVisibility(View.GONE);
                }
                mAdapter.refreshData(dtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pulltorefreshview.onComplete(bean.isIsMore());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
        pulltorefreshview.onComplete(error.isIsMore());
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        netWork(false);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        netWork(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderDTO dto =  mAdapter.getItem(position);
        if(dto != null) {
            Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
            intent.putExtra("orderdto", dto);
            intent.putExtra("orderid", dto.getId());
            startActivity(intent);
        }
    }
}
