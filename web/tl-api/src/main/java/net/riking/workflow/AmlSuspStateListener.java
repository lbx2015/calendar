package net.riking.workflow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.CheckResult;
import net.riking.service.BigSuspCheckServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BaseReceiptRepo;

@Component
public class AmlSuspStateListener implements StateListener {

	@Autowired
	AmlSuspiciousRepo suspiciousRepo;

	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	BaseReceiptRepo baseReceiptRepo;
	

	@Override
	public EventResult stateChanged(WorkflowContext workflowContext) {
		EventResult result = EventResult.of(workflowContext.getJobEvent().getJobId());
		AmlSuspicious amlSuspicious = workflowContext.getJobEvent().getTargetObj(AmlSuspicious.class);
		try {
			if (amlSuspicious == null) {
				amlSuspicious = suspiciousRepo.getByJobId(workflowContext.getJobEvent().getJobId());
			} else {
				if (StringUtils.isNotEmpty(amlSuspicious.getCfrc())) {
					String getCfrc = amlSuspicious.getCfrc().replaceAll(",", "");
					amlSuspicious.setCfrc(getCfrc);
				}
				if (StringUtils.isNotEmpty(amlSuspicious.getTstp())) {
					String tstp = amlSuspicious.getTstp().replaceAll(",", "");
					amlSuspicious.setTstp(tstp);
				}
				if (StringUtils.isNotEmpty(amlSuspicious.getTrcd())) {
					String trcd = amlSuspicious.getTrcd().replaceAll(",", "");
					amlSuspicious.setTrcd(trcd);
				}
			}
			String event = workflowContext.getJobEvent().getEvent();
			// 可疑交易分析处理
			if (event.equals("STOP") || event.equals("SISP_DEAL") || event.equals("SISP_VERIFY_NOT_PASS")
					|| event.equals("SISP_VERIFY_PASS") || event.equals("RESAVE")) {

				if (!event.equals("RESAVE") && !event.equals("STOP")) {
					CheckResult checkResult = bigSuspCheckService.checkPreSusp(amlSuspicious);
					if (StringUtils.isNotEmpty(checkResult.getReason())) {
						result.setData(checkResult.getReason());
						return result.FAIL();
					}
				}
				if (event.equals("SISP_DEAL") || event.equals("SISP_VERIFY_PASS") || event.equals("RESAVE")) {
					amlSuspicious.setAptp("01");// 修改审核状态--未审核
				} else if (event.equals("SISP_VERIFY_NOT_PASS")) {
					amlSuspicious.setAptp("02");// 修改审核状态---审核不通过
				}
				this.addOrUpdate(amlSuspicious);
			} else {
				// 可疑交易报送处理
				if (event.equals("SUBMIT")) {
					CheckResult checkResult = bigSuspCheckService.checkSuspicious(amlSuspicious, false);
					if (StringUtils.isNotEmpty(checkResult.getReason())) {
						result.setData(checkResult.getReason());
						return result.FAIL();
					}
				} else if (event.equals("VERIFY_PASS")) {
					CheckResult checkResult = bigSuspCheckService.checkSuspicious(amlSuspicious, false);
					if (StringUtils.isNotEmpty(checkResult.getReason())) {
						result.setData(checkResult.getReason());
						return result.FAIL();
					}
					amlSuspicious.setAptp("03");
				} else if (event.equals("CANCEL_VERIFY")) {
					amlSuspicious.setAptp("01");
				} else if (event.equals("VERIFY_NOT_PASS")) {
					amlSuspicious.setAptp("02");
				} else if (event.equals("EXPORT")) {
					baseReceiptRepo.updateByHzState(amlSuspicious.getId());
				} else if (event.equals("REPORT_AGAIN")) {
					amlSuspicious.setAptp("01");
				} else if (event.equals("CHANGE")) {
					CheckResult checkResult = bigSuspCheckService.checkSuspicious(amlSuspicious, false);
					if (StringUtils.isNotEmpty(checkResult.getReason())) {
						result.setData(checkResult.getReason());
						return result.FAIL();
					}
					amlSuspicious.setAptp("01");
				}
				suspiciousRepo.save(amlSuspicious);
			}
		} catch (Exception e) {
			result.setData("操作失败");
			return result.FAIL();
		}
		return result.SUCCESS();
	}

	private AmlSuspicious addOrUpdate(AmlSuspicious amlSuspicious) {
		AmlSuspicious aml = suspiciousRepo.findOne(amlSuspicious.getId());
		aml.setDetr(amlSuspicious.getDetr());
		aml.setDorp(amlSuspicious.getDorp());
		aml.setOdrp(amlSuspicious.getOdrp());
		aml.setTptr(amlSuspicious.getTptr());
		aml.setOtpr(amlSuspicious.getOtpr());
		aml.setStcb(amlSuspicious.getStcb());
		aml.setAosp(amlSuspicious.getAosp());
		aml.setAptp(amlSuspicious.getAptp());
		aml = suspiciousRepo.save(aml);
		return aml;
	}
	/*
	private AmlSuspicious save(AmlSuspicious suspicious) {
		AmlSuspicious suspicious2 = suspiciousRepo.findOne(suspicious.getId());
		suspicious.setDeleteState(suspicious2.getDeleteState());
		suspicious.setCheckType(suspicious2.getCheckType());
		suspicious = suspiciousRepo.save(suspicious);
		return suspicious;
	}
*/
}
