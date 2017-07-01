package com.chat.entity;

import java.util.List;

/**
 * 监拍机的区域分组
 * Created by czz on 2017/3/28.
 */
public class JpjGroup {
    /**
     * 群组id
     */
    private int group_id;

    private String group_name;

    private int owner_id;

    private int create_date;

    private int cmpid;

    private int busiess_id;

    /**
     * 拥有的设备列表
     */
    private List<JpjDevice> jpjDevices;

    @Override
    public String toString() {
        return "JpjGroup [group_id=" + group_id + ", group_name=" + group_name
                + ", owner_id=" + owner_id + ", create_date=" + create_date
                + ", cmpid=" + cmpid + ", busiess_id=" + busiess_id
                + ", jpjDevices=" + jpjDevices + "]";
    }


    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getCreate_date() {
        return create_date;
    }

    public void setCreate_date(int create_date) {
        this.create_date = create_date;
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

    public List<JpjDevice> getJpjDevices() {
        return jpjDevices;
    }

    public void setJpjDevices(List<JpjDevice> jpjDevices) {
        this.jpjDevices = jpjDevices;
    }
}
