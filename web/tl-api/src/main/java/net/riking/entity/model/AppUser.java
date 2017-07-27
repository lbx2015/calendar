package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_app_user")
public class AppUser extends BaseEntity {
//用户表
	@Id
	@Column(name = "Id", length = 36)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("用户名称")
	@Column(name = "name", length = 32)
	private String name;
	
	@Comment("用户性别")
	@Column(name = "sex")
	private Integer sex;
	
	@Comment("用户生日")
	@Temporal(TemporalType.DATE)
	@Column(name = "birthday")
	private Date birthday;
	
	@Comment("用户邮箱")
	@Column(name = "email",length = 32)
	private String email;
	
	@Comment("用户电话")
	@Column(name = "telephone",length = 32)
	private String telephone;
	
	@Comment("用户地址")
	@Column(name = "address",length = 32)
	private String address;
	
	@Comment("用户登录密码")
	@Column(name = "passWord",length = 64)
	private String passWord;
	
	//0-禁用    1-启用
	@Comment("用户状态")
	@Column(name = "status")
	private Integer status;
	
	@Comment("备注信息")
	@Column(name = "remark", length = 500)
	private String remark;
	
	@Comment("部门ID")
	@Column(name = "dept_id")
	private Integer deptId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
