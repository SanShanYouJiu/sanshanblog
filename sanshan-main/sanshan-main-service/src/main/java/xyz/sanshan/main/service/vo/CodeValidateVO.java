package xyz.sanshan.main.service.vo;

import lombok.Data;
import lombok.ToString;

/**
 */
@Data
@ToString
public class CodeValidateVO {

    private  String imageCode;

    private Long codeId;


    public CodeValidateVO(String imageCode, Long codeId) {
        this.imageCode = imageCode;
        this.codeId = codeId;
    }
}
