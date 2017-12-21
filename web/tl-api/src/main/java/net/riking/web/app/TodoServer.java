package net.riking.web.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.TodoRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Todo;
import net.riking.entity.params.TodoParams;
import net.riking.util.MergeUtil;

/**
 * 待办的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/todo")
public class TodoServer {

	@Autowired
	TodoRepo todoRepo;

	@ApiOperation(value = "用户待办新增/修改", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public AppResp save(@RequestBody Todo todo) {
		Todo todo2 = todoRepo.findOne(todo.getTodoId());
		if (null == todo2) {
			todo = todoRepo.save(todo);
		} else {
			try {
				todo2 = MergeUtil.merge(todo2, todo);
			} catch (Exception e) {
				return new AppResp(CodeDef.ERROR);
			}
			todo = todoRepo.save(todo2);
		}
		return new AppResp(todo, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除代办信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public AppResp delMore(@RequestBody TodoParams todoParams) {
		todoRepo.delete(todoParams.getTodoId());

		return new AppResp().setCode(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "app获取代办未完成信息", notes = "POST")
	@RequestMapping(value = "/getTodo", method = RequestMethod.POST)
	public AppResp getTodo(@RequestBody Todo todo) {
		if (todo.getPindex() == null) {
			todo.setPindex(0);
		}
		List<Todo> todos = todoRepo.findTodo(todo.getUserId(), 0, new PageRequest(todo.getPindex(), 10));
		return new AppResp(todos, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "app获取历史代办信息", notes = "POST")
	@RequestMapping(value = "/getTodoHis", method = RequestMethod.POST)
	public AppResp getTodoHis(@RequestBody Todo todo) {
		if (todo.getPindex() == null) {
			todo.setPindex(0);
		}
		List<Todo> todos = todoRepo.findTodo(todo.getUserId(), 1, new PageRequest(todo.getPindex(), 10));
		return new AppResp(todos, CodeDef.SUCCESS);
	}
}
