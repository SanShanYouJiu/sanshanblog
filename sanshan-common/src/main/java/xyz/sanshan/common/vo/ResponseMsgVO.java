package xyz.sanshan.common.vo;

import xyz.sanshan.common.info.PosCodeEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResponseMsgVO<T> implements Serializable {

    private static final long serialVersionUID = -7268116244546414571L;

    private Integer status;// 是否成功标志

    private T data;// 成功时返回的数据

    private String msg;// 错误信息

    public ResponseMsgVO() {
    }

    public ResponseMsgVO buildWithMsgAndStatus(PosCodeEnum posCode, String msg) {
        this.status = posCode.getStatus();
        this.msg = msg;
        return this;
    }


    public ResponseMsgVO buildWithPosCode(PosCodeEnum posCode) {
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
