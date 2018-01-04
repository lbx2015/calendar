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
import net.riking.dao.repo.TopicRepo;
import net.riking.entity.model.QuestionKeyWord;
import net.riking.service.QuestionKeyWordService;

@RestController
@RequestMapping(value = "/questionKeyWord")
public class questionKeyWordController {
	@Autowired
	TopicRepo topicRepo;
	
	@Autowired
	QuestionKeyWordService questionKeyWordService;
	
	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		QuestionKeyWord keyWord = questionKeyWordService.getRepo().findOne(id);
		return new Resp(keyWord, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute QuestionKeyWord keyWord) {
		query.setSort("id_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<QuestionKeyWord> example = Example.of(keyWord, ExampleMatcher.matchingAll());
		Page<QuestionKeyWord> page = questionKeyWordService.getRepo().findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody QuestionKeyWord keyWord) {
		questionKeyWordService.addKeyWord(keyWord);
		return new Resp(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "删除信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody List<Long> ids) {
		questionKeyWordService.delKeyWord(ids);
		return new Resp(CodeDef.SUCCESS);
	}
	
}
