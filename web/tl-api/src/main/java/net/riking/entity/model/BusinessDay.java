package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * @author Lucky.Liu on 2017/8/05.
 */
@Comment("日历")
@Entity
@Table(name = "v_businessDay")
public class BusinessDay extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5761845453911201779L;
	@Id
	@Column(name = "Businessday")
	private String businessDay;

	public String getBusinessDay() {
		return businessDay;
	}

	public void setBusinessDay(String businessDay) {
		this.businessDay = businessDay;
	}

}
