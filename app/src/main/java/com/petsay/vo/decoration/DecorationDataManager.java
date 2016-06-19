package com.petsay.vo.decoration;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.petsay.R;
import com.petsay.cache.DataFileCache;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.DecorationNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangw
 * 验证饰品数据是否已下载或资源包是否已经下载
 */
public class DecorationDataManager implements NetCallbackInterface {

    private static final String TAGTABLE_NAME = "-chat-db";

    /**保存每个类型最大常用的饰品数量*/
    protected static int MAXUSUALLYCOUNT = 15;
	private static DecorationDataManager instance;
	private DecorationRoot mData;
	private int mState = -3;
	private Context mContext;
	private DecorationTaskListener mListener;
	private SharedPreferences mSharedPreferences;
    private DecorationNet mNet;

    private HashMap<String,LinkedList<DecorationBean>> mCurrUsuallyMap;
    private HashMap<String,LinkedList<DecorationBean>> mNewUsuallyMap;
    private HashMap<String,List<DecorationTitleBean>> mGroupMap;
//	private Handler mHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			onDecorationCallback(getState() == 2);
//		};
//	};

	public static DecorationDataManager getInstance(Context context){
		if(instance == null)
			instance = new DecorationDataManager(context);
		return instance;
	}
	
	public DecorationDataManager(Context context){
		this.mContext = context;
        mNet = new DecorationNet();
        mNet.setCallback(this);
		mSharedPreferences = mContext.getSharedPreferences("decoration.verify", Context.MODE_PRIVATE);
	}

	public void setListener(DecorationTaskListener listener){
		mListener = listener;
	}
	
	public void removeListener(){
		mListener = null;
	}
	
	public DecorationRoot getData(){
		if(mData == null){
			mData = readLocalDecorationData();	
		}
		return mData;
	}

    /**
     * 获取本地常用的饰品信息
     * @return
     */
    public HashMap<String,LinkedList<DecorationBean>> getLocalUsullyData(){
        try {
//            if(mCurrUsuallyMap == null)
//                mCurrUsuallyMap = (HashMap<String, LinkedList<DecorationBean>>) DataFileCache.getSingleton().loadObject(petId+ Constants.USULLYDECORATION);
//            if(mCurrUsuallyMap != null){
//                return new HashMap<String, LinkedList<DecorationBean>>(mCurrUsuallyMap);
//            }
            return (HashMap<String, LinkedList<DecorationBean>>) DataFileCache.getSingleton().loadObject(getActivePetId()+ Constants.USULLYDECORATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCurrUsuallyMap;
    }

    public List<DecorationTitleBean> getDecorationGroupByType(String type){
        if(mGroupMap == null)
            mGroupMap = new HashMap<String, List<DecorationTitleBean>>();
        if(mGroupMap.containsKey(type)){
            return mGroupMap.get(type);
        }else {
            List<DecorationTitleBean> titleBeans = getData().getChildren();
            List<DecorationTitleBean> groups = null;
            for (int i=0;i<titleBeans.size();i++){
                DecorationTitleBean title = titleBeans.get(i);
                if(title.getType().equals(type)){
                    List<DecorationTitleBean> second = title.getChildren();
                    groups = new ArrayList<DecorationTitleBean>();
                    for (int j=0;j<second.size();j++){
                        DecorationTitleBean group = second.get(j);
                        List<DecorationBean> beans = group.getDecorations();
                        if(beans == null || beans.isEmpty()){
                            continue;
                        }
                        groups.add(group);
                    }
                    if(groups != null)
                        mGroupMap.put(type,groups);
                    return groups;
                }
            }
        }
        return null;
    }

    /**
     * 根据类型获取常用列表
     * @param type
     * @return
     */
    public LinkedList<DecorationBean> getUsullyQueue(String type){
        if(TextUtils.isEmpty(type))
            return null;
        if(mNewUsuallyMap == null){
            mNewUsuallyMap = getLocalUsullyData();
            if(mNewUsuallyMap == null)
                mNewUsuallyMap = new HashMap<String, LinkedList<DecorationBean>>();
        }
        if(mCurrUsuallyMap == null){
            mCurrUsuallyMap = new HashMap<String, LinkedList<DecorationBean>>(mNewUsuallyMap);
        }

        if(mCurrUsuallyMap.containsKey(type)){
            return mCurrUsuallyMap.get(type);
        }else if(getData() != null) {
            List<DecorationTitleBean> titleBeans = getData().getChildren();
            LinkedList<DecorationBean> list = null;
            for (int i = 0;i<titleBeans.size();i++){
                DecorationTitleBean bean =titleBeans.get(i);
                if(type.equals(bean.getType())){
                    List<DecorationTitleBean> sencodeType = bean.getChildren();
                    list = new LinkedList<DecorationBean>();
                    for(int j=0;j<sencodeType.size();j++){
                        List<DecorationBean> beans = sencodeType.get(j).getDecorations();
                        if(beans != null && !beans.isEmpty()){
                            for (int t=0;t < beans.size();t++){
                                if(list.size() < MAXUSUALLYCOUNT){
                                    list.addLast(beans.get(t));
                                }else {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
            if(list != null) {
                mNewUsuallyMap.put(type, list);
                mCurrUsuallyMap.put(type,new LinkedList<DecorationBean>(list));
            }
            return list;
        }
        return null;
    }

    /**
     * 保存常用
     * @param
     */
    public void saveUsullayData(){
        if(mCurrUsuallyMap != null)
            DataFileCache.getSingleton().asyncSaveData(getActivePetId()+Constants.USULLYDECORATION,mNewUsuallyMap);
        mCurrUsuallyMap = null;
        mNewUsuallyMap = null;
    }

    public void updateUsullayData(DecorationBean bean){
        if(bean == null)
            return;
        LinkedList<DecorationBean> list = null;
        if(mNewUsuallyMap == null){
            list = getUsullyQueue(bean.getType());
        }else {
            list = mNewUsuallyMap.get(bean.getType());
        }
        if(list != null){
            DecorationBean bean1 = null;
           for(int i=0;i<list.size();i++){
               DecorationBean temp = list.get(i);
               if(temp.getId().equals(bean.getId())){
                   bean1 = temp;
                   break;
               }
           }
            if(bean1 != null)
                list.remove(bean1);
            list.addFirst(bean);
        }
    }

    /**
     * 获取服务器的饰品信息
     */
    public void getServerDecorationData(){
        mState = -1;
        mNet.getDecorationData(getActivePetId());
    }
	
	/**
	 * 获取当前状态
	 *  -2 : 下载中
	 *  -1 : 下载数据失败
	 *  0  : 验证中
	 *  1  : 验证完毕
	 *  2  : 保存数据完毕
	 * @return
	 */
	public void setState(int state){
		this.mState = state;
	}
	
	/**
	 * 获取当前状态
	 *  -2 : 下载中
	 *  -1 : 下载数据失败
	 *  0  : 验证中
	 *  1  : 验证完毕
	 *  2  : 保存数据完毕
	 * @return
	 */
	public int getState(){
		return mState;
	}
	
	public void setDecorationData(DecorationRoot data) {
		if(data == null){
			mState = -1;
		}else {
			mData = data;
			mState = 2;
		}
		verifyDate();
//		mHandler.sendEmptyMessage(0);
		onDecorationCallback(getState() == 2);
	}
	
	public void setDecorationJson(String jsonString){
		if(TextUtils.isEmpty(jsonString))
			return;
		DecorationRoot data = JsonUtils.resultData(jsonString, DecorationRoot.class);
		setDecorationData(data);
	}
	
	private void verifyDate(){
		//1.先验证Sharedpreference文件是否新建,如果是新建则，保存已有的数据
		boolean flag = mSharedPreferences.getBoolean("init", true);
		if(flag){
			saveAssetsDecorationIds();
		}
	}
	
	protected void onDecorationCallback(Boolean result) {
		if(mListener != null)
			mListener.onDecorationCallback(result);
	}
	
	/**
	 * 保存数据
	 * @param data
	 */
	public void saveData(final DecorationRoot data){
		try {
            if(data != null)
			    DataFileCache.getSingleton().asyncSaveData(Constants.DECORATION, data);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 检查文件是否已下载
	 * @param bean
	 * @return
	 */
	public boolean checkFileDownload(DecorationBean bean){
		return checkFileDownload(bean.getFileName());
	}
	
	public boolean checkFileDownload(String key){
		return mSharedPreferences.getBoolean(key, false);
	}
	
	/**
	 * 修改文件下载状态
	 * @param bean
	 */
	public boolean setFileDonloadState(DecorationBean bean){
		return setFileDonloadState(bean.getFileName());
	}
	
	public boolean setFileDonloadState(String key){
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, true);
		return editor.commit();
	}
	
	/**
	 * 读取本地饰品信息
	 * @return
	 */
	public DecorationRoot readLocalDecorationData(){
		Object obj = null;
		try {
			obj = DataFileCache.getSingleton().loadObject(Constants.DECORATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(obj != null)
			return (DecorationRoot)obj;
		return null;
	}
	
	/**
	 * 保存Asstes中饰品状态
	 */
	private void saveAssetsDecorationIds(){
		InputStream is = null;
		try {
			is = mContext.getAssets().open("decoration.ver");
			if(is == null)
				return;
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String value = new String(buffer);
			if(!TextUtils.isEmpty(value)){
				String[] arr = value.split("\r\n");
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				for (int i = 0; i < arr.length; i++) {
					String uuid = arr[i];
					editor.putBoolean(uuid, true);
					editor.commit();
				}
				editor.putBoolean("init", false);
				editor.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is != null){
					is.close();
					is = null;
				}
			} catch (Exception e2) {
			}
		}
	}

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        setDecorationJson(bean.getValue());
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        switch (error.getCode()){
            case PetSayError.CODE_SESSIONTOKEN_DISABLE:
            case PetSayError.CODE_PERMISSION_ERROR:
                ToastUtiles.showDefault(mContext, R.string.seesiontoken_error);
                break;
        }
        setState(-1);
    }

    public interface DecorationTaskListener {
		public void onDecorationCallback(boolean success);
	}

    private String getActivePetId(){
        return UserManager.getSingleton().getActivePetId();
    }



}
