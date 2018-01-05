package net.riking.web.app;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.entity.AppResp;
import net.riking.entity.model.SysNoticeRead;
import net.riking.entity.model.SysNoticeResult;
import net.riking.entity.params.SysNoticeParams;
import net.riking.service.SysNoticeService;
import net.riking.util.Utils;

/**
 * 消息通知接口
 * @author james.you
 * @version crateTime：2018年1月3日 下午7:57:55
 * @used TODO
 */
@RestController
@RequestMapping(value = "/notice")
public class SysNoticeServer {
	private static final Logger logger = LogManager.getLogger("SysNoticeServer");

	@Autowired
	SysNoticeService sysNoticeService;

	

	@ApiOperation(value = "获取系统消息通知", notes = "POST")
	@RequestMapping(value = "/getMoreSysNotice", method = RequestMethod.POST)
	public AppResp _getMoreNotice(@RequestBody Map<String, Object> params) {
		SysNoticeParams sysNoticeParams = Utils.map2Obj(params, SysNoticeParams.class);
		if(StringUtils.isBlank(sysNoticeParams.getUserId()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "userId " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		
		List<SysNoticeResult> list = sysNoticeService.findSysNoticeResult(sysNoticeParams.getUserId());
		return new AppResp(list, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "获取用户消息通知", notes = "POST")
	@RequestMapping(value = "/getMoreUserNotice", method = RequestMethod.POST)
	public AppResp _getMoreUserNotice(@RequestBody Map<String, Object> params) {
		SysNoticeParams sysNoticeParams = Utils.map2Obj(params, SysNoticeParams.class);
		if(null==sysNoticeParams || StringUtils.isBlank(sysNoticeParams.getUserId()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "userId " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		
		List<SysNoticeResult> list = sysNoticeService.findUserNoticeResult(sysNoticeParams.getUserId(), sysNoticeParams.getReqTimeStamp());
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除消息通知", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public AppResp _delMore(@RequestBody Map<String, Object> params) {
		SysNoticeParams sysNoticeParams = Utils.map2Obj(params, SysNoticeParams.class);
		
		if(StringUtils.isBlank(sysNoticeParams.getUserId()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "userId " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		if(StringUtils.isBlank(sysNoticeParams.getNoticeIds()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "noticeIds " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		if(sysNoticeParams.getHaveSysInfo() == null)
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "haveSysInfo " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		
		String[] arr_noticeId = sysNoticeParams.getNoticeIds().split(",");
		sysNoticeService.batchDelete(sysNoticeParams.getUserId(), sysNoticeParams.getHaveSysInfo().intValue()==1?true:false, arr_noticeId);
		return new AppResp(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "已读一条消息通知", notes = "POST")
	@RequestMapping(value = "/readOne", method = RequestMethod.POST)
	public AppResp _readOne(@RequestBody Map<String, Object> params) {
		SysNoticeParams sysNoticeParams = Utils.map2Obj(params, SysNoticeParams.class);
		
		if(StringUtils.isBlank(sysNoticeParams.getUserId()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "userId " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		if(StringUtils.isBlank(sysNoticeParams.getNoticeId()))
			return new AppResp(CodeDef.EMP.PARAM_EMPTY_ERROR, "noticeId " + CodeDef.EMP.PARAM_EMPTY_ERROR_DESC);
		
		SysNoticeRead entity = new SysNoticeRead();
		entity.setUserId(sysNoticeParams.getUserId());
		entity.setNoticeId(sysNoticeParams.getNoticeId());
		entity.setIsDeleted(Const.IS_NOT_DELETE);
		sysNoticeService.readSysNotice(entity);
		return new AppResp(CodeDef.SUCCESS);
	}

}
