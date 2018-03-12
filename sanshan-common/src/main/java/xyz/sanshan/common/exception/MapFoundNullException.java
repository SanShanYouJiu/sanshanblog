package xyz.sanshan.common.exception;

import java.util.Map;

/**
 */
public class MapFoundNullException extends CheckException {

    public MapFoundNullException() {
        super("The required value could not be found in the Map");
    }

    public MapFoundNullException(Map map) {
        super("The required value could not be found in the Map:"+map.toString());
    }

    public MapFoundNullException(String msg) {
        super(msg);
    }

    public MapFoundNullException(String msg, Map map) {
        super(msg+":"+map.toString());
    }
}
