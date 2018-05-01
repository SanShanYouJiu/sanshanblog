

package xyz.sanshan.common.info;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethodEnum {

	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,ALL;


	private static final Map<String, HttpMethodEnum> mappings = new HashMap<String, HttpMethodEnum>(9);

	static {
		for (HttpMethodEnum httpMethod : values()) {
			mappings.put(httpMethod.name(), httpMethod);
		}
	}

	/**
	 *
	 * @param method 请求方法
	 * @return HttpMethodEnum 返回HttpMethodEnum
	 */
	public static HttpMethodEnum resolve(String method) {
		return (method != null ? mappings.get(method) : null);
	}


	public boolean matches(String method) {
		if (this==HttpMethodEnum.ALL){
			return true;
		}
		return (this == resolve(method));
	}

}
