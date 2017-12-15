package net.riking.web.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QACommentResult;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.params.UserFollowParams;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;

/**
 * 我的动态接口
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/userDynamic")
public class AppUserDynamicServer {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserService appUserService;

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	TopicService topicService;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	/**
	 * userId;optType(1-评论；2-回答；3-提问)
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "我的动态（评论，回答，提问列表）", notes = "POST")
	@RequestMapping(value = "/myDynamic", method = RequestMethod.POST)
	public AppResp myDynamic_(@RequestBody UserFollowParams userFollowParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())
				|| userFollowParams.getOptType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (userFollowParams.getPindex() == null) {
			userFollowParams.setPindex(0);
		}
		if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
			userFollowParams.setPindex(userFollowParams.getPindex() - 1);
		}
		// 分页计算
		int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ? 30
				: userFollowParams.getPcount();

		int pageBegin = userFollowParams.getPindex() == 0 ? 0 : userFollowParams.getPindex() + pageCount;
		int pageEnd = pageBegin + pageCount;
		switch (userFollowParams.getOptType()) {
			// 评论
			case Const.OBJ_OPT_COMMENT:
				List<QACommentResult> qaCommentResults = qACommentRepo.findByUserId(userFollowParams.getUserId());
				for (QACommentResult qaCommentResult : qaCommentResults) {
					// 点赞数 TODO 后面从redis中取
					Integer agreeNumber = qACommentRepo.commentCount(qaCommentResult.getQuestionAnswerId());
					qaCommentResult.setAgreeNumber(agreeNumber);
					TopicQuestion topicQuestion = topicQuestionRepo.getById(qaCommentResult.getTqId());
					if (null != topicQuestion) {
						qaCommentResult.setTqTitle(topicQuestion.getTitle());
					}
				}
				return new AppResp(qaCommentResults, CodeDef.SUCCESS);
			// 回答
			case Const.OBJ_OPT_ANSWER:
				// 回答
				List<QAnswerResult> qAnswerResults = questionAnswerRepo
						.findQAnswerByUserId(userFollowParams.getUserId(), new PageRequest(pageBegin, pageEnd));
				for (QAnswerResult qAnswerResult : qAnswerResults) {
					// TODO 获取评论数，点赞数后面从redis中获取
					qAnswerResult.setQaCommentNum(qACommentRepo.commentCount(qAnswerResult.getQaId()));
					qAnswerResult
							.setQaCommentNum(qAnswerRelRepo.agreeCount(qAnswerResult.getQaId(), Const.OBJ_OPT_GREE));
				}
				return new AppResp(qAnswerResults, CodeDef.SUCCESS);
			// 提问
			case Const.OBJ_OPT_INQUIRY:
				List<QuestResult> questResults = topicQuestionRepo.findByUserId(userFollowParams.getUserId(),
						new PageRequest(pageBegin, pageEnd));

				return new AppResp(questResults, CodeDef.SUCCESS);
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
	}

	// /**
	// * userId;objType(1-问题回答，2-行业资讯)
	// * @param userFollowParams
	// * @return
	// * @throws IllegalArgumentException
	// * @throws IllegalAccessException
	// */
	// @ApiOperation(value = "我的收藏", notes = "POST")
	// @RequestMapping(value = "/myCollection", method = RequestMethod.POST)
	// public AppResp myCollection_(@RequestBody UserFollowParams userFollowParams)
	// throws IllegalArgumentException, IllegalAccessException {
	// if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())
	// || userFollowParams.getObjType() == null) {
	// return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
	// }
	// if (userFollowParams.getPindex() == null) {
	// userFollowParams.setPindex(0);
	// }
	// if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
	// userFollowParams.setPindex(userFollowParams.getPindex() - 1);
	// }
	// // 分页计算
	// int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ?
	// 30
	// : userFollowParams.getPcount();
	//
	// int pageBegin = userFollowParams.getPindex() == 0 ? 0 : userFollowParams.getPindex() +
	// pageCount;
	// int pageEnd = pageBegin + pageCount;
	// switch (userFollowParams.getObjType()) {
	// // 问题回答
	// case Const.OBJ_TYPE_ANSWER:
	// topicQuestionServer
	// return new AppResp(qaCommentResults, CodeDef.SUCCESS);
	// // 行业资讯
	// case Const.OBJ_TYPE_NEWS:
	// return new AppResp(qAnswerResults, CodeDef.SUCCESS);
	// default:
	// return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
	// }
	// }
}
