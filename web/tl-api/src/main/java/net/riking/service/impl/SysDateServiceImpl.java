package net.riking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.model.Period;
import net.riking.entity.model.SysDays;
import net.riking.service.SysDateService;

@Service("sysDateService")
public class SysDateServiceImpl implements SysDateService {

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Override
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	@Override
	public Map<String, Set<String>> getMounthWeek(String date) {
		Map<String, Set<String>> map = new HashMap<>();
		Set<String> day = getDaysByYearMonth(date);
		for (String oneDate : day) {
			String weekDay = getDayOfWeekByDate(oneDate);
			if (!map.containsKey(weekDay)) {
				Set<String> set = new HashSet<>();
				set.add(oneDate);
				map.put(weekDay, set);
			} else {
				Set<String> set = map.get(weekDay);
				set.add(oneDate);
				map.put(weekDay, set);
			}
		}
		return map;
	}

	public Set<String> getDaysByYearMonth(String date) {
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		Set<String> set = new HashSet<>();
		for (int i = 0; i < maxDate; i++) {
			String oneDate;
			int n = i + 1;
			if (i < 9) {
				oneDate = "" + year + month + "0" + n;
			} else {
				oneDate = "" + year + month + n;
			}
			set.add(oneDate);
		}
		return set;
	}

	/**
	 * 根据日期 找到对应日期的 星期
	 */
	public static String getDayOfWeekByDate(String date) {
		String str = null;
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
			Date myDate = myFormatter.parse(date);
			SimpleDateFormat formatter = new SimpleDateFormat("E");
			str = formatter.format(myDate);
			System.err.println(str);
			switch (str) {
				case "星期一":
					str = "1";
					break;
				case "星期二":
					str = "2";
					break;
				case "星期三":
					str = "3";
					break;
				case "星期四":
					str = "4";
					break;
				case "星期五":
					str = "5";
					break;
				case "星期六":
					str = "6";
					break;
				case "星期日":
					str = "7";
					break;
			}
		} catch (Exception e) {
			System.out.println("错误!");
		}
		return str;
	}

	public Period getDate(String date, String type) {
		SysDays day = sysDaysRepo.findOne(date);
		Integer tenWorkDay;
		Integer tenAllDay;
		if (date.substring(6, 7).equals("0")) {
			tenWorkDay = sysDaysRepo.findByDate(date.substring(0, 6) + "01", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "00");
		} else if (date.substring(6, 7).equals("1")) {
			tenWorkDay = sysDaysRepo.findByDate(date.substring(0, 6) + "11", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "10");
		} else {
			tenWorkDay = sysDaysRepo.findByDate(date.substring(0, 6) + "21", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "20");
		}
		Integer monthWorkDay = sysDaysRepo.findByDate(date.substring(0, 6) + "01", date);
		Integer monthAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "00");
		Integer seasonWorkDay;
		Integer seasonAllDay;
		if (3 < Integer.valueOf(date.substring(4, 6)) && Integer.valueOf(date.substring(4, 6)) < 7) {
			seasonWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0401", date);
			seasonAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0401", date);
		} else if (6 < Integer.valueOf(date.substring(4, 6)) && Integer.valueOf(date.substring(4, 6)) < 10) {
			seasonWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0701", date);
			seasonAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0701", date);
		} else if (Integer.valueOf(date.substring(4, 6)) > 9) {
			seasonWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "1001", date);
			seasonAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "1001", date);
		} else {
			seasonWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0101", date);
			seasonAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		}
		Integer halfYearWorkDay;
		Integer halfYearAllDay;
		if (Integer.valueOf(date.substring(4, 6)) < 7) {
			halfYearWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0101", date);
			halfYearAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		} else {
			halfYearWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0701", date);
			halfYearAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0701", date);
		}
		Integer yearWorkDay = sysDaysRepo.findByDate(date.substring(0, 4) + "0101", date);
		Integer yearAllDay = sysDaysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		Period period;
		if ("1".equals(type)) {
			period = new Period(Integer.valueOf(day.getWeekday()), tenWorkDay, monthWorkDay, seasonWorkDay,
					halfYearWorkDay, yearWorkDay);
		} else {
			period = new Period(Integer.valueOf(day.getWeekday()), tenAllDay, monthAllDay, seasonAllDay, halfYearAllDay,
					yearAllDay);
		}
		return period;

	}

}
