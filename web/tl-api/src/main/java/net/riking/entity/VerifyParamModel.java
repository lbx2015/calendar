package net.riking.entity;

import java.util.Set;

/**
 * 审批返回对象
 * @author jc.tan 2017年12月27日
 * @see
 * @since 1.0
 */
public class VerifyParamModel {
	private Set<String> ids;

	private String event;

	public VerifyParamModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<String> getIds() {
		return ids;
	}

	public void setIds(Set<String> ids) {
		this.ids = ids;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
