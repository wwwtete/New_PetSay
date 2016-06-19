package com.petsay.activity.petalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.story.StoryPreviewActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.activity.petalk.adapter.HotDetailsAdapter;
import com.petsay.component.face.FaceConversionUtil;
import com.petsay.component.face.FaceRelativeLayout;
import com.petsay.component.face.ResizeLayout;
import com.petsay.component.gifview.AudioGifView;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.gifview.ImageLoaderListener;
import com.petsay.component.media.MediaPlayManager;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.ExCircleView;
import com.petsay.component.view.ExProgressBar;
import com.petsay.component.view.HotDetailsListTtile;
import com.petsay.component.view.HotDetailsListTtile.TitleChangeListener;
import com.petsay.component.view.ListTabScrollView;
import com.petsay.component.view.PullToRefreshView;
import com.petsay.component.view.PullToRefreshView.OnFooterRefreshListener;
import com.petsay.component.view.PullToRefreshView.OnHeaderRefreshListener;
import com.petsay.component.view.TagView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.VolumeView;
import com.petsay.component.view.functionbar.FunctionBarView;
import com.petsay.component.view.functionbar.StepAnimView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.download.DownloadQueue;
import com.petsay.network.net.SayDataNet;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.network.net.UserNet;
import com.petsay.network.upload.UploadTools;
import com.petsay.network.upload.UploadTools.UploadServiceListener;
import com.petsay.application.UserManager;
import com.petsay.utile.ActivityTurnToManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.NetworkManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.CommentVo;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.Share.ShareCallback;
import cn.sharesdk.onekeyshare.SharePopupWindow;
import roboguice.inject.InjectView;

/**
 * @author wangw
 * 宠物详情界面
 */
public class DetailActivity extends BaseActivity implements OnClickListener,com.petsay.component.view.ListTabScrollView.OnScrollListener, IAddShowLocationViewService,ShareCallback, NetCallbackInterface{

    @InjectView(R.id.titlebar)
    private TitleBar mTitleBar;
    @InjectView(R.id.lv_details)
    private ListView mListView;
    @InjectView(R.id.functionbar)
    private FunctionBarView mFunctionBarView;
    @InjectView(R.id.hotdetail_listtitle)
    private HotDetailsListTtile mListTtile;
    @InjectView(R.id.pulltorefreshview)
    private PullToRefreshView mPullToRefreshView;
    @InjectView(R.id.iv_flag)
    private ImageView mIvFlag;
    private HotDetailsAdapter mReviewAdapter;
    private int mReviewSelection=0;
    private HotDetailsAdapter mTreadAdapter;
    private int mTreadSelection=0;
    private int mTabIndex=0;
    private  RelativeLayout mLayoutInfo;

    //	private View mHeadLayout;
    private ImageView mIvContent;
    private TextView mTvTitle;
    private TextView mTVAddress;
    //	private CircleImageView mIvHeader;
    //	private AttentionButtonView mTvAtt;
    private ExCircleView mHeadView;
    private TextView mTvName,mTvAge;
    private TextView mTvGrade;
    private ImageView imgSex,imgGrade;
    private TextView mTvDate;
    private ResizeLayout layoutRoot;
    private EditText mEdComment;
    private RelativeLayout mLayoutReply;
    private Button mBtnReply;
    private RelativeLayout layoutInput;
    private LinearLayout layoutRecord;
    private Button btnRecord;
    private ImageButton btnKeyBoard;
    private FaceRelativeLayout faceRelativeLayout;
    private TagView tagView;
    private PopupWindow mCancelMenu;
    private VolumeView mVolumeView;
    //辅助layout 用来固定listview的显示
    //	private LinearLayout layoutAssist;
    /**
     * 发送按钮是否为录音模式： true发语音  false发送
     */
    private boolean isRecord=true;

    /**
     * 操作类型1、转发，2、评论
     */
    private int mOperationType=0;
    private PetalkVo mSayVo;
//    private SayModule mSayModule;
//    private SayData mSayData;
//    private UserData mUserData;
//    private UserModule mUserModule;
    private SayDataNet mSayDataNet;
    private UserNet mUserNet;
    private AudioGifView mGifView;
    //	private ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean isRefreshTread=false;
    /**
     * 跳转来源 0说说列表  1 消息(只有说说id)
     */
    private int from=0;
    public static final int FROM_MESSAGE=1;
    public static final int FROM_PUSH=2;
    public static final int FROM_LIST=0;

    private final int  UPLOAD_SUCCESS=999;
    private final int  UPLOAD_FAILED=998;
    private boolean isRefreshInfo=true;

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
           
            switch (msg.what) {
                case FaceRelativeLayout.MSG_RESIZE:
                    if (msg.arg1 == FaceRelativeLayout.BIGGER) {
                        faceRelativeLayout.curTat = FaceRelativeLayout.BIGGER;
                        faceRelativeLayout.setFaceVisible(true);
                    } else {
                        faceRelativeLayout.curTat = FaceRelativeLayout.SMALLER;
                        faceRelativeLayout.setFaceVisible(false);
                    }
                    break;
                case UPLOAD_SUCCESS:
                    if (UserManager.getSingleton().isLoginStatus()) {
                        if (isRecord) {
                            layoutRecord.setVisibility(View.VISIBLE);
                            layoutInput.setVisibility(View.GONE);
                            String type;
                            if (mOperationType==1) {
                                type=Constants.RELAY;
                            }else {
                                type=Constants.COMMENT;
                            }
                            if (isCommentOtherUser) {
                                mSayDataNet.interactionCreate(mSayVo.getPetalkId(),type,UserManager.getSingleton().getActivePetInfo().getId(),commentPetId,"", msg.obj.toString(),recordSeconds );
                            }else {
                            	mSayDataNet.interactionCreate(mSayVo.getPetalkId(),type,UserManager.getSingleton().getActivePetInfo().getId(), mSayVo.getPetId(),"", msg.obj.toString(),recordSeconds );
                            }
                            int a=0;

                        }else{
                            String content=mEdComment.getText().toString();
                            String type;
                            if (mOperationType==1) {
                                type=Constants.RELAY;
                            }else {
                                type=Constants.COMMENT;
                            }
                            mSayDataNet.interactionCreate(mSayVo.getPetalkId(),type,UserManager.getSingleton().getActivePetInfo().getId(), mSayVo.getPetId(),"", msg.obj.toString(),recordSeconds );
                        }

                    }else {
                        Intent intent=new Intent(getApplicationContext(), UserLogin_Activity.class);
                        startActivity(intent);
                    }
                    break;
                //			case UPLOAD_FAILED:
                //				break;
                //			case GET_TOKEN_SUCCESS:
                //				UploadTools uploadService=new UploadTools();
                //				uploadService.setUploadListener(new UploadServiceListener() {
                //
                //					@Override
                //					public void onUploadFinishCallback(boolean isSuccess,String path, String hash,Object tag) {
                //						if (isSuccess) {
                //							String audioUrl=Constants.DOWNLOAD_SERVER+path;
                //							Message message=new Message();
                //							message.obj=audioUrl;
                //							message.what=UPLOAD_SUCCESS;
                //							mHandler.sendMessage(message);
                //						}else {
                //							mHandler.sendEmptyMessage(UPLOAD_FAILED);
                //						}
                //					}
                //
                //					@Override
                //					public void onProcess(long current, long total, Object tag) {
                //
                //					}
                //				});
                //				File file=new File(msg.obj.toString());
                //				if(file != null && file.exists()){
                //					String path = PublicMethod.getServerUploadPath(Constants.COMMENT_AUDIO)+file.getName();
                //					uploadService.doUpload(file,path,"");
                //				}else {
                //				}
                //				break;
            }

            closeLoading();
        };
    };

    protected void onStop() {
        super.onStop();
        removeSuspend();
        DownloadQueue.getInstance().cancelDownload(this);
    };



    private ListTabScrollView listTabScrollView;
    private LinearLayout mTabLayout;
    private WindowManager mWindowManager;
//    /**
//     * 手机屏幕宽度
//     */
//    private int screenWidth;
    /**
     * 悬浮框View
     */
    private static View suspendView;
    /**
     * 悬浮框的参数
     */
    private static WindowManager.LayoutParams suspendLayoutParams;
    /**
     * tab布局的高度
     */
    private int tabLayoutHeight;
    /**
     * myScrollView与其父类布局的顶部距离
     */
    private int myScrollViewTop;

    /**
     * tab布局与其父类布局的顶部距离
     */
    private int tabLayoutTop;

    private int mLayoutWidth;
    private StepAnimView mStepAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSayVo=Constants.Detail_Sayvo;
        mLayoutWidth= PublicMethod.getDisplayWidth(DetailActivity.this) - PublicMethod.getDiptopx(DetailActivity.this, 10);
        mOperationType=getIntent().getIntExtra("operationType", 0);
        setContentView(R.layout.details_layout);
        initView();
        setListener();
        from=getIntent().getIntExtra("from", FROM_LIST);
        mUserNet=new UserNet();
        mUserNet.setCallback(this);
        mUserNet.setTag(this);
        mSayDataNet=new SayDataNet();
        mSayDataNet.setCallback(this);
        mSayDataNet.setTag(this);
        //		if (from==FROM_MESSAGE||from==FROM_PUSH) {
        if (UserManager.getSingleton().isLoginStatus()) {
            mSayDataNet.petalkOne( mSayVo.getPetalkId(),UserManager.getSingleton().getActivePetId());
        }else {
        	mSayDataNet.petalkOne(mSayVo.getPetalkId(),"");
        }
        //		}else {
        //			showLoading();
        //			initData();
        //			onRefresh();
        //		}
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            tabLayoutHeight = mTabLayout.getHeight();
            tabLayoutTop = mTabLayout.getTop();
            //			layoutAssist=mFunctionBarView.get
            //int h=PublicMethod.getDisplayHeight(getApplicationContext())-mTitleBar.getHeight()-mFunctionBarView.getHeight();
            //			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, h);

            //			View view=new View(getApplicationContext());
            //			view.setLayoutParams(params);
            //			layoutAssist.addView(view);
            myScrollViewTop = listTabScrollView.getTop();

			/*评论或转发进入该activity时调用 */
            if (mOperationType==1||mOperationType==2) {
                listTabScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        listTabScrollView.scrollTo(0, tabLayoutTop);
                    }
                });
            }
        }
    }

    private void setListener() {
        mFunctionBarView.initListener(this);
        layoutRoot.setOnClickListener(this);
        mBtnReply.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        btnKeyBoard.setOnClickListener(this);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                switch (mTabIndex) {
                    case 0:
                        if (UserManager.getSingleton().isLoginStatus()) {
                            CommentVo commentVo= (CommentVo) mReviewAdapter.getItem(position);
                            if (null!=commentVo) {
                                //							mListView.setSelection(position);
                                commentPetId=commentVo.getPetId();
                                commentPetNickName=commentVo.getPetNickName();
                                if (commentVo.getPetId().equals(UserManager.getSingleton().getActivePetId())) {
                                    commentOtherUser(false);
                                }else {
                                    commentOtherUser(true);
                                }
                            }
                        }else {
                            Intent intent=new Intent(DetailActivity.this,UserLogin_Activity.class);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        CommentVo commentVo= (CommentVo) mTreadAdapter.getItem(position);
                        if (null!=commentVo) {
                            PetVo pet=new PetVo();
                            pet.setId(commentVo.getPetId());
                            pet.setNickName(commentVo.getPetNickName());
                            pet.setHeadPortrait(commentVo.getPetHeadPortrait());
                            ActivityTurnToManager.getSingleton().userHeaderGoto(DetailActivity.this, pet);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        //		mTvAtt.setOnClickListener(new OnClickListener() {
        //
        //			@Override
        //			public void onClick(View v) {
        ////				if (UserManager.getSingleton().isLoginStatus()) {
        ////					mTvAtt.setClickable(false);
        ////					showLoading();
        ////					if (mSayVo.getRs()==0) {
        ////						mUserModule.addListener(mUserData);
        ////						mUserModule.focus(mUserData,mSayVo.getPetInfo().getId(),UserManager.getSingleton().getActivePetInfo().getId());
        ////					}else if (mSayVo.getRs()==1||mSayVo.getRs()==2) {
        ////						//TODO 取消关注
        ////						mUserModule.addListener(mUserData);
        ////						mUserModule.cancleFocus(mUserData,mSayVo.getPetInfo().getId(),UserManager.getSingleton().getActivePetInfo().getId());
        ////					}
        ////				}else {
        ////					Intent intent=new Intent(getApplicationContext(), UserLogin_Activity.class);
        ////					startActivity(intent);
        ////				}
        //
        //
        //				if (UserManager.getSingleton().isLoginStatus()){
        //					mTvAtt.setClickable(false);
        //					if (UserManager.getSingleton().focusMap.containsKey(UserManager.getSingleton().getActivePetInfo().getId())) {
        //						mTvAtt.startAnim();
        //					}else {
        //						mUserModule.addListener(mUserData);
        //						mUserModule.focus(mUserData,mSayVo.getPetInfo().getId(),UserManager.getSingleton().getActivePetInfo().getId());
        //					}
        //					// if (sayVo.getRs()==0) {
        //
        //					// }
        //					// else if (sayVo.getRs()==1||sayVo.getRs()==2) {
        //					// //TODO 取消关注
        //					// mUserModule.addListener(mUserData);
        //					// mUserModule.cancleFocus(mUserData,petInfo.getId(),mUserManager.getActivePetInfo().getId());
        //					// }
        //				} else {
        //					Intent intent=new Intent(getApplicationContext(), UserLogin_Activity.class);
        //					startActivity(intent);
        //				}
        //			}
        //		});

        btnRecord.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getY();
                int h = v.getHeight();
                if (y >= -h && y <= (h * 2)) {
                    if (!isSendRecord) {
                    }
                    isSendRecord = true;
                } else {
                    if (isSendRecord) {
                    }
                    isSendRecord = false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 正在录音时,不可点击
                        if (!recordRunning) {
                            recordRunning = true;
                            MediaPlayManager.getSingleton().stopAudio();
                            mGifView.stopGif();
                            startRecord();
                            //							dislogView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (recordRunning) {
                            stopRecord();
                            //							dislogView.setVisibility(View.GONE);//TODO:LIBO
                        }
                        break;
                    default:
                        break;
                }
                return true ;
            }
        });


        layoutRoot.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = FaceRelativeLayout.BIGGER;
                if (h < oldh) {
                    change = FaceRelativeLayout.SMALLER;
                }
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                mHandler.sendMessage(msg);
            }
        });

        mEdComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                setInputStatus(s.toString());

            }
        });
        mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                onRefresh();
            }
        });
        mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                onAddMore();
            }
        });

        mPullToRefreshView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFunctionBarView.getVisibility()!=View.VISIBLE) {
                    mLayoutReply.setVisibility(View.GONE);
                    mFunctionBarView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    protected void initView() {
        super.initView();
        mTitleBar.setTitleText(R.string.hotdetails);
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.more);
        imageView.setAdjustViewBounds(true);
        mTitleBar.addRightView(imageView);
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showCustomMenu();
            }
        });

        layoutRoot=(ResizeLayout) findViewById(R.id.layout_root);
        faceRelativeLayout=(FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
        mLayoutReply=(RelativeLayout) findViewById(R.id.layout_reply);
        mEdComment=(EditText) findViewById(R.id.et_sendmessage);
        mBtnReply=(Button) findViewById(R.id.btn_send);
        layoutInput=(RelativeLayout) findViewById(R.id.rl_input);
        layoutRecord=(LinearLayout) findViewById(R.id.layout_record);
        btnRecord=(Button) findViewById(R.id.btn_record);
        btnKeyBoard=(ImageButton) findViewById(R.id.btn_keyboard);
        mVolumeView = (VolumeView) findViewById(R.id.img_volume);
        if (mOperationType==1||mOperationType==2) {
            if (mOperationType==1) {
                mEdComment.setHint("转发");
                isRecord = false;
                mBtnReply.setText(R.string.comment_reply);
                mBtnReply.setBackgroundColor(Color.TRANSPARENT);
            }else {
                isRecord=true;
                mBtnReply.setBackgroundResource(R.drawable.comment_luyin);
                mBtnReply.setText("");
                mEdComment.setHint("评论");
            }
            mLayoutReply.setVisibility(View.VISIBLE);
            mFunctionBarView.setVisibility(View.INVISIBLE);
            PublicMethod.openSoftKeyBoard(mEdComment, 0);
            if (null!=mReviewAdapter&&mReviewAdapter.getCount()>1) {
                mReviewSelection=1;
            }
        }else {
            //			PublicMethod.showSoftKeyBoard(mEdComment, 0);
            mLayoutReply.setVisibility(View.GONE);
            mFunctionBarView.setVisibility(View.VISIBLE);
        }
        mTitleBar.setFinishEnable(true);
        initHeadView();
        mListView.addFooterView(getNullListFooter());

        mListTtile.setOnTitleChangeListener(mTitleChangeListener);

        listTabScrollView=(ListTabScrollView) findViewById(R.id.listTabScrollView);
        mTabLayout = (LinearLayout) findViewById(R.id.buy);

        PublicMethod.setListViewHeightBasedOnChildren(mListView);
        listTabScrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        View view = findViewById(R.id.layout_content);
        PublicMethod.initPetalkViewLayout(view,screenWidth);


    }

    private void initData(){
        int width = PublicMethod.getDisplayWidth(DetailActivity.this);
        initVolumeView();
        initImageContent(width);
        mIvContent.setOnClickListener(this);
        if(mSayVo.isAudioModel())
            initGifView(width);
        else if(mSayVo.isStoryModel()){
            initStoryView(width);
        }else {
            ImageLoaderHelp.displayContentImage(mSayVo.getPhotoUrl(), mIvContent);
        }

        if(mSayVo.getModel() == 0){
            mIvFlag.setImageResource(R.drawable.audio_flag_icon);
        }else if(mSayVo.getModel() == 2){
            mIvFlag.setImageResource(R.drawable.story_flag_icon);
        }else {
            mIvFlag.setImageResource(R.drawable.image_flag_icon);
        }
        mFunctionBarView.setValue(mSayVo.getCounter().getRelay(), mSayVo.getCounter().getComment(), mSayVo.getCounter().getFavour(), mSayVo.getCounter().getShare());
        mFunctionBarView.setStepStatus(mSayVo.getPetalkId());

        mListTtile.setReviewCount(mSayVo.getCounter().getRelay(), mSayVo.getCounter().getComment());
        mListTtile.setTreadCount(mSayVo.getCounter().getFavour());
        mListView.setAdapter(mReviewAdapter);

        tagView.removeAllViews();
        tagView.setTags(mSayVo.getTags(),TagView.getTagBgResId(DetailActivity.this),tagView.Use_SayList,mLayoutWidth);


        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(getApplicationContext(),mSayVo.getDescription());
        mTvTitle.setText(spannableString);
        mTvName.setText(mSayVo.getPetNickName());
        //		PicassoUtile.loadHeadImg(DetailActivity.this, mSayVo.getPetInfo().getHeadPortrait(), mIvHeader);
//		ImageLoaderHelp.displayHeaderImage(mSayVo.getPetHeadPortrait(), mIvHeader);
        mHeadView.setBackgroudImage(mSayVo.getPetHeadPortrait());
        mHeadView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityTurnToManager.getSingleton().userHeaderGoto(DetailActivity.this, mSayVo.getPet());
            }
        });
        mTvDate.setText(PublicMethod.calculateTime(mSayVo.getCreateTime()));
        if (mSayVo.getPet().getGender()==0) {
            imgSex.setVisibility(View.VISIBLE);
            imgSex.setImageResource(R.drawable.female);

        }else if (mSayVo.getPet().getGender()==1) {
            imgSex.setVisibility(View.VISIBLE);
            imgSex.setImageResource(R.drawable.male);
        }else {
            imgSex.setVisibility(View.GONE);
        }
//		mTvAge.setText(Constants.petMap.get(mSayVo.getPet().getType())+" | "+mSayVo.getPet().getAge(false)+" | ");
//		mTvGrade.setText("Lv"+mSayVo.getPet().getIntGrade());
//		if (-1==mSayVo.getPet().getLevenIconResId()) {
//			imgGrade.setImageDrawable(null);
//		}else {
//			imgGrade.setImageResource(mSayVo.getPet().getLevenIconResId());
//		}
        PetVo petVo=mSayVo.getPet();
        if (petVo.getIntGrade()<=0) {
            mTvAge.setText(Constants.petMap.get(petVo.getType())+"｜"+petVo.getAge(false));
            imgGrade.setVisibility(View.GONE);
            mTvGrade.setVisibility(View.GONE);
        }else {
            mTvAge.setText(Constants.petMap.get(petVo.getType())+"｜"+petVo.getAge(false)+"｜");
            mTvGrade.setVisibility(View.VISIBLE);
            if (-1==petVo.getLevenIconResId()) {
//				holder.imgGrade.setImageDrawable(null);
                imgGrade.setVisibility(View.GONE);
            }else {
                imgGrade.setVisibility(View.VISIBLE);
                imgGrade.setImageResource(petVo.getLevenIconResId());
            }
            mTvGrade.setText("Lv"+petVo.getIntGrade());
        }
        if(!TextUtils.isEmpty(petVo.getStar()) && "1".equals(petVo.getStar())){
            mHeadView.setBottomRightImage(R.drawable.star);
        }
    }

    private void initStoryView(int width) {
        findViewById(R.id.img_play).setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_storytitle);
        tvTitle.setText(mSayVo.getStoryTitle());
        tvTitle.setTextSize(18);
        tvTitle.setVisibility(View.VISIBLE);
        TextView tvTime = (TextView) findViewById(R.id.tv_storytime);
        tvTime.setText(mSayVo.getStoryTime());
        tvTime.setTextSize(14);
        tvTime.setVisibility(View.VISIBLE);
        ImageLoaderHelp.displayContentImage(mSayVo.getPhotoUrl(), mIvContent);
        PublicMethod.initStoryCoverViewLayout(tvTitle, tvTime, mIvContent, width);

    }

    @SuppressLint("NewApi")
    private void initGifView(int width){
        //初始化Gif的Item的布局
        LayoutParams params = (LayoutParams) mGifView.getLayoutParams();
        PetalkDecorationVo ad = mSayVo.getDecorations()[0];
        mGifView.initData(mSayVo, width, width);
        PublicMethod.updateGifItemLayout(width, width, ad, mGifView, params);
        View playView = findViewById(R.id.img_play);
        android.widget.RelativeLayout.LayoutParams playViewParams = (android.widget.RelativeLayout.LayoutParams) playView.getLayoutParams();
        playViewParams.topMargin = (width - PublicMethod.getDiptopx(this, 100))/2 + PublicMethod.getDiptopx(this, 70);
        playView.setLayoutParams(playViewParams);
        mGifView.setPlayBtnView(playView);
        ProgressBar playBar = (ProgressBar) findViewById(R.id.playprogressbar);
        mGifView.setPlayProgressBar(playBar);
        boolean flag = GifViewManager.getInstance().getAllowAutoPlay();
        mGifView.setPlayBtnVisibility(!flag);
        if(flag)
            GifViewManager.getInstance().playGif(mGifView);
        else {
            playView.setVisibility(View.VISIBLE);
        }
    }

    private void initVolumeView(){
        android.widget.RelativeLayout.LayoutParams volumeParams = (android.widget.RelativeLayout.LayoutParams) mVolumeView.getLayoutParams();
        int height = PublicMethod.getDisplayHeight(this);
        volumeParams.topMargin = (height - PublicMethod.getDiptopx(this, 110))/2;// + PublicMethod.getDiptopx(this, 70);;
        mVolumeView.setLayoutParams(volumeParams);
    }

    private void initImageContent(int width){
        ExProgressBar progressBar = (ExProgressBar)findViewById(R.id.pro_loaderpro);
        PublicMethod.initPetalkViewLayout(mIvContent, width);
        ImageLoaderListener listener = new ImageLoaderListener(progressBar);
        mGifView.setImageLoaderListener(listener);
        ImageLoaderHelp.displayContentImage(mSayVo.getPhotoUrl(), mIvContent,listener,listener);
    }

    @SuppressLint("NewApi")
    private void initHeadView() {
        mLayoutInfo=(RelativeLayout) findViewById(R.id.rlayout_info);
        mIvContent = (ImageView)findViewById( R.id.img_pet );
        mTvTitle = (TextView)findViewById( R.id.tv_title );
        mTVAddress = (TextView)findViewById( R.id.tv_address );
        mHeadView = (ExCircleView) findViewById(R.id.headview);
//		mIvHeader = (CircleImageView)findViewById( R.id.img_header );
        //		mTvAtt = (AttentionButtonView)findViewById( R.id.btn_attention );
        mTvName = (TextView)findViewById( R.id.tv_name );
        mTvAge=(TextView) findViewById(R.id.tv_age);
        mTvGrade=(TextView) findViewById(R.id.tv_grade);
        imgGrade=(ImageView) findViewById(R.id.img_grade);
        imgSex=(ImageView) findViewById(R.id.img_sex);
        mTvDate = (TextView)findViewById( R.id.tv_date );
        mGifView = (AudioGifView) findViewById(R.id.am_gif);
        tagView=(TagView) findViewById(R.id.tagview);
        mStepAnimView = (StepAnimView) findViewById(R.id.stepanim);
        mFunctionBarView.setStepAnimView(mStepAnimView);
    }


    private TextView getNullListFooter(){
        TextView txt = new TextView(this);
        txt.setHeight(PublicMethod.getDiptopx(this, 70));
        return txt;
    }

    TitleChangeListener mTitleChangeListener = new TitleChangeListener() {

        @Override
        public void onTitleChangeCallback(int position) {
            if(position == 0 && mReviewAdapter != null){
                mTabIndex=0;
                //				mListView.setAdapter(mReviewAdapter);
                mListView.setAdapter(mReviewAdapter);
                PublicMethod.setListViewHeightBasedOnChildren(mListView);
                if(mReviewAdapter.getCount() == 0)
                    onRefresh();
            }else if(mTreadAdapter != null) {
                mTabIndex=1;
                mListView.setAdapter(mTreadAdapter);
                PublicMethod.setListViewHeightBasedOnChildren(mListView);
                if(mTreadAdapter.getCount() == 0||isRefreshTread){
                    isRefreshTread=false;
                    onRefresh();
                }
            }
            mListTtile.setUnderLinePosition(position);
        }
    };

    private void onRefresh() {
        mPullToRefreshView.showHeaderAnimation();
        if (isRefreshInfo) {
            if (UserManager.getSingleton().isLoginStatus()) {
                mSayDataNet.petalkOne( mSayVo.getPetalkId(),UserManager.getSingleton().getActivePetId());
            }else
            	mSayDataNet.petalkOne( mSayVo.getPetalkId(),"");
        }else {
            netWork(false);
        }


    }

    private void onAddMore() {
        netWork(true);
    }

    private void onPulltoRefreshCallback(boolean isMore){
        if(isMore){
        	mPullToRefreshView.onFooterRefreshComplete();
        }else { 
        	mPullToRefreshView.onHeaderRefreshComplete();
        }
    }

    private void netWork(boolean isMore){
        //		showLoading();
        String type = "";
        String id = "";
        BaseAdapter adapter = null;
        if(mTabIndex == 0){
            type = Constants.COMMENT+"_"+Constants.RELAY;
            adapter = mReviewAdapter;
        }else {
            type = Constants.FAVOUR;
            adapter = mTreadAdapter;
        }
        if(isMore&& null!=adapter&&adapter.getCount() > 0){
            id = ((CommentVo)adapter.getItem(adapter.getCount() - 1)).getId();
        }
        mSayDataNet.interactionList(mSayVo.getPetalkId(), type, id, 20, isMore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_forward:
                forward();
                break;
            case R.id.layout_comment:
                mOperationType = 2;
                mEdComment.setHint("评论");
                mLayoutReply.setVisibility(View.VISIBLE);
                mFunctionBarView.setVisibility(View.INVISIBLE);
                setInputStatus(mEdComment.getText().toString());
                if(layoutInput.getVisibility() == View.VISIBLE)
                    PublicMethod.openSoftKeyBoard(mEdComment, 0);
                break;
            case R.id.layout_share:
                SharePopupWindow  popupWindow=new SharePopupWindow(getApplicationContext(), mSayVo, this,this,true);
                popupWindow.show();
                break;
            case R.id.layout_step:
                if (UserManager.getSingleton().isLoginStatus()) {
                    if (mSayVo.getZ()==0) {
                        mFunctionBarView.startStepAnimation();
                        mSayVo.setZ(1);
                        mSayDataNet.interactionCreate( mSayVo.getPetalkId(),Constants.FAVOUR,UserManager.getSingleton().getActivePetInfo().getId(), mSayVo.getPetId());
                        mFunctionBarView.addStepCount(mSayVo.getPetalkId());
                    }else{
                        PublicMethod.showToast(getApplicationContext(), "已赞过");
                    }

                }else{
                    Intent intent=new Intent(DetailActivity.this, UserLogin_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.layout_root:
                isShowFunctionView();
                break;
            case R.id.btn_send:
                if (UserManager.getSingleton().isLoginStatus()) {
                    if (isRecord) {
                        layoutRecord.setVisibility(View.VISIBLE);
                        layoutInput.setVisibility(View.GONE);
                        PublicMethod.closeSoftKeyBoard(DetailActivity.this, mEdComment);
                    }else{
                        showLoading();
                        String content=mEdComment.getText().toString();
                        String type;
                        if (mOperationType==1) {
                            type=Constants.RELAY;
                        }else {
                            type=Constants.COMMENT;
                        }
                        if (isCommentOtherUser) {
                            mSayDataNet.interactionCreate( mSayVo.getPetalkId(),type,UserManager.getSingleton().getActivePetInfo().getId(), commentPetId,content, "", 0);
                        }else {
                        	mSayDataNet.interactionCreate( mSayVo.getPetalkId(),type,UserManager.getSingleton().getActivePetInfo().getId(), "",content, "", 0);
                        }
                    }

                }else {
                    Intent intent=new Intent(getApplicationContext(), UserLogin_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.img_pet:
                if(mSayVo.getModel() == 0){
                    if(mGifView != null) {
                        GifViewManager.getInstance().pauseGif(mGifView);
                        if (mGifView.isPlaying()) {
                            MediaPlayManager.getSingleton().pause();
                        }
                    }
                }else if(mSayVo.isStoryModel()){
                    GifViewManager.getInstance().stopGif();
                    Intent intent = new Intent(this, StoryPreviewActivity.class);
                    intent.putExtra("storypieces",mSayVo.getStoryPieces());
                    startActivity(intent);
                }
                break;
            case R.id.btn_record:
                break;
            case R.id.btn_keyboard:
                layoutInput.setVisibility(View.VISIBLE);
                layoutRecord.setVisibility(View.GONE);
                PublicMethod.openSoftKeyBoard(mEdComment, 0);
                break;
            case R.id.tv_del:
                mSayDataNet.petalkDelete(mSayVo.getPetalkId());
                mCancelMenu.dismiss();
                break;
            case R.id.tv_report:
                mCancelMenu.dismiss();
                PublicMethod.showToast(getApplicationContext(), R.string.detail_more_report_msg);
                break;
            case R.id.btn_cancle:
                mCancelMenu.dismiss();
                break;
        }
    }

    private void setInputStatus(String text){
        if (mOperationType==2) {
            if (text.trim().equals("")) {
                isRecord=true;
                mBtnReply.setBackgroundResource(R.drawable.comment_luyin);
                mBtnReply.setText("");
            }else {
                isRecord=false;
                mBtnReply.setText("发送");
                mBtnReply.setBackgroundColor(Color.TRANSPARENT);

            }
        }else {
            isRecord=false;
            mBtnReply.setText("发送");
            mBtnReply.setBackgroundColor(Color.TRANSPARENT);
        }
    }



    MediaRecorder mRecorder;
    private boolean flag = false;
    private boolean recordRunning = false;
    long recordStartTime = 0l;
    long recordStopTime = 0l;
    float recordSeconds = 0;
    private String audioFilePath;
    boolean isSendRecord = true;
    /**
     * 录音开始
     */
    public void startRecord() {

        btnRecord.setText("松开结束");
        recordStartTime=System.currentTimeMillis();
        recordSeconds = 0;
        audioFilePath=FileUtile.getPath(getApplicationContext(), Constants.SOUND_FILEPATH)+System.currentTimeMillis()+".amr";
        //audioFilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator ;
        //		File directory = new File(audioFilePath).getParentFile();
        //		if (!directory.exists()) {
        //			//PetsayLog.i(LOG_TAG, "Path to file could not be created");
        //			directory.mkdirs();
        //		}
        //		Toast.makeText(getApplicationContext(), "开始录音", 0).onShow();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mRecorder.setOutputFile(audioFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mVolumeView.setVisibility(View.VISIBLE);
        updateVoluem();
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateVoluem();
        }
    };

    private void updateVoluem(){
        if(mRecorder != null && recordRunning){
            int ratio = mRecorder.getMaxAmplitude() / 600;
            int db = 10;// 分贝 
            if(ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            mVolumeView.volumeChange(db);
            int temp = ((int) (System.currentTimeMillis() - recordStartTime) + 500);
            PublicMethod.log_d("录音时长：" + temp);
            mVolumeView.setRecordTime(PublicMethod.changeTimeFormat(((int) (System.currentTimeMillis() - recordStartTime) + 500))+"“");
            mHandler.postDelayed(mUpdateMicStatusTimer, 200);
        }
    }

    public  void stopRecord() {
        mVolumeView.setVisibility(View.GONE);
        recordRunning = false;
        recordStopTime = System.currentTimeMillis();
        if (null!=mRecorder) {
            try{
                mRecorder.stop();
            }catch(Exception e){
            }
            mRecorder.release();
            mRecorder = null;
        }
        recordSeconds = ((float) (recordStopTime - recordStartTime) + 500) / 1000;
        if (recordSeconds >=3f) {
            if (isSendRecord) {
                onUpload(audioFilePath);
            } else {
                Toast.makeText(getApplicationContext(), "取消录音", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "录音太短...", Toast.LENGTH_SHORT).show();
        }
        btnRecord.setEnabled(true);
        btnRecord.setText("按住说话");
    }

    private void onUpload(final String filepath){
        showLoading();
        //		PublishPetService publishPetService=new PublishPetService();
        //		publishPetService.addListener(new PublishPetListener() {
        //			@Override
        //			public void onPublishPetTalkCallback(ResponseBean bean) {
        //
        //			}
        //
        //			@Override
        //			public void onGetUploadTokenCallback(String token) {
        //				if (!TextUtils.isEmpty(token)) {
        //					Constants.UPLOAD_TOKEN=token;
        //					Message message=new Message();
        //					message.obj=filepath;
        //					message.what=GET_TOKEN_SUCCESS;
        //					mHandler.sendMessage(message);
        //
        //
        //					//				ByteArrayOutputStream out = new ByteArrayOutputStream();
        //					//				headBitmap.compress(Bitmap.CompressFormat.PNG,100,out);
        //					//				uploadService.doUpload(out.toByteArray(), "123");
        //				}else {
        //					mHandler.sendEmptyMessage(UPLOAD_FAILED);
        //				}
        //
        //			}
        //		});
        //		publishPetService.getUploadToken();

        UploadTokenNet net = new UploadTokenNet();
        net.setCallback(new NetCallbackInterface() {
            @Override
            public void onSuccessCallback(ResponseBean bean, int requestCode) {
                if(!TextUtils.isEmpty(bean.getValue())){
                    Constants.UPLOAD_TOKEN=bean.getValue();
                    uploadCore(filepath);
                }else {
                    showToast("获取UploadToken失败");
                }
                closeLoading();
            }

            @Override
            public void onErrorCallback(PetSayError error, int requestCode) {
                if(error.getCode() == PetSayError.CODE_NETWORK_DISABLED){
                    PublicMethod.showToast(DetailActivity.this, R.string.network_disabled);
                }else {
                    showToast(R.string.network_getuploadtoken_error);
                }
                closeLoading();
            }
        });
        net.getUploadToken();
    }

    public void uploadCore(String filepath){
        UploadTools tools=new UploadTools();
        tools.setUploadListener(new UploadServiceListener() {

            @Override
            public void onUploadFinishCallback(boolean isSuccess,String path, String hash,Object tag) {
                if (isSuccess) {
                    String audioUrl=Constants.DOWNLOAD_SERVER +path;
                    Message message=new Message();
                    message.obj=audioUrl;
                    message.what=UPLOAD_SUCCESS;
                    mHandler.sendMessage(message);
                }else {
                    mHandler.sendEmptyMessage(UPLOAD_FAILED);
                }
            }

            @Override
            public void onProcess(long current, long total, Object tag) {

            }
        });
        File file=new File(filepath);
        if(file != null && file.exists()){
            String path = PublicMethod.getServerUploadPath(Constants.COMMENT_AUDIO)+file.getName();
            tools.doUpload(file,path,"");
        }else {
            PublicMethod.log_d("上传音频文件不存在,请检查文件");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayManager.getSingleton().stopAudio();
        mGifView.stopGif();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (from == FROM_PUSH) {
            ActivityTurnToManager.getSingleton().returnMainActivity(DetailActivity.this);
        }
    }

//	@Override
//	protected void applySkin() {
//		super.applySkin();
//		mFunctionBarView.setColorBg(getSkinManager().getColor(getString(R.string.functionbar_bg_color)));
//		//		mTvAtt.setBackGroundRes(SkinManager.getInstance(getApplicationContext()).getDrawable(getString(R.string.detail_focus_bg)));
//		//	    //SkinHelp.setBackground(mTvAtt, getApplicationContext(), getString(R.string.detail_focus_bg));
//		//		mTvAtt.setTextColorRes(SkinManager.getInstance(getApplicationContext()).getColor(getString(R.string.detail_focus_text_color)));
//
//		mStepAnimView.refreshSkin();
//	}

    @Override
    public void onScroll(int scrollY) {

        if (!recordRunning) {
            isCommentOtherUser=false;
            isShowFunctionView();
        }



    }

    /**
     * 移除购买的悬浮框
     */
    private void removeSuspend() {
        if (suspendView != null) {
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
    }

    private boolean isShowFunctionView(){
        if (mLayoutReply != null && mLayoutReply.getVisibility() == View.VISIBLE) {
            isCommentOtherUser=false;
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdComment.getWindowToken(), 0) ;
            mLayoutReply.setVisibility(View.GONE);
            mFunctionBarView.setVisibility(View.VISIBLE);
            if(mReviewAdapter != null)
                mReviewAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private boolean isCommentOtherUser=false;
    private String commentPetId;
    private String commentPetNickName;

    public  void commentOtherUser(boolean isCommentSayVo ){
        if (isCommentSayVo) {
            isCommentOtherUser=true;
            mEdComment.setHint("评论@"+commentPetNickName);
        }else {
            isCommentOtherUser=false;
            mEdComment.setHint("评论");
        }
        mOperationType = 2;
        mLayoutReply.setVisibility(View.VISIBLE);
        mFunctionBarView.setVisibility(View.INVISIBLE);
        setInputStatus(mEdComment.getText().toString());
    }

    public void setSelectCommentVo(CommentVo commentVo,int position){
        if (UserManager.getSingleton().isLoginStatus()) {
            if (null!=commentVo) {
                //				mListView.setSelection(position);
                commentPetId=commentVo.getPetId();
                commentPetNickName=commentVo.getPetNickName();
                if (commentVo.getPetId().equals(UserManager.getSingleton().getActivePetId())) {
                    commentOtherUser(false);
                }else {
                    commentOtherUser(true);
                }
            }
        }else {
            Intent intent=new Intent(DetailActivity.this,UserLogin_Activity.class);
            startActivity(intent);
        }
    }

    @Override
    public View getParentView() {
        return layoutRoot;
    }
    @Override
    public Activity getActivity() {
        return this;
    }

    private void hidenCustomMenu(){
        if(mCancelMenu != null)
            mCancelMenu.dismiss();
    }

    private void showCustomMenu(){
        hidenCustomMenu();
        View view = getLayoutInflater().inflate(R.layout.detail_more, null);
        view.findViewById(R.id.tv_del).setOnClickListener(this);
        view.findViewById(R.id.tv_report).setOnClickListener(this);
        view.findViewById(R.id.btn_cancle).setOnClickListener(this);
        LinearLayout layoutDel=(LinearLayout) view.findViewById(R.id.layout_del);

        if (UserManager.getSingleton().isLoginStatus()&&mSayVo.getPet().getId().equals(UserManager.getSingleton().getActivePetId())) {
            layoutDel.setVisibility(View.VISIBLE);
        }else {
            layoutDel.setVisibility(View.GONE);
        }

        mCancelMenu = new PopupWindow(view,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCancelMenu.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        mCancelMenu.setFocusable(true);
        mCancelMenu.setBackgroundDrawable(new BitmapDrawable());
        mCancelMenu.setAnimationStyle(R.anim.bottom_in);
        mCancelMenu.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
    }

    public void forward(){
        mOperationType=1;
        mEdComment.setHint("转发");
        mLayoutReply.setVisibility(View.VISIBLE);
        mFunctionBarView.setVisibility(View.INVISIBLE);
        PublicMethod.openSoftKeyBoard(mEdComment, 0);
        setInputStatus(mEdComment.getText().toString());
    }

    public void refreshCommentCount(int tag){
        if (tag==1) {
            mFunctionBarView.minusCommentCount();
            mListTtile.setReviewCount(mSayVo.getCounter().getRelay(), mFunctionBarView.getCommentCount());
        }else {
            mFunctionBarView.minusRelayCount();
            mListTtile.setReviewCount(mFunctionBarView.getRelayCount(), mSayVo.getCounter().getComment());
        }

    }

    @Override
    public void shareError(Platform platform, int arg1, Throwable arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shareComplete(Platform platform, int arg1,
                              HashMap<String, Object> arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shareCancel(Platform platform, int arg1) {
        // TODO Auto-generated method stub

    }

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKONE:
              List<PetalkVo> temp = null;
              try {
                  temp = JsonUtils.getList("["+bean.getValue()+"]", PetalkVo.class);
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
              if (null!=temp&&!temp.isEmpty()) {
                  mSayVo=temp.get(0);
                  mReviewAdapter = new HotDetailsAdapter(DetailActivity.this, HotDetailsAdapter.TYPE_REVIEW,mTitleChangeListener,mGifView);
                  if (null==mSayVo.getPet()||null==mSayVo.getPet().getId()) {
                      mReviewAdapter.setData(null, DetailActivity.this);
                  }else {
                      mReviewAdapter.setData(mSayVo.getPet().getId(), DetailActivity.this);
                  }

                  mTreadAdapter = new HotDetailsAdapter(DetailActivity.this, HotDetailsAdapter.TYPE_TREAD,mTitleChangeListener);
                  initData();
                  isRefreshInfo=false;
                  onRefresh();
              }
              

			break;
		case RequestCode.REQUEST_INTERACTIONLIST:
			onPulltoRefreshCallback(bean.isIsMore());
			String type=(String) bean.getTag();
			if (type.equals("C")||type.equals("C_R")) {
				 try {
                     List<CommentVo> mCommentVos=JsonUtils.getList(bean.getValue(), CommentVo.class);
                     if(bean.isIsMore())
                    	 mReviewAdapter.addMore(mCommentVos);
                     else {
                    	 mReviewAdapter.refreshData(mCommentVos);
                     }
                     PublicMethod.setListViewHeightBasedOnChildren(mListView);
                     mEdComment.setText("");
                 } catch (Exception e) {
                     System.err.println("详情解析json出错");
                     e.printStackTrace();
                 }
			}else if (type.equals("F")) {
                 try {
                     List<CommentVo> mStepCommentVos=JsonUtils.getList(bean.getValue(), CommentVo.class);
                     if(bean.isIsMore()){
                    	 mTreadAdapter.addMore(mStepCommentVos);
                     }else {
                    	 mTreadAdapter.refreshData(mStepCommentVos);
                     }
                     PublicMethod.setListViewHeightBasedOnChildren(mListView);
                 } catch (Exception e) {
                     System.err.println("说说详情获取赞列表解析json出错");
                     e.printStackTrace();
                 }
			}
			break;
		case RequestCode.REQUEST_INTERACTIONCREATE:
			type=(String) bean.getTag();
			if (type.equals(Constants.COMMENT)) {
                 ToastUtiles.showCenter(DetailActivity.this,bean.getMessage());
                 mEdComment.setText("");
                 isShowFunctionView();
                 mTabIndex = 0;
                 onRefresh();
                 mFunctionBarView.addCommentCount();
                 mListTtile.setReviewCount(mSayVo.getCounter().getRelay(), mFunctionBarView.getCommentCount());
                 //mSayModule.interactionList(mSayData, mSayVo.getTheID(), "C_R", "", 10);
			
			} else if (type.equals(Constants.FAVOUR)) {
                  isRefreshTread=true;
                  mListTtile.setTreadCount(mSayVo.getCounter().getFavour()+1);
                  UserManager.getSingleton().addStepByPetalkVo(mSayVo);
                  if (mTabIndex==1) {
                      onRefresh();
                  }
			} else if (type.equals(Constants.RELAY)) {
				  mEdComment.setText("");
                  isShowFunctionView();
                  //				mSayModule.removeListener(mSayData);
                  onRefresh();
                  mFunctionBarView.addRelayCount();
                  mListTtile.setReviewCount(mFunctionBarView.getRelayCount(), mSayVo.getCounter().getComment());
			} else {
				//SHARE_SUCCESS;
			}
			break;
		case RequestCode.REQUEST_PETALKDELETE:
             PublicMethod.showToast(getApplicationContext(), R.string.detail_delete_success);
             Constants.delPetalk.put(mSayVo.getPetalkId(), mSayVo);
             finish();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		ResponseBean bean=error.getResponseBean();
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKONE:
			isRefreshInfo=true;
             if (bean != null && bean.getError()==500) {
                 PublicMethod.showToast(getApplicationContext(), bean.getMessage());
                 finish();
             }else{
                 NetworkManager.getSingleton().canclePullRefresh(DetailActivity.this, null);
             }			
			break;
		case RequestCode.REQUEST_INTERACTIONLIST:
            NetworkManager.getSingleton().canclePullRefresh(DetailActivity.this, mPullToRefreshView);
			break;
		case RequestCode.REQUEST_INTERACTIONCREATE:
            String type = (String) bean.getTag();
            if(!TextUtils.isEmpty(type)) {
                if (type.equals(Constants.COMMENT)) {
                    PublicMethod.showToast(DetailActivity.this, "评论失败");
                } else if (type.equals(Constants.FAVOUR)) {

                } else if (type.equals(Constants.RELAY)) {

                } else {
                    //SHARE_FAILED
                }
            }else {
                showToast(R.string.network_error);
            }
			break;
        case RequestCode.REQUEST_PETALKDELETE:
            PublicMethod.showToast(getApplicationContext(), R.string.detail_delete_failed);
			break;
		default:
			break;
		}
	}

}