package com.petsay.vo.decoration;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangw
 * 嘴型和饰品数据模型
 */
public class DecorationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4483236408297300357L;
	private String deleted;
	
	private String id;// ID
	private String name;// 名称
	private String type;// 细类
	private String thumbnail;// 缩略图

	private String url;// 资源标识
	private String hash;// 资源HASH
	private String fileName;// 资源文件名
	private String fileType;// 资源文件类型
	private int fileCount;// 资源数量
	private String createTime;// 创建时间
    private List<String> files;
	
	/**是否已下载本地*/
	private boolean isDownloaded;
	/**是否在assets文件夹下*/
	private boolean assetsed;
	/**是否下载中*/
	private transient boolean downloading;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getDeleted() {
		return "true".equals(deleted);
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getFileCount() {
		return fileCount;
	}
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
	public boolean isAssetsed() {
		return assetsed;
	}
	public void setAssetsed(boolean assetsed) {
		this.assetsed = assetsed;
	}
	public boolean isDownloaded() {
		return isDownloaded;
	}
	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	
	public  boolean isDownloading() {
		return downloading;
	}
	public void setDownloading(boolean downloading) {
		this.downloading = downloading;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof DecorationBean){
			return id.equals(((DecorationBean)o).getId());
		}
		return false;
	}
	
}
