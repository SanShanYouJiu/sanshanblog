package com.sanshan.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class UserDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     *头像
     */
    private String avatear;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String passowrd;

    /**
     * 博客链接
     */
    private String blog;


}
