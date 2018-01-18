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
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.QACReply;
import net.riking.service.QACommentService;

@RestController
@RequestMapping(value = "/qaCReply")
public class QACReplyController {

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
	AppUserDetailRepo appUserDetailRepo;

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		QACReply qacReply = qacReplyRepo.findOne(id);
		// 设置回复时间
		qacReply.setCommentTime(qacReply.getCreatedTime());
		return new Resp(qacReply, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute QACReply qacReply) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount());
		if (null == qacReply.getIsDeleted()) {
			qacReply.setIsDeleted(1);
		}
		Example<QACReply> example = Example.of(qacReply, ExampleMatcher.matchingAll());
		Page<QACReply> modulePage = qacReplyRepo.findAll(example, pageable);
		int i = query.getPindex() * query.getPcount();
		List<QACReply> list = modulePage.getContent();
		for (QACReply qacReply2 : list) {
			// 设置序列
			i++;
			qacReply2.setSerialNumber(new Integer(i));
			// 设置回复时间
			qacReply2.setCommentTime(qacReply2.getCreatedTime());
			// 设置用回复户名
			AppUserDetail fromUserDetail = appUserDetailRepo.findOne(qacReply2.getFromUserId());
			if (fromUserDetail == null) {
				continue;
			}
			if (qacReply2.getToUserId() != null) {
				// 设置评论用户名
				AppUserDetail toUserDetail = appUserDetailRepo.findOne(qacReply2.getToUserId());
				if (toUserDetail != null) {
					qacReply2.setToUserName(toUserDetail.getUserName());
				}
			}
			qacReply2.setFromUserName(fromUserDetail.getUserName());
		}
		return new Resp(modulePage, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核回答信息", notes = "GET")
	@RequestMapping(value = "/answerIsAduit", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id, @RequestParam String isAduit) {
		QACReply qacReply = qacReplyRepo.findOne(id);
		qacReply.setIsAduit(new Integer(isAduit));
		qacReplyRepo.save(qacReply);
		return new Resp(qacReply, CodeDef.SUCCESS);
	}

}
