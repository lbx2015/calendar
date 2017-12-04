package net.riking.web.app;

import java.util.ArrayList;
import java.util.List;
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
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QAnswerRel;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.params.QAnswerParams;
import net.riking.util.DateUtils;
import net.riking.util.Utils;

/**
 * 
 * 问题的回答接口
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/qAnswer")
public class QAnswerServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NCReplyRepo nCReplyRepo;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	NewsRelRepo newsRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	/**
	 * 问题回答详情
	 * @param params[questAnswerId]
	 * @return
	 */
	@ApiOperation(value = "问题回答详情", notes = "POST")
	@RequestMapping(value = "/getQAnswer", method = RequestMethod.POST)
	public AppResp getQAnswer(@RequestBody Map<String, Object> params) {
		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
		QuestionAnswer questionAnswer = questionAnswerRepo.getById(qAnswerParams.getQuestAnswerId());

		Map<String, Object> questionAnswerMap = Utils.objProps2Map(questionAnswer, true);
		String pattern = "yyyyMMddHHmmssSSS";
		questionAnswerMap.put("createdTime", DateUtils.DateFormatMS(questionAnswer.getCreatedTime(), pattern));
		questionAnswerMap.put("modifiedTime", DateUtils.DateFormatMS(questionAnswer.getModifiedTime(), pattern));
		return new AppResp(questionAnswerMap, CodeDef.SUCCESS);
	}

	/**
	 * 问题回答的评论
	 * @param params[userId,questAnswerId,content]
	 * @return
	 */
	@ApiOperation(value = "问题回答的评论", notes = "POST")
	@RequestMapping(value = "/qACommentPub", method = RequestMethod.POST)
	public AppResp qACommentPub(@RequestBody Map<String, Object> params) {
		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
		QAComment qaComment = new QAComment();
		qaComment.setUserId(qAnswerParams.getUserId());
		qaComment.setQuestionAnswerId(qAnswerParams.getQuestAnswerId());
		qaComment.setContent(qAnswerParams.getContent());
		qaComment.setCreatedBy(qAnswerParams.getUserId());
		qaComment.setModifiedBy(qAnswerParams.getUserId());
		qACommentRepo.save(qaComment);
		return new AppResp(CodeDef.SUCCESS);
	}

	/**
	 * 问题回答的点赞/收藏
	 * @param params[userId,questAnswerId,optType,enabled]
	 * @return
	 */
	@ApiOperation(value = "问题回答的点赞/收藏", notes = "POST")
	@RequestMapping(value = "/agreeOrCollect", method = RequestMethod.POST)
	public AppResp QAnswerAgree(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
		switch (qAnswerParams.getOptType()) {
			// 1-点赞
			case 1:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					// 如果传过来的参数是点赞，保存新的一条关注记录
					QAnswerRel qAnswerRel = new QAnswerRel();
					qAnswerRel.setUserId(qAnswerParams.getUserId());
					qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
					qAnswerRel.setDataType(1);// 点赞
					qAnswerRelRepo.save(qAnswerRel);
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消点赞，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_GREE);// 点赞
				} else {
					logger.error("参数异常：enabled=" + qAnswerParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 2-收藏
			case 2:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					// 如果传过来的参数是收藏，保存新的一条关注记录
					QAnswerRel qAnswerRel = new QAnswerRel();
					qAnswerRel.setUserId(qAnswerParams.getUserId());
					qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
					qAnswerRel.setDataType(2);// 收藏
					qAnswerRelRepo.save(qAnswerRel);
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消收藏，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_COLLECT);// 收藏
				} else {
					logger.error("参数异常：enabled=" + qAnswerParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				logger.error("参数异常：objType=" + qAnswerParams.getOptType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		return new AppResp(CodeDef.SUCCESS);
	}

	/**
	 * TODO 问题回答评论列表[tqId，questAnswerId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题回答评论列表", notes = "POST")
	@RequestMapping(value = "/qACommentList", method = RequestMethod.POST)
	public AppResp qACommentList(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
		// 返回前台问题回答评论的map列表
		List<Map<String, Object>> questionAnswerMapList = new ArrayList<Map<String, Object>>();
		// 返回到前台的问题回答列表
		List<QAComment> questionAnswerList = qACommentRepo.findByQaId(qAnswerParams.getQuestAnswerId());
		for (QAComment qAComment : questionAnswerList) {
			List<QACReply> qacReplies = qACReplyRepo.getByCommentId(qAComment.getId());
			for (QACReply qacReply : qacReplies) {
				// qacReply将对象转换成map
				Map<String, Object> qacReplyMap = Utils.objProps2Map(qacReply, true);
				String pattern = "yyyyMMddHHmmssSSS";
				qacReplyMap.put("createdTime", DateUtils.DateFormatMS(qacReply.getCreatedTime(), pattern));
				qacReplyMap.put("modifiedTime", DateUtils.DateFormatMS(qacReply.getModifiedTime(), pattern));
				qAComment.getQACReplyList().add(qacReplyMap);
			}
			// TODO 统计数后面从redis中取点赞数
			Integer agreeNum = 0;
			agreeNum = qACAgreeRelRepo.agreeCount(qAComment.getId(), Const.OBJ_OPT_GREE);// 点赞
			qAComment.setAgreeNum(agreeNum);

			// 将对象转换成map
			Map<String, Object> questionAnswerMap = Utils.objProps2Map(qAComment, true);
			String pattern = "yyyyMMddHHmmssSSS";
			questionAnswerMap.put("createdTime", DateUtils.DateFormatMS(qAComment.getCreatedTime(), pattern));
			questionAnswerMap.put("modifiedTime", DateUtils.DateFormatMS(qAComment.getModifiedTime(), pattern));
			questionAnswerMapList.add(questionAnswerMap);
		}

		return new AppResp(questionAnswerMapList, CodeDef.SUCCESS);
	}
}
