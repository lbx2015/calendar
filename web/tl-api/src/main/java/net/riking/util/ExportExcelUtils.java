package net.riking.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExportExcelUtils {
	@SuppressWarnings("unchecked")
	public static <T> void exportByList(List<T> list, OutputStream output, Map<String, String> fields)
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

	private static Object getValue(XSSFCell xssfRow, Class<?> clazz) {
		String stringValue = getXStringValue(xssfRow);
		if (clazz.getSimpleName().equals("Double")) {
			return Double.valueOf(stringValue);
		} else if (clazz.getSimpleName().equals("Long")) {
			return Long.valueOf(stringValue);
		} else if (clazz.getSimpleName().equals("Integer")) {
			return Integer.valueOf(stringValue);
		} else {
			return stringValue;
		}
	}

	private static Object getValue(HSSFCell hssfCell, Class<?> clazz) {
		String stringValue = getHStringValue(hssfCell);
		if (clazz.getSimpleName().equals("Double")) {
			return Double.valueOf(stringValue);
		} else if (clazz.getSimpleName().equals("Long")) {
			return Long.valueOf(stringValue);
		} else if (clazz.getSimpleName().equals("Integer")) {
			return Integer.valueOf(stringValue);
		} else {
			return stringValue;
		}
	}

	private static String getHStringValue(HSSFCell xssfRow) {
		if (xssfRow.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_STRING) {
			return String.valueOf(xssfRow.getStringCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_BLANK) {
			return String.valueOf(xssfRow.getStringCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	private static String getXStringValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(Double.valueOf(xssfRow.getNumericCellValue()).intValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_STRING) {
			return String.valueOf(xssfRow.getStringCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_BLANK) {
			return String.valueOf(xssfRow.getStringCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}
}
