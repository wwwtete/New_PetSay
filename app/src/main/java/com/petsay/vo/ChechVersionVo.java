package com.petsay.vo;

public class ChechVersionVo {
	private String vname;
	private int vcode;
	private String downloadUrl;
	private String description;
	private String compulsively;
	
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public int getVcode() {
		return vcode;
	}
	public void setVcode(int vcode) {
		this.vcode = vcode;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String isCompulsively() {
		return compulsively;
	}
	public void setCompulsively(String compulsively) {
		this.compulsively = compulsively;
	}
}
