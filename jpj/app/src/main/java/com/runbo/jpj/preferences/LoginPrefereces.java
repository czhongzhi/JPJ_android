package com.runbo.jpj.preferences;

import android.content.Context;

import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.PreferencesUtil;

public class LoginPrefereces {
    /**
     * 本地缓存文件名
     */
    private static final String tPreference = "login_jpj";

    private static final String tIpKey = "ip";
    private static final String tNumberKey = "number";//账号
    private static final String tPasswdKey = "password";//
    private static final String tLoginStatus = "loginStatus";//登录状态
    private static final String tSavePswStatusKey = "savepsw";//记住密码状态
    private static final String tPhoneKey = "phone";
    private static final String tCurrGroupidKey = "currGroupid";//保存当前组id
    private static final String tCurrDeviceidKey = "currDeviceid";//保存当前设备id

    private static final String accountKey = "account";
    private static final String pswKey = "psw";

    public static void setIp(Context mContext, String ip) {
        Constants.resetServerIp(ip);
        PreferencesUtil.setStringPreferences(mContext, tPreference, tIpKey, ip);
    }

    public static void setNumber(Context mContext, String number) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, tNumberKey, number);
    }

    public static void setPasswd(Context mContext, String password) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, tPasswdKey, password);
    }

    public static void setLoginStatus(Context mContext,boolean status){
        PreferencesUtil.setbooleanPreference(mContext,tPreference,tLoginStatus,status);
    }

    public static void setSavePswStatus(Context mContext,boolean status){
        PreferencesUtil.setbooleanPreference(mContext,tPreference,tSavePswStatusKey,status);
    }

    public static void setCurrGroupID(Context mContext,int groupID){
        PreferencesUtil.setIntPreference(mContext,tPreference,tCurrGroupidKey,groupID);
    }

    public static void settCurrDeviceID(Context mContext,int deviceID){
        PreferencesUtil.setIntPreference(mContext,tPreference,tCurrDeviceidKey,deviceID);
    }

    /********************/

    public static void setPhone(Context mContext, String phone) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, tPhoneKey, phone);
    }

    public static void setAccount(Context mContext, String account) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, accountKey, account);
    }

    public static void setPsw(Context mContext, String psw) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, pswKey, psw);
    }

    public static void setData_String(Context mContext, String key, String mstring) {
        PreferencesUtil.setStringPreferences(mContext, tPreference, key, mstring);
    }

    public static void setData_Int(Context mContext, String key, int num) {
        PreferencesUtil.setIntPreference(mContext, tPreference, key, num);
    }


    public static String getIp(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, tIpKey, Constants.IP);
    }


    public static String getNumber(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, tNumberKey, "");
    }

    public static String getPasswd(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, tPasswdKey, "");
    }

    public static boolean getLoginStatus(Context mContext){
        return  PreferencesUtil.getbooleanPreference(mContext,tPreference,tLoginStatus);
    }

    public static boolean getSavePswStatus(Context mContext){
        return  PreferencesUtil.getbooleanPreference(mContext,tPreference,tSavePswStatusKey);
    }

    public static int getCurrGroupID(Context mContext){
        return  PreferencesUtil.getIntPreference(mContext,tPreference,tCurrGroupidKey,-1);
    }

    public static int getCurrDeviceID(Context mContext){
        return  PreferencesUtil.getIntPreference(mContext,tPreference,tCurrDeviceidKey,-1);
    }


    /********************/

    public static String getPhone(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, tPhoneKey, "88800000");
    }

    public static String getAccount(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, accountKey, "88800000");
    }

    public static String getPsw(Context mContext) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, pswKey, "123456");
    }

    public static String getData_String(Context mContext, String key) {
        return PreferencesUtil.getStringPreference(mContext, tPreference, key, null);
    }

    public static int getData_Int(Context mContext, String key) {
        return PreferencesUtil.getIntPreference(mContext, tPreference, key, 0);
    }

    public static boolean getState(Context mContext, String key) {
        return PreferencesUtil.getbooleanPreference(mContext, tPreference, key);
    }

    public static void setState(Context mContext, boolean flag, String key) {
        PreferencesUtil.setbooleanPreference(mContext, tPreference, key, flag);
    }

    public static boolean getDefualtState(Context mContext, String key, boolean flag) {
        return PreferencesUtil.getDefualtState(mContext, tPreference, key, flag);
    }

    public static void setDefualtState(Context mContext, boolean flag, String key) {
        PreferencesUtil.setDefualtState(mContext, tPreference, key, flag);
    }

}
