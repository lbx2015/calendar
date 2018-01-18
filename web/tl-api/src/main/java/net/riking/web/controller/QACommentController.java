package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
import net.riking.entity.model.QAComment;
import net.riking.service.QACommentService;

@RestController
@RequestMapping(value = "/qaCommentController")
public class QACommentController {

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
		QAComment qaComment = qaCommentRepo.findOne(id);
		if (qaComment != null) {
			AppUserDetail appUserDetail = appUserDetailRepo.findOne(qaComment.getUserId());
			if (appUserDetail != null) {
				qaComment.setUserName(appUserDetail.getUserName());
			}
		}
		// 设置评论时间
		qaComment.setCommentTime(qaComment.getCreatedTime());
		return new Resp(qaComment, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute QAComment qaComment) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount());
		if (null == qaComment.getIsDeleted()) {
			qaComment.setIsDeleted(1);
		}
		Page<QAComment> modulePage = qaCommentService.findAll(qaComment, pageable);
		int i = query.getPindex() * query.getPcount();
		List<QAComment> list = modulePage.getContent();
		for (QAComment qaComment2 : list) {
			i++;
			qaComment2.setSerialNumber(new Integer(i));
			qaComment2.setCommentTime(qaComment2.getCreatedTime());
		}
		return new Resp(modulePage, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody QAComment news) {
		if (StringUtils.isEmpty(news.getId())) {
			news.setIsDeleted(1);
			news.setIsAduit(0);
		}
		if (news.getIsAduit() == 2) {
			news.setIsAduit(0);
		}
		QAComment save = qaCommentRepo.save(news);
		return new Resp(save, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = qaCommentRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "审核回答信息", notes = "GET")
	@RequestMapping(value = "/answerIsAduit", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id, @RequestParam String isAduit) {
		QAComment qaComment = qaCommentRepo.findOne(id);
		qaComment.setIsAduit(new Integer(isAduit));
		qaCommentRepo.save(qaComment);
		return new Resp(qaComment, CodeDef.SUCCESS);
	}

}
