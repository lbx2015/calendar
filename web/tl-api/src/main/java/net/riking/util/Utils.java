package net.riking.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.riking.config.Const;
import net.riking.entity.MyDateFormat;
import net.riking.entity.model.SysDays;

public class Utils {

	/**
	 * obj 转换成 Map
	 * 
	 * @used TODO
	 * @param obj
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public static <T extends Object> Map<String, Object> objProps2Map(T obj, boolean deep) {
		Map<String, Object> map = null;
		if (deep) {
			map = objPropsDeep2Map(obj);
		} else {
			map = objPropsEasy2Map(obj);
		}
		String id = obj.getClass().getName().substring(obj.getClass().getName().lastIndexOf(".") + 1) + "Id";
		id = id.substring(0, 1).toLowerCase() + id.substring(1);
		Object removeValue = map.remove("id");
		map.put(id, removeValue);
		return map;
	}

	/**
	 * 判断是否是java原生类 true - java类，false - 自己定义的类
	 * @used TODO
	 * @param clz
	 * @return
	 */
	private static boolean isJavaClass(Class<?> clazz) {
		return clazz != null && clazz.getClassLoader() == null;
	}

	/**
	 * 深转换
	 * 
	 * @used TODO
	 * @param obj
	 * @param map
	 */
	private static <T extends Object> Map<String, Object> objPropsDeep2Map(T obj) {
		Map<String, Object> map = new HashMap<>();
		if (null == obj) {
			return map;
		}
		Class<? extends Object> clazz = obj.getClass();
		try {
			for (; null != clazz; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					String name = field.getName();
					if (name.equals("serialVersionUID")) {
						continue;
					}
					Object value = field.get(obj);
					Class<?> type = field.getType();
					if (isJavaClass(type)) {
						if (null != value) {
							if (type == Date.class) {
								String pattern = null != field.getAnnotation(MyDateFormat.class)
										? field.getAnnotation(MyDateFormat.class).pattern() : "yyyyMMddHHmmssSSS";
								SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
								map.put(name, dateFormat.format((Date) value));
							} else {
								map.put(name, value);
							}
						}
					} else {
						map.put(name, objPropsDeep2Map(value));
					}
				}
			}
		} catch (Exception e) {
			System.out.println(clazz.getName() + clazz.getSuperclass().getName());
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 浅转换
	 * 
	 * @used TODO
	 * @param obj
	 * @param map
	 */
	private static <T extends Object> Map<String, Object> objPropsEasy2Map(T obj) {
		Map<String, Object> map = new HashMap<>();
		if (null == obj) {
			return map;
		}
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String name = field.getName();
				if (name.equals("serialVersionUID")) {
					continue;
				}
				Class<?> type = field.getType();
				Object value = field.get(obj);
				if (null != value) {
					if (type == Date.class) {
						String pattern = null != field.getAnnotation(MyDateFormat.class)
								? field.getAnnotation(MyDateFormat.class).pattern() : "yyyyMMddHHmmssSSS";
						SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
						map.put(name, dateFormat.format((Date) value));
					} else {
						map.put(name, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * map 转对象
	 * 
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
			id = id.substring(0, 1).toLowerCase() + id.substring(1);
			Object removeValue = map.remove(id);
			map.put("id", removeValue);
			obj = clazz.newInstance();
			List<Field> list = getAllFields(clazz);
			Set<String> keySet = map.keySet();
			for (String fieldName : keySet) {
				for (Field field : list) {
					if (field.getName().equals(fieldName)) {
						if (field.getType() == Date.class) {
							String pattern = null != field.getAnnotation(MyDateFormat.class)
									? field.getAnnotation(MyDateFormat.class).pattern() : "yyyyMMddHHmmssSSS";
							SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
							map.put(fieldName, dateFormat.parse((String) map.get(fieldName)));
						}
						// else if (field.getType() == Integer.class) {
						// map.put(fieldName, Integer.parseInt((String)
						// map.get(fieldName)));
						// }
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
	 * 
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

	/**
	 * fromObj的值赋在toObj上
	 * @param fromObj
	 * @param toObj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object fromObjToObjValue(Object fromObj, Object toObj)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fromObjfields = fromObj.getClass().getDeclaredFields();
		for (Field fromObjfield : fromObjfields) {
			fromObjfield.setAccessible(true);
			Field[] toObjfields = toObj.getClass().getDeclaredFields();
			for (Field toObjfield : toObjfields) {
				toObjfield.setAccessible(true);
				if (fromObjfield.getName().equals(toObjfield.getName())) {
					if (null != fromObjfield.get(fromObj)) {
						toObjfield.set(toObj, fromObjfield.get(fromObj));
					}
				}
			}
		}
		return toObj;
	}

	public static String getWorkday(String afterDates) {
		if (RedisUtil.getInstall().getObject(Const.SYS_DAY + afterDates) != null) {
			SysDays sysDays = (SysDays) RedisUtil.getInstall().getObject(Const.SYS_DAY + afterDates);
			// 如果是节假日，就延后一天，直到返回非节假日
			if (sysDays.getIsHoliday() == 1) {
				Date date = DateUtils.parseDate(afterDates);
				afterDates = DateUtils.getDateByDays(date, 1);
				return getWorkday(afterDates);
			}
		}
		return afterDates;
	}

	public static void main(String[] args) {
		// AppUser appUser = new AppUser();
		// appUser.setCreatedTime(new Date());
		//
		// Map<String, Object> map = Utils.objProps2Map(appUser, true);
		// System.err.println(map);

		String dates = Utils.getWorkday("20171001");
		System.out.println(dates);
	}
}
