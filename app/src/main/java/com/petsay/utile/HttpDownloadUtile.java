package com.petsay.utile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;

import android.text.TextUtils;



/**
 * @author wangw
 * 核心下载
 */
public class HttpDownloadUtile {

	//	private HttpDownloadListener mListener;
	//	
	//	public HttpDownloadUtile(){
	//	}
	//	
	//	/**
	//	 * 设置监听函数
	//	 */
	//	public void setOnDownloadListener(HttpDownloadListener listener){
	//		this.mListener = listener;
	//	}

	/**
	 * 开始下载
	 * @param url
	 * @param targetDirs
	 */
	public static File download(String url,String targetDirs){
//		File target = new File(targetDirs);
//		if(!target.exists()){
//			target.mkdirs();
//		}
//		FileOutputStream out = null;
//		File file = null;
//		try {
//			URL ul = new URL(url);
//			HttpURLConnection connection = (HttpURLConnection) ul.openConnection();
//			InputStream in = connection.getInputStream();
//			file = new File(target.getAbsolutePath()+File.separator+FileUtile.getFileNameByUrl(url));
//			if(file.exists()){
//				file.delete();
//			}
//			if(!file.exists())
//				file.createNewFile();
//			out = new FileOutputStream(file);
//			byte[] buffer = new byte[512*1024];
//			int readlength = 0;
//			while((readlength = in.read(buffer)) != -1){
//				out.write(buffer,0,readlength);
//			}
//			out.flush();
//			out.close();
//			out = null;
//			in.close();
//			in = null;
//			//			downloadFinish(true, file);
//			return file;
//		} catch (Exception e) {
//			e.printStackTrace();
//			//			downloadFinish(false, null);
//			return null;
//		}
		return download(url, targetDirs, null);
	}


	public static File download(String url,String targetDirs,DownloadProgressCallback listener) {
		File target = new File(targetDirs);
		if(!target.exists()){
			target.mkdirs();
		}
		File file = null;
//		BufferedInputStream bis = null;
		InputStream in = null;
		BufferedOutputStream bos = null;
		try {
			URL ul = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) ul.openConnection();
			connection.setConnectTimeout(12000);//连接超时为120秒
			in = connection.getInputStream();
			file = new File(target.getAbsolutePath()+File.separator+FileUtile.getFileNameByUrl(url));
			if(file.exists()){
				file.delete();
			}
			if(!file.exists())
				file.createNewFile();
			long total = 0;
			long current = 0;
			String length = connection.getHeaderField("Content-Length");
			if(!TextUtils.isEmpty(length))
				total = Long.valueOf(length);
//			bis = new BufferedInputStream(in);
			FileOutputStream out = new FileOutputStream(file);
			bos = new BufferedOutputStream(out);

			byte[] buffer = new byte[256*1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				current += len;
				if (listener != null) {
					listener.onUpdateProgress(total, current);
				}
			}
			bos.flush();
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(bos);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(bos);
		}
	}

	//	protected void downloadFinish(boolean flag,File file){
	//		if(mListener != null)
	//			mListener.onDownloadFinish(flag, file);
	//	}




	public interface DownloadProgressCallback{
		public void onUpdateProgress(long total, long current);
	}

	}
