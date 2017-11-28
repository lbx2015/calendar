package net.riking.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

	/**
	 * obj 转换成 Map
	 * @used TODO
	 * @param obj
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public static <T extends Object> Map<String, Object> objProps2Map(T obj, boolean deep) {
		Map<String, Object> map = new HashMap<>();
		if (null == obj) {
			return map;
		}
		if (deep) {
			objPropsDeep2Map(obj, map);
		} else {
			objPropsEasy2Map(obj, map);
		}
		return map;
	}

	/**
	 * 深转换
	 * @used TODO
	 * @param obj
	 * @param map
	 */
	private static <T extends Object> void objPropsDeep2Map(T obj, Map<String, Object> map) {
		try {
			for (Class<? extends Object> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					String name = field.getName();
					Object value = field.get(obj);
					if (null != value) {
						map.put(name, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 浅转换
	 * @used TODO
	 * @param obj
	 * @param map
	 */
	private static <T extends Object> void objPropsEasy2Map(T obj, Map<String, Object> map) {
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(obj);
				if (null != value) {
					map.put(name, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * map 转对象
	 * @used TODO
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T extends Object> T map2Obj(Map<String, Object> map, Class<T> clazz) {
		T obj = null;
		if (null == map || map.size() == 0) {
			return obj;
		}
		try {
			String id = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1) + "Id";
			Object removeValue = map.remove(id);
			map.put("id", removeValue);
			obj = clazz.newInstance();
			List<Field> list = getAllFields(clazz);
			Set<String> keySet = map.keySet();
			for (String fieldName : keySet) {
				for (Field field : list) {
					if (field.getName().equals(fieldName)) {
						field.set(obj, map.get(fieldName));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 获取所有属性
	 * @used TODO
	 * @param clazz
	 * @return
	 */
	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> list = new ArrayList<>();
		try {
			for (; clazz != null; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					list.add(field);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
