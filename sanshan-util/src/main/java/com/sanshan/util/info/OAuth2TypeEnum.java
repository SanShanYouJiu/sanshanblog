package com.sanshan.util.info;

public enum OAuth2TypeEnum {
    GITHUB(0,"GITHUB");

    private String type;
    private int index;

    OAuth2TypeEnum(int index, String type) {
        this.index=index;
        this.type=type;
    }


}
