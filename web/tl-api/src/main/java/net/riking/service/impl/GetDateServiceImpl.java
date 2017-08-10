package net.riking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.riking.service.getDateService;

@Service("getDateService")
public class GetDateServiceImpl implements getDateService {
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

}
