package net.riking.web.app;

import org.apache.commons.lang.StringUtils;
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
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.NCAgreeRel;
import net.riking.entity.model.NCReply;
import net.riking.entity.model.QACAgreeRel;
import net.riking.entity.model.QACReply;
import net.riking.entity.params.CommentParams;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;

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
	QACommentRepo qACommentRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	AppUserRepo appUserRepo;

	/**
	 * 评论点赞
	 * @param params[userId,commentId（评论ID）,objType(1-回答类；2-资讯类),enabled(0-取消；1-赞同)]
	 * @return
	 */
	@ApiOperation(value = "评论点赞", notes = "POST")
	@RequestMapping(value = "/commentAgree", method = RequestMethod.POST)
	public AppResp commentAgree_(@RequestBody CommentParams commentParams) {
		// 将map转换成参数对象
		if (commentParams.getObjType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(commentParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(commentParams.getCommentId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (commentParams.getEnabled() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						QACAgreeRel rels = qACAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), 1);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							QACAgreeRel qACAgreeRel = new QACAgreeRel();
							qACAgreeRel.setUserId(commentParams.getUserId());
							qACAgreeRel.setQacId(commentParams.getCommentId());
							qACAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							qACAgreeRelRepo.save(qACAgreeRel);
						}
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
						NCAgreeRel rels = nCAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), 1);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							NCAgreeRel nCAgreeRel = new NCAgreeRel();
							nCAgreeRel.setUserId(commentParams.getUserId());
							nCAgreeRel.setNcId(commentParams.getCommentId());
							nCAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							nCAgreeRelRepo.save(nCAgreeRel);
						}
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

	/**
	 * 评论的回复和回复的回复
	 * @param params[fromUserId,objType(1-回答类；2-资讯类),commentId(评论ID),replyId（回复ID）,toUserId,content]
	 * @return
	 */
	@ApiOperation(value = "评论的回复和回复的回复", notes = "POST")
	@RequestMapping(value = "/commentReply", method = RequestMethod.POST)
	public AppResp commentReply_(@RequestBody CommentParams commentParams) {

		AppUser appUser = appUserRepo.findOne(commentParams.getUserId());
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				QACReply qACReply = new QACReply();
				qACReply.setFromUserId(commentParams.getUserId());
				qACReply.setCreatedBy(commentParams.getUserId());
				qACReply.setModifiedBy(commentParams.getUserId());
				qACReply.setCommentId(commentParams.getCommentId());
				qACReply.setContent(commentParams.getContent());
				qACReply.setReplyId(commentParams.getReplyId());
				qACReply.setToUserId(commentParams.getToUserId());
				qACReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				qACReply = qACReplyRepo.save(qACReply);
				if (null != appUser) {
					FromUser fromUser = new FromUser();
					fromUser.setUserId(commentParams.getUserId());
					fromUser.setUserName(appUser.getUserName());
					qACReply.setFromUser(fromUser);
				}
				if (null != commentParams.getToUserId()) {
					AppUser apptoUser = appUserRepo.findOne(commentParams.getToUserId());
					if (null != apptoUser) {
						ToUser toUser = new ToUser();
						toUser.setUserId(commentParams.getToUserId());
						toUser.setUserName(apptoUser.getUserName());
						qACReply.setToUser(toUser);
					}
				}
				// qACReply.setFromUserId(null);
				// qACReply.setToUserId(null);
				return new AppResp(qACReply, CodeDef.SUCCESS);
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				NCReply ncReply = new NCReply();
				ncReply.setFromUserId(commentParams.getUserId());
				ncReply.setCreatedBy(commentParams.getUserId());
				ncReply.setModifiedBy(commentParams.getUserId());
				ncReply.setCommentId(commentParams.getCommentId());
				ncReply.setContent(commentParams.getContent());
				ncReply.setReplyId(commentParams.getReplyId());
				ncReply.setToUserId(commentParams.getToUserId());
				ncReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				ncReply = nCReplyRepo.save(ncReply);
				if (null != appUser) {
					FromUser fromUser = new FromUser();
					fromUser.setUserId(commentParams.getUserId());
					fromUser.setUserName(appUser.getUserName());
					ncReply.setFromUser(fromUser);
				}
				if (null != commentParams.getToUserId()) {
					AppUser apptoUser = appUserRepo.findOne(commentParams.getToUserId());
					if (null != apptoUser) {
						ToUser toUser = new ToUser();
						toUser.setUserId(commentParams.getToUserId());
						toUser.setUserName(apptoUser.getUserName());
						ncReply.setToUser(toUser);
					}
				}
				// ncReply.setUserId(null);
				// ncReply.setToUserId(null);
				return new AppResp(ncReply, CodeDef.SUCCESS);
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
	}

}
