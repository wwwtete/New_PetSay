package com.petsay.activity.main;

import java.util.List;

import com.petsay.R;
import com.petsay.activity.personalcustom.clothing.ClothingDetailActivity;
import com.petsay.activity.personalcustom.clothing.ClothingListActivity;
import com.petsay.activity.personalcustom.diary.DiaryPreViewActivity;
import com.petsay.activity.personalcustom.postcard.CustomPostPreviewActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.SquareVo;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 首页定制view
 * @author GaoJian
 *
 */
public class PersonalCustomView extends LinearLayout implements NetCallbackInterface{

	private TitleBar mTitleBar;
	private Context mContext;
    private ListView mLv;
    private PullToRefreshView mPullToRefreshView; 
	private List<SquareVo> mCustomLists;
	private SayDataNet mSayDataNet;
	
	public PersonalCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
		setListener();
		
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(context);
		mSayDataNet.layoutDatum(7);
	}
	
	private void initView(){
		inflate(mContext, R.layout.main_customview, this);
		initTitleBar();
		mLv=(ListView) findViewById(R.id.lv);
		mPullToRefreshView=(PullToRefreshView) findViewById(R.id.pulltorefreshview);
	}
	
	private void setListener(){
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SquareVo squareVo=mCustomLists.get(position);
				int categoryId=Integer.parseInt(squareVo.getKey());
				if (squareVo.getHandleType()==13) {
					
					switch (categoryId) {
					case 1:
						goToActivity(CustomPostPreviewActivity.class,categoryId,squareVo.getTitle());
						break;
					case 2:
			            goToActivity(DiaryPreViewActivity.class,categoryId,squareVo.getTitle());
						break;
					default:
						goToActivity(ClothingDetailActivity.class,categoryId,squareVo.getTitle());
						break;
					}
				}else if (squareVo.getHandleType()==14) {
					goToActivity(ClothingListActivity.class,categoryId,squareVo.getTitle());
				}
				
			}
		});
	}
	
	private void initTitleBar(){
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mTitleBar.setTitleText("私宠定制");
		mTitleBar.setBackVisibility(View.GONE);
	}
	
	private void goToActivity(Class<?> cls,int categoryId,String title){
		Intent intent;
		if (UserManager.getSingleton().isLoginStatus()) {
		    intent=new Intent(mContext,cls);
		    intent.putExtra("categoryId", categoryId);
		    intent.putExtra("title", title);
			mContext.startActivity(intent);
		}else {
			intent=new Intent(mContext,UserLogin_Activity.class);
			mContext.startActivity(intent);
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		
		switch (requestCode) {
		case RequestCode.REQUEST_LAYOUTDATUM:
			try {
				mCustomLists=JsonUtils.getList(bean.getValue(), SquareVo.class);
				mLv.setAdapter(new ListAdapter());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_LAYOUTDATUM:
			break;
		}
	}
	
    private class ListAdapter extends  BaseAdapter{

		@Override
		public int getCount() {
			if (null == mCustomLists || mCustomLists.isEmpty()) {
				return 0;
			} else
				return mCustomLists.size();
		}

		@Override
		public Object getItem(int position) {
			if (null == mCustomLists || mCustomLists.isEmpty()) {
				return null;
			} else
				return mCustomLists.get(position);
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
				convertView=LayoutInflater.from(mContext).inflate(R.layout.main_customview_listitem, null);
			    holder.imgCover=(ImageView) convertView.findViewById(R.id.img_cover);
			    convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			ImageLoaderHelp.displayContentImage(mCustomLists.get(position).getIconUrl(), holder.imgCover);
			return convertView;
		}
    }

   private class Holder{
    	private ImageView imgCover;
    }
}
