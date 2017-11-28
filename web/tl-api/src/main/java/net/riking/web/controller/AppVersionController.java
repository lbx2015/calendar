package net.riking.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppVersionRepo;
import net.riking.entity.model.AppVersion;

/**
 * app版本的维护
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/appVersion")
public class AppVersionController {

	@Autowired
	AppVersionRepo appVersionRepo;

	@ApiOperation(value = "得到单个app版本信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		AppVersion appVersion = appVersionRepo.findOne(id);
		return new Resp(appVersion, CodeDef.SUCCESS);
	}

	// TODO 暂时注释
	// @ApiOperation(value = "得到所有的app版本信息", notes = "GET")
	// @RequestMapping(value = "/getMore", method = RequestMethod.GET)
	// public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute AppVersion appVersion){
	// PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(),
	// query.getSortObj());
	// if(StringUtils.isEmpty(appVersion.getDeleteState())){
	// appVersion.setDeleteState("1");
	// }
	// Example<AppVersion> example = Example.of(appVersion, ExampleMatcher.matchingAll());
	// Page<AppVersion> page = appVersionRepo.findAll(example,pageable);
	// return new Resp(page, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "添加或者更新app版本信息", notes = "POST")
	// @RequestMapping(value = "/save", method = RequestMethod.POST)
	// public Resp save_(@RequestBody AppVersion appVersion) {
	// if (StringUtils.isEmpty(appVersion.getId()) ||
	// StringUtils.isEmpty(appVersion.getDeleteState())) {
	// appVersion.setDeleteState("1");
	// }
	// AppVersion save = appVersionRepo.save(appVersion);
	// return new Resp(save, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "批量删除app版本信息", notes = "POST")
	// @RequestMapping(value = "/delMore", method = RequestMethod.POST)
	// public Resp delMore_(@RequestBody Set<String> ids) {
	// int rs = 0;
	// if (ids.size() > 0) {
	// rs = appVersionRepo.deleteById(ids);
	// }
	// if (rs > 0) {
	// return new Resp().setCode(CodeDef.SUCCESS);
	// } else {
	// return new Resp().setCode(CodeDef.ERROR);
	// }
	// }
}
