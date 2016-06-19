package com.petsay.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SystemNet;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ChechVersionVo;
import com.petsay.vo.ResponseBean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CheckVersionManager implements NetCallbackInterface {
	private Activity mActivity;
	private ChechVersionVo chechVersionVo;
	private OnCheckVersionListtener mCheckVersionListtener;
	public static final int DOWNLOAD_SUCCESS = 1;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				update();
				break;
			}
		}
	};

	private static CheckVersionManager _instance;

	public static CheckVersionManager getSingleton() {
		if (null == _instance) {
			_instance = new CheckVersionManager();
		}
		return _instance;
	}

    public void checkVersion(Activity activity){
        mActivity = activity;
        SystemNet net = new SystemNet();
        net.setCallback(this);
        net.systemVersion();
    }

	public void checkVersion(Activity activity,OnCheckVersionListtener checkVersionListtener) {
		mActivity = activity;
		mCheckVersionListtener = checkVersionListtener;
		SystemNet net = new SystemNet();
		net.setCallback(this);
		net.systemVersion();
	}

	ProgressBar bar = null;
	String UPDATE_SERVERAPK = "petsay.apk";

	//
	/**
	 * 更新版本
	 */
	@SuppressLint("NewApi")
	private void doNewVersionUpdate() {
		// String verName = getVerName(this);
		// if (chechVersionVo.isCompulsively()) {
		// pd = new ProgressDialog(context);
		// pd.setTitle("正在下载");
		// pd.setMessage("请稍后。。。");
		// pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// pd.setMax(100);
		// downFile(chechVersionVo.getDownloadUrl());
		// }else {
		// StringBuffer sb = new StringBuffer();
		// sb.append("发现版本：").append(chechVersionVo.getVname())
		// .append("\n" + chechVersionVo.getDescription());
		Builder builder;
		if (Build.VERSION.SDK_INT > 10)
			builder = new AlertDialog.Builder(mActivity,
					R.style.ver_update_dialog);
		else
			builder = new AlertDialog.Builder(mActivity);
		// Builder builder=new
		// AlertDialog.Builder(mActivity,R.style.ver_update_dialog);
		final Dialog dialog = builder.create();
		// dialog.setContentView(R.layout.check_version_dialog);
		// 显示更新框

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		Window window = dialog.getWindow();
		window.setContentView(R.layout.check_version_dialog);
		// dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.getWindow().setAttributes(lp);
		// dialog.
		TextView tvVerName = (TextView) window.findViewById(R.id.tv_vername);
		TextView tvUpdateMsg = (TextView) window.findViewById(R.id.tv_updatemsg);
		TextView tvUpdate = (TextView) window.findViewById(R.id.tv_update);
		TextView tvCancle = (TextView) window.findViewById(R.id.tv_cancle);
		WebView webView = (WebView) window.findViewById(R.id.web_content);
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
		tvVerName.setText(chechVersionVo.getVname());
		webView.loadData(chechVersionVo.getDescription(),
				"text/html; charset=UTF-8", null);
		// tvUpdateMsg.setText(chechVersionVo.getDescription());
		// tvUpdateMsg.setText(Html.fromHtml(chechVersionVo.getDescription()));
		// String
		// aaa="<ol><li><span>这一版增加了滤镜，加上了相框装饰，让爱宠美美的出镜；</span>	</li>	<li>		<span >消息提醒和消息推送功能，让好友的互动不再错过；</span>	</li>	<li>		<span >新增微调功能，随心调整位置；</span>	</li>	<li>		<span >修改了部分bug，体验更优。</span><span ></span>	</li></ol>";
		// tvUpdateMsg.setText(Html.fromHtml(aaa));
		tvUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// pd = new ProgressDialog(mActivity);
				// pd.setTitle("正在下载");
				// pd.setMessage("请稍后。。。");
				// pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				// pd.setMax(100);
				// pd.set
				PublicMethod.showToast(mActivity, "正在下载");
				downFile(chechVersionVo.getDownloadUrl());

			}
		});

		tvCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

	}

	/**
	 * 下载apk
	 */
	public void downFile(final String url) {
		UPDATE_SERVERAPK = url.substring(url.lastIndexOf("/") + 1);
		// pd.onShow();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				int i = 0;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					long fileSize = entity.getContentLength();
					long downSize = 0;
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								UPDATE_SERVERAPK);
						fileOutputStream = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int charb = -1;
						while ((charb = is.read(b)) != -1) {
							fileOutputStream.write(b, 0, charb);
							downSize += charb;
							int result = (int) (downSize * 100 / fileSize);
							// pd.setProgress(result);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
						fileOutputStream.close();
					down();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 下载完成，通过handler将下载对话框取消
	 */
	private void down() {
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
			}
		}.start();
	}

	/**
	 * 安装应用
	 */
	private void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), UPDATE_SERVERAPK)),
				"application/vnd.android.package-archive");
		mActivity.startActivity(intent);
	}

	public interface OnCheckVersionListtener {
		/**
		 * 检查版本成功
		 * 
		 * @param hasNew
		 */
		void onFinish(boolean hasNew);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		try {
			boolean hasNew;
			chechVersionVo = JsonUtils.resultData(bean.getValue(),ChechVersionVo.class);
			MobileInfoManager.getSingleton().getAppInfo(mActivity);
			if (Constants.VERSION_CODE < chechVersionVo.getVcode()) {
				// System.out.println("Constants.VERSION_CODE:"+Constants.VERSION_CODE+"     chechVersionVo.getVcode():"+chechVersionVo.getVcode());
				// System.out.println("IMEI:"+chechVersionVo.getDownloadUrl());
				doNewVersionUpdate();
				hasNew = true;
			} else {
				hasNew = false;
				// PublicMethod.showToast(context, "当前版本已为最新版本");
			}
            if(mCheckVersionListtener != null)
			    mCheckVersionListtener.onFinish(hasNew);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		// System.err.println("检查新版本失败");
	}

}
