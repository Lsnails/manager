package com.base.modules.api.entity;

import com.base.modules.business.system.wxuser.entity.WxUserEntity;

import java.util.List;

/**
 * @ClassName WxEntityVo
 * @Author zc
 * @Date 2019/10/17 13:37
 * @Version 1.0
 **/
public class WxEntityVo {
    private WxUserEntity wxUserEntity; //用户相关信息
    private String qrUrl; //扫码wx 二维码
    private List<AcItem> items;

    public WxUserEntity getWxUserEntity() {
        return wxUserEntity;
    }

    public void setWxUserEntity(WxUserEntity wxUserEntity) {
        this.wxUserEntity = wxUserEntity;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public List<AcItem> getItems() {
        return items;
    }

    public void setItems(List<AcItem> items) {
        this.items = items;
    }
}
