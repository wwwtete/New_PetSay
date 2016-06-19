package com.petsay.activity.personalcustom.postcard;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.GalleryView;
import com.petsay.component.view.GalleryView.OnItemClickListener;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.component.view.postcard.ContactUsView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.network.net.ProductNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.StringUtiles;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderItemDTO;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;
import com.petsay.vo.personalcustom.OrderVo;
import com.petsay.vo.personalcustom.PostCardVo;
import com.petsay.vo.personalcustom.ProductDTO;
import com.petsay.vo.petalk.PetalkVo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author GJ 定制明信片首页
 */
public class CustomPostCardActivity extends BaseActivity
		implements NetCallbackInterface, OnClickListener, IAddShowLocationViewService {
	private TitleBar mTitleBar;
	private GalleryView mGalleryview;
	private AddOrderView mAddOrderView;
	private ImageView mImgAddCustom;
	private RelativeLayout mLayoutCard;
	private PostCardView mCardView, mNextCardView;
	private LinearLayout mLayoutDescription;
	private TextView mTvOneprice;
	private LinearLayout mLayoutDetail;
	private TextView mTvPostage, mTvDesc;
	private RelativeLayout mLayoutRoot;
	private ContactUsView mContactUsView;
	private SayDataNet mSayDataNet;
	private ProductNet mProductNet;
	private OrderNet mOrderNet;

	private List<PetalkVo> mPetalkVos = new ArrayList<PetalkVo>();
	private boolean isLoadingMore = true;
	private final int pageSize = 10;
	public static PostCardVo mPostCardVo;
	private int selectPosition = 0;
	private ArrayList<OrderVo> mOrderVos = new ArrayList<OrderVo>();
	private ProductDTO mProductDTO;
	private int mCurrentOrientation = Configuration.ORIENTATION_PORTRAIT;
	private boolean isFirstLoad = true;
	private float downX = 0, upX = 0;
	private final int Limit_Order_Count = 20;
	private final int From_OrderList_Ac = 100;
	public static final int MinCount=8;
	private TranslateAnimation mAnimLeftout, mAnimLeftin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_postcard);
		initPostCardVo();
		initView();
		setListener();
		initAnim();
		mSayDataNet = new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		netwrok(false);

		mProductNet = new ProductNet();
		mProductNet.setCallback(this);
		mProductNet.setTag(this);
		getProductDetail();

		// mSayDataNet.petalkUserList(UserManager.getSingleton().getActivePetId(),
		// Constants.ORIGINAL, "", 5, false);
		// showLoading(true);
	}

	private void getProductDetail() {
		mProductNet.productDetailSpecsByCategory(1);
	}

	protected void initView() {
		super.initView();
		mGalleryview = (GalleryView) findViewById(R.id.galleryview);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		mImgAddCustom = (ImageView) findViewById(R.id.img_addcustom);
		mAddOrderView = (AddOrderView) findViewById(R.id.view_addorder);
		mLayoutCard = (RelativeLayout) findViewById(R.id.layout_card);
		mLayoutDescription = (LinearLayout) findViewById(R.id.layout_description);
		mTvOneprice = (TextView) findViewById(R.id.tv_oneprice);
		mLayoutDetail = (LinearLayout) findViewById(R.id.layout_detail);
		mTvPostage = (TextView) findViewById(R.id.tv_postage);
		mTvDesc = (TextView) findViewById(R.id.tv_title1);
		mLayoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		mContactUsView = (ContactUsView) findViewById(R.id.view_contactus);
		mContactUsView.initPopup(this);
		initTitleBar(getIntent().getStringExtra("title"), true);

		mImgAddCustom.setOnClickListener(this);
		mAddOrderView.setClickListener(this);

		mCardView = new PostCardView(CustomPostCardActivity.this, mPostCardVo, "");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				(mPostCardVo.getHeight() * PublicMethod.getDisplayWidth(this)) / mPostCardVo.getWidth());
		mCardView.setLayoutParams(layoutParams);

		mNextCardView = new PostCardView(CustomPostCardActivity.this, mPostCardVo, "");
		mNextCardView.setLayoutParams(layoutParams);

		mLayoutCard.addView(mNextCardView);
		mLayoutCard.addView(mCardView);
		mAddOrderView.setEnabled(false);
	}

	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}

	private void setListener() {
		mGalleryview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View itemView, int position) {
				selectPosition = position;
				mCardView.setPetalkVo(mPetalkVos.get(position));
			}
		});

		mGalleryview.setOnRecylerViewScrollListener(new OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
				int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
				int totalItemCount = mLayoutManager.getItemCount();
				if (lastVisibleItem >= totalItemCount - 4 && dx > 0) {
					if (isLoadingMore) {
						// Log.d(TAG,"ignore manually update!");
					} else {
						isLoadingMore = true;
						netwrok(true);// 这里多线程也要手动控制isLoadingMore
					}
				}
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mCurrentOrientation = newConfig.orientation;
		if (isLandscape(mCurrentOrientation)) {
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
			mTitleBar.setVisibility(View.GONE);
			mGalleryview.setVisibility(View.GONE);
			mImgAddCustom.setVisibility(View.GONE);
			mLayoutDescription.setVisibility(View.GONE);
			mAddOrderView.setVisibility(View.GONE);
			setCardViewWH(60);
		} else {
			mTitleBar.setVisibility(View.VISIBLE);
			mGalleryview.setVisibility(View.VISIBLE);
			mImgAddCustom.setVisibility(View.VISIBLE);
			mLayoutDescription.setVisibility(View.VISIBLE);
			mAddOrderView.setVisibility(View.VISIBLE);
			setCardViewWH(0);
		}
	}

	/**
	 * 联网获取数据
	 * 
	 * @param pageIndex
	 * @param what
	 */
	private void netwrok(boolean isMore) {
		String id = "";
		if (isMore) {
			if (mPetalkVos != null && !mPetalkVos.isEmpty()) {
				id = ((PetalkVo) mPetalkVos.get(mPetalkVos.size() - 1)).getPetalkId();
				mSayDataNet.petalkList4Postcard(id, pageSize, isMore);
			}
		} else {
			mSayDataNet.petalkList4Postcard("", pageSize, isMore);
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKLIST4POSTCARD:
			try {
				List<PetalkVo> tempList;
				tempList = JsonUtils.parseList(bean.getValue(), PetalkVo.class);
				mPetalkVos.addAll(tempList);
				mGalleryview.setList(tempList);
				if (isFirstLoad && !mPetalkVos.isEmpty()) {
					isFirstLoad = false;
					mCardView.setPetalkVo(mPetalkVos.get(0));
					for (int i = 0; i < tempList.size(); i++) {
						ImageLoaderHelp.getImageLoader().loadImage(tempList.get(i).getPhotoUrl(),
								ImageLoaderHelp.contentPetImgOptions, null);
					}
					if (mPetalkVos.size() > 1) {
						mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			isLoadingMore = false;
			break;
		case RequestCode.REQUEST_PRODUCTDETAILSPECSBYCATEGORY:
			mProductDTO = JsonUtils.resultData(bean.getValue(), ProductDTO.class);
			String content = "优惠价格：￥" + mProductDTO.getPrice() + "元/张";
			mTvOneprice.setText(StringUtiles.formatSpannableString(content, 5, content.length() - 3,
					Color.rgb(0xff, 0x8f, 0x53), 18));
			mTvPostage.setText("运费：" + mProductDTO.getShippingFee() + "元");

			mTvDesc.setText(mProductDTO.getTitle());
			mAddOrderView.setEnabled(true);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 10, 0, 0);
			final int imgWidth=PublicMethod.getDisplayWidth(this)-PublicMethod.getDiptopx(this, 20);
			for ( int i = 0; i < mProductDTO.getDescPics().size(); i++) {
				final int index=i;
			
				ImageView imageView = new ImageView(this);
				imageView.setLayoutParams(layoutParams);
				imageView.setAdjustViewBounds(true);
				// imageView.setScaleType(ScaleType.MATRIX);
				mLayoutDetail.addView(imageView);
				// imageView.setImageResource(R.drawable.guide1_pic);
//				ImageLoaderHelp.displayContentImage(mProductDTO.getDescPics().get(i).getPic(), imageView);
				ImageLoaderHelp.displayContentImage(mProductDTO.getDescPics().get(i).getPic(), imageView,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								int width = arg2.getWidth();
								int height = arg2.getHeight();
								int imgHeight=imgWidth*height/width;
								LinearLayout.LayoutParams layoutParams = new LayoutParams(imgWidth,imgHeight);
								mLayoutDetail.getChildAt(index+3).setLayoutParams(layoutParams);
							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								// TODO Auto-generated method stub

							}
						}, null);
			}
			break;
		case RequestCode.REQUEST_ORDERCREATE:
			OrderDTO orderDTO = JsonUtils.resultData(bean.getValue(), OrderDTO.class);
			Intent intent = new Intent(this, OrderConfirmActivity.class);
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

	private void initPostCardVo() {
		if (null == mPostCardVo) {
			mPostCardVo = JsonUtils.resultData(FileUtile.getStringFromFile(this, "postcardjson.json"),
					PostCardVo.class);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_addcustom:// 定制
			if (mPetalkVos.size() > selectPosition&&null!=mProductDTO) {
				addOrder(mPetalkVos.get(selectPosition));
			}else{
				ToastUtiles.showDefault(this, "获取商品信息错误");
			}
			break;
		case R.id.layout_count:
			if (mOrderVos.size() > 0) {
				Intent intent = new Intent(this, PostCardOrderListActivity.class);
				intent.putExtra("orderlist", mOrderVos);
				intent.putExtra("ProductDto", mProductDTO);
				startActivityForResult(intent, From_OrderList_Ac);
			}
			break;
		case R.id.btn_commit_order: // 立即下单

			int count = 0;
			for (int i = 0; i < mOrderVos.size(); i++) {
				count += mOrderVos.get(i).getCount();
			}
			if (count > 7) {
				showLoading(false);
				commitOrder();
			} else {
				ToastUtiles.showDefault(CustomPostCardActivity.this, "明信片最少选择"+MinCount+"张才能下单哦");
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 添加订单 根据说说id判断订单是否重复，如果重复则订单合并该订单count数+1
	 * 
	 * @param petalkVo
	 */
	private void addOrder(PetalkVo petalkVo) {
		if (mOrderVos.size() < Limit_Order_Count) {
			if (mOrderVos.isEmpty()) {
				mAddOrderView.addOrder(mProductDTO.getPrice());
				OrderVo orderVo = new OrderVo();
				orderVo.setId(petalkVo.getPetalkId());
				orderVo.setCount(1);
				orderVo.setDescription(petalkVo.getDescription());
				orderVo.setThumbnail(petalkVo.getPhotoUrl() + "?imageView2/2/w/100");
				mOrderVos.add(orderVo);
			} else {
				mAddOrderView.addOrder(mProductDTO.getPrice());
				String id = petalkVo.getPetalkId();
				for (int i = 0; i < mOrderVos.size(); i++) {
					if (id.equals(mOrderVos.get(i).getId())) {
						int count = mOrderVos.get(i).getCount();
						mOrderVos.get(i).setCount(count + 1);
						return;
					}
				}

				OrderVo orderVo = new OrderVo();
				orderVo.setId(petalkVo.getId());
				orderVo.setCount(1);
				orderVo.setDescription(petalkVo.getDescription());
				orderVo.setThumbnail(petalkVo.getPhotoUrl() + "?imageView2/2/w/100");
				mOrderVos.add(orderVo);
			}
		} else {
			ToastUtiles.showDefault(this, "只能最多同时购买" + Limit_Order_Count + "个");
		}
	}

	private void setCardViewWH(int marginValue) {
		int displayWidth = PublicMethod.getDisplayWidth(this) - PublicMethod.getDiptopx(this, marginValue);
		int displayHeight = PublicMethod.getDisplayHeight(this);
		int viewWidth, viewHeight;
		int displayScale = displayWidth / displayHeight;
		int displayOffset = displayWidth % displayHeight;
		int viewScale = mPostCardVo.getWidth() / mPostCardVo.getHeight();
		int viewOffset = mPostCardVo.getWidth() % mPostCardVo.getHeight();
		if (isLandscape(mCurrentOrientation)) {
			if (displayScale > viewScale) {
				viewWidth = displayWidth;
				viewHeight = (mPostCardVo.getHeight() * viewWidth) / mPostCardVo.getWidth();
			} else if (displayScale < viewScale) {
				viewHeight = displayHeight;
				viewWidth = (mPostCardVo.getWidth() * viewHeight) / mPostCardVo.getHeight();

			} else if (displayOffset > viewOffset) {
				viewWidth = displayWidth;
				viewHeight = (mPostCardVo.getHeight() * viewWidth) / mPostCardVo.getWidth();

			} else {
				viewHeight = displayHeight;
				viewWidth = (mPostCardVo.getWidth() * viewHeight) / mPostCardVo.getHeight();
			}
		} else {
			if (displayScale > viewScale) {
				viewHeight = displayHeight;
				viewWidth = (mPostCardVo.getWidth() * viewHeight) / mPostCardVo.getHeight();
			} else if (displayScale < viewScale) {
				viewWidth = displayWidth;
				viewHeight = (mPostCardVo.getHeight() * viewWidth) / mPostCardVo.getWidth();
			} else if (displayOffset > viewOffset) {
				viewHeight = displayHeight;
				viewWidth = (mPostCardVo.getWidth() * viewHeight) / mPostCardVo.getHeight();
			} else {
				viewWidth = displayWidth;
				viewHeight = (mPostCardVo.getHeight() * viewWidth) / mPostCardVo.getWidth();
			}
		}

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewWidth, viewHeight);
		mCardView.setLayoutParams(layoutParams);
		if (!mPetalkVos.isEmpty()) {
			mCardView.setPetalkVo(mPetalkVos.get(selectPosition));
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isLandscape(mCurrentOrientation)) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 横屏
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/***
	 * 是否横屏
	 * 
	 * @return
	 */
	private boolean isLandscape(int state) {
		if (state == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		} else {
			return false;
		}
	}

	private void turnPage(boolean isNext) {
		if (isNext && selectPosition < mPetalkVos.size() - 1) {
			mCardView.startAnimation(mAnimLeftout);

		} else if (!isNext && selectPosition > 0) {
			mCardView.startAnimation(mAnimLeftin);

		}
	}

	private void initAnim() {
		int durationValue = 2000;
		mAnimLeftin = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.5f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimLeftin.setDuration(durationValue);
		mAnimLeftin.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				selectPosition--;
				mCardView.setPetalkVo(mPetalkVos.get(selectPosition));
				mGalleryview.setReclyerViewSelection(selectPosition);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));

			}
		});

		mAnimLeftout = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.5f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimLeftout.setDuration(durationValue);
		mAnimLeftout.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				selectPosition++;
				mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));
				mGalleryview.setReclyerViewSelection(selectPosition);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCardView.setPetalkVo(mPetalkVos.get(selectPosition));
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == From_OrderList_Ac) {
			mOrderVos = (ArrayList<OrderVo>) data.getSerializableExtra("orderlist");
			mAddOrderView.setOrderCount(calOrderCount(), mProductDTO.getPrice());
			// mTvOneprice
		}
	}

	/**
	 * 计算订单数（包括重复的订单）
	 */
	private int calOrderCount() {
		int totalCount = 0;
		for (int i = 0; i < mOrderVos.size(); i++) {
			totalCount = totalCount + mOrderVos.get(i).getCount();
		}
		return totalCount;
	}

	private void commitOrder() {
		if (null == mOrderNet) {
			mOrderNet = new OrderNet();
			mOrderNet.setCallback(this);
			mOrderNet.setTag(this);
		}
		List<OrderItemDTO> orderItemDTOs = new ArrayList<OrderItemDTO>();
		for (int i = 0; i < mOrderVos.size(); i++) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setProductId(mProductDTO.getId());
			orderItemDTO.setProductUpdateTime(mProductDTO.getUpdateTime());
			orderItemDTO.setUseCard(false);
			orderItemDTO.setCount(mOrderVos.get(i).getCount());
			List<OrderProductSpecDTO> orderProductSpecDTOs = new ArrayList<OrderProductSpecDTO>();
			OrderProductSpecDTO dto1 = new OrderProductSpecDTO();
			dto1.setId("postcardTmplId");
			dto1.setValue(mProductDTO.getSpecs().get(0).getValues().get(0).getId());
			OrderProductSpecDTO dto2 = new OrderProductSpecDTO();
			dto2.setId("petalkId");
			dto2.setValue(mOrderVos.get(i).getId());
			orderProductSpecDTOs.add(dto1);
			orderProductSpecDTOs.add(dto2);
			orderItemDTO.setSpecs(orderProductSpecDTOs);
			orderItemDTOs.add(orderItemDTO);
		}
		String orderItems = JSONArray.toJSONString(orderItemDTOs);
		mOrderNet.orderCreate(orderItems);
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
