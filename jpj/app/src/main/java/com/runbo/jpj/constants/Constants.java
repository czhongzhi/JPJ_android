package com.runbo.jpj.constants;

/**
 * Created by czz on 2017/3/24.
 */
public class Constants {

    /**
     * 百度地图AK
     */
    //public static final String AK_BAIDU_MAP = "4uuyUrvM9IjYzCGpTKDKdxwvx0QdLj1X";

    /**
     * 微信AK
     */
    public static final String AK_WXCHAT = "wx7473419af3ef537f";

    /**
     * 服务器地址
     */
    //public static  String IP = "192.168.107.164";//  LoginPrefereces.getIp(LoginActivity.this);
    public  static String IP = "123.207.100.80";

    /**
     * Socker端口
     */
    public static final int MINA_SOCKER_PORT = 17001;//17001

    /**
     * 版本更新网址
     */
    public static final String BASE_URL = "http://upgrade-cn.runbo.net/";

    public static class ApiUrl{
        public static String UPDATE_CHECK = BASE_URL+"POC/version_jpj.xml";//更新检查版本地址
        public static String UPDATE_VERSION= BASE_URL+"POC/JPJ_release.apk";//更新下载版本地址

        public static String tBASE_URL_API = "http://"+IP+":8080/ptt/api/";

        public static String tBASE_USER = tBASE_URL_API + "user/";
        public static String tBASE_GROUP = tBASE_URL_API + "group/";
        public static String tBASE_LOCATION = tBASE_URL_API + "location/";
        /**登录*/
        public static String tLogin_URL = tBASE_URL_API + "group/";
        /**上传*/
        public static String tUpload_URL = tBASE_URL_API + "uploadFile";
        public static String tUpload_hPic = tBASE_URL_API + "upload/";
        public static String UpdateURL = "http://"+IP+":8080/ptt/servlet/UploadHeaderPicServlet";

        public static String tBASE_PIC_URL = "http://"+IP+":8080/ptt";//图片访问拼接地址
    }

    /**
     * 重设服务IP地址
     * @param ip
     */
    public static void resetServerIp(String ip){
        Constants.IP = ip;
        ApiUrl.tBASE_URL_API = "http://"+IP+":8080/ptt/api/";

        ApiUrl.tBASE_USER = ApiUrl.tBASE_URL_API + "user/";
        ApiUrl.tBASE_GROUP = ApiUrl.tBASE_URL_API + "group/";
        ApiUrl.tBASE_LOCATION = ApiUrl.tBASE_URL_API + "location/";
        ApiUrl.tLogin_URL = ApiUrl.tBASE_URL_API + "group/";
        ApiUrl.tUpload_URL = ApiUrl.tBASE_URL_API + "uploadFile";
        ApiUrl.tUpload_hPic = ApiUrl.tBASE_URL_API + "upload/";

        ApiUrl.UpdateURL = "http://"+IP+":8080/ptt/servlet/UploadHeaderPicServlet";
        ApiUrl.tBASE_PIC_URL = "http://"+IP+":8080/ptt";;
    }

    public static class Tcp{
        public static final int SERVER_MEDIA_PORT = 17001;// server port
    }

    /**
     * 广播action常量
     */
    public final static class BroadCastAction{
        //fragment切换广播
        public final static String BROADCASE_FM_SWITCH = "com.runbo.jpj.broadcase_fm_switch";
        //改变显示的标题
        public final static String BROADCASE_MAIN_TIILE = "com.runbo.jpj.broadcase_main_tiile";

        public final static String BROADCAST_DIALOG_SHOW = "com.runbo.jpj.broadcast_dialog_show";

        public final static String BROADCAST_DIALOG_CANCEL = "com.runbo.jpj.broadcast_dialog_cancel";

        public final static String BROADCAST_CHANGE_USERINFO = "com.runbo.jpj.broadcast_change_userinfo";

    }

    public static class Filestate{
        public static String Idle="idle";
        public static String Uploading="uploading";
        public static String Upload_fail="upload_fail";
        public static String Downloading="downloading";
        public static String Download_fail="download_fail";
        public static String Complete="complete";
        public static String Error="error";
    }

}
