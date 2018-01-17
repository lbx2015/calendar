package net.riking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.EmailSuffix;

public interface EmailSuffixService {

	/**
	 * 获得需要显示的评论信息
	 * @param qaComment
	 * @param pageRequest
	 * @return
	 */
	public Page<EmailSuffix> findAll(EmailSuffix emailSuffix, PageRequest pageRequest);
}
