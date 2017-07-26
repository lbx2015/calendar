package net.riking.service.repo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.entity.model.BusinessDay;
import net.riking.service.repo.BusinessDayRepo;

/**
 * Created by bing.xun on 2017/5/15.
 */
@Service("businessDayRepo")
public class BusinessDayRepoImpl {

	/*
	 * @PersistenceContext private EntityManager entityManager;
	 */

	@Autowired
	BusinessDayRepo businessDayRepo;

	// 时间T-n T为date ，n为count
	public List<Date> getBusinessDayList(Date date, Integer count) throws Exception {
		/*
		 * Query q= entityManager.createNativeQuery(
		 * "SELECT BusinessDay FROM V_BusinessDay"); List<String> businessDays =
		 * q.getResultList();
		 */
		List<BusinessDay> businessDays = businessDayRepo.findAll();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Collections.sort(businessDays, new Comparator<BusinessDay>() {
			public int compare(BusinessDay o1, BusinessDay o2) {
				try {
					Date tempDate1 = formatter.parse(o1.getBusinessDay());
					Date tempDate2 = formatter.parse(o2.getBusinessDay());
					if (tempDate1.after(tempDate2)) {
						return 1;
					} else {
						return -1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return -1;
			}
		});

		Date tempDate;
		Integer index = 0;
		for (BusinessDay businessDay : businessDays) {
			tempDate = formatter.parse(businessDay.getBusinessDay());
			index++;
			if (DateUtils.isSameDay(tempDate, date)) {
				break;
			}
			if (tempDate.after(date)) {
				index--;
				break;
			}
		}
		List<Date> list = new ArrayList<Date>();
		for (int i = index - count; i < index; i++) {
			if (i > 0) {
				list.add(formatter.parse(businessDays.get(i).getBusinessDay()));
			}
		}
		return list;
	}
}
