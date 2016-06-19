package com.petsay.cache;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataFileCache {
	private static Context mContext;
	private static DataFileCache _instance;
	public static DataFileCache getSingleton(){
		if (null==_instance) {
			_instance=new DataFileCache();
		}
		return _instance;
	}
	public static void initContext(Context context){
		mContext=context;
	}
	private DataFileCache(){};

	    /**  
	     * 从字节数组获取对象  
	     * @EditTime 2007-8-13 上午11:46:34  
	     */   
	    private  Object getObjectFromBytes(byte[] objBytes) throws Exception {   
	        if (objBytes == null || objBytes.length == 0) {   
	            return null;   
	        }   
	        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);   
	        ObjectInputStream oi = new ObjectInputStream(bi);   
	        return oi.readObject();   
	    }   
	  
	   
	    
	    public  void saveObj(String filename,Serializable obj) throws Exception{
	    	 if (obj == null) {   
		            return ;   
		        }   
		        ByteArrayOutputStream bo = new ByteArrayOutputStream();   
		        ObjectOutputStream oo = new ObjectOutputStream(bo);   
		        oo.writeObject(obj);   
	    	/*
	 	    * 1、根据上下文对象能快速得到一个文件输出流对象；
	 	    * 2、私有操作模式：创建出来的文件只能被本应用访问，其他应用无法访问该文件：Context.MODE_PRIVATE；
	 	    * 另外采用私有操作模式创建的文件，写入的内容会覆盖原文件的内容。
	 	    * 3、openFileOutput()方法的第一个参数用于指定文件名称，不能包含路径分隔符"/"，如果文件不存在，
	 	    * Android会自动创建它，创建的文件保存在/data/data/<package name>/files目录，如/data/data/org.example.files/files.
	 	    */
	 	   FileOutputStream outStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
	 	   //把字符串传化为二进制数据写入到文件中
	 	   
	 	   outStream.write(bo.toByteArray());
	 	   //然后关掉这个流
	 	   outStream.close();
	    }
	    
	    public Object loadObject(String fileName) throws Exception{
	    	 /*
	 	    * 1、从上下文对象中得到一个文件输入流对像，context.openFileInput(filename)得到文件输入流对象；
	 	    * 2、
	 	    */
	 	   FileInputStream inStream = mContext.openFileInput(fileName);
	 	   //把每次读到的数据都存放在内存中
	 	   ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	 	   //定义数组大小
	 	   byte[] buffer = new byte[inStream.available()];
	 	   int len = 0;
	 	   //读取这个输入流数组,判断数据是否读完
	 	   while((len = inStream.read(buffer)) != -1)
	 	   {
	 	    outStream.write(buffer,0,len);
	 	   }
	 	   //从内存中获取得到的数据
	 	   byte[] data = outStream.toByteArray();
	    	
	    	return getObjectFromBytes(data);
	    }
	    
	    /**
	     * 使用异步保存数据
	     */
	    public void asyncSaveData(final String filename,final Serializable obj){
	    	Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						saveObj(filename, obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	    	thread.start();
	    }
}
