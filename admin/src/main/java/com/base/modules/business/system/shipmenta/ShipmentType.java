package com.base.modules.business.system.shipmenta;

/**
 * 出库类型
 */
public enum ShipmentType {
    JD(1,"京东"),
    TM(2,"天猫"),
    TB(3,"淘宝"),
    PDD(4,"拼多多")
    ;

    ShipmentType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    ShipmentType() {
    }

    public static ShipmentType getShipmentType(int code){
        if(code == JD.getCode()){
            return JD;
        }
        if(code == TM.getCode()){
            return TM;
        }
        if(code == TB.getCode()){
            return TB;
        }
        if(code == PDD.getCode()){
            return PDD;
        }
        return null;
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

    private int code;
    private String desc;
}
