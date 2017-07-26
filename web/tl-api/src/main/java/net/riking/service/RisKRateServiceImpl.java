package net.riking.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.entity.Resp;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.Customer;
import net.riking.entity.model.RiskFactorConfig;
import net.riking.entity.risk.RiskFactor;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.service.repo.CusRateInfoRepo;
import net.riking.service.repo.CustomerRepo;
import net.riking.service.repo.RiskFactorConfigRepo;
import net.riking.service.repo.RiskFactorRepo;

@Service("risKRateServiceImpl")
public class RisKRateServiceImpl {

	@Autowired
	CustomerRepo customerRepo;
	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;
	@Autowired
	RiskFactorConfigRepo riskFactorConfigRepo;
	@Autowired
	RiskFactorRepo riskFactorRepo;
	@Autowired
	CusRateInfoRepo cusRateInfoRepo;

	private Map<String, String> khxigkcdMap = new HashMap<String, String>();// 客户信息的公开程度

	private Map<String, String> hzqdgxMap = new HashMap<String, String>();// 合作渠道关系

	private Map<String, String> mtbdMap = new HashMap<String, String>();// 涉及客户的风险提示信息或权威媒体报道信息

	// 非自然人客户的股权或控制权结构
	private Map<String, String> gqhkzqdjgMap = new HashMap<String, String>();

	// 主要交易对手方的注册地、经营地或国籍地、住所
	private Map<String, String> zyjydsszdMap = new HashMap<String, String>();

	// 非面对面交易
	private Map<String, String> fmdmjylxMap = new HashMap<String, String>();

	// 跨境交易
	private Map<String, String> kjbsMap = new HashMap<String, String>();

	// 代理交易
	private Map<String, String> dljyMap = new HashMap<String, String>();

	// 特殊业务类型的交易频率
	private Map<String, String> tsywlxdjyplMap = new HashMap<String, String>();

	// 产品和服务风险
	private Map<String, String> cphfwfxMap = new HashMap<String, String>();

	// 客户所持身份证件或身份证明文件的种类
	private Map<String, String> khlxMap = new HashMap<String, String>();

	public int baseCorpCustRiskRate(List<BaseCorpCust> cusList)
			throws InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		init();
		int count = 0;
		List<RiskFactor> riskFactorList = riskFactorRepo.getCorpCust();// 所有二级因子表信息
		Set<String> codes = new HashSet<>();
		for (int i = 0; i < riskFactorList.size(); i++) {
			codes.add(riskFactorList.get(i).getFactorCode());
		}
		Map<String, BigDecimal> factorMap = new HashMap<String, BigDecimal>();
		for (RiskFactor riskFactor : riskFactorList) {
			factorMap.put(riskFactor.getFactorName(), new BigDecimal(riskFactor.getWeights()));

		}
		List<RiskFactorConfig> configList =new ArrayList<>();
		 configList = riskFactorConfigRepo.getConfigs(codes);
		Map<String, BigDecimal> scoreMap = new HashMap<>();
		for (RiskFactorConfig config : configList) {
			scoreMap.put(config.getFactorCodeName(), config.getScore());
		}
		List<BaseCorpCust> lstCorpCust = new ArrayList<BaseCorpCust>();
		for (BaseCorpCust cus : cusList) {

			BigDecimal total = BigDecimal.ZERO;
			String codeName = "";
			for (RiskFactor riskFactor : riskFactorList) {

				
				BigDecimal score = BigDecimal.ZERO;
				BigDecimal percent = factorMap.get(riskFactor.getFactorName());
				if ("公司客户注册地、经营地".equals(riskFactor.getFactorName())) {
					// 根据客户国籍打分
					codeName = this.zyjydsszdMap.get(cus.getJyszdgj());
					if (!StringUtils.isEmpty(codeName)) {
						score = scoreMap.get(codeName);
						if (null == score) {
							score = BigDecimal.valueOf(0);
						}
						score = score.multiply(percent);
					} else {
						// 设为其他
						score = BigDecimal.ONE;
						score = score.multiply(percent);
					}

					total = total.add(score);
				} else if ("行业（职业）".equals(riskFactor.getFactorName())) {
					// 设为其他
					score = BigDecimal.ONE;
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("客户所持身份证件或身份证明文件的种类".equals(riskFactor.getFactorName())) {
					codeName = this.khlxMap.get(cus.getKhlx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("非自然人客户的股权或控制权结构".equals(riskFactor.getFactorName())) {
					codeName = this.khxigkcdMap.get(cus.getCzrjjcf());
					if ("私营企业".equals(codeName) || "私营独资企业".equals(codeName)) {
						score = BigDecimal.ONE;
						score = score.multiply(percent);
					} else {
						score = new BigDecimal("2");
						score = score.multiply(percent);
					}

					total = total.add(score);
				} else if ("客户信息的公开程度".equals(riskFactor.getFactorName())) {
					codeName = this.khxigkcdMap.get(cus.getCzrjjcf());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("金融机构与客户建立或维持业务关系的渠道".equals(riskFactor.getFactorName())) {
					codeName = this.hzqdgxMap.get(cus.getHzqdgx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("反洗钱交易监测记录".equals(riskFactor.getFactorName())) {
					if ("1".equals(cus.getSfbljl())) {
						score = scoreMap.get("有记录");
						if (null == score) {
							score = BigDecimal.valueOf(0);
						}
						score = score.multiply(percent);
					} else {
						score = scoreMap.get("无记录");
						if (null == score) {
							score = BigDecimal.valueOf(0);
						}
						score = score.multiply(percent);
					}
					total = total.add(score);
				} else if ("涉及客户的风险提示信息或权威媒体报道信息".equals(riskFactor.getFactorName())) {
					codeName = this.mtbdMap.get(cus.getSfbljl());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("非自然人客户的存续时间".equals(riskFactor.getFactorName())) {
					if (null != cus.getZcrq()) {
						BigDecimal dff = new BigDecimal(this.dateDiff(new Date(), cus.getZcrq()));
						if (dff.compareTo(new BigDecimal(1830)) >= 0) {
							score = scoreMap.get("社会经济活动记录持续5年或更久");
							if (null == score) {
								score = BigDecimal.valueOf(0);
							}
							score = score.multiply(percent);
						} else if (dff.compareTo(new BigDecimal(732)) > 0 && dff.compareTo(new BigDecimal(1830)) < 0) {
							score = scoreMap.get("社会经济活动记录持续2以上年或5年以下 ");
							if (null == score) {
								score = BigDecimal.valueOf(0);
							}
							score = score.multiply(percent);
						} else {
							score = BigDecimal.ONE;
							score = score.multiply(percent);
						}
						total = total.add(score);
					}
				} else if ("公司客户实际受益人、实际控制人的国籍、住所".equals(riskFactor.getFactorName())) {
					codeName = this.zyjydsszdMap.get(cus.getSyrdqfx());
					if (!StringUtils.isEmpty(codeName)) {
						score = scoreMap.get(codeName);
						if (null == score) {
							score = BigDecimal.valueOf(0);
						}
						score = score.multiply(percent);
					} else {
						// 设为其他
						score = BigDecimal.ONE;
						score = score.multiply(percent);
					}
				} else if ("主要交易对手方的注册地、经营地或国籍地、住所".equals(riskFactor.getFactorName())) {
					codeName = this.zyjydsszdMap.get(cus.getZyjydsszd());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("非面对面交易".equals(riskFactor.getFactorName())) {
					codeName = this.fmdmjylxMap.get(cus.getFmdmjylx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("跨境交易".equals(riskFactor.getFactorName())) {
					codeName = this.kjbsMap.get(cus.getKjbs());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("代理交易".equals(riskFactor.getFactorName())) {
					codeName = this.dljyMap.get(cus.getDljy());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("特殊业务类型的交易频率".equals(riskFactor.getFactorName())) {
					codeName = this.tsywlxdjyplMap.get(cus.getTsywlxdjypl());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("产品和服务风险".equals(riskFactor.getFactorName())) {
					codeName = this.cphfwfxMap.get(cus.getCphfwfx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				}
			}

			cus.setFxdjfs(total.divide(new BigDecimal("3"), 0, RoundingMode.CEILING));

			if (cus.getFxdjfs().compareTo(new BigDecimal("70")) > 0) {
				cus.setKhfxdj("高");
			} else if (cus.getFxdjfs().compareTo(new BigDecimal("60")) >= 0
					&& cus.getFxdjfs().compareTo(new BigDecimal("70")) <= 0) {
				cus.setKhfxdj("中");
			} else if (cus.getFxdjfs().compareTo(new BigDecimal("60")) < 0) {
				cus.setKhfxdj("低");
			}
			cus.setPfrj(new Date());
			lstCorpCust.add(cus);
			count++;
		}
		if (lstCorpCust.size() > 0) {
			baseCorpCustRepo.save(lstCorpCust);
		}
		return count;
	}

	public Resp setRateTime(List<Customer> cusList, Date date) {
		int count = 0;
		for (Customer customer : cusList) {
			customerRepo.setRiskTime(customer.getId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		count++;
		return new Resp(count);
	}

	public Resp setRateTime(List<BaseCorpCust> cusList) {
		int count = 0;
		for (BaseCorpCust customer : cusList) {
			baseCorpCustRepo.setRiskTime(customer.getId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		count++;
		return new Resp(count);
	}

	private void init() {
		// 客户信息的公开程度
		khxigkcdMap.put("1", "国家机关");
		khxigkcdMap.put("2", "公共机构");
		khxigkcdMap.put("3", "国有企业");
		khxigkcdMap.put("4", "上市公司");
		khxigkcdMap.put("5", "非上市公司");
		khxigkcdMap.put("6", "合作经营");
		khxigkcdMap.put("7", "合伙经营");
		khxigkcdMap.put("8", "私营企业");
		khxigkcdMap.put("9", "私营独资企业");
		khxigkcdMap.put("10", "股份制公司");
		khxigkcdMap.put("11", "非营利性企业");
		khxigkcdMap.put("12", "信托公司");
		khxigkcdMap.put("13", "基金公司");
		khxigkcdMap.put("14", "非营利性组织");
		khxigkcdMap.put("15", "慈善机构");
		khxigkcdMap.put("16", "壳公司");
		khxigkcdMap.put("17", "离岸企业");
		// 金融机构与客户建立或维持业务关系的渠道
		this.hzqdgxMap.put("1", "现场建立合作或维护");
		this.hzqdgxMap.put("2", "其他");
		this.hzqdgxMap.put("3", "通过中介或代理建立合作或维护");

		

		// 涉及客户的风险提示信息或权威媒体报道信息
		this.mtbdMap.put("1", "无不良记录和不良新闻");
		this.mtbdMap.put("2", "客户受到任何监管机构、执法机关或金融机构的关注，或受到任何权威媒体的重大不良新闻报道。");

		// 非自然人客户的股权或控制权结构
		this.gqhkzqdjgMap.put("1", "个人独资企业");
		this.gqhkzqdjgMap.put("2", "合伙企业");
		this.gqhkzqdjgMap.put("3", "隐名股东或被指定股东");
		this.gqhkzqdjgMap.put("4", "股权或控制权结构为2级以上");
		this.gqhkzqdjgMap.put("5", "难以获取该类信息 ");
		this.gqhkzqdjgMap.put("6", "其他");

		// 主要交易对手方的注册地、经营地或国籍地、住所
		this.zyjydsszdMap.put("1", "其他");
		this.zyjydsszdMap.put("2", "高风险国家/地区（附录A）");
		this.zyjydsszdMap.put("3", "避税离岸金融中心");
		this.zyjydsszdMap.put("4", "上海自贸区");

		// 跨境交易
		this.kjbsMap.put("1", "无跨境交易");
		this.kjbsMap.put("2", "其他");
		this.kjbsMap.put("3", "曾经的交易对手位于高风险国家/司法管辖区名单中");

		// 非面对面交易
		this.fmdmjylxMap.put("1", "同一人或少数人通过不同客户的财务账户进行的网上交易");
		this.fmdmjylxMap.put("2", "在开户地址以外的IP地址或海外任何地址频繁的进行网上交易");
		this.fmdmjylxMap.put("3", "不同客户账户但使用同一IP地址进行网上交易");
		this.fmdmjylxMap.put("4", "大额网上交易");
		this.fmdmjylxMap.put("5", "公司账户与自然人往来频繁、大额交易 ");
		this.fmdmjylxMap.put("6", "关联企业间的异常交易");
		this.fmdmjylxMap.put("7", "无此类业务");

		// 代理交易
		this.dljyMap.put("1", "代理或代办人开户");
		this.dljyMap.put("2", "代理或代办人办理涉嫌可疑交易报告事宜");
		this.dljyMap.put("3", "同一代理人在同一时间代表他人开设三个或多个账户或同时办理重复的交易");
		this.dljyMap.put("4", "两个或两个以上的客户联系人或保留联系人号码相同");
		this.dljyMap.put("5", "无此类业务");

		// 特殊业务类型的交易频率
		this.tsywlxdjyplMap.put("1", "客户开立10个或10个以上的账户");
		this.tsywlxdjyplMap.put("2", "非自然人与自然人经常在半年内连续三天以上大额资金转移");
		this.tsywlxdjyplMap.put("3", "非自然人与自然人之间经常大额资金一天三次以上的转移");
		this.tsywlxdjyplMap.put("4", "无此类业务");

		// 产品和服务风险
		this.cphfwfxMap.put("1", "低风险产品/服务（附录B）");
		this.cphfwfxMap.put("2", "中等风险产品/服务（附录B）");
		this.cphfwfxMap.put("3", "高风险产品/服务（附录B）");
		
		//客户所持身份证件或身份证明文件的种类
		this.khlxMap.put("610001", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610005", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610009", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610013", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610017", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610021", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610025", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610029", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610033", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610037", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610041", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610045", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610049", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610053", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610057", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610061", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("619999", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610003", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610007", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610011", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610015", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610019", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610023", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610031", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610035", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610039", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610043", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610047", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610051", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610055", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610059", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("610099", "营业执照，组织机构代码，税务登记证，机构信用代码，外商企业批准证书");
		this.khlxMap.put("629999", "境外法人注册证明文件");
	}

	private long dateDiff(Date d1, Date d2) {
		long n1 = d1.getTime();
		long n2 = d2.getTime();
		long diff = Math.abs(n1 - n2);
		diff /= 3600 * 1000 * 24;
		return diff;
	}
}
