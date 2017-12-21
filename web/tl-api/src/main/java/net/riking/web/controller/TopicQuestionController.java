package net.riking.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
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
