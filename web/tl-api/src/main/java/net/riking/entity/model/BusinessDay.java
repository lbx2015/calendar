package net.riking.entity.model;

import net.riking.core.annos.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Lucky.Liu on 2017/8/05.
 */
@Comment("日历")
@Entity
@Table(name = "v_businessDay")
public class BusinessDay  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="Businessday")
    private String businessDay;

	public String getBusinessDay() {
		return businessDay;
	}

	public void setBusinessDay(String businessDay) {
		this.businessDay = businessDay;
	}
   
}
