package com.chat.dbentity;

import android.net.Uri;

public class DBConstant {
    public static final String AUTOR = "com.jpj.chat.chatprovider";
//    public static final String AUTOR = "com.android.chat.ConentProvider.firstContentProvider";
//    public static String Tab_chatMessage = "chatMessage";
//    public static String Tab_downMessage = "downMessage";
//    public static String Tab_soundMessage = "soundMessage";
//    public static String Tab_callMessage = "callMessage";
//    public static String Tab_AloneChatMessage = "alonechatMessage";
//    public static String Tab_Groups = "groups";
//    public static String Tab_GroupsChe = "groupchannel";
//    public static String Tab_CustomerVo = "customervos";
//    public static String Tab_ppt_group_user = "ptt_group_user";
//    public static String Tab_Groups_temp = "groups_temp";
//    public static String gpslocation = "gpslocation";
//    public static String Tab_UserInfo = "userInfo";
//    public static String Tab_RepeaterInfo = "repeaterInfo";

    //jpj sqliet数据表
    public static String Tab_jpjGroup = "jpjgroup";
    public static String Tab_jpjDevice = "jpjdevice";
    public static String Tab_jpjUser = "jpjuser";

    public static class JpjGroup {
        public static final String uri = "content://" + AUTOR + "/jpjgroup";
        public static final Uri center_url = Uri.parse(uri);
        public static String group_id = "group_id";
        public static String group_name = "group_name";
        public static String owner_id = "owner_id";
        public static String create_date = "create_date";
        public static String cmpid = "cmpid";
        public static String busiess_id = "busiess_id";
    }

    public static class JpjDevice {
        public static final String uri = "content://" + AUTOR + "/jpjdevice";
        public static final Uri center_url = Uri.parse(uri);
        public static String group_id = "group_id";
        public static String deviceIndex = "deviceIndex";
        public static String deviceID = "deviceID";
        public static String user_name = "user_name";
        public static String address = "address";
        public static String cmpid = "cmpid";
        public static String busiess_id = "busiess_id";
        public static String deviceType = "deviceType";
        public static String deviceManufactorName = "deviceManufactorName";
        public static String deviceModel = "deviceModel";
        public static String deviceProductionDate = "deviceProductionDate";
        public static String deviceInstallDate = "deviceInstallDate";
        public static String deviceOrientation = "deviceOrientation";
        public static String deviceTele = "deviceTele";
        public static String deviceMeid = "deviceMeid";
        public static String deviceNetType = "deviceNetType";
        public static String deviceRunState = "deviceRunState";
        public static String deviceShieldState = "deviceShieldState";
        public static String deviceDangerID = "deviceDangerID";
        public static String deviceLng = "deviceLng";
        public static String deviceLat = "deviceLat";
        public static String lineID = "lineID";
        public static String towerID = "towerID";
        public static String account_type = "account_type";
        public static String isjpjadmin = "isjpjadmin";
    }

    public static class JpjUser {
        public static final String uri = "content://" + AUTOR + "/jpjuser";
        public static final Uri center_url = Uri.parse(uri);
        public static String userid = "userid";
        public static String password = "password";
        public static String username = "username";
        public static String headerUrl = "headerUrl";
        public static String phone = "phone";
        public static String sex = "sex";
        public static String area = "area";
        public static String busiess_id = "busiess_id";
    }


    /**************************************************/

//    public static class ChatMessage {
//
//        public static final String uri = "content://" + AUTOR + "/chatmessage";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String msgtype = "msgtype";
//        public static String groupid = "groupid";
//        public static String userid = "userid";
//        public static String recvuserid = "recvuserid";
//        public static String charsetname = "charsetname";
//        public static String msgcontent = "msgcontent";
//        public static String datetime = "datetime";
//
//        public static String filename = "filename";
//        public static String filetype = "filetype";
//        public static String filepath = "filepath";
//        public static String filesize = "filesize";
//        public static String filestate = "filestate";
//        public static String filepercent = "filepercent";
//        public static String filemd5 = "filemd5";
//        public static String fileid = "fileid";
//
//        //0表示没有查看的,1表示已经查看的
//        public static String display = "display";
//    }
//
//    public static class DownMessage {
//        public static final String uri = "content://" + AUTOR + "/downMessage";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String msgtype = "msgtype";
//        public static String userid = "userid";
//        public static String groupid = "groupid";
//        public static String threadid = "threadid";
//        public static String downlength = "downlength";
//        public static String state = "state";
//        public static String downurl = "downurl";//例如:http://192.168.107.104:8080/ptt/servlet/DownloadHandleServlet?filename=6ccc4c070af5c6114a9ebc1e6552f141
//    }
//
//    public static class SoundMessage {
//        public static final String uri = "content://" + AUTOR + "/soundmessage";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String msgtype = "msgtype";
//        public static String calling_userid = "calling_userid";
//        public static String Called_userid = "Called_userid";
//        public static String groupid = "groupid";
//        public static String startdatetime = "startdatetime";
//        public static String enddatetime = "enddatetime";
//    }
//
//    public static class CallMessage {
//        public static final String uri = "content://" + AUTOR + "/callMessage";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String msgtype = "msgtype";
//        public static String calling_userid = "calling_userid";
//        public static String called_userid = "called_userid";
//        public static String groupid = "groupid";
//        public static String isanswer = "isanswer";
//        public static String startdatetime = "startdatetime";
//        public static String enddatetime = "enddatetime";
//    }
//
//    public static class AloneChatMessage {
//
//        public static final String uri = "content://" + AUTOR + "/aloneMessage";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String msgtype = "msgtype";
//        public static String groupid = "groupid";
//        public static String userid = "userid";
//        public static String recvuserid = "recvuserid";
//        public static String charsetname = "charsetname";
//        public static String msgcontent = "msgcontent";
//        public static String datetime = "datetime";
//        public static String display = "display";
//    }
//
//    public static class Groups {
//        public static final String uri = "content://" + AUTOR + "/groups";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String group_id = "group_id";
//        public static String group_name = "group_name";
//        public static String owner_id = "owner_id";
//        public static String create_date = "create_date";
//        public static String user_id = "user_id";
//    }
//
//    public static class GroupsAndChe {
//        public static final String uri = "content://" + AUTOR + "/groupchannel";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String group_id = "group_id";
//        public static String channel_num = "channel_num";
//    }
//
//    public static class CustomerVo {
//        public static final String uri = "content://" + AUTOR + "/customervo";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String user_id = "user_id";
//        public static String password = "password";
//        public static String phone = "phone";
//        public static String user_name = "user_name";
//        public static String grade = "grade";
//        public static String logon = "logon";
//        public static String is_chat = "is_chat";
//        public static String gender = "gender";
//        public static String register_date = "register_date";
//        public static String group_id = "group_id";
//        public static String isaccount = "isaccount";
//    }
//
//    public static class GPSlocation {
//        public static final String uri = "content://" + AUTOR + "/gpslocation";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String error = "error";
//        public static String latitude = "latitude";
//        public static String lontitude = "lontitude";
//        public static String radius = "radius";
//        public static String addr = "addr";
//        public static String operationers = "operationers";
//        public static String locationtime = "locationtime";
//        public static String loactionname = "loactionname";
//    }
//
//    public static class groups_temp {
//        public static final String uri = "content://" + AUTOR + "/groupss_temp";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String group_id = "group_id";
//        public static String group_name = "group_name";
//        public static String owner_id = "owner_id";
//        public static String create_date = "create_date";
//        public static String user_id = "user_id";
//    }
//
//    //用户信息的字段  weixj add
//    public static class UserInfo {
//        public static final String uri = "content://" + AUTOR + "/userinfo";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String user_id = "user_id";
//        public static String user_name = "user_name";
//        public static String account = "account";
//        public static String gender = "gender";
//        public static String region = "region";
//        public static String file_MD5 = "file_MD5";
//        public static String file_name = "file_name";
//    }
//
//    //中继台权限信息的字段  hkh add
//    public static class RepeaterInfo {
//        public static final String uri = "content://" + AUTOR + "/repeaterinfo";
//        public static final Uri center_url = Uri.parse(uri);
//        public static String _id = "_id";
//        public static String groupid = "groupid";
//        public static String user_id = "user_id";
//        public static String rx = "rx";
//        public static String tx = "tx";
//        public static String modestate = "modestate";
//        public static String modestateptt = "modestateptt";
//        public static String modestatepoc = "modestatepoc";
//    }
}
