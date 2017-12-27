package com.riking.calendar.pojo.base;

import com.riking.calendar.pojo.params.BaseParams;
import com.riking.calendar.util.ZPreference;

public class PageQuery{
    // 用户id
    public String userId= ZPreference.getUserId();
    public Integer pindex; // 页数
    public Integer pcount;// 每页条数
    public String sort; // like: id_asc|name_desc
/*
    public Sort getSortObj() {
		if (StringUtils.isNotEmpty(sort)) {
			String[] arr = sort.split("\\|");
			List<Order> orders = new ArrayList<>();
			for (String item : arr) {
				if (StringUtils.isNotEmpty(item)) {
					String[] its = item.split("_");
					orders.add(new Order(
							Direction.valueOf(its[1].toUpperCase()), its[0]));
				}
			}
			return new Sort(orders);
		}
		return null;
	}*/
}
