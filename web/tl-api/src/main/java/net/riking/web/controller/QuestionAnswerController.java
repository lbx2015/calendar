package net.riking.web.controller;

import java.util.Date;
import java.util.List;

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
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;
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

	// @ApiOperation(value = "禁用用户信息", notes = "GET")
	// @RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
	// public Resp passwordReset_(@RequestParam String id) {
	// int rs = appUserRepo.passwordReset(id);
	// return new Resp(rs, CodeDef.SUCCESS);
	// }

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

	// TODO 暫時注釋
	// @AuthPass
	// @ApiOperation(value = "上传头像", notes = "POST")
	// @RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	// public Resp upLoad(HttpServletRequest request, @RequestParam("id") String
	// id) {
	// MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest)
	// request;
	// MultipartFile mFile = mRequest.getFile("fileName");
	// String suffix =
	// mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	// String fileName = sdf.format(new Date()) + suffix;
	// InputStream is = null;
	// FileOutputStream fos = null;
	// try {
	// is = mFile.getInputStream();
	// String path = this.getClass().getResource("/").getPath() +
	// Const.TL_STATIC_PATH +
	// Const.TL_PHOTO_PATH;
	// File dir = new File(path);
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	// String photoUrl = path + fileName;
	// fos = new FileOutputStream(photoUrl);
	// int len = 0;
	// byte[] buf = new byte[1024 * 1024];
	// while ((len = is.read(buf)) > -1) {
	// fos.write(buf, 0, len);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new Resp(false, CodeDef.ERROR);
	// } finally {
	// try {
	// fos.close();
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return new Resp(false, CodeDef.ERROR);
	// }
	// }
	// // 截取资源访问路径
	// String url = request.getRequestURL().toString();
	// String projectPath = StringUtil.getProjectPath(url);
	// int rs = appUserRepo.updatePhoto(id, projectPath + Const.TL_PHOTO_PATH +
	// fileName);
	// if (rs > 0) {
	// return new Resp(true, CodeDef.SUCCESS);
	// }
	// return new Resp(CodeDef.ERROR);
	// }

	// TODO 暂时注释
	// private void setPhotoUrl(String url, List<AppUser> list) {
	// String projectPath = StringUtil.getProjectPath(url);
	// for (AppUser appUser : list) {
	// if (appUser.getPhotoUrl() != null &&
	// !appUser.getPhotoUrl().contains("http"))
	// appUser.setPhotoUrl(projectPath + appUser.getPhotoUrl());
	// }
	// }
	/**
	 * 获取questionAnswerVO
	 * @param questionAnswer
	 * @return
	 */
	private QuestionAnswerVO getQuestionVOByQuestionAnswer(QuestionAnswer questionAnswer) {
		QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO();
		// 获取编号
		String id = questionAnswer.getId();
		// 获取话题信息
		TopicQuestion topicQuestion = topicQuestionRepo.findOne(questionAnswer.getQuestionId());
		// 获取会回答人信息
		AppUser appUser = appUserRepo.findOne(questionAnswer.getUserId());
		// 获取回答时间
		Date replyTime = questionAnswer.getCreatedTime();
		Integer commentNum = new Integer(0);
		// 获取评论数量
		if (appUser != null && questionAnswer != null) {
			commentNum = qaCommentRepo.getQACommentByQuestionAnswerId(questionAnswer.getId());
		}
		// 获取评论审核数(未审核/不通过/已通过)
		String isAduitNum = getIsAduitNum(id);
		// 获取回复审核数
		String replyIsaudit = getReplyNum(id);
		// 获取回答内容
		String content = questionAnswer.getContent();

		// 设置审核状态
		questionAnswerVO.setIsAduit(questionAnswer.getIsAduit());
		questionAnswerVO.setId(id);
		questionAnswerVO.setAppUser(appUser);
		questionAnswerVO.setCommentNum(commentNum.toString());
		questionAnswerVO.setIsAduitNum(isAduitNum);
		questionAnswerVO.setIsAduitNumByReply(replyIsaudit);
		questionAnswerVO.setReplyTime(replyTime);
		questionAnswerVO.setTopicQuestion(topicQuestion);
		questionAnswerVO.setContent(content);

		return questionAnswerVO;
	}

	/**
	 * 获取该回答下的所有回复数
	 * @param id
	 * @return
	 */
	private String getReplyNum(String id) {
		// 未审核的数量
		int noIsAduit = 0;
		// 已审核
		int isAduit = 0;
		// 未通过
		int reject = 0;
		// 获取所有的评论信息
		List<QAComment> qaComments = qaCommentRepo.getAllByQuestionAnswerId(id);
		// 便利获取回复数量
		for (QAComment qaComment : qaComments) {
			// 获取评论的回复数量
			List<QACReply> qacReplies = qACReplyRepo.getByCommentId(qaComment.getId());
			for (QACReply qacReply : qacReplies) {
				// 记录评审状态
				if (qacReply.getIsAduit() == null) {
					continue;
				}
				String temp = qacReply.getIsAduit().toString();
				if (temp.equals("0")) {
					// 未审核
					noIsAduit++;
				}
				if (temp.equals("1")) {
					// 已审核
					isAduit++;
				}
				if (temp.equals("2")) {
					// 拒绝
					reject++;
				}
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(noIsAduit + " / ");
		stringBuilder.append(reject + " / ");
		stringBuilder.append(isAduit);
		return stringBuilder.toString();
	}

	/**
	 * 获取评论审核数
	 * @return
	 */
	private String getIsAduitNum(String questionAnswerId) {
		// 获取未审核
		Integer num0 = qaCommentRepo.getQACommentNumByQuestionAnswerIdAndIsAduit(questionAnswerId, new Integer(0));
		Integer num1 = qaCommentRepo.getQACommentNumByQuestionAnswerIdAndIsAduit(questionAnswerId, new Integer(1));
		Integer num2 = qaCommentRepo.getQACommentNumByQuestionAnswerIdAndIsAduit(questionAnswerId, new Integer(2));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + " / ");
		stringBuilder.append(num2 + " / ");
		stringBuilder.append(num1);
		return stringBuilder.toString();
	}

}
