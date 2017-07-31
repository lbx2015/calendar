package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.core.service.repo.ModelPropdictRepo;

@RestController
@RequestMapping(value = "/modelPropdict")
public class ModelPropDictController {
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	@Autowired
	DataDictService dataDictService;
	
	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	public Resp getModelAttrsInfo(@PathVariable(name = "tableName") String tableName, @RequestBody Set<String> fields)
			throws Exception {
		List<ModelPropDict> list = dataDictService.getDictsByFields(tableName, fields);
		return new Resp(list);
	}
}
