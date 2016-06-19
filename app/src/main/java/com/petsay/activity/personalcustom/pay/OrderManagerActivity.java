package com.petsay.activity.personalcustom.pay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.fragment.OrderListFragment;
import com.petsay.activity.petalk.rank.RankFragment;
import com.petsay.component.view.SlidingView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.vo.personalcustom.OrderVo;

/**
 * 
 * @author GJ
 *订单管理
 */
public class OrderManagerActivity extends BaseActivity {
	private TitleBar mTitleBar;
	private SlidingView mSlidingView;
	
	
	private ArrayList<OrderVo> mOrderVos=new ArrayList<OrderVo>();
	private int mTotalCount=0;
	private float mTotalPrice=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordermanager);
		initView();
		initSlidingView();
	}
	
	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar("订单管理",true);
		mSlidingView=(SlidingView) findViewById(R.id.view_sliding);
		mOrderVos=(ArrayList<OrderVo>) getIntent().getSerializableExtra("orderlist");
	}
	
	 private void initSlidingView() {
	        String[] titles = {"待支付","待发货","待收货","已完成"};
	        OrderListFragment fragment0 = OrderListFragment.getInstance(0);
	        OrderListFragment fragment1 = OrderListFragment.getInstance(1);
	        OrderListFragment fragment2 = OrderListFragment.getInstance(2);
	        OrderListFragment fragment3 = OrderListFragment.getInstance(3);
	        List<Fragment> list = new ArrayList<Fragment>();
	        list.add(fragment0);
	        list.add(fragment1);
	        list.add(fragment2);
	        list.add(fragment3);
	        mSlidingView.initView(getSupportFragmentManager(),titles,list, Color.parseColor("#85CBFC"), Color.parseColor("#646464"), true, Color.WHITE,Color.parseColor("#00000000"));
	        mSlidingView.hideCursorView(false);
	    }
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}
	
}
