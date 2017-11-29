package net.riking.web.appInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.QureyResulte;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.ReportSubmitCaliberRepo;
import net.riking.service.repo.SysDaysRepo;

/***
 * 获取完成报表
 * @author Lucky.Liu
 * @version crateTime：2017年8月23日 下午5:22:26
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUserReportCompleteRel")
public class AppUserReportCompleteRelServer {
	// @Autowired
	// AppUserReportCompletRelRepo appUserReportCompleteRelRepo;

	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	AppUserReportRelRepo appUserReportRelRepo;

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	SysDateServiceImpl sysDateService;

	// @ApiOperation(value = "用户报表完成情况新增", notes = "POST")
	// @RequestMapping(value = "/save", method = RequestMethod.POST)
	// public AppResp save(@RequestBody ReportCompletedRel appUserReportCompleteRel) {
	// appUserReportCompleteRelRepo.save(appUserReportCompleteRel);
	// return new AppResp(CodeDef.SUCCESS);
	// }

	@ApiOperation(value = "用户获取当天报表完成情况", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody ReportCompletedRel completeRel) {
		List<QureyResulte> completeRels = new ArrayList<QureyResulte>();
		// completeRels = appUserReportCompleteRelRepo.getReportId(completeRel.getAppUserId(),
		// completeRel.getCompleteDate());
		return new AppResp(completeRels, CodeDef.SUCCESS);

	}

	@ApiOperation(value = "获取当天未完成和已完成的报表列表", notes = "POST")
	@RequestMapping(value = "/getReportByIdAndTime", method = RequestMethod.POST)
	public AppResp getReportByIdAndTime(@RequestBody ReportCompletedRel completeRel) {
		// List<>
		List<ReportCompletedRel> list = new ArrayList<ReportCompletedRel>();
		// list = reportSubmitCaliberService
		// .findCompleteReportByIdAndTime(completeRel.getAppUserId(),
		// completeRel.getCompleteDate());
		return new AppResp(list, CodeDef.SUCCESS);
	}
	// 先注释，后面要打开
	// TODO @ApiOperation(value = "获取当天之后的打点日期", notes = "POST")
	// @RequestMapping(value = "/getDatedot", method = RequestMethod.POST)
	// public AppResp getDatedot(@RequestBody AppUser appUser) throws ParseException {
	// // List<String> list = getDateByToDay();//获取当日之后的一年日期
	// List<String> allList = new ArrayList<>();// 存日期 返回
	// Set<String> result = new HashSet<String>();// 取交集用
	//
	// // 获取当月的打点数据 如果只打点当月之后的数据
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	// // sdf.parse(str);
	// List<String> dateList = getAllTheDateOftheMonth(sdf.parse(appUser.getRemindTime()));
	//
	// // 根据userId去用户订阅表里查询未完成的 报表id 集合
	// Set<String> userSetList =
	// appUserReportRelRepo.findReportByUserIdAndIsComplete(appUser.getId());
	// // 循环日期 根据当天日期去查询 报送口径表 相符合的报表id集合
	// for (String date : dateList) {
	// Set<String> setList = getReportList(date);// 计算出的reportId集合
	// result.clear();
	// result.addAll(userSetList);
	// result.retainAll(setList);
	// if (result != null && !result.isEmpty()) {// 如果存在并集 则将日期存进list
	// allList.add(date);
	// }
	// }
	//
	// return new AppResp(allList, CodeDef.SUCCESS);
	// }

	private List<String> getAllTheDateOftheMonth(Date date) {
		List<String> list = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		SimpleDateFormat sdf = null;
		while (cal.get(Calendar.MONTH) == month) {
			sdf = new SimpleDateFormat("yyyyMMdd");
			list.add(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

	public List<String> getDateByToDay() throws ParseException {
		List<String> list = new ArrayList<>();
		Date d = new Date();
		System.out.println(d);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String dateNowStr = sdf1.format(d);
		System.out.println("格式化后的日期：" + dateNowStr);

		Date today = sdf1.parse(dateNowStr);
		System.out.println("字符串转成日期：" + today);

		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, +1);// 延迟一年
		date = calendar.getTime();

		Calendar dd = Calendar.getInstance();// 定义日期实例

		dd.setTime(today);// 设置日期起始时间
		SimpleDateFormat sdf = null;
		while (dd.getTime().before(date)) {// 判断是否到结束日期

			sdf = new SimpleDateFormat("yyyyMMdd");

			String str = sdf.format(dd.getTime());

			System.out.println(str);// 输出日期结果
			list.add(str);
			dd.add(Calendar.DATE, 1);// 进行当前日期加1
		}
		return list;
	}

	/*
	 * public Set<String> getReportList(String date) { SysDays day = sysDaysRepo.findOne(date);
	 * Set<String> reportId = new HashSet<>(); Period periods = sysDateService.getDate(date, "0");
	 * if (day.getIsWork() == 1) { Period period = sysDateService.getDate(date, "1"); reportId =
	 * reportSubmitCaliberRepo.findByWorkDatefromReportId(period.getWeek(), period.getTen(),
	 * period.getMonth(), period.getSeason(), period.getHalfYear(), period.getYear()); Set<String>
	 * reportIdAdd = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(),
	 * periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
	 * periods.getYear()); for (String string : reportIdAdd) { reportId.add(string); } } else {
	 * reportId = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(),
	 * periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
	 * periods.getYear()); }
	 * 
	 * return reportId; }
	 */
}
