package com.chat.entity;

import java.io.Serializable;

/**
 * 拍照记录信息
 * Created by czz on 2017/3/31.
 */
public class JpjPicRecord implements Serializable {
    /**
     * 图片存储索引
     */
    private int pictureID;

    /**
     * 设备名称
     */
    private String user_name;
    /**
     * 图片名称
     */
    private String pictureName;
    /**
     * 图片拍照时间
     */
    private int pictureCaptureDT;
    /**
     * 图片文件大小
     */
    private double pictureFileSize;
    /**
     * 图片存储时间
     */
    private int pictureSaveDT;
    /**
     * 图片上传速率
     */
    private double pictureUploadSpeed;
    /**
     * 图片分析告警标志
     */
    private String pictureAlarmFlag;
    /**
     * 拍照装置ID
     */
    private String deviceID;
    /**
     * 图片访问路径URL
     */
    private String pictureWebURL;
    /**
     * 缩略图路径
     */
    private String thumbWebURL;
    /**
     * 采集类型
     */
    private int snapmode;
    /**
     * 像素
     */
    private int photoSize;
    /**
     * 通道号—表示采集装置上的摄像机编号。
     * 如：一个装置连接2部摄像机，则分别标号为1、2
     */
    private int channel_No;

    @Override
    public String toString() {
        return "JpjPicRecord [pictureID=" + pictureID + ", pictureName="
                + pictureName + ", pictureCaptureDT=" + pictureCaptureDT
                + ", pictureFileSize=" + pictureFileSize + ", pictureSaveDT="
                + pictureSaveDT + ", pictureUploadSpeed=" + pictureUploadSpeed
                + ", pictureAlarmFlag=" + pictureAlarmFlag + ", deviceID="
                + deviceID + ", pictureWebURL=" + pictureWebURL
                + ", thumbWebURL=" + thumbWebURL + ", snapmode="
                + snapmode + ", photoSize=" + photoSize + ", channel_No="
                + channel_No + "]";
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getPictureCaptureDT() {
        return pictureCaptureDT;
    }

    public void setPictureCaptureDT(int pictureCaptureDT) {
        this.pictureCaptureDT = pictureCaptureDT;
    }

    public double getPictureFileSize() {
        return pictureFileSize;
    }

    public void setPictureFileSize(double pictureFileSize) {
        this.pictureFileSize = pictureFileSize;
    }

    public int getPictureSaveDT() {
        return pictureSaveDT;
    }

    public void setPictureSaveDT(int pictureSaveDT) {
        this.pictureSaveDT = pictureSaveDT;
    }

    public double getPictureUploadSpeed() {
        return pictureUploadSpeed;
    }

    public void setPictureUploadSpeed(double pictureUploadSpeed) {
        this.pictureUploadSpeed = pictureUploadSpeed;
    }

    public String getPictureAlarmFlag() {
        return pictureAlarmFlag;
    }

    public void setPictureAlarmFlag(String pictureAlarmFlag) {
        this.pictureAlarmFlag = pictureAlarmFlag;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getPictureWebURL() {
        return pictureWebURL;
    }

    public void setPictureWebURL(String pictureWebURL) {
        this.pictureWebURL = pictureWebURL;
    }

    public String getThumbWebURL() {
        return thumbWebURL;
    }

    public void setThumbWebURL(String thumbWebURL) {
        this.thumbWebURL = thumbWebURL;
    }

    public int getSnapmode() {
        return snapmode;
    }

    public void setSnapmode(int snapmode) {
        this.snapmode = snapmode;
    }

    public int getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(int photoSize) {
        this.photoSize = photoSize;
    }

    public int getChannel_No() {
        return channel_No;
    }

    public void setChannel_No(int channel_No) {
        this.channel_No = channel_No;
    }
}
