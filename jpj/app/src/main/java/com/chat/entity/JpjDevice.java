package com.chat.entity;

import java.io.Serializable;

/**
 * 监拍机信息
 * Created by czz on 2017/3/28.
 */
public class JpjDevice implements Serializable{
    private int group_id;
    /**
     * 设备Index
     */
    private int deviceIndex;
    /**
     * 设备id
     */
    private String deviceID;
    /**
     * 设备名称
     */
    private String user_name;
    /**
     * 地址
     */
    private String address;

    private int cmpid;

    private int busiess_id;
    /**
     * 装置类型	0：图像; 1：视频；2：测温
     */
    private int deviceType;
    /**
     * 装置厂家名称
     */
    private String deviceManufactorName;
    /**
     * 装置型号
     */
    private String deviceModel;
    /**
     * 装置生产日期
     */
    private String deviceProductionDate;
    /**
     * 装置安装日期
     */
    private String deviceInstallDate;
    /**
     * 装置安装朝向
     */
    private String deviceOrientation;
    /**
     * 装置电话号码
     */
    private String deviceTele;
    /**
     * 装置出厂编码
     */
    private String deviceMeid;
    /**
     * 装置网络类型	1 移动 ；2 联通；3 电信
     */
    private int deviceNetType;
    /**
     * 装置运行状态	0 未运行；2 已运行；3 已拆除
     */
    private int deviceRunState;
    /**
     * 装置屏蔽状态	1:未屏蔽状态; 2:屏蔽状态
     */
    private int deviceShieldState;
    /**
     * 装置关注隐患	1 线下施工；2 建筑工地；3 塔吊作业；4 线下堆物；5 树木生长;
     *  6 野火防范 ; 7 杆塔本体 ；5 鸟类活动； 9 其他类型
     */
    private int deviceDangerID;
    /**
     * 设备的GPS 经度信息
     */
    private double deviceLng;
    /**
     * 设备的GPS 纬度信息
     */
    private double deviceLat;
    /**
     * 所属线路ID
     */
    private int lineID;
    /**
     * 关联杆塔ID
     */
    private int towerID;
    /**
     * poc帐号类型,值有 poc 表示巡检人, jpj 表示监拍机或平台帐号
     */
    private String account_type;
    /**
     * 是否是平台管理员
     */
    private String isjpjadmin;
    @Override
    public String toString() {
        return "JpjDevice [deviceIndex=" + deviceIndex + ", deviceID="
                + deviceID + ", user_name=" + user_name + ", address="
                + address + ", cmpid=" + cmpid + ", busiess_id=" + busiess_id
                + ", deviceType=" + deviceType + ", deviceManufactorName="
                + deviceManufactorName + ", deviceModel=" + deviceModel
                + ", deviceProductionDate=" + deviceProductionDate
                + ", deviceInstallDate=" + deviceInstallDate
                + ", deviceOrientation=" + deviceOrientation + ", deviceTele="
                + deviceTele + ", deviceMeid=" + deviceMeid
                + ", deviceNetType=" + deviceNetType + ", deviceRunState="
                + deviceRunState + ", deviceShieldState=" + deviceShieldState
                + ", deviceDangerID=" + deviceDangerID + ", deviceLng="
                + deviceLng + ", deviceLat=" + deviceLat + ", lineID=" + lineID
                + ", towerID=" + towerID + ", account_type=" + account_type
                + "]";
    }
    public int getDeviceIndex() {
        return deviceIndex;
    }
    public void setDeviceIndex(int deviceIndex) {
        this.deviceIndex = deviceIndex;
    }
    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getCmpid() {
        return cmpid;
    }
    public void setCmpid(int cmpid) {
        this.cmpid = cmpid;
    }
    public int getBusiess_id() {
        return busiess_id;
    }
    public void setBusiess_id(int busiess_id) {
        this.busiess_id = busiess_id;
    }
    public int getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceManufactorName() {
        return deviceManufactorName;
    }
    public void setDeviceManufactorName(String deviceManufactorName) {
        this.deviceManufactorName = deviceManufactorName;
    }
    public String getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
    public String getDeviceProductionDate() {
        return deviceProductionDate;
    }
    public void setDeviceProductionDate(String deviceProductionDate) {
        this.deviceProductionDate = deviceProductionDate;
    }
    public String getDeviceInstallDate() {
        return deviceInstallDate;
    }
    public void setDeviceInstallDate(String deviceInstallDate) {
        this.deviceInstallDate = deviceInstallDate;
    }
    public String getDeviceOrientation() {
        return deviceOrientation;
    }
    public void setDeviceOrientation(String deviceOrientation) {
        this.deviceOrientation = deviceOrientation;
    }
    public String getDeviceTele() {
        return deviceTele;
    }
    public void setDeviceTele(String deviceTele) {
        this.deviceTele = deviceTele;
    }
    public String getDeviceMeid() {
        return deviceMeid;
    }
    public void setDeviceMeid(String deviceMeid) {
        this.deviceMeid = deviceMeid;
    }
    public int getDeviceNetType() {
        return deviceNetType;
    }
    public void setDeviceNetType(int deviceNetType) {
        this.deviceNetType = deviceNetType;
    }
    public int getDeviceRunState() {
        return deviceRunState;
    }
    public void setDeviceRunState(int deviceRunState) {
        this.deviceRunState = deviceRunState;
    }
    public int getDeviceShieldState() {
        return deviceShieldState;
    }
    public void setDeviceShieldState(int deviceShieldState) {
        this.deviceShieldState = deviceShieldState;
    }
    public int getDeviceDangerID() {
        return deviceDangerID;
    }
    public void setDeviceDangerID(int deviceDangerID) {
        this.deviceDangerID = deviceDangerID;
    }
    public double getDeviceLng() {
        return deviceLng;
    }
    public void setDeviceLng(double deviceLng) {
        this.deviceLng = deviceLng;
    }
    public double getDeviceLat() {
        return deviceLat;
    }
    public void setDeviceLat(double deviceLat) {
        this.deviceLat = deviceLat;
    }
    public int getLineID() {
        return lineID;
    }
    public void setLineID(int lineID) {
        this.lineID = lineID;
    }
    public int getTowerID() {
        return towerID;
    }
    public void setTowerID(int towerID) {
        this.towerID = towerID;
    }
    public String getAccount_type() {
        return account_type;
    }
    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
    public void setIsjpjadmin(String isjpjadmin) {
        this.isjpjadmin = isjpjadmin;
    }
    public String getIsjpjadmin() {
        return isjpjadmin;
    }
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
    public int getGroup_id() {
        return group_id;
    }
}
