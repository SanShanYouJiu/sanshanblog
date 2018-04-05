package xyz.sanshan.main.api.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
public class UserInfo implements Serializable{

    private String _id;
    private String username;
    private String password;
    private String description;

    /**
     * 权限
     */
    private List<String> roles;
    private Date lastPasswordResetDate;

    private Date created;
    private Date updated;

}
