package net.riking.task.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.riking.entity.model.*;
import net.riking.service.*;
import net.riking.service.repo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.entity.model.TaskJobInfo;
import net.riking.core.service.DataDictService;
import net.riking.core.service.repo.TaskJobInfoRepo;
import net.riking.core.task.IJobRunner;
import net.riking.entity.DataCollectInfo;

/**
 * Created by bing.xun on 2017/4/26.
 */
@Component
public class RiskDataCollectJob2 extends IJobRunner {

	@Autowired
	private ModelAmlCorptrnRepo modelAmlCorptrnRepo;

	@Autowired
	private SuspiciousPropDictRepo suspiciousPropDictRepo;

	@Autowired
	private AmlRuleEngineRepo amlRuleEngineRepo;

	@Autowired
	private DataDictService dataDictService;

	@Autowired
	private AmlSuspiciousRepo amlSuspiciousRepo;

	@Autowired
	private BigAmountRepo bigAmountRepo;

	@Autowired
	private BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	private TaskJobInfoRepo taskJobInfoRepo;

	@Autowired
	private ExtractDataServiceImpl extractDataService;

	@Autowired
	private SdcurrpdServiceImpl sdcurrpdService;

	@Autowired
	private TranFeaturesRepo tranFeaturesRepo;

	@Autowired
	private  AmlRuleEngineScoreRepo amlRuleEngineScoreRepo;

	@Autowired
	private ModelAmlInditrnRepo modelAmlInditrnRepo;

	@Autowired
	private WorkflowServiceImpl workflowService;

	@Autowired
	private BaseSyncServiceImpl baseSyncService;

	@Autowired
	private BlackWhiteListRepo blackWhiteListRepo;

	private List<SuspiciousPropDict> suspiciousPropDictList;

	private List<ModelPropDict> modelPropDictList;

	// 黑名单
	private List<BlackWhiteList> blackWhiteList;

	// 汇率
	private Map<String, Sdcurrpd> dcurrpdMap;

	private Map<String, String> symbolMap = new HashMap<String, String>();

	private Map<Long, TranFeatures> tranFeaturesMap = new HashMap<Long, TranFeatures>();

	private List<TranFeatures> tranFeaturesList = new ArrayList<TranFeatures>();

	//key : tranFeaturesId
	private Map<Long,List<AmlRuleEngineScore>> amlRuleEngineScoreListMap = new HashMap<Long, List<AmlRuleEngineScore>>();

	private Map<String, Integer> torpMap = new HashMap<String, Integer>();

	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
	DataCollectInfo info = new DataCollectInfo();
	@Override
	public Short callback(TaskJobInfo taskJobInfo) throws Exception {
		System.out.println("--开始采集--");
		destroyProps();
		Date endDate;
		ObjectMapper mapper = new ObjectMapper();
		info = mapper.readValue(taskJobInfo.getData(), DataCollectInfo.class);
		if (null == info.getType()) {
			info.setType((byte) 3);
		}
		if (StringUtils.isBlank(info.getDate())) {
			endDate = new Date();
		} else {
			String data = info.getDate();
			endDate = dft.parse(data);
		}
		Long t0 = System.currentTimeMillis();
		baseSyncService.sncyInformation(endDate);
		Long t1 = System.currentTimeMillis();
		System.out.println("--对公数据同步完成，耗时：" + (t1 - t0) / 1000 + "s");
		if (hasYsh(endDate,info.getType())) {
			taskJobInfo.setResult("原采集数据包含已审核，不能重复采集");
			taskJobInfo.setDateUpdate(new Date());
			taskJobInfoRepo.updateJobr(taskJobInfo.getResult(), taskJobInfo.getBatchId());
			return STATUS_ENUM.SUCCESS;
		}
		Long t2 = System.currentTimeMillis();
		System.out.println("--检查是否包含已审核完成，耗时：" + (t2 - t1) / 1000 + "s");
		initCrimes();
		Long t3 = System.currentTimeMillis();
		System.out.println("--初始化涉罪类型完成，耗时：" + (t3 - t2) / 1000 + "s");
		dcurrpdMap = sdcurrpdService.getSdcurrpd(endDate);
		Long t4 = System.currentTimeMillis();
		System.out.println("--初始化汇率完成，耗时：" + (t4 - t3) / 1000 + "s");
		// 对私
		Set<Byte> dskhlx = new HashSet<Byte>();
		dskhlx.add((byte) 2);
		dskhlx.add((byte) 3);
		// 对公
		Set<Byte> dgkhlx = new HashSet<Byte>();
		dgkhlx.add((byte) 1);
		dgkhlx.add((byte) 3);
		Map<AmlRuleEngine, List<ModelAmlCorptrn>> bigAmountMap = new HashMap<AmlRuleEngine, List<ModelAmlCorptrn>>();
		Map<AmlRuleEngine, List<ModelAmlInditrn>> bigAmountInditrnMap = new HashMap<AmlRuleEngine, List<ModelAmlInditrn>>();
		if (info.getType().equals((byte) 1) || info.getType().equals((byte) 3)) {
			// 对公大额
			List<AmlRuleEngine> bigAmountAmlRuleEnginelist = amlRuleEngineRepo
					.findAmlRuleEngineByEnabledandTypeAndKhlxIn(new Integer(1), new Integer(1), dgkhlx);
			if (bigAmountAmlRuleEnginelist.size() > 0) {
				for (AmlRuleEngine amlRuleEngine : bigAmountAmlRuleEnginelist) {
					List<ModelAmlCorptrn> listFilter = bigAmountfilter(amlRuleEngine, endDate);
					bigAmountMap.put(amlRuleEngine, listFilter);
				}
			}
			// 对私大额
			List<AmlRuleEngine> bigAmountInditrnAmlRuleEnginelist = amlRuleEngineRepo
					.findAmlRuleEngineByEnabledandTypeAndKhlxIn(new Integer(1), new Integer(1), dskhlx);
			if (bigAmountInditrnAmlRuleEnginelist.size() > 0) {
				for (AmlRuleEngine amlRuleEngine : bigAmountInditrnAmlRuleEnginelist) {
					List<ModelAmlInditrn> listFilter = bigAmountInditrnfilter(amlRuleEngine, endDate);
					bigAmountInditrnMap.put(amlRuleEngine, listFilter);
				}
			}
		}
		Long t5 = System.currentTimeMillis();
		System.out.println("--大额数据过滤完成，耗时：" + (t5 - t4) / 1000 + "s");

		Map<AmlRuleEngine, List<ModelAmlCorptrn>> suspiciousMap = new HashMap<AmlRuleEngine, List<ModelAmlCorptrn>>();
		Map<AmlRuleEngine, List<ModelAmlInditrn>> suspiciousInditrnMap = new HashMap<AmlRuleEngine, List<ModelAmlInditrn>>();
		if (info.getType().equals((byte) 2) || info.getType().equals((byte) 3)) {
			// 对公可疑
			List<AmlRuleEngine> suspiciousAmlRuleEnginelist = amlRuleEngineRepo
					.findAmlRuleEngineByEnabledandTypeAndKhlxIn(new Integer(1), new Integer(2), dgkhlx);
			if (suspiciousAmlRuleEnginelist.size() > 0) {
				for (AmlRuleEngine amlRuleEngine : suspiciousAmlRuleEnginelist) {
					List<ModelAmlCorptrn> listFilter = suspiciousfilter(amlRuleEngine, endDate);
					suspiciousMap.put(amlRuleEngine, listFilter);
				}
			}

			// 对私可疑
			List<AmlRuleEngine> suspiciousInditrnAmlRuleEnginelist = amlRuleEngineRepo
					.findAmlRuleEngineByEnabledandTypeAndKhlxIn(new Integer(1), new Integer(2), dskhlx);
			if (suspiciousInditrnAmlRuleEnginelist.size() > 0) {
				for (AmlRuleEngine amlRuleEngine : suspiciousInditrnAmlRuleEnginelist) {
					List<ModelAmlInditrn> listFilter = suspiciousInditrnfilter(amlRuleEngine, endDate);
					suspiciousInditrnMap.put(amlRuleEngine, listFilter);
				}
			}
		}
		Long t6 = System.currentTimeMillis();
		System.out.println("--可疑数据过滤完成，耗时：" + (t6 - t5) / 1000 + "s");

		List<BigAmount> bigAmountList = getBigAmountList(bigAmountMap, endDate);

		List<BigAmount> bigAmountInditrnList = getInditrnBigAmountList(bigAmountInditrnMap, endDate);

		List<AmlSuspicious> amlSuspiciousList = getAmlSuspiciousList(suspiciousMap, endDate);

		List<AmlSuspicious> amlSuspiciousInditrnList = getInditrnAmlSuspiciousList(suspiciousInditrnMap, endDate);
		Long t7 = System.currentTimeMillis();
		System.out.println("--对公数据转换大额可疑数据完成，耗时：" + (t7 - t6) / 1000 + "s");

		workflowService.initBigAmount(bigAmountList);
		workflowService.initBigAmount(bigAmountInditrnList);
		workflowService.initAmlSuspicious(amlSuspiciousList);
		workflowService.initAmlSuspicious(amlSuspiciousInditrnList);
		Long t8 = System.currentTimeMillis();
		System.out.println("--大额数据校验完成，耗时：" + (t8 - t7) / 1000 + "s");
		bigSuspCheckService.destroyCacheTemBig();
		workflowService.saveBigAmountList(bigAmountList);
		workflowService.saveBigAmountList(bigAmountInditrnList);
		workflowService.saveAmlSuspiciousList(amlSuspiciousList);
		workflowService.saveAmlSuspiciousList(amlSuspiciousInditrnList);
		Long t9 = System.currentTimeMillis();
		System.out.println("--数据保存完成，耗时：" + (t9 - t8) / 1000 + "s");
		taskJobInfo.setResult("大额数据一共采集到 " + (bigAmountList.size() + bigAmountInditrnList.size()) + " 条,可疑数据一共采集到 "
				+ (amlSuspiciousList.size() + amlSuspiciousInditrnList.size()) + " 条。");

		taskJobInfo.setDateUpdate(new Date());
		taskJobInfoRepo.updateJobr(taskJobInfo.getResult(), taskJobInfo.getBatchId());
		return STATUS_ENUM.SUCCESS;
	}

	public String getSqlString(String ruleExpressName) {
		String sqlString = ruleExpressName.replace(",", "");
		setSymbolMap();
		for (Map.Entry<String, String> entry : symbolMap.entrySet()) {
			sqlString = sqlString.replace(entry.getKey(), entry.getValue());
		}
		if (null == suspiciousPropDictList || suspiciousPropDictList.size() == 0) {
			suspiciousPropDictList = suspiciousPropDictRepo.findAll();
		}
		if (null == modelPropDictList || modelPropDictList.size() == 0) {
			Set<String> set = new HashSet<String>();
			for (SuspiciousPropDict suspiciousPropDict : suspiciousPropDictList) {
				if ("enum".equals(suspiciousPropDict.getPropType())) {
					set.add(suspiciousPropDict.getRulePropCode());
				}
			}
			if (set.size() > 0) {
				modelPropDictList = dataDictService.getDictsByFields("T_MODEL_AML_CORPTRN", set);
			} else {
				modelPropDictList = new ArrayList<ModelPropDict>();
			}
		}
		for (SuspiciousPropDict suspiciousPropDict : suspiciousPropDictList) {
			sqlString = sqlString.replace("$" + suspiciousPropDict.getRulePropName() + "$",
					suspiciousPropDict.getRulePropCode());
		}
		for (ModelPropDict modelPropDict : modelPropDictList) {
			sqlString = sqlString.replace("#" + modelPropDict.getValu() + "#", modelPropDict.getKe());
		}
		return sqlString;
	}

	// 对私
	public Map<String, Map<String, List<ModelAmlInditrn>>> getAllModelAmlInditrnMap(Date startTime, Date endTime) {
		List<ModelAmlInditrn> allList = modelAmlInditrnRepo.findByJyrqGreaterThanEqualAndJyrqLessThanEqual(startTime,
				endTime);
		Map<String, Map<String, List<ModelAmlInditrn>>> allMap = new HashMap<String, Map<String, List<ModelAmlInditrn>>>();
		for (ModelAmlInditrn modelAmlInditrn : allList) {
			if (null == allMap.get(modelAmlInditrn.getHzzj())) {
				Map<String, List<ModelAmlInditrn>> map = new HashMap<String, List<ModelAmlInditrn>>();
				// 客户-交易list (收/贷)
				map.put("C", new ArrayList<ModelAmlInditrn>());
				// 客户-交易list (支/借)
				map.put("D", new ArrayList<ModelAmlInditrn>());
				allMap.put(modelAmlInditrn.getHzzj(), map);
			}
			Map<String, List<ModelAmlInditrn>> map = allMap.get(modelAmlInditrn.getHzzj());
			if ("C".equals(modelAmlInditrn.getJdbj())) {
				map.get("C").add(modelAmlInditrn);
			}
			if ("D".equals(modelAmlInditrn.getJdbj())) {
				map.get("D").add(modelAmlInditrn);
			}
		}
		return allMap;
	}

	// 对公
	Map<String, Map<String, List<ModelAmlCorptrn>>> getAllModelAmlCorptrnMap(Date startTime, Date endTime) {
		List<ModelAmlCorptrn> allList = modelAmlCorptrnRepo.findByJyrqGreaterThanAndJyrqLessThanEqual(startTime,
				endTime);
		Map<String, Map<String, List<ModelAmlCorptrn>>> allMap = new HashMap<String, Map<String, List<ModelAmlCorptrn>>>();
		for (ModelAmlCorptrn modelAmlCorptrn : allList) {
			if (null == allMap.get(modelAmlCorptrn.getHzzj())) {
				Map<String, List<ModelAmlCorptrn>> map = new HashMap<String, List<ModelAmlCorptrn>>();
				// 客户-交易list (收/贷)
				map.put("C", new ArrayList<ModelAmlCorptrn>());
				// 客户-交易list (支/借)
				map.put("D", new ArrayList<ModelAmlCorptrn>());
				allMap.put(modelAmlCorptrn.getHzzj(), map);
			}
			Map<String, List<ModelAmlCorptrn>> map = allMap.get(modelAmlCorptrn.getHzzj());
			if ("C".equals(modelAmlCorptrn.getJdbj())) {
				map.get("C").add(modelAmlCorptrn);
			}
			if ("D".equals(modelAmlCorptrn.getJdbj())) {
				map.get("D").add(modelAmlCorptrn);
			}
		}
		return allMap;
	}

	// 对公可疑过滤
	public List<ModelAmlCorptrn> suspiciousfilter(AmlRuleEngine amlRuleEngine, Date endDate) throws Exception {
		List<ModelAmlCorptrn> temp = new ArrayList<ModelAmlCorptrn>();
		Calendar date = Calendar.getInstance();
		date.setTime(endDate);
		if (null == amlRuleEngine.getDateRange() || amlRuleEngine.getDateRange() == 0) {
			amlRuleEngine.setDateRange(3);
		}
		date.set(Calendar.DATE, date.get(Calendar.DATE) - amlRuleEngine.getDateRange());
		Date startDate = dft.parse(dft.format(date.getTime()));
		Map<String, Map<String, List<ModelAmlCorptrn>>> allMap = getAllModelAmlCorptrnMap(startDate, endDate);
		List<ModelAmlCorptrn> modelAmlCorptrnWithSqlList = new ArrayList<ModelAmlCorptrn>();
		Set<String> cnumSet = new HashSet<String>();

		String sql = getSqlString(amlRuleEngine.getRuleExpressName());
		modelAmlCorptrnWithSqlList = modelAmlCorptrnRepo.getWithTime(sql, startDate, endDate);
		if (modelAmlCorptrnWithSqlList.size() == 0) {
			return temp;
		}
		for (ModelAmlCorptrn modelAmlCorptrn : modelAmlCorptrnWithSqlList) {
			cnumSet.add(modelAmlCorptrn.getHzzj());
		}

		// 根据客户条件过滤
		for (Map.Entry<String, Map<String, List<ModelAmlCorptrn>>> entry : allMap.entrySet()) {
			if (!cnumSet.contains(entry.getKey())) {
				continue;
			}

			if (!isDebitCreditCountAndOr(amlRuleEngine.getDebitCreditCountAndor(),
					amlRuleEngine.getCreditCountCondition(), amlRuleEngine.getDebitCountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditCount(),
					amlRuleEngine.getDebitCount())) {
				continue;
			}

			if (!isDebitCreditAmountAndOr(amlRuleEngine.getDebitCreditAmountAndor(),
					amlRuleEngine.getCreditAmountCondition(), amlRuleEngine.getDebitAmountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditAmount(),
					amlRuleEngine.getDebitAmount())) {
				continue;
			}

			if (!isDebitCreditPercentAndOr(amlRuleEngine.getDebitCreditPercentAndor(),
					amlRuleEngine.getCreditDebitPercentCondition(), amlRuleEngine.getDebitCreditPercentCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditDebitPercent(),
					amlRuleEngine.getDebitCreditPercent())) {
				continue;
			}
			if (!isDollarDebitCreditAmountAndOr(amlRuleEngine.getDollarDebitCreditAmountAndor(),
					amlRuleEngine.getDollarCreditAmountCondition(), amlRuleEngine.getDollarDebitAmountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getDollarCreditAmount(),
					amlRuleEngine.getDollarDebitAmount())) {
				continue;
			}
			if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
				if (!isContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
						amlRuleEngine.getContinuousDays(), endDate)) {
					continue;
				}
			}
			if ("01".equals(amlRuleEngine.getSameCounterparties())) {
				if (!isSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
					continue;
				}
			}
			if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
				if (!isTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
						entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
					continue;
				}
			}
			if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
				if (!isIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate, entry.getKey(),
						amlRuleEngine.getIntervalDays())) {
					continue;
				}
			}
			temp.addAll(entry.getValue().get("D"));
			temp.addAll(entry.getValue().get("C"));
		}
		// 没有可疑属性过滤条件
		if (modelAmlCorptrnWithSqlList.size() == 0) {
			return temp;
		}
		// 客户属性过滤没有产生可疑交易
		if (temp.size() == 0) {
			return temp;
		}
		List<ModelAmlCorptrn> tempFinal = new ArrayList<ModelAmlCorptrn>();
		for (ModelAmlCorptrn modelAmlCorptrn : modelAmlCorptrnWithSqlList) {
			if (temp.contains(modelAmlCorptrn)) {
				tempFinal.add(modelAmlCorptrn);
			}
		}
		return tempFinal;
	}

	// 对私可疑过滤
	public List<ModelAmlInditrn> suspiciousInditrnfilter(AmlRuleEngine amlRuleEngine, Date endDate) throws Exception {
		List<ModelAmlInditrn> temp = new ArrayList<ModelAmlInditrn>();
		Calendar date = Calendar.getInstance();
		date.setTime(endDate);
		if (null == amlRuleEngine.getDateRange() || amlRuleEngine.getDateRange() == 0) {
			amlRuleEngine.setDateRange(3);
		}
		date.set(Calendar.DATE, date.get(Calendar.DATE) - amlRuleEngine.getDateRange());
		Date startDate = dft.parse(dft.format(date.getTime()));
		Map<String, Map<String, List<ModelAmlInditrn>>> allMap = getAllModelAmlInditrnMap(startDate, endDate);
		List<ModelAmlInditrn> modelAmlInditrnWithSqlList = new ArrayList<ModelAmlInditrn>();
		Set<String> cnumSet = new HashSet<String>();

		String sql = getSqlString(amlRuleEngine.getRuleExpressName());
		modelAmlInditrnWithSqlList = modelAmlInditrnRepo.getWithTime(sql, startDate, endDate);
		if (modelAmlInditrnWithSqlList.size() == 0) {
			return temp;
		}
		for (ModelAmlInditrn modelAmlInditrn : modelAmlInditrnWithSqlList) {
			cnumSet.add(modelAmlInditrn.getHzzj());
		}

		// 根据客户条件过滤
		for (Map.Entry<String, Map<String, List<ModelAmlInditrn>>> entry : allMap.entrySet()) {
			if (!cnumSet.contains(entry.getKey())) {
				continue;
			}

			if (!isInditrnDebitCreditCountAndOr(amlRuleEngine.getDebitCreditCountAndor(),
					amlRuleEngine.getCreditCountCondition(), amlRuleEngine.getDebitCountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditCount(),
					amlRuleEngine.getDebitCount())) {
				continue;
			}

			if (!isInditrnDebitCreditAmountAndOr(amlRuleEngine.getDebitCreditAmountAndor(),
					amlRuleEngine.getCreditAmountCondition(), amlRuleEngine.getDebitAmountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditAmount(),
					amlRuleEngine.getDebitAmount())) {
				continue;
			}

			if (!isInditrnDebitCreditPercentAndOr(amlRuleEngine.getDebitCreditPercentAndor(),
					amlRuleEngine.getCreditDebitPercentCondition(), amlRuleEngine.getDebitCreditPercentCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getCreditDebitPercent(),
					amlRuleEngine.getDebitCreditPercent())) {
				continue;
			}
			if (!isInditrnDollarDebitCreditAmountAndOr(amlRuleEngine.getDollarDebitCreditAmountAndor(),
					amlRuleEngine.getDollarCreditAmountCondition(), amlRuleEngine.getDollarDebitAmountCondition(),
					entry.getValue().get("C"), entry.getValue().get("D"), amlRuleEngine.getDollarCreditAmount(),
					amlRuleEngine.getDollarDebitAmount())) {
				continue;
			}
			if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
				if (!isInditrnContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
						amlRuleEngine.getContinuousDays(), endDate)) {
					continue;
				}
			}
			if ("01".equals(amlRuleEngine.getSameCounterparties())) {
				if (!isInditrnSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
					continue;
				}
			}
			if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
				if (!isInditrnTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
						entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
					continue;
				}
			}
			if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
				if (!isInditrnIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate, entry.getKey(),
						amlRuleEngine.getIntervalDays())) {
					continue;
				}
			}
			temp.addAll(entry.getValue().get("D"));
			temp.addAll(entry.getValue().get("C"));
		}
		// 没有可疑属性过滤条件
		if (modelAmlInditrnWithSqlList.size() == 0) {
			return temp;
		}
		// 客户属性过滤没有产生可疑交易
		if (temp.size() == 0) {
			return temp;
		}
		List<ModelAmlInditrn> tempFinal = new ArrayList<ModelAmlInditrn>();
		for (ModelAmlInditrn modelAmlInditrn : modelAmlInditrnWithSqlList) {
			if (temp.contains(modelAmlInditrn)) {
				tempFinal.add(modelAmlInditrn);
			}
		}
		return tempFinal;
	}

	// 大额过滤 对公
	public List<ModelAmlCorptrn> bigAmountfilter(AmlRuleEngine amlRuleEngine, Date endDate) throws Exception {
		List<ModelAmlCorptrn> temp = new ArrayList<ModelAmlCorptrn>();
		Calendar date = Calendar.getInstance();
		date.setTime(endDate);
		if (null == amlRuleEngine.getDateRange() || amlRuleEngine.getDateRange() == 0) {
			amlRuleEngine.setDateRange(3);
		}
		date.set(Calendar.DATE, date.get(Calendar.DATE) - amlRuleEngine.getDateRange());
		Date startDate = dft.parse(dft.format(date.getTime()));
		Map<String, Map<String, List<ModelAmlCorptrn>>> allMap = getAllModelAmlCorptrnMap(startDate, endDate);
		List<ModelAmlCorptrn> modelAmlCorptrnWithSqlList = new ArrayList<ModelAmlCorptrn>();
		Set<String> cnumSet = new HashSet<String>();

		String sql = getSqlString(amlRuleEngine.getRuleExpressName());
		modelAmlCorptrnWithSqlList = modelAmlCorptrnRepo.getWithTime(sql, startDate, endDate);
		if (modelAmlCorptrnWithSqlList.size() == 0) {
			return temp;
		}
		for (ModelAmlCorptrn modelAmlCorptrn : modelAmlCorptrnWithSqlList) {
			cnumSet.add(modelAmlCorptrn.getHzzj());
		}

		boolean hasCredit = false;
		if ((null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0)
				|| (null != amlRuleEngine.getCreditAmount()
						&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0)
				|| (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f)
				|| (null != amlRuleEngine.getDollarCreditAmount()
						&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0)) {
			hasCredit = true;
		}
		boolean hasDebit = false;
		if ((null != amlRuleEngine.getDebitCount() && amlRuleEngine.getDebitCount() > 0)
				|| (null != amlRuleEngine.getDebitAmount()
						&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0)
				|| (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f)
				|| (null != amlRuleEngine.getDollarDebitAmount()
						&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0)) {
			hasDebit = true;
		}
		if (hasCredit) {
			// 根据客户条件过滤-收入C
			for (Map.Entry<String, Map<String, List<ModelAmlCorptrn>>> entry : allMap.entrySet()) {
				if (!cnumSet.contains(entry.getKey())) {
					continue;
				}
				if ("or".equals(amlRuleEngine.getDebitCreditCountAndor())) {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getCreditCountCondition(), entry.getValue().get("C"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getCreditCountCondition(), entry.getValue().get("C"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getDebitCountCondition(), entry.getValue().get("D"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				}
				if (isCNY(entry.getValue().get("C"))) {
					if ("or".equals(amlRuleEngine.getDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(), entry.getValue().get("D"),
									amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					}
				} else {
					BigDecimal debitAmount = BigDecimal.valueOf(0);
					BigDecimal creditAmount = BigDecimal.valueOf(0);
					if (hasCNY((entry.getValue().get("C")))) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							debitAmount = compareCnyAndUsd(amlRuleEngine.getDebitAmount(),
									amlRuleEngine.getDollarDebitAmount()) ? amlRuleEngine.getDollarDebitAmount()
											: getUSDAmount("CNY", amlRuleEngine.getDebitAmount());
						} else {
							debitAmount = amlRuleEngine.getDollarDebitAmount();
						}
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							creditAmount = compareCnyAndUsd(amlRuleEngine.getCreditAmount(),
									amlRuleEngine.getDollarCreditAmount()) ? amlRuleEngine.getDollarCreditAmount()
											: getUSDAmount("CNY", amlRuleEngine.getCreditAmount());
						} else {
							creditAmount = amlRuleEngine.getDollarCreditAmount();
						}
					} else {
						debitAmount = amlRuleEngine.getDollarDebitAmount();
						creditAmount = amlRuleEngine.getDollarCreditAmount();
					}
					if ("or".equals(amlRuleEngine.getDollarDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					}
				}

				if ("or".equals(amlRuleEngine.getDebitCreditPercentAndor())) {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				}

				if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
					if (!isContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
							amlRuleEngine.getContinuousDays(), endDate)) {
						continue;
					}
				}
				if ("01".equals(amlRuleEngine.getSameCounterparties())) {
					if (!isSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
						continue;
					}
				}
				if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
					if (!isTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
							entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
						continue;
					}
				}
				if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
					if (!isIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate, entry.getKey(),
							amlRuleEngine.getIntervalDays())) {
						continue;
					}
				}
				temp.addAll(entry.getValue().get("C"));
			}
		}
		if (hasDebit) {
			// 根据客户条件过滤-支出D
			for (Map.Entry<String, Map<String, List<ModelAmlCorptrn>>> entry : allMap.entrySet()) {
				if (!cnumSet.contains(entry.getKey())) {
					continue;
				}
				if ("or".equals(amlRuleEngine.getDebitCreditCountAndor())) {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getDebitCountCondition(), entry.getValue().get("D"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getCreditCountCondition(), entry.getValue().get("C"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isDebitCreditCount(amlRuleEngine.getDebitCountCondition(), entry.getValue().get("D"),
								amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				}
				if (isCNY(entry.getValue().get("D"))) {
					if ("or".equals(amlRuleEngine.getDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(), entry.getValue().get("D"),
									amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(), entry.getValue().get("D"),
									amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					}
				} else {
					BigDecimal debitAmount = BigDecimal.valueOf(0);
					BigDecimal creditAmount = BigDecimal.valueOf(0);
					if (hasCNY((entry.getValue().get("D")))) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							debitAmount = compareCnyAndUsd(amlRuleEngine.getDebitAmount(),
									amlRuleEngine.getDollarDebitAmount()) ? amlRuleEngine.getDollarDebitAmount()
											: getUSDAmount("CNY", amlRuleEngine.getDebitAmount());
						} else {
							debitAmount = amlRuleEngine.getDollarDebitAmount();
						}
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							creditAmount = compareCnyAndUsd(amlRuleEngine.getCreditAmount(),
									amlRuleEngine.getDollarCreditAmount()) ? amlRuleEngine.getDollarCreditAmount()
											: getUSDAmount("CNY", amlRuleEngine.getCreditAmount());
						} else {
							creditAmount = amlRuleEngine.getDollarCreditAmount();
						}
					} else {
						debitAmount = amlRuleEngine.getDollarDebitAmount();
						creditAmount = amlRuleEngine.getDollarCreditAmount();
					}
					if ("or".equals(amlRuleEngine.getDollarDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					}
				}

				if ("or".equals(amlRuleEngine.getDebitCreditPercentAndor())) {
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				}

				if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
					if (!isContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
							amlRuleEngine.getContinuousDays(), endDate)) {
						continue;
					}
				}
				if ("01".equals(amlRuleEngine.getSameCounterparties())) {
					if (!isSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
						continue;
					}
				}
				if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
					if (!isTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
							entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
						continue;
					}
				}
				if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
					if (!isIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate, entry.getKey(),
							amlRuleEngine.getIntervalDays())) {
						continue;
					}
				}
				temp.addAll(entry.getValue().get("D"));
			}
		}
		// 没有可疑属性过滤条件
		if (modelAmlCorptrnWithSqlList.size() == 0) {
			return temp;
		}
		// 客户属性过滤没有产生可疑交易
		if (temp.size() == 0) {
			return temp;
		}
		List<ModelAmlCorptrn> tempFinal = new ArrayList<ModelAmlCorptrn>();
		for (ModelAmlCorptrn modelAmlCorptrn : modelAmlCorptrnWithSqlList) {
			if (temp.contains(modelAmlCorptrn)) {
				tempFinal.add(modelAmlCorptrn);
			}
		}
		return tempFinal;
	}

	// 大额过滤 对私
	public List<ModelAmlInditrn> bigAmountInditrnfilter(AmlRuleEngine amlRuleEngine, Date endDate) throws Exception {
		List<ModelAmlInditrn> temp = new ArrayList<ModelAmlInditrn>();
		Calendar date = Calendar.getInstance();
		date.setTime(endDate);
		if (null == amlRuleEngine.getDateRange() || amlRuleEngine.getDateRange() == 0) {
			amlRuleEngine.setDateRange(3);
		}
		date.set(Calendar.DATE, date.get(Calendar.DATE) - amlRuleEngine.getDateRange());
		Date startDate = dft.parse(dft.format(date.getTime()));
		Map<String, Map<String, List<ModelAmlInditrn>>> allMap = getAllModelAmlInditrnMap(startDate, endDate);
		List<ModelAmlInditrn> modelAmlInditrnWithSqlList = new ArrayList<ModelAmlInditrn>();
		Set<String> cnumSet = new HashSet<String>();

		String sql = getSqlString(amlRuleEngine.getRuleExpressName());
		modelAmlInditrnWithSqlList = modelAmlInditrnRepo.getWithTime(sql, startDate, endDate);
		if (modelAmlInditrnWithSqlList.size() == 0) {
			return temp;
		}
		for (ModelAmlInditrn modelAmlInditrn : modelAmlInditrnWithSqlList) {
			cnumSet.add(modelAmlInditrn.getHzzj());
		}

		boolean hasCredit = false;
		if ((null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0)
				|| (null != amlRuleEngine.getCreditAmount()
						&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0)
				|| (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f)
				|| (null != amlRuleEngine.getDollarCreditAmount()
						&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0)) {
			hasCredit = true;
		}
		boolean hasDebit = false;
		if ((null != amlRuleEngine.getDebitCount() && amlRuleEngine.getDebitCount() > 0)
				|| (null != amlRuleEngine.getDebitAmount()
						&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0)
				|| (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f)
				|| (null != amlRuleEngine.getDollarDebitAmount()
						&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0)) {
			hasDebit = true;
		}
		if (hasCredit) {
			// 根据客户条件过滤-收入C
			for (Map.Entry<String, Map<String, List<ModelAmlInditrn>>> entry : allMap.entrySet()) {
				if (!cnumSet.contains(entry.getKey())) {
					continue;
				}
				if ("or".equals(amlRuleEngine.getDebitCreditCountAndor())) {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getCreditCountCondition(),
								entry.getValue().get("C"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getCreditCountCondition(),
								entry.getValue().get("C"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getDebitCountCondition(),
								entry.getValue().get("D"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				}
				if (isInditrnCNY(entry.getValue().get("C"))) {
					if ("or".equals(amlRuleEngine.getDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(),
									entry.getValue().get("D"), amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					}
				} else {
					BigDecimal debitAmount = BigDecimal.valueOf(0);
					BigDecimal creditAmount = BigDecimal.valueOf(0);
					if (hasInditrnCNY((entry.getValue().get("C")))) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							debitAmount = compareCnyAndUsd(amlRuleEngine.getDebitAmount(),
									amlRuleEngine.getDollarDebitAmount()) ? amlRuleEngine.getDollarDebitAmount()
											: getUSDAmount("CNY", amlRuleEngine.getDebitAmount());
						} else {
							debitAmount = amlRuleEngine.getDollarDebitAmount();
						}
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							creditAmount = compareCnyAndUsd(amlRuleEngine.getCreditAmount(),
									amlRuleEngine.getDollarCreditAmount()) ? amlRuleEngine.getDollarCreditAmount()
											: getUSDAmount("CNY", amlRuleEngine.getCreditAmount());
						} else {
							creditAmount = amlRuleEngine.getDollarCreditAmount();
						}
					} else {
						debitAmount = amlRuleEngine.getDollarDebitAmount();
						creditAmount = amlRuleEngine.getDollarCreditAmount();
					}
					if ("or".equals(amlRuleEngine.getDollarDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					}
				}

				if ("or".equals(amlRuleEngine.getDebitCreditPercentAndor())) {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				}

				if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
					if (!isInditrnContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
							amlRuleEngine.getContinuousDays(), endDate)) {
						continue;
					}
				}
				if ("01".equals(amlRuleEngine.getSameCounterparties())) {
					if (!isInditrnSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
						continue;
					}
				}
				if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
					if (!isInditrnTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
							entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
						continue;
					}
				}
				if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
					if (!isInditrnIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate,
							entry.getKey(), amlRuleEngine.getIntervalDays())) {
						continue;
					}
				}

				temp.addAll(entry.getValue().get("C"));
			}
		}
		if (hasDebit) {
			// 根据客户条件过滤-支出D
			for (Map.Entry<String, Map<String, List<ModelAmlInditrn>>> entry : allMap.entrySet()) {
				if (!cnumSet.contains(entry.getKey())) {
					continue;
				}
				if ("or".equals(amlRuleEngine.getDebitCreditCountAndor())) {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getDebitCountCondition(),
								entry.getValue().get("D"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getCreditCountCondition(),
								entry.getValue().get("C"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
					if (null != amlRuleEngine.getCreditCount() && amlRuleEngine.getCreditCount() > 0) {
						if (!isInditrnDebitCreditCount(amlRuleEngine.getDebitCountCondition(),
								entry.getValue().get("D"), amlRuleEngine.getCreditCount())) {
							continue;
						}
					}
				}
				if (isInditrnCNY(entry.getValue().get("D"))) {
					if ("or".equals(amlRuleEngine.getDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(),
									entry.getValue().get("D"), amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getCreditAmountCondition(),
									entry.getValue().get("C"), amlRuleEngine.getCreditAmount())) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDebitCreditAmount(amlRuleEngine.getDebitAmountCondition(),
									entry.getValue().get("D"), amlRuleEngine.getDebitAmount())) {
								continue;
							}
						}
					}
				} else {
					BigDecimal debitAmount = BigDecimal.valueOf(0);
					BigDecimal creditAmount = BigDecimal.valueOf(0);
					if (hasInditrnCNY((entry.getValue().get("D")))) {
						if (null != amlRuleEngine.getDebitAmount()
								&& amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							debitAmount = compareCnyAndUsd(amlRuleEngine.getDebitAmount(),
									amlRuleEngine.getDollarDebitAmount()) ? amlRuleEngine.getDollarDebitAmount()
											: getUSDAmount("CNY", amlRuleEngine.getDebitAmount());
						} else {
							debitAmount = amlRuleEngine.getDollarDebitAmount();
						}
						if (null != amlRuleEngine.getCreditAmount()
								&& amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0
								&& null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							creditAmount = compareCnyAndUsd(amlRuleEngine.getCreditAmount(),
									amlRuleEngine.getDollarCreditAmount()) ? amlRuleEngine.getDollarCreditAmount()
											: getUSDAmount("CNY", amlRuleEngine.getCreditAmount());
						} else {
							creditAmount = amlRuleEngine.getDollarCreditAmount();
						}
					} else {
						debitAmount = amlRuleEngine.getDollarDebitAmount();
						creditAmount = amlRuleEngine.getDollarCreditAmount();
					}
					if ("or".equals(amlRuleEngine.getDollarDebitCreditAmountAndor())) {
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					} else {
						if (null != amlRuleEngine.getDollarCreditAmount()
								&& amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarCreditAmountCondition(),
									entry.getValue().get("C"), creditAmount)) {
								continue;
							}
						}
						if (null != amlRuleEngine.getDollarDebitAmount()
								&& amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0)) > 0) {
							if (!isInditrnDollarDebitCreditAmount(amlRuleEngine.getDollarDebitAmountCondition(),
									entry.getValue().get("D"), debitAmount)) {
								continue;
							}
						}
					}
				}

				if ("or".equals(amlRuleEngine.getDebitCreditPercentAndor())) {
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				} else {
					if (null != amlRuleEngine.getCreditDebitPercent() && amlRuleEngine.getCreditDebitPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getCreditDebitPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getCreditDebitPercent(), (byte) 1)) {
							continue;
						}
					}
					if (null != amlRuleEngine.getDebitCreditPercent() && amlRuleEngine.getDebitCreditPercent() > 0f) {
						if (!isInditrnDebitCreditPercent(amlRuleEngine.getDebitCreditPercentCondition(),
								entry.getValue().get("C"), entry.getValue().get("D"),
								amlRuleEngine.getDebitCreditPercent(), (byte) 2)) {
							continue;
						}
					}
				}

				if (null != amlRuleEngine.getContinuousDays() && amlRuleEngine.getContinuousDays() > 0) {
					if (!isInditrnContinuousDays(entry.getValue().get("D"), entry.getValue().get("C"),
							amlRuleEngine.getContinuousDays(), endDate)) {
						continue;
					}
				}
				if ("01".equals(amlRuleEngine.getSameCounterparties())) {
					if (!isInditrnSameCounterparties(entry.getValue().get("C"), entry.getValue().get("D"))) {
						continue;
					}
				}
				if (null != amlRuleEngine.getTotalCount() && amlRuleEngine.getTotalCount() > 0) {
					if (!isInditrnTotalCount(amlRuleEngine.getTotalCountCondition(), entry.getValue().get("C"),
							entry.getValue().get("D"), amlRuleEngine.getTotalCount())) {
						continue;
					}
				}
				if (null != amlRuleEngine.getIntervalDays() && amlRuleEngine.getIntervalDays() > 0) {
					if (!isInditrnIntervalDays(amlRuleEngine.getIntervalDaysCondition(), startDate, endDate,
							entry.getKey(), amlRuleEngine.getIntervalDays())) {
						continue;
					}
				}
				temp.addAll(entry.getValue().get("D"));
			}
		}
		// 没有可疑属性过滤条件
		if (modelAmlInditrnWithSqlList.size() == 0) {
			return temp;
		}
		// 客户属性过滤没有产生可疑交易
		if (temp.size() == 0) {
			return temp;
		}
		List<ModelAmlInditrn> tempFinal = new ArrayList<ModelAmlInditrn>();
		for (ModelAmlInditrn modelAmlInditrn : modelAmlInditrnWithSqlList) {
			if (temp.contains(modelAmlInditrn)) {
				tempFinal.add(modelAmlInditrn);
			}
		}
		return tempFinal;
	}

	// 收入笔数/支出笔数(且或) 对公
	private boolean isDebitCreditCountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlCorptrn> creditList, List<ModelAmlCorptrn> debitList, Integer creditCount,
			Integer debitCount) {
		if (null != creditCount && creditCount > 0 && null != debitCount && debitCount > 0) {
			if ("or".equals(andOr)) {
				return (isDebitCreditCount(creditCondition, creditList, creditCount)
						|| isDebitCreditCount(debitCondition, debitList, debitCount));
			} else {
				return (isDebitCreditCount(creditCondition, creditList, creditCount)
						&& isDebitCreditCount(debitCondition, debitList, debitCount));
			}
		}
		if (null != creditCount && creditCount > 0) {
			return isDebitCreditCount(creditCondition, creditList, creditCount);
		}
		if (null != debitCount && debitCount > 0) {
			return isDebitCreditCount(debitCondition, debitList, debitCount);
		}
		return true;
	}

	// 收入笔数/支出笔数(且/或) 对私
	private boolean isInditrnDebitCreditCountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlInditrn> creditList, List<ModelAmlInditrn> debitList, Integer creditCount,
			Integer debitCount) {
		if (null != creditCount && creditCount > 0 && null != debitCount && debitCount > 0) {
			if ("or".equals(andOr)) {
				return (isInditrnDebitCreditCount(creditCondition, creditList, creditCount)
						|| isInditrnDebitCreditCount(debitCondition, debitList, debitCount));
			} else {
				return (isInditrnDebitCreditCount(creditCondition, creditList, creditCount)
						&& isInditrnDebitCreditCount(debitCondition, debitList, debitCount));
			}
		}
		if (null != creditCount && creditCount > 0) {
			return isInditrnDebitCreditCount(creditCondition, creditList, creditCount);
		}
		if (null != debitCount && debitCount > 0) {
			return isInditrnDebitCreditCount(debitCondition, debitList, debitCount);
		}
		return true;
	}

	// 收入/支出限额(且/或) 对公
	private boolean isDebitCreditAmountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlCorptrn> creditList, List<ModelAmlCorptrn> debitList, BigDecimal creditAmount,
			BigDecimal debitAmount) {
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0 && null != debitAmount
				&& debitAmount.compareTo(new BigDecimal(0)) > 0) {
			if ("or".equals(andOr)) {
				return (isDebitCreditAmount(creditCondition, creditList, creditAmount)
						|| isDebitCreditAmount(debitCondition, debitList, debitAmount));
			} else {
				return (isDebitCreditAmount(creditCondition, creditList, creditAmount)
						&& isDebitCreditAmount(debitCondition, debitList, debitAmount));
			}
		}
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0) {
			return isDebitCreditAmount(creditCondition, creditList, creditAmount);
		}
		if (null != debitAmount && debitAmount.compareTo(new BigDecimal(0)) > 0) {
			return isDebitCreditAmount(debitCondition, debitList, debitAmount);
		}
		return true;
	}

	// 收入/支出限额(且/或) 对私
	private boolean isInditrnDebitCreditAmountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlInditrn> creditList, List<ModelAmlInditrn> debitList, BigDecimal creditAmount,
			BigDecimal debitAmount) {
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0 && null != debitAmount
				&& debitAmount.compareTo(new BigDecimal(0)) > 0) {
			if ("or".equals(andOr)) {
				return (isInditrnDebitCreditAmount(creditCondition, creditList, creditAmount)
						|| isInditrnDebitCreditAmount(debitCondition, debitList, debitAmount));
			} else {
				return (isInditrnDebitCreditAmount(creditCondition, creditList, creditAmount)
						&& isInditrnDebitCreditAmount(debitCondition, debitList, debitAmount));
			}
		}
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0) {
			return isInditrnDebitCreditAmount(creditCondition, creditList, creditAmount);
		}
		if (null != debitAmount && debitAmount.compareTo(new BigDecimal(0)) > 0) {
			return isInditrnDebitCreditAmount(debitCondition, debitList, debitAmount);
		}
		return true;
	}

	// 美金收入/支出限额(且/或) 对公
	private boolean isDollarDebitCreditAmountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlCorptrn> creditList, List<ModelAmlCorptrn> debitList, BigDecimal creditAmount,
			BigDecimal debitAmount) {
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0 && null != debitAmount
				&& debitAmount.compareTo(new BigDecimal(0)) > 0) {
			if ("or".equals(andOr)) {
				return (isDollarDebitCreditAmount(creditCondition, creditList, creditAmount)
						|| isDebitCreditAmount(debitCondition, debitList, debitAmount));
			} else {
				return (isDollarDebitCreditAmount(creditCondition, creditList, creditAmount)
						&& isDebitCreditAmount(debitCondition, debitList, debitAmount));
			}
		}
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0) {
			return isDollarDebitCreditAmount(creditCondition, creditList, creditAmount);
		}
		if (null != debitAmount && debitAmount.compareTo(new BigDecimal(0)) > 0) {
			return isDollarDebitCreditAmount(debitCondition, debitList, debitAmount);
		}
		return true;
	}

	// 美金收入/支出限额(且/或) 对私
	private boolean isInditrnDollarDebitCreditAmountAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlInditrn> creditList, List<ModelAmlInditrn> debitList, BigDecimal creditAmount,
			BigDecimal debitAmount) {
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0 && null != debitAmount
				&& debitAmount.compareTo(new BigDecimal(0)) > 0) {
			if ("or".equals(andOr)) {
				return (isInditrnDollarDebitCreditAmount(creditCondition, creditList, creditAmount)
						|| isInditrnDebitCreditAmount(debitCondition, debitList, debitAmount));
			} else {
				return (isInditrnDollarDebitCreditAmount(creditCondition, creditList, creditAmount)
						&& isInditrnDebitCreditAmount(debitCondition, debitList, debitAmount));
			}
		}
		if (null != creditAmount && creditAmount.compareTo(new BigDecimal(0)) > 0) {
			return isInditrnDollarDebitCreditAmount(creditCondition, creditList, creditAmount);
		}
		if (null != debitAmount && debitAmount.compareTo(new BigDecimal(0)) > 0) {
			return isInditrnDollarDebitCreditAmount(debitCondition, debitList, debitAmount);
		}
		return true;
	}

	// 收入支出比/支出收入比(且/或) 对公
	private boolean isDebitCreditPercentAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlCorptrn> creditList, List<ModelAmlCorptrn> debitList, Float creditDebitpercent,
			Float debitCreditpercent) {
		if (null != creditDebitpercent && creditDebitpercent > 0f && null != debitCreditpercent
				&& debitCreditpercent > 0f) {
			if ("or".equals(andOr)) {
				return (isDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent, (byte) 1)
						|| isDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent, (byte) 2));
			} else {
				return (isDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent, (byte) 1)
						&& isDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent, (byte) 2));
			}
		}
		if (null != creditDebitpercent && creditDebitpercent > 0f) {
			return isDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent, (byte) 1);
		}
		if (null != debitCreditpercent && debitCreditpercent > 0f) {
			return isDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent, (byte) 2);
		}
		return true;
	}

	// 收入支出比/支出收入比(且/或) 对私
	private boolean isInditrnDebitCreditPercentAndOr(String andOr, String creditCondition, String debitCondition,
			List<ModelAmlInditrn> creditList, List<ModelAmlInditrn> debitList, Float creditDebitpercent,
			Float debitCreditpercent) {
		if (null != creditDebitpercent && creditDebitpercent > 0f && null != debitCreditpercent
				&& debitCreditpercent > 0f) {
			if ("or".equals(andOr)) {
				return (isInditrnDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent,
						(byte) 1)
						|| isInditrnDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent,
								(byte) 2));
			} else {
				return (isInditrnDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent,
						(byte) 1)
						&& isInditrnDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent,
								(byte) 2));
			}
		}
		if (null != creditDebitpercent && creditDebitpercent > 0f) {
			return isInditrnDebitCreditPercent(creditCondition, creditList, debitList, creditDebitpercent, (byte) 1);
		}
		if (null != debitCreditpercent && debitCreditpercent > 0f) {
			return isInditrnDebitCreditPercent(debitCondition, creditList, debitList, debitCreditpercent, (byte) 2);
		}
		return true;
	}

	// 收入笔数/支出笔数 对公
	private boolean isDebitCreditCount(String condition, List<ModelAmlCorptrn> list, Integer count) {
		if (null == list) {
			return false;
		}
		if ("=".equals(condition)) {
			return list.size() == count;
		}
		if (">".equals(condition)) {
			return list.size() > count;
		}
		if ("<".equals(condition)) {
			return list.size() < count;
		}
		if (">=".equals(condition)) {
			return list.size() >= count;
		}
		if ("<=".equals(condition)) {
			return list.size() <= count;
		}
		return false;
	}

	// 收入笔数/支出笔数 对私
	private boolean isInditrnDebitCreditCount(String condition, List<ModelAmlInditrn> list, Integer count) {
		if (null == list) {
			return false;
		}
		if ("=".equals(condition)) {
			return list.size() == count;
		}
		if (">".equals(condition)) {
			return list.size() > count;
		}
		if ("<".equals(condition)) {
			return list.size() < count;
		}
		if (">=".equals(condition)) {
			return list.size() >= count;
		}
		if ("<=".equals(condition)) {
			return list.size() <= count;
		}
		return false;
	}

	// 人民币收入/支出限额 对公
	private boolean isDebitCreditAmount(String condition, List<ModelAmlCorptrn> list, BigDecimal amount) {
		if (null == list) {
			return false;
		}
		BigDecimal totalAmount = new BigDecimal(0);
		for (ModelAmlCorptrn modelAmlCorptrn : list) {
			if (null != modelAmlCorptrn.getJyje()) {
				totalAmount = totalAmount.add(getCNYAmount(modelAmlCorptrn.getBz(), modelAmlCorptrn.getJyje()));
			}
		}
		if ("=".equals(condition)) {
			return totalAmount.compareTo(amount) == 0;
		}
		if (">".equals(condition)) {
			return totalAmount.compareTo(amount) > 0;
		}
		if ("<".equals(condition)) {
			return totalAmount.compareTo(amount) < 0;
		}
		if (">=".equals(condition)) {
			return totalAmount.compareTo(amount) >= 0;
		}
		if ("<=".equals(condition)) {
			return totalAmount.compareTo(amount) <= 0;
		}
		return false;
	}

	// 人民币收入/支出限额 对私
	private boolean isInditrnDebitCreditAmount(String condition, List<ModelAmlInditrn> list, BigDecimal amount) {
		if (null == list) {
			return false;
		}
		BigDecimal totalAmount = new BigDecimal(0);
		for (ModelAmlInditrn modelAmlInditrn : list) {
			if (null != modelAmlInditrn.getJyje()) {
				totalAmount = totalAmount.add(getCNYAmount(modelAmlInditrn.getBz(), modelAmlInditrn.getJyje()));
			}
		}
		if ("=".equals(condition)) {
			return totalAmount.compareTo(amount) == 0;
		}
		if (">".equals(condition)) {
			return totalAmount.compareTo(amount) > 0;
		}
		if ("<".equals(condition)) {
			return totalAmount.compareTo(amount) < 0;
		}
		if (">=".equals(condition)) {
			return totalAmount.compareTo(amount) >= 0;
		}
		if ("<=".equals(condition)) {
			return totalAmount.compareTo(amount) <= 0;
		}
		return false;
	}

	// 美元收入/支出限额 对公
	private boolean isDollarDebitCreditAmount(String condition, List<ModelAmlCorptrn> list, BigDecimal amount) {
		if (null == list) {
			return false;
		}
		BigDecimal totalAmount = new BigDecimal(0);
		for (ModelAmlCorptrn modelAmlCorptrn : list) {
			if (null != modelAmlCorptrn.getJyje()) {
				totalAmount = totalAmount.add(getUSDAmount(modelAmlCorptrn.getBz(), modelAmlCorptrn.getJyje()));
			}
		}
		if ("=".equals(condition)) {
			return totalAmount.compareTo(amount) == 0;
		}
		if (">".equals(condition)) {
			return totalAmount.compareTo(amount) > 0;
		}
		if ("<".equals(condition)) {
			return totalAmount.compareTo(amount) < 0;
		}
		if (">=".equals(condition)) {
			return totalAmount.compareTo(amount) >= 0;
		}
		if ("<=".equals(condition)) {
			return totalAmount.compareTo(amount) <= 0;
		}
		return false;
	}

	// 美元收入/支出限额 对私
	private boolean isInditrnDollarDebitCreditAmount(String condition, List<ModelAmlInditrn> list, BigDecimal amount) {
		if (null == list) {
			return false;
		}
		BigDecimal totalAmount = new BigDecimal(0);
		for (ModelAmlInditrn modelAmlInditrn : list) {
			if (null != modelAmlInditrn.getJyje()) {
				totalAmount = totalAmount.add(getUSDAmount(modelAmlInditrn.getBz(), modelAmlInditrn.getJyje()));
			}
		}
		if ("=".equals(condition)) {
			return totalAmount.compareTo(amount) == 0;
		}
		if (">".equals(condition)) {
			return totalAmount.compareTo(amount) > 0;
		}
		if ("<".equals(condition)) {
			return totalAmount.compareTo(amount) < 0;
		}
		if (">=".equals(condition)) {
			return totalAmount.compareTo(amount) >= 0;
		}
		if ("<=".equals(condition)) {
			return totalAmount.compareTo(amount) <= 0;
		}
		return false;
	}

	// type 1: 收入支出比 2:支出收入比 对公
	private boolean isDebitCreditPercent(String condition, List<ModelAmlCorptrn> cList, List<ModelAmlCorptrn> dList,
			Float percent, Byte type) {
		if (null == dList) {
			dList = new ArrayList<ModelAmlCorptrn>();
		}
		if (null == cList) {
			cList = new ArrayList<ModelAmlCorptrn>();
		}
		BigDecimal dTotalAmount = new BigDecimal(0);
		BigDecimal cTotalAmount = new BigDecimal(0);
		for (ModelAmlCorptrn modelAmlCorptrn : dList) {
			if (null != modelAmlCorptrn.getJyje()) {
				dTotalAmount = dTotalAmount.add(getUSDAmount(modelAmlCorptrn.getBz(), modelAmlCorptrn.getJyje()));
			}
		}
		for (ModelAmlCorptrn modelAmlCorptrn : cList) {
			if (null != modelAmlCorptrn.getJyje()) {
				cTotalAmount = cTotalAmount.add(getUSDAmount(modelAmlCorptrn.getBz(), modelAmlCorptrn.getJyje()));
			}
		}
		Float tempPer = 0F;
		if (type == 2) {
			if (cTotalAmount.compareTo(new BigDecimal(0)) == 0) {
				return false;
			}
			tempPer = dTotalAmount.divide(cTotalAmount, 2, RoundingMode.HALF_UP).floatValue();
		}
		if (type == 1) {
			if (dTotalAmount.compareTo(new BigDecimal(0)) == 0) {
				return false;
			}
			tempPer = cTotalAmount.divide(dTotalAmount, 2, RoundingMode.HALF_UP).floatValue();
		}
		if ("=".equals(condition)) {
			return tempPer.compareTo(percent) == 0;
		}
		if (">".equals(condition)) {
			return tempPer.compareTo(percent) > 0;
		}
		if ("<".equals(condition)) {
			return tempPer.compareTo(percent) < 0;
		}
		if (">=".equals(condition)) {
			return tempPer.compareTo(percent) >= 0;
		}
		if ("<=".equals(condition)) {
			return tempPer.compareTo(percent) <= 0;
		}
		return false;
	}

	// type 1: 收入支出比 2:支出收入比 对私
	private boolean isInditrnDebitCreditPercent(String condition, List<ModelAmlInditrn> cList,
			List<ModelAmlInditrn> dList, Float percent, Byte type) {
		if (null == dList) {
			dList = new ArrayList<ModelAmlInditrn>();
		}
		if (null == cList) {
			cList = new ArrayList<ModelAmlInditrn>();
		}
		BigDecimal dTotalAmount = new BigDecimal(0);
		BigDecimal cTotalAmount = new BigDecimal(0);
		for (ModelAmlInditrn modelAmlInditrn : dList) {
			if (null != modelAmlInditrn.getJyje()) {
				dTotalAmount = dTotalAmount.add(getUSDAmount(modelAmlInditrn.getBz(), modelAmlInditrn.getJyje()));
			}
		}
		for (ModelAmlInditrn modelAmlInditrn : cList) {
			if (null != modelAmlInditrn.getJyje()) {
				cTotalAmount = cTotalAmount.add(getUSDAmount(modelAmlInditrn.getBz(), modelAmlInditrn.getJyje()));
			}
		}
		Float tempPer = 0F;
		if (type == 2) {
			if (cTotalAmount.compareTo(new BigDecimal(0)) == 0) {
				return false;
			}
			tempPer = dTotalAmount.divide(cTotalAmount, 2, RoundingMode.HALF_UP).floatValue();
		}
		if (type == 1) {
			if (dTotalAmount.compareTo(new BigDecimal(0)) == 0) {
				return false;
			}
			tempPer = cTotalAmount.divide(dTotalAmount, 2, RoundingMode.HALF_UP).floatValue();
		}
		if ("=".equals(condition)) {
			return tempPer.compareTo(percent) == 0;
		}
		if (">".equals(condition)) {
			return tempPer.compareTo(percent) > 0;
		}
		if ("<".equals(condition)) {
			return tempPer.compareTo(percent) < 0;
		}
		if (">=".equals(condition)) {
			return tempPer.compareTo(percent) >= 0;
		}
		if ("<=".equals(condition)) {
			return tempPer.compareTo(percent) <= 0;
		}
		return false;
	}

	// 连续天数 对公
	private boolean isContinuousDays(List<ModelAmlCorptrn> dList, List<ModelAmlCorptrn> cList, Integer daysCount,
			Date endDate) throws ParseException {
		if (null == dList) {
			dList = new ArrayList<ModelAmlCorptrn>();
		}
		if (null == cList) {
			cList = new ArrayList<ModelAmlCorptrn>();
		}
		String dateString = dft.format(endDate);
		for (int i = 0; i < daysCount; i++) {
			Calendar date = Calendar.getInstance();
			date.setTime(endDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - i);
			boolean has = false;
			for (ModelAmlCorptrn modelAmlCorptrn : dList) {
				if (dft.format(modelAmlCorptrn.getJyrq()).equals(dateString)) {
					has = true;
					break;
				}
			}
			if (!has) {
				for (ModelAmlCorptrn modelAmlCorptrn : cList) {
					if (dft.format(modelAmlCorptrn.getJyrq()).equals(dateString)) {
						has = true;
						break;
					}
				}
			}
			if (!has) {
				return false;
			}
		}
		return true;
	}

	// 连续天数 对私
	private boolean isInditrnContinuousDays(List<ModelAmlInditrn> dList, List<ModelAmlInditrn> cList, Integer daysCount,
			Date endDate) throws ParseException {
		if (null == dList) {
			dList = new ArrayList<ModelAmlInditrn>();
		}
		if (null == cList) {
			cList = new ArrayList<ModelAmlInditrn>();
		}
		String dateString = dft.format(endDate);
		for (int i = 0; i < daysCount; i++) {
			Calendar date = Calendar.getInstance();
			date.setTime(endDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - i);
			boolean has = false;
			for (ModelAmlInditrn modelAmlInditrn : dList) {
				if (dft.format(modelAmlInditrn.getJyrq()).equals(dateString)) {
					has = true;
					break;
				}
			}
			if (!has) {
				for (ModelAmlInditrn modelAmlInditrn : cList) {
					if (dft.format(modelAmlInditrn.getJyrq()).equals(dateString)) {
						has = true;
						break;
					}
				}
			}
			if (!has) {
				return false;
			}
		}
		return true;
	}

	// 是否有重复交易对手 对公
	private boolean isSameCounterparties(List<ModelAmlCorptrn> cList, List<ModelAmlCorptrn> dList) {
		List<String> list = new ArrayList<String>();
		if (null != cList) {
			for (ModelAmlCorptrn modelAmlCorptrn : cList) {
				if (StringUtils.isNotBlank(modelAmlCorptrn.getJydszh())) {
					if (list.contains(modelAmlCorptrn.getJydszh())) {
						return true;
					}
					list.add(modelAmlCorptrn.getJydszh());
				}
			}
		}
		if (null != dList) {
			for (ModelAmlCorptrn modelAmlCorptrn : dList) {
				if (StringUtils.isNotBlank(modelAmlCorptrn.getJydszh())) {
					if (list.contains(modelAmlCorptrn.getJydszh())) {
						return true;
					}
					list.add(modelAmlCorptrn.getJydszh());
				}
			}
		}
		return false;
	}

	// 是否有重复交易对手 对私
	private boolean isInditrnSameCounterparties(List<ModelAmlInditrn> cList, List<ModelAmlInditrn> dList) {
		List<String> list = new ArrayList<String>();
		if (null != cList) {
			for (ModelAmlInditrn modelAmlInditrn : cList) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getJydszh())) {
					if (list.contains(modelAmlInditrn.getJydszh())) {
						return true;
					}
					list.add(modelAmlInditrn.getJydszh());
				}
			}
		}
		if (null != dList) {
			for (ModelAmlInditrn modelAmlInditrn : dList) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getJydszh())) {
					if (list.contains(modelAmlInditrn.getJydszh())) {
						return true;
					}
					list.add(modelAmlInditrn.getJydszh());
				}
			}
		}
		return false;
	}

	// 总交易笔数 对公
	private boolean isTotalCount(String condition, List<ModelAmlCorptrn> cList, List<ModelAmlCorptrn> dist,
			Integer count) {
		Integer totalCount = cList.size() + dist.size();
		if ("=".equals(condition)) {
			return totalCount == count;
		}
		if (">".equals(condition)) {
			return totalCount > count;
		}
		if ("<".equals(condition)) {
			return totalCount < count;
		}
		if (">=".equals(condition)) {
			return totalCount >= count;
		}
		if ("<=".equals(condition)) {
			return totalCount <= count;
		}
		return false;
	}

	// 总交易笔数 对私
	private boolean isInditrnTotalCount(String condition, List<ModelAmlInditrn> cList, List<ModelAmlInditrn> dist,
			Integer count) {
		Integer totalCount = cList.size() + dist.size();
		if ("=".equals(condition)) {
			return totalCount == count;
		}
		if (">".equals(condition)) {
			return totalCount > count;
		}
		if ("<".equals(condition)) {
			return totalCount < count;
		}
		if (">=".equals(condition)) {
			return totalCount >= count;
		}
		if ("<=".equals(condition)) {
			return totalCount <= count;
		}
		return false;
	}

	// 间隔天数 对公
	private boolean isIntervalDays(String condition, Date startTime, Date endTime, String hzzj, Integer count) {
		Date minDate = modelAmlCorptrnRepo.findMinDate(startTime, endTime, hzzj);
		Date maxDate = modelAmlCorptrnRepo.findMaxDate(startTime, hzzj);

		if (null != minDate && null != maxDate) {
			Double dataCount = Math.ceil((maxDate.getTime() - minDate.getTime()) / (24 * 60 * 60 * 1000));
			if ("=".equals(condition)) {
				return dataCount.intValue() == count;
			}
			if (">".equals(condition)) {
				return dataCount.intValue() > count;
			}
			if ("<".equals(condition)) {
				return dataCount.intValue() < count;
			}
			if (">=".equals(condition)) {
				return dataCount.intValue() >= count;
			}
			if ("<=".equals(condition)) {
				return dataCount.intValue() <= count;
			}
		}
		return true;
	}

	// 间隔天数 对私
	private boolean isInditrnIntervalDays(String condition, Date startTime, Date endTime, String hzzj, Integer count) {
		Date minDate = modelAmlInditrnRepo.findMinDate(startTime, endTime, hzzj);
		Date maxDate = modelAmlInditrnRepo.findMaxDate(startTime, hzzj);

		if (null != minDate && null != maxDate) {
			Double dataCount = Math.ceil((maxDate.getTime() - minDate.getTime()) / (24 * 60 * 60 * 1000));
			if ("=".equals(condition)) {
				return dataCount.intValue() == count;
			}
			if (">".equals(condition)) {
				return dataCount.intValue() > count;
			}
			if ("<".equals(condition)) {
				return dataCount.intValue() < count;
			}
			if (">=".equals(condition)) {
				return dataCount.intValue() >= count;
			}
			if ("<=".equals(condition)) {
				return dataCount.intValue() <= count;
			}
		}
		return true;
	}

	// 黑名单匹配-对私
	private boolean isInBlackInditrn(String blackPros, String hmdlx, String mdly, ModelAmlInditrn modelAmlInditrn) {
		if (StringUtils.isBlank(blackPros)) {
			return true;
		}
		List<String> blackProArray = Arrays.asList(blackPros.split(","));
		if (null == blackProArray || blackProArray.size() == 0 || null == modelAmlInditrn) {
			return true;
		}
		if (null == blackWhiteList || blackWhiteList.size() == 0) {
			blackWhiteList = blackWhiteListRepo.findAll();
		}
		List<BlackWhiteList> temp = new ArrayList<BlackWhiteList>();
		if (StringUtils.isBlank(hmdlx) && StringUtils.isBlank(mdly)) {
			temp = blackWhiteList;
		} else if (StringUtils.isBlank(hmdlx)) {
			for (BlackWhiteList blackWhite : blackWhiteList) {
				if (hmdlx.equals(blackWhite.getHmdlx())) {
					temp.add(blackWhite);
				}
			}
		} else if (StringUtils.isBlank(mdly)) {
			List<String> mdlyList = Arrays.asList(mdly.split(","));
			for(String mdlyStr:mdlyList){
				for (BlackWhiteList blackWhite : blackWhiteList) {
					if (StringUtils.isNotBlank(mdlyStr)&&mdlyStr.equals(blackWhite.getMdly())) {
						temp.add(blackWhite);
					}
				}
			}
		} else {
			for (BlackWhiteList blackWhite : blackWhiteList) {
				List<String> mdlyList = Arrays.asList(mdly.split(","));
				for(String mdlyStr:mdlyList) {
					if (StringUtils.isNotBlank(mdlyStr)&&hmdlx.equals(blackWhite.getHmdlx()) && mdlyStr.equals(blackWhite.getMdly())) {
						temp.add(blackWhite);
					}
				}
			}
		}

		for (BlackWhiteList blackWhite : temp) {
			// 名称
			if (blackProArray.contains("mc")) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getKhzwmc())
						&& modelAmlInditrn.getKhzwmc().equals(blackWhite.getZwmc())) {
					return true;
				}
				if (StringUtils.isNotBlank(modelAmlInditrn.getKhywmc())
						&& modelAmlInditrn.getKhywmc().equals(blackWhite.getYwmc())) {
					return true;
				}
			}
			// 证件号码
			if (blackProArray.contains("zjhm")) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getZjhm())
						&& modelAmlInditrn.getZjhm().equals(blackWhite.getZjhm())) {
					return true;
				}
			}
			// 国籍
			if (blackProArray.contains("gj")) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getKhgj())
						&& modelAmlInditrn.getKhgj().equals(blackWhite.getGj())) {
					return true;
				}
			}
			// 联系方式
			if (blackProArray.contains("lxfs")) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getLxdh())
						&& modelAmlInditrn.getLxfs().equals(blackWhite.getDh())) {
					return true;
				}
				if (StringUtils.isNotBlank(modelAmlInditrn.getLxdh())
						&& modelAmlInditrn.getLxfs().equals(blackWhite.getSjh())) {
					return true;
				}
			}
			// 地址
			if (blackProArray.contains("dz")) {
				if (StringUtils.isNotBlank(modelAmlInditrn.getJzdz())
						&& modelAmlInditrn.getJzdz().equals(blackWhite.getDz())) {
					return true;
				}
			}
		}

		return false;
	}

	// 黑名单匹配-对公
	private boolean isInBlack(String blackPros, String hmdlx, String mdly, ModelAmlCorptrn codelAmlCorptrn) {
		if (StringUtils.isBlank(blackPros)) {
			return true;
		}
		List<String> blackProArray = Arrays.asList(blackPros.split(","));
		if (null == blackProArray || blackProArray.size() == 0 || null == codelAmlCorptrn) {
			return true;
		}
		if (null == blackWhiteList || blackWhiteList.size() == 0) {
			blackWhiteList = blackWhiteListRepo.findAll();
		}
		List<BlackWhiteList> temp = new ArrayList<BlackWhiteList>();
		if (StringUtils.isBlank(hmdlx) && StringUtils.isBlank(mdly)) {
			temp = blackWhiteList;
		} else if (StringUtils.isBlank(hmdlx)) {
			for (BlackWhiteList blackWhite : blackWhiteList) {
				if (hmdlx.equals(blackWhite.getHmdlx())) {
					temp.add(blackWhite);
				}
			}
		} else if (StringUtils.isBlank(mdly)) {
			List<String> mdlyList = Arrays.asList(mdly.split(","));
			for(String mdlyStr:mdlyList){
				for (BlackWhiteList blackWhite : blackWhiteList) {
					if (StringUtils.isNotBlank(mdlyStr)&&mdlyStr.equals(blackWhite.getMdly())) {
						temp.add(blackWhite);
					}
				}
			}
		} else {
			for (BlackWhiteList blackWhite : blackWhiteList) {
				List<String> mdlyList = Arrays.asList(mdly.split(","));
				for(String mdlyStr:mdlyList) {
					if (StringUtils.isNotBlank(mdlyStr)&&hmdlx.equals(blackWhite.getHmdlx()) && mdlyStr.equals(blackWhite.getMdly())) {
						temp.add(blackWhite);
					}
				}
			}
		}

		for (BlackWhiteList blackWhite : temp) {
			// 名称
			if (blackProArray.contains("mc")) {
				if (StringUtils.isNotBlank(codelAmlCorptrn.getKhzwmc())
						&& codelAmlCorptrn.getKhzwmc().equals(blackWhite.getZwmc())) {
					return true;
				}
				if (StringUtils.isNotBlank(codelAmlCorptrn.getKhywmc())
						&& codelAmlCorptrn.getKhywmc().equals(blackWhite.getYwmc())) {
					return true;
				}
			}
			// 证件号码
			if (blackProArray.contains("zjhm")) {
				if (StringUtils.isNotBlank(codelAmlCorptrn.getZjhm())
						&& codelAmlCorptrn.getZjhm().equals(blackWhite.getZjhm())) {
					return true;
				}
			}
			// 国籍
			if (blackProArray.contains("gj")) {
				if (StringUtils.isNotBlank(codelAmlCorptrn.getZbszdgj())
						&& codelAmlCorptrn.getZbszdgj().equals(blackWhite.getGj())) {
					return true;
				}
			}
			// 联系方式
			if (blackProArray.contains("lxfs")) {
				if (StringUtils.isNotBlank(codelAmlCorptrn.getLxdh())
						&& codelAmlCorptrn.getLxfs().equals(blackWhite.getDh())) {
					return true;
				}
				if (StringUtils.isNotBlank(codelAmlCorptrn.getLxdh())
						&& codelAmlCorptrn.getLxfs().equals(blackWhite.getSjh())) {
					return true;
				}
			}
			// 地址
			if (blackProArray.contains("dz")) {
				if (StringUtils.isNotBlank(codelAmlCorptrn.getJzdz())
						&& codelAmlCorptrn.getJzdz().equals(blackWhite.getDz())) {
					return true;
				}
			}
		}

		return false;
	}

	private List<BigAmount> getBigAmountList(Map<AmlRuleEngine, List<ModelAmlCorptrn>> bigAmountMap, Date endDate)
			throws Exception {
		List<BigAmount> list = new ArrayList<BigAmount>();
		for (Map.Entry<AmlRuleEngine, List<ModelAmlCorptrn>> entry : bigAmountMap.entrySet()) {
			for (ModelAmlCorptrn modelAmlCorptrn : entry.getValue()) {
				if (StringUtils.isBlank(modelAmlCorptrn.getHzzj())) {
					continue;
				}

				BigAmount bigAmount = getBigAmount(modelAmlCorptrn, endDate);
				bigAmount.setCrcd(entry.getKey().getRuleNo());
				list.add(bigAmount);
			}
		}
		return list;
	}

	private List<BigAmount> getInditrnBigAmountList(Map<AmlRuleEngine, List<ModelAmlInditrn>> bigAmountMap,
			Date endDate) throws Exception {
		List<BigAmount> list = new ArrayList<BigAmount>();
		for (Map.Entry<AmlRuleEngine, List<ModelAmlInditrn>> entry : bigAmountMap.entrySet()) {
			for (ModelAmlInditrn modelAmlInditrn : entry.getValue()) {
				if (StringUtils.isBlank(modelAmlInditrn.getHzzj())) {
					continue;
				}

				BigAmount bigAmount = getInditrnBigAmount(modelAmlInditrn, endDate);
				bigAmount.setCrcd(entry.getKey().getRuleNo());
				list.add(bigAmount);
			}
		}
		return list;
	}

	private List<AmlSuspicious> getAmlSuspiciousList(Map<AmlRuleEngine, List<ModelAmlCorptrn>> suspiciousMap,
			Date endDate) throws Exception {
		List<AmlSuspicious> list = new ArrayList<>();
		// 可疑特征号
		Map<ModelAmlCorptrn, String> map = new HashMap<ModelAmlCorptrn, String>();
		Set<ModelAmlCorptrn> allModelAmlCorptrnSet = new HashSet<ModelAmlCorptrn>();
		Map<ModelAmlCorptrn,Set<AmlRuleEngine>> mrMap = new HashMap<ModelAmlCorptrn,Set<AmlRuleEngine>>();
		Map<ModelAmlCorptrn,Set<String>> szlxMap = new HashMap<ModelAmlCorptrn, Set<String>>();

		for (Map.Entry<AmlRuleEngine, List<ModelAmlCorptrn>> entry : suspiciousMap.entrySet()) {
			for (ModelAmlCorptrn modelAmlCorptrn : entry.getValue()) {
				allModelAmlCorptrnSet.add(modelAmlCorptrn);
				if (StringUtils.isBlank(map.get(modelAmlCorptrn))) {
					map.put(modelAmlCorptrn, entry.getKey().getRuleNo());
				} else {
					map.replace(modelAmlCorptrn, map.get(modelAmlCorptrn) + "," + entry.getKey().getRuleNo());
				}
				if(null == mrMap.get(modelAmlCorptrn)){
					Set<AmlRuleEngine> ruleEngineTempList = new HashSet<AmlRuleEngine>();
					ruleEngineTempList.add(entry.getKey());
					mrMap.put(modelAmlCorptrn, ruleEngineTempList);
				}else{
					mrMap.get(modelAmlCorptrn).add(entry.getKey());
				}
			}
		}

		//满足分值的
		Set<ModelAmlCorptrn> modelAmlCorptrnListMzFz = new HashSet<ModelAmlCorptrn>();

		for(TranFeatures tranFeatures : tranFeaturesList){
			List<AmlRuleEngineScore> socreList = amlRuleEngineScoreListMap.get(tranFeatures.getId());
			Map<Long,AmlRuleEngineScore> socreTempMap = new HashMap<Long,AmlRuleEngineScore>();
			for(AmlRuleEngineScore score:socreList){
				socreTempMap.put(score.getRuleEngineId(),score);
			}
			for(Map.Entry<ModelAmlCorptrn, Set<AmlRuleEngine>> entry: mrMap.entrySet()){
				if(!isInBlack(tranFeatures.getBlackProps(),tranFeatures.getHmdlx(),tranFeatures.getMdly(),entry.getKey())){
					continue;
				}

				Integer tempScore = 0;
				for(AmlRuleEngine amlRuleEngine:entry.getValue()){
					if(null != socreTempMap.get(amlRuleEngine.getId())){
						tempScore += socreTempMap.get(amlRuleEngine.getId()).getScore();
					}
				}
				if(tempScore >= tranFeatures.getFzfs()){
					modelAmlCorptrnListMzFz.add(entry.getKey());
					if(null == szlxMap.get(entry.getKey())){
						szlxMap.put(entry.getKey(),new HashSet<String>());
					}
					Set<String> szlxSet =szlxMap.get(entry.getKey());
					if(StringUtils.isNotBlank(tranFeatures.getSzlxdm())){
						szlxSet.addAll(Arrays.asList(tranFeatures.getSzlxdm().split(",")));
					}
				}
			}
		}

		for (ModelAmlCorptrn modelAmlCorptrn : modelAmlCorptrnListMzFz) {
			AmlSuspicious AmlSuspicious = getAmlSuspicious(modelAmlCorptrn, endDate);
			AmlSuspicious.setStcr(map.get(modelAmlCorptrn));
			if(null != szlxMap.get(modelAmlCorptrn)){
				AmlSuspicious.setTosc(StringUtils.join(szlxMap.get(modelAmlCorptrn),","));
			}
			list.add(AmlSuspicious);
		}
		return list;
	}

	private List<AmlSuspicious> getInditrnAmlSuspiciousList(Map<AmlRuleEngine, List<ModelAmlInditrn>> suspiciousMap,
			Date endDate) throws Exception {
		List<AmlSuspicious> list = new ArrayList<>();
		// 可疑特征号
		Map<ModelAmlInditrn, String> map = new HashMap<ModelAmlInditrn, String>();
		Set<ModelAmlInditrn> allModelAmlInditrnSet = new HashSet<ModelAmlInditrn>();
		Map<ModelAmlInditrn,Set<AmlRuleEngine>> mrMap = new HashMap<ModelAmlInditrn,Set<AmlRuleEngine>>();
		for (Map.Entry<AmlRuleEngine, List<ModelAmlInditrn>> entry : suspiciousMap.entrySet()) {
			for (ModelAmlInditrn modelAmlInditrn : entry.getValue()) {
				allModelAmlInditrnSet.add(modelAmlInditrn);
				if (StringUtils.isBlank(map.get(modelAmlInditrn))) {
					map.put(modelAmlInditrn, entry.getKey().getRuleNo());
				} else {
					map.replace(modelAmlInditrn, map.get(modelAmlInditrn) + "," + entry.getKey().getRuleNo());
				}
				if(null == mrMap.get(modelAmlInditrn)){
					Set<AmlRuleEngine> ruleEngineTempList = new HashSet<AmlRuleEngine>();
					ruleEngineTempList.add(entry.getKey());
					mrMap.put(modelAmlInditrn, ruleEngineTempList);
				}else{
					mrMap.get(modelAmlInditrn).add(entry.getKey());
				}
			}
		}

		//满足分值的
		Set<ModelAmlInditrn> modelAmlInditrnSetMzFz = new HashSet<ModelAmlInditrn>();

		for(TranFeatures tranFeatures : tranFeaturesList){
			List<AmlRuleEngineScore> socreList = amlRuleEngineScoreListMap.get(tranFeatures.getId());
			Map<Long,AmlRuleEngineScore> socreTempMap = new HashMap<Long,AmlRuleEngineScore>();
			for(AmlRuleEngineScore score:socreList){
				socreTempMap.put(score.getRuleEngineId(),score);
			}
			for(Map.Entry<ModelAmlInditrn, Set<AmlRuleEngine>> entry: mrMap.entrySet()){
				if(!isInBlackInditrn(tranFeatures.getBlackProps(),tranFeatures.getHmdlx(),tranFeatures.getMdly(),entry.getKey())){
					continue;
				}
				Integer tempScore = 0;
				for(AmlRuleEngine amlRuleEngine:entry.getValue()){
					if(null != socreTempMap.get(amlRuleEngine.getId())){
						tempScore += socreTempMap.get(amlRuleEngine.getId()).getScore();
					}
				}
				if(tempScore >= tranFeatures.getFzfs()){
					modelAmlInditrnSetMzFz.add(entry.getKey());
				}
			}
		}

		for (ModelAmlInditrn modelAmlInditrn : modelAmlInditrnSetMzFz) {
			AmlSuspicious AmlSuspicious = getInditrnAmlSuspicious(modelAmlInditrn, endDate);
			AmlSuspicious.setStcr(map.get(modelAmlInditrn));
			list.add(AmlSuspicious);
		}
		return list;
	}

	private BigAmount getBigAmount(ModelAmlCorptrn modelAmlCorptrn, Date date) throws Exception {
		BigAmount amlBigAmount = extractDataService.extractBig(modelAmlCorptrn);
		amlBigAmount.setRpdt(date);
		if (null == amlBigAmount.getCrmb() && null != amlBigAmount.getCrat()
				&& amlBigAmount.getCrat().compareTo(BigDecimal.valueOf(0)) > 0) {
			amlBigAmount.setCrmb(getCNYAmount(modelAmlCorptrn.getBz(), amlBigAmount.getCrat()));
		}
		if (null == amlBigAmount.getCusd() && null != amlBigAmount.getCrat()
				&& amlBigAmount.getCrat().compareTo(BigDecimal.valueOf(0)) > 0) {
			amlBigAmount.setCusd(getUSDAmount(modelAmlCorptrn.getBz(), amlBigAmount.getCrat()));
		}
		return amlBigAmount;
	}

	private BigAmount getInditrnBigAmount(ModelAmlInditrn modelAmlInditrn, Date date) throws Exception {
		BigAmount amlBigAmount = extractDataService.extractBig(modelAmlInditrn);
		amlBigAmount.setRpdt(date);
		if (null == amlBigAmount.getCrmb() && null != amlBigAmount.getCrat()
				&& amlBigAmount.getCrat().compareTo(BigDecimal.valueOf(0)) > 0) {
			amlBigAmount.setCrmb(getCNYAmount(modelAmlInditrn.getBz(), amlBigAmount.getCrat()));
		}
		if (null == amlBigAmount.getCusd() && null != amlBigAmount.getCrat()
				&& amlBigAmount.getCrat().compareTo(BigDecimal.valueOf(0)) > 0) {
			amlBigAmount.setCusd(getUSDAmount(modelAmlInditrn.getBz(), amlBigAmount.getCrat()));
		}
		return amlBigAmount;
	}

	// 对公
	private AmlSuspicious getAmlSuspicious(ModelAmlCorptrn modelAmlCorptrn, Date date) throws Exception {
		AmlSuspicious amlSuspicious = extractDataService.extractShadiness(modelAmlCorptrn);
		amlSuspicious.setRpdt(date);
		if (StringUtils.isNotBlank(modelAmlCorptrn.getZjhm())) {
			Integer torp = torpMap.get(modelAmlCorptrn.getZjhm());
			if (null == torp) {
				torp = amlSuspiciousRepo.getMaxTorp(modelAmlCorptrn.getZjhm());
			}
			if (null == torp) {
				torp = 1;
			} else {
				torp = torp + 1;
			}
			torpMap.put(modelAmlCorptrn.getZjhm(), torp);
			amlSuspicious.setTorp(torp.toString());
		}
		return amlSuspicious;
	}

	// 对私
	private AmlSuspicious getInditrnAmlSuspicious(ModelAmlInditrn modelAmlInditrn, Date date) throws Exception {
		AmlSuspicious amlSuspicious = extractDataService.extractShadiness(modelAmlInditrn);
		amlSuspicious.setRpdt(date);
		if (StringUtils.isNotBlank(modelAmlInditrn.getZjhm())) {
			Integer torp = torpMap.get(modelAmlInditrn.getZjhm());
			if (null == torp) {
				torp = amlSuspiciousRepo.getMaxTorp(modelAmlInditrn.getZjhm());
			}
			if (null == torp) {
				torp = 1;
			} else {
				torp = torp + 1;
			}
			torpMap.put(modelAmlInditrn.getZjhm(), torp);
			amlSuspicious.setTorp(torp.toString());
		}
		return amlSuspicious;
	}

	// 转换成美元
	private BigDecimal getUSDAmount(String bz, BigDecimal jyje) {
		if (null == jyje || jyje.compareTo(BigDecimal.valueOf(0)) == 0) {
			return BigDecimal.valueOf(0);
		}
		if (StringUtils.isBlank(bz)) {
			return BigDecimal.valueOf(0);
		}
		if ("USD".equals(bz)) {
			return jyje;
		} else {
			Sdcurrpd sdcurrpd;
			if (StringUtils.isNotBlank(bz)) {
				sdcurrpd = dcurrpdMap.get(bz);
			} else {
				sdcurrpd = dcurrpdMap.get("CNY");
			}
			if (null != sdcurrpd) {
				if ("D".equals(sdcurrpd.getMethod())) {
					return jyje.divide(sdcurrpd.getRate(), 4, RoundingMode.HALF_UP);
				} else {
					return jyje.multiply(sdcurrpd.getRate());
				}
			}
		}
		return BigDecimal.valueOf(0);
	}

	// 比较人名币金额是否大于美元金额
	private Boolean compareCnyAndUsd(BigDecimal cny, BigDecimal usd) {
		Sdcurrpd sdcurrpd = dcurrpdMap.get("CNY");
		BigDecimal cnyToUsd = BigDecimal.valueOf(0);
		if (null != sdcurrpd && null != sdcurrpd.getRate() && sdcurrpd.getRate().compareTo(BigDecimal.valueOf(0)) > 0) {
			if ("D".equals(sdcurrpd.getMethod())) {
				cnyToUsd = cny.divide(sdcurrpd.getRate(), 4, RoundingMode.HALF_UP);
			} else {
				cnyToUsd = cny.multiply(sdcurrpd.getRate());
			}
		}
		return cnyToUsd.compareTo(usd) > 0;
	}

	// 转换成人民币
	private BigDecimal getCNYAmount(String bz, BigDecimal jyje) {
		if (null == jyje || jyje.compareTo(BigDecimal.valueOf(0)) == 0) {
			return BigDecimal.valueOf(0);
		}
		if (StringUtils.isBlank(bz)) {
			return BigDecimal.valueOf(0);
		}
		BigDecimal usdAmount = getUSDAmount(bz, jyje);
		if (bz.equals("CNY")) {
			return jyje;
		}
		Sdcurrpd sdcurrpd = dcurrpdMap.get("CNY");
		if (null != sdcurrpd && null != sdcurrpd.getRate() && sdcurrpd.getRate().compareTo(BigDecimal.valueOf(0)) > 0) {
			if ("M".equals(sdcurrpd.getMethod())) {
				return usdAmount.divide(sdcurrpd.getRate(), 4, RoundingMode.HALF_UP);
			} else {
				return usdAmount.multiply(sdcurrpd.getRate());
			}
		}
		return BigDecimal.valueOf(0);
	}

	// 对公
	private boolean isCNY(List<ModelAmlCorptrn> list) {
		for (ModelAmlCorptrn modelAmlCorptrn : list) {
			if (StringUtils.isNotBlank(modelAmlCorptrn.getBz())) {
				if (!"CNY".equals(modelAmlCorptrn.getBz())) {
					return false;
				}
			}
		}
		return true;
	}

	// 对公
	private boolean hasCNY(List<ModelAmlCorptrn> list) {
		for (ModelAmlCorptrn modelAmlCorptrn : list) {
			if (StringUtils.isNotBlank(modelAmlCorptrn.getBz())) {
				if ("CNY".equals(modelAmlCorptrn.getBz())) {
					return true;
				}
			}
		}
		return false;
	}

	// 对私
	private boolean isInditrnCNY(List<ModelAmlInditrn> list) {
		for (ModelAmlInditrn modelAmlInditrn : list) {
			if (StringUtils.isNotBlank(modelAmlInditrn.getBz())) {
				if (!"CNY".equals(modelAmlInditrn.getBz())) {
					return false;
				}
			}
		}
		return true;
	}

	// 对公
	private boolean hasInditrnCNY(List<ModelAmlInditrn> list) {
		for (ModelAmlInditrn modelAmlInditrn : list) {
			if (StringUtils.isNotBlank(modelAmlInditrn.getBz())) {
				if ("CNY".equals(modelAmlInditrn.getBz())) {
					return true;
				}
			}
		}
		return false;
	}

	private void setSymbolMap() {
		symbolMap.put("@大于@", ">");
		symbolMap.put("@小于@", "<");
		symbolMap.put("@等于@", "=");
		symbolMap.put("@不等于@", "<>");
		symbolMap.put("@大于等于@", ">=");
		symbolMap.put("@小于等于@", "<=");
		symbolMap.put("@不包含@", "not like");
		symbolMap.put("@包含@", "like");
		symbolMap.put("@开头@", "like");
		symbolMap.put("@结尾@", "like");
		symbolMap.put("*并且*", "and");
		symbolMap.put("*或者*", "or");
	}

	private List<String> getStatusAfterYsh() {
		List<String> list = new ArrayList<String>();
		list.add("PRE_EXPORT");
		list.add("PRE_EXPORTOVER");
		list.add("PRE_STORAGE");
		return list;
	}

	// 是否包含已审核，不包涵 全部删除
	private boolean hasYsh(Date date,byte type) {
		// 是否包含已审核

		Set<Long> bigAmountIds = new HashSet<Long>();
		Set<Long> amlSuspiciousIds = new HashSet<Long>();
		if (info.getType().equals((byte) 1) || info.getType().equals((byte) 3)) {
			List<BigAmount> bigAmountList = bigAmountRepo.getByRpdtAndDeleteState(date, "1");
			for (BigAmount bigAmountIn : bigAmountList) {
				if (null != bigAmountIn.getJob() && getStatusAfterYsh().contains(bigAmountIn.getJob().getCurJobState())) {
					return true;
				}
			}
			for (BigAmount bigAmountIn : bigAmountList) {
				bigAmountIds.add(bigAmountIn.getId());
			}
		}

		if (info.getType().equals((byte) 2) || info.getType().equals((byte) 3)) {
			List<AmlSuspicious> amlSuspiciousList = amlSuspiciousRepo.getByRpdtAndDeleteState(date, "1");
			for (AmlSuspicious amlSuspicious : amlSuspiciousList) {
				if (null != amlSuspicious.getJob()
						&& getStatusAfterYsh().contains(amlSuspicious.getJob().getCurJobState())) {
					return true;
				}
			}
			for (AmlSuspicious amlSuspicious : amlSuspiciousList) {
				amlSuspiciousIds.add(amlSuspicious.getId());
			}
		}

		deleteSuspiciousOrBigAmount(amlSuspiciousIds, bigAmountIds);
		return false;
	}

	// 清空大额和可疑
	private void deleteSuspiciousOrBigAmount(Set<Long> amlSuspiciousIds, Set<Long> bigAmountIds) {
		List<Long> amlSuspiciousIdList = new ArrayList<Long>();
		amlSuspiciousIdList.addAll(amlSuspiciousIds);
		List<Long> bigAmountIdList = new ArrayList<Long>();
		bigAmountIdList.addAll(bigAmountIds);
		for (int i = 0; i < amlSuspiciousIdList.size(); i += 100) {
			if (amlSuspiciousIdList.size() - i > 100) {
				Set<Long> tempSet = new HashSet<Long>();
				tempSet.addAll(amlSuspiciousIdList.subList(i, i + 100));
				amlSuspiciousRepo.deleteAmlSuspicious(tempSet);
			} else {
				if (amlSuspiciousIdList.size() > i) {
					Set<Long> tempSet = new HashSet<Long>();
					tempSet.addAll(amlSuspiciousIdList.subList(i, amlSuspiciousIdList.size()));
					amlSuspiciousRepo.deleteAmlSuspicious(tempSet);
				}
			}
		}

		for (int i = 0; i < bigAmountIdList.size(); i += 100) {
			if (bigAmountIdList.size() - i > 100) {
				Set<Long> tempSet = new HashSet<Long>();
				tempSet.addAll(bigAmountIdList.subList(i, i + 100));
				bigAmountRepo.deleteAmlBigAmount(tempSet);
			} else {
				if (bigAmountIdList.size() > i) {
					Set<Long> tempSet = new HashSet<Long>();
					tempSet.addAll(bigAmountIdList.subList(i, bigAmountIdList.size()));
					bigAmountRepo.deleteAmlBigAmount(tempSet);
				}
			}
		}
	}

	// 获取交易特征
	private void initCrimes() {
		tranFeaturesList = tranFeaturesRepo.findByApproved(1);
		for (TranFeatures features : tranFeaturesList) {
			tranFeaturesMap.put(features.getId(), features);
			amlRuleEngineScoreListMap.put(features.getId(),amlRuleEngineScoreRepo.findByTranFeaturesId(features.getId()));
		}
	}

	private void destroyProps(){
		suspiciousPropDictList = null;

		modelPropDictList = null;

		// 黑名单
		blackWhiteList = null;

		// 汇率
		dcurrpdMap = null;

		symbolMap = new HashMap<String, String>();

		tranFeaturesMap = new HashMap<Long, TranFeatures>();

		tranFeaturesList = new ArrayList<TranFeatures>();

		//key : tranFeaturesId
		amlRuleEngineScoreListMap = new HashMap<Long, List<AmlRuleEngineScore>>();

		torpMap = new HashMap<String, Integer>();
	}
}
