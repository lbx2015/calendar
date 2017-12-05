package net.riking.web.app;

import java.util.HashMap;
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
import net.riking.entity.model.NCReply;
import net.riking.entity.model.QACReply;
import net.riking.entity.params.CommentParams;
import net.riking.util.Utils;

/**
 * 
 * 〈回复接口〉
 *
 * 〈回复类〉
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/reply")
public class ReplyServer {
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
	 * 回复的回复
	 * @param params[userId,objType(1-回答类；2-资讯类),commentId(评论ID),replyId（回复ID）,toUserId,content]
	 * @return
	 */
	@ApiOperation(value = "回复的回复", notes = "POST")
	@RequestMapping(value = "/replyToReply", method = RequestMethod.POST)
	public AppResp replyToReply_(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		CommentParams commentParams = Utils.map2Obj(params, CommentParams.class);
		if (commentParams.getToUserId() == null || commentParams.getReplyId() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		AppUser appUser = appUserRepo.findOne(commentParams.getUserId());
		AppUser apptoUser = appUserRepo.findOne(commentParams.getToUserId());
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				QACReply qACReply = new QACReply();
				qACReply.setUserId(commentParams.getUserId());
				qACReply.setCreatedBy(commentParams.getUserId());
				qACReply.setModifiedBy(commentParams.getUserId());
				qACReply.setCommentId(commentParams.getCommentId());
				qACReply.setContent(commentParams.getContent());
				qACReply.setReplyId(commentParams.getReplyId());
				qACReply.setToUserId(commentParams.getToUserId());
				qACReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				qACReply = qACReplyRepo.save(qACReply);
				if (null != appUser) {
					qACReply.setUserName(appUser.getUserName());
				}
				if (null != apptoUser) {

					qACReply.setToUserName(apptoUser.getUserName());
				}
				map = Utils.objProps2Map(qACReply, true);
				break;
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				NCReply ncReply = new NCReply();
				ncReply.setUserId(commentParams.getUserId());
				ncReply.setCreatedBy(commentParams.getUserId());
				ncReply.setModifiedBy(commentParams.getUserId());
				ncReply.setCommentId(commentParams.getCommentId());
				ncReply.setContent(commentParams.getContent());
				ncReply.setReplyId(commentParams.getReplyId());
				ncReply.setToUserId(commentParams.getToUserId());
				ncReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				ncReply = nCReplyRepo.save(ncReply);
				if (null != appUser) {
					ncReply.setUserName(appUser.getUserName());
				}
				if (null != apptoUser) {
					ncReply.setToUserName(apptoUser.getUserName());
				}
				map = Utils.objProps2Map(ncReply, true);
				break;
			default:
				break;
		}
		return new AppResp(map, CodeDef.SUCCESS);
	}

}
