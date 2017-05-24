package com.sanshan.service.vo;

import com.sanshan.util.info.PosCodeEnum;

/**
 * 通用返回对象
 * @param <T>
 */
public class ResultVO<T> {

    private Integer status;

    private T data;

    private String msg;

    public ResultVO() {
    }

    public ResultVO buildWithMsgAndStatus(PosCodeEnum posCode, String msg) {
        this.status = posCode.getStatus();
        this.msg = msg;
        return this;
    }

    public ResultVO buildWithPosCode(PosCodeEnum posCode){
        this.status = posCode.getStatus();
        this.msg = posCode.getMsg();
        return this;
    }

    public ResultVO buildOK() {
        this.status = PosCodeEnum.OK.getStatus();
        this.msg = PosCodeEnum.OK.getMsg();
        return this;
    }

    @SuppressWarnings("unchecked")
    public ResultVO buildOKWithData(T data) {
        this.status = PosCodeEnum.OK.getStatus();
        this.msg = PosCodeEnum.OK.getMsg();
        this.data = data;
        return this;
    }


}
