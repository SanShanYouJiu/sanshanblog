package xyz.sanshan.common;

import xyz.sanshan.common.info.ContextConstants;
import xyz.sanshan.common.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class UserContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getUserID(){
        Object value = get(ContextConstants.CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static String getUsername(){
        Object value = get(ContextConstants.CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }

    public static String getToken(){
        Object value = get(ContextConstants.CONTEXT_KEY_USER_TOKEN);
        return StringHelper.getObjectValue(value);
    }
    public static void setToken(String token){set(ContextConstants.CONTEXT_KEY_USER_TOKEN,token);}


    public static void setUserID(String userID){
        set(ContextConstants.CONTEXT_KEY_USER_ID,userID);
    }

    public static void setUsername(String username){
        set(ContextConstants.CONTEXT_KEY_USERNAME,username);
    }

    private static String returnObjectValue(Object value) {
        return value==null?null:value.toString();
    }

    public static void remove(){
        threadLocal.remove();
    }

}
