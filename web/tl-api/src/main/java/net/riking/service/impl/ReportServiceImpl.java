package net.riking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.dao.ReportDao;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.VO.ReportVO;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportMoudleTypeResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportSubmitCaliber;
import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;
import net.riking.service.ReportService;
import net.riking.util.DateUtils;
import net.riking.util.FileUtils;
import net.riking.util.Utils;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ReportDao reportDao;

	@Autowired
	ReportCompletedRelDao reportCompletedRelDao;

	@Autowired
	ReportCompletedRelRepo reportCompletedRelRepo;

	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	ReportRepo reportRepo;

	@Autowired
	ReportSubmitCaliberRepo caliberRepo;

	@Autowired
	ReportSubscribeRelRepo reportSubscribeRelRepo;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportDao.findAppUserReportById(userId);
	}

	@Override
	public List<ReportResult> getReportResultByParam(String reportName, String userId, Integer upperLimit) {
		return reportDao.getAllReportByParams(reportName, userId, upperLimit);
	}

	@Override
	public List<ReportListResult> getReportByParam(String reportName, String userId, Integer upperLimit) {
		// TODO Auto-generated method stub
		// 获取查询相关报表集合
		List<ReportResult> list = reportDao.getAllReportByParams(reportName, userId, upperLimit);

		// 存储不同机构的报表结果集
		Map<String, List<ReportMoudleTypeResult>> agenceMap = new HashMap<String, List<ReportMoudleTypeResult>>();

		for (ReportResult report : list) {
			// 机构下相同模块的集合
			List<ReportMoudleTypeResult> reportMoudleTypeResultList = null;
			// 模块类别
			ReportMoudleTypeResult reportMoudleTypeResult = null;
			List<ReportResult> reportResults = null;
			if (agenceMap.containsKey(report.getReportType().trim())) {
				/* 存在的机构结果集 */
				reportMoudleTypeResultList = agenceMap.get(report.getReportType().trim());
				if (reportMoudleTypeResultList == null || reportMoudleTypeResultList.isEmpty()) {
					// 新创建报表模块对象
					reportMoudleTypeResultList = new ArrayList<ReportMoudleTypeResult>();
				}

				// 遍历查看二级数据是否有存在的相同类别集合
				for (ReportMoudleTypeResult data : reportMoudleTypeResultList) {
					if (data.getModuleType().trim().equals(report.getModuleType().trim())) {
						reportMoudleTypeResult = data;
						break;
					}
				}

				// 第二级数据
				if (reportMoudleTypeResult == null) {
					reportMoudleTypeResult = new ReportMoudleTypeResult();
					reportMoudleTypeResult.setModuleType(report.getModuleType().trim());
					reportMoudleTypeResult.setModuleTypeName(report.getModuleTypeName());
				}
				/* 第三级数据 */

				if (reportMoudleTypeResult.getModuleType().trim().equals(report.getModuleType().trim())) {
					// 存在的模块
					reportResults = reportMoudleTypeResult.getList();
				}

				if (reportResults == null) {
					reportResults = new ArrayList<ReportResult>();
				}
				// 添加第三级数据
				reportResults.add(report);
				reportMoudleTypeResult.setList(reportResults);
				reportMoudleTypeResultList.remove(reportMoudleTypeResult);
				reportMoudleTypeResultList.add(reportMoudleTypeResult);
			} else {
				/* 不存在的机构结果集 */
				reportMoudleTypeResultList = new ArrayList<ReportMoudleTypeResult>();
				reportMoudleTypeResult = new ReportMoudleTypeResult();
				reportMoudleTypeResult.setModuleType(report.getModuleType().trim());
				reportMoudleTypeResult.setModuleTypeName(report.getModuleTypeName());
				reportResults = new ArrayList<ReportResult>();
				reportResults.add(report);
				reportMoudleTypeResult.setList(reportResults);
				reportMoudleTypeResultList.add(reportMoudleTypeResult);
			}
			agenceMap.put(report.getReportType().trim(), reportMoudleTypeResultList);
		}

		// 机构列表
		List<ReportListResult> resultList = new ArrayList<ReportListResult>();

		for (String key : agenceMap.keySet()) {
			ReportListResult reportListResult = new ReportListResult();
			reportListResult.setAgenceCode(key);
			reportListResult.setList(agenceMap.get(key));
			resultList.add(reportListResult);
		}

		return resultList;
	}

	/*
	 * private void setReportListResult(List<ReportTypeListResult> typeListResults, int j,
	 * List<ReportListResult> reportListResults) { ReportListResult reportListResult = new
	 * ReportListResult(); reportListResult.setAgenceCode(typeListResults.get(j).getAgenceCode());
	 * List<ReportTypeListResult> results = new ArrayList<ReportTypeListResult>();
	 * results.add(typeListResults.get(j)); reportListResult.setList(results);
	 * reportListResults.add(reportListResult); }
	 * 
	 * private void setReportTypeListResult(List<ReportResult> list, int i,
	 * List<ReportTypeListResult> typeListResults) { ReportTypeListResult typeListResult = new
	 * ReportTypeListResult(); typeListResult.setAgenceCode(list.get(i).getReportType());
	 * typeListResult.setModuleType(list.get(i).getModuleType());
	 * typeListResult.setModuleTypeName(list.get(i).getModuleTypeName()); List<ReportResult> results
	 * = new ArrayList<ReportResult>(); results.add(list.get(i)); typeListResult.setList(results);
	 * typeListResults.add(typeListResult); }
	 */

	@Override
	public List<ReportCompletedRelResult> findExpireReportByPage(String userId, PageQuery pageQuery) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findExpireReportByPage(userId, pageQuery);
	}

	@Override
	public List<ReportCompletedRelResult> findHisCompletedReportByPage(String userId, PageQuery pageQuery) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findHisCompletedReportByPage(userId, pageQuery);
	}

	@Override
	public List<CurrentReportTaskResp> findCurrentTasks(String userId, String currentDate) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findCurrentTasks(userId, currentDate);
	}

	@Override
	@Transactional
	public boolean addReportTaskByUserSubscribe(String userId, String[] reportIds, String currentDate) {
		// TODO Auto-generated method stub
		String _year = currentDate.substring(0, 4);
		String _month = currentDate.substring(4, 6);
		// String _day = currentDate.substring(6, 8);
		if (reportIds == null) {
			reportIds = new String[] {};
		}
		// 先删除不在本次订阅内的reportId
		reportSubscribeRelRepo.deleteNotSubscribeByUserId(userId, reportIds);
		// 删除在该次订阅的时间范围内用户核销相关数据
		reportCompletedRelRepo.deleteSubscriptTask(userId, reportIds, currentDate);
		// 查找该用户剩余订阅的数据
		List<String> currentReportIds = reportSubscribeRelRepo.findByUserId(userId);

		List<String> reportIdList = new ArrayList<String>();
		// 批量插入
		for (String reportId : reportIds) {
			boolean isRn = true;
			for (String cutReportId : currentReportIds) {
				if (reportId.equals(cutReportId)) {
					// 订阅的报表已经在已订阅之内，不让其新增
					isRn = false;
				}
			}
			if (isRn) {
				// 保存该次订阅的数据
				ReportSubscribeRel rel = new ReportSubscribeRel();
				rel.setReportId(reportId);
				rel.setUserId(userId);
				reportSubscribeRelRepo.save(rel);
				reportIdList.add(reportId);
			}
		}
		// 处理新增核销任务
		addReportCompletedRelTask(reportIdList, _year, _month, currentDate, userId);
		return true;
	}

	/**
	 * 处理新增核销任务
	 * @param reportIdList
	 * @param _year
	 * @param _month
	 * @param currentDate
	 * @param userId
	 */
	private void addReportCompletedRelTask(List<String> reportIdList, String _year, String _month, String currentDate,
			String userId) {
		// 处理新增核销任务
		for (String reportId : reportIdList) {
			List<String> yearList = sysDaysRepo.findEnabledOnlyYear();
			for (String year : yearList) {
				int _mth = Integer.parseInt(_month);
				if (Integer.parseInt(_year) <= Integer.parseInt(year)) {
					if (Integer.parseInt(_year) < Integer.parseInt(year)) {
						_year = year;
						_mth = 1;
					}
					// 新增用户订阅报表的核销任务
					ReportSubmitCaliber reportSubmitCaliber = reportSubmitCaliberRepo.findByReportId(reportId);
					if (reportSubmitCaliber != null) {
						for (int i = _mth; i <= 12; i++) {
							_mth = i;
							_month = _mth < 10 ? "0" + _mth : _mth + "";

							String[] arr_month = reportSubmitCaliber.getSubmitMonth().split(",");

							for (String month : arr_month) {
								if (Integer.parseInt(month) == Integer.parseInt(_month)) {
									// 上报开始时间
									String submitStartTime = "";
									// 上报截止时间
									String submitEndTime = "";
									Date date = null;
									// 延后日期时间
									String afterDateStr = "";
									switch (reportSubmitCaliber.getFrequency()) {
										case 0:// 日
										case 1:// 周
										case 2:// 旬
										case 3:// 月
										case 4:// 季
											date = DateUtils.parseDate(_year + _month + "01");
											submitStartTime = _year + _month + "01";
											break;
										case 5:// 半年
											if (Integer.parseInt(_month) < 7) {// 添加上半年的半年报
												date = DateUtils.parseDate(_year + "0601");
												submitStartTime = _year + "0601";
											} else {// 添加上半年的半年报
												date = DateUtils.parseDate(_year + "1201");
												submitStartTime = _year + "1201";
											}
											break;
										case 6:// 年
											date = DateUtils.parseDate(_year + "12" + "01");
											submitStartTime = _year + "1201";
											break;
									}

									// 根据天数得到日期yyyyMMdd
									afterDateStr = DateUtils.getDateByDays(date,
											reportSubmitCaliber.getDelayDates() - 1);

									// 获取年份是否有国家节假日
									String afterYear = afterDateStr.substring(0, 4);
									if (yearList.contains(afterYear)) {
										// 未来日期>=当前日期，新增任务
										if (Integer.parseInt(currentDate) <= Integer.parseInt(afterDateStr)) {
											submitStartTime += "0000";

											// 判断是否与国家节假日，延迟上报截止时间
											afterDateStr = Utils.getWorkday(afterDateStr);
											logger.info("afterDateStr={}", afterDateStr);

											submitEndTime = afterDateStr + reportSubmitCaliber.getSubmitTime();
											logger.info("submitStartTime={}, submitEndTime={}", submitStartTime,
													submitEndTime);

											ReportCompletedRel data = reportCompletedRelRepo.findByOne(userId, reportId,
													submitStartTime, submitEndTime);
											if (data == null) {
												data = new ReportCompletedRel();
												data.setUserId(userId);
												data.setReportId(reportId);
												data.setIsCompleted(0);
												// yyyyMMddHHmm
												data.setSubmitStartTime(submitStartTime);
												data.setSubmitEndTime(submitEndTime);
												reportCompletedRelRepo.save(data);
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}

	/********** web **********/
	@Override
	public void saveOrUpdate(ReportVO reportVO) {
		Report report = reportVO.getReport();
		ReportSubmitCaliber caliber = reportVO.getReportSubmitCaliber();
		if (StringUtils.isEmpty(reportVO.getId())) {
			report.setIsDeleted(1);
			report.setIsAduit(0);
			report.setEnabled(1);
		}
		if (report.getIsAduit() == 2) {
			report.setIsAduit(0);
		}
		// 临时文件的图片转移路径
		String[] summFileNames = report.getSummary().split("alt=");
		String[] fillinFileNames = report.getFillinNote().split("alt=");
		if (summFileNames.length != 0 && summFileNames != null) {
			copyFile(summFileNames);
		}
		if (fillinFileNames.length != 0 && fillinFileNames != null) {
			copyFile(fillinFileNames);
		}
		report.setSummary(report.getSummary().replace("temp", "report"));
		report.setFillinNote(report.getFillinNote().replace("temp", "report"));
		Report pReportList = reportRepo.save(report);
		caliber.setId(pReportList.getId());
		caliberRepo.save(caliber);
	}

	private void copyFile(String[] fileNames) {
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_REPORT_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				throw new RuntimeException("文件复制异常" + e);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
	}

	@Override
	public Page<ReportVO> findAll(ReportVO reportVO, PageRequest pageable) {
		Specification<Report> bCondi = whereCondition(reportVO);
		// 1.得到Page<AppUser>对象
		Page<Report> pageB = reportRepo.findAll(bCondi, pageable);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<Report> appUsers = pageB.getContent();
			List<ReportVO> appUserVOs = getVos(appUsers);
			Page<ReportVO> modulePage = new PageImpl<ReportVO>(appUserVOs, pageable, pageB.getTotalElements());
			return modulePage;
		}
		return null;
	}

	private List<ReportVO> getVos(List<Report> reports) {
		List<ReportVO> reportVOs = new ArrayList<ReportVO>();
		for (Report report : reports) {
			String id = report.getId();
			ReportSubmitCaliber caliber = caliberRepo.findOne(id);
			ReportVO reportVO = new ReportVO();
			reportVO.setId(report.getId());
			reportVO.setReport(report);
			reportVO.setReportSubmitCaliber(caliber);
			reportVOs.add(reportVO);
		}
		return reportVOs;
	}

	private Specification<Report> whereCondition(ReportVO reportVO) {
		return new Specification<Report>() {
			@Override
			public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				if (null != reportVO.getReport()) {
					if (StringUtils.isNotBlank(reportVO.getReport().getCode())) {
						predicates.add(cb.like(root.<String> get("code"), "%" + reportVO.getReport().getCode() + "%"));
					}
					if (StringUtils.isNotBlank(reportVO.getReport().getReportType())) {
						predicates.add(cb.equal(root.<String> get("reportType"), reportVO.getReport().getReportType()));
					}
					if (StringUtils.isNotBlank(reportVO.getReport().getReportKind())) {
						predicates.add(cb.equal(root.<String> get("reportKind"), reportVO.getReport().getReportKind()));
					}
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

	@Override
	public boolean addReportTaskByUserSingleSubscribe(String userId, String[] reportIds, String currentDate,
			Integer subscribeType) {
		// TODO Auto-generated method stub
		String _year = currentDate.substring(0, 4);
		String _month = currentDate.substring(4, 6);
		// String _day = currentDate.substring(6, 8);
		if (reportIds == null) {
			reportIds = new String[] {};
		}
		// 查找该用户剩余订阅的数据
		List<String> currentReportIds = reportSubscribeRelRepo.findByUserId(userId);
		// 删除在该次订阅的时间范围内用户核销相关数据
		reportCompletedRelRepo.deleteSubscriptTask(userId, reportIds, currentDate);
		if (subscribeType == Const.IS_SUBSCRIBE) {

			List<String> reportIdList = new ArrayList<String>();
			// 批量插入
			for (String reportId : reportIds) {
				boolean isRn = true;
				for (String cutReportId : currentReportIds) {
					if (reportId.equals(cutReportId)) {
						// 订阅的报表已经在已订阅之内，不让其新增
						isRn = false;
					}
				}
				if (isRn) {
					// 保存该次订阅的数据
					ReportSubscribeRel rel = new ReportSubscribeRel();
					rel.setReportId(reportId);
					rel.setUserId(userId);
					reportSubscribeRelRepo.save(rel);
					reportIdList.add(reportId);
				}
			}
			// 处理新增核销任务
			addReportCompletedRelTask(reportIdList, _year, _month, currentDate, userId);
			return true;
		} else if (subscribeType == Const.IS_NOT_SUBSCRIBE) {
			reportSubscribeRelRepo.deleteReportRelByUserIdAndReportId(userId, reportIds[0]);
			return true;
		} else {
			return false;
		}
	}

}
