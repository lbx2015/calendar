package net.riking.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.UserLogRstHisRepo;
import net.riking.entity.model.ReportCodeCount;
import net.riking.service.SysDataService;
import net.riking.util.DateUtils;

/**
 * 数据统计
 * @author jc.tan 2018年1月5日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/dataCount")
public class dataCountController {
	@Autowired
	SysDataService sysDataService;

	@Autowired
	ReportRepo reportRepo;

	@Autowired
	UserLogRstHisRepo userLogRstHisRepo;

	@ApiOperation(value = "报表关注数统计", notes = "POST")
	@RequestMapping(value = "/reportFollowCount", method = RequestMethod.POST)
	public Resp reportFollowCount_() {
		List<ModelPropDict> modelPropDicts = sysDataService.getDicts("T_REPORT", "REPORT_TYPE");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ModelPropDict modelPropDict : modelPropDicts) {
			Map<String, Object> map = new HashMap<String, Object>();
			Integer count = reportRepo.countSubscribeNumByReportType(modelPropDict.getKe());
			map.put("key", modelPropDict.getKe());
			map.put("value", count);
			list.add(map);
		}

		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "报表类型关注数统计", notes = "POST")
	@RequestMapping(value = "/reportTypeFollowCount", method = RequestMethod.POST)
	public Resp reportTypeFollowCount_() {
		List<ModelPropDict> modelPropDicts = sysDataService.getDicts("T_REPORT", "REPORT_TYPE");
		List<Map<String, List<ReportCodeCount>>> list = new ArrayList<Map<String, List<ReportCodeCount>>>();
		for (ModelPropDict modelPropDict : modelPropDicts) {
			Map<String, List<ReportCodeCount>> map = new HashMap<String, List<ReportCodeCount>>();
			List<ReportCodeCount> reportCodeCounts = reportRepo.countSubscribeNumByReportCode(modelPropDict.getKe());
			map.put(modelPropDict.getKe(), reportCodeCounts);
			list.add(map);
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "用户活跃数统计", notes = "POST")
	@RequestMapping(value = "/userActiveNumCount", method = RequestMethod.POST)
	public Resp userActiveNumCount_(@RequestBody Map<String, Object> params) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		String frequency = params.get("frequency").toString();
		String date = params.get("date").toString();
		if (StringUtils.isNotBlank(date)) {
			cal.setTime(DateUtils.StringFormatMS(date, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(frequency)) {
			Integer fre = Integer.parseInt(frequency);
			switch (fre) {
				case Const.SYS_MONTH:
					countByMonthAndDataType(cal, map, Const.USER_OPT_LOGIN);
					break;
				case Const.SYS_SEASON:
					countBySeasonAndDataType(cal, map, Const.USER_OPT_LOGIN);
					break;
				case Const.SYS_HALF_YEAR:
					countByHalfYearAndDataType(cal, map, Const.USER_OPT_LOGIN);
					break;
				case Const.SYS_YEAR:
					countByYearAndDataType(cal, map, Const.USER_OPT_LOGIN);
					break;
				default:
					break;
			}
		}
		return new Resp(map, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "用户注册数统计", notes = "POST")
	@RequestMapping(value = "/userRegistNumCount", method = RequestMethod.POST)
	public Resp userRegistNumCount_(@RequestBody Map<String, Object> params) throws ParseException {
		Calendar cal = Calendar.getInstance();
		String frequency = params.get("frequency").toString();
		String date = params.get("date").toString();
		if (StringUtils.isNotBlank(date)) {
			cal.setTime(DateUtils.StringFormatMS(date, "yyyy-MM-dd"));
		}
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		if (StringUtils.isNotBlank(frequency)) {
			Integer fre = Integer.parseInt(frequency);
			switch (fre) {
				case Const.SYS_MONTH:
					countByMonthAndDataType(cal, map, Const.USER_OPT_REGIST);
					break;
				case Const.SYS_SEASON:
					countBySeasonAndDataType(cal, map, Const.USER_OPT_REGIST);
					break;
				case Const.SYS_HALF_YEAR:
					countByHalfYearAndDataType(cal, map, Const.USER_OPT_REGIST);
					break;
				case Const.SYS_YEAR:
					countByYearAndDataType(cal, map, Const.USER_OPT_REGIST);
					break;
				default:
					break;
			}
		}
		return new Resp(map, CodeDef.SUCCESS);
	}

	private void countByMonthAndDataType(Calendar cal, Map<String, Integer> map, Integer dataType)
			throws ParseException {
		int maxDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= maxDayOfMonth; i++) {
			cal.set(Calendar.DATE, i);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(cal.getTime());
			calEnd.set(Calendar.DATE, i + 1);
			Date begin = DateUtils.StringFormatMS(DateUtils.DateFormatMS(cal.getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
			Date end = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calEnd.getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
			Integer count = userLogRstHisRepo.countByTimeAndType(begin, end, dataType);

			String date = DateUtils.DateFormatMS(cal.getTime(), "MM月dd日");
			map.put(date, count);
		}
	}

	private void countByYearAndDataType(Calendar cal, Map<String, Integer> map, Integer dataType)
			throws ParseException {
		int month = cal.getActualMaximum(Calendar.MONTH) + 1;
		for (int i = 1; i <= month; i++) {
			cal.set(Calendar.MONTH, i - 1);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(cal.getTime());
			calEnd.set(Calendar.MONTH, i);
			Date begin = DateUtils.StringFormatMS(DateUtils.DateFormatMS(cal.getTime(), "yyyy-MM"), "yyyy-MM");
			Date end = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calEnd.getTime(), "yyyy-MM"), "yyyy-MM");
			Integer count = userLogRstHisRepo.countByTimeAndType(begin, end, dataType);

			String date = DateUtils.DateFormatMS(cal.getTime(), "MM月");
			map.put(date, count);
		}
	}

	private void countBySeasonAndDataType(Calendar cal, Map<String, Integer> map, int dataType) throws ParseException {
		int month = 4;
		for (int i = 1; i <= month; i++) {
			cal.set(Calendar.MONTH, i * 3 - 3);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(cal.getTime());
			calEnd.set(Calendar.MONTH, i * 3);
			Date begin = DateUtils.StringFormatMS(DateUtils.DateFormatMS(cal.getTime(), "yyyy-MM"), "yyyy-MM");
			Date end = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calEnd.getTime(), "yyyy-MM"), "yyyy-MM");
			Integer count = userLogRstHisRepo.countByTimeAndType(begin, end, dataType);

			map.put("第" + i + "季度", count);
		}
	}

	private void countByHalfYearAndDataType(Calendar cal, Map<String, Integer> map, int dataType)
			throws ParseException {
		int month = 2;
		for (int i = 1; i <= month; i++) {
			cal.set(Calendar.MONTH, i * 6 - 6);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(cal.getTime());
			calEnd.set(Calendar.MONTH, i * 6);
			Date begin = DateUtils.StringFormatMS(DateUtils.DateFormatMS(cal.getTime(), "yyyy-MM"), "yyyy-MM");
			Date end = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calEnd.getTime(), "yyyy-MM"), "yyyy-MM");
			Integer count = userLogRstHisRepo.countByTimeAndType(begin, end, dataType);

			if (i == 1) {
				map.put("前半年", count);
			} else {
				map.put("后半年", count);
			}

		}
	}

}
