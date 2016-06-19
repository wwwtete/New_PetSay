package com.petsay.network.download;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wangw
 * 下载任务队列
 */
public class DownloadQueue {

	private static DownloadQueue mInstance;
	
	public static DownloadQueue getInstance(){
		if(mInstance == null){
			mInstance = new DownloadQueue();
		}
		return mInstance;
	}
	
	private List<DownloadTask> mDownloadQueue;
	
	public DownloadQueue(){
		mDownloadQueue = new ArrayList<DownloadTask>();
	}
	
	public boolean add(DownloadTask task){
		return mDownloadQueue.add(task);
	}
	
	public boolean remove(DownloadTask task){
		if(mDownloadQueue.size() > 0)
			return mDownloadQueue.remove(task);
		else {
			return false;
		}
	}
	
	/**
	 * 根据Tag取消下载
	 * @param tag
	 */
	public void cancelDownload(Object tag){
		synchronized (mDownloadQueue) {
			for (int i = 0; i < mDownloadQueue.size(); i++) {
				DownloadTask task = mDownloadQueue.get(i);
				if(tag == null && task.getTag() == null){
					task.cancel(true);
				}else if(tag instanceof String && tag.equals(task.getTag())){
					task.cancel(true);
				}else if(tag == task.getTag()){
					task.cancel(true);
				}
			}
		}
	}
	
	public void cancleDownloadAll(){
		synchronized (mDownloadQueue) {
			for (int i = 0; i < mDownloadQueue.size(); i++) {
				mDownloadQueue.get(i).cancel(true);
			}
		}
	}
	
}
