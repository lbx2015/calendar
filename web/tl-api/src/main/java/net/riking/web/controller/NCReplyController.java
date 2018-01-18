package net.riking.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.NCReply;
import net.riking.service.QACommentService;

@RestController
@RequestMapping(value = "/nCReply")
public class NCReplyController {

	@Autowired
	QACommentRepo qaCommentRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	QACReplyRepo qacReplyRepo;

	@Autowired
	QACommentService qaCommentService;

	@Autowired
	NCReplyRepo ncReplyRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		NCReply ncReply = ncReplyRepo.findOne(id);
		return new Resp(ncReply, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute NCReply ncReply) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount());
		if (null == ncReply.getIsDeleted()) {
			ncReply.setIsDeleted(1);
		}
		Example<NCReply> example = Example.of(ncReply, ExampleMatcher.matchingAll());
		Page<NCReply> modulePage = ncReplyRepo.findAll(example, pageable);
		int i = query.getPindex() * query.getPcount();
		List<NCReply> list = modulePage.getContent();
		for (NCReply qacReply2 : list) {
			// 设置序列
			i++;
			qacReply2.setSerialNumber(new Integer(i));
			// 设置评论用户名
			AppUserDetail fromUserDetail = appUserDetailRepo.findOne(qacReply2.getFromUserId());
			if (fromUserDetail == null) {
				continue;
			}
			// 设置回复用户名
			if (qacReply2.getToUserId() != null) {
				AppUserDetail toUserDetail = appUserDetailRepo.findOne(qacReply2.getToUserId());
				qacReply2.setToUserName(toUserDetail.getUserName());
			}
			qacReply2.setUserName(fromUserDetail.getUserName());

		}
		return new Resp(modulePage, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核回答信息", notes = "GET")
	@RequestMapping(value = "/answerIsAduit", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id, @RequestParam String isAduit) {
		NCReply ncReply = ncReplyRepo.findOne(id);
		ncReply.setIsAduit(new Integer(isAduit));
		ncReplyRepo.save(ncReply);
		return new Resp(ncReply, CodeDef.SUCCESS);
	}

}
