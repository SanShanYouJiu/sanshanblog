package xyz.sanshan.main.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author sanshan
 */
@Data
@NoArgsConstructor
@ToString
public class MarkDownBlogDTO extends BaseBlogDTO implements Serializable {

    private static final long serialVersionUID = -1499743079035930506L;
}
