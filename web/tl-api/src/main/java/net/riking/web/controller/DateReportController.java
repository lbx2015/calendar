package net.riking.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.riking.core.annos.AuthPass;
import net.riking.core.entity.Resp;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.service.ExportAllDateServiceImpl;
import net.riking.service.ExportDataExcelServiceImpl;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;
import oracle.jdbc.driver.T2CConnection;

@RestController
@RequestMapping(value = "/dateReport")
public class DateReportController {

	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	ExportAllDateServiceImpl exportAllDateServiceImpl;

	@Autowired
	AmlSuspiciousRepo suspiciousRepo;

	@Autowired
	ExportDataExcelServiceImpl exportDataExcelService;

	public static String AML_EXCEL_MODEL_PATH = "/static/excelModel/AML报表模板.xlsx";

	@AuthPass
	@RequestMapping(value = "/exportData", method = RequestMethod.GET)
	public String exportData(@RequestParam("star") String stars, @RequestParam("end") String end,
			@RequestParam("mk") String mks, @RequestParam("zt") String zts, @RequestParam("bw") String bws,
			@RequestParam("userId") Long userId, @RequestParam("token") String token, HttpServletResponse response)
			throws Exception {
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		String[] mk;
		if (mks.equals("")) {
			mk = null;
		} else {
			mk = mks.split(",");
		}
		String[] zt;
		if (zts.equals("")) {
			zt = null;
		} else {
			zt = zts.split(",");
		}
		String[] bw;
		if (zts.equals("")) {
			bw = null;
		} else {
			bw = bws.split(",");
		}
		// List<String> jgbm =
		// userBranchServiceImpl.get(TokenHolder.get().getUserId());
		List<String> reportType = new ArrayList<>();
		if (bw != null) {
			for (int i = 0; i < bw.length; i++) {
				reportType.add(bw[i]);
			}
		}
		Date star;
		Date ends;
		if (stars.equals("")) {
			star = null;
		} else {
			star = dfs.parse(stars);
		}
		if (end.equals("")) {
			ends = null;
		} else {
			ends = dfs.parse(end);
		}

		List<BigAmount> bigAmounts = null;
		List<AmlSuspicious> amlSuspicious = null;
		if (mk != null) {
			for (int i = 0; i < mk.length; i++) {
				if (mk[i].equals("bigAmount")) {
					// bigAmounts= dateExportImpl.getBigAmount(stars, end, zt,
					// bw,jgbm);
					// BigAmount bigAmount = null;
					Specification<BigAmount> b1 = new Specification<BigAmount>() {
						@Override
						public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
							List<Predicate> list = new ArrayList<Predicate>();
							// Join<BigAmount, Job> depJoin = root
							// .join(root.getModel().getSingularAttribute("job",
							// Job.class), JoinType.LEFT);
							if (zt != null) {
								for (int i = 0; i < zt.length; i++) {
									if (zt[i].equals("PRE_RECROD")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_RECROD"));
									}
									if (zt[i].equals("PRE_SUBMIT")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_SUBMIT"));
									}
									if (zt[i].equals("PRE_VERIFY")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_VERIFY"));
									}
									if (zt[i].equals("PRE_EXPORT")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORT"));
									}
									if (zt[i].equals("PRE_EXPORTOVER")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORTOVER"));
									}
									if (zt[i].equals("PRE_STORAGE")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_STORAGE"));
									}
								}
								Predicate[] p = new Predicate[list.size()];
								return cb.or(list.toArray(p));
							} else {
								return null;
							}
						}
					};
					Specification<BigAmount> b2 = new Specification<BigAmount>() {
						@Override
						public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
							List<Predicate> list = new ArrayList<Predicate>();
							if (star != null || ends != null) {
								if (ends == null) {
									list.add(cb.greaterThanOrEqualTo(root.<Date>get("rpdt"), star));
								} else if (star == null) {
									list.add(cb.lessThanOrEqualTo(root.<Date>get("rpdt"), ends));
								} else {
									list.add(cb.between(root.<Date>get("rpdt"), star, ends));
								}
							}
							list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
							if (reportType.size() > 0) {
								list.add(root.get("reportType").as(String.class).in(reportType));
							}
							// 非超级管理员
							if (null != userId) {
								List<String> jgbm = userBranchServiceImpl.get(userId, token);
								if (jgbm != null && jgbm.size() > 0) {
									list.add(root.get("jgbm").as(String.class).in(jgbm));
								}
							}
							Predicate[] p = new Predicate[list.size()];
							return cb.and(list.toArray(p));
						}
					};
					bigAmounts = bigAmountRepo.findAll(Specifications.where(b1).and(b2));
				} else if (mk[i].equals("sus")) {
					// amlSuspicious = dateExportImpl.getAmlSuspiciou(stars,
					// end, zt, bw, jgbm);
					// AmlSuspicious amlSuspiciou = null;
					Specification<AmlSuspicious> b1 = new Specification<AmlSuspicious>() {
						@Override
						public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query,
								CriteriaBuilder cb) {
							List<Predicate> list = new ArrayList<Predicate>();
							// Join<AmlSuspicious, Job> depJoin = root
							// .join(root.getModel().getSingularAttribute("job",
							// Job.class), JoinType.LEFT);
							if (zt != null) {
								for (int i = 0; i < zt.length; i++) {
									if (zt[i].equals("PRE_RECROD")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_RECROD"));
									}
									if (zt[i].equals("PRE_SUBMIT")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_SUBMIT"));
									}
									if (zt[i].equals("PRE_VERIFY")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_VERIFY"));
									}
									if (zt[i].equals("PRE_EXPORT")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORT"));
									}
									if (zt[i].equals("PRE_EXPORTOVER")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORTOVER"));
									}
									if (zt[i].equals("PRE_STORAGE")) {
										list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_STORAGE"));
									}
								}
								Predicate[] p = new Predicate[list.size()];
								return cb.or(list.toArray(p));
							} else {
								return null;
							}
						}
					};
					Specification<AmlSuspicious> b2 = new Specification<AmlSuspicious>() {
						@Override
						public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query,
								CriteriaBuilder cb) {
							List<Predicate> list = new ArrayList<Predicate>();
							if (star != null || ends != null) {
								if (ends == null) {
									list.add(cb.greaterThanOrEqualTo(root.<Date>get("rpdt"), star));
								} else if (star == null) {
									list.add(cb.lessThanOrEqualTo(root.<Date>get("rpdt"), ends));
								} else {
									list.add(cb.between(root.<Date>get("rpdt"), star, ends));
								}
							}
							list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
							if (reportType.size() > 0) {
								list.add(root.get("reportType").as(String.class).in(reportType));
							}

							// 非超级管理员
							if (null != userId) {
								List<String> jgbm = userBranchServiceImpl.get(userId, token);
								if (jgbm != null && jgbm.size() > 0) {
									list.add(root.get("jgbm").as(String.class).in(jgbm));
								}
							}
							Predicate[] p = new Predicate[list.size()];
							return cb.and(list.toArray(p));
						}
					};
					amlSuspicious = suspiciousRepo.findAll(Specifications.where(b1).and(b2));
				}
			}
		}
		HSSFWorkbook wb = null;
		if (bigAmounts != null || amlSuspicious != null) {
			wb = exportAllDateServiceImpl.exportBigAmountDate(bigAmounts, amlSuspicious);
		}
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar calendar = Calendar.getInstance();
		String fliename = df.format(calendar.getTime());
		fliename = fliename + ".xls";
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			if (os.size() > 0) {
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition", "attachment; filename=" + fliename);
				response.addHeader("Content-Length", "" + os.size());
				IOUtils.write(os.toByteArray(), response.getOutputStream());
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@AuthPass
	@RequestMapping(value = "/exportDataTmpl", method = RequestMethod.GET)
	public String exportDataTmpl(@RequestParam("star") String start, @RequestParam("end") String end,
			@RequestParam("branchName") String branchName, @RequestParam("userId") Long userId,
			@RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String path = this.getClass().getResource("/").getPath();
			FileInputStream is = new FileInputStream(path + AML_EXCEL_MODEL_PATH);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Set<String> jgbm = new HashSet<String>();
			List<String> list = userBranchServiceImpl.get(userId, token);
			jgbm.addAll(list);
			exportDataExcelService.fillData(is, os, sdf.parse(start), sdf.parse(end), branchName, jgbm);
			if (os.size() > 0) {
				String fileName = start + "TO" + end + "AML.xlsx";
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				response.addHeader("Content-Length", "" + os.size());
				IOUtils.write(os.toByteArray(), response.getOutputStream());
				response.getOutputStream().flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@AuthPass
	@RequestMapping(value = "/downLoadExcel", method = RequestMethod.GET)
	public Resp downLoad(@RequestParam("path") String path, HttpServletResponse response) throws Exception {
		File file = new File(path);
		// 取得文件名。
		String filename = file.getName();
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=" + filename);
			response.addHeader("Content-Length", "" + file.length());
			IOUtils.write(FileUtils.readFileToByteArray(file), response.getOutputStream());
			response.getOutputStream().flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Resp();
	}
}
