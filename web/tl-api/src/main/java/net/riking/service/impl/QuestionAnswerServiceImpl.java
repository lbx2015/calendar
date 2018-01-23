package net.riking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.VO.AppUserVO;
import net.riking.entity.VO.QuestionAnswerVO;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TopicQuestion;
import net.riking.service.QuestionAnswerService;

@Service("questionAnswerService")
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	private TopicQuestionRepo topicQuestionRepo;

	@Autowired
	private QACommentRepo qACommentRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Override
	public Page<QuestionAnswerVO> findAll(QuestionAnswerVO questionAnswerVO, PageRequest pageable) {
		Specification<QuestionAnswer> bCondi = whereCondition(questionAnswerVO);
		// 1.得到Page<AppUser>对象
		Page<QuestionAnswer> pageB = questionAnswerRepo.findAll(bCondi, pageable);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<QuestionAnswer> questionAnswers = pageB.getContent();
			List<QuestionAnswerVO> questionAnswerVOs = getVos(questionAnswers);
			Page<QuestionAnswerVO> modulePage = new PageImpl<QuestionAnswerVO>(questionAnswerVOs, pageable,
					pageB.getTotalElements());
			return modulePage;
		}
		return null;
	}

	/**
	 * 构建要显示的回答话题页面
	 * @param topicQuestions
	 * @return
	 */
	private List<QuestionAnswerVO> getVos(List<QuestionAnswer> questionAnswers) {
		List<QuestionAnswerVO> questionAnswerVOs = new ArrayList<QuestionAnswerVO>();
		for (QuestionAnswer questionAnswer : questionAnswers) {
			QuestionAnswerVO questionAnswerVO = getQuestionVOByQuestionAnswer(questionAnswer);
			if (questionAnswerVO != null) {
				questionAnswerVOs.add(questionAnswerVO);
			}
		}
		return questionAnswerVOs;
	}

	/**
	 * 获取questionAnswerVO
	 * @param questionAnswer
	 * @return
	 */
	private QuestionAnswerVO getQuestionVOByQuestionAnswer(QuestionAnswer questionAnswer) {
		QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO();
		// 获取编号
		String id = questionAnswer.getId();
		// 获取话题信息
		TopicQuestion topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
		// 获取会回答人信息
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(questionAnswer.getUserId());
		// 获取回答时间
		Date replyTime = questionAnswer.getCreatedTime();
		Integer commentNum = new Integer(0);
		// 获取评论数量
		if (appUserDetail != null && questionAnswer != null) {
			commentNum = qACommentRepo.getQACommentByQuestionAnswerId(questionAnswer.getId());
		}
		// 获取评论审核数(未审核/不通过/已通过)
		String isAduitNum = getIsAduitNum(id);
		// 获取回复审核数
		String replyIsaudit = getReplyNum(id);
		// 获取回答内容
		String content = questionAnswer.getContent();

		// 设置审核状态
		questionAnswerVO.setIsAduit(questionAnswer.getIsAduit());
		questionAnswerVO.setId(id);
		questionAnswerVO.setAppUserDetail(appUserDetail);
		questionAnswerVO.setCommentNum(commentNum.toString());
		questionAnswerVO.setIsAduitNum(isAduitNum);
		questionAnswerVO.setIsAduitNumByReply(replyIsaudit);
		questionAnswerVO.setReplyTime(replyTime);
		questionAnswerVO.setTopicQuestion(topicQuestion);
		questionAnswerVO.setContent(content);

		return questionAnswerVO;
	}

	/**
	 * 获取该回答下的所有回复数
	 * @param id
	 * @return
	 */
	private String getReplyNum(String id) {
		// 未审核的数量
		int noIsAduit = 0;
		// 已审核
		int isAduit = 0;
		// 未通过
		int reject = 0;
		// 获取所有的评论信息
		List<QAComment> qaComments = qACommentRepo.getAllByQuestionAnswerId(id);
		// 获取所有的回复信息
		List<QACReply> qacReplies = qACReplyRepo.GetMore();
		for (QAComment qaComment : qaComments) {
			for (QACReply qacReply : qacReplies) {
				// 获取所有的该评论的回复信息
				if (qaComment.getId() != null && qacReply.getCommentId().equals(qaComment.getId())) {
					if (qacReply.getIsAduit() == null) {
						continue;
					}
					String temp = qacReply.getIsAduit().toString();
					if (temp.equals("0")) {
						// 未审核
						noIsAduit++;
					}
					if (temp.equals("1")) {
						// 已审核
						isAduit++;
					}
					if (temp.equals("2")) {
						// 拒绝
						reject++;
					}
				}
			}
		}

		// // 便利获取回复数量
		// for (QAComment qaComment : qaComments) {
		// // 获取评论的回复数量
		// List<QACReply> qacReplies = qACReplyRepo.getByCommentId(qaComment.getId());
		// for (QACReply qacReply : qacReplies) {
		// // 记录评审状态
		// if (qacReply.getIsAduit() == null) {
		// continue;
		// }
		// String temp = qacReply.getIsAduit().toString();
		// if (temp.equals("0")) {
		// // 未审核
		// noIsAduit++;
		// }
		// if (temp.equals("1")) {
		// // 已审核
		// isAduit++;
		// }
		// if (temp.equals("2")) {
		// // 拒绝
		// reject++;
		// }
		// }
		// }
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(noIsAduit + " / ");
		stringBuilder.append(reject + " / ");
		stringBuilder.append(isAduit);
		return stringBuilder.toString();
	}

	/**
	 * 获取评论审核数
	 * @return
	 */
	private String getIsAduitNum(String questionAnswerId) {
		// 获取未审核
		List<QAComment> list = qACommentRepo.getQACommentNumByQuestionAnswerId(questionAnswerId);
		// 获取统计的个数
		int num0 = 0;
		int num1 = 0;
		int num2 = 0;
		for (QAComment qaComment : list) {
			switch (qaComment.getIsAduit()) {
				case 0:
					num0++;
					break;
				case 1:
					num1++;
					break;
				case 2:
					num2++;
					break;
				default:
					break;
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + " / ");
		stringBuilder.append(num2 + " / ");
		stringBuilder.append(num1);
		return stringBuilder.toString();
	}

	private Specification<QuestionAnswer> whereCondition(QuestionAnswerVO questionAnswerVO) {
		return new Specification<QuestionAnswer>() {
			@Override
			public Predicate toPredicate(Root<QuestionAnswer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

	@Override
	public void updateModule(AppUserVO appUserVO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void del(String id) {
		// TODO Auto-generated method stub

	}

}
