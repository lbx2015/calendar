package net.riking.entity.model;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月16日 上午10:28:19
 * @used 首页加载设置
 */
public class LoadSetting {

	private Long id;
	
	private String moduleName;//模块名称
	
	private String moduleCode;//code
	
	private String type;//模块类别 1.好友关注的话题 2.好友回答的问题 3.好友点赞的问题 4.好友点赞的回答 5.好友关注的回答 6.大众的问题  7.大众的回答 8.可能感兴趣的话题9.可能感兴趣的问题 10.可能感兴趣的人脉 11.广告
	
	private String isFlag;//是否有效
	
	private Integer sequence;//排序

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
