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
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.NCAgreeRel;
import net.riking.entity.model.QACAgreeRel;
import net.riking.entity.params.CommentParams;
import net.riking.util.Utils;

/**
 * 
 * 〈评论接口〉
 *
 * 〈评论类（回答、资讯）〉
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/comment")
public class CommentServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NCReplyRepo nCReplyRepo;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	NewsRelRepo newsRelRepo;

	/**
	 * 评论点赞
	 * @param params[userId,commentId（评论ID）,objType(1-回答类；2-资讯类),enabled(0-取消；1-赞同)]
	 * @return
	 */
	@ApiOperation(value = "评论点赞", notes = "POST")
	@RequestMapping(value = "/commentAgree", method = RequestMethod.POST)
	public AppResp commentAgree_(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		CommentParams commentParams = Utils.map2Obj(params, CommentParams.class);
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						// 如果传过来的参数是点赞，保存新的一条收藏记录
						QACAgreeRel qACAgreeRel = new QACAgreeRel();
						qACAgreeRel.setUserId(commentParams.getUserId());
						qACAgreeRel.setQacId(commentParams.getCommentId());
						qACAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
						qACAgreeRelRepo.save(qACAgreeRel);
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						qACAgreeRelRepo.deleteByUIdAndQaId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						// 如果传过来的参数是点赞，保存新的一条收藏记录
						NCAgreeRel nCAgreeRel = new NCAgreeRel();
						nCAgreeRel.setUserId(commentParams.getUserId());
						nCAgreeRel.setNcId(commentParams.getCommentId());
						nCAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
						nCAgreeRelRepo.save(nCAgreeRel);
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						nCAgreeRelRepo.deleteByUIdAndNcId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				break;
		}
		return new AppResp(CodeDef.SUCCESS);
	}

	// /**
	// * 资讯的详情
	// * @param params[newsId]
	// * @return
	// */
	// @ApiOperation(value = "获取资讯的详情", notes = "POST")
	// @RequestMapping(value = "/getNews", method = RequestMethod.POST)
	// public AppResp getNewsInfo(@RequestBody Map<String, Object> params) {
	// // 将map转换成参数对象
	// NewsParams newsParams = Utils.map2Obj(params, NewsParams.class);
	// News newsInfo = newsRepo.getById(newsParams.getNewsId());
	// // 将对象转换成map
	// Map<String, Object> newsInfoMap = Utils.objProps2Map(newsInfo, true);
	// return new AppResp(newsInfoMap, CodeDef.SUCCESS);
	// }

	// /**
	// * 资讯详情评论列表
	// * @param params[newsId]
	// * @return
	// */
	// @ApiOperation(value = "获取资讯详情评论列表", notes = "POST")
	// @RequestMapping(value = "/findNewsCommentList", method = RequestMethod.POST)
	// public AppResp findNewsCommentList(@RequestBody Map<String, Object> params) {
	// // 将map转换成参数对象
	// NewsParams newsParams = Utils.map2Obj(params, NewsParams.class);
	// // 资讯详情评论的Map列表(把资讯详情评论对象封装成Map传进前台)
	// List<Map<String, Object>> newsCommentInfoMapList = new ArrayList<Map<String, Object>>();
	// // 根据NewsId查出资讯详情评论列表（30条）
	// List<NewsComment> newsCommentInfoList = newsCommentRepo.findByNewsId(newsParams.getNewsId(),
	// new PageRequest(0, 30));
	//
	// // 评论列表
	// for (NewsComment newsCommentInfoNew : newsCommentInfoList) {
	// // 根据评论id取到回复列表
	// List<NCReply> nCommentReplyInfoList =
	// nCReplyRepo.findByNewsCommentId(newsCommentInfoNew.getId());
	// // 回复列表
	// for (NCReply nCommentReplyInfo : nCommentReplyInfoList) {
	// Map<String, Object> nCommentReplyInfoObjMap = new HashMap<String, Object>();
	// // TODO AppUser appUser = appUserRepo.findOne(nCommentReplyInfo.getUserId());
	// // nCommentReplyInfo.setUserName(appUser.getUserName());
	// // 将评论回复对象转换成map
	// nCommentReplyInfoObjMap = Utils.objProps2Map(nCommentReplyInfo, true);
	// // 回复的数据列表添加到评论类里面
	// newsCommentInfoNew.getNCommentReplyInfoList().add(nCommentReplyInfoObjMap);
	// }
	// // 点赞数 TODO 后面从redis里面去找
	// Integer agree = 0;
	// agree = nCAgreeRelRepo.agreeCount(newsCommentInfoNew.getId(), 1);// 1-点赞
	// newsCommentInfoNew.setAgreeNumber(agree);
	// Map<String, Object> newsCommentInfoObjMap = Utils.objProps2Map(newsCommentInfoNew, true);
	// newsCommentInfoMapList.add(newsCommentInfoObjMap);
	// }
	// return new AppResp(newsCommentInfoMapList, CodeDef.SUCCESS);
	// }
	//
	// /**
	// * 资讯评论发布
	// * @param params[userId,newsId,content]
	// * @return
	// */
	// @ApiOperation(value = "资讯评论发布", notes = "POST")
	// @RequestMapping(value = "/newsCommentPub", method = RequestMethod.POST)
	// public AppResp newsCommentPub(@RequestBody Map<String, Object> params) {
	// // 将map转换成参数对象
	// NewsParams newsParams = Utils.map2Obj(params, NewsParams.class);
	// NewsComment newsCommentInfo = new NewsComment();
	// newsCommentInfo.setUserId(newsParams.getUserId());
	// newsCommentInfo.setNewsId(newsParams.getNewsId());
	// newsCommentInfo.setContent(newsParams.getContent());
	// newsCommentInfo.setIsAduit(0);// 0-未审核，1-已审核,2-不通过
	// newsCommentRepo.save(newsCommentInfo);
	// return new AppResp(CodeDef.SUCCESS);
	// }
	//
	// /**
	// * 资讯收藏
	// * @param params[userId,newsId,enabled]
	// * @return
	// */
	// @ApiOperation(value = "资讯收藏", notes = "POST")
	// @RequestMapping(value = "/newsCollect", method = RequestMethod.POST)
	// public AppResp newsCollect(@RequestBody Map<String, Object> params) {
	// // 将map转换成参数对象
	// NewsParams newsParams = Utils.map2Obj(params, NewsParams.class);
	//
	// switch (newsParams.getEnabled()) {
	// case Const.EFFECTIVE:
	// // 如果传过来的参数是收藏，保存新的一条收藏记录
	// NewsRel newsRel = new NewsRel();
	// newsRel.setUserId(newsParams.getUserId());
	// newsRel.setNewsId(newsParams.getNewsId());
	// newsRelRepo.save(newsRel);
	// break;
	// case Const.INVALID:
	// // 如果传过来是取消收藏，把之前一条记录物理删除
	// newsRelRepo.deleteByUIdAndNId(newsParams.getUserId(), newsParams.getNewsId(), 2);// 2-收藏
	// break;
	// default:
	// logger.error("参数异常：enabled=" + newsParams.getEnabled());
	// return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
	// }
	// return new AppResp(CodeDef.SUCCESS);
	// }

}
