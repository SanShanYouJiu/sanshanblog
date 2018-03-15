package xyz.sanshan.main.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
public class UserVoteInfoVO implements Serializable {

    private static final long serialVersionUID = 399144381390511567L;

    private Integer amountFavours;

    private Integer amountTreads;

    private Map<Long, Integer> blogFavourMap;

    private Map<Long, Integer> blogTreadMap;

    public UserVoteInfoVO(Integer amountFavours, Integer amountTreads) {
        this.amountFavours = amountFavours;
        this.amountTreads = amountTreads;
    }
}
