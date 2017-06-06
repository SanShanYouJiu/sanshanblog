package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class UserDTO {

    private Long id;

    private String avatear;

    private String nickname;

    private String email;

    private String passowrd;

    private String blog;

    private String honor;

    private Integer status;


}
