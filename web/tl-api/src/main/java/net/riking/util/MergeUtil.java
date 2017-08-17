package net.riking.util;

import java.lang.reflect.Field;

/**
 * merge工具类
 * @author lucky.liu
 * @version crateTime：2017年8月17日 上午11:17:33
 * @used TODO
 */
public class MergeUtil {

	public static <T> T merge(T dbObj,T appObj) throws Exception{
		Field[] fields = dbObj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Object val = field.get(appObj);
			if(val!=null){
				field.set(dbObj, val);
			}
		}
		return dbObj;
	}
}
