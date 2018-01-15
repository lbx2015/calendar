package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.QACommentDao;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.NCAgreeRel;
import net.riking.entity.model.QACAgreeRel;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QACommentResult;
import net.riking.entity.params.CommentParams;
import net.riking.service.QACommentService;
import net.riking.service.TQuestionService;
import net.riking.util.Utils;

@Service("qaCommentService")
@Transactional
public class QACommentServiceImpl implements QACommentService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	QACommentDao qaCommentDao;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Override
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount) {
		return qaCommentDao.findByUserId(userId, pageBegin, pageCount);
	}

	@Override
	public boolean commentAgree(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		CommentParams commentParams = new CommentParams();
		commentParams = (CommentParams) Utils.fromObjToObjValue(optCommon, commentParams);
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						QACAgreeRel rels = qACAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), Const.OBJ_OPT_GREE);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							QACAgreeRel qACAgreeRel = new QACAgreeRel();
							qACAgreeRel.setUserId(commentParams.getUserId());
							qACAgreeRel.setQacId(commentParams.getCommentId());
							qACAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							qACAgreeRelRepo.save(qACAgreeRel);
							return true;
						}
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						qACAgreeRelRepo.deleteByUIdAndQaId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						throw new RuntimeException("参数异常：enabled=" + commentParams.getEnabled());
				}
				break;
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						NCAgreeRel rels = nCAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), 1);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							NCAgreeRel nCAgreeRel = new NCAgreeRel();
							nCAgreeRel.setUserId(commentParams.getUserId());
							nCAgreeRel.setNcId(commentParams.getCommentId());
							nCAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							nCAgreeRelRepo.save(nCAgreeRel);
							return true;
						}
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						nCAgreeRelRepo.deleteByUIdAndNcId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						throw new RuntimeException("参数异常：enabled=" + commentParams.getEnabled());
				}
				break;
			default:
				break;
		}
		return false;

	}

	@Override
	public Page<QAComment> findAll(QAComment qaComment, PageRequest pageRequest) {
		Specification<QAComment> bCondi = whereCondition(qaComment);
		Page<QAComment> pageB = qACommentRepo.findAll(bCondi, pageRequest);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<QAComment> qaComments = pageB.getContent();
			getVos(qaComments);
			Page<QAComment> modulePage = new PageImpl<QAComment>(qaComments, pageRequest, pageB.getTotalElements());
			return modulePage;
		}
		return null;
	}

	/**
	 * 获取qacomment的信息
	 * @param qaComments
	 * @return
	 */
	private void getVos(List<QAComment> qaComments) {
		// TODO Auto-generated method stub
		for (QAComment qaComment : qaComments) {
			String id = qaComment.getId();
			// 获取用户信息
			AppUser appUser = appUserRepo.findOne(qaComment.getUserId());
			if (appUser == null) {
				continue;
			}
			qaComment.setUserName(appUser.getUserName());
			// 获取回复审核数
			String isAduitNum = getIsAduitNum(id);
			qaComment.setIsAduitNum(isAduitNum);
		}
	}

	private String getIsAduitNum(String id) {
		int num0 = qACReplyRepo.countGetByCommentId(id, new Integer(0));
		int num1 = qACReplyRepo.countGetByCommentId(id, new Integer(1));
		int num2 = qACReplyRepo.countGetByCommentId(id, new Integer(2));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + " / ");
		stringBuilder.append(num2 + " / ");
		stringBuilder.append(num1);
		return stringBuilder.toString();
	}

	private Specification<QAComment> whereCondition(QAComment qaComment) {
		return new Specification<QAComment>() {
			@Override
			public Predicate toPredicate(Root<QAComment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				// 添加审核状态的条件判断
				if (qaComment.getIsAduit() != null) {
					predicates.add(cb.equal(root.<String> get("isAduit"), qaComment.getIsAduit()));
				}
				// 获取评论人所有的id
				if (qaComment.getUserName() != null) {
					Set<String> idSet = appUserRepo.getUserIdsByUserName(qaComment.getUserName());
					In<String> in = cb.in(root.get("userId"));
					if (idSet.size() != 0) {
						for (String id : idSet) {
							in.value(id);
						}
					} else {
						in.value("");
					}
					predicates.add(in);
				}
				// 获取时间查询
				if (qaComment.getStartTime() != null || qaComment.getEndTime() != null) {
					if (qaComment.getEndTime() == null) {
						predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), qaComment.getStartTime()));
					} else if (qaComment.getStartTime() == null) {
						predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), qaComment.getEndTime()));
					} else {
						predicates.add(
								cb.between(root.get("createdTime"), qaComment.getStartTime(), qaComment.getEndTime()));
					}
				}
				// 获取问题回答的编号
				if (qaComment.getQuestionAnswerId() != null) {
					predicates.add(cb.equal(root.<String> get("questionAnswerId"), qaComment.getQuestionAnswerId()));
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

}
