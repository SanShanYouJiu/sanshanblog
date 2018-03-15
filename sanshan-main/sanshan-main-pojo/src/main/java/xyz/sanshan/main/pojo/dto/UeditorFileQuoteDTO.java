package xyz.sanshan.main.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**

 */
@Data
@NoArgsConstructor
@ToString
public class UeditorFileQuoteDTO implements Serializable{

    private String filename;

    private Integer quote;

    public UeditorFileQuoteDTO(String filename,Integer quote) {
        this.filename = filename;
        this.quote = quote;
    }
}
