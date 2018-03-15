package xyz.sanshan.main.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class UeditorBlogDTO  extends BaseBlogEditorDTO implements Serializable{

    private static final long serialVersionUID = 1604587939100832871L;
    private  Long id;

}
