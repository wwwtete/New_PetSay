package com.petsay.activity.petalk.publishtalk;

import android.content.Intent;
import android.view.MotionEvent;

import com.petsay.component.customview.module.BasicSurfaceViewModule;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/30
 * @Description
 */
public class SimpleEditActivity extends BaseEditActivity {


    @Override
    protected void initGroupTitleView() {
        if(mRootData != null) {
            List<DecorationTitleBean> beans = mRootData.getChildren();
            List<DecorationTitleBean> list = new ArrayList<DecorationTitleBean>();
            for(int i = 0;i<beans.size();i++){
                if(!BaseEditActivity.TYPE_MOUTH.equals(beans.get(i).getType())){
                    list.add(beans.get(i));
                }
            }
            mDtGroupview.setGroupData(list);
        }
    }


    @Override
    protected void onNext() {
        boolean flag = updatePublishVale();
        showLoading();
        if(flag){
            compositeImages(true);
            closeLoading();
            if(PublishParam.editImg == null){
                PublicMethod.showToast(this, "合成图片失败！请重试");
            }else {
                Intent intent = new Intent(this, PublishTalkActivity.class);
                this.startActivity(intent);
            }
        }else {
            closeLoading();
            showToast("编辑失败，请联系客服");
        }
    }

    @Override
    public void onTouchModule(BasicSurfaceViewModule module, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if(module == null || !mEditView.getFocusable())
                    mEditView.hidenBorder(true);
                break;
        }
    }

    public boolean updatePublishVale(){
        if(PublishParam == null){
            PublishParam = new PublishTalkParam();
        }
        try{
            PublishParam.petId = getActivePetId();
            PublishParam.model = 1;
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
