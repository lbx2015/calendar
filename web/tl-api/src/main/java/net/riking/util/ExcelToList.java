package net.riking.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToList {

	public static <T> List<T> readXlsx(InputStream is, String[] fields, Class<T> clazz) throws Exception {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<T> list = new ArrayList<T>();

		BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet sheetAt = xssfWorkbook.getSheetAt(numSheet);
			for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
				XSSFRow row = sheetAt.getRow(i);
				T obj = clazz.newInstance();
				for (int j = 0; j < fields.length; j++) {
					for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
						if (fields[j].equals(propertyDescriptor.getName())) {
							Object o = getValue(row.getCell(j));
							propertyDescriptor.getWriteMethod().invoke(obj, o);
							break;
						}
					}
				}
				list.add(obj);
			}
		}
		is.close();
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> readXls(InputStream is, String[] fields, Class<T> clazz) throws Exception {
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
		List<T> list = new ArrayList<T>();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet sheetAt = xssfWorkbook.getSheetAt(numSheet);
			for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
				HSSFRow row = sheetAt.getRow(i);
				T obj = clazz.newInstance();
				for (int j = 0; j < fields.length; j++) {
					for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
						if (fields[j].equals(propertyDescriptor.getName())) {
							Object o = getValue(row.getCell(j));
							propertyDescriptor.getWriteMethod().invoke(obj, o);
							break;
						}
					}
				}
				list.add(obj);
			}
		}
		is.close();
		return list;
	}

	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow==null) {
			return null;
		}
		if (xssfRow.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return Double.valueOf(xssfRow.getNumericCellValue()).intValue() + "";
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

	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return Double.valueOf(hssfCell.getNumericCellValue()).intValue() + "";
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_STRING) {
			return String.valueOf(hssfCell.getStringCellValue());
		}else if (hssfCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
