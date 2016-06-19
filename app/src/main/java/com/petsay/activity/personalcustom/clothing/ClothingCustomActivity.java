package com.petsay.activity.personalcustom.clothing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.TagView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.component.view.postcard.ClothingTypeView;
import com.petsay.component.view.postcard.ClothingTypeView.OnTagItemClickListener;
import com.petsay.component.view.postcard.CustomGoodsSpecsView;
import com.petsay.component.view.postcard.EditPetInfoView;
import com.petsay.component.view.postcard.OrderEditView;
import com.petsay.component.view.postcard.OrderEditView.OnOrderButtonClickListener;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderItemDTO;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;
import com.petsay.vo.personalcustom.OrderVo;
import com.petsay.vo.personalcustom.PostCardVo;
import com.petsay.vo.personalcustom.ProductDTO;
import com.petsay.vo.personalcustom.SpecDTO;
import com.petsay.vo.personalcustom.SpecValueDTO;
import com.petsay.vo.petalk.PetTagVo;

/**
 * 
 * @author GJ
 *服装定制信息（添加地址，选择衣服样式）
 */
public class ClothingCustomActivity extends BaseActivity implements NetCallbackInterface,OnClickListener{
	private TitleBar mTitleBar;
	private LoopImgPagerView mLoopImgPagerView;
//	private ClothingTypeView mClothingTypeView;
	private TextView mTvClothingName,mTvClothingTitle;
	private ImageView mImgThumb;
	private EditPetInfoView mEditPetInfoView;
	private AddOrderView mAddOrderView;
	private OrderEditView mOrderEditView;
	private TextView mTvPostage;
	private TextView mTvOnePrice;
	private LinearLayout mLayoutSpecs;
//	private LinearLayout mLayoutPrice,mLayoutMPrice;
	
	private ArrayList<OrderProductSpecDTO> mPetinfoSpecDTOs;
	private ProductDTO mProductDTO;
	private List<SpecDTO> mSpecDTOs;
	private ArrayList<OrderVo> mOrderVos=new ArrayList<OrderVo>();
	//选择或者已编辑好的商品规则信息(需要提交的信息)
	private List<OrderProductSpecDTO> mSelectedSpecList=new ArrayList<OrderProductSpecDTO>();
	private Map<String, Float> offsetPriceMap=new HashMap<String, Float>();
	private float onePrice=0;
	
	private OrderNet mOrderNet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clothing_custom);
		mProductDTO=(ProductDTO) getIntent().getSerializableExtra("ProductDto");
		mOrderNet=new OrderNet();
		mOrderNet.setCallback(this);
		mOrderNet.setTag(this);
		initView();
		initData();
		setListener();
	}
	
	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mEditPetInfoView=(EditPetInfoView) findViewById(R.id.view_editpetinfo);
		initTitleBar("定制信息",true);		
		mAddOrderView=(AddOrderView) findViewById(R.id.view_addorder);
		mTvClothingName=(TextView) findViewById(R.id.tv_name);
		mTvClothingTitle=(TextView) findViewById(R.id.tv_title);
		mImgThumb=(ImageView) findViewById(R.id.img_thumb);
		mOrderEditView=(OrderEditView) findViewById(R.id.ordereditview);
		mTvPostage=(TextView) findViewById(R.id.tv_postage);
		mTvOnePrice=(TextView) findViewById(R.id.tv_oneprice);
		mLayoutSpecs=(LinearLayout) findViewById(R.id.layout_specs);
		mSpecDTOs=mProductDTO.getSpecs();
		onePrice=mProductDTO.getPrice();
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < mSpecDTOs.size(); i++) {
			SpecDTO specDTO= mSpecDTOs.get(i);
			OrderProductSpecDTO orderProductSpecDTO=new OrderProductSpecDTO();
			orderProductSpecDTO.setId(specDTO.getId());
			orderProductSpecDTO.setName(specDTO.getName());
			orderProductSpecDTO.setValue("");
			mSelectedSpecList.add(orderProductSpecDTO);
			if (null!=specDTO.getValues()&&!specDTO.getValues().isEmpty()) {
				offsetPriceMap.put(specDTO.getId(), specDTO.getValues().get(0).getPrice());
				CustomGoodsSpecsView customGoodsSpecsView=new CustomGoodsSpecsView(this);
				customGoodsSpecsView.setLayoutParams(layoutParams);
				customGoodsSpecsView.initData(specDTO, R.drawable.tag_normal,R.drawable.selected_tag_bg_blue, PublicMethod.getDisplayWidth(this),new OnTagItemClickListener() {
					
					@Override
					public void onItemClick(SpecValueDTO specValueDTO) {
						for (int j = 0; j < mSelectedSpecList.size(); j++) {
							OrderProductSpecDTO orderProductSpecDTO=mSelectedSpecList.get(j);
							if (orderProductSpecDTO.getId().equals(specValueDTO.getSpecId())) {
								onePrice-=offsetPriceMap.get(specValueDTO.getSpecId());
								orderProductSpecDTO.setValue(specValueDTO.getId()); 
								mSelectedSpecList.set(j, orderProductSpecDTO);
								onePrice+=specValueDTO.getPrice();
								mTvOnePrice.setText("专属价格：￥"+(onePrice));
								mAddOrderView.setOrderCount(mOrderEditView.getOrderCount(), onePrice);
								break;
							}
						}
					}
				});
				mLayoutSpecs.addView(customGoodsSpecsView);
				
				Iterator it = offsetPriceMap.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					float value = offsetPriceMap.get(key);
					onePrice+=value;
				}
				
			}
		}
//		List<SpecValueDTO> specValueDTOs=mProductDTO.getSpecs().get(0).getValues();
//		mSpecValueDTOs=new SpecValueDTO[mProductDTO.getSpecs().get(0).getValues().size()];
//		mSelectedSpecValueDTO=specValueDTOs.get(0);
		mTvPostage.setText("运费为："+mProductDTO.getShippingFee()+"元");
		mTvOnePrice.setText("专属价格：￥"+(onePrice));
		mAddOrderView.setOrderCount(1, onePrice);
//		mClothingTypeView.setTagTextColor(Color.BLACK);
//		mClothingTypeView.setTags(mSpecValueDTOs,  R.drawable.tag_normal,R.drawable.selected_tag_bg_blue, 720);
        
	}
	
	@Override
	protected void onResume() {
		try {
			mPetinfoSpecDTOs = (ArrayList<OrderProductSpecDTO>) DataFileCache.getSingleton().loadObject("petinfo_"+ UserManager.getSingleton().getActivePetId());
		} catch (Exception e) {
			mPetinfoSpecDTOs = new ArrayList<OrderProductSpecDTO>();
		}
        mEditPetInfoView.setInfoData(mPetinfoSpecDTOs);
		super.onResume();
	}
	
	private void setListener(){
		mAddOrderView.setClickListener(this);
		mOrderEditView.setOnOrderButtonClickListener(new OnOrderButtonClickListener() {
			
			@Override
			public void onMinusClick(int count) {
				mAddOrderView.setOrderCount(count,onePrice);
			}
			
			@Override
			public void onAddClick(int count) {
				mAddOrderView.setOrderCount(count,onePrice);
			}
		});
	}
	
	private void initData(){
		ImageLoaderHelp.displayContentImage(mProductDTO.getCover(), mImgThumb);
		mTvClothingName.setText(mProductDTO.getName());
		mTvClothingTitle.setText(mProductDTO.getTitle());
	}
	
	
	
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKUSERLIST:
			break;
		case RequestCode.REQUEST_ORDERCREATE:
			OrderDTO orderDTO=JsonUtils.resultData(bean.getValue(), OrderDTO.class);
			Intent intent =new Intent(this,OrderConfirmActivity.class);
			intent.putExtra("OrderDTO", orderDTO);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_count:
//			Intent intent=new Intent(this, PostCardOrderListActivity.class);
//			intent.putExtra("orderlist", mOrderVos);
//			startActivity(intent);
			break;
//		case R.id.layout_price:
//			
//			break;
		case R.id.btn_commit_order:
			
			if (null!=mPetinfoSpecDTOs&&!mPetinfoSpecDTOs.isEmpty()) {
				showLoading(false);
				String orderItems="";
				for (int i = 0; i <mSelectedSpecList.size(); i++) {
					for (int j = 0; j < mPetinfoSpecDTOs.size(); j++) {
						if (mSelectedSpecList.get(i).getId().equals(mPetinfoSpecDTOs.get(j).getId())) {
							mSelectedSpecList.get(i).setValue(mPetinfoSpecDTOs.get(j).getValue());
						}
					}
				}
				
				List<OrderItemDTO> orderItemDTOs = new ArrayList<OrderItemDTO>();
				OrderItemDTO orderItemDTO = new OrderItemDTO();
				orderItemDTO.setProductId(mProductDTO.getId());
				orderItemDTO.setProductUpdateTime(mProductDTO.getUpdateTime());
				orderItemDTO.setUseCard(false);
				orderItemDTO.setCount(mOrderEditView.getOrderCount());
				orderItemDTO.setSpecs(mSelectedSpecList);
				orderItemDTOs.add(orderItemDTO);
				
				
				orderItems=JSONArray.toJSONString(orderItemDTOs);
				mOrderNet.orderCreate(orderItems);
			}
			
			break;
		default:
			break;
		}
	}
	
}
