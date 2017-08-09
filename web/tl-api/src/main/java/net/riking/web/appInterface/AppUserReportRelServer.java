package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.ReportList;
import net.riking.service.repo.AppUserReportRepo;
import net.riking.service.repo.ReportListRepo;


/**用户获取所属的报表信息
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/appUserReport")
public class AppUserReportRelServer {

	@Autowired
	AppUserReportRepo appUserReportRepo;
	@Autowired
	ReportListRepo reportListRepo;
	

	@ApiOperation(value = "app获取用户下的报表", notes = "POST")
	@RequestMapping(value = "/getUserRepor", method = RequestMethod.POST)
	public Resp getUserReport(@RequestParam("appUserId") String appUserId,@RequestParam("pcount") Integer pcount,@RequestParam("pcount") Integer pindex){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserId);
		PageRequest pageable = new PageRequest(pindex, pcount, null);
		Specification<ReportList> s1 = new Specification<ReportList>() {
			@Override
			public Predicate toPredicate(Root<ReportList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("deleteState").as(String.class)),"1"));
				if (reportIds.size()>0) {
					list.add(root.get("id").as(String.class).in(reportIds));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Page<ReportList> page = reportListRepo.findAll(Specifications.where(s1), pageable);
		return new Resp(page);
	}
	
	@ApiOperation(value = "用户添加所属报表", notes = "POST")
	@RequestMapping(value = "/userAddReport", method = RequestMethod.POST)
	public Resp userAddReport_(@ModelAttribute String appUserId){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserId);
		List<ReportList> reportLists = reportListRepo.findbyReoprtId(reportIds);
		return new Resp(reportLists, CodeDef.SUCCESS);
	}
	
	
}
