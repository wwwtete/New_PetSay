package com.petsay.vo.forum;

import java.io.Serializable;

/**
 * 主题
 * 
 * @author xiaoyi
 *
 */
public class TopicDTO implements Serializable {

	private static final long serialVersionUID = 5263054680105319551L;
	
	private String id;
	private String content;
	private String pic;
	private long viewCount;
	private String top;
	private long createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public long getViewCount() {
		return viewCount;
	}
	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
