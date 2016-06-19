package com.petsay.activity.global;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.utile.ActivityTurnToManager;

import roboguice.inject.InjectView;

/**
 * @author wangw
 *
 */
public class WebViewActivity extends BaseActivity {

	@InjectView(R.id.webview)
	private WebView mWebView;
	
	@InjectView(R.id.text)
	private TextView mTextView;
	private String mUrl;
	private String mContent;
	public static int FROM_PUSH=1;
	private String titleStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		getData();
		initView();
	}

	private void getData() {
//		String folderPath = getIntent().getStringExtra("folderPath");
		if(TextUtils.isEmpty(titleStr))
			titleStr = "宠物说";
		initTitleBar(titleStr);
		mUrl = getIntent().getStringExtra("url");
		mContent=getIntent().getStringExtra("content");
		
//		if(TextUtils.isEmpty(mUrl))
//			this.finish();
	}

	protected void initView() {
		super.initView();
		mTitleBar.setFinishEnable(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true); 
		 WebChromeClient wvcc = new WebChromeClient() {  
	            @Override  
	            public void onReceivedTitle(WebView view, String title) {  
	                super.onReceivedTitle(view, title);  
//	                PetsayLog.d("ANDROID_LAB", "TITLE=" + folderPath);
//	                txtTitle.setText("ReceivedTitle:" +folderPath);
	                if (TextUtils.isEmpty(titleStr)||titleStr.equals("标题")) {
	                	initTitleBar(title);
					}
	                
	            }  
	  
	        };  
	        // 设置setWebChromeClient对象  
	        mWebView.setWebChromeClient(wvcc);  
	        
	        mWebView.setWebViewClient(new WebViewClient() {
	       	 
	            /* (non-Javadoc)
	             * @see   android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	             */
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            	view.loadUrl(url);   //在当前的webview中跳转到新的url
	            	 
	                return true;
	            }
	             
	        });
		setProgressBarIndeterminateVisibility(true);
		setProgressBarVisibility(true);
		if(TextUtils.isEmpty(mUrl)){
			if (TextUtils.isEmpty(mContent)) {
				this.finish();
			}else {
				mWebView.setVisibility(View.GONE);
				mTextView.setVisibility(View.VISIBLE);
				mTextView.setText(mContent);
			}
			
		}else {
			mWebView.setVisibility(View.VISIBLE);
			mTextView.setVisibility(View.GONE);
			mWebView.loadUrl(mUrl);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void finish() {
		ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
		if(viewGroup != null){
			viewGroup.removeAllViews();
		}
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		mWebView.stopLoading();
		mWebView.destroy();
		mWebView = null;
		if (getIntent().getIntExtra("from", 0)==FROM_PUSH) {
			ActivityTurnToManager.getSingleton().returnMainActivity(WebViewActivity.this);
		}
		super.onDestroy();
	}
	
}
