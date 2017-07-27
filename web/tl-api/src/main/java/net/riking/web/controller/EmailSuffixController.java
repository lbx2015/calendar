package net.riking.web.controller;

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
import net.riking.entity.model.EmailSuffix;
import net.riking.service.repo.EmailSuffixRepo;
import net.riking.util.ExcelToList;

@RestController
@RequestMapping(value = "/emailSuffix")
public class EmailSuffixController {
	@Autowired
	EmailSuffixRepo emailSuffixRepo;
	
	@ApiOperation(value = "得到<单个>邮箱后缀", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		EmailSuffix emailSuffix = emailSuffixRepo.findOne(id);
		return new Resp(emailSuffix, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>邮箱后缀", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute EmailSuffix emailSuffix){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<EmailSuffix> example = Example.of(emailSuffix, ExampleMatcher.matchingAll());
		Page<EmailSuffix> page = emailSuffixRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "GET")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public Resp getAll_(){
		List<EmailSuffix> list = emailSuffixRepo.findAll();
		return new Resp(list, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新邮箱后缀", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody EmailSuffix emailSuffix) {
		EmailSuffix save = emailSuffixRepo.save(emailSuffix);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量添加邮箱后缀", notes = "POST")
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("fileName");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<EmailSuffix> list =null;
		try {
			InputStream is = mFile.getInputStream();
			String[] fields = { "suffix" };
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, EmailSuffix.class);
			} else {
				list = ExcelToList.readXls(is, fields, EmailSuffix.class);
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}
		
		if(list!=null && list.size()>0){
			List<EmailSuffix> rs = emailSuffixRepo.save(list);
			return new Resp(rs, CodeDef.SUCCESS);
		}else{
			return new Resp(CodeDef.ERROR);
		}
	}
	
	@ApiOperation(value = "批量删除邮箱后缀", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = emailSuffixRepo.deleteByIds(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
	
}
