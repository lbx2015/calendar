package net.riking.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.PageQuery;
import net.riking.entity.model.CrtyHdayCrcy;
import net.riking.service.repo.CrtyHdayCrcyRepo;
import net.riking.util.ExcelToList;

@RestController
@RequestMapping(value = "/crtyHdayCrcy")
public class CrtyHdayCrcyController {
	@Autowired
	CrtyHdayCrcyRepo crtyHdayCrcyRepo;
	
	@ApiOperation(value = "得到<单个>各国节假日信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		CrtyHdayCrcy crtyHdayCrcy = crtyHdayCrcyRepo.findOne(id);
		return new Resp(crtyHdayCrcy, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>各国节假日信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute CrtyHdayCrcy crtyHdayCrcy){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<CrtyHdayCrcy> example = Example.of(crtyHdayCrcy, ExampleMatcher.matchingAll());
		Page<CrtyHdayCrcy> page = crtyHdayCrcyRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新各国节假日信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody CrtyHdayCrcy crtyHdayCrcy) {
		CrtyHdayCrcy save = crtyHdayCrcyRepo.save(crtyHdayCrcy);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量添加各国节假日币种信息", notes = "POST")
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("fileName");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<CrtyHdayCrcy> list =null;
		try {
			InputStream is = mFile.getInputStream();
			String[] fields = { "icon", "ctryName", "hdayName", "hdayDate", "crcy", "remark" };
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, CrtyHdayCrcy.class);
			} else {
				list = ExcelToList.readXls(is, fields, CrtyHdayCrcy.class);
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}
		
		if(list!=null && list.size()>0){
			List<CrtyHdayCrcy> rs = crtyHdayCrcyRepo.save(list);
			return new Resp(rs, CodeDef.SUCCESS);
		}else{
			return new Resp(CodeDef.ERROR);
		}
	}
	
	@ApiOperation(value = "批量删除各国节假日信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = crtyHdayCrcyRepo.deleteByIds(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
}
