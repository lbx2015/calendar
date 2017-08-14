package net.riking.web.appInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
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
@RestController
@RequestMapping(value = "/ctryHdayCrcyApp")
public class CtryHdayCrcyServer {
	@Autowired
	CtryHdayCrcyRepo crtyHdayCrcyRepo;
	
	@Autowired
	Config config;
	
	@Autowired
	SysDataService sysDataservice;
	
	@Autowired
	private  HttpServletRequest request;


	@ApiOperation(value = "得到<单个>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestParam("id") String id) {
		CtryHdayCrcy ctryHdayCrcy = crtyHdayCrcyRepo.findOne(id);
//		CtryHdayCrcy ctryHdayCrcy = sysDataservice.getCtryHdayCrcy(id);
		if(ctryHdayCrcy!=null){
			this._setDictValue(Arrays.asList(ctryHdayCrcy),request);
		}
		return new AppResp(ctryHdayCrcy, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/getMore", method = RequestMethod.POST)
	public AppResp getMore(@RequestBody CtryHdayCrcy crtyHdayCrcy){
		PageRequest pageable = new PageRequest(crtyHdayCrcy.getPindex(),crtyHdayCrcy.getPcount(), crtyHdayCrcy.getSortObj());
		if(StringUtils.isEmpty(crtyHdayCrcy.getDeleteState())){
			crtyHdayCrcy.setDeleteState("1");
		}
		//Example<CtryHdayCrcy> example = Example.of(crtyHdayCrcy, ExampleMatcher.matchingAll());
		Page<CtryHdayCrcy> page = crtyHdayCrcyRepo.findAll(new Specification<CtryHdayCrcy>(){
			@Override
			public Predicate toPredicate(Root<CtryHdayCrcy> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("deleteState").as(String.class)),crtyHdayCrcy.getDeleteState()));
				if(StringUtils.isNotBlank(crtyHdayCrcy.getCtryName())){
					list.add(cb.equal((root.get("ctryName").as(String.class)),crtyHdayCrcy.getCtryName()));
				}
				if(StringUtils.isNotBlank(crtyHdayCrcy.getCrcy())){
					list.add(cb.equal((root.get("crcy").as(String.class)),crtyHdayCrcy.getCrcy()));
				}
				if(StringUtils.isNotBlank(crtyHdayCrcy.getHdayName())){
					list.add(cb.equal((root.get("hdayName").as(String.class)),crtyHdayCrcy.getHdayName()));
				}
				if(crtyHdayCrcy.getHdayDate()!=null){
					list.add(cb.like(root.get("hdayDate").as(String.class),crtyHdayCrcy.getHdayDate()+"%"));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		},pageable);
		
//		List<CtryHdayCrcy> list = sysDataservice.getMoreCtryHdayCrcy(crtyHdayCrcy);
//		Page<CtryHdayCrcy> page = new PageImpl<CtryHdayCrcy>(list,pageable,list.size());
		if(page.getContent()!=null && page.getContent().size()>0){
			this._setDictValue(page.getContent(),request);
		}
		//将压缩包解压
		this.getIcon();
		return new AppResp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "模糊查询得到<批量>各国节假日信息", notes = "POST")
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	public AppResp vagueQuery(@RequestBody CtryHdayCrcy crtyHdayCrcy){
		PageRequest pageable = new PageRequest(crtyHdayCrcy.getPindex(), crtyHdayCrcy.getPcount(), crtyHdayCrcy.getSortObj());
		if(StringUtils.isEmpty(crtyHdayCrcy.getDeleteState())){
			crtyHdayCrcy.setDeleteState("1");
		}
		List<String> paramList = new ArrayList<String>();
		String queryParam = crtyHdayCrcy.getQueryParam();
		Specification<CtryHdayCrcy> s1 =null;
		if(StringUtils.isNotBlank(queryParam)){
			Set<String> set = new HashSet<String>();
			set.add("CTRY");
			set.add("CRCY");
			set.add("HDAY");
			List<ModelPropDict> list = sysDataservice.getDictsByFields("T_CTRY_HDAY_CRCY",set);
			paramList.add(queryParam);
			for (ModelPropDict dict : list) {
				if(dict.getValu().equals(queryParam)){
					paramList.add(dict.getKe());
				}
			}
			s1 =  new Specification<CtryHdayCrcy>(){
				@Override
				public Predicate toPredicate(Root<CtryHdayCrcy> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					if(paramList.size()>0){
						for (String param : paramList) {
							list.add(cb.like((root.get("ctryName").as(String.class)),"%"+param+"%"));
							list.add(cb.like((root.get("crcy").as(String.class)),"%"+param+"%"));
							list.add(cb.like((root.get("hdayName").as(String.class)),"%"+param+"%"));
						}
					}
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		}
		Specification<CtryHdayCrcy> s2 =  new Specification<CtryHdayCrcy>(){
			@Override
			public Predicate toPredicate(Root<CtryHdayCrcy> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("deleteState").as(String.class)),crtyHdayCrcy.getDeleteState()));
				Predicate[] p = new Predicate[list.size()];
				return cb.or(list.toArray(p));
			}
		};
		Page<CtryHdayCrcy> page = crtyHdayCrcyRepo.findAll(Specifications.where(s1).and(s2),pageable);
		if(page.getContent()!=null && page.getContent().size()>0){
			this._setDictValue(page.getContent(),request);
		}
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
	
	private void _setDictValue(List<CtryHdayCrcy> list,HttpServletRequest request){
		AppUser user = (AppUser)request.getSession().getAttribute("currentUser");
		String url = request.getRequestURL().toString();
		for (CtryHdayCrcy chc : list) {
			chc.setIconUrl(getPortPath(url) + Const.TL_ICON_PATH + (null==user? "" : user.getPhoneType() + "/") + chc.getCtryName()+".png");
			//币种
//			ModelPropDict dict1 = sysDataservice.getDict("T_CTRY_HDAY_CRCY", "CRCY", chc.getCrcy());
//			chc.setCrcyValue(dict1.getValu());
			//国家或地区
			ModelPropDict dict2 = sysDataservice.getDict("T_CTRY_HDAY_CRCY", "CTRY", chc.getCtryName());
			chc.setCtryNameValue(dict2.getValu());
			//节假日
			ModelPropDict dict3 = sysDataservice.getDict("T_CTRY_HDAY_CRCY", "HDAY", chc.getHdayName());
			chc.setHdayNameValue(dict3.getValu());
		}
	}
	
	private String getPortPath(String url){
		Pattern p = Pattern.compile("[a-zA-z]+://[^/]*");
		Matcher matcher = p.matcher(url);  
		if(matcher.find()){
			return matcher.group();  
		}
		return null;
	}
}
