package xyz.sanshan.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 */
@NoArgsConstructor
@Data
@ToString
public class UeditorIdFileMapDTO implements Serializable{

    private static final long serialVersionUID = -5288491615042220240L;

    private Long blog_id;

    private List<String> filenames;

    public UeditorIdFileMapDTO(Long blog_id, List<String> filenames) {
        this.blog_id = blog_id;
        this.filenames = filenames;
    }


}
