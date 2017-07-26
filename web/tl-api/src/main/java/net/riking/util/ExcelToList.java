package net.riking.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BlackWhiteList;
import net.riking.entity.model.Sdcurrpd;
import net.riking.util.jpinyin.ChineseHelper;
import net.riking.util.jpinyin.PinyinFormat;
import net.riking.util.jpinyin.PinyinHelper;

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

	// 天网红色通缉名单 2007 以上 .xlsx
	public static List<BlackWhiteList> readXlsxBySkyNet(InputStream is) throws Exception {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		BlackWhiteList bwl = null;
		List<BlackWhiteList> list = new ArrayList<BlackWhiteList>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets() - 1; numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			int lastRowNum = 0;
			String idCardVal = "";
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				lastRowNum = isMergedRow(xssfSheet, rowNum, 0);
				if (xssfRow != null) {
					bwl = new BlackWhiteList();
					XSSFCell customerName = xssfRow.getCell(0);
					XSSFCell unitAndPost = xssfRow.getCell(2);
					if (isMergedRegion(xssfSheet, rowNum, 3)) {
						XSSFCell idCard = xssfRow.getCell(3);
						idCardVal = getValue(idCard);
					} else {
						for (; rowNum <= lastRowNum; rowNum++) {
							XSSFCell idCard = xssfSheet.getRow(rowNum).getCell(3);
							idCardVal += (";" + getValue(idCard));
						}
					}

					XSSFCell filingUnit = xssfRow.getCell(7);
					XSSFCell charge = xssfRow.getCell(8);
					XSSFCell wantedNum = xssfRow.getCell(10);
					if (customerName == null || unitAndPost == null || idCardVal == null || filingUnit == null
							|| charge == null || wantedNum == null) {

					} else {
						bwl.setZwmc(getValue(customerName));
						bwl.setYgzdw(getValue(unitAndPost));
						bwl.setZjhm(idCardVal);
						bwl.setLadw(getValue(filingUnit));
						bwl.setSjmx(getValue(charge));
						bwl.setSah(getValue(wantedNum));
						if (ChineseHelper.containsChinese(bwl.getZwmc())) {
							bwl.setZwmp(
									PinyinHelper.convertToPinyinString(bwl.getZwmc(), "'", PinyinFormat.WITHOUT_TONE));
						}
						list.add(bwl);
						rowNum = lastRowNum;
					}
				}
			}
		}
		is.close();
		return list;
	}

	// 天网红色通缉名单 2007 以前 .xls
	public static List<BlackWhiteList> readXlsBySkyNet(InputStream is) throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		BlackWhiteList bwl = null;
		List<BlackWhiteList> list = new ArrayList<BlackWhiteList>();
		// Read the Sheet
		int lastRowNum=0;
		String idCardVal ="";
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				lastRowNum = isMergedRow(hssfSheet,rowNum,0);
				if (hssfRow != null) {
					bwl = new BlackWhiteList();
					HSSFCell customerName = hssfRow.getCell(0);
					HSSFCell unitAndPost = hssfRow.getCell(2);
					if(isMergedRegion(hssfSheet,rowNum,3)){
						HSSFCell idCard = hssfRow.getCell(3);
						idCardVal = getValue(idCard);
					}else{
						for(;rowNum<=lastRowNum;rowNum++){
							HSSFCell idCard = hssfSheet.getRow(rowNum).getCell(3);
							idCardVal+=(";"+getValue(idCard));
						}
					}
					HSSFCell filingUnit = hssfRow.getCell(7);
					HSSFCell charge = hssfRow.getCell(8);
					HSSFCell wantedNum = hssfRow.getCell(10);
					if (customerName == null || unitAndPost == null || idCardVal == null || filingUnit == null
							|| charge == null || wantedNum == null) {

					} else {
						bwl.setZwmc(getValue(customerName));
						bwl.setYgzdw(getValue(unitAndPost));
						bwl.setZjhm(idCardVal);
						bwl.setLadw(getValue(filingUnit));
						bwl.setSjmx(getValue(charge));
						bwl.setSah(getValue(wantedNum));
						if (ChineseHelper.containsChinese(bwl.getZwmc())) {
							bwl.setZwmp(
									PinyinHelper.convertToPinyinString(bwl.getZwmc(), "'", PinyinFormat.WITHOUT_TONE));
						}
						list.add(bwl);
						rowNum = lastRowNum;
					}
				}
			}
		}
		is.close();
		return list;
	}

	// 判断合并单元格
	private static boolean isMergedRegion(Sheet sheet, int row, int column) {

		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {

			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	// 判断合并行
	private static int isMergedRow(Sheet sheet, int row, int column) {

		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column == firstColumn && column == lastColumn) {
					return lastRow;
				}
			}
		}
		return row;
	}
	
	public static List<AmlSuspicious> readsusXlsx(InputStream is,String fileName) throws Exception {
		List<AmlSuspicious> list =new ArrayList<AmlSuspicious>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					AmlSuspicious amlSuspiciou = new AmlSuspicious();
					amlSuspiciou.setRicd(getValue(xssfRow.getCell(1)));
					amlSuspiciou.setRpnc(getValue(xssfRow.getCell(2)));
					amlSuspiciou.setFinc(getValue(xssfRow.getCell(3)));
					amlSuspiciou.setRlfc(getValue(xssfRow.getCell(4)));
					amlSuspiciou.setDetr(getValue(xssfRow.getCell(5)));
					amlSuspiciou.setRpnm(getValue(xssfRow.getCell(6)));
					amlSuspiciou.setCsnm1(getValue(xssfRow.getCell(7)));
					amlSuspiciou.setSevc(getValue(xssfRow.getCell(8)));
					amlSuspiciou.setSenm(getValue(xssfRow.getCell(9)));
					amlSuspiciou.setSetp(getValue(xssfRow.getCell(10)));
					amlSuspiciou.setOitp1(getValue(xssfRow.getCell(11)));
					amlSuspiciou.setSeid(getValue(xssfRow.getCell(12)));
					amlSuspiciou.setStnt(getValue(xssfRow.getCell(13)));
					amlSuspiciou.setSctl(getValue(xssfRow.getCell(14)));
					amlSuspiciou.setSear(getValue(xssfRow.getCell(15)));
					amlSuspiciou.setSeei(getValue(xssfRow.getCell(16)));
					amlSuspiciou.setSrnm(getValue(xssfRow.getCell(17)));
					amlSuspiciou.setSrit(getValue(xssfRow.getCell(18)));
					amlSuspiciou.setOrit(getValue(xssfRow.getCell(19)));
					amlSuspiciou.setSrid(getValue(xssfRow.getCell(20)));
					amlSuspiciou.setScnm(getValue(xssfRow.getCell(21)));
					amlSuspiciou.setScit(getValue(xssfRow.getCell(22)));
					amlSuspiciou.setOcit(getValue(xssfRow.getCell(23)));
					amlSuspiciou.setScid(getValue(xssfRow.getCell(24)));
					amlSuspiciou.setCtnm(getValue(xssfRow.getCell(25)));
					amlSuspiciou.setCitp(getValue(xssfRow.getCell(26)));
					amlSuspiciou.setOitp2(getValue(xssfRow.getCell(27)));
					amlSuspiciou.setCtid(getValue(xssfRow.getCell(28)));
					amlSuspiciou.setCsnm2(getValue(xssfRow.getCell(29)));
					amlSuspiciou.setCatp(getValue(xssfRow.getCell(30)));
					amlSuspiciou.setCtac(getValue(xssfRow.getCell(31)));
					if (xssfRow.getCell(32)==null) {
						amlSuspiciou.setOatm(null);
					}else{
						amlSuspiciou.setOatm(xssfRow.getCell(32).getDateCellValue());
					}if (xssfRow.getCell(33)==null) {
						amlSuspiciou.setCatm(null);
					}else{
						amlSuspiciou.setCatm(xssfRow.getCell(33).getDateCellValue());
					}
					amlSuspiciou.setCbct(getValue(xssfRow.getCell(34)));
					amlSuspiciou.setOcbt(getValue(xssfRow.getCell(35)));
					amlSuspiciou.setCbcn(getValue(xssfRow.getCell(36)));
					amlSuspiciou.setTbnm(getValue(xssfRow.getCell(37)));
					amlSuspiciou.setTbit(getValue(xssfRow.getCell(38)));
					amlSuspiciou.setOitp3(getValue(xssfRow.getCell(39)));
					amlSuspiciou.setTbid(getValue(xssfRow.getCell(40)));
					amlSuspiciou.setTbnt(getValue(xssfRow.getCell(41)));
					if (xssfRow.getCell(42)==null) {
						amlSuspiciou.setTstm(null);
					}else{
						amlSuspiciou.setTstm(xssfRow.getCell(42).getDateCellValue());
					}
					amlSuspiciou.setTrcd(getValue(xssfRow.getCell(43)));
					amlSuspiciou.setTicd(getValue(xssfRow.getCell(44)));
					amlSuspiciou.setRpmt(getValue(xssfRow.getCell(45)));
					amlSuspiciou.setRpmn(getValue(xssfRow.getCell(46)));
					amlSuspiciou.setTstp(getValue(xssfRow.getCell(47)));
					amlSuspiciou.setOctt(getValue(xssfRow.getCell(48)));
					amlSuspiciou.setOoct(getValue(xssfRow.getCell(49)));
					amlSuspiciou.setOcec(getValue(xssfRow.getCell(50)));
					amlSuspiciou.setBptc(getValue(xssfRow.getCell(51)));
					amlSuspiciou.setTsct(getValue(xssfRow.getCell(52)));
					amlSuspiciou.setTsdr(getValue(xssfRow.getCell(53)));
					amlSuspiciou.setCrsp(getValue(xssfRow.getCell(54)));
					amlSuspiciou.setCrtp(getValue(xssfRow.getCell(55)));
					if (xssfRow.getCell(56)==null) {
						amlSuspiciou.setCrat(null);
					}else{
						amlSuspiciou.setCrat(new BigDecimal(getValue(xssfRow.getCell(56))));
					}
					amlSuspiciou.setCfin(getValue(xssfRow.getCell(57)));
					amlSuspiciou.setCfct(getValue(xssfRow.getCell(58)));
					amlSuspiciou.setCfic(getValue(xssfRow.getCell(59)));
					amlSuspiciou.setCfrc(getValue(xssfRow.getCell(60)));
					amlSuspiciou.setTcnm(getValue(xssfRow.getCell(61)));
					amlSuspiciou.setTcit(getValue(xssfRow.getCell(62)));
					amlSuspiciou.setOitp4(getValue(xssfRow.getCell(63)));
					amlSuspiciou.setTcid(getValue(xssfRow.getCell(64)));
					amlSuspiciou.setTcat(getValue(xssfRow.getCell(65)));
					amlSuspiciou.setTcac(getValue(xssfRow.getCell(66)));
					amlSuspiciou.setRotf1(getValue(xssfRow.getCell(67)));
					amlSuspiciou.setRotf2(getValue(xssfRow.getCell(68)));
					amlSuspiciou.setDorp(getValue(xssfRow.getCell(69)));
					amlSuspiciou.setOdrp(getValue(xssfRow.getCell(70)));
					amlSuspiciou.setTptr(getValue(xssfRow.getCell(71)));
					amlSuspiciou.setOtpr(getValue(xssfRow.getCell(72)));
					amlSuspiciou.setStcb(getValue(xssfRow.getCell(73)));
					amlSuspiciou.setAosp(getValue(xssfRow.getCell(74)));
					amlSuspiciou.setTosc(getValue(xssfRow.getCell(75)));
					amlSuspiciou.setStcr(getValue(xssfRow.getCell(76)));
					list.add(amlSuspiciou);
				}
			}
		}
		is.close();
		return list;
	}

	public static List<AmlSuspicious> readsusXls(InputStream is,String fileName) throws Exception {
		List<AmlSuspicious> list =new ArrayList<AmlSuspicious>();
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					AmlSuspicious amlSuspiciou = new AmlSuspicious();
					amlSuspiciou.setRicd(getValue(xssfRow.getCell(1)));
					amlSuspiciou.setRpnc(getValue(xssfRow.getCell(2)));
					amlSuspiciou.setFinc(getValue(xssfRow.getCell(3)));
					amlSuspiciou.setRlfc(getValue(xssfRow.getCell(4)));
					amlSuspiciou.setDetr(getValue(xssfRow.getCell(5)));
					amlSuspiciou.setRpnm(getValue(xssfRow.getCell(6)));
					amlSuspiciou.setCsnm1(getValue(xssfRow.getCell(7)));
					amlSuspiciou.setSevc(getValue(xssfRow.getCell(8)));
					amlSuspiciou.setSenm(getValue(xssfRow.getCell(9)));
					amlSuspiciou.setSetp(getValue(xssfRow.getCell(10)));
					amlSuspiciou.setOitp1(getValue(xssfRow.getCell(11)));
					amlSuspiciou.setSeid(getValue(xssfRow.getCell(12)));
					amlSuspiciou.setStnt(getValue(xssfRow.getCell(13)));
					amlSuspiciou.setSctl(getValue(xssfRow.getCell(14)));
					amlSuspiciou.setSear(getValue(xssfRow.getCell(15)));
					amlSuspiciou.setSeei(getValue(xssfRow.getCell(16)));
					amlSuspiciou.setSrnm(getValue(xssfRow.getCell(17)));
					amlSuspiciou.setSrit(getValue(xssfRow.getCell(18)));
					amlSuspiciou.setOrit(getValue(xssfRow.getCell(19)));
					amlSuspiciou.setSrid(getValue(xssfRow.getCell(20)));
					amlSuspiciou.setScnm(getValue(xssfRow.getCell(21)));
					amlSuspiciou.setScit(getValue(xssfRow.getCell(22)));
					amlSuspiciou.setOcit(getValue(xssfRow.getCell(23)));
					amlSuspiciou.setScid(getValue(xssfRow.getCell(24)));
					amlSuspiciou.setCtnm(getValue(xssfRow.getCell(25)));
					amlSuspiciou.setCitp(getValue(xssfRow.getCell(26)));
					amlSuspiciou.setOitp2(getValue(xssfRow.getCell(27)));
					amlSuspiciou.setCtid(getValue(xssfRow.getCell(28)));
					amlSuspiciou.setCsnm2(getValue(xssfRow.getCell(29)));
					amlSuspiciou.setCatp(getValue(xssfRow.getCell(30)));
					amlSuspiciou.setCtac(getValue(xssfRow.getCell(31)));
					amlSuspiciou.setOatm(xssfRow.getCell(32).getDateCellValue());
					amlSuspiciou.setCatm(xssfRow.getCell(33).getDateCellValue());
					amlSuspiciou.setCbct(getValue(xssfRow.getCell(34)));
					amlSuspiciou.setOcbt(getValue(xssfRow.getCell(35)));
					amlSuspiciou.setCbcn(getValue(xssfRow.getCell(36)));
					amlSuspiciou.setTbnm(getValue(xssfRow.getCell(37)));
					amlSuspiciou.setTbit(getValue(xssfRow.getCell(38)));
					amlSuspiciou.setOitp3(getValue(xssfRow.getCell(39)));
					amlSuspiciou.setTbid(getValue(xssfRow.getCell(40)));
					amlSuspiciou.setTbnt(getValue(xssfRow.getCell(41)));
					amlSuspiciou.setTstm(xssfRow.getCell(32).getDateCellValue());
					amlSuspiciou.setTrcd(getValue(xssfRow.getCell(43)));
					amlSuspiciou.setTicd(getValue(xssfRow.getCell(44)));
					amlSuspiciou.setRpmt(getValue(xssfRow.getCell(45)));
					amlSuspiciou.setRpmn(getValue(xssfRow.getCell(46)));
					amlSuspiciou.setTstp(getValue(xssfRow.getCell(47)));
					amlSuspiciou.setOctt(getValue(xssfRow.getCell(48)));
					amlSuspiciou.setOoct(getValue(xssfRow.getCell(49)));
					amlSuspiciou.setOcec(getValue(xssfRow.getCell(50)));
					amlSuspiciou.setBptc(getValue(xssfRow.getCell(51)));
					amlSuspiciou.setTsct(getValue(xssfRow.getCell(52)));
					amlSuspiciou.setTsdr(getValue(xssfRow.getCell(53)));
					amlSuspiciou.setCrsp(getValue(xssfRow.getCell(54)));
					amlSuspiciou.setCrtp(getValue(xssfRow.getCell(55)));
					amlSuspiciou.setCrat(new BigDecimal(getValue(xssfRow.getCell(56))));
					amlSuspiciou.setCfin(getValue(xssfRow.getCell(57)));
					amlSuspiciou.setCfct(getValue(xssfRow.getCell(58)));
					amlSuspiciou.setCfic(getValue(xssfRow.getCell(59)));
					amlSuspiciou.setCfrc(getValue(xssfRow.getCell(60)));
					amlSuspiciou.setTcnm(getValue(xssfRow.getCell(61)));
					amlSuspiciou.setTcit(getValue(xssfRow.getCell(62)));
					amlSuspiciou.setOitp4(getValue(xssfRow.getCell(63)));
					amlSuspiciou.setTcid(getValue(xssfRow.getCell(64)));
					amlSuspiciou.setTcat(getValue(xssfRow.getCell(65)));
					amlSuspiciou.setTcac(getValue(xssfRow.getCell(66)));
					amlSuspiciou.setRotf1(getValue(xssfRow.getCell(67)));
					amlSuspiciou.setRotf2(getValue(xssfRow.getCell(68)));
					amlSuspiciou.setDorp(getValue(xssfRow.getCell(69)));
					amlSuspiciou.setOdrp(getValue(xssfRow.getCell(70)));
					amlSuspiciou.setTptr(getValue(xssfRow.getCell(71)));
					amlSuspiciou.setOtpr(getValue(xssfRow.getCell(72)));
					amlSuspiciou.setStcb(getValue(xssfRow.getCell(73)));
					amlSuspiciou.setAosp(getValue(xssfRow.getCell(74)));
					amlSuspiciou.setTosc(getValue(xssfRow.getCell(75)));
					amlSuspiciou.setStcr(getValue(xssfRow.getCell(76)));
					list.add(amlSuspiciou);
				}
			}
		}
		is.close();
		return list;
	}
	
	public static List<Sdcurrpd> readSdXlsx(InputStream is,String fileName) throws Exception {
		String date;
		if (fileName.length() == 22) {
			date = fileName.substring(11, 15) + "-0" + fileName.substring(16, 17) + "-" + fileName.substring(18, 20);
		} else {
			date = fileName.substring(11, 15) + "-" + fileName.substring(16, 18) + "-" + fileName.substring(19, 21);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<Sdcurrpd> list = new ArrayList<Sdcurrpd>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				if (rowNum < 6 || rowNum > 52) {
					continue;
				}
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					Sdcurrpd sd1 = new Sdcurrpd();
					XSSFCell ccy = xssfRow.getCell(1);
					XSSFCell rate = xssfRow.getCell(4);
					sd1.setCurrency(getValue(ccy));
					sd1.setRate(new BigDecimal(rate.toString()));
					sd1.setRateDate(sdf.parse(date));
					sd1.setMethod("M");
					list.add(sd1);
					Sdcurrpd sd2 = new Sdcurrpd();
					XSSFCell ccy2 = xssfRow.getCell(5);
					XSSFCell rate2 = xssfRow.getCell(8);
					sd2.setCurrency(getValue(ccy2));
					sd2.setRate(new BigDecimal(rate2.toString()));
					sd2.setRateDate(sdf.parse(date));
					sd2.setMethod("M");
					list.add(sd2);
				}
			}
		}
		is.close();
		return list;
	}

	public static List<Sdcurrpd> readSdXls(InputStream is,String fileName) throws Exception {
		String date;
		if (fileName.length() == 22) {
			date = fileName.substring(11, 15) + "-0" + fileName.substring(16, 17) + "-" + fileName.substring(18, 20);
		} else {
			date = fileName.substring(11, 15) + "-" + fileName.substring(16, 18) + "-" + fileName.substring(19, 21);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
		List<Sdcurrpd> list = new ArrayList<Sdcurrpd>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				if (rowNum < 6 || rowNum > 52) {
					continue;
				}
				HSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					Sdcurrpd sd1 = new Sdcurrpd();
					HSSFCell ccy = xssfRow.getCell(1);
					HSSFCell rate = xssfRow.getCell(4);
					sd1.setCurrency(getValue(ccy));
					sd1.setRate(new BigDecimal(rate.toString()));
					sd1.setRateDate(sdf.parse(date));
					sd1.setMethod("M");
					list.add(sd1);
					Sdcurrpd sd2 = new Sdcurrpd();
					HSSFCell ccy2 = xssfRow.getCell(5);
					HSSFCell rate2 = xssfRow.getCell(8);
					sd2.setCurrency(getValue(ccy2));
					sd2.setRate(new BigDecimal(rate2.toString()));
					sd2.setRateDate(sdf.parse(date));
					sd2.setMethod("M");
					list.add(sd2);
				}
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
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
