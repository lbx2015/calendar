package net.riking.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.MultipleChoiceCustom;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserGradeRepo;
import net.riking.entity.model.AppUserGrade;

/**
 * web端横幅操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUserGrade")
public class AppUserGradeController {
	@Autowired
	AppUserGradeRepo appUserGradeRepo;

	@ApiOperation(value = "得到<单个>信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Integer id) {
		AppUserGrade appUserGrade = appUserGradeRepo.findOne(id);
		if (null != appUserGrade) {
			return new Resp(appUserGrade, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "得到<批量>信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute AppUserGrade appUserGrade) {
		appUserGrade.setIsDeleted(Const.EFFECTIVE);
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<AppUserGrade> example = Example.of(appUserGrade, ExampleMatcher.matchingAll());
		Page<AppUserGrade> page = appUserGradeRepo.findAll(example, pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody AppUserGrade appUserGrade) {
		// 修改
		appUserGradeRepo.save(appUserGrade);
		return new Resp(appUserGrade, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "启用信息", notes = "GET")
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id) {
		// AppUserGrade appUserGrade = appUserGradeRepo.findOne(id);
		// banner = bannerRepo.save(banner);
		return new Resp(null, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "禁用信息", notes = "GET")
	@RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	public Resp unEnable_(@RequestParam String id) {
		// Banner banner = bannerRepo.findOne(id);
		// banner.setEnabled("0");
		// banner = bannerRepo.save(banner);
		return new Resp(null, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核", notes = "POST")
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public Resp verifyMore(@RequestBody Map<String, String> params) {
		// Banner banner = bannerRepo.findOne(params.get("id"));
		// if (banner != null) {
		// banner.setIsAduit(params.get("isAduit"));
		// banner = bannerRepo.save(banner);
		// return new Resp(banner, CodeDef.SUCCESS);
		// } else {
		// return new Resp(null, CodeDef.ERROR);
		// }
		return new Resp(null, CodeDef.ERROR);
	}

	@ApiOperation(value = "删除信息", notes = "POST")
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public Resp delMore(@RequestBody String id) {
		id = id.replaceAll("\"", "");
		AppUserGrade appUserGrade = appUserGradeRepo.findOne(Integer.parseInt(id));
		if (appUserGrade != null) {
			appUserGrade.setIsDeleted(Const.INVALID);
			appUserGradeRepo.save(appUserGrade);
			return new Resp(appUserGrade, CodeDef.SUCCESS);
		}
		return new Resp(null, CodeDef.ERROR);
	}

	@RequestMapping(value = "/getUserGradeEnum", method = RequestMethod.GET)
	public Resp getUserGradeEnum(@RequestParam(value = "prop", required = false) String prop) throws Exception {
		List<AppUserGrade> list = appUserGradeRepo.findByIsDeleted(new Integer(1));
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (AppUserGrade appUserGrade : list) {
			choice = new MultipleChoiceCustom();
			choice.setKey(appUserGrade.getGrade().toString());
			choice.setValue("等级" + appUserGrade.getGrade());
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms, CodeDef.SUCCESS);
	}
}
