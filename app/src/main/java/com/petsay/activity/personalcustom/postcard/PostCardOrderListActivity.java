package com.petsay.activity.personalcustom.postcard;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.activity.chat.ChatMsgListActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.chat.ChatDataBaseManager;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.TitleBar.OnClickBackListener;
import com.petsay.component.view.postcard.AddOrderView;
import com.petsay.component.view.postcard.OrderEditView;
import com.petsay.component.view.postcard.OrderEditView.OnOrderButtonClickListener;
import com.petsay.component.view.swipelistview.BaseSwipeListViewListener;
import com.petsay.component.view.swipelistview.SwipeListView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.OrderNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.chat.NewestMsg;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderItemDTO;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;
import com.petsay.vo.personalcustom.OrderVo;
import com.petsay.vo.personalcustom.ProductDTO;

/**
 * 
 * @author GJ
 *定制明信片首页
 */
public class PostCardOrderListActivity extends BaseActivity implements NetCallbackInterface, OnClickListener{
	private TitleBar mTitleBar;
	private SwipeListView mLv;
	private AddOrderView mAddOrderView;
	private ArrayList<OrderVo> mOrderVos=new ArrayList<OrderVo>();
	private ProductDTO mProductDTO;
	private OrderNet mOrderNet;
	private ListAdapter mAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postcard_list);
		mOrderVos=(ArrayList<OrderVo>) getIntent().getSerializableExtra("orderlist");
		mProductDTO=(ProductDTO) getIntent().getSerializableExtra("ProductDto");
		initView();
		setListener();
		mOrderNet=new OrderNet();
		mOrderNet.setCallback(this);
		mOrderNet.setTag(this);
		
	}
	
	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar("详情",false);
		mTitleBar.setOnClickBackListener(new OnClickBackListener() {
			
			@Override
			public void OnClickBackListener() {
				returnBack();
			}
		});
		mLv=(SwipeListView) findViewById(R.id.swipelistviwe);
		mAddOrderView=(AddOrderView) findViewById(R.id.view_addorder);
		mAddOrderView.setOrderCount(calOrderCount(), mProductDTO.getPrice());
		mAdapter=new ListAdapter(this); 
		mLv.setAdapter(mAdapter);
		mLv.setOffsetLeft(PublicMethod.getDisplayWidth(this)-PublicMethod.getDiptopx(this,80));
	}
	
	private void setListener(){
		mAddOrderView.setClickListener(this);
		 mLv.setSwipeListViewListener(mSwipeListener);
	}
	
	private void returnBack(){
		Intent intent=new Intent();
		intent.putExtra("orderlist", mOrderVos);
		setResult(100, intent);
		finish();
	}
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			returnBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 计算订单数（包括重复的订单）
	 */
	private int calOrderCount(){
		int totalCount=0;
		for (int i = 0; i < mOrderVos.size(); i++) {
			totalCount=totalCount+mOrderVos.get(i).getCount();
		}
		return totalCount;
	}
	
	private class ListAdapter extends BaseAdapter{

		private Context mContext;
		public ListAdapter(Context context){
			mContext=context;
		}
		
		@Override
		public int getCount() {
			if (null==mOrderVos||mOrderVos.isEmpty()) {
				return 0;
			}
			return mOrderVos.size();
		}

		@Override
		public Object getItem(int position) {
			if (null==mOrderVos||mOrderVos.isEmpty()) {
				return null;
			}
			return mOrderVos.get(position);
		}

		@Override
		public long getItemId(int position) {
			if (null==mOrderVos||mOrderVos.isEmpty()) {
				return 0;
			}
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Holder holder;
			if (null==convertView) {
				holder=new Holder();
				convertView=LayoutInflater.from(mContext).inflate(R.layout.order_list_item, null);
			    holder.img=(ImageView) convertView.findViewById(R.id.img_thumb);
			    holder.tvDescription=(TextView) convertView.findViewById(R.id.tv_description);
			    holder.orderEditView=(OrderEditView) convertView.findViewById(R.id.ordereditview);
			    convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			OrderVo orderVo=mOrderVos.get(position);
			holder.tvDescription.setText(orderVo.getDescription());
			ImageLoaderHelp.displayContentImage(orderVo.getThumbnail(), holder.img);
			holder.orderEditView.setOrderCount(orderVo.getCount());
			holder.orderEditView.setOnOrderButtonClickListener(new OnOrderButtonClickListener() {
				
				@Override
				public void onMinusClick(int count) {
//					int count=mOrderVos.get(position).getCount();
					mOrderVos.get(position).setCount(count);
					mAddOrderView.minusOrder(mProductDTO.getPrice());
				}
				
				@Override
				public void onAddClick(int count) {
//					int count=mOrderVos.get(position).getCount();
					mOrderVos.get(position).setCount(count);
					mAddOrderView.addOrder(mProductDTO.getPrice());
				}
			});
			return convertView;
		}
		
	}
	private class Holder{
		private TextView tvDescription;
		private ImageView img;
		private OrderEditView orderEditView;
		
	}
	
	private BaseSwipeListViewListener mSwipeListener = new BaseSwipeListViewListener(){

        @Override
        public void onListChanged()
        {
            mLv.closeOpenedItems();
        }

        @Override
        public void onClickFrontView(int position){ 
        	
        }

        @Override
        public void onClickBackView(int position)
        {
        	int count=mOrderVos.get(position).getCount();
        	mOrderVos.remove(position);
        	mAdapter.notifyDataSetChanged();
//           NewestMsg msg =  mAdapter.remove(position);
            mLv.closeOpenedItems();
            for (int i = 0; i < count; i++) {
				mAddOrderView.minusOrder(mProductDTO.getPrice());
			}
//            if(msg != null) {
//                ChatDataBaseManager.getInstance().deleteChatContacts(msg);
//            }
        }

    };
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit_order: //立即下单
			int count = 0;
			for (int i = 0; i < mOrderVos.size(); i++) {
				count += mOrderVos.get(i).getCount();
			}
			if (count > 7) {
				showLoading(false);
				commitOrder();
			} else {
				ToastUtiles.showDefault(PostCardOrderListActivity.this, "明信片最少选择"+CustomPostCardActivity.MinCount+"张才能下单哦");
			}
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}
	private void commitOrder(){
		if (null==mOrderNet) {
			mOrderNet=new OrderNet();
			mOrderNet.setCallback(this);
			mOrderNet.setTag(this);
		}
		List<OrderItemDTO> orderItemDTOs=new ArrayList<OrderItemDTO>();
		for (int i = 0; i < mOrderVos.size(); i++) {
			OrderItemDTO orderItemDTO=new OrderItemDTO();
			orderItemDTO.setProductId(mProductDTO.getId());
			orderItemDTO.setProductUpdateTime(mProductDTO.getUpdateTime());
			orderItemDTO.setUseCard(false);
			orderItemDTO.setCount(mOrderVos.get(i).getCount());
			List<OrderProductSpecDTO> orderProductSpecDTOs=new ArrayList<OrderProductSpecDTO>();
			OrderProductSpecDTO dto1=new OrderProductSpecDTO();
			dto1.setId("postcardTmplId");
			dto1.setValue(mProductDTO.getSpecs().get(0).getValues().get(0).getId());
			OrderProductSpecDTO dto2=new OrderProductSpecDTO();
			dto2.setId("petalkId");
			dto2.setValue(mOrderVos.get(i).getId());
			orderProductSpecDTOs.add(dto1);
			orderProductSpecDTOs.add(dto2);
			orderItemDTO.setSpecs(orderProductSpecDTOs);
			orderItemDTOs.add(orderItemDTO);
		}
		String orderItems=JSONArray.toJSONString(orderItemDTOs);
		mOrderNet.orderCreate(orderItems);
}
}