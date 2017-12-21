package net.riking.web.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import net.riking.config.Const;
import net.riking.dao.repo.RSubscribeRelRepo;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Period;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.SysDays;
import net.riking.entity.params.RCompletedRelParams;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDateService;
import net.riking.service.impl.SysDateServiceImpl;

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
	ReportService reportService;

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
	public AppResp save(@RequestBody RCompletedRelParams rCompletedRelParams) {
		ReportCompletedRel completedRel = new ReportCompletedRel();
		completedRel.setReportId(rCompletedRelParams.getReportId());
		completedRel.setUserId(rCompletedRelParams.getUserId());
		completedRel.setCompletedDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		reportCompletedRelRepo.save(completedRel);
		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
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
