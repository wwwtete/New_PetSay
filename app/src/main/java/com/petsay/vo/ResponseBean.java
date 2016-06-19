package com.petsay.vo;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 服务器返回数据对象
 * @author wangw
 *
 */
public class ResponseBean implements Parcelable{
	private int error = 0;
	private String message = "";
	private String value = "";
    /**用于区分同一个请求执行不同的操作时所用*/
    private Object mTag;
    /**当前的请求是否执行加载更多操作*/
    private boolean mIsMore;

    //广场需要调用的布局样式
	private int layout;

	public ResponseBean() {
	}

	public ResponseBean(int errorCode) {
		this.error = errorCode;
	}

	public int getError() {
		return error;
	}
	public void setError(int error_code) {
		this.error = error_code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getValue() {
		return value.toString();
	}
	public void setValue(String result) {
		this.value = result;
	}

    /**
     * @return 用于区分同一个请求执行不同的操作时所用
     */
    public Object getTag() {
        return mTag;
    }

    /**
     * 用于区分同一个请求执行不同的操作时所用
     * @param mTag 可以是任何类型的值
     */
    public void setTag(Object mTag) {
        this.mTag = mTag;
    }

    /**
     * @return 用于区分当前的请求是否加载更多操作，默认是false,
     * true:执行刷新操作  false：执行加载更多操作
     */
    public boolean isIsMore() {
        return mIsMore;
    }

    /**
     * 用于区分当前的请求是否加载更多操作，默认是true,
     * true:执行刷新操作  false：执行加载更多操作
     * @param isMore 默认为false
     */
    public void setIsMore(boolean isMore) {
        this.mIsMore = isMore;
    }

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(error);
		dest.writeString(message);
		dest.writeString(value);
        dest.writeValue(mTag);
	}

	public int getLayout() {
		return layout;
	}
	public void setLayout(int layout) {
		this.layout = layout;
	}

	public static final Parcelable.Creator<ResponseBean> CREATOR = new Parcelable.Creator<ResponseBean>() {

		@Override
		public ResponseBean createFromParcel(Parcel source) {
			ResponseBean bean = new ResponseBean();
			bean.error = source.readInt();
			bean.message = source.readString();
			bean.value = source.readString();
            bean.mTag = source.readValue(Object.class.getClassLoader());
			return bean;
		}

		@Override
		public ResponseBean[] newArray(int size) {
			return new ResponseBean[size];
		}
	};
	
}
