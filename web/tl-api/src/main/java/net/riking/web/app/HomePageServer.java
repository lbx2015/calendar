package net.riking.web.app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.TQuestionRel;
import net.riking.entity.model.TopicRel;
import net.riking.entity.params.HomeParams;
import net.riking.util.Utils;

/**
 * 首页接口
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/homePage")
public class HomePageServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	// TODO 首页要过滤掉屏蔽的信息
	/**
	 *
	 * @param params[userId,direct,reqTimeStamp]
	 * @return
	 */
	@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody Map<String, Object> params) {
		// // 将map转换成参数对象
		// CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		// // 判断用户id是否为空
		// if (StringUtils.isNotBlank(commonParams.getUserId())) {// 为空 则是未登录 则首页数据显示的是热点数据
		//
		// } else {// 查询用户自己关注类(话题、问题),自己订阅类,好友的动态,热门话题、问题、回答
		// // 分页数据
		// List<TopicFollowInfo> newsInfoList = new ArrayList<TopicFollowInfo>();
		// // 把分页数据封装成map传入前台
		// List<Map<String, Object>> newsInfoMapList = new ArrayList<Map<String, Object>>();
		//
		// List<String> ids = new ArrayList<String>();
		// {
		// // 1.话题关注表和关注表关联查出用户关注的话题列表
		// List<TopicInfo> topicInfos = topicInfoRepo.findByUserId(commonParams.getUserId());
		// List<String> questionIdList = new ArrayList<String>();
		//
		// for (TopicInfo topicInfo : topicInfos) {
		// // 2.根据话题ID(topId)找到话题下面的问题ID(questionId)列表
		// List<String> questionId = questionInfoRepo.findQidByTopicId(topicInfo.getId());
		// questionIdList.addAll(questionId);
		// }
		// }
		// {
		// // 1.用户关注表和用户表关联查出关注用户的的列表
		// List<AppUser> appUsers = appUserRepo.findFollowInfoByUserId(commonParams.getUserId());
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
		//
		// switch (commonParams.getDirect()) {
		// // 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
		// case Const.DIRECT_UP:
		//
		// newsInfoList = newsInfoRepo.findNewsListPageNext(commonParams.getReqTimeStamp(),
		// new PageRequest(0, 30));
		// break;
		// // 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
		// case Const.DIRECT_DOWN:
		// List<NewsInfo> newsInfoAscList =
		// newsInfoRepo.findNewsListRefresh(commonParams.getReqTimeStamp(),
		// new PageRequest(0, 30));
		// // 把查出来的数据按倒序重新排列
		// for (int i = 0; i < newsInfoAscList.size(); i++) {
		// newsInfoList.add(newsInfoAscList.get(newsInfoAscList.size() - i));
		// }
		// break;
		// default:
		// logger.error("请求方向参数异常：direct:" + commonParams.getDirect());
		// throw new RuntimeException("请求方向参数异常：direct:" + commonParams.getDirect());
		// }
		// for (NewsInfo newsInfo : newsInfoList) {
		// // 从数据库查询评论数插到资讯表
		// Integer count = newsCommentInfoRepo.commentCount(newsInfo.getId());
		// newsInfo.setCommentNumber(count);
		// // 将对象转换成map
		// Map<String, Object> newsInfoMapNew = Utils.objProps2Map(newsInfo, true);
		// newsInfoMapList.add(newsInfoMapNew);
		// }
		//
		// return new AppResp(newsInfoMapList, CodeDef.SUCCESS);
		//
		// }
		return new AppResp("", CodeDef.SUCCESS);
	}

	/**
	 * 屏蔽问题
	 * @param params[userId,objType,objId,enabled]
	 * @return
	 */
	@ApiOperation(value = "屏蔽问题", notes = "POST")
	@RequestMapping(value = "/shieldProblem", method = RequestMethod.POST)
	public AppResp shieldProblem(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		HomeParams homeParams = Utils.map2Obj(params, HomeParams.class);

		switch (homeParams.getObjType()) {
			// 问题
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == homeParams.getEnabled()) {
					TQuestionRel rels = tQuestionRelRepo.findByOne(homeParams.getUserId(), homeParams.getObjId(), 3);// 3-屏蔽
					if (null == rels) {
						// 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
						TQuestionRel tQuestionRel = new TQuestionRel();
						tQuestionRel.setUserId(homeParams.getUserId());
						tQuestionRel.setTqId(homeParams.getObjId());
						tQuestionRel.setDataType(3);// 0-关注 3-屏蔽
						tQuestionRelRepo.save(tQuestionRel);
					}
				} else if (Const.INVALID == homeParams.getEnabled()) {
					// 如果传过来是取消屏蔽，把之前一条记录物理删除
					tQuestionRelRepo.deleteByUIdAndTqId(homeParams.getUserId(), homeParams.getObjId(), 3);// 0-关注3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + homeParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 话题
			case Const.OBJ_TYPE_2:
				if (Const.EFFECTIVE == homeParams.getEnabled()) {
					// TODO 确认是否有话题屏蔽
					TopicRel rels = topicRelRepo.findByOne(homeParams.getUserId(), homeParams.getObjId(), 3);// 3-屏蔽
					if (null == rels) {
						// 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
						TopicRel topicRel = new TopicRel();
						topicRel.setUserId(homeParams.getUserId());
						topicRel.setTopicId(homeParams.getObjId());
						topicRel.setDataType(3);// 0-关注；3-屏蔽
						topicRelRepo.save(topicRel);
					}
				} else if (Const.INVALID == homeParams.getEnabled()) {
					// 如果传过来是取消屏蔽，把之前一条记录物理删除
					topicRelRepo.deleteByUIdAndTopId(homeParams.getUserId(), homeParams.getObjId(), 3);// 0-关注3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + homeParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				logger.error("对象类型异常，objType=" + homeParams.getObjType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		return new AppResp("", CodeDef.SUCCESS);
	}

}
