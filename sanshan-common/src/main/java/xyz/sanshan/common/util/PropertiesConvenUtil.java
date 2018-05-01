package xyz.sanshan.common.util;

import xyz.sanshan.common.exception.PropertiesConventException;
import xyz.sanshan.common.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * map与properties相互转换 并且存储在磁盘上
 **/
@Slf4j
public class PropertiesConvenUtil {

    /**
     * 根据java标准properties文件读取信息(文件编码格式采用UTF-8格式！)，默认赋值为一个 {@code HashMap<String,String>}
     *
     * @param path 路径
     * @param map map
     * @param type 0是指 String,Set(Long) 1则是Long,EditorTypeEnum
     *             2则是Long,String 3则是Date,Set(Long) 4则是 Long Date
     * @return 返回的map
     * @throws Exception 抛出的异常
     */
    public  final static Map fileToMap(String path, Map map, int type) throws Exception {
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
                    switch (type) {
                        case 0:
                            value = (String) entry.getValue();
                            set = stringConventSet(value);
                            map.put(((String) entry.getKey()), set);
                            break;
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
                            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = fmt.parse((String) entry.getKey());
                            map.put(date, set);
                            break;
                        case 4:
                            DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date2 = fmt2.parse((String) entry.getValue());
                            map.put(Long.valueOf((String) entry.getKey()), date2);
                            break;
                         default:
                             throw  new PropertiesConventException("没有这个转换类型");
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
     * 根据java标准properties文件读取信息，并赋值为一个 {@code HashMap<String,String>}
     *
     * @param path 路径
     * @param map map
     * @param encoding  编码
     * @return 返回 {@code Map<String,String>}’
     * @throws Exception 异常
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


    private final static Set<Long> stringConventSet(String s) {
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
     * @param path 路径
     * @param map map
     * @param description  需要加入的描述信息
     * @throws IOException io异常
     */
    public final static void idMapToFile(String path, Map<Long, EditorTypeEnum> map, String description) throws IOException {
        if (map.size() != 0) {
            Properties properties = new Properties();
            for (Entry<Long, EditorTypeEnum> key : map.entrySet()) {
                properties.setProperty(key.getKey().toString(), key.getValue().toString());
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            properties.store(osw, description);
        }
    }


    /**
     * 存储到本地磁盘的properties文件中
     *
     * @param path 路径
     * @param map  map
     * @param description 描述信息
     * @throws IOException 异常
     */
    public final static void setLongStringToFile(String path, Map<String, Set<Long>> map, String description) throws IOException {
        if (map.size() != 0) {
            Properties properties = new Properties();
            for (Entry<String, Set<Long>> key : map.entrySet()) {
                properties.setProperty(key.getKey().toString(), key.getValue().toString());
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            properties.store(osw, description);
        }
    }

    /**
     * 存储到本地磁盘的properties文件中
     *
     * @param path 路径
     * @param map map
     * @param description  描述信息
     * @throws IOException io异常
     */
    public final static void setLongDateMapToFile(String path, Map<Date, Set<Long>> map, String description) throws IOException {
        if (map.size() != 0) {
            Properties properties = new Properties();
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Entry<Date, Set<Long>> key : map.entrySet()) {
                properties.setProperty(fmt.format(key.getKey()), key.getValue().toString());
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            properties.store(osw, description);
        }
    }


    public final static void longStringMapToFile(String path, Map<Long, String> map, String description) throws IOException {
        if (map.size() != 0) {
            Properties properties = new Properties();
            for (Entry<Long, String> key : map.entrySet()) {
                properties.setProperty(String.valueOf(key.getKey()), key.getValue());
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            properties.store(osw, description);
        }
    }

    public final static void longDateMapToFile(String path, Map<Long, Date> map, String description) throws IOException {
        if (map.size() != 0) {
            Properties properties = new Properties();
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Entry<Long, Date> key : map.entrySet()) {
                properties.setProperty(String.valueOf(key.getKey()), fmt.format(key.getValue()));
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            properties.store(osw, description);
        }
    }

}