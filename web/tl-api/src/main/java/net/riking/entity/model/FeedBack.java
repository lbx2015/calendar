package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("反馈表")
@Entity
@Table(name = "t_feedback")
public class FeedBack extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5622478704716507201L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("feedBackId")
	private String id;

	@Comment("反馈内容")
	@Column(name = "content")
	private String content;

	@Comment("多个图片名称，以','逗号分隔")
	@Column(name = "imgs")
	private String imgs;

	@Comment("是否采纳:0-未操作；1-未采纳；2-采纳")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "accept")
	private Integer accept;

	@Comment("反馈用户名")
	@Transient
	private String userName;

	@Comment("反馈用户电话")
	@Transient
	private String userPhone;

	public FeedBack(String content, String imgs, String createBy) {
		super();
		this.content = content;
		this.imgs = imgs;
		this.setCreatedBy(createBy);
		this.setModifiedBy(createBy);
	}

	public FeedBack() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public Integer getAccept() {
		return accept;
	}

	public void setAccept(Integer accept) {
		this.accept = accept;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

}
