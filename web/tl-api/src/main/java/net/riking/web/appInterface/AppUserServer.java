package net.riking.web.appInterface;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppUserRepo;
/**
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:31:03
 * @used TODO
 * app用户信息操作
 */
@RestController
@RequestMapping(value = "/appUserApp")
public class AppUserServer {
	@Autowired
	AppUserRepo appUserRepo;
	
	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public Resp get_(@RequestParam("id") String id) {
		AppUser appUser = appUserRepo.findOne(id);
		return new Resp(appUser, CodeDef.SUCCESS);
	}
	
	
	@ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody AppUser appUser) {
		if(StringUtils.isEmpty(appUser.getId())||StringUtils.isEmpty(appUser.getDeleteState())){
			appUser.setDeleteState("1");
		}
		AppUser save = appUserRepo.save(appUser);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "更新用户手机设备信息", notes = "POST")
	@RequestMapping(value = "/IsChangeMac", method = RequestMethod.POST)
	public Resp IsChangeMac_(@RequestBody AppUser appUser) {
		AppUser appUser2 = appUserRepo.findOne(appUser.getId());
		String mac = appUser.getMac();
		if(appUser2!=null && !appUser2.getMac().equals(mac)){
			appUser2.setMac(mac);
			appUserRepo.save(appUser2);
			return new Resp(true,CodeDef.SUCCESS);
		}
		return new Resp(false,CodeDef.SUCCESS);
	}
	
}
