package net.riking.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.riking.entity.report.CrimeReport;
import net.riking.entity.report.CustomerReport;
import net.riking.entity.report.Report;

public class ExcelUtil {
	
	//复制单元格样式
	public static void copyCellStyle(XSSFCellStyle fromStyle, XSSFCellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		// 边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		toStyle.setFont(fromStyle.getFont());
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());

	}
	/**
	 * Sheet复制
	 * 
	 * @param fromSheet
	 * @param toSheet
	 * @param copyValueFlag
	 */
	public static void copySheet(XSSFWorkbook wb, XSSFSheet fromSheet, XSSFSheet toSheet, boolean copyValueFlag) {
		// 合并区域处理
		mergerRegion(fromSheet, toSheet);
		for (Iterator<?> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
			XSSFRow tmpRow = (XSSFRow) rowIt.next();
			XSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());
			// 行复制
			copyRow(wb, tmpRow, newRow, copyValueFlag);
		}
	}
	/**
	 * 行复制功能
	 * 
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(XSSFWorkbook wb, XSSFRow fromRow, XSSFRow toRow, boolean copyValueFlag) {
		toRow.setHeight(fromRow.getHeight());
		toRow.setRowStyle(fromRow.getRowStyle());
		for (Iterator<?> cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			XSSFCell tmpCell = (XSSFCell) cellIt.next();
			XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb, tmpCell, newCell, copyValueFlag);
		}
	}
	/**
	 * 复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param sheetCreat
	 *            新创建sheet
	 * @param sheet
	 *            原有的sheet
	 */
	public static void mergerRegion(XSSFSheet fromSheet, XSSFSheet toSheet) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress region = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(region);
		}
	}
	public static void mergerRegion(XSSFSheet fromSheet, int fromSheetLastRow) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress region = fromSheet.getMergedRegion(i);
			region.setFirstRow(region.getFirstRow() + fromSheetLastRow);
			region.setLastRow(region.getLastRow() + fromSheetLastRow);
			fromSheet.addMergedRegion(region);
		}
	}
	/**
	 * 复制单元格
	 * 
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(XSSFWorkbook wb, XSSFCell srcCell, XSSFCell distCell, boolean copyValueFlag) {
		XSSFCellStyle newstyle = wb.createCellStyle();
		copyCellStyle(srcCell.getCellStyle(), newstyle);
		// 样式
		distCell.setCellStyle(newstyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == XSSFCell.CELL_TYPE_NUMERIC) {
				// if (XSSFDateUtil.isCellDateFormatted(srcCell)) {
				// distCell.setCellValue(srcCell.getDateCellValue());
				// } else {
				distCell.setCellValue(srcCell.getNumericCellValue());
				// }
			} else if (srcCellType == XSSFCell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == XSSFCell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}
	// 复制  sheet {笔数（按客户）}的前四行模板
	private static void copyModelRows(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, String khmc, String khbh) {
		//第一行
		copyRow(xssfWorkbook, sheet.getRow(1), sheet.createRow(sheet.getLastRowNum() + 1), true);
		sheet.getRow(sheet.getLastRowNum()).getCell(0).setCellValue("客户名称：" + khmc);
		sheet.getRow(sheet.getLastRowNum()).getCell(4).setCellValue("客户号： " + khbh);
		//第一行需要合并单元格
		CellRangeAddress region = new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 3);
		sheet.addMergedRegion(region);
		CellRangeAddress region1 = new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 4, 5);
		sheet.addMergedRegion(region1);
		//第二行
		copyRow(xssfWorkbook, sheet.getRow(2), sheet.createRow(sheet.getLastRowNum() + 1), true);
		CellRangeAddress region3 = new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 1, 5);
		sheet.addMergedRegion(region3);
		//第三行
		copyRow(xssfWorkbook, sheet.getRow(3), sheet.createRow(sheet.getLastRowNum() + 1), true);
		//第四行
		copyRow(xssfWorkbook, sheet.getRow(4), sheet.createRow(sheet.getLastRowNum() + 1), false);
	}
	//复制sheet最后一行的样式 并  向下 添加一行
	private static void addModelRow(XSSFWorkbook xssfWorkbook, XSSFSheet sheet) {
		copyRow(xssfWorkbook, sheet.getRow(sheet.getLastRowNum()), sheet.createRow(sheet.getLastRowNum() + 1), false);
	}

	// 笔数（汇总）
	public static void fillDataHz(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, Report report) {
		//所属行
		XSSFRow row1 = sheet.getRow(1);
		row1.getCell(0).setCellValue(row1.getCell(0).getStringCellValue() + report.getBranchName());
		//报送日期
		XSSFRow row2 = sheet.getRow(2);
		row2.getCell(1).setCellValue(report.getReportDate());
		//大额交易份数（总份数）
		XSSFRow row4 = sheet.getRow(4);
		row4.getCell(3).setCellValue(report.getBigAmountTotal());
		row4.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		//补正份数
		XSSFRow row5 = sheet.getRow(5);
		row5.getCell(3).setCellValue(report.getBigAmountBZTotal());
		row5.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		//可疑交易份数（总份数）
		XSSFRow row6 = sheet.getRow(6);
		row6.getCell(3).setCellValue(report.getSuspiciousTotal());
		row6.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

		// XSSFRow row7 = sheet.getRow(7);
		// row7.getCell(3).setCellValue(report.getSuspiciousBZXTotal());
		// row7.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		
		//1：报中心
		XSSFRow row8 = sheet.getRow(8);
		row8.getCell(3).setCellValue(report.getSuspiciousBZXTotal());
		row8.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		// 2：报中心和人民银行当地分支机构
		XSSFRow row9 = sheet.getRow(9);
		row9.getCell(3).setCellValue(report.getSuspiciousBZXHDDRMYHFZJGTotal());
		row9.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		//3：报中心和当地公安
		XSSFRow row10 = sheet.getRow(10);
		row10.getCell(3).setCellValue(report.getSuspiciousBZXHDDGATotal());
		row10.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		//4：报中心、人民银行当地分支机构和当地公安
		XSSFRow row11 = sheet.getRow(11);
		row11.getCell(3).setCellValue(report.getSuspiciousBZXHDDRMYHFZJGHDDGATotal());
		row11.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

	}

	// 笔数（按客户）
	public static void fillDataKh(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, Report report) {
		//报送日期
		XSSFRow row2 = sheet.getRow(2);
		row2.getCell(1).setCellValue(report.getReportDate());
		
		Map<String, List<CustomerReport>> bigMap = report.getBigAmountTotalMap();
		Map<String, List<CustomerReport>> suspMap = report.getSuspiciousTotalMap();
		boolean isFirst = true;//是否是第一次添加数据
		//遍历大额  同时 找到该客户对应的 可疑
		if(bigMap.size()>0){
			for (String key : bigMap.keySet()) {
				if (isFirst) {//第一次添加数据  不要做 CopyModelRow
					XSSFRow row1 = sheet.getRow(1);
					row1.getCell(0).setCellValue("客户名称："+ bigMap.get(key).get(0).getName());
					row1.getCell(4).setCellValue("客户号： "+key);
					isFirst = false;
				} else {//客户换了，需要复制前四行的客户模板 CopyModelRow
					copyModelRows(xssfWorkbook, sheet, bigMap.get(key).get(0).getName(), key);
				}
				List<CustomerReport> list = bigMap.get(key);
				List<CustomerReport> list2 = suspMap.get(key);
				int len = list2!=null&&list2.size() > list.size() ? list2.size() : list.size();//比较大额 和 可疑 谁的数量多，取多的
				for (int i = 0; i < len; i++) {
					if (i > 0) {//数据不止一条 ， 向下添加一行
						addModelRow(xssfWorkbook, sheet);
					}
					XSSFRow row = sheet.getRow(sheet.getLastRowNum());
					if (list!=null&&list.size()>i&&list.get(i) != null) {//fill 大额的数据
						row.getCell(0).setCellValue(list.get(i).getTotal());
						row.getCell(1).setCellValue(list.get(i).getBz());
						row.getCell(2).setCellValue(list.get(i).getJe().toString());
					}
					if (list2!=null&&list2.size()>i&&list2.get(i) != null) {// fill 可疑的数据
						row.getCell(3).setCellValue(list2.get(i).getTotal());
						row.getCell(4).setCellValue(list2.get(i).getBz());
						row.getCell(5).setCellValue(list2.get(i).getJe().toString());
					}
				}
			}
		}
		if(suspMap.size()>0){
			//遍历只有可疑 交易 的客户
			for (String key : suspMap.keySet()) {
				if(!bigMap.containsKey(key)){
					copyModelRows(xssfWorkbook, sheet, suspMap.get(key).get(0).getName(), key);//客户换了，需要复制前四行的客户模板 CopyModelRow
					List<CustomerReport> list2 = suspMap.get(key);
					for (int i = 0; i < list2.size(); i++) {
						if (i > 0) {//数据不止一条 ， 向下添加一行
							addModelRow(xssfWorkbook, sheet);
						}
						XSSFRow row = sheet.getRow(sheet.getLastRowNum());
						if (list2!=null&&list2.size()>i&&list2.get(i) != null) {// fill 可疑的数据
							row.getCell(3).setCellValue(list2.get(i).getTotal());
							row.getCell(4).setCellValue(list2.get(i).getBz());
							row.getCell(5).setCellValue(list2.get(i).getJe().toString());
						}
					}
				}
			}
		}
	
	}
	// 大额交易特征代码
	public static void fillDataDetzdm(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, Report report) {
		//报送日期
		XSSFRow row1 = sheet.getRow(1);
		row1.getCell(1).setCellValue(report.getReportDate());
		//0501
		XSSFRow row4 = sheet.getRow(3);
		row4.getCell(1).setCellValue(report.getBigAmount0501Total()==null? 0 :report.getBigAmount0501Total());
		//0502
		XSSFRow row6 = sheet.getRow(5);
		row6.getCell(1).setCellValue(report.getBigAmount0502Total()==null? 0 :report.getBigAmount0502Total());
		//0503
		XSSFRow row8 = sheet.getRow(7);
		row8.getCell(1).setCellValue(report.getBigAmount0503Total()==null? 0 :report.getBigAmount0503Total());
		//0504
		XSSFRow row10 = sheet.getRow(9);
		row10.getCell(1).setCellValue(report.getBigAmount0504Total()==null? 0 :report.getBigAmount0504Total());
	}
	// 疑似涉罪类型
	public static void fillDataSzlx(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, Report report) {
		//报送日期
		XSSFRow row1 = sheet.getRow(1);
		row1.getCell(1).setCellValue(report.getReportDate());
		//遍历并 fill所有的数据
		List<CrimeReport> list = report.getCrimeReportList();
		for (int i = 0; i<list.size(); i++) {
			if(i>0){//数据不止一条 ， 向下添加一行
				addModelRow(xssfWorkbook, sheet);
			}
			XSSFRow row = sheet.getRow(sheet.getLastRowNum());
			if(list.get(i)!=null){//fill 数据
				row.getCell(0).setCellValue(list.get(i).getCrime());
				row.getCell(1).setCellValue(list.get(i).getReportTotal());
				row.getCell(2).setCellValue(list.get(i).getReportedTotal());
				row.getCell(3).setCellValue(list.get(i).getTotal());
			}
		}
	}
}
