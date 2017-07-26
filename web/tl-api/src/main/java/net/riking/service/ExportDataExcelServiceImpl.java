package net.riking.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.entity.model.QueryResult;
import net.riking.entity.report.CrimeReport;
import net.riking.entity.report.CustomerReport;
import net.riking.entity.report.Report;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;
import net.riking.util.ExcelUtil;

@Service("exportDataExcelServiceImpl")
public class ExportDataExcelServiceImpl {

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;

	@Autowired
	ModelPropdictRepo modelPropdictRepo;

	public void fillData(InputStream is, OutputStream os, Report report) {
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			XSSFSheet sheet1 = xssfWorkbook.getSheet("笔数（汇总）");
			ExcelUtil.fillDataHz(xssfWorkbook, sheet1, report);

			XSSFSheet sheet2 = xssfWorkbook.getSheet("笔数（按客户）");
			ExcelUtil.fillDataKh(xssfWorkbook, sheet2, report);

			XSSFSheet sheet3 = xssfWorkbook.getSheet("大额交易特征代码");
			ExcelUtil.fillDataDetzdm(xssfWorkbook, sheet3, report);

			XSSFSheet sheet4 = xssfWorkbook.getSheet("疑似涉罪类型");
			ExcelUtil.fillDataSzlx(xssfWorkbook, sheet4, report);

			xssfWorkbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fillData(InputStream is, OutputStream os, Date start, Date end, String branchName, Set<String> jgbm) {

		Report report = getReport(start, end, branchName, jgbm);
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			XSSFSheet sheet1 = xssfWorkbook.getSheet("笔数（汇总）");
			ExcelUtil.fillDataHz(xssfWorkbook, sheet1, report);

			XSSFSheet sheet2 = xssfWorkbook.getSheet("笔数（按客户）");
			ExcelUtil.fillDataKh(xssfWorkbook, sheet2, report);

			XSSFSheet sheet3 = xssfWorkbook.getSheet("大额交易特征代码");
			ExcelUtil.fillDataDetzdm(xssfWorkbook, sheet3, report);

			XSSFSheet sheet4 = xssfWorkbook.getSheet("疑似涉罪类型");
			ExcelUtil.fillDataSzlx(xssfWorkbook, sheet4, report);

			xssfWorkbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Report getReport(Date start, Date end, String breanchName, Set<String> jgbm) {
		Report report = new Report();
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
		report.setReportDate(format.format(start) + "--" + format.format(end));
		report.setBranchName(breanchName);
		// 大额总数
		Long resultBig = bigAmountRepo.findAllReport(start, end, null, jgbm);
		report.setBigAmountTotal(resultBig);
		// 补正报文
		Long resultBigBz = bigAmountRepo.findAllReport(start, end, "bzhz", jgbm);
		report.setBigAmountBZTotal(resultBigBz);
		// 可疑总数
		Long resultSus = amlSuspiciousRepo.findAllReport(start, end, null, jgbm);
		report.setSuspiciousTotal(resultSus);
		// 可疑报中心
		Long resultSusBzx = amlSuspiciousRepo.findAllReport(start, end, "1", jgbm);
		report.setSuspiciousBZXTotal(resultSusBzx);
		// 可疑报中心和人民银行当地分支机构
		Long resultSusBzxYhfzjg = amlSuspiciousRepo.findAllReport(start, end, "2", jgbm);
		report.setSuspiciousBZXHDDRMYHFZJGTotal(resultSusBzxYhfzjg);
		// 可疑报中心和当地公安
		Long resultSusBzxGa = amlSuspiciousRepo.findAllReport(start, end, "3", jgbm);
		report.setSuspiciousBZXHDDGATotal(resultSusBzxGa);
		// 可疑报中心、人民银行当地分支机构和当地公安
		Long resultSusBzxRmyhGa = amlSuspiciousRepo.findAllReport(start, end, "4", jgbm);
		report.setSuspiciousBZXHDDRMYHFZJGHDDGATotal(resultSusBzxRmyhGa);

		Map<String, List<CustomerReport>> bigAmountTotalMap = new HashMap<>();

		List<CustomerReport> bigList = bigAmountRepo.findByCtnmAndCsnm(null, null, start, end, jgbm);
		for (int i = 0; i < bigList.size(); i++) {
			List<CustomerReport> customerlist = bigAmountTotalMap.get(bigList.get(i).getNumber());
			if (customerlist == null) {
				List<CustomerReport> customerReports = new ArrayList<>();
				customerReports.add(bigList.get(i));
				bigAmountTotalMap.put(bigList.get(i).getNumber(), customerReports);
			} else {
				customerlist.add(bigList.get(i));
			}
		}
		report.setBigAmountTotalMap(bigAmountTotalMap);

		Map<String, List<CustomerReport>> suspiciousTotalMap = new HashMap<>();
		List<CustomerReport> supList = amlSuspiciousRepo.findBySenmAndCsnm(null, null, start, end, jgbm);
		for (int i = 0; i < supList.size(); i++) {
			List<CustomerReport> customerlist = suspiciousTotalMap.get(supList.get(i).getNumber());
			if (customerlist == null) {
				List<CustomerReport> customerReports = new ArrayList<>();
				customerReports.add(supList.get(i));
				suspiciousTotalMap.put(supList.get(i).getNumber(), customerReports);
			} else {
				customerlist.add(supList.get(i));
			}
		}

		report.setSuspiciousTotalMap(suspiciousTotalMap);
		List<QueryResult> bigAmountRepoReportByCrcd = bigAmountRepo.findReportByCrcd(start, end, jgbm);
		for (QueryResult result : bigAmountRepoReportByCrcd) {
			if (result.getName().equals("0501")) {
				report.setBigAmount0501Total(result.getValue());
			}
			if (result.getName().equals("0502")) {
				report.setBigAmount0502Total(result.getValue());
			}
			if (result.getName().equals("0503")) {
				report.setBigAmount0503Total(result.getValue());
			}
			if (result.getName().equals("0504")) {
				report.setBigAmount0504Total(result.getValue());
			}
		}
		List<CrimeReport> crimeReportList = new ArrayList<>();
		HashSet<String> set = new HashSet<String>();
		set.add("SZLXDM");
		List<ModelPropDict> crimeList = modelPropdictRepo.getDatas("T_AML_CRIME_TYPE", set);
		List<List<String>> lists = amlSuspiciousRepo.findReportByTosc(start, end, null, jgbm);
		for (ModelPropDict dict : crimeList) {
			CrimeReport crimeReport = new CrimeReport();
			String tosc = dict.getKe();
			crimeReport.setCrime(tosc);
			Long reportedTotal = 0L;
			Long reportTotal = 0L;
			Long total = 0L;
			for (int i = 0; i < lists.get(0).size(); i++) {
				if (null != lists.get(0).get(i) &&lists.get(0).get(i).contains(tosc)) {
					reportedTotal++;
				}
			}
			for (int i = 0; i < lists.get(1).size(); i++) {
				if (null != lists.get(1).get(i) && lists.get(1).get(i).contains(tosc)) {
					reportTotal++;
				}
			}
			for (int i = 0; i < lists.get(2).size(); i++) {
				if (null != lists.get(2).get(i) &&lists.get(2).get(i).contains(tosc)) {
					total++;
				}
			}
			crimeReport.setReportedTotal(reportedTotal);
			crimeReport.setReportTotal(reportTotal);
			crimeReport.setTotal(total);
			crimeReportList.add(crimeReport);
		}
		report.setCrimeReportList(crimeReportList);
		return report;
	}

	public static void main(String[] args) {
		Report report = new Report();
		report.setBranchName("xxxx上海分行");
		report.setReportDate("2017.7.10 -- 2017.7.11");
		report.setBigAmountTotal(10L);
		report.setBigAmountBZTotal(5L);
		report.setSuspiciousTotal(10L);
		report.setSuspiciousBZXTotal(1L);
		report.setSuspiciousBZXHDDRMYHFZJGTotal(2L);
		report.setSuspiciousBZXHDDGATotal(3L);
		report.setSuspiciousBZXHDDRMYHFZJGHDDGATotal(4L);
		CustomerReport customerReport = new CustomerReport();
		customerReport.setBz("CNY");
		customerReport.setJe(BigDecimal.valueOf(10000000));
		customerReport.setName("zhangsan");
		customerReport.setNumber("10001");
		customerReport.setTotal(5L);
		CustomerReport customerReport3 = new CustomerReport();
		customerReport3.setBz("AWD");
		customerReport3.setJe(BigDecimal.valueOf(9999999));
		customerReport3.setName("zhangsan");
		customerReport3.setNumber("10001");
		customerReport3.setTotal(2L);
		CustomerReport customerReport2 = new CustomerReport();
		customerReport2.setBz("USA");
		customerReport2.setJe(BigDecimal.valueOf(500000));
		customerReport2.setName("lisi");
		customerReport2.setNumber("10002");
		customerReport2.setTotal(2L);
		CustomerReport customerReport4 = new CustomerReport();
		customerReport4.setBz("CNY");
		customerReport4.setJe(BigDecimal.valueOf(500000));
		customerReport4.setName("lisi");
		customerReport4.setNumber("10002");
		customerReport4.setTotal(2L);

		CustomerReport customerReport5 = new CustomerReport();
		customerReport5.setBz("CNY");
		customerReport5.setJe(BigDecimal.valueOf(700000));
		customerReport5.setName("wangwu");
		customerReport5.setNumber("10003");
		customerReport5.setTotal(2L);

		CustomerReport customerReport6 = new CustomerReport();
		customerReport6.setBz("CNY");
		customerReport6.setJe(BigDecimal.valueOf(700000));
		customerReport6.setName("赵六");
		customerReport6.setNumber("10004");
		customerReport6.setTotal(2L);

		List<CustomerReport> list = new ArrayList<CustomerReport>();
		list.add(customerReport);
		list.add(customerReport3);
		List<CustomerReport> list2 = new ArrayList<CustomerReport>();
		list2.add(customerReport2);
		list2.add(customerReport4);
		List<CustomerReport> list3 = new ArrayList<CustomerReport>();
		list3.add(customerReport5);
		list3.add(customerReport5);
		List<CustomerReport> list4 = new ArrayList<CustomerReport>();
		list4.add(customerReport6);
		list4.add(customerReport6);
		List<CustomerReport> list5 = new ArrayList<CustomerReport>();
		list5.add(customerReport2);
		Map<String, List<CustomerReport>> bigAmountTotalMap = new HashMap<String, List<CustomerReport>>();
		bigAmountTotalMap.put(list.get(0).getNumber(), list);
		bigAmountTotalMap.put(list2.get(0).getNumber(), list2);
		bigAmountTotalMap.put(list4.get(0).getNumber(), list4);
		report.setBigAmountTotalMap(bigAmountTotalMap);
		Map<String, List<CustomerReport>> suspMap = new HashMap<String, List<CustomerReport>>();
		suspMap.put(list.get(0).getNumber(), list);
		suspMap.put(list5.get(0).getNumber(), list5);
		suspMap.put(list3.get(0).getNumber(), list3);
		report.setSuspiciousTotalMap(suspMap);
		// report.getSuspiciousTotalMap().put("1003",
		// Arrays.asList(customerReport5));

		report.setBigAmount0501Total(501L);
		report.setBigAmount0502Total(502L);
		report.setBigAmount0503Total(503L);
		report.setBigAmount0504Total(504L);
		CrimeReport crimeReport = new CrimeReport();
		crimeReport.setCrime("1103");
		crimeReport.setReportedTotal(20L);
		crimeReport.setReportTotal(33L);
		crimeReport.setTotal(66L);
		List<CrimeReport> crimeReportList = new ArrayList<CrimeReport>();
		crimeReportList.add(crimeReport);
		crimeReportList.add(crimeReport);
		crimeReportList.add(crimeReport);
		crimeReportList.add(crimeReport);
		crimeReportList.add(crimeReport);
		report.setCrimeReportList(crimeReportList);
		ExportDataExcelServiceImpl serviceImpl = new ExportDataExcelServiceImpl();
		try {
			FileInputStream is = new FileInputStream(
					"D:\\git\\amlNew\\aml-api\\src\\main\\resources\\static\\excelModel\\AML报表模板.xlsx");
			FileOutputStream os = new FileOutputStream("d:\\text.xlsx");
			serviceImpl.fillData(is, os, report);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
