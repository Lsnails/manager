package com.base.modules.business.system.shipmentb;

public enum SaleType {
    T1(1,"赊销")
    ;

    SaleType() {
    }


    private int code;
    private String desc;

    SaleType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
