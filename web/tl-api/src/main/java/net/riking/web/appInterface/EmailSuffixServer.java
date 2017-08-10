package net.riking.web.appInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.entity.AppResp;
import net.riking.service.SysDataService;
/**
 * app邮箱后缀接口
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:31:37
 * @used TODO
 */
@RestController
@RequestMapping(value = "/emailSuffixApp")
public class EmailSuffixServer {
//	@Autowired
//	DataDictService dataDictService;
	
	@Autowired
	SysDataService sysDataservice;
	
	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "POST")
	@RequestMapping(value = "/getAll", method = RequestMethod.POST)
	public AppResp getAll_(){
		List<ModelPropDict> list = sysDataservice.getDicts("T_APP_USER", "EMAILSUFFIX");
		return new AppResp(list, CodeDef.SUCCESS);
	}
	
}
