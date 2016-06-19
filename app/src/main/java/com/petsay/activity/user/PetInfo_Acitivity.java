package com.petsay.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.petalk.CameraActivity;
import com.petsay.activity.user.shippingaddress.ShippingAddressManagActivity;
import com.petsay.cache.DataFileCache;
import com.petsay.component.view.CircleImageView;
import com.petsay.component.view.TitleBar;
import com.petsay.component.wheelview.CityWheelView;
import com.petsay.component.wheelview.DateWheelView;
import com.petsay.component.wheelview.GenderWheelView;
import com.petsay.component.wheelview.PetWheelView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.network.net.UserNet;
import com.petsay.network.upload.UploadTools;
import com.petsay.network.upload.UploadTools.UploadServiceListener;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.json.JsonParse;
import com.petsay.utile.json.JsonUtils;
import com.petsay.utile.xml.SaxPetTypeParse;
import com.petsay.vo.user.PetType;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetVo;

import java.io.ByteArrayOutputStream;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw
 * 宠物详细信息页面
 *
 */
public class PetInfo_Acitivity extends BaseActivity implements OnClickListener, NetCallbackInterface{

    public static final int GO_CAMERA_CODE=1110;
    /**跳转类型*/
    public static final String TURN_TYPE = "turntype";
    public static final int TYPE_ADDPET=1;
    public static final int TYPE_REG=2;
    public static final int TYPE_EDIT=3;


    private String platformName="";
    private String hintName;

    @InjectView(R.id.titlebar)
    private TitleBar mTitleBar;

    /**展示视图*/
    @InjectView(R.id.layout_show)
    private LinearLayout mShowLayout;


    private EditText mTxt_Name;
    private TextView mTxt_Sex;
    @InjectView(R.id.txt_pet_kind)
    private TextView mTxt_Kind;
    //	@InjectView(R.id.txt_pet_type)
    //	private TextView mTxt_Type;
    @InjectView(R.id.txt_pet_provice)
    private TextView mTxt_Provice;
    @InjectView(R.id.ll_shippingaddress)
    private View mShippingAddress;
    private TextView mTxt_birthday;
    private CircleImageView imgHeader;
    private LinearLayout mLayoutInfo;




    //	private ImageView mImg_Edit;
    private TextView mTxt_Edit;
    private int mTurnType;

    //	@InjectView(R.id.layout_wheel)
    private LinearLayout layoutWheel;

    //	@InjectView(R.id.rlayout_wheel)
    private RelativeLayout rLayoutWheel;
    private Button btnOk;

    private DateWheelView dateWheelView;
    private PetWheelView petWheelView;
    private CityWheelView cityWheelView;
    private GenderWheelView genderWheelView;

    //    @Inject
//    private UserModule mUserModule;
//    private UserData mUserData;
    private UserNet mUserNet;


    private String mLoginName;
    private String mPwd;
    private String mPhoneNumber;
    private PetVo mPetInfo;
    private List<PetType> petTypes;

    public static Bitmap headBitmap;

    private final int UploadHeader_Success=3001;
    private final int UploadHeader_Failed=3002;
//	private final int GETUPLOADTOKEN_SUCCESS = 3003;

    private long defaultPetBirth=System.currentTimeMillis();

    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            ResponseBean bean=(ResponseBean) msg.obj;
            switch (msg.what) {

                case UploadHeader_Success:
                    if (mTurnType==TYPE_ADDPET) {
//					mUserModule.addListener(mUserData);
                        mUserNet.addPet( UserManager.getSingleton().getUserInfo().getId(), mPetInfo);
                    }else if (mTurnType==TYPE_EDIT) {
                        mUserNet.editPet( UserManager.getSingleton().getUserInfo().getId(), mPetInfo);
                    }else if (mTurnType==TYPE_REG) {
//					mUserModule.addListener(mUserData);
                        mUserNet.reg(mLoginName, mPwd, mPhoneNumber, mPetInfo,platformName);
                    }
                    break;
                case UploadHeader_Failed:
                    onUploadFailed();
                    break;

//			case GETUPLOADTOKEN_SUCCESS:
//				doUploadHeader();
//				break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petinfo_layout);
        //默认为编辑宠物资料状态
        mUserNet=new UserNet();
        mUserNet.setCallback(this);
        mUserNet.setTag(this);
        mTurnType = getIntent().getIntExtra(TURN_TYPE,3);
        platformName=getIntent().getStringExtra("platform");
        hintName=getIntent().getStringExtra("hintName");
        if (null==platformName) {
            platformName="";
        }

        headBitmap=null;
        initPets();
        initView();
        if (null != hintName && !hintName.trim().equals("")) {
            mTxt_Name.setText(hintName+"的宝贝");
        }
        setListener();
    }

    private void setListener() {
        mTxt_Kind.setOnClickListener(this);
        mTxt_Provice.setOnClickListener(this);
        mTxt_Sex.setOnClickListener(this);
        mTxt_birthday.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        if(mTurnType == TYPE_EDIT)
            mShippingAddress.setOnClickListener(this);
    }

    private void initPets() {
        try {
            petTypes=SaxPetTypeParse.parse(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化视图组件
     */
    protected void initView() {
        super.initView();
        mTitleBar.setFinishEnable(true);
        layoutWheel=(LinearLayout) findViewById(R.id.layout_wheel);
        rLayoutWheel=(RelativeLayout) findViewById(R.id.rlayout_wheel);
        mTxt_Name=(EditText) findViewById(R.id.txt_pet_name);
        mTxt_Sex=(TextView) findViewById(R.id.txt_pet_sex);
        mTxt_birthday=(TextView) findViewById(R.id.txt_pet_birthday);
        btnOk=(Button) findViewById(R.id.btn_ok);
        imgHeader=(CircleImageView) findViewById(R.id.img_header_usercenter);
        mLayoutInfo=(LinearLayout) findViewById(R.id.layout_info);
        mLayoutInfo.setVisibility(View.GONE);
        imgHeader.setOnClickListener(this);
        switch (mTurnType) {
            case TYPE_ADDPET:
                defaultPetBirth=System.currentTimeMillis();
                initAddPetView();
                break;
            case TYPE_REG:
                defaultPetBirth=System.currentTimeMillis();
                initRegPetView();
                mLoginName=getIntent().getStringExtra("loginName");
                mPwd=getIntent().getStringExtra("password");
                mPhoneNumber=getIntent().getStringExtra("phonenumber");
                //			mShowLayout.setVisibility(View.GONE);
                break;
            case TYPE_EDIT:
                defaultPetBirth=UserManager.getSingleton().getActivePetInfo().getBirthday();
                initEditView();
                //			mEditLayout.setVisibility(View.GONE);
                mShowLayout.setVisibility(View.VISIBLE);
                break;

        }
    }

    /**
     * 从注册跳转过来的TitleBar
     */
    private void initRegPetView(){
        mTitleBar.setTitleText(R.string.reg_pet_title);
        mTxt_Edit = new TextView(this);
        mTxt_Edit.setTextColor(Color.WHITE);
        mTxt_Edit.setText(R.string.reg);
        mTxt_Edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //				if (null==headBitmap) {
                //					PublicMethod.showToast(getApplicationContext(), "还没给您的宠物设置头像哦！");
                //				}else
                switch (mTurnType) {
                    case TYPE_REG:
                        regAdd();
                        break;
                    case TYPE_EDIT:
                        editPet();
                        break;
                }
            }
        });
        mTitleBar.addRightView(mTxt_Edit);
    }

    /**
     * 编辑宠物界面
     */
    private void initEditView(){

        mTxt_Edit =new TextView(this);
        mTxt_Edit.setTextColor(Color.WHITE);
        mTxt_Edit.setText(R.string.title_save);
        LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txt_Params.gravity = Gravity.CENTER;
        txt_Params.leftMargin = 10;
        mPetInfo = UserManager.getSingleton().getActivePetInfo();
        mTxt_Name.setText(mPetInfo.getNickName());
//		ImageLoader.getInstance().displayImage(mPetInfo.getHeadPortrait(), imgHeader, Constants.headerImgOptions);
//		PicassoUtile.loadHeadImg(PetInfo_Acitivity.this, mPetInfo.getHeadPortrait(), imgHeader);
        ImageLoaderHelp.displayHeaderImage(mPetInfo.getHeadPortrait(), imgHeader);
        if (mPetInfo.getGender()>=2) {
            mPetInfo.setGender(2);
        }
        //		mTxt_Kind.setText(text)
        mTxt_Sex.setText(Constants.genderArray[mPetInfo.getGender()]);
        mTxt_Provice.setText(mPetInfo.getAddress());
        mTxt_birthday.setText(PublicMethod.formatTimeToString(mPetInfo.getBirthday(), "yyyy-MM-dd"));
        if (null==Constants.petTypes) {
			Constants.InitPettype(this);
		}
        for (int i = 0; i < Constants.petTypes.size(); i++) {
            PetType type = Constants.petTypes.get(i);
            for (int j = 0; j < type.getId().length; j++) {
                int id = type.getId()[j];
                if(id == mPetInfo.getType()){
                    mTxt_Kind.setText(type.getTypeName() + " " +type.getName()[j]);
                    break;
                }
            }
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(mTxt_Edit,txt_Params);
        layout.setTag(0);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editPet();
            }
        });
        mTitleBar.addRightView(layout);
        mTitleBar.setTitleText(R.string.title_petinfo);
        mShippingAddress.setVisibility(View.VISIBLE);
    }

    /**
     * 从个人中心添加宠物跳转过来的TitleBar
     */
    private void initAddPetView(){
        mTitleBar.setTitleText("添加宠物");
        mTxt_Edit = new TextView(this);
        mTxt_Edit.setTextColor(Color.WHITE);
        mTxt_Edit.setText(R.string.title_add);
        LayoutParams txt_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txt_Params.gravity = Gravity.CENTER;
        txt_Params.leftMargin = 10;
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(mTxt_Edit,txt_Params);
        layout.setTag(0);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addPet();
            }

        });
        mTitleBar.addRightView(layout);
    }
    private void addPet() {
        mPetInfo=new PetVo();
        if(verifyName() && verifyGender() && verifyType() && verifyAddress() && verifyData() && verifyHeadImg()){
            uploadHeader();
            mTxt_Edit.setEnabled(false);
        }

    }

    private void regAdd(){
        mPetInfo=new PetVo();
        if(verifyName() && verifyGender() && verifyType() && verifyAddress() && verifyData() && verifyHeadImg()){
            uploadHeader();
            mTxt_Edit.setEnabled(false);
        }
    }

    private void editPet(){
        if(!verifyName())
            return;
//		mPetInfo.setNickname(mTxt_Name.getText().toString());
        if (null!=genderWheelView) {
            mPetInfo.setGender(genderWheelView.getGenderId());
        }
        if (null!=dateWheelView) {
            mPetInfo.setBirthday(PublicMethod.formatTimeToLong(dateWheelView.getSelectDate(),"yyyy-MM-dd"));
        }
        if (null!=cityWheelView)
            mPetInfo.setAddress(mTxt_Provice.getText().toString());

        if (null!=petWheelView) {
            mPetInfo.setType(Constants.petTypes.get(petWheelView.getSelectIndex()[0]).getId()[petWheelView.getSelectIndex()[1]]);
        }
        uploadHeader();
        mTxt_Edit.setEnabled(false);
    }

    protected boolean verifyName(){
        String name = mTxt_Name.getText().toString().trim().replace(" ", "").replace("　", "");
        if(TextUtils.isEmpty(name)){
            showToast(R.string.no_name);
            return false;
        }else if (name.length()<2||name.length()>15) {
            showToast(R.string.error_name);
            return false;
        }
        mPetInfo.setNickName(name);
        return true;
    }

    private boolean verifyGender(){
        if (null==genderWheelView) {
            showToast(R.string.no_gender);
            return false;
        }else {
            mPetInfo.setGender(genderWheelView.getGenderId());
            return true;
        }
    }

    private boolean verifyData(){
        if (null==dateWheelView) {
            showToast(R.string.no_date);
            return false;
        }else {
            mPetInfo.setBirthday(PublicMethod.formatTimeToLong(dateWheelView.getSelectDate(), "yyyy-MM-dd"));
            return true;
        }
    }

    private boolean verifyType(){
        if (null==petWheelView) {
            showToast(R.string.no_type);
            return false;
        }else {
            mPetInfo.setType(Constants.petTypes.get(petWheelView.getSelectIndex()[0]).getId()[petWheelView.getSelectIndex()[1]]);
            return true;
        }
    }

    private boolean verifyAddress(){
        if (null==cityWheelView) {
            showToast(R.string.no_add);
            return false;
        }else {
            mPetInfo.setAddress(mTxt_Provice.getText().toString());
            return true;
        }
    }

    private boolean verifyHeadImg(){
        if(headBitmap == null){
            showToast(R.string.no_head);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_pet_kind:
                petWheelView=new PetWheelView(getApplicationContext());
                layoutWheel.removeAllViews();
                layoutWheel.addView(petWheelView);
                rLayoutWheel.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_pet_provice:
                cityWheelView=new CityWheelView(getApplicationContext());
                layoutWheel.removeAllViews();
                layoutWheel.addView(cityWheelView);
                rLayoutWheel.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_pet_sex:
                genderWheelView=new GenderWheelView(getApplicationContext(),false);
                layoutWheel.removeAllViews();
                layoutWheel.addView(genderWheelView);
                rLayoutWheel.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_pet_birthday:
                dateWheelView=new DateWheelView(getApplicationContext(),defaultPetBirth);
                layoutWheel.removeAllViews();
                layoutWheel.addView(dateWheelView);
                rLayoutWheel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ok:
                if (layoutWheel.getChildAt(0)==petWheelView) {
                    int[] index=petWheelView.getSelectIndex();
                    mTxt_Kind.setText(Constants.petTypes.get(index[0]).getTypeName()+" "+Constants.petTypes.get(index[0]).getName()[index[1]]);
                }else if(layoutWheel.getChildAt(0)==cityWheelView){
                    mTxt_Provice.setText(cityWheelView.getSelectCity());
                }else if (layoutWheel.getChildAt(0)==dateWheelView) {
                    mTxt_birthday.setText(dateWheelView.getSelectDate());
                }else {
                    mTxt_Sex.setText(Constants.genderArray[genderWheelView.getGenderId()]);
                }
                rLayoutWheel.setVisibility(View.GONE);
                break;
            case R.id.img_header_usercenter:
                Intent intent=new Intent(this,CameraActivity.class);
                intent.putExtra("fromType", 1);
                startActivityForResult(intent, GO_CAMERA_CODE);
                break;
            case R.id.ll_shippingaddress:
                Intent intent2 = new Intent(this,ShippingAddressManagActivity.class);
//                intent2.putExtra("petid",mPetInfo.getId());
                startActivity(intent2);
                break;
        }
    }

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode==KeyEvent.KEYCODE_BACK) {
//			if (rLayoutWheel.getVisibility()==View.VISIBLE) {
//				rLayoutWheel.setVisibility(View.GONE);
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //		switch (requestCode) {
        //		case GO_CAMERA_CODE:
        //			if (resultCode==GO_CAMERA_CODE) {
        if (null!=headBitmap) {
            imgHeader.setImageBitmap(headBitmap);
        }
    }

    private void uploadHeader(){
        showLoading(false);
        if (null==headBitmap) {
            if (mTurnType==TYPE_ADDPET) {
//				mUserModule.addListener(mUserData);
                mUserNet.addPet( UserManager.getSingleton().getUserInfo().getId(), mPetInfo);
            }else if (mTurnType==TYPE_EDIT) {
                mUserNet.editPet(UserManager.getSingleton().getUserInfo().getId(), mPetInfo);
            }else if (mTurnType==TYPE_REG) {
//				mUserModule.addListener(mUserData);
//				if (condition) {
//					
//				}
                mUserNet.reg(mLoginName, mPwd, mPhoneNumber, mPetInfo,platformName);
            }
        }else {
//			PublishPetService publishPetService=new PublishPetService();
//			publishPetService.addListener(new PublishPetListener() {
//				@Override
//				public void onPublishPetTalkCallback(ResponseBean bean) {
//				}
//
//				@Override
//				public void onGetUploadTokenCallback(String token) {
//					if (!TextUtils.isEmpty(token)) {
//						Constants.UPLOAD_TOKEN=token;
//						handler.sendEmptyMessage(GETUPLOADTOKEN_SUCCESS);
//					}else {
//						handler.sendEmptyMessage(UploadHeader_Failed);
//					}	
//
//				}
//			});
//			publishPetService.getUploadToken();

            UploadTokenNet net = new UploadTokenNet();
            net.setCallback(new NetCallbackInterface() {
                @Override
                public void onSuccessCallback(ResponseBean bean, int requestCode) {
                    if(!TextUtils.isEmpty(bean.getValue())){
                        Constants.UPLOAD_TOKEN=bean.getValue();
                        doUploadHeader();
                    }else {
                        onUploadFailed();
                    }
                }

                @Override
                public void onErrorCallback(PetSayError error, int requestCode) {
                    if(error.getCode() == PetSayError.CODE_NETWORK_DISABLED){
                        showToast(R.string.network_disabled);
                    }else {
                        onUploadFailed();
                    }
                    closeLoading();
                }
            });
            net.getUploadToken();


        }

    }

    private void onUploadFailed() {
        mTxt_Edit.setEnabled(true);
        closeLoading();
        showToast("上传头像失败");
    };

    private void doUploadHeader(){
        if(headBitmap == null)
            return;
        AsyncTask<Bitmap, Void,ByteArrayOutputStream> task = new AsyncTask<Bitmap, Void,ByteArrayOutputStream>(){

            @Override
            protected ByteArrayOutputStream doInBackground(Bitmap... params) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                headBitmap.compress(Bitmap.CompressFormat.PNG,50,out);
                return out;
            }

            protected void onPostExecute(ByteArrayOutputStream result) {
                if(result == null){
                    onUploadFailed();
                    return;
                }
                String path = PublicMethod.getServerUploadPath(Constants.AVATAR_IMG)+System.currentTimeMillis()+".png";
                UploadTools UploadTools=new UploadTools();
                UploadTools.setUploadListener(new UploadServiceListener() {
                    @Override
                    public void onUploadFinishCallback(boolean isSuccess,String path, String hash,Object tag) {
                        if (isSuccess) {
                            String headUrl=Constants.DOWNLOAD_SERVER +path;
                            mPetInfo.setHeadPortrait(headUrl);
                            handler.sendEmptyMessage(UploadHeader_Success);
                        }else {
                            handler.sendEmptyMessage(UploadHeader_Failed);
                        }
                    }

                    @Override
                    public void onProcess(long current, long total, Object tag) {
                    }
                });
                UploadTools.doUpload(result.toByteArray(), path,"123");
            };
        };
        task.execute(headBitmap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        headBitmap=null;
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode) {
            case RequestCode.REQUEST_REG:
                JsonParse.getSingleton().parseLogin(PetInfo_Acitivity.this,bean.getValue().toString());
                ToastUtiles.showCenter(getApplicationContext(), bean.getMessage());
                if(RegUser_Acivity.Instance != null)
                    RegUser_Acivity.Instance.finish();
                Intent intent=new Intent(PetInfo_Acitivity.this, RecommendPetsActivity.class);
                closeLoading();
                startActivity(intent);
                finish();
                break;
            case RequestCode.REQUEST_ADDPET:
                PetVo petInfo=JsonUtils.resultData(bean.getValue(), PetVo.class);
                UserManager.getSingleton().getUserInfo().getPetList().add(petInfo);
                DataFileCache.getSingleton().asyncSaveData(Constants.UserFile, UserManager.getSingleton().getUserInfo());
                closeLoading();
                finish();
                break;
            case RequestCode.REQUEST_EDITPET:
                UserManager.getSingleton().editActivePetInfo(mPetInfo);
                DataFileCache.getSingleton().asyncSaveData(Constants.UserFile, UserManager.getSingleton().getUserInfo());
                closeLoading();
                PetInfo_Acitivity.this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        switch (requestCode) {
            case RequestCode.REQUEST_REG:
                mTxt_Edit.setEnabled(true);
                closeLoading();
                if(TextUtils.isEmpty(error.getMessage())){
                    onErrorShowToast(error);
                }else
                    showToast(error.getResponseBean().getMessage());
                break;
            case RequestCode.REQUEST_ADDPET:
                mTxt_Edit.setEnabled(true);
                closeLoading();
                if(TextUtils.isEmpty(error.getMessage())){
                    onErrorShowToast(error);
                }else
                    showToast(error.getResponseBean().getMessage());
                break;
            case RequestCode.REQUEST_EDITPET:
                mTxt_Edit.setEnabled(true);
                PublicMethod.showToast(PetInfo_Acitivity.this, "保存失败！");
                break;
            default:
                break;
        }
    }
}
