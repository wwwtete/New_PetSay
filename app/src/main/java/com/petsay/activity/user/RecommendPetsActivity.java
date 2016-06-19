package com.petsay.activity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/***
 * 热门用户推荐关注页面
 * @author G
 *
 */
public class RecommendPetsActivity extends BaseActivity implements NetCallbackInterface {
	private GridView mGrid;
	private TextView mBtnFocus,mBtnRefresh;
	private TitleBar mTitleBar;
	private List<PetVo> mPetInfos;
	private HashMap<String, String> mSelectMap=new HashMap<String, String>();
//	private List<PetInfo> mSelectedPetInfos;
//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;
	private RecommendPetsAdapter mAdapter;
//	private Drawable selectDrawable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend_pets);
		
		initView();
//		mUserModule=UserModule.getSingleton();
//		mUserData=new UserData(mHandler);
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		if (UserManager.getSingleton().isLoginStatus()) {
			mUserNet.petRecommend(9, UserManager.getSingleton().getActivePetId());
		}else {
			finish();
		}
	}
	
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
//		selectDrawable=SkinManager.getInstance(getApplicationContext()).getDrawable(getString(R.string.select_pet));
//		selectDrawable=SkinHelp.getDrawable(RecommendPetsActivity.this, R.string.select_pet);
		mGrid=(GridView) findViewById(R.id.grid_pets);
		mBtnFocus=(TextView) findViewById(R.id.btn_focus);
		mBtnRefresh=(TextView) findViewById(R.id.btn_refresh);
		mAdapter=new RecommendPetsAdapter();
		mGrid.setAdapter(mAdapter);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				PetVo petInfo=mPetInfos.get(position);
				if (mSelectMap.containsKey(petInfo.getId())) {
					mSelectMap.remove(petInfo.getId());
				}else {
					mSelectMap.put(petInfo.getId(),petInfo.getId());
				}
				mAdapter.notifyDataSetChanged();
				if (mSelectMap.isEmpty()) {
					mBtnFocus.setText("以后再说");
				}else {
					mBtnFocus.setText("确认关注");
				}
			}
		});
		
		mBtnFocus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Iterator<String> iter = mSelectMap.keySet().iterator();
				if (!mSelectMap.isEmpty()) {
					String value = "";
					int index=0;
					while (iter.hasNext()) {
						if (index>0) {
							value += ",";
						}
						index++;
						value += mSelectMap.get(iter.next());
					}
					 mUserNet.petfansBatchFocus(value, UserManager.getSingleton().getActivePetId());
				}else {
					
				}
				finish();
			}
			
			
		});
		
		
		mBtnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getSingleton().isLoginStatus()) {
					mUserNet.petRecommend( 9, UserManager.getSingleton().getActivePetId());
				}
			}
		});
		
	}
	
	public class RecommendPetsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null!=mPetInfos) {
				return mPetInfos.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (null != mPetInfos && !mPetInfos.isEmpty()) {
				return mPetInfos.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (null != mPetInfos && !mPetInfos.isEmpty()) {
				return position;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null==convertView) {
				holder = new Holder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recommend_pets_grid_item, null);
				holder.imgHeader = (CircleImageView) convertView.findViewById(R.id.img_header);
				holder.imgSex = (ImageView) convertView.findViewById(R.id.img_sex);
				holder.imgSelect = (ImageView) convertView.findViewById(R.id.img_select);
				holder.tvAge=(TextView) convertView.findViewById(R.id.tv_age);
				holder.tvKind=(TextView) convertView.findViewById(R.id.tv_kind);
				holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			PetVo petInfo=mPetInfos.get(position);
			ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.imgHeader);
			
			holder.tvName.setText(petInfo.getNickName());
			holder.tvKind.setText(Constants.petMap.get(petInfo.getType()));
//			holder.imgSelect.setImageDrawable(selectDrawable);
			holder.tvAge.setText(PublicMethod.getAge(petInfo.getBirthday(), false));
			if (mSelectMap.containsKey(petInfo.getId())) {
				holder.imgSelect.setVisibility(View.VISIBLE);
			}else {
				holder.imgSelect.setVisibility(View.GONE);
			}
			
			if (petInfo.getGender()==0) {
				holder.imgSex.setVisibility(View.VISIBLE);
				holder.imgSex.setImageResource(R.drawable.female);
				
			}else if (petInfo.getGender()==1) {
				holder.imgSex.setVisibility(View.VISIBLE);
				holder.imgSex.setImageResource(R.drawable.male);
			}else {
				holder.imgSex.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	private class Holder{
		private CircleImageView imgHeader;
		private TextView tvName,tvAge,tvKind;
		private ImageView imgSex;
		private ImageView imgSelect;
	}
	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_PETRECOMMEND:
			try {
				mPetInfos=JsonUtils.getList(bean.getValue(), PetVo.class);
			} catch (Exception e) {
//				System.err.println("RecommendPetsActivity.class   onekey focus Exception ");
				e.printStackTrace();
			}
			if (null != mPetInfos && !mPetInfos.isEmpty()) {
//				mSelectedPetInfos.addAll(mPetInfos);
				for (int i = 0; i < mPetInfos.size(); i++) {
					String petId=mPetInfos.get(i).getId();
					mSelectMap.put(petId, petId);
				}
				
//				mGrid.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mBtnFocus.setText("确认关注");
			}
			break;
        case RequestCode.REQUEST_PETFANSBATCHFOCUS:
			PublicMethod.logOutput("RecommendPetsActivity", "RecommendPetsActivity:"+bean.getValue());
			break;
		default:
			break;
		}
		
	}


	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_PETRECOMMEND:
			
			break;
        case RequestCode.REQUEST_PETFANSBATCHFOCUS:
			
			break;
		default:
			break;
		}
		
	}
}