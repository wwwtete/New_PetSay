package com.petsay.activity.user.signin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.MemberNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.sign.ActivityPartakeDetailVo;
import com.petsay.vo.sign.ActivityPartakeVo;
import com.petsay.vo.sign.SignInCalendarVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SigninActivity extends BaseActivity implements NetCallbackInterface{
	private TitleBar mTitleBar;
	private ProgressDialog mDialog;

	private RelativeLayout mLayoutRoot;
	private GridView mGrid;
	private TextView mTvSignDays,mTvSignMsg;//连续签到天数//连续签到几天可获得奖励
	private MemberNet mMemberNet;
	private List<ActivityPartakeVo> mActivityPartakeVos;
	private ActivityPartakeVo mActivityPartakeVo;
	private SignInCalendarVo mSignInCalendarVo;
//	private List<ActivityPartakeDetailVo> mActivityPartakeDetailVos;
	private ActivityPartakeDetailVo[] mActivityPartakeDetailVos=new ActivityPartakeDetailVo[16];
	private Map<String, ActivityPartakeDetailVo> map=new HashMap<String, ActivityPartakeDetailVo>();
	
	//获取签到历史数
	private  final int signCalSize=7;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_main);
		initView();
		mMemberNet=new MemberNet();
		mMemberNet.setTag(this);
		mMemberNet.setCallback(this);
		mActivityPartakeVo=(ActivityPartakeVo) getIntent().getSerializableExtra("activitypartakevo");
		if (mActivityPartakeVo.getState()==1) {
			mMemberNet.activitySignIn(UserManager.getSingleton().getActivePetId());
		}else {
			mMemberNet.activitySignCal(UserManager.getSingleton().getActivePetId(), signCalSize);
		}
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		mTitleBar.setTitleText("签到");
		mTitleBar.setFinishEnable(true);
		mLayoutRoot=(RelativeLayout) findViewById(R.id.layout_root);
		mGrid=(GridView) findViewById(R.id.grid_signin);
		mTvSignDays=(TextView) findViewById(R.id.tv_signdays);
		mTvSignMsg=(TextView) findViewById(R.id.tv_signmsg);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			}
		});
		
		
	}
	
	
	private class SignAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if (null==mActivityPartakeDetailVos) {
				return 0;
			}
			return mActivityPartakeDetailVos.length;
		}

		@Override
		public Object getItem(int position) {
			if (null==mActivityPartakeDetailVos||mActivityPartakeDetailVos.length==0) {
				return null;
			}
			return mActivityPartakeDetailVos[position];
		}

		@Override
		public long getItemId(int position) {
			if (null==mActivityPartakeDetailVos||mActivityPartakeDetailVos.length==0) {
				return 0;
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null==convertView) {
				holder=new Holder();
				convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.signcal_grid_item, null);
			    holder.tvDay=(TextView) convertView.findViewById(R.id.tv_day);
			    holder.tvScore=(TextView) convertView.findViewById(R.id.tv_score);
			    holder.tvMonth=(TextView) convertView.findViewById(R.id.tv_month);
			    convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			ActivityPartakeDetailVo activityPartakeDetailVo=mActivityPartakeDetailVos[position];
			if (activityPartakeDetailVo.getState()==3) {
				holder.tvDay.setBackgroundResource(R.drawable.sign_success_bg);
				holder.tvScore.setVisibility(View.VISIBLE);
				holder.tvScore.setText(activityPartakeDetailVo.getAward()+"积分");
				holder.tvDay.setTextColor(getResources().getColor(R.color.signin_success_date_color));
			}else {
				holder.tvScore.setVisibility(View.GONE);
				holder.tvDay.setBackgroundResource(R.drawable.sign_failed_bg);
				holder.tvDay.setTextColor(getResources().getColor(R.color.signin_failed_date_color));
			}
			
			int[] date=PublicMethod.getYMD(activityPartakeDetailVo.getCreateTime());
			holder.tvDay.setText(date[2]+"");
			holder.tvMonth.setText(date[1]+"月");
			 
			return convertView;
		}
		
		
	}

	private class Holder{
		private TextView tvDay;
		private TextView tvMonth;
		private TextView tvScore;
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_ACTIVITYPARTAKE:
			try {
				mActivityPartakeVos=JsonUtils.getList(bean.getValue(), ActivityPartakeVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < mActivityPartakeVos.size(); i++) {
				if (mActivityPartakeVos.get(i).getCode().equals("signIn")) {
					mActivityPartakeVo=mActivityPartakeVos.get(i);
					if (mActivityPartakeVo.getState()==1) {
						mMemberNet.activitySignIn(UserManager.getSingleton().getActivePetId());
					}
					
					break;
				}
			}
			if (null==mActivityPartakeVo) {
				
			}
//			SigninPopupWindow popupWindow=new SigninPopupWindow(mLayoutRoot, getApplicationContext(), "哈士奇签到成功", "恭喜获得"+"1"+"积分");
//			popupWindow.onShow();
			break;
		case RequestCode.REQUEST_ACTIVITYSIGNIN:
			
			mMemberNet.activitySignCal(UserManager.getSingleton().getActivePetId(), signCalSize);
			SigninPopupWindow popupWindow=new SigninPopupWindow(mLayoutRoot, getApplicationContext(), "您已签到成功", "恭喜获得"+bean.getValue()+"积分");
			popupWindow.show();
            break;
		case RequestCode.REQUEST_ACTIVITYSIGNCAL:
			mSignInCalendarVo=JsonUtils.resultData(bean.getValue(), SignInCalendarVo.class);
			mTvSignDays.setText("已连续签到"+mSignInCalendarVo.getCount()+"天");
			List<ActivityPartakeDetailVo> temp=mSignInCalendarVo.getDetails();
			int oneDay=24*60*60*1000;
			mActivityPartakeDetailVos[0]=temp.get(temp.size()-1);
			for (int i = 0; i < temp.size(); i++) {
				map.put(PublicMethod.formatTimeToString(temp.get(i).getCreateTime(), "yyyy-MM-dd"), temp.get(i));
			}
			
			long startTime=mActivityPartakeDetailVos[0].getCreateTime();
			for (int i = 0; i < mActivityPartakeDetailVos.length; i++) {
				if (map.containsKey(PublicMethod.formatTimeToString(startTime+i*oneDay, "yyyy-MM-dd"))) {
					//此日已签到
					mActivityPartakeDetailVos[i]=map.get(PublicMethod.formatTimeToString(startTime+i*oneDay, "yyyy-MM-dd"));
				}else {
					//此日未签到
					ActivityPartakeDetailVo activityPartakeDetailVo=new ActivityPartakeDetailVo();
					activityPartakeDetailVo.setCreateTime(startTime+i*oneDay);
					activityPartakeDetailVo.setState(0);
					mActivityPartakeDetailVos[i]=activityPartakeDetailVo;
				}
			}
			mTvSignMsg.setText(mSignInCalendarVo.getMemo());
			mGrid.setAdapter(new SignAdapter());
			break;
		default:
			break;
		}
		
	}

	

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
		
	}
	
	/**
	 * 签到完成后显示的对话框
	 */
	private void showSigninDialog() {
		
	}

}
