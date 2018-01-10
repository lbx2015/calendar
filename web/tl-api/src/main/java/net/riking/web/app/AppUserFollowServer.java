package net.riking.web.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.TopicResult;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.UserFollowParams;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.util.FileUtils;

/**
 * 我的关注接口
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/userFollow")
public class AppUserFollowServer {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserService appUserService;

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	TopicService topicService;

	@Autowired
	TopicRelRepo topicRelRepo;

	/**
	 * userId;objType(1-问题，2-话题，3-用户);myUserId
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "关注/人/话题/问题", notes = "POST")
	@RequestMapping(value = "/myFollow", method = RequestMethod.POST)
	public AppResp myFollow_(@RequestBody UserFollowParams userFollowParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())
				|| userFollowParams.getObjType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (userFollowParams.getPindex() == null) {
			userFollowParams.setPindex(0);
		}
		if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
			userFollowParams.setPindex(userFollowParams.getPindex() - 1);
		}
		// 如果toUserId不为空，userId和toUserId互换，userId是对方，toUserId是当前登录用户
		if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
			String userId = "";
			userId = userFollowParams.getUserId();
			userFollowParams.setUserId(userFollowParams.getToUserId());
			userFollowParams.setToUserId(userId);
		}
		int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ? 30
				: userFollowParams.getPcount();

		int pageBegin = userFollowParams.getPindex() * pageCount;

		switch (userFollowParams.getObjType()) {
			// 问题
			case Const.OBJ_TYPE_1:
				List<QuestResult> questResults = tQuestionService.userFollowQuest(userFollowParams.getUserId(),
						pageBegin, pageCount);
				return new AppResp(questResults, CodeDef.SUCCESS);
			// 话题
			case Const.OBJ_TYPE_2:
				List<TopicResult> topicResults = topicService.userFollowTopic(userFollowParams.getUserId(), pageBegin,
						pageCount);
				if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
					List<String> list = topicRelRepo.findByUser(userFollowParams.getToUserId(), 0);// 0-关注
					for (TopicResult topicResult : topicResults) {
						topicResult.setIsFollow(0);// 0-未关注
						if (list.contains(topicResult.getId())) {
							topicResult.setIsFollow(1);// 1-已关注
						}
					}
				}
				return new AppResp(topicResults, CodeDef.SUCCESS);
			// 用户
			case Const.OBJ_TYPE_3:
				List<AppUserResult> userResults = appUserService.userFollowUser(userFollowParams.getUserId(), pageBegin,
						pageCount);
				userResults = appendUrlGrade(userFollowParams.getUserId(), userResults);
				if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
					List<UserFollowRel> followRels = userFollowRelRepo.findByUser(userFollowParams.getToUserId());
					for (AppUserResult appUserResult : userResults) {
						appUserResult.setIsFollow(0);// 未关注
						for (UserFollowRel userFollowRel : followRels) {
							if (userFollowRel.getFollowStatus() == 1
									&& userFollowRel.getToUserId().equals(appUserResult.getId())) {
								appUserResult.setIsFollow(1);// 已关注
							} else if (userFollowRel.getFollowStatus() == 2
									&& userFollowRel.getToUserId().equals(appUserResult.getId())) {
								appUserResult.setIsFollow(2);// 互相关注
							}
						}
					}
				}

				return new AppResp(userResults, CodeDef.SUCCESS);
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
	}

	/**
	 * userId,pindex,pcount
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "我的粉丝", notes = "POST")
	@RequestMapping(value = "/myFans", method = RequestMethod.POST)
	public AppResp myFans_(@RequestBody UserFollowParams userFollowParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (userFollowParams.getPindex() == null) {
			userFollowParams.setPindex(0);
		}
		if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
			userFollowParams.setPindex(userFollowParams.getPindex() - 1);
		}
		int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ? 30
				: userFollowParams.getPcount();

		int pageBegin = userFollowParams.getPindex() * pageCount;

		// 用户
		List<AppUserResult> userResults = appUserService.findMyFans(userFollowParams.getUserId(), pageBegin, pageCount);
		userResults = appendUrlGrade(userFollowParams.getUserId(), userResults);

		return new AppResp(userResults, CodeDef.SUCCESS);

	}

	/**
	 * 加等级，图片地址
	 * @param userId
	 * @param userResults
	 * @return
	 */
	private List<AppUserResult> appendUrlGrade(String userId, List<AppUserResult> userResults) {
		for (AppUserResult appUserResult : userResults) {
			if (null != appUserResult.getPhotoUrl()) {
//				appUserResult
//						.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + appUserResult.getPhotoUrl());
				appUserResult
				.setPhotoUrl(FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + appUserResult.getPhotoUrl());
			}
			// 等级
			if (null != appUserResult.getExperience()) {
				appUserResult.setGrade(appUserService.transformExpToGrade(appUserResult.getExperience()));
			}
		}
		return userResults;
	}

}
