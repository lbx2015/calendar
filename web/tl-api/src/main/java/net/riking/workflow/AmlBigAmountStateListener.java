package net.riking.workflow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.CheckResult;
import net.riking.service.BigSuspCheckServiceImpl;
import net.riking.service.repo.BaseReceiptRepo;
import net.riking.service.repo.BigAmountRepo;

@Component
public class AmlBigAmountStateListener implements StateListener {

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	BaseReceiptRepo baseReceiptRepo;
	

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		try {
			BigAmount bigAmount = context.getJobEvent().getTargetObj(BigAmount.class);
			if (bigAmount == null) {
				bigAmount = bigAmountRepo.getByJobId(context.getJobEvent().getJobId());
			} else {
				if (StringUtils.isNotEmpty(bigAmount.getTstp())) {
					String tstp = bigAmount.getTstp().replaceAll(",", "");
					bigAmount.setTstp(tstp);
				}
				if (StringUtils.isNotEmpty(bigAmount.getTrcd())) {
					String trcd = bigAmount.getTrcd().replaceAll(",", "");
					bigAmount.setTrcd(trcd);
				}
				if (StringUtils.isNotEmpty(bigAmount.getCfrc())) {
					String tdrc = bigAmount.getCfrc().replaceAll(",", "");
					bigAmount.setCfrc(tdrc);
				}
			}
			String event = context.getJobEvent().getEvent();
			if (event.equals("SUBMIT")) {
				CheckResult checkResult = bigSuspCheckService.checkBigAmount(bigAmount, false);
				if (StringUtils.isNotEmpty(checkResult.getReason())) {
					result.setData(checkResult.getReason());
					return result.FAIL();
				}
			} else if (event.equals("VERIFY_PASS")) {
				CheckResult checkResult = bigSuspCheckService.checkBigAmount(bigAmount, false);
				if (StringUtils.isNotEmpty(checkResult.getReason())) {
					result.setData(checkResult.getReason());
					return result.FAIL();
				}
				if (StringUtils.isEmpty(bigAmount.getReportType())) {
					bigAmount.setReportType("N");
				}
				bigAmount.setAptp("03");
			} else if (event.equals("CANCEL_VERIFY")) {
				bigAmount.setAptp("01");
			} else if (event.equals("VERIFY_NOT_PASS")) {
				bigAmount.setAptp("02");
			} else if (event.equals("EXPORT")) {
				baseReceiptRepo.updateByHzState(bigAmount.getId());
			} else if (event.equals("REPORT_AGAIN")) {
				bigAmount.setAptp("01");
			} else if (event.equals("CHANGE")) {
				CheckResult checkResult = bigSuspCheckService.checkBigAmount(bigAmount, false);
				if (StringUtils.isNotEmpty(checkResult.getReason())) {
					result.setData(checkResult.getReason());
					return result.FAIL();
				}
				bigAmount.setAptp("01");
			}
			if (bigAmount != null) {
//				BigAmount bigAmounts = bigAmountRepo.findOne(bigAmount.getId());
//				bigAmount.setDeleteState(bigAmounts.getDeleteState());
//				bigAmount.setCheckType(bigAmounts.getCheckType());
				//commonRepo.merge(Arrays.asList(bigAmount));
				bigAmountRepo.save(bigAmount);
			}
		} catch (Exception e) {
			result.setData("操作失败");
			return result.FAIL();
		}
		return result.SUCCESS();
	}
}

