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
 * @author tao.yuan
 * @version crateTime：2017年11月18日 上午11:40:40
 * @used 首页接口
 */
@RestController
@RequestMapping(value = "/homePageServer")
public class HomePageServer {

	@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody Map<String, String> params) {
		String userId = params.get("userId");
		String direct = params.get("direct");
		String reqTimeStamp = params.get("reqTimeStamp");
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(userId)) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 查询用户自己关注类(话题、问题),自己订阅类,好友的动态,热门话题、问题、回答

		}
		return new AppResp(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "屏蔽问题", notes = "POST")
	@RequestMapping(value = "/shieldProblem", method = RequestMethod.POST)
	public AppResp shieldProblem(@RequestBody Map<String, String> params) {
		// 判断用户id是否为空
		if (StringUtils.isNotBlank(params.get("userId"))) {// 为空 则是未登录 则首页数据显示的是热点数据

		} else {// 屏蔽问题
			String userId = params.get("userId");
			String objType = params.get("objType");
			String objId = params.get("objId");
			String enabled = params.get("enabled");

			switch (objType) {
				// 问题
				case "1":

					break;
				// 话题
				case "2":

					break;
				default:
					throw new RuntimeException("对象类型异常，objType=" + objType);
			}
		}
		return new AppResp(CodeDef.SUCCESS);
	}
}
