package xyz.sanshan.main.pojo.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
@Document
public class FeedbackDO extends BaseDO implements Serializable {


    private static final long serialVersionUID = -2512029643744464787L;

    private String _id;

    private String email;

    private String opinion;


}
