package net.riking.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Config;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.DataReport;
import net.riking.service.repo.DataExportRepo;
@Service("ExportAllDateService")
public class ExportAllDateServiceImpl {
	@Autowired
	DataExportRepo dateExportRepo;
	@Autowired
	Config config;
	public HSSFWorkbook exportBigAmountDate(List<BigAmount> bigAmounts,List<AmlSuspicious> amlSuspicious) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
	    // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet 
        if (bigAmounts!=null) {
        	List<DataReport> list = dateExportRepo.findByModular("2");
        	  HSSFSheet sheet = wb.createSheet("BigAmount");  
              // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
              HSSFRow row = sheet.createRow((int) 0); 
              // 第四步，创建单元格，并设置值表头 设置表头居中  

              HSSFCellStyle style = wb.createCellStyle();  
              style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
              HSSFCell cell = row.createCell(0);
              for (int i = 0; i < list.size(); i++) {
                  cell = row.createCell(i);  
            	  DataReport dataReport = list.get(i);
            	  cell.setCellValue(dataReport.getParaName()); 
                  cell.setCellStyle(style);  
              } 
              for (int i = 0; i < bigAmounts.size(); i++) {
            	  row = sheet.createRow((int) i + 1); 
            	  BigAmount bigAmount =bigAmounts.get(i);
            	  for (int j = 0; j < list.size(); j++) {
            		  cell=row.createCell(j);
            		  DataReport dataReport = list.get(j);
            		  cell.setCellValue(BeanUtils.getProperty(bigAmount,dataReport.getNameKey()));
            	  }
				}
			}if (amlSuspicious!=null) {
				List<DataReport> list = dateExportRepo.findByModular("1");
	        	  HSSFSheet sheet = wb.createSheet("AmlSuspiciou");  
	              // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	              HSSFRow row = sheet.createRow((int) 0); 
	              // 第四步，创建单元格，并设置值表头 设置表头居中  

	              HSSFCellStyle style = wb.createCellStyle();  
	              style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	              HSSFCell cell = row.createCell(0);
	              for (int i = 0; i < list.size(); i++) {
	                  cell = row.createCell(i);  
	            	  DataReport dataReport = list.get(i);
	            	  cell.setCellValue(dataReport.getParaName()); 
	                  cell.setCellStyle(style);  
	              } 
	              for (int i = 0; i < amlSuspicious.size(); i++) {
	            	  row = sheet.createRow((int) i + 1); 
	            	  AmlSuspicious amlSuspiciou =amlSuspicious.get(i);
	            	  for (int j = 0; j < list.size(); j++) {
	            		  cell=row.createCell(j);
	            		  DataReport dataReport = list.get(j);
	            		  cell.setCellValue(BeanUtils.getProperty(amlSuspiciou,dataReport.getNameKey()));
	            	  }
					}
			}
             /* List list = CreateSimpleExcelToDisk.getStudent();  
              
              for (int i = 0; i < list.size(); i++)  
              {  
                  row = sheet.createRow((int) i + 1);  
                  Student stu = (Student) list.get(i);  
                  // 第四步，创建单元格，并设置值  
                  row.createCell((short) 0).setCellValue((double) stu.getId());  
                  row.createCell((short) 1).setCellValue(stu.getName());  
                  row.createCell((short) 2).setCellValue((double) stu.getAge());  
                  cell = row.createCell((short) 3);  
                  cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu  
                          .getBirth()));  
              }  */
              // 第六步，将文件存到指定位置  
			//String fliename = null;
      /*  try  
        {  
        	DateFormat df = new SimpleDateFormat("yyyyMMdd");
        	DateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        	Calendar calendar = Calendar.getInstance();
        	String path = config.getSummary();
    		String startDate = df.format(calendar.getTime());
    	    fliename = dfs.format(calendar.getTime());
    		fliename = path + "" + startDate + "\\" + fliename+".xls";
    		File file = new File(fliename);
    		File fileParent = file.getParentFile();
    		if (!fileParent.exists()) {
    			fileParent.mkdirs();
    		}
            FileOutputStream fout = new FileOutputStream(fliename); 
            wb.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }*/
		return wb;  
	}
	
}
