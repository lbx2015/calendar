package net.riking.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.entity.model.BaseAif;
import net.riking.service.repo.BaseAifRepo;

@Component
public class BaseAifStateListener implements StateListener {

	@Autowired
	BaseAifRepo baseAifRpo;

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		BaseAif baseAif = context.getJobEvent().getTargetObj(BaseAif.class);
		String event = context.getJobEvent().getEvent();
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		if (event.equals("APPROVE_PASS")) {
			baseAif.setConfirmStatus("101002");
			baseAif.setSyncLastTime(System.currentTimeMillis());
			if (baseAif.getOldId() != null) {
				BaseAif baseAif2 = baseAifRpo.findOne(baseAif.getOldId());
				baseAif2.setEnabled("0");
				baseAifRpo.save(baseAif2);
			}
		}
		if (null != baseAif) {
			BaseAif bas = baseAifRpo.save(baseAif);
			if (bas == null) {
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
