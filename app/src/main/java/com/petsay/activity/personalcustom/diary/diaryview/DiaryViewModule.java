package com.petsay.activity.personalcustom.diary.diaryview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.application.UserManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/5
 * @Description
 */
public class DiaryViewModule extends View{//SurfaceView implements SurfaceHolder.Callback {

    private int mPageIndex = -2;
    private List<PetalkVo> mPetalks;

    private PetalkVo mTalkVo;
    private Bitmap mBg;
    private Rect mBgRect;
    private int mTotalPage;
    private int mTotalElements;
    private float mTemplatewidth = 1819.0f;
    private float mTemplateHeight = 2551.0f;
    private TextPaint mTxtPaint;
    private Bitmap mHeadBmp;
    /**-1:加载失败 0：未加载 1：加载中 2：加载成功*/
    private int mHeadStatus;
    private Bitmap mFirstContent;
    private int mFirstStatus;
    private Bitmap mSecondContent;
    private int mSecondStatus;

    private boolean misRefresh;
    private boolean mIsLocked;

    public DiaryViewModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DiaryViewModule(Context context) {
        super(context);
        initView();

    }

    private void initView() {
        setBackgroundColor(Color.WHITE);
        setWillNotDraw(false);
        mBgRect = new Rect(0,0,getWidth(),getHeight());
        mTxtPaint = new TextPaint();
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setColor(Color.parseColor("#717071"));
//        getHolder().addCallback(this);
    }

    @Deprecated
    public void setData(List<PetalkVo> vos){
        this.mPetalks = vos;
    }

    /**
     * 设置单页的总页数
     * @param totalPage
     */
    public void setTotalPage(int totalPage){
        this.mTotalPage = totalPage;
    }

    /**
     * 设置说说总数量
     * @param totalElements
     */
    public void setTotalElements(int totalElements){
        this.mTotalElements = totalElements;
    }

    public void setPageIndex(int pageIndex){
        mHeadStatus = mFirstStatus = mSecondStatus = 0;
        mIsLocked = misRefresh = false;
        this.mPageIndex = pageIndex;
        invalidate();
    }



    public void updateView(int pageIndex,List<PetalkVo> vos){
        mHeadStatus = mFirstStatus = mSecondStatus = 0;
        mIsLocked = misRefresh = false;
        if(pageIndex != mPageIndex) {
            release();
            this.mPageIndex = pageIndex;
            this.mPetalks = vos;
            invalidate();
        }
    }

    public void refresh(){
//        if(mIsLocked)
//            misRefresh = true;
//        else {
//            mis = false;
        PetsayLog.d("[refresh]");
//        setVisibility(INVISIBLE);
//        ((ViewGroup)getParent()).setWillNotDraw(false);
        invalidate();
//        setVisibility(VISIBLE);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        PetsayLog.d("[DiaryViewModule][onDraw]%s->start",mPageIndex);
        onDrawView(canvas);
        super.onDraw(canvas);
        PetsayLog.d("[DiaryViewModule][onDraw]%s->stop", mPageIndex);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        PetsayLog.d("[dispatchDraw]mPageIndex="+mPageIndex);
        super.dispatchDraw(canvas);
    }

    private void onDrawView(Canvas canvas) {
        if(canvas == null)
            return;
//        synchronized (getHolder()) {
//            Canvas canvas = getHolder().lockCanvas();
//            mIsLocked = true;
//            canvas.drawColor(Color.WHITE);//, PorterDuff.Mode.CLEAR);
//            onDrawCore(canvas);
//            getHolder().unlockCanvasAndPost(canvas);
            onDrawCore(canvas);
//            mIsLocked = false;
//            if(misRefresh) {
//                misRefresh = false;
//                invalidate();
//            }
//        }
    }

    private void onDrawCore(Canvas canvas) {
        if(mPageIndex < 7){
            onDrawTitlePage(canvas);
        }else if(mPageIndex < mTotalPage) {
            int chapter = (mPageIndex - 7) % 18;
            if (chapter < 2) {
                onDrawChapter(canvas, chapter);
            } else {
                onDrawPetalk(canvas, chapter);
            }
        }else {
            onDrawBookBottom(canvas);
        }
    }

    private void onDrawBookBottom(Canvas canvas) {
        mBg = getBmp("diary_finish");
        onDrawBg(canvas);
    }

    private void onDrawPetalk(Canvas canvas, int chapter) {
        int templateIndex = ((chapter-2)+(mPageIndex -7)/18*16)%3;
        int index = getSayIndex();
        int end = index;
        List<PetalkVo> vos = null;
        switch (templateIndex) {
            case 0:
                end = index+2;
                break;
            case 1:
                index += 2;
                end = index +1;
                break;
            case 2:
                index += 3;
                end = index +2;
                break;
        }
        vos = getSayVos(index, end);
        if(vos != null && !vos.isEmpty()){
            for (int i=0;i<vos.size();i++){
                PetalkVo vo = vos.get(i);
                if(!TextUtils.isEmpty(vo.getPetHeadPortrait()))
                    onDrawHead(canvas, templateIndex, i, vo.getPetHeadPortrait());
                if(!TextUtils.isEmpty(vo.getPhotoUrl()))
                 onDrawImgContent(canvas,templateIndex,i,vo.getPhotoUrl());
            }
            mBg = getBmp("diary_template_0" + templateIndex);
            onDrawBg(canvas);
            for (int i=0;i<vos.size();i++){
                PetalkVo vo = vos.get(i);
                onDrawPetName(canvas, templateIndex, i, vo.getPetNickName());
                onDrawCreateTime(canvas, templateIndex, i, vo.getCreateTime());
                onDrawDescription(canvas, templateIndex, i, vo.getDescription());
//                if(!TextUtils.isEmpty(vo.getPetHeadPortrait()))
//                    onDrawHead(canvas, templateIndex, i, vo.getPetHeadPortrait());
//                onDrawImgContent(canvas,templateIndex,i,vo.getPhotoUrl());
            }
        }else if(index >= mTotalElements) {
            onDrawBlank(canvas);
        }else {
            mBg = getBmp("diary_template_0" + templateIndex);
            onDrawBg(canvas);
        }
    }

    private void onDrawImgContent(Canvas canvas, int templateIndex, int i, final String photoUrl) {
        RectF rectF = null;
        switch (templateIndex) {
            case 0:
                if(i == 0){
                    rectF = getRange(177,816,688,688);
                }else {
                    rectF = getRange(1008,1188,688,688);
                }
                break;
            case 1:
                rectF = getRange(412,1099,1138,1138);
                break;
            case 2:
                if(i == 0){
                    rectF = getRange(1015,520,688,688);
                }else {
                    rectF = getRange(193,1434,688,688);
                }
                break;
        }

        if(i == 0){
            if(mFirstContent == null || mFirstStatus < 1){
                loadFirstBitmap(photoUrl);
        }else if(mFirstContent != null) {
            canvas.drawBitmap(mFirstContent,null,rectF,null);
        }
        }else {
            if(mSecondContent == null || mSecondStatus < 1){
                mSecondStatus = 1;
                ImageLoaderHelp.getImageLoader().loadImage(photoUrl,getOptions(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        mSecondStatus  = -1;
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        PetsayLog.d("[onLoadingComplete][2] position=%s",mPageIndex);
                        if(photoUrl.equals(s) && bitmap != null) {
                            mSecondStatus = 2;
                            mSecondContent = bitmap;
                            refresh();
                        }
                    }
                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                });
            }else if(mSecondContent != null) {
                canvas.drawBitmap(mSecondContent,null,rectF,null);
            }
        }

    }

    private void loadFirstBitmap(final String url){
            mFirstStatus = 1;
            ImageLoaderHelp.getImageLoader().loadImage(url,getOptions(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    mFirstStatus  = -1;
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    PetsayLog.d("[onLoadingComplete][1] position=%s",mPageIndex);
                    if(url.equals(s) && bitmap != null) {
                        mFirstStatus = 2;
                        mFirstContent = bitmap;
                        refresh();
                    }
                }
                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
    }

    private DisplayImageOptions getOptions(){
        return ImageLoaderOptionsManager.getDiaryOptions();
    }

    private void onDrawHead(Canvas canvas, final int templateIndex, int i, String petHeadPortrait) {
        RectF rectF = null;
        switch (templateIndex) {
            case 0:
                if(i == 0){
                    rectF = getRange(148,557,168,168);
                }else {
                    rectF = getRange(885,547,168,168);
                }
                break;
            case 1:
                rectF = getRange(262,484,168,168);
                break;
            case 2:
                if(i == 0){
                    rectF = getRange(275,530,168,168);
                }else {
                    rectF = getRange(1282,1447,168,168);
                }
                break;
        }

        if(mHeadBmp == null || mHeadStatus < 1){
            loadHeadBitmap(petHeadPortrait);
        }else if(mHeadBmp != null) {
//        mHeadBmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.menu_left_header_icon);
            canvas.drawBitmap(mHeadBmp,null,rectF,null);
        }

    }

    private void loadHeadBitmap(final String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        mHeadStatus = 1;
        ImageLoaderHelp.getImageLoader().loadImage(url, getOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                mHeadStatus = -1;
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                PetsayLog.d("[onLoadingComplete][head] position=%s", mPageIndex);
                if (url.equals(s) && bitmap != null) {
                    mHeadStatus = 2;
                    mHeadBmp = bitmap;
                    refresh();
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    private void onDrawDescription(Canvas canvas, int templateIndex, int i, String description) {
        RectF rectF = null;
        switch (templateIndex) {
            case 0:
                if(i == 0){
                    rectF = getRange(292,1922,538,375);
                }else {
                    rectF = getRange(1043,877,619,276);
                }
                break;
            case 1:
                rectF = getRange(419,839,800,241);
                break;
            case 2:
                if(i == 0){
                    rectF = getRange(523,865,462,413);
                }else {
                    rectF = getRange(917,1812,568,455);
                }
                break;
        }
        mTxtPaint.setTextSize(40/mTemplatewidth*getWidth());
//        mTxtPaint.setFakeBoldText(false);
//        canvas.drawText(description, rectF.left, rectF.centerY(), mTxtPaint);

        StaticLayout layout = getStaticLayout(description,(int)rectF.width());
        canvas.save();
        canvas.translate(rectF.left,rectF.top);
        layout.draw(canvas);
        canvas.restore();
    }

    private StaticLayout getStaticLayout(String mInputString,int mTxtRangeW){
//        int textSize = 60;
//        int mPreTxtW =0;
//        int mPreTxtH =0;
//        Rect bounds = new Rect();
//        int total = mInputString.length();
//        while(textSize > 5){
//            mTxtPaint.setTextSize(textSize);
//            //计算出单个文字所占的平均大小
////				mPreSizeAvg = (int)mTxtPaint.measureText(mInputString)/mInputString.length();
//            mTxtPaint.getTextBounds(mInputString, 0, mInputString.length(), bounds);
//            PublicMethod.log_d("w=" + bounds.width() + " | h = " + bounds.height());
//            mPreTxtW = bounds.width()/mInputString.length();
//            mPreTxtH = bounds.height();
//            //计算出一行可以放多少个字
//            int row = mTxtRangeW/mPreTxtW;
//            //计算出一列可以放多少个字
//            int line = mTxtRangeH/mPreTxtH - 1;
//            //计算总共可以放下多少字
//            total = line*row;
//            //如果当前字体大小正合身，就使用当前的字体大小
//            if(total >= mInputString.length()){
//                total = mInputString.length();
//                break;
//            }
//            textSize -= 5;
//        }
        return new StaticLayout(mInputString, 0, mInputString.length(), mTxtPaint, mTxtRangeW, Layout.Alignment.ALIGN_NORMAL, (float) 1.0,(float) 0.0, true, TextUtils.TruncateAt.MARQUEE, 0);
    }

    private void onDrawCreateTime(Canvas canvas, int templateIndex, int i, long createTime) {
        RectF rectF = null;
        switch (templateIndex) {
            case 0:
                if(i == 0){
                    rectF = getRange(340,668,226,23);
                }else {
                    rectF = getRange(1081,667,226,23);
                }
                break;
            case 1:
                rectF = getRange(475,598,226,23);
                break;
            case 2:
                if(i == 0){
                    rectF = getRange(468,640,226,23);
                }else {
                    rectF = getRange(1035,1563,226,23);
                }
                break;
        }

        mTxtPaint.setTextSize(24.5f / mTemplatewidth * getWidth());
        mTxtPaint.setFakeBoldText(false);
        String time = DateFormat.format("yyyy-MM-dd HH:mm:kk", createTime).toString();
        if(templateIndex == 2 && i == 1){
            float width=mTxtPaint.measureText(time);
            canvas.drawText(time,rectF.right-width,rectF.centerY(),mTxtPaint);
        }else {
            canvas.drawText(time, rectF.left, rectF.centerY(), mTxtPaint);
        }
    }

    private void onDrawPetName(Canvas canvas, int templateIndex, int i, String petNickName) {
        float x = 0;
        float y = 0;
        int w = 0;
        int h = 0;
        int dw = getWidth();
        int dh = getHeight();
//        float sw = getWidth()/1819.0f;
//        float sh = getHeight()/2551.0f;
        switch (templateIndex) {
            case 0:
                if(i == 0){
                    x = 343;
                    y = 614;
                    w = 536;
                    h = 40;
                }else {
                    x = 1081;
                    y = 613;
                    w  = 614;
                    h = 40;
                }
                break;
            case 1:
                x = 475;
                y = 542;
                w = 1051;
                h = 40;
                break;
            case 2:
                if(i == 0){
                    x = 469;
                    y = 588;
                    w = 524;
                    h = 40;
                }else {
                    x = 885;
                    y = 1509;
                    w  = 373;
                    h = 40;
                }
                break;
        }
//        x = x/mTemplatewidth*dw;
//        y = y/mTemplateHeight*dh;
////        w = (int) (w/mTemplatewidth*dw);
//        h = (int) (h/mTemplateHeight*dh);
        RectF rectF = getRange(x,y,w,h);

        mTxtPaint.setTextSize(60/mTemplatewidth*getWidth());
        mTxtPaint.setFakeBoldText(true);
        if(templateIndex == 2 && i == 1){
            float width=mTxtPaint.measureText(petNickName);
            canvas.drawText(petNickName,rectF.right-width,rectF.centerY(),mTxtPaint);
        }else {
            canvas.drawText(petNickName, rectF.left, rectF.centerY(), mTxtPaint);
        }
    }

    private RectF getRange(float x, float y, float w, float h){
        float left = x/mTemplatewidth*getWidth();
        float top = y/mTemplateHeight*getHeight();
        float right = w/mTemplatewidth*getWidth() + left;
        float bottom = h/mTemplateHeight*getHeight() + top;
        return new RectF(left,top,right,bottom);
    }

    private List<PetalkVo> getSayVos(int startIndex,int end){
        if(startIndex >= mPetalks.size())
            return null;
        try {
            if(end > mPetalks.size())
                end = mPetalks.size();
            return mPetalks.subList(startIndex, end);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void onDrawChapter(Canvas canvas, int chapter) {
        int chapterCount = getChapterCount();
        if(chapter == 0)
            mBg = getBmp("section_text_" + chapterCount);
        else
            mBg = getBmp("section_NO_"+chapterCount);
        onDrawBg(canvas);
    }

    private void onDrawTitlePage(Canvas canvas) {
        if(mPageIndex == - 1) {
            mBg = getBmp("diary_description");
//            mBg = getBmp("diary_finish");
            onDrawBg(canvas);
        }else if(mPageIndex == 0){
            mBg = getBmp("homepage");
            onDrawBg(canvas);
        }else if (mPageIndex == 4){
            onDrawPetInfo(canvas);

        }else {
            mBg = getBmp("titlepage_0"+ mPageIndex);
            onDrawBg(canvas);
        }
    }

    private void onDrawPetInfo(Canvas canvas) {
        PetVo vo =UserManager.getSingleton().getActivePetInfo();
        RectF rectF = null;
        if(mHeadBmp != null){
            rectF = getRange(376,504,202,204);
            canvas.drawBitmap(mHeadBmp,null,rectF,null);
        }else if(mHeadStatus < 1){
            String url = vo.getHeadPortrait();
            loadHeadBitmap(url);
        }
        if(mFirstContent != null){
            rectF = getRange(517,715,981,982);
            canvas.drawBitmap(mFirstContent,null,rectF,null);
        }else if(mFirstStatus < 1 && mPetalks != null && !mPetalks.isEmpty()) {
                PetalkVo talk = mPetalks.get(0);
                loadFirstBitmap(talk.getPhotoUrl());
        }
        mBg = getBmp("titlepage_0"+ mPageIndex);
        onDrawBg(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(50 / mTemplatewidth * getWidth());
        paint.setColor(Color.parseColor("#888889"));
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/huakangwawati.ttc"));
        rectF = getRange(674,1753,816,41);
        canvas.drawText(vo.getNickName(), rectF.left, rectF.bottom, paint);
        rectF = getRange(673,1838,238,41);
        canvas.drawText(vo.getGender() == 0 ? "女" : "男",rectF.left,rectF.bottom,paint);
        rectF = getRange(673,1925,818,38);
        canvas.drawText(DateFormat.format("yyyy-MM-dd",vo.getBirthday()).toString(),rectF.left,rectF.bottom,paint);
//        rectF = getRange(774,2011,718,37);
//        canvas.drawText(DateFormat.format("yyyy.MM.dd",vo.getCreateTime()).toString(),rectF.left,rectF.top,mTxtPaint);
//        rectF = getRange(773,2092,718,44);
//        canvas.drawText(vo.getAddress(), rectF.left, rectF.top, mTxtPaint);
    }


    private void onDrawBlank(Canvas canvas){
        mBg = getBmp("diary_blank");
        onDrawBg(canvas);
    }

    private void onDrawBg(Canvas canvas) {
        if(mBg != null){
            mBgRect.set(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(mBg, null, mBgRect, mTxtPaint);
        }
    }

    /**
     * 获取当前第几章
     * @return
     */
    private int getChapterCount(){
        return (mPageIndex - 7) / 18 + 1;
    }

    /**
     * 获取章内第几页
     * @return
     */
    private int getPageCount(){
        return (mPageIndex - 7) % 18 - 2;
    }

    /**
     * 获取说说列表下标
     * @return
     */
    private int getSayIndex(){
        return (getPageCount() + (mPageIndex - 7)/18*16)/3*5;
    }

    private Bitmap getBmp(String name){
        return FileUtile.loadImageByAssets(Bitmap.Config.RGB_565, 99, getWidth(), getHeight(), getContext().getAssets(), "diary/" + name + ".png");
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        onDrawCore();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }

    public void release(){
        if(mBg != null){
            mBg.recycle();
            mBg = null;
        }
        if(mFirstContent != null){
            mFirstContent.recycle();
            mFirstContent = null;
        }

        if(mSecondContent != null){
            mSecondContent.recycle();
            mSecondContent = null;
        }
    }
}
