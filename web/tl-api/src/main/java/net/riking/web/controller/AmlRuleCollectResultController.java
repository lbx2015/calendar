package net.riking.web.controller;

import java.text.ParseException;

import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.entity.DataCollectInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.core.task.TaskMgr;
import net.riking.task.job.RiskDataCollectJob2;
@InModLog(modName = "采集")
@RestController
@RequestMapping(value = "/amlRuleCollectResult")
public class AmlRuleCollectResultController {

	@Autowired
	TaskMgr taskMgr;
	@Autowired
	RiskDataCollectJob2 riskDataCollectJob;
	@InFunLog(funName = "添加", args = { 0 })
	@RequestMapping(value = "/collect", method = RequestMethod.GET)
	public Resp save(@RequestParam String start,@RequestParam Byte type)
			throws JsonProcessingException, InstantiationException, IllegalAccessException, ParseException {
		// 获取group
		// 生成taskMgr，由此获取批次号和jobinfo
		String batchId = null;
		DataCollectInfo info = new DataCollectInfo();
		info.setType(type);
		info.setBranchId(TokenHolder.get().getBranchId());
		info.setDate(start);
		info.setToken(TokenHolder.get().getToken());
		batchId = taskMgr.addJob(Const.TASK_RISK_DATA_GROUP, info, riskDataCollectJob);
		return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getLast", method = RequestMethod.GET)
	public Resp getLast(@RequestParam String batchId) {
		if (StringUtils.isNotEmpty(batchId)) {
			return new Resp(taskMgr.getJobInfo(batchId),CodeDef.SUCCESS);
		} else {
			return new Resp(taskMgr.getLastJobInfoByGroup(Const.TASK_RISK_DATA_GROUP), CodeDef.SUCCESS);
		}
	}
}
