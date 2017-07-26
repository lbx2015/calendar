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
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.Branch;
import net.riking.entity.model.ExportOrder;
import net.riking.service.repo.BigAmountRepo;
import net.riking.service.repo.ExportOrderRepo;
import net.riking.util.xmlmodel.Cati;
import net.riking.util.xmlmodel.Cbif;
import net.riking.util.xmlmodel.Ccif_c;
import net.riking.util.xmlmodel.Ccif_t;
import net.riking.util.xmlmodel.Dtdt;
import net.riking.util.xmlmodel.Htcr;
import net.riking.util.xmlmodel.Tsdt;

@Service("cenerateBigAmountXml")
public class GenerateBigAmountXmlUtil {
	// private String[] amlBigamountCharcterCode = new String[] { "0901",
	// "0902", "0903", "0904" };

	private SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	/*
	 * 大额新增报文
	 */
	public boolean writeNXML(Branch branch, List<BigAmount> blist, String reportType, String bwzt, String oldPostDate,
			String oldbatch, String xmlFilePath, String zipFilePath, String zipNowPath, ExportOrderRepo exportOrderRepo,
			BigAmountRepo bigAmountRepo, ExportConfig exportConfig) throws Exception {
		if (blist.isEmpty()) {
			// log.info("根据日期，没有找到相关的反洗钱信息！");
			return false;
		}
		// zip包上限大小
		int size = exportConfig.getSize();
		List<List<BigAmount>> Lists = new ArrayList<List<BigAmount>>();
		if (blist.size() > size) {
			int len = blist.size() % size == 0 ? blist.size() / size : blist.size() / size + 1;
			for (int i = 0; i < len; i++) {
				List<BigAmount> llist = blist.subList(i * size,
						(i + 1) * size > blist.size() ? blist.size() : (i + 1) * size);
				Lists.add(llist);
			}
		} else {
			Lists.add(blist);
		}
		boolean flag = false;
		for (List<BigAmount> list : Lists) {
			// 得到文件名
			String name = getName(branch, reportType, bwzt, oldPostDate, oldbatch, exportOrderRepo);
			String[] names = name.split("-");
			Map<String, Cati> map = getInfoByCsnm_N(list);
			Element HVTR = new Element("HVTR");
			Element RBIF = this.setRBIF_N(branch, map.size());
			HVTR.addContent(RBIF);
			// 报告主体和交易信息 CTIFs
			Element CATIs = new Element("CATIs");
			int count = 1;
			for (String key : map.keySet()) {
				Element CATI = getElement(map.get(key), "CATI");
				CATI.setAttribute("seqno", count + "");
				CATIs.addContent(CATI);
				count++;
			}
			HVTR.addContent(CATIs);
			Document doc = new Document(HVTR);
			flag = xmlUtil(xmlFilePath + name + ".XML", doc);
			if (flag) {
				String zipPath = zipFilePath + name + ".ZIP";
				JZip.compress(xmlFilePath, true, zipPath);// 压缩xml
				FileOperate.getInstance().copyFile(zipPath, zipNowPath + name + ".ZIP");
				saveExportOrder(branch, ymdFormat.parse(names[1]), reportType, names[2], exportOrderRepo);
				// 修改数据批次 和文件名等
				if (list.size() > 0) {
					setBigAmountStatus(list, names[1], names[2], name);
					bigAmountRepo.save(list);
					// list.clear();
				}
				FileUtil.deleteDir(xmlFilePath);
				FileUtil.deleteDir(zipFilePath);
			}
		}
		return flag;
	}

	private Map<String, Cati> getInfoByCsnm_N(List<BigAmount> list) {
		HashMap<String, Cati> map = new HashMap<String, Cati>();
		for (BigAmount amount : list) {
			// 根据客户号判断 CATI
			if (!map.containsKey(amount.getCsnm())) {
				Cati CATI = new Cati();
				Cbif CBIF = this.getCBIF_N(amount);
				CATI.setCBIF(CBIF);
				// 交易时间
				CATI.setHTDT(ymdFormat.format(amount.getHtdt()));
				ArrayList<Htcr> HTCRs = new ArrayList<Htcr>();
				Htcr HTCR = this.getHTCR_N(amount);
				HTCRs.add(HTCR);
				CATI.setHTCRs(HTCRs);
				map.put(amount.getCsnm(), CATI);
			} else {
				Cati CATI = map.get(amount.getCsnm());
				String[] ctnts = amount.getCtnt().split(",", -1);
				for (int i = 0; i < ctnts.length; i++) {
					CATI.getCBIF().getCTNTs().add(ctnts[i]);
				}
				String[] cctls = amount.getCctl().split(",", -1);
				for (int i = 0; i < cctls.length; i++) {
					CATI.getCBIF().getCCIF().getCCTLs().add(cctls[i]);
				}
				String[] ctars = amount.getCtar().split(",", -1);
				for (int i = 0; i < ctars.length; i++) {
					CATI.getCBIF().getCCIF().getCATRs().add(ctars[i]);
				}
				String[] cceis = amount.getCcei().split(",", -1);
				for (int i = 0; i < cceis.length; i++) {
					CATI.getCBIF().getCCIF().getCCEIs().add(cceis[i]);
				}
				// CATI.getCBIF().getCTNTs().add(amount.getCtnt());
				// CATI.getCBIF().getCCIF().getCCTLs().add(amount.getCctl());
				// CATI.getCBIF().getCCIF().getCATRs().add(amount.getCtar());
				// CATI.getCBIF().getCCIF().getCCEIs().add(amount.getCcei());
				// 是否已包含该大额特征 不包含getHTCR 反之 为包含getCCIF
				this.getHTCROrCCIFOrTSDT_N(CATI.getHTCRs(), amount);

			}
		}
		return map;
	}

	private Element setRBIF_N(Branch branch, int custmTotalNum) {
		Element RICD, CTTN;
		Element RBIF = new Element("RBIF");

		RICD = new Element("RICD");// 报告机构编码
		RICD.setText(branch.getAmlBranchCode());
		CTTN = new Element("CTTN");
		CTTN.setText(custmTotalNum + "");// 交易主体总数

		// 报告基本信息
		RBIF.addContent(RICD);
		RBIF.addContent(CTTN);

		return RBIF;
	}

	private Cbif getCBIF_N(BigAmount amount) {
		Cbif CBIF = new Cbif();
		// 客户编号
		CBIF.setCSNM(amount.getCsnm());
		// 客户国籍
		Set<String> CTNTs = new HashSet<String>();
		String[] ctnts = amount.getCtnt().split(",", -1);
		for (int i = 0; i < ctnts.length; i++) {
			CTNTs.add(ctnts[i]);
		}
		CBIF.setCTNTs(CTNTs);
		// 客户职业或行业
		CBIF.setCTVC(amount.getCtvc());

		Ccif_c CCIF_C = new Ccif_c();
		// 客户联系方式
		Set<String> CCTLs = new HashSet<String>();
		String[] cctls = amount.getCctl().split(",", -1);
		for (int i = 0; i < cctls.length; i++) {
			CCTLs.add(cctls[i]);
		}
		CCIF_C.setCCTLs(CCTLs);
		// 客户住址/经营地址
		Set<String> CATRs = new HashSet<String>();
		String[] ctars = amount.getCtar().split(",", -1);
		for (int i = 0; i < ctars.length; i++) {
			CATRs.add(ctars[i]);
		}
		CCIF_C.setCATRs(CATRs);
		// 客户其他联系方式
		Set<String> CCEIs = new HashSet<String>();
		String[] cceis = amount.getCcei().split(",", -1);
		for (int i = 0; i < cceis.length; i++) {
			CCEIs.add(cceis[i]);
		}
		CCIF_C.setCCEIs(CCEIs);
		CBIF.setCCIF(CCIF_C);
		return CBIF;
	}

	private Htcr getHTCR_N(BigAmount amount) {
		Htcr HTCR = new Htcr();
		// 大额交易特征码
		HTCR.setCRCD(amount.getCrcd());
		// 交易总数
		HTCR.setTTNM(1 + "");
		//
		ArrayList<Ccif_t> CCIFs = new ArrayList<Ccif_t>();
		Ccif_t CCIF_T = this.getCCIF_T_N(amount);
		CCIFs.add(CCIF_T);
		HTCR.setCCIFs(CCIFs);
		return HTCR;
	}

	private Ccif_t getCCIF_T_N(BigAmount amount) {
		Ccif_t CCIF_T = new Ccif_t();
		// 客户姓名
		CCIF_T.setCTNM(amount.getCtnm());
		// 客户身份证件类型
		CCIF_T.setCITP(amount.getCitp());
		// 其他证件类型
		CCIF_T.setOITP(amount.getOitp1());
		// 证件号码
		CCIF_T.setCTID(amount.getCtid());

		ArrayList<Tsdt> TSDTs = new ArrayList<Tsdt>();
		Tsdt TSDT = this.getTSDT_N(amount);
		TSDTs.add(TSDT);
		CCIF_T.setTSDTs(TSDTs);
		return CCIF_T;
	}

	private Tsdt getTSDT_N(BigAmount amount) {
		Tsdt TSDT = new Tsdt();
		Map<String, String> RINI = new LinkedHashMap<String, String>();
		// 金融机构网点代码
		RINI.put("FINC", amount.getFinc());
		// 金融机构与客户关系
		RINI.put("RLFC", amount.getRlfc());
		TSDT.setRINI(RINI);

		Map<String, String> ATIF = new LinkedHashMap<String, String>();
		// 客户账户类型
		ATIF.put("CATP", amount.getCatp());
		// 客户账号
		ATIF.put("CTAC", amount.getCtac());
		// 客户账户开立时间
		ATIF.put("OATM", ymdhmsFormat.format(amount.getOatm()));
		// 客户银行卡类型
		ATIF.put("CBCT", amount.getCbct());
		// 客户银行卡其他类型
		ATIF.put("OCBT", amount.getOcbt());
		// 客户银行卡号码
		ATIF.put("CBCN", amount.getCbcn());
		TSDT.setATIF(ATIF);

		Map<String, String> TBIF = new LinkedHashMap<String, String>();
		// 交易代办人姓名
		TBIF.put("TBNM", amount.getTbnm());
		// 交易代办人证件类型
		TBIF.put("TBIT", amount.getTbit());
		// 交易代办人其他证件类型
		TBIF.put("OITP", amount.getOitp2());
		// 交易代办人证件号码
		TBIF.put("TBID", amount.getTbid());
		// 交易代办人国籍
		TBIF.put("TBNT", amount.getTbnt());
		TSDT.setTBIF(TBIF);

		Map<String, String> TSIF = new LinkedHashMap<String, String>();
		// 交易时间
		TSIF.put("TSTM", ymdhmsFormat.format(amount.getTstm()));
		// 交易发生地
		TSIF.put("TRCD", amount.getTrcd());
		// 业务标识号
		TSIF.put("TICD", amount.getTicd());
		// 收付款方匹配号类型
		TSIF.put("RPMT", amount.getRpmt());
		// 收付款方匹配号
		TSIF.put("RPMN", amount.getRpmn());
		// 交易方式
		TSIF.put("TSTP", amount.getTstp2());
		// 非柜台交易方式
		TSIF.put("OCTT", amount.getOctt());
		// 其他非柜台交易方式
		TSIF.put("OOCT", amount.getOoct());
		// 非柜台交易方式的设备代码
		TSIF.put("OCEC", amount.getOcec());
		// 银行与业务机构之间的业务交易编码
		TSIF.put("BPTC", amount.getBptc());
		// 涉外收支交易分类域代码
		TSIF.put("TSCT", amount.getTsct());
		// 资金收付标志
		TSIF.put("TSDR", amount.getTsdr());
		// 资金用途
		TSIF.put("CRPP", amount.getCrpp());
		// 交易币种
		TSIF.put("CRTP", amount.getCatp());
		// 交易金额
		TSIF.put("CRAT", amount.getCrat().toString());
		TSDT.setTSIF(TSIF);

		Map<String, String> TCIF = new LinkedHashMap<String, String>();
		// 对方金融机构网点名称
		TCIF.put("CFIN", amount.getCfin());
		// 对方金融机构网点代码类型
		TCIF.put("CFCT", amount.getCfct());
		// 对方金融就够网点代码
		TCIF.put("CFIC", amount.getCfic());
		// 对方金融机构网点行政区划代码
		TCIF.put("CFRC", amount.getCfrc());
		// 交易对手名称
		TCIF.put("TCNM", amount.getTcnm());
		// 交易的对手身份证件类型
		TCIF.put("TCIT", amount.getTcit());
		// 其他身份证件类型
		TCIF.put("OITP", amount.getOitp3());
		// 交易对手身份证件号码
		TCIF.put("TCID", amount.getTcid());
		// 交易对手账户类型
		TCIF.put("TCAT", amount.getTcat());
		// 交易对手账号
		TCIF.put("TCAC", amount.getTcac());
		TSDT.setTCIF(TCIF);
		// 交易备注信息
		List<String> ROTFs = new ArrayList<String>();
		ROTFs.add(amount.getRotf1());
		ROTFs.add(amount.getRotf2());
		TSDT.setROTFs(ROTFs);
		return TSDT;

	}

	private void getHTCROrCCIFOrTSDT_N(List<Htcr> HTCRs, BigAmount amount) {
		for (Htcr HTCR : HTCRs) {
			// 大额特征
			if (HTCR.getCRCD().equals(amount.getCrcd())) {
				List<Ccif_t> CCIFs = HTCR.getCCIFs();
				for (Ccif_t CCIF : CCIFs) {
					// 客户身份 ctnm citp oitp ctid
					if (CCIF.getCTNM().equals(amount.getCtnm()) && CCIF.getOITP().equals(amount.getOitp1())
							&& CCIF.getCITP().equals(amount.getCitp()) && CCIF.getCTID().equals(amount.getCtid())) {
						// 包含大额特征 且 客户身份 一致
						Tsdt TSDT = this.getTSDT_N(amount);
						CCIF.getTSDTs().add(TSDT);
						return;
					}
				}
				// 包含大额特征 但 客户身份 不一致
				Ccif_t CCIF_T = this.getCCIF_T_N(amount);
				HTCR.getCCIFs().add(CCIF_T);
				int ttnm = Integer.parseInt(HTCR.getTTNM()) + 1;
				HTCR.setTTNM(ttnm + "");
				return;
			}
		}
		// 不包含大额特征
		Htcr HTCR = getHTCR_N(amount);
		HTCRs.add(HTCR);
	}

	/*
	 * 大额修改报文
	 */
	public boolean writeCXML(Branch branch, List<BigAmount> blist, String reportType, String bwzt, String oldPostDate,
			String oldbatch, String xmlFilePath, String zipFilePath, String zipNowPath, ExportOrderRepo exportOrderRepo,
			BigAmountRepo bigAmountRepo, ExportConfig exportConfig) throws Exception {
		if (blist.isEmpty()) {
			// log.info("根据日期，没有找到相关的反洗钱信息！");
			return false;
		}
		// zip包上限大小
		int size = exportConfig.getSize();
		// 按mirs(人工补正标识)分组
		Map<String, List<BigAmount>> map = getByDiffMirs(blist);
		boolean flag = false;
		for (String mirs : map.keySet()) {
			// 设置上限大小
			List<List<BigAmount>> Lists = new ArrayList<List<BigAmount>>();
			List<BigAmount> _list = map.get(mirs);
			if (_list.size() > size) {
				int len = blist.size() % size == 0 ? _list.size() / size : map.get(mirs).size() / size + 1;
				for (int i = 0; i < len; i++) {
					List<BigAmount> llist = _list.subList(i * size,
							(i + 1) * size > _list.size() ? _list.size() : (i + 1) * size);
					Lists.add(llist);
				}
			} else {
				Lists.add(_list);
			}

			for (List<BigAmount> list : Lists) {
				// 得到文件名
				String name = getName(branch, reportType, bwzt, oldPostDate, oldbatch, exportOrderRepo);
				String[] names = name.split("-");
				List<Tsdt> tsdts = this.getInfoFor_C(list);
				Element CHTR = new Element("CHTR");
				Element RBIF = this.setRBIF_C(branch, tsdts.size(), mirs);
				CHTR.addContent(RBIF);
				// 报告主体和交易信息 CTIFs
				Element TSDTs = new Element("TSDTs");
				int count = 1;
				for (Tsdt tsdt : tsdts) {
					Element TSDT = this.getElement(tsdt, "TSDT");
					TSDT.setAttribute("seqno", count + "");
					TSDTs.addContent(TSDT);
					count++;
				}
				CHTR.addContent(TSDTs);
				Document doc = new Document(CHTR);
				flag = xmlUtil(xmlFilePath + name + ".XML", doc);
				if (flag) {
					String zipPath = zipFilePath + name + ".ZIP";
					JZip.compress(xmlFilePath, true, zipPath);// 压缩xml
					FileOperate.getInstance().copyFile(zipPath, zipNowPath + name + ".ZIP");
					saveExportOrder(branch, ymdFormat.parse(names[1]), reportType, names[2], exportOrderRepo);
					// 修改数据批次 和文件名等
					if (list.size() > 0) {
						setBigAmountStatus(list, names[1], names[2], name);
						bigAmountRepo.save(list);
						// list.clear();
					}
					FileUtil.deleteDir(xmlFilePath);
					FileUtil.deleteDir(zipFilePath);
				}
			}
		}

		return flag;

	}

	// 按mirs(人工补正标识)分组
	private Map<String, List<BigAmount>> getByDiffMirs(List<BigAmount> list) {
		Map<String, List<BigAmount>> map = new HashMap<String, List<BigAmount>>();
		for (BigAmount amount : list) {
			if (map.containsKey(amount.getMirs())) {
				map.get(amount.getMirs()).add(amount);
			} else {
				ArrayList<BigAmount> _list = new ArrayList<BigAmount>();
				_list.add(amount);
				map.put(amount.getMirs(), _list);
			}
		}
		return map;
	}

	private Element setRBIF_C(Branch branch, int custmTotalNum, String mirs) {
		Element RICD, TSTN, MIRS;
		Element RBIF = new Element("RBIF");

		RICD = new Element("RICD");// 报告机构编码
		RICD.setText(branch.getAmlBranchCode());
		TSTN = new Element("TSTN");
		TSTN.setText(custmTotalNum + "");// 交易主体总数

		MIRS = new Element("MIRS");
		MIRS.setText(mirs);// 人工补正标识
		// 报告基本信息
		RBIF.addContent(RICD);
		RBIF.addContent(TSTN);
		RBIF.addContent(MIRS);
		return RBIF;
	}

	private List<Tsdt> getInfoFor_C(List<BigAmount> list) {
		ArrayList<Tsdt> tsdts = new ArrayList<Tsdt>();
		for (BigAmount amount : list) {
			Tsdt TSDT = getTSDT_C(amount);
			tsdts.add(TSDT);
		}
		return tsdts;
	}

	private Tsdt getTSDT_C(BigAmount amount) {
		Tsdt TSDT = this.getTSDT_N(amount);
		TSDT.setOCNM(amount.getCsnm());
		TSDT.setOTDT(ymdFormat.format(amount.getHtdt()));
		TSDT.setOTCD(amount.getCrcd());
		TSDT.setOTIC(amount.getTicd());
		return TSDT;
	}

	/*
	 * 大额删除报文
	 */

	public boolean writeDXML(Branch branch, List<BigAmount> blist, String reportType, String bwzt, String oldPostDate,
			String oldbatch, String xmlFilePath, String zipFilePath, String zipNowPath, ExportOrderRepo exportOrderRepo,
			BigAmountRepo bigAmountRepo, ExportConfig exportConfig) throws Exception {
		if (blist.isEmpty()) {
			// log.info("根据日期，没有找到相关的反洗钱信息！");
			return false;
		}
		// zip包上限大小
		int size = exportConfig.getSize();
		List<List<BigAmount>> Lists = new ArrayList<List<BigAmount>>();
		if (blist.size() > size) {
			int len = blist.size() % size == 0 ? blist.size() / size : blist.size() / size + 1;
			for (int i = 0; i < len; i++) {
				List<BigAmount> llist = blist.subList(i * size,
						(i + 1) * size > blist.size() ? blist.size() : (i + 1) * size);
				Lists.add(llist);
			}
		} else {
			Lists.add(blist);
		}
		boolean flag = false;
		for (List<BigAmount> list : Lists) {
			// 得到文件名
			String name = getName(branch, reportType, bwzt, oldPostDate, oldbatch, exportOrderRepo);
			String[] names = name.split("-");

			List<Dtdt> dtdts = this.getInfoFor_D(list, bwzt);
			Element DHTR = new Element("DHTR");
			Element RBIF = this.setRBIF_D(branch, dtdts.size());
			DHTR.addContent(RBIF);
			// 报告主体和交易信息 CTIFs
			Element DTDTs = new Element("DTDTs");
			int count = 1;
			for (Dtdt dtdt : dtdts) {
				Element DTDT = this.getElement(dtdt, "DTDT");
				DTDT.setAttribute("seqno", count + "");
				DTDTs.addContent(DTDT);
				count++;
			}
			DHTR.addContent(DTDTs);
			Document doc = new Document(DHTR);
			flag = xmlUtil(xmlFilePath + name + ".XML", doc);
			if (flag) {
				String zipPath = zipFilePath + name + ".ZIP";
				JZip.compress(xmlFilePath, true, zipPath);// 压缩xml
				FileOperate.getInstance().copyFile(zipPath, zipNowPath + name + ".ZIP");
				saveExportOrder(branch, ymdFormat.parse(names[1]), reportType, names[2], exportOrderRepo);
				// 修改数据批次 和文件名等
				if (list.size() > 0) {
					setBigAmountStatus(list, names[1], names[2], name);
					bigAmountRepo.save(list);
					// list.clear();
				}
				FileUtil.deleteDir(xmlFilePath);
				FileUtil.deleteDir(zipFilePath);
			}
		}
		return flag;

	}

	private Element setRBIF_D(Branch branch, int custmTotalNum) {
		Element RICD, DTTN;
		Element RBIF = new Element("RBIF");
		RICD = new Element("RICD");// 报告机构编码
		RICD.setText(branch.getAmlBranchCode());
		DTTN = new Element("DTTN");
		DTTN.setText(custmTotalNum + "");// 交易主体总数
		// 报告基本信息
		RBIF.addContent(RICD);
		RBIF.addContent(DTTN);
		return RBIF;
	}

	private List<Dtdt> getInfoFor_D(List<BigAmount> list, String bwzt) {
		List<Dtdt> DTDTs = new ArrayList<Dtdt>();
		for (BigAmount amount : list) {
			Dtdt DTDT = getDTDT_D(amount, bwzt);
			DTDTs.add(DTDT);
		}
		return DTDTs;
	}

	private Dtdt getDTDT_D(BigAmount amount, String bwzt) {
		Dtdt DTDT = new Dtdt();
		if (bwzt.equals("scbw")) {
			DTDT.setCSNM(amount.getCsnm());
			DTDT.setHTDT(ymdFormat.format(amount.getHtdt()));
			DTDT.setCRCD(amount.getCrcd());
			DTDT.setTICD(amount.getTicd());
		} else if (bwzt.equals("sjsc")) {
			DTDT.setCSNM("@N");
			DTDT.setHTDT(ymdFormat.format(amount.getHtdt()));
			DTDT.setCRCD("@N");
			DTDT.setTICD("@N");
		} else if (bwzt.equals("khsc")) {
			DTDT.setCSNM(amount.getCsnm());
			DTDT.setHTDT(ymdFormat.format(amount.getHtdt()));
			DTDT.setCRCD("@N");
			DTDT.setTICD("@N");
		} else if (bwzt.equals("tzsc")) {
			DTDT.setCSNM(amount.getCsnm());
			DTDT.setHTDT(ymdFormat.format(amount.getHtdt()));
			DTDT.setCRCD(amount.getCrcd());
			DTDT.setTICD("@N");
		}
		return DTDT;
	}

	private String getName(Branch branch, String reporttype, String bwzt, String oldpostdate, String oldbatch,
			ExportOrderRepo exportOrderRepo) {
		int zipNOrder = 0;
		Integer orderNum = 0;
		Date now = new Date();
		if (reporttype.equals("N")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "AMLNZIP");
		} else if (reporttype.equals("D")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "AMLDZIP");
		} else if (reporttype.equals("C")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "AMLCZIP");
		} else if (reporttype.equals("A")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "AMLAZIP");
		} else if (reporttype.equals("S")) {
			orderNum = exportOrderRepo.getOrderNum(branch.getBranchCode(), now, "AMLSZIP");
		}
		if (orderNum != null) {
			zipNOrder = orderNum.intValue() + 1;
		}
		String batch = this.getBathNo(String.valueOf(zipNOrder));

		String nowdates = ymdFormat.format(now);

		if (StringUtils.isNotEmpty(oldpostdate)) {
			oldpostdate = oldpostdate.replaceAll("-", "");
		}
		// 正常数据包
		if ("N".equalsIgnoreCase(reporttype)) {
			return "NBH" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		// 修改数据包
		if ("C".equalsIgnoreCase(reporttype)) {
			// 已入库
			if (StringUtils.isNotEmpty(bwzt) && bwzt.equals("bzhz")) {
				return "CBH" + branch.getAmlBranchCode() + "-" + oldpostdate + "-" + oldbatch;

			} else {
				return "CBH" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
			}
		}
		// 删除数据包
		if ("D".equalsIgnoreCase(reporttype)) {
			return "DBH" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		// 更正数据包
		if ("A".equalsIgnoreCase(reporttype)) {
			return "ABH" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		// 补充数据包
		if ("S".equalsIgnoreCase(reporttype)) {
			return "SBH" + branch.getAmlBranchCode() + "-" + nowdates + "-" + batch;
		}
		return null;
	}

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

	private void setBigAmountStatus(List<BigAmount> list, String nowdates, String zipbath, String xmlName) {
		// xmlName = xmlName.substring(xmlName.lastIndexOf("\\") + 1,
		// xmlName.indexOf(".XML"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (BigAmount amount : list) {
			if (StringUtils.isEmpty(amount.getSubmitBatch())) {
				amount.setSubmitBatch(zipbath);// 源报送批次
			} else {
				amount.setSubmitBatch(amount.getNowSubmitBatch());
			}
			amount.setNowSubmitBatch(zipbath);// 现报送批次
			amount.setTname(xmlName);
			try {
				amount.setOrpdt(sdf.parse(nowdates)); // 报送日期
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
			exportOrderZip.setOrderType("AMLNZIP");
		} else if (reportType.equals("C")) {
			exportOrderZip.setOrderType("AMLCZIP");
		} else if (reportType.equals("D")) {
			exportOrderZip.setOrderType("AMLDZIP");
		} else if (reportType.equals("A")) {
			exportOrderZip.setOrderType("AMLAZIP");
		} else if (reportType.equals("S")) {
			exportOrderZip.setOrderType("AMLSZIP");
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
					if (obj instanceof Htcr) {
						Element element = getElement((Htcr) obj, "HTCR");
						element.setAttribute("seqno", count + "");
						lElement.addContent(element);
						count++;
					}
					if (obj instanceof Ccif_t) {
						Element element = getElement((Ccif_t) obj, "CCIF");
						element.setAttribute("seqno", count + "");
						lElement.addContent(element);
						count++;
					}
					if (obj instanceof Tsdt) {
						Element element = getElement((Tsdt) obj, "TSDT");
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
			if (f.getType().toString().equals("class net.riking.util.xmlmodel.Cbif")) {
				Element cbifElement = getElement((Cbif) f.get(e), "CBIF");
				tElement.addContent(cbifElement);
			}
			if (f.getType().toString().equals("class net.riking.util.xmlmodel.Ccif_c")) {
				Element cbifElement = getElement((Ccif_c) f.get(e), "CCIF");
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
						if (obj instanceof Ccif_t) {
							Element element = getElement((Ccif_t) obj, "CCIF");
							lElement.addContent(element);
						}
						if (obj instanceof Tsdt) {
							Element element = getElement((Ccif_t) obj, "TSDT");
							lElement.addContent(element);
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
				}
				tElement.addContent(lElement);
			}
		}
		return tElement;
	}

	private boolean xmlUtil(String fileName, Document doc) {
		// 写xml文件头
		Format format = Format.getCompactFormat();
		format.setEncoding("utf-8");// 设置xml文件的字符为gb18030
		format.setIndent("    "); // 设置xml文件的缩进为4个空格
		XMLOutputter XMLOut = new XMLOutputter(format);// 元素后换行一层元素缩四格
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(fileName);
			XMLOut.output(doc, os);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// log.info(e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			// log.info(e);
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
}
