package com.chat.entity;

import java.io.Serializable;

/**
 * 更新版本实体类
 * Created by czz on 2017/3/29.
 */
public class UpdateInfo implements Serializable {
    public String version;
    public String description;
    public String description_en;
    public String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    @Override
    public String toString() {
        return "UpdataInfo [version=" + version + ", description="
                + description + ", description_en=" + description_en + ", url="
                + url + "]";
    }
}
