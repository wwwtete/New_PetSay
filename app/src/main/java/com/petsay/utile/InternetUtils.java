package com.petsay.utile;

import android.util.Log;

import com.petsay.constants.Constants;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangw 联网事物工具类
 */
public class InternetUtils {
	private static final int TIMEOUT = 30000;
	private static final String TAG = "InternetUtils";

	/**
	 * connect Internet http request by get method
	 * 
	 * @param urlString
	 * @return
	 */
	public static HttpResponse OpenHttpConnection(String urlString) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		DefaultHttpClient hc = new DefaultHttpClient(params);
		HttpGet get = new HttpGet();
		try {
			get.setURI(new URI(urlString));
			return hc.execute(get);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get method 普通联网
	 * 
	 * @param url
	 * @return String 服务器相应字符串格式
	 */
	public static String GetMethodOpenHttpConnectJrt(String url) {
		Log.e("url====", "" + url);
		try {
			HttpParams params = new BasicHttpParams();

			if (Constants.isProxyConnect == true) {
				final HttpHost proxy = new HttpHost(Constants.mProxyHost,
						Constants.mProxyPort, "http");
				params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT);
			HttpConnectionParams.setSocketBufferSize(params, 8192);
			DefaultHttpClient hc = new DefaultHttpClient(params);
			HttpGet get = new HttpGet();
			HttpResponse response = null;
			try {
				get.setURI(new URI(url));
				response = hc.execute(get);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				try {
					InputStream is = response.getEntity().getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					StringBuffer sb = new StringBuffer();
					String temp = "";
					// 以字符方式显示文件内容
					while ((temp = br.readLine()) != null) {
						sb.append(temp);
					}
					return sb.toString();
				} catch (IllegalStateException e) {
					return "";
				} catch (IOException e) {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {

			return "";
		}

	}
	
	/**
	 * 新的网络请求数据，修复网络异常时，返回正确的json格式
	 * @param jsonProtocol 字符串
	 * @return 
	 */
	public static String GetMethodOpenHttpConnectSecurity(org.json.JSONObject jsonProtocol){
		return GetMethodOpenHttpConnectSecurityNew(Constants.LOT_SERVER_V1,jsonProtocol.toString());
	}
	
	public static String GetMethodOpenHttpConnectSecurity(String json){
		return GetMethodOpenHttpConnectSecurityNew(Constants.LOT_SERVER_V1,json);
	}
	
	
	/**
	 * get method 安全联网
	 * 新的网络请求数据，修复网络异常时，返回正确的json格式
	 * @param url
	 * @return String 服务器相应字符串格式
	 */
	public static String GetMethodOpenHttpConnectSecurityNew(String url,
			String data) {
		InputStreamEntity entity = null;
		try {
			HttpParams params = new BasicHttpParams();

			if (Constants.isProxyConnect == true) {
				final HttpHost proxy = new HttpHost(Constants.mProxyHost,
						Constants.mProxyPort, "http");
				params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT);
			HttpConnectionParams.setSocketBufferSize(params, 8192);
			DefaultHttpClient hc = new DefaultHttpClient(params);
			HttpPost post = new HttpPost();

			try {
//				StringBufferInputStream fis = new StringBufferInputStream(
//						ToolsAesCrypt.Encrypt(data, Constants.KEY));
//				StringBufferInputStream fis = new StringBufferInputStream(data);
//				entity = new InputStreamEntity(fis, fis.available());
				StringEntity entity2 = new StringEntity(data, "UTF-8");  

//				entity2.setContentEncoding("UTF-8");
				((HttpPost) post).setEntity(entity2);
			} catch (Exception e) {
				e.printStackTrace();
			}

			HttpResponse response = null;
			try {
				post.setURI(new URI(url));
				response = hc.execute(post);
			} catch (IOException e) {
				e.printStackTrace();
				return "{\"error\":-1,\"message\":\"网络连接超时\"}";
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"error\":-1,\"message\":\""+e.getMessage()+"\"}";
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				try {
					InputStream is = response.getEntity().getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					StringBuffer sb = new StringBuffer();
					String temp = "";
					// 以字符方式显示文件内容
					while ((temp = br.readLine()) != null) {
						sb.append(temp);
					}

//					String deStr = ToolsAesCrypt.Decrypt(sb.toString(),
//							Constants.KEY);
//
//					byte decompress[] = Base64.decode(deStr);
//					byte[] decompress2 = PublicMethod.decompress2(decompress);
//					String str = "";
//					try {
//						str = new String(decompress2, "utf-8");
//					} catch (UnsupportedEncodingException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					return sb.toString();

				} catch (Exception e) {
					return "{\"error\":-1,\"message\":\"数据异常\"}";
				}
			} else {
				return "{\"error\":-1,\"message\":\"网络连接超时\"}";
			}
		} catch (Exception e) {
			return "{\"error\":-1,\"message\":\""+e.getMessage()+"\"}";
		}

	}

	/**
	 * connect Internet http request by post method
	 * 
	 * @param postUrl
	 * @param uploadContextMap
	 *            post 上传参数,格式 Map<String key,String value>
	 * @return HttpResponse
	 */
	public static HttpResponse OpenHttpConnection(String postUrl,
			Map<String, String> uploadContextMap) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		DefaultHttpClient hc = new DefaultHttpClient(params);
		HttpPost post = new HttpPost();
		try {
			post.setURI(new URI(postUrl));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		post.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
		post.setHeader("Accept-Encoding", "gzip,deflate");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : uploadContextMap.keySet()) {
			nvps.add(new BasicNameValuePair(key, uploadContextMap.get(key)));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		try {
			return hc.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}