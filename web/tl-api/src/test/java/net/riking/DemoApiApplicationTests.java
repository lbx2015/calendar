package net.riking;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.JobEvent;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.Job;
import net.riking.entity.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApiApplicationTests {
	@Autowired
	private TestRestTemplate template;
	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void employeeTest() throws JSONException, JsonParseException, JsonMappingException, IOException {
		// String name1 = UUID.randomUUID().toString();
		// String name2 = name1;
		// String name3 = name1;
		// String name4 = UUID.randomUUID().toString();
		// // add one
		// Employee employee = new Employee();
		// employee.setAge(25);
		// employee.setName(name1);
		// HttpHeaders hds = new HttpHeaders();
		// // hds.add("Content-Type", "application/x-json");
		// HttpEntity<Employee> entity = new HttpEntity<Employee>(employee,
		// hds);
		// Resp emResp = template.postForObject("/employees/addOrUpdate",
		// entity, Resp.class);
		// Assert.assertNotNull(emResp);
		// Employee emp = emResp.getData(Employee.class);
		// Assert.assertEquals(CodeDef.SUCCESS, emResp.getCode().shortValue());
		// Assert.assertEquals(name1, emp.getName());
		// Long id1 = emp.getId();
		// System.out.println(id1);
		// // 修改年龄
		// emResp.setAge(1);
		// emResp = template.postForObject("/employees/addOrUpdate", emResp,
		// Employee.class);
		// Assert.assertNotNull(emResp);
		// Assert.assertEquals(1, emResp.getAge().intValue());
		//
		// employee = new Employee();
		// employee.setId(id1);
		// employee.setName(name4);
		// Integer modifyResp = template.postForObject("/employees/modifyName",
		// new HttpEntity<Employee>(employee),
		// Integer.class);
		// Assert.assertNotNull(modifyResp);
		// Assert.assertEquals(1, modifyResp.intValue());
		//
		// Set<Employee> employees = new HashSet<>();
		// employee = new Employee();
		// employee.setAge(25);
		// employee.setName(name2);
		// employees.add(employee);
		// employee = new Employee();
		// employee.setAge(25);
		// employee.setName(name3);
		// employees.add(employee);
		//
		// Long[] ids = template.postForObject("/employees/addMore", new
		// HttpEntity<Set<Employee>>(employees, null),
		// Long[].class);
		// Assert.assertNotNull(ids);
		// Assert.assertEquals(2, ids.length);
		// Long id2 = ids[0];
		// Long id3 = ids[1];
		// Assert.assertNotNull(id2);
		// Assert.assertNotNull(id3);
		//
		// Employee em = template.getForObject("/employees/get?id={id}",
		// Employee.class, id1);
		// Assert.assertNotNull(em);
		// Assert.assertEquals(1, em.getAge().intValue());
		// Assert.assertEquals(name4, em.getName());
		//
		// // get more
		// String resp =
		// template.getForObject("/employees/getMore?name={name}&pindex={i}&pcount={c}&sort=id_asc",
		// String.class, name1, 0, 3);
		// Assert.assertNotNull(resp);
		// System.out.println(resp);
		// JSONObject page = new JSONObject(resp);
		// Assert.assertEquals(2, page.getInt("totalElements"));
		// Assert.assertEquals(1, page.getInt("totalPages"));
		// Assert.assertEquals(id2.longValue(),
		// page.getJSONArray("content").getJSONObject(0).getLong("id"));
		//
		// Integer del =
		// template.getForObject("/employees/delByName?name={name}",
		// Integer.class, name1);
		// Assert.assertNotNull(del);
		// Assert.assertEquals(2, del.intValue());
		//
		// resp =
		// template.getForObject("/employees/getMore?name={name}&pindex={i}&pcount={c}&sort=id_asc",
		// String.class,
		// name1, 0, 3);
		// page = new JSONObject(resp);
		// Assert.assertNotNull(resp);
		// Assert.assertEquals(0, page.getInt("totalElements"));
		//
		// del = template.getForObject("/employees/del?id={id}", Integer.class,
		// id1);
		// Assert.assertNotNull(del);
		// Assert.assertEquals(1, del.intValue());
		//
		// em = template.getForObject("/employees/get?id={id}", Employee.class,
		// id1);
		// Assert.assertNull(em);
	}


	public void workflowTest() throws JSONException, JsonParseException, JsonMappingException, IOException {
		Set<Employee> employees = new HashSet<>();
		Employee employee = new Employee();
		employee.setAge(25);

		employees.add(employee);
		employee = new Employee();
		employee.setAge(25);

		employees.add(employee);
		// 生成工作流
		Resp resp = template.postForObject("/employees/addMore", new HttpEntity<Set<Employee>>(employees, null),
				Resp.class);
		Assert.assertNotNull(resp);
		Long[] ids = resp.getData(Long[].class);
		Assert.assertNotNull(ids);
		// 查询信息，此时employee1已经有状态信息
		resp = template.getForObject("/employees/get?id={id}", Resp.class, ids[0]);
		Assert.assertNotNull(resp);
		Employee employee1 = resp.getData(Employee.class);
		Assert.assertNotNull(employee1.getJob());
		Assert.assertNotNull(employee1.getEvents());

		resp = template.getForObject("/employees/get?id={id}", Resp.class, ids[1]);
		Assert.assertNotNull(resp);
		Employee employee2 = resp.getData(Employee.class);
		Assert.assertNotNull(employee1.getJob());
		Assert.assertNotNull(employee1.getEvents());

		// // 拿出当前节点事件中的第一个（页面中是显示全部，由用户点击）
		String et = employee2.getEvents().get(0).getEvent();
		JobEvent event = new JobEvent(employee2.getJob().getJobId(), et, "李四");
		event.setNextStateOwner("张三");
		event.setComments("不通过原因：XXX");
		event.setTargetJson(mapper.writeValueAsString(employee2));
		Set<JobEvent> ets = new HashSet<>();
		ets.add(event);
		// 触发事件
		resp = template.postForObject("/workflow/events/send", new HttpEntity<Set<JobEvent>>(ets, null), Resp.class);
		Assert.assertNotNull(resp);
		int affact = resp.getData(int.class);
		Assert.assertEquals(1, affact);
		// 查出流程
		resp = template.getForObject("/workflow/jobs/get?jobId={jobId}", Resp.class, employee2.getJob().getJobId());
		Assert.assertNotNull(resp);
		Job job2 = resp.getData(Job.class);
		Assert.assertNotNull(job2.getJobStates());
		Assert.assertNotNull(job2.getWorkflowStates());
		Assert.assertTrue(job2.getWorkflowStates().size() > 0);
		// 验证状态
		resp = template.getForObject("/employees/get?id={id}", Resp.class, employee2.getId());
		Assert.assertNotNull(resp);
		employee2 = resp.getData(Employee.class);
		Assert.assertNotNull(employee2.getJob());
		Assert.assertEquals(employee2.getJob().getCurJobState(), job2.getCurJobState());
	}

}
