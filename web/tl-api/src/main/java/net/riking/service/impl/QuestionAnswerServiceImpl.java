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

import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.VO.AppUserVO;
import net.riking.entity.VO.QuestionAnswerVO;
import net.riking.entity.model.AppUser;
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
	private AppUserRepo appUserRepo;

	@Autowired
	private QACommentRepo qACommentRepo;

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
			QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO();
			// 获取编号
			String id = questionAnswer.getId();
			// 获取话题信息
			TopicQuestion topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
			// 获取会回答人信息
			AppUser appUser = appUserRepo.findOne(questionAnswer.getUserId());
			// 获取回答时间
			Date replyTime = questionAnswer.getCreatedTime();
			Integer commentNum = new Integer(0);
			// 获取评论数量
			if (appUser != null && questionAnswer != null) {
				commentNum = qACommentRepo.getQACommentByUserIdAndquestionAnswerId(appUser.getId(),
						questionAnswer.getId());
			}
			// 获取评论审核数(未审核/不通过/已通过)
			String isAduitNum = "0";
			// 获取回复审核数
			String replyIsaudit = "0";
			// 获取回答内容
			String content = questionAnswer.getContent();
			questionAnswerVO.setId(id);
			questionAnswerVO.setAppUser(appUser);
			questionAnswerVO.setCommentNum(commentNum.toString());
			questionAnswerVO.setIsAduitNum(isAduitNum);
			questionAnswerVO.setIsAduitNumByReply(replyIsaudit);
			questionAnswerVO.setReplyTime(replyTime);
			questionAnswerVO.setTopicQuestion(topicQuestion);
			questionAnswerVO.setContent(content);
			questionAnswerVOs.add(questionAnswerVO);
		}
		return questionAnswerVOs;
	}

	/**
	 * 获取评论审核数
	 * @return
	 */
	private String getIsAduitNum(String userId) {
		// 获取未审核
		Integer num0 = questionAnswerRepo.answerCountByUserIdAndIsAudit(userId, "0");
		Integer num1 = questionAnswerRepo.answerCountByUserIdAndIsAudit(userId, "1");
		Integer num2 = questionAnswerRepo.answerCountByUserIdAndIsAudit(userId, "2");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + "/");
		stringBuilder.append(num1 + "/");
		stringBuilder.append(num2);
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
