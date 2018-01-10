package net.riking.entity.model;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * web
 * @author jc.tan 2018年1月8日
 * @see
 * @since 1.0
 */
public class ReportCodeCount extends BaseEntity {

	@Comment("报表代码")
	private String code;

	@Comment("报表代码订阅数")
	private long count;

	public ReportCodeCount(String code, long count) {
		super();
		this.code = code;
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
