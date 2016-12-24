package kr.co.easymanual.utils;

import java.util.HashMap;
import java.util.Map;

public class Return {
	private Return() {};

//	public static <K, V> Map<K, V> success(Map<K, V> map) {
//		return map;
//	}

//	public static <K, V> Map<String, Object> success(Map<K, V> map) {
//		return getData(map);
//	}
//
//	public static <E> Map<String, Object> success(List<E> list) {
//		return getData(list);
//	}

//	private static final Map<String, String> codeMap = new ConcurrentHashMap<String, String>();
//
//	static {
//
//	}

	public static <T> Map<String, Object> success(T t) {
		return getData(t);
	}

	public static Map<String, Object> success() {
		return getData();
	}

	private static <T> Map<String, Object> getData(T t) {
		Map<String, Object> m = getData();
		m.put("data", t);
		return m;
	}

	private static Map<String, Object> getData() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("response", getSuccessResponse());
		return m;
	}

	private static Map<String, String> getSuccessResponse() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("code", "200");
		m.put("status", "success");
		m.put("message", "");

		return m;
	}

	public static Map<String, Object> fail() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("response", getFailResponse());
		return m;
	}

	private static Map<String, String> getFailResponse() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("code", "500");
		m.put("status", "fail");
		m.put("message", "");

		return m;
	}
}
