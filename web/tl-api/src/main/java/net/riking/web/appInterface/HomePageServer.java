package net.riking.web.appInterface;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;

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
	public AppResp findHomePageData(@RequestBody AppUser appUser){
		//判断用户id是否为空
		if(StringUtils.isNotBlank(appUser.getId())){//为空 则是未登录 则首页数据显示的是热点数据
			
		}else{//查询用户屏蔽的话题id，查询用户是否关注了话题、问题、人脉
			 
		}
		return new AppResp( CodeDef.SUCCESS);
	}

	
}
