package net.riking.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.WebVersionRepo;
import net.riking.entity.model.WebVersion;

/**
 * web版本的维护
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/webVersion")
public class WebVersionController {

	@Autowired
	WebVersionRepo webVersionRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@ApiOperation(value = "得到单个Web版本信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		WebVersion webVersion = webVersionRepo.findOne(id);
		return new Resp(webVersion, CodeDef.SUCCESS);
	}

	// TODO 暂时注释
	// @ApiOperation(value = "得到所有的Web版本信息", notes = "GET")
	// @RequestMapping(value = "/getMore", method = RequestMethod.GET)
	// public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute WebVersion webVersion){
	// PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(),
	// query.getSortObj());
	// if(StringUtils.isEmpty(webVersion.getDeleteState())){
	// webVersion.setDeleteState("1");
	// }
	// Example<WebVersion> example = Example.of(webVersion, ExampleMatcher.matchingAll());
	// Page<WebVersion> page = webVersionRepo.findAll(example,pageable);
	// return new Resp(page, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "添加或者更新Web版本信息", notes = "POST")
	// @RequestMapping(value = "/save", method = RequestMethod.POST)
	// public Resp save_(@RequestBody WebVersion webVersion) {
	// if (StringUtils.isEmpty(webVersion.getId()) ||
	// StringUtils.isEmpty(webVersion.getDeleteState())) {
	// webVersion.setDeleteState("1");
	// }
	// WebVersion save = webVersionRepo.save(webVersion);
	// return new Resp(save, CodeDef.SUCCESS);
	// }

	/*
	 * @ApiOperation(value = "批量删除web版本信息", notes = "POST")
	 * 
	 * @RequestMapping(value = "/delMore", method = RequestMethod.POST) public Resp
	 * delMore(@RequestBody Set<String> ids) { int rs = 0; if (ids.size() > 0) { rs =
	 * webVersionRepo.deleteById(ids); } if (rs > 0) { return new Resp().setCode(CodeDef.SUCCESS); }
	 * else { return new Resp().setCode(CodeDef.ERROR); } }
	 */
}
