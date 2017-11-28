package net.riking.web.app;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.resp.TQuestionParams;
import net.riking.util.Utils;

/**
 * 
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 问题接口
 */
@RestController
@RequestMapping(value = "/topicQuestion")
public class TopicQuestionServer {

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	/**
	 * 问题的详情[userId,tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题的详情", notes = "POST")
	@RequestMapping(value = "/getTopicQuestion", method = RequestMethod.POST)
	public AppResp getTopicQuestion(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		TopicQuestion topicQuestion = topicQuestionRepo.getById(tQuestionParams.getTqId());
		// 将对象转换成map
		Map<String, Object> topicQuestionMap = Utils.objProps2Map(topicQuestion, true);
		return new AppResp(topicQuestionMap, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "搜索列表（报表/话题/人脉/资讯/问题）", notes = "POST")
	@RequestMapping(value = "/findSearchList", method = RequestMethod.POST)
	public AppResp findSearchList(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 搜索列表（报表/话题/人脉/资讯/问题）

		}
		return new AppResp(CodeDef.SUCCESS);
	}
}
