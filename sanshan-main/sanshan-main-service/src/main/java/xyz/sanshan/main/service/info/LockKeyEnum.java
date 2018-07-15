package xyz.sanshan.main.service.info;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sanshan <sanshan@maihaoche.com>
 * @date 2018-07-08
 */
@AllArgsConstructor
public enum LockKeyEnum {

    BLOG_ID_GENERATE_DATA("BLOG_ID_GENERATE_DATA", "博客ID生成"),
    CONSUMER_ACCEPT_METHOD("CONSUMER_ACCEPT_METHOD", "消费者任务分发"),
    NIGHT_CHECK("NIGHT_CHECK", "午夜检查"),
    RECOMMEND_GENERATE("RECOMMEND_GENERATE", "推荐数据生成"),
    ;

    private static final Map<String, LockKeyEnum> mappings = new HashMap<String, LockKeyEnum>(5);

    @Getter
    private String key;
    @Getter
    private String desc;

    static {
        for (LockKeyEnum keyEnum : values()) {
            mappings.put(keyEnum.name(), keyEnum);
        }
    }

    public static LockKeyEnum resolve(String type) {
        return (type != null ? mappings.get(type) : null);
    }


}
