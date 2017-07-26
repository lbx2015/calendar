package net.riking.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.CheckResult;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.util.CheckUtil;

@Component
public class BaseCorpCustStateListener implements StateListener {

	@Autowired
	BaseCorpCustRepo baseCorpCustRpo;
	@Autowired
	CheckUtil check;

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		BaseCorpCust baseCorpCust = context.getJobEvent().getTargetObj(BaseCorpCust.class);
		String event = context.getJobEvent().getEvent();
		if (event.equals("RECROD_SAVE")) {
			CheckResult checkResult = check.checkCusts(baseCorpCust);
			if (checkResult != null) {
				result.setData(checkResult.getReason());
				return result.FAIL();
			}
		}
		if (event.equals("APPROVE_PASS")) {
			baseCorpCust.setConfirmStatus("101002");
			baseCorpCust.setSyncLastTime(System.currentTimeMillis());
			if (baseCorpCust.getKhlx().equals("D0101")) {
				baseCorpCust.setAccountMainType("12");
			}else if (baseCorpCust.getKhlx().equals("E0109")) {
				baseCorpCust.setAccountMainType("13");
			}else {
				baseCorpCust.setAccountMainType("11");
			}
			if (baseCorpCust.getOldId() != null) {
				BaseCorpCust baseCorpCust2 = baseCorpCustRpo.findOne(baseCorpCust.getOldId());
				baseCorpCust2.setEnabled("0");
				baseCorpCustRpo.save(baseCorpCust2);
			}
		}
		if (null != baseCorpCust) {
			BaseCorpCust baseCorpCust2 = baseCorpCustRpo.save(baseCorpCust);
			if (baseCorpCust2 == null) {
				result.setData("操作失败");
				return result;
			}
		} else {
			result.setData("操作失败");
			return result;
		}
		return result.SUCCESS();
	}
}
