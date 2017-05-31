package com.sanshan.service.vo;

import com.sanshan.util.info.PosCodeEnum;

/**
 * 通用返回对象
 * @param <T>
 */
public class ResponseMsgVO<T> {

    private Integer status;

    private T data;

    private String msg;

    public ResponseMsgVO() {
    }

    public ResponseMsgVO buildWithMsgAndStatus(PosCodeEnum posCode, String msg) {
        this.status = posCode.getStatus();
        this.msg = msg;
        return this;
    }

    public ResponseMsgVO buildWithPosCode(PosCodeEnum posCode){
        this.status = posCode.getStatus();
        this.msg = posCode.getMsg();
        return this;
    }

    public ResponseMsgVO buildOK() {
        this.status = PosCodeEnum.OK.getStatus();
        this.msg = PosCodeEnum.OK.getMsg();
        return this;
    }

    @SuppressWarnings("unchecked")
    public ResponseMsgVO buildOKWithData(T data) {
        this.status = PosCodeEnum.OK.getStatus();
        this.msg = PosCodeEnum.OK.getMsg();
        this.data = data;
        return this;
    }


}
