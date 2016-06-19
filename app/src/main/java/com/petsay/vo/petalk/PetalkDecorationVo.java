package com.petsay.vo.petalk;

import android.os.Parcel;
import android.os.Parcelable;

import com.petsay.vo.decoration.DecorationBean;

import java.io.Serializable;

/**
 * @author wangw
 * 饰品坐标及缩放等信息
 * 
 */
public class PetalkDecorationVo implements Parcelable,Serializable{
	private static final long serialVersionUID = -5050615621944098221L;
	private String id;// 主键
	private String name;
	private String petalkId;
	private String decorationId;
	private float width;// 宽
	private float height;// 高
	private float centerX;// 中心点偏移X
	private float centerY;// 中心点偏移Y
	private float rotationX;// 旋转
	private float rotationY;// 旋转
	private double rotationZ;// 旋转
	
	private DecorationBean origin;
	
	public DecorationBean getOrigin() {
		return origin;
	}
	public void setOrigin(DecorationBean origin) {
		this.origin = origin;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getCenterX() {
		return centerX;
	}
	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}
	public float getCenterY() {
		return centerY;
	}
	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}
	public float getRotationX() {
		return rotationX;
	}
	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}
	public float getRotationY() {
		return rotationY;
	}
	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}
	public double getRotationZ() {
		return rotationZ;
	}
	public String getPetalkId() {
		return petalkId;
	}
	public void setPetalkId(String petalkId) {
		this.petalkId = petalkId;
	}
	public String getDecorationId() {
		return decorationId;
	}
	public void setDecorationId(String decorationId) {
		this.decorationId = decorationId;
	}
	public void setRotationZ(double rotationZ) {
		this.rotationZ = rotationZ;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeFloat(width);
		dest.writeFloat(height);
		dest.writeFloat(centerX);
		dest.writeFloat(centerY);
		dest.writeFloat(rotationX);
		dest.writeFloat(rotationY);
		dest.writeDouble(rotationZ);
		dest.writeString(petalkId);
		dest.writeString(decorationId);
		
		
	}
	
	public static final Parcelable.Creator<PetalkDecorationVo> CREATOR = new Parcelable.Creator<PetalkDecorationVo>() {

		@Override
		public PetalkDecorationVo createFromParcel(Parcel source) {
			PetalkDecorationVo animationImg = new PetalkDecorationVo();
			animationImg.id=source.readString();
			animationImg.name = source.readString();
			animationImg.centerX=source.readFloat();
			animationImg.centerY=source.readFloat();
			animationImg.rotationX=source.readFloat();
			animationImg.rotationY=source.readFloat();
			animationImg.rotationZ=source.readDouble();
			animationImg.petalkId = source.readString();
			animationImg.decorationId = source.readString();
			return animationImg;
		}

		@Override
		public PetalkDecorationVo[] newArray(int size) {
			return new PetalkDecorationVo[size];
		}
	};
	
	
}
