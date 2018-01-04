package net.riking.task;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SysNoticeRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.SysNotice;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.service.ContactsInviteService;
import net.riking.service.NewsService;
import net.riking.service.QACommentService;
import net.riking.service.QAInviteService;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.spring.SpringBeanUtil;
import net.riking.util.JdpushUtil;
import net.riking.util.SmsUtil;
import net.sf.json.JSONObject;

/**
 * 用户交互监听
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
@Component("mqSysOptListener")
public class MQSysOptListener implements MessageListener {
	private static final Logger logger = LogManager.getLogger("mqSysOptListener");

	private QAInviteService qaInviteService;
	private QAnswerService qAnswerService;
	private QuestionAnswerRepo questionAnswerRepo;
	private NewsService newsService;
	private TopicService topicService;
	private QACommentService qaCommentService;
	private QACommentRepo qaCommentRepo;
	private TQuestionService tQuestionService;
	private TopicQuestionRepo topicQuestionRepo;
	private ContactsInviteService contactsInviteService;
	private SysNoticeRepo sysNoticeRepo;
	private AppUserRepo appUserRepo;
	private AppUserDetailRepo appUserDetailRepo;
	
	@Override
	public void onMessage(Message message) {
		sysNoticeRepo = (SysNoticeRepo) SpringBeanUtil.getInstance().getBean("SysNoticeRepo");
		appUserRepo = (AppUserRepo) SpringBeanUtil.getInstance().getBean("AppUserRepo");
		appUserDetailRepo = (AppUserDetailRepo) SpringBeanUtil.getInstance().getBean("AppUserDetailRepo");
		TextMessage txtMessage = (TextMessage) message;
		try {
			JSONObject jsonobject = JSONObject.fromObject(txtMessage.getText());
			MQOptCommon optCommon = (MQOptCommon) JSONObject.toBean(jsonobject, MQOptCommon.class);
			SysNotice sysNotice = null;
			TopicQuestion topicQuestion = null;
			QAComment qaComment = null;
			QuestionAnswer questionAnswer = null;
			String title = null;
			String content = null;
			AppUser appUser = null;
			AppUserDetail appUserDetail = null;
			boolean isRn = false;
			Integer dataType = null;
			Jdpush jdpush = null;
			//是否发送极光
			boolean isSendJdPush = false;
			
			String notificationTitle = "";
			String msgTitle = "";
			String msgContent = "";
			String phoneDeviceid = "";
			
			switch (optCommon.getMqOptType()) {
				case Const.MQ_OPT_ANSWERINVITE://邀请回答的邀请 
					qaInviteService = (QAInviteService) SpringBeanUtil.getInstance().getBean("qaInviteService");
					topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("TopicQuestionRepo");
					appUser = appUserRepo.findOne(optCommon.getUserId());
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					topicQuestion = topicQuestionRepo.findOne(optCommon.getTqId());
					qaInviteService.saveQaInvite(optCommon);
					
					sysNotice = new SysNotice();
					title = appUser.getUserName() + " 邀请你回答问题";
					content = appUser.getUserName() + " 邀请你回答问题 " + topicQuestion.getTitle();
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(optCommon.getAttentObjId());
					sysNotice.setDataType(Const.NOTICE_OPT_ANSWERINVITE);
					sysNoticeRepo.save(sysNotice);
					
					isSendJdPush = true;
					notificationTitle = title;
					msgContent = content;
					phoneDeviceid = appUserDetail.getPhoneDeviceid();
					break;
				case Const.MQ_OPT_QA_AGREEOR_COLLECT://问题回答点赞或收藏 
					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("QAnswerService");
					questionAnswerRepo = (QuestionAnswerRepo) SpringBeanUtil.getInstance().getBean("QuestionAnswerRepo");
					
					isRn = qAnswerService.qaAgreeCollect(optCommon);
					if(isRn){
						topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("TopicQuestionRepo");
						appUser = appUserRepo.findOne(optCommon.getUserId());
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						questionAnswer = questionAnswerRepo.findOne(optCommon.getQuestAnswerId());
						topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
						
						sysNotice = new SysNotice();
						String optStr = optCommon.getOptType().intValue() == 1 ? " 赞了 " : " 收藏了";
						dataType = optCommon.getOptType().intValue() == 1 ? Const.NOTICE_OPT_QA_AGREEOR : Const.NOTICE_OPT_QA_COLLECT;
						title = appUser.getUserName() + optStr + "你的回答";
						content = appUser.getUserName() + optStr + "你的回答 " + questionAnswer.getTitle();
						sysNotice.setTitle(title);
						sysNotice.setContent(content);
						sysNotice.setNoticeUserId(topicQuestion.getUserId());
						sysNotice.setDataType(dataType);
						sysNoticeRepo.save(sysNotice);
						
						isSendJdPush = true;
						notificationTitle = title;
						msgContent = content;
						phoneDeviceid = appUserDetail.getPhoneDeviceid();
						
					}
					break;
				case Const.MQ_OPT_NEW_COLLECT://资讯的收藏
					newsService = (NewsService) SpringBeanUtil.getInstance().getBean("newsService");
					newsService.newsCollect(optCommon);
					break;
				case Const.MQ_OPT_SHIELD_QUEST://问题的屏蔽
					topicService = (TopicService) SpringBeanUtil.getInstance().getBean("topicService");
					topicService.shield(optCommon);
					break;
				case Const.MQ_OPT_FOLLOW:// 问题，话题，用户的关注
					tQuestionService = (TQuestionService) SpringBeanUtil.getInstance().getBean("tQuestionService");
					topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("TopicQuestionRepo");
					isRn = tQuestionService.follow(optCommon);
					if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_2){
						//关注话题不推送信息
						return;
					}
					if(isRn){
						appUser = appUserRepo.findOne(optCommon.getUserId());
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						sysNotice = new SysNotice();
						if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_1){//问题 
							topicQuestion = topicQuestionRepo.findOne(optCommon.getAttentObjId());
							title = appUser.getUserName() + " 关注了你的问题";
							content = appUser.getUserName() + " 关注了你的问题 " + topicQuestion.getTitle();
							dataType = Const.NOTICE_OPT_QUESTION_FOLLOW;
						}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_3){//用户
							title = appUser.getUserName() + " 关注了你";
							content = appUser.getUserName() + " 关注了你";
							dataType = Const.NOTICE_OPT_USER_FOLLOW;
						}else{
							return;
						}
						
						sysNotice.setTitle(title);
						sysNotice.setContent(content);
						sysNotice.setNoticeUserId(topicQuestion.getUserId());
						sysNotice.setDataType(dataType);
						sysNoticeRepo.save(sysNotice);
						
						isSendJdPush = true;
						notificationTitle = title;
						msgContent = content;
						phoneDeviceid = appUserDetail.getPhoneDeviceid();
						
					}
					break;
				case Const.MQ_OPT_COMMENT_AGREE:// 评论点赞
					qaCommentRepo = (QACommentRepo) SpringBeanUtil.getInstance().getBean("QACommentRepo");
					isRn = qaCommentService.commentAgree(optCommon);
					
					if(isRn){
						appUser = appUserRepo.findOne(optCommon.getUserId());
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						qaComment = qaCommentRepo.findOne(optCommon.getCommentId());
						if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_ANSWER){//回答类
							title = appUser.getUserName() + " 赞了你的回答评论";
							content = appUser.getUserName() + " 赞了你的回答评论 " + qaComment.getContent();
						}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_NEWS){//资讯类
							title = appUser.getUserName() + " 赞了你的资讯评论";
							content = appUser.getUserName() + " 赞了你的资讯评论 " + qaComment.getContent();
						}
						
						sysNotice = new SysNotice();
						sysNotice.setTitle(title);
						sysNotice.setContent(content);
						sysNotice.setNoticeUserId(qaComment.getUserId());
						sysNotice.setDataType(Const.NOTICE_OPT_COMMENT_AGREE);
						sysNoticeRepo.save(sysNotice);
						
						isSendJdPush = true;
						notificationTitle = title;
						msgContent = content;
						phoneDeviceid = appUserDetail.getPhoneDeviceid();
						
					}
					break;
				case Const.MQ_OPT_CONTACTS_INVITE:// 通讯录的邀请
					contactsInviteService = (ContactsInviteService) SpringBeanUtil.getInstance().getBean("contactsInviteService");
					contactsInviteService.contactsInvite(optCommon);
					//需要发短信
//					SmsUtil smsUtil = new SmsUtil();
					
					break;
				case Const.MQ_OPT_QANSWER_COMMENT:// 问题回答的评论
					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("QAnswerService");
					questionAnswerRepo = (QuestionAnswerRepo) SpringBeanUtil.getInstance().getBean("QuestionAnswerRepo");
					questionAnswer = questionAnswerRepo.findOne(optCommon.getQuestAnswerId());
					qAnswerService.qACommentPub(optCommon);
					appUser = appUserRepo.findOne(optCommon.getUserId());
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					title = appUser.getUserName() + " 评论了你的回答";
					content = appUser.getUserName() + " 评论了你的回答 " + optCommon.getContent();
					
					sysNotice = new SysNotice();
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(questionAnswer.getUserId());
					sysNotice.setDataType(Const.NOTICE_OPT_QANSWER_COMMENT);
					sysNoticeRepo.save(sysNotice);
					
					isSendJdPush = true;
					notificationTitle = title;
					msgContent = content;
					phoneDeviceid = appUserDetail.getPhoneDeviceid();
					
					break;
				case Const.MQ_OPT_NEWS_COMMENT:// 资讯评论发布
					newsService = (NewsService) SpringBeanUtil.getInstance().getBean("newsService");
					newsService.newsCommentPub(optCommon);
				case Const.MQ_OPT_COMMENT_REPLY:// 评论的回复和回复的回复
					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("QAnswerService");
					qAnswerService.commentReply(optCommon);
					appUser = appUserRepo.findOne(optCommon.getUserId());
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					
					if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_ANSWER){//回答类
						title = appUser.getUserName() + " 回复了你的回答评论回复";
						content = appUser.getUserName() + " 回复了你的回答评论回复 " + optCommon.getContent();
					}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_NEWS){//资讯类
						title = appUser.getUserName() + " 回复了你的资讯评论回复";
						content = appUser.getUserName() + " 回复了你的资讯评论回复 " + optCommon.getContent();
					}
					
					sysNotice = new SysNotice();
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(optCommon.getToUserId());
					sysNotice.setDataType(Const.NOTICE_OPT_COMMENT_REPLY);
					sysNoticeRepo.save(sysNotice);
					
					isSendJdPush = true;
					notificationTitle = title;
					msgContent = content;
					phoneDeviceid = appUserDetail.getPhoneDeviceid();
					break;
				default:
					break;
			}
			
			if(isSendJdPush){
				jdpush = new Jdpush();
				msgTitle = notificationTitle;
				jdpush = new Jdpush();
				jdpush.setNotificationTitle(notificationTitle);
				jdpush.setMsgTitle(msgTitle);
				jdpush.setMsgContent(msgContent);
				jdpush.setExtrasparam("");
				jdpush.setRegisrationId(phoneDeviceid);
				JdpushUtil.sendToRegistrationId(jdpush);
			}
			logger.info("get message " + txtMessage.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
