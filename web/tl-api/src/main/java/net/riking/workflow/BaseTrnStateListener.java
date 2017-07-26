package net.riking.workflow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.config.Config;
import net.riking.core.entity.WorkflowContext;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.StateListener;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.model.BaseTrn;
import net.riking.service.repo.BaseTrnRepo;

@Component
public class BaseTrnStateListener implements StateListener {

	@Autowired
	BaseTrnRepo baseTrnRpo;

	@Autowired
	Config config;

	@Autowired
	WorkflowMgr workflowMgr;

	@Override
	public EventResult stateChanged(WorkflowContext context) {
		BaseTrn baseTrn = context.getJobEvent().getTargetObj(BaseTrn.class);
		String event = context.getJobEvent().getEvent();
		EventResult result = EventResult.of(context.getJobEvent().getJobId());
		if (baseTrn != null) {
			if (StringUtils.isNotEmpty(baseTrn.getJyfs())) {
				String jyfs = baseTrn.getJyfs().replaceAll(",", "");
				baseTrn.setJyfs(jyfs);
			}if (StringUtils.isNotEmpty(baseTrn.getJyfsd())) {
				String jyfsd = baseTrn.getJyfsd().replaceAll(",", "");
				baseTrn.setJyfsd(jyfsd);
			}
			if (event.equals("APPROVE_PASS")) {
				baseTrn.setConfirmStatus("101002");
				baseTrn.setSyncLastTime(System.currentTimeMillis());
				Integer rs = baseTrnRpo.delByJylshAndNotId(baseTrn.getJylsh(), baseTrn.getId());
				if (rs > 0) {
					// 将新的审核过后的变为可用
					baseTrn.setEnabled(1);
				}
			}
			/*
			 * if(event.equals("UPDATE")){ //将老的先保存 try { BaseTrn findOne =
			 * baseTrnRpo.findOne(baseTrn.getId()); JSONObject json =
			 * JSONObject.fromObject(findOne); BaseTrn oldTrn =
			 * (BaseTrn)JSONObject.toBean(json, BaseTrn.class);
			 * oldTrn.setId(null); oldTrn.setJob(null); oldTrn =
			 * baseTrnRpo.save(oldTrn); oldTrn.setStartState("APPROVED");
			 * workflowMgr.addJobs(config.getBaseInfoWorkId(),
			 * Arrays.asList(oldTrn)); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); return
			 * result.FAIL(); } //将新的设为不可用 baseTrn.setEnabled(0); }
			 */
			BaseTrn baseTrn2 = baseTrnRpo.save(baseTrn);
			if (baseTrn2 == null) {
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
