package net.riking.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xlsx2xls {
	private String outFn;
	private File inpFn;

	public Xlsx2xls(File inpFn) {
		this.outFn = inpFn + ".xls";
		this.inpFn = inpFn;
	}

	public static void main(String[] args) throws InvalidFormatException, IOException {
		new Xlsx2xls(new File("C:\\Users\\you.fei\\Desktop\\aaaa.xlsx")).xlsx2xls_progress();
	}

	public void xlsx2xls_progress() throws InvalidFormatException, IOException {
		InputStream in = new FileInputStream(inpFn);
		try {
			XSSFWorkbook wbIn = new XSSFWorkbook(in);
			File outF = new File(outFn);
			if (outF.exists()) {
				outF.delete();
			}

			Workbook wbOut = new HSSFWorkbook();
			int sheetCnt = wbIn.getNumberOfSheets();
			for (int i = 0; i < sheetCnt; i++) {
				Sheet sIn = wbIn.getSheetAt(0);
				Sheet sOut = wbOut.createSheet(sIn.getSheetName());
				Iterator<Row> rowIt = sIn.rowIterator();
				while (rowIt.hasNext()) {
					Row rowIn = rowIt.next();
					Row rowOut = sOut.createRow(rowIn.getRowNum());
					rowOut.setHeight(rowIn.getHeight());
					rowOut.setHeightInPoints(rowIn.getHeightInPoints());
					rowOut.setRowNum(rowIn.getRowNum());
					
					Iterator<Cell> cellIt = rowIn.cellIterator();
					while (cellIt.hasNext()) {
						Cell cellIn = cellIt.next();
						Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), cellIn.getCellType());
						switch (cellIn.getCellType()) {
						case Cell.CELL_TYPE_BLANK:
							break;

						case Cell.CELL_TYPE_BOOLEAN:
							cellOut.setCellValue(cellIn.getBooleanCellValue());
							break;

						case Cell.CELL_TYPE_ERROR:
							cellOut.setCellValue(cellIn.getErrorCellValue());
							break;

						case Cell.CELL_TYPE_FORMULA:
							cellOut.setCellFormula(cellIn.getCellFormula());
							break;

						case Cell.CELL_TYPE_NUMERIC:
							cellOut.setCellValue(cellIn.getNumericCellValue());
							break;

						case Cell.CELL_TYPE_STRING:
							cellOut.setCellValue(cellIn.getStringCellValue());
							break;
						}

						CellStyle styleIn = cellIn.getCellStyle();
						CellStyle styleOut = cellOut.getCellStyle();
						styleOut.setDataFormat(styleIn.getDataFormat());
						styleOut.setAlignment(styleIn.getAlignment());
						styleOut.setBorderBottom(styleIn.getBorderBottom());
						styleOut.setBorderLeft(styleIn.getBorderLeft());
						styleOut.setBorderRight(styleIn.getBorderRight());
						styleOut.setBorderTop(styleIn.getBorderTop());
						styleOut.setBottomBorderColor(styleIn.getBottomBorderColor());
						cellOut.setCellStyle(styleOut);

						cellOut.setCellComment(cellIn.getCellComment());

					}
				}
			}
			OutputStream out = new BufferedOutputStream(new FileOutputStream(outF));
			try {
				wbOut.write(out);
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
}
