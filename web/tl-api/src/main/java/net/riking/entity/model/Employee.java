package net.riking.entity.model;

import java.util.Date;

import javax.persistence.*;

import net.riking.core.entity.model.BaseFlowJob;

@Entity
@Table(name = "T_EMPLOYEE")
public class Employee extends BaseFlowJob {
	private static final long serialVersionUID = 6801241458118857608L;
	@Id
	@GeneratedValue
	private Long id;
	private Integer age;
	private String company;

	private Date birthday;
	private Byte sex;
	private String description;
	private String address;
	private String province;
	private String country;
	private String hobby;
	private String email;
	private Integer rating;
	@Column(name = "current_FAMI")
	private Integer currentFamilyAverageMonthlyIncome;
	@Column(name = "family_year_income")
	private Integer familyYearIncome;
	@Column(name = "family_situation")
	private String familySituation;

	@Transient
	private EmployeeDesc desc;

	private String demo1;
	private String demo2;
	private String demo3;
	private String demo4;
	private String demo5;
	private String demo6;
	private String demo7;
	private String demo8;
	private String demo9;

	@Column(name = "column_definition")
	private String columnDefinition;


	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getCurrentFamilyAverageMonthlyIncome() {
		return currentFamilyAverageMonthlyIncome;
	}

	public void setCurrentFamilyAverageMonthlyIncome(Integer currentFamilyAverageMonthlyIncome) {
		this.currentFamilyAverageMonthlyIncome = currentFamilyAverageMonthlyIncome;
	}

	public Integer getFamilyYearIncome() {
		return familyYearIncome;
	}

	public void setFamilyYearIncome(Integer familyYearIncome) {
		this.familyYearIncome = familyYearIncome;
	}

	public String getFamilySituation() {
		return familySituation;
	}

	public void setFamilySituation(String familySituation) {
		this.familySituation = familySituation;
	}

	public String getDemo1() {
		return demo1;
	}

	public void setDemo1(String demo1) {
		this.demo1 = demo1;
	}

	public String getDemo2() {
		return demo2;
	}

	public void setDemo2(String demo2) {
		this.demo2 = demo2;
	}

	public String getDemo3() {
		return demo3;
	}

	public void setDemo3(String demo3) {
		this.demo3 = demo3;
	}

	public String getDemo4() {
		return demo4;
	}

	public void setDemo4(String demo4) {
		this.demo4 = demo4;
	}

	public String getDemo5() {
		return demo5;
	}

	public void setDemo5(String demo5) {
		this.demo5 = demo5;
	}

	public String getDemo6() {
		return demo6;
	}

	public void setDemo6(String demo6) {
		this.demo6 = demo6;
	}

	public String getDemo7() {
		return demo7;
	}

	public void setDemo7(String demo7) {
		this.demo7 = demo7;
	}

	public String getDemo8() {
		return demo8;
	}

	public void setDemo8(String demo8) {
		this.demo8 = demo8;
	}

	public String getDemo9() {
		return demo9;
	}

	public void setDemo9(String demo9) {
		this.demo9 = demo9;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// @PrePersist
	// public void prePersist() {
	// // setStartState("PRE_RECROD");
	// // 如果调用PrePersist覆盖了父类。必须手动调用super.genJob()方法
	// super.genJob();
	// }


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public EmployeeDesc getDesc() {
		return desc;
	}


	public void setDesc(EmployeeDesc desc) {
		this.desc = desc;
	}
}
