package net.riking.web.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.ReportSubcribeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportAgence;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportSubcribeRel;
import net.riking.entity.params.ReportParams;
import net.riking.service.ReportAgenceFrencyService;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.repo.ReportRepo;
import net.riking.util.RedisUtil;
import net.riking.util.Utils;

/**
 * 报表信息接口
 * @author jc.tan 2017年11月29日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/report")
public class AppReportServer {
	@Autowired
	ReportService reportService;
//	ReportRepo reportRepo;

	@Autowired
	ReportSubcribeRelRepo reportSubcribeRelRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	ReportAgenceFrencyService reportAgenceFrencyService;

	/**
	 * 
	 * @param [userId]
	 * @version crateTime：2017年11月6日 下午3:41:08
	 * @used TODO
	 * @return
	 */
	@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody Map<String, Object> params) {
		// List<QueryReport> list = reportSubmitCaliberService.findAllReport();
		ReportParams reportParams = Utils.map2Obj(params, ReportParams.class);
		List<ReportAgence> reportAgenceList = new ArrayList<ReportAgence>();
		//获取订阅关联表
		List<ReportSubcribeRel> reportSubcribeRelList = reportSubcribeRelRepo.findUserReportList(reportParams.getUserId());
		
		List<Report> reportList = null;
		if(RedisUtil.getInstall().getList(Const.ALL_REPORT) != null){
			reportList = RedisUtil.getInstall().getList(Const.ALL_REPORT);
		}else{
			reportList = reportService.getAllReport();
		}
		
//		List<Report> reportResult = new ArrayList<Report>();
//		for(Report r : reportList){
		for(int i = 0; i < reportList.size(); i++){
			Report r = reportList.get(i);
			for(ReportSubcribeRel rel : reportSubcribeRelList){
				if(r.getReportId().equals(rel.getReportId())){
					r.setIsSubcribe("1");//已订阅
					reportList.remove(i);
					reportList.add(i, r);
				}
			}
		}
		
		/*List<ReportAgence> reportAgenceList = new ArrayList<ReportAgence>();// 保存集合数据 传给移动端
		Set<String> agenceList = reportAgenceFrencyService.findALLAgence();// 查询所有的汇报机构
		List<BaseModelPropdict> list = null;
		// 根据汇报机构 查询字典表 查询出汇报机构下面的中文名称
		if (agenceList != null && agenceList.size() > 0) {
			ReportAgence reportAgence = null;
			for (String value : agenceList) {
				reportAgence = new ReportAgence();
				reportAgence.setAgenceName(value);// 汇报机构名称
				list = reportAgenceFrencyService.findAgenceNameList(value);// 中文名称
				if (list != null && list.size() > 0) {// 如果存在中文名称 则拿到中文名称的主键id 和ke 关联用户的id查询
					List<ReportFrequency> frencyList = null;
					for (BaseModelPropdict baseModelPropdict : list) {
						frencyList = reportAgenceFrencyService.findReportByModuleType(baseModelPropdict.getKe());// 根据ke去查询报表
						// if(StringUtils.isNotBlank(appUser.getId())){//用户id不为空
						if (frencyList != null && frencyList.size() > 0) {
							ReportSubcribeRel reportSubcribeRel = null;
							for (ReportFrequency frencey : frencyList) {
								// 根据用户id和 报表id 查询 此用户是否订阅
								reportSubcribeRel = reportSubcribeRelRepo
										.findByUserIdAndReportId(reportParams.getUserId(), frencey.getReportId());
								if (null != reportSubcribeRel) {// 不为空 则是已经订阅的
									frencey.setIsSubscribe("1");
								} else {// 为空
									frencey.setIsSubscribe("0");
								}
							}
						}
						// }
						baseModelPropdict.setList(frencyList);// 将查询出的报表集合放入
					}
				}
				reportAgence.setList(list);// 将汇报机构下面的中文名称放进去
				reportAgenceList.add(reportAgence);
			}
		}*/
		return new AppResp(reportList, CodeDef.SUCCESS);
	}

	/**
	 * 
	 * @author tao.yuan[title,userId]
	 * @version crateTime：2017年11月14日 上午11:41:56
	 * @used 根据报表名称查询报表列表
	 * @param reportList
	 * @return
	 */
	@ApiOperation(value = "根据报表名称查询报表列表", notes = "POST")
	@RequestMapping(value = "/getReportByName", method = RequestMethod.POST)
	public AppResp getReportByName(@RequestBody Map<String, Object> params) {
		// List<ReportList> list =
		// reportListRepo.findReportByreportName(reportList.getReportName());
		ReportParams reportParams = Utils.map2Obj(params, ReportParams.class);
		List<ReportFrequency> list = reportAgenceFrencyService.findReportListByName(reportParams.getReportName());
		if (StringUtils.isNotBlank(reportParams.getUserId())) {// 用户id 不为空 判断报表是否订阅
			ReportSubcribeRel reportSubcribeRel = null;
			if (list != null && list.size() > 0) {
				for (ReportFrequency frency : list) {
					// 根据用户id和 报表id 查询 此用户是否订阅
					reportSubcribeRel = reportSubcribeRelRepo.findByUserIdAndReportId(reportParams.getUserId(),
							frency.getReportId());
					if (null != reportSubcribeRel) {// 不为空 则是已经订阅的
						frency.setIsSubscribe("1");
					} else {// 为空
						frency.setIsSubscribe("0");
					}
				}
			}
		}

		return new AppResp(list, CodeDef.SUCCESS);
	}
}
