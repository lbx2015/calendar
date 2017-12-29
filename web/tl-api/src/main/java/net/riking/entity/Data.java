package net.riking.entity;

/**
 * layui富文本返回类
 * @author jc.tan 2017年12月27日
 * @see
 * @since 1.0
 */
public class Data {
	private String src;

	private String title;

	public Data() {
		super();
	}

	public Data(String src, String title) {
		super();
		this.src = src;
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
