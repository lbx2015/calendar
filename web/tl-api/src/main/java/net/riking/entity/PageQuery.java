package net.riking.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageQuery extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7460526310221003824L;
	private Integer pindex = 0; // 页数
	private Integer pcount = 10;// 每页条数
	private String sort; // like: id_asc|name_desc

	public Integer getPindex() {
		return pindex;
	}

	public void setPindex(Integer pindex) {
		this.pindex = pindex;
	}

	public Integer getPcount() {
		return pcount;
	}

	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	public String getSort() {
		return sort;
	}

	@Transient
	@JsonIgnore
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
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
