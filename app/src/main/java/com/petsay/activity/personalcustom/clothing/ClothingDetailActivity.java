package com.petsay.activity.personalcustom.clothing;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.activity.personalcustom.box.GoodsCustomActivity;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.postcard.ContactUsView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ProductNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.StringUtiles;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.ProductDTO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author GJ
 *衣服详情
 */
public class ClothingDetailActivity extends BaseActivity implements NetCallbackInterface,OnClickListener, IAddShowLocationViewService{
	private TitleBar mTitleBar;
	private LoopImgPagerView mLoopImgPagerView;
	private Button mBtnAddCustom;
	private TextView mTvClothingName,mTvClothingTitle,mTvPrice,mTvPostage;
	private RelativeLayout mLayoutRoot;
	private LinearLayout mLayoutDetail;
	
	private ProductDTO mProductDTO;
	private DisplayImageOptions mDisplayImageOptions; 
	private ProductNet mProductNet;
	private ContactUsView mContactUsView;
//	private ArrayList<OrderVo> mOrderVos=new ArrayList<OrderVo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clothing_detail);
		mDisplayImageOptions=ImageLoaderHelp.GetOptions(R.drawable.pet1,ImageScaleType.NONE);
		mProductDTO=(ProductDTO) getIntent().getSerializableExtra("ProductDTO");
		
		initView();
		setListener();
		mProductNet=new ProductNet();
		mProductNet.setCallback(this);
		mProductNet.setTag(this);
		if (null==mProductDTO) {
			mProductNet.productDetailSpecs(getIntent().getStringExtra("categoryId"));
		}else {
			mProductNet.productDetailSpecs(mProductDTO.getId());
		}
		
	}
	
	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mLoopImgPagerView=(LoopImgPagerView) findViewById(R.id.loopImgView);
		mBtnAddCustom=(Button) findViewById(R.id.btn);
		mLayoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		mContactUsView=(ContactUsView) findViewById(R.id.view_contactus);
		mContactUsView.initPopup(this);
		initTitleBar("详情",true);		
		mTvClothingName=(TextView) findViewById(R.id.tv_name);
		mTvClothingTitle=(TextView) findViewById(R.id.tv_title);
		mTvPrice=(TextView) findViewById(R.id.tv_price);
		mTvPostage=(TextView) findViewById(R.id.tv_postage);
		mLayoutDetail=(LinearLayout) findViewById(R.id.layout_detail);
		
	}
	
	private void setListener(){
		mBtnAddCustom.setOnClickListener(this);
		
	}
	
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_PRODUCTDETAILSPECS:
			mProductDTO=JsonUtils.resultData(bean.getValue(), ProductDTO.class);
			
			int length=mProductDTO.getPics().size();
			String[] imgUrls=new String[length];
			for (int i = 0; i <length; i++) {
				imgUrls[i]=mProductDTO.getPics().get(i).getPic();
			}
			mLoopImgPagerView.setImgUrls(imgUrls);
			mTvClothingName.setText(mProductDTO.getName());
			mTvClothingTitle.setText(mProductDTO.getTitle());
			mTvPostage.setText("运费： ￥"+mProductDTO.getShippingFee());
			String content="优惠价格：￥"+mProductDTO.getPrice();
			mTvPrice.setText(StringUtiles.formatSpannableString(content, 5, content.length(), Color.rgb(0xff, 0x74, 0x71), 18));
			LinearLayout.LayoutParams layoutParams=new LayoutParams(LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 10, 0, 0);
			for (int i = 0; i < mProductDTO.getDescPics().size(); i++) {
				ImageView imageView=new ImageView(this);
				imageView.setLayoutParams(layoutParams);
				imageView.setImageResource(R.drawable.pet1);
				imageView.setAdjustViewBounds(true);
				mLayoutDetail.addView(imageView);
				ImageLoaderHelp.displayImage(mProductDTO.getDescPics().get(i).getPic(), imageView,mDisplayImageOptions);
			}
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
		case R.id.btn:
			if (null==mProductDTO) {
				ToastUtiles.showDefault(getApplicationContext(), "获取商品信息失败");
			}else {
				Intent intent=new Intent();
				if (mProductDTO.getCategory().getId().equals("3")) {
					 intent.setClass(this, ClothingCustomActivity.class);
				}else {
					intent.setClass(this, GoodsCustomActivity.class);
				}
				intent.putExtra("ProductDto", mProductDTO);
				startActivity(intent);
			}
			
			break;
		case R.id.layout_count:
//			Intent intent=new Intent(this, PostCardOrderListActivity.class);
//			intent.putExtra("orderlist", mOrderVos);
//			startActivity(intent);
			break;
		}
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return mLayoutRoot;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}
}
