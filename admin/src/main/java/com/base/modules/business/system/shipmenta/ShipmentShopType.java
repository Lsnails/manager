package com.base.modules.business.system.shipmenta;

public enum ShipmentShopType {
    JD(11, "新飞冰洗旗舰店"),
    JD1(12, "星诺电器专营店"),
    TM(21, "新飞大豫云商专卖店"),
    TB(31, "大豫电器商城 "),
    PDD(41, "大豫电器商城");

    ShipmentShopType() {
    }

    ShipmentShopType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ShipmentShopType getShopUnit(int code) {
        if (code == JD.getCode()) {
            return JD;
        }
        if (code == JD1.getCode()) {
            return JD1;
        }
        if (code == TM.getCode()) {
            return TM;
        }
        if (code == TB.getCode()) {
            return TB;
        }
        if (code == PDD.getCode()) {
            return PDD;
        }
        return null;
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
