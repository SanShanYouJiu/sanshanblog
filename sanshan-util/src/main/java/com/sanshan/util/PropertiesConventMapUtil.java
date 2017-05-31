package com.sanshan.util;

import com.sanshan.util.info.EditorTypeEnum;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * map与properties相互转换 并且存储在磁盘上
 **/
public class PropertiesConventMapUtil {

    private final static Logger log = Logger.getLogger(PropertiesConventMapUtil.class);

	/**
	 * 根据java标准properties文件读取信息(文件编码格式采用UTF-8格式！)，并赋值为一个 HashMap<Long,EditorType>
	 * @param path
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public final static Map<Long,EditorTypeEnum> fileToMap(String path, Map<Long,EditorTypeEnum> map) throws Exception{
		if(map == null){
			map = new TreeMap<Long,EditorTypeEnum>();
		}
		FileInputStream isr = null;
		Reader r = null;
		try {
			isr = new FileInputStream(path);
			r = new InputStreamReader(isr, "utf-8");
			Properties props = new Properties();
			props.load(r);
			Set<Entry<Object, Object>> entrySet = props.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				if (!entry.getKey().toString().startsWith("#")) {
					map.put((Long.valueOf((String) entry.getKey())), EditorTypeEnum.getEditorType((String) entry
							.getValue()));
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
					log.error("文件关闭异常",e2);
				}
			}
		}
	}

	
	/**
	 * 根据java标准properties文件读取信息，并赋值为一个 HashMap<String,String>
	 * @param path
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public final static Map<String,String> fileToMap(String path,Map<String,String> map,String encoding) throws Exception{
		if(map == null){
			map = new HashMap<String,String>();
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
					log.error("文件关闭异常",e2);
				}
			}
		}
	}

	/**
	 * 存储到本地磁盘的properties文件中
	 * @param path
	 * @param map
	 * @throws IOException
	 */
	public final static void  MapToProperties(String path,Map<Long, EditorTypeEnum> map) throws IOException {
		String content ="";
			for (Entry<Long, EditorTypeEnum> key:map.entrySet()) {
				content += key.getKey();
				content+="=";
				content += key.getValue();
				content += "\n";
			}
			FileUtils.write(new File(path), content);
	}



}