package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.ReportSubcribeRel;
import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.News;
import net.riking.entity.model.ReportAgence;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.Report;
import net.riking.service.ReportAgenceFrencyService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.impl.GetDateServiceImpl;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.DaysRepo;
import net.riking.service.repo.ReportListRepo;
import net.riking.util.Utils;

/**
 * app端报表配置的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportListApp")
public class AppReportListServer {
	@Autowired
	ReportListRepo reportListRepo;
	@Autowired
	AppUserReportRelRepo appUserReportRepo;
	@Autowired
	SysDataService sysDataservice;
	@Autowired
	GetDateServiceImpl getDateService;
	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;
	@Autowired 
	DaysRepo daysRepo;
	@Autowired
	ReportAgenceFrencyService reportAgenceFrencyService;
	
	/**
	 * 
	 * @author tao.yuan
	 * @version crateTime：2017年11月6日 下午3:41:08
	 * @used TODO
	 * @return
	 */
	@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody AppUser appUser) {
		//List<QueryReport> list = reportSubmitCaliberService.findAllReport();
		List<ReportAgence> reportAgenceList = new ArrayList<>();//保存集合数据 传给移动端
		Set<String> agenceList = reportAgenceFrencyService.findALLAgence();//查询所有的汇报机构
		List<BaseModelPropdict> list = null;
		//根据汇报机构 查询字典表 查询出汇报机构下面的中文名称
		if(agenceList != null && agenceList.size() > 0){
			ReportAgence reportAgence = null;
			for (String value : agenceList) {
				reportAgence = new ReportAgence();
 				reportAgence.setAgenceName(value);//汇报机构名称
				list = reportAgenceFrencyService.findAgenceNameList(value);//中文名称
				if(list != null && list.size() > 0){//如果存在中文名称  则拿到中文名称的主键id 和ke 关联用户的id查询
					List<ReportFrequency> frencyList =  null;
					for (BaseModelPropdict baseModelPropdict : list) {
						frencyList = reportAgenceFrencyService.findReportByModuleType(baseModelPropdict.getKe());//根据ke去查询报表
						//if(StringUtils.isNotBlank(appUser.getId())){//用户id不为空
							if(frencyList != null && frencyList.size() > 0){
								ReportSubcribeRel appUserReportRel = null;
								for(ReportFrequency frencey:frencyList){
									//根据用户id和 报表id 查询 此用户是否订阅
									appUserReportRel = appUserReportRepo.findByUserIdAndReportId(appUser.getId(), frencey.getReportId());
									if(null != appUserReportRel){//不为空 则是已经订阅的
										frencey.setIsSubscribe("1");
									}else{//为空
										frencey.setIsSubscribe("0");
									}
								}
							}
						//}
						baseModelPropdict.setList(frencyList);//将查询出的报表集合放入
					}
				}
				reportAgence.setList(list);//将汇报机构下面的中文名称放进去
				reportAgenceList.add(reportAgence);
			}
		}
		return new AppResp(reportAgenceList, CodeDef.SUCCESS);
	}
	
	/**
	 * 
	 * @author tao.yuan
	 * @version crateTime：2017年11月14日 上午11:41:56
	 * @used 根据报表名称查询报表列表
	 * @param reportList
	 * @return
	 */
	@ApiOperation(value = "根据报表名称查询报表列表", notes = "POST")
	@RequestMapping(value = "/getReportByName", method = RequestMethod.POST)
	public AppResp getReportByName(@RequestBody Report reportList){
		//List<ReportList> list = reportListRepo.findReportByreportName(reportList.getReportName());
		List<ReportFrequency> list = reportAgenceFrencyService.findReportListByName(reportList.getReportName());
		if(StringUtils.isNotBlank(reportList.getUserId())){//用户id 不为空 判断报表是否订阅
			ReportSubcribeRel appUserReportRel = null;
			if(list != null && list.size() > 0){
				for (ReportFrequency frency : list) {
					//根据用户id和 报表id 查询 此用户是否订阅
					appUserReportRel = appUserReportRepo.findByUserIdAndReportId(reportList.getUserId(), frency.getReportId());
					if(null != appUserReportRel){//不为空 则是已经订阅的
						frency.setIsSubscribe("1");
					}else{//为空
						frency.setIsSubscribe("0");
					}
				}
			}
		}
	
		return new AppResp(list,CodeDef.SUCCESS);
	}
}
