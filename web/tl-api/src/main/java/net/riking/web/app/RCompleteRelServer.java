package net.riking.web.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.RSubscribeRelRepo;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Period;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.SysDays;
import net.riking.entity.params.RCompletedRelParams;
import net.riking.entity.resp.RCompletedRelResp;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDateService;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.util.Utils;

/**
 * 核销信息接口
 * @author jc.tan 2017年11月29日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/rCompletedRel")
public class RCompleteRelServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ReportCompletedRelRepo reportCompletedRelRepo;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	RSubscribeRelRepo rSubscribeRelRepo;

	@Autowired
	SysDateService sysDateService;

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	SysDateServiceImpl getSysDateService;

	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;

	/**
	 * 用户报表完成情况新增[userId,reportId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "用户报表完成情况新增", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public AppResp save(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		RCompletedRelParams rCompletedRelParams = Utils.map2Obj(params, RCompletedRelParams.class);
		ReportCompletedRel completedRel = new ReportCompletedRel();
		completedRel.setReportId(rCompletedRelParams.getReportId());
		completedRel.setUserId(rCompletedRelParams.getUserId());
		completedRel.setCompletedDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		reportCompletedRelRepo.save(completedRel);
		return new AppResp(CodeDef.SUCCESS);
	}

	/**
	 * 用户获取当天报表完成情况[userId,completedDate]
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "用户获取当天报表完成情况", notes = "POST")
	@RequestMapping(value = "/findNowReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody Map<String, Object> params) throws ParseException {
		RCompletedRelParams rCompletedRelParams = Utils.map2Obj(params, RCompletedRelParams.class);
		// {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(completedDate);
		// calendar.add(Calendar.DATE, +1);// 后一天
		// endDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
		// }
		// {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(completedDate);
		// calendar.add(Calendar.DATE, -1);// 前一天
		// beginDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
		// }
		List<RCompletedRelResp> rCompletedRelRespList = reportCompletedRelRepo
				.findNowReport(rCompletedRelParams.getUserId(), rCompletedRelParams.getCompletedDate());

		List<Map<String, Object>> rCompletedRelRespMapList = new ArrayList<Map<String, Object>>();
		for (RCompletedRelResp rCompletedRelResp : rCompletedRelRespList) {
			// 将对象转换成map
			Map<String, Object> rCompletedRelRespMap = Utils.objProps2Map(rCompletedRelResp, true);
			rCompletedRelRespMapList.add(rCompletedRelRespMap);
		}

		return new AppResp(rCompletedRelRespMapList, CodeDef.SUCCESS);
	}

	/**
	 * 获取当天未完成和已完成的报表列表[userId,completedDate]
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取当天未完成和已完成的报表列表", notes = "POST")
	@RequestMapping(value = "/findByIdAndTime", method = RequestMethod.POST)
	public AppResp findByIdAndTime(@RequestBody Map<String, Object> params) throws ParseException {
		RCompletedRelParams rCompletedRelParams = Utils.map2Obj(params, RCompletedRelParams.class);
		List<Map<String, Object>> rCompletedRelRespMapList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(completedDate);
		// calendar.add(Calendar.DATE, +1);// 后一天
		// endDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
		// }
		// {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(completedDate);
		// calendar.add(Calendar.DATE, -1);// 前一天
		// beginDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
		// }
		List<RCompletedRelResp> list = reportSubmitCaliberService
				.findCompleteReportByIdAndTime(rCompletedRelParams.getUserId(), rCompletedRelParams.getCompletedDate());

		for (RCompletedRelResp rCompletedRelResp : list) {
			// 将对象转换成map
			Map<String, Object> rCompletedRelRespMap = Utils.objProps2Map(rCompletedRelResp, true);
			rCompletedRelRespMapList.add(rCompletedRelRespMap);
		}
		return new AppResp(rCompletedRelRespMapList, CodeDef.SUCCESS);
	}

	/**
	 * 获取当天之后的打点日期[remindTime,userId]
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取当天之后的打点日期", notes = "POST")
	@RequestMapping(value = "/getDatedot", method = RequestMethod.POST)
	public AppResp getDatedot(@RequestBody Map<String, Object> params) throws ParseException {
		RCompletedRelParams rCompletedRelParams = Utils.map2Obj(params, RCompletedRelParams.class);
		// List<String> list = getDateByToDay();//获取当日之后的一年日期
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();// 存日期 返回
		Set<String> result = new HashSet<String>();// 取交集用

		// 获取当月的打点数据 如果只打点当月之后的数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		// sdf.parse(str);
		List<String> dateList = getAllTheDateOftheMonth(sdf.parse(sdf.format(rCompletedRelParams.getRemindTime())));

		// 根据userId去用户订阅表里查询未完成的 报表id 集合
		Set<String> userSetList = rSubscribeRelRepo.findReportByUserIdAndIsComplete(rCompletedRelParams.getUserId());
		// 循环日期 根据当天日期去查询 报送口径表 相符合的报表id集合
		for (String date : dateList) {
			Set<String> setList = getReportList(date);// 计算出的reportId集合
			result.clear();
			result.addAll(userSetList);
			result.retainAll(setList);
			if (result != null && !result.isEmpty()) {// 如果存在并集 则将日期存进list
				Map<String, Object> rCompletedRelRespMap = new HashMap<String, Object>();
				rCompletedRelRespMap.put("date", date);
				allList.add(rCompletedRelRespMap);
			}
		}

		return new AppResp(allList, CodeDef.SUCCESS);
	}

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

	public Set<String> getReportList(String date) {
		SysDays day = sysDaysRepo.findOne(date);
		Set<String> reportId = new HashSet<>();
		Period periods = getSysDateService.getDate(date, "0");
		if (day.getIsWork() == 1) {
			Period period = getSysDateService.getDate(date, "1");
			reportId = reportSubmitCaliberRepo.findByWorkDatefromReportId(period.getWeek(), period.getTen(),
					period.getMonth(), period.getSeason(), period.getHalfYear(), period.getYear());
			Set<String> reportIdAdd = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(),
					periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear());
			for (String string : reportIdAdd) {
				reportId.add(string);
			}
		} else {
			reportId = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(), periods.getTen(),
					periods.getMonth(), periods.getSeason(), periods.getHalfYear(), periods.getYear());
		}

		return reportId;
	}
}
