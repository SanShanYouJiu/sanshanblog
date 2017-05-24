package com.sanshan.util.info;

/**
 * Created by han on 2017/5/13.
 */
public enum SanShanBlogInfoEnum {
    SAVE_SUCCESS("插入成功"),
    SAVE_FAILURE("插入失败"),
    UPDATE_SUCCESS("更新成功"),
    UPDATE_FAILURE("更新失败"),
    DELETE_SUCCESS("删除成功"),
    DELETE_FAILURE("删除失败"),

    NOT_FOUND_ID("找不到对应的ID"),
    NOT_FOUND_TITLE("找不到对应的文章标题"),
    NOT_FOUND_TIME("找不到该时间下的文件"),
    NOT_FOUND_TAG("找不到对应的标签"),;

    private String value;

    SanShanBlogInfoEnum(String message) {
      this.value=message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
