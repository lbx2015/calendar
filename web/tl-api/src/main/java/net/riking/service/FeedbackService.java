package net.riking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.FeedBack;

public interface FeedbackService {

	/********************* WEB ***************/

	public Page<FeedBack> findAllTo(FeedBack feedBack, PageRequest pageRequest);

	/******************** WEB END ***********/
}
