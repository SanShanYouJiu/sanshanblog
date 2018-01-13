package com.sanshan.util.info;

import com.sanshan.util.exception.EditorTypeConventException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EditorTypeEnum {
    UEDITOR_EDITOR(0,"UEDITOR_EDITOR"),
    MARKDOWN_EDITOR(1,"MARKDOWN_EDITOR"),
    VOID_ID(404,"VOID_ID");

    private String type;
    private int index;

    EditorTypeEnum(int index, String type) {
        this.index=index;
        this.type=type;
    }



    public static EditorTypeEnum getEditorType(String name) throws EditorTypeConventException {
        switch (name) {
            case "UEDITOR_EDITOR" :return EditorTypeEnum.UEDITOR_EDITOR;
            case "MARKDOWN_EDITOR":return  EditorTypeEnum.MARKDOWN_EDITOR;
            case "VOID_ID":return EditorTypeEnum.VOID_ID;
            default:break;
        }
        log.error("转换出错");
        throw new EditorTypeConventException();
    }


    public static String getName(int index){
        for (EditorTypeEnum e: EditorTypeEnum.values()) {
            if (e.getIndex()==index){
                return e.getType();
            }
        }
        return null;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
