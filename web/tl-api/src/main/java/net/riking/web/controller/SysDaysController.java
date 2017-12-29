package net.riking.web.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.SysDaysTempRepo;
import net.riking.entity.model.SysDaysTemp;
import net.riking.service.SysDateService;
import net.riking.util.ExcelToList;

/**
 * web端横幅操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/sysDays")
public class SysDaysController {
	@Autowired
	SysDaysTempRepo sysDaysTempRepo;

	@Autowired
	SysDateService sysDateService;

	@ApiOperation(value = "得到<单个>信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		SysDaysTemp sysDaysTemp = sysDaysTempRepo.findOne(id);
		if (null != sysDaysTemp) {
			return new Resp(sysDaysTemp, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "得到<批量>信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute SysDaysTemp sysDaysTemp) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		// Example<SysDaysTemp> example = Example.of(sysDaysTemp, ExampleMatcher.matchingAll());
		// Page<SysDaysTemp> page = sysDaysTempRepo.findAll(example, pageable);
		Page<SysDaysTemp> page = sysDateService.findAllToPage(sysDaysTemp, pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody SysDaysTemp sysDaysTemp) {
		// 修改
		sysDaysTempRepo.save(sysDaysTemp);
		return new Resp(sysDaysTemp, CodeDef.SUCCESS);
	}

	@AuthPass
	@ApiOperation(value = "导入日期信息", notes = "POST")
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("fileName");
		String fileName = mFile.getOriginalFilename();
		String sysDays = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<SysDaysTemp> list = null;
		try {
			InputStream is = mFile.getInputStream();
			String[] fields = { "dates", "weekday", "isWork", "isHoliday", "remark" };
			if (sysDays.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, SysDaysTemp.class);
			} else {
				list = ExcelToList.readXls(is, fields, SysDaysTemp.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}

		if (list != null && list.size() > 0) {
			for (SysDaysTemp dict : list) {
				dict.setEnabled("1");
			}
			List<SysDaysTemp> rs = sysDaysTempRepo.save(list);
			if (rs.size() > 0) {
				return new Resp(true, CodeDef.SUCCESS);
			} else {
				return new Resp(CodeDef.ERROR);
			}
		} else {
			return new Resp(CodeDef.ERROR);
		}
	}

}
