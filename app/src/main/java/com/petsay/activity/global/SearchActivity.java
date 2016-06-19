package com.petsay.activity.global;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.global.adapter.SearchTagAdapter;
import com.petsay.activity.global.adapter.SearchUserAdapter;
import com.petsay.activity.homeview.adapter.HotListViewAdapter;
import com.petsay.component.gifview.GifListScrollListener;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;
import com.petsay.vo.petalk.SearchTagVo;

import java.util.List;

public class SearchActivity extends BaseActivity implements OnClickListener ,IAddShowLocationViewService, NetCallbackInterface{
	private EditText edSearch;
	private Button btnSearch;
//	private LinearLayout layoutTab1, layoutTab2, layoutTab3;
	private TextView t1,t2,t3;
	private TextView[] layouts;
	private ListView lvSearch;
	private int curTabIndex = 0;
//	private List<SayVo> sayVos;
	private HotListViewAdapter mSearchSayListViewAdapter;
//	private List<SearchTagVo> mSearchTagVos;
	private SearchTagAdapter searchTagAdapter;
//	private List<PetInfo> mPetInfos;
	private SearchUserAdapter searchUserAdapter;
	private boolean isEnableSearch = false;
	private GifListScrollListener mScrollListener;
	private PullToRefreshView mPullToRefreshView;
	private String mKeyword;
	private int mPagIndex = 0;
//	private SayData mSayData;
//	private SayModule mSayModule;
	private SayDataNet mSayDataNet;
	
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mSayModule = SayModule.getSingleton();
//		mSayData = new SayData(handler);
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		setContentView(R.layout.search);
		initView();
		setListener();
	}

	protected void initView() {
		super.initView();
		edSearch = (EditText) findViewById(R.id.ed_search);
		btnSearch = (Button) findViewById(R.id.btn_search);
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		layouts = new TextView[3];
		layouts[0] = t1;
		layouts[1] = t2;
		layouts[2] = t3;
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefreshview);
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				onRefresh();
			}
		});
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				onAddMore();
			}
		});
		lvSearch = (ListView) findViewById(R.id.lv_search);
		searchTagAdapter = new SearchTagAdapter(SearchActivity.this);
		searchUserAdapter = new SearchUserAdapter(SearchActivity.this);
		mSearchSayListViewAdapter = new HotListViewAdapter(this,SearchActivity.this,true);
		lvSearch.setAdapter(mSearchSayListViewAdapter);
		lvSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				GifViewManager.getInstance().stopGif();
				switch (curTabIndex) {
				case 0:
					Intent intent=new Intent(getApplicationContext(), DetailActivity.class);
					Constants.Detail_Sayvo=(PetalkVo) mSearchSayListViewAdapter.getItem(position);//.get(position);
					startActivity(intent);
					break;
				case 1:
					intent=new Intent(getApplicationContext(), TagSayListActivity.class);
					intent.putExtra("id",((SearchTagVo) searchTagAdapter.getItem(position)).getId());//mSearchTagVos.get(position).getTagInfo());
					intent.putExtra("folderPath",((SearchTagVo) searchTagAdapter.getItem(position)).getName());//mSearchTagVos.get(position).getTagInfo());
					startActivity(intent);
					break;
				case 2:
					ActivityTurnToManager.getSingleton().userHeaderGoto(SearchActivity.this, (PetVo) searchUserAdapter.getItem(position));// mPetInfos.get(position));
					break;
				}
				
			}
		});
	}

	@Override
	protected void onPause() {
		closeLoading();
		mScrollListener.release();
		super.onPause();
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		switch (mPagIndex) {
		case 0:
			if (null!=mSearchSayListViewAdapter&&mSearchSayListViewAdapter.getCount()>0) {
				mSearchSayListViewAdapter.notifyDataSetChanged();
			}
			break;
		case 1:
			break;
		case 2:
			
			break;
		}
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			String str = edSearch.getText().toString();
			if (str.equals("")) {
               PublicMethod.showToast(getApplicationContext(), "请输入要搜索的内容");
			} else {
				search(str);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void search(String str) {
//		if (null!=sayVos) {
//			sayVos.clear();
//			hotListViewAdapter.refreshData(sayVos);
//		}
//		if (null!=mSearchTagVos) {
//			mSearchTagVos.clear();
//			searchTagAdapter.notifyDataSetChanged();
//		}
//		
//		if (null!=mPetInfos) {
//			mPetInfos.clear();
//			searchUserAdapter.notifyDataSetChanged();
//		}
		showLoading();
		GifViewManager.getInstance().stopGif();
		mSearchSayListViewAdapter.clear();
		searchTagAdapter.clear();
		searchUserAdapter.clear();
		mKeyword = str;
		onRefresh();
	}
	
	private void onRefresh(){
		mPagIndex = 0;
		network(mKeyword, false);
	}
	
	private void onAddMore(){
		mPagIndex++;
		network(mKeyword, true);
	}
	
	private void onPulltoRefreshCallback(boolean isMore){
		if(isMore){
			mPullToRefreshView.onFooterRefreshComplete();
		}else {
			mPullToRefreshView.onHeaderRefreshComplete();
		}
	}
	
	private void network(String keyword,boolean isMore){
		if(TextUtils.isEmpty(keyword))
			return;
//		showLoading();
		String petId="";
		if (UserManager.getSingleton().isLoginStatus()) {
			petId=UserManager.getSingleton().getActivePetInfo().getId();
		}
		switch (curTabIndex) {
		case 0:
//			mSayModule.addListener(mSayData);
			mSayDataNet.searchPetalk( petId, keyword, mPagIndex, 10,isMore);
			break;
		case 1:
//			mSayModule.addListener(mSayData);
			mSayDataNet.searchTag( petId, keyword, mPagIndex, 10,isMore);
			break;
		case 2:
//			mSayModule.addListener(mSayData);
			mSayDataNet.searchUser( petId, keyword, mPagIndex, 10,isMore);
			break;
		}
	}

	private void setListener() {
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		//图片距离顶部的距离
		int titleHeight = PublicMethod.getDiptopx(SearchActivity.this, 60);
		mScrollListener = new GifListScrollListener(lvSearch,titleHeight);
		lvSearch.setOnScrollListener(mScrollListener);

		edSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals("")) {
					isEnableSearch = false;
					btnSearch.setText(getString(R.string.search_btn_hint));
					// edSearch.setImeOptions(EditorInfo.IME_ACTION_NONE);
				} else {
					isEnableSearch = true;
					btnSearch.setText(getString(R.string.search_btn));

					// edSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.img_clear_search:
//			edSearch.setText("");
//			break;
		case R.id.text1:
			GifViewManager.getInstance().stopGif();
			if (curTabIndex != 0) {
				curTabIndex = 0;
				PublicMethod.changeTextViewColor(layouts, curTabIndex, getResources().getColor(R.color.serch_select), getResources().getColor(R.color.serch_default));
				if (null == mSearchSayListViewAdapter) {
					mSearchSayListViewAdapter = new HotListViewAdapter(this,SearchActivity.this,true);
				}
				lvSearch.setAdapter(mSearchSayListViewAdapter);
				lvSearch.setOnScrollListener(mScrollListener);
				if (isEnableSearch) {
					search(edSearch.getText().toString());
				}
			}
			break;
		case R.id.text2:
			GifViewManager.getInstance().stopGif();
			if (curTabIndex != 1) {
				curTabIndex = 1;
				PublicMethod.changeTextViewColor(layouts, curTabIndex, getResources().getColor(R.color.serch_select), getResources().getColor(R.color.serch_default));
				lvSearch.setAdapter(searchTagAdapter);
				lvSearch.setOnScrollListener(null);
				if (isEnableSearch) {
					search(edSearch.getText().toString());
				}
			}
			break;
		case R.id.text3:
			GifViewManager.getInstance().stopGif();
			if (curTabIndex != 2) {
				curTabIndex = 2;
				PublicMethod.changeTextViewColor(layouts, curTabIndex, getResources().getColor(R.color.serch_select), getResources().getColor(R.color.serch_default));
				lvSearch.setAdapter(searchUserAdapter);
				lvSearch.setOnScrollListener(null);
				if (isEnableSearch) {
					search(edSearch.getText().toString());
				}
			}
			break;
		case R.id.btn_search:
			// 如果状态为搜索
			if (isEnableSearch) {
				search(edSearch.getText().toString());
			} else {
				finish();
			}
			break;
		}
	}

	@Override
	public View getParentView() {
		return mLayoutRoot;
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		onPulltoRefreshCallback(bean.isIsMore());
		switch (requestCode) {
		case RequestCode.REQUEST_SEARCHPETALK:
//			String jsonStr=JsonParse.getSingleton().parseListString(bean.getValue());
			List<PetalkVo> sayVos=JsonUtils.parseList(bean.getValue(), PetalkVo.class);
			if(bean.isIsMore()){
				mSearchSayListViewAdapter.addMore(sayVos);
			}else {
				mSearchSayListViewAdapter.refreshData(sayVos);
				if (null==sayVos||sayVos.isEmpty()) {
					PublicMethod.showToast(getActivity(), "无搜索内容");
				}
			}
			lvSearch.setAdapter(mSearchSayListViewAdapter);
			break;
		case RequestCode.REQUEST_SEARCHTAG:
			List<SearchTagVo> mSearchTagVos=JsonUtils.parseList(bean.getValue(), SearchTagVo.class);
			if(bean.isIsMore()){
				searchTagAdapter.addMore(mSearchTagVos);
			}else {
				searchTagAdapter.refreshData(mSearchTagVos);
				if (null==mSearchTagVos||mSearchTagVos.isEmpty()) {
					PublicMethod.showToast(getActivity(), "无搜索内容");
				}
			}
				lvSearch.setAdapter(searchTagAdapter);
			break;
		case RequestCode.REQUEST_SEARCHUSER:
			List<PetVo> mPetInfos=JsonUtils.parseList(bean.getValue(), PetVo.class);
			if(bean.isIsMore()){
				searchUserAdapter.addMore(mPetInfos);
			}else {
				searchUserAdapter.refreshData(mPetInfos);
				if (null==mPetInfos||mPetInfos.isEmpty()) {
					PublicMethod.showToast(getActivity(), "无搜索内容");
				}
				
			}
				lvSearch.setAdapter(searchUserAdapter);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onPulltoRefreshCallback(error.isIsMore());
		switch (requestCode) {
		case RequestCode.REQUEST_SEARCHPETALK:
//			
			break;
		case RequestCode.REQUEST_SEARCHTAG:
			break;
		case RequestCode.REQUEST_SEARCHUSER:
			break;
		default:
			break;
		}
		
	}
}