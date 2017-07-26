package net.riking.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.cache.RCache;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.CheckResult;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;

@Service("bigSuspCheckService")
public class BigSuspCheckServiceImpl {

	public final String NOTNULL = "不能为空";
	public final String MORELEN = "内容过长";
	public static final String TDF = "@NIE";
	SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
	
	private static RCache<List<String>> tiDaiFus = new RCache<List<String>>(60 * 5);

	private static RCache<Set<String>> cacheLibBig = new RCache<Set<String>>(60 * 5);
	private static Map<String, Set<String>> cacheTemBig = new HashMap<String, Set<String>>();

	private static RCache<Set<String>> cacheLibSusp = new RCache<Set<String>>(60 * 5);
	private static Map<String, Set<String>> cacheTemSusp = new HashMap<String, Set<String>>();
	
	
	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;

	private Set<String> getTicdByTstm(Date tstm, boolean isBig) {
		Date date = DateUtils.addDays(tstm, 1);
		Date start = null;
		Date end = null;
		try {
			start = sdf.parse(sdf.format(tstm));
			end = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Set<String> set = null;
		if (isBig) {
			set = bigAmountRepo.findTicdByTstm(start, end);
		} else {
			set = amlSuspiciousRepo.findTicdByTstm(start, end);
		}
		return set;
	}

	private void putLibBig(Date tstm) {
		cacheLibBig.put(sdf.format(tstm), getTicdByTstm(tstm, true));
	}

	public Set<String> getLibBig(Date tstm) {
		if (cacheLibBig.get(sdf.format(tstm)) != null) {
			return cacheLibBig.get(sdf.format(tstm));
		} else {
			this.putLibBig(tstm);
			return cacheLibBig.get(sdf.format(tstm));
		}
	}

	public void putTemBig(Date tstm, String ticd) {
		Set<String> set = cacheTemBig.get(sdf.format(tstm));
		if (set == null) {
			set = new HashSet<String>();
			set.add(ticd);
			cacheTemBig.put(sdf.format(tstm), set);
		} else {
			set.add(ticd);
		}
	}

	public Set<String> getTemBig(Date tstm) {
		if (cacheTemBig.get(sdf.format(tstm)) != null) {
			return cacheTemBig.get(sdf.format(tstm));
		} else {
			Set<String> set = new HashSet<String>();
			cacheTemBig.put(sdf.format(tstm), set);
			return cacheTemBig.get(sdf.format(tstm));
		}
	}

	public void destroyCacheTemBig() {
		cacheTemBig.clear();
	}

	private void putLibSusp(Date tstm) {
		cacheLibSusp.put(sdf.format(tstm), getTicdByTstm(tstm, false));
	}

	public Set<String> getLibSusp(Date tstm) {
		if (cacheLibSusp.get(sdf.format(tstm)) != null) {
			return cacheLibSusp.get(sdf.format(tstm));
		} else {
			this.putLibSusp(tstm);
			return cacheLibSusp.get(sdf.format(tstm));
		}
	}

	public void putTemSusp(Date tstm, String ticd) {
		Set<String> set = cacheTemSusp.get(sdf.format(tstm));
		if (set == null) {
			set = new HashSet<String>();
			set.add(ticd);
			cacheTemSusp.put(sdf.format(tstm), set);
		} else {
			set.add(ticd);
		}
	}

	public Set<String> getTemSusp(Date tstm) {
		if (cacheTemSusp.get(sdf.format(tstm)) != null) {
			return cacheTemSusp.get(sdf.format(tstm));
		} else {
			Set<String> set = new HashSet<String>();
			cacheTemSusp.put(sdf.format(tstm), set);
			return cacheTemSusp.get(sdf.format(tstm));
		}
	}

	public void destroyCacheTemSusp() {
		cacheTemSusp.clear();
	}

	public boolean checkTicd(Date tstm, String ticd, boolean isBig) {
		if (isBig) {// 是否是大额交易
			if (this.getLibBig(tstm).contains(ticd)) {
				return true;
			}
			if (this.getTemBig(tstm).contains(ticd)) {
				return true;
			}
			this.putTemBig(tstm, ticd);
		} else {
			if (this.getLibSusp(tstm).contains(ticd)) {
				return true;
			}
			if (this.getTemSusp(tstm).contains(ticd)) {
				return true;
			}
			this.putTemSusp(tstm, ticd);
		}
		return false;
	}
	
	private void putTiDaiFu(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("@N");
		list.add("@I");
		list.add("@E");
		tiDaiFus.put(TDF, list);
	}
	
	private List<String> getTiDaiFu(){
		if(tiDaiFus.get(TDF)==null){
			this.putTiDaiFu();
		}
		return tiDaiFus.get(TDF);
	}
	
	public CheckResult checkBigAmount(BigAmount amount, Boolean isDataCollect) {
		List<String> list = amountCheckNull(amount);
		if (list.size() == 0) {
			list = amountCheckContent(amount, isDataCollect);
		}
		StringBuilder sb = new StringBuilder();
		if (list.size() != 0 && list != null) {
			for (String str : list) {
				sb.append(str + ",");
			}
		}
		CheckResult result = new CheckResult();
		result.setCheckId(amount.getId());
		result.setCheckDate(new Date());
		result.setFlag(2);
		result.setType("2");
		String reason = sb.toString();
		if (StringUtils.isNotEmpty(reason)) {// 去掉最后一个逗号
			result.setReason(reason.substring(0, reason.length() - 1));
		}

		return result;
	}

	public CheckResult checkSuspicious(AmlSuspicious suspicious, boolean isDataCollect) {
		List<String> list = suspCheckNull(suspicious);
		if (list.size() == 0) {
			list = suspCheckContent(suspicious, isDataCollect);
		}
		StringBuilder sb = new StringBuilder();
		if (list.size() != 0 && list != null) {
			for (String str : list) {
				sb.append(str + ",");
			}
		}
		CheckResult result = new CheckResult();
		result.setCheckId(suspicious.getId());
		result.setCheckDate(new Date());
		result.setFlag(2);
		result.setType("2");
		String reason = sb.toString();
		if (StringUtils.isNotEmpty(reason)) {// 去掉最后一个逗号
			result.setReason(reason.substring(0, reason.length() - 1));
		}
		return result;
	}

	// 大额非空校验
	private List<String> amountCheckNull(BigAmount amount) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(amount.getRicd())) {
			list.add("ricd:金融机构代码" + NOTNULL);
		} else {
			if (checkLength(amount.getRicd(), 16)) {
				list.add("ricd:金融机构代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getFinc())) {
			list.add("finc:报告金融机构网点代码" + NOTNULL);
		} else {
			if (checkLength(amount.getFinc(), 16)) {
				list.add("finc:报告金融机构网点代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getRlfc())) {
			list.add("rlfc:金融机构与客户的关系" + NOTNULL);
		} else {
			if (checkLength(amount.getRlfc(), 2)) {
				list.add("rlfc:金融机构与客户的关系" + MORELEN);
			}
		}
		if (amount.getHtdt() == null) {
			list.add("htdt:大额交易发生日期" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getCrcd())) {
			list.add("crcd:大额交易特征代码" + NOTNULL);
		} else {
			if (checkLength(amount.getCrcd(), 4)) {
				list.add("crcd:大额交易特征代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCsnm())) {
			list.add("csnm:客户编号" + NOTNULL);
		} else {
			if (checkLength(amount.getCsnm(), 32)) {
				list.add("csnm:客户编号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCitp())) {
			list.add("citp:客户身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(amount.getCitp(), 6)) {
				list.add("citp:客户身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOitp1())) {
			list.add("oitp1:客户其他证件类型说明" + NOTNULL);
		} else {
			if (checkLength(amount.getOitp1(), 32)) {
				list.add("oitp1:客户其他证件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtid())) {
			list.add("ctid:客户身份证件/证明文件号码" + NOTNULL);
		} else {
			if (checkLength(amount.getCtid(), 128)) {
				list.add("ctid:客户身份证件/证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtnt())) {
			list.add("ctnt:客户国籍" + NOTNULL);
		} else {
			if (checkLength(amount.getCtnt(), 30)) {
				list.add("ctnt:客户国籍" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtvc())) {
			list.add("ctvc:客户职业（对私）或客户行业（对公）" + NOTNULL);
		} else {
			if (checkLength(amount.getCtvc(), 32)) {
				list.add("ctvc:客户职业（对私）或客户行业（对公）" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCctl())) {
			list.add("cctl:客户联系电话" + NOTNULL);
		} else {
			if (checkLength(amount.getCctl(), 64)) {
				list.add("cctl:客户联系电话" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCcei())) {
			list.add("ccei:客户其他联系方式" + NOTNULL);
		} else {
			if (checkLength(amount.getCcei(), 512)) {
				list.add("ccei:客户其他联系方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtar())) {
			list.add("ctar:客户住址/经营地址" + NOTNULL);
		} else {
			if (checkLength(amount.getCtar(), 512)) {
				list.add("ctar:客户住址/经营地址" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtnm())) {
			list.add("ctnm:客户名称/姓名" + NOTNULL);
		} else {
			if (checkLength(amount.getCtnm(), 512)) {
				list.add("ctnm:客户名称/姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCatp())) {
			list.add("catp:账户类型" + NOTNULL);
		} else {
			if (checkLength(amount.getCatp(), 6)) {
				list.add("catp:账户类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCtac())) {
			list.add("ctac:账号" + NOTNULL);
		} else {
			if (checkLength(amount.getCtac(), 64)) {
				list.add("ctac:账号" + MORELEN);
			}
		}
		if (amount.getOatm() == null) {
			list.add("oatm:客户开户时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getCbct())) {
			list.add("cbct:银行卡类型" + NOTNULL);
		} else {
			if (checkLength(amount.getCbct(), 2)) {
				list.add("cbct:银行卡类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOcbt())) {
			list.add("ocbt:客户银行卡其他类型说明" + NOTNULL);
		} else {
			if (checkLength(amount.getOcbt(), 32)) {
				list.add("ocbt:客户银行卡其他类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCbcn())) {
			list.add("cbcn:银行卡号码" + NOTNULL);
		} else {
			if (checkLength(amount.getCbcn(), 64)) {
				list.add("cbcn:银行卡号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTbnm())) {
			list.add("tbnm:交易代办人姓名" + NOTNULL);
		} else {
			if (checkLength(amount.getTbnm(), 128)) {
				list.add("tbnm:交易代办人姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTbit())) {
			list.add("tbit:交易代办人身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(amount.getTbit(), 6)) {
				list.add("tbit:交易代办人身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOitp2())) {
			list.add("oitp2:代办人其他证件类型说明" + NOTNULL);
		} else {
			if (checkLength(amount.getOitp2(), 32)) {
				list.add("oitp2:代办人其他证件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTbid())) {
			list.add("tbid:交易代办人身份证件/证明文件号码" + NOTNULL);
		} else {
			if (checkLength(amount.getTbid(), 128)) {
				list.add("tbid:交易代办人身份证件/证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTbnt())) {
			list.add("tbnt:交易代办人国籍" + NOTNULL);
		} else {
			if (checkLength(amount.getTbnt(), 3)) {
				list.add("tbnt:交易代办人国籍" + MORELEN);
			}
		}
		if (amount.getTstm() == null) {
			list.add("tstm:交易时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getTrcd())) {
			list.add("trcd:交易发生地" + NOTNULL);
		} else {
			if (checkLength(amount.getTrcd(), 9)) {
				list.add("trcd:交易发生地" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTicd())) {
			list.add("ticd:业务标识号" + NOTNULL);
		} else {
			if (checkLength(amount.getTicd(), 256)) {
				list.add("ticd:业务标识号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getRpmt())) {
			list.add("rpmt:收付款方匹配号类型" + NOTNULL);
		} else {
			if (checkLength(amount.getRpmt(), 2)) {
				list.add("rpmt:收付款方匹配号类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getRpmn())) {
			list.add("rpmn:收付款方匹配号" + NOTNULL);
		} else {
			if (checkLength(amount.getRpmn(), 500)) {
				list.add("rpmn:收付款方匹配号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTstp2())) {
			list.add("tstp:交易方式" + NOTNULL);
		} else {
			if (checkLength(amount.getTstp2(), 6)) {
				list.add("tstp:交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOctt())) {
			list.add("octt:非柜台交易方式" + NOTNULL);
		} else {
			if (checkLength(amount.getOctt(), 2)) {
				list.add("octt:非柜台交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOoct())) {
			list.add("ooct:其他非柜台交易方式" + NOTNULL);
		} else {
			if (checkLength(amount.getOoct(), 32)) {
				list.add("ooct:其他非柜台交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOcec())) {
			list.add("ocec:非柜台交易方式的设备代码" + NOTNULL);
		} else {
			if (checkLength(amount.getOcec(), 500)) {
				list.add("ocec:非柜台交易方式的设备代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getBptc())) {
			list.add("bptc:银行与支付机构之间的业务交易编码" + NOTNULL);
		} else {
			if (checkLength(amount.getBptc(), 500)) {
				list.add("bptc:银行与支付机构之间的业务交易编码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTsct())) {
			list.add("tsct:涉外收支交易分类与代码" + NOTNULL);
		} else {
			if (checkLength(amount.getTsct(), 16)) {
				list.add("tsct:涉外收支交易分类与代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTsdr())) {
			list.add("tsdr:收/付类型" + NOTNULL);
		} else {
			if (checkLength(amount.getTsdr(), 2)) {
				list.add("tsdr:收/付类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCrpp())) {
			list.add("crpp:资金用途" + NOTNULL);
		} else {
			if (checkLength(amount.getCrpp(), 128)) {
				list.add("crpp:资金用途" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCrtp())) {
			list.add("crtp:交易币种" + NOTNULL);
		} else {
			if (checkLength(amount.getCrtp(), 3)) {
				list.add("crtp:交易币种" + MORELEN);
			}
		}
		if (amount.getCrat() == null) {
			list.add("crat:交易金额" + NOTNULL);
		} else {
			if (checkLength(amount.getCrat().toString(), 20)) {
				list.add("crat:交易金额" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCfin())) {
			list.add("cfin:对方金融机构网点名称" + NOTNULL);
		} else {
			if (checkLength(amount.getCfin(), 64)) {
				list.add("cfin:对方金融机构网点名称" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCfct())) {
			list.add("cfct:对方金融机构网点代码类型" + NOTNULL);
		} else {
			if (checkLength(amount.getCfct(), 2)) {
				list.add("cfct:对方金融机构网点代码类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCfic())) {
			list.add("cfic:对方金融机构网点代码" + NOTNULL);
		} else {
			if (checkLength(amount.getCfic(), 16)) {
				list.add("cfic:对方金融机构网点代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getCfrc())) {
			list.add("cfrc:对方金融机构网点行政区划代码" + NOTNULL);
		} else {
			if (checkLength(amount.getCfrc(), 9)) {
				list.add("cfrc:对方金融机构网点行政区划代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTcnm())) {
			list.add("tcnm:交易对手名称/姓名" + NOTNULL);
		} else {
			if (checkLength(amount.getTcnm(), 128)) {
				list.add("tcnm:交易对手名称/姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTcit())) {
			list.add("tcit:交易对手身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(amount.getTcit(), 6)) {
				list.add("tcit:交易对手身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getOitp3())) {
			list.add("oitp3:交易对手其他证件类型说明" + NOTNULL);
		} else {
			if (checkLength(amount.getOitp3(), 32)) {
				list.add("oitp3:交易对手其他证件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTcid())) {
			list.add("tcid:交易对手证件号码" + NOTNULL);
		} else {
			if (checkLength(amount.getTcid(), 128)) {
				list.add("tcid:交易对手证件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTcat())) {
			list.add("tcat:交易对手账号类型" + NOTNULL);
		} else {
			if (checkLength(amount.getTcat(), 6)) {
				list.add("tcat:交易对手账号类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getTcac())) {
			list.add("tcac:交易对手账号" + NOTNULL);
		} else {
			if (checkLength(amount.getOitp1(), 64)) {
				list.add("tcac:交易对手账号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getRotf1())) {
			list.add("rotf1:交易信息备注1" + NOTNULL);
		} else {
			if (checkLength(amount.getRotf1(), 64)) {
				list.add("rotf1:交易信息备注1" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getRotf2())) {
			list.add("rotf2:交易信息备注2" + NOTNULL);
		} else {
			if (checkLength(amount.getRotf2(), 64)) {
				list.add("rotf2:交易信息备注2" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(amount.getMirs())) {
			list.add("mirs: 人工补正标识" + NOTNULL);
		} else {
			if (checkLength(amount.getMirs(), 64)) {
				list.add("mirs: 人工补正标识" + MORELEN);
			}
		}
		if (amount.getRpdt() == null) {
			list.add("rpdt:采集时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getBwzt())) {
			list.add("bwzt:报文状态" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getReportType())) {
			list.add("reportType:报文类型" + NOTNULL);
		}
		if (StringUtils.isEmpty(amount.getSubmitType())) {
			list.add("submitType:是否上报" + NOTNULL);
		}
		if (StringUtils.isNotEmpty(amount.getSubmitType()) && amount.getSubmitType().equals("N")
				&& StringUtils.isEmpty(amount.getRemarks())) {
			list.add("remarks:上报备注" + NOTNULL);
		}
		return list;
	}

	// 大额内容校验
	private List<String> amountCheckContent(BigAmount amount, boolean isDataCollect) {
		List<String> _list = new ArrayList<String>();
		// 交易日期
		Date htdt = amount.getHtdt();
		Date tstm = amount.getTstm();
		if (!DateUtils.isSameDay(htdt, tstm)) {
			_list.add("htdt:交易日期与大额交易发生日期不一致");
		}else if(tstm.getTime()>(new Date()).getTime()){
			_list.add("tstm,htdt:交易日期与大额交易发生日期不得超过当前时间");
		}
		//客户帐户开立时间
		Date oatm = amount.getOatm();
		if(oatm.getTime()>htdt.getTime()){
			_list.add("oatm:客户帐户开立时间不得在大额交易发生日期之后");
		}
		// 联系电话校验
		String cctl = amount.getCctl();
		String[] cctls = cctl.split(",", -1);
		if (checkRepeat(cctls)) {// 重复校验
			_list.add("cctl:客户联系电话重复");
		} else {
			for (int i = 0; i < cctls.length; i++) {
				if (!checkCellphone(cctls[i]) && !checkTelephone(cctls[i])) {
					if(cctls.length==1&&!getTiDaiFu().contains(cctls[i])){
						_list.add("cctl:客户联系电话格式错误");
					}else if(cctls.length>1){
						_list.add("cctl:客户联系电话格式错误");
					}
				}
			}
		}
		// 客户国籍
		String ctnt = amount.getCtnt();
		String[] ctnts = ctnt.split(",", -1);
		if (checkRepeat(ctnts)) {// 重复校验
			_list.add("ctnt:客户国籍重复");
		}
		// 证件类型说明校验//证件号码校验
		String citp = amount.getCitp();
		String oitp1 = amount.getOitp1();
		if (checkZjlxsm(citp, oitp1)) {
			_list.add("oitp1:客户身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		// 客户证件号码
		String ctid = amount.getCtid();
		if (citp.equals("110001") || citp.equals("110003")) {
			if (!checkId(ctid)) {
				_list.add("ctid:客户身份证件/证明文件号码错误");
			}
		} else if (citp.equals("610001")) {// 全国组织机构代码610001
			if (ctid.length() != 9) {
				_list.add("ctid:组织机构代码长度应该为9位");
			}
		}
		// 代办人
		String tbit = amount.getTbit();
		String oitp2 = amount.getOitp2();
		if (checkZjlxsm(tbit, oitp2)) {
			_list.add("oitp2:代办人身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		String tbid = amount.getTbid();
		if (tbit.equals("110001") || tbit.equals("110003")) {
			if (!checkId(tbid)) {
				_list.add("tbid:代办人身份证件/证明文件号码错误");
			}
		} else if (tbit.equals("610001")) {// 全国组织机构代码610001
			if (tbid.length() != 9) {
				_list.add("tbid:组织机构代码长度应该为9位");
			}
		}
		// 交易对手
		String tcit = amount.getTcit();
		String oitp3 = amount.getOitp3();
		if (checkZjlxsm(tcit, oitp3)) {
			_list.add("oitp3:交易对手身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		String tcid = amount.getTcid();
		if (tcit.equals("110001") || tcit.equals("110003")) {
			if (!checkId(tcid)) {
				_list.add("tcid:交易对手身份证件/证明文件号码错误");
			}
		} else if (tcit.equals("610001")) {// 全国组织机构代码610001
			if (tcid.length() != 9) {
				_list.add("tcid:组织机构代码长度应该为9位");
			}
		}
		// 交易金额
		BigDecimal crat = amount.getCrat();
		String cratStr = crat.toString();
		if (cratStr.length() - (cratStr.indexOf(".") != -1 ? cratStr.indexOf(".") + 1 : cratStr.length()) > 3) {
			_list.add("crat:交易金额小数点后最多保留3位小数");
		}
		// 客户银行卡类型说明校验
		String cbct = amount.getCbct();
		String ocbt = amount.getOcbt();
		if (cbct.equals("90") && this.getTiDaiFu().contains(ocbt)) {
			_list.add("ocbt:客户银行卡类型为“90”时，银行卡类型说明必填。");
		}
		//账号和银行卡号交易 
		String catp = amount.getCatp();//账户类型
		String ctac = amount.getCtac();//账号
		String cbcn = amount.getCbcn();//银行卡号码
		if(getTiDaiFu().contains(ctac)&&getTiDaiFu().contains(cbcn)){
			_list.add("ctac,cbcn:账号和银行卡号码不能同时都为替代符。");
		}else{
			if(getTiDaiFu().contains(ctac)){
				if(!ctac.equals(catp)){
					_list.add("ctac,catp:账号和账户类型的替代符不一致。");
				}
			}else{
				if(getTiDaiFu().contains(catp)){
					_list.add("catp:账号不为替代符时，账户类型不能是替代符。");
				}
			}
			if(getTiDaiFu().contains(cbcn)){
				if(!cbcn.equals(cbct)){
					_list.add("cbcn,cbct:银行卡号码和银行卡类型的替代符不一致。");
				}
			}else{
				if(getTiDaiFu().contains(cbct)){
					_list.add("cbct:银行卡号码不为替代符时，银行卡类型不能是替代符。");
				}
			}
		}
		
		// 非柜台交易方式
		String octt = amount.getOctt();
		String ooct = amount.getOoct();
		if (octt.equals("99") && ooct.equals("@N")) {
			_list.add("ooct:非柜台交易方式为“90”时，银行卡类型说明必填。");
		}

		// 业务标识号校验
		String ticd = amount.getTicd();
		if (isDataCollect) {
			if (checkTicd(tstm, ticd, true)) {
				_list.add("ticd:业务标示号重复");
			}
		} else {
			Long id = amount.getId();
			Date date = DateUtils.addDays(tstm, 1);
			Date start = null;
			Date end = null;
			try {
				start = sdf.parse(sdf.format(tstm));
				end = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<BigAmount> list = bigAmountRepo.findByTicdAndTstmBetweenAndDeleteStateAndIdNot(ticd, start, end, "1",
					id == null ? 0L : id);
			if (list != null && list.size() > 0) {
				_list.add("ticd:业务标示号重复");
			}
		}
		
		/*
		 * if ((StringUtils.isEmpty(ticd) || ticd.getBytes().length > 64)) {
		 * _list.add("ticd:业务标示号不能为空或格式不正确"); } String tstp = amount.getTstp();
		 * String trcd = amount.getTrcd(); if (tstp.length() != 6) {
		 * _list.add("tstp:交易方式未填完整"); } else { String tstp1_2 =
		 * tstp.substring(0, 2); String tstp3_4 = tstp.substring(2, 4); String
		 * tstp5_6 = tstp.substring(4); String tstp3_ = tstp.substring(2);
		 * boolean flag = true; if (tstp1_2.equals("00")) { if
		 * (tstp3_4.equals("00") && ((!tstp5_6.equals("11")) ||
		 * (!tstp5_6.equals("15")) || (!tstp5_6.equals("16")) ||
		 * (!tstp5_6.equals("17")))) { flag = false; } } else if
		 * (tstp1_2.equals("01")) { if (tstp3_.equals("0001") ||
		 * tstp3_.equals("0002") || tstp3_.equals("0003") ||
		 * tstp3_.equals("0004") || tstp3_.equals("0111")) { flag = false; } }
		 * else if (tstp1_2.equals("02")) { if (tstp3_.equals("0001") ||
		 * tstp3_.equals("0002") || tstp3_.equals("0003") ||
		 * tstp3_.equals("0004") || tstp3_.equals("0005")) { flag = false; } }
		 * if (flag) { if (trcd.equals("@I") || trcd.equals("@N") ||
		 * trcd.equals("@E")) { _list.add("trcd:当交易方式为现金类交易时必须按境内真实交易发生地填写"); }
		 * } } if (trcd.getBytes().length < 3) { _list.add("trcd:交易发生地格式长度不正确");
		 * } // 账号校验 String ctac = amount.getCtac(); if
		 * (StringUtils.isNotEmpty(ctac)) { if
		 * (!ctac.matches("^[a-zA-Z0-9\\-]+$") ||
		 * ctac.matches("[^\\x00-\\xff]")) {
		 * _list.add("ctac:账号中不得含有除数字、字母、“-”之外的字符，且不得使用全角字符"); } boolean flag =
		 * true; for (int i = 1; i < ctac.length(); i++) { if (ctac.charAt(i -
		 * 1) != ctac.charAt(i)) { flag = false; } } if (flag) {
		 * _list.add("ctac:账号字段不得全部为连续相同的数字。"); } } // 客户名称校验 String ctnm =
		 * amount.getCtnm(); if (ctnm.matches("^\\d+$")) {
		 * _list.add("ctnm:客户名称不能仅由数字组成"); } if (!spList.contains(ctnm)) { if
		 * (citp.equals("21") || citp.equals("29")) { if (ctnm.getBytes().length
		 * < 8) { _list.add("ctnm:客户类别为对公客户，则客户名称字段长度不得少于8个字节。"); } } else { if
		 * (ctnm.getBytes().length < 4) { _list.add("ctnm:客户姓名字段的长度不得少于4个字节"); }
		 * } }
		 * 
		 * 
		 * // 客户国籍 String ctnt = amount.getCtnt(); // 人或组织信息完整性校验 boolean b1 =
		 * this.infoFull(ctnm, citp, ctid, ctnt); if (b1) { _list.add(
		 * "ctnm;citp;ctid;ctnt:姓名，身份证件/证明文件类型，身份证件/证明文件号码，国籍可填写替代符号，但这四项中有一项非替代符号，则其他项不得填写替代符号。"
		 * ); } // 代办人信息 String tbnm = amount.getTbnm(); String tbnt =
		 * amount.getTbnt(); // 人或组织信息完整性校验 boolean b2 = this.infoFull(tbnm,
		 * tbid, tbit, tbnt); if (b2) { _list.add(
		 * "tbnm;tbit;tbid;tbnt:姓名，身份证件/证明文件类型，身份证件/证明文件号码，国籍可填写替代符号，但这四项中有一项非替代符号，则其他项不得填写替代符号。"
		 * ); }
		 * 
		 * 
		 * // 交易对手名称 String tcnm = amount.getTcnm(); if (tcnm.equals("未知") ||
		 * isNumeric(tcnm) || tcnm.trim() == null) {
		 * _list.add("tcnm:交易对手名称不能仅为数字、空格或“未知”"); }
		 * 
		 * // 对方网点代码类型 String cfct = amount.getCfct(); if (!(cfct.equals("11")
		 * || cfct.equals("12") || cfct.equals("13") || cfct.equals("14"))) {
		 * _list.add(
		 * "cfct:对方网点代码类型必须是（11：现代化支付系统行号；12：人民币结算账户管理系统行号；13：银行内部机构号；14：其他）");
		 * }
		 */
		return _list;
	}

	// 可疑非空校验
	private List<String> suspCheckNull(AmlSuspicious suspicious) {
		ArrayList<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(suspicious.getRicd())) {
			list.add("ricd:报告机构代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRicd(), 14)) {
				list.add("ricd:报告机构代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRpnc())) {
			list.add("rpnc:上报网点代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRpnc(), 16)) {
				list.add("rpnc:上报网点代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getFinc())) {
			list.add("finc:金融机构代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getFinc(), 16)) {
				list.add("finc:金融机构代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRlfc())) {
			list.add("rlfc:金融机构与客户的关系" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRlfc(), 2)) {
				list.add("rlfc:金融机构与客户的关系" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getDetr())) {
			list.add("detr:可疑交易报告紧急程度" + NOTNULL);
		} else {
			if (checkLength(suspicious.getDetr(), 2)) {
				list.add("detr:可疑交易报告紧急程度" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTorp())) {
			list.add("torp:报送次数标志" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTorp(), 5)) {
				list.add("torp:报送次数标志" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOrxn())) {
			list.add("orxn:初次报送的可疑交易报告报文名称" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOrxn(), 64)) {
				list.add("orxn:初次报送的可疑交易报告报文名称" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRpnm())) {
			list.add("rpnm:可疑交易报告的填报人员" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRpnm(), 16)) {
				list.add("rpnm:可疑交易报告的填报人员" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCsnm1())) {
			list.add("csnm1:可疑主体客户号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCsnm1(), 32)) {
				list.add("csnm1:可疑主体客户号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSevc())) {
			list.add("sevc:可疑主体职业（对私）或行业（对公）" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSevc(), 32)) {
				list.add("sevc:可疑主体职业（对私）或行业（对公）" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSenm())) {
			list.add("senm:可疑主体姓名/名称" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSenm(), 512)) {
				list.add("senm:可疑主体姓名/名称" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSetp())) {
			list.add("setp:可疑主体身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSetp(), 6)) {
				list.add("setp:可疑主体身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOitp1())) {
			list.add("oitp1:可疑主体其他身份证件证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOitp1(), 32)) {
				list.add("oitp1:可疑主体其他身份证件证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSeid())) {
			list.add("seid:可疑主体身份证件/证明文件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSeid(), 128)) {
				list.add("seid:可疑主体身份证件/证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getStnt())) {
			list.add("stnt:可疑主体国籍" + NOTNULL);
		} else {
			if (checkLength(suspicious.getStnt(), 30)) {
				list.add("stnt:可疑主体国籍" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSctl())) {
			list.add("sctl:可疑主体联系电话" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSctl(), 64)) {
				list.add("sctl:可疑主体联系电话" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSear())) {
			list.add("sear:可疑主体住址/经营地址" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSear(), 512)) {
				list.add("sear:可疑主体住址/经营地址" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSeei())) {
			list.add("seei:可疑主体其他联系方式" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSeei(), 512)) {
				list.add("seei:可疑主体其他联系方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSrnm())) {
			list.add("srnm:可疑主体法定代表人姓名" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSrnm(), 512)) {
				list.add("srnm:可疑主体法定代表人姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSrit())) {
			list.add("srit:可疑主体法定代表人身份证件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSrit(), 6)) {
				list.add("srit:可疑主体法定代表人身份证件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOrit())) {
			list.add("orit:可疑主体法定代表人其他身份证件证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOrit(), 32)) {
				list.add("orit:可疑主体法定代表人其他身份证件证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getSrid())) {
			list.add("srid:可疑主体法定代表人身份证件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getSrid(), 128)) {
				list.add("srid:可疑主体法定代表人身份证件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getScnm())) {
			list.add("scnm:可疑主体控股股东或实际控制人名称" + NOTNULL);
		} else {
			if (checkLength(suspicious.getScnm(), 512)) {
				list.add("scnm:可疑主体控股股东或实际控制人名称" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getScit())) {
			list.add("scit:可疑主体控股股东或实际控制人身份证件证明文件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getScit(), 6)) {
				list.add("scit:可疑主体控股股东或实际控制人身份证件证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOcit())) {
			list.add("ocit:可疑主体控股股东或实际控制人其他身份证件证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOcit(), 32)) {
				list.add("ocit:可疑主体控股股东或实际控制人其他身份证件证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getScid())) {
			list.add("scid:可疑主体控股股东或实际控制人身份证件证明文件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getScid(), 128)) {
				list.add("scid:可疑主体控股股东或实际控制人身份证件证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCtnm())) {
			list.add("ctnm:客户名称/姓名" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCtnm(), 512)) {
				list.add("ctnm:客户名称/姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCitp())) {
			list.add("citp:客户身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCitp(), 6)) {
				list.add("citp:客户身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOitp2())) {
			list.add("oitp2:客户其他身份证件/证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOitp2(), 32)) {
				list.add("oitp2:客户其他身份证件/证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCtid())) {
			list.add("ctid:客户身份证件/证明文件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCtid(), 128)) {
				list.add("ctid:客户身份证件/证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCsnm2())) {
			list.add("csnm2: 客户号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCsnm2(), 32)) {
				list.add("csnm2: 客户号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCatp())) {
			list.add("catp:客户账户类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCatp(), 6)) {
				list.add("catp:客户账户类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCtac())) {
			list.add("ctac:客户账号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCtac(), 64)) {
				list.add("ctac:客户账号" + MORELEN);
			}
		}
		if (suspicious.getOatm() == null) {
			list.add("oatm:客户账户开立时间" + NOTNULL);
		}
		if (suspicious.getCatm() == null) {
			list.add("catm:客户账户销户时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(suspicious.getCbct())) {
			list.add("cbct:客户银行卡类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCbct(), 2)) {
				list.add("cbct:客户银行卡类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOcbt())) {
			list.add("ocbt:客户银行卡其他类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOcbt(), 32)) {
				list.add("ocbt:客户银行卡其他类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCbcn())) {
			list.add("cbcn:银行卡号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCbcn(), 64)) {
				list.add("cbcn:银行卡号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTbnm())) {
			list.add("tbnm:交易代办人姓名" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTbnm(), 128)) {
				list.add("tbnm:交易代办人姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTbit())) {
			list.add("tbit:交易代办人身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTbit(), 6)) {
				list.add("tbit:交易代办人身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOitp3())) {
			list.add("oitp3:代办人其他身份证件证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOitp3(), 32)) {
				list.add("oitp3:代办人其他身份证件证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTbid())) {
			list.add("tbid:交易代办人身份证件/证明文件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTbid(), 128)) {
				list.add("tbid:交易代办人身份证件/证明文件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTbnt())) {
			list.add("tbnt:交易代办人国籍" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTbnt(), 3)) {
				list.add("tbnt:交易代办人国籍" + MORELEN);
			}
		}
		if (suspicious.getTstm() == null) {
			list.add("tstm:交易时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(suspicious.getTrcd())) {
			list.add("trcd:交易发生地" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTrcd(), 9)) {
				list.add("trcd:交易发生地" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTicd())) {
			list.add("ticd:业务标识号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTicd(), 256)) {
				list.add("ticd:业务标识号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRpmt())) {
			list.add("rpmt:收付款方匹配号类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRpmt(), 2)) {
				list.add("rpmt:收付款方匹配号类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRpmn())) {
			list.add("rpmn:收付款方匹配号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRpmn(), 500)) {
				list.add("rpmn:收付款方匹配号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTstp2())) {
			list.add("tstp:交易方式" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTstp2(), 6)) {
				list.add("tstp:交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOctt())) {
			list.add("octt:非柜台交易方式" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOctt(), 2)) {
				list.add("octt:非柜台交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOoct())) {
			list.add("ooct:其他非柜台交易方式" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOoct(), 32)) {
				list.add("ooct:其他非柜台交易方式" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOcec())) {
			list.add("ocec:非柜台交易方式的设备代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOcec(), 500)) {
				list.add("ocec:非柜台交易方式的设备代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getBptc())) {
			list.add("bptc:银行与支付机构之间的业务交易编码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getBptc(), 500)) {
				list.add("bptc:银行与支付机构之间的业务交易编码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTsct())) {
			list.add("tsct:涉外收支交易分类与代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTsct(), 16)) {
				list.add("tsct:涉外收支交易分类与代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTsdr())) {
			list.add("tsdr:资金收付标志" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTsdr(), 2)) {
				list.add("tsdr:资金收付标志" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCrsp())) {
			list.add("crsp:资金来源或用途" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCrsp(), 128)) {
				list.add("crsp:资金来源或用途" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCrtp())) {
			list.add("crtp:交易币种" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCrtp(), 3)) {
				list.add("crtp:交易币种" + MORELEN);
			}
		}
		if (suspicious.getCrat() == null) {
			list.add("crat:交易金额" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCrat().toString(), 20)) {
				list.add("crat:交易金额" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCfin())) {
			list.add("cfin:对方金融机构网点名称" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCfin(), 64)) {
				list.add("cfin:对方金融机构网点名称" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCfct())) {
			list.add("cfct:对方金融机构网点代码类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCfct(), 2)) {
				list.add("cfct:对方金融机构网点代码类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCfic())) {
			list.add("cfic:对方金融机构网点代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCfic(), 16)) {
				list.add("cfic:对方金融机构网点代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getCfrc())) {
			list.add("cfrc:对方金融机构网点行政区划代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getCfrc(), 9)) {
				list.add("cfrc:对方金融机构网点行政区划代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTcnm())) {
			list.add("tcnm:交易对手名称/姓名" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTcnm(), 128)) {
				list.add("tcnm:交易对手名称/姓名" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTcit())) {
			list.add("tcit:交易对手身份证件/证明文件类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTcit(), 6)) {
				list.add("tcit:交易对手身份证件/证明文件类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOitp4())) {
			list.add("oitp4:交易对手其他身份证件/证明文件类型说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOitp4(), 32)) {
				list.add("oitp4:交易对手其他身份证件/证明文件类型说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTcid())) {
			list.add("tcid:交易对手证件号码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTcid(), 128)) {
				list.add("tcid:交易对手证件号码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTcat())) {
			list.add("tcat:交易对手账号类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTcat(), 6)) {
				list.add("tcat:交易对手账号类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTcac())) {
			list.add("tcac:交易对手账号" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTcac(), 64)) {
				list.add("tcac:交易对手账号" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRotf1())) {
			list.add("rotf1:交易信息备注1" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRotf1(), 64)) {
				list.add("rotf1:交易信息备注1" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getRotf2())) {
			list.add("rotf2:交易信息备注2" + NOTNULL);
		} else {
			if (checkLength(suspicious.getRotf2(), 64)) {
				list.add("rotf2:交易信息备注2" + MORELEN);
			}
		}
		if (suspicious.getRpdt() == null) {
			list.add("rpdt:采集时间" + NOTNULL);
		}
		if (StringUtils.isEmpty(suspicious.getDorp())) {
			list.add("dorp:报送方向" + NOTNULL);
		} else {
			if (checkLength(suspicious.getDorp(), 2)) {
				list.add("dorp:报送方向" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOdrp())) {
			list.add("odrp:其他报送方向" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOdrp(), 32)) {
				list.add("odrp:其他报送方向" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTptr())) {
			list.add("tptr:可疑交易报告触发点" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTptr(), 2)) {
				list.add("tptr:可疑交易报告触发点" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getOtpr())) {
			list.add("otpr:其他可疑交易报告触发点说明" + NOTNULL);
		} else {
			if (checkLength(suspicious.getOtpr(), 2000)) {
				list.add("otpr:其他可疑交易报告触发点说明" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getStcb())) {
			list.add("stcb:资金交易及客户行为情况" + NOTNULL);
		} else {
			if (checkLength(suspicious.getStcb(), 10000)) {
				list.add("stcb:资金交易及客户行为情况" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getAosp())) {
			list.add("aosp:疑点分析" + NOTNULL);
		} else {
			if (checkLength(suspicious.getAosp(), 10000)) {
				list.add("aosp:疑点分析" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTosc())) {
			list.add("tosc:疑似涉罪类型" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTosc(), 200)) {
				list.add("tosc:疑似涉罪类型" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getStcr())) {
			list.add("stcr:可疑交易特征代码" + NOTNULL);
		} else {
			if (checkLength(suspicious.getStcr(), 32)) {
				list.add("stcr:可疑交易特征代码" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getMirs())) {
			list.add("mirs: 人工补正标识" + NOTNULL);
		} else {
			if (checkLength(suspicious.getMirs(), 64)) {
				list.add("mirs: 人工补正标识" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getBwzt())) {
			list.add("bwzt:报文状态" + NOTNULL);
		}
		if (StringUtils.isEmpty(suspicious.getReportType())) {
			list.add("reportType:报文类型" + NOTNULL);
		}
		if (StringUtils.isEmpty(suspicious.getSubmitType())) {
			list.add("submitType:是否上报" + NOTNULL);
		}
		if (StringUtils.isNotEmpty(suspicious.getSubmitType()) && suspicious.getSubmitType().equals("N")
				&& StringUtils.isEmpty(suspicious.getRemarks())) {
			list.add("remarks:上报备注" + NOTNULL);
		}
		return list;
	}

	// 可疑内容校验
	private List<String> suspCheckContent(AmlSuspicious suspicious, boolean isDataCollect) {
		ArrayList<String> _list = new ArrayList<String>();
		// 报送次数标志
		String torp = suspicious.getTorp();
		if (!isNumeric(torp)) {
			_list.add("torp:报送次数标志只能填写数字");
		} else if (Integer.parseInt(torp) > 1) {
			// 报文名匹配 torp=1 且已入库
			System.err.println("还未做");
		}

		// 交易金额
		BigDecimal crat = suspicious.getCrat();
		String cratStr = crat.toString();
		if (cratStr.length() - (cratStr.indexOf(".") != -1 ? cratStr.indexOf(".") + 1 : cratStr.length()) > 3) {
			_list.add("crat:交易金额小数点后最多保留3位小数");
		}

		// 证件类型和证件号码校验
		// 可疑主体证件类型
		String setp = suspicious.getSetp();
		// 可疑主体证件类型说明
		String oitp1 = suspicious.getOitp1();
		// 可疑主体证件号码
		String seid = suspicious.getSeid();
		if (checkZjlxsm(setp, oitp1)) {
			_list.add("oitp1:可疑主体身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (setp.equals("110001") || setp.equals("110003")) {
			if (!checkId(seid)) {
				_list.add("seid:可疑主体身份证件/证明文件号码错误");
			}
		} else if (setp.equals("610001")) {// 全国组织机构代码610001
			if (seid.length() != 9) {
				_list.add("seid:组织机构代码长度应该为9位");
			}
		}
		// 可疑法定代表人
		// 证件类型
		String srit = suspicious.getSrit();
		// 证件说明
		String orit = suspicious.getOrit();
		// 证件号
		String srid = suspicious.getSrid();
		if (checkZjlxsm(srit, orit)) {
			_list.add("orit:可疑主体法定代表人身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (srit.equals("110001") || srit.equals("110003")) {
			if (!checkId(srid)) {
				_list.add("srid:可疑主体法定代表人身份证件/证明文件号码错误");
			}
		} else if (srit.equals("610001")) {// 全国组织机构代码610001
			if (srid.length() != 9) {
				_list.add("srid:组织机构代码长度应该为9位");
			}
		}
		// 可疑控股人
		// 证件类型
		String scit = suspicious.getScit();
		// 证件类型说明
		String ocit = suspicious.getOcit();
		// 证件号码
		String scid = suspicious.getScid();
		if (checkZjlxsm(scit, ocit)) {
			_list.add("ocit:可疑主体控股股东或实际控制人身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (scit.equals("110001") || scit.equals("110003")) {
			if (!checkId(scid)) {
				_list.add("scid:可疑主体控股股东或实际控制人身份证件/证明文件号码错误");
			}
		} else if (scit.equals("610001")) {// 全国组织机构代码610001
			if (scid.length() != 9) {
				_list.add("scid:组织机构代码长度应该为9位");
			}
		}
		// 客户
		String citp = suspicious.getCitp();
		String oitp2 = suspicious.getOitp2();
		String ctid = suspicious.getCtid();
		if (checkZjlxsm(citp, oitp2)) {
			_list.add("oitp2:客户身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (citp.equals("110001") || citp.equals("110003")) {
			if (!checkId(ctid)) {
				_list.add("ctid:客户身份证件/证明文件号码错误");
			}
		} else if (citp.equals("610001")) {// 全国组织机构代码610001
			if (ctid.length() != 9) {
				_list.add("ctid:组织机构代码长度应该为9位");
			}
		}
		// 代办人
		String tbit = suspicious.getTbit();
		String oitp3 = suspicious.getOitp3();
		String tbid = suspicious.getTbid();
		if (checkZjlxsm(tbit, oitp3)) {
			_list.add("oitp3:代办人身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (tbit.equals("110001") || tbit.equals("110003")) {
			if (!checkId(tbid)) {
				_list.add("tbid:代办人身份证件/证明文件号码错误");
			}
		} else if (tbit.equals("610001")) {// 全国组织机构代码610001
			if (tbid.length() != 9) {
				_list.add("tbid:组织机构代码长度应该为9位");
			}
		}
		// 交易对手
		String tcit = suspicious.getTcit();
		String oitp4 = suspicious.getOitp4();
		String tcid = suspicious.getTcid();
		if (checkZjlxsm(tcit, oitp4)) {
			_list.add("oitp4:交易对手身份证件/证明文件类型为119999、129999、619999 或 629999时，证件类型说明必填");
		}
		if (tcit.equals("110001") || tcit.equals("110003")) {
			if (!checkId(tcid)) {
				_list.add("tcid:交易对手身份证件/证明文件号码错误");
			}
		} else if (tcit.equals("610001")) {// 全国组织机构代码610001
			if (tcid.length() != 9) {
				_list.add("tcid:组织机构代码长度应该为9位");
			}
		}
		// 可疑主体联系电话
		String sctl = suspicious.getSctl();
		String[] sctls = sctl.split(",", -1);
		if (checkRepeat(sctls)) {
			_list.add("sctl:可疑主体联系电话重复");
		} else {
			for (int i = 0; i < sctls.length; i++) {
				if (!checkCellphone(sctls[i]) && !checkTelephone(sctls[i])) {
					_list.add("sctl:可疑主体联系电话格式错误");
				}
			}
		}
		// 可疑主体国籍
		String stnt = suspicious.getStnt();
		String[] stnts = stnt.split(",", -1);
		if (checkRepeat(stnts)) {
			_list.add("stnt:可疑主体国籍重复");
		}

		// 业务标识号校验
		String ticd = suspicious.getTicd();
		Date tstm = suspicious.getTstm();
		if (isDataCollect) {
			if (checkTicd(tstm, ticd, false)) {
				_list.add("ticd:业务标示号重复");
			}
		} else {
			Long id = suspicious.getId();
			Date date = DateUtils.addDays(tstm, 1);
			Date start = null;
			Date end = null;
			try {
				start = sdf.parse(sdf.format(tstm));
				end = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<AmlSuspicious> list = amlSuspiciousRepo.findByTicdAndTstmBetweenAndDeleteStateAndIdNot(ticd, start,
					end, "1", id == null ? 0L : id);
			if (list != null && list.size() > 0) {
				_list.add("ticd:业务标示号重复");
			}
		}

		// 银行卡类型
		String cbct = suspicious.getCbct();
		String ocbt = suspicious.getOcbt();
		if (cbct.equals("90") && ocbt.equals("@N")) {
			_list.add("ocbt:客户银行卡类型为“90”时，银行卡类型说明必填。");
		}
		
		//账号和银行卡号交易 
		String catp = suspicious.getCatp();//账户类型
		String ctac = suspicious.getCtac();//账号
		String cbcn = suspicious.getCbcn();//银行卡号码
		if(getTiDaiFu().contains(ctac)&&getTiDaiFu().contains(cbcn)){
			_list.add("ctac,cbcn:账号和银行卡号码不能同时都为替代符。");
		}else{
			if(getTiDaiFu().contains(ctac)){
				if(!ctac.equals(catp)){
					_list.add("ctac,catp:账号和账户类型的替代符不一致。");
				}
			}else{
				if(getTiDaiFu().contains(catp)){
					_list.add("catp:账号不为替代符时，账户类型不能是替代符。");
				}
			}
			if(getTiDaiFu().contains(cbcn)){
				if(!cbcn.equals(cbct)){
					_list.add("cbcn,cbct:银行卡号码和银行卡类型的替代符不一致。");
				}
			}else{
				if(getTiDaiFu().contains(cbct)){
					_list.add("cbct:银行卡号码不为替代符时，银行卡类型不能是替代符。");
				}
			}
		}
		
		// 非柜台交易方式
		String octt = suspicious.getOctt();
		String ooct = suspicious.getOoct();
		if (octt.equals("99") && ooct.equals("@N")) {
			_list.add("ooct:非柜台交易方式为“90”时，银行卡类型说明必填。");
		}
		// 报送方向
		String dorp = suspicious.getDorp();
		String odrp = suspicious.getOdrp();
		if (dorp.equals("99") && odrp.equals("@N")) {
			_list.add("odrp:报送方向为“99”时，其他报送方向说明必填。");
		}
		// 报告触发点
		String tptr = suspicious.getTptr();
		String otpr = suspicious.getOtpr();
		if (tptr.equals("99") && otpr.equals("@N")) {
			_list.add("otpr:其他可疑交易报告触发点说明为“99”时，其他报送方向说明必填。");
		}
		return _list;
	}

	// 可疑分析处理校验
	public CheckResult checkPreSusp(AmlSuspicious suspicious) {
		List<String> list = preSuspCheckNull(suspicious);
		StringBuilder sb = new StringBuilder();
		if (list.size() != 0 && list != null) {
			for (String str : list) {
				sb.append(str + ",");
			}
		}
		CheckResult result = new CheckResult();
		result.setCheckId(suspicious.getId());
		result.setCheckDate(new Date());
		result.setFlag(2);
		result.setType("2");
		String reason = sb.toString();
		if (StringUtils.isNotEmpty(reason)) {// 去掉最后一个逗号
			result.setReason(reason.substring(0, reason.length() - 1));
		}
		return result;
	}

	// 可疑前处理非空校验
	private List<String> preSuspCheckNull(AmlSuspicious suspicious) {
		ArrayList<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(suspicious.getDetr())) {
			list.add("detr:可疑交易报告紧急程度" + NOTNULL);
		} else {
			if (checkLength(suspicious.getDetr(), 2)) {
				list.add("detr:可疑交易报告紧急程度" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getDorp())) {
			list.add("dorp:报送方向" + NOTNULL);
		} else {
			if (checkLength(suspicious.getDorp(), 2)) {
				list.add("dorp:报送方向" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getTptr())) {
			list.add("tptr:可疑交易报告触发点" + NOTNULL);
		} else {
			if (checkLength(suspicious.getTptr(), 2)) {
				list.add("tptr:可疑交易报告触发点" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getStcb())) {
			list.add("stcb:资金交易及客户行为情况" + NOTNULL);
		} else {
			if (checkLength(suspicious.getStcb(), 10000)) {
				list.add("stcb:资金交易及客户行为情况" + MORELEN);
			}
		}
		if (StringUtils.isEmpty(suspicious.getAosp())) {
			list.add("aosp:疑点分析" + NOTNULL);
		} else {
			if (checkLength(suspicious.getAosp(), 10000)) {
				list.add("aosp:疑点分析" + MORELEN);
			}
		}
		return list;
	}

	/*
	 * // 信息完整性校验 private boolean infoFull(String s1, String s2, String s3,
	 * String s4) { // 特殊替代符集合 if (spList.contains(s1) && spList.contains(s2) &&
	 * spList.contains(s3) && spList.contains(s4)) { return false; } else if
	 * (!spList.contains(s1) && !spList.contains(s2) && !spList.contains(s3) &&
	 * !spList.contains(s4)) { return false; } return true; }
	 */

	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 验证手机号码
	 * 
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145 电信号码段:133、153、180、189
	 * 
	 * @param cellphone
	 * @return
	 */
	private boolean checkCellphone(String cellphone) {
		String regex = "^(1[0-9][0-9])\\d{8}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cellphone);
		return matcher.matches();
	}

	/**
	 * 验证固话号码
	 * 
	 * @param telephone
	 * @return
	 */
	private boolean checkTelephone(String telephone) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(telephone);
		return matcher.matches();
	}

	// 证件类型说明校验
	private boolean checkZjlxsm(String zjlx, String lxsm) {
		if ((zjlx.equals("119999") || zjlx.equals("129999") || zjlx.equals("619999") || zjlx.equals("629999"))
				&& this.getTiDaiFu().contains(lxsm)) {
			return true;
		}
		return false;
	}

	// 是否为空、空格、
	private boolean isTrim(String string) {
		if (string != null) {
			if (string.trim().equals("")) {
				return true;
			}
		}
		return false;
	}

	// 判断是否为特殊字符
	private boolean isSpecialChar(String string, Integer i) {
		// i为1时可带&
		boolean flag = true;
		if (string != null) {
			String all = string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "");
			if (i == 1) {
				// 可带&
				if (all.length() != 0) {
					String[] split = all.split("");
					for (String s : split) {
						if (!s.equals("&")) {
							flag = false;
							break;
						}
					}
				}
			} else {
				// 含特殊字符时为FALSE,
				flag = (all.length() == 0);
			}
		}
		return flag;
	}

	// 一块判断是否为空、空格、特殊字符
	private boolean checkSpecialAndTrim(String string, Integer i) {
		boolean flag1 = this.isSpecialChar(string, i);
		boolean flag2 = this.isTrim(string);
		if (flag1 == true && flag2 == true) {
			return true;
		}
		return false;
	}

	// 身份证校验
	public boolean checkId(String id) {
		char[] ch = id.toCharArray();
		boolean flag1 = verForm(id);
		boolean flag2 = verify(ch);
		if (flag1 == true && flag2 == true) {
			return true;
		}
		return false;
	}

	// <------------------身份证格式的正则校验----------------->
	public static boolean verForm(String num) {
		String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
		if (!num.matches(reg)) {
			System.err.println("ID Format Error!");
			return false;
		}
		return true;
	}

	// <------------------身份证最后一位的校验算法----------------->
	public static boolean verify(char[] id) {
		int sum = 0;
		int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		for (int i = 0; i < id.length - 1; i++) {
			sum += (id[i] - '0') * w[i];
		}
		int c = sum % 11;
		char code = ch[c];
		char last = id[id.length - 1];
		last = last == 'x' ? 'X' : last;
		return last == code;
	}

	private boolean checkLength(String str, int len) {
		if (StringUtils.isBlank(str)) {
			return true;
		}
		if (str.length() > len) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkRepeat(String[] array) {
		Set<String> set = new HashSet<String>();
		for (String str : array) {
			set.add(str);
		}
		if (set.size() != array.length - 1) {
			return false;// 有重复
		} else {
			return true;// 不重复
		}
	}

}
