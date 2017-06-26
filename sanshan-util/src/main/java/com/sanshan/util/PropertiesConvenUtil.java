package com.sanshan.util;

import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * map与properties相互转换 并且存储在磁盘上
 **/
@Slf4j
public class PropertiesConvenUtil {

    /**
     * 根据java标准properties文件读取信息(文件编码格式采用UTF-8格式！)，默认赋值为一个 HashMap<String,String>
     *
     * @param path
     * @param map
     * @param type 0是指 String,Set(Long) 1则是Long,EditorTypeEnum
     *             2则是Long,String 3则是Date,Set(Long) 4则是 Long Date
     * @return
     * @throws Exception
     */
    public final static Map fileToMap(String path, Map map, int type) throws Exception {
        if (map == null) {
            map = new HashMap<String, String>();
        }
        FileInputStream isr = null;
        Reader r = null;
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                log.info("文件不存在{}", path);
                file.createNewFile();
            }
            isr = new FileInputStream(path);
            r = new InputStreamReader(isr, "utf-8");
            Properties props = new Properties();

            props.load(r);
            Set<Entry<Object, Object>> entrySet = props.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                if (!entry.getKey().toString().startsWith("#")) {
                    String value;
                    Set<Long> set;
                   switch (type){
                       case 0:
                             value = (String) entry.getValue();
                            set = stringConventSet(value);
                           map.put(((String) entry.getKey()).trim(), set);break;
                       case 1:
                           map.put((Long.valueOf((String) entry.getKey())), EditorTypeEnum.getEditorType((String) entry
                                   .getValue()));
                           break;
                       case 2:
                           map.put((Long.valueOf((String) entry.getKey())), entry.getValue());
                           break;
                       case 3:
                           value = (String) entry.getValue();
                           set = stringConventSet(value);
                           map.put(new Date((String) entry.getKey()),set);
                           break;
                       case 4:
                           map.put(Long.valueOf((String) entry.getKey()), new Date((String) entry.getValue()));
                   }
                }
            }
            return map;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    log.error("文件关闭异常", e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e2) {
                    log.error("文件关闭异常", e2);
                }
            }
        }
    }

    /**
     * 根据java标准properties文件读取信息，并赋值为一个 HashMap<String,String>
     *
     * @param path
     * @param map
     * @return
     * @throws Exception
     */
    public final static Map<String, String> fileToMap(String path, Map map, String encoding) throws Exception {
        if (map == null) {
            map = new HashMap<String, String>();
        }
        FileInputStream isr = null;
        Reader r = null;
        try {
            isr = new FileInputStream(path);
            r = new InputStreamReader(isr, encoding);
            Properties props = new Properties();
            props.load(r);
            Set<Entry<Object, Object>> entrySet = props.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                if (!entry.getKey().toString().startsWith("#")) {
                    map.put(((String) entry.getKey()).trim(), ((String) entry
                            .getValue()).trim());
                }
            }
            return map;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    log.error("文件关闭异常", e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e2) {
                    log.error("文件关闭异常", e2);
                }
            }
        }
    }


    private  final  static Set<Long> stringConventSet(String s) {
        Set<Long> longs = new HashSet<>();
        char[] chars = s.toCharArray();
        chars[0] = ' ';
        chars[chars.length - 1] = ' ';
        String str = String.valueOf(chars);
        StringTokenizer toKenizer = new StringTokenizer(str, ", ");
        while (toKenizer.hasMoreElements()) {
            longs.add(Long.valueOf(toKenizer.nextToken()));
        }
        return longs;
    }


    /**
     * 存储到本地磁盘的properties文件中
     *
     * @param path
     * @param map
     * @throws IOException
     */
    public final static void IdMapToFile(String path, Map<Long, EditorTypeEnum> map,String description) throws IOException {
        Properties properties = new Properties();
        for (Entry<Long, EditorTypeEnum> key : map.entrySet()) {
            properties.setProperty(key.getKey().toString(), key.getValue().toString());
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        properties.store(osw,description);
    }


    /**
     * 存储到本地磁盘的properties文件中
     *
     * @param path
     * @param map
     * @throws IOException
     */
    public final static void setLongStringToFile(String path, Map<String, Set<Long>> map,String description) throws IOException {
        Properties properties = new Properties();
        for (Entry<String, Set<Long>> key : map.entrySet()) {
            properties.setProperty(key.getKey(), key.getValue().toString());
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        properties.store(osw,description);
    }

    /**
     * 存储到本地磁盘的properties文件中
     *
     * @param path
     * @param map
     * @throws IOException
     */
    public final static void setLongDateMapToFile(String path, Map<Date, Set<Long>> map,String description) throws IOException {
        Properties properties = new Properties();
        for (Entry<Date, Set<Long>> key : map.entrySet()) {
            properties.setProperty(String.valueOf(key.getKey()), key.getValue().toString());
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        properties.store(osw,description);
    }


    public final static void LongStringMapToFile(String path, Map<Long, String> map,String description) throws IOException {
        Properties properties = new Properties();
        for (Entry<Long, String> key : map.entrySet()) {
            properties.setProperty(String.valueOf(key.getKey()), key.getValue());
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        properties.store(osw,description);
    }

    public final static void LongDateMapToFile(String path, Map<Long, Date> map,String description) throws IOException {
        Properties properties = new Properties();
        for (Entry<Long, Date> key : map.entrySet()) {
            properties.setProperty(String.valueOf(key.getKey()), String.valueOf(key.getValue()));
        }
        FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        properties.store(osw,description);
    }

}