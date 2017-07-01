package com.chat.entity;

/**
 * 登录用户信息
 * Created by czz on 2017/3/28.
 */
public class JpjUser {
    /**
     * 账号
     */
    private int userid;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名称
     */
    private String username;
    /**
     * 头像地址
     */
    private String headerUrl;

    /**
     * 手机号码
     */
    private String phone;
    /**
     * 性别
     */
    private String sex;
    /**
     * 地区
     */
    private String area;
    /**
     * 市级单位id
     */
    private int busiess_id;
    @Override
    public String toString() {
        return "JpjUser [userid=" + userid + ", password=" + password
                + ", username=" + username + ", headerUrl=" + headerUrl
                + ", phone=" + phone + ", sex=" + sex + ", area=" + area
                + ", busiess_id=" + busiess_id + "]";
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getHeaderUrl() {
        return headerUrl;
    }
    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public int getBusiess_id() {
        return busiess_id;
    }

    public void setBusiess_id(int busiess_id) {
        this.busiess_id = busiess_id;
    }
}
