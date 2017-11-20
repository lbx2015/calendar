package net.riking.entity.model;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月20日 下午2:06:56
 * @used 话题下的问题返回给移动端的实体对象
 */
public class TopicQuestionResult {

	private String questionId;//问题id
	
	private String topicId;//话题id
	
	private String questionName;//问题名称
	
	private String topicName;//话题名称
	
	private String answerName;//回答的内容
	
	private String answerComments;//回答的评论数
	
	private String answerPraise;//回答的点赞数 
	
	private String questionImage;//问题图片路径
	
	public TopicQuestionResult(String questionId,String topicId,String questionName,String topicName,String answerName,String answerComments,String answerPraise,String questionImage){
		this.questionId = questionId;
		this.topicId = topicId;
		this.questionName = questionName;
		this.topicName = topicName;
		this.answerName = answerName;
		this.answerComments = answerComments;
		this.answerPraise = answerPraise;
		this.questionImage = questionImage;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public String getAnswerComments() {
		return answerComments;
	}

	public void setAnswerComments(String answerComments) {
		this.answerComments = answerComments;
	}

	public String getAnswerPraise() {
		return answerPraise;
	}

	public void setAnswerPraise(String answerPraise) {
		this.answerPraise = answerPraise;
	}

	public String getQuestionImage() {
		return questionImage;
	}

	public void setQuestionImage(String questionImage) {
		this.questionImage = questionImage;
	}
	
}
