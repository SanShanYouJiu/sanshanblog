package xyz.sanshan.common.exception;

import xyz.sanshan.common.info.PosCodeEnum;

import java.util.Map;

/**
 */
public class MapFoundNullException extends CheckException {

    public MapFoundNullException() {
        super("The required value could not be found in the Map", PosCodeEnum.NOT_FOUND.getStatus());
    }

    public MapFoundNullException(Map map) {
        super("The required value could not be found in the Map:"+map.toString(),PosCodeEnum.NOT_FOUND.getStatus());
    }
}
