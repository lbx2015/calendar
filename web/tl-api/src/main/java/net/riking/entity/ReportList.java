package net.riking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

@Comment("报表信息")
@Entity
@Table(name = "T_Report_List")
public class ReportList {

	@Id
	@Column(name = "Id", length = 36)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
}
