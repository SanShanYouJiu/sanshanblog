package com.sanshan.util.info;

import com.sanshan.util.exception.EditorTypeConventException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EditorTypeEnum {
    UEDITOR_EDITOR("UEDITOR_EDITOR",0),
    MarkDown_EDITOR("MarkDown_EDITOR",1),
    Void_Id("Void_Id",404);

    private String type;
    private int index;

    EditorTypeEnum(String type, int index) {
        this.type=type;
        this.index=index;
    }



    public static EditorTypeEnum getEditorType(String name) throws EditorTypeConventException {
        switch (name) {
            case "UEDITOR_EDITOR" :return EditorTypeEnum.UEDITOR_EDITOR;
            case "MarkDown_EDITOR":return  EditorTypeEnum.MarkDown_EDITOR;
            case "Void_Id":return EditorTypeEnum.Void_Id;
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
