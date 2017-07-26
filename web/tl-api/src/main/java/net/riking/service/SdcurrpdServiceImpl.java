package net.riking.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.cache.RCache;
import net.riking.entity.model.Sdcurrpd;
import net.riking.service.repo.SdcurrpdRepo;

/**
 * Created by bing.xun on 2017/5/10.
 */
@Service("sdcurrpdServiceImpl")
public class SdcurrpdServiceImpl {
	private static RCache<Map<String, Sdcurrpd>> cache = new RCache<Map<String, Sdcurrpd>>(60*3);
	@Autowired
	SdcurrpdRepo sdcurrpdRepo;

	public Map<String, Sdcurrpd> Calculation(Date time) {
		List<Sdcurrpd> sdcurrpdList = sdcurrpdRepo.findByRateDateBefor(time);
		Map<String, Sdcurrpd> sdcurrpd = new HashMap<>();
		for (int i = 0; i < sdcurrpdList.size(); i++) {
			if (null != sdcurrpd.get(sdcurrpdList.get(i).getCurrency())) {
				if (sdcurrpd.get(sdcurrpdList.get(i).getCurrency()).getRateDate()
						.before(sdcurrpdList.get(i).getRateDate())) {
					sdcurrpd.put(sdcurrpdList.get(i).getCurrency(), sdcurrpdList.get(i));
				}
			} else {
				sdcurrpd.put(sdcurrpdList.get(i).getCurrency(), sdcurrpdList.get(i));
			}

		}
		return sdcurrpd;
	}

	public void put(Date time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String timeStr = sf.format(time);
		Map<String, Sdcurrpd> sdcurrpd = this.Calculation(time);
		cache.put(timeStr,sdcurrpd);
	}

	public Map<String, Sdcurrpd> getSdcurrpd(Date time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String timeStr = sf.format(time);
		if (cache.get(timeStr) != null) {
			return cache.get(timeStr);
		} else {
			this.put(time);
			return cache.get(timeStr);
		}
	}
}
