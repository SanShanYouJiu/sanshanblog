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

    private String username;

    private String email;

    private String bloglink;

    private String honor;

    private Integer status;


}
