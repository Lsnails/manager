package com.base.modules.business.system.storagea;

public enum BuyType {
    T1(1,"赊购");

    BuyType() {
    }

    BuyType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(int code){
        if(code == T1.getCode()){
            return T1.getDesc();
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
