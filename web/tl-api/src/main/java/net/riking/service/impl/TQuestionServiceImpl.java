package net.riking.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.TQuestionDao;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.TQuestionRel;
import net.riking.entity.model.TQuestionResult;
import net.riking.entity.model.TopicRel;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.TQuestionParams;
import net.riking.service.TQuestionService;
import net.riking.util.Utils;

@Service("tQuestionService")
@Transactional
public class TQuestionServiceImpl implements TQuestionService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	TQuestionDao tQuestionDao;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	@Override
	public List<TQuestionResult> findTopicHomeUp(String userId, Date reqTimeStamp, String tqIds, int start, int end) {

		return tQuestionDao.findTopicHomeUp(userId, reqTimeStamp, tqIds, start, end);

	}

	@Override
	public List<TQuestionResult> findTopicHomeDown(String userId, Date reqTimeStamp, String tqIds, int start, int end) {
		return tQuestionDao.findTopicHomeDown(userId, reqTimeStamp, tqIds, start, end);
	}

	@Override
	public List<QAnswerResult> findEssenceByTid(String topicId, String userId, int start, int pageCount) {
		return tQuestionDao.findEssenceByTid(topicId, userId, start, pageCount);
	}

	@Override
	public List<QAExcellentResp> findExcellentResp(String topicId, int start, int pageCount) {
		return tQuestionDao.findExcellentResp(topicId, start, pageCount);
	}

	@Override
	public List<QuestResult> userFollowQuest(String userId, int start, int pageCount) {
		return tQuestionDao.userFollowQuest(userId, start, pageCount);
	}

	@Override
	public void follow(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		TQuestionParams tQuestionParams = new TQuestionParams();
		tQuestionParams = (TQuestionParams) Utils.fromObjToObjValue(optCommon, tQuestionParams);
		switch (tQuestionParams.getObjType()) {
			// 问题关注
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					TQuestionRel rels = tQuestionRelRepo.findByOne(tQuestionParams.getUserId(),
							tQuestionParams.getAttentObjId(), Const.OBJ_OPT_FOLLOW);// 0-关注
					if (null == rels) {
						// 如果传过来的参数是关注，保存新的一条关注记录
						TQuestionRel topQuestionRel = new TQuestionRel();
						topQuestionRel.setUserId(tQuestionParams.getUserId());
						topQuestionRel.setTqId(tQuestionParams.getAttentObjId());
						topQuestionRel.setDataType(Const.OBJ_OPT_FOLLOW);// 关注
						tQuestionRelRepo.save(topQuestionRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					tQuestionRelRepo.deleteByUIdAndTqId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
							Const.OBJ_OPT_FOLLOW);// 0-关注 3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					throw new RuntimeException("参数异常：enabled=" + tQuestionParams.getEnabled());
				}
				break;
			// 话题关注
			case Const.OBJ_TYPE_2:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					TopicRel rels = topicRelRepo.findByOne(tQuestionParams.getUserId(),
							tQuestionParams.getAttentObjId(), Const.OBJ_OPT_FOLLOW);// 0-关注
					if (null == rels) {
						// 如果传过来的参数是关注，保存新的一条关注记录
						TopicRel topicRel = new TopicRel();
						topicRel.setUserId(tQuestionParams.getUserId());
						topicRel.setTopicId(tQuestionParams.getAttentObjId());
						topicRel.setDataType(Const.OBJ_OPT_FOLLOW);// 关注
						topicRelRepo.save(topicRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					topicRelRepo.deleteByUIdAndTopId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
							Const.OBJ_OPT_FOLLOW);// 0-关注3-屏蔽

				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					throw new RuntimeException("参数异常：enabled=" + tQuestionParams.getEnabled());
				}
				break;
			// 用户关注
			case Const.OBJ_TYPE_3:
				UserFollowRel userFollowRel = new UserFollowRel();
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 先根据toUserId 去数据库查一次记录，如果有一条点赞记录就新增一条关注记录并关注状态改为：2-互相关注
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (toUserFollowRel != null) {
						UserFollowRel rels = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getUserId(),
								tQuestionParams.getAttentObjId());
						if (null == rels) {
							// 更新对方关注表，互相关注
							userFollowRelRepo.updFollowStatus(toUserFollowRel.getUserId(),
									toUserFollowRel.getToUserId(), 2);// 2-互相关注
							// 如果传过来的参数是关注，保存新的一条关注记录
							userFollowRel.setUserId(tQuestionParams.getUserId());
							userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
							userFollowRel.setFollowStatus(2);// 互相关注
							userFollowRelRepo.save(userFollowRel);
						}
					} else {
						// 如果传过来的参数是关注，保存新的一条关注记录
						userFollowRel.setUserId(tQuestionParams.getUserId());
						userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
						userFollowRel.setFollowStatus(1);// 非互相关注
						userFollowRelRepo.save(userFollowRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (null != toUserFollowRel) {
						userFollowRelRepo.updFollowStatus(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
								1);// 0-非互相关注
					}
					// 如果传过来是取消关注，把之前一条记录物理删除
					userFollowRelRepo.deleteByUIdAndToId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId());
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					throw new RuntimeException("参数异常：enabled=" + tQuestionParams.getEnabled());
				}
				break;
			default:
				logger.error("参数异常：objType=" + tQuestionParams.getObjType());
				throw new RuntimeException("参数异常：objType=" + tQuestionParams.getObjType());
		}
	}

}
