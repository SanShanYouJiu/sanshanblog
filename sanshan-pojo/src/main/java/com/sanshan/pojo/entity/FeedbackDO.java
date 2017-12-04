package com.sanshan.pojo.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
@Table(name = "feedback")
public class FeedbackDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = -3306932378732722579L;

    @Id
    private Long id;

    private String email;

    private String opinion;


}
