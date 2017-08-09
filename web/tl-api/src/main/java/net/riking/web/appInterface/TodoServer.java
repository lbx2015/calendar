package net.riking.web.appInterface;

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
import net.riking.entity.model.Todo;
import net.riking.service.repo.TodoRepo;

/**
 * 代办的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/Todo")
public class TodoServer {

	@Autowired
	TodoRepo todoRepo;
	
	@ApiOperation(value = "用户代办新增/修改", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save(@RequestBody Todo todo){
		todo = todoRepo.save(todo);
		return new Resp(todo, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除代办信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestParam("id") String id) {
			todoRepo.delete(id);;
			return new Resp().setCode(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "app获取代办信息", notes = "POST")
	@RequestMapping(value = "/getTodo", method = RequestMethod.POST)
	public Resp getTodo(@RequestBody Todo todo) {
		todo.setIsComplete(1);
		todo.setSort("isImportant_desc|strDate_asc");
		PageRequest pageable = new PageRequest(todo.getPcount(), todo.getPindex(), todo.getSortObj());
		Example<Todo> example = Example.of(todo, ExampleMatcher.matchingAll());
		Page<Todo> page = todoRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "app获取历史代办信息", notes = "POST")
	@RequestMapping(value = "/getTodoHis", method = RequestMethod.POST)
	public Resp getTodoHis(@RequestBody Todo todo) {
		todo.setIsComplete(0);
		todo.setSort("completeDate_desc");
		PageRequest pageable = new PageRequest(todo.getPcount(), todo.getPindex(), todo.getSortObj());
		Example<Todo> example = Example.of(todo, ExampleMatcher.matchingAll());
		Page<Todo> page = todoRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
}