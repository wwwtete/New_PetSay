package com.petsay.activity.petalk.publishtalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.customview.ExSurfaceViewListener;
import com.petsay.component.customview.MultiframeSurfaceView;
import com.petsay.component.customview.module.BasicSurfaceViewModule;
import com.petsay.component.customview.module.DialogSurfaceViewModule;
import com.petsay.component.customview.module.EditSurfaceViewModule;
import com.petsay.component.customview.module.RahmenSurfaceModule;
import com.petsay.component.view.AdjustBarView;
import com.petsay.component.view.publishtalk.DecoratioUsuallyListView;
import com.petsay.component.view.publishtalk.DecorationGroupTitleView;
import com.petsay.component.view.publishtalk.DecorationItemView;
import com.petsay.component.view.publishtalk.DecorationMoreView;
import com.petsay.constants.Constants;
import com.petsay.network.download.DownloadTask;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.decoration.DecorationBean;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.decoration.DecorationRoot;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/29
 * @Description
 */
public abstract class BaseEditActivity extends BaseActivity implements DecorationDataManager.DecorationTaskListener, DecorationGroupTitleView.ClickDecorationGroupCallback, DecoratioUsuallyListView.ClickUsuallyDecorationCallback, DialogSurfaceViewModule.DialogSurfaceViewModuleCallback, View.OnClickListener, AdjustBarView.AdjustBarCallback, ExSurfaceViewListener, DecorationMoreView.DecorationMoreViewCallback {

    /**拍照后的图片*/
    public static Bitmap CameraBmp;
//    /**编辑后的图片*/
//    public static Bitmap EditedPetBmp;

    public static PublishTalkParam PublishParam;
    public static BaseEditActivity Instance;

//    public static Bitmap PET_BACKGROUD;
//    public static Bitmap PET_THUMB;

    /**单个嘴型*/
    public static final String TYPE_MOUTH = "mouth";
    /**服装饰品贴纸*/
    public static final String TYPE_COSTUME = "costume";
    /**对话框*/
    public static final String TYPE_DIALOG = "dialog";
    /**相框*/
    public static final String TYPE_RAHMEN = "rahmen";
    /**滤镜*/
    public static final String TYPE_FILTER = "filter";
    public static final String ID_DELETE = "delete";
//    /**保存每个类型最大常用的饰品数量*/
//    protected static int MAXUSUALLYCOUNT = 15;
    @InjectView(R.id.dt_groupview)
    protected DecorationGroupTitleView mDtGroupview;
    @InjectView(R.id.dt_moreview)
    protected DecoratioUsuallyListView mDtUsuallyview;
    @InjectView(R.id.adjustview)
    protected AdjustBarView mAdjustview;
    @InjectView(R.id.decorate_view)
    protected MultiframeSurfaceView mEditView;
    @InjectView(R.id.moreview)
    protected DecorationMoreView mMoreView;

    @InjectView(R.id.layout_input)
    private View mInputView;
    @InjectView(R.id.tv_ok)
    private TextView mTvOk;
    @InjectView(R.id.tv_cancel)
    private TextView mTvCancel;
    @InjectView(R.id.et_input)
    private EditText mEditInput;

    protected Bitmap mControlBmp;
    protected Bitmap mDeleteBmp;
    protected DecorationRoot mRootData;
    /**本地常用*/
    protected List<DecorationBean> mTotalUsually;
    /**正在使用的常用Map*/
//    protected HashMap<String,LinkedList<DecorationBean>> mCurrUsuallyMap;
    private Bitmap mOkBmp;
    private DialogSurfaceViewModule mDialogModule;
    private RahmenSurfaceModule mRahmen;

    protected DecorationDataManager mDataManager;
    protected boolean mInitOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        PublishParam  = new PublishTalkParam();;
        PublishParam.setTag((com.petsay.vo.petalk.PetTagVo) getIntent().getSerializableExtra("tag"));
        setView();
        preparView();
        initData();
    }

    /*************************没显示前的准备工作****************************************************************/
    protected void setView() {
        setContentView(R.layout.activity_editimage);
    }

    /**
     * 开始准备View
     */
    private void preparView() {
        super.initView();
        initTitleBar("编辑");
        initEditView();
        setListener();
    }

    private void setListener() {
        mDtGroupview.setCallback(this);
        mDtUsuallyview.setCallback(this);
        mMoreView.setCallback(this);
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mAdjustview.setOnCallback(this);
        mEditInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    onSubmitInput();
                }
                return false;
            }
        });
        mEditView.setOnListener(this);
    }

    @Override
    protected void initTitleBar(String title) {
        super.initTitleBar(title);
        mTitleBar.setBackgroundColor((Color.parseColor("#5a5a5a")));
        mTitleBar.setFinishEnable(true);

        TextView next= PublicMethod.addTitleRightText(mTitleBar, "下一步");
//        next.setText("下一步");
//        next.setTextColor(Color.WHITE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
    }

    protected abstract void onNext();

    /**
     * 初始化编辑SurfaceView
     */
    private void initEditView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEditView.getLayoutParams();
        int dw = PublicMethod.getDisplayWidth(this);
        params.width = dw;
        params.height = dw;
        mEditView.setLayoutParams(params);
        mEditView.setViewHeight(dw);
        mEditView.setViewWidth(dw);
    }

    protected void initData(){
        mDataManager = DecorationDataManager.getInstance(this);
//        mCurrUsuallyMap = mDataManager.getLocalUsullyData(getActivePetId());
        checkData();
    }

    private void checkData() {
        mRootData = mDataManager.getData();
        if(mRootData == null){
            showToast("正在努力加载数据。。。");
            mDataManager.setListener(this);
            if(mDataManager.getState() == -1){
                mDataManager.getServerDecorationData();
            }
        }
    }

    @Override
    public void onDecorationCallback(boolean success) {
        if(success){
            mRootData = mDataManager.getData();
            if(mInitOver){
                initShowViewByData();
            }
        }else {
            showToast("获取失败！请重试");
        }
    }

    /************************************开始显示********************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        if(!mInitOver) {
            mInitOver = true;
            initEditBg();
            initShowViewByData();
        }
    }

    /**
     * 初始化依赖饰品数据展示的View
     */
    protected void initShowViewByData(){
        initGroupTitleView();
    }

    /**
     * 初始化编辑View的背景图片
     */
    private void initEditBg() {
        mControlBmp = BitmapFactory.decodeResource(getResources(), R.drawable.rangd);
        mDeleteBmp = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
        int dw = PublicMethod.getDisplayWidth(this);
        CameraBmp = FileUtile.createScaledBitmap(CameraBmp, dw, dw);
        PublishParam.cameraImg = CameraBmp;
        if(CameraBmp != null) {
            BasicSurfaceViewModule bgM = new BasicSurfaceViewModule(CameraBmp);
            mEditView.setBackGroundModule(bgM);
        }
    }

    /**
     * 初始化饰品组标题
     */
    protected abstract void initGroupTitleView();

    /****************************************退出到后台*************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Instance = null;
        mDataManager.saveUsullayData();
        mDataManager.saveData(mRootData);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mMoreView !=null && mMoreView.isShow()){
                mMoreView.onHiden();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /****************************************公用方法和回调*****************************************************/

    public void compositeImages(boolean compositeAll){
        try {
            PublishParam.editImg = mEditView.compositeBitmap(compositeAll);
            Bitmap temp = mEditView.compositeBitmap(true);
            PublishParam.thumbImg = FileUtile.createScaledBitmap(temp, 100, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //		if(PET_THUMB != null)
        //			FileUtile.saveImage(FileUtile.getPath(BaseEditImgActivity.this, Constants.FilePath)+"test3.png", PET_THUMB);
    }

    /**
     * 根据类型获取常用列表
     * @param type
     * @return
     */
    protected LinkedList<DecorationBean> getUsullyQueue(String type){
//       if(TextUtils.isEmpty(type))
//           return null;
//        if(mCurrUsuallyMap == null){
//            mCurrUsuallyMap = new HashMap<String, LinkedList<DecorationBean>>();
//        }
//
//        if(mCurrUsuallyMap.containsKey(type)){
//            return mCurrUsuallyMap.get(type);
//        }else {
//            List<DecorationTitleBean> titleBeans = mRootData.getChildren();
//            LinkedList<DecorationBean> list = null;
//           for (int i = 0;i<titleBeans.size();i++){
//               DecorationTitleBean bean =titleBeans.get(i);
//                if(type.equals(bean.getType())){
//                    List<DecorationBean> beans = bean.getDecorations();
//                    list = new LinkedList<DecorationBean>();
//                    for(int j=0;j<beans.size();j++){
//                        if(j <= MAXUSUALLYCOUNT){
//                            list.addLast(beans.get(j));
//                        }else {
//                            break;
//                        }
//                    }
//                    break;
//                }
//           }
//            if(list != null)
//                mCurrUsuallyMap.put(type,list);
//            return list;
//        }
        return mDataManager.getUsullyQueue(type);
    }

    /**
     * 设置常用饰品列表
     * @param beans
     */
    protected void setUsuallyDecorationList(List<DecorationBean> beans,String type){
            mDtUsuallyview.setUsuallyDecorationData(beans,type);
    }

    @Override
    public void onClickDecoratoiinGroupTitle(DecorationTitleBean bean) {
        setUsuallyDecorationList(getUsullyQueue(bean.getType()),bean.getType());
    }

    @Override
    public void onClickUsuallyDecoration(DecorationItemView view) {
        addDecoration(view);
    }

    @Override
    public void onClicClearView(String type){
        if(TYPE_RAHMEN.equals(type)){
            mRahmen = null;
            mEditView.setRahmenModule(null);
        }
    }

    @Override
    public void onClickDecorationMore(String type){
        if(mRootData != null)
            showMoreView(type);
    }

    protected void showMoreView(String type) {
        mMoreView.updateView(mDataManager.getDecorationGroupByType(type));
        mMoreView.onShow();
    }

    protected void addDecoration(DecorationItemView view){
        DecorationBean bean = view.getData();
        if(bean == null)
            return;
        verifyFile(bean);
        boolean flag = true;
        if(bean.isDownloaded() || bean.isAssetsed()){
            flag = onAddDecoration(bean);
        }else if(bean.isDownloading()) {
            setDonloadDecorateState(view,0);
        }else {
            downloadDecorate(view);
        }
        if(!flag && !bean.isDownloading()){
            downloadDecorate(view);
        }
        mDataManager.updateUsullayData(bean);
    }

    /**
     * 验证文件是否存在
     * @param data
     */
    protected void verifyFile(DecorationBean data){
        if(data.isAssetsed() || data.isDownloaded() || data.isDownloading())
            return;
        String fileName = FileUtile.getFileNameByUrl(data.getUrl());
        data.setAssetsed(FileUtile.assetsHasFile(BaseEditActivity.this, Constants.DECORATE + data.getFileType(), fileName));
        if(!data.isAssetsed()){
            String filePath = FileUtile.getPath(BaseEditActivity.this, Constants.SDCARD_DECORATE+data.getFileType());
            data.setDownloaded(FileUtile.sdCardHasFile(filePath, fileName));
        }
        if((data.isAssetsed() || data.isDownloaded()) && !mDataManager.checkFileDownload(data)){
            mDataManager.setFileDonloadState(data);
        }
    }

    /**
     * 设置ImageVIew的下载状态
     * @param view
     * @param state -1：下载失败 0:下载中  1：下载成功
     */
    private void setDonloadDecorateState(DecorationItemView view,int state){
        DecorationBean bean = view.getData();
        if(view == null)
            return;
        if(bean != null){
            bean.setDownloading(state == 0);
            bean.setDownloaded(state == 1);
            if(bean.isDownloaded()){
                mDataManager.setFileDonloadState(bean);
            }
        }
        switch (state) {
            case -1:	//下载失败
                view.setDownloadFaile();
                break;
            case 0:	//下载中
                view.showLoading(true);
                break;
            case 1:	//下载成功
                view.setDownloadSuccess();
                break;
        }
    }

    /**
     * 验证完毕后并且存在后执行添加饰品
     * @param bean
     */
    protected boolean onAddDecoration(DecorationBean bean) {
        String type = bean.getType();
        if(type.equals(TYPE_COSTUME)) {
            return onAddCostume(bean);
        }else if(type.equals(TYPE_DIALOG)){
            return onAddDialog(bean);
        }else if(type.equals(TYPE_FILTER)){
            return onAddFilter(bean);
        }else if(type.equals(TYPE_RAHMEN)){
            return onAddRahmen(bean);
        }
        return true;
    }

    protected void downloadDecorate(DecorationItemView v){
        DecorationBean bean = v.getData();
        String target = FileUtile.getPath(BaseEditActivity.this, Constants.SDCARD_DECORATE+bean.getFileType());
        startDowload(bean.getUrl(), target, v);
        setDonloadDecorateState(v,0);
    }

    private void startDowload(String url, String target, DecorationItemView view){
        DownloadTask task = new DownloadTask(view, target);
        task.setCallback(new DownloadTask.DownloadTaskCallback() {
            @Override
            public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess, String url,
                                                 File file, Object what) {
                DecorationItemView view = (DecorationItemView)what;
                onDownloadDecorationCallback(isSuccess,view);
            }
            @Override
            public void onCancelCallback(DownloadTask task,String url,Object what) {
            }
        });
        task.execute(url);
    }

    protected void onDownloadDecorationCallback(boolean isSuccess,DecorationItemView view){
        if(isSuccess){
            setDonloadDecorateState(view, 1);
        }else {
            setDonloadDecorateState(view, -1);
            PublicMethod.showToast(BaseEditActivity.this, "下载表情包失败！"+view.getData().getName());
        }
    }

    protected boolean onAddRahmen(DecorationBean bean){
//删除边框
        if(ID_DELETE.equals(bean.getId())){
            mRahmen = null;
            mEditView.setRahmenModule(null);
            return true;
        }
        boolean flag = true;
        if(mRahmen == null)
            flag = initRahmen(bean);
        else {
            Bitmap bmp = getDecorationBmp(bean);
            if(bmp == null)
                return false;
            mRahmen.changeView(bmp);
        }
        if(flag){
            int dw = PublicMethod.getDisplayWidth(this);
            mRahmen.setMaxHeight(dw);
            mRahmen.setMaxWidth(dw);
            Bitmap bitmap = mRahmen.getBitmap();
            Matrix mx = mRahmen.getMatrix();
            float sx =(dw)*100.0f/bitmap.getWidth()/100.0f;
            float sy =(dw)*100.0f/bitmap.getHeight()/100.0f;
            mx.setScale(sx, sy);
            mRahmen.setTag(bean);
            mEditView.refreshView();
        }
        return flag;
    }

    private boolean initRahmen(DecorationBean bean){
        Bitmap bmp = getDecorationBmp(bean);
        if(bmp == null)
            return false;
        mRahmen = new RahmenSurfaceModule(bmp);
        mEditView.setRahmenModule(mRahmen);
        return true;
    }

    protected boolean onAddFilter(DecorationBean bean){
        return true;
    }

    protected boolean onAddDialog(DecorationBean bean){
        Bitmap bmp = getDecorationBmp(bean);
        if(bmp == null){
            bean.setDownloaded(false);
            return false;
        }

        BasicSurfaceViewModule module = mEditView.getFocusModule();
        if(module != null && module instanceof DialogSurfaceViewModule){
            mDialogModule = (DialogSurfaceViewModule) module;
            mDialogModule.changeView(bmp);
            mEditView.refreshView();
        }else {
//			Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HYWaWaZhuanJ.TTF");
            if(mOkBmp == null)
                mOkBmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ok);
            DialogSurfaceViewModule dm = new DialogSurfaceViewModule(bmp, mControlBmp, mDeleteBmp,getString(R.string.edit_pet_dialog_tip),mOkBmp);
//			module.setTextFont(typeface);
//			mEditInput.setTypeface(typeface);
            dm.setTag(bean);
            dm.setCallback(this);
            mDialogModule = dm;
            onAddGeneralDecoration(bmp, dm);
        }
        return true;
    }

    protected boolean onAddCostume(DecorationBean bean){
        Bitmap bmp = getDecorationBmp(bean);
        if(bmp == null){
            bean.setDownloaded(false);
            return false;
        }
        //		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        //		params.height = mImgPetHeight;
        BasicSurfaceViewModule module = new EditSurfaceViewModule(bmp, mControlBmp, mDeleteBmp);
        module.setTag(bean);
        onAddGeneralDecoration(bmp, module);
        return true;
    }

    protected Bitmap getDecorationBmp(DecorationBean bean){
        String fileName = FileUtile.getFileNameByUrl(bean.getUrl());
        if(bean.isAssetsed()){
            InputStream is;
            try {
                is = getAssets().open(Constants.DECORATE+bean.getFileType()+File.separator+fileName);
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else {
            String path = FileUtile.getPath(BaseEditActivity.this, Constants.SDCARD_DECORATE+bean.getFileType()+File.separator+fileName);
            return BitmapFactory.decodeFile(path);
        }
    }

    /**
     * 添加一般的饰品(也就是允许存在多个同类型的饰品)
     * @param module
     */
    protected void onAddGeneralDecoration(Bitmap bmp,BasicSurfaceViewModule module){
        if(module == null)
            return;
        Matrix mx = module.getMatrix();
        int dw = PublicMethod.getDisplayWidth(this);
        module.setMaxHeight(dw);
        module.setMaxWidth(dw);
        float sx =bmp.getWidth()*100.0f/(dw/2)/100.0f;
        float sy = bmp.getHeight()*100.0f/(dw/2)/100.0f;
        if(sx > 1 || sy > 1){
            sx = (dw/2*100.0f)/bmp.getWidth()/100.0f;
            sy = (dw/2*100.0f)/bmp.getHeight()/100.0f;
        }
        mx.postScale(sx,sy);
        PublicMethod.setRandomPosition(dw, dw, dw/2,dw/2, mx);
        addSurfaceModule(module);
    }

    /**
     * 向FrameLayout中添加装饰
     * @param module
     */
    protected void addSurfaceModule(BasicSurfaceViewModule module){
        if(mEditView == null)
            return;
        if(module instanceof EditSurfaceViewModule){
            ((EditSurfaceViewModule) module).setInitMatrix(module.getMatrix());
        }
        mEditView.addModule(module);
    }

    @Override
    public void showSoftInput(DialogSurfaceViewModule module,
                              String preInputString) {
        mDialogModule = module;
        mEditInput.setText("");
        if(!TextUtils.isEmpty(mDialogModule.getInputString()) && !mDialogModule.getInputString().equals(getString(R.string.edit_pet_dialog_tip))){
            mEditInput.append(mDialogModule.getInputString());
        }
        onShowSoftInput();
    }

    private void onShowSoftInput(){
        mInputView.setVisibility(View.VISIBLE);
        mEditInput.setFocusable(true);
        PublicMethod.openSoftKeyBoard(mEditInput, 0);
    }

    @Override
    public void hidenSofInput(DialogSurfaceViewModule module) {
        onHidenSofInput();
    }


    private void onHidenSofInput() {
        mEditView.refreshView();
        mInputView.setVisibility(View.GONE);
        PublicMethod.closeSoftKeyBoard(BaseEditActivity.this, mEditInput);
    }

    private void onSubmitInput(){
        if(mDialogModule != null)
            mDialogModule.setInputString(mEditInput.getText().toString());
        onHidenSofInput();
        mEditView.refreshView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                onSubmitInput();
                break;
            case R.id.tv_cancel:
                onHidenSofInput();
                mEditView.refreshView();
                break;
        }
    }

    public EditSurfaceViewModule getFocusEditModule(){
        BasicSurfaceViewModule module = mEditView.getFocusModule();
        if(module == null)
            return null;
        else if(module instanceof EditSurfaceViewModule)
            return (EditSurfaceViewModule) module;
        return null;
    }

    @Override
    public void onEnlarged() {
        EditSurfaceViewModule module = getFocusEditModule();
        if(module != null){
            module.enlarged(0.001f);
            mEditView.refreshView();
        }
    }

    @Override
    public void onReduced() {
        EditSurfaceViewModule module = getFocusEditModule();
        if(module != null){
            module.reduced(0.001f);
            mEditView.refreshView();
        }
    }

    @Override
    public void onRotateleft() {
        EditSurfaceViewModule module = getFocusEditModule();
        if(module != null){
            module.rotateleft(0.1f);
            mEditView.refreshView();
        }
    }

    @Override
    public void onRotateright() {
        EditSurfaceViewModule module = getFocusEditModule();
        if(module != null){
            module.rotateright(0.1f);
            mEditView.refreshView();
        }
    }

    @Override
    public void onReset() {
        EditSurfaceViewModule module = getFocusEditModule();
        if(module != null){
            module.reset();
            mEditView.refreshView();
        }
    }

    @Override
    public void onClickDecorationItemView(DecorationItemView view) {
        addDecoration(view);
        if(view.getData() != null && (view.getData().isAssetsed() || view.getData().isDownloaded())){
            mMoreView.onHiden();
        }
    }

    @Override
    public void onTouchModule(BasicSurfaceViewModule module, MotionEvent event) {
    }
}
