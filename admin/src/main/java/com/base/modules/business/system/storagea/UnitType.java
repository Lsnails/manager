package com.base.modules.business.system.storagea;

public enum UnitType {
    T1(1,"台")
    ;

    UnitType() {
    }

    UnitType(int code, String desc) {
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

    private int code;
    private String desc;
}
