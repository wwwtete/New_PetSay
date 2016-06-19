package com.petsay.vo.award;

import java.io.Serializable;

/**
 * 奖品照片
 * 
 * @author xiaoyi
 *
 */
public class AwardPicDTO implements Serializable {

	private static final long serialVersionUID = -2156798118686782883L;
	
	private String id;
	/**  奖品轮播图的url  */
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
