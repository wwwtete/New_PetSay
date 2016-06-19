package com.petsay.utile;

import android.content.Context;
import android.content.Intent;

import com.petsay.activity.main.MainActivity;
import com.petsay.activity.user.OtherUserActivity;
import com.petsay.activity.user.UserCenterActivity;
import com.petsay.application.UserManager;
import com.petsay.vo.petalk.PetVo;

/**
 * activity跳转管理 
 * @author G
 *
 */
public class ActivityTurnToManager {
	private static ActivityTurnToManager _instance;
	public static ActivityTurnToManager getSingleton(){
		if (null==_instance) {
			_instance=new ActivityTurnToManager();
		}
		return _instance;
	}
	private ActivityTurnToManager(){}
	
	/**
	 * 说说列表中点击用户头像activity跳转
	 * @param context
	 * @param petInfo
	 */
	public void userHeaderGoto(Context context,PetVo pet){
		Intent intent;
		if (UserManager.getSingleton().isLoginStatus()) {
			if (pet.getId().equals(UserManager.getSingleton().getActivePetId())) {
//				intent=new Intent(context, OtherUserActivity.class);
				intent = new Intent();
//				intent.putExtra(UserCenterFragment, 2);
//				intent.putExtra("fragment", -1);
				intent.setClass(context, UserCenterActivity.class);
				context.startActivity(intent);
			}else {
				intent=new Intent(context, OtherUserActivity.class);
				intent.putExtra("petInfo", pet);
				context.startActivity(intent);
			}
		}else {
			intent=new Intent(context, OtherUserActivity.class);
			intent.putExtra("petInfo", pet);
			context.startActivity(intent);
		}
		
	}
	
	public void userHeaderGoto(Context context,String petId){
		PetVo pet=new PetVo();
		pet.setId(petId);
		Intent intent;
		if (UserManager.getSingleton().isLoginStatus()) {
			if (pet.getId().equals(UserManager.getSingleton().getActivePetId())) {
//				intent=new Intent(context, OtherUserActivity.class);
				intent = new Intent();
//				intent.putExtra(UserCenterFragment, 2);
//				intent.putExtra("fragment", -1);
				intent.setClass(context, UserCenterActivity.class);
				context.startActivity(intent);
			}else {
				intent=new Intent(context, OtherUserActivity.class);
				intent.putExtra("petInfo", pet);
				context.startActivity(intent);
			}
		}else {
			intent=new Intent(context, OtherUserActivity.class);
			intent.putExtra("petInfo", pet);
			context.startActivity(intent);
		}
		
	}
	
	public void returnMainActivity(Context context){
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		context.startActivity(intent);
	}
}
