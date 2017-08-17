package net.riking.web.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
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
import net.riking.core.entity.Resp;
import net.riking.entity.PageQuery;
import net.riking.entity.model.ReportList;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.ReportListRepo;
import net.riking.util.ExcelToList;

/**
 * 报表配置的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportList")
public class ReportListController {

	@Autowired
	ReportListRepo reportListRepo;
	@Autowired
	AppUserReportRelRepo appUserReportRepo;

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@AuthPass
	@ApiOperation(value = "得到<单个>报表信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		ReportList reportList = reportListRepo.findOne(id);
		return new Resp(reportList, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到<全部>报表信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ReportList reportList) {
		reportList.setDeleteState("1");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<ReportList> example = Example.of(reportList, ExampleMatcher.matchingAll());
		Page<ReportList> page = reportListRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到用户报表信息", notes = "GET")
	@RequestMapping(value = "/getUserReport", method = RequestMethod.GET)
	public Resp getUserReport_(@ModelAttribute PageQuery query, @ModelAttribute ReportList reportList) {
		Set<String> reportIds = appUserReportRepo.findbyAppUserId(reportList.getId());
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<ReportList> s1 = new Specification<ReportList>() {
			@Override
			public Predicate toPredicate(Root<ReportList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				if (reportIds.size() > 0) {
					list.add(root.get("id").as(String.class).in(reportIds));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Page<ReportList> page = reportListRepo.findAll(Specifications.where(s1), pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "新增/修改一条报表数据", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody ReportList reportList) {
		if (reportList.getId() == null) {
			reportList.setDeleteState("1");
		}
		ReportList pReportList = reportListRepo.save(reportList);
		return new Resp(pReportList, CodeDef.SUCCESS);
	}

	@AuthPass
	@ApiOperation(value = "新增多条报表数据", notes = "POST")
	@RequestMapping(value = "/saveMore", method = RequestMethod.POST)
	public Resp saveMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("file");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<ReportList> list = null;
		String[] fields = { "reportName", "reportCode", "reportBrief", "reportOrganization", "reportFrequency",
				"reportStyle", "reportUnit", "reportRound", "reportCurrency", "moduleType", "downloadUrl" };
		try {
			InputStream is = mFile.getInputStream();
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, ReportList.class);
			} else {
				list = ExcelToList.readXls(is, fields, ReportList.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = "1";
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setDeleteState("1");
			}
			result = "2";
		}
		reportListRepo.save(list);
		return new Resp(result, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除用户信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = reportListRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

}
