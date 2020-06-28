package com.dohko.lock;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 11:03
 */
public enum ClusterTypeEnum {

    SINGLE("single"),
    SENTINEL("sentinel"),
    CLUSTER("cluster");

    private String type;

    ClusterTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
