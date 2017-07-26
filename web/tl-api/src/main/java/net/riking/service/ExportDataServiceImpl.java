package net.riking.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.riking.config.ExportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.Branch;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;
import net.riking.service.repo.ExportOrderRepo;
import net.riking.service.repo.SusAttachmentRepo;
import net.riking.util.FileOperate;
import net.riking.util.FileUtil;
import net.riking.util.GenerateBigAmountXmlUtil;
import net.riking.util.GenerateSuspXmlUtil;

@Service("exportDataService")
public class ExportDataServiceImpl {
	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;

	@Autowired
	ExportConfig exportConfig;

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	ExportOrderRepo exportOrderRepo;

	@Autowired
	SusAttachmentRepo susAttachmentRepo;

	@Autowired
	private UserBranchServiceImpl userBranchServiceImpl;

	private List<BigAmount> listN = new ArrayList<BigAmount>();// 正常报文

	private List<BigAmount> listA = new ArrayList<BigAmount>();// 更正报文

	private List<BigAmount> listC = new ArrayList<BigAmount>();// 修改报文

	private List<BigAmount> listS = new ArrayList<BigAmount>();// 补充报文

	private List<BigAmount> listD = new ArrayList<BigAmount>();// 单个删除报文

	private List<BigAmount> listDTstm = new ArrayList<BigAmount>();// 交易时间删除报文

	private List<BigAmount> listDCrcd = new ArrayList<BigAmount>();// 客户号删除报文

	private List<BigAmount> listDCsnm = new ArrayList<BigAmount>();// 大额特征删除报文

	private Map<String, List<BigAmount>> mapC = new HashMap<String, List<BigAmount>>();// 未入库修改

	private List<AmlSuspicious> listN2 = new ArrayList<AmlSuspicious>();// 正常报文

	private List<AmlSuspicious> listA2 = new ArrayList<AmlSuspicious>();// 更正报文

	private List<AmlSuspicious> listC2 = new ArrayList<AmlSuspicious>();// 修改报文

	private List<AmlSuspicious> listS2 = new ArrayList<AmlSuspicious>();// 补充报文

	private Map<String, List<AmlSuspicious>> mapC2 = new HashMap<String, List<AmlSuspicious>>();// 未入库修改

	public String exportBigAmount(List<BigAmount> list, String token,Long branchId) throws Exception {
		Branch branch = userBranchServiceImpl.getBranch(branchId, token);
		//Branch branch = userBranchServiceImpl.getBranch(token.getBranchId()==null?6L:token.getBranchId(), token.getToken());
		try {
			this.convertList(list);// 将所有的大额交易封装到不同的LIST里面
			String zipFilePath = exportConfig.getExportBigamountZipDir();// zip文件绝对路径
			String zipNowPath = exportConfig.getExportBigamountNowDir();// zip历史文件绝对路径
			java.io.File folder = new java.io.File(zipNowPath);
			if (!folder.exists()) {
				// folder.createNewFile();
				FileUtil.newFolder(zipNowPath);
			}
			FileOperate.getInstance().delAllFile(zipFilePath);// 删除该文件夹下面的所有文件
			// 正常报文
			if (null != listN && listN.size() > 0) {
				String xmlNFilePath = exportConfig.getExportBigamountNXmlDir();// xml文件正常报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlNFilePath)) {
					FileUtil.newFolder(xmlNFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeNXML(branch, listN, "N", "xzbw", null, null, xmlNFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}

			// 已入库修改报文
			if (null != listC && listC.size() > 0) {
				String xmlCFilePath = exportConfig.getExportBigamountXmlDir();// xml文件修改报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlCFilePath)) {
					FileUtil.newFolder(xmlCFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeCXML(branch, listC, "C", "cwhz", null, null, xmlCFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 未入库修改报文
			if (mapC.size() > 0) {
				String xmlCFilePath = exportConfig.getExportBigamountXmlDir();// xml文件修改报文绝对路径
				Iterator<String> it = mapC.keySet().iterator();
				if (!FileUtil.deleteDir(xmlCFilePath)) {
					FileUtil.newFolder(xmlCFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				while (it.hasNext()) {
					String key = it.next().toString();
					String[] keys = key.split("-");
					GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
					xmlUtil.writeCXML(branch, mapC.get(key), "C", "bzhz", keys[0], keys[1], xmlCFilePath, zipFilePath,
							zipNowPath, exportOrderRepo, bigAmountRepo, exportConfig);
				}

			}
			// 删除报文
			if (null != listD && listD.size() > 0) {
				String xmlDFilePath = exportConfig.getExportBigamountXmlDir();// xml文件删除报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlDFilePath)) {
					FileUtil.newFolder(xmlDFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				// scbw sjsc khsc tzsc
				xmlUtil.writeDXML(branch, listD, "D", "scbw", null, null, xmlDFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 交易时间删除报文
			if (null != listDTstm && listDTstm.size() > 0) {
				String xmlDFilePath = exportConfig.getExportBigamountXmlDir();// xml文件删除报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlDFilePath)) {
					FileUtil.newFolder(xmlDFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeDXML(branch, listDTstm, "D", "sjsc", null, null, xmlDFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 客户号删除报文
			if (null != listDCsnm && listDCsnm.size() > 0) {
				String xmlDFilePath = exportConfig.getExportBigamountXmlDir();// xml文件删除报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlDFilePath)) {
					FileUtil.newFolder(xmlDFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeDXML(branch, listDCsnm, "D", "khsc", null, null, xmlDFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 大额特征删除报文
			if (null != listDCrcd && listDCrcd.size() > 0) {
				String xmlDFilePath = exportConfig.getExportBigamountXmlDir();// xml文件删除报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlDFilePath)) {
					FileUtil.newFolder(xmlDFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeDXML(branch, listDCrcd, "D", "tzsc", null, null, xmlDFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 更正报文
			if (null != listA && listA.size() > 0) {
				String xmlAFilePath = exportConfig.getExportBigamountXmlDir();// xml文件更正报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlAFilePath)) {
					FileUtil.newFolder(xmlAFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeCXML(branch, listA, "A", "rggz", null, null, xmlAFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
			// 补充报文
			if (null != listS && listS.size() > 0) {
				String xmlSFilePath = exportConfig.getExportBigamountNXmlDir();// xml文件补充报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlSFilePath)) {
					FileUtil.newFolder(xmlSFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateBigAmountXmlUtil xmlUtil = new GenerateBigAmountXmlUtil();
				xmlUtil.writeCXML(branch, listS, "S", "rgbc", null, null, xmlSFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, bigAmountRepo, exportConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			this.listA.clear();
			this.listC.clear();
			this.listD.clear();
			this.listDTstm.clear();
			this.listDCsnm.clear();
			this.listDCrcd.clear();
			this.listS.clear();
			this.listN.clear();
			this.mapC.clear();
		}
		return "导出成功！";
	}

	public String exportSusp(List<AmlSuspicious> list, String token, Long branchId) throws Exception {
		Branch branch = userBranchServiceImpl.getBranch(branchId, token);
		//Branch branch = userBranchServiceImpl.getBranch(token.getBranchId()==null?6L:token.getBranchId(), token.getToken());
		try {
			this.convertList2(list);// 将所有的可疑交易封装到不同的LIST里面
			String zipFilePath = exportConfig.getExportShadinessZipDir();// zip文件绝对路径
			String zipNowPath = exportConfig.getExportShadinessNowDir();// zip历史文件绝对路径
			FileOperate.getInstance().delAllFile(zipFilePath);// 删除该文件夹下面的所有文件
			java.io.File folder = new java.io.File(zipNowPath);
			if (!folder.exists()) {
				FileUtil.newFolder(zipNowPath);
			}
			// 正常报文
			if (null != listN2 && listN2.size() > 0) {
				String xmlNFilePath = exportConfig.getExportShadinessNXmlDir();// xml文件正常报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlNFilePath)) {
					FileUtil.newFolder(xmlNFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateSuspXmlUtil xmlUtil = new GenerateSuspXmlUtil();
				xmlUtil.writeNXML(branch, listN2, "N", "xzbw", null, null, xmlNFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, amlSuspiciousRepo, susAttachmentRepo, exportConfig);
			}

			// 已入库修改报文
			if (null != listC2 && listC2.size() > 0) {
				String xmlCFilePath = exportConfig.getExportShadinessXmlDir();// xml文件修改报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlCFilePath)) {
					FileUtil.newFolder(xmlCFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateSuspXmlUtil xmlUtil = new GenerateSuspXmlUtil();
				xmlUtil.writeNXML(branch, listC2, "C", "cwhz", null, null, xmlCFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, amlSuspiciousRepo, susAttachmentRepo, exportConfig);
			}

			// 未入库修改报文
			if (mapC2.size() > 0) {
				String xmlCFilePath = exportConfig.getExportShadinessXmlDir();// xml文件修改报文绝对路径
				Iterator<String> it = mapC2.keySet().iterator();
				if (!FileUtil.deleteDir(xmlCFilePath)) {
					FileUtil.newFolder(xmlCFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				while (it.hasNext()) {
					String key = it.next().toString();
					String[] keys = key.split("-");
					// 0 riqi 1 pici
					GenerateSuspXmlUtil xmlUtil = new GenerateSuspXmlUtil();
					xmlUtil.writeNXML(branch, mapC2.get(key), "C", "bzhz", keys[0], keys[1], xmlCFilePath, zipFilePath,
							zipNowPath, exportOrderRepo, amlSuspiciousRepo, susAttachmentRepo, exportConfig);
				}

			}

			if (null != listS2 && listS2.size() > 0) {
				// 补充报文
				String xmlSFilePath = exportConfig.getExportShadinessXmlDir();// xml文件补充报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlSFilePath)) {
					FileUtil.newFolder(xmlSFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateSuspXmlUtil xmlUtil = new GenerateSuspXmlUtil();
				xmlUtil.writeNXML(branch, listS2, "S", "rgbc", null, null, xmlSFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, amlSuspiciousRepo, susAttachmentRepo, exportConfig);
			}

			if (null != listA2 && listA2.size() > 0) {
				// 更正报文
				String xmlAFilePath = exportConfig.getExportShadinessXmlDir();// xml文件更正报文绝对路径
				// 删除产生文件存放路径下的所有文件。
				if (!FileUtil.deleteDir(xmlAFilePath)) {
					FileUtil.newFolder(xmlAFilePath);
					FileUtil.newFolder(zipFilePath);
				}
				GenerateSuspXmlUtil xmlUtil = new GenerateSuspXmlUtil();
				xmlUtil.writeNXML(branch, listA2, "A", "rggz", null, null, xmlAFilePath, zipFilePath, zipNowPath,
						exportOrderRepo, amlSuspiciousRepo, susAttachmentRepo, exportConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			this.listA2.clear();
			this.listC2.clear();
			this.listS2.clear();
			this.listN2.clear();
			this.mapC2.clear();
		}
		return "导出成功";
	}

	private void convertList(List<BigAmount> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (BigAmount bigAmount : list) {
			if (bigAmount.getBwzt().equals("xzbw") || bigAmount.getBwzt().equals("cwhz")) {
				this.listN.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("bzhz")) {
				String submitBatch = bigAmount.getNowSubmitBatch();// 报送批次
				String orpdt = sdf.format(bigAmount.getOrpdt());// 原报送日期
				String key = orpdt + "-" + submitBatch;
				if (mapC.containsKey(key)) {
					mapC.get(key).add(bigAmount);
				} else {
					List<BigAmount> l = new ArrayList<BigAmount>();
					l.add(bigAmount);
					mapC.put(key, l);
				}
				;
			}
			if (bigAmount.getBwzt().equals("zdjc")) {
				this.listC.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("scbw")) {
				this.listD.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("sjsc")) {
				this.listDTstm.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("khsc")) {
				this.listDCsnm.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("tzsc")) {
				this.listDCrcd.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("rggz")) {
				this.listA.add(bigAmount);
			}
			if (bigAmount.getBwzt().equals("rgbc")) {
				this.listS.add(bigAmount);
			}
		}
	}

	private void convertList2(List<AmlSuspicious> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (AmlSuspicious shadiness : list) {
			if (shadiness.getBwzt().equals("xzbw") || shadiness.getBwzt().equals("cwhz")) {
				this.listN2.add(shadiness);
			} else if (shadiness.getBwzt().equals("bzhz")) {
				String submitBatch = shadiness.getSubmitBatch();// 报送批次
				String orpdt = sdf.format(shadiness.getOrpdt());// 原报送日期
				String key = orpdt + "-" + submitBatch;
				if (mapC2.containsKey(key)) {
					mapC2.get(key).add(shadiness);
				} else {
					List<AmlSuspicious> l = new ArrayList<AmlSuspicious>();
					l.add(shadiness);
					mapC2.put(key, l);
				}
			} else if (shadiness.getBwzt().equals("zdjc")) {
				this.listC2.add(shadiness);
			} else if (shadiness.getBwzt().equals("rggz")) {
				this.listA2.add(shadiness);
			} else if (shadiness.getBwzt().equals("rgbc")) {
				this.listS2.add(shadiness);
			}

		}
	}

}
