package net.riking.web.appInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.entity.model.CtryHdayCrcy;
import net.riking.service.repo.CtryHdayCrcyRepo;
import net.riking.util.ZipFileUtil;
/****
 * 
 * 
 * @author you.fei
 *app各国节假日币种接口
 */
@RestController
@RequestMapping(value = "/ctryHdayCrcyApp")
public class CtryHdayCrcyServer {
	@Autowired
	CtryHdayCrcyRepo crtyHdayCrcyRepo;
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	public static String  TL_STATIC_ICON_PATH = "static/icon/";
	
	@ApiOperation(value = "得到<单个>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public Resp get_(@RequestParam("id") String id) {
		CtryHdayCrcy crtyHdayCrcy = crtyHdayCrcyRepo.findOne(id);
		return new Resp(crtyHdayCrcy, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/getMore", method = RequestMethod.POST)
	public Resp getMore(@RequestBody CtryHdayCrcy crtyHdayCrcy){
		PageRequest pageable = new PageRequest(crtyHdayCrcy.getPindex(), crtyHdayCrcy.getPcount(), crtyHdayCrcy.getSortObj());
		if(StringUtils.isEmpty(crtyHdayCrcy.getDeleteState())){
			crtyHdayCrcy.setDeleteState("1");
		}
		Example<CtryHdayCrcy> example = Example.of(crtyHdayCrcy, ExampleMatcher.matchingAll());
		Page<CtryHdayCrcy> page = crtyHdayCrcyRepo.findAll(example,pageable);
		//将压缩包解压
		this.getIcon();
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	
	@ApiOperation(value = "手机端得到查询条件", notes = "POST")
	@RequestMapping(value = "/getParam", method = RequestMethod.POST)
	public Resp getParam_() {
		Map<String, List<ModelPropDict>> map = new HashMap<String,List<ModelPropDict>>();
		//币种
		HashSet<String> crcrSet = new HashSet<String>();
		crcrSet.add("CRCY");
		map.put("crcy",modelPropdictRepo.getDatas("T_CTRY_HDAY_CRCY", crcrSet));
		//国家或地区
		HashSet<String> ctrySet = new HashSet<String>();
		crcrSet.add("CTRY");
		map.put("ctry",modelPropdictRepo.getDatas("T_CTRY_HDAY_CRCY", ctrySet));
		
		//节假日
		HashSet<String> hdaySet = new HashSet<String>();
		crcrSet.add("HDAY");
		map.put("hday",modelPropdictRepo.getDatas("T_CTRY_HDAY_CRCY", hdaySet));
		return new Resp(map, CodeDef.SUCCESS);
	}
	
	
	private void getIcon(){
		String path = this.getClass().getResource("/").getPath()+ TL_STATIC_ICON_PATH;
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
}
