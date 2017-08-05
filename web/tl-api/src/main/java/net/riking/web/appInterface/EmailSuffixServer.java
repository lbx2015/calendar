package net.riking.web.appInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
/****
 * 
 * @author you.fei
 *app邮箱后缀接口
 */
@RestController
@RequestMapping(value = "/emailSuffixApp")
public class EmailSuffixServer {
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "POST")
	@RequestMapping(value = "/getAll", method = RequestMethod.POST)
	public Resp getAll_(){
		ModelPropDict emailSuffix = new ModelPropDict();
		emailSuffix.setClazz("T_APP_USER");
		emailSuffix.setField("EMAILSUFFIX");
		Example<ModelPropDict> example = Example.of(emailSuffix, ExampleMatcher.matchingAll());
		List<ModelPropDict> list = modelPropdictRepo.findAll(example);
		return new Resp(list, CodeDef.SUCCESS);
	}
	
}
