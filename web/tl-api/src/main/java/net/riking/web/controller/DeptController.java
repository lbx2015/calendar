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
import net.riking.entity.PageQuery;
import net.riking.entity.model.Dept;
import net.riking.service.repo.DeptRepo;

@RestController
@RequestMapping(value = "/dept")
public class DeptController {
	
	@Autowired
	DeptRepo deptRepo;
	
	@ApiOperation(value = "得到<单个>部门信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		Dept dept = deptRepo.findOne(id);
		return new Resp(dept, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>部门信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute Dept dept){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if(StringUtils.isEmpty(dept.getDeleteState())){
			dept.setDeleteState("1");
		}
		Example<Dept> example = Example.of(dept, ExampleMatcher.matchingAll());
		Page<Dept> page = deptRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<所有>部门信息", notes = "GET")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public Resp getAll_(){
		Dept dept = new Dept();
		dept.setDeleteState("1");
		Example<Dept> example = Example.of(dept, ExampleMatcher.matchingAll());
		List<Dept> list = deptRepo.findAll(example);
		return new Resp(list, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新部门信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody Dept dept) {
		if(StringUtils.isEmpty(dept.getId())||StringUtils.isEmpty(dept.getDeleteState())){
			dept.setDeleteState("1");
		}
		Dept save = deptRepo.save(dept);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除部门信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = deptRepo.deleteByIds(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
}
