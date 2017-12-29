package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.WebVersionRepo;
import net.riking.entity.PageQuery;
import net.riking.entity.model.WebVersion;

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

	@ApiOperation(value = "得到所有的Web版本信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute WebVersion webVersion) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		webVersion.setIsDeleted(1);
		Example<WebVersion> example = Example.of(webVersion, ExampleMatcher.matchingAll());
		Page<WebVersion> page = webVersionRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新Web版本信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody WebVersion webVersion) {
		if (webVersion.getOpt().equals("add")) {
			// 校验是否是存在该版本
			boolean flag = checkLastVersion(webVersion);
			if (!flag) {
				return new Resp("-1", CodeDef.ERROR);
			}
			if (StringUtils.isEmpty(webVersion.getId())) {
				webVersion.setIsDeleted(1);
			}
			WebVersion save = webVersionRepo.save(webVersion);
			return new Resp(save, CodeDef.SUCCESS);
		} else {
			WebVersion version = webVersionRepo.findOneByVersionNO(webVersion.getVersionNo());
			webVersion.setId(version.getId());
			WebVersion save = webVersionRepo.save(webVersion);
			return new Resp(save, CodeDef.SUCCESS);
		}

	}

	@ApiOperation(value = "批量删除web版本信息", notes = "POST")

	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = webVersionRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	/**
	 * 校验是否是最新的web版本号
	 * @param webVersion
	 */
	private boolean checkLastVersion(WebVersion version) {
		boolean flag = true;
		// 获取前台的版本号
		String versionNO = version.getVersionNo();
		// 获取客户端页面有效的版本信息
		List<WebVersion> webVersions = webVersionRepo.findMaxVersion();
		for (WebVersion webVersion : webVersions) {
			if (webVersion.getVersionNo().equals(versionNO)) {
				flag = false;
				break;
			} else {
				flag = true;
			}
		}
		return flag;
	}

}
