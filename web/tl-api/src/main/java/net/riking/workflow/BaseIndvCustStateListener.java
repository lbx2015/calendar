package net.riking.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.entity.model.BaseIndvCust;
import net.riking.service.repo.BaseIndvCustAddRepo;
import net.riking.service.repo.BaseIndvCustRepo;

@Component
public class BaseIndvCustStateListener implements StateListener {

	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	BaseIndvCustAddRepo baseIndvCustAddRepo;

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		String event = context.getJobEvent().getEvent();
		BaseIndvCust baseIndvCust = context.getJobEvent().getTargetObj(BaseIndvCust.class);
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		if (event.equals("APPROVE_PASS")) {
			baseIndvCust.setConfirmStatus("101002");
			baseIndvCust.setSyncLastTime(System.currentTimeMillis());
			if (baseIndvCust.getOldId() != null) {
				BaseIndvCust baseIndvCust2 = baseIndvCustRepo.findOne(baseIndvCust.getOldId());
				if (baseIndvCust2.getBaseIndvCustAdd()!=null) {
					baseIndvCustAddRepo.save(baseIndvCust2.getBaseIndvCustAdd());
				}
				baseIndvCust2.setEnabled("0");
				baseIndvCustRepo.save(baseIndvCust2);
			}
		}
		if (null != baseIndvCust) {
			baseIndvCustAddRepo.save(baseIndvCust.getBaseIndvCustAdd());
			BaseIndvCust baseIndvCust2 = baseIndvCustRepo.save(baseIndvCust);
			if (baseIndvCust2 == null) {
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
