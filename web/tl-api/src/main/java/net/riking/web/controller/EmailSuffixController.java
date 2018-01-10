package net.riking.web.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.dao.repo.EmailSuffixRepo;
import net.riking.entity.PageQuery;
import net.riking.entity.EO.EmailSuffixEO;
import net.riking.entity.model.EmailSuffix;
import net.riking.util.ExcelToList;

/**
 * web端邮箱操作
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:42
 * @used TODO
 */
@RestController
@RequestMapping(value = "/emailSuffix")
public class EmailSuffixController {
	@Autowired
	ModelPropdictRepo modelPropdictRepo;

	@Autowired
	EmailSuffixRepo emailSuffixRepo;

	@ApiOperation(value = "得到<单个>邮箱后缀", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		EmailSuffix emailSuffix = emailSuffixRepo.findOne(id);
		return new Resp(emailSuffix, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到<批量>邮箱后缀", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute EmailSuffix emailSuffix) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		emailSuffix.setIsDeleted(Const.EFFECTIVE);
		Example<EmailSuffix> example = Example.of(emailSuffix, ExampleMatcher.matchingAll());
		Page<EmailSuffix> page = emailSuffixRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "POST")
	@RequestMapping(value = "/getAll", method = RequestMethod.POST)
	public Resp getAll_() {
		ModelPropDict emailSuffix = new ModelPropDict();
		emailSuffix.setClazz("T_APP_USER");
		emailSuffix.setField("EMAILSUFFIX");
		Example<ModelPropDict> example = Example.of(emailSuffix, ExampleMatcher.matchingAll());
		List<ModelPropDict> list = modelPropdictRepo.findAll(example);
		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新邮箱后缀", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody EmailSuffix emailSuffix) {
		emailSuffix.setOpt(emailSuffix.getOpt().replaceAll("\'", ""));
		if (emailSuffix.getOpt().equals("add")) {
			if (!StringUtils.isBlank(emailSuffix.getEmailSuffix())) {
				// 获取有效的数据
				List<EmailSuffix> emailSuffixs = emailSuffixRepo.findInvalidDataByIsDeleted();
				for (EmailSuffix suffix : emailSuffixs) {
					if (suffix.getEmailSuffix().equals(emailSuffix.getEmailSuffix())) {
						return new Resp("-1", CodeDef.ERROR);
					}
				}
			}
			emailSuffix.setCreatedTime(new Date());
		}
		emailSuffix.setIsDeleted(Const.EFFECTIVE);
		emailSuffixRepo.save(emailSuffix);
		return new Resp(emailSuffix, CodeDef.SUCCESS);
	}

	@AuthPass
	@ApiOperation(value = "批量添加邮箱后缀", notes = "POST")
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("fileName");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<EmailSuffixEO> list = null;
		try {
			InputStream is = mFile.getInputStream();
			String[] fields = { "emailSuffix", "enabled", "remark" };
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, EmailSuffixEO.class);
			} else {
				list = ExcelToList.readXls(is, fields, EmailSuffixEO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}

		if (list != null && list.size() > 0) {
			List<EmailSuffix> emailSuffixs = new ArrayList<>();
			for (EmailSuffixEO dict : list) {
				EmailSuffix emailSuffix = new EmailSuffix();
				emailSuffix.setCreatedTime(new Date());
				emailSuffix.setEnabled(Integer.parseInt(dict.getEnabled()));
				emailSuffix.setIsDeleted(1);
				emailSuffix.setRemark(dict.getRemark());
				emailSuffix.setEmailSuffix(dict.getEmailSuffix());
			}
			List<EmailSuffix> rs = emailSuffixRepo.save(emailSuffixs);
			if (rs.size() > 0) {
				return new Resp(true, CodeDef.SUCCESS);
			} else {
				return new Resp(CodeDef.ERROR);
			}
		} else {
			return new Resp(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "批量删除邮箱后缀", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		List<EmailSuffix> liSuffixs = null;
		if (ids.size() > 0) {
			liSuffixs = emailSuffixRepo.findByIds(ids);
		}
		if (liSuffixs != null && liSuffixs.size() > 0) {
			for (EmailSuffix emailSuffix : liSuffixs) {
				emailSuffix.setIsDeleted(Const.INVALID);
			}
			emailSuffixRepo.save(liSuffixs);
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "批量删除邮箱后缀", notes = "POST")
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public Resp delSuffix(@RequestBody String id) {
		String idString = id.replaceAll("\"", "");
		// 修改状态
		EmailSuffix emailSuffix = emailSuffixRepo.findOne(idString);
		emailSuffix.setIsDeleted(Const.INVALID);
		emailSuffix = emailSuffixRepo.save(emailSuffix);
		return new Resp(emailSuffix, CodeDef.SUCCESS);
	}

}
