package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.CommonParams;
import net.riking.entity.model.NCommentReplyInfo;
import net.riking.entity.model.NewsCollectInfo;
import net.riking.entity.model.NewsCommentInfo;
import net.riking.entity.model.NewsInfo;
import net.riking.service.repo.AppUserRepo;
import net.riking.service.repo.NCommentAgreeInfoRepo;
import net.riking.service.repo.NCommentReplyInfoRepo;
import net.riking.service.repo.NewsCollectInfoRepo;
import net.riking.service.repo.NewsCommentInfoRepo;
import net.riking.service.repo.NewsInfoRepo;
import net.riking.util.Utils;

/**
 * 
 * 〈行业资讯〉
 *
 * <p>
 * 〈资讯列表，资讯详情，资讯详情评论列表，资讯评论发布，资讯收藏〉
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/newsApp")
public class NewsInfoServer {

	@Autowired
	NewsInfoRepo newsInfoRepo;

	@Autowired
	NewsCommentInfoRepo newsCommentInfoRepo;

	@Autowired
	NewsCollectInfoRepo newsCollectInfoRepo;

	@Autowired
	NCommentReplyInfoRepo nCommentReplyInfoRepo;

	@Autowired
	NCommentAgreeInfoRepo nCommentAgreeInfoRepo;

	@Autowired
	AppUserRepo appUserRepo;

	/**
	 * 获取资讯列表
	 * @param commonParams
	 * @return
	 */
	@ApiOperation(value = "获取资讯列表", notes = "POST")
	@RequestMapping(value = "/findNewsList", method = RequestMethod.POST)
	public AppResp findNewsList(@RequestBody Map<String, String> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		// 分页数据
		List<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();
		// 把分页数据封装成map传入前台
		List<Map<String, String>> newsInfoMapList = new ArrayList<Map<String, String>>();
		switch (commonParams.getDirect()) {
			// 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
			case Const.DIRECT_UP:
				// 查询查出前30条数据
				newsInfoList = newsInfoRepo.findNewsListPageNext(commonParams.getReqTimeStamp(),
						new PageRequest(0, 30));
				break;
			// 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
			case Const.DIRECT_DOWN:
				List<NewsInfo> newsInfoAscList = newsInfoRepo.findNewsListRefresh(commonParams.getReqTimeStamp(),
						new PageRequest(0, 30));
				// 把查出来的数据按倒序重新排列
				for (int i = 0; i < newsInfoAscList.size(); i++) {
					newsInfoList.add(newsInfoAscList.get(newsInfoAscList.size() - i));
				}
				break;
			default:
				throw new RuntimeException("请求方向参数异常：direct:" + commonParams.getDirect());
		}
		for (NewsInfo newsInfo : newsInfoList) {
			// 从数据库查询评论数插到资讯表
			Integer count = newsCommentInfoRepo.commentCount(newsInfo.getId());
			newsInfo.setCommentNumber(count);
			// 将对象转换成map
			Map<String, String> newsInfoMapNew = Utils.objProps2Map(newsInfo, true);
			newsInfoMapList.add(newsInfoMapNew);
		}

		return new AppResp(newsInfoMapList, CodeDef.SUCCESS);
	}

	/**
	 * 资讯的详情
	 * @param commonParamsjavascript:;
	 * @return
	 */
	@ApiOperation(value = "获取资讯的详情", notes = "POST")
	@RequestMapping(value = "/getNewsInfo", method = RequestMethod.POST)
	public AppResp getNewsInfo(@RequestBody Map<String, String> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		NewsInfo newsInfo = newsInfoRepo.getById(commonParams.getNewsId());
		// 将对象转换成map
		Map<String, String> newsInfoMap = Utils.objProps2Map(newsInfo, true);
		return new AppResp(newsInfoMap, CodeDef.SUCCESS);
	}

	/**
	 * 资讯详情评论列表
	 * @param commonParams
	 * @return
	 */
	@ApiOperation(value = "获取资讯详情评论列表", notes = "POST")
	@RequestMapping(value = "/findNewsCommentInfo", method = RequestMethod.POST)
	public AppResp findNewsCommentInfo(@RequestBody Map<String, String> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		// 资讯详情评论的Map列表(把资讯详情评论对象封装成Map传进前台)
		List<Map<String, String>> newsCommentInfoMapList = new ArrayList<Map<String, String>>();
		// 根据NewsId查出资讯详情评论列表（30条）
		List<NewsCommentInfo> newsCommentInfoList = newsCommentInfoRepo.findByNewsId(commonParams.getNewsId(),
				new PageRequest(0, 30));

		// 评论列表
		for (NewsCommentInfo newsCommentInfoNew : newsCommentInfoList) {
			// 回复的数据列表
			List<Map<String, String>> nCommentReplyInfoObjMapList = new ArrayList<Map<String, String>>();
			// 根据评论id取到回复列表
			List<NCommentReplyInfo> nCommentReplyInfoList = nCommentReplyInfoRepo
					.findByNewsCommentId(newsCommentInfoNew.getId());
			// 回复列表
			for (NCommentReplyInfo nCommentReplyInfo : nCommentReplyInfoList) {
				Map<String, String> nCommentReplyInfoObjMap = new HashMap<String, String>();
				AppUser appUser = appUserRepo.findOne(nCommentReplyInfo.getUserId());
				nCommentReplyInfo.setUserName(appUser.getName());
				// 将评论回复对象转换成map
				nCommentReplyInfoObjMap = Utils.objProps2Map(nCommentReplyInfo, true);
				nCommentReplyInfoObjMapList.add(nCommentReplyInfoObjMap);
			}
			AppUser appUser = appUserRepo.findOne(newsCommentInfoNew.getUserId());
			newsCommentInfoNew.setUserName(appUser.getName());
			// 点赞数
			Integer agree = nCommentAgreeInfoRepo.commentCount(newsCommentInfoNew.getId());
			newsCommentInfoNew.setAgreeNumber(agree);
			Map<String, String> newsCommentInfoObjMap = Utils.objProps2Map(newsCommentInfoNew, true);
			// TODO map里面装回复list的什么形式? json字符串还是toString,待确认
			newsCommentInfoMapList.add(newsCommentInfoObjMap);
		}
		return new AppResp(newsCommentInfoMapList, CodeDef.SUCCESS);
	}

	/**
	 * 资讯评论发布
	 * @param commonParams
	 * @return
	 */
	@ApiOperation(value = "资讯评论发布", notes = "POST")
	@RequestMapping(value = "/newsCommentInfoPublish", method = RequestMethod.POST)
	public AppResp newsCommentInfoPublish(@RequestBody Map<String, String> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		NewsCommentInfo newsCommentInfo = new NewsCommentInfo();
		newsCommentInfo.setUserId(commonParams.getUserId());
		newsCommentInfo.setNewsId(commonParams.getNewsId());
		newsCommentInfo.setContent(commonParams.getContent());
		newsCommentInfoRepo.save(newsCommentInfo);
		return new AppResp(CodeDef.SUCCESS);
	}

	/**
	 * 资讯收藏
	 * @param commonParams
	 * @return
	 */
	@ApiOperation(value = "资讯收藏", notes = "POST")
	@RequestMapping(value = "/newsCollect", method = RequestMethod.POST)
	public AppResp newsCollect(@RequestBody Map<String, String> params) {
		// 将map转换成参数对象
		CommonParams commonParams = Utils.map2Obj(params, CommonParams.class);
		if (Const.EFFECTIVE.toString().equals(commonParams.getEnabled())) {
			// 如果传过来的参数是收藏，保存新的一条收藏记录
			NewsCollectInfo newsCollectInfo = new NewsCollectInfo();
			newsCollectInfo.setUserId(commonParams.getUserId());
			newsCollectInfo.setNewsId(commonParams.getNewsId());
			newsCollectInfoRepo.save(newsCollectInfo);
		} else if (Const.INVALID.toString().equals(commonParams.getEnabled())) {
			// 如果传过来是取消收藏，把之前一条记录设为无效数据
			newsCollectInfoRepo.updInvalid(commonParams.getUserId(), commonParams.getNewsId());
		} else {
			throw new RuntimeException("参数异常：enabled=" + commonParams.getEnabled());
		}
		return new AppResp(CodeDef.SUCCESS);
	}

}
