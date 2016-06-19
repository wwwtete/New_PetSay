package com.petsay.utile.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.petsay.utile.FileUtile;

import android.os.AsyncTask;

/**
 * @author wangw
 * 解压zip文件工具类
 */
public class UnZipTask extends AsyncTask<Void, Void, Boolean> {

	private InputStream mInputStream;
	private File mTarget;
	private UnzipListener mListener;
	
	public UnZipTask(InputStream in,File targetDirs){
		this.mInputStream = in;
		mTarget = targetDirs;
	}
	
	public void setUnzipListener(UnzipListener listener){
		mListener = listener;
	}
	
	public void removeUnzipListener(){
		mListener = null;
	}
	
//	@Override
//	public void run() {
//		currentThread().setName("UnzipThread"+"_"+currentThread().getName());
//		if(mInputStream == null)
//			return;
		
//		boolean b = false;
//		//创建解压后的文件夹
//		if(!mTarget.exists())
//			b = mTarget.mkdirs();
////		PublicMethod.log_d("创建文件夹："+b);
//		
//		//初始化zipStream数据流
//		ZipInputStream zipStream = new ZipInputStream(mInputStream);
//		//获取一个文件实体
//		try {
//			ZipEntry entry = zipStream.getNextEntry();
//			//初始化缓存区对象
//			byte[] buffer = new byte[512*1024];
//			int readCount = 0;
//			//遍历压缩包内的文件
//			while (entry != null) {
//				//判断是否为目录
//				if(!entry.isDirectory()){
//					File temp = new File(mTarget.getAbsolutePath(),FileUtile.getFileNameByUrl(entry.getName()));
//					if(!temp.exists()){
//						temp.createNewFile();
//						FileOutputStream outputStream = new FileOutputStream(temp);
//						while((readCount = zipStream.read(buffer)) > 0){
//							outputStream.write(buffer,0,readCount);
//						}
//						outputStream.flush();
//						outputStream.close();
//						outputStream = null;
//					}
//				}
//				entry = zipStream.getNextEntry();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				zipStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		boolean flag = FileUtile.unZip(mInputStream, mTarget);
//		
//		if(mListener != null){
//			mListener.onUnzipFinishListener(mTarget,flag);
//		}
//	}
	
	public interface UnzipListener{
		public void onUnzipFinishListener(File outDirs,boolean success);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		return FileUtile.unZip(mInputStream, mTarget);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(mListener != null){
			mListener.onUnzipFinishListener(mTarget,result);
		}
		super.onPostExecute(result);
	}
	
}
