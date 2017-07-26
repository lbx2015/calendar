package net.riking.task.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.model.TaskJobInfo;
import net.riking.core.service.repo.TaskJobInfoRepo;
import net.riking.core.task.IJobRunner;
import net.riking.entity.DataCollectInfo;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BlackWhiteList;
import net.riking.entity.model.ModelAmlCorptrn;
import net.riking.entity.model.ModelAmlInditrn;
import net.riking.service.ExtractDataServiceImpl;
import net.riking.service.WorkflowServiceImpl;
import net.riking.service.repo.BlackWhiteListRepo;
import net.riking.service.repo.ModelAmlCorptrnRepo;
import net.riking.service.repo.ModelAmlInditrnRepo;

/**
 * Created by bing.xun on 2017/5/26. 回溯
 */
@Component
public class FlashBackJob extends IJobRunner {

	@Autowired
	ModelAmlCorptrnRepo modelAmlCorptrnRepo;

	@Autowired
	BlackWhiteListRepo blackWhiteListRepo;

	@Autowired
	private TaskJobInfoRepo taskJobInfoRepo;

	@Autowired
	private ExtractDataServiceImpl extractDataService;

	@Autowired
	private ModelAmlInditrnRepo modelAmlInditrnRepo;

	DataCollectInfo info = new DataCollectInfo();

	@Autowired
	private WorkflowServiceImpl workflowService;

	@Override
	public Short callback(TaskJobInfo taskJobInfo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		info = mapper.readValue(taskJobInfo.getData(), DataCollectInfo.class);
		Date endDate = new Date();
		// 3年内的数据
		Date startDate = new Date(endDate.getTime() - 3 * 365 * 24 * 60 * 60 * 1000L);
		Set<BlackWhiteList> blackWhiteLists = blackWhiteListRepo.getBlackWhiteLists();

		Set<String> zwmSet = new HashSet<String>();
		Set<String> ywmSet = new HashSet<String>();
		Set<String> zjhmSet = new HashSet<String>();

		for (BlackWhiteList blackWhiteList : blackWhiteLists) {
			if (StringUtils.isNotBlank(blackWhiteList.getZwmc())) {
				zwmSet.add(blackWhiteList.getZwmc());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getZwmcpy())) {
				ywmSet.add(blackWhiteList.getZwmcpy());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getYwmc())) {
				ywmSet.add(blackWhiteList.getYwmc());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getZwxpy()) && StringUtils.isNotBlank(blackWhiteList.getZwmp())) {
				ywmSet.add(blackWhiteList.getZwxpy() + blackWhiteList.getZwmp());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getZwxf()) && StringUtils.isNotBlank(blackWhiteList.getZwmf())) {
				zwmSet.add(blackWhiteList.getZwxf() + blackWhiteList.getZwmf());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getYwx()) && StringUtils.isNotBlank(blackWhiteList.getYwm())) {
				ywmSet.add(blackWhiteList.getYwx() + blackWhiteList.getYwm());
			}
			if (StringUtils.isNotBlank(blackWhiteList.getZjhm())) {
				zjhmSet.add(blackWhiteList.getZjhm());
			}
		}

		Set<String> zwms = modelAmlCorptrnRepo.getZwms(startDate, endDate);

		Set<String> ywms = modelAmlCorptrnRepo.getYwms(startDate, endDate);

		Set<String> zjhms = modelAmlCorptrnRepo.getZjhms(startDate, endDate);

		List<String> zwmsN = new ArrayList<String>();

		List<String> ywmsN = new ArrayList<String>();

		List<String> zjhmsN = new ArrayList<String>();

		for (String zwm : zwms) {
			if (zwmSet.contains(zwm)) {
				zwmsN.add(zwm);
			}
		}
		for (String ywm : ywms) {
			if (ywmSet.contains(ywm)) {
				ywmsN.add(ywm);
			}
		}
		for (String zjhm : zjhms) {
			if (zjhmSet.contains(zjhm)) {
				zjhmsN.add(zjhm);
			}
		}

		Set<ModelAmlCorptrn> set = getAll(zwmsN, ywmsN, zjhmsN);
		List<AmlSuspicious> amlSuspiciouslist = getAmlSuspiciousList(set, endDate);
		workflowService.saveAmlSuspiciousList(amlSuspiciouslist);

		zwms = modelAmlInditrnRepo.getZwms(startDate, endDate);

		ywms = modelAmlInditrnRepo.getYwms(startDate, endDate);

		zjhms = modelAmlInditrnRepo.getZjhms(startDate, endDate);

		zwmsN = new ArrayList<String>();

		ywmsN = new ArrayList<String>();

		zjhmsN = new ArrayList<String>();

		for (String zwm : zwms) {
			if (zwmSet.contains(zwm)) {
				zwmsN.add(zwm);
			}
		}
		for (String ywm : ywms) {
			if (ywmSet.contains(ywm)) {
				ywmsN.add(ywm);
			}
		}
		for (String zjhm : zjhms) {
			if (zjhmSet.contains(zjhm)) {
				zjhmsN.add(zjhm);
			}
		}
		// 对私
		Set<ModelAmlInditrn> inditrnSet = getAllInditrn(zwmsN, ywmsN, zjhmsN);
		List<AmlSuspicious> amlSuspiciouslistInditrn = getAmlSuspiciousListInditrn(inditrnSet, endDate);
		workflowService.saveAmlSuspiciousList(amlSuspiciouslistInditrn);
		blackWhiteListRepo.setIshs();

		taskJobInfo.setResult("生成" + (amlSuspiciouslist.size() + amlSuspiciouslistInditrn.size()) + "条可疑交易");
		taskJobInfo.setDateUpdate(new Date());
		taskJobInfoRepo.updateJobr(taskJobInfo.getResult(), taskJobInfo.getBatchId());
		return STATUS_ENUM.SUCCESS;
	}

	// 获取所有对公交易
	private Set<ModelAmlCorptrn> getAll(List<String> zwms, List<String> ywms, List<String> zjhms) {
		Set<ModelAmlCorptrn> set = new HashSet<ModelAmlCorptrn>();
		for (int i = 0; i < zwms.size(); i += 100) {
			if (zwms.size() - i > 100) {
				set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithZwm(zwms.subList(i, i + 100)));
			} else {
				if (zwms.size() > i) {
					set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithZwm(zwms.subList(i, zwms.size())));
				}
			}
		}
		for (int i = 0; i < ywms.size(); i += 100) {
			if (ywms.size() - i > 100) {
				set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithYwm(ywms.subList(i, i + 100)));
			} else {
				if (ywms.size() > i) {
					set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithYwm(ywms.subList(i, ywms.size())));
				}
			}
		}
		for (int i = 0; i < zjhms.size(); i += 100) {
			if (zjhms.size() - i > 100) {
				set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithZjhm(zjhms.subList(i, i + 100)));
			} else {
				if (ywms.size() > i) {
					set.addAll(modelAmlCorptrnRepo.getModelAmlCorptrnWithZjhm(zjhms.subList(i, ywms.size())));
				}
			}
		}
		return set;
	}

	// 获取所有对私交易
	private Set<ModelAmlInditrn> getAllInditrn(List<String> zwms, List<String> ywms, List<String> zjhms) {
		Set<ModelAmlInditrn> set = new HashSet<ModelAmlInditrn>();
		for (int i = 0; i < zwms.size(); i += 100) {
			if (zwms.size() - i > 100) {
				set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithZwm(zwms.subList(i, i + 100)));
			} else {
				if (zwms.size() > i) {
					set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithZwm(zwms.subList(i, zwms.size())));
				}
			}
		}
		for (int i = 0; i < ywms.size(); i += 100) {
			if (ywms.size() - i > 100) {
				set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithYwm(ywms.subList(i, i + 100)));
			} else {
				if (ywms.size() > i) {
					set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithYwm(ywms.subList(i, ywms.size())));
				}
			}
		}
		for (int i = 0; i < zjhms.size(); i += 100) {
			if (zjhms.size() - i > 100) {
				set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithZjhm(zjhms.subList(i, i + 100)));
			} else {
				if (ywms.size() > i) {
					set.addAll(modelAmlInditrnRepo.getModelAmlInditrnWithZjhm(zjhms.subList(i, ywms.size())));
				}
			}
		}
		return set;
	}

	private List<AmlSuspicious> getAmlSuspiciousList(Set<ModelAmlCorptrn> set, Date date) throws Exception {
		List<AmlSuspicious> amlSuspiciouslist = new ArrayList<AmlSuspicious>();
		for (ModelAmlCorptrn modelAmlCorptrn : set) {
			amlSuspiciouslist.add(getAmlSuspicious(modelAmlCorptrn, date));
		}
		return amlSuspiciouslist;
	}

	private List<AmlSuspicious> getAmlSuspiciousListInditrn(Set<ModelAmlInditrn> set, Date date) throws Exception {
		List<AmlSuspicious> amlSuspiciouslist = new ArrayList<AmlSuspicious>();
		for (ModelAmlInditrn modelAmlInditrn : set) {
			amlSuspiciouslist.add(getAmlSuspicious(modelAmlInditrn, date));
		}
		return amlSuspiciouslist;
	}

	private AmlSuspicious getAmlSuspicious(ModelAmlCorptrn modelAmlCorptrn, Date date) throws Exception {
		AmlSuspicious amlSuspicious = extractDataService.extractShadiness(modelAmlCorptrn);
		amlSuspicious.setRpdt(date);
		amlSuspicious.setSsds("涉恐名单回溯");
		return amlSuspicious;
	}

	private AmlSuspicious getAmlSuspicious(ModelAmlInditrn modelAmlInditrn, Date date) throws Exception {
		AmlSuspicious amlSuspicious = extractDataService.extractShadiness(modelAmlInditrn);
		amlSuspicious.setRpdt(date);
		amlSuspicious.setSsds("涉恐名单回溯");
		return amlSuspicious;
	}
}
