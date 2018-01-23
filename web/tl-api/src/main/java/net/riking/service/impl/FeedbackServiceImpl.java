package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import net.riking.dao.repo.FeedBackRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.FeedBack;
import net.riking.service.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	FeedBackRepo feedBackRepo;

	@Override
	public Page<FeedBack> findAllTo(FeedBack feedBack, PageRequest pageRequest) {
		Specification<FeedBack> bCondi = whereCondition(feedBack);

		return null;
	}

	private Specification<FeedBack> whereCondition(FeedBack feedBack) {

		return new Specification<FeedBack>() {
			@Override
			public Predicate toPredicate(Root<FeedBack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				if (StringUtils.isNotBlank(feedBack.getUserName())) {
					Join<AppUserDetail, FeedBack> join = root.join("createdBy", JoinType.LEFT);
					predicates.add(cb.equal(join.get("userName"), feedBack.getUserName()));
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

}
