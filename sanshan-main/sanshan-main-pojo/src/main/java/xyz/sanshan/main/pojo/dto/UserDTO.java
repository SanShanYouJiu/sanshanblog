package xyz.sanshan.main.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@Data
@ToString
public class UserDTO  implements Serializable{

    private static final long serialVersionUID = 5162067894810129753L;

    private String id;

    private String avatar;

    private String username;

    private String email;

    private String blogLink;

    private String honor;

    private Integer status;


}
