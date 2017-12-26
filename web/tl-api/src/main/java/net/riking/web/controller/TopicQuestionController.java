package net.riking.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.ApiResp;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TopicQuestion;
import net.riking.service.AppUserService;
import net.riking.util.FileUtils;
import net.riking.util.StringUtil;
import net.riking.util.Utils;

/**
 * 问题接口
 * 
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 问题接口
 */
@RestController
@RequestMapping(value = "/tQuestion")
public class TopicQuestionController {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	QAInviteRepo qAInviteRepo;

	@Autowired
	AppUserService appUserService;

	@AuthPass
	@ApiOperation(value = "提问/回答上传图片到临时路径", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public ApiResp upLoad(@RequestParam("file") MultipartFile mFile) {
		String fileName = null;
		try {
			fileName = appUserService.savePhotoFile(mFile, Const.TL_TEMP_PHOTO_PATH);
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
			}
		}
		Data data = new Data(
				StringUtil.getProjectPath(request.getRequestURL().toString()) + Const.TL_TEMP_PHOTO_PATH + fileName,
				fileName);
		return new ApiResp(data, (short) 0);

	}

	@RequestMapping(value = "/questionSave", method = RequestMethod.GET)
	public Resp questionSave_(@RequestParam HashMap<String, Object> params) {
		TopicQuestion topicQuestion = Utils.map2Obj(params, TopicQuestion.class);
		try {
			topicQuestion.setTitle(URLDecoder.decode(topicQuestion.getTitle(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return new Resp(CodeDef.ERROR);
		}
		topicQuestion.setCreatedBy(topicQuestion.getUserId());
		topicQuestion.setModifiedBy(topicQuestion.getUserId());
		topicQuestion.setIsAduit(0);
		topicQuestionRepo.save(topicQuestion);
		String[] fileNames = topicQuestion.getContent().split("alt=");
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_QUESTION_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				return new Resp(CodeDef.ERROR);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}

		return new Resp(topicQuestion, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/answerSave", method = RequestMethod.GET)
	public Resp answerSave_(@RequestParam HashMap<String, Object> params) {
		QuestionAnswer questionAnswer = Utils.map2Obj(params, QuestionAnswer.class);
		questionAnswer.setCreatedBy(questionAnswer.getUserId());
		questionAnswer.setModifiedBy(questionAnswer.getUserId());
		questionAnswer.setIsAduit(0);
		questionAnswer.setCoverUrl(questionAnswer.getContent().split("alt=")[1].split(">")[0]);
		questionAnswerRepo.save(questionAnswer);
		String[] fileNames = questionAnswer.getContent().split("alt=");
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_ANSWER_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				return new Resp(CodeDef.ERROR);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
		return new Resp(questionAnswer, CodeDef.SUCCESS);
	}

	/****************** web ******************/
	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		TopicQuestion topic = topicQuestionRepo.findOne(id);
		return new Resp(topic, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute TopicQuestion topic) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if (null == topic.getIsDeleted()) {
			topic.setIsDeleted(1);
		}
		Example<TopicQuestion> example = Example.of(topic, ExampleMatcher.matchingAll());
		Page<TopicQuestion> page = topicQuestionRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody TopicQuestion topic) {
		if (StringUtils.isEmpty(topic.getId())) {
			topic.setIsDeleted(1);
			topic.setIsAduit(0);
		}
		TopicQuestion save = topicQuestionRepo.save(topic);
		return new Resp(save, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = topicQuestionRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "批量审核", notes = "POST")
	@RequestMapping(value = "/verifyMore", method = RequestMethod.POST)
	public Resp verifyMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = topicQuestionRepo.verifyById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

}

class Data {
	private String src;

	private String title;

	public Data() {
		super();
	}

	public Data(String src, String title) {
		super();
		this.src = src;
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
