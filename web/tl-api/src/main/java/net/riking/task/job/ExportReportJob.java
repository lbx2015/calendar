package net.riking.task.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.config.enums.ExportType;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.model.TaskJobInfo;
import net.riking.core.task.IJobRunner;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.BigAmountMz;
import net.riking.entity.DataExportInfo;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.service.BigCheckServiceImpl;
import net.riking.service.ExportDataServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;

@Component("exportReportJob")
public class ExportReportJob extends IJobRunner {
	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;
	@Autowired
	BigAmountRepo bigAmountRepo;
	@Autowired
	ExportDataServiceImpl exportDataServiceImpl;
	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	BigCheckServiceImpl bigCheckService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Transactional
	@Override
	public Short callback(TaskJobInfo taskJobInfo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DataExportInfo info = mapper.readValue(taskJobInfo.getData(), DataExportInfo.class);
		String bothOrOne = info.getBothOrOne();
		Long branchId = info.getBranchId();
		String token = info.getToken();
		Date date = sdf.parse(info.getDate());
		String userName = info.getUserName();
		Short flag = STATUS_ENUM.FAIL;
		if(branchId==null){
			return flag;
		}
		if (bothOrOne.equals(ExportType.Big.getName())) {// 只导出大额
			flag = this.exportBigAmounts(date, token, branchId, userName);
		} else if (bothOrOne.equals(ExportType.Susp.getName())) {// 只导出可疑
			flag = this.exportSusp(date, token, branchId, userName);
		} else if (bothOrOne.equals(ExportType.Both.getName())){// 两个都导出
			flag = this.exportBigAmounts(date, token, branchId, userName);
			if (flag.equals(STATUS_ENUM.SUCCESS)) {
				flag = this.exportSusp(date, token, branchId, userName);
			}
		}
		return flag;
	}

	private Short exportBigAmounts(Date exportDate, String token, Long branchId, String userName) {
		List<BigAmount> bigList2 = new ArrayList<BigAmount>();
		String message = null;
		HashSet<String> set = new HashSet<String>();
		set.add("PRE_RECROD");
		set.add("PRE_SUBMIT");
		set.add("PRE_VERIFY");
		Integer notPassCont = bigAmountRepo.findNotPass(exportDate, "1", "Y", set);
		if (notPassCont > 0) {
			return STATUS_ENUM.NOT_START;
		}
		List<BigAmount> bigList = this.bigAmountRepo.findAll(new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("rpdt").as(Date.class)), exportDate));
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				list.add(cb.equal((root.get("submitType").as(String.class)), "Y"));
				list.add(cb.like(root.get("flowState").as(String.class), "PRE_EXPORT"));
				// 非超级管理员
//				if (branchCodes != null && branchCodes.size() > 0) {
//					list.add(root.get("jgbm").as(String.class).in(branchCodes));
//				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
		// 判断金额是否满足
		Map<String, BigAmountMz> csnmSfmzMap = new HashMap<String, BigAmountMz>();
		for (BigAmount bigAmountIn : bigList) {
			if (!csnmSfmzMap.containsKey(bigAmountIn.getCsnm())) {
				try {
					csnmSfmzMap.put(bigAmountIn.getCsnm(), bigCheckService.getBigamountSfmz(bigAmountIn));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (csnmSfmzMap.get(bigAmountIn.getCsnm()).getSfmz().equals("01")) {
				bigList2.add(bigAmountIn);
			}
		}
		if (bigList2.size() == 0) {
			return STATUS_ENUM.FAIL;
		}
		try {
			message = exportDataServiceImpl.exportBigAmount(bigList2, token, branchId);
		} catch (Exception e) {
			e.printStackTrace();
			return STATUS_ENUM.EXCEPTION;
		}
		if (StringUtils.isNotEmpty(message)) {
			//Set<Long> ids = new HashSet<Long>();
			ArrayList<JobEvent> list2 = new ArrayList<JobEvent>();
			String event = "EXPORT";
			for (BigAmount bigAmount : bigList2) {
				JobEvent jobEvent = new JobEvent(bigAmount.getJobId(), event, userName);
				jobEvent.setFireEvent(false);
				//ids.add(bigAmount.getId());
				list2.add(jobEvent);
			}
			workflowMgr.sendEvents(list2);
		}
		return STATUS_ENUM.SUCCESS;
	}

	private Short exportSusp(Date exportDate, String token, Long branchId, String userName) {
		String message = null;
		List<AmlSuspicious> suspList = this.amlSuspiciousRepo.findAll(new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("rpdt").as(Date.class)), exportDate));
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				list.add(cb.equal((root.get("submitType").as(String.class)), "Y"));
				list.add(cb.like(root.get("flowState").as(String.class), "PRE_EXPORT"));
				// 非超级管理员
//				if (branchCodes != null && branchCodes.size() > 0) {
//					list.add(root.get("jgbm").as(String.class).in(branchCodes));
//				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
		if (suspList.size() > 0) {
			try {
				message = exportDataServiceImpl.exportSusp(suspList, token, branchId);
			} catch (Exception e) {
				e.printStackTrace();
				return STATUS_ENUM.EXCEPTION;
			}
		} else {
			return STATUS_ENUM.FAIL;
		}
		if (StringUtils.isNotEmpty(message)) {
			String event = "EXPORT";
			ArrayList<JobEvent> list3 = new ArrayList<JobEvent>();
			for (AmlSuspicious susp : suspList) {
				JobEvent jobEvent = new JobEvent(susp.getJobId(), event, userName);
				jobEvent.setFireEvent(false);
				list3.add(jobEvent);
			}
			workflowMgr.sendEvents(list3);
		} else {
			return STATUS_ENUM.FAIL;
		}
		return STATUS_ENUM.SUCCESS;
	}
}
