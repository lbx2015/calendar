package net.riking.task;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SysNoticeRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.NewsComment;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.SysNotice;
import net.riking.entity.model.TopicQuestion;
import net.riking.service.ContactsInviteService;
import net.riking.service.NewsService;
import net.riking.service.QACommentService;
import net.riking.service.QAInviteService;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.util.JdpushUtil;
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

	@Autowired
	private QAInviteService qaInviteService;
	@Autowired
	private QAnswerService qAnswerService;
	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;
	@Autowired
	private NewsService newsService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private QACommentService qaCommentService;
	@Autowired
	private QACommentRepo qaCommentRepo;
	@Autowired
	private NewsCommentRepo newsCommentRepo;
	@Autowired
	private TQuestionService tQuestionService;
	@Autowired
	private TopicQuestionRepo topicQuestionRepo;
	@Autowired
	private ContactsInviteService contactsInviteService;
	@Autowired
	private SysNoticeRepo sysNoticeRepo;
	
//	@Autowired
//	private AppUserRepo appUserRepo;
	@Autowired
	private AppUserDetailRepo appUserDetailRepo;
	
	@Override
	public void onMessage(Message message) {
//		sysNoticeRepo = (SysNoticeRepo) SpringBeanUtil.getInstance().getBean("sysNoticeRepo");
		//appUserRepo = (AppUserRepo) SpringBeanUtil.getInstance().getBean("AppUserRepo");
//		appUserDetailRepo = (AppUserDetailRepo) SpringBeanUtil.getInstance().getBean("appUserDetailRepo");
		TextMessage txtMessage = (TextMessage) message;
		try {
			JSONObject jsonobject = JSONObject.fromObject(txtMessage.getText());
			MQOptCommon optCommon = (MQOptCommon) JSONObject.toBean(jsonobject, MQOptCommon.class);
			SysNotice sysNotice = null;
			TopicQuestion topicQuestion = null;
			QAComment qaComment = null;
			NewsComment newsComment = null;
			QuestionAnswer questionAnswer = null;
			String title = "";
			String content = "";
			String toUserId = "";
			Map<String, String> extrasMap = new HashMap<>();
//			AppUser appUser = null;
			AppUserDetail appUserDetail = null;
			boolean isRn = false;
			Integer dataType = null;
			Jdpush jdpush = null;
			//是否发送极光
			boolean isSendJdPush = false;
			
			switch (optCommon.getMqOptType()) {
				case Const.MQ_OPT_ANSWERINVITE://邀请回答的邀请 
//					qaInviteService = (QAInviteService) SpringBeanUtil.getInstance().getBean("qaInviteService");
//					topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("topicQuestionRepo");
					//appUser = appUserRepo.findOne(optCommon.getUserId());
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					topicQuestion = topicQuestionRepo.findOne(optCommon.getTqId());
					qaInviteService.saveQaInvite(optCommon);
					
					sysNotice = new SysNotice();
					title = appUserDetail.getUserName() + " 邀请你回答问题";
					//content = appUser.getUserName() + " 邀请你回答问题 " + topicQuestion.getTitle();
					content = topicQuestion.getTitle();
					sysNotice.setTitle(title);
					//话题问题id
					sysNotice.setObjId(topicQuestion.getId());
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(optCommon.getAttentObjId());
					sysNotice.setDataType(Const.NOTICE_OPT_ANSWERINVITE);
					sysNoticeRepo.save(sysNotice);
//					extrasParam.setDataType(sysNotice.getDataType());
//					extrasParam.setobjId(sysNotice.getObjId());
					extrasMap.put("dataType", sysNotice.getDataType()+"");
					extrasMap.put("objId", sysNotice.getObjId());
					isSendJdPush = true;
					break;
				case Const.MQ_OPT_QUESTION_ANWSER : //问题回答
					questionAnswer = questionAnswerRepo.findOne(optCommon.getQuestAnswerId());
					topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
					appUserDetail = appUserDetailRepo.findOne(questionAnswer.getUserId());
					title = appUserDetail.getUserName() + " 回答了你的问题";
					content = topicQuestion.getTitle();
					sysNotice = new SysNotice();
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(topicQuestion.getUserId());
					sysNotice.setDataType(Const.NOTICE_OPT_QUESTION_ANSWER);
					sysNotice.setObjId(questionAnswer.getId());
//					extrasParam.setDataType(sysNotice.getDataType());
//					extrasParam.setobjId(sysNotice.getObjId());
					extrasMap.put("dataType", sysNotice.getDataType()+"");
					extrasMap.put("objId", sysNotice.getObjId());
					isSendJdPush = true;
					break;
				case Const.MQ_OPT_QA_AGREEOR_COLLECT://问题回答点赞或收藏 
//					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("qAnswerService");
//					questionAnswerRepo = (QuestionAnswerRepo) SpringBeanUtil.getInstance().getBean("questionAnswerRepo");
					
					isRn = qAnswerService.qaAgreeCollect(optCommon);
					if(isRn){
//						topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("topicQuestionRepo");
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						questionAnswer = questionAnswerRepo.findOne(optCommon.getQuestAnswerId());
						topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
						
						sysNotice = new SysNotice();
						String optStr = optCommon.getOptType().intValue() == 1 ? " 赞了 " : " 收藏了";
						dataType = optCommon.getOptType().intValue() == 1 ? Const.NOTICE_OPT_QA_AGREEOR : Const.NOTICE_OPT_QA_COLLECT;
						title = appUserDetail.getUserName() + optStr + "你的回答";
						//content = appUser.getUserName() + optStr + "你的回答 " + questionAnswer.getTitle();
						content = topicQuestion.getTitle();
						sysNotice.setTitle(title);
						//问题回答id
						sysNotice.setObjId(questionAnswer.getId());
						sysNotice.setContent(content);
						sysNotice.setNoticeUserId(topicQuestion.getUserId());
						sysNotice.setDataType(dataType);
						sysNoticeRepo.save(sysNotice);
//						extrasParam.setDataType(sysNotice.getDataType());
//						extrasParam.setobjId(sysNotice.getObjId());
						extrasMap.put("dataType", sysNotice.getDataType()+"");
						extrasMap.put("objId", sysNotice.getObjId());
						isSendJdPush = true;
					}
					break;
				case Const.MQ_OPT_NEW_COLLECT://资讯的收藏
//					newsService = (NewsService) SpringBeanUtil.getInstance().getBean("newsService");
					newsService.newsCollect(optCommon);
					break;
				case Const.MQ_OPT_SHIELD_QUEST://问题的屏蔽
//					topicService = (TopicService) SpringBeanUtil.getInstance().getBean("topicService");
					topicService.shield(optCommon);
					break;
				case Const.MQ_OPT_FOLLOW:// 问题，话题，用户的关注
//					tQuestionService = (TQuestionService) SpringBeanUtil.getInstance().getBean("tQuestionService");
//					topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("topicQuestionRepo");
					isRn = tQuestionService.follow(optCommon);
					if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_2){
						//关注话题不推送信息
						return;
					}
					if(isRn){
						//appUser = appUserRepo.findOne(optCommon.getUserId());
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						sysNotice = new SysNotice();
						if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_1){//问题 
							topicQuestion = topicQuestionRepo.findOne(optCommon.getAttentObjId());
							title = appUserDetail.getUserName() + " 关注了你的问题";
							//content = appUser.getUserName() + " 关注了你的问题 " + topicQuestion.getTitle();
							content = topicQuestion.getTitle();
							//话题问题id
							sysNotice.setObjId(topicQuestion.getId());
							sysNotice.setNoticeUserId(topicQuestion.getUserId());
							dataType = Const.NOTICE_OPT_QUESTION_FOLLOW;
							
						}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_3){//用户
							title = appUserDetail.getUserName() + " 关注了你";
							//content = appUser.getUserName() + " 关注了你";
//							content = appUserDetail.getUserName();
							//话题问题id
							sysNotice.setObjId(appUserDetail.getId());
							sysNotice.setNoticeUserId(optCommon.getAttentObjId());
							dataType = Const.NOTICE_OPT_USER_FOLLOW;
						}else{
							return;
						}
						
						sysNotice.setTitle(title);
						if(optCommon.getObjType().intValue() != Const.OBJ_TYPE_3){//关注人不推送content信息
							sysNotice.setContent(content);
						}
						sysNotice.setDataType(dataType);
						sysNoticeRepo.save(sysNotice);
//						extrasParam.setDataType(sysNotice.getDataType());
//						extrasParam.setobjId(sysNotice.getObjId());
						extrasMap.put("dataType", sysNotice.getDataType()+"");
						extrasMap.put("objId", sysNotice.getObjId());
						isSendJdPush = true;
					}
					break;
				case Const.MQ_OPT_CONTACTS_INVITE:// 通讯录的邀请
//					contactsInviteService = (ContactsInviteService) SpringBeanUtil.getInstance().getBean("contactsInviteService");
					contactsInviteService.contactsInvite(optCommon);
					//需要发短信
//					SmsUtil smsUtil = new SmsUtil();
					break;
				case Const.MQ_OPT_NEWS_COMMENT:// 资讯评论发布
//					newsService = (NewsService) SpringBeanUtil.getInstance().getBean("newsService");
					newsService.newsCommentPub(optCommon);
					break;
				case Const.MQ_OPT_QANSWER_COMMENT:// 问题回答的评论
//					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("qAnswerService");
//					questionAnswerRepo = (QuestionAnswerRepo) SpringBeanUtil.getInstance().getBean("questionAnswerRepo");
					questionAnswer = questionAnswerRepo.findOne(optCommon.getQuestAnswerId());
//					topicQuestionRepo = (TopicQuestionRepo) SpringBeanUtil.getInstance().getBean("topicQuestionRepo");
					qAnswerService.qACommentPub(optCommon);
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
					
					sysNotice = new SysNotice();
					
					title = appUserDetail.getUserName() + " 评论了你的回答";
					//content = appUser.getUserName() + " 评论了你的回答 " + optCommon.getContent();
					content = optCommon.getContent();
					//问题回答的id
					sysNotice.setObjId(questionAnswer.getId());
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(questionAnswer.getUserId());
					sysNotice.setDataType(Const.NOTICE_OPT_QANSWER_COMMENT);
					sysNoticeRepo.save(sysNotice);
//					extrasParam.setDataType(sysNotice.getDataType());
//					extrasParam.setobjId(sysNotice.getObjId());
					extrasMap.put("dataType", sysNotice.getDataType()+"");
					extrasMap.put("objId", sysNotice.getObjId());
					isSendJdPush = true;
					break;
				case Const.MQ_OPT_COMMENT_AGREE:// 评论点赞
//					qaCommentRepo = (QACommentRepo) SpringBeanUtil.getInstance().getBean("qaCommentRepo");
//					newsCommentRepo = (NewsCommentRepo) SpringBeanUtil.getInstance().getBean("newsCommentRepo");
					isRn = qaCommentService.commentAgree(optCommon);
					if(isRn){
						appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
						sysNotice = new SysNotice();
						if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_ANSWER){//回答类
							qaComment = qaCommentRepo.findOne(optCommon.getCommentId());
							title = appUserDetail.getUserName() + " 赞了你的回答评论";
							//content = appUser.getUserName() + " 赞了你的回答评论 " + qaComment.getContent();
							content = qaComment.getContent();
							//回答类评论id
							sysNotice.setObjId(qaComment.getQuestionAnswerId());
							sysNotice.setNoticeUserId(qaComment.getUserId());
							sysNotice.setDataType(Const.NOTICE_OPT_ANSWER_COMMENT_AGREE);
						}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_NEWS){//资讯类
							newsComment = newsCommentRepo.findOne(optCommon.getCommentId());
							title = appUserDetail.getUserName() + " 赞了你的资讯评论";
							//content = appUser.getUserName() + " 赞了你的资讯评论 " + qaComment.getContent();
							content = newsComment.getContent();
							//资讯类评论id
							sysNotice.setObjId(newsComment.getNewsId());
							sysNotice.setNoticeUserId(newsComment.getUserId());
							sysNotice.setDataType(Const.NOTICE_OPT_NEWS_COMMENT_AGREE);
						}
						
						sysNotice.setTitle(title);
						sysNotice.setContent(content);
//						sysNotice.setDataType(Const.NOTICE_OPT_COMMENT_AGREE);
						sysNoticeRepo.save(sysNotice);
//						extrasParam.setDataType(sysNotice.getDataType());
//						extrasParam.setobjId(objId);
						extrasMap.put("dataType", sysNotice.getDataType()+"");
						extrasMap.put("objId", sysNotice.getObjId());
						isSendJdPush = true;
					}
					break;
				case Const.MQ_OPT_COMMENT_REPLY:// 评论的回复和回复的回复
//					qAnswerService = (QAnswerService) SpringBeanUtil.getInstance().getBean("qAnswerService");
//					qaCommentRepo = (QACommentRepo) SpringBeanUtil.getInstance().getBean("qaCommentRepo");
//					newsCommentRepo = (NewsCommentRepo) SpringBeanUtil.getInstance().getBean("newsCommentRepo");
					qAnswerService.commentReply(optCommon);
					appUserDetail = appUserDetailRepo.findOne(optCommon.getUserId());
					sysNotice = new SysNotice();
					
					if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_ANSWER){//回答类
						qaComment = qaCommentRepo.findOne(optCommon.getCommentId());//获取评论内容
						title = appUserDetail.getUserName() + " 回复了你";
						//content = appUserDetail.getUserName() + " 回复了你的回答评论回复 " + optCommon.getContent();
						content = optCommon.getContent();
						toUserId =StringUtils.isBlank(optCommon.getToUserId())?qaComment.getUserId():optCommon.getToUserId();
						//回答类评论id
						sysNotice.setObjId(qaComment.getQuestionAnswerId());
						sysNotice.setDataType(Const.NOTICE_OPT_ANSWER_COMMENT_REPLY);
					}else if(optCommon.getObjType().intValue() == Const.OBJ_TYPE_NEWS){//资讯类
						newsComment = newsCommentRepo.findOne(optCommon.getCommentId());
						title = appUserDetail.getUserName() + " 回复了你";
						//content = appUserDetail.getUserName() + " 回复了你的资讯评论回复 " + optCommon.getContent();
						content = optCommon.getContent();
						toUserId =StringUtils.isBlank(optCommon.getToUserId())?newsComment.getUserId():optCommon.getToUserId();
						//资讯类评论id
						sysNotice.setObjId(newsComment.getNewsId());
						sysNotice.setDataType(Const.NOTICE_OPT_NEWS_COMMENT_REPLY);
					}
					
					
					sysNotice.setTitle(title);
					sysNotice.setContent(content);
					sysNotice.setNoticeUserId(toUserId);
//					sysNotice.setDataType(Const.NOTICE_OPT_COMMENT_REPLY);
					sysNoticeRepo.save(sysNotice);
//					extrasParam.setDataType(sysNotice.getDataType());
//					extrasParam.setobjId(objId);
					extrasMap.put("dataType", sysNotice.getDataType()+"");
					extrasMap.put("objId", sysNotice.getObjId());
					isSendJdPush = true;
					break;
				default:
					break;
			}
			if(isSendJdPush){
				
				jdpush = new Jdpush();
				jdpush.setNotificationTitle(title);
				jdpush.setMsgTitle(title);
				jdpush.setMsgContent(content);
				jdpush.setExtrasparam("");
				jdpush.setExtrasMap(extrasMap);
				if(sysNotice != null){
					AppUserDetail noticeUserDetail = appUserDetailRepo.findOne(sysNotice.getNoticeUserId());
					if(StringUtils.isNotBlank(noticeUserDetail.getPhoneDeviceId())){
						jdpush.setRegisrationId(noticeUserDetail.getPhoneDeviceId());
						JdpushUtil.sendToRegistrationId(jdpush);
					}
				}
			}
			logger.info("get message " + txtMessage.getText());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

}
