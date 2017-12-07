package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * App版本获取接口参数
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:39:26
 * @used TODO
 */
public class IndustryParams extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5561392751645316865L;

	// 行业id
	private String industryId;

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

}
