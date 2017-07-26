package net.riking.entity.model;

import java.util.Date;


/**
 * 用户

 * @author Caixm
 *
 */
public class User extends BaseModel {
	private static final long serialVersionUID = -8599384769890240027L;

	/**
	 * 正常状态
	 */
	public static final int NORMAL = 0;

	/**
	 * 锁定状态
	 */
	public static final int LOCK = 1;

	/**
	 * 删除状态
	 */
	public static final int DELETE = 2;
	private String userName;
	private String name;
	private String password;
	private Integer status;     //用户状态
	private Integer dataPermission;  //数据权限
	private Branch branch;
	private String email;
	private String telephone;
	private Boolean isPwdEdit; 
	private String remark;	
	private String newPassword;
	
	private String isFirstPwd;//是否是第一次密码 1为第一次 ,0 或者null为不是
	
	private Date nextResetPwdDate;//下一次重置密码日期
	private Integer errNumber;//密码错误次数设置，达到后，用户无效。
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getDataPermission() {
		return dataPermission;
	}
	public void setDataPermission(Integer dataPermission) {
		this.dataPermission = dataPermission;
	}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Boolean getIsPwdEdit() {
		return isPwdEdit;
	}
	public void setIsPwdEdit(Boolean isPwdEdit) {
		this.isPwdEdit = isPwdEdit;
	}
	public String getIsFirstPwd() {
		if(null==this.isFirstPwd){
			return "0";
		}
		return isFirstPwd;
	}
	public void setIsFirstPwd(String isFirstPwd) {
		this.isFirstPwd = isFirstPwd;
	}
	public Date getNextResetPwdDate() {
		return nextResetPwdDate;
	}
	public void setNextResetPwdDate(Date nextResetPwdDate) {
		this.nextResetPwdDate = nextResetPwdDate;
	}
	public Integer getErrNumber() {
		return errNumber;
	}
	public void setErrNumber(Integer errNumber) {
		this.errNumber = errNumber;
	}
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}