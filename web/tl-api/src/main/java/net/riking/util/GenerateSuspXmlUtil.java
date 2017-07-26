package net.riking.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Service;

import net.riking.config.ExportConfig;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.Branch;
import net.riking.entity.model.ExportOrder;
import net.riking.entity.model.SusAttachment;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.ExportOrderRepo;
import net.riking.service.repo.SusAttachmentRepo;
import net.riking.util.suspxmlmodel.Bstr;
import net.riking.util.suspxmlmodel.Rbif;
import net.riking.util.suspxmlmodel.Scif;
import net.riking.util.suspxmlmodel.Seif;
import net.riking.util.suspxmlmodel.Siif;
import net.riking.util.suspxmlmodel.Stif;

@Service("generateSuspXml")
public class GenerateSuspXmlUtil {

	// private //logger //log = //logger.get//logger(this.getClass());
	private SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	public boolean writeNXML(Branch branch, List<AmlSuspicious> list, String reportType, String sfrk,
			String oldPostDate, String oldbatch, String xmlFilePath, String zipFilePath, String zipNowPath,
			ExportOrderRepo exportOrderRepo, AmlSuspiciousRepo amlSuspiciousRepo, SusAttachmentRepo susAttachmentRepo,
			ExportConfig exportConfig) throws Exception {
		if (list.isEmpty()) {
			//// log.info("根据日期，没有找到相关的反洗钱信息！");
			return false;
		}

		// 限制大小
		//int size = exportConfig.getSize();

		Map<String, List<AmlSuspicious>> map = getByCsnm_N(list);
		boolean flag = false;
		for (String key : map.keySet()) {
			// 得到文件名
			String name = getName(branch, reportType, sfrk, oldPostDate, oldbatch, exportOrderRepo);
			String[] names = name.split("-");
			Bstr BSTR = getInfo_N(branch, map.get(key));
			Element root = getElement(BSTR, "BSTR");
			Document doc = new Document(root);
			// 得到附件
			HashSet<Long> set = new HashSet<Long>();
			for (AmlSuspicious amlSuspicious : map.get(key)) {
				set.add(amlSuspicious.getId());
			}
			List<SusAttachment> list3 = susAttachmentRepo.findBySusIdIn(set);
			for (SusAttachment sus : list3) {
				FileOperate.getInstance().copyFile(sus.getFilePath(), zipFilePath + sus.getFileName());// 复制附件
			}
			String zipMarkPath = xmlFilePath + name + "附件" + ".ZIP";
			JZip.compress(zipFilePath, true, zipMarkPath);// 压缩附件
			FileUtil.deleteDir(zipFilePath);// 删除附件
			flag = xmlUtils(xmlFilePath + name + ".XML", doc);// 写报文
			if (flag) {
				// 修改交易的报文信息
				setAmlSuspStatus(map.get(key), names[1], names[2], name);
				amlSuspiciousRepo.save(map.get(key));
				// 压缩打包
				String zipPath = zipFilePath + name + ".ZIP";
				JZip.compress(xmlFilePath, true, zipPath);// 压缩xml
				FileOperate.getInstance().copyFile(zipPath, zipNowPath + name + ".ZIP");// 复制到历史文件夹中
				saveExportOrder(branch, ymdFormat.parse(names[1]), reportType, names[2], exportOrderRepo);// 修改批次表种记录
				FileUtil.deleteDir(xmlFilePath);
				FileUtil.deleteDir(zipFilePath);
			}
		}
		return flag;
	}

	private Map<String, List<AmlSuspicious>> getByCsnm_N(List<AmlSuspicious> list) {
		Map<String, List<AmlSuspicious>> map = new HashMap<String, List<AmlSuspicious>>();
		for (AmlSuspicious susp : list) {
			if (map.containsKey(susp.getCsnm1())) {
				map.get(susp.getCsnm1()).add(susp);
			} else {
				ArrayList<AmlSuspicious> _list = new ArrayList<AmlSuspicious>();
				_list.add(susp);
				map.put(susp.getCsnm1(), _list);
			}
		}
		return map;
	}

	private Bstr getInfo_N(Branch branch, List<AmlSuspicious> list) {
		Bstr BSTR = new Bstr();

		AmlSuspicious susp = list.get(0);
		Rbif RBIF = new Rbif();
		// 报告机构编码
		RBIF.setRICD(branch.getAmlBranchCode());
		// 上报网点代码
		RBIF.setRPNC(susp.getRpnc());
		// 可疑报告紧急程度
		RBIF.setDETR(susp.getDetr());
		// 报送次数标志
		RBIF.setTORP(susp.getTorp());
		// 报送方向
		RBIF.setDORP(String.valueOf(susp.getDorp()));
		// 其他报送方向
		RBIF.setODRP(susp.getOdrp());
		// 可疑报告触发点
		RBIF.setTPTR(susp.getTptr());
		// 其他可疑报告触发点
		RBIF.setOTPR(susp.getOtpr());
		// 资金交易与客户行为情况
		RBIF.setSTCB(susp.getStcb());
		// 疑点分析
		RBIF.setAOSP(susp.getAosp());
		// 疑似涉罪类型
		HashSet<String> TOSCs = new HashSet<String>();
		RBIF.setTOSCs(TOSCs);
		// 可疑特征代码
		HashSet<String> STCRs = new HashSet<String>();
		RBIF.setSTCRs(STCRs);
		// 可疑主体数
		RBIF.setSETN(1 + "");
		// 可疑交易总数
		RBIF.setSTNM(list.size() + "");
		// 可以报告的填报人
		RBIF.setRPNM(susp.getRpnm());
		// 人工补正标识
		RBIF.setMIRS(susp.getMirs());
		BSTR.setRBIF(RBIF);

		List<Seif> SEIFs = new ArrayList<Seif>();
		BSTR.setSEIFs(SEIFs);
		Seif SEIF = new Seif();
		// 客户号
		SEIF.setCSNM(susp.getCsnm1());
		// 可疑主体职业
		SEIF.setSEVC(susp.getSevc());

		List<Siif> SIIFs = new ArrayList<Siif>();
		Siif SIIF = new Siif();
		SIIFs.add(SIIF);
		SEIF.setSIIFs(SIIFs);
		// 可疑主体国籍
		Set<String> STNTs = new HashSet<String>();
		SEIF.setSTNTs(STNTs);
		Scif SCIF = new Scif();
		// 可疑主体联系电话
		Set<String> SCTLs = new HashSet<String>();
		SCIF.setSCTLs(SCTLs);
		// 可疑主体住址或经营地址
		Set<String> SEARs = new HashSet<String>();
		SCIF.setSEARs(SEARs);
		// 可疑主体其他联系方式
		Set<String> SEEIs = new HashSet<String>();
		SCIF.setSEEIs(SEEIs);
		SEIF.setSCIF(SCIF);

		Map<String, String> SRIF = new LinkedHashMap<String, String>();
		// 可疑主体法定代表人姓名
		SRIF.put("SRNM", susp.getSrnm());
		// 可疑主体法定代表人身份证件类型
		SRIF.put("SRIT", susp.getSrit());
		// 可疑主体法定代表人身份证件类型说明
		SRIF.put("ORIT", susp.getOrit());
		// 可疑主体法定代表人身份证件号码
		SRIF.put("SRID", susp.getSrid());

		// 可疑主体控股股东姓名
		SRIF.put("SCNM", susp.getScnm());
		// 可疑主体控股股东身份证件类型
		SRIF.put("SCIT", susp.getScit());
		// 可疑主体控股股东身份证件类型说明
		SRIF.put("OCIT", susp.getOcit());
		// 可疑主体控股股东身份证件号码
		SRIF.put("SCID", susp.getScid());
		SEIF.setSRIF(SRIF);
		SEIFs.add(SEIF);

		List<Stif> STIFs = new ArrayList<Stif>();
		BSTR.setSTIFs(STIFs);
		for (AmlSuspicious amlSuspicious : list) {
			// 疑似涉罪类型
			String[] toscs = amlSuspicious.getTosc().split(",", -1);
			for (int i = 0; i < toscs.length; i++) {
				TOSCs.add(toscs[i]);
			}
			// 可疑特征代码
			String[] stcr_s = amlSuspicious.getStcr().split(",", -1);
			for (int i = 0; i < stcr_s.length; i++) {
				if (StringUtils.isNotEmpty(stcr_s[i])) {
					STCRs.add(stcr_s[i]);
				}
			}
			// 可疑主体姓名或名称
			SIIF.setSENM(susp.getSenm());
			// 可疑主体身份证件/证明文件类型
			SIIF.setSETP(susp.getSetp());
			// 其他身份证件类型
			SIIF.setOITP(susp.getOitp1());
			// 可疑主体身份证件/证明文件号码
			SIIF.setSEID(susp.getSeid());
			// 可疑主体国籍
			String[] stnts = amlSuspicious.getStnt().split(",", -1);
			for (int i = 0; i < stnts.length; i++) {
				STNTs.add(stnts[i]);
			}
			// 可疑主体联系电话
			String[] sctls = susp.getSctl().split(",", -1);
			for (int i = 0; i < sctls.length; i++) {
				SCTLs.add(sctls[i]);
			}
			// 可疑主体住址或经营地址
			String[] sears = susp.getSear().split(",", -1);
			for (int i = 0; i < sears.length; i++) {
				SEARs.add(sears[i]);
			}

			// 可疑主体其他联系方式
			String[] seeis = susp.getSeei().split(",", -1);
			for (int i = 0; i < seeis.length; i++) {
				SEEIs.add(seeis[i]);
			}
			Stif STIF = new Stif();
			STIFs.add(STIF);
			Map<String, String> RINI = new LinkedHashMap<String, String>();
			STIF.setRINI(RINI);
			Map<String, String> CIIF = new LinkedHashMap<String, String>();
			STIF.setCIIF(CIIF);
			Map<String, String> ATIF = new LinkedHashMap<String, String>();
			STIF.setATIF(ATIF);
			Map<String, String> TBIF = new LinkedHashMap<String, String>();
			STIF.setTBIF(TBIF);
			Map<String, String> TSIF = new LinkedHashMap<String, String>();
			STIF.setTSIF(TSIF);
			Map<String, String> TCIF = new LinkedHashMap<String, String>();
			STIF.setTCIF(TCIF);
			ArrayList<String> ROTFs = new ArrayList<String>();
			STIF.setROTFs(ROTFs);

			// 金融机构网点代码
			RINI.put("FINC", amlSuspicious.getFinc());
			// 金融机构与客户的关系
			RINI.put("RLFC", amlSuspicious.getRlfc());
			// 客户姓名
			CIIF.put("CTNM", amlSuspicious.getCtnm());
			// 客户身份证件类型
			CIIF.put("CITP", amlSuspicious.getCitp());
			// 客户身份证件类型说明
			CIIF.put("OITP", amlSuspicious.getOitp2());
			// 客户身份证件号码
			CIIF.put("CTID", amlSuspicious.getCtid());
			// 客户号
			CIIF.put("CSNM", amlSuspicious.getCsnm2());
			// 客户账号类型
			ATIF.put("CATP", amlSuspicious.getCatp());
			// 客户账号
			ATIF.put("CTAC", amlSuspicious.getCtac());
			// 客户账户开户时间
			ATIF.put("OATM", ymdhmsFormat.format(amlSuspicious.getOatm()));
			// 客户账户销户时间
			ATIF.put("CATM", ymdhmsFormat.format(amlSuspicious.getCatm()));
			// 客户银行卡类型
			ATIF.put("CBCT", amlSuspicious.getCbct());
			// 客户银行卡类型说明
			ATIF.put("OCBT", amlSuspicious.getOcbt());
			// 客户银行卡号码
			ATIF.put("CBCN", amlSuspicious.getCbcn());
			// 交易代办人姓名
			TBIF.put("TBNM", amlSuspicious.getTbnm());
			// 交易代办人证件类型
			TBIF.put("TBIT", amlSuspicious.getTbit());
			// 交易代办人证件类型说明
			TBIF.put("OITP", amlSuspicious.getOitp3());
			// 交易代办人证件号码
			TBIF.put("TBID", amlSuspicious.getTbid());
			// 交易代办人国籍
			TBIF.put("TBNT", amlSuspicious.getTbnt());

			// 交易时间
			TSIF.put("TSTM", ymdhmsFormat.format(amlSuspicious.getTstm()));
			// 交易发生地
			TSIF.put("TRCD", amlSuspicious.getTrcd());
			// 业务标识号
			TSIF.put("TICD", amlSuspicious.getTicd());
			// 收付款方匹配号类型
			TSIF.put("RPMT", amlSuspicious.getRpmt());
			// 收付款方匹配号
			TSIF.put("RPMN", amlSuspicious.getRpnm());
			// 交易方式
			TSIF.put("TSTP", amlSuspicious.getTstp2());
			// 非柜台交易方式
			TSIF.put("OCTT", amlSuspicious.getOcit());
			// 其他非柜台交易方式
			TSIF.put("OOCT", amlSuspicious.getOoct());
			// 非柜台交易方式的设备代码
			TSIF.put("OCEC", amlSuspicious.getOcec());
			// 银行与业务机构之间的业务交易编码
			TSIF.put("BPTC", amlSuspicious.getBptc());
			// 涉外收支交易分类域代码
			TSIF.put("TSCT", amlSuspicious.getTsct());
			// 资金收付标志
			TSIF.put("TSDR", amlSuspicious.getTsdr());
			// 资金用途
			TSIF.put("CRSP", amlSuspicious.getCrsp());
			// 交易币种
			TSIF.put("CRTP", amlSuspicious.getCatp());
			// 交易金额
			TSIF.put("CRAT", amlSuspicious.getCrat().toString());
			// 对方金融机构网点名称
			TCIF.put("CFIN", amlSuspicious.getCfin());
			// 对方金融机构网点代码类型
			TCIF.put("CFCT", amlSuspicious.getCfct());
			// 对方金融就够网点代码
			TCIF.put("CFIC", amlSuspicious.getCfic());
			// 对方金融机构网点行政区划代码
			TCIF.put("CFRC", amlSuspicious.getCfrc());
			// 交易对手名称
			TCIF.put("TCNM", amlSuspicious.getTcnm());
			// 交易的对手身份证件类型
			TCIF.put("TCIT", amlSuspicious.getTcit());
			// 其他身份证件类型
			TCIF.put("OITP", amlSuspicious.getOitp4());
			// 交易对手身份证件号码
			TCIF.put("TCID", amlSuspicious.getTcid());
			// 交易对手账户类型
			TCIF.put("TCAT", amlSuspicious.getTcat());
			// 交易对手账号
			TCIF.put("TCAC", amlSuspicious.getTcac());
			// 交易备注1
			ROTFs.add(susp.getRotf1());
			// 交易备注2
			ROTFs.add(susp.getRotf2());

		}
		return BSTR;
	}

	private boolean xmlUtils(String xmlName, Document doc) {
		Format format = Format.getCompactFormat();// 写xml文件头

		format.setEncoding("utf-8");// 设置xml文件的字符为gb18030

		format.setIndent("    "); // 设置xml文件的缩进为4个空格

		XMLOutputter XMLOut = new XMLOutputter(format);// 元素后换行一层元素缩四格
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(xmlName);
			XMLOut.output(doc, os);
			return true;
		} catch (FileNotFoundException e) {

			e.printStackTrace();

			//// log.info(e);

			return false;

		} catch (IOException e) {

			e.printStackTrace();

			//// log.info(e);

			return false;

		} catch (Exception e) {

			e.printStackTrace();

			//// log.info(e);

			return false;

		} finally {
			if (null != os) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 递归方式 计算文件的大小
	/*
	 * private long getFileLength(final java.io.File file) { if (file.isFile())
	 * return file.length(); final java.io.File[] children = file.listFiles();
	 * long total = 0; if (children != null) for (final java.io.File child :
	 * children) total += getFileLength(child); return total; }
	 */

	/**
	 * 获取批次号
	 * 
	 * @param bath
	 * @return
	 */
	private String getBathNo(String bath) {
		if (bath.equals("0")) {
			bath = "1";
		}
		if (null != bath) {
			int temLengh = 8 - bath.length();
			for (int i = 0; i < temLengh; i++) {
				bath = "0" + bath;
			}
		}
		return bath;
	}

	private String getName(Branch branch, String reporttype, String bwzt, String oldpostdate, String oldbatch,
			ExportOrderRepo exportOrderRepo) {

		int zipNOrder = 0;
		Integer orderNum = 0;
		Date now = new Date();
		if (reporttype.equals("N")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "SHANZIP");
		} else if (reporttype.equals("C")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "SHACZIP");
		} else if (reporttype.equals("A")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "SHAAZIP");
		} else if (reporttype.equals("S")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "SHASZIP");
		}
		if (orderNum != null) {
			zipNOrder = orderNum.intValue() + 1;
		}
		String batch = this.getBathNo(String.valueOf(zipNOrder));
		System.err.println(batch);
		String nowdates = ymdFormat.format(now);
		// 正常报文
		if ("N".equalsIgnoreCase(reporttype)) {
			return "NBS" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		// 纠错报文
		if ("C".equalsIgnoreCase(reporttype)) {
			// 已入库
			if (StringUtils.isNotEmpty(bwzt) && bwzt.equals("bzhz")) {
				return "CBS" + branch.getAmlBranchCode() + "-" + oldpostdate + "-" + oldbatch;

			} else {
				return "CBS" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
			}
		}
		// 更正报文
		if ("A".equalsIgnoreCase(reporttype)) {
			return "ABS" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		// 补充报文
		if ("S".equalsIgnoreCase(reporttype)) {
			return "SBS" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		return null;
	}

	private void setAmlSuspStatus(List<AmlSuspicious> list, String nowdates, String zipbath, String xmlName) {
		for (AmlSuspicious susp : list) {
			if (StringUtils.isEmpty(susp.getSubmitBatch())) {
				susp.setSubmitBatch(zipbath);// 原报送批次（第一次）
			} else {
				susp.setSubmitBatch(susp.getNowSubmitBatch());// （第n次）
			}
			susp.setNowSubmitBatch(zipbath);// 现报送批次
			susp.setTname(xmlName);
			try {
				susp.setOrpdt(ymdFormat.parse(nowdates));// 报送日期
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveExportOrder(Branch branch, Date now, String reportType, String batch,
			ExportOrderRepo exportOrderRepo) {
		ExportOrder exportOrderZip = new ExportOrder();
		exportOrderZip.setBranchCode(branch.getBranchCode());
		exportOrderZip.setSysDate(now);
		if (reportType.equals("N")) {
			exportOrderZip.setOrderType("SHANZIP");
		} else if (reportType.equals("C")) {
			exportOrderZip.setOrderType("SHACZIP");
		} else if (reportType.equals("A")) {
			exportOrderZip.setOrderType("SHAAZIP");
		} else if (reportType.equals("S")) {
			exportOrderZip.setOrderType("SHASZIP");
		}
		exportOrderZip.setOrderNum(Integer.valueOf(batch));
		exportOrderRepo.save(exportOrderZip);
	}

	private Element getElement(Object e, String name) throws Exception {
		Element tElement = new Element(name);
		Class<? extends Object> cls = e.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			if (null == f.get(e)) {
				continue;
			}
			if (f.getType().toString().equals("interface java.util.Map")) {
				Element mElement = new Element(f.getName());
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) f.get(e);
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if (StringUtils.isNotEmpty(entry.getValue())) {
						Element element = new Element(entry.getKey());
						element.setText(entry.getValue());
						mElement.addContent(element);
					}
				}
				tElement.addContent(mElement);
			}
			if (f.getType().toString().equals("interface java.util.List")) {
				Element lElement = new Element(f.getName());
				@SuppressWarnings("unchecked")
				ArrayList<Object> list = (ArrayList<Object>) f.get(e);
				int count = 1;
				for (Object obj : list) {
					if (obj instanceof Seif) {
						Element element = getElement((Seif) obj, "SEIF");
						element.setAttribute("seqno", count + "");
						lElement.addContent(element);
						count++;
					}
					if (obj instanceof Stif) {
						Element element = getElement((Stif) obj, "STIF");
						element.setAttribute("seqno", count + "");
						lElement.addContent(element);
						count++;
					}
					if (obj instanceof Siif) {
						Element element = getElement((Siif) obj, "SIIF");
						element.setAttribute("seqno", count + "");
						lElement.addContent(element);
						count++;
					}
					if (obj instanceof String) {
						String text = (String) obj;
						if (StringUtils.isNotEmpty(text)) {
							Element selement = new Element(f.getName().substring(0, f.getName().length() - 1));
							selement.setAttribute("seqno", count + "");
							selement.setText(text);
							lElement.addContent(selement);
							count++;
						}
					}
				}
				tElement.addContent(lElement);
			}
			if (f.getType().toString().equals("class net.riking.util.suspxmlmodel.Rbif")) {
				Element cbifElement = getElement((Rbif) f.get(e), "RBIF");
				tElement.addContent(cbifElement);
			}
			if (f.getType().toString().equals("class net.riking.util.suspxmlmodel.Scif")) {
				Element cbifElement = getElement((Scif) f.get(e), "SCIF");
				tElement.addContent(cbifElement);
			}
			if (f.getType().toString().equals("class java.lang.String")) {
				if (f.get(e) != null) {
					Element sElement = new Element(f.getName());
					sElement.setText(f.get(e).toString());
					tElement.addContent(sElement);
				}
			}
			if (f.getType().toString().equals("interface java.util.Set")) {
				Element lElement = new Element(f.getName());
				@SuppressWarnings("unchecked")
				HashSet<Object> set = (HashSet<Object>) f.get(e);
				if (null != set && set.size() > 0) {
					for (Object obj : set) {
						if (obj instanceof String) {
							lElement = new Element(f.getName());
						}
						break;
					}
					int count = 1;
					for (Object obj : set) {
						if (obj instanceof String) {
							String text = (String) obj;
							if (StringUtils.isNotEmpty(text)) {
								Element selement = new Element(f.getName().substring(0, f.getName().length() - 1));
								selement.setAttribute("seqno", count + "");
								selement.setText(text);
								lElement.addContent(selement);
								count++;
							}
						}
					}
				}
				tElement.addContent(lElement);
			}
		}
		return tElement;
	}
}
