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
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 搜索列表（报表/话题/人脉/资讯/问题）结果
 */
@RestController
@RequestMapping(value = "/searchListServer")
public class SearchListServer {

	@ApiOperation(value = "显示热门搜索列表", notes = "POST")
	@RequestMapping(value = "/findHotSearchList", method = RequestMethod.POST)
	public AppResp findHotSearchList(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 查询热门搜索列表

		}
		return new AppResp(CodeDef.SUCCESS);
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
