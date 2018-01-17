package net.riking.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.ShieldKeyWordRepo;
import net.riking.entity.model.ShieldKeyWord;
import net.riking.service.ShieldKeyWordService;
import net.riking.util.ExcelToList;
import net.riking.util.ExportExcelUtils;

@RestController
@RequestMapping(value = "/shieldKeyWord")
public class ShieldKeyWordController {

	@Autowired
	ShieldKeyWordService shieldKeyWordService;

	@Autowired
	ShieldKeyWordRepo shieldKeyWordRepo;

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		ShieldKeyWord keyWord = shieldKeyWordService.getRepo().findOne(id);
		return new Resp(keyWord, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ShieldKeyWord keyWord) {
		query.setSort("id_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<ShieldKeyWord> example = Example.of(keyWord, ExampleMatcher.matchingAll());
		Page<ShieldKeyWord> page = shieldKeyWordService.getRepo().findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody ShieldKeyWord keyWord) {
		shieldKeyWordService.addKeyWord(keyWord);
		return new Resp(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "删除信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody List<Long> ids) {
		shieldKeyWordService.delKeyWord(ids);
		return new Resp(CodeDef.SUCCESS);
	}

	@AuthPass
	@ApiOperation(value = "导入敏感词信息", notes = "POST")
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore_(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("fileName");
		String fileName = mFile.getOriginalFilename();
		String sysDays = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<ShieldKeyWord> list = null;
		try {
			InputStream is = mFile.getInputStream();
			String[] fields = { "keyWord" };
			if (sysDays.equals("xlsx")) {
				list = ExcelToList.readXlsx(is, fields, ShieldKeyWord.class);
			} else {
				list = ExcelToList.readXls(is, fields, ShieldKeyWord.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(CodeDef.ERROR);
		}

		if (list != null && list.size() > 0) {
			for (ShieldKeyWord dict : list) {
				dict.setEnabled(new Integer(1));
			}
			shieldKeyWordService.addMoreKeyWord(list);
			if (list.size() > 0) {
				return new Resp(true, CodeDef.SUCCESS);
			} else {
				return new Resp(CodeDef.ERROR);
			}
		} else {
			return new Resp(CodeDef.ERROR);
		}
	}

	/**
	 * 导出excel
	 * 
	 * @param value response
	 * @return
	 */
	@AuthPass
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public Resp exportExcel(HttpServletResponse response) {
		Resp downloadExcel = null;
		OutputStream outputStream = null;
		String name = "shieldKeyWord";
		try {
			response.reset();
			// response.setContentType("application/octet-stream");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-Disposition",
					"attachment; filename=" + new String(name.getBytes("utf-8"), "iso8859-1") + ".xls");
			outputStream = response.getOutputStream();
			List<ShieldKeyWord> shieldKeyWords = shieldKeyWordRepo.findAll();
			// ExcelUtils.exportByList(shieldKeyWords, outputStream, new String[] { "keyWord" });
			LinkedHashMap<String, String> fields = new LinkedHashMap<>();
			fields.put("keyWord", "关键词");
			ExportExcelUtils.exportByList(shieldKeyWords, outputStream, fields);
			downloadExcel = new Resp(CodeDef.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			downloadExcel = new Resp(CodeDef.ERROR);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return downloadExcel;
	}

}
