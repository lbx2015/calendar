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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.entity.Resp;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.Customer;
import net.riking.entity.model.RiskFactorConfig;
import net.riking.entity.risk.RiskFactor;
import net.riking.service.repo.BaseIndvCustAddRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.service.repo.CusRateInfoRepo;
import net.riking.service.repo.CustomerRepo;
import net.riking.service.repo.RiskFactorConfigRepo;
import net.riking.service.repo.RiskFactorRepo;

@Service("risKInRateServiceImpl")
public class RisKInRateServiceImpl {

	@Autowired
	CustomerRepo customerRepo;
	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	BaseIndvCustAddRepo BaseIndvCustAddRepo;
	@Autowired
	RiskFactorConfigRepo riskFactorConfigRepo;
	@Autowired
	RiskFactorRepo riskFactorRepo;
	@Autowired
	CusRateInfoRepo cusRateInfoRepo;

	private Map<String, String> khxigkcdMap = new HashMap<String, String>();// 客户信息的公开程度

	private Map<String, String> hzqdgxMap = new HashMap<String, String>();// 合作渠道关系

	private Map<String, String> mtbdMap = new HashMap<String, String>();// 涉及客户的风险提示信息或权威媒体报道信息
	// 证件类型
	private Map<String, String> zjlxMap = new HashMap<String, String>();
	// 年龄
	private Map<String, String> nlMap = new HashMap<String, String>();
	// 个人客户的国籍所在地、住所
	private Map<String, String> gjszdMap = new HashMap<String, String>();
	// 产品和服务风险
	private Map<String, String> cphfwfxMap = new HashMap<String, String>();
	// 主要交易对手方的注册地、经营地或国籍地、住所
	private Map<String, String> zyjydsszdMap = new HashMap<String, String>();
	// 非面对面交易
	private Map<String, String> fmdmjylxMap = new HashMap<String, String>();
	// 客户所持身份证件或身份证明文件的种类
	private Map<String, String> khlxMap = new HashMap<String, String>();
	// 跨境交易
	private Map<String, String> kjbsMap = new HashMap<String, String>();
	// 代理交易
	private Map<String, String> dljyMap = new HashMap<String, String>();
	// 特殊业务类型的交易频率
	private Map<String, String> tsywlxdjyplMap = new HashMap<String, String>();

	
	public int baseIndvCustRiskRate(List<BaseIndvCust> cusList)
			throws InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		init();
		int count = 0;
		List<RiskFactor> riskFactorList = riskFactorRepo.findAll();// 所有二级因子表信息
		Set<String> codes = new HashSet<>();
		for (int i = 0; i < riskFactorList.size(); i++) {
			codes.add(riskFactorList.get(i).getFactorCode());
		}
		Map<String, BigDecimal> factorMap = new HashMap<String, BigDecimal>();
		for (RiskFactor riskFactor : riskFactorList) {
			factorMap.put(riskFactor.getFactorName(), new BigDecimal(riskFactor.getWeights()));

		}
		Map<String, BigDecimal> scoreMap = new HashMap<>();
		List<RiskFactorConfig> configList = riskFactorConfigRepo.getConfigs(codes);
		for (RiskFactorConfig config : configList) {
			scoreMap.put(config.getFactorCodeName(), config.getScore());
		}
		List<BaseIndvCust> lstCorpCust = new ArrayList<BaseIndvCust>();
		for (BaseIndvCust cus : cusList) {
			BigDecimal total = BigDecimal.ZERO;
			String codeName = "";
			for (RiskFactor riskFactor : riskFactorList) {
				BigDecimal score = BigDecimal.ZERO;
				BigDecimal percent = factorMap.get(riskFactor.getFactorName());

				if ("个人客户的国籍所在地、住所".equals(riskFactor.getFactorName())) { // 根据客户国籍打分
					codeName = this.gjszdMap.get(cus.getBaseIndvCustAdd().getGjszd());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("行业（职业）".equals(riskFactor.getFactorName())) { // 设为其他
					score = BigDecimal.ONE;
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("客户信息的公开程度".equals(riskFactor.getFactorName())) {
					codeName = this.khxigkcdMap.get(cus.getBaseIndvCustAdd().getXxgkcd());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("自然人客户年龄".equals(riskFactor.getFactorName())) {
					codeName = this.nlMap.get(cus.getBaseIndvCustAdd().getNl());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("金融机构与客户建立或维持业务关系的渠道".equals(riskFactor.getFactorName())) {
					codeName = this.hzqdgxMap.get(cus.getBaseIndvCustAdd().getHzqdgx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("反洗钱交易监测记录".equals(riskFactor.getFactorName())) {
					if ("Y".equals(cus.getBaseIndvCustAdd().getSfbljl())) {
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
					codeName = this.mtbdMap.get(cus.getBaseIndvCustAdd().getSfbljl());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("客户所持身份证件或身份证明文件的种类".equals(riskFactor.getFactorName())) {
					codeName = this.mtbdMap.get(cus.getZjlx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				}  else if ("主要交易对手方的注册地、经营地或国籍地、住所".equals(riskFactor.getFactorName())) { // 默认设为其他
					codeName = this.zyjydsszdMap.get(cus.getBaseIndvCustAdd().getFmdmjylx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("非面对面交易".equals(riskFactor.getFactorName())) { // 默认设为无此类业务
					codeName = this.fmdmjylxMap.get(cus.getBaseIndvCustAdd().getFmdmjylx());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("跨境交易".equals(riskFactor.getFactorName())) { // 默认设为其他
																		// //
																		// score=new
					codeName = this.kjbsMap.get(cus.getBaseIndvCustAdd().getKjbs());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("代理交易".equals(riskFactor.getFactorName())) { // 默认设为无此类业务
					codeName = this.dljyMap.get(cus.getBaseIndvCustAdd().getDljy());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("特殊业务类型的交易频率".equals(riskFactor.getFactorName())) { // 默认设为无此类业务
					codeName = this.tsywlxdjyplMap.get(cus.getBaseIndvCustAdd().getTsywlxdjypl());
					score = scoreMap.get(codeName);
					if (null == score) {
						score = BigDecimal.valueOf(0);
					}
					score = score.multiply(percent);
					total = total.add(score);
				} else if ("产品和服务风险".equals(riskFactor.getFactorName())) { // 默认设为低风险
					codeName = this.cphfwfxMap.get(cus.getBaseIndvCustAdd().getCphfwfx());
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
			} // cus.setPfrj(new Date());
			lstCorpCust.add(cus);
			count++;
		}
		if (lstCorpCust.size() > 0) {
			baseIndvCustRepo.save(lstCorpCust);
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

	public Resp setRateTime(List<BaseIndvCust> cusList) {
		int count = 0;
		for (BaseIndvCust customer : cusList) {
			baseIndvCustRepo.setRiskTime(customer.getId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		count++;
		return new Resp(count);
	}

	private void init() {
		// 客户信息的公开程度
		khxigkcdMap.put("1", "信息完整且已核实");
		khxigkcdMap.put("2", "客户提供的联系方式错误或无法联系（电话号码、地址等）");
		// 金融机构与客户建立或维持业务关系的渠道
		this.hzqdgxMap.put("1", "现场建立合作或维护");
		this.hzqdgxMap.put("2", "其他");
		this.hzqdgxMap.put("3", "通过中介或代理建立合作或维护");

		// 证件类型
		this.zjlxMap.put("0", "0 身份证");
		this.zjlxMap.put("1", "1 户口簿");
		this.zjlxMap.put("2", "2 护照");
		this.zjlxMap.put("3", "3 军官证");
		this.zjlxMap.put("4", "4 士兵证");
		this.zjlxMap.put("5", "5 港澳居民来往内地通行证");
		this.zjlxMap.put("6", "6 台湾同胞来往内地通行证");
		this.zjlxMap.put("7", "7 临时身份证");
		this.zjlxMap.put("8", "8 外国人居留证");
		this.zjlxMap.put("9", "9 警官证");
		this.zjlxMap.put("A", "A 香港身份证");
		this.zjlxMap.put("C", "C 台湾身份证");
		this.zjlxMap.put("X", "X 其他证件");
		this.zjlxMap.put("Z", "Z N/A");

		// 年龄
		this.nlMap.put("1", "16-60岁");
		this.nlMap.put("2", "61-80岁");
		this.nlMap.put("3", "1-15岁或81岁以上");
		// 个人客户的国籍所在地、住所
		this.gjszdMap.put("", "其他");
		this.gjszdMap.put("", "高风险国家/地区（附录A） ");
		this.gjszdMap.put("", "避税离岸金融中心");
		// 产品和服务风险
		this.cphfwfxMap.put("1", "低风险产品/服务（附录B） ");
		this.cphfwfxMap.put("2", "中等风险产品/服务（附录B）  ");
		this.cphfwfxMap.put("3", "高风险产品/服务（附录B）  ");
		// 主要交易对手方的注册地、经营地或国籍地、住所
		this.zyjydsszdMap.put("1", "其他");
		this.zyjydsszdMap.put("2", "高风险国家/地区（附录A）");
		this.zyjydsszdMap.put("3", "避税离岸金融中心");
		// 非面对面交易
		this.fmdmjylxMap.put("1", "同一人或少数人通过不同客户的财务账户进行的网上交易");
		this.fmdmjylxMap.put("2", "在开户地址以外的IP地址或海外任何地址频繁的进行网上交易");
		this.fmdmjylxMap.put("3", "不同客户账户但使用同一IP地址进行网上交易");
		this.fmdmjylxMap.put("4", "大额网上交易");
		this.fmdmjylxMap.put("5", "公司账户与自然人往来频繁、大额交易 ");
		this.fmdmjylxMap.put("6", "无此类业务");
		// 跨境交易
		this.kjbsMap.put("1", "无跨境交易");
		this.kjbsMap.put("2", "其他");
		this.kjbsMap.put("3", "曾经的交易对手位于高风险国家/司法管辖区名单中");
		// 代理交易
		this.dljyMap.put("1", "代理或代办人开户");
		this.dljyMap.put("2", "代理或代办人办理涉嫌可疑交易报告事宜");
		this.dljyMap.put("3", "同一代理人在同一时间代表他人开设三个或多个账户或同时办理重复的交易");
		this.dljyMap.put("4", "两个或两个以上的客户联系人或保留联系人号码相同 ");
		this.dljyMap.put("5", "无此类业务");
		// 特殊业务类型的交易频率
		this.tsywlxdjyplMap.put("1", "客户开立10个或10个以上的账户");
		this.tsywlxdjyplMap.put("2", "非自然人与自然人经常在半年内连续三天以上大额资金转移");
		this.tsywlxdjyplMap.put("3", "非自然人与自然人之间经常大额资金一天三次以上的转移 ");
		this.tsywlxdjyplMap.put("4", "无此类业务");
		// 涉及客户的风险提示信息或权威媒体报道信息
		this.mtbdMap.put("1", "无不良记录和不良新闻");
		this.mtbdMap.put("2", "客户受到任何监管机构、执法机关或金融机构的关注，或受到任何权威媒体的重大不良新闻报道。");
		//客户所持身份证件或身份证明文件的种类
		this.khlxMap.put("110001", "通过公安部公民网络身份识别系统核查的第二代中国公民身份证");
		this.khlxMap.put("110009", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110005", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110013", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110017", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110021", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110023", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110015", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110011", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110007", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110003", "其他身份证明文件（除第二代中国公民身份证外）");
		this.khlxMap.put("110021", "港澳通行证、大陆居民往来台湾通行证");
		this.khlxMap.put("110019", "港澳通行证、大陆居民往来台湾通行证");
		this.khlxMap.put("外国护照", "外国人护照");
		this.khlxMap.put("110029", "其他身份证明文件（除护照外）");
		this.khlxMap.put("119999", "其他身份证明文件（除护照外）");
		this.khlxMap.put("129999", "其他身份证明文件（除护照外）");
	}
}
