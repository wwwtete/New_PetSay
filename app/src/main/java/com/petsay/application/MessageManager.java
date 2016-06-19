package com.petsay.application;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.vo.ResponseBean;

public class MessageManager implements NetCallbackInterface {
//	private Context mContext;
//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;
	
	private int msgCount=0;
	public int unreadMsgCount=0;
	public boolean hasUnreadMsg=false;
	private MessageCallBack mCallBack;
	private static MessageManager _instance;
	public static MessageManager getSingleton(){
		if (null==_instance) {
			_instance=new MessageManager();
		}
		return _instance;
	}
	
	private  MessageManager(){
//		mUserModule = UserModule.getSingleton();
//		mUserData = new UserData(handler);
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
	}
	
//	public void initContext(Context context){
//		 mContext = context;
//	}
	
//	public int getMessageCount(){
//		
//	}

    public void setCallBack(MessageCallBack callBack){
        mCallBack = callBack;
    }
	
	public void refreshMsgCount(){
        refreshMsgCount(mCallBack);
	}

    public void refreshMsgCount(MessageCallBack callBack){
        setCallBack(callBack);
        if (UserManager.getSingleton().isLoginStatus()) {
            mUserNet.messageUMC( UserManager.getSingleton().getActivePetId());
        }
    }
	
	public interface MessageCallBack{
		void setUnreadMsgCount(int count);
	}
	
	public void getAnnouncementCount(){
		mUserNet.announcementCount("");
		
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_MESSAGEUMC:
			try {
				msgCount=Integer.parseInt(bean.getValue());
			} catch (Exception e) {
				System.err.println("-----Get msgCount error");
				msgCount=0;
			}
			getAnnouncementCount();
			break;
		case RequestCode.REQUEST_ANNOUNCEMENTCOUNT:
			int count=0;
			try {
				count=Integer.parseInt(bean.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			mCallBack.setUnreadMsgCount(msgCount+count);
			break;
		default:
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_MESSAGEUMC:
			break;
		case RequestCode.REQUEST_ANNOUNCEMENTCOUNT:
			break;
		default:
			break;
		}
	}
}
