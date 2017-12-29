package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.VerifyParamModel;
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;

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

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		QAComment qaComment = qaCommentRepo.findOne(id);
		qaComment.setUserName(appUserRepo.findOne(qaComment.getUserId()).getUserName());
		return new Resp(qaComment, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute QAComment qaComment) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount());
		if (null == qaComment.getIsDeleted()) {
			qaComment.setIsDeleted(1);
		}

		Long totalElements = (long) qaCommentRepo.countGetMore(qaComment.getIsDeleted());
		List<QAComment> qaComments = qaCommentRepo.findAllQAC(qaComment.getIsDeleted(), pageable);// 未删除数据
		Page<QAComment> modulePage = new PageImpl<QAComment>(qaComments, pageable, totalElements);
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

	@ApiOperation(value = "批量审核", notes = "POST")
	@RequestMapping(value = "/verifyMore", method = RequestMethod.POST)
	public Resp verifyMore_(@RequestBody VerifyParamModel verifyParamModel) {
		if (verifyParamModel.getIds() == null || verifyParamModel.getIds().size() < 1) {
			return new Resp("参数有误", CodeDef.ERROR);
		}
		int rs = 0;
		List<QAComment> datas = qaCommentRepo.findAll(verifyParamModel.getIds());
		// successCount表示删除成功的条数
		Integer successCount = 0;
		// failCount表示删除失败的条数
		Integer failCount = 0;
		for (QAComment qaComment : datas) {
			// 已提交才可以进行审批
			if (Const.ADUIT_NO == qaComment.getIsAduit()) {
				switch (verifyParamModel.getEvent()) {
					case "VERIFY_NOT_PASS":
						// 如果审批不通过
						if (verifyParamModel.getIds().size() > 0) {
							rs = qaCommentRepo.verifyNotPassById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					// 如果审批通过
					case "VERIFY_PASS":
						if (verifyParamModel.getIds().size() > 0) {
							rs = qaCommentRepo.verifyById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					default:
						failCount += 1;
						break;
				}
			} else {
				failCount += 1;
			}
		}
		// 如果数据只有一条且失败返回失败
		if (datas.size() == 1 && failCount == 1) {
			return new Resp(CodeDef.ERROR);
		} else if (datas.size() == 1 && successCount == 1) {
			return new Resp("审批成功", CodeDef.SUCCESS);
		} else {
			return new Resp("操作成功!成功" + successCount + "条" + "失败" + failCount + "条", CodeDef.SUCCESS);
		}

	}

	@ApiOperation(value = "得到回复信息", notes = "GET")
	@RequestMapping(value = "/getReply", method = RequestMethod.GET)
	public Resp getReply_(@ModelAttribute PageQuery query, @ModelAttribute QACReply qaCReply) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount());
		if (null == qaCReply.getIsDeleted()) {
			qaCReply.setIsDeleted(1);
		}

		Long totalElements = (long) qacReplyRepo.countGetMore(qaCReply.getIsDeleted());
		List<QACReply> qacReplies = qacReplyRepo.findByCommentId(qaCReply.getCommentId(), qaCReply.getIsDeleted(),
				pageable);// 未删除数据
		Page<QACReply> modulePage = new PageImpl<QACReply>(qacReplies, pageable, totalElements);
		return new Resp(modulePage, CodeDef.SUCCESS);
	}
}
