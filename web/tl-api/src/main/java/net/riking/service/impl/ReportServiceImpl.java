package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportDao;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.entity.VO.ReportVO;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportSubmitCaliber;
import net.riking.entity.model.ReportTypeListResult;
import net.riking.service.ReportService;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	@Autowired
	ReportDao reportDao;

	@Autowired
	ReportRepo reportRepo;

	@Autowired
	ReportSubmitCaliberRepo caliberRepo;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportDao.findAppUserReportById(userId);
	}

	@Override
	public List<ReportResult> getReportResultByParam(String reportName, String userId) {
		return reportDao.getAllReportByParams(reportName, userId);
	}

	@Override
	public List<ReportListResult> getReportByParam(String reportName, String userId) {
		// TODO Auto-generated method stub

		List<ReportResult> list = reportDao.getAllReportByParams(reportName, userId);

		List<ReportTypeListResult> typeListResults = new ArrayList<ReportTypeListResult>();

		List<ReportListResult> reportListResults = new ArrayList<ReportListResult>();

		for (int i = 0; i < list.size(); i++) {
			// 塞ReportTypeListResult第二层数据
			if (typeListResults.size() == 0) {
				setReportTypeListResult(list, i, typeListResults);
			}
			for (int j = 0; j < typeListResults.size(); j++) {
				// 把报表类型相等并且报表所属模块相等的归一类
				if (list.get(i).getReportType().toUpperCase().equals(typeListResults.get(j).getAgenceCode())
						&& list.get(i).getModuleType().equals(typeListResults.get(j).getModuleType())) {
					typeListResults.get(j).getList().add(list.get(i));
				} else {
					if (j == typeListResults.size() - 1) {
						setReportTypeListResult(list, i, typeListResults);
					}
				}
				// 塞ReportListResult第三层数据,在第二层数据塞完才执行
				if (list.size() - 1 == i) {
					if (reportListResults.size() == 0) {
						setReportListResult(typeListResults, j, reportListResults);
					}
					for (int k = 0; k < reportListResults.size(); k++) {
						if (reportListResults.get(k).getAgenceCode().toUpperCase()
								.equals(typeListResults.get(j).getAgenceCode())) {
							reportListResults.get(k).getList().add(typeListResults.get(j));
						} else {
							if (k == reportListResults.size() - 1) {
								setReportListResult(typeListResults, j, reportListResults);
							}
						}
					}
				}
			}
		}

		return reportListResults;
	}

	private void setReportListResult(List<ReportTypeListResult> typeListResults, int j,
			List<ReportListResult> reportListResults) {
		ReportListResult reportListResult = new ReportListResult();
		reportListResult.setAgenceCode(typeListResults.get(j).getAgenceCode());
		List<ReportTypeListResult> results = new ArrayList<ReportTypeListResult>();
		results.add(typeListResults.get(j));
		reportListResult.setList(results);
		reportListResults.add(reportListResult);
	}

	private void setReportTypeListResult(List<ReportResult> list, int i, List<ReportTypeListResult> typeListResults) {
		ReportTypeListResult typeListResult = new ReportTypeListResult();
		typeListResult.setAgenceCode(list.get(i).getReportType());
		typeListResult.setModuleType(list.get(i).getModuleType());
		typeListResult.setModuleTypeName(list.get(i).getModuleTypeName());
		List<ReportResult> results = new ArrayList<ReportResult>();
		results.add(list.get(i));
		typeListResult.setList(results);
		typeListResults.add(typeListResult);
	}

	/********** web **********/
	@Override
	public void saveOrUpdate(ReportVO reportVO) {
		Report report = reportVO.getReport();
		ReportSubmitCaliber caliber = reportVO.getReportSubmitCaliber();
		if (StringUtils.isEmpty(reportVO.getId())) {
			report.setIsDeleted(1);
			report.setIsAduit(0);
		}
		Report pReportList = reportRepo.save(report);
		caliber.setId(pReportList.getId());
		caliberRepo.save(caliber);
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

}
