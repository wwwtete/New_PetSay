package com.petsay.activity.homeview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.petalk.ALLSayListActivity;
import com.petsay.activity.petalk.ChannelSayListActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;
/**
 * 广场activity
 * @author G
 *
 */
public class SquareActivity extends BaseActivity implements NetCallbackInterface{
	private ListView lv;
	private TitleBar mTitleBar;
	private TextView tvTitleRight;
	
	private List<SquareVo> mSquareVos;
	private SquareAdapter mAdapter;
	private SquareVo mSquareVo;
	private SayDataNet mSayDataNet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_activity_layout);
		mSquareVo=(SquareVo) getIntent().getSerializableExtra("squareVo");
		initView();
		showLoading(true);
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		mSayDataNet.layoutDatum(Integer.parseInt(mSquareVo.getKey()));
	}
	
	protected void initView(){
		super.initView();
//		layoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		lv=(ListView) findViewById(R.id.lv_squarelist);
		mAdapter = new SquareAdapter();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar();		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				SquareVo squareVo=(SquareVo) mAdapter.getItem(position);
				
				
				switch (squareVo.getHandleType()) {
				case 1:
					break;
				case 2:
					Intent intent=new Intent(SquareActivity.this, TagSayListActivity.class);
					intent.putExtra("id", squareVo.getKey());
					intent.putExtra("folderPath", squareVo.getTitle());
					startActivity(intent);
					break;
				case 3:
				case 4:
					 intent=new Intent(SquareActivity.this, ChannelSayListActivity.class);
					 intent.putExtra("squareVo", squareVo);
					 startActivity(intent);
					break;
				case 5:
					intent=new Intent(SquareActivity.this, ALLSayListActivity.class);
					intent.putExtra("squareVo", squareVo);
					startActivity(intent);
					break;
				case 6:
//					PublicMethod.showToast(SquareActivity.this, "广场跳转");
					intent=new Intent(SquareActivity.this, SquareActivity.class);
					intent.putExtra("squareVo", squareVo);
					startActivity(intent);
					break;
				case 7:
					intent=new Intent(SquareActivity.this, WebViewActivity.class);
					intent.putExtra("url", squareVo.getKey());
					startActivity(intent);
					break;
				case 8:
					intent=new Intent(SquareActivity.this, DetailActivity.class);
					PetalkVo petalkVo=new PetalkVo();
					petalkVo.setPetalkId(squareVo.getKey());
					Constants.Detail_Sayvo=petalkVo;
					startActivity(intent);
					break;
				default:
					break;
				}
			
			}
		});
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(mSquareVo.getTitle());
		mTitleBar.setFinishEnable(true);
		
	}
	
	private class SquareAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if (null==mSquareVos||mSquareVos.isEmpty()) {
				return 0;
			}
			return mSquareVos.size();
		}

		@Override
		public Object getItem(int position) {
			if (null==mSquareVos||mSquareVos.isEmpty()) {
				return null;
			}
			return mSquareVos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null==convertView) {
				holder=new Holder();
				convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.square_list_item, null);
				holder.imgTag=(ImageView)convertView. findViewById(R.id.img_tag);
				convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			if (TextUtils.isEmpty(mSquareVos.get(position).getIconUrl())) {
				holder.imgTag.setVisibility(View.GONE);
			}else {
				holder.imgTag.setVisibility(View.VISIBLE);
                DisplayImageOptions options = ImageLoaderOptionsManager.getRectOptions();
				ImageLoaderHelp.displayImage(mSquareVos.get(position).getIconUrl(), holder.imgTag,options);
			}
			
			return convertView;
		}
		
	}
	
	class Holder {
		private ImageView imgTag;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_LAYOUTDATUM:
			try {
				mSquareVos=JsonUtils.getList(bean.getValue(), SquareVo.class);
				lv.setAdapter(mAdapter);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("广场标签json解析出错");
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
}
