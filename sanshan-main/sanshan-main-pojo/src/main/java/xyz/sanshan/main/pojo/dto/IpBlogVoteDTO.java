package xyz.sanshan.main.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author sanshan
 */
@NoArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class IpBlogVoteDTO  implements Serializable{

    private static final long serialVersionUID = 6401177498104862955L;

    @Id
    private Long id;

    private String ip;

    private Long blogId;

    private Boolean favour =false;

    private Boolean tread = false;

}
