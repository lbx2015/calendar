package net.riking.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.EnumCustom;
import net.riking.core.entity.MultipleChoiceCustom;
import net.riking.core.entity.Resp;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.Employee;
import net.riking.entity.model.EmployeeDesc;
import net.riking.service.repo.EmployeeRepo;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController {
	@Autowired
	Config config;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	WorkflowMgr workflowMgr;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@ApiOperation(value = "添加或者更新员工信息", notes = "POST-@Employee")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody Employee employee) {
		if (StringUtils.isEmpty(employee.getCompany())) {
			// 配置文件用法
			employee.setCompany(config.getCompany());
		}
		Employee emp = employeeRepo.save(employee);
		emp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getAmlWorkId(), Arrays.asList(emp));

		return new Resp(emp, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "删除员工", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "Long")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public Resp del_(@RequestParam("id") Long id) {
		employeeRepo.delete(id);
		return new Resp(1);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		Employee employee = employeeRepo.findOne(id);
		employee.setDesc(new EmployeeDesc());
		workflowMgr.fillJobsInfo(Arrays.asList(employee));
		return new Resp(employee, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore(@RequestBody Set<Employee> employees) {
		for (Employee employee : employees) {
			if (StringUtils.isEmpty(employee.getCompany())) {
				employee.setCompany(config.getCompany());
			}
		}

		List<Employee> es = employeeRepo.save(employees);
		// es.get(0).setStartState("");
		workflowMgr.addJobs(config.getAmlWorkId(), employees);

		Iterator<Employee> iter = es.iterator();
		List<Long> ids = new ArrayList<>();
		while (iter.hasNext()) {
			ids.add(iter.next().getId());
		}
		return new Resp(ids.toArray(new Long[] {}));
	}

	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = 0;
		count = employeeRepo.deleteByIds(ids);
		return new Resp(count);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute Employee em) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<Employee> example = Example.of(em, ExampleMatcher.matchingAll());
		Page<Employee> page = employeeRepo.findAll(example, pageable);
		Random r = new Random();
		for (Employee e : page.getContent()) {
			e.setDesc(new EmployeeDesc());
			e.setDemo3("key" + 8);
			e.setDemo4("keyF" + r.nextInt(100) + "," + "keyS" + r.nextInt(10));
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/getDemoEnum", method = RequestMethod.GET)
	public Resp getDemoEnum(@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		EnumCustom enumCustom;
		for (int i = 0; i < 100; i++) {
			enumCustom = new EnumCustom();
			// enumCustom.setKey(r.nextInt(100000)+"");
			// enumCustom.setValue(r.nextInt(100000)+"");
			enumCustom.setKey("key" + i);
			enumCustom.setValue("value" + i);
			// 为了区分多个枚举，此字段为框架自动传入
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getDemoEnumLinkF", method = RequestMethod.GET)
	public Resp getDemoEnumLinkF(@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		EnumCustom enumCustom;
		for (int i = 0; i < 100; i++) {
			enumCustom = new EnumCustom();
			// enumCustom.setKey(r.nextInt(100000)+"");
			// enumCustom.setValue(r.nextInt(100000)+"");
			enumCustom.setKey("keyF" + i);
			enumCustom.setValue("valueF" + i);
			// 为了区分多个枚举，此字段为框架自动传入,联动下拉框会追加‘_i’区分，如‘name_1’
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getDemoEnumLinkS", method = RequestMethod.GET)
	public Resp getDemoEnumLinkS(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		EnumCustom enumCustom;
		int size = 10;
		Random r = new Random();
		if (StringUtils.isNotBlank(key)) {
			size = r.nextInt(50);
		}
		for (int i = 0; i < size; i++) {
			enumCustom = new EnumCustom();
			// enumCustom.setKey(r.nextInt(100000)+"");
			// enumCustom.setValue(r.nextInt(100000)+"");
			enumCustom.setKey("keyS" + i);
			enumCustom.setValue("valueS" + i);
			// 为了区分多个枚举，此字段为框架自动传入
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getDemoMultipleChoice", method = RequestMethod.GET)
	public Resp getDemoMultipleChoice(@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "prop", required = false) String prop) {
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (int i = 0; i < size; i++) {
			choice = new MultipleChoiceCustom();
			choice.setKey("keyS" + i);
			choice.setValue("valueS" + i);
			// 为了区分多个复选，此字段为框架自动传入
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms);
	}
}
