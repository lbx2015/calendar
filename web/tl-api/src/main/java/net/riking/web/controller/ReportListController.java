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
import net.riking.entity.model.ReportList;
import net.riking.service.repo.ReportListRepo;
import net.riking.util.ExcelToList;

@RestController
@RequestMapping(value = "/ReportList")
public class ReportListController {
	@Autowired
	ReportListRepo reportListRepo;
	
	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
		// 增，改使用RequestMethod.POST，不能重复请求
		// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@ApiOperation(value = "得到<单个>报表信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		ReportList reportList = reportListRepo.findOne(id);
		return new Resp(reportList, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<全部>报表信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ReportList reportList){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<ReportList> example = Example.of(reportList, ExampleMatcher.matchingAll());
		Page<ReportList> page = reportListRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "新增修改一条报表数据", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save( @ModelAttribute ReportList reportList){
		ReportList pReportList = reportListRepo.save(reportList);
		return new Resp(pReportList, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "新增多条报表数据", notes = "POST")
	@RequestMapping(value = "/saveMore", method = RequestMethod.POST)
	public Resp saveMore( HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("file");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<ReportList> list = null;
		try {
			InputStream is = mFile.getInputStream();
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readReportListXlsx(is, fileName);
			} else {
				list = ExcelToList.readReportListXls(is, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Resp(list.size(), CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除用户信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = reportListRepo.deleteById(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
}
