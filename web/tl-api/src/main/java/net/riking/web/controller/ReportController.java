package net.riking.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.entity.VerifyParamModel;
import net.riking.entity.VO.ReportVO;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportSubmitCaliber;
import net.riking.service.ReportService;
import net.riking.service.SysDataService;

/**
 * 报表配置的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportList")
public class ReportController {

	@Autowired
	ReportRepo reportRepo;

	@Autowired
	ReportSubmitCaliberRepo caliberRepo;

	@Autowired
	ReportService reportService;

	// @Autowired
	// AppUserReportRelRepo appUserReportRepo;

	@Autowired
	SysDataService sysDataService;

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@AuthPass
	@ApiOperation(value = "得到<单个>报表信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		Report reportList = reportRepo.findOne(id);
		if (null != reportList) {
			ReportVO reportVO = new ReportVO();
			reportVO.setId(id);
			reportVO.setReport(reportList);
			ReportSubmitCaliber reportSubmitCaliber = caliberRepo.findByReportId(reportList.getId());
			if (null != reportSubmitCaliber) {
				reportVO.setReportSubmitCaliber(reportSubmitCaliber);
			}
			return new Resp(reportVO, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}
	
	@ApiOperation(value = "得到<全部>报表信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ReportVO reportVO) {
		query.setSort("modifiedTime_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Page<ReportVO> page = reportService.findAll(reportVO, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	// TODO 暫時注釋
	// @ApiOperation(value = "得到用户报表信息", notes = "GET")
	// @RequestMapping(value = "/getUserReport", method = RequestMethod.GET)
	// public Resp getUserReport_(@ModelAttribute PageQuery query,
	// @ModelAttribute Report
	// reportList) {
	// Set<String> reportIds =
	// appUserReportRepo.findbyAppUserId(reportList.getId());
	// PageRequest pageable = new PageRequest(query.getPindex(),
	// query.getPcount(),
	// query.getSortObj());
	// Specification<Report> s1 = new Specification<Report>() {
	// @Override
	// public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query,
	// CriteriaBuilder cb) {
	// List<Predicate> list = new ArrayList<Predicate>();
	// list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
	// if (reportIds.size() > 0) {
	// list.add(root.get("id").as(String.class).in(reportIds));
	// }
	// Predicate[] p = new Predicate[list.size()];
	// return cb.and(list.toArray(p));
	// }
	// };
	// Page<Report> page = reportListRepo.findAll(Specifications.where(s1),
	// pageable);
	// return new Resp(page);
	// }

	@ApiOperation(value = "新增/修改一条报表数据", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody ReportVO reportVO) {
		reportService.saveOrUpdate(reportVO);

		return new Resp(CodeDef.SUCCESS);
	}

	// TODO 暂时注释
	// @AuthPass
	// @ApiOperation(value = "新增多条报表数据", notes = "POST")
	// @RequestMapping(value = "/saveMore", method = RequestMethod.POST)
	// public Resp saveMore_(HttpServletRequest request) {
	// MultipartHttpServletRequest multipartRequest =
	// (MultipartHttpServletRequest) request;
	// MultipartFile mFile = multipartRequest.getFile("file");
	// String fileName = mFile.getOriginalFilename();
	// String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
	// List<Report> list = null;
	// String[] fields = { "reportName", "reportCode", "reportBrief",
	// "reportOrganization",
	// "reportFrequency",
	// "reportStyle", "reportUnit", "reportRound", "reportCurrency",
	// "downloadUrl", "moduleType" };
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

	@ApiOperation(value = "批量删除", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = reportRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "批量审核", notes = "POST")
	@RequestMapping(value = "/verifyMore", method = RequestMethod.POST)
	public Resp verifyMore_(@RequestBody VerifyParamModel verifyParamModel) {
		if (verifyParamModel.getIds() == null || verifyParamModel.getIds().size() < 1) {
			return new Resp("参数有误", CodeDef.ERROR);
		}
		int rs = 0;
		List<Report> datas = reportRepo.findAll(verifyParamModel.getIds());
		// successCount表示删除成功的条数
		Integer successCount = 0;
		// failCount表示删除失败的条数
		Integer failCount = 0;
		for (Report report : datas) {
			// 已提交才可以进行审批
			if (Const.ADUIT_NO == report.getIsAduit()) {
				switch (verifyParamModel.getEvent()) {
					case "VERIFY_NOT_PASS":
						// 如果审批不通过
						if (verifyParamModel.getIds().size() > 0) {
							rs = reportRepo.verifyNotPassById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					// 如果审批通过
					case "VERIFY_PASS":
						if (verifyParamModel.getIds().size() > 0) {
							rs = reportRepo.verifyById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					default:
						failCount += 1;
						break;
				}
			} else {
				failCount += 1;
			}
		}
		// 如果数据只有一条且失败返回失败
		if (datas.size() == 1 && failCount == 1) {
			return new Resp("审批失败", CodeDef.ERROR);
		} else if (datas.size() == 1 && successCount == 1) {
			return new Resp("审批成功", CodeDef.SUCCESS);
		} else {
			return new Resp("操作成功!成功" + successCount + "条" + "失败" + failCount + "条", CodeDef.SUCCESS);
		}

	}

	@ApiOperation(value = "启用禁用", notes = "POST")
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	public Resp enable_(@RequestBody HashMap<String, String> map) {
		String id = map.get("id");
		String type = map.get("type");
		int rs = 0;
		// 启用
		if (Integer.parseInt(type) == 1) {
			rs = reportRepo.updEnable(id, 1);//
		} else {
			// 禁用
			rs = reportRepo.updEnable(id, 0);
		}
		if (rs > 0) {
			return new Resp(CodeDef.SUCCESS);
		} else {
			return new Resp(CodeDef.ERROR);
		}
	}

	// 暂时注释
	// @AuthPass
	// @ApiOperation(value = "得到<单个>报表详情h5页面", notes = "GET")
	// @RequestMapping(value = "/getForHtml", method = RequestMethod.GET)
	// public Resp getForHtml_(@RequestParam("id") String id) {
	// Report reportList = reportListRepo.findOne(id);
	// ModelPropDict dict = sysDataService.getDict("T_REPORT_LIST",
	// "REPORTUNIT",
	// reportList.getReportUnit());
	// reportList.setReportUnit(dict.getValu());
	// return new Resp(reportList, CodeDef.SUCCESS);
	// }

}
