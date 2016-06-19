package com.petsay.vo.award;

import java.io.Serializable;

/**
 * 广告展位图
 * 
 * @author xiaoyi
 *
 */
public class BannerPicDTO implements Serializable {

	private static final long serialVersionUID = 964819968027632907L;
	
	private String id;
	/**  广告轮播图的url  */
	private String url;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
