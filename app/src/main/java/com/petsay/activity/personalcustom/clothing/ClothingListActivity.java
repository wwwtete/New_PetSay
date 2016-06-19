package com.petsay.activity.personalcustom.clothing;

import java.util.List;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.LoopImgPagerView.OnLoopImgItemClickListener;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ProductNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.personalcustom.ProductDTO;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import roboguice.inject.InjectView;

/**
 * 服装定制
 * 
 * @author G
 */
public class ClothingListActivity extends BaseActivity implements NetCallbackInterface {
	private ListView lv;
	private TitleBar mTitleBar;
	private RelativeLayout layoutRoot;
	@InjectView(R.id.pulltorefreshview)
	private PullToRefreshView mPullView;

	private ClothingListAdapter mClothesListAdapter;
//	private List<ClothesVo> mClothesVos = new ArrayList<ClothesVo>();
	private List<SquareVo> squareVos;
	private int pageIndex = 0;
	private int pageSize = 10;

	
	private List<ProductDTO> mProductDTOs;
	private ProductNet mProductNet;
	private int mCategoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clothing_list);
		mCategoryId=getIntent().getIntExtra("categoryId",3);
		initView();
		
		initTitleBar(getIntent().getStringExtra("title"));
//		showLoading();
		
	
		
		mProductNet=new ProductNet();
		mProductNet.setCallback(this);
		mProductNet.setTag(this);
		onRefresh();
	
	}

	protected void initView() {
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		lv = (ListView) findViewById(R.id.lv);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.FILL_PARENT,
				android.widget.AbsListView.LayoutParams.WRAP_CONTENT);

		
		initPullToRefreshView();
		mClothesListAdapter = new ClothingListAdapter(ClothingListActivity.this);
		
		lv.setAdapter(mClothesListAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				int count=lv.getHeaderViewsCount();
				int selectedPosition=position-count;
				if (selectedPosition>=0) {
					ProductDTO dto = (ProductDTO) mClothesListAdapter.getItem(selectedPosition);
					if (null != dto) {
						Intent intent = new Intent(ClothingListActivity.this,ClothingDetailActivity.class);
						intent.putExtra("ProductDTO", dto);
						startActivity(intent);
					}
				}
			}
		});

	}

	protected void initTitleBar(String title) {
		mTitleBar.setTitleText(title);
		mTitleBar.setFinishEnable(true);
	
	}

	private void initPullToRefreshView() {
		mPullView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				onRefresh();
			}
		});

		mPullView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onAddMore();
			}
		});
	}

	private void onAddMore() {
		pageIndex++;
		mProductNet.productListByCategory(mCategoryId, pageIndex, pageSize,true);
	}

	private void onRefresh() {
		pageIndex=0;
		mProductNet.productListByCategory(mCategoryId, pageIndex, pageSize,false);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mClothesListAdapter && mClothesListAdapter.getCount() > 0) {
			mClothesListAdapter.notifyDataSetChanged();
		}
		// mTvCoin.setText("宠豆："+UserManager.getSingleton().getActivePetInfo().getCoin());
	}

	@Override
	protected void onPause() {
		GifViewManager.getInstance().stopGif();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PRODUCTLISTBYCATEGORY:
			closeLoading();
			try {
				mProductDTOs=JsonUtils.getList(bean.getValue(), ProductDTO.class);
				
				if (bean.isIsMore()) {
					mClothesListAdapter.addMore(mProductDTOs);
					mPullView.onFooterRefreshComplete();
				}else {
					mClothesListAdapter.refreshData(mProductDTOs);
					mPullView.onHeaderRefreshComplete();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_PRODUCTLISTBYCATEGORY:
			if (error.isIsMore()) {
				mPullView.onFooterRefreshComplete();
			}else {
				mPullView.onHeaderRefreshComplete();
			}
			break;

		default:
			break;
		}
	}

}