package net.riking.web.controller;

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
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.service.AppUserService;

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
	TQuestionRelRepo tQuestionRelRepo;

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
	public AppResp upLoad(@RequestParam("file") MultipartFile mFile) {
		String fileName = null;
		// MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		// MultipartFile mFile = mRequest.getFile("file");
		try {
			fileName = appUserService.savePhotoFile(mFile, Const.TL_TEMP_PHOTO_PATH);
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
			}
		}
		Data data = new Data("http://localhost:8281" + Const.TL_TEMP_PHOTO_PATH + fileName, "测试");
		System.err.println(data.getSrc());
		return new AppResp(data, (short) 0);

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