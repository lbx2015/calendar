package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
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

import net.riking.dao.repo.EmailSuffixRepo;
import net.riking.entity.model.EmailSuffix;
import net.riking.service.EmailSuffixService;
import net.riking.service.TQuestionService;

@Service("emailSuffixService")
@Transactional
public class EmailSuffixServiceImpl implements EmailSuffixService {

	@Autowired
	private EmailSuffixRepo emailSuffixRepo;

	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Override
	public Page<EmailSuffix> findAll(EmailSuffix emailSuffix, PageRequest pageRequest) {
		Specification<EmailSuffix> bCondi = whereCondition(emailSuffix);
		Page<EmailSuffix> pageB = emailSuffixRepo.findAll(bCondi, pageRequest);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<EmailSuffix> emailSuffixs = pageB.getContent();
			Page<EmailSuffix> modulePage = new PageImpl<EmailSuffix>(emailSuffixs, pageRequest,
					pageB.getTotalElements());
			return modulePage;
		}
		return null;
	}

	private Specification<EmailSuffix> whereCondition(EmailSuffix emailSuffix) {
		return new Specification<EmailSuffix>() {
			@Override
			public Predicate toPredicate(Root<EmailSuffix> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));
				// 添加启用条件的状态判断
				if (emailSuffix.getEnabled() != null) {
					predicates.add(cb.equal(root.<String> get("enabled"), emailSuffix.getEnabled()));
				}
				// 添加邮箱后缀的查询条件
				if (emailSuffix.getEmailSuffix() != null) {
					predicates.add(cb.like(root.<String> get("emailSuffix"), "%" + emailSuffix.getEmailSuffix() + "%"));
				}
				// 添加公司名称查询条件
				if (emailSuffix.getCompanyName() != null) {
					predicates.add(cb.like(root.<String> get("companyName"), "%" + emailSuffix.getCompanyName() + "%"));
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

}
