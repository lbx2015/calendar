package net.riking.web.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import net.riking.core.annos.AuthPass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.Resp;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.entity.PageQuery;
import net.riking.entity.model.Sdcurrpd;
import net.riking.service.repo.SdcurrpdRepo;
import net.riking.util.ExcelToList;
@InModLog(modName="汇率")
@RestController
@RequestMapping(value = "/sdcurrpdList")
public class SdcurrpdController {
	@Autowired
	Config config;

	@Autowired
	SdcurrpdRepo sdcurrpdRepo;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	
	@InFunLog(funName="增加",args={0})
	@ApiOperation(value = "添加或者汇率信息", notes = "POST-@Sdcurrpd")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody Sdcurrpd sdcurrpd) {
		Sdcurrpd s = sdcurrpdRepo.save(sdcurrpd);
		return new Resp(s, CodeDef.SUCCESS);
	}	
	
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public Resp check(@RequestBody Sdcurrpd sdcurrpd) {
		StringBuilder reason = new StringBuilder();
		if(StringUtils.isEmpty(sdcurrpd.getCurrency())){
			reason.append("currency:币种不能为空,");
		}
		if(StringUtils.isEmpty(sdcurrpd.getMethod())){
			reason.append("method:算法不能为空,");
		}
		if(sdcurrpd.getRate()==null){
			reason.append("rate:汇率不能为空,");
		}
		if(sdcurrpd.getRateDate()==null){
			reason.append("rateDate:汇率日期不能为空,");
		}
		if(StringUtils.isEmpty(reason.toString())){
			Sdcurrpd sdcurrpd2 = sdcurrpdRepo.getSdcurrpd(sdcurrpd.getCurrency(),sdcurrpd.getMethod(),sdcurrpd.getRateDate());
			if(sdcurrpd2!=null){
				reason.append("currency:当天同种类型的币种重复,");
			}
		}
		return new Resp(reason.toString(),CodeDef.SUCCESS);
	}
	

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		Sdcurrpd sd = sdcurrpdRepo.findOne(id);
		return new Resp(sd, CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = 0;
		for (Long id : ids) {
			sdcurrpdRepo.delete(id);
			count += 1;
		}
		return new Resp(count);
	}

	


	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute Sdcurrpd sd) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<Sdcurrpd> s1 = new Specification<Sdcurrpd>() {
			@Override
			public Predicate toPredicate(Root<Sdcurrpd> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != sd.getCurrency()) {
					list.add(cb.equal((root.get("currency").as(String.class)),sd.getCurrency()));
				}
				if (null != sd.getRateDate()) {
					list.add(cb.equal((root.get("rateDate").as(Date.class)),sd.getRateDate()));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		
		Page<Sdcurrpd> page = sdcurrpdRepo.findAll(Specifications.where(s1), pageable);
		return new Resp(page);
	}

	@AuthPass
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("sd");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String fileNames = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.lastIndexOf("."));
		String date;
		if (fileNames.length() == 22) {
			date = fileNames.substring(11, 15) + "-0" + fileNames.substring(16, 17) + "-" + fileNames.substring(18, 20);
		} else {
			date = fileNames.substring(11, 15) + "-" + fileNames.substring(16, 18) + "-" + fileNames.substring(19, 21);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int rs = 0;
		try {
			InputStream is = mFile.getInputStream();
			List<Sdcurrpd> list = null;
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readSdXlsx(is,fileNames);
			} else {
				list = ExcelToList.readSdXls(is,fileNames);
			}	/*
			Calendar ca = Calendar.getInstance();   
		    ca.set(Calendar.DAY_OF_MONTH, ca.getMinimum(Calendar.DAY_OF_MONTH)); 
			Date startDt=ca.getTime();
			ca = Calendar.getInstance();   
		    ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
			Date endDate=ca.getTime();*/
			sdcurrpdRepo.deleteByRateDate(sdf.parse(date), sdf.parse(date));
			sdcurrpdRepo.save(list);
			rs=list.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs== 0) {
			return new Resp(rs, CodeDef.ERROR);
		}
		return new Resp(rs, CodeDef.SUCCESS);
	}

}
