package net.riking.web.controller;

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
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.VO.QuestionAnswerVO;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TopicQuestion;
import net.riking.service.AppUserService;
import net.riking.service.QuestionAnswerService;

/**
 * web端app用户操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/questionAnswer")
public class QuestionAnswerController {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	AppUserService appUserService;

	@Autowired
	QuestionAnswerService questionAnswerService;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	QACommentRepo qaCommentRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@ApiOperation(value = "得到<单个>用户信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		QuestionAnswer questionAnswer = questionAnswerRepo.findOne(id);
		if (null != questionAnswer) {
			// 组装

			QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO();
			AppUser appUser = appUserRepo.findOne(questionAnswer.getUserId());
			String userId = appUser.getId();
			questionAnswerVO.setAppUser(appUser);
			String questionAnswerId = questionAnswer.getId();
			if (userId != null && questionAnswerId != null) {
				questionAnswerVO
						.setCommentNum(qaCommentRepo.getQACommentByQuestionAnswerId(questionAnswerId).toString());
			}
			questionAnswerVO.setContent(questionAnswer.getContent());
			questionAnswerVO.setIsAduitNum("0");
			questionAnswerVO.setIsAduitNumByReply("0");
			questionAnswerVO.setIsAduit(questionAnswer.getIsAduit());
			questionAnswerVO.setReplyTime(questionAnswer.getCreatedTime());
			TopicQuestion topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
			questionAnswerVO.setTopicQuestion(topicQuestion);
			return new Resp(questionAnswerVO, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "得到<批量>用户信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute QuestionAnswerVO questionAnswerVO) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Page<QuestionAnswerVO> page = questionAnswerService.findAll(questionAnswerVO, pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody QuestionAnswerVO questionAnswerVO) {
		// 修改
		// appUserService.updateModule(appUserVO);
		QuestionAnswer questionAnswer = questionAnswerRepo.findOne(questionAnswerVO.getId());
		questionAnswer.setContent(questionAnswerVO.getContent());
		questionAnswerRepo.save(questionAnswer);
		System.out.println(1);
		return new Resp(1, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核回答信息", notes = "GET")
	@RequestMapping(value = "/answerIsAduit", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id, @RequestParam String isAduit) {
		QuestionAnswer questionAnswer = questionAnswerRepo.findOne(id);
		questionAnswer.setIsAduit(new Integer(isAduit));
		questionAnswerRepo.save(questionAnswer);
		return new Resp(questionAnswer, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "评论审核信息", notes = "GET")
	@RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	public Resp unEnable_(@RequestParam String id) {
		int rs = appUserRepo.unEnable(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除用户信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody String id) {
		id = id.replaceAll("\\[", "");
		id = id.replaceAll("\\]", "");
		id = id.replaceAll("\"", "");

		QuestionAnswer questionAnswer = questionAnswerRepo.findOne(id);
		if (questionAnswer != null) {
			questionAnswer.setIsDeleted(new Integer(Const.INVALID));
			questionAnswerRepo.save(questionAnswer);
			return new Resp(questionAnswer, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}



}
