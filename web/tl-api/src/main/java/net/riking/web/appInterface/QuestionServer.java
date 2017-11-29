package net.riking.web.appInterface;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;

/**
 * 
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午11:08:41
 * @used 搜索列表（报表/话题/人脉/资讯/问题）结果
 */
@RestController
@RequestMapping(value = "/question")
public class QuestionServer {

	@ApiOperation(value = "问题详情", notes = "POST")
	@RequestMapping(value = "/problemDetails", method = RequestMethod.POST)
	public AppResp problemDetails(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 问题详情

		}
		return new AppResp(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "问题关注", notes = "POST")
	@RequestMapping(value = "/problemConcerns", method = RequestMethod.POST)
	public AppResp problemConcerns(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 问题关注

		}
		return new AppResp(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "邀请回答的邀请", notes = "POST")
	@RequestMapping(value = "/inviteAnswer", method = RequestMethod.POST)
	public AppResp inviteAnswer(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 邀请回答的邀请

		}
		return new AppResp(CodeDef.SUCCESS);
	}
}
