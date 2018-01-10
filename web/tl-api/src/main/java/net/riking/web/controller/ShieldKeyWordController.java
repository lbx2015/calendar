package net.riking.web.controller;

import java.util.List;

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
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.entity.model.ShieldKeyWord;
import net.riking.service.ShieldKeyWordService;

@RestController
@RequestMapping(value = "/shieldKeyWord")
public class ShieldKeyWordController {
	
	@Autowired
	ShieldKeyWordService shieldKeyWordService;
	
	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		ShieldKeyWord keyWord = shieldKeyWordService.getRepo().findOne(id);
		return new Resp(keyWord, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ShieldKeyWord keyWord) {
		query.setSort("id_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<ShieldKeyWord> example = Example.of(keyWord, ExampleMatcher.matchingAll());
		Page<ShieldKeyWord> page = shieldKeyWordService.getRepo().findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody ShieldKeyWord keyWord) {
		shieldKeyWordService.addKeyWord(keyWord);
		return new Resp(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "删除信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody List<Long> ids) {
		shieldKeyWordService.delKeyWord(ids);
		return new Resp(CodeDef.SUCCESS);
	}
	
}
