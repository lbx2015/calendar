package net.riking.web.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.config.ExportConfig;
import net.riking.config.enums.ExportType;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InModLog;
import net.riking.core.task.TaskMgr;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.BigAmountMz;
import net.riking.entity.DataExportInfo;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.ZipFileModel;
import net.riking.service.BigCheckServiceImpl;
import net.riking.service.ExportDataServiceImpl;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;
import net.riking.task.job.ExportReportJob;

@InModLog(modName = "报文导出")
@RestController
@RequestMapping(value = "/dataExport")
public class DataExportController {
	@Autowired
	ExportConfig exportConfig;
	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;
	@Autowired
	BigAmountRepo bigAmountRepo;
	@Autowired
	ExportDataServiceImpl exportDataServiceImpl;

	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	BigCheckServiceImpl bigCheckService;
	@Autowired
	ExportReportJob exportReportJob;
	
	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;
	
	@Autowired
	TaskMgr taskMgr;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public Resp getData(@RequestParam("exportDate") Date exportDate) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		// 可疑交易带导出数量
		List<AmlSuspicious> list = amlSuspiciousRepo.findAll(new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("rpdt").as(Date.class)), exportDate));
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				list.add(cb.equal((root.get("submitType").as(String.class)), "Y"));
//				Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//						JoinType.LEFT);
				list.add(cb.like(root.get("flowState").as(String.class), "PRE_EXPORT"));
				// 非超级管理员
//				if (null != TokenHolder.get().getBranchId()) {
//					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
//					if (branchCodes != null && branchCodes.size() > 0) {
//						list.add(root.get("jgbm").as(String.class).in(branchCodes));
//					}
//				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
		for (AmlSuspicious susp : list) {
			if (susp.getBwzt().equals("xzbw") || susp.getBwzt().equals("cwhz")) {
				if (map.containsKey("sn")) {
					map.put("sn", map.get("sn") + 1);
				} else {
					map.put("sn", 1);
				}
			} else if (susp.getBwzt().equals("bzhz") || susp.getBwzt().equals("zdjc")) {
				if (map.containsKey("sc")) {
					map.put("sc", map.get("sc") + 1);
				} else {
					map.put("sc", 1);
				}
			} else if (susp.getBwzt().equals("rggz")) {
				if (map.containsKey("sa")) {
					map.put("sa", map.get("sa") + 1);
				} else {
					map.put("sa", 1);
				}
			} else if (susp.getBwzt().equals("rgbc")) {
				if (map.containsKey("ss")) {
					map.put("ss", map.get("ss") + 1);
				} else {
					map.put("ss", 1);
				}
			}
		}
		// 大额未审核数量
		HashSet<String> set = new HashSet<String>();
		set.add("PRE_RECROD");
		set.add("PRE_SUBMIT");
		set.add("PRE_VERIFY");
		Integer notPassCont = bigAmountRepo.findNotPass(exportDate, "1", "Y", set);
		map.put("ap", notPassCont);
		// 大额审核后带导出数量
		List<BigAmount> list2 = bigAmountRepo.findAll(new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("rpdt").as(Date.class)), exportDate));
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				list.add(cb.equal((root.get("submitType").as(String.class)), "Y"));
//				Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//						JoinType.LEFT);
				list.add(cb.like(root.get("flowState").as(String.class), "PRE_EXPORT"));
				// 非超级管理员
//				if (null != TokenHolder.get().getBranchId()) {
//					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
//					if (branchCodes != null && branchCodes.size() > 0) {
//						list.add(root.get("jgbm").as(String.class).in(branchCodes));
//					}
//				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
		// 判断金额是否满足
		Map<String, BigAmountMz> csnmSfmzMap = new HashMap<String, BigAmountMz>();
		for (BigAmount bigAmount : list2) {
			if (!csnmSfmzMap.containsKey(bigAmount.getCtid())) {
				try {
					csnmSfmzMap.put(bigAmount.getCtid(), bigCheckService.getBigamountSfmz(bigAmount));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			bigAmount.setSfmz(csnmSfmzMap.get(bigAmount.getCtid()).getSfmz());
			// 不满足02
			if (bigAmount.getSfmz().equals("02")) {
				continue;
			}
			if (bigAmount.getBwzt().equals("xzbw") || bigAmount.getBwzt().equals("cwhz")) {
				if (map.containsKey("dn")) {
					map.put("dn", map.get("dn") + 1);
				} else {
					map.put("dn", 1);
				}
			} else if (bigAmount.getBwzt().equals("bzhz") || bigAmount.getBwzt().equals("zdjc")) {
				if (map.containsKey("dc")) {
					map.put("dc", map.get("dc") + 1);
				} else {
					map.put("dc", 1);
				}
			} else if (bigAmount.getBwzt().equals("scbw") || bigAmount.getBwzt().equals("sjsc")
					|| bigAmount.getBwzt().equals("tzsc") || bigAmount.getBwzt().equals("khsc")) {
				if (map.containsKey("dd")) {
					map.put("dd", map.get("dd") + 1);
				} else {
					map.put("dd", 1);
				}
			} else if (bigAmount.getBwzt().equals("rggz")) {
				if (map.containsKey("da")) {
					map.put("da", map.get("da") + 1);
				} else {
					map.put("da", 1);
				}
			} else if (bigAmount.getBwzt().equals("rgbc")) {
				if (map.containsKey("ds")) {
					map.put("ds", map.get("ds") + 1);
				} else {
					map.put("ds", 1);
				}
			}
		}
		return new Resp(map, CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/exportBig", method = RequestMethod.GET)
	public Resp exportBig(@RequestParam("exportDate") String exportDate) {
		String userName = TokenHolder.get().getUserName();
		String token = TokenHolder.get().getToken();
		Long branchId = TokenHolder.get().getBranchId();
//		List<String> branchCodes = new ArrayList<String>();
//		if (null != TokenHolder.get().getBranchId()) {
//			branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
//		}
		//DataExportInfo info = new DataExportInfo(config.getExportBig(),branchId,exportDate,token,userName,branchCodes);
		DataExportInfo info = new DataExportInfo(ExportType.Big.getName(),branchId,exportDate,token,userName);
		String batchId = null;
		try {
			batchId = taskMgr.addJob(Const.EXPORT_REPORT_GROUP, info, exportReportJob);
		} catch (JsonProcessingException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}
		return new Resp(taskMgr.getJobInfo(batchId),CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/exportBigSusp", method = RequestMethod.GET)
	public Resp exportBigSusp(@RequestParam("exportDate") String exportDate) {
		String userName = TokenHolder.get().getUserName();
		String token = TokenHolder.get().getToken();
		Long branchId = TokenHolder.get().getBranchId();
//		List<String> branchCodes = new ArrayList<String>();
//		if (null != TokenHolder.get().getBranchId()) {
//			branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
//		}
		//DataExportInfo info = new DataExportInfo(config.getExportBoth(),branchId,exportDate,token,userName,branchCodes);
		DataExportInfo info = new DataExportInfo(ExportType.Both.getName(),branchId,exportDate,token,userName);
		String batchId = null;
		try {
			batchId = taskMgr.addJob(Const.EXPORT_REPORT_GROUP, info, exportReportJob);
		} catch (JsonProcessingException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}
		return new Resp(taskMgr.getJobInfo(batchId),CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/exportSusp", method = RequestMethod.GET)
	public Resp exportSusp(@RequestParam("exportDate") String exportDate) {
		String userName = TokenHolder.get().getUserName();
		String token = TokenHolder.get().getToken();
		Long branchId = TokenHolder.get().getBranchId();
//		List<String> branchCodes = new ArrayList<String>();
//		if (null != TokenHolder.get().getBranchId()) {
//			branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
//		}
		//DataExportInfo info = new DataExportInfo(config.getExportSusp(),branchId,exportDate,token,userName,branchCodes);
		DataExportInfo info = new DataExportInfo(ExportType.Susp.getName(),branchId,exportDate,token,userName);
		String batchId = null;
		try {
			batchId = taskMgr.addJob(Const.EXPORT_REPORT_GROUP, info, exportReportJob);
		} catch (JsonProcessingException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}
		return new Resp(taskMgr.getJobInfo(batchId),CodeDef.SUCCESS);
	}
	
	
	@RequestMapping(value = "/getLast", method = RequestMethod.GET)
	public Resp getLast(@RequestParam String batchId) {
		if (StringUtils.isNotEmpty(batchId)) {
			return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
		} else {
			return new Resp(taskMgr.getLastJobInfoByGroup(Const.EXPORT_REPORT_GROUP), CodeDef.SUCCESS);
		}
	}

	@RequestMapping(value = "/getZip", method = RequestMethod.GET)
	public Resp getZip(@ModelAttribute PageQuery query, @ModelAttribute ZipFileModel zip) {
		if (StringUtils.isEmpty(zip.getType())) {
			zip.setType("BigSusp");
		}
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		List<ZipFileModel> bigList = new ArrayList<ZipFileModel>();
		List<ZipFileModel> susplist = new ArrayList<ZipFileModel>();
		if (zip.getType().contains("Big")) {
			String bigAmountPath = exportConfig.getExportBigamountNowDir();
			bigList = this.getZips("Big", bigAmountPath);
		}
		if (zip.getType().contains("Susp")) {
			String suspPath = exportConfig.getExportShadinessNowDir();
			susplist = this.getZips("Susp", suspPath);
		}
		susplist.addAll(bigList);
		ArrayList<ZipFileModel> list = new ArrayList<ZipFileModel>();
		if (zip.getCreatTime() != null) {
			for (ZipFileModel zipFileModel : susplist) {
				if (zipFileModel.getCreatTime().getTime() == zip.getCreatTime().getTime()) {
					list.add(zipFileModel);
				}
			}
		} else {
			list.addAll(susplist);
		}
		List<ZipFileModel> subList = null;
		if (list.size() > (query.getPindex() + 1) * query.getPcount()) {
			int s = query.getPindex() * query.getPcount();
			int e = (query.getPindex() + 1) * query.getPcount();
			subList = list.subList(s, e);
		} else {
			int s = query.getPindex() * query.getPcount();
			subList = list.subList(s, list.size());
		}
		PageImpl<ZipFileModel> page = new PageImpl<ZipFileModel>(subList, pageable, list.size());
		return new Resp(page, CodeDef.SUCCESS);
	}

	@AuthPass
	@RequestMapping(value = "/downLoad", method = RequestMethod.GET)
	public String downLoad(@RequestParam("name") String name, @RequestParam("type") String type,
			HttpServletResponse response) {
		String path = null;
		if (type.equals("Susp")) {
			path = exportConfig.getExportShadinessNowDir();
		} else if (type.equals("Big")) {
			path = exportConfig.getExportBigamountNowDir();
		}
		this.downLoadUtil(path + name, response);
		return null;
	}

	private List<ZipFileModel> getZips(String type, String path) {
		File folder = new File(path);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<ZipFileModel> list = new ArrayList<ZipFileModel>();
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files.length > 0) {
				for (File file : files) {
					if (!file.isDirectory()) {
						String name = file.getName();
						try {
							Date creatTime = sdf.parse(name.split("-")[1]);
							String submitBatch = name.split("-")[2].substring(0, 8);
							ZipFileModel zipFileModel = new ZipFileModel(name, type, creatTime, submitBatch);
							list.add(zipFileModel);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return list;
	}

	private void downLoadUtil(String path, HttpServletResponse response) {
		File file = new File(path);
		// 取得文件名。
		String filename = file.getName();
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.addHeader("Content-Length", "" + file.length());
			IOUtils.write(FileUtils.readFileToByteArray(file), response.getOutputStream());
			response.getOutputStream().flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
