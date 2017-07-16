package com.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class UserDTO {

    private Long id;

    private String avatar;

    private String username;

    private String email;

    private String blogLink;

    private String honor;

    private Integer status;


}
