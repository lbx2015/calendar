package net.riking.web.appInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.QureyResults;
import net.riking.service.repo.AppUserReportCompleteRelRepo;

@RestController
@RequestMapping(value = "/appUserReportCompleteRel")
public class AppUserReportCompletRelServer {
     @Autowired
     AppUserReportCompleteRelRepo appUserReportCompleteRelRepo;
     
     @ApiOperation(value = "用户获取当天报表完成情况", notes = "POST")
 	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
 	public AppResp getAllReport(@RequestParam("userId") String userId,@RequestParam("date") String date) {
    	 List<QureyResults> completeRels = appUserReportCompleteRelRepo.getReportId(userId, date);
		return new AppResp(completeRels, CodeDef.SUCCESS);
    	 
     }
     
     @ApiOperation(value = "用户报表完成情况新增", notes = "POST")
  	@RequestMapping(value = "/save", method = RequestMethod.POST)
  	public AppResp save(@RequestBody AppUserReportCompleteRel appUserReportCompleteRel) {
     	 appUserReportCompleteRelRepo.save(appUserReportCompleteRel);
 		return new AppResp( CodeDef.SUCCESS);
      }
}
