package net.riking.task;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.service.ContactsInviteService;
import net.riking.service.NewsService;
import net.riking.service.QACommentService;
import net.riking.service.QAInviteService;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.spring.SpringBeanUtil;
import net.sf.json.JSONObject;

/**
 * 系统操作监听
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
@Component("mqSysOptListener")
public class MQSysOptListener implements MessageListener {
	private static final Logger logger = LogManager.getLogger("mqSysOptListener");

	QAInviteService qaInviteService;

	QAInviteRepo qAInviteRepo;

	QAnswerService qAnswerService;

	QAnswerRelRepo qAnswerRelRepo;

	NewsRelRepo newsRelRepo;

	TQuestionRelRepo tQuestionRelRepo;

	TopicRelRepo topicRelRepo;

	NewsService newsService;

	TopicService topicService;

	QACommentService qaCommentService;

	TQuestionService tQuestionService;

	ContactsInviteService contactsInviteService;

	@Override
	public void onMessage(Message message) {
		qaInviteService = (QAInviteService) SpringBeanUtil.getInstance().getBean("qaInviteService");
		qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("QAnswerService");
		newsService = (NewsService) SpringBeanUtil.getInstance().getBean("newsService");
		topicService = (TopicService) SpringBeanUtil.getInstance().getBean("topicService");
		qaCommentService = (QACommentService) SpringBeanUtil.getInstance().getBean("qaCommentService");
		tQuestionService = (TQuestionService) SpringBeanUtil.getInstance().getBean("tQuestionService");
		contactsInviteService = (ContactsInviteService) SpringBeanUtil.getInstance().getBean("contactsInviteService");
		TextMessage txtMessage = (TextMessage) message;
		try {
			JSONObject jsonobject = JSONObject.fromObject(txtMessage.getText());
			MQOptCommon optCommon = (MQOptCommon) JSONObject.toBean(jsonobject, MQOptCommon.class);
			switch (optCommon.getMqOptType()) {
				case Const.MQ_OPT_ANSWERINVITE:
					// 保存新的一条邀请记录
					qaInviteService.saveQaInvite(optCommon);
					break;
				case Const.MQ_OPT_QA_AGREEORCOLLECT:
					// 问题回答点赞或收藏
					qAnswerService.QaAgreeCollect(optCommon);
					break;
				case Const.MQ_OPT_NEW_COLLECT:
					// 资讯收藏
					newsService.newsCollect(optCommon);
					break;
				case Const.MQ_OPT_SHIELD_QUEST:
					// 屏蔽问题
					topicService.shield(optCommon);
					break;
				case Const.MQ_OPT_FOLLOW:
					// 问题，话题，用户的关注
					tQuestionService.follow(optCommon);
					break;
				case Const.MQ_OPT_COMMENT_AGREE:
					// 评论点赞
					qaCommentService.commentAgree(optCommon);
					break;
				case Const.MQ_OPT_CONTACTS_INVITE:
					// 通讯录的邀请
					contactsInviteService.contactsInvite(optCommon);
					break;
				case Const.MQ_OPT_QANSWER_COMMENT:
					// 问题回答的评论
					qAnswerService.qACommentPub(optCommon);
					break;
				case Const.MQ_OPT_NEWS_COMMENT:
					// 资讯评论发布
					newsService.newsCommentPub(optCommon);
				case Const.MQ_OPT_COMMENT_REPLY:
					// 评论的回复和回复的回复
					qAnswerService.commentReply(optCommon);
					break;
				default:
					break;
			}
			logger.info("get message " + txtMessage.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
