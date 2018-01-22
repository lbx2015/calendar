package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.QACommentDao;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.News;
import net.riking.entity.model.NewsComment;
import net.riking.service.NewsCommentService;

@Service("newsCommentService")
@Transactional
public class NewsCommentServiceImpl implements NewsCommentService {

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

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NewsRepo newsRepo;

	@Autowired
	NCReplyRepo ncReplyRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Override
	public Page<NewsComment> findAll(NewsComment newsComment, PageRequest pageRequest) {
		Specification<NewsComment> bCondi = whereCondition(newsComment);
		Page<NewsComment> pageB = newsCommentRepo.findAll(bCondi, pageRequest);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<NewsComment> newsComments = pageB.getContent();
			getVos(newsComments);
			// Page<NewsComment> modulePage = new PageImpl<NewsComment>(newsComments, pageRequest,
			// pageB.getTotalElements());
			return pageB;
		}
		return null;
	}

	/**
	 * 获取qacomment的信息
	 * @param qaComments
	 * @return
	 */
	private void getVos(List<NewsComment> newsComments) {
		// TODO Auto-generated method stub
		for (NewsComment newsComment : newsComments) {
			// String id = newsComment.getId();
			// 设置咨询标题
			News news = newsRepo.findOne(newsComment.getNewsId());
			if (news != null) {
				newsComment.setTitle(news.getTitle());
			}
			AppUserDetail appUserDetail = appUserDetailRepo.findOne(newsComment.getUserId());
			if (appUserDetail != null) {
				newsComment.setCommentUserName(appUserDetail.getUserName());
			}
			// 设置回复审核数
			String isAduitNum = getIsAduitNum(newsComment.getId());
			newsComment.setIsAduitNum(isAduitNum);
		}
	}

	/**
	 * 获取回复评论审核数
	 * @return
	 */
	private String getIsAduitNum(String commentId) {
		// 获取未审核
		Integer num0 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(0));
		Integer num1 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(1));
		Integer num2 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(2));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + " / ");
		stringBuilder.append(num2 + " / ");
		stringBuilder.append(num1);
		return stringBuilder.toString();
	}

	private Specification<NewsComment> whereCondition(NewsComment newsComment) {
		return new Specification<NewsComment>() {
			@Override
			public Predicate toPredicate(Root<NewsComment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				if (!StringUtils.isBlank(newsComment.getCommentUserName())) {
					In<String> in = cb.in(root.get("userId"));
					Set<String> idSet = appUserRepo.getUserIdsByUserName(newsComment.getCommentUserName());
					if (idSet.size() != 0) {
						for (String id : idSet) {
							in.value(id);
						}
					} else {
						in.value("");
					}
					// 获取所有用户名叫的id
					predicates.add(in);
				}
				if (!StringUtils.isBlank(newsComment.getTitle())) {
					In<String> in = cb.in(root.get("newsId"));
					Set<String> idSet = newsRepo.getNewsIdsByTitle(newsComment.getTitle());
					if (idSet.size() != 0) {
						for (String id : idSet) {
							in.value(id);
						}
					} else {
						in.value("");
					}
					predicates.add(in);
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

}
