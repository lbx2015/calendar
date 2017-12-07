package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseAuditProp;

import java.util.Date;


/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("回答信息 表")
//@Entity
//@Table(name = "t_question_answer")
public class QuestionAnswer extends BaseAuditProp {

	public String questionAnswerId;

//	@Comment("物理主键")
//	@Id
//	@Column(name = "id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
//	@JsonProperty("questionAnswerId")
//	public String id;

//	@Comment("回答人主键: fk t_app_user")
//	@Column(name = "user_id", nullable = false)
	public String userId;

//	@Comment("问题主键: fk t_topic_question")
//	@Column(name = "question_id", nullable = false)
	public String questionId;

//	@Comment("回答内容")
//	@Lob
//	@Column(name = "content", nullable = false)
	public String content;

	// @Comment("用户收藏数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="collect_num",insertable=false, nullable=false)
	// public Integer collectNum;
	//

	// 用户名
//	@Transient
	public String userName;

	// 用户评论数
//	@Transient
	public int commentNum;

	// 用户点赞数
//	@Transient
	public int agreeNum;

	// 用户头像路径
//	@Transient
	public String photoUrl;

	// 问题的标题
//	@Transient
	public String title;

	// 经验值
//	@Transient
	public int experience;

	// 是否已点赞（0-未点赞；1-已点赞）
//	@Transient
	public int isAgree;

	// 是否已收藏（0-未收藏；1-已收藏）
//	@Transient
	public int isCollect;

}
