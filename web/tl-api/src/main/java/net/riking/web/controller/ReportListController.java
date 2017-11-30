package net.riking.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.Report;
import net.riking.service.SysDataService;
import net.riking.service.repo.ReportListRepo;

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

	// @Autowired
	// AppUserReportRelRepo appUserReportRepo;

	@Autowired
	SysDataService sysDataService;

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@ApiOperation(value = "得到<单个>报表信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		Report reportList = reportListRepo.findOne(id);
		return new Resp(reportList, CodeDef.SUCCESS);
	}

	// TODO 暂时注释
	// @ApiOperation(value = "得到<全部>报表信息", notes = "GET")
	// @RequestMapping(value = "/getMore", method = RequestMethod.GET)
	// public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute Report reportList) {
	// reportList.setDeleteState("1");
	// PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(),
	// query.getSortObj());
	// Example<Report> example = Example.of(reportList, ExampleMatcher.matchingAll());
	// Page<Report> page = reportListRepo.findAll(example, pageable);
	// return new Resp(page, CodeDef.SUCCESS);
	// }

	// TODO 暫時注釋
	// @ApiOperation(value = "得到用户报表信息", notes = "GET")
	// @RequestMapping(value = "/getUserReport", method = RequestMethod.GET)
	// public Resp getUserReport_(@ModelAttribute PageQuery query, @ModelAttribute Report
	// reportList) {
	// Set<String> reportIds = appUserReportRepo.findbyAppUserId(reportList.getId());
	// PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(),
	// query.getSortObj());
	// Specification<Report> s1 = new Specification<Report>() {
	// @Override
	// public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	// List<Predicate> list = new ArrayList<Predicate>();
	// list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
	// if (reportIds.size() > 0) {
	// list.add(root.get("id").as(String.class).in(reportIds));
	// }
	// Predicate[] p = new Predicate[list.size()];
	// return cb.and(list.toArray(p));
	// }
	// };
	// Page<Report> page = reportListRepo.findAll(Specifications.where(s1), pageable);
	// return new Resp(page);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "新增/修改一条报表数据", notes = "POST")
	// @RequestMapping(value = "/save", method = RequestMethod.POST)
	// public Resp save_(@RequestBody Report reportList) {
	// if (reportList.getId() == null) {
	// reportList.setDeleteState("1");
	// }
	// Report pReportList = reportListRepo.save(reportList);
	// return new Resp(pReportList, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @AuthPass
	// @ApiOperation(value = "新增多条报表数据", notes = "POST")
	// @RequestMapping(value = "/saveMore", method = RequestMethod.POST)
	// public Resp saveMore_(HttpServletRequest request) {
	// MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	// MultipartFile mFile = multipartRequest.getFile("file");
	// String fileName = mFile.getOriginalFilename();
	// String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
	// List<Report> list = null;
	// String[] fields = { "reportName", "reportCode", "reportBrief", "reportOrganization",
	// "reportFrequency",
	// "reportStyle", "reportUnit", "reportRound", "reportCurrency", "downloadUrl", "moduleType" };
	// try {
	// InputStream is = mFile.getInputStream();
	// if (suffix.equals("xlsx")) {
	// list = ExcelToList.readXlsx(is, fields, Report.class);
	// } else {
	// list = ExcelToList.readXls(is, fields, Report.class);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// String result = "1";
	// if (list.size() > 0) {
	// for (int i = 0; i < list.size(); i++) {
	// list.get(i).setDeleteState("1");
	// }
	// result = "2";
	// }
	// reportListRepo.save(list);
	// return new Resp(result, CodeDef.SUCCESS);
	// }

	/*
	 * @ApiOperation(value = "批量删除用户信息", notes = "POST")
	 * 
	 * @RequestMapping(value = "/delMore", method = RequestMethod.POST) public Resp
	 * delMore_(@RequestBody Set<String> ids) { int rs = 0; if (ids.size() > 0) { rs =
	 * reportListRepo.deleteById(ids); } if (rs > 0) { return new Resp().setCode(CodeDef.SUCCESS); }
	 * else { return new Resp().setCode(CodeDef.ERROR); } }
	 */

	// 暂时注释
	// @AuthPass
	// @ApiOperation(value = "得到<单个>报表详情h5页面", notes = "GET")
	// @RequestMapping(value = "/getForHtml", method = RequestMethod.GET)
	// public Resp getForHtml_(@RequestParam("id") String id) {
	// Report reportList = reportListRepo.findOne(id);
	// ModelPropDict dict = sysDataService.getDict("T_REPORT_LIST", "REPORTUNIT",
	// reportList.getReportUnit());
	// reportList.setReportUnit(dict.getValu());
	// return new Resp(reportList, CodeDef.SUCCESS);
	// }

}
