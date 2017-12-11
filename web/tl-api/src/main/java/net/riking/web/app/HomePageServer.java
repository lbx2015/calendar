package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import net.riking.entity.model.TQuestionResult;
import net.riking.entity.model.TopicRel;
import net.riking.entity.params.HomeParams;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.util.DateUtils;

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

	@Autowired
	TopicQuestionServer topicQuestionServer;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	QAnswerService qAnswerService;

	// TODO 首页要过滤掉屏蔽的信息
	/**
	 *
	 * @param params[userId,direct,reqTimeStamp]
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody HomeParams homeParams) throws ParseException {
		if (homeParams == null) {
			homeParams = new HomeParams();
			homeParams.setReqTimeStamp(new Date());
			homeParams.setDirect(Const.DIRECT_UP);
		}
		if (homeParams.getReqTimeStamp() == null || "".equals(homeParams.getReqTimeStamp())) {
			homeParams.setReqTimeStamp(DateUtils.StringFormatMS(DateUtils.DateFormatMS(new Date(), "yyyyMMddHHmmssSSS"),
					"yyyyMMddHHmmssSSS"));
			homeParams.setDirect(Const.DIRECT_UP);
		}
		if (homeParams.getDirect() == null) {
			homeParams.setDirect(Const.DIRECT_UP);
		}

		// 分页数据
		List<TQuestionResult> tQuestionList = new ArrayList<TQuestionResult>();
		switch (homeParams.getDirect()) {
			// 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
			case Const.DIRECT_UP:
				// 查询查出前30条数据
				tQuestionList = tQuestionService.findTopicHomeUp(homeParams.getUserId(), homeParams.getReqTimeStamp(),
						0, 30);
				break;
			// 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
			case Const.DIRECT_DOWN:
				// 查询查出前30条数据
				List<TQuestionResult> tQuestionListAsc = tQuestionService.findTopicHomeDown(homeParams.getUserId(),
						homeParams.getReqTimeStamp(), 0, 30);
				// 把查出来的数据按倒序重新排列
				for (int i = 0; i < tQuestionListAsc.size(); i++) {
					tQuestionList.add(tQuestionListAsc.get(tQuestionListAsc.size() - i - 1));
				}
				break;
			default:
				logger.error("请求方向参数异常：direct:" + homeParams.getDirect());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		for (TQuestionResult tQuestionResult : tQuestionList) {
			/* 1-根据用户关注的话题推送问题 */
			if (tQuestionResult.getPushType() == 1) {
				tQuestionResult.setQaContent(qAnswerService.getAContentByOne(tQuestionResult.getTqId()));
			}
		}
		return new AppResp(tQuestionList, CodeDef.SUCCESS);
	}

	/**
	 * 屏蔽问题
	 * @param params[userId,objType,objId,enabled]
	 * @return
	 */
	@ApiOperation(value = "屏蔽问题", notes = "POST")
	@RequestMapping(value = "/shield", method = RequestMethod.POST)
	public AppResp shield_(@RequestBody HomeParams homeParams) {

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
