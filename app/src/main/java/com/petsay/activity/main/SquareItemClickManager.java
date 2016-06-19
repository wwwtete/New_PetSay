package com.petsay.activity.main;

import com.petsay.activity.award.AwardListActivity;
import com.petsay.activity.coupon.ReceiveCouponsActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.homeview.SquareActivity;
import com.petsay.activity.petalk.ALLSayListActivity;
import com.petsay.activity.petalk.ChannelSayListActivity;
import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.petalk.rank.RankActivity;
import com.petsay.activity.topic.TopicDetailActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.constants.Constants;
import com.petsay.vo.SquareVo;
import com.petsay.vo.petalk.PetalkVo;

import android.content.Context;
import android.content.Intent;

public class SquareItemClickManager {

	public static void squareVoClick(Context context, SquareVo squareVo) {
		Intent intent;
		switch (squareVo.getHandleType()) {
		case 1:
			// 不处理
			break;
		case 2:
			// Intent intent=new Intent(mContext, PhotoWallActivity.class);

			intent = new Intent(context, TagSayListActivity.class);
			intent.putExtra("id", squareVo.getKey());
			intent.putExtra("folderPath", squareVo.getTitle());
			context.startActivity(intent);
			break;
		case 3:
		case 4:
			intent = new Intent(context, ChannelSayListActivity.class);
			intent.putExtra("squareVo", squareVo);
			context.startActivity(intent);
			break;
		case 5:
			intent = new Intent(context, ALLSayListActivity.class);
			intent.putExtra("squareVo", squareVo);
			context.startActivity(intent);
			break;
		case 6:
			// PublicMethod.showToast(mContext, "广场跳转");
			intent = new Intent(context, SquareActivity.class);
			intent.putExtra("squareVo", squareVo);
			context.startActivity(intent);
			break;
		case 7:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", squareVo.getKey());
			context.startActivity(intent);
			break;
		case 8:
			intent = new Intent(context, DetailActivity.class);
			PetalkVo petalkVo = new PetalkVo();
			petalkVo.setPetalkId(squareVo.getKey());
			Constants.Detail_Sayvo = petalkVo;
			context.startActivity(intent);
			break;
		case 9:
			// TODO 排行榜
			intent = new Intent(context, RankActivity.class);
			context.startActivity(intent);
			break;
		case 10:
			// TODO 奖品列表
			if (UserManager.getSingleton().isLoginStatus()) {
				intent = new Intent(context, AwardListActivity.class);
				context.startActivity(intent);
			} else {
				intent = new Intent(context, UserLogin_Activity.class);
				context.startActivity(intent);
			}
			break;
		case 11:
			intent = new Intent(context, TopicDetailActivity.class);
			context.startActivity(intent);
			break;
		case 15:
			if (UserManager.getSingleton().isLoginStatus()) {
				intent = new Intent(context, ReceiveCouponsActivity.class);
				intent.putExtra("key", squareVo.getKey());
			} else {
				intent = new Intent(context, UserLogin_Activity.class);
			}
			context.startActivity(intent);
			default:
			break;
		}
	}
}
