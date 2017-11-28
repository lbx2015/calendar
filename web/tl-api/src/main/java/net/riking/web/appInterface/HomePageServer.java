package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.entity.AppResp;
import net.riking.entity.model.CommonParams;
import net.riking.entity.model.QuestionIgnore;
import net.riking.entity.model.Topic;
import net.riking.entity.model.TopicRel;
import net.riking.service.repo.AppUserRepo;
import net.riking.service.repo.QuestionIgnoreRepo;
import net.riking.service.repo.QuestionInfoRepo;
import net.riking.service.repo.TopicInfoRepo;
import net.riking.util.Utils;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月18日 上午11:40:40
 * @used 首页接口
 */
@RestController
@RequestMapping(value = "/homePageServer")
public class HomePageServer {

	@Autowired
	QuestionIgnoreRepo questionIgnoreRepo;

	@Autowired
	TopicInfoRepo topicInfoRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	QuestionInfoRepo questionInfoRepo;

	/**
	 *
	 * @param params[userId,direct,reqTimeStamp]
	 * @return
	 */
	/*@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(commonParams.getUserId())) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 查询用户自己关注类(话题、问题),自己订阅类,好友的动态,热门话题、问题、回答
			// 分页数据
			List<TopicRel> newsInfoList = new ArrayList<TopicRel>();
			// 把分页数据封装成map传入前台
			List<Map<String, Object>> newsInfoMapList = new ArrayList<Map<String, Object>>();

			List<String> ids = new ArrayList<String>();
			{
				// 1.话题关注表和关注表关联查出用户关注的话题列表
				List<Topic> topicInfos = topicInfoRepo.findByUserId(commonParams.getUserId());
				List<String> questionIdList = new ArrayList<String>();

				for (Topic topicInfo : topicInfos) {
					// 2.根据话题ID(topId)找到话题下面的问题ID(questionId)列表
					List<String> questionId = questionInfoRepo.findQidByTopicId(topicInfo.getId());
					questionIdList.addAll(questionId);
				}
			}
			// {
			// // 1.用户关注表和用户表关联查出关注用户的的列表
			// List<AppUser> appUsers =
			// appUserRepo.findFollowInfoByUserId(commonParams.getUserId());
			// for (AppUser appUser : appUsers) {
			// // 2.根据关注的用户的Id找
			// List<String> questionIdList = qAnswerAgreeInfoRepo.findByUserId(appUser.getId());
			// }
			// // 根据用户Id找到
			// for (AppUser appUser : appUsers) {
			// // 从关注的用户表中找到用户点赞的信息
			//
			// appUser.getId();
			// }
			// }

			// switch (commonParams.getDirect()) {
			// // 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
			// case Const.DIRECT_UP:
			//
			// newsInfoList = newsInfoRepo.findNewsListPageNext(commonParams.getReqTimeStamp(),
			// new PageRequest(0, 30));
			// break;
			// // 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
			// case Const.DIRECT_DOWN:
			// List<News> newsInfoAscList =
			// newsInfoRepo.findNewsListRefresh(commonParams.getReqTimeStamp(),
			// new PageRequest(0, 30));
			// // 把查出来的数据按倒序重新排列
			// for (int i = 0; i < newsInfoAscList.size(); i++) {
			// newsInfoList.add(newsInfoAscList.get(newsInfoAscList.size() - i));
			// }
			// break;
			// default:
			// throw new RuntimeException("请求方向参数异常：direct:" + commonParams.getDirect());
			// }
			// for (News newsInfo : newsInfoList) {
			// // 从数据库查询评论数插到资讯表
			// Integer count = newsCommentInfoRepo.commentCount(newsInfo.getId());
			// newsInfo.setCommentNumber(count);
			// // 将对象转换成map
			// Map<String, Object> newsInfoMapNew = Utils.objProps2Map(newsInfo, true);
			// newsInfoMapList.add(newsInfoMapNew);
			// }

			return new AppResp(newsInfoMapList, CodeDef.SUCCESS);

		}
		return new AppResp(CodeDef.SUCCESS);
	}*/

	/**
	 * 屏蔽问题
	 * @param params[userId,objType,objId,enabled]
	 * @return
	 */
	/*@ApiOperation(value = "屏蔽问题", notes = "POST")
	@RequestMapping(value = "/shieldProblem", method = RequestMethod.POST)
	public AppResp shieldProblem(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);

		switch (commonParams.getObjType()) {
			// 问题
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == commonParams.getEnabled()) {
					// 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
					QuestionIgnore questionIgnore = new QuestionIgnore();
					questionIgnore.setUserId(commonParams.getUserId());
					questionIgnore.setQuestionId(commonParams.getObjId());
					questionIgnoreRepo.save(questionIgnore);
				} else if (Const.INVALID == commonParams.getEnabled()) {
					// 如果传过来是取消屏蔽，把之前一条记录设为无效数据
					questionIgnoreRepo.updInvalid(commonParams.getUserId(), commonParams.getObjId());
				} else {
					throw new RuntimeException("参数异常：enabled=" + commonParams.getEnabled());
				}
				break;
			// // 话题
			// case Const.OBJ_TYPE_2:
			// if (Const.EFFECTIVE == commonParams.getEnabled()) {
			// // TODO 确认是否有话题屏蔽
			// // 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
			// TopicInfo topicInfo = new TopicInfo();
			// topicInfo.setUserId(commonParams.getUserId());
			// topicInfo.setQuestionId(commonParams.getObjId());
			// topicInfoRepo.save(topicInfo);
			// } else if (Const.INVALID == commonParams.getEnabled()) {
			// // 如果传过来是取消屏蔽，把之前一条记录设为无效数据
			// questionIgnoreRepo.updInvalid(commonParams.getUserId(), commonParams.getObjId());
			// } else {
			// throw new RuntimeException("参数异常：enabled=" + commonParams.getEnabled());
			// }
			// break;
			default:
				throw new RuntimeException("对象类型异常，objType=" + commonParams.getObjType());
		}

		return new AppResp(CodeDef.SUCCESS);
	}*/

}
