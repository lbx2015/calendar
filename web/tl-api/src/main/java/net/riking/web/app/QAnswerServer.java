package net.riking.web.app;

import java.util.Map;

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
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QAnswerRel;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.params.QAnswerParams;
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

	/**
	 * 问题回答详情
	 * @param params[tqId]
	 * @return
	 */
	@ApiOperation(value = "问题回答详情", notes = "POST")
	@RequestMapping(value = "/getQAnswer", method = RequestMethod.POST)
	public AppResp getQAnswer(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
		QuestionAnswer questionAnswer = questionAnswerRepo.getById(qAnswerParams.getQuestAnswerId());

		Map<String, Object> questionAnswerMap = Utils.objProps2Map(questionAnswer, true);
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
		// 将map转换成参数对象
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
	@RequestMapping(value = "/QAnswerAgree", method = RequestMethod.POST)
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
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(), 1);// 1-点赞2-收藏3-屏蔽
				} else {
					throw new RuntimeException("参数异常：enabled=" + qAnswerParams.getEnabled());
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
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(), 2);// 1-点赞2-收藏3-屏蔽
				} else {
					throw new RuntimeException("参数异常：enabled=" + qAnswerParams.getEnabled());
				}
				break;
			default:
				throw new RuntimeException("参数异常：objType=" + qAnswerParams.getOptType());
		}
		return new AppResp(CodeDef.SUCCESS);
	}

	// /**
	// * 问题回答评论列表[tqId，questAnswerId]
	// * @param params
	// * @return
	// */
	// @ApiOperation(value = "问题回答列表", notes = "POST")
	// @RequestMapping(value = "/qACommentList", method = RequestMethod.POST)
	// public AppResp qACommentList(@RequestBody Map<String, Object> params) {
	// // 将map转换成参数对象
	// QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
	// // 返回到前台的问题回答列表
	//
	// List<QAComment> questionAnswerList =
	// qACommentRepo.findByQaId(qAnswerParams.getQuestAnswerId());
	// for (QAComment qAComment : questionAnswerList) {
	// // TODO 统计数后面从redis中取回答的评论数
	// Integer commentNum = qACommentRepo.commentCount(questionAnswer.getId());
	// questionAnswer.setCommentNum(commentNum);
	// // TODO 统计数后面从redis中取点赞数
	// Integer agreeNum = qACAgreeRelRepo.agreeCount(questionAnswer.getId(), 1);// 1-点赞
	// questionAnswer.setAgreeNum(agreeNum);
	//
	// // 将对象转换成map
	// Map<String, Object> questionAnswerMap = Utils.objProps2Map(questionAnswer, true);
	// questionAnswerMapList.add(questionAnswerMap);
	// }
	//
	// return new AppResp(questionAnswerMapList, CodeDef.SUCCESS);
	// }
}
