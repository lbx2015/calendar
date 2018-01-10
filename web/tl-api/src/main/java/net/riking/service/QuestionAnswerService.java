package net.riking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.VO.AppUserVO;
import net.riking.entity.VO.QuestionAnswerVO;

public interface QuestionAnswerService {

	/********************* WEB ***************/

	Page<QuestionAnswerVO> findAll(QuestionAnswerVO questionAnswerVO, PageRequest pageable);

	void updateModule(AppUserVO appUserVO);

	void del(String id);

	/******************** WEB END ***********/
}
