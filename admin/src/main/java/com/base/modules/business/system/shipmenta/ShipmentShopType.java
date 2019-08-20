package com.base.modules.business.system.shipmenta;

public enum ShipmentShopType {
    JD(1,"新飞冰洗旗舰店"),
    TM(2,"大豫电器商城"),
    TB(3,"新飞大豫云商专卖店"),
    PDD(4,"大豫电器商城")
    ;

    ShipmentShopType() {
    }

    ShipmentShopType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

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
