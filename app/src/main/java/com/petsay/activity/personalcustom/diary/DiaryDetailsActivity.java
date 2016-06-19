package com.petsay.activity.personalcustom.diary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.component.view.IndicatorViewPage;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.component.view.postcard.OrderEditView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.network.net.ProductNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderItemDTO;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;
import com.petsay.vo.personalcustom.ProductDTO;
import com.petsay.vo.personalcustom.ProductDescPicDTO;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/10
 * @Description 成长笔记详情页面
 */
public class DiaryDetailsActivity extends BaseActivity implements NetCallbackInterface, OrderEditView.OnOrderButtonClickListener, View.OnClickListener {

    @InjectView(R.id.ivpage)
    private IndicatorViewPage mVPager;
    @InjectView(R.id.tv_total_money)
    private TextView mTvTotalMoney;
    @InjectView(R.id.tv_numlbl)
    private TextView mTvNumlbl;
    @InjectView(R.id.ordereditview)
    private OrderEditView mOrdereditview;
    @InjectView(R.id.tv_freight)
    private TextView mTvFreight;
    @InjectView(R.id.view_addorder)
    private AddOrderView mViewAddorder;
    @InjectView(R.id.ll_desc)
    private LinearLayout mLlDesc;
    @InjectView(R.id.tv_name)
    private TextView mTvName;
    @InjectView(R.id.tv_description)
    private TextView mTvDescription;

    private ProductDTO mProduct;
    private ProductNet mNet;
    private OrderNet mOrderNet;
    private int mTotalelements;
    private float mPrice;
//    private float mGroupPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarydetails);
        mTotalelements = getIntent().getIntExtra("totalelements",0);
        initData();
        initView();
    }

    private void initData() {
        showLoading();
        mNet = new ProductNet();
        mNet.setCallback(this);
        mNet.productDetailSpecsByCategory(2);

        mOrderNet = new OrderNet();
        mOrderNet.setCallback(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("萌宠日记", true);
        mViewAddorder.setOrderIcon(R.drawable.diary_order_icon);
        mOrdereditview.setTextColor(Color.parseColor("#FE9053"));
        mOrdereditview.setTextSize(26);
        mOrdereditview.setTextBold(true);
        setListener();
//        test();
    }

    @Override
    protected void initTitleBar(String title, boolean finsihEnable) {
        super.initTitleBar(title, finsihEnable);
//        ImageView img = PublicMethod.addTitleRightIcon(mTitleBar, R.drawable.rank_button_rule);
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DiaryDetailsActivity.this, ShippingAddressManagActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void initMoneyTextView(){
//        mGroupPrice = mTotalelements * mPrice;
        String str = "￥ "+mPrice+" 元";// (￥"+mPrice+"元/页 共"+mTotalelements+"页)";
        SpannableString sp = new SpannableString(str);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(26,true);
        sp.setSpan(sizeSpan,1,str.indexOf("元"),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTotalMoney.setText(sp);
    }

    private void setListener() {
        mOrdereditview.setOnOrderButtonClickListener(this);
        mViewAddorder.setClickListener(this);
    }

//    private void test() {
//        String[] urls = new String[4];
//        urls[0] = "http://img0.tuicool.com/auAFfu.jpg";
//        urls[1] = "http://images.cnitblog.com/blog/284122/201502/051531127968461.png";
//        urls[2] = "http://images.cnitblog.com/blog/284122/201502/051531164538579.png";
//        urls[3] = "http://bj.010lm.com/data/attachment/block/be/beecfd29f9fe414657de750ea846a549.jpg";
//        mVPager.setData(urls);
//    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        closeLoading();
        switch (requestCode){
            case RequestCode.REQUEST_PRODUCTDETAILSPECSBYCATEGORY:
                onGetProductDetails(bean);
                break;
            case RequestCode.REQUEST_ORDERCREATE:
                OrderDTO orderDTO=JsonUtils.resultData(bean.getValue(), OrderDTO.class);
                Intent intent =new Intent(this,OrderConfirmActivity.class);
                intent.putExtra("OrderDTO", orderDTO);
                startActivity(intent);
                break;
        }
    }

    private void onGetProductDetails(ResponseBean bean) {
        mProduct = JsonUtils.resultData(bean.getValue(), ProductDTO.class);
        mPrice = mProduct.getPrice();
        initMoneyTextView();
        initDescView();
        updateOrderView(1);
        initSlidingView();
//        if(mProduct.getShippingFee() > 0){
            mTvFreight.setVisibility(View.VISIBLE);
            mTvFreight.setText("运费为:" + mProduct.getShippingFee() +"元");
//        }

        mTvName.setText(mProduct.getName());
        mTvDescription.setText(mProduct.getTitle());
    }

    private void initDescView() {
        if(mProduct == null || mProduct.getDescPics() == null)
            return;
        mLlDesc.removeAllViews();
        for (int i=0;i<mProduct.getDescPics().size();i++){
            ProductDescPicDTO pic = mProduct.getDescPics().get(i);
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            img.setScaleType(ImageView.ScaleType.MATRIX);
            params.topMargin = 5;
            ImageLoaderHelp.displayContentImage(pic.getPic(),img);
            mLlDesc.addView(img,params);
        }
    }

    private void initSlidingView() {
        if(mProduct == null || mProduct.getPics() == null)
            return;
        String[] arr = new String[mProduct.getPics().size()];
        for (int i= 0;i<mProduct.getPics().size();i++){
            arr[i] = mProduct.getPics().get(i).getPic();
        }
        mVPager.setData(arr);
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        onErrorShowToast(error);
    }

    @Override
    public void onAddClick(int count) {
        updateOrderView(mOrdereditview.getOrderCount());
    }

    @Override
    public void onMinusClick(int count) {
        updateOrderView(mOrdereditview.getOrderCount());
    }

    private void updateOrderView(int count){
        mViewAddorder.setOrderCount(count, mPrice);
    }

    @Override
    public void onClick(View v) {
        if(mTotalelements < 100){
            showToast("说说不足100条，无法制作哦，继续加油发说说吧~");
        }else {
            onCreateOrder();
        }

    }

    private void onCreateOrder() {
        showLoading();
        List<OrderItemDTO> items  = new ArrayList<OrderItemDTO>(1);
//        for (int i=0;i<mOrdereditview.getOrderCount();i++){
            OrderItemDTO dto = new OrderItemDTO();
            dto.setProductId(mProduct.getId());
            dto.setProductUpdateTime(mProduct.getUpdateTime());
            dto.setCount(mOrdereditview.getOrderCount());
            List<OrderProductSpecDTO> orderProductSpecDTOs=new ArrayList<OrderProductSpecDTO>();
            OrderProductSpecDTO dto1=new OrderProductSpecDTO();
            dto1.setId(mProduct.getSpecs().get(0).getId());
            dto1.setValue(mProduct.getSpecs().get(0).getValues().get(0).getId());
            orderProductSpecDTOs.add(dto1);
            dto.setSpecs(orderProductSpecDTOs);
            items.add(dto);
//        }
        String orderItems= JSONArray.toJSONString(items);
        mOrderNet.orderCreate(orderItems);
    }


}
