package com.petsay.activity.award;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.homeview.HomeFragment;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.LoopImgPagerView;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.AwardNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.award.ActivityDTO;
import com.petsay.vo.award.JoinerDTO;
import com.petsay.vo.petalk.PetVo;

/**
 * 奖品详情
 *
 * @author G
 *
 */
public class AwardDetailActivity extends BaseActivity implements
		NetCallbackInterface, OnClickListener, IAddShowLocationViewService {

	private TitleBar mTitleBar;
	private RelativeLayout layoutRoot;
	private TextView tvJoincount, tvStartTime, tvJoinRule;
	private LoopImgPagerView loopImgPagerView;
	private Button btnJoin,btnGoFinish;
	private LinearLayout layoutHeader;

	private AwardNet mAwardNet;
	private ActivityDTO mActivityDTO;


	private int headerImgWidth=100;
//	private boolean isTurn=false;

//	private DialogPopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.award_detail);
		mActivityDTO = (ActivityDTO) getIntent().getSerializableExtra("activityDTO");
		initView();
//		popupWindow=new DialogPopupWindow(AwardDetailActivity.this, this);
//		popupWindow.setOnClickListener(this);
		mAwardNet = new AwardNet();
		mAwardNet.setCallback(this);
		mAwardNet.setTag(this);
		mAwardNet.awardActivityOne(mActivityDTO.getId(), UserManager.getSingleton().getActivePetId());
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		tvJoincount = (TextView) findViewById(R.id.tv_joincount);
		tvJoinRule = (TextView) findViewById(R.id.tv_join_rule);
		tvStartTime = (TextView) findViewById(R.id.tv_startTime);
		loopImgPagerView = (LoopImgPagerView) findViewById(R.id.loopImgView);
		layoutHeader=(LinearLayout) findViewById(R.id.layout_header);
		btnJoin = (Button) findViewById(R.id.btn_join);
		btnGoFinish=(Button) findViewById(R.id.btn_gofinish);
		initTitleBar();
		setOnClickListener();

	}

	private void setOnClickListener() {
		btnJoin.setOnClickListener(this);
		btnGoFinish.setOnClickListener(this);
	}

	private void initTitleBar() {
		mTitleBar.setTitleText("");
		mTitleBar.setFinishEnable(true);
		mTitleBar.setBackgroundResource(R.color.transparent);
		mTitleBar.setLeftBtnRes(R.drawable.bac_attach_bg);

		// tvTitleRight = PublicMethod.getTitleRightText(this);
		// tvTitleRight.setText("兑换记录");
		// LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// txt_Params.gravity = Gravity.CENTER;
		// txt_Params.leftMargin = 10;
		// LinearLayout layout = new LinearLayout(this);
		// layout.setOrientation(LinearLayout.HORIZONTAL);
		// layout.setGravity(Gravity.CENTER_VERTICAL);
		// layout.addView(tvTitleRight,txt_Params);
		// layout.setTag(0);
		// layout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent=new
		// Intent(PrizeListActivity.this,ExchangeHistoryActivity.class);
		// startActivity(intent);
		// }
		// });
		//
		// mTitleBar.addRightView(layout);
	}

	private void setData() {
		tvJoincount.setText("参加人数：" + mActivityDTO.getJoinerCount() + "人");
		tvStartTime.setText(PublicMethod.formatTimeToString(
				mActivityDTO.getBeginTime(), "yyyy年MM月dd日")
				+ "-"
				+ PublicMethod.formatTimeToString(mActivityDTO.getEndTime(),
				"yyyy年MM月dd日"));
		tvJoinRule.setText(mActivityDTO.getDescription());
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
			case RequestCode.REQUEST_AWARDACTIVITYONE:
				mActivityDTO = JsonUtils.resultData(bean.getValue(),ActivityDTO.class);
				setData();
				int size = mActivityDTO.getAwardPics().size();
				String[] imgUrls = new String[size];
				for (int i = 0; i < mActivityDTO.getAwardPics().size(); i++) {
					imgUrls[i] = mActivityDTO.getAwardPics().get(i).getUrl();
				}
				loopImgPagerView.setImgUrls(imgUrls);
				if (mActivityDTO.getState()==2) {
					btnGoFinish.setVisibility(View.GONE);
					btnJoin.setText("参加");
					btnJoin.setTextColor(Color.WHITE);
					btnJoin.setBackgroundColor(Color.rgb(0xb7, 0xe8, 0xfe));
				}else {
					if (mActivityDTO.getState() == 3) {
						if (mActivityDTO.getCatagory() == 2) {
							if (mActivityDTO.getCatagory()==2) {
								btnGoFinish.setText("去首页");
							}else {
								btnGoFinish.setText("去完成");
							}
						} else {
							btnGoFinish.setText("已参与");
						}

					} else if (mActivityDTO.getState() == 5) {
						btnGoFinish.setText("已结束");
						btnGoFinish.setClickable(false);
					}
					btnGoFinish.setVisibility(View.VISIBLE);
					btnGoFinish.setTextColor(Color.WHITE);
					btnGoFinish.setBackgroundColor(Color.GRAY);
					btnJoin.setText("去我的任务");
					btnJoin.setTextColor(Color.WHITE);
					btnJoin.setBackgroundColor(Color.GRAY);
				}

				initHeaderView();
//			if (isTurn) {
//				Intent intent=new Intent();
//				if (mActivityDTO.getCatagory()==2) {
//					 intent.setClass (this,MyTaskActivity.class);
//					 startActivity(intent);
//				}else {
//					popupWindow.setPopupText(1,null, "是否马上去发布说说？", "确定", "取消");
//					popupWindow.showDefault();
//				}
//			}
				break;
			case RequestCode.REQUEST_AWARDACTIVITYJOIN:
				btnJoin.setText("已参加");
//			isTurn=true;
				mAwardNet.awardActivityOne(mActivityDTO.getId(), UserManager.getSingleton().getActivePetId());
				break;
			default:
				break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_join:

				if (mActivityDTO.getState()==2) {
					showLoading();
					mAwardNet.awardActivityJoin(UserManager.getSingleton()
							.getActivePetId(), mActivityDTO.getId(), UserManager
							.getSingleton().getActivePetInfo().getHeadPortrait());
				}else {
					Intent intent=new Intent();
					intent.setClass(AwardDetailActivity.this, MyTaskActivity.class);
					startActivity(intent);
				}
				break;
//		case R.id.tv_dialog_ok:
//			btnJoin.setClickable(true);
//			popupWindow.dismiss();
//			
//			intent.setClass(this, TagSayListActivity.class);
//			intent.putExtra("id", mActivityDTO.getTagId());
//			startActivity(intent);
//			break;
//		case R.id.tv_dialog_cancle:
//			
//			popupWindow.dismiss();
//			break;
//			
			case R.id.btn_gofinish:
				btnJoin.setClickable(true);
				Intent intent =new Intent();
				if (mActivityDTO.getCatagory()==2) {
					intent.setClass (this,MainActivity.class);
					intent.putExtra(HomeFragment.PAGEINDEX, 0);
					startActivity(intent);
				}else {
					intent.setClass(this, TagSayListActivity.class);
					intent.putExtra("id", mActivityDTO.getTagId());
					startActivity(intent);
				}
				break;
			default:
				break;
		}

	}

	private void initHeaderView(){
		layoutHeader.removeAllViews();
		int padding=10;
		LinearLayout.LayoutParams layoutParams=new LayoutParams(headerImgWidth, headerImgWidth);
		layoutParams.setMargins(padding, 0, 0, 0);
		int imageCount=(PublicMethod.getDisplayWidth(getApplicationContext())-padding-PublicMethod.getDiptopx(getApplicationContext(), 20))/(padding+headerImgWidth);
		int size=mActivityDTO.getJoiners().size();
		if (size>imageCount) {
			size=imageCount;
		}
		for (int i = 0; i < size; i++) {
			View view = (LinearLayout) LayoutInflater.from(AwardDetailActivity.this).inflate(R.layout.award_detail_pet_item, null);
			CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_header);
//			circleImageView.setPadding(padding, padding, padding, padding);
			circleImageView.setLayoutParams(layoutParams);
			final JoinerDTO joinerDTO=mActivityDTO.getJoiners().get(i);
			ImageLoaderHelp.displayHeaderImage(joinerDTO.getAvatarUrl(),circleImageView);
			layoutHeader.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PetVo petVo=new PetVo();
					petVo.setId(joinerDTO.getId());
					ActivityTurnToManager.getSingleton().userHeaderGoto(AwardDetailActivity.this, petVo);

				}
			});
		}
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return layoutRoot;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

}
