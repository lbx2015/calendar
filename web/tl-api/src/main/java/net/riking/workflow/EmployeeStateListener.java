package net.riking.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.entity.model.Employee;
import net.riking.service.repo.EmployeeRepo;

@Component
public class EmployeeStateListener implements StateListener {

	@Autowired
	EmployeeRepo employeeRepo;

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		logger.info("------------StateListener:" + context.getJobEvent().getJobId());
		Employee em = context.getJobEvent().getTargetObj(Employee.class);
		if (em != null) {
			logger.info("当前对象信息:{}", em.toString());
		}
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		// result.setData(obj);
		return result.SUCCESS();
	}

}
