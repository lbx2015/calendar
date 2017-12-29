package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.TopicDao;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.TQuestionRel;
import net.riking.entity.model.Topic;
import net.riking.entity.model.TopicRel;
import net.riking.entity.model.TopicResult;
import net.riking.entity.params.HomeParams;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.util.FileUtils;
import net.riking.util.Utils;

@Service("topicService")
@Transactional
public class TopicServiceImpl implements TopicService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	TopicDao topicDao;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	TopicRepo topicRepo;

	@Override
	public List<TopicResult> findTopicOfInterest(String userId, String topicIds, int begin, int end) {
		return topicDao.findTopicOfInterest(userId, topicIds, begin, end);
	}

	@Override
	public List<TopicResult> userFollowTopic(String userId, int begin, int pageCount) {
		return topicDao.userFollowTopic(userId, begin, pageCount);
	}

	@Override
	public void shield(MQOptCommon common) throws IllegalArgumentException, IllegalAccessException {
		HomeParams homeParams = new HomeParams();
		homeParams = (HomeParams) Utils.fromObjToObjValue(common, homeParams);
		switch (homeParams.getObjType()) {
			// 问题
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == homeParams.getEnabled()) {
					TQuestionRel rels = tQuestionRelRepo.findByOne(homeParams.getUserId(), homeParams.getObjId(),
							Const.OBJ_OPT_SHIELD);// 3-屏蔽
					if (null == rels) {
						// 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
						TQuestionRel tQuestionRel = new TQuestionRel();
						tQuestionRel.setUserId(homeParams.getUserId());
						tQuestionRel.setTqId(homeParams.getObjId());
						tQuestionRel.setDataType(Const.OBJ_OPT_SHIELD);// 0-关注 3-屏蔽
						tQuestionRelRepo.save(tQuestionRel);
					}
				} else if (Const.INVALID == homeParams.getEnabled()) {
					// 如果传过来是取消屏蔽，把之前一条记录物理删除
					tQuestionRelRepo.deleteByUIdAndTqId(homeParams.getUserId(), homeParams.getObjId(),
							Const.OBJ_OPT_SHIELD);// 0-关注3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + homeParams.getEnabled());
					throw new RuntimeException("参数异常：enabled=" + homeParams.getEnabled());
				}
				break;
			// 话题
			case Const.OBJ_TYPE_2:
				if (Const.EFFECTIVE == homeParams.getEnabled()) {
					// TODO 确认是否有话题屏蔽
					TopicRel rels = topicRelRepo.findByOne(homeParams.getUserId(), homeParams.getObjId(),
							Const.OBJ_OPT_SHIELD);// 3-屏蔽
					if (null == rels) {
						// 如果传过来的参数是屏蔽，保存新的一条屏蔽记录
						TopicRel topicRel = new TopicRel();
						topicRel.setUserId(homeParams.getUserId());
						topicRel.setTopicId(homeParams.getObjId());
						topicRel.setDataType(Const.OBJ_OPT_SHIELD);// 0-关注；3-屏蔽
						topicRelRepo.save(topicRel);
					}
				} else if (Const.INVALID == homeParams.getEnabled()) {
					// 如果传过来是取消屏蔽，把之前一条记录物理删除
					topicRelRepo.deleteByUIdAndTopId(homeParams.getUserId(), homeParams.getObjId(),
							Const.OBJ_OPT_SHIELD);// 0-关注3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + homeParams.getEnabled());
					throw new RuntimeException("参数异常：enabled=" + homeParams.getEnabled());
				}
				break;
			default:
				logger.error("对象类型异常，objType=" + homeParams.getObjType());
				throw new RuntimeException("对象类型异常，objType=" + homeParams.getObjType());
		}
	}

	@Override
	public void moveFile(Topic topic) {
		if (topic.getIsAduit() == 2) {
			topic.setIsAduit(0);
		}
		// 临时文件的图片转移路径
		String[] contentFileNames = topic.getContent().split("alt=");
		if (contentFileNames.length != 0 && contentFileNames != null) {
			copyFile(contentFileNames);
		}
		topic.setContent(topic.getContent().replace("temp", "topic"));
		topicRepo.save(topic);
	}

	private void copyFile(String[] fileNames) {
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TOPIC_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				throw new RuntimeException("文件复制异常" + e);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
	}

}
