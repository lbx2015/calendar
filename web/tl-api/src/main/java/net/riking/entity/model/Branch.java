package net.riking.entity.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * 组织机构表 Caixm
 */
public class Branch extends BaseModel {
	private static final long serialVersionUID = 1054057217896796149L;
	private String branchCode;
	private String branchName;
	private String branchEglishName;
	private String branchShortName;
	private String safeBranchCode; // 行内部银行代码
	private Branch parent;
	private String remark;

	private String amlBranchCode; // 大额生成报文所需要的Code
	private String safeCode; // 金融机构网点所在地区行政区划代码 6位

	private String fssCode;

	private String branchMaster;
	private String createUser;
	private String approveUser;
	private String branchType;
	private String branchAreaCode; // PBOC用到的地区代码，应该是7位

	private Set<Branch> children = new HashSet<Branch>(0);

	private String machinetaxnr;
	private String machinenr;
	private String taxDiskPassword;

	private String brcds;
	private String rpsCode;// 金融机构代码 用于 RcpMis (申报号码)
	private String bankOrgCode;// 银行机构代码 用于RCPMIS（报文名称 字段）
	private String mbtBranchCode;// 企业征信金融机构代码 11位

	// 以下字段用于vat模块
	private String openBank;// 开户银行
	private String openAccount;// 开户账号
	private String taxPayerNo;// 纳税人识别号
	private String contactTel;// 电话
	private String address;// 地址

	private String invoiceSpotName1;
	private String invoiceIp1;
	private String extenNumber1;
	private String invoiceSpotName2;
	private String invoiceIp2;
	private String extenNumber2;
	private String invoiceSpotName0;
	private String invoiceIp0;
	private String extenNumber0;

	private String taxServiceIp; // 税控服务IP
	private String machinePw; // 开票机密码
	private String interfaceType; // 接口类型
	private String specTicklimit; // 专票限额
	private String genTicklimit; // 普票限额
	private BigDecimal preRate; // 预征率

	/**
	 * mbt2.2
	 */
	private String contactName;// 联系人
	private String telephone;// 联系电话

	public String getInvoiceSpotName1() {
		return invoiceSpotName1;
	}

	public void setInvoiceSpotName1(String invoiceSpotName1) {
		this.invoiceSpotName1 = invoiceSpotName1;
	}

	public String getInvoiceIp1() {
		return invoiceIp1;
	}

	public void setInvoiceIp1(String invoiceIp1) {
		this.invoiceIp1 = invoiceIp1;
	}

	public String getExtenNumber1() {
		return extenNumber1;
	}

	public void setExtenNumber1(String extenNumber1) {
		this.extenNumber1 = extenNumber1;
	}

	public String getInvoiceSpotName2() {
		return invoiceSpotName2;
	}

	public void setInvoiceSpotName2(String invoiceSpotName2) {
		this.invoiceSpotName2 = invoiceSpotName2;
	}

	public String getInvoiceIp2() {
		return invoiceIp2;
	}

	public void setInvoiceIp2(String invoiceIp2) {
		this.invoiceIp2 = invoiceIp2;
	}

	public String getExtenNumber2() {
		return extenNumber2;
	}

	public void setExtenNumber2(String extenNumber2) {
		this.extenNumber2 = extenNumber2;
	}

	public String getInvoiceSpotName0() {
		return invoiceSpotName0;
	}

	public void setInvoiceSpotName0(String invoiceSpotName0) {
		this.invoiceSpotName0 = invoiceSpotName0;
	}

	public String getInvoiceIp0() {
		return invoiceIp0;
	}

	public void setInvoiceIp0(String invoiceIp0) {
		this.invoiceIp0 = invoiceIp0;
	}

	public String getExtenNumber0() {
		return extenNumber0;
	}

	public void setExtenNumber0(String extenNumber0) {
		this.extenNumber0 = extenNumber0;
	}

	public String getSafeCode() {
		return safeCode;
	}

	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSafeBranchCode() {
		return safeBranchCode;
	}

	public void setSafeBranchCode(String safeBranchCode) {
		this.safeBranchCode = safeBranchCode;
	}

	public Branch getParent() {
		return parent;
	}

	public void setParent(Branch parent) {
		this.parent = parent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBranchShortName() {
		return branchShortName;
	}

	public void setBranchShortName(String branchShortName) {
		this.branchShortName = branchShortName;
	}

	public Set<Branch> getChildren() {
		Set<Branch> bset = new HashSet<Branch>();
		bset.addAll(children);
		for (Branch b : children) {
			if (b.getChildren().size() > 0) {
				bset.addAll(b.getChildren());
			}
		}
		return bset;
	}

	public void setChildren(Set<Branch> children) {
		this.children = children;
	}

	public String getAmlBranchCode() {
		return amlBranchCode;
	}

	public void setAmlBranchCode(String amlBranchCode) {
		this.amlBranchCode = amlBranchCode;
	}

	public String getBranchEglishName() {
		return branchEglishName;
	}

	public void setBranchEglishName(String branchEglishName) {
		this.branchEglishName = branchEglishName;
	}

	public String getFssCode() {
		return fssCode;
	}

	public void setFssCode(String fssCode) {
		this.fssCode = fssCode;
	}

	public String getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(String branchMaster) {
		this.branchMaster = branchMaster;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

	public String getBranchAreaCode() {
		return branchAreaCode;
	}

	public void setBranchAreaCode(String branchAreaCode) {
		this.branchAreaCode = branchAreaCode;
	}

	public String getStrbankcodeName() {
		return this.branchAreaCode + "-" + this.branchName;
	}

	public String getBrcds() {
		return brcds;
	}

	public void setBrcds(String brcds) {
		this.brcds = brcds;
	}

	public String getMachinetaxnr() {
		return machinetaxnr;
	}

	public void setMachinetaxnr(String machinetaxnr) {
		this.machinetaxnr = machinetaxnr;
	}

	public String getMachinenr() {
		return machinenr;
	}

	public void setMachinenr(String machinenr) {
		this.machinenr = machinenr;
	}

	public String getTaxDiskPassword() {
		return taxDiskPassword;
	}

	public void setTaxDiskPassword(String taxDiskPassword) {
		this.taxDiskPassword = taxDiskPassword;
	}

	public void setRpsCode(String rpsCode) {
		this.rpsCode = rpsCode;
	}

	public String getRpsCode() {
		return rpsCode;
	}

	public String getBankOrgCode() {
		return bankOrgCode;
	}

	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getOpenAccount() {
		return openAccount;
	}

	public void setOpenAccount(String openAccount) {
		this.openAccount = openAccount;
	}

	public String getTaxPayerNo() {
		return taxPayerNo;
	}

	public void setTaxPayerNo(String taxPayerNo) {
		this.taxPayerNo = taxPayerNo;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTaxServiceIp(String taxServiceIp) {
		this.taxServiceIp = taxServiceIp;
	}

	public String getTaxServiceIp() {
		return taxServiceIp;
	}

	public String getMachinePw() {
		return machinePw;
	}

	public void setMachinePw(String machinePw) {
		this.machinePw = machinePw;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getMbtBranchCode() {
		return mbtBranchCode;
	}

	public void setMbtBranchCode(String mbtBranchCode) {
		this.mbtBranchCode = mbtBranchCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSpecTicklimit() {
		return specTicklimit;
	}

	public void setSpecTicklimit(String specTicklimit) {
		this.specTicklimit = specTicklimit;
	}

	public String getGenTicklimit() {
		return genTicklimit;
	}

	public void setGenTicklimit(String genTicklimit) {
		this.genTicklimit = genTicklimit;
	}

	public BigDecimal getPreRate() {
		return preRate;
	}

	public void setPreRate(BigDecimal preRate) {
		this.preRate = preRate;
	}
}
