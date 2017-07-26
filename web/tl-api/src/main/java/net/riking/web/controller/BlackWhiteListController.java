package net.riking.web.controller;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import net.riking.core.annos.AuthPass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.core.task.TaskMgr;
import net.riking.entity.DataCollectInfo;
import net.riking.entity.PageQuery;
import net.riking.entity.model.BlackWhiteList;
import net.riking.service.repo.BlackWhiteListRepo;
import net.riking.task.job.FlashBackJob;
import net.riking.util.ExcelToList;
import net.riking.util.jpinyin.ChineseHelper;
import net.riking.util.jpinyin.PinyinException;
import net.riking.util.jpinyin.PinyinFormat;
import net.riking.util.jpinyin.PinyinHelper;

@InModLog(modName = "黑名单")
@RestController
@RequestMapping(value = "/blackWhiteList")
public class BlackWhiteListController {
	@Autowired
	Config config;

	@Autowired
	DataDictService dataDictService;

	@Autowired
	BlackWhiteListRepo blackWhiteListRepo;

	@Autowired
	TaskMgr taskMgr;

	@Autowired
	FlashBackJob flashBackJob;

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "添加或者更新黑白名单信息", args = { 0 })
	@ApiOperation(value = "添加或者更新黑白名单信息", notes = "POST-@bwlloyee")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BlackWhiteList blackWhiteList) {
		try {
			if (ChineseHelper.containsChinese(blackWhiteList.getZwmc())) {
				blackWhiteList.setZwmcpy(
						(PinyinHelper.convertToPinyinString(blackWhiteList.getZwmc(), "'", PinyinFormat.WITHOUT_TONE)));
			}
		} catch (PinyinException e) {
			e.printStackTrace();
		}
		BlackWhiteList bwl = blackWhiteListRepo.save(blackWhiteList);
		return new Resp(bwl, CodeDef.SUCCESS);
	}

	@AuthPass
	@InFunLog(funName = "添加或者更新", args = {})
	@RequestMapping(value = "/addMore", method = RequestMethod.POST, produces = "text/plain;charset=utf-8 ")
	public String addMore(HttpServletRequest request) {
		String liskType = request.getParameter("liskType");
		String mdly = request.getParameter("mdly");
		String userName = request.getParameter("userName");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("bw");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		int rs = 0;
		if (!fileName.contains(mdly)) {
			return "-1";
		}
		try {
			InputStream is = mFile.getInputStream();
			List<BlackWhiteList> list = null;
			if (mdly.equals("4")) {// 天网红色通缉名单
				if (suffix.equals("xlsx")) {
					list = ExcelToList.readXlsxBySkyNet(is);
				} else {
					list = ExcelToList.readXlsBySkyNet(is);
				}
			} else if (mdly.equals("3") || mdly.equals("1")) {// 贩毒和恐怖国家
				if (suffix.equals("xlsx")) {
					String[] fields = { "zwmc", "ywmc" };
					list = ExcelToList.readXlsx(is, fields, BlackWhiteList.class);
				} else {
					String[] fields = { "zwmc", "ywmc" };
					list = ExcelToList.readXls(is, fields, BlackWhiteList.class);
				}
			} else if (mdly.equals("2")) {// 风险
				if (suffix.equals("xlsx")) {
					String[] fields = { "ywmc", "zwmc" };
					list = ExcelToList.readXlsx(is, fields, BlackWhiteList.class);
				} else {
					String[] fields = { "ywmc", "zwmc" };
					list = ExcelToList.readXls(is, fields, BlackWhiteList.class);
				}
			} else if (mdly.equals("5")) {// 公安查控
				if (suffix.equals("xlsx")) {
					String[] fields = { "zwmc", "", "", "ywmc", "", "", "", "", "", "", "", "gj", "zjhm", "", "xl",
							"sjmx", "sah" };
					list = ExcelToList.readXlsx(is, fields, BlackWhiteList.class);
				} else {
					String[] fields = { "zwmc", "", "", "ywmc", "", "", "", "", "", "", "", "gj", "zjhm", "", "xl",
							"sjmx", "sah" };
					list = ExcelToList.readXls(is, fields, BlackWhiteList.class);
				}
			}

			boolean flag = true;
			if (flag && list != null && list.size() > 0) {
				if (mdly.equals("4")) {
					List<BlackWhiteList> _list = blackWhiteListRepo.findByMdly("4");
					HashSet<Long> ids = new HashSet<Long>();
					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < _list.size(); j++) {
							if(list.get(i).getZjhm().contains(_list.get(j).getZjhm())){
								ids.add(_list.get(j).getId());
								break;
							}
						}
					}
					if(ids.size()>0){
						blackWhiteListRepo.deleteByIds(ids);
					}
				}
				for (BlackWhiteList blackWhiteList : list) {
					blackWhiteList.setCjsj(new Date());
					blackWhiteList.setCjr(userName);
					blackWhiteList.setHmdlx((liskType));
					blackWhiteList.setMdly(mdly);
					if(mdly.equals("5")){
						blackWhiteList.setIshs((byte)1);
					}
				}
				List<BlackWhiteList> list2 = blackWhiteListRepo.save(list);
				rs= list2.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		return rs+"";
	}

	@InFunLog(funName = "删除黑白名单", argNames = { "ID" })
	@ApiOperation(value = "删除黑白名单", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "名单ID", required = true, dataType = "Long")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public Resp del_(@RequestParam("id") Long id) {
		blackWhiteListRepo.delete(id);
		return new Resp(1);
	}

	@InFunLog(funName = "删除黑白名单", args = { 0 }, argNames = { "ID" })
	@ApiOperation(value = "删除黑白名单", notes = "根据id数组删除对象")
	@ApiImplicitParam(name = "ids", value = "名单ID数组", required = true, dataType = "Set<Long>")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		blackWhiteListRepo.deleteByIds(ids);
		return new Resp(1);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BlackWhiteList blackWhiteList = blackWhiteListRepo.findOne(id);
		return new Resp(blackWhiteList, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BlackWhiteList bwl) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<BlackWhiteList> s1 = null;
		Specification<BlackWhiteList> s2 = null;
		// 客户名不为空
		if (StringUtils.isNotEmpty(bwl.getZwmc())) {
			s1 = new Specification<BlackWhiteList>() {
				@Override
				public Predicate toPredicate(Root<BlackWhiteList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					// 判断是否是中文
					if (bwl.getZwmc()!=null) {
						if (ChineseHelper.containsChinese(bwl.getZwmc())) {
							// 对名称简繁体转换都查
							String JTCustomerName = ChineseHelper.convertToSimplifiedChinese(bwl.getZwmc());
							String FTCustomerName = ChineseHelper.convertToTraditionalChinese(bwl.getZwmc());
							list.add(cb.like((root.get("zwmc").as(String.class)), "%" + JTCustomerName + "%"));
							list.add(cb.like((root.get("zwmc").as(String.class)), "%" + FTCustomerName + "%"));

						} else {
							// 拼音或英语
							list.add(cb.like((root.get("zwmcpy").as(String.class)), "%" + bwl.getZwmc() + "%"));
							list.add(cb.like((root.get("ywmc").as(String.class)), "%" + bwl.getZwmc() + "%"));
						}
						list.add(cb.like((root.get("zwmc").as(String.class)), "%" + bwl.getZwmc() + "%"));
					}
					if (bwl.getZjhm()!=null) {
						list.add(cb.like((root.get("zjhm").as(String.class)), "%" + bwl.getZjhm() + "%"));
					}
					if (bwl.getHmdlx()!=null) {
						list.add(cb.equal((root.get("hmdlx").as(Integer.class)), bwl.getHmdlx()));
					}
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		}
		if (bwl.getHmdlx() != null) {
			s2 = new Specification<BlackWhiteList>() {
				@Override
				public Predicate toPredicate(Root<BlackWhiteList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					if (bwl.getHmdlx() != null) {
						list.add(cb.equal((root.get("hmdlx").as(Integer.class)), bwl.getHmdlx()));
					}
					Predicate[] p = new Predicate[list.size()];
					return cb.and(list.toArray(p));
				}
			};
		}
		if (s1 == null && s2 == null) {
			Page<BlackWhiteList> page = blackWhiteListRepo.findAll(pageable);
			return new Resp(page);
		}
		Page<BlackWhiteList> page = blackWhiteListRepo.findAll(Specifications.where(s1).and(s2), pageable);
		return new Resp(page);
	}

	@InFunLog(funName = "回溯", args = {})
	@RequestMapping(value = "/flashBack", method = RequestMethod.GET)
	public Resp flashBack()
			throws JsonProcessingException, InstantiationException, IllegalAccessException, ParseException {
		// 获取group
		// 生成taskMgr，由此获取批次号和jobinfo
		String batchId = null;
		DataCollectInfo info = new DataCollectInfo();
		info.setToken(TokenHolder.get().getToken());
		batchId = taskMgr.addJob(Const.TASK_FLASH_BACK_GROUP, info, flashBackJob);
		return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getLast", method = RequestMethod.GET)
	public Resp getLast(@RequestParam String batchId) {
		if (StringUtils.isNotEmpty(batchId)) {
			return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
		} else {
			return new Resp(taskMgr.getLastJobInfoByGroup(Const.TASK_FLASH_BACK_GROUP), CodeDef.SUCCESS);
		}
	}

	@RequestMapping(value = "/getEnumData", method = RequestMethod.GET)
	public Resp getEnumData() {
		Map<String, List<ModelPropDict>> map = new LinkedHashMap<String, List<ModelPropDict>>();
		List<ModelPropDict> liskTypes = dataDictService.getDicts("T_BASE_KYC_BLACKWHITELIST", "LISKTYPE");
		map.put("liskType", liskTypes);
		List<ModelPropDict> mdlys = dataDictService.getDicts("T_BASE_KYC_BLACKWHITELIST", "MDLY");
		map.put("mdly", mdlys);
		return new Resp(map, CodeDef.SUCCESS);
	}

}
