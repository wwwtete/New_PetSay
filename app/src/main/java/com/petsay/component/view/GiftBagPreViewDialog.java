package com.petsay.component.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.GiftBagNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.member.GiftBagVo;
import com.petsay.vo.decoration.DecorationBean;

import java.util.List;

/**
 * Created by wangw on 2014/12/16.
 *  礼包预览界面
 */
public class GiftBagPreViewDialog extends Dialog implements NetCallbackInterface,View.OnClickListener {

    private GiftBagVo mGifBag;
    private GiftBagNet mNet;
    private TextView mTvTitle;
    private TextView mTvdescription;
    private TextView mTvDraw;
    private LinearLayout mLayoutIcons;
    private ImageView mIvLoading;
    private GiftBagNet mBagNet;
    public GiftBagPreViewDialog(Context context,GiftBagNet net) {
        super(context);
        this.mBagNet = net;
        initVew();
    }

    private void initVew() {
        mNet = new GiftBagNet();
        mNet.setTag(this);
        mNet.setCallback(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.giftbag_preview_dialog, null);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvdescription = (TextView) view.findViewById(R.id.tv_description);
        mTvDraw = (TextView) view.findViewById(R.id.tv_draw);
        mLayoutIcons = (LinearLayout) view.findViewById(R.id.layout_icons);
        mIvLoading = (ImageView) view.findViewById(R.id.iv_loading);

        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(view);
        setCancelable(true);
    }

    public void showDialog(GiftBagVo giftBag){
        if(giftBag != null) {
            if(giftBag != mGifBag) {
                this.mGifBag = giftBag;
                getBifBagDetail();
                mLayoutIcons.removeAllViews();
                mIvLoading.setVisibility(View.VISIBLE);
                mTvdescription.setText(mGifBag.getDescription());
                mTvTitle.setText(mGifBag.getName());
                if (giftBag.getState() == 3) {
                    mTvDraw.setVisibility(View.VISIBLE);
                    mTvDraw.setOnClickListener(this);
                } else {
                    mTvDraw.setVisibility(View.GONE);
                    mTvDraw.setOnClickListener(null);
            }
            }
            show();
        }
    }

    public void closeDialog(){
        this.dismiss();
    }

    public void release(){
        if(this.isShowing()){
            closeDialog();
        }
        if(mNet != null) {
            mNet.cancelAll(this);
            mNet = null;
        }
    }

    private void getBifBagDetail(){
        mNet.getBifBagDetail(mGifBag.getCode());
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode){
            case RequestCode.REQUEST_GETBIFBAGDETAIL:
                onGetBifBagDetail(bean);
                break;
        }
    }

    private void onGetBifBagDetail(ResponseBean bean) {
        try {
            List<DecorationBean> list = JsonUtils.getList(bean.getValue(),DecorationBean.class);
            mLayoutIcons.removeAllViews();
            mIvLoading.setVisibility(View.GONE);
            int size = PublicMethod.getDiptopx(getContext(),80);
            int left = PublicMethod.getDiptopx(getContext(),10);
            for(int i = 0;i < list.size(); i++){
                DecorationBean db = list.get(i);
                ImageView img = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
                params.leftMargin = left;
                img.setLayoutParams(params);
                ImageLoaderHelp.displayImage(db.getThumbnail(),img, ImageLoaderOptionsManager.getGiftBagOptions());
                mLayoutIcons.addView(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        PublicMethod.showToast(getContext(),"获取礼包信息失败");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                closeDialog();
                break;
            case R.id.tv_draw:
                mBagNet.drawGifBag(UserManager.getSingleton().getActivePetId(),mGifBag.getCode());
                mTvDraw.setVisibility(View.GONE);
                break;
        }
    }

}
