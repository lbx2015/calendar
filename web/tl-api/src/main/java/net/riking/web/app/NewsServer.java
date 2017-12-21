package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.NCReply;
import net.riking.entity.model.News;
import net.riking.entity.model.NewsComment;
import net.riking.entity.model.NewsRel;
import net.riking.entity.params.NewsParams;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;
import net.riking.service.AppUserService;
import net.riking.util.DateUtils;

/**
 * 
 * 〈行业资讯〉
 *
 * 〈资讯列表，资讯详情，资讯详情评论列表，资讯评论发布，资讯收藏〉
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/news")
public class NewsServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	NewsRepo newsRepo;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NCReplyRepo nCReplyRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	NewsRelRepo newsRelRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	AppUserService appUserService;

	/**
	 * 获取资讯列表
	 * @param params[direct,reqTimeStamp]
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取资讯列表", notes = "POST")
	@RequestMapping(value = "/findNewsList", method = RequestMethod.POST)
	public AppResp findNewsList(@RequestBody NewsParams newsParams) throws ParseException {
		if (newsParams == null) {
			newsParams = new NewsParams();
			newsParams.setReqTimeStamp(new Date());
			newsParams.setDirect(Const.DIRECT_UP);
		}
		if (newsParams.getReqTimeStamp() == null || "".equals(newsParams.getReqTimeStamp())) {
			newsParams.setReqTimeStamp(DateUtils.StringFormatMS(DateUtils.DateFormatMS(new Date(), "yyyyMMddHHmmssSSS"),
					"yyyyMMddHHmmssSSS"));
			newsParams.setDirect(Const.DIRECT_UP);
		}
		if (newsParams.getDirect() == null) {
			newsParams.setDirect(Const.DIRECT_UP);
		}

		// 分页数据
		List<News> newsInfoList = new ArrayList<News>();
		switch (newsParams.getDirect()) {
			// 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
			case Const.DIRECT_UP:
				// 查询查出前30条数据
				newsInfoList = newsRepo.findNewsListPageNext(newsParams.getReqTimeStamp(), new PageRequest(0, 30));
				break;
			// 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
			case Const.DIRECT_DOWN:
				// 查询查出前30条数据
				List<News> newsInfoAscList = newsRepo.findNewsListRefresh(newsParams.getReqTimeStamp(),
						new PageRequest(0, 30));
				// 把查出来的数据按倒序重新排列
				Collections.sort(newsInfoAscList, new Comparator<News>() {

					@Override
					public int compare(News o1, News o2) {
						if (o2.getCreatedTime().after(o1.getCreatedTime())) {
							return -1;
						} else {
							return 1;
						}
					}
				});
				newsInfoList = newsInfoAscList;
				break;
			default:
				logger.error("请求方向参数异常：direct:" + newsParams.getDirect());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		// if (null == newsInfoList) {
		// return new AppResp(CodeDef.EMP.DATE_FOUND_EMPTY, CodeDef.EMP.DATE_FOUND_EMPTY_DESC);
		// }
		for (News newsInfo : newsInfoList) {
			// TODO 从数据库查询评论数插到资讯表,后面从redis里面找
			int count = 0;
			count = newsCommentRepo.commentCount(newsInfo.getId());
			newsInfo.setCommentNumber(count);
			// 截取资源访问路径
			if (null != newsInfo.getPhotoUrl()) {
				newsInfo.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + newsInfo.getPhotoUrl());
			}
			// 等级
			if (null != newsInfo.getExperience()) {
				newsInfo.setGrade(appUserService.transformExpToGrade(newsInfo.getExperience()));
			}
		}

		return new AppResp(newsInfoList, CodeDef.SUCCESS);
	}

	/**
	 * 资讯的详情
	 * @param params[newsId]
	 * @return
	 */
	@ApiOperation(value = "获取资讯的详情", notes = "POST")
	@RequestMapping(value = "/getNews", method = RequestMethod.POST)
	public AppResp getNewsInfo(@RequestBody NewsParams newsParams) {
		News newsInfo = newsRepo.getById(newsParams.getNewsId());
		if (newsInfo == null) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		// TODO 从数据库查询评论数插到资讯表,后面从redis里面找
		Integer count = 0;
		count = newsCommentRepo.commentCount(newsInfo.getId());
		newsInfo.setCommentNumber(count);
		newsInfo.setIsCollect(0);// 0-未收藏
		if (StringUtils.isNotBlank(newsParams.getUserId())) {
			List<String> newsIds = newsRelRepo.findByUserId(newsParams.getUserId(), 2);// 收藏
			for (String newsId : newsIds) {
				if (newsInfo.getId().equals(newsId)) {
					newsInfo.setIsCollect(1);// 1-已收藏
				}
			}
		}
		// 截取资源访问路径
		if (null != newsInfo.getPhotoUrl()) {
			newsInfo.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + newsInfo.getPhotoUrl());
		}
		// 等级
		if (null != newsInfo.getExperience()) {
			newsInfo.setGrade(appUserService.transformExpToGrade(newsInfo.getExperience()));
		}
		return new AppResp(newsInfo, CodeDef.SUCCESS);
	}

	/**
	 * 资讯详情评论列表
	 * @param params[userId,newsId]
	 * @return
	 */
	@ApiOperation(value = "获取资讯详情评论列表", notes = "POST")
	@RequestMapping(value = "/findNewsCommentList", method = RequestMethod.POST)
	public AppResp findNewsCommentList(@RequestBody NewsParams newsParams) {
		// 根据NewsId查出资讯详情评论列表（30条）
		List<NewsComment> newsCommentInfoList = newsCommentRepo.findByNewsId(newsParams.getNewsId(),
				newsParams.getUserId(), new PageRequest(0, 30));

		// 评论列表
		for (NewsComment newsCommentInfoNew : newsCommentInfoList) {
			if (null != newsCommentInfoNew.getPhotoUrl()) {
				newsCommentInfoNew.setPhotoUrl(
						appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + newsCommentInfoNew.getPhotoUrl());
			}
			// 等级
			if (null != newsCommentInfoNew.getExperience()) {
				newsCommentInfoNew.setGrade(appUserService.transformExpToGrade(newsCommentInfoNew.getExperience()));
			}
			// 根据评论id取到回复列表
			List<NCReply> nCommentReplyInfoList = nCReplyRepo.findByNewsCommentId(newsCommentInfoNew.getId());
			// 回复列表
			for (NCReply nCommentReplyInfo : nCommentReplyInfoList) {
				FromUser fromUser = new FromUser();
				fromUser.setUserId(nCommentReplyInfo.getFromUserId());
				fromUser.setUserName(nCommentReplyInfo.getUserName());
				nCommentReplyInfo.setFromUser(fromUser);
				if (null != nCommentReplyInfo.getToUserId()) {
					ToUser toUser = new ToUser();
					toUser.setUserId(nCommentReplyInfo.getToUserId());
					toUser.setUserName(nCommentReplyInfo.getToUserName());
					nCommentReplyInfo.setToUser(toUser);
				}

			}
			// 回复的数据列表添加到评论类里面
			newsCommentInfoNew.setNcReplyList(nCommentReplyInfoList);
			// 点赞数 TODO 后面从redis里面去找
			Integer agree = 0;
			agree = nCAgreeRelRepo.agreeCount(newsCommentInfoNew.getId(), 1);// 1-点赞
			newsCommentInfoNew.setAgreeNumber(agree);
		}
		return new AppResp(newsCommentInfoList, CodeDef.SUCCESS);
	}

	/**
	 * 资讯评论发布
	 * @param params[userId,newsId,content]
	 * @return
	 */
	@ApiOperation(value = "资讯评论发布", notes = "POST")
	@RequestMapping(value = "/newsCommentPub", method = RequestMethod.POST)
	public AppResp newsCommentPub(@RequestBody NewsParams newsParams) {
		NewsComment newsCommentInfo = new NewsComment();
		AppUser appUser = appUserRepo.findOne(newsParams.getUserId());
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(newsParams.getUserId());

		newsCommentInfo.setUserId(newsParams.getUserId());
		newsCommentInfo.setNewsId(newsParams.getNewsId());
		newsCommentInfo.setContent(newsParams.getContent());
		newsCommentInfo.setIsAduit(0);// 0-未审核，1-已审核,2-不通过
		newsCommentInfo = newsCommentRepo.save(newsCommentInfo);

		if (null != appUser) {
			newsCommentInfo.setUserName(appUser.getUserName());
		}
		if (null != appUserDetail) {
			newsCommentInfo.setPhotoUrl(appUserDetail.getPhotoUrl());
			newsCommentInfo.setExperience(appUserDetail.getExperience());
		}
		newsCommentInfo.setIsAgree(0);// 0-未点赞
		if (StringUtils.isNotBlank(newsParams.getUserId())) {
			List<String> ncIds = nCAgreeRelRepo.findByUserId(newsParams.getUserId(), 1);// 点赞
			for (String ncId : ncIds) {
				if (newsCommentInfo.getId().equals(ncId)) {
					newsCommentInfo.setIsAgree(1);// 1-已点赞
				}
			}
		}
		if (null != newsCommentInfo.getPhotoUrl()) {
			newsCommentInfo
					.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + newsCommentInfo.getPhotoUrl());
		}
		// 等级
		if (null != newsCommentInfo.getExperience()) {
			newsCommentInfo.setGrade(appUserService.transformExpToGrade(newsCommentInfo.getExperience()));
		}
		return new AppResp(newsCommentInfo, CodeDef.SUCCESS);
	}

	/**
	 * 资讯收藏
	 * @param params[userId,newsId,enabled]
	 * @return
	 */
	@ApiOperation(value = "资讯收藏", notes = "POST")
	@RequestMapping(value = "/newsCollect", method = RequestMethod.POST)
	public AppResp newsCollect(@RequestBody NewsParams newsParams) {
		if (StringUtils.isBlank(newsParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(newsParams.getNewsId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (newsParams.getEnabled() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		switch (newsParams.getEnabled()) {
			case Const.EFFECTIVE:
				NewsRel rels = newsRelRepo.findByOne(newsParams.getUserId(), newsParams.getNewsId(), 2);// 2-收藏
				if (null == rels) {
					// 如果传过来的参数是收藏，保存新的一条收藏记录
					NewsRel newsRel = new NewsRel();
					newsRel.setUserId(newsParams.getUserId());
					newsRel.setNewsId(newsParams.getNewsId());
					newsRel.setDataType(2);
					newsRelRepo.save(newsRel);
				}
				break;
			case Const.INVALID:
				// 如果传过来是取消收藏，把之前一条记录物理删除
				newsRelRepo.deleteByUIdAndNId(newsParams.getUserId(), newsParams.getNewsId(), 2);// 2-收藏
				break;
			default:
				logger.error("参数异常：enabled=" + newsParams.getEnabled());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		return new AppResp(Const.EMPTY,CodeDef.SUCCESS);
	}

}
