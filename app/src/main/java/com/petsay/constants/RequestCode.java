package com.petsay.constants;


/**
 * @author wangw
 * 这个类只用于存放请求码，用于区分回调请求
 * 格式：
 * REQUEST_方法名
 */
public class RequestCode {

	public static final int REQUEST_GETUPLOADTOKEN = 1000;
	public static final int REQUEST_GETDECORATIONDATA = 11000;
	public static final int REQUEST_PUBLISHPETTALK = 1200;
    public static final int REQUEST_PUBLISHSTORY = 1201;
	public static final int REQUEST_GETCAPTCHA = 1300;
	public static final int REQUEST_VERIFYCAPTCHA = 1400;
	public static final int REQUEST_RESTPASSWORD = 1500;
	public static final int REQUEST_MODIFYPASSWORD = 1600;
    public static final int REQUEST_GETSAYTAGALL = 1700;
    public static final int REQUEST_SEARCHTAG = 1800;
    public static final int REQUEST_CREATETAG = 1900;
    public static final int REQUEST_GETHOTTAGLIST = 1701;
	
	/*===SystemNet==*/
	public static final int REQUEST_SYSTEMVERSION=100;
	public static final int REQUEST_SYSTEMSTARTUP=101;
	/*===UserNet==*/
	public static final int REQUEST_LOGIN=200;
	public static final int REQUEST_LOGOUT=201;
	public static final int REQUEST_CHECKUSERNAME=202;
	public static final int REQUEST_REG=203;
	public static final int REQUEST_GETUSERINFO=204;
	public static final int REQUEST_ADDPET=205;
	public static final int REQUEST_EDITPET=206;
	public static final int REQUEST_DELPET=207;
	public static final int REQUEST_CHANGEPET=208;
	public static final int REQUEST_FOCUS=209;
	public static final int REQUEST_CANCLEFOCUS=210;
	public static final int REQUEST_MYFANS=211;
	public static final int REQUEST_MYFOCUS=212;
	public static final int REQUEST_MESSAGEUML=213;
	public static final int REQUEST_MESSAGEUMC=214;
	public static final int REQUEST_MESSAGECULM=215;
	public static final int REQUEST_MESSAGECUP=216;
	public static final int REQUEST_PETONE=217;
	public static final int REQUEST_ANNOUNCEMENTCOUNT=218;
	public static final int REQUEST_ANNOUNCEMENTLIST=219;
	public static final int REQUEST_MESSAGEUMCG=220;
	public static final int REQUEST_MESSAGEMPTS=221;
	public static final int REQUEST_PETRECOMMEND=222;
	public static final int REQUEST_PETFANSBATCHFOCUS=223;
    public static final int REQUEST_PETGROWUP = 224;
    public static final int REQUEST_GETSHIPPINGADDRESS = 225;
    public static final int REQUEST_SAVESHIPPINGADDRESS = 226;
	
	/*==SayDataNet==*/
	public static final int REQUEST_GETREVIEWHOTSAYLIST = 300;
	public static final int REQUEST_CHOICEHOTSAY = 301;
	public static final int REQUEST_PETALKUSERLIST=302;
	public static final int REQUEST_PETALKUSERLIST_ADDMORE=303;
	public static final int REQUEST_LAYOUTINTRO=304;
	public static final int REQUEST_LAYOUTDATUM=305;
	public static final int REQUEST_TAGONE=306;
	public static final int REQUEST_PETALKTAGLIST=307;
	public static final int REQUEST_PETALKTAGLISTTIMELINE=308;
	public static final int REQUEST_CHANNELONE=309;
	public static final int REQUEST_INTERACTIONDELETE=310;
    public static final int REQUEST_PETALKPOPRANKWEEKLIST=311;
    public static final int REQUEST_PETSCORETOTALRANKDAYLIST=312;
    public static final int REQUEST_SIMPLEPETSCORETOTALRANKDAYLIST=313;
    public static final int REQUEST_PETALKFOCUSLIST=314;
    public static final int REQUEST_PETALKLIST=315;
    public static final int REQUEST_PETALKALL=316;
    public static final int REQUEST_PETALKCHANNEL=317;
    public static final int REQUEST_PETALKPETBREED=318;
    public static final int REQUEST_PETALKONE=319;
    public static final int REQUEST_INTERACTIONLIST=320;
    public static final int REQUEST_INTERACTIONCREATE=321;
    public static final int REQUEST_PETALKDELETE=322;
    public static final int REQUEST_COUNTERPET=323;
    public static final int REQUEST_SEARCHPETALK=324;
    public static final int REQUEST_SEARCHUSER=325;
    public static final int REQUEST_PETALKLIST4POSTCARD=326;
    public static final int REQUEST_GETTOPTAGHOTLIST=327;
    public static final int REQUEST_LAYOUTTOP3HOT=328;

    /*==GiftBagNet==*/
    public static final int REQUEST_GETALLGIFBAG = 400;
    public static final int REQUEST_GETMYGIFBAG = 410;
    public static final int REQUEST_GETBIFBAGDETAIL = 420;
    public static final int REQUEST_DRAWGIFBAG = 430;
    public static final int REQUEST_GETGIFTTABTITLE = 440;

    /*==MemberNet==*/
    public static final int REQUEST_GETSCORETOTAL = 500;
    public static final int REQUEST_GETSCOREDETAIL = 510;
    public static final int REQUEST_GETCOINDETAIL = 520;
    public static final int REQUEST_GETGRADERULE = 530;
    public static final int REQUEST_GETPETGRADE = 540;
    /**可参与活动列表(登陆后访问，获取签到状态)*/
    public static final int REQUEST_ACTIVITYPARTAKE = 511;
    /**签到*/
    public static final int REQUEST_ACTIVITYSIGNIN = 512;
    /**签到日历*/
    public static final int REQUEST_ACTIVITYSIGNCAL = 513;
    /**获取宠物宠豆*/
    public static final int REQUEST_PETCOIN = 514;
    

    
    /*==ShopNet==*/
    public static final int REQUEST_GOODSTRIALTOP2=601;
    public static final int REQUEST_GOODSTRIALLIST=602;
    public static final int REQUEST_GOODSEXCHANGELIST=603;
    public static final int REQUEST_GOODSORDERLIST=604;
    public static final int REQUEST_GOODSDETAIL=605;
    public static final int REQUEST_GOODSORDERCREATE=606;

    /**ChatSettingNet**/
    public static final int REQUEST_GETCHATSETTING = 701;
    public static final int REQUEST_MODIFYCHATSETTING = 702;
    public static final int REQUEST_GETBLACKLIST = 703;
    public static final int REQUEST_DELETEBLACK = 704;
    public static final int REQUEST_ADDBLACK = 705;
    public static final int REQUEST_CANCHAT = 706;
    
    /**AwardNet**/
    public static final int REQUEST_AWARDACTIVITYLIST=801;
    public static final int REQUEST_AWARDACTIVITYONE=802;
    public static final int REQUEST_AWARDACTIVITYJOIN=803;
    public static final int REQUEST_AWARDACTIVITYMYLIST=804;
    public static final int REQUEST_AWARDACTIVITYMYLISTALL=805;
    public static final int REQUEST_AWARDACTIVITYMYONE=806;
    public static final int REQUEST_AWARDACTIVITYMYLISTINFO=807;
    /**TopicNet**/
    public static final int REQUEST_TOPICLIST=901;
    public static final int REQUEST_TOPICTALKLIST=902;
    public static final int REQUEST_TOPICCOMMENTLIST=903;
    public static final int REQUEST_TOPICTALKONE=904;
    public static final int REQUEST_TOPICCREATETALK=905;
    public static final int REQUEST_TOPICCREATECOMMENT=906;
    public static final int REQUEST_TOPICCREATELIKE=907;
    public static final int REQUEST_TOPICCANCELLIKE=908;
    public static final int REQUEST_TOPICTOPONE=909;

    /**ProductNet**/
    public static final int REQUEST_GETPRODUCTDETAIL = 1001;
    public static final int REQUEST_PRODUCTDETAILSPECSBYCATEGORY = 1002;
    public static final int REQUEST_PRODUCTLISTBYCATEGORY = 1003;
    public static final int REQUEST_PRODUCTDETAILSPECS = 1004;
    /**OrderNet**/
    public static final int REQUEST_ORDERCONFIRM=1101;
    public static final int REQUEST_ORDERCREATE=1102;
    public static final int REQUEST_TOPAYLIST=1103;
    public static final int REQUEST_TOSHIPLIST=1104;
    public static final int REQUEST_TORECEIVELIST=1105;
    public static final int REQUEST_FINISHEDLIST=1106;
    public static final int REQUEST_ORDERDETAILS=1107;
    public static final int REQUEST_RECEIVEPRODUCT=1108;
    public static final int REQUEST_CANCELORDER=1109;

    /**shippingaddress*/
    public static final int REQUEST_ONESHIPPINGADDRESS = 1201;
    public static final int REQUEST_LISTSHIPPINGADDRESS = 1202;
    public static final int REQUEST_DEFAULTSHIPPINGADDRESS = 1203;
    public static final int REQUEST_CREATESHIPPINGADDRESS = 1204;
    public static final int REQUEST_UPDATESHIPPINGADDRESS = 1205;
    public static final int REQUEST_SETDEFAULTSHIPPINGADDRESS = 1206;
    public static final int REQUEST_DELETESHIPPINGADDRESS = 1207;
    
    public static final int REQUEST_COUPONAVAILABLELIST = 1301;
    public static final int REQUEST_COUPONUNAVAILABLELIST = 1302;
    public static final int REQUEST_COUPONORDERAVAILABLELIST = 1303;
    public static final int REQUEST_COUPONACTIVITY = 1304;
    public static final int REQUEST_COUPONGETBYACTIVITY = 1305;
    public static final int REQUEST_COUPONGETBYCODE = 1305;
}
