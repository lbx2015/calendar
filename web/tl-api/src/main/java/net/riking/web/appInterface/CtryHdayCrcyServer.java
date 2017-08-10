package net.riking.web.appInterface;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.CtryHdayCrcy;
import net.riking.service.SysDataService;
import net.riking.service.repo.CtryHdayCrcyRepo;
import net.riking.util.ZipFileUtil;
/**
 * app各国节假日币种接口
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:33:16
 * @used TODO
 */
@SessionAttributes("currentUser")
@RestController
@RequestMapping(value = "/ctryHdayCrcyApp")
public class CtryHdayCrcyServer {
	@Autowired
	CtryHdayCrcyRepo crtyHdayCrcyRepo;
	
	
	@Autowired
	SysDataService sysDataservice;
	
	
	@ApiOperation(value = "得到<单个>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@ModelAttribute("currentUser") AppUser cAppUser,@RequestParam("id") String id) {
		CtryHdayCrcy ctryHdayCrcy = crtyHdayCrcyRepo.findOne(id);
//		CtryHdayCrcy ctryHdayCrcy = sysDataservice.getCtryHdayCrcy(id);
		setIconUrl(Arrays.asList(ctryHdayCrcy),cAppUser);
		return new AppResp(ctryHdayCrcy, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/getMore", method = RequestMethod.POST)
	public AppResp getMore(@ModelAttribute("currentUser") AppUser cAppUser, @RequestBody CtryHdayCrcy crtyHdayCrcy){
		PageRequest pageable = new PageRequest(crtyHdayCrcy.getPindex(), crtyHdayCrcy.getPcount(), crtyHdayCrcy.getSortObj());
		if(StringUtils.isEmpty(crtyHdayCrcy.getDeleteState())){
			crtyHdayCrcy.setDeleteState("1");
		}
		Example<CtryHdayCrcy> example = Example.of(crtyHdayCrcy, ExampleMatcher.matchingAll());
		Page<CtryHdayCrcy> page = crtyHdayCrcyRepo.findAll(example,pageable);
		
//		List<CtryHdayCrcy> list = sysDataservice.getMoreCtryHdayCrcy(crtyHdayCrcy);
//		Page<CtryHdayCrcy> page = new PageImpl<CtryHdayCrcy>(list,pageable,list.size());
		
		setIconUrl(page.getContent(),cAppUser);
		//将压缩包解压
		this.getIcon();
		return new AppResp(page, CodeDef.SUCCESS);
	}
	
	
	@ApiOperation(value = "手机端得到查询条件", notes = "POST")
	@RequestMapping(value = "/getParam", method = RequestMethod.POST)
	public AppResp getParam_() {
		Map<String, List<ModelPropDict>> map = new HashMap<String,List<ModelPropDict>>();
		//币种
		map.put("crcy",sysDataservice.getDicts("T_CTRY_HDAY_CRCY", "CRCY"));
		//国家或地区
		map.put("ctryName",sysDataservice.getDicts("T_CTRY_HDAY_CRCY", "CTRY"));
		//节假日
		map.put("hdayName",sysDataservice.getDicts("T_CTRY_HDAY_CRCY", "HDAY"));
		return new AppResp(map, CodeDef.SUCCESS);
	}
	
	
	private void getIcon(){
		String path = this.getClass().getResource("/").getPath()+ Const.TL_STATIC_ICON_PATH;
		File dir = new File(path);
		parseIconZip(dir);
	}
	
	private void parseIconZip(File dir){
		if(!dir.exists()){
			return;
		}
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()){
				parseIconZip(files[i]);
				continue;
			}
			String suffix = files[i].getName().substring(files[i].getName().lastIndexOf(".")).toUpperCase();
			if(suffix.equals(".ZIP")){
				try {
					ZipFileUtil.parseZip(files[i],files[i].getParent());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void setIconUrl(List<CtryHdayCrcy> list,AppUser user){
		for (CtryHdayCrcy chc : list) {
			chc.setIconUrl(Const.TL_STATIC_ICON_PATH + (user==null? "" : user.getPhoneType() + "/") + chc.getCrcy()+".pong");
		}
	}
}
