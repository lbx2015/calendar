package net.riking.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcelUtils {
	@SuppressWarnings("unchecked")
	public static <T> void exportByList(List<T> list, OutputStream output, LinkedHashMap<String, String> fields)
			throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("sheet0");
		// 第一行
		HSSFRow fRow = sheet.createRow(0);
		int temp = 0;
		for (String name : fields.values()) {
			fRow.createCell(temp).setCellValue(name);
			temp++;
		}

		if (list != null && list.size() > 0) {
			Class<T> clazz = (Class<T>) list.get(0).getClass();
			BeanInfo beaninfo = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] propertyDescriptors = beaninfo.getPropertyDescriptors();

			for (int i = 0; i < list.size(); i++) {
				HSSFRow row = sheet.createRow(i + 1);
				int j = 0;
				for (String key : fields.keySet()) {
					String value = null;
					for (PropertyDescriptor prop : propertyDescriptors) {
						if (prop.getName().equals(key)) {
							value = (String) prop.getReadMethod().invoke(list.get(i));
							break;
						}
					}
					row.createCell(j).setCellValue(value);
					j++;
				}
			}

		}
		hssfWorkbook.write(output);
	}

}
